package dk.frankbille.mysqldumper;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.Preferences;

public class Table implements Comparable<Table> {

    private final String name;
    private final long ownSize;
    private final Set<Table> dependentTables = new TreeSet<>();
    private boolean structureIncluded = true;
    private boolean dataIncluded = true;

    public Table(String name, long ownSize) {
        this.name = name;
        this.ownSize = ownSize;
    }

    public String getName() {
        return name;
    }

    public long getOwnSize() {
        return ownSize;
    }

    public long getTreeSize() {
        return getTreeSize(new HashSet<>());
    }

    private long getTreeSize(Set<Table> visitedTables) {
        visitedTables.add(this);

        long treeSize = getOwnSize();
        for (Table dependentTable : dependentTables) {
            if (!visitedTables.contains(dependentTable)) {
                treeSize += dependentTable.getTreeSize(visitedTables);
            }
        }
        return treeSize;
    }

    public Set<Table> getDependentTables() {
        return dependentTables;
    }

    public void addDependentTable(Table dependentTable) {
        dependentTables.add(dependentTable);
    }

    public boolean isStructureIncluded() {
        return structureIncluded;
    }

    public void setStructureIncluded(boolean structureIncluded) {
        setStructureIncluded(structureIncluded, new HashSet<>());
    }

    private void setStructureIncluded(boolean structureIncluded, Set<Table> visitedTables) {
        this.structureIncluded = structureIncluded;

        visitedTables.add(this);

        for (Table dependentTable : dependentTables) {
            if (!visitedTables.contains(dependentTable)) {
                dependentTable.setStructureIncluded(structureIncluded, visitedTables);
            }
        }

        if (!structureIncluded) {
            setDataIncluded(false);
        }
    }

    public boolean isDataIncluded() {
        return dataIncluded;
    }

    public void setDataIncluded(boolean dataIncluded) {
        setDataIncluded(dataIncluded, new HashSet<>());
    }

    private void setDataIncluded(boolean dataIncluded, Set<Table> visitedTables) {
        this.dataIncluded = dataIncluded;

        visitedTables.add(this);

        for (Table dependentTable : dependentTables) {
            if (!visitedTables.contains(dependentTable)) {
                dependentTable.setDataIncluded(dataIncluded, visitedTables);
            }
        }

        if (dataIncluded) {
            setStructureIncluded(true);
        }
    }

    @Override
    public int compareTo(Table otherTable) {
        return getName().compareTo(otherTable.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Table)) {
            return false;
        }

        Table otherTable = (Table) obj;

        return getName().equals(otherTable.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return toString(new HashSet<>());
    }

    private String toString(Set<Table> visitedTables) {
        visitedTables.add(this);

        String toString = getName();
        toString += " (" + getOwnSize() + ", " + getTreeSize() + ") [";
        boolean first = true;
        for (Table dependentTable : dependentTables) {
            if (!visitedTables.contains(dependentTable)) {
                if (!first) {
                    toString += ", ";
                }
                first = false;
                toString += dependentTable.toString(visitedTables);
            }
        }
        toString += "]";
        return toString;
    }
}

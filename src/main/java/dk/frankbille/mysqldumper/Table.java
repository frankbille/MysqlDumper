package dk.frankbille.mysqldumper;

import java.util.Set;
import java.util.TreeSet;

public class Table implements Comparable<Table> {

    private final String name;
    private final long ownSize;
    private final Set<Table> dependentTables = new TreeSet<>();
    private boolean definitionIncluded = true;
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
        long treeSize = ownSize;
        for (Table dependentTable : dependentTables) {
            treeSize += dependentTable.getTreeSize();
        }
        return treeSize;
    }

    public Set<Table> getDependentTables() {
        return dependentTables;
    }

    public void addDependentTable(Table dependentTable) {
        dependentTables.add(dependentTable);
    }

    public boolean isDefinitionIncluded() {
        return definitionIncluded;
    }

    public void setDefinitionIncluded(boolean definitionIncluded) {
        this.definitionIncluded = definitionIncluded;

        for (Table dependentTable : dependentTables) {
            dependentTable.setDefinitionIncluded(definitionIncluded);
        }
    }

    public boolean isDataIncluded() {
        return dataIncluded;
    }

    public void setDataIncluded(boolean dataIncluded) {
        this.dataIncluded = dataIncluded;

        for (Table dependentTable : dependentTables) {
            dependentTable.setDataIncluded(dataIncluded);
        }
    }

    @Override
    public int compareTo(Table otherTable) {
        return name.compareTo(otherTable.name);
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

        return name.equals(otherTable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        String toString = name;
        toString += " (" + ownSize + ", " + getTreeSize() + ") [";
        if (dependentTables != null) {
            boolean first = true;
            for (Table dependentTable : dependentTables) {
                if (!first) {
                    toString += ", ";
                }
                first = false;
                toString += dependentTable;
            }
        }
        toString += "]";
        return toString;
    }
}

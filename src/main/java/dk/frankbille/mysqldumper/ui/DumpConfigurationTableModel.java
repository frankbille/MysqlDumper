package dk.frankbille.mysqldumper.ui;

import dk.frankbille.mysqldumper.DumpConfiguration;
import dk.frankbille.mysqldumper.Table;

import javax.swing.table.AbstractTableModel;

public class DumpConfigurationTableModel extends AbstractTableModel {
    private final DumpConfiguration dumpConfiguration;

    public DumpConfigurationTableModel(DumpConfiguration dumpConfiguration) {
        this.dumpConfiguration = dumpConfiguration;
    }

    public DumpConfiguration getDumpConfiguration() {
        return dumpConfiguration;
    }

    @Override
    public int getRowCount() {
        return dumpConfiguration.getTables().size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return false;
            case 4:
                return false;
            default:
                throw new IllegalStateException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Table table = dumpConfiguration.getTables().get(rowIndex);

        switch (columnIndex) {
            case 0:
                return table.getName();
            case 1:
                return table.isStructureIncluded();
            case 2:
                return table.isDataIncluded();
            case 3:
                return table.getOwnSize();
            case 4:
                return table.getTreeSize();
            default:
                throw new IllegalStateException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final Table table = dumpConfiguration.getTables().get(rowIndex);

        switch (columnIndex) {
            case 0:
                break;
            case 1:
                boolean structureIncluded = (boolean) aValue;
                table.setStructureIncluded(structureIncluded);
                fireTableDataChanged();
                break;
            case 2:
                boolean dataIncluded = (boolean) aValue;
                table.setDataIncluded(dataIncluded);
                fireTableDataChanged();
                break;
            case 3:
            case 4:
                break;
            default:
                throw new IllegalStateException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            case 3:
                return Long.class;
            case 4:
                return Long.class;
            default:
                throw new IllegalStateException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Table name";
            case 1:
                return "S";
            case 2:
                return "D";
            case 3:
                return "Table size";
            case 4:
                return "Tree size";
            default:
                throw new IllegalStateException("Invalid column index: " + columnIndex);
        }
    }
}

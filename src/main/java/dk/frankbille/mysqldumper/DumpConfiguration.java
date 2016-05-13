package dk.frankbille.mysqldumper;

import java.math.BigInteger;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DumpConfiguration {
    private final List<Table> tables = new ArrayList<>();

    public DumpConfiguration(List<Map<String, Object>> dependentTables, List<Map<String, Object>> tableSizes) {
        clearTables();
        buildTables(dependentTables, tableSizes);
    }

    private void buildTables(List<Map<String, Object>> dependentTables, List<Map<String, Object>> tableSizes) {
        Map<String, Long> tableSizeMap = buildTableSizeMap(tableSizes);

        Map<String, Table> tableLookupMap = new HashMap<>();

        for (Map<String, Object> dependentTableRow : dependentTables) {
            String tableName = (String) dependentTableRow.get("table_name");
            String dependentTableName = (String) dependentTableRow.get("dependent_table_name");

            Table table = getTable(tableName, tableLookupMap, tableSizeMap);

            if (dependentTableName != null) {
                Table dependentTable = getTable(dependentTableName, tableLookupMap, tableSizeMap);
                table.addDependentTable(dependentTable);
            }
        }

        addAllTables(tableLookupMap.values());
    }

    private Table getTable(String tableName, Map<String, Table> tableLookupMap, Map<String, Long> tableSizeMap) {
        Table table = tableLookupMap.get(tableName);
        if (table == null) {
            table = new Table(tableName, tableSizeMap.get(tableName));
            tableLookupMap.put(tableName, table);
        }
        return table;
    }

    private Map<String, Long> buildTableSizeMap(List<Map<String, Object>> tableSizes) {
        Map<String, Long> tableSizeMap = new HashMap<>();

        for (Map<String, Object> tableSize : tableSizes) {
            tableSizeMap.put((String) tableSize.get("table_name"), ((BigInteger) tableSize.get("table_size")).longValue());
        }

        return tableSizeMap;
    }

    public List<Table> getTables() {
        return tables;
    }

    private void addAllTables(Collection<Table> tables) {
        this.tables.addAll(tables);
    }

    private void clearTables() {
        tables.clear();
    }

    public long getTotalSize() {
        long total = 0;
        for (Table table : getTables()) {
            total += table.getOwnSize();
        }
        return total;
    }

    public long getSelectedSize() {
        long total = 0;
        for (Table table : getTables()) {
            if (table.isDataIncluded()) {
                total += table.getOwnSize();
            }
        }
        return total;
    }

}

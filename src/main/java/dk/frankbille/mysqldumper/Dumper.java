package dk.frankbille.mysqldumper;

import dk.frankbille.mysqldumper.sql.MysqlClient;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class Dumper {

    public static DumpConfiguration prepareDump(MysqlClient mysqlClient, ConnectionConfiguration connectionConfiguration) {
        final List<Map<String, Object>> dependentTables = mysqlClient.query("SELECT\n" +
                "  TABLES.TABLE_NAME AS table_name,\n" +
                "  REFERENTIAL_CONSTRAINTS.TABLE_NAME AS dependent_table_name\n" +
                "FROM\n" +
                "  information_schema.TABLES\n" +
                "  LEFT JOIN\n" +
                "  information_schema.REFERENTIAL_CONSTRAINTS ON information_schema.TABLES.TABLE_SCHEMA = information_schema.REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA AND information_schema.TABLES.TABLE_NAME = information_schema.REFERENTIAL_CONSTRAINTS.REFERENCED_TABLE_NAME\n" +
                "WHERE\n" +
                "  TABLES.TABLE_SCHEMA = '" + connectionConfiguration.getDatabase() + "'\n" +
                "ORDER BY\n" +
                "  TABLES.TABLE_NAME", connectionConfiguration);

        final List<Map<String, Object>> tableSizes = mysqlClient.query("SELECT\n" +
                "  table_name,\n" +
                "  (data_length + index_length) AS table_size\n" +
                "FROM\n" +
                "  information_schema.tables\n" +
                "WHERE\n" +
                "  table_schema = '" + connectionConfiguration.getDatabase() + "'\n" +
                "ORDER BY\n" +
                "  table_name", connectionConfiguration);

        return new DumpConfiguration(dependentTables, tableSizes);
    }

    public static void dump(DumpConfiguration dumpConfiguration, OutputStream outputStream) {

    }

}

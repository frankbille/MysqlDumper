package dk.frankbille.mysqldumper;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class Dumper {

    public static DumpConfiguration prepareDump(ConnectionConfiguration connectionConfiguration) {
        JdbcOperations jdbcOperations = new JdbcTemplate(connectionConfiguration.createDataSource());

        final List<Map<String, Object>> dependentTables = jdbcOperations.queryForList("SELECT\n" +
                "  TABLES.TABLE_NAME AS table_name,\n" +
                "  REFERENTIAL_CONSTRAINTS.TABLE_NAME AS dependent_table_name\n" +
                "FROM\n" +
                "  information_schema.TABLES\n" +
                "  LEFT JOIN\n" +
                "  information_schema.REFERENTIAL_CONSTRAINTS ON information_schema.TABLES.TABLE_SCHEMA = information_schema.REFERENTIAL_CONSTRAINTS.CONSTRAINT_SCHEMA AND information_schema.TABLES.TABLE_NAME = information_schema.REFERENTIAL_CONSTRAINTS.REFERENCED_TABLE_NAME\n" +
                "WHERE\n" +
                "  TABLES.TABLE_SCHEMA = '"+connectionConfiguration.getDatabase()+"'\n" +
                "ORDER BY\n" +
                "  TABLES.TABLE_NAME");

        final List<Map<String, Object>> tableSizes = jdbcOperations.queryForList("SELECT\n" +
                "  table_name,\n" +
                "  (data_length + index_length) AS table_size\n" +
                "FROM\n" +
                "  information_schema.tables\n" +
                "WHERE\n" +
                "  table_schema = '"+connectionConfiguration.getDatabase()+"'\n" +
                "ORDER BY\n" +
                "  table_name");

        return new DumpConfiguration(dependentTables, tableSizes);
    }

    public static void dump(DumpConfiguration dumpConfiguration, OutputStream outputStream) {

    }

}

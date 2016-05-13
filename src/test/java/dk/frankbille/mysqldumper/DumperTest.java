package dk.frankbille.mysqldumper;

import dk.frankbille.mysqldumper.sql.MysqlClient;
import dk.frankbille.mysqldumper.sql.MysqlJdbcClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DumperTest {

    private ConnectionConfiguration connectionConfiguration;
    private MysqlJdbcClient mysqlClient;

    @Before
    public void setupTestDatabase() throws IOException {
        Configuration configuration = new Configuration("/test");
        mysqlClient = new MysqlJdbcClient();

        connectionConfiguration = configuration.addNewConnection();
        connectionConfiguration.setHost("localhost");
        connectionConfiguration.setDatabase("mysqldumpertest");
        connectionConfiguration.setUsername("root");

        DataSource dataSource = mysqlClient.createDataSource(connectionConfiguration, false);
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

        jdbcOperations.execute("DROP DATABASE IF EXISTS mysqldumpertest");
        jdbcOperations.execute("CREATE DATABASE mysqldumpertest");

        dataSource = mysqlClient.createDataSource(connectionConfiguration);
        jdbcOperations = new JdbcTemplate(dataSource);

        jdbcOperations.execute(new String(Files.readAllBytes(Paths.get("src/test/resources/testdb.sql"))));
    }

    @Test
    public void prepareDumpShouldCorrectSettingsForDatabase() {
        final DumpConfiguration dumpConfiguration = Dumper.prepareDump(mysqlClient, connectionConfiguration);

        assertThat(dumpConfiguration).isNotNull();

        assertThat(dumpConfiguration.getTables()).hasSize(5);
        assertThat(getDependentTables(dumpConfiguration, "person")).hasSize(2);
        assertThat(getDependentTables(dumpConfiguration, "person")).contains(getTable(dumpConfiguration, "person_relationship"));
        assertThat(getDependentTables(dumpConfiguration, "person")).contains(getTable(dumpConfiguration, "pet"));
        assertThat(getDependentTables(dumpConfiguration, "pet")).hasSize(1);
        assertThat(getDependentTables(dumpConfiguration, "pet")).contains(getTable(dumpConfiguration, "pet_accessories"));
        assertThat(getDependentTables(dumpConfiguration, "accessories")).hasSize(1);
        assertThat(getDependentTables(dumpConfiguration, "accessories")).contains(getTable(dumpConfiguration, "pet_accessories"));
        assertThat(getDependentTables(dumpConfiguration, "person_relationship")).isEmpty();
        assertThat(getDependentTables(dumpConfiguration, "pet_accessories")).isEmpty();
    }

    private Set<Table> getDependentTables(DumpConfiguration dumpConfiguration, String tableName) {
        return getTable(dumpConfiguration, tableName).getDependentTables();
    }

    private Table getTable(DumpConfiguration dumpConfiguration, String tableName) {
        for (Table table : dumpConfiguration.getTables()) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        throw new IllegalStateException("Table not found: "+tableName);
    }

}
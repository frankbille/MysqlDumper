package dk.frankbille.mysqldumper.sql;

import dk.frankbille.mysqldumper.ConnectionConfiguration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class MysqlJdbcClient implements MysqlClient {
    @Override
    public boolean isValidMysqlBinDirectory() {
        return true;
    }

    @Override
    public List<Map<String, Object>> query(String sql, ConnectionConfiguration connectionConfiguration) {
        JdbcOperations jdbcOperations = new JdbcTemplate(createDataSource(connectionConfiguration));

        return jdbcOperations.queryForList(sql);
    }

    public DataSource createDataSource(ConnectionConfiguration connectionConfiguration) {
        return createDataSource(connectionConfiguration, true);
    }

    public DataSource createDataSource(ConnectionConfiguration connectionConfiguration, boolean withDatabase) {
        String jdbcUrl = "jdbc:mysql://";
        jdbcUrl += connectionConfiguration.getHost();
        if (connectionConfiguration.getPort() != 3306 && connectionConfiguration.getPort() != -1) {
            jdbcUrl += ":" + connectionConfiguration.getPort();
        }
        if (withDatabase) {
            jdbcUrl += "/" + connectionConfiguration.getDatabase();
        }
        jdbcUrl += "?allowMultiQueries=true";

        return new DriverManagerDataSource(jdbcUrl, connectionConfiguration.getUsername(), connectionConfiguration.getPassword());
    }
}

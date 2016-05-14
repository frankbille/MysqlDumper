package dk.frankbille.mysqldumper.sql.jdbc;

import dk.frankbille.mysqldumper.ConnectionConfiguration;
import dk.frankbille.mysqldumper.sql.MysqlClient;
import dk.frankbille.mysqldumper.sql.ResultTransformer;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlJdbcClient implements MysqlClient {
    @Override
    public boolean isValidMysqlBinDirectory() {
        return true;
    }

    @Override
    public List<Map<String, Object>> query(String sql, ConnectionConfiguration connectionConfiguration, ResultTransformer resultTransformer) {
        JdbcOperations jdbcOperations = new JdbcTemplate(createDataSource(connectionConfiguration));
        return jdbcOperations.query(sql, new RowMapper(resultTransformer));
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

    private static class RowMapper extends ColumnMapRowMapper {
        private final ResultTransformer resultTransformer;

        private RowMapper(ResultTransformer resultTransformer) {
            if (resultTransformer == null) {
                resultTransformer = new ResultTransformer();
            }
            this.resultTransformer = resultTransformer;
        }

        @Override
        protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
            final String columnName = rs.getMetaData().getColumnName(index);
            final String value = rs.getString(index);
            return resultTransformer.convert(columnName, value);
        }
    }
}

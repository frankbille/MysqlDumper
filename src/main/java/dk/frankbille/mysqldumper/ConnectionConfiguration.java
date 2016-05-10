package dk.frankbille.mysqldumper;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class ConnectionConfiguration {

    private String host;
    private int port = -1;
    private String database;
    private String username;
    private String password = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataSource createDataSource() {
        return createDataSource(true);
    }

    public DataSource createDataSource(boolean withDatabase) {
        String jdbcUrl = "jdbc:mysql://";
        jdbcUrl += host;
        if (port != 3306 && port != -1) {
            jdbcUrl += ":" + port;
        }
        if (withDatabase) {
            jdbcUrl += "/" + database;
        }
        jdbcUrl += "?allowMultiQueries=true";

        return new DriverManagerDataSource(jdbcUrl, username, password);
    }
}

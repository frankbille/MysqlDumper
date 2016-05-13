package dk.frankbille.mysqldumper.sql;

import dk.frankbille.mysqldumper.ConnectionConfiguration;

import java.util.List;
import java.util.Map;

public interface MysqlClient {

    boolean isValidMysqlBinDirectory();

    List<Map<String, Object>> query(String sql, ConnectionConfiguration connectionConfiguration);

}

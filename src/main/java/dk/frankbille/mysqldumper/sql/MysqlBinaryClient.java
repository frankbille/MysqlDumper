package dk.frankbille.mysqldumper.sql;

import dk.frankbille.mysqldumper.Configuration;
import dk.frankbille.mysqldumper.ConnectionConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MysqlBinaryClient implements MysqlClient {

    private final Configuration configuration;

    public MysqlBinaryClient(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isValidMysqlBinDirectory() {
        boolean valid = false;

        final String mysqlBinDirectoryPath = configuration.getMysqlBinDirectory();

        if (mysqlBinDirectoryPath != null) {
            File mysqlBinDirectory = new File(mysqlBinDirectoryPath);
            if (mysqlBinDirectory.exists() && mysqlBinDirectory.isDirectory()) {
                File mysqlBinUnix = new File(mysqlBinDirectory, "mysql");
                File mysqlBinWindows = new File(mysqlBinDirectory, "mysql.exe");

                if (mysqlBinUnix.exists() && getMysqlClientVersion(mysqlBinUnix) != null) {
                    valid = true;
                } else if (mysqlBinWindows.exists() && getMysqlClientVersion(mysqlBinWindows) != null) {
                    valid = true;
                }
            }
        }

        return valid;
    }

    @Override
    public List<Map<String, Object>> query(String sql, ConnectionConfiguration connectionConfiguration) {
        return new ArrayList<>();
    }

    private String getMysqlClientVersion(File mysqlBin) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(mysqlBin.getAbsolutePath(), "--version");
            final Process process = processBuilder.start();
            return read(process.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}

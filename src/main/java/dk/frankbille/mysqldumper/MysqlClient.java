package dk.frankbille.mysqldumper;

import java.io.*;
import java.util.stream.Collectors;

public class MysqlClient {

    public static boolean isValidMysqlBinDirectory(String mysqlBinDirectoryPath) {
        boolean valid = false;

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

    public static String getMysqlClientVersion(File mysqlBin) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(mysqlBin.getAbsolutePath(), "--version");
            final Process process = processBuilder.start();
            final String version = read(process.getInputStream());
            return version;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }


}

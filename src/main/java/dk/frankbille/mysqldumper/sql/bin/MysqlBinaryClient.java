package dk.frankbille.mysqldumper.sql.bin;

import dk.frankbille.mysqldumper.Configuration;
import dk.frankbille.mysqldumper.ConnectionConfiguration;
import dk.frankbille.mysqldumper.sql.MysqlClient;
import dk.frankbille.mysqldumper.sql.ResultTransformer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

        final File mysqlBinary = getMysqlBinary();
        if (mysqlBinary != null && getMysqlClientVersion(mysqlBinary) != null) {
            valid = true;
        }

        return valid;
    }

    @Override
    public List<Map<String, Object>> query(String sql, ConnectionConfiguration connectionConfiguration, ResultTransformer resultTransformer) {
        final List<Map<String, Object>> resultList = new ArrayList<>();

        if (resultTransformer == null) {
            resultTransformer = new ResultTransformer();
        }

        try {
            CommandLine commandLine = CommandLine.parse(getMysqlBinary().getAbsolutePath());
            commandLine.addArguments("-h " + connectionConfiguration.getHost());
            commandLine.addArguments("-P " + connectionConfiguration.getPort());
            commandLine.addArguments("-u " + connectionConfiguration.getUsername());
            if (connectionConfiguration.getPassword() != null && connectionConfiguration.getPassword().length() > 0) {
                commandLine.addArguments("-p " + connectionConfiguration.getPassword());
            }
            commandLine.addArgument("-X");
            commandLine.addArgument(connectionConfiguration.getDatabase());

            final ByteArrayOutputStream commandOutputStream = new ByteArrayOutputStream();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(commandOutputStream, System.err, new ByteArrayInputStream(sql.getBytes())));
            executor.execute(commandLine);

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(new ByteArrayInputStream(commandOutputStream.toByteArray()));

            Element resultSetElement = document.getDocumentElement();
            final NodeList rowNodes = resultSetElement.getChildNodes();
            for (int rowIndex = 0; rowIndex < rowNodes.getLength(); rowIndex++) {
                final Node rowNode = rowNodes.item(rowIndex);

                if ("row".equals(rowNode.getNodeName())) {
                    final Map<String, Object> row = new LinkedHashMap<>();
                    resultList.add(row);

                    final NodeList fieldNodes = rowNode.getChildNodes();
                    for (int fieldIndex = 0; fieldIndex < fieldNodes.getLength(); fieldIndex++) {
                        final Node fieldNode = fieldNodes.item(fieldIndex);

                        if ("field".equals(fieldNode.getNodeName())) {
                            final NamedNodeMap attributes = fieldNode.getAttributes();
                            final Node name = attributes.getNamedItem("name");
                            final String columnName = name.getTextContent();

                            String value = fieldNode.getTextContent();
                            final Node nil = attributes.getNamedItem("xsi:nil");
                            if (nil != null && "true".equals(nil.getTextContent())) {
                                value = null;
                            }

                            row.put(columnName, resultTransformer.convert(columnName, value));
                        }
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }

    private File getMysqlBinary() {
        return getBinary("mysql", "mysql.exe");
    }

    private File getBinary(String unixName, String windowsName) {
        final String mysqlBinDirectoryPath = configuration.getMysqlBinDirectory();

        if (mysqlBinDirectoryPath != null) {
            File mysqlBinDirectory = new File(mysqlBinDirectoryPath);
            if (mysqlBinDirectory.exists() && mysqlBinDirectory.isDirectory()) {
                File mysqlBinUnix = new File(mysqlBinDirectory, unixName);
                File mysqlBinWindows = new File(mysqlBinDirectory, windowsName);

                if (mysqlBinUnix.exists()) {
                    return mysqlBinUnix;
                } else if (mysqlBinWindows.exists()) {
                    return mysqlBinWindows;
                }
            }
        }

        return null;
    }

    private String getMysqlClientVersion(File mysqlBin) {
        try {
            CommandLine commandLine = CommandLine.parse(mysqlBin.getAbsolutePath());
            commandLine.addArgument("--version");

            DefaultExecutor executor = new DefaultExecutor();
            final ByteArrayOutputStream commandOutputStream = new ByteArrayOutputStream();
            executor.setStreamHandler(new PumpStreamHandler(commandOutputStream));
            executor.execute(commandLine);

            return new String(commandOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

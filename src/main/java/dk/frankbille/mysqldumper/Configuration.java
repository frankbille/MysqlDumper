package dk.frankbille.mysqldumper;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Configuration {

    private static final Preferences PREFERENCES = Preferences.userRoot().node("/dk/frankbille/mysqldumper");

    public interface ConfigurationChangedListener {
        void connectionAdded(ConnectionConfiguration connectionConfiguration);
    }

    private List<ConnectionConfiguration> connections = new ArrayList<>();
    private Preferences rootNode;
    private Preferences connectionsNode;
    private final List<ConfigurationChangedListener> listeners = new ArrayList<>();

    public Configuration() {
        this(null);
    }

    public Configuration(String rootKeyOverride) {
        try {
            rootNode = PREFERENCES;
            if (rootKeyOverride != null) {
                rootNode = Preferences.userRoot().node(rootKeyOverride);
            }

            connectionsNode = rootNode.node("connections");

            final String[] connectionKeys;
            connectionKeys = connectionsNode.childrenNames();
            for (String connectionKey : connectionKeys) {
                this.connections.add(new ConnectionConfiguration(connectionsNode.node(connectionKey)));
            }
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMysqlBinDirectory() {
        return rootNode.get("mysqlBinDirectory", null);
    }

    public void setMysqlBinDirectory(String mysqlBinDirectory) {
        rootNode.put("mysqlBinDirectory", mysqlBinDirectory);
    }

    public List<ConnectionConfiguration> getConnections() {
        return connections;
    }

    public ConnectionConfiguration addNewConnection() {
        final int connectionsCount = rootNode.getInt("connectionsCount", 0);
        final Preferences connectionNode = connectionsNode.node("" + connectionsCount);
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(connectionNode);
        connections.add(connectionConfiguration);
        rootNode.putInt("connectionsCount", connectionsCount+1);
        for (ConfigurationChangedListener listener : listeners) {
            listener.connectionAdded(connectionConfiguration);
        }
        return connectionConfiguration;
    }

    public void addConfigurationChangedListener(ConfigurationChangedListener configurationChangedListener) {
        listeners.add(configurationChangedListener);
    }

}

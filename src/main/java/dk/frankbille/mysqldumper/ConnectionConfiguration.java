package dk.frankbille.mysqldumper;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ConnectionConfiguration {

    private final Preferences connectionConfiguration;
    private final List<ConnectionChangedListener> listeners = new ArrayList<>();

    public ConnectionConfiguration(Preferences connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
    }

    public String getName() {
        String name = connectionConfiguration.get("name", null);

        if (name == null || name.length() == 0) {
            name = "";

            if (getUsername() != null) {
                name += getUsername() + "@";
            }

            if (getHost() != null) {
                name += getHost();
            }

            if (getPort() != 3306 && getPort() != -1) {
                name += ":" + getPort();
            }

            if (getDatabase() != null) {
                name += "/" + getDatabase();
            }

            if (name.equals("")) {
                name = "NEW CONNECTION";
            }
        }

        return name;
    }

    public void setName(String name) {
        connectionConfiguration.put("name", name);
        fireConnectionChanged();
    }

    public String getHost() {
        return connectionConfiguration.get("host", null);
    }

    public void setHost(String host) {
        connectionConfiguration.put("host", host);
        fireConnectionChanged();
    }

    public int getPort() {
        return connectionConfiguration.getInt("port", 3306);
    }

    public void setPort(int port) {
        connectionConfiguration.putInt("port", port);
        fireConnectionChanged();
    }

    public String getDatabase() {
        return connectionConfiguration.get("database", null);
    }

    public void setDatabase(String database) {
        connectionConfiguration.put("database", database);
        fireConnectionChanged();
    }

    public String getUsername() {
        return connectionConfiguration.get("username", null);
    }

    public void setUsername(String username) {
        connectionConfiguration.put("username", username);
        fireConnectionChanged();
    }

    public String getPassword() {
        return connectionConfiguration.get("password", "");
    }

    public void setPassword(String password) {
        connectionConfiguration.put("password", password);
        fireConnectionChanged();
    }

    public void addConnectionChangedListener(ConnectionChangedListener connectionChangedListener) {
        listeners.add(connectionChangedListener);
    }

    private void fireConnectionChanged() {
        for (ConnectionChangedListener listener : listeners) {
            listener.connectionChanged(this);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    public interface ConnectionChangedListener {
        void connectionChanged(ConnectionConfiguration connectionConfiguration);
    }
}

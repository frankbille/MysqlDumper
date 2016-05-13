package dk.frankbille.mysqldumper.ui;

import dk.frankbille.mysqldumper.Configuration;
import dk.frankbille.mysqldumper.Configuration.ConfigurationChangedListener;
import dk.frankbille.mysqldumper.ConnectionConfiguration;
import dk.frankbille.mysqldumper.ConnectionConfiguration.ConnectionChangedListener;

import javax.swing.*;

public class ConnectionListModel extends AbstractListModel<ConnectionConfiguration> implements ConfigurationChangedListener, ConnectionChangedListener {

    private final Configuration configuration;

    public ConnectionListModel(Configuration configuration) {
        this.configuration = configuration;
        configuration.addConfigurationChangedListener(this);

        for (ConnectionConfiguration connectionConfiguration : configuration.getConnections()) {
            connectionConfiguration.addConnectionChangedListener(this);
        }
    }

    @Override
    public int getSize() {
        return configuration.getConnections().size();
    }

    @Override
    public ConnectionConfiguration getElementAt(int index) {
        return configuration.getConnections().get(index);
    }



    @Override
    public void connectionAdded(ConnectionConfiguration connectionConfiguration) {
        fireContentsChanged(this, 0, getSize());
        connectionConfiguration.addConnectionChangedListener(this);
    }

    @Override
    public void connectionChanged(ConnectionConfiguration connectionConfiguration) {
        final int index = configuration.getConnections().indexOf(connectionConfiguration);
        fireContentsChanged(this, index, index);
    }
}


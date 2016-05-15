package dk.frankbille.mysqldumper.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dk.frankbille.mysqldumper.ConnectionConfiguration;
import dk.frankbille.mysqldumper.DumpConfiguration;
import dk.frankbille.mysqldumper.Dumper;
import dk.frankbille.mysqldumper.sql.MysqlClient;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

public class DumpConfigurationPanel {

    private JPanel panel1;
    private JTable sqlTablesTable;
    private JLabel sizeLabel;
    private JButton connectButton;
    private JButton dumpButton;
    private JButton refreshButton;
    private JTextField destinationField;
    private JButton destinationButton;
    private MysqlClient mysqlClient;
    private ConnectionConfiguration connectionConfiguration;
    private DumpConfiguration dumpConfiguration;

    public DumpConfigurationPanel() {
        connectButton.addActionListener(e -> setDumpConfiguration(Dumper.prepareDump(mysqlClient, connectionConfiguration)));
        refreshButton.addActionListener(e -> setDumpConfiguration(Dumper.prepareDump(mysqlClient, connectionConfiguration)));

        connectButton.setEnabled(false);
        refreshButton.setEnabled(false);
        dumpButton.setEnabled(false);

        sqlTablesTable.setAutoCreateRowSorter(true);
        sqlTablesTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        sqlTablesTable.setTableHeader(new JTableHeader(sqlTablesTable.getColumnModel()) {
            private String[] columnToolTips = {
                    "Name of the table in the database",
                    "Include structure of table in dump",
                    "Include data of table in dump",
                    "Size of the tables data + indexes",
                    "Size of the tables, and all it's dependent tables data + indexes"
            };

            @Override
            public String getToolTipText(MouseEvent event) {
                Point p = event.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = columnModel.getColumn(index).getModelIndex();
                return columnToolTips[realIndex];
            }
        });
        sqlTablesTable.setDefaultRenderer(Long.class, new DefaultTableCellRenderer.UIResource() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Long) {
                    Long longValue = (Long) value;
                    value = Utils.humanReadableByteCount(longValue);
                }
                super.setValue(value);
            }
        });

        destinationField.getDocument().addDocumentListener(new PersistedDocumentListener() {
            @Override
            protected void doSaveChanges(String text) {
                dumpConfiguration.setDestination(text);
                updateDumpButtonEnabled();
            }
        });
        destinationButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(dumpConfiguration.getDestination());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("SQL files", "sql"));
            if (fileChooser.showDialog(destinationButton, "Select") == JFileChooser.APPROVE_OPTION) {
                destinationField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    public void setConnectionConfiguration(MysqlClient mysqlClient, ConnectionConfiguration connectionConfiguration) {
        this.mysqlClient = mysqlClient;
        this.connectionConfiguration = connectionConfiguration;
        this.dumpConfiguration = null;

        connectButton.setEnabled(true);
        updateDumpButtonEnabled();
        refreshButton.setEnabled(false);
        destinationField.setEnabled(false);
        destinationButton.setEnabled(false);
        sizeLabel.setText("");
    }

    public void setDumpConfiguration(DumpConfiguration dumpConfiguration) {
        this.dumpConfiguration = dumpConfiguration;
        final DumpConfigurationTableModel dataModel = new DumpConfigurationTableModel(dumpConfiguration);
        dataModel.addTableModelListener(e -> updateSizeLabel(dumpConfiguration));
        sqlTablesTable.setModel(dataModel);
        sqlTablesTable.getRowSorter().toggleSortOrder(0);
        updateSizeLabel(dumpConfiguration);
        connectButton.setEnabled(false);
        refreshButton.setEnabled(true);
        updateDumpButtonEnabled();
        destinationField.setEnabled(true);
        destinationButton.setEnabled(true);
    }

    private void updateDumpButtonEnabled() {
        boolean enabled = false;
        if (dumpConfiguration != null) {
            enabled = dumpConfiguration.isDestinationValid();
        }
        dumpButton.setEnabled(enabled);
    }

    private void updateSizeLabel(DumpConfiguration dumpConfiguration) {
        sizeLabel.setText("Selected size: " + Utils.humanReadableByteCount(dumpConfiguration.getSelectedSize()) + ", Total size: " + Utils.humanReadableByteCount(dumpConfiguration.getTotalSize()));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);
        sqlTablesTable = new JTable();
        scrollPane1.setViewportView(sqlTablesTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:max(d;4px):grow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.add(panel2, BorderLayout.SOUTH);
        connectButton = new JButton();
        connectButton.setText("Connect");
        CellConstraints cc = new CellConstraints();
        panel2.add(connectButton, cc.xy(3, 7));
        sizeLabel = new JLabel();
        sizeLabel.setText("");
        panel2.add(sizeLabel, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.CENTER));
        dumpButton = new JButton();
        dumpButton.setText("Dump");
        panel2.add(dumpButton, cc.xy(7, 7));
        refreshButton = new JButton();
        refreshButton.setText("Refresh");
        panel2.add(refreshButton, cc.xy(5, 7));
        final JSeparator separator1 = new JSeparator();
        panel2.add(separator1, cc.xyw(1, 5, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        panel2.add(panel3, cc.xyw(1, 3, 7));
        final JLabel label1 = new JLabel();
        label1.setText("Destination");
        panel3.add(label1, cc.xy(1, 3));
        destinationField = new JTextField();
        panel3.add(destinationField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        destinationButton = new JButton();
        destinationButton.setText("...");
        panel3.add(destinationButton, cc.xy(5, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

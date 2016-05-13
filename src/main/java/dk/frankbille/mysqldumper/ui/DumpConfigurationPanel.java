package dk.frankbille.mysqldumper.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import dk.frankbille.mysqldumper.ConnectionConfiguration;
import dk.frankbille.mysqldumper.DumpConfiguration;
import dk.frankbille.mysqldumper.Dumper;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DumpConfigurationPanel {

    private JPanel panel1;
    private JTable sqlTablesTable;
    private JLabel sizeLabel;
    private JButton connectButton;
    private JButton dumpButton;
    private JButton refreshButton;
    private ConnectionConfiguration connectionConfiguration;

    public DumpConfigurationPanel() {
        connectButton.addActionListener(e -> setDumpConfiguration(Dumper.prepareDump(connectionConfiguration)));
        refreshButton.addActionListener(e -> setDumpConfiguration(Dumper.prepareDump(connectionConfiguration)));

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
    }

    public void setConnectionConfiguration(ConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;

        connectButton.setEnabled(true);
        dumpButton.setEnabled(false);
        refreshButton.setEnabled(false);
        sizeLabel.setText("");
    }

    public void setDumpConfiguration(DumpConfiguration dumpConfiguration) {
        final DumpConfigurationTableModel dataModel = new DumpConfigurationTableModel(dumpConfiguration);
        dataModel.addTableModelListener(e -> updateSizeLabel(dumpConfiguration));
        sqlTablesTable.setModel(dataModel);
        sqlTablesTable.getRowSorter().toggleSortOrder(0);
        updateSizeLabel(dumpConfiguration);
        connectButton.setEnabled(false);
        refreshButton.setEnabled(true);
        dumpButton.setEnabled(true);
    }

    private void updateSizeLabel(DumpConfiguration dumpConfiguration) {
        sizeLabel.setText("Selected size: " + dumpConfiguration.getSelectedSize() + ", Total size: " + dumpConfiguration.getTotalSize());
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
        panel2.setLayout(new FormLayout("fill:max(d;4px):grow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.add(panel2, BorderLayout.SOUTH);
        connectButton = new JButton();
        connectButton.setText("Connect");
        CellConstraints cc = new CellConstraints();
        panel2.add(connectButton, cc.xy(3, 3));
        sizeLabel = new JLabel();
        sizeLabel.setText("");
        panel2.add(sizeLabel, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        dumpButton = new JButton();
        dumpButton.setText("Dump");
        panel2.add(dumpButton, cc.xy(7, 3));
        refreshButton = new JButton();
        refreshButton.setText("Refresh");
        panel2.add(refreshButton, cc.xy(5, 3));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

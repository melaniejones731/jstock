/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2009 Yan Cheng Cheok <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.yccheok.jstock.gui.analysis;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yccheok.jstock.gui.Icons;
import org.yccheok.jstock.gui.IndicatorProjectManager;
import org.yccheok.jstock.gui.JTableUtilities;
import org.yccheok.jstock.gui.Utils;
import org.yccheok.jstock.gui.table.GenericRenderer;
import org.yccheok.jstock.internationalization.GUIBundle;

/**
 *
 * @author yccheok
 */
public class WizardSelectIndicatorJPanel extends javax.swing.JPanel {

    /** Creates new form WizardSelectIndicatorJPanel */
    public WizardSelectIndicatorJPanel(IndicatorProjectManager indicatorProjectManager) {
        this.indicatorProjectManager = indicatorProjectManager;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout(10, 10));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/yccheok/jstock/data/gui"); // NOI18N
        jXHeader1.setDescription(bundle.getString("WizardSelectIndicatorJPanel_Description")); // NOI18N
        jXHeader1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32x32/ark2.png"))); // NOI18N
        jXHeader1.setTitle(bundle.getString("WizardSelectIndicatorJPanel_Title")); // NOI18N
        add(jXHeader1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout(10, 10));

        jPanel3.setLayout(new java.awt.BorderLayout(10, 10));

        jSplitPane1.setDividerLocation(230);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Indicators"));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(getEmptyTableModel());
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.jTable1.getTableHeader().setReorderingAllowed(false);
        // When use with AUTO_RESIZE_LAST_COLUMN, must set locking = true. If not, will not work as expected.
        JTableUtilities.makeTableColumnWidthFit(this.jTable1, 0, 5, true);

        this.jTable1.setDefaultRenderer(String.class, new IndicatorRenderer());
        this.booleanTableCellRenderer = this.jTable1.getDefaultRenderer(Boolean.class);
        this.jTable1.setDefaultRenderer(Boolean.class, new IndicatorRenderer());
        this.jTable1.getSelectionModel().addListSelectionListener(new RowListener());
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel4.setLayout(new java.awt.BorderLayout(10, 10));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText(" ");
        jPanel5.add(jLabel1);

        jPanel4.add(jPanel5, java.awt.BorderLayout.NORTH);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jEditorPane1.setMinimumSize(new java.awt.Dimension(20, 20));
        jEditorPane1.setPreferredSize(new java.awt.Dimension(20, 20));
        jEditorPane1.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jEditorPane1HyperlinkUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(jEditorPane1);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel4);

        jPanel3.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText(bundle.getString("WizardSelectIndicatorJPanel_GettingIndicatorInfo...")); // NOI18N
        jPanel2.add(jLabel3);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16x16/spinner.gif"))); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel2.add(jLabel2);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jEditorPane1HyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jEditorPane1HyperlinkUpdate
        Utils.launchWebBrowser(evt);
    }//GEN-LAST:event_jEditorPane1HyperlinkUpdate

    public void start() {
        // Only start the download task, if we haven't obtained indicator information.
        if (this.indicatorDownloadManager == null) {
            // Cancel any previous task.
            this.cancel();
            this.indicatorDownloadManagerTask = this.getIndicatorDownloadManagerTask();
            this.indicatorDownloadManagerTask.execute();
        }
        else {
            // We cannot solely depend on tableModel, as we click next and back
            // to this page, some previous "planned install" will already be "installed".
            // We need to update tableModel to reflect on this.
            if (this.jTable1.getModel() instanceof IndicatorDownloadManagerTableModel)
            {
                ((IndicatorDownloadManagerTableModel)this.jTable1.getModel()).updatePlannedInstall();
            }
        }
        this.updateGUIState();
    }

    public void cancel() {
        if (this.indicatorDownloadManagerTask != null) {
            this.indicatorDownloadManagerTask.cancel(true);
        }
        this.indicatorDownloadManagerTask = null;
    }

    private TableModel getEmptyTableModel() {
        return new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                GUIBundle.getString("WizardSelectIndicatorJPanel_Install"), GUIBundle.getString("WizardSelectIndicatorJPanel_Indicator")
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
    }

    private void updateGUIState() {
        if (this.indicatorDownloadManager != null) {
            /* Success! */
            this.jLabel2.setVisible(false);
            this.jLabel3.setVisible(true);
            if (false == this.jTable1.getModel() instanceof IndicatorDownloadManagerTableModel)
            {
                // First time.
                this.jTable1.setModel(new IndicatorDownloadManagerTableModel(this.indicatorDownloadManager, this.indicatorProjectManager));
                this.jTable1.setRowSelectionInterval(0, 0);
            }
            // IndicatorProjectManager may has been alerted. Refresh table to update install status.
            this.jTable1.repaint();
            final String output = MessageFormat.format(GUIBundle.getString("WizardSelectIndicatorJPanel_NIndicatorWillBeInstalled_template"), this.getPlannedInstallProjectSize());
            this.jLabel3.setForeground(Color.BLUE);
            this.jLabel3.setText(output);
            JTableUtilities.makeTableColumnWidthFit(jTable1, 0, 5, true);
            // Inform wizard.
            this.observable.setChanged();
            WizardSelectIndicatorJPanel.this.observable.notifyObservers();
        }
        else
        {
            if (this.indicatorDownloadManagerTask != null) {
                final boolean isIndicatorDownloadManagerTaskDone = this.indicatorDownloadManagerTask.isDone();
                if (!isIndicatorDownloadManagerTaskDone) {
                    /* Download In Progress */
                    this.jLabel2.setVisible(true);
                    this.jLabel3.setVisible(true);
                    this.jLabel3.setText(GUIBundle.getString("WizardSelectIndicatorJPanel_GettingIndicatorInfo..."));
                    this.jLabel3.setForeground(Color.BLUE);
                }
                else
                {
                    /* Fail! */
                    this.jLabel2.setVisible(false);
                    this.jLabel3.setVisible(true);
                    this.jLabel3.setText(GUIBundle.getString("WizardSelectIndicatorJPanel_FailToGetIndicatorInfo"));
                    this.jLabel3.setForeground(Color.RED);
                }
            }
            else
            {
                /* Not Started Yet */
                this.jLabel2.setVisible(true);
                this.jLabel3.setVisible(true);
                this.jLabel3.setText(GUIBundle.getString("WizardSelectIndicatorJPanel_GettingIndicatorInfo..."));
                this.jLabel3.setForeground(Color.BLUE);
                this.jTable1.setModel(this.getEmptyTableModel());
                JTableUtilities.makeTableColumnWidthFit(jTable1, 0, 5, true);
            }
        }
    }

    private int getTotalProjectSize() {
        if (this.indicatorDownloadManager == null) {
            return -1;
        }
        return this.indicatorDownloadManager.size();
    }

    private int getInstalledProjectSize() {
        if (this.indicatorDownloadManager == null) {
            return -1;
        }
        int num = 0;
        for (int i = 0; i < this.indicatorDownloadManager.size(); i++) {
            if (this.indicatorProjectManager.contains(this.indicatorDownloadManager.get(i).projectName)) {
                num++;
            }
        }
        return num;
    }

    public List<IndicatorDownloadManager.Info> getPlannedInstallIndicatorDownloadInfos() {
        if (this.indicatorDownloadManager == null) {
            // Use emptyList instead of EMPTY_LIST, to avoid compiler warning.
            return java.util.Collections.emptyList();
        }
        final List<IndicatorDownloadManager.Info> installIndicatorDownloadInfos = new ArrayList<IndicatorDownloadManager.Info>();
        final IndicatorDownloadManagerTableModel tableModel = (IndicatorDownloadManagerTableModel)this.jTable1.getModel();
        for (int i = 0; i < this.indicatorDownloadManager.size(); i++) {
            final IndicatorDownloadManager.Info info = this.indicatorDownloadManager.get(i);
            if (tableModel.isPlannedInstall(info.projectName)) {
                installIndicatorDownloadInfos.add(info);
            }
        }
        return installIndicatorDownloadInfos;
    }

    private int getPlannedInstallProjectSize() {
        if (this.indicatorDownloadManager == null) {
            return -1;
        }
        final IndicatorDownloadManagerTableModel tableModel = (IndicatorDownloadManagerTableModel)this.jTable1.getModel();
        int num = 0;
        for (int i = 0; i < this.indicatorDownloadManager.size(); i++) {
            final String projectName = this.indicatorDownloadManager.get(i).projectName;
            if (tableModel.isPlannedInstall(projectName)) {
                num++;
            }
        }
        return num;
    }

    public boolean useFinishButton() {
        final int total = this.getTotalProjectSize();
        final int installed = this.getInstalledProjectSize();
        if (total < 0 || installed < 0) {
            return false;
        }
        return (total == installed);
    }

    public boolean isNextFinishButtonEnabled() {
        final int total = this.getTotalProjectSize();
        final int installed = this.getInstalledProjectSize();
        final int planned = this.getPlannedInstallProjectSize();
        if (total < 0 || installed < 0 || planned < 0) {
            // Not ready yet.
            return false;
        }
        if (total == 0) {
            // Nothing to install.
            return true;
        }
        if (total == installed) {
            // All were installed.
            return true;
        }
        return planned > 0;
    }
    
    private SwingWorker<IndicatorDownloadManager, Void> getIndicatorDownloadManagerTask() {
        SwingWorker<IndicatorDownloadManager, Void> worker = new SwingWorker<IndicatorDownloadManager, Void>() {

            @Override
            protected void done() {
                // The done Method: When you are informed that the SwingWorker
                // is done via a property change or via the SwingWorker object's
                // done method, you need to be aware that the get methods can
                // throw a CancellationException. A CancellationException is a
                // RuntimeException, which means you do not need to declare it
                // thrown and you do not need to catch it. Instead, you should
                // test the SwingWorker using the isCancelled method before you
                // use the get method.
                if (this.isCancelled()) {
                    // Cancelled by user explicitly. Do not perform any GUI update.
                    // No pop-up message.
                    WizardSelectIndicatorJPanel.this.indicatorDownloadManager = null;
                    return;
                }
                try {
                    WizardSelectIndicatorJPanel.this.indicatorDownloadManager = get();
                } catch (InterruptedException ex) {
                    log.error(null, ex);
                } catch (ExecutionException ex) {
                    log.error(null, ex);
                }

                updateGUIState();
            }

            @Override
            protected IndicatorDownloadManager doInBackground() {
                if (isCancelled()) {
                    return null;
                }
                final String location = IndicatorDownloadManager.getIndicatorDownloadManagerDescriptionFileLocation(indicatorProjectManager.getPreferredOperatorIndicatorType());
                final Utils.InputStreamAndMethod inputStreamAndMethod = Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(location);

                if (inputStreamAndMethod.inputStream == null) {
                    inputStreamAndMethod.method.releaseConnection();
                    return null;
                }

                final IndicatorDownloadManager _indicatorDownloadManager = Utils.fromXML(IndicatorDownloadManager.class, inputStreamAndMethod.inputStream);
                try {
                    inputStreamAndMethod.inputStream.close();
                } catch (IOException exp) {
                    log.error(null, exp);
                } finally {
                    inputStreamAndMethod.method.releaseConnection();
                }
                if (_indicatorDownloadManager == null) {
                    return null;
                }
                
                // Perform simple validation.
                for (int i = 0; i < _indicatorDownloadManager.size(); i++) {
                    if (_indicatorDownloadManager.get(i).type != indicatorProjectManager.getPreferredOperatorIndicatorType()) {
                        return null;
                    }
                }

                for (int i = 0; i < _indicatorDownloadManager.size(); i++) {
                    final IndicatorDownloadManager.Info info = _indicatorDownloadManager.get(i);
                    final URL descriptionURL = info.descriptionURL;
                    final String respond = Utils.getResponseBodyAsStringBasedOnProxyAuthOption(descriptionURL.toString());
                    if (respond != null) {
                        if (respond.contains(Utils.getJStockUUID())) {
                            htmlDescriptionMap.put(info.projectName, respond);
                        }
                    }
                }

                return _indicatorDownloadManager;
            }

        };
        return worker;
    }

    private class IndicatorRenderer extends GenericRenderer {
        /* Help me to obtain JLabel for boolean. */
        private final DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(
                                JTable table, Object value,
                                boolean isSelected, boolean hasFocus,
                                int row, int column) {
            Component c = null;
            if (value instanceof Boolean) {
                final IndicatorDownloadManagerTableModel tableModel = (IndicatorDownloadManagerTableModel)table.getModel();
                final String projectName = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
                if (tableModel.isInstalled(projectName))
                {
                    c = defaultTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (c instanceof JLabel) {
                        final JLabel l = (JLabel)c;
                        l.setText(null);
                        l.setHorizontalAlignment(JLabel.CENTER);
                        l.setHorizontalTextPosition(JLabel.CENTER);
                        l.setIcon(Icons.OK);
                    }
                    else {
                        c = booleanTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                }
                else
                {
                    c = booleanTableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
                if (isSelected || hasFocus) {
                    return c;
                }
                c.setBackground(this.getBackgroundColor(row));
            }
            else {
                c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }

            return c;
        }
    }

    private class RowListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            final int view = jTable1.getSelectedRow();
            final int index = jTable1.convertRowIndexToModel(view);
            final String projectName = (String) ((IndicatorDownloadManagerTableModel)jTable1.getModel()).getValueAt(index, 1);
            jLabel1.setText(projectName);
            String htmlDescription = htmlDescriptionMap.get(projectName);
            jEditorPane1.setText(htmlDescription);
            // Make the scroll bar scrolls to the top.
            jEditorPane1.setCaretPosition(0);
        }
    }

    private class IndicatorDownloadManagerTableModel extends AbstractTableModel {
        private final String[] columnNames = {GUIBundle.getString("WizardSelectIndicatorJPanel_Install"), GUIBundle.getString("WizardSelectIndicatorJPanel_Indicator")};
        private final Class[] columnClasses = {Boolean.class, String.class};

        private final IndicatorDownloadManager downloadManager;
        private final IndicatorProjectManager projectManager;
        private final List<String> planToInstallProjectName = new ArrayList<String>();
        
        public IndicatorDownloadManagerTableModel(IndicatorDownloadManager downloadManager, IndicatorProjectManager projectManager) {
            this.downloadManager = downloadManager;
            this.projectManager = projectManager;
        }

        public boolean isInstalled(String projectName) {
            return this.projectManager.contains(projectName);
        }

        public boolean isPlannedInstall(String projectName) {
            return this.planToInstallProjectName.contains(projectName);
        }

        public void updatePlannedInstall() {
            for (int i = 0; i < this.planToInstallProjectName.size(); i++) {
                if (this.isInstalled(this.planToInstallProjectName.get(i))) {
                    this.planToInstallProjectName.remove(i);
                    i--;
                }
            }
        }

        @Override
        public int getRowCount() {
            return this.downloadManager.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                final String projectName = this.downloadManager.get(rowIndex).projectName;
                return this.isInstalled(projectName) || this.isPlannedInstall(projectName);
            }
            else if (columnIndex == 1) {
                return this.downloadManager.get(rowIndex).projectName;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex != 0) {
                return false;
            }
            final String projectName = this.downloadManager.get(rowIndex).projectName;
            return !(this.isInstalled(projectName));
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            assert(columnIndex == 0);
            final String projectName = this.downloadManager.get(rowIndex).projectName;

            /* Hacking way to detect JCheckBox change state at here. */
            if ((Boolean)aValue) {
                if (this.planToInstallProjectName.contains(projectName) == false) {
                    this.planToInstallProjectName.add(projectName);
                }                
            }
            else {
                this.planToInstallProjectName.remove(projectName);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
            WizardSelectIndicatorJPanel.this.updateGUIState();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Class getColumnClass(int c) {
            return columnClasses[c];
        }
    }

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    private static final Log log = LogFactory.getLog(WizardSelectIndicatorJPanel.class);

    private final IndicatorProjectManager indicatorProjectManager;

    private volatile SwingWorker<IndicatorDownloadManager, Void> indicatorDownloadManagerTask = null;
    private IndicatorDownloadManager indicatorDownloadManager = null;
    private final Map<String, String> htmlDescriptionMap = new HashMap<String, String>();

    /* Hacking way, to obtain JCheckBox for JTable. */
    private TableCellRenderer booleanTableCellRenderer = null;

    // setChanged must be called, before calling notifyObservers.
    // Cumbersome! Perhaps we still should stick back with our own Subject
    // Observer framework.
    private static final class ObservableEx extends java.util.Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
    private final ObservableEx observable = new ObservableEx();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    // End of variables declaration//GEN-END:variables

}
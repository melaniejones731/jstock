/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2010 Yan Cheng CHEOK <yccheok@yahoo.com>
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

package org.yccheok.jstock.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.*;
import java.text.*;
import java.util.Enumeration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.text.*;
import org.yccheok.jstock.engine.Country;
import org.yccheok.jstock.engine.StockServerFactory;
import org.yccheok.jstock.internationalization.MessagesBundle;
import org.yccheok.jstock.network.ProxyDetector;

/**
 *
 * @author  yccheok
 */
public class OptionsNetworkJPanel extends javax.swing.JPanel implements JStockOptionsObserver {
    
    /** Creates new form OptionsNetworkJPanel */
    public OptionsNetworkJPanel() {
        initComponents();
    }

    private SwingWorker getTestConnectionSwingWorker() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            public Boolean doInBackground() {
                // Store old proxy server and old proxy auth information.
                final String httpproxyHost = System.getProperties().getProperty("http.proxyHost");
                final String httpproxyPort = System.getProperties().getProperty("http.proxyPort");
                final String oldProxyAuthUsername = MainFrame.getInstance().getJStockOptions().getProxyAuthUserName();
                final String oldProxyAuthPassword = MainFrame.getInstance().getJStockOptions().getProxyAuthPassword();
                final boolean oldIsProxyAuthEnabled = MainFrame.getInstance().getJStockOptions().isProxyAuthEnabled();

                // Set new proxy auth information.
                final String newProxyAuthUsername = jTextField2.getText() != null ? jTextField2.getText() : "";
                final String newProxyAuthPassword = Utils.encrypt(new String(jPasswordField1.getPassword()));                
                MainFrame.getInstance().getJStockOptions().setProxyAuthUserName(newProxyAuthUsername);
                MainFrame.getInstance().getJStockOptions().setProxyAuthPassword(newProxyAuthPassword);
                MainFrame.getInstance().getJStockOptions().setIsProxyAuthEnabled(jCheckBox1.isSelected());

                // Set new proxy server information.
                if ((jTextField1.getText().length() > 0) && org.yccheok.jstock.engine.Utils.isValidPortNumber(jFormattedTextField1.getText())) {
                    System.getProperties().put("http.proxyHost", jTextField1.getText());
                    System.getProperties().put("http.proxyPort", jFormattedTextField1.getText());
                }
                else {
                    System.getProperties().remove("http.proxyHost");
                    System.getProperties().remove("http.proxyPort");
                }

                try {
                    final String request = "http://www.google.com";
                    // We are not interested at the returned content at all. We just want
                    // to know whether there will be any exception being thrown, during
                    // the process of getting respond.
                    final boolean success = (null != org.yccheok.jstock.gui.Utils.getResponseBodyAsStringBasedOnProxyAuthOption(request));

                    return success;
                }
                catch(Exception exp) {
                    log.error(null, exp);
                }
                finally {
                    // Restore.
                    MainFrame.getInstance().getJStockOptions().setProxyAuthUserName(oldProxyAuthUsername);
                    MainFrame.getInstance().getJStockOptions().setProxyAuthPassword(oldProxyAuthPassword);
                    MainFrame.getInstance().getJStockOptions().setIsProxyAuthEnabled(oldIsProxyAuthEnabled);

                    if (httpproxyHost != null) {
                        System.getProperties().put("http.proxyHost", httpproxyHost);
                    }
                    else {
                        System.getProperties().remove("http.proxyHost");
                    }

                    if (httpproxyPort != null) {
                        System.getProperties().put("http.proxyPort", httpproxyPort);
                    }
                    else {
                        System.getProperties().remove("http.proxyPort");
                    }
                }
                return false;
            }

            @Override
            public void done() {
                // The done Method: When you are informed that the SwingWorker 
                // is done via a property change or via the SwingWorker object's
                // done method, you need to be aware that the get methods can 
                // throw a CancellationException. A CancellationException is a 
                // RuntimeException, which means you do not need to declare it 
                // thrown and you do not need to catch it. Instead, you should 
                // test the SwingWorker using the isCancelled method before you 
                // use the get method.
                if (this.isCancelled()) {
                    return;
                }

                Boolean status = null;
                try {
                    status = get();
                } catch (InterruptedException ex) {
                    log.error(null, ex);
                } catch (ExecutionException ex) {
                    log.error(null, ex);
                } catch (CancellationException ex) {
                    // Some developers suggest to catch this exception, instead of
                    // checking on isCancelled. As I am not confident by merely
                    // isCancelled check can prevent CancellationException (What
                    // if cancellation is happen just after isCancelled check?),
                    // I will apply both techniques.
                    log.error(null, ex);
                }

                OptionsNetworkJPanel.this.updateGUIState();

                if (status == null || status == false) {
                    JOptionPane.showMessageDialog(OptionsNetworkJPanel.this, MessagesBundle.getString("warning_message_wrong_proxy_or_port"), MessagesBundle.getString("warning_title_wrong_proxy_or_port"), JOptionPane.WARNING_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(OptionsNetworkJPanel.this, MessagesBundle.getString("info_message_correct_proxy_and_port"), MessagesBundle.getString("info_title_correct_proxy_and_port"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        return worker;
    }

    private void initJRadioButtons(JStockOptions jStockOptions) {
        final Country country = jStockOptions.getCountry();
        final java.util.List<StockServerFactory> stockServerFactories = MainFrame.getInstance().getStockServerFactories();
        final JLabel label = new JLabel();

        boolean selected = false;
        StockServerFactoryJRadioButton first = null;

        for (StockServerFactory stockServerFactory : stockServerFactories) {
            final StockServerFactoryJRadioButton stockServerJRadioButton = new StockServerFactoryJRadioButton(stockServerFactory);
            
            if (first == null) {
                first = stockServerJRadioButton;
            }

            stockServerJRadioButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    final String text = StockServerFactoryJRadioButton.toReadableText(stockServerJRadioButton.getStockServerFactory()) + " is being used as primary server";
                    label.setText(text);
                }
                
            });

            if (stockServerFactory.getClass() == jStockOptions.getPrimaryStockServerFactoryClass(country)) {
                selected = true;
                stockServerJRadioButton.setSelected(true);
                final String text = StockServerFactoryJRadioButton.toReadableText(stockServerJRadioButton.getStockServerFactory()) + " is being used as primary server";
                label.setText(text);
            }
            this.buttonGroup1.add(stockServerJRadioButton);
            final JPanel jPanel = new JPanel();
            final FlowLayout layout = new FlowLayout();
            layout.setAlignment(FlowLayout.LEFT);
            layout.setHgap(0);
            layout.setVgap(0);
            jPanel.setLayout(layout);
            // Instead of placing stockServerJRadioButton on jPanel5 directly, 
            // we places it on a FlowLayout panel. This is the prevent 
            // stockServerJRadioButton width being extended to the left end of
            // window.
            jPanel.add(stockServerJRadioButton);
            jPanel5.add(jPanel);
        }

        if (selected == false) {
            if (first != null) {
                first.setSelected(true);
                final String text = StockServerFactoryJRadioButton.toReadableText(first.getStockServerFactory()) + " is being used as primary server";
                label.setText(text);
            }
        }
        
        jPanel5.add(label);
    }

    private JFormattedTextField getPortNumberJFormattedTextField() {
        DecimalFormat df = new DecimalFormat("#####");
        NumberFormatter nf = new NumberFormatter(df) {
            @Override
            public String valueToString(Object iv) throws ParseException {
                if ((iv == null) || (((Integer)iv).intValue() == -1)) {
                    return "";
                }
                else {
                    return super.valueToString(iv);
                }
            }
            @Override
            public Object stringToValue(String text) throws ParseException {
                if ("".equals(text)) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        nf.setMinimum(0);
        nf.setMaximum(65534);
        nf.setValueClass(Integer.class);
        return new JFormattedTextField(nf);        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jFormattedTextField1 = getPortNumberJFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/yccheok/jstock/data/gui"); // NOI18N
        jXHeader1.setDescription(bundle.getString("OptionsNetworkJPanel_Description")); // NOI18N
        jXHeader1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/32x32/connect_to_network.png"))); // NOI18N
        jXHeader1.setTitle(bundle.getString("OptionsNetworkJPanel_ProxyServer")); // NOI18N
        add(jXHeader1, java.awt.BorderLayout.NORTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OptionsNetworkJPanel_StockServer"))); // NOI18N

        jPanel5.setLayout(new java.awt.GridLayout(3, 1, 5, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OptionsNetworkJPanel_ProxyServer"))); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("NT LAN Manager (NTLM)"));

        jCheckBox1.setText(bundle.getString("OptionsNetworkJPanel_EnableNTLM")); // NOI18N
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jLabel5.setText(bundle.getString("OptionsNetworkJPanel_Username")); // NOI18N

        jLabel6.setText(bundle.getString("OptionsNetworkJPanel_Password")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPasswordField1, 0, 0, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(242, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("OptionsNetworkJPanel_Server"))); // NOI18N

        jLabel1.setText(bundle.getString("OptionsNetworkJPanel_ProxyServer")); // NOI18N

        jLabel2.setText(bundle.getString("OptionsNetworkJPanel_Port")); // NOI18N

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()-1f));
        jLabel3.setText(bundle.getString("OptionsNetworkJPanel_Example0")); // NOI18N

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getSize()-1f));
        jLabel4.setText(bundle.getString("OptionsNetworkJPanel_Example1")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jFormattedTextField1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16x16/idea.png"))); // NOI18N
        jButton2.setText(bundle.getString("OptionsNetworkJPanel_AutoDetectProxy")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16x16/internet.png"))); // NOI18N
        jButton1.setText(bundle.getString("OptionsNetworkJPanel_TestConnection")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton1);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16x16/spinner.gif"))); // NOI18N
        jPanel7.add(jLabel7);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.testConnectionSwingWorker = getTestConnectionSwingWorker();
        this.updateGUIState();
        this.jButton1.requestFocus();
        this.testConnectionSwingWorker.execute();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        updateGUIState();
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ProxyDetector detector = ProxyDetector.getInstance();

        if ( detector.directConnectionAvailable() ) {
            JOptionPane.showMessageDialog(this, MessagesBundle.getString("info_message_no_proxy_necessary"), MessagesBundle.getString("info_title_no_proxy_necessary"), JOptionPane.INFORMATION_MESSAGE);
            this.jTextField1.setText(null);
            this.jFormattedTextField1.setText(null);
        }
        else {
            if ( !detector.proxyDetected() ) {
                JOptionPane.showMessageDialog(this, MessagesBundle.getString("error_message_no_proxy_found"), MessagesBundle.getString("error_title_no_proxy_found"), JOptionPane.ERROR_MESSAGE);
            }
            else {
                this.jTextField1.setText(detector.getHostname());
                this.jFormattedTextField1.setText("" + detector.getPort());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    @Override
    public void set(JStockOptions jStockOptions) {
        jTextField1.setText(jStockOptions.getProxyServer());
        if (org.yccheok.jstock.engine.Utils.isValidPortNumber(jStockOptions.getProxyPort()))
            jFormattedTextField1.setText("" + jStockOptions.getProxyPort());
        else
            jFormattedTextField1.setText("");

        this.jCheckBox1.setSelected(jStockOptions.isProxyAuthEnabled());
        this.jTextField2.setText(jStockOptions.getProxyAuthUserName());
        this.jPasswordField1.setText(Utils.decrypt(jStockOptions.getProxyAuthPassword()));
        initJRadioButtons(jStockOptions);

        updateGUIState();
    }

    @Override
    public boolean apply(JStockOptions jStockOptions) {        
        if ((jTextField1.getText().length() > 0) && org.yccheok.jstock.engine.Utils.isValidPortNumber(jFormattedTextField1.getText())) {
            System.getProperties().put("http.proxyHost", jTextField1.getText());
            System.getProperties().put("http.proxyPort", jFormattedTextField1.getText());
        }
        else {
            System.getProperties().remove("http.proxyHost");
            System.getProperties().remove("http.proxyPort");
        }
        
        jStockOptions.setProxyServer(jTextField1.getText());
        int port = -1;
        
        if (jFormattedTextField1.getText().length() > 0) {
            try {
                port = Integer.parseInt(jFormattedTextField1.getText());
            }
            catch (NumberFormatException exp) {
                log.error("", exp);
            }
        }
        
        jStockOptions.setProxyPort(port);

        jStockOptions.setIsProxyAuthEnabled(this.jCheckBox1.isSelected());
        jStockOptions.setProxyAuthUserName(jTextField2.getText());
        jStockOptions.setProxyAuthPassword(Utils.encrypt(new String(jPasswordField1.getPassword())));

        JRadioButton tmp = org.yccheok.jstock.gui.Utils.getSelection(this.buttonGroup1);
        // Impossible. Just to be paranoid.
        if (tmp != null) {
            StockServerFactoryJRadioButton button = ((StockServerFactoryJRadioButton)tmp);
            MainFrame.getInstance().updatePrimaryStockServerFactory(jStockOptions.getCountry(), button.getStockServerFactory().getClass());
        }

        return true;
    }

    public void cancel() {
        if (this.testConnectionSwingWorker != null) {
            this.testConnectionSwingWorker.cancel(true);
        }
    }

    private void updateGUIState() {
        // Whether test network connection thread is done or not started yet.
        final boolean isTestConnectionDone = (testConnectionSwingWorker == null || testConnectionSwingWorker.isDone());

        final boolean state = jCheckBox1.isSelected();
        jTextField2.setEnabled(state & isTestConnectionDone);
        jPasswordField1.setEnabled(state & isTestConnectionDone);
        this.jLabel5.setEnabled(state);
        this.jLabel6.setEnabled(state);

        this.jTextField1.setEnabled(isTestConnectionDone);
        this.jFormattedTextField1.setEnabled(isTestConnectionDone);
        this.jCheckBox1.setEnabled(isTestConnectionDone);
        this.jButton1.setEnabled(isTestConnectionDone);
        this.jButton2.setEnabled(isTestConnectionDone);
        jLabel7.setVisible(!isTestConnectionDone);
    }

    private static final Log log = LogFactory.getLog(OptionsNetworkJPanel.class);

    private volatile SwingWorker testConnectionSwingWorker = null;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    // End of variables declaration//GEN-END:variables
    
}

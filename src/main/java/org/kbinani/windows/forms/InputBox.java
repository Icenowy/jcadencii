/*
 * InputBox.cs
 * Copyright © 2008-2011 kbinani
 *
 * This file is part of org.kbinani.windows.forms.
 *
 * org.kbinani.windows.forms is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.windows.forms is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.windows.forms;

import org.kbinani.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


public class InputBox extends BDialog {
    private static final long serialVersionUID = -8016706120038301899L;
    private JPanel jPanel = null;
    private BLabel lblMessage = null;
    private BTextBox txtInput = null;
    private BButton btnOk = null;
    private JPanel jPanel1 = null;
    private BButton btnCancel = null;

    public InputBox(String message) {
        super();
        initialize();
        registerEventHandlers();
        lblMessage.setText(message);
    }

    public String getResult() {
        return txtInput.getText();
    }

    public void setResult(String value) {
        txtInput.setText(value);
    }

    public void btnCancel_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.CANCEL);
    }

    public void btnOk_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.OK);
    }

    private void registerEventHandlers() {
        btnOk.clickEvent.add(new BEventHandler(this, "btnOk_Click"));
        btnCancel.clickEvent.add(new BEventHandler(this, "btnCancel_Click"));
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setSize(new Dimension(320, 132));
        this.setContentPane(getJPanel());
        setCancelButton(btnCancel);
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.gridy = 2;

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.insets = new Insets(3, 12, 6, 12);
            gridBagConstraints1.gridx = 0;

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(12, 12, 3, 12);
            gridBagConstraints.gridy = 0;
            lblMessage = new BLabel();
            lblMessage.setText("JLabel");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(lblMessage, gridBagConstraints);
            jPanel.add(getTxtInput(), gridBagConstraints1);
            jPanel.add(getJPanel1(), gridBagConstraints4);
        }

        return jPanel;
    }

    /**
     * This method initializes txtInput
     *
     * @return javax.swing.JTextField
     */
    private BTextBox getTxtInput() {
        if (txtInput == null) {
            txtInput = new BTextBox();
        }

        return txtInput;
    }

    /**
     * This method initializes btnOk
     *
     * @return javax.swing.JButton
     */
    private BButton getBtnOk() {
        if (btnOk == null) {
            btnOk = new BButton();
            btnOk.setText("OK");
            btnOk.setPreferredSize(new Dimension(100, 29));
        }

        return btnOk;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 0;

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.insets = new Insets(0, 0, 0, 12);
            gridBagConstraints2.gridy = 0;
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(getBtnOk(), gridBagConstraints2);
            jPanel1.add(getBtnCancel(), gridBagConstraints3);
        }

        return jPanel1;
    }

    /**
     * This method initializes btnCancel
     *
     * @return javax.swing.JButton
     */
    private BButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new BButton();
            btnCancel.setText("Cancel");
            btnCancel.setPreferredSize(new Dimension(100, 29));
        }

        return btnCancel;
    }
}

/*
 * FormProjectProperty.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.cadencii.
 *
 * org.kbinani.cadencii is free software; you can redistribute it and/or
 * modify it under the terms of the GPLv3 License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.cadencii;

import org.kbinani.*;

import org.kbinani.apputil.*;

import org.kbinani.windows.forms.*;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BForm;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BPanel;
import org.kbinani.windows.forms.BTextBox;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


public class FormTrackProperty extends BDialog {
    private static final long serialVersionUID = 1L;
    private int m_master_tuning;
    private JPanel jPanel = null;
    private BLabel lblMasterTuning = null;
    private BTextBox txtMasterTuning = null;
    private BPanel jPanel2 = null;
    private BButton btnOK = null;
    private BButton btnCancel = null;

    public FormTrackProperty(int master_tuning_in_cent) {
        super();
        initialize();
        registerEventHandlers();
        setResources();
        applyLanguage();
        m_master_tuning = master_tuning_in_cent;
        txtMasterTuning.setText(master_tuning_in_cent + "");
        Util.applyFontRecurse(this, AppManager.editorConfig.getBaseFont());
    }

    public void applyLanguage() {
        lblMasterTuning.setText(gettext("Master Tuning in Cent"));
        setTitle(gettext("Track Property"));
        btnOK.setText(gettext("OK"));
        btnCancel.setText(gettext("Cancel"));
    }

    public int getMasterTuningInCent() {
        return m_master_tuning;
    }

    private String gettext(String id) {
        return Messaging.getMessage(id);
    }

    private void registerEventHandlers() {
        txtMasterTuning.textChangedEvent.add(new BEventHandler(this,
                "txtMasterTuning_TextChanged"));
        btnOK.clickEvent.add(new BEventHandler(this, "btnOK_Click"));
        btnCancel.clickEvent.add(new BEventHandler(this, "btnCancel_Click"));
    }

    private void setResources() {
    }

    public void txtMasterTuning_TextChanged(Object sender, BEventArgs e) {
        int v = m_master_tuning;

        try {
            v = str.toi(txtMasterTuning.getText());
            m_master_tuning = v;
        } catch (Exception ex) {
        }
    }

    public void btnCancel_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.CANCEL);
    }

    public void btnOK_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.OK);
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setSize(new Dimension(288, 138));
        this.setTitle("Project Property");
        this.setContentPane(getJPanel());
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.weighty = 1.0D;
            gridBagConstraints2.anchor = GridBagConstraints.NORTHEAST;
            gridBagConstraints2.gridy = 2;

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new Insets(2, 35, 12, 0);
            gridBagConstraints1.gridx = 0;

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(12, 12, 2, 0);
            gridBagConstraints.gridy = 0;
            lblMasterTuning = new BLabel();
            lblMasterTuning.setText("Master Tuning in Cent");
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(lblMasterTuning, gridBagConstraints);
            jPanel.add(getTxtMasterTuning(), gridBagConstraints1);
            jPanel.add(getJPanel2(), gridBagConstraints2);
        }

        return jPanel;
    }

    /**
     * This method initializes txtMasterTuning
     *
     * @return org.kbinani.windows.forms.BTextBox
     */
    private BTextBox getTxtMasterTuning() {
        if (txtMasterTuning == null) {
            txtMasterTuning = new BTextBox();
            txtMasterTuning.setPreferredSize(new Dimension(187, 20));
        }

        return txtMasterTuning;
    }

    /**
     * This method initializes jPanel2
     *
     * @return org.kbinani.windows.forms.BPanel
     */
    private BPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
            gridBagConstraints52.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints52.gridx = 1;
            gridBagConstraints52.gridy = 0;
            gridBagConstraints52.insets = new Insets(0, 0, 0, 16);

            GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
            gridBagConstraints42.anchor = GridBagConstraints.WEST;
            gridBagConstraints42.gridx = 0;
            gridBagConstraints42.gridy = 0;
            gridBagConstraints42.insets = new Insets(0, 0, 0, 16);
            jPanel2 = new BPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(getBtnOK(), gridBagConstraints42);
            jPanel2.add(getBtnCancel(), gridBagConstraints52);
        }

        return jPanel2;
    }

    /**
     * This method initializes btnOK
     *
     * @return org.kbinani.windows.forms.BButton
     */
    private BButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new BButton();
            btnOK.setText("OK");
        }

        return btnOK;
    }

    /**
     * This method initializes btnCancel
     *
     * @return org.kbinani.windows.forms.BButton
     */
    private BButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new BButton();
            btnCancel.setText("Cancel");
        }

        return btnCancel;
    }
}

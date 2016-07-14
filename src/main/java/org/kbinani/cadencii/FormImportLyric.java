/*
 * FormImportLyric.cs
 * Copyright © 2008-2011 kbinani
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
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BTextArea;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.*;

import javax.swing.JPanel;


public class FormImportLyric extends BDialog {
    private static final long serialVersionUID = 1L;
    private int m_max_notes = 1;
    private JPanel jContentPane = null;
    private BLabel lblNotes = null;
    private BTextArea txtLyrics = null;
    private JPanel jPanel = null;
    private BButton btnOK = null;
    private BButton btnCancel = null;
    private BLabel lblRightValue = null;

    public FormImportLyric(int max_notes) {
        super();
        initialize();
        registerEventHandlers();
        setResources();
        applyLanguage();
        setMaxNotes(max_notes);
        Util.applyFontRecurse(this, AppManager.editorConfig.getBaseFont());
    }

    public void setVisible(boolean value) {
        super.setVisible(value);

        //TODO: FormImportLyric#setVisible
    }

    public void applyLanguage() {
        setTitle(gettext("Import lyrics"));
        btnCancel.setText(gettext("Cancel"));
        btnOK.setText(gettext("OK"));
    }

    /// <summary>
    /// このダイアログに入力できる最大の文字数を設定します．
    /// </summary>
    /// <param name="max_notes"></param>
    public void setMaxNotes(int max_notes) {
        String notes = (max_notes > 1) ? " [notes]" : " [note]";
        this.lblNotes.setText("Max : " + max_notes + notes);
        this.m_max_notes = max_notes;
    }

    public String[] getLetters() {
        Vector<Character> _SMALL = new Vector<Character>(Arrays.asList(
                    new Character[] {
                        'ぁ', 'ぃ', 'ぅ', 'ぇ', 'ぉ', 'ゃ', 'ゅ', 'ょ', 'ァ', 'ィ', 'ゥ',
                        'ェ', 'ォ', 'ャ', 'ュ', 'ョ'
                    }));
        String tmp = "";

        for (int i = 0; i < m_max_notes; i++) {
            if (i >= txtLyrics.getLineCount()) {
                break;
            }

            try {
                int start = txtLyrics.getLineStartOffset(i);
                int end = txtLyrics.getLineEndOffset(i);
                tmp += (txtLyrics.getText(start, end - start) + " ");
            } catch (Exception ex) {
                Logger.write(FormImportLyric.class + ".getLetters; ex=" + ex +
                    "\n");
            }
        }

        String[] spl = PortUtil.splitString(tmp,
                new char[] { '\n', '\t', ' ', '　', '\r' }, true);
        Vector<String> ret = new Vector<String>();

        for (int j = 0; j < spl.length; j++) {
            String s = spl[j];
            char[] list = s.toCharArray();
            String t = "";
            int i = -1;

            while ((i + 1) < list.length) {
                i++;

                if ((0x41 <= list[i]) && (list[i] <= 0x176)) {
                    t += (list[i] + "");
                } else {
                    if (PortUtil.getStringLength(t) > 0) {
                        ret.add(t);
                        t = "";
                    }

                    if ((i + 1) < list.length) {
                        if (_SMALL.contains(list[i + 1])) {
                            // 次の文字が拗音の場合
                            ret.add(list[i] + "" + list[i + 1] + "");
                            i++;
                        } else {
                            ret.add(list[i] + "");
                        }
                    } else {
                        ret.add(list[i] + "");
                    }
                }
            }

            if (PortUtil.getStringLength(t) > 0) {
                ret.add(t);
            }
        }

        return ret.toArray(new String[] {  });
    }

    private static String gettext(String id) {
        return Messaging.getMessage(id);
    }

    private void registerEventHandlers() {
        btnOK.clickEvent.add(new BEventHandler(this, "btnOK_Click"));
        btnCancel.clickEvent.add(new BEventHandler(this, "btnCancel_Click"));
    }

    private void setResources() {
    }

    public void btnOK_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.OK);
    }

    public void btnCancel_Click(Object sender, BEventArgs e) {
        setDialogResult(BDialogResult.CANCEL);
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(456, 380);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
        setCancelButton(btnCancel);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.insets = new Insets(0, 0, 16, 0);
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 2;

            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
            gridBagConstraints1.insets = new Insets(0, 16, 16, 16);
            gridBagConstraints1.gridx = 0;

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(16, 16, 8, 0);
            gridBagConstraints.gridy = 0;
            lblNotes = new BLabel();
            lblNotes.setText("Max : *[notes]");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(lblNotes, gridBagConstraints);
            jContentPane.add(getTxtLyrics(), gridBagConstraints1);
            jContentPane.add(getJPanel(), gridBagConstraints4);
        }

        return jContentPane;
    }

    /**
     * This method initializes txtLyrics
     *
     * @return javax.swing.BTextArea
     */
    private BTextArea getTxtLyrics() {
        if (txtLyrics == null) {
            txtLyrics = new BTextArea();
        }

        return txtLyrics;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.gridy = 0;
            lblRightValue = new BLabel();
            lblRightValue.setText("");
            lblRightValue.setPreferredSize(new Dimension(4, 4));

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints3.gridy = 0;

            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.insets = new Insets(0, 0, 0, 12);
            gridBagConstraints2.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getBtnOK(), gridBagConstraints2);
            jPanel.add(getBtnCancel(), gridBagConstraints3);
            jPanel.add(lblRightValue, gridBagConstraints5);
        }

        return jPanel;
    }

    /**
     * This method initializes btnOK
     *
     * @return javax.swing.BButton
     */
    private BButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new BButton();
            btnOK.setText("OK");
            btnOK.setPreferredSize(new Dimension(100, 29));
        }

        return btnOK;
    }

    /**
     * This method initializes btnCancel
     *
     * @return javax.swing.BButton
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

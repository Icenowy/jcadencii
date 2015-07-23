/*
 * FormCheckUnknownSingerAndResampler.cs
 * Copyright © 2010 kbinani
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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BCheckBox;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BTextBox;


import java.awt.*;
import java.util.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;

    public class FormCheckUnknownSingerAndResampler extends BDialog
    {
        /// <summary>
        /// コンストラクタ．
        /// </summary>
        /// <param name="singer"></param>
        /// <param name="apply_singer"></param>
        /// <param name="resampler"></param>
        /// <param name="apply_resampler"></param>
        public FormCheckUnknownSingerAndResampler( String singer, boolean apply_singer, String resampler, boolean apply_resampler )
        {
super();
initialize();
applyLanguage();
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );

// singers
checkSingerImport.setSelected( apply_singer );
checkSingerImport.setEnabled( apply_singer );
textSingerPath.setEditable( false );
textSingerPath.setEnabled( apply_singer );
if ( apply_singer ) {
    textSingerPath.setText( singer );
    SingerConfig sc = new SingerConfig();
    String path_image = Utility.readUtauSingerConfig( singer, sc );
    Image img = IconParader.createIconImage( path_image, sc.VOICENAME );
    pictureSinger.setImage( img );
    labelSingerName.setText( sc.VOICENAME );
}

// resampler
checkResamplerImport.setSelected( apply_resampler );
checkResamplerImport.setEnabled( apply_resampler );
textResamplerPath.setEditable( false );
textResamplerPath.setEnabled( apply_resampler );
if ( apply_resampler ) {
    textResamplerPath.setText( resampler );
}

registerEventHandlers();
        }

        /// <summary>
        /// 原音の項目にチェックが入れられたか
        /// </summary>
        /// <returns></returns>
        public boolean isSingerChecked()
        {
return checkSingerImport.isSelected();
        }

        /// <summary>
        /// 原音のパスを取得します
        /// </summary>
        /// <returns></returns>
        public String getSingerPath()
        {
return textSingerPath.getText();
        }

        /// <summary>
        /// リサンプラーの項目にチェックが入れられたかどうか
        /// </summary>
        /// <returns></returns>
        public boolean isResamplerChecked()
        {
return checkResamplerImport.isSelected();
        }

        /// <summary>
        /// リサンプラーのパスを取得します
        /// </summary>
        /// <returns></returns>
        public String getResamplerPath()
        {
return textResamplerPath.getText();
        }

        /// <summary>
        /// イベントハンドラを登録します
        /// </summary>
        private void registerEventHandlers()
        {
        }

        private static String gettext( String id )
        {
return Messaging.getMessage( id );
        }

        private void applyLanguage()
        {
setTitle( gettext( "Unknown singers and resamplers" ) );
labelMessage.setText( gettext( "These singers and resamplers are not registered to Cadencii.\nCheck the box if you want to register them." ) );
checkSingerImport.setText( gettext( "Import singer" ) );
checkResamplerImport.setText( gettext( "Import resampler" ) );
        }


    private static final long serialVersionUID = 5210609912644248288L;
    private JPanel jPanel1 = null;
    private JPanel jPanel3 = null;
    private BButton buttonCancel = null;
    private BButton buttonOk = null;
    private JPanel jPanel2 = null;
    private BLabel labelMessage = null;
    private BCheckBox checkSingerImport = null;
    private BLabel labelSingerName = null;
    private BTextBox textSingerPath = null;
    private BCheckBox checkResamplerImport = null;
    private BTextBox textResamplerPath = null;
    private IconParader pictureSinger = null;
    private BLabel lblRightValue = null;


    private void initialize() {
        this.setSize(new Dimension(419, 349));
        this.setTitle("Unknown singers and resamplers");
        this.setContentPane(getJPanel1());
        setCancelButton( buttonCancel );
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints31.gridy = 4;
            gridBagConstraints31.weightx = 1.0;
            gridBagConstraints31.insets = new Insets(6, 36, 6, 12);
            gridBagConstraints31.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(12, 12, 6, 12);
            gridBagConstraints2.gridy = 3;
            GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
            gridBagConstraints110.gridx = 1;
            gridBagConstraints110.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints110.insets = new Insets(6, 36, 12, 12);
            gridBagConstraints110.gridy = 2;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.insets = new Insets(6, 12, 6, 12);
            gridBagConstraints5.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new Insets(12, 12, 6, 12);
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints.gridy = 0;
            labelMessage = new BLabel();
            labelMessage.setText("These singers and resamplers are not registered to Cadencii. Check the box if you want to register them.");
            labelMessage.setVerticalAlignment(SwingConstants.TOP);
            labelMessage.setAutoEllipsis(true);
            labelMessage.setPreferredSize(new Dimension(56, 57));
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridy = 5;
            gridBagConstraints20.gridheight = 1;
            gridBagConstraints20.gridwidth = 2;
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.gridwidth = 3;
            gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints20.anchor = GridBagConstraints.NORTHEAST;
            gridBagConstraints20.weightx = 0.0D;
            gridBagConstraints20.insets = new Insets(16, 0, 16, 12);
            gridBagConstraints20.weighty = 1.0D;
            gridBagConstraints20.gridy = 6;
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(getJPanel3(), gridBagConstraints20);
            jPanel1.add(labelMessage, gridBagConstraints);
            jPanel1.add(getCheckSingerImport(), gridBagConstraints5);
            jPanel1.add(getJPanel2(), gridBagConstraints110);
            jPanel1.add(getCheckResamplerImport(), gridBagConstraints2);
            jPanel1.add(getTextResamplerPath(), gridBagConstraints31);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel3	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.gridy = 0;
            lblRightValue = new BLabel();
            lblRightValue.setText("");
            lblRightValue.setPreferredSize(new Dimension(4, 4));
            GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
            gridBagConstraints111.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints111.gridy = 0;
            gridBagConstraints111.gridx = 2;
            GridBagConstraints gridBagConstraints1211 = new GridBagConstraints();
            gridBagConstraints1211.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints1211.gridy = 0;
            gridBagConstraints1211.gridx = 1;
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(getButtonCancel(), gridBagConstraints1211);
            jPanel3.add(getButtonOk(), gridBagConstraints111);
            jPanel3.add(lblRightValue, gridBagConstraints4);
        }
        return jPanel3;
    }

    /**
     * This method initializes buttonCancel	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getButtonCancel() {
        if (buttonCancel == null) {
            buttonCancel = new BButton();
            buttonCancel.setText("Cancel");
            buttonCancel.setPreferredSize(new Dimension(100, 29));
        }
        return buttonCancel;
    }

    /**
     * This method initializes buttonOk	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getButtonOk() {
        if (buttonOk == null) {
            buttonOk = new BButton();
            buttonOk.setText("OK");
            buttonOk.setPreferredSize(new Dimension(100, 29));
        }
        return buttonOk;
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            labelSingerName = new BLabel();
            labelSingerName.setText("(name)");
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridheight = 2;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.insets = new Insets(3, 12, 3, 0);
            gridBagConstraints3.gridy = 1;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new Insets(3, 12, 3, 0);
            gridBagConstraints1.gridy = 0;
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(getPictureSinger(), gridBagConstraints6);
            jPanel2.add(labelSingerName, gridBagConstraints1);
            jPanel2.add(getTextSingerPath(), gridBagConstraints3);
        }
        return jPanel2;
    }

    /**
     * This method initializes checkSingerImport	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckSingerImport() {
        if (checkSingerImport == null) {
            checkSingerImport = new BCheckBox();
            checkSingerImport.setText("Import singer");
        }
        return checkSingerImport;
    }

    /**
     * This method initializes pictureSinger	
     * 	
     * @return org.kbinani.cadencii.IconParader	
     */
    private IconParader getPictureSinger() {
        if (pictureSinger == null) {
            pictureSinger = new IconParader();
            pictureSinger.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
            pictureSinger.setPreferredSize(new Dimension(48, 48));
        }
        return pictureSinger;
    }

    /**
     * This method initializes textSingerPath	
     * 	
     * @return org.kbinani.windows.forms.BTextBox	
     */
    private BTextBox getTextSingerPath() {
        if (textSingerPath == null) {
            textSingerPath = new BTextBox();
            textSingerPath.setPreferredSize(new Dimension(169, 19));
        }
        return textSingerPath;
    }

    /**
     * This method initializes checkResamplerImport	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckResamplerImport() {
        if (checkResamplerImport == null) {
            checkResamplerImport = new BCheckBox();
            checkResamplerImport.setText("Import resampler");
        }
        return checkResamplerImport;
    }

    /**
     * This method initializes textResamplerPath	
     * 	
     * @return org.kbinani.windows.forms.BTextBox	
     */
    private BTextBox getTextResamplerPath() {
        if (textResamplerPath == null) {
            textResamplerPath = new BTextBox();
            textResamplerPath.setPreferredSize(new Dimension(169, 19));
        }
        return textResamplerPath;
    }

    }


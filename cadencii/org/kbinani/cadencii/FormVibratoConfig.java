/*
 * FormVibratoConfig.cs
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BComboBox;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BGroupBox;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BPanel;
import org.kbinani.windows.forms.BRadioButton;
import org.kbinani.windows.forms.BTextBox;
import org.kbinani.windows.forms.RadioButtonManager;


import java.awt.event.*;
import java.util.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;

    public class FormVibratoConfig extends BDialog{
        private VibratoHandle m_vibrato;
        private int m_note_length;

        /// <summary>
        /// コンストラクタ．引数vibrato_handleには，Cloneしたものを渡さなくてよい．
        /// </summary>
        /// <param name="vibrato_handle"></param>
        /// <param name="note_length"></param>
        /// <param name="default_vibrato_length"></param>
        /// <param name="type"></param>
        /// <param name="use_original"></param>
        public FormVibratoConfig( 
VibratoHandle vibrato_handle, 
int note_length, 
DefaultVibratoLengthEnum default_vibrato_length, 
SynthesizerType type, 
boolean use_original )
        {
super();
initialize();

if ( use_original ) {
    radioUserDefined.setSelected( true );
} else {
    if ( type == SynthesizerType.VOCALOID1 ) {
        radioVocaloid1.setSelected( true );
    } else {
        radioVocaloid2.setSelected( true );
    }
}
if ( vibrato_handle != null ) {
    m_vibrato = (VibratoHandle)vibrato_handle.clone();
}

// 選択肢の状態を更新
updateComboBoxStatus();
// どれを選ぶか？
if( vibrato_handle != null ){
    for ( int i = 0; i < comboVibratoType.getItemCount(); i++ ) {
        VibratoHandle handle = (VibratoHandle)comboVibratoType.getItemAt( i );
        if ( vibrato_handle.IconID.equals( handle.IconID ) ) {
            comboVibratoType.setSelectedIndex( i );
            break;
        }
    }
}

txtVibratoLength.setEnabled( vibrato_handle != null );
if ( vibrato_handle != null ) {
    txtVibratoLength.setText( (int)((float)vibrato_handle.getLength() / (float)note_length * 100.0f) + "" );
} else {
    String s = "";
    if ( default_vibrato_length == DefaultVibratoLengthEnum.L100 ) {
        s = "100";
    } else if ( default_vibrato_length == DefaultVibratoLengthEnum.L50 ) {
        s = "50";
    } else if ( default_vibrato_length == DefaultVibratoLengthEnum.L66 ) {
        s = "66";
    } else if ( default_vibrato_length == DefaultVibratoLengthEnum.L75 ) {
        s = "75";
    }
    txtVibratoLength.setText( s );
}
  
m_note_length = note_length;

registerEventHandlers();
setResources();
applyLanguage();

Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public void applyLanguage()
        {
setTitle( _( "Vibrato property" ) );
lblVibratoLength.setText( _( "Vibrato length" ) );
lblVibratoLength.setMnemonic( KeyEvent.VK_L, txtVibratoLength );
lblVibratoType.setText( _( "Vibrato Type" ) );
lblVibratoType.setMnemonic( KeyEvent.VK_T, comboVibratoType );
btnOK.setText( _( "OK" ) );
btnCancel.setText( _( "Cancel" ) );
groupSelect.setTitle( _( "Select from" ) );
        }

        /// <summary>
        /// 編集済みのビブラート設定．既にCloneされているので，改めてCloneしなくて良い
        /// </summary>
        public VibratoHandle getVibratoHandle()
        {
return m_vibrato;
        }

        private static String _( String id )
        {
return Messaging.getMessage( id );
        }

        /// <summary>
        /// ビブラートの選択肢の状態を更新します
        /// </summary>
        private void updateComboBoxStatus()
        {
// 選択位置
int old = comboVibratoType.getSelectedIndex();

// 全部削除
comboVibratoType.removeAllItems();

// 「ビブラート無し」を表すアイテムを追加
VibratoHandle empty = new VibratoHandle();
empty.setCaption( "[Non Vibrato]" );
empty.IconID = "$04040000";
comboVibratoType.addItem( empty );

// 選択元を元に，選択肢を追加する
if ( radioUserDefined.isSelected() ) {
    // ユーザー定義のを使う場合
    int size = AppManager.editorConfig.AutoVibratoCustom.size();
    for ( int i = 0; i < size; i++ ) {
        VibratoHandle handle = AppManager.editorConfig.AutoVibratoCustom.get( i );
        comboVibratoType.addItem( handle );
    }
} else {
    // VOCALOID1/VOCALOID2のシステム定義のを使う場合
    SynthesizerType type = radioVocaloid1.isSelected() ? SynthesizerType.VOCALOID1 : SynthesizerType.VOCALOID2;
    for ( Iterator<VibratoHandle> itr = VocaloSysUtil.vibratoConfigIterator( type ); itr.hasNext(); ) {
        VibratoHandle vconfig = itr.next();
        comboVibratoType.addItem( vconfig );
    }
}

// 選択位置を戻せるなら戻す
int index = old;
if ( index >= comboVibratoType.getItemCount() ) {
    index = comboVibratoType.getItemCount() - 1;
}
if ( 0 <= index ) {
    comboVibratoType.setSelectedIndex( index );
}
        }

        private void registerEventHandlers()
        {
btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
btnCancel.clickEvent.add( new BEventHandler( this, "btnCancel_Click" ) );
radioUserDefined.checkedChangedEvent.add( new BEventHandler( this, "handleRadioCheckedChanged" ) );
comboVibratoType.selectedIndexChangedEvent.add( new BEventHandler( this, "comboVibratoType_SelectedIndexChanged" ) );
  txtVibratoLength.textChangedEvent.add( new BEventHandler( this, "txtVibratoLength_TextChanged" ) );
        }

        public void handleRadioCheckedChanged( Object sender, BEventArgs e )
        {
comboVibratoType.selectedIndexChangedEvent.remove( new BEventHandler( this, "comboVibratoType_SelectedIndexChanged" ) );
updateComboBoxStatus();
comboVibratoType.selectedIndexChangedEvent.add( new BEventHandler( this, "comboVibratoType_SelectedIndexChanged" ) );
        }

        private void setResources()
        {
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
        }

        public void comboVibratoType_SelectedIndexChanged( Object sender, BEventArgs e )
        {
int index = comboVibratoType.getSelectedIndex();
if ( index >= 0 ) {
    String s = ((VibratoHandle)comboVibratoType.getItemAt( index )).IconID;
    if ( s.equals( "$04040000" ) ) {
        m_vibrato = null;
        txtVibratoLength.setEnabled( false );
        return;
    } else {
        txtVibratoLength.setEnabled( true );
        VibratoHandle src = null;
        if ( radioUserDefined.isSelected() ) {
            int size = vec.size( AppManager.editorConfig.AutoVibratoCustom );
            for ( int i = 0; i < size; i++ ) {
                VibratoHandle handle = vec.get( AppManager.editorConfig.AutoVibratoCustom, i );
                if ( str.compare( s, handle.IconID ) ) {
                    src = handle;
                    break;
                }
            }
        } else {
            SynthesizerType type = radioVocaloid1.isSelected() ? SynthesizerType.VOCALOID1 : SynthesizerType.VOCALOID2;
            for ( Iterator<VibratoHandle> itr = VocaloSysUtil.vibratoConfigIterator( type ); itr.hasNext(); ) {
                VibratoHandle vconfig = itr.next();
                if ( str.compare( s, vconfig.IconID ) ) {
                    src = vconfig;
                    break;
                }
            }
        }
        if ( src != null ) {
            int percent;
            try {
                percent = str.toi( txtVibratoLength.getText() );
            } catch ( Exception ex ) {
                return;
            }
            m_vibrato = (VibratoHandle)src.clone();
            m_vibrato.setLength( (int)(m_note_length * percent / 100.0f) );
            return;
        }
    }
}
        }

        public void txtVibratoLength_TextChanged( Object sender, BEventArgs e )
        {
int percent = 0;
try {
    percent = str.toi( txtVibratoLength.getText() );
    if ( percent < 0 ) {
        percent = 0;
    } else if ( 100 < percent ) {
        percent = 100;
    }
} catch ( Exception ex ) {
    return;
}
if ( percent == 0 ) {
    m_vibrato = null;
    txtVibratoLength.setEnabled( false );
} else {
    if ( m_vibrato != null ) {
        int new_length = (int)(m_note_length * percent / 100.0f);
        m_vibrato.setLength( new_length );
    }
}
        }

        public void btnCancel_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }

    
	private static final long serialVersionUID = 1L;
	private BPanel jContentPane = null;
	private BPanel jPanel2 = null;
	private BButton btnOK = null;
	private BButton btnCancel = null;
	private BLabel lblVibratoLength = null;
	private BTextBox txtVibratoLength = null;
	private BLabel jLabel1 = null;
	private BLabel lblVibratoType = null;
	private BComboBox comboVibratoType = null;
    private BGroupBox groupSelect = null;
    private BRadioButton radioVocaloid1 = null;
    private BRadioButton radioVocaloid2 = null;
    private BRadioButton radioUserDefined = null;
    private RadioButtonManager mManager = null;
    private BLabel lblSpacer = null;
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(398, 225);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		mManager = new RadioButtonManager();
		mManager.add( radioUserDefined );
		mManager.add( radioVocaloid1 );
		mManager.add( radioVocaloid2 );
        setCancelButton( btnCancel );
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(6, 12, 0, 12);
			gridBagConstraints31.gridwidth = 3;
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			gridBagConstraints31.weighty = 1.0D;
			gridBagConstraints31.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.weighty = 0.0D;
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.insets = new Insets(16, 0, 16, 12);
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 0.0D;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.insets = new Insets(3, 12, 3, 0);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(3, 12, 3, 0);
			gridBagConstraints3.gridy = 1;
			lblVibratoType = new BLabel();
			lblVibratoType.setText("Vibrato Type");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new Insets(12, 3, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridy = 0;
			jLabel1 = new BLabel();
			jLabel1.setText("% (0-100)");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(12, 12, 3, 0);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(12, 12, 3, 0);
			gridBagConstraints.gridy = 0;
			lblVibratoLength = new BLabel();
			lblVibratoLength.setText("Vibrato Length");
			jContentPane = new BPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(lblVibratoLength, gridBagConstraints);
			jContentPane.add(getTxtVibratoLength(), gridBagConstraints1);
			jContentPane.add(jLabel1, gridBagConstraints2);
			jContentPane.add(lblVibratoType, gridBagConstraints3);
			jContentPane.add(getComboVibratoType(), gridBagConstraints4);
			jContentPane.add(getJPanel2(), gridBagConstraints5);
			jContentPane.add(getGroupSelect(), gridBagConstraints31);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.gridy = 0;
			lblSpacer = new BLabel();
			lblSpacer.setPreferredSize(new Dimension(4, 4));
			lblSpacer.setText("");
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.anchor = GridBagConstraints.SOUTHWEST;
			gridBagConstraints52.gridx = 1;
			gridBagConstraints52.gridy = 0;
			gridBagConstraints52.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.anchor = GridBagConstraints.WEST;
			gridBagConstraints42.gridx = 2;
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.insets = new Insets(0, 0, 0, 0);
			jPanel2 = new BPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getBtnOK(), gridBagConstraints42);
			jPanel2.add(getBtnCancel(), gridBagConstraints52);
			jPanel2.add(lblSpacer, gridBagConstraints9);
		}
		return jPanel2;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnOK() {
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
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new BButton();
			btnCancel.setText("Cancel");
			btnCancel.setPreferredSize(new Dimension(100, 29));
		}
		return btnCancel;
	}

	/**
	 * This method initializes txtVibratoLength	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtVibratoLength() {
		if (txtVibratoLength == null) {
			txtVibratoLength = new BTextBox();
			txtVibratoLength.setPreferredSize(new Dimension(70, 19));
		}
		return txtVibratoLength;
	}

	/**
	 * This method initializes comboVibratoType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getComboVibratoType() {
		if (comboVibratoType == null) {
			comboVibratoType = new BComboBox();
			comboVibratoType.setPreferredSize(new Dimension(167, 27));
		}
		return comboVibratoType;
	}

    /**
     * This method initializes groupSelect	
     * 	
     * @return org.kbinani.windows.forms.BGroupBox	
     */
    private BGroupBox getGroupSelect() {
        if (groupSelect == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 2;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.weightx = 1.0D;
            gridBagConstraints8.gridy = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.weightx = 1.0D;
            gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.gridy = 0;
            groupSelect = new BGroupBox();
            groupSelect.setLayout(new GridBagLayout());
            groupSelect.setTitle("Select from");
            groupSelect.add(getRadioVocaloid1(), gridBagConstraints6);
            groupSelect.add(getRadioVocaloid2(), gridBagConstraints7);
            groupSelect.add(getRadioUserDefined(), gridBagConstraints8);
        }
        return groupSelect;
    }

    /**
     * This method initializes radioVocaloid1	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private BRadioButton getRadioVocaloid1() {
        if (radioVocaloid1 == null) {
            radioVocaloid1 = new BRadioButton();
            radioVocaloid1.setText("VOCALOID1");
            radioVocaloid1.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return radioVocaloid1;
    }

    /**
     * This method initializes radioVocaloid2	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private BRadioButton getRadioVocaloid2() {
        if (radioVocaloid2 == null) {
            radioVocaloid2 = new BRadioButton();
            radioVocaloid2.setText("VOCALOID2");
            radioVocaloid2.setSelected(true);
            radioVocaloid2.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return radioVocaloid2;
    }

    /**
     * This method initializes radioUserDefined	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private BRadioButton getRadioUserDefined() {
        if (radioUserDefined == null) {
            radioUserDefined = new BRadioButton();
            radioUserDefined.setText("User defined");
            radioUserDefined.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return radioUserDefined;
    }

    }


/*
 * FormShortcutKeys.cs
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BCheckBox;
import org.kbinani.windows.forms.BComboBox;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BListView;


import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormShortcutKeys extends BDialog {
        /// <summary>
        /// カテゴリーのリスト
        /// </summary>
        private static final String[] mCategories = new String[]{
"menuFile", "menuEdit", "menuVisual", "menuJob", "menuLyric", "menuTrack",
"menuScript", "menuSetting", "menuHelp", ".other" };
        private static int mColumnWidthCommand = 272;
        private static int mColumnWidthShortcutKey = 177;
        private static int mWindowWidth = 541;
        private static int mWindowHeight = 572;

        private TreeMap<String, ValuePair<String, BKeys[]>> mDict;
        private TreeMap<String, ValuePair<String, BKeys[]>> mFirstDict;
        private Vector<String> mFieldName = new Vector<String>();
        private FormMain mMainForm = null;

        /// <summary>
        /// コンストラクタ
        /// </summary>
        /// <param name="dict">メニューアイテムの表示文字列をキーとする，メニューアイテムのフィールド名とショートカットキーのペアを格納したマップ</param>
        public FormShortcutKeys( TreeMap<String, ValuePair<String, BKeys[]>> dict, FormMain main_form )
        {
super();
try {
    initialize();
} catch ( Exception ex ) {
}

mMainForm = main_form;
list.setColumnHeaders( new String[] { gettext( "Command" ), gettext( "Shortcut Key" ) } );
list.setColumnWidth( 0, mColumnWidthCommand );
list.setColumnWidth( 1, mColumnWidthShortcutKey );

applyLanguage();
setResources();

mDict = dict;
comboCategory.setSelectedIndex( 0 );
mFirstDict = new TreeMap<String, ValuePair<String, BKeys[]>>();
copyDict( mDict, mFirstDict );

comboEditKey.removeAllItems();
comboEditKey.addItem( BKeys.None );
// アルファベット順になるように一度配列に入れて並べ替える
int size = AppManager.SHORTCUT_ACCEPTABLE.size();
BKeys[] keys = new BKeys[size];
for ( int i = 0; i < size; i++ ){
    keys[i] = AppManager.SHORTCUT_ACCEPTABLE.get( i );
}
boolean changed = true;
while( changed ){
    changed = false;
    for( int i = 0; i < size - 1; i++ ){
        for( int j = i + 1; j < size; j++ ){
            String itemi = keys[i] + "";
            String itemj = keys[j] + "";
            if( itemi.compareTo( itemj ) > 0 ){
                BKeys t = keys[i];
                keys[i] = keys[j];
                keys[j] = t;
                changed = true;
            }
        }
    }
}
for( BKeys key : keys ){
    comboEditKey.addItem( key );
}
this.setSize( mWindowWidth, mWindowHeight );

registerEventHandlers();
updateList();
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public void applyLanguage()
        {
setTitle( gettext( "Shortcut Config" ) );

btnOK.setText( gettext( "OK" ) );
btnCancel.setText( gettext( "Cancel" ) );
btnRevert.setText( gettext( "Revert" ) );
btnLoadDefault.setText( gettext( "Load Default" ) );

list.setColumnHeaders( new String[] { gettext( "Command" ), gettext( "Shortcut Key" ) } );

labelCategory.setText( gettext( "Category" ) );
int selected = comboCategory.getSelectedIndex();
comboCategory.removeAllItems();
for ( String category : mCategories ) {
    String c = category;
    if ( str.compare( category, "menuFile" ) ) {
        c = gettext( "File" );
    } else if ( str.compare( category, "menuEdit" ) ) {
        c = gettext( "Edit" );
    } else if ( str.compare( category, "menuVisual" ) ) {
        c = gettext( "Visual" );
    } else if ( str.compare( category, "menuJob" ) ) {
        c = gettext( "Job" );
    } else if ( str.compare( category, "menuLyric" ) ) {
        c = gettext( "Lyric" );
    } else if ( str.compare( category, "menuTrack" ) ) {
        c = gettext( "Track" );
    } else if ( str.compare( category, "menuScript" ) ) {
        c = gettext( "Script" );
    } else if ( str.compare( category, "menuSetting" ) ){
        c = gettext( "Setting" );
    } else if ( str.compare( category, "menuHelp" ) ) {
        c = gettext( "Help" );
    } else {
        c = gettext( "Other" );
    }
    comboCategory.addItem( c );
}
if ( comboCategory.getItemCount() <= selected ) {
    selected = comboCategory.getItemCount() - 1;
}
comboCategory.setSelectedIndex( selected );

labelCommand.setText( gettext( "Command" ) );
labelEdit.setText( gettext( "Edit" ) );
labelEditKey.setText( gettext( "Key:" ) );
labelEditModifier.setText( gettext( "Modifier:" ) );
        }

        public TreeMap<String, ValuePair<String, BKeys[]>> getResult()
        {
TreeMap<String, ValuePair<String, BKeys[]>> ret = new TreeMap<String, ValuePair<String, BKeys[]>>();
copyDict( mDict, ret );
return ret;
        }

        private static String gettext( String id )
        {
return Messaging.getMessage( id );
        }

        private static void copyDict( TreeMap<String, ValuePair<String, BKeys[]>> src, TreeMap<String, ValuePair<String, BKeys[]>> dest )
        {
dest.clear();
for ( Iterator<String> itr = src.keySet().iterator(); itr.hasNext(); ) {
    String name = itr.next();
    String key = src.get( name ).getKey();
    BKeys[] values = src.get( name ).getValue();
    Vector<BKeys> cp = new Vector<BKeys>();
    for ( BKeys k : values ) {
        cp.add( k );
    }
    dest.put( name, new ValuePair<String, BKeys[]>( key, cp.toArray( new BKeys[] { } ) ) );
}
        }

        /// <summary>
        /// リストを更新します
        /// </summary>
        private void updateList()
        {
list.selectedIndexChangedEvent.remove( new BEventHandler( this, "list_SelectedIndexChanged" ) );
list.clear();
list.selectedIndexChangedEvent.add( new BEventHandler( this, "list_SelectedIndexChanged" ) );
mFieldName.clear();

// 現在のカテゴリーを取得
int selected = comboCategory.getSelectedIndex();
if ( selected < 0 ) {
    selected = 0;
}
String category = mCategories[selected];

// 現在のカテゴリーに合致するものについてのみ，リストに追加
for ( Iterator<String> itr = mDict.keySet().iterator(); itr.hasNext(); ) {
    String display = itr.next();
    ValuePair<String, BKeys[]> item = mDict.get( display );
    String field_name = item.getKey();
    BKeys[] keys = item.getValue();
    boolean add_this_one = false;
    if ( str.compare( category, ".other" ) ) {
        add_this_one = true;
        for ( int i = 0; i < mCategories.length; i++ ) {
            String c = mCategories[i];
            if ( str.compare( c, ".other" ) ) {
                continue;
            }
            if ( str.startsWith( field_name, c ) ) {
                add_this_one = false;
                break;
            }
        }
    } else {
        if ( str.startsWith( field_name, category ) ) {
            add_this_one = true;
        }
    }
    if ( add_this_one ) {
         list.addRow( new String[] { display, Utility.getShortcutDisplayString( keys ) } );
         mFieldName.add( field_name );
    }
}

updateColor();
//applyLanguage();
        }

        /// <summary>
        /// リストアイテムの背景色を更新します．
        /// 2つ以上のメニューに対して同じショートカットが割り当てられた場合に警告色で表示する．
        /// </summary>
        private void updateColor()
        {
int size = list.getItemCountRow();
for ( int i = 0; i < size; i++ ) {
    //BListViewItem list_item = list.getItemAt( i );
    String field_name = mFieldName.get( i );
    String key_display = list.getItemAt( i, 1 );
    if ( str.compare( key_display, "" ) ){
        // ショートカットキーが割り当てられていないのでスルー
        list.setRowBackColor( i, java.awt.Color.white );
        continue;
    }

    boolean found = false;
    for ( Iterator<String> itr = mDict.keySet().iterator(); itr.hasNext(); ) {
        String display1 = itr.next();
        ValuePair<String, BKeys[]> item1 = mDict.get( display1 );
        String field_name1 = item1.getKey();
        if ( str.compare( field_name, field_name1 ) ) {
            // 自分自身なのでスルー
            continue;
        }
        BKeys[] keys1 = item1.getValue();
        String key_display1 = Utility.getShortcutDisplayString( keys1 );
        if ( str.compare( key_display, key_display1 ) ) {
            // 同じキーが割り当てられてる！！
            found = true;
            break;
        }
    }

    // 背景色を変える
    if ( found ) {
        list.setRowBackColor( i, java.awt.Color.yellow );
    } else {
        list.setRowBackColor(  i, java.awt.Color.white );
    }
}
        }

        private void registerEventHandlers()
        {
//this.list.keyDownEvent.add( new BKeyEventHandler( this, "list_KeyDown" ) );
btnLoadDefault.clickEvent.add( new BEventHandler( this, "btnLoadDefault_Click" ) );
btnRevert.clickEvent.add( new BEventHandler( this, "btnRevert_Click" ) );
this.formClosingEvent.add( new BFormClosingEventHandler( this, "FormShortcutKeys_FormClosing" ) );
btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
btnCancel.clickEvent.add( new BEventHandler( this, "btnCancel_Click" ) );
comboCategory.selectedIndexChangedEvent.add( new BEventHandler( this, "comboCategory_SelectedIndexChanged" ) );
list.selectedIndexChangedEvent.add( new BEventHandler( this, "list_SelectedIndexChanged" ) );
this.sizeChangedEvent.add( new BEventHandler( this, "FormShortcutKeys_SizeChanged" ) );
reRegisterHandlers();
        }

        private void unRegisterHandlers()
        {
comboEditKey.selectedIndexChangedEvent.remove( new BEventHandler( this, "comboEditKey_SelectedIndexChanged" ) );
checkCommand.checkedChangedEvent.remove( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkShift.checkedChangedEvent.remove( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkControl.checkedChangedEvent.remove( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkOption.checkedChangedEvent.remove( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
        }
        
        private void reRegisterHandlers()
        {
comboEditKey.selectedIndexChangedEvent.add( new BEventHandler( this, "comboEditKey_SelectedIndexChanged" ) );
checkCommand.checkedChangedEvent.add( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkShift.checkedChangedEvent.add( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkControl.checkedChangedEvent.add( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
checkOption.checkedChangedEvent.add( new BEventHandler( this, "handleModifier_CheckedChanged" ) );
        }

        private void setResources()
        {
        }

        public void FormShortcutKeys_SizeChanged( Object sender, BEventArgs e )
        {
mWindowWidth = getWidth();
mWindowHeight = getHeight();
        }
        
        public void handleModifier_CheckedChanged( Object sender, BEventArgs e )
        {
updateSelectionKeys();
        }

        public void comboEditKey_SelectedIndexChanged( Object sender, BEventArgs e )
        {
updateSelectionKeys();
        }
        
        /// <summary>
        /// 現在選択中のコマンドのショートカットキーを，comboEditKey, 
        /// checkCommand, checkShift, checkControl, checkControlの状態にあわせて変更します．
        /// </summary>
        private void updateSelectionKeys()
        {
int indx = comboEditKey.getSelectedIndex();
if( indx < 0 ){
    return;
}
int indx_row = list.getSelectedRow();
if( indx_row < 0 ){
    return;
}
BKeys key = (BKeys)comboEditKey.getItemAt( indx );
String display = list.getItemAt( indx_row, 0 );
if ( !mDict.containsKey( display ) ) {
    return;
}
Vector<BKeys> capturelist = new Vector<BKeys>();
if( key != BKeys.None ){
    capturelist.add( key );
    if( checkCommand.isSelected() ){
        capturelist.add( BKeys.Menu );
    }
    if( checkShift.isSelected() ){
        capturelist.add( BKeys.Shift );
    }
    if( checkControl.isSelected() ){
        capturelist.add( BKeys.Control );
    }
    if( checkOption.isSelected() ){
        capturelist.add( BKeys.Alt );
    }
}
BKeys[] keys = capturelist.toArray( new BKeys[] { } );
mDict.get( display ).setValue( keys );
list.setItemAt( indx_row, 1, Utility.getShortcutDisplayString( keys ) ); 
        } 

        public void list_SelectedIndexChanged( Object sender, BEventArgs e )
        {
int indx = list.getSelectedRow();
if( indx < 0 ){
    return;
}
String display = list.getItemAt( indx, 0 );
if( !mDict.containsKey( display ) ){
    return;
}
unRegisterHandlers();
ValuePair<String, BKeys[]> item = mDict.get( display );
BKeys[] keys = item.getValue();
Vector<BKeys> vkeys = new Vector<BKeys>( Arrays.asList( keys ) );
checkCommand.setSelected( vkeys.contains( BKeys.Menu ) );
checkShift.setSelected( vkeys.contains( BKeys.Shift ) );
checkControl.setSelected( vkeys.contains( BKeys.Control ) );
checkOption.setSelected( vkeys.contains( BKeys.Alt ) );
int size = comboEditKey.getItemCount();
comboEditKey.setSelectedIndex( -1 );
for( int i = 0; i < size; i++ ){
    BKeys k = (BKeys)comboEditKey.getItemAt( i );
    if( vkeys.contains( k ) ){
        comboEditKey.setSelectedIndex( i );
        break;
    }
}
reRegisterHandlers();
        }
        
        public void comboCategory_SelectedIndexChanged( Object sender, BEventArgs e )
        {
int selected = comboCategory.getSelectedIndex();
if ( selected < 0 ) {
    comboCategory.setSelectedIndex( 0 );
    //updateList();
    return;
}
updateList();
        }

        public void btnRevert_Click( Object sender, BEventArgs e )
        {
copyDict( mFirstDict, mDict );
updateList();
        }

        public void btnLoadDefault_Click( Object sender, BEventArgs e )
        {
Vector<ValuePairOfStringArrayOfKeys> defaults = mMainForm.getDefaultShortcutKeys();
for ( int i = 0; i < defaults.size(); i++ ) {
    String name = defaults.get( i ).Key;
    BKeys[] keys = defaults.get( i ).Value;
    for ( Iterator<String> itr = mDict.keySet().iterator(); itr.hasNext(); ) {
        String display = itr.next();
        if ( name.equals( mDict.get( display ).getKey() ) ) {
            mDict.get( display ).setValue( keys );
            break;
        }
    }
}
updateList();
        }

        public void FormShortcutKeys_FormClosing( Object sender, BFormClosingEventArgs e )
        {
mColumnWidthCommand = list.getColumnWidth( 0 );
mColumnWidthShortcutKey = list.getColumnWidth( 1 );
        }

        public void btnCancel_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
        }

 
    private static final long serialVersionUID = 2743132471603994391L;
    private JPanel jPanel = null;
    private JPanel jPanel3 = null;
    private BButton btnLoadDefault = null;
    private BButton btnRevert = null;
    private JPanel jPanel31 = null;
    private BButton btnCancel = null;
    private BButton btnOK = null;
    private BListView list = null;
    private BLabel labelCategory = null;
    private BComboBox comboCategory = null;
    private JScrollPane jScrollPane = null;
    private BComboBox comboEditKey = null;
    private BLabel labelCommand = null;
    private BLabel labelEdit = null;
    private JPanel panelEdit = null;
    private BLabel labelEditKey = null;
    private BLabel labelEditModifier = null;
    private BCheckBox checkCommand = null;
    private BCheckBox checkShift = null;
    private BCheckBox checkControl = null;
    private BCheckBox checkOption = null;
    private BLabel labelEditKey1 = null;
    private BLabel labelEditKey11 = null;

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(541, 572));
        this.setTitle("Shortcut Config");
        this.setContentPane(getJPanel());
        setCancelButton( btnCancel );
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.gridx = 0;
            gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints41.insets = new Insets(3, 36, 3, 12);
            gridBagConstraints41.gridy = 5;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 0;
            gridBagConstraints31.insets = new Insets(3, 12, 3, 12);
            gridBagConstraints31.anchor = GridBagConstraints.WEST;
            gridBagConstraints31.gridy = 4;
            labelEdit = new BLabel();
            labelEdit.setText("Edit");
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.insets = new Insets(6, 12, 3, 12);
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.gridy = 2;
            labelCommand = new BLabel();
            labelCommand.setText("Command");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 3;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.weighty = 1.0;
            gridBagConstraints11.insets = new Insets(3, 36, 6, 17);
            gridBagConstraints11.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.insets = new Insets(3, 33, 3, 12);
            gridBagConstraints3.weighty = 0.0D;
            gridBagConstraints3.gridx = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(12, 12, 3, 12);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridy = 0;
            labelCategory = new BLabel();
            labelCategory.setText("Category");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.anchor = GridBagConstraints.EAST;
            gridBagConstraints2.insets = new Insets(0, 0, 16, 12);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 7;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new Insets(24, 10, 12, 0);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 6;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(labelCategory, gridBagConstraints);
            jPanel.add(getComboCategory(), gridBagConstraints3);
            jPanel.add(labelCommand, gridBagConstraints21);
            jPanel.add(getJScrollPane(), gridBagConstraints11);
            jPanel.add(getJPanel3(), gridBagConstraints1);
            jPanel.add(getJPanel31(), gridBagConstraints2);
            jPanel.add(labelEdit, gridBagConstraints31);
            jPanel.add(getPanelEdit(), gridBagConstraints41);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel3	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 3;
            gridBagConstraints12.fill = GridBagConstraints.NONE;
            gridBagConstraints12.weightx = 1.0D;
            gridBagConstraints12.gridy = 0;
            labelEditKey1 = new BLabel();
            labelEditKey1.setText("");
            labelEditKey1.setPreferredSize(new Dimension(4, 4));
            GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
            gridBagConstraints111.insets = new Insets(0, 0, 0, 16);
            gridBagConstraints111.gridy = 0;
            gridBagConstraints111.anchor = GridBagConstraints.WEST;
            gridBagConstraints111.gridx = 0;
            GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
            gridBagConstraints121.insets = new Insets(0, 0, 0, 16);
            gridBagConstraints121.gridy = 0;
            gridBagConstraints121.anchor = GridBagConstraints.WEST;
            gridBagConstraints121.gridx = 2;
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.setPreferredSize(new Dimension(239, 40));
            jPanel3.add(getBtnLoadDefault(), gridBagConstraints121);
            jPanel3.add(getBtnRevert(), gridBagConstraints111);
            jPanel3.add(labelEditKey1, gridBagConstraints12);
        }
        return jPanel3;
    }

    /**
     * This method initializes btnLoadDefault	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getBtnLoadDefault() {
        if (btnLoadDefault == null) {
            btnLoadDefault = new BButton();
            btnLoadDefault.setText("Load Default");
        }
        return btnLoadDefault;
    }

    /**
     * This method initializes btnRevert	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getBtnRevert() {
        if (btnRevert == null) {
            btnRevert = new BButton();
            btnRevert.setText("Revert");
        }
        return btnRevert;
    }

    /**
     * This method initializes jPanel31	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel31() {
        if (jPanel31 == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.weightx = 1.0D;
            gridBagConstraints13.gridy = 0;
            labelEditKey11 = new BLabel();
            labelEditKey11.setPreferredSize(new Dimension(4, 4));
            labelEditKey11.setText("");
            GridBagConstraints gridBagConstraints1111 = new GridBagConstraints();
            gridBagConstraints1111.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints1111.gridy = 0;
            gridBagConstraints1111.gridx = 2;
            GridBagConstraints gridBagConstraints1211 = new GridBagConstraints();
            gridBagConstraints1211.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints1211.gridy = 0;
            gridBagConstraints1211.fill = GridBagConstraints.NONE;
            gridBagConstraints1211.gridx = 1;
            jPanel31 = new JPanel();
            jPanel31.setLayout(new GridBagLayout());
            jPanel31.setPreferredSize(new Dimension(220, 40));
            jPanel31.add(getBtnCancel(), gridBagConstraints1211);
            jPanel31.add(getBtnOK(), gridBagConstraints1111);
            jPanel31.add(labelEditKey11, gridBagConstraints13);
        }
        return jPanel31;
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
            btnCancel.setPreferredSize(new Dimension(100, 29));
        }
        return btnCancel;
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
            btnOK.setPreferredSize(new Dimension(100, 29));
        }
        return btnOK;
    }

    /**
     * This method initializes list	
     * 	
     * @return javax.swing.JTable	
     */
    private BListView getList() {
        if (list == null) {
            list = new BListView();
            list.setCheckBoxes(false);
            list.setShowGrid(false);
        }
        return list;
    }

    /**
     * This method initializes comboCategory	
     * 	
     * @return org.kbinani.windows.forms.BComboBox	
     */
    private BComboBox getComboCategory() {
        if (comboCategory == null) {
            comboCategory = new BComboBox();
            comboCategory.setPreferredSize(new Dimension(167, 27));
            comboCategory.setMaximumRowCount(10);
        }
        return comboCategory;
    }

    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setPreferredSize(new Dimension(100, 100));
            jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setViewportView(getList());
        }
        return jScrollPane;
    }

    /**
     * This method initializes comboEditKey	
     * 	
     * @return org.kbinani.windows.forms.BComboBox	
     */
    private BComboBox getComboEditKey() {
        if (comboEditKey == null) {
            comboEditKey = new BComboBox();
            comboEditKey.setPreferredSize(new Dimension(167, 27));
        }
        return comboEditKey;
    }

    /**
     * This method initializes panelEdit	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getPanelEdit() {
        if (panelEdit == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 4;
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            gridBagConstraints10.gridy = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 3;
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridy = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 2;
            gridBagConstraints8.anchor = GridBagConstraints.WEST;
            gridBagConstraints8.gridy = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.insets = new Insets(0, 6, 0, 0);
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.gridy = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridy = 1;
            labelEditModifier = new BLabel();
            labelEditModifier.setText("Modifier:");
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.gridwidth = 4;
            gridBagConstraints5.insets = new Insets(3, 6, 3, 0);
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = GridBagConstraints.WEST;
            gridBagConstraints4.gridy = 0;
            labelEditKey = new BLabel();
            labelEditKey.setText("Key:");
            panelEdit = new JPanel();
            panelEdit.setLayout(new GridBagLayout());
            panelEdit.add(labelEditKey, gridBagConstraints4);
            panelEdit.add(getComboEditKey(), gridBagConstraints5);
            panelEdit.add(labelEditModifier, gridBagConstraints6);
            panelEdit.add(getCheckCommand(), gridBagConstraints7);
            panelEdit.add(getCheckShift(), gridBagConstraints8);
            panelEdit.add(getCheckControl(), gridBagConstraints9);
            panelEdit.add(getCheckOption(), gridBagConstraints10);
        }
        return panelEdit;
    }

    /**
     * This method initializes checkCommand	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckCommand() {
        if (checkCommand == null) {
            checkCommand = new BCheckBox();
            checkCommand.setText("command");
        }
        return checkCommand;
    }

    /**
     * This method initializes checkShift	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckShift() {
        if (checkShift == null) {
            checkShift = new BCheckBox();
            checkShift.setText("shift");
        }
        return checkShift;
    }

    /**
     * This method initializes checkControl	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckControl() {
        if (checkControl == null) {
            checkControl = new BCheckBox();
            checkControl.setText("control");
        }
        return checkControl;
    }

    /**
     * This method initializes checkOption	
     * 	
     * @return org.kbinani.windows.forms.BCheckBox	
     */
    private BCheckBox getCheckOption() {
        if (checkOption == null) {
            checkOption = new BCheckBox();
            checkOption.setText("option");
        }
        return checkOption;
    }


    }


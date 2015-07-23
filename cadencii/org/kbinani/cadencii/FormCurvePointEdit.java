/*
 * FormCurvePointEdit.cs
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BTextBox;


import java.util.*;
import java.awt.event.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;

    public class FormCurvePointEdit extends BDialog {
        private long m_editing_id = -1;
        private CurveType m_curve;
        private boolean m_changed = false;
        private FormMain mMainWindow = null;

        public FormCurvePointEdit( FormMain main_window, long editing_id, CurveType curve )
        {
super();
initialize();
mMainWindow = main_window;
registerEventHandlers();
setResources();
applyLanguage();
m_editing_id = editing_id;
m_curve = curve;

VsqBPPairSearchContext context = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( m_curve.getName() ).findElement( m_editing_id );
txtDataPointClock.setText( context.clock + "" );
txtDataPointValue.setText( context.point.value + "" );
txtDataPointValue.selectAll();

btnUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
btnRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
        }

        public void applyLanguage()
        {
setTitle( gettext( "Edit Value" ) );
lblDataPointClock.setText( gettext( "Clock" ) );
lblDataPointValue.setText( gettext( "Value" ) );
btnApply.setText( gettext( "Apply" ) );
btnExit.setText( gettext( "Exit" ) );
        }

        private String gettext( String id )
        {
return Messaging.getMessage( id );
        }

        private void applyValue( boolean mode_clock )
        {
if ( !m_changed ) {
    return;
}
int value = m_curve.getDefault();
try {
    value = str.toi( txtDataPointValue.getText() );
} catch ( Exception ex ) {
    Logger.write( FormCurvePointEdit.class + ".applyValue; ex=" + ex + "\n" );
    return;
}
if ( value < m_curve.getMinimum() ) {
    value = m_curve.getMinimum();
} else if ( m_curve.getMaximum() < value ) {
    value = m_curve.getMaximum();
}

int clock = 0;
try {
    clock = str.toi( txtDataPointClock.getText() );
} catch ( Exception ex ) {
    Logger.write( FormCurvePointEdit.class + ".applyValue; ex=" + ex + "\n" );
    return;
}

int selected = AppManager.getSelected();
VsqTrack vsq_track = AppManager.getVsqFile().Track.get( selected );
VsqBPList src = vsq_track.getCurve( m_curve.getName() );
VsqBPList list = (VsqBPList)src.clone();

VsqBPPairSearchContext context = list.findElement( m_editing_id );
list.move( context.clock, clock, value );
CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandTrackCurveReplace( selected,
                                                                                        m_curve.getName(),
                                                                                        list ) );
EditedZone zone = new EditedZone();
Utility.compareList( zone, new VsqBPListComparisonContext( list, src ) );
Vector<EditedZoneUnit> zoneUnits = new Vector<EditedZoneUnit>();
for ( Iterator<EditedZoneUnit> itr = zone.iterator(); itr.hasNext(); ) {
    zoneUnits.add( itr.next() );
}
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );

txtDataPointClock.setText( clock + "" );
txtDataPointValue.setText( value + "" );

if ( mMainWindow != null ) {
    mMainWindow.setEdited( true );
    mMainWindow.ensureVisible( clock );
    mMainWindow.refreshScreen();
}

if ( mode_clock ) {
    txtDataPointClock.selectAll();
} else {
    txtDataPointValue.selectAll();
}

btnUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
btnRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
m_changed = false;
        }


        private void setResources()
        {
        }

        private void registerEventHandlers()
        {
btnForward.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnBackward.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnBackward2.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnForward2.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnApply.clickEvent.add( new BEventHandler( this, "btnApply_Click" ) );
txtDataPointClock.textChangedEvent.add( new BEventHandler( this, "commonTextBox_TextChanged" ) );
txtDataPointClock.keyUpEvent.add( new BKeyEventHandler( this, "commonTextBox_KeyUp" ) );
txtDataPointValue.textChangedEvent.add( new BEventHandler( this, "commonTextBox_TextChanged" ) );
txtDataPointValue.keyUpEvent.add( new BKeyEventHandler( this, "commonTextBox_KeyUp" ) );
btnBackward3.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnForward3.clickEvent.add( new BEventHandler( this, "commonButton_Click" ) );
btnUndo.clickEvent.add( new BEventHandler( this, "handleUndoRedo_Click" ) );
btnRedo.clickEvent.add( new BEventHandler( this, "handleUndoRedo_Click" ) );
btnExit.clickEvent.add( new BEventHandler( this, "btnExit_Click" ) );
        }

        public void commonTextBox_KeyUp( Object sender, BKeyEventArgs e )
        {
if ( (e.KeyValue & KeyEvent.VK_ENTER) != KeyEvent.VK_ENTER ) {
    return;
}
applyValue( (sender == txtDataPointClock) );
        }

        public void commonButton_Click( Object sender, BEventArgs e )
        {
VsqBPList list = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( m_curve.getName() );
VsqBPPairSearchContext search = list.findElement( m_editing_id );
int index = search.index;
if ( sender == btnForward ) {
    index++;
} else if ( sender == btnBackward ) {
    index--;
} else if ( sender == btnBackward2 ) {
    index -= 5;
} else if ( sender == btnForward2 ) {
    index += 5;
} else if ( sender == btnForward3 ) {
    index += 10;
} else if ( sender == btnBackward3 ) {
    index -= 10;
}

if ( index < 0 ) {
    index = 0;
}

if ( list.size() <= index ) {
    index = list.size() - 1;
}

VsqBPPair bp = list.getElementB( index );
m_editing_id = bp.id;
int clock = list.getKeyClock( index );
txtDataPointClock.textChangedEvent.remove( new BEventHandler( this, "commonTextBox_TextChanged" ) );
txtDataPointValue.textChangedEvent.remove( new BEventHandler( this, "commonTextBox_TextChanged" ) );
txtDataPointClock.setText( clock + "" );
txtDataPointValue.setText( bp.value + "" );
txtDataPointClock.textChangedEvent.add( new BEventHandler( this, "commonTextBox_TextChanged" ) );
txtDataPointValue.textChangedEvent.add( new BEventHandler( this, "commonTextBox_TextChanged" ) );

txtDataPointValue.requestFocus();
txtDataPointValue.selectAll();

AppManager.itemSelection.clearPoint();
AppManager.itemSelection.addPoint( m_curve, bp.id );
if ( mMainWindow != null ) {
    mMainWindow.ensureVisible( clock );
    mMainWindow.refreshScreen();
}
        }

        public void btnApply_Click( Object sender, BEventArgs e )
        {
applyValue( true );
        }

        public void commonTextBox_TextChanged( Object sender, BEventArgs e )
        {
m_changed = true;
        }

        public void handleUndoRedo_Click( Object sender, BEventArgs e )
        {
if ( sender == btnUndo ) {
    AppManager.undo();
} else if ( sender == btnRedo ) {
    AppManager.redo();
} else {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
boolean exists = false;
if ( vsq != null ) {
    exists = vsq.Track.get( AppManager.getSelected() ).getCurve( m_curve.getName() ).findElement( m_editing_id ).index >= 0;
}
txtDataPointClock.setEnabled( exists );
txtDataPointValue.setEnabled( exists );
btnApply.setEnabled( exists );
btnBackward.setEnabled( exists );
btnBackward2.setEnabled( exists );
btnBackward3.setEnabled( exists );
btnForward.setEnabled( exists );
btnForward2.setEnabled( exists );
btnForward3.setEnabled( exists );

if ( exists ) {
    AppManager.itemSelection.clearPoint();
    AppManager.itemSelection.addPoint( m_curve, m_editing_id );
}

if ( mMainWindow != null ) {
    mMainWindow.updateDrawObjectList();
    mMainWindow.refreshScreen();
}
btnUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
btnRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
        }

        public void btnExit_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }

    
    private static final long serialVersionUID = 1L;
    private JPanel jPanel = null;
    private BButton btnBackward3 = null;
    private BButton btnBackward2 = null;
    private BButton btnBackward = null;
    private BButton btnForward = null;
    private BButton btnForward2 = null;
    private BButton btnForward3 = null;
    private JPanel jPanel1 = null;
    private JLabel lblDataPointValue = null;
    private BTextBox txtDataPointValue = null;
    private BButton btnUndo = null;
    private JLabel lblDataPointClock = null;
    private BTextBox txtDataPointClock = null;
    private BButton btnRedo = null;
    private JPanel jPanel3 = null;
    private BButton btnExit = null;
    private BButton btnApply = null;
    private BLabel lblRightValue = null;

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(328, 195));
        this.setTitle("FormCurvePointEdit");
        this.setContentPane(getJPanel1());
    	setCancelButton( btnExit );
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 5;
            gridBagConstraints5.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 4;
            gridBagConstraints4.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 3;
            gridBagConstraints3.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(0, 1, 0, 1);
            gridBagConstraints.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getBtnBackward3(), gridBagConstraints);
            jPanel.add(getBtnBackward2(), gridBagConstraints1);
            jPanel.add(getBtnBackward(), gridBagConstraints2);
            jPanel.add(getBtnForward(), gridBagConstraints3);
            jPanel.add(getBtnForward2(), gridBagConstraints4);
            jPanel.add(getBtnForward3(), gridBagConstraints5);
        }
        return jPanel;
    }

    /**
     * This method initializes btnBackward3	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnBackward3() {
        if (btnBackward3 == null) {
            btnBackward3 = new BButton();
            btnBackward3.setText("<10");
            btnBackward3.setPreferredSize(new Dimension(55, 29));
        }
        return btnBackward3;
    }

    /**
     * This method initializes btnBackward2	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnBackward2() {
        if (btnBackward2 == null) {
            btnBackward2 = new BButton();
            btnBackward2.setText("<5");
            btnBackward2.setPreferredSize(new Dimension(48, 29));
        }
        return btnBackward2;
    }

    /**
     * This method initializes btnBackward	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnBackward() {
        if (btnBackward == null) {
            btnBackward = new BButton();
            btnBackward.setText("<");
            btnBackward.setPreferredSize(new Dimension(41, 29));
        }
        return btnBackward;
    }

    /**
     * This method initializes btnForward	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnForward() {
        if (btnForward == null) {
            btnForward = new BButton();
            btnForward.setText(">");
            btnForward.setPreferredSize(new Dimension(41, 29));
        }
        return btnForward;
    }

    /**
     * This method initializes btnForward2	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnForward2() {
        if (btnForward2 == null) {
            btnForward2 = new BButton();
            btnForward2.setText("5>");
            btnForward2.setPreferredSize(new Dimension(48, 29));
        }
        return btnForward2;
    }

    /**
     * This method initializes btnForward3	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnForward3() {
        if (btnForward3 == null) {
            btnForward3 = new BButton();
            btnForward3.setText("10>");
            btnForward3.setPreferredSize(new Dimension(55, 29));
        }
        return btnForward3;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridwidth = 3;
            gridBagConstraints13.anchor = GridBagConstraints.EAST;
            gridBagConstraints13.insets = new Insets(12, 0, 12, 0);
            gridBagConstraints13.weighty = 1.0D;
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 3;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.insets = new Insets(4, 0, 0, 12);
            gridBagConstraints12.gridy = 2;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 2;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new Insets(4, 12, 0, 0);
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = GridBagConstraints.NORTHEAST;
            gridBagConstraints10.insets = new Insets(4, 0, 0, 0);
            gridBagConstraints10.gridy = 2;
            lblDataPointClock = new JLabel();
            lblDataPointClock.setText("Clock");
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 2;
            gridBagConstraints9.insets = new Insets(4, 0, 0, 12);
            gridBagConstraints9.gridy = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridwidth = 3;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.insets = new Insets(12, 0, 12, 0);
            gridBagConstraints8.gridx = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.insets = new Insets(4, 12, 0, 0);
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = GridBagConstraints.EAST;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.insets = new Insets(4, 0, 0, 0);
            gridBagConstraints6.gridy = 1;
            lblDataPointValue = new JLabel();
            lblDataPointValue.setText("Value");
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(getJPanel(), gridBagConstraints8);
            jPanel1.add(lblDataPointValue, gridBagConstraints6);
            jPanel1.add(getTxtDataPointValue(), gridBagConstraints7);
            jPanel1.add(getBtnUndo(), gridBagConstraints9);
            jPanel1.add(lblDataPointClock, gridBagConstraints10);
            jPanel1.add(getTxtDataPointClock(), gridBagConstraints11);
            jPanel1.add(getBtnRedo(), gridBagConstraints12);
            jPanel1.add(getJPanel3(), gridBagConstraints13);
        }
        return jPanel1;
    }

    /**
     * This method initializes txtDataPointValue	
     * 	
     * @return javax.swing.JTextField	
     */
    private BTextBox getTxtDataPointValue() {
        if (txtDataPointValue == null) {
            txtDataPointValue = new BTextBox();
            txtDataPointValue.setPreferredSize(new Dimension(71, 20));
        }
        return txtDataPointValue;
    }

    /**
     * This method initializes btnUndo	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnUndo() {
        if (btnUndo == null) {
            btnUndo = new BButton();
            btnUndo.setText("undo");
            btnUndo.setPreferredSize(new Dimension(63, 29));
        }
        return btnUndo;
    }

    /**
     * This method initializes txtDataPointClock	
     * 	
     * @return javax.swing.JTextField	
     */
    private BTextBox getTxtDataPointClock() {
        if (txtDataPointClock == null) {
            txtDataPointClock = new BTextBox();
            txtDataPointClock.setPreferredSize(new Dimension(71, 20));
        }
        return txtDataPointClock;
    }

    /**
     * This method initializes btnRedo	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnRedo() {
        if (btnRedo == null) {
            btnRedo = new BButton();
            btnRedo.setText("redo");
            btnRedo.setPreferredSize(new Dimension(63, 29));
        }
        return btnRedo;
    }

    /**
     * This method initializes jPanel3	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.weightx = 1.0D;
            gridBagConstraints14.gridy = 0;
            lblRightValue = new BLabel();
            lblRightValue.setText("");
            lblRightValue.setPreferredSize(new Dimension(4, 4));
            GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
            gridBagConstraints111.insets = new Insets(0, 0, 0, 12);
            gridBagConstraints111.gridy = 0;
            gridBagConstraints111.gridx = 2;
            GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
            gridBagConstraints121.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints121.gridy = 0;
            gridBagConstraints121.gridx = 1;
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(getBtnExit(), gridBagConstraints121);
            jPanel3.add(getBtnApply(), gridBagConstraints111);
            jPanel3.add(lblRightValue, gridBagConstraints14);
        }
        return jPanel3;
    }

    /**
     * This method initializes btnExit	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getBtnExit() {
        if (btnExit == null) {
            btnExit = new BButton();
            btnExit.setText("Exit");
            btnExit.setPreferredSize(new Dimension(100, 29));
        }
        return btnExit;
    }

    /**
     * This method initializes btnApply	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getBtnApply() {
        if (btnApply == null) {
            btnApply = new BButton();
            btnApply.setText("Apply");
            btnApply.setPreferredSize(new Dimension(100, 29));
        }
        return btnApply;
    }


    }


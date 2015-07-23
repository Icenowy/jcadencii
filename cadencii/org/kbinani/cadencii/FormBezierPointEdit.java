/*
 * FormBezierPointEdit.cs
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
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BCheckBox;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BGroupBox;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BTextBox;


import java.awt.*;
import java.util.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormBezierPointEdit extends BDialog {
        private BezierPoint m_point;
        private int m_min;
        private int m_max;
        /// <summary>
        /// 移動ボタンでデータ点または制御点を動かすためにマウスを強制的に動かす直前の，スクリーン上のマウス位置
        /// </summary>
        private Point m_last_mouse_global_location;
        private TrackSelector m_parent;
        private boolean m_btn_datapoint_downed = false;
        private double m_min_opacity = 0.4;
        private CurveType m_curve_type;
        private int m_track;
        private int m_chain_id = -1;
        private int m_point_id = -1;
        private BezierPickedSide m_picked_side = BezierPickedSide.BASE;
        /// <summary>
        /// 移動ボタンでデータ点または制御点を動かすためにマウスを強制的に動かした直後の，スクリーン上のマウス位置
        /// </summary>
        private Point mScreenMouseDownLocation;

        public FormBezierPointEdit( TrackSelector parent,
                        CurveType curve_type,
                        int selected_chain_id,
                        int selected_point_id )
        {
super();
initialize();
registerEventHandlers();
setResources();
applyLanguage();
m_parent = parent;
m_curve_type = curve_type;
m_track = AppManager.getSelected();
m_chain_id = selected_chain_id;
m_point_id = selected_point_id;
boolean found = false;
VsqFileEx vsq = AppManager.getVsqFile();
BezierCurves attached = vsq.AttachedCurves.get( m_track - 1 );
Vector<BezierChain> chains = attached.get( m_curve_type );
for ( int i = 0; i < chains.size(); i++ ) {
    if ( chains.get( i ).id == m_chain_id ) {
        found = true;
        break;
    }
}
if ( !found ) {
    return;
}
boolean smooth = false;
for ( Iterator<BezierPoint> itr = attached.getBezierChain( m_curve_type, m_chain_id ).points.iterator(); itr.hasNext(); ) {
    BezierPoint bp = itr.next();
    if ( bp.getID() == m_point_id ) {
        m_point = bp;
        smooth = 
            (bp.getControlLeftType() != BezierControlType.None) ||
            (bp.getControlRightType() != BezierControlType.None);
        break;
    }
}
updateStatus();
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public void applyLanguage()
        {
setTitle( _( "Edit Bezier Data Point" ) );

groupDataPoint.setTitle( _( "Data Poin" ) );
lblDataPointClock.setText( _( "Clock" ) );
lblDataPointValue.setText( _( "Value" ) );

groupLeft.setTitle( _( "Left Control Point" ) );
lblLeftClock.setText( _( "Clock" ) );
lblLeftValue.setText( _( "Value" ) );

groupRight.setTitle( _( "Right Control Point" ) );
lblRightClock.setText( _( "Clock" ) );
lblRightValue.setText( _( "Value" ) );

chkEnableSmooth.setText( _( "Smooth" ) );
        }

        private void updateStatus()
        {
txtDataPointClock.setText( m_point.getBase().getX() + "" );
txtDataPointValue.setText( m_point.getBase().getY() + "" );
txtLeftClock.setText( ((int)(m_point.getBase().getX() + m_point.controlLeft.getX())) + "" );
txtLeftValue.setText( ((int)(m_point.getBase().getY() + m_point.controlLeft.getY())) + "" );
txtRightClock.setText( ((int)(m_point.getBase().getX() + m_point.controlRight.getX())) + "" );
txtRightValue.setText( ((int)(m_point.getBase().getY() + m_point.controlRight.getY())) + "" );
boolean smooth = 
    (m_point.getControlLeftType() != BezierControlType.None) ||
    (m_point.getControlRightType() != BezierControlType.None);
chkEnableSmooth.setSelected( smooth );
btnLeft.setEnabled( smooth );
btnRight.setEnabled( smooth );
m_min = m_curve_type.getMinimum();
m_max = m_curve_type.getMaximum();
        }

        private static String _( String message )
        {
return Messaging.getMessage( message );
        }

        private void registerEventHandlers()
        {
btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
btnCancel.clickEvent.add( new BEventHandler( this, "btnCancel_Click" ) );
chkEnableSmooth.checkedChangedEvent.add( new BEventHandler( this, "chkEnableSmooth_CheckedChanged" ) );
btnLeft.mouseMoveEvent.add( new BMouseEventHandler( this, "common_MouseMove" ) );
btnLeft.mouseDownEvent.add( new BMouseEventHandler( this, "handleOperationButtonMouseDown" ) );
btnLeft.mouseUpEvent.add( new BMouseEventHandler( this, "common_MouseUp" ) );
btnDataPoint.mouseMoveEvent.add( new BMouseEventHandler( this, "common_MouseMove" ) );
btnDataPoint.mouseDownEvent.add( new BMouseEventHandler( this, "handleOperationButtonMouseDown" ) );
btnDataPoint.mouseUpEvent.add( new BMouseEventHandler( this, "common_MouseUp" ) );
btnRight.mouseMoveEvent.add( new BMouseEventHandler( this, "common_MouseMove" ) );
btnRight.mouseDownEvent.add( new BMouseEventHandler( this, "handleOperationButtonMouseDown" ) );
btnRight.mouseUpEvent.add( new BMouseEventHandler( this, "common_MouseUp" ) );
btnBackward.clickEvent.add( new BEventHandler( this, "handleMoveButtonClick" ) );
btnForward.clickEvent.add( new BEventHandler( this, "handleMoveButtonClick" ) );
        }

        private void setResources()
        {
this.btnLeft.setIcon( new ImageIcon( Resources.get_target__pencil() ) );
this.btnDataPoint.setIcon( new ImageIcon( Resources.get_target__pencil() ) );
this.btnRight.setIcon( new ImageIcon( Resources.get_target__pencil() ) );
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
try {
    int x, y;
    x = str.toi( txtDataPointClock.getText() );
    y = str.toi( txtDataPointValue.getText() );
    if ( y < m_min || m_max < y ) {
        AppManager.showMessageBox( 
            _( "Invalid value" ), 
            _( "Error" ), 
            org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION, 
            org.kbinani.windows.forms.Utility.MSGBOX_ERROR_MESSAGE );
        return;
    }
    if ( chkEnableSmooth.isSelected() ) {
        x = str.toi( txtLeftClock.getText() );
        y = str.toi( txtLeftValue.getText() );
        x = str.toi( txtRightClock.getText() );
        y = str.toi( txtRightValue.getText() );
    }
    setDialogResult( BDialogResult.OK );
} catch ( Exception ex ) {
    AppManager.showMessageBox( _( "Integer format error" ), _( "Error" ), org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION, org.kbinani.windows.forms.Utility.MSGBOX_ERROR_MESSAGE );
    setDialogResult( BDialogResult.CANCEL );
    Logger.write( FormBezierPointEdit.class + ".btnOK_Click; ex=" + ex + "\n" );
}
        }

        public void chkEnableSmooth_CheckedChanged( Object sender, BEventArgs e )
        {
boolean value = chkEnableSmooth.isSelected();
txtLeftClock.setEnabled( value );
txtLeftValue.setEnabled( value );
btnLeft.setEnabled( value );
txtRightClock.setEnabled( value );
txtRightValue.setEnabled( value );
btnRight.setEnabled( value );

boolean old = 
    (m_point.getControlLeftType() != BezierControlType.None) ||
    (m_point.getControlRightType() != BezierControlType.None);
if ( value ) {
    m_point.setControlLeftType( BezierControlType.Normal );
    m_point.setControlRightType( BezierControlType.Normal );
} else {
    m_point.setControlLeftType( BezierControlType.None );
    m_point.setControlRightType( BezierControlType.None );
}
txtLeftClock.setText( ((int)(m_point.getBase().getX() + m_point.controlLeft.getX())) + "" );
txtLeftValue.setText( ((int)(m_point.getBase().getY() + m_point.controlLeft.getY())) + "" );
txtRightClock.setText( ((int)(m_point.getBase().getX() + m_point.controlRight.getX())) + "" );
txtRightValue.setText( ((int)(m_point.getBase().getY() + m_point.controlRight.getY())) + "" );
m_parent.invalidate();
        }

        public void handleOperationButtonMouseDown( Object sender, BMouseEventArgs e )
        {
BezierPickedSide side = BezierPickedSide.BASE;
if ( sender == btnLeft ) {
    side = BezierPickedSide.LEFT;
} else if ( sender == btnRight ) {
    side = BezierPickedSide.RIGHT;
}

m_last_mouse_global_location = PortUtil.getMousePosition();
PointD pd = m_point.getPosition( side );
Point loc_on_trackselector = 
    new Point( 
        AppManager.xCoordFromClocks( (int)pd.getX() ),
        m_parent.yCoordFromValue( (int)pd.getY() ) );
Point loc_topleft = m_parent.getLocationOnScreen();
mScreenMouseDownLocation =
    new Point(
        loc_topleft.x + loc_on_trackselector.x,
        loc_topleft.y + loc_on_trackselector.y );
PortUtil.setMousePosition( mScreenMouseDownLocation );
BMouseEventArgs event_arg =
    new BMouseEventArgs(
        BMouseButtons.Left, 0,
        loc_on_trackselector.x, loc_on_trackselector.y, 0 );
m_parent.TrackSelector_MouseDown( this, event_arg );
m_picked_side = side;
m_btn_datapoint_downed = true;
        }

        public void common_MouseUp( Object sender, BMouseEventArgs e )
        {
m_btn_datapoint_downed = false;
setVisible( true );
Point loc_on_screen = PortUtil.getMousePosition();
Point loc_trackselector = m_parent.getLocationOnScreen();
Point loc_on_trackselector = 
    new Point( loc_on_screen.x - loc_trackselector.x, loc_on_screen.y - loc_trackselector.y );
BMouseEventArgs event_arg = 
    new BMouseEventArgs( BMouseButtons.Left, 0, loc_on_trackselector.x, loc_on_trackselector.y, 0 );
m_parent.TrackSelector_MouseUp( this, event_arg );
PortUtil.setMousePosition( m_last_mouse_global_location );
m_parent.invalidate();
        }

        public void common_MouseMove( Object sender, BMouseEventArgs e )
        {
if ( m_btn_datapoint_downed ) {
    Point loc_on_screen = PortUtil.getMousePosition();

    if ( loc_on_screen.x == mScreenMouseDownLocation.x &&
        loc_on_screen.y == mScreenMouseDownLocation.y ) {
        // マウスが動いていないようならbailout
        return;
    }

    Point loc_trackselector = m_parent.getLocationOnScreen();
    Point loc_on_trackselector = 
        new Point( loc_on_screen.x - loc_trackselector.x, loc_on_screen.y - loc_trackselector.y );
    BMouseEventArgs event_arg = 
        new BMouseEventArgs( BMouseButtons.Left, 0, loc_on_trackselector.x, loc_on_trackselector.y, 0 );
    BezierPoint ret = m_parent.HandleMouseMoveForBezierMove( event_arg, m_picked_side );

    txtDataPointClock.setText( ((int)ret.getBase().getX()) + "" );
    txtDataPointValue.setText( ((int)ret.getBase().getY()) + "" );
    txtLeftClock.setText( ((int)ret.getControlLeft().getX()) + "" );
    txtLeftValue.setText( ((int)ret.getControlLeft().getY()) + "" );
    txtRightClock.setText( ((int)ret.getControlRight().getX()) + "" );
    txtRightValue.setText( ((int)ret.getControlRight().getY()) + "" );

    m_parent.invalidate();
}
        }

        public void handleMoveButtonClick( Object sender, BEventArgs e )
        {
// イベントの送り主によって動作を変える
int delta = 1;
if ( sender == btnBackward ) {
    delta = -1;
}

// 選択中のデータ点を検索し，次に選択するデータ点を決める
BezierChain target = AppManager.getVsqFile().AttachedCurves.get( m_track - 1 ).getBezierChain( m_curve_type, m_chain_id );
int index = -2;
int size = target.size();
for ( int i = 0; i < size; i++ ) {
    if ( target.points.get( i ).getID() == m_point_id ) {
        index = i + delta;
        break;
    }
}

// 次に選択するデータ点のインデックスが有効範囲なら，選択を実行
if ( 0 <= index && index < size ) {
    // 選択を実行
    m_point_id = target.points.get( index ).getID();
    m_point = target.points.get( index );
    updateStatus();
    m_parent.mEditingPointID = m_point_id;
    m_parent.invalidate();

    // スクリーン上でデータ点が見えるようにする
    FormMain main = m_parent.getMainForm();
    if ( main != null ) {
        main.ensureVisible( (int)m_point.getBase().getX() );
    }
}
        }

        public void btnCancel_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }


    private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private BButton btnBackward = null;
	private BCheckBox chkEnableSmooth = null;
	private BButton btnForward = null;
	private BGroupBox groupLeft = null;
	private BLabel lblLeftClock = null;
	private BTextBox txtLeftClock = null;
	private BLabel lblLeftValue = null;
	private BTextBox txtLeftValue = null;
	private BButton btnLeft = null;
	private BGroupBox groupDataPoint = null;
	private BLabel lblDataPointClock = null;
	private BTextBox txtDataPointClock = null;
	private BLabel lblDataPointValue = null;
	private BTextBox txtDataPointValue = null;
	private BButton btnDataPoint = null;
	private BGroupBox groupRight = null;
	private BLabel lblRightClock = null;
	private BTextBox txtRightClock = null;
	private BLabel lblRightValue = null;
	private BTextBox txtRightValue = null;
	private BButton btnRight = null;
	private BButton btnOK = null;
	private BButton btnCancel = null;
	private BLabel jLabel4 = null;
	private BLabel jLabel5 = null;
	private JPanel jPanel3 = null;
    private BLabel lblRightValue1 = null;


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(469, 266);
		this.setContentPane(getJContentPane());
		this.setTitle("Edit Bezier Data Point");
		setCancelButton( btnCancel );
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 0;
			gridBagConstraints91.gridwidth = 3;
			gridBagConstraints91.anchor = GridBagConstraints.EAST;
			gridBagConstraints91.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints91.gridy = 4;
			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.gridx = 0;
			gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints81.gridwidth = 3;
			gridBagConstraints81.gridy = 3;
			jLabel5 = new BLabel();
			jLabel5.setText("    ");
			GridBagConstraints gridBagConstraints73 = new GridBagConstraints();
			gridBagConstraints73.gridx = 0;
			gridBagConstraints73.gridwidth = 3;
			gridBagConstraints73.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints73.gridy = 1;
			jLabel4 = new BLabel();
			jLabel4.setText("     ");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints9.gridy = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints8.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getBtnBackward(), gridBagConstraints);
			jContentPane.add(getChkEnableSmooth(), gridBagConstraints1);
			jContentPane.add(getBtnForward(), gridBagConstraints2);
			jContentPane.add(getGroupLeft(), gridBagConstraints8);
			jContentPane.add(getGroupDataPoint(), gridBagConstraints9);
			jContentPane.add(getGroupRight(), gridBagConstraints10);
			jContentPane.add(jLabel4, gridBagConstraints73);
			jContentPane.add(jLabel5, gridBagConstraints81);
			jContentPane.add(getJPanel3(), gridBagConstraints91);
			jContentPane.add(jLabel4, gridBagConstraints13);
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnBackward	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnBackward() {
		if (btnBackward == null) {
			btnBackward = new BButton();
			btnBackward.setText("<<");
		}
		return btnBackward;
	}

	/**
	 * This method initializes chkEnableSmooth	
	 * 	
	 * @return javax.swing.BCheckBox	
	 */
	private BCheckBox getChkEnableSmooth() {
		if (chkEnableSmooth == null) {
			chkEnableSmooth = new BCheckBox();
			chkEnableSmooth.setText("Smooth");
		}
		return chkEnableSmooth;
	}

	/**
	 * This method initializes btnForward	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnForward() {
		if (btnForward == null) {
			btnForward = new BButton();
			btnForward.setText(">>");
		}
		return btnForward;
	}

	/**
	 * This method initializes groupLeft	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getGroupLeft() {
		if (groupLeft == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.ipadx = 0;
			gridBagConstraints7.ipady = 0;
			gridBagConstraints7.insets = new Insets(5, 20, 5, 20);
			gridBagConstraints7.gridy = 3;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints5.gridy = 1;
			lblLeftValue = new BLabel();
			lblLeftValue.setText("Value");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints3.gridy = 0;
			lblLeftClock = new BLabel();
			lblLeftClock.setText("Clock");
			groupLeft = new BGroupBox();
			groupLeft.setLayout(new GridBagLayout());
			groupLeft.setTitle("Left Control Point");
			groupLeft.add(lblLeftClock, gridBagConstraints3);
			groupLeft.add(getJTextField(), gridBagConstraints4);
			groupLeft.add(lblLeftValue, gridBagConstraints5);
			groupLeft.add(getTxtLeftValue(), gridBagConstraints6);
			groupLeft.add(getBtnLeft(), gridBagConstraints7);
		}
		return groupLeft;
	}

	/**
	 * This method initializes BTextBox	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getJTextField() {
		if (txtLeftClock == null) {
			txtLeftClock = new BTextBox();
		}
		return txtLeftClock;
	}

	/**
	 * This method initializes txtLeftValue	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getTxtLeftValue() {
		if (txtLeftValue == null) {
			txtLeftValue = new BTextBox();
		}
		return txtLeftValue;
	}

	/**
	 * This method initializes btnLeft	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnLeft() {
		if (btnLeft == null) {
			btnLeft = new BButton();
			btnLeft.setText("");
			btnLeft.setIcon(new ImageIcon(getClass().getResource("/target--pencil.png")));
		}
		return btnLeft;
	}

	/**
	 * This method initializes groupDataPoint	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getGroupDataPoint() {
		if (groupDataPoint == null) {
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.gridx = 0;
			gridBagConstraints71.gridy = 2;
			gridBagConstraints71.weightx = 1.0D;
			gridBagConstraints71.insets = new Insets(5, 20, 5, 20);
			gridBagConstraints71.gridwidth = 2;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 1;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints61.gridx = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 0;
			gridBagConstraints51.anchor = GridBagConstraints.WEST;
			gridBagConstraints51.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints51.gridy = 1;
			lblDataPointValue = new BLabel();
			lblDataPointValue.setText("Value");
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 0;
			gridBagConstraints41.weightx = 1.0D;
			gridBagConstraints41.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints41.gridx = 1;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 0;
			lblDataPointClock = new BLabel();
			lblDataPointClock.setText("Clock");
			groupDataPoint = new BGroupBox();
			groupDataPoint.setLayout(new GridBagLayout());
			groupDataPoint.setTitle("Data Point");
			groupDataPoint.add(lblDataPointClock, gridBagConstraints31);
			groupDataPoint.add(getTxtDataPointClock(), gridBagConstraints41);
			groupDataPoint.add(lblDataPointValue, gridBagConstraints51);
			groupDataPoint.add(getTxtDataPointValue(), gridBagConstraints61);
			groupDataPoint.add(getBtnDataPoint(), gridBagConstraints71);
		}
		return groupDataPoint;
	}

	/**
	 * This method initializes txtDataPointClock	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getTxtDataPointClock() {
		if (txtDataPointClock == null) {
			txtDataPointClock = new BTextBox();
		}
		return txtDataPointClock;
	}

	/**
	 * This method initializes txtDataPointValue	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getTxtDataPointValue() {
		if (txtDataPointValue == null) {
			txtDataPointValue = new BTextBox();
		}
		return txtDataPointValue;
	}

	/**
	 * This method initializes btnDataPoint	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnDataPoint() {
		if (btnDataPoint == null) {
			btnDataPoint = new BButton();
			btnDataPoint.setText("");
			btnDataPoint.setIcon(new ImageIcon(getClass().getResource("/target--pencil.png")));
		}
		return btnDataPoint;
	}

	/**
	 * This method initializes groupRight	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getGroupRight() {
		if (groupRight == null) {
			GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
			gridBagConstraints72.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints72.gridx = 0;
			gridBagConstraints72.gridy = 2;
			gridBagConstraints72.weightx = 1.0D;
			gridBagConstraints72.insets = new Insets(5, 20, 5, 20);
			gridBagConstraints72.gridwidth = 2;
			GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
			gridBagConstraints62.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints62.gridy = 1;
			gridBagConstraints62.weightx = 1.0;
			gridBagConstraints62.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints62.gridx = 1;
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.gridx = 0;
			gridBagConstraints52.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints52.gridy = 1;
			lblRightValue = new BLabel();
			lblRightValue.setText("Value");
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.weightx = 1.0D;
			gridBagConstraints42.insets = new Insets(5, 5, 5, 15);
			gridBagConstraints42.gridx = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints32.gridy = 0;
			lblRightClock = new BLabel();
			lblRightClock.setText("Clock");
			groupRight = new BGroupBox();
			groupRight.setLayout(new GridBagLayout());
			groupRight.setTitle("Right Control Point");
			groupRight.add(lblRightClock, gridBagConstraints32);
			groupRight.add(getTxtRightClock(), gridBagConstraints42);
			groupRight.add(lblRightValue, gridBagConstraints52);
			groupRight.add(getTxtRightValue(), gridBagConstraints62);
			groupRight.add(getBtnRight(), gridBagConstraints72);
		}
		return groupRight;
	}

	/**
	 * This method initializes txtRightClock	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getTxtRightClock() {
		if (txtRightClock == null) {
			txtRightClock = new BTextBox();
		}
		return txtRightClock;
	}

	/**
	 * This method initializes txtRightValue	
	 * 	
	 * @return javax.swing.BTextBox	
	 */
	private BTextBox getTxtRightValue() {
		if (txtRightValue == null) {
			txtRightValue = new BTextBox();
		}
		return txtRightValue;
	}

	/**
	 * This method initializes btnRight	
	 * 	
	 * @return javax.swing.BButton	
	 */
	private BButton getBtnRight() {
		if (btnRight == null) {
			btnRight = new BButton();
			btnRight.setText("");
			btnRight.setIcon(new ImageIcon(getClass().getResource("/target--pencil.png")));
		}
		return btnRight;
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
			lblRightValue1 = new BLabel();
			lblRightValue1.setText("");
			lblRightValue1.setPreferredSize(new Dimension(4, 4));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.insets = new Insets(0, 0, 0, 12);
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints12.gridy = 0;
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.add(getBtnCancel(), gridBagConstraints12);
			jPanel3.add(getBtnOK(), gridBagConstraints11);
			jPanel3.add(lblRightValue1, gridBagConstraints14);
		}
		return jPanel3;
	}

    }


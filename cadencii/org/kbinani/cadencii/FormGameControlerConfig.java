/*
 * FormGameControlerConfig.cs
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
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BPanel;
import org.kbinani.windows.forms.BPictureBox;
import org.kbinani.windows.forms.BProgressBar;


import java.util.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormGameControlerConfig extends BDialog
    {
        private Vector<Integer> m_list = new Vector<Integer>();
        private Vector<Integer> m_povs = new Vector<Integer>();
        private int index;
        private BTimer timer;

        public FormGameControlerConfig()
        {
super();
initialize();

timer = new BTimer();
registerEventHandlers();
setResources();
for ( int i = 0; i < 10; i++ ) {
    m_list.add( -1 );
}
for ( int i = 0; i < 4; i++ ) {
    m_povs.add( Integer.MIN_VALUE );
}
applyLanguage();
int num_dev = 0;
if ( num_dev > 0 ) {
    pictButton.setImage( Resources.get_btn1() );
    progressCount.setMaximum( 8 );
    progressCount.setMinimum( 0 );
    progressCount.setValue( 0 );
    index = 1;
    btnSkip.setEnabled( true );
    btnReset.setEnabled( true );
    timer.start();
} else {
    btnSkip.setEnabled( false );
    btnReset.setEnabled( false );
}
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public void applyLanguage()
        {
int num_dev = 0;
if ( num_dev > 0 ) {
    lblMessage.setText( gettext( "Push buttons in turn as shown below" ) );
} else {
    lblMessage.setText( gettext( "Game controler instanceof not available" ) );
}
setTitle( gettext( "Game Controler Configuration" ) );
btnOK.setText( gettext( "OK" ) );
btnCancel.setText( gettext( "Cancel" ) );
btnReset.setText( gettext( "Reset And Exit" ) );
btnSkip.setText( gettext( "Skip" ) );
        }

        public int getRectangle()
        {
return m_list.get( 0 );
        }

        public int getTriangle()
        {
return m_list.get( 1 );
        }

        public int getCircle()
        {
return m_list.get( 2 );
        }

        public int getCross()
        {
return m_list.get( 3 );
        }

        public int getL1()
        {
return m_list.get( 4 );
        }

        public int getL2()
        {
return m_list.get( 5 );
        }

        public int getR1()
        {
return m_list.get( 6 );
        }

        public int getR2()
        {
return m_list.get( 7 );
        }

        public int getSelect()
        {
return m_list.get( 8 );
        }

        public int getStart()
        {
return m_list.get( 9 );
        }

        public int getPovDown()
        {
return m_povs.get( 0 );
        }

        public int getPovLeft()
        {
return m_povs.get( 1 );
        }

        public int getPovUp()
        {
return m_povs.get( 2 );
        }

        public int getPovRight()
        {
return m_povs.get( 3 );
        }

        private static String gettext( String id )
        {
return Messaging.getMessage( id );
        }

        private void registerEventHandlers()
        {
timer.tickEvent.add( new BEventHandler( this, "timer_Tick" ) );
btnSkip.clickEvent.add( new BEventHandler( this, "btnSkip_Click" ) );
btnReset.clickEvent.add( new BEventHandler( this, "btnReset_Click" ) );
btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
btnCancel.clickEvent.add( new BEventHandler( this, "btnCancel_Click" ) );
        }

        private void setResources()
        {
        }

        public void timer_Tick( Object sender, BEventArgs e )
        {
//int num_btn = vstidrv.JoyGetNumButtons( 0 );
byte[] btn;
int pov;
pov = -1;
btn = new byte[]{};

boolean added = false;
if ( index <= 4 ) {
    if ( pov >= 0 && !m_povs.contains( pov ) ) {
        m_povs.set( index - 1, pov );
        added = true;
    }
} else {
    for ( int i = 0; i < btn.length; i++ ) {
        if ( btn[i] > 0x0 && !m_list.contains( i ) ) {
            m_list.set( index - 5, i );
            added = true;
            break;
        }
    }
}
if ( added ) {
    if ( index <= 8 ) {
        progressCount.setValue( index );
    } else if ( index <= 12 ) {
        progressCount.setValue( index - 8 );
    } else {
        progressCount.setValue( index - 12 );
    }

    if ( index == 8 ) {
        pictButton.setImage( Resources.get_btn2() );
        progressCount.setValue( 0 );
        progressCount.setMaximum( 4 );
    } else if ( index == 12 ) {
        pictButton.setImage( Resources.get_btn3() );
        progressCount.setValue( 0 );
        progressCount.setMaximum( 2 );
    }
    if ( index == 14 ) {
        btnSkip.setEnabled( false );
        btnOK.setEnabled( true );
        timer.stop();
    }
    index++;
}
        }

        public void btnSkip_Click( Object sender, BEventArgs e )
        {
if ( index <= 4 ) {
    m_povs.set( index - 1, Integer.MIN_VALUE );
} else {
    m_list.set( index - 5, -1 );
}
if ( index <= 8 ) {
    progressCount.setValue( index );
} else if ( index <= 12 ) {
    progressCount.setValue( index - 8 );
} else {
    progressCount.setValue( index - 12 );
}

if ( index == 8 ) {
    pictButton.setImage( Resources.get_btn2() );
    progressCount.setValue( 0 );
    progressCount.setMaximum( 4 );
} else if ( index == 12 ) {
    pictButton.setImage( Resources.get_btn3() );
    progressCount.setValue( 0 );
    progressCount.setMaximum( 2 );
}
if ( index == 14 ) {
    btnSkip.setEnabled( false );
    btnOK.setEnabled( true );
    timer.stop();
}
index++;
        }

        public void btnReset_Click( Object sender, BEventArgs e )
        {
m_list.set( 0, 3 ); // □
m_list.set( 1, 0 ); // △
m_list.set( 2, 1 ); // ○
m_list.set( 3, 2 ); // ×
m_list.set( 4, 4 ); // L1
m_list.set( 5, 6 ); // L2
m_list.set( 6, 5 ); // R1
m_list.set( 7, 7 ); // R2
m_list.set( 8, 8 ); // SELECT
m_list.set( 9, 9 ); // START
m_povs.set( 0, 18000 ); // down
m_povs.set( 1, 27000 ); // left
m_povs.set( 2, 0 ); // up
m_povs.set( 3, 9000 ); // right
setDialogResult( BDialogResult.OK );
        }

        public void btnCancel_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
        }


    private static final long serialVersionUID = 1L;
    private BPanel BPanel = null;
    private BLabel lblMessage = null;
    private BPictureBox pictButton = null;
    private BProgressBar progressCount = null;
    private BButton btnSkip = null;
    private BButton btnReset = null;
    private BPanel jPanel11 = null;
    private BButton btnOK = null;
    private BButton btnCancel = null;
    private BLabel jLabel4 = null;


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(356, 224));
        this.setTitle("Game Controler Configuration");
        this.setContentPane(getJPanel());
        setCancelButton( btnCancel );
    }

    /**
     * This method initializes BPanel	
     * 	
     * @return javax.swing.BPanel	
     */
    private BPanel getJPanel() {
        if (BPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.weighty = 1.0D;
            gridBagConstraints5.anchor = GridBagConstraints.NORTH;
            gridBagConstraints5.insets = new Insets(12, 0, 12, 0);
            gridBagConstraints5.gridy = 4;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.weightx = 0.5D;
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.insets = new Insets(0, 12, 0, 12);
            gridBagConstraints4.gridy = 3;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.weightx = 0.5D;
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.insets = new Insets(0, 24, 0, 12);
            gridBagConstraints3.gridy = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(12, 12, 12, 12);
            gridBagConstraints2.gridy = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.ipadx = 1;
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(16, 12, 0, 12);
            gridBagConstraints.gridy = 0;
            lblMessage = new BLabel();
            lblMessage.setText(" ");
            BPanel = new BPanel();
            BPanel.setLayout(new GridBagLayout());
            BPanel.add(lblMessage, gridBagConstraints);
            BPanel.add(getPictButton(), gridBagConstraints1);
            BPanel.add(getProgressCount(), gridBagConstraints2);
            BPanel.add(getBtnSkip(), gridBagConstraints3);
            BPanel.add(getBtnReset(), gridBagConstraints4);
            BPanel.add(getJPanel11(), gridBagConstraints5);
        }
        return BPanel;
    }

    /**
     * This method initializes pictButton	
     * 	
     * @return javax.swing.BPanel	
     */
    private BPictureBox getPictButton() {
        if (pictButton == null) {
            pictButton = new BPictureBox();
            pictButton.setLayout(new GridBagLayout());
        }
        return pictButton;
    }

    /**
     * This method initializes progressCount	
     * 	
     * @return javax.swing.BProgressBar	
     */
    private BProgressBar getProgressCount() {
        if (progressCount == null) {
            progressCount = new BProgressBar();
        }
        return progressCount;
    }

    /**
     * This method initializes btnSkip	
     * 	
     * @return javax.swing.BButton	
     */
    private BButton getBtnSkip() {
        if (btnSkip == null) {
            btnSkip = new BButton();
            btnSkip.setText("Skip");
        }
        return btnSkip;
    }

    /**
     * This method initializes btnReset	
     * 	
     * @return javax.swing.BButton	
     */
    private BButton getBtnReset() {
        if (btnReset == null) {
            btnReset = new BButton();
            btnReset.setText("Reset and Exit");
        }
        return btnReset;
    }

    /**
     * This method initializes jPanel11	
     * 	
     * @return org.kbinani.windows.forms.BPanel	
     */
    private BPanel getJPanel11() {
        if (jPanel11 == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = GridBagConstraints.BOTH;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.gridx = 0;
            jLabel4 = new BLabel();
            jLabel4.setText(" ");
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.anchor = GridBagConstraints.EAST;
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints16.gridx = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.anchor = GridBagConstraints.EAST;
            gridBagConstraints15.gridy = 0;
            gridBagConstraints15.insets = new Insets(0, 0, 0, 12);
            gridBagConstraints15.gridx = 2;
            jPanel11 = new BPanel();
            jPanel11.setLayout(new GridBagLayout());
            jPanel11.add(getBtnOK(), gridBagConstraints15);
            jPanel11.add(getBtnCancel(), gridBagConstraints16);
            jPanel11.add(jLabel4, gridBagConstraints17);
        }
        return jPanel11;
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


    }


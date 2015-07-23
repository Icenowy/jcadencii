/*
 * FormRealtimeConfig.cs
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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BForm;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BNumericUpDown;


import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormRealtimeConfig extends BDialog {
        private boolean m_game_ctrl_enabled = false;
        private double m_last_event_processed;
        private BTimer timer;

        public FormRealtimeConfig()
        {
super();
initialize();
timer = new BTimer();
timer.setDelay( 10 );
registerEventHandlers();
setResources();
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public float getSpeed()
        {
return (float)numSpeed.getFloatValue();
        }

        public void FormRealtimeConfig_Load( Object sender, BEventArgs e )
        {
System.err.println( "info; FormRealtimeConfig#FormRealtimeConfig_Load; not implemented yet; \"int num_joydev = 0\"" );
int num_joydev = 0;
m_game_ctrl_enabled = (num_joydev > 0);
if ( m_game_ctrl_enabled ) {
    timer.start();
}
        }

        public void timer_Tick( Object sender, BEventArgs e )
        {
        }

        public void btnStart_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
close();
        }

        public void btnCancel_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.CANCEL );
        }

        private void registerEventHandlers()
        {
this.loadEvent.add( new BEventHandler( this, "FormRealtimeConfig_Load" ) );
timer.tickEvent.add( new BEventHandler( this, "timer_Tick" ) );
btnStart.clickEvent.add( new BEventHandler( this, "btnStart_Click" ) );
btnCancel.clickEvent.add( new BEventHandler( this, "btnCancel_Click" ) );
        }

        private void setResources()
        {
        }


    private static final long serialVersionUID = 1L;
    private JLabel lblRealTimeInput = null;
    private JPanel jPanel = null;
    private BButton btnStart = null;
    private BButton btnCancel = null;
    private JPanel jPanel1 = null;
    private BLabel lblSpeed = null;
    private BNumericUpDown numSpeed = null;
    private JPanel jPanel2 = null;

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        lblRealTimeInput = new JLabel();
        lblRealTimeInput.setText("Realtime Input");
        lblRealTimeInput.setFont(new Font("Dialog", Font.PLAIN, 18));
        this.setSize(new Dimension(320, 182));
        this.setContentPane(getJPanel());
    		
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.fill = GridBagConstraints.NONE;
            gridBagConstraints5.weighty = 1.0D;
            gridBagConstraints5.gridy = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(12, 0, 12, 0);
            gridBagConstraints.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(lblRealTimeInput, gridBagConstraints);
            jPanel.add(getJPanel1(), gridBagConstraints5);
            jPanel.add(getJPanel2(), gridBagConstraints6);
        }
        return jPanel;
    }

    /**
     * This method initializes btnStart	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnStart() {
        if (btnStart == null) {
            btnStart = new BButton();
            btnStart.setText("Start");
            btnStart.setPreferredSize(new Dimension(120, 33));
            btnStart.setFont(new Font("Dialog", Font.PLAIN, 12));
        }
        return btnStart;
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
            btnCancel.setPreferredSize(new Dimension(120, 33));
            btnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
        }
        return btnCancel;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.NONE;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new Insets(0, 6, 0, 0);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new Insets(0, 0, 0, 6);
            gridBagConstraints3.gridy = 0;
            lblSpeed = new BLabel();
            lblSpeed.setText("Speed");
            lblSpeed.setFont(new Font("Dialog", Font.PLAIN, 12));
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(lblSpeed, gridBagConstraints3);
            jPanel1.add(getNumSpeed(), gridBagConstraints4);
        }
        return jPanel1;
    }

    /**
     * This method initializes numSpeed	
     * 	
     * @return javax.swing.JComboBox	
     */
    private BNumericUpDown getNumSpeed() {
        if (numSpeed == null) {
            numSpeed = new BNumericUpDown();
            numSpeed.setPreferredSize(new Dimension(120, 19));
        }
        return numSpeed;
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.insets = new Insets(0, 12, 12, 0);
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.insets = new Insets(0, 0, 12, 12);
            gridBagConstraints1.anchor = GridBagConstraints.EAST;
            gridBagConstraints1.gridy = 0;
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(getBtnStart(), gridBagConstraints1);
            jPanel2.add(getBtnCancel(), gridBagConstraints2);
        }
        return jPanel2;
    }

    }


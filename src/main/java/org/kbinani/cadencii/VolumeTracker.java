/*
 * VolumeTracker.cs
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

import org.kbinani.vsq.*;

import org.kbinani.windows.forms.*;
import org.kbinani.windows.forms.BCheckBox;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BSlider;
import org.kbinani.windows.forms.BTextBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class VolumeTracker extends JPanel implements IAmplifierView {
    public static final int WIDTH = 85;
    public static final int HEIGHT = 284;
    private static final int[][] _KEY = {
            { 55, 26 },
            { 51, 27 },
            { 47, 28 },
            { 42, 30 },
            { 38, 31 },
            { 35, 33 },
            { 31, 34 },
            { 28, 36 },
            { 24, 37 },
            { 21, 39 },
            { 18, 40 },
            { 15, 42 },
            { 12, 43 },
            { 10, 45 },
            { 7, 46 },
            { 5, 48 },
            { 2, 49 },
            { 0, 51 },
            { -2, 52 },
            { -5, 54 },
            { -7, 55 },
            { -10, 57 },
            { -12, 58 },
            { -15, 60 },
            { -18, 61 },
            { -21, 63 },
            { -24, 64 },
            { -28, 66 },
            { -31, 67 },
            { -35, 69 },
            { -38, 70 },
            { -42, 72 },
            { -47, 73 },
            { -51, 75 },
            { -55, 76 },
            { -60, 78 },
            { -65, 79 },
            { -70, 81 },
            { -76, 82 },
            { -81, 84 },
            { -87, 85 },
            { -93, 87 },
            { -100, 88 },
            { -107, 89 },
            { -114, 91 },
            { -121, 92 },
            { -129, 94 },
            { -137, 95 },
            { -145, 97 },
            { -154, 98 },
            { -163, 100 },
            { -173, 101 },
            { -183, 103 },
            { -193, 104 },
            { -204, 106 },
            { -215, 107 },
            { -227, 109 },
            { -240, 110 },
            { -253, 112 },
            { -266, 113 },
            { -280, 115 },
            { -295, 116 },
            { -311, 118 },
            { -327, 119 },
            { -344, 121 },
            { -362, 122 },
            { -380, 124 },
            { -399, 125 },
            { -420, 127 },
            { -441, 128 },
            { -463, 130 },
            { -486, 131 },
            { -510, 133 },
            { -535, 134 },
            { -561, 136 },
            { -589, 137 },
            { -617, 139 },
            { -647, 140 },
            { -678, 142 },
            { -711, 143 },
            { -745, 145 },
            { -781, 146 },
            { -818, 148 },
            { -857, 149 },
            { -898, 151 },
        };
    private static final long serialVersionUID = 1L;
    private int mFeder = 0;
    private String m_number = "0";
    private String m_title = "";
    private Object m_tag = null;
    private boolean mMuted = false;
    private int mPanpot = 0;
    private int mTrack = 0;
    public BEvent<BEventHandler> federChangedEvent = new BEvent<BEventHandler>();
    public BEvent<BEventHandler> panpotChangedEvent = new BEvent<BEventHandler>();
    public BEvent<BEventHandler> muteButtonClickEvent = new BEvent<BEventHandler>();
    public BEvent<BEventHandler> soloButtonClickEvent = new BEvent<BEventHandler>();
    private BTextBox txtFeder = null;
    private BSlider trackFeder = null;
    private BSlider trackPanpot = null;
    private BTextBox txtPanpot = null;
    private BLabel lblTitle = null;
    private BCheckBox chkMute = null;
    private BCheckBox chkSolo = null;

    public VolumeTracker() {
        super();
        initialize();
        registerEventHandlers();
        setResources();
        setMuted(false);
        setSolo(false);
    }

    public int getTrack() {
        return mTrack;
    }

    public void setTrack(int value) {
        mTrack = value;
    }

    public double getAmplifyL() {
        double ret = 0.0;

        if (!mMuted) {
            ret = VocaloSysUtil.getAmplifyCoeffFromFeder(mFeder) * VocaloSysUtil.getAmplifyCoeffFromPanLeft(mPanpot);
        }

        return ret;
    }

    public double getAmplifyR() {
        double ret = 0.0;

        if (!mMuted) {
            ret = VocaloSysUtil.getAmplifyCoeffFromFeder(mFeder) * VocaloSysUtil.getAmplifyCoeffFromPanRight(mPanpot);
        }

        return ret;
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }

    public void setTag(Object value) {
        m_tag = value;
    }

    public Object getTag() {
        return m_tag;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String value) {
        m_title = value;
        updateTitle();
    }

    private void updateTitle() {
        if (str.compare(m_number, "")) {
            lblTitle.setText(m_title);
        } else if (str.compare(m_title, "")) {
            lblTitle.setText(m_number);
        } else {
            lblTitle.setText(m_number + " " + m_title);
        }
    }

    public String getNumber() {
        return m_number;
    }

    public void setNumber(String value) {
        m_number = value;
        updateTitle();
    }

    public boolean isMuted() {
        return chkMute.isSelected();
    }

    public void setMuted(boolean value) {
        boolean old = chkMute.isSelected();
        chkMute.setSelected(value);
        chkMute.setBackground(value ? PortUtil.DimGray : Color.white);
        mMuted = value;
    }

    public boolean isSolo() {
        return chkSolo.isSelected();
    }

    public void setSolo(boolean value) {
        boolean old = chkSolo.isSelected();
        chkSolo.setSelected(value);
        chkSolo.setBackground(value ? PortUtil.DarkCyan : Color.white);
    }

    public int getPanpot() {
        return trackPanpot.getValue();
    }

    public void setPanpot(int value) {
        trackPanpot.setValue(value);
    }

    public boolean isSoloButtonVisible() {
        return chkSolo.isVisible();
    }

    public void setSoloButtonVisible(boolean value) {
        chkSolo.setVisible(value);
    }

    public int getFeder() {
        return mFeder;
    }

    public void setFeder(int value) {
        int old = mFeder;
        mFeder = value;

        if (old != mFeder) {
            try {
                federChangedEvent.raise(mTrack, mFeder);
            } catch (Exception ex) {
                serr.println("VolumeTracker#setFeder; ex=" + ex);
            }
        }

        int v = 177 - getYCoordFromFeder(mFeder);
        trackFeder.setValue(v);
    }

    private static int getFederFromYCoord(int y) {
        int feder = _KEY[0][0];
        int min_diff = Math.abs(_KEY[0][1] - y);
        int index = 0;
        int len = _KEY.length;

        for (int i = 1; i < len; i++) {
            int diff = Math.abs(_KEY[i][1] - y);

            if (diff < min_diff) {
                index = i;
                min_diff = diff;
                feder = _KEY[i][0];
            }
        }

        return feder;
    }

    private static int getYCoordFromFeder(int feder) {
        int y = _KEY[0][1];
        int min_diff = Math.abs(_KEY[0][0] - feder);
        int index = 0;
        int len = _KEY.length;

        for (int i = 1; i < len; i++) {
            int diff = Math.abs(_KEY[i][0] - feder);

            if (diff < min_diff) {
                index = i;
                min_diff = diff;
                y = _KEY[i][1];
            }
        }

        return y;
    }

    private void txtPanpot_Enter(Object sender, BEventArgs e) {
        txtPanpot.selectAll();
    }

    private void txtFeder_Enter(Object sender, BEventArgs e) {
        txtFeder.selectAll();
    }

    public void VolumeTracker_Resize(Object sender, BEventArgs e) {
    }

    public void trackFeder_ValueChanged(Object sender, BEventArgs e) {
        mFeder = getFederFromYCoord(151 - (trackFeder.getValue() - 26));
        txtFeder.setText((mFeder / 10.0) + "");

        try {
            federChangedEvent.raise(mTrack, mFeder);
        } catch (Exception ex) {
            serr.println("VolumeTracker#trackFeder_ValueChanged; ex=" + ex);
        }
    }

    public void trackPanpot_ValueChanged(Object sender, BEventArgs e) {
        mPanpot = trackPanpot.getValue();
        txtPanpot.setText(mPanpot + "");

        try {
            panpotChangedEvent.raise(mTrack, mPanpot);
        } catch (Exception ex) {
            serr.println("VolumeTracker#trackPanpot_ValueChanged; ex=" + ex);
        }
    }

    public void txtFeder_KeyDown(Object sender, BKeyEventArgs e) {
        if ((e.getKeyCode() & KeyEvent.VK_ENTER) != KeyEvent.VK_ENTER) {
            return;
        }

        try {
            int feder = (int) ((float) str.tof(txtFeder.getText()) * 10.0f);

            if (55 < feder) {
                feder = 55;
            }

            if (feder < -898) {
                feder = -898;
            }

            setFeder(feder);
            txtFeder.setText((getFeder() / 10.0f) + "");
            txtFeder.requestFocusInWindow();
            txtFeder.selectAll();
        } catch (Exception ex) {
            serr.println("VolumeTracker#txtFeder_KeyDown; ex=" + ex);
        }
    }

    public void txtPanpot_KeyDown(Object sender, BKeyEventArgs e) {
        if ((e.getKeyCode() & KeyEvent.VK_ENTER) != KeyEvent.VK_ENTER) {
            return;
        }

        try {
            int panpot = str.toi(txtPanpot.getText());

            if (panpot < -64) {
                panpot = -64;
            }

            if (64 < panpot) {
                panpot = 64;
            }

            setPanpot(panpot);
            txtPanpot.setText(getPanpot() + "");
            txtPanpot.requestFocusInWindow();
            txtPanpot.selectAll();
        } catch (Exception ex) {
            serr.println("VolumeTracker#txtPanpot_KeyDown; ex=" + ex);
        }
    }

    public void chkSolo_Click(Object sender, BEventArgs e) {
        try {
            soloButtonClickEvent.raise(this, e);
        } catch (Exception ex) {
            serr.println("VolumeTracker#chkSolo_Click; ex=" + ex);
        }
    }

    public void chkMute_Click(Object sender, BEventArgs e) {
        mMuted = chkMute.isSelected();

        try {
            muteButtonClickEvent.raise(this, e);
        } catch (Exception ex) {
            serr.println("VolumeTracker#chkMute_Click; ex=" + ex);
        }
    }

    private void registerEventHandlers() {
        trackFeder.valueChangedEvent.add(new BEventHandler(this,
                "trackFeder_ValueChanged"));
        trackPanpot.valueChangedEvent.add(new BEventHandler(this,
                "trackPanpot_ValueChanged"));
        txtPanpot.keyDownEvent.add(new BKeyEventHandler(this,
                "txtPanpot_KeyDown"));
        txtFeder.keyDownEvent.add(new BKeyEventHandler(this, "txtFeder_KeyDown"));
        chkSolo.clickEvent.add(new BEventHandler(this, "chkSolo_Click"));
        chkMute.clickEvent.add(new BEventHandler(this, "chkMute_Click"));
    }

    private void setResources() {
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.weightx = 1.0D;
        gridBagConstraints21.anchor = GridBagConstraints.WEST;

        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.weightx = 0.0D;
        gridBagConstraints12.anchor = GridBagConstraints.WEST;

        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.fill = GridBagConstraints.BOTH;
        gridBagConstraints11.weightx = 1.0D;
        gridBagConstraints11.gridwidth = 2;
        gridBagConstraints11.gridy = 5;
        lblTitle = new BLabel();
        lblTitle.setText("TITLE");
        lblTitle.setPreferredSize(new Dimension(85, 23));
        lblTitle.setBackground(Color.white);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new Insets(0, 10, 10, 10);
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.gridx = 0;

        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 0.0D;
        gridBagConstraints2.insets = new Insets(3, 3, 3, 3);
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.gridx = 0;

        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0D;
        gridBagConstraints1.insets = new Insets(10, 0, 10, 0);
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridx = 0;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0D;
        gridBagConstraints.insets = new Insets(10, 3, 0, 3);
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        this.setSize(86, 284);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(85, 284));
        this.setBackground(new Color(180, 180, 180));
        this.add(getChkMute(), gridBagConstraints12);
        this.add(getChkSolo(), gridBagConstraints21);
        this.add(getTxtFeder(), gridBagConstraints);
        this.add(getTrackFeder(), gridBagConstraints1);
        this.add(getTrackPanpot(), gridBagConstraints2);
        this.add(getTxtPanpot(), gridBagConstraints3);
        this.add(lblTitle, gridBagConstraints11);
    }

    /**
     * This method initializes txtFeder
     *
     * @return javax.swing.JTextField
     */
    private BTextBox getTxtFeder() {
        if (txtFeder == null) {
            txtFeder = new BTextBox();
            txtFeder.setPreferredSize(new Dimension(79, 19));
            txtFeder.setHorizontalAlignment(BTextBox.CENTER);
            txtFeder.setText("0");
        }

        return txtFeder;
    }

    /**
     * This method initializes trackFeder
     *
     * @return javax.swing.JSlider
     */
    private BSlider getTrackFeder() {
        if (trackFeder == null) {
            trackFeder = new BSlider();
            trackFeder.setOrientation(BSlider.VERTICAL);
            trackFeder.setMinimum(26);
            trackFeder.setPreferredSize(new Dimension(45, 144));
            trackFeder.setValue(100);
            trackFeder.setBackground(new Color(180, 180, 180));
            trackFeder.setMajorTickSpacing(10);
            trackFeder.setMaximum(151);
        }

        return trackFeder;
    }

    /**
     * This method initializes trackPanpot
     *
     * @return javax.swing.JSlider
     */
    private BSlider getTrackPanpot() {
        if (trackPanpot == null) {
            trackPanpot = new BSlider();
            trackPanpot.setMaximum(64);
            trackPanpot.setValue(0);
            trackPanpot.setBackground(new Color(180, 180, 180));
            trackPanpot.setMajorTickSpacing(1);
            trackPanpot.setMinimum(-64);
        }

        return trackPanpot;
    }

    /**
     * This method initializes txtPanpot
     *
     * @return javax.swing.JTextField
     */
    private BTextBox getTxtPanpot() {
        if (txtPanpot == null) {
            txtPanpot = new BTextBox();
            txtPanpot.setHorizontalAlignment(BTextBox.CENTER);
            txtPanpot.setText("0");
        }

        return txtPanpot;
    }

    /**
     * This method initializes chkMute
     *
     * @return org.kbinani.windows.forms.BCheckBox
     */
    private BCheckBox getChkMute() {
        if (chkMute == null) {
            chkMute = new BCheckBox();
            chkMute.setText("M");
        }

        return chkMute;
    }

    /**
     * This method initializes chkSolo
     *
     * @return org.kbinani.windows.forms.BCheckBox
     */
    private BCheckBox getChkSolo() {
        if (chkSolo == null) {
            chkSolo = new BCheckBox();
            chkSolo.setText("S");
        }

        return chkSolo;
    }
}

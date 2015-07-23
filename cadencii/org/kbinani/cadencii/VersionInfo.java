/*
 * VersionInfo.cs
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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BCheckBox;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BPanel;
import org.kbinani.windows.forms.BPictureBox;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import org.kbinani.*;
import org.kbinani.windows.forms.*;
import org.kbinani.apputil.*;


    public class VersionInfo extends BDialog {
        static final float m_speed = 35f;
        static final int m_height = 380;
        static final int FONT_SIZE = 12;

        private final Color m_background = Color.white;

        private double m_scroll_started;
        private AuthorListEntry[] m_credit;
        private String m_version;
        private boolean m_credit_mode = false;
        private float m_last_t = 0f;
        private float m_last_speed = 0f;
        private float m_shift = 0f;
        private int m_button_width_about = 75;
        private int m_button_width_credit = 75;
        private BufferedImage m_scroll = null;
        private BufferedImage m_scroll_with_id = null;
        private String m_app_name = "";
        private Color m_app_name_color = Color.black;
        private Color m_version_color = new Color( 105, 105, 105 );
        private boolean m_shadow_enablde = false;
        private BTimer timer;
        private boolean m_show_twitter_id = false;

        public VersionInfo( String app_name, String version )
        {
super();
initialize();
timer = new BTimer();
m_version = version;
m_app_name = app_name;

timer.setDelay( 30 );
registerEventHandlers();
setResources();
applyLanguage();


m_credit = new AuthorListEntry[] { };
lblVstLogo.setForeground( m_version_color );
chkTwitterID.setVisible( false );
        }

        public boolean isShowTwitterID()
        {
return m_show_twitter_id;
        }

        public void setShowTwitterID( boolean value )
        {
m_show_twitter_id = value;
        }

        public void applyLanguage()
        {
String about = PortUtil.formatMessage( gettext( "About {0}" ), m_app_name );
String credit = gettext( "Credit" );
Dimension size1 = Util.measureString( about, btnFlip.getFont() );
Dimension size2 = Util.measureString( credit, btnFlip.getFont() );
m_button_width_about = Math.max( 75, (int)(size1.width * 1.3) );
m_button_width_credit = Math.max( 75, (int)(size2.width * 1.3) );
if ( m_credit_mode ) {
    btnFlip.setText( about );
} else {
    btnFlip.setText( credit );
}
setTitle( about );
        }

        public static String gettext( String s )
        {
return Messaging.getMessage( s );
        }

        /// <summary>
        /// バージョン番号表示の文字色を取得または設定します
        /// </summary>
        public Color getVersionColor()
        {
return m_version_color;
        }

        public void setVersionColor( Color value )
        {
m_version_color = value;
lblVstLogo.setForeground( value );
        }

        /// <summary>
        /// アプリケーション名表示の文字色を取得または設定します
        /// </summary>
        public Color getAppNameColor()
        {
return m_app_name_color;
        }

        public void setAppNameColor( Color value )
        {
m_app_name_color = value;
        }


        public String getAppName()
        {
return m_app_name;
        }

        public void setAppName( String value )
        {
m_app_name = value;
        }

        public void setAuthorList( AuthorListEntry[] value )
        {
m_credit = value;
m_scroll = generateAuthorListB( false );
m_scroll_with_id = generateAuthorListB( true );
        }

        private BufferedImage generateAuthorListB( boolean show_twitter_id )
        {
int shadow_shift = 2;
String font_name = "Arial";
Font font = new Font( font_name, java.awt.Font.PLAIN, FONT_SIZE );
Dimension size = Util.measureString( "the quick brown fox jumped over the lazy dogs. THE QUICK BROWN FOX JUMPED OVER THE LAZY DOGS. 0123456789", font );
int width = getWidth();
int height = size.height;
//StringFormat sf = new StringFormat();
BufferedImage ret = new BufferedImage( (int)width, (int)(40f + m_credit.length * height * 1.1f), BufferedImage.TYPE_INT_BGR );
Graphics2D g = ret.createGraphics();
g.setColor( Color.white );
g.fillRect( 0, 0, ret.getWidth( null ), ret.getHeight( null ) );
int align = 0;
int valign = 0;
//sf.Alignment = StringAlignment.Center;
Font f = new Font( font_name, java.awt.Font.BOLD, (int)(FONT_SIZE * 1.2f) );
if ( m_shadow_enablde ) {
    g.setColor( new Color( 0, 0, 0, 40 ) );
    PortUtil.drawStringEx(
        g,
        m_app_name,
        f,
        new Rectangle( shadow_shift, shadow_shift + height, width, height ),
        align,
        valign );
}
g.setColor( Color.black );
PortUtil.drawStringEx(
    g,
    m_app_name,
    f,
    new Rectangle( 0, height, width, height ),
    align,
    valign );
for ( int i = 0; i < m_credit.length; i++ ) {
    AuthorListEntry itemi = m_credit[i];
    Font f2 = new Font( font_name, itemi.getStyle(), FONT_SIZE );
    String id = show_twitter_id ? itemi.getTwitterID() : "";
    if ( id == null ) {
        id = "";
    }
    String str = itemi.getName() + (id.equals( "" ) ? "" : (" (" + id + ")"));
    if ( m_shadow_enablde ) {
        g.setColor( new Color( 0, 0, 0, 40 ) );
        PortUtil.drawStringEx(
            g,
            str,
            font,
            new Rectangle( 0 + shadow_shift, 40 + (int)(i * height * 1.1) + shadow_shift + height, width, height ),
            align,
            valign );
    }
    g.setColor( Color.black );
    PortUtil.drawStringEx( 
        g,
        str,
        f2,
        new Rectangle( 0, 40 + (int)(i * height * 1.1) + height, width, height ),
        align,
        valign );
}
return ret;
        }

        void btnSaveAuthorList_Click( Object sender, BEventArgs e )
        {
        }

        public void btnOK_Click( Object sender, BEventArgs e )
        {
setDialogResult( BDialogResult.OK );
timer.stop();
close();
        }

        public void btnFlip_Click( Object sender, BEventArgs e )
        {
m_credit_mode = !m_credit_mode;
if ( m_credit_mode ) {
    try {
        btnFlip.setText( PortUtil.formatMessage( gettext( "About {0}" ), m_app_name ) );
    } catch ( Exception ex ) {
        btnFlip.setText( "About " + m_app_name );
    }
    m_scroll_started = PortUtil.getCurrentTime();
    m_last_speed = 0f;
    m_last_t = 0f;
    m_shift = 0f;
    pictVstLogo.setVisible( false );
    lblVstLogo.setVisible( false );
    chkTwitterID.setVisible( true );
    timer.start();
} else {
    timer.stop();
    btnFlip.setText( gettext( "Credit" ) );
    pictVstLogo.setVisible( true );
    lblVstLogo.setVisible( true );
    chkTwitterID.setVisible( false );
}
this.repaint();
        }

        public void timer_Tick( Object sender, BEventArgs e )
        {
this.repaint();
        }

        public void VersionInfo_Paint( Object sender, BPaintEventArgs e )
        {
try {
    e.Graphics.setColor( Color.white );
    e.Graphics.fillRect( 0, 0, this.getWidth(), this.getHeight() );
    paintCor( e.Graphics );
} catch ( Exception ex ) {
}
        }

        private void paintCor( Graphics g1 )
        {
Graphics2D g = (Graphics2D)g1;
g.clipRect( 0, 0, getWidth(), m_height );
g.setColor( Color.white );
g.fillRect( 0, 0, getWidth(), getHeight() );
//g.clearRect( 0, 0, getWidth(), getHeight() );
if ( m_credit_mode ) {
    float times = (float)(PortUtil.getCurrentTime() - m_scroll_started) - 3f;
    float speed = (float)((2.0 - math.erfc( times * 0.8 )) / 2.0) * m_speed;
    float dt = times - m_last_t;
    m_shift += (speed + m_last_speed) * dt / 2f;
    m_last_t = times;
    m_last_speed = speed;
    BufferedImage image = m_show_twitter_id ? m_scroll_with_id : m_scroll;
    if ( image != null ) {
        float dx = (getWidth() - image.getWidth( null )) * 0.5f;
        g.drawImage( image, (int)dx, (int)(90f - m_shift), null );
        if ( 90f - m_shift + image.getHeight( null ) < 0 ) {
            m_shift = -m_height * 1.5f;
        }
    }
    int grad_height = 60;
    Rectangle top = new Rectangle( 0, 0, getWidth(), grad_height );
    Rectangle bottom = new Rectangle( 0, m_height - grad_height, getWidth(), grad_height );
    g.clipRect( 0, m_height - grad_height + 1, getWidth(), grad_height - 1 );
    g.setClip( null );
} else {
    g.setFont( new Font( "Century Gorhic", java.awt.Font.BOLD, FONT_SIZE * 2 ) );
    g.setColor( m_app_name_color );
    g.drawString( m_app_name, 20, 60 );
    g.setFont( new Font( "Arial", 0, FONT_SIZE ) );
    String[] spl = PortUtil.splitString( m_version, '\n' );
    int y = 100;
    int delta = (int)(FONT_SIZE * 1.1);
    if( delta == FONT_SIZE ){
        delta++;
    }
    for( int i = 0; i < spl.length; i++ ){
        g.drawString( (i == 0 ? "version" : "") + spl[i], 25, y );
        y += delta;
    }
}
        }

        private void VersionInfo_KeyDown( Object sender, BKeyEventArgs e )
        {
if( (e.getKeyCode() & KeyEvent.VK_ESCAPE) == KeyEvent.VK_ESCAPE )
 {
    setDialogResult( BDialogResult.CANCEL );
    close();
}
        }

        private void VersionInfo_FontChanged( Object sender, BEventArgs e )
        {
Util.applyFontRecurse( this, getFont() );
        }

        public void chkTwitterID_CheckedChanged( Object sender, BEventArgs e )
        {
m_show_twitter_id = chkTwitterID.isSelected();
repaint();
        }

        private void registerEventHandlers()
        {
this.panelCredit.paintEvent.add( new BPaintEventHandler( this, "VersionInfo_Paint" ) );
this.timer.tickEvent.add( new BEventHandler( this, "timer_Tick" ) );
this.btnFlip.clickEvent.add( new BEventHandler( this, "btnFlip_Click" ) );
this.btnOK.clickEvent.add( new BEventHandler( this, "btnOK_Click" ) );
this.chkTwitterID.checkedChangedEvent.add( new BEventHandler( this, "chkTwitterID_CheckedChanged" ) );
        }

        private void setResources()
        {
pictVstLogo.setImage( Resources.get_VSTonWht() );
        }


    private static final long serialVersionUID = 1L;
    private JPanel jPanel = null;
    private BPanel panelCredit = null;
    private JPanel jPanel1 = null;
    private JPanel jPanel2 = null;
    private BButton btnFlip = null;
    private BButton btnSaveAuthorList = null;
    private BButton btnOK = null;
    private JLabel jLabel1 = null;
    private JTextArea lblVstLogo = null;
    private BPictureBox pictVstLogo = null;
    private BCheckBox chkTwitterID = null;
    

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(327, 455));
        this.setMinimumSize(new Dimension(327, 455));
        this.setPreferredSize(new Dimension(327, 455));
        this.setResizable(false);
        this.setContentPane(getJPanel());
        setCancelButton( btnOK );
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.weightx = 0.0D;
            gridBagConstraints11.insets = new Insets(0, 0, 12, 3);
            gridBagConstraints11.anchor = GridBagConstraints.EAST;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.weightx = 0.0D;
            gridBagConstraints3.insets = new Insets(0, 3, 12, 12);
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.insets = new Insets(0, 12, 12, 0);
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.insets = new Insets(0, 0, 12, 0);
            gridBagConstraints.gridx = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getJPanel2(), gridBagConstraints);
            jPanel.add(getBtnFlip(), gridBagConstraints1);
            jPanel.add(getBtnSaveAuthorList(), gridBagConstraints2);
            jPanel.add(getChkTwitterID(), gridBagConstraints11);
            jPanel.add(getBtnOK(), gridBagConstraints3);
        }
        return jPanel;
    }

    /**
     * This method initializes panelCredit	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private BPanel getPanelCredit() {
        if (panelCredit == null) {
            panelCredit = new BPanel();
            panelCredit.setName("jScrollPane");
            panelCredit.setBackground(Color.white);
            panelCredit.setPreferredSize(new Dimension(327, 111));
        }
        return panelCredit;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.weighty = 0.0D;
            gridBagConstraints6.insets = new Insets(0, 12, 6, 12);
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.weighty = 1.0D;
            gridBagConstraints5.gridy = 1;
            jLabel1 = new JLabel();
            jLabel1.setText(" ");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints4.gridwidth = 1;
            gridBagConstraints4.anchor = GridBagConstraints.NORTH;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.gridy = 2;
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.setName("jPanel1");
            jPanel1.setBackground(Color.white);
            jPanel1.setPreferredSize(new Dimension(327, 111));
            jPanel1.add(jLabel1, gridBagConstraints5);
            jPanel1.add(getLblVstLogo(), gridBagConstraints6);
            jPanel1.add(getPictVstLogo(), new GridBagConstraints());
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new CardLayout());
            jPanel2.setPreferredSize(new Dimension(327, 111));
            jPanel2.add(getPanelCredit(), getPanelCredit().getName());
            jPanel2.add(getJPanel1(), getJPanel1().getName());
        }
        return jPanel2;
    }

    /**
     * This method initializes btnFlip	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnFlip() {
        if (btnFlip == null) {
            btnFlip = new BButton();
            btnFlip.setText("Credit");
            btnFlip.setPreferredSize(new Dimension(150, 29));
        }
        return btnFlip;
    }

    /**
     * This method initializes btnSaveAuthorList	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnSaveAuthorList() {
        if (btnSaveAuthorList == null) {
            btnSaveAuthorList = new BButton();
            btnSaveAuthorList.setVisible(false);
        }
        return btnSaveAuthorList;
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return javax.swing.JButton	
     */
    private BButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new BButton();
            btnOK.setText("OK");
        }
        return btnOK;
    }

    /**
     * This method initializes lblVstLogo	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getLblVstLogo() {
        if (lblVstLogo == null) {
            lblVstLogo = new JTextArea();
            lblVstLogo.setText("VST PlugIn Technology by Steinberg Media Technologies GmbH");
            lblVstLogo.setLineWrap(true);
        }
        return lblVstLogo;
    }

    /**
     * This method initializes pictVstLogo	
     * 	
     * @return javax.swing.JPanel	
     */
    private BPictureBox getPictVstLogo() {
        if (pictVstLogo == null) {
            pictVstLogo = new BPictureBox();
            pictVstLogo.setLayout(new GridBagLayout());
        }
        return pictVstLogo;
    }

    /**
     * This method initializes chkTwitterID	
     * 	
     * @return javax.swing.JButton	
     */
    private BCheckBox getChkTwitterID() {
        if (chkTwitterID == null) {
            chkTwitterID = new BCheckBox();
            chkTwitterID.setText("TwtrID");
            chkTwitterID.setName("btnOK1");
        }
        return chkTwitterID;
    }

    }


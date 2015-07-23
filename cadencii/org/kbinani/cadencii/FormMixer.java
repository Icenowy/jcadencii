/*
 * FormMixer.cs
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BMenu;
import org.kbinani.windows.forms.BMenuItem;
import org.kbinani.windows.forms.BPanel;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;

    public class FormMixer extends BForm
    {
        final int SCROLL_HEIGHT = 15;
        private FormMain m_parent;
        private Vector<VolumeTracker> m_tracker = null;
        private boolean mPreviousAlwaysOnTop;

        public BEvent<FederChangedEventHandler> federChangedEvent = new BEvent<FederChangedEventHandler>();

        public BEvent<PanpotChangedEventHandler> panpotChangedEvent = new BEvent<PanpotChangedEventHandler>();

        public BEvent<SoloChangedEventHandler> soloChangedEvent = new BEvent<SoloChangedEventHandler>();

        public BEvent<MuteChangedEventHandler> muteChangedEvent = new BEvent<MuteChangedEventHandler>();

        public FormMixer( FormMain parent )
        {
super();
initialize();
registerEventHandlers();
setResources();
volumeMaster.setFeder( 0 );
volumeMaster.setMuted( false );
volumeMaster.setSolo( true );
volumeMaster.setNumber( "Master" );
volumeMaster.setPanpot( 0 );
volumeMaster.setSoloButtonVisible( false );
volumeMaster.setTitle( "" );
applyLanguage();
m_parent = parent;
        }

        /// <summary>
        /// AlwaysOnTopが強制的にfalseにされる直前の，AlwaysOnTop値を取得します．
        /// </summary>
        public boolean getPreviousAlwaysOnTop()
        {
return mPreviousAlwaysOnTop;
        }
        
        /// <summary>
        /// AlwaysOnTopが強制的にfalseにされる直前の，AlwaysOnTop値を設定しておきます．
        /// </summary>
        public void setPreviousAlwaysOnTop( boolean value )
        {
mPreviousAlwaysOnTop = value;
        }

        /// <summary>
        /// マスターボリュームのUIコントロールを取得します
        /// </summary>
        /// <returns></returns>
        public VolumeTracker getVolumeTrackerMaster()
        {
return volumeMaster;
        }

        /// <summary>
        /// 指定したトラックのボリュームのUIコントロールを取得します
        /// </summary>
        /// <param name="track"></param>
        /// <returns></returns>
        public VolumeTracker getVolumeTracker( int track )
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( 1 <= track && track < vsq.Track.size() &&
     0 <= track - 1 && track - 1 < m_tracker.size() ) {
    return m_tracker.get( track - 1 );
} else if ( track == 0 ) {
    return volumeMaster;
} else {
    return null;
}
        }

        /// <summary>
        /// 指定したBGMのボリュームのUIコントロールを取得します
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public VolumeTracker getVolumeTrackerBgm( int index )
        {
VsqFileEx vsq = AppManager.getVsqFile();
int offset = vsq.Track.size() - 1;
if ( 0 <= index + offset && index + offset < m_tracker.size() ) {
    return m_tracker.get( index + offset );
} else {
    return null;
}
        }

        /// <summary>
        /// ソロ，ミュートのボタンのチェック状態を更新します
        /// </summary>
        private void updateSoloMute()
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
// マスター
boolean masterMuted = vsq.getMasterMute();
volumeMaster.setMuted( masterMuted );

// VSQのトラック
boolean soloSpecificationExists = false; // 1トラックでもソロ指定があればtrue
for ( int i = 1; i < vsq.Track.size(); i++ ) {
    if ( vsq.getSolo( i ) ) {
        soloSpecificationExists = true;
        break;
    }
}
for ( int track = 1; track < vsq.Track.size(); track++ ) {
    if ( soloSpecificationExists ) {
        if ( vsq.getSolo( track ) ) {
            m_tracker.get( track - 1 ).setSolo( true );
            m_tracker.get( track - 1 ).setMuted( masterMuted ? true : vsq.getMute( track ) );
        } else {
            m_tracker.get( track - 1 ).setSolo( false );
            m_tracker.get( track - 1 ).setMuted( true );
        }
    } else {
        m_tracker.get( track - 1 ).setSolo( vsq.getSolo( track ) );
        m_tracker.get( track - 1 ).setMuted( masterMuted ? true : vsq.getMute( track ) );
    }
}

// BGM
int offset = vsq.Track.size() - 1;
for ( int i = 0; i < vsq.BgmFiles.size(); i++ ) {
    m_tracker.get( offset + i ).setMuted( masterMuted ? true : vsq.BgmFiles.get( i ).mute == 1 );
}

this.repaint();
        }

        public void applyShortcut( KeyStroke shortcut )
        {
menuVisualReturn.setAccelerator( shortcut );
        }

        public void applyLanguage()
        {
setTitle( gettext( "Mixer" ) );
        }

        /// <summary>
        /// 現在のシーケンスの状態に応じて，ミキサーウィンドウの状態を更新します
        /// </summary>
        public void updateStatus()
        {
VsqFileEx vsq = AppManager.getVsqFile();
int num = vsq.Mixer.Slave.size() + AppManager.getBgmCount();
if ( m_tracker == null ) {
    m_tracker = new Vector<VolumeTracker>();
}

// イベントハンドラをいったん解除する
unregisterEventHandlers();

// panelに追加済みのものをいったん除去する
for( int i = 0; i < m_tracker.size(); i++ ){
    panelSlaves.remove( m_tracker.get( i ) );
}

// trackerの総数が変化したかどうか
boolean num_changed = (m_tracker.size() != num);

// trackerに過不足があれば数を調節
if ( m_tracker.size() < num ) {
    int remain = num - m_tracker.size();
    for ( int i = 0; i < remain; i++ ) {
        VolumeTracker item = new VolumeTracker();
        m_tracker.add( item );
    }
} else if ( m_tracker.size() > num ) {
    int delete = m_tracker.size() - num;
    for ( int i = 0; i < delete; i++ ) {
        int indx = m_tracker.size() - 1;
        VolumeTracker tr = m_tracker.get( indx );
        m_tracker.removeElementAt( indx );
    }
}

// 同時に表示できるVolumeTrackerの個数を計算
int max = PortUtil.getWorkingArea( this ).width;
int bordersize = 4;// TODO: ここもともとは SystemInformation.FrameBorderSize;だった
int max_client_width = max - 2 * bordersize;
int max_num = (int)Math.floor( max_client_width / (VolumeTracker.WIDTH + 1.0f) );
num++;

int screen_num = num <= max_num ? num : max_num; //スクリーン上に表示するVolumeTrackerの個数

// panelSlaves上に配置するVolumeTrackerの個数
int num_vtracker_on_panel = vsq.Mixer.Slave.size() + AppManager.getBgmCount();
// panelSlaves上に一度に表示可能なVolumeTrackerの個数
int panel_capacity = max_num - 1;


int j = -1;
for ( Iterator<VsqMixerEntry> itr = vsq.Mixer.Slave.iterator(); itr.hasNext(); ) {
    VsqMixerEntry vme = itr.next();
    j++;
    VolumeTracker tracker = m_tracker.get( j );
    tracker.setFeder( vme.Feder );
    tracker.setPanpot( vme.Panpot );
    tracker.setTitle( vsq.Track.get( j + 1 ).getName() );
    tracker.setNumber( (j + 1) + "" );
    tracker.setLocation( j * (VolumeTracker.WIDTH + 1), 0 );
    tracker.setSoloButtonVisible( true );
    tracker.setMuted( (vme.Mute == 1) );
    tracker.setSolo( (vme.Solo == 1) );
    tracker.setTrack( j + 1 );
    tracker.setSoloButtonVisible( true );
    tracker.setPreferredSize( new Dimension( VolumeTracker.WIDTH, VolumeTracker.HEIGHT ) );
    addToPanelSlaves( tracker, j );
}
int count = AppManager.getBgmCount();
for ( int i = 0; i < count; i++ ) {
    j++;
    BgmFile item = AppManager.getBgm( i );
    VolumeTracker tracker = m_tracker.get( j );
    tracker.setFeder( item.feder );
    tracker.setPanpot( item.panpot );
    tracker.setTitle( PortUtil.getFileName( item.file ) );
    tracker.setNumber( "" );
    tracker.setLocation( j * (VolumeTracker.WIDTH + 1), 0 );
    tracker.setSoloButtonVisible( false );
    tracker.setMuted( (item.mute == 1) );
    tracker.setSolo( false );
    tracker.setTrack( -i - 1 );
    tracker.setSoloButtonVisible( false );
    tracker.setPreferredSize( new Dimension( VolumeTracker.WIDTH, VolumeTracker.HEIGHT ) );
    addToPanelSlaves( tracker, j );
}
volumeMaster.setFeder( vsq.Mixer.MasterFeder );
volumeMaster.setPanpot( vsq.Mixer.MasterPanpot );
volumeMaster.setSoloButtonVisible( false );

updateSoloMute();

// イベントハンドラを再登録
reregisterEventHandlers();

// ウィンドウのサイズを更新（必要なら）
if( num_changed ){
    this.setResizable( true );
    scrollSlaves.setPreferredSize( new Dimension( (VolumeTracker.WIDTH + 1) * (screen_num - 1), VolumeTracker.HEIGHT ) );
    JPanel c = getJContentPane();
    Dimension size = c.getSize();
    Rectangle rc = new Rectangle();
    rc = c.getBounds( rc );
    int xdiff = this.getWidth() - rc.width;
    int ydiff = this.getHeight() - rc.height;
    int w = screen_num * (VolumeTracker.WIDTH + 1) + 3;
    int h = VolumeTracker.HEIGHT + SCROLL_HEIGHT;
    pack();
    this.setResizable( false );
}
        }

        private void addToPanelSlaves( VolumeTracker item, int ix )
        {
GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = ix;
gbc.gridy = 0;
gbc.weightx = 1.0D;
gbc.weighty = 1.0D;
gbc.anchor = GridBagConstraints.WEST;
gbc.fill = GridBagConstraints.VERTICAL;
panelSlaves.add( item, gbc );
        }

        private static String gettext( String id )
        {
return Messaging.getMessage( id );
        }

        private void unregisterEventHandlers()
        {
int size = 0;
if ( m_tracker != null ) {
    size = m_tracker.size();
}
for ( int i = 0; i < size; i++ ) {
    VolumeTracker item = m_tracker.get( i );
    item.panpotChangedEvent.remove( new PanpotChangedEventHandler( this, "FormMixer_PanpotChanged" ) );
    item.federChangedEvent.remove( new FederChangedEventHandler( this, "FormMixer_FederChanged" ) );
    item.muteButtonClickEvent.remove( new BEventHandler( this, "FormMixer_MuteButtonClick" ) );
    item.soloButtonClickEvent.remove( new BEventHandler( this, "FormMixer_SoloButtonClick" ) );
}
volumeMaster.panpotChangedEvent.remove( new PanpotChangedEventHandler( this, "volumeMaster_PanpotChanged" ) );
volumeMaster.federChangedEvent.remove( new FederChangedEventHandler( this, "volumeMaster_FederChanged" ) );
volumeMaster.muteButtonClickEvent.remove( new BEventHandler( this, "volumeMaster_MuteButtonClick" ) );
        }

        /// <summary>
        /// ボリューム用のイベントハンドラを再登録します
        /// </summary>
        private void reregisterEventHandlers()
        {
int size = 0;
if ( m_tracker != null ) {
    size = m_tracker.size();
}
for ( int i = 0; i < size; i++ ) {
    VolumeTracker item = m_tracker.get( i );
    item.panpotChangedEvent.add( new PanpotChangedEventHandler( this, "FormMixer_PanpotChanged" ) );
    item.federChangedEvent.add( new FederChangedEventHandler( this, "FormMixer_FederChanged" ) );
    item.muteButtonClickEvent.add( new BEventHandler( this, "FormMixer_MuteButtonClick" ) );
    item.soloButtonClickEvent.add( new BEventHandler( this, "FormMixer_SoloButtonClick" ) );
}
volumeMaster.panpotChangedEvent.add( new PanpotChangedEventHandler( this, "volumeMaster_PanpotChanged" ) );
volumeMaster.federChangedEvent.add( new FederChangedEventHandler( this, "volumeMaster_FederChanged" ) );
volumeMaster.muteButtonClickEvent.add( new BEventHandler( this, "volumeMaster_MuteButtonClick" ) );
        }

        private void registerEventHandlers()
        {
menuVisualReturn.clickEvent.add( new BEventHandler( this, "menuVisualReturn_Click" ) );
this.formClosingEvent.add( new BFormClosingEventHandler( this, "FormMixer_FormClosing" ) );
this.loadEvent.add( new BEventHandler( this, "FormMixer_Load" ) );
reregisterEventHandlers();
        }

        private void setResources()
        {
setIconImage( Resources.get_icon() );
        }

        private void invokePanpotChangedEvent( int track, int panpot )
        {
try{
    panpotChangedEvent.raise( track, panpot );
}catch( Exception ex ){
}
        }

        private void invokeFederChangedEvent( int track, int feder )
        {
try{
    federChangedEvent.raise( track, feder );
}catch( Exception ex ){
}
        }

        private void invokeSoloChangedEvent( int track, boolean solo )
        {
try{
    soloChangedEvent.raise( track, solo );
}catch( Exception ex ){
}
        }

        private void invokeMuteChangedEvent( int track, boolean mute )
        {
try{
    muteChangedEvent.raise( track, mute );
}catch( Exception ex ){
}
        }

        public void FormMixer_Load( Object sender, BEventArgs e )
        {
setAlwaysOnTop( true );
        }
        
        public void FormMixer_PanpotChanged( int track, int panpot )
        {
try {
    invokePanpotChangedEvent( track, panpot );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".FormMixer_PanpotChanged; ex=" + ex + "\n" );
    serr.println( "FormMixer#FormMixer_PanpotChanged; ex=" + ex );
}
        }

        public void FormMixer_FederChanged( int track, int feder )
        {
try {
    invokeFederChangedEvent( track, feder );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".FormMixer_FederChanged; ex=" + ex + "\n" );
    serr.println( "FormMixer#FormMixer_FederChanged; ex=" + ex );
}
        }

        public void FormMixer_SoloButtonClick( Object sender, BEventArgs e )
        {
VolumeTracker parent = (VolumeTracker)sender;
int track = parent.getTrack();
try {
    invokeSoloChangedEvent( track, parent.isSolo() );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".FormMixer_SoloButtonClick; ex=" + ex + "\n" );
    serr.println( "FormMixer#FormMixer_IsSoloChanged; ex=" + ex );
}
updateSoloMute();
        }

        public void FormMixer_MuteButtonClick( Object sender, BEventArgs e )
        {
VolumeTracker parent = (VolumeTracker)sender;
int track = parent.getTrack();
try {
    invokeMuteChangedEvent( track, parent.isMuted() );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".FormMixer_MuteButtonClick; ex=" + ex + "\n" );
    serr.println( "FormMixer#FormMixer_IsMutedChanged; ex=" + ex );
}
updateSoloMute();
        }

        public void menuVisualReturn_Click( Object sender, BEventArgs e )
        {
this.setVisible( false );
        }

        public void FormMixer_FormClosing( Object sender, BFormClosingEventArgs e )
        {
this.setVisible( false );
        }


        public void volumeMaster_FederChanged( int track, int feder )
        {
try {
    invokeFederChangedEvent( 0, feder );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".volumeMaster_FederChanged; ex=" + ex + "\n" );
    serr.println( "FormMixer#volumeMaster_FederChanged; ex=" + ex );
}
        }

        public void volumeMaster_PanpotChanged( int track, int panpot )
        {
try {
    invokePanpotChangedEvent( 0, panpot );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".volumeMaster_PanpotChanged; ex=" + ex + "\n" );
    serr.println( "FormMixer#volumeMaster_PanpotChanged; ex=" + ex );
}
        }

        public void volumeMaster_MuteButtonClick( Object sender, BEventArgs e )
        {
try {
    invokeMuteChangedEvent( 0, volumeMaster.isMuted() );
} catch ( Exception ex ) {
    Logger.write( FormMixer.class + ".volumeMaster_MuteButtonClick; ex=" + ex + "\n" );
    serr.println( "FormMixer#volumeMaster_IsMutedChanged; ex=" + ex );
}
        }


    private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private BPanel panelSlaves = null;
	private VolumeTracker volumeMaster = null;
	private JMenuBar menuMain = null;
    private BMenu menuVisual = null;
    private BMenuItem menuVisualReturn = null;
    private JScrollPane scrollSlaves = null;
    private JLabel jLabel = null;


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(190, 348);
		this.setJMenuBar(getMenuMain());
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridheight = 2;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.weightx = 0.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.anchor = GridBagConstraints.NORTH;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getVolumeMaster(), gridBagConstraints4);
			jContentPane.add(getScrollSlaves(), gridBagConstraints11);
			jContentPane.add(jLabel, gridBagConstraints2);
		}
		return jContentPane;
	}

	/**
	 * This method initializes panelSlaves	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private BPanel getPanelSlaves() {
		if (panelSlaves == null) {
			panelSlaves = new BPanel();
			panelSlaves.setLayout(new GridBagLayout());
			panelSlaves.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			panelSlaves.setBackground(new Color(180, 180, 180));
		}
		return panelSlaves;
	}

	/**
	 * This method initializes volumeMaster	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getVolumeMaster() {
		if (volumeMaster == null) {
			jLabel = new JLabel();
			jLabel.setText(" ");
			jLabel.setPreferredSize(new Dimension(4, 15));
			volumeMaster = new VolumeTracker();
		}
		return volumeMaster;
	}

	/**
     * This method initializes menuMain	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getMenuMain() {
        if (menuMain == null) {
            menuMain = new JMenuBar();
            menuMain.add(getMenuVisual());
        }
        return menuMain;
    }

    /**
     * This method initializes menuVisual	
     * 	
     * @return javax.swing.JMenu	
     */
    private BMenu getMenuVisual() {
        if (menuVisual == null) {
            menuVisual = new BMenu();
            menuVisual.add(getMenuVisualReturn());
        }
        return menuVisual;
    }

    /**
     * This method initializes menuVisualReturn	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuVisualReturn() {
        if (menuVisualReturn == null) {
            menuVisualReturn = new BMenuItem();
        }
        return menuVisualReturn;
    }

    /**
     * This method initializes scrollSlaves	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getScrollSlaves() {
        if (scrollSlaves == null) {
            scrollSlaves = new JScrollPane();
            scrollSlaves.setViewportBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            scrollSlaves.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollSlaves.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollSlaves.setBackground(new Color(180, 180, 180));
            scrollSlaves.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            scrollSlaves.setViewportView(getPanelSlaves());
        }
        return scrollSlaves;
    }


    }


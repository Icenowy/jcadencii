/*
 * FormMainUiImpl.cs
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import org.kbinani.windows.forms.BButton;
import org.kbinani.windows.forms.BForm;
import org.kbinani.windows.forms.BHScrollBar;
import org.kbinani.windows.forms.BLabel;
import org.kbinani.windows.forms.BMenu;
import org.kbinani.windows.forms.BMenuBar;
import org.kbinani.windows.forms.BMenuItem;
import org.kbinani.windows.forms.BPanel;
import org.kbinani.windows.forms.BPopupMenu;
import org.kbinani.windows.forms.BSlider;
import org.kbinani.windows.forms.BSplitPane;
import org.kbinani.windows.forms.BToggleButton;
import org.kbinani.windows.forms.BToolBar;
import org.kbinani.windows.forms.BToolBarButton;
import org.kbinani.windows.forms.BVScrollBar;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiDevice;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.componentmodel.*;
import org.kbinani.media.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;
import org.kbinani.xml.*;


    /// <summary>
    /// エディタのメイン画面クラス
    /// </summary>
    public class FormMain extends BForm implements FormMainUi
    {
        /// <summary>
        /// 特殊なキーの組み合わせのショートカットと、メニューアイテムとの紐付けを保持するクラスです。
        /// </summary>
        private class SpecialShortcutHolder
        {
/// <summary>
/// ショートカットキーを表すKeyStrokeクラスのインスタンス
/// </summary>
public KeyStroke shortcut;
/// <summary>
/// ショートカットキーとの紐付けを行う相手先のメニューアイテム
/// </summary>
public BMenuItem menu;

/// <summary>
/// ショートカットキーとメニューアイテムを指定したコンストラクタ
/// </summary>
/// <param name="shortcut">ショートカットキー</param>
/// <param name="menu">ショートカットキーとの紐付けを行うメニューアイテム</param>
public SpecialShortcutHolder( KeyStroke shortcut, BMenuItem menu )
{
    this.shortcut = shortcut;
    this.menu = menu;
}
        }


        /// <summary>
        /// ピアノロールでの，音符の塗りつぶし色
        /// </summary>
        public static final Color mColorNoteFill = new Color( 181, 220, 86 );
        private final Color mColorR105G105B105 = new Color( 105, 105, 105 );
        private final Color mColorR187G187B255 = new Color( 187, 187, 255 );
        private final Color mColorR007G007B151 = new Color( 7, 7, 151 );
        private final Color mColorR065G065B065 = new Color( 65, 65, 65 );
        private final Color mColorTextboxBackcolor = new Color( 128, 128, 128 );
        private final Color mColorR214G214B214 = new Color( 214, 214, 214 );
        private final AuthorListEntry[] _CREDIT = new AuthorListEntry[]{
new AuthorListEntry( "instanceof developped by:", 2 ),
new AuthorListEntry( "kbinani", "@kbinani" ),
new AuthorListEntry( "修羅場P", "@shurabaP" ),
new AuthorListEntry( "もみじぱん", "@momijipan" ),
new AuthorListEntry( "結晶", "@gondam" ),
new AuthorListEntry( "" ),
new AuthorListEntry(),
new AuthorListEntry(),
new AuthorListEntry( "Special Thanks to", 3 ),
new AuthorListEntry(),
new AuthorListEntry( "tool icons designer:", 2 ),
new AuthorListEntry( "Yusuke KAMIYAMANE", "@ykamiyamane" ),
new AuthorListEntry(),
new AuthorListEntry( "developper of WORLD:", 2 ),
new AuthorListEntry( "Masanori MORISE", "@m_morise" ),
new AuthorListEntry(),
new AuthorListEntry( "developper of v.Connect-STAND:", 2 ),
new AuthorListEntry( "修羅場P", "@shurabaP" ),
new AuthorListEntry(),
new AuthorListEntry( "developper of UTAU:", 2 ),
new AuthorListEntry( "飴屋/菖蒲", "@ameyaP_" ),
new AuthorListEntry(),
new AuthorListEntry( "developper of RebarDotNet:", 2 ),
new AuthorListEntry( "Anthony Baraff" ),
new AuthorListEntry(),
new AuthorListEntry( "promoter:", 2 ),
new AuthorListEntry( "zhuo", "@zhuop" ),
new AuthorListEntry(),
new AuthorListEntry( "library tester:", 2 ),
new AuthorListEntry( "evm" ),
new AuthorListEntry( "そろそろP" ),
new AuthorListEntry( "めがね１１０" ),
new AuthorListEntry( "上総" ),
new AuthorListEntry( "NOIKE", "@knoike" ),
new AuthorListEntry( "逃亡者" ),
new AuthorListEntry(),
new AuthorListEntry( "translator:", 2 ),
new AuthorListEntry( "Eji (zh-TW translation)", "@ejiwarp" ),
new AuthorListEntry( "kankan (zh-TW translation)" ),
new AuthorListEntry( "yxmline (zh-CN translation)" ),
new AuthorListEntry( "BubblyYoru (en translation)", "@BubblyYoru" ),
new AuthorListEntry( "BeForU (kr translation)", "@BeForU" ),
new AuthorListEntry(),
new AuthorListEntry(),
new AuthorListEntry( "Thanks to", 3 ),
new AuthorListEntry(),
new AuthorListEntry( "ないしょの人" ),
new AuthorListEntry( "naquadah" ),
new AuthorListEntry( "1zo" ),
new AuthorListEntry( "Amby" ),
new AuthorListEntry( "ケロッグ" ),
new AuthorListEntry( "beginner" ),
new AuthorListEntry( "b2ox", "@b2ox" ),
new AuthorListEntry( "麻太郎" ),
new AuthorListEntry( "PEX", "@pex_zeo" ),
new AuthorListEntry( "やなぎがうら" ),
new AuthorListEntry( "cocoonP", "@cocoonP" ),
new AuthorListEntry( "かつ" ),
new AuthorListEntry( "ちゃそ", "@marimarikerori" ),
new AuthorListEntry( "ちょむ" ),
new AuthorListEntry( "whimsoft" ),
new AuthorListEntry( "kitiketao", "@okoktaokokta" ),
new AuthorListEntry( "カプチ２" ),
new AuthorListEntry( "あにぃ" ),
new AuthorListEntry( "tomo" ),
new AuthorListEntry( "ナウ□マP", "@now_romaP" ),
new AuthorListEntry( "内藤　魅亜", "@mianaito" ),
new AuthorListEntry( "空茶", "@maizeziam" ),
new AuthorListEntry( "いぬくま" ),
new AuthorListEntry( "shu-t", "@shu_sonicwave" ),
new AuthorListEntry( "さささ", "@sasasa3396" ),
new AuthorListEntry( "あろも～ら", "@aromora" ),
new AuthorListEntry( "空耳P", "@soramiku" ),
new AuthorListEntry( "kotoi" ),
new AuthorListEntry( "げっぺータロー", "@geppeitaro" ),
new AuthorListEntry( "みけCAT", "@mikecat_mixc" ),
new AuthorListEntry( "ぎんじ" ),
new AuthorListEntry( "BeForU", "@BeForU" ),
new AuthorListEntry( "all members of Cadencii bbs", 2 ),
new AuthorListEntry(),
new AuthorListEntry( "     ... and you !", 3 ),
        };

        /// <summary>
        /// カーブエディタ画面の編集モード
        /// </summary>
        enum CurveEditMode
        {
/// <summary>
/// 何もしていない
/// </summary>
NONE,
/// <summary>
/// 鉛筆ツールで編集するモード
/// </summary>
EDIT,
/// <summary>
/// ラインツールで編集するモード
/// </summary>
LINE,
/// <summary>
/// 鉛筆ツールでVELを編集するモード
/// </summary>
EDIT_VEL,
/// <summary>
/// ラインツールでVELを編集するモード
/// </summary>
LINE_VEL,
/// <summary>
/// 真ん中ボタンでドラッグ中
/// </summary>
MIDDLE_DRAG,
        }

        enum ExtDragXMode
        {
RIGHT,
LEFT,
NONE,
        }

        enum ExtDragYMode
        {
UP,
DOWN,
NONE,
        }

        enum GameControlMode
        {
DISABLED,
NORMAL,
KEYBOARD,
CURSOR,
        }

        enum PositionIndicatorMouseDownMode
        {
NONE,
MARK_START,
MARK_END,
TEMPO,
TIMESIG,
        }

        /// <summary>
        /// スクロールバーの最小サイズ(ピクセル)
        /// </summary>
        public static final int MIN_BAR_ACTUAL_LENGTH = 17;
        /// <summary>
        /// エントリの端を移動する時の、ハンドル許容範囲の幅
        /// </summary>
        public static final int _EDIT_HANDLE_WIDTH = 7;
        public static final int _TOOL_BAR_HEIGHT = 46;
        /// <summary>
        /// 単音プレビュー時に、wave生成完了を待つ最大の秒数
        /// </summary>
        public static final double _WAIT_LIMIT = 5.0;
        public static final String _APP_NAME = "Cadencii";
        /// <summary>
        /// 表情線の先頭部分のピクセル幅
        /// </summary>
        public static final int _PX_ACCENT_HEADER = 21;
        public static final String _VERSION_HISTORY_URL = "http://www.ne.jp/asahi/kbinani/home/cadencii/version_history.xml";
        /// <summary>
        /// splitContainer2.Panel2の最小サイズ
        /// </summary>
        public static final int _SPL2_PANEL2_MIN_HEIGHT = 25;
        /// <summary>
        /// splitContainer*で使用するSplitterWidthプロパティの値
        /// </summary>
        public static final int _SPL_SPLITTER_WIDTH = 9;
        static final int _PICT_POSITION_INDICATOR_HEIGHT = 48;
        static final int _SCROLL_WIDTH = 16;
        /// <summary>
        /// Overviewペインの高さ
        /// </summary>
        public static final int _OVERVIEW_HEIGHT = 50;
        /// <summary>
        /// splitContainerPropertyの最小幅
        /// </summary>
        static final int _PROPERTY_DOCK_MIN_WIDTH = 50;
        /// <summary>
        /// WAVE再生時のバッファーサイズの最大値
        /// </summary>
        static final int MAX_WAVE_MSEC_RESOLUTION = 1000;
        /// <summary>
        /// WAVE再生時のバッファーサイズの最小値
        /// </summary>
        static final int MIN_WAVE_MSEC_RESOLUTION = 100;

        /// <summary>
        /// refreshScreenが呼ばれている最中かどうか
        /// </summary>
        private static boolean mIsRefreshing = false;
        /// <summary>
        /// CTRLキー。MacOSXの場合はMenu
        /// </summary>
        public int s_modifier_key = InputEvent.CTRL_MASK;

        /// <summary>
        /// コントローラ
        /// </summary>
        private FormMainController controller = null;
        public VersionInfo mVersionInfo = null;
        /// <summary>
        /// ボタンがDownされた位置。(座標はpictureBox基準)
        /// </summary>
        public Point mButtonInitial = new Point();
        /// <summary>
        /// 真ん中ボタンがダウンされたときのvscrollのvalue値
        /// </summary>
        public int mMiddleButtonVScroll;
        /// <summary>
        /// 真ん中ボタンがダウンされたときのhscrollのvalue値
        /// </summary>
        public int mMiddleButtonHScroll;
        public boolean mEdited = false;
        /// <summary>
        /// 最後にメイン画面が更新された時刻(秒単位)
        /// </summary>
        private double mLastScreenRefreshedSec;
        /// <summary>
        /// カーブエディタの編集モード
        /// </summary>
        private CurveEditMode mEditCurveMode = CurveEditMode.NONE;
        /// <summary>
        /// ピアノロールの右クリックが表示される直前のマウスの位置
        /// </summary>
        public Point mContextMenuOpenedPosition = new Point();
        /// <summary>
        /// ピアノロールの画面外へのドラッグ時、前回自動スクロール操作を行った時刻
        /// </summary>
        public double mTimerDragLastIgnitted;
        /// <summary>
        /// 画面外への自動スクロールモード
        /// </summary>
        private ExtDragXMode mExtDragX = ExtDragXMode.NONE;
        private ExtDragYMode mExtDragY = ExtDragYMode.NONE;
        /// <summary>
        /// EditMode=MoveEntryで，移動を開始する直前のマウスの仮想スクリーン上の位置
        /// </summary>
        public Point mMouseMoveInit = new Point();
        /// <summary>
        /// EditMode=MoveEntryで，移動を開始する直前のマウスの位置と，音符の先頭との距離(ピクセル)
        /// </summary>
        public int mMouseMoveOffset;
        /// <summary>
        /// マウスが降りているかどうかを表すフラグ．AppManager.isPointerDownedとは別なので注意
        /// </summary>
        public boolean mMouseDowned = false;
        public int mTempoDraggingDeltaClock = 0;
        public int mTimesigDraggingDeltaClock = 0;
        public boolean mMouseDownedTrackSelector = false;
        private ExtDragXMode mExtDragXTrackSelector = ExtDragXMode.NONE;
        public boolean mMouseMoved = false;
        public boolean mLastIsImeModeOn = true;
        public boolean mLastSymbolEditMode = false;
        /// <summary>
        /// 鉛筆のモード
        /// </summary>
        public PencilMode mPencilMode = new PencilMode();
        /// <summary>
        /// ビブラート範囲を編集中の音符のInternalID
        /// </summary>
        public int mVibratoEditingId = -1;
        /// <summary>
        /// このフォームがアクティブ化されているかどうか
        /// </summary>
        public boolean mFormActivated = true;
        private GameControlMode mGameMode = GameControlMode.DISABLED;
        public BTimer mTimer;
        public boolean mLastPovR = false;
        public boolean mLastPovL = false;
        public boolean mLastPovU = false;
        public boolean mLastPovD = false;
        public boolean mLastBtnX = false;
        public boolean mLastBtnO = false;
        public boolean mLastBtnRe = false;
        public boolean mLastBtnTr = false;
        public boolean mLastBtnSelect = false;
        /// <summary>
        /// 前回ゲームコントローラのイベントを処理した時刻
        /// </summary>
        public double mLastEventProcessed;
        public boolean mSpacekeyDowned = false;
        public MidiInDevice mMidiIn = null;
        public FormMidiImExport mDialogMidiImportAndExport = null;
        public TreeMap<EditTool, Cursor> mCursor = new TreeMap<EditTool, Cursor>();
        private Preference mDialogPreference;
        public PropertyPanelContainer mPropertyPanelContainer;

        /// <summary>
        /// PositionIndicatorのマウスモード
        /// </summary>
        private PositionIndicatorMouseDownMode mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
        /// <summary>
        /// AppManager.keyWidthを調節するモードに入ったかどうか
        /// </summary>
        public boolean mKeyLengthSplitterMouseDowned = false;
        /// <summary>
        /// AppManager.keyWidthを調節するモードに入る直前での、マウスのスクリーン座標
        /// </summary>
        public Point mKeyLengthSplitterInitialMouse = new Point();
        /// <summary>
        /// AppManager.keyWidthを調節するモードに入る直前での、keyWidthの値
        /// </summary>
        public int mKeyLengthInitValue = 68;
        /// <summary>
        /// AppManager.keyWidthを調節するモードに入る直前での、trackSelectorのgetRowsPerColumn()の値
        /// </summary>
        public int mKeyLengthTrackSelectorRowsPerColumn = 1;
        /// <summary>
        /// AppManager.keyWidthを調節するモードに入る直前での、splitContainer1のSplitterLocationの値
        /// </summary>
        public int mKeyLengthSplitterDistance = 0;
        public BFileChooser openXmlVsqDialog;
        public BFileChooser saveXmlVsqDialog;
        public BFileChooser openUstDialog;
        public BFileChooser openMidiDialog;
        public BFileChooser saveMidiDialog;
        public BFileChooser openWaveDialog;
        public BTimer timer;
        public BBackgroundWorker bgWorkScreen;
        /// <summary>
        /// アイコンパレットのドラッグ＆ドロップ処理中，一度でもpictPianoRoll内にマウスが入ったかどうか
        /// </summary>
        private boolean mIconPaletteOnceDragEntered = false;
        private byte mMtcFrameLsb;
        private byte mMtcFrameMsb;
        private byte mMtcSecLsb;
        private byte mMtcSecMsb;
        private byte mMtcMinLsb;
        private byte mMtcMinMsb;
        private byte mMtcHourLsb;
        private byte mMtcHourMsb;
        /// <summary>
        /// MTCを最後に受信した時刻
        /// </summary>
        private double mMtcLastReceived = 0.0;
        /// <summary>
        /// 特殊な取り扱いが必要なショートカットのキー列と、対応するメニューアイテムを保存しておくリスト。
        /// </summary>
        private Vector<SpecialShortcutHolder> mSpecialShortcutHolders = new Vector<SpecialShortcutHolder>();
        /// <summary>
        /// 歌詞流し込み用のダイアログ
        /// </summary>
        private FormImportLyric mDialogImportLyric = null;
        /// <summary>
        /// デフォルトのストローク
        /// </summary>
        private BasicStroke mStrokeDefault = null;
        /// <summary>
        /// 描画幅2pxのストローク
        /// </summary>
        private BasicStroke mStroke2px = null;
        /// <summary>
        /// pictureBox2の描画ループで使うグラフィックス
        /// </summary>
        private Graphics2D mGraphicsPictureBox2 = null;
        /// <summary>
        /// 再生中にソングポジションが前進だけしてほしいので，逆行を防ぐために前回のソングポジションを覚えておく
        /// </summary>
        private int mLastClock = 0;
        /// <summary>
        /// PositionIndicatorに表示しているポップアップのクロック位置
        /// </summary>
        private int mPositionIndicatorPopupShownClock;

        /// <summary>
        /// コンストラクタ
        /// </summary>
        /// <param name="file">最初に開くxvsq，vsqファイルのパス</param>
        public FormMain( FormMainController controller, String file )
        {
		    super();
this.controller = controller;
this.controller.setupUi( this );

// 言語設定を反映させる
Messaging.setLanguage( AppManager.editorConfig.Language );

AppManager.propertyPanel = new PropertyPanel();
AppManager.propertyWindow = new FormNoteProperty();
AppManager.propertyWindow.addComponent( AppManager.propertyPanel );

AppManager.superFont10Bold = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.BOLD, AppManager.FONT_SIZE10 );
AppManager.superFont8 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE8 );
AppManager.superFont10 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE10 );
AppManager.superFont9 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE9 );
AppManager.superFont50Bold = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.BOLD, AppManager.FONT_SIZE50 );

s_modifier_key = 
    InputEvent.CTRL_MASK;
VsqFileEx tvsq = 
    new VsqFileEx( 
        AppManager.editorConfig.DefaultSingerName,
        1,
        4,
        4,
        500000 );
RendererKind kind = AppManager.editorConfig.DefaultSynthesizer;
String renderer = AppManager.getVersionStringFromRendererKind( kind );
Vector<VsqID> singers = AppManager.getSingerListFromRendererKind( kind );
tvsq.Track.get( 1 ).changeRenderer( renderer, singers );
AppManager.setVsqFile( tvsq );

trackSelector = new TrackSelector( this ); // initializeで引数なしのコンストラクタが呼ばれるのを予防
//TODO: これはひどい
panelWaveformZoom = (WaveformZoomUiImpl)(new WaveformZoomController( this, waveView )).getUi();

initialize();
timer = new BTimer();
getCMenuPiano();
getCMenuTrackTab();
getCMenuTrackSelector();
// MIDIステップ入力は使えないことにする
//            toolStripTool.remove( getStripBtnStepSequencer() );

panelOverview.setMainForm( this );
pictPianoRoll.setMainForm( this );
bgWorkScreen = new BBackgroundWorker();

openXmlVsqDialog = new BFileChooser();
openXmlVsqDialog.addFileFilter( "VSQ Format(*.vsq)|*.vsq" );
openXmlVsqDialog.addFileFilter( "XML-VSQ Format(*.xvsq)|*.xvsq" );

saveXmlVsqDialog = new BFileChooser();
saveXmlVsqDialog.addFileFilter( "VSQ Format(*.vsq)|*.vsq" );
saveXmlVsqDialog.addFileFilter( "XML-VSQ Format(*.xvsq)|*.xvsq" );
saveXmlVsqDialog.addFileFilter( "All files(*.*)|*.*" );

openUstDialog = new BFileChooser();
openUstDialog.addFileFilter( "UTAU Project File(*.ust)|*.ust" );
openUstDialog.addFileFilter( "All Files(*.*)|*.*" );

openMidiDialog = new BFileChooser();
saveMidiDialog = new BFileChooser();
openWaveDialog = new BFileChooser();

/*mOverviewScaleCount = AppManager.editorConfig.OverviewScaleCount;
mOverviewPixelPerClock = getOverviewScaleX( mOverviewScaleCount );*/

menuVisualOverview.setSelected( AppManager.editorConfig.OverviewEnabled );
mPropertyPanelContainer = new PropertyPanelContainer();

registerEventHandlers();
setResources();

menuSettingPaletteTool.setVisible( false );
menuScript.setVisible( false );
trackSelector.updateVisibleCurves();
trackSelector.setBackground( new Color( 108, 108, 108 ) );
trackSelector.setCurveVisible( true );
trackSelector.setSelectedCurve( CurveType.VEL );
trackSelector.mouseClickEvent.add( new BMouseEventHandler( this, "trackSelector_MouseClick" ) );
trackSelector.mouseUpEvent.add( new BMouseEventHandler( this, "trackSelector_MouseUp" ) );
trackSelector.mouseDownEvent.add( new BMouseEventHandler( this, "trackSelector_MouseDown" ) );
trackSelector.mouseMoveEvent.add( new BMouseEventHandler( this, "trackSelector_MouseMove" ) );
trackSelector.keyDownEvent.add( new BKeyEventHandler( this, "handleSpaceKeyDown" ) );
trackSelector.keyUpEvent.add( new BKeyEventHandler( this, "handleSpaceKeyUp" ) );
trackSelector.previewKeyDownEvent.add( new BPreviewKeyDownEventHandler( this, "trackSelector_PreviewKeyDown" ) );
trackSelector.selectedTrackChangedEvent.add( new SelectedTrackChangedEventHandler( this, "trackSelector_SelectedTrackChanged" ) );
trackSelector.selectedCurveChangedEvent.add( new SelectedCurveChangedEventHandler( this, "trackSelector_SelectedCurveChanged" ) );
trackSelector.renderRequiredEvent.add( new RenderRequiredEventHandler( this, "trackSelector_RenderRequired" ) );
trackSelector.preferredMinHeightChangedEvent.add( new BEventHandler( this, "trackSelector_PreferredMinHeightChanged" ) );
trackSelector.mouseDoubleClickEvent.add( new BMouseEventHandler( this, "trackSelector_MouseDoubleClick" ) );

stripBtnScroll.setSelected( AppManager.mAutoScroll );

applySelectedTool();
applyQuantizeMode();

// Palette Toolの読み込み


splitContainerProperty.setLeftComponent( mPropertyPanelContainer );
mPropertyPanelContainer.setMinimumSize( new Dimension( 0, 0 ) );


updatePropertyPanelState( AppManager.editorConfig.PropertyWindowStatus.State );

pictPianoRoll.mouseWheelEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseWheel" ) );
trackSelector.mouseWheelEvent.add( new BMouseEventHandler( this, "trackSelector_MouseWheel" ) );
picturePositionIndicator.mouseWheelEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseWheel" ) );

menuVisualOverview.checkedChangedEvent.add( new BEventHandler( this, "menuVisualOverview_CheckedChanged" ) );

hScroll.setMaximum( AppManager.getVsqFile().TotalClocks + 240 );
hScroll.setVisibleAmount( 240 * 4 );

vScroll.setMaximum( (int)(controller.getScaleY() * 100 * 128) );
vScroll.setVisibleAmount( 24 * 4 );

trackSelector.setCurveVisible( true );

// inputTextBoxの初期化
AppManager.mInputTextBox = new LyricTextBox( this );
AppManager.mInputTextBox.setVisible( false );
AppManager.mInputTextBox.setSize( 80, 22 );
AppManager.mInputTextBox.setBackground( Color.white );
AppManager.mInputTextBox.setFont( new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, 9 ) );
AppManager.mInputTextBox.setEnabled( false );
AppManager.mInputTextBox.keyPressEvent.add( new BKeyPressEventHandler( this, "mInputTextBox_KeyPress" ) );

int fps = 1000 / AppManager.editorConfig.MaximumFrameRate;
timer.setDelay( (fps <= 0) ? 1 : fps );

menuHelp.remove( menuHelpDebug );


menuHelpLogSwitch.setSelected( Logger.isEnabled() );
applyShortcut();

AppManager.mMixerWindow = new FormMixer( this );
AppManager.iconPalette = new FormIconPalette( this );

// ファイルを開く
if ( !str.compare( file, "" ) ) {
    if ( fsys.isFileExists( file ) ) {
        String low_file = file.toLowerCase();
        if ( low_file.endsWith( ".xvsq" ) ) {
            openVsqCor( low_file );
            //AppManager.readVsq( file );
        } else if ( low_file.endsWith( ".vsq" ) ) {
            VsqFileEx vsq = null;
            try {
                vsq = new VsqFileEx( file, "Shift_JIS" );
                AppManager.setVsqFile( vsq );
                updateBgmMenuState();
            } catch ( Exception ex ) {
                Logger.write( FormMain.class + ".ctor; ex=" + ex + "\n" );
                serr.println( "FormMain#.ctor; ex=" + ex );
            }
        }
    }
}

trackBar.setValue( AppManager.editorConfig.DefaultXScale );
AppManager.setCurrentClock( 0 );
setEdited( false );

AppManager.previewStartedEvent.add( new BEventHandler( this, "AppManager_PreviewStarted" ) );
AppManager.previewAbortedEvent.add( new BEventHandler( this, "AppManager_PreviewAborted" ) );
AppManager.gridVisibleChangedEvent.add( new BEventHandler( this, "AppManager_GridVisibleChanged" ) );
AppManager.itemSelection.selectedEventChangedEvent.add( new SelectedEventChangedEventHandler( this, "ItemSelectionModel_SelectedEventChanged" ) );
AppManager.selectedToolChangedEvent.add( new BEventHandler( this, "AppManager_SelectedToolChanged" ) );
AppManager.updateBgmStatusRequiredEvent.add( new BEventHandler( this, "AppManager_UpdateBgmStatusRequired" ) );
AppManager.mainWindowFocusRequiredEvent.add( new BEventHandler( this, "AppManager_MainWindowFocusRequired" ) );
AppManager.editedStateChangedEvent.add( new EditedStateChangedEventHandler( this, "AppManager_EditedStateChanged" ) );
AppManager.waveViewReloadRequiredEvent.add( new WaveViewRealoadRequiredEventHandler( this, "AppManager_WaveViewRealoadRequired" ) );
EditorConfig.quantizeModeChangedEvent.add( new BEventHandler( this, "handleEditorConfig_QuantizeModeChanged" ) );

mPropertyPanelContainer.stateChangeRequiredEvent.add( new StateChangeRequiredEventHandler( this, "mPropertyPanelContainer_StateChangeRequired" ) );

updateRecentFileMenu();

// C3が画面中央に来るように調整
int draft_start_to_draw_y = 68 * (int)(100 * controller.getScaleY()) - pictPianoRoll.getHeight() / 2;
int draft_vscroll_value = (int)((draft_start_to_draw_y * (double)vScroll.getMaximum()) / (128 * (int)(100 * controller.getScaleY()) - vScroll.getHeight()));
try {
    vScroll.setValue( draft_vscroll_value );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".FormMain_Load; ex=" + ex + "\n" );
}

// x=97がプリメジャークロックになるように調整
int cp = AppManager.getVsqFile().getPreMeasureClocks();
int draft_hscroll_value = (int)(cp - 24.0 * controller.getScaleXInv());
try {
    hScroll.setValue( draft_hscroll_value );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".FormMain_Load; ex=" + ex + "\n" );
}

//s_pen_dashed_171_171_171.DashPattern = new float[] { 3, 3 };
//s_pen_dashed_209_204_172.DashPattern = new float[] { 3, 3 };

menuVisualNoteProperty.setSelected( AppManager.editorConfig.ShowExpLine );
menuVisualLyrics.setSelected( AppManager.editorConfig.ShowLyric );
menuVisualMixer.setSelected( AppManager.editorConfig.MixerVisible );
menuVisualPitchLine.setSelected( AppManager.editorConfig.ViewAtcualPitch );

updateMenuFonts();

AppManager.mMixerWindow.federChangedEvent.add( new FederChangedEventHandler( this, "mixerWindow_FederChanged" ) );
AppManager.mMixerWindow.panpotChangedEvent.add( new PanpotChangedEventHandler( this, "mixerWindow_PanpotChanged" ) );
AppManager.mMixerWindow.muteChangedEvent.add( new MuteChangedEventHandler( this, "mixerWindow_MuteChanged" ) );
AppManager.mMixerWindow.soloChangedEvent.add( new SoloChangedEventHandler( this, "mixerWindow_SoloChanged" ) );
AppManager.mMixerWindow.updateStatus();
if ( AppManager.editorConfig.MixerVisible ) {
    AppManager.mMixerWindow.setVisible( true );
}
AppManager.mMixerWindow.formClosingEvent.add( new BFormClosingEventHandler( this, "mixerWindow_FormClosing" ) );

Point p1 = AppManager.editorConfig.FormIconPaletteLocation.toPoint();
if ( !PortUtil.isPointInScreens( p1 ) ) {
    Rectangle workingArea = PortUtil.getWorkingArea( this );
    p1 = new Point( workingArea.x, workingArea.y );
}
AppManager.iconPalette.setLocation( p1 );
if ( AppManager.editorConfig.IconPaletteVisible ) {
    AppManager.iconPalette.setVisible( true );
}
AppManager.iconPalette.formClosingEvent.add( new BFormClosingEventHandler( this, "iconPalette_FormClosing" ) );
AppManager.iconPalette.locationChangedEvent.add( new BEventHandler( this, "iconPalette_LocationChanged" ) );

trackSelector.commandExecutedEvent.add( new BEventHandler( this, "trackSelector_CommandExecuted" ) );


clearTempWave();
updateVibratoPresetMenu();
mPencilMode.setMode( PencilModeEnum.Off );
updateCMenuPianoFixed();
loadGameControler();
reloadMidiIn();
menuVisualWaveform.setSelected( AppManager.editorConfig.ViewWaveform );

updateRendererMenu();

// ウィンドウの位置・サイズを再現
if ( AppManager.editorConfig.WindowMaximized ) {
    setExtendedState( BForm.MAXIMIZED_BOTH );
} else {
    setExtendedState( BForm.NORMAL );
}
Rectangle bounds = AppManager.editorConfig.WindowRect;
this.setBounds( bounds );
// ウィンドウ位置・サイズの設定値が、使えるディスプレイのどれにも被っていない場合
Rectangle rc2 = PortUtil.getScreenBounds( this );
if ( bounds.x < rc2.x ||
     rc2.x + rc2.width < bounds.x + bounds.width ||
     bounds.y < rc2.y ||
     rc2.y + rc2.height < bounds.y + bounds.height ) {
    bounds.x = rc2.x;
    bounds.y = rc2.y;
    this.setBounds( bounds );
    AppManager.editorConfig.WindowRect = bounds;
}
this.windowStateChangedEvent.add( new BEventHandler( this, "FormMain_WindowStateChanged" ) );
this.locationChangedEvent.add( new BEventHandler( this, "FormMain_LocationChanged" ) );

updateScrollRangeHorizontal();
updateScrollRangeVertical();

// プロパティウィンドウの位置を復元
Rectangle rc1 = PortUtil.getScreenBounds( this );
Rectangle rcScreen = new Rectangle( rc1.x, rc1.y, rc1.width, rc1.height );
Point p = this.getLocation();
XmlRectangle xr = AppManager.editorConfig.PropertyWindowStatus.Bounds;
Point p0 = new Point( xr.x, xr.y );
Point a = new Point( p.x + p0.x, p.y + p0.y );
Rectangle rc = new Rectangle( a.x,
                              a.y,
                              AppManager.editorConfig.PropertyWindowStatus.Bounds.getWidth(),
                              AppManager.editorConfig.PropertyWindowStatus.Bounds.getHeight() );

if ( a.y > rcScreen.y + rcScreen.height ) {
    a = new Point( a.x, rcScreen.y + rcScreen.height - rc.height );
}
if ( a.y < rcScreen.y ) {
    a = new Point( a.x, rcScreen.y );
}
if ( a.x > rcScreen.x + rcScreen.width ) {
    a = new Point( rcScreen.x + rcScreen.width - rc.width, a.y );
}
if ( a.x < rcScreen.x ) {
    a = new Point( rcScreen.x, a.y );
}

AppManager.propertyWindow.setBounds( a.x, a.y, rc.width, rc.height );
AppManager.propertyWindow.windowStateChangedEvent.add( new BEventHandler( this, "propertyWindow_WindowStateChanged" ) );
AppManager.propertyWindow.locationChangedEvent.add( new BEventHandler( this, "propertyWindow_LocationOrSizeChanged" ) );
AppManager.propertyWindow.sizeChangedEvent.add( new BEventHandler( this, "propertyWindow_LocationOrSizeChanged" ) );
AppManager.propertyWindow.formClosingEvent.add( new BFormClosingEventHandler( this, "propertyWindow_FormClosing" ) );
AppManager.propertyPanel.commandExecuteRequiredEvent.add( new CommandExecuteRequiredEventHandler( this, "propertyPanel_CommandExecuteRequired" ) );
updateBgmMenuState();
AppManager.mLastTrackSelectorHeight = trackSelector.getPreferredMinSize();
flipControlCurveVisible( true );

repaint();
updateLayout();
menuHidden.setVisible( false );


menuTrackRenderer.remove( menuTrackRendererAquesTone );
cMenuTrackTabRenderer.remove( cMenuTrackTabRendererAquesTone );

menuVisual.remove( menuVisualPluginUi );
menuSetting.remove( menuSettingGameControler );


        }


        public void focusPianoRoll()
        {
pictPianoRoll.requestFocus();
        }


        /// <summary>
        /// 指定した歌手とリサンプラーについて，設定値に登録されていないものだったら登録する．
        /// </summary>
        /// <param name="resampler_path"></param>
        /// <param name="singer_path"></param>
        private void checkUnknownResamplerAndSinger( ByRef<String> resampler_path, ByRef<String> singer_path )
        {
String utau = Utility.getExecutingUtau();
String utau_dir = "";
if ( !str.compare( utau, "" ) ) {
    utau_dir = PortUtil.getDirectoryName( utau );
}

// 可能なら，VOICEの文字列を置換する
String search = "%VOICE%";
if ( str.startsWith( singer_path.value, search ) && str.length( singer_path.value ) > str.length( search ) ) {
    singer_path.value = str.sub( singer_path.value, str.length( search ) );
    singer_path.value = fsys.combine( fsys.combine( utau_dir, "voice" ), singer_path.value );
}

// 歌手はknownかunknownか？
// 歌手指定が知らない歌手だった場合に，ダイアログを出すかどうか
boolean check_unknown_singer = false;
if ( fsys.isFileExists( fsys.combine( singer_path.value, "oto.ini" ) ) ) {
    // oto.iniが存在する場合
    // editorConfigに入っていない場合に，ダイアログを出す
    boolean found = false;
    for ( int i = 0; i < vec.size( AppManager.editorConfig.UtauSingers ); i++ ) {
        SingerConfig sc = vec.get( AppManager.editorConfig.UtauSingers, i );
        if ( sc == null ) {
            continue;
        }
        if ( str.compare( sc.VOICEIDSTR, singer_path.value ) ) {
            found = true;
            break;
        }
    }
    check_unknown_singer = !found;
}

// リサンプラーが知っているやつかどうか
boolean check_unknwon_resampler = false;
String resampler_dir = PortUtil.getDirectoryName( resampler_path.value );
if ( str.compare( resampler_dir, "" ) ) {
    // ディレクトリが空欄なので，UTAUのデフォルトのリサンプラー指定である
    resampler_path.value = fsys.combine( utau_dir, resampler_path.value );
    resampler_dir = PortUtil.getDirectoryName( resampler_path.value );
}
if ( !str.compare( resampler_dir, "" ) &&  fsys.isFileExists( resampler_path.value ) ) {
    boolean found = false;
    for ( int i = 0; i < AppManager.editorConfig.getResamplerCount(); i++ ) {
        String resampler = AppManager.editorConfig.getResamplerAt( i );
        if ( str.compare( resampler, resampler_path.value ) ) {
            found = true;
            break;
        }
    }
    check_unknwon_resampler = !found;
}

// unknownな歌手やリサンプラーが発見された場合.
// 登録するかどうか問い合わせるダイアログを出す
FormCheckUnknownSingerAndResampler dialog = null;
try {
    if ( check_unknown_singer || check_unknwon_resampler ) {
        dialog = new FormCheckUnknownSingerAndResampler( singer_path.value, check_unknown_singer, resampler_path.value, check_unknwon_resampler );
        dialog.setLocation( getFormPreferedLocation( dialog ) );
        BDialogResult dr = AppManager.showModalDialog( dialog, this );
        if ( dr != BDialogResult.OK ) {
            return;
        }

        // 登録する
        // リサンプラー
        if ( dialog.isResamplerChecked() ) {
            String path = dialog.getResamplerPath();
            if ( fsys.isFileExists( path ) ) {
                AppManager.editorConfig.addResampler( path, false );
            }
        }
        // 歌手
        if ( dialog.isSingerChecked() ) {
            String path = dialog.getSingerPath();
            if ( fsys.isDirectoryExists( path ) ) {
                SingerConfig sc = new SingerConfig();
                Utility.readUtauSingerConfig( path, sc );
                vec.add( AppManager.editorConfig.UtauSingers, sc );
            }
            AppManager.reloadUtauVoiceDB();
        }
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".checkUnknownResamplerAndSinger; ex=" + ex + "\n" );
} finally {
    if ( dialog != null ) {
        try {
            dialog.close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        /// <summary>
        /// ピアノロールの縦軸の拡大率をdelta段階上げます
        /// </summary>
        /// <param name="delta"></param>
        private void zoomY( int delta )
        {
int scaley = AppManager.editorConfig.PianoRollScaleY;
int draft = scaley + delta;
if ( draft < EditorConfig.MIN_PIANOROLL_SCALEY ) {
    draft = EditorConfig.MIN_PIANOROLL_SCALEY;
}
if ( EditorConfig.MAX_PIANOROLL_SCALEY < draft ) {
    draft = EditorConfig.MAX_PIANOROLL_SCALEY;
}
if ( scaley != draft ) {
    AppManager.editorConfig.PianoRollScaleY = draft;
    updateScrollRangeVertical();
    controller.setStartToDrawY( calculateStartToDrawY( vScroll.getValue() ) );
    updateDrawObjectList();
}
        }

        /// <summary>
        /// ズームスライダの現在の値から，横方向の拡大率を計算します
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        private float getScaleXFromTrackBarValue( int value )
        {
return value / 480.0f;
        }

        /// <summary>
        /// ユーザー定義のビブラートのプリセット関係のメニューの表示状態を更新します
        /// </summary>
        private void updateVibratoPresetMenu()
        {
menuLyricCopyVibratoToPreset.removeAll();
int size = AppManager.editorConfig.AutoVibratoCustom.size();
for ( int i = 0; i < size; i++ ) {
    VibratoHandle handle = AppManager.editorConfig.AutoVibratoCustom.get( i );
    BMenuItem item = new BMenuItem();
    item.setText( handle.getCaption() );
    item.clickEvent.add( new BEventHandler( this, "handleVibratoPresetSubelementClick" ) );
    menuLyricCopyVibratoToPreset.add( item );
}
        }

        /// <summary>
        /// MIDIステップ入力中に，ソングポジションが動いたときの処理を行います
        /// AppManager.mAddingEventが非nullの時，音符の先頭は決まっているので，
        /// ソングポジションと，音符の先頭との距離から音符の長さを算出し，更新する
        /// AppManager.mAddingEventがnullの時は何もしない
        /// </summary>
        private void updateNoteLengthStepSequencer()
        {
if ( !controller.isStepSequencerEnabled() ) {
    return;
}

VsqEvent item = AppManager.mAddingEvent;
if ( item == null ) {
    return;
}

int song_position = AppManager.getCurrentClock();
int start = item.Clock;
int length = song_position - start;
if ( length < 0 ) length = 0;
Utility.editLengthOfVsqEvent(
    item,
    length,
    AppManager.vibratoLengthEditingRule );
        }

        /// <summary>
        /// 現在追加しようとしている音符の内容(AppManager.mAddingEvent)をfixします
        /// </summary>
        /// <returns></returns>
        private void fixAddingEvent()
        {
VsqFileEx vsq = AppManager.getVsqFile();
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
LyricHandle lyric = new LyricHandle( "あ", "a" );
VibratoHandle vibrato = null;
int vibrato_delay = 0;
if ( AppManager.editorConfig.EnableAutoVibrato ) {
    int note_length = AppManager.mAddingEvent.ID.getLength();
    // 音符位置での拍子を調べる
    Timesig timesig = vsq.getTimesigAt( AppManager.mAddingEvent.Clock );

    // ビブラートを自動追加するかどうかを決める閾値
    int threshold = AppManager.editorConfig.AutoVibratoThresholdLength;
    if ( note_length >= threshold ) {
        int vibrato_clocks = 0;
        if ( AppManager.editorConfig.DefaultVibratoLength == DefaultVibratoLengthEnum.L100 ) {
            vibrato_clocks = note_length;
        } else if ( AppManager.editorConfig.DefaultVibratoLength == DefaultVibratoLengthEnum.L50 ) {
            vibrato_clocks = note_length / 2;
        } else if ( AppManager.editorConfig.DefaultVibratoLength == DefaultVibratoLengthEnum.L66 ) {
            vibrato_clocks = note_length * 2 / 3;
        } else if ( AppManager.editorConfig.DefaultVibratoLength == DefaultVibratoLengthEnum.L75 ) {
            vibrato_clocks = note_length * 3 / 4;
        }
        SynthesizerType type = SynthesizerType.VOCALOID2;
        RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
        if ( kind == RendererKind.VOCALOID1 ) {
            type = SynthesizerType.VOCALOID1;
        }
        vibrato = AppManager.editorConfig.createAutoVibrato( type, vibrato_clocks );
        vibrato_delay = note_length - vibrato_clocks;
    }
}

// oto.iniの設定を反映
VsqEvent item = vsq_track.getSingerEventAt( AppManager.mAddingEvent.Clock );
SingerConfig singerConfig = null;
if ( item != null && item.ID != null && item.ID.IconHandle != null ) {
    singerConfig = AppManager.getSingerInfoUtau( item.ID.IconHandle.Language, item.ID.IconHandle.Program );
}

if ( singerConfig != null && AppManager.mUtauVoiceDB.containsKey( singerConfig.VOICEIDSTR ) ) {
    UtauVoiceDB utauVoiceDb = AppManager.mUtauVoiceDB.get( singerConfig.VOICEIDSTR );
    OtoArgs otoArgs = utauVoiceDb.attachFileNameFromLyric( lyric.L0.Phrase );
    AppManager.mAddingEvent.UstEvent.setPreUtterance( otoArgs.msPreUtterance );
    AppManager.mAddingEvent.UstEvent.setVoiceOverlap( otoArgs.msOverlap );
}

// 自動ノーマライズのモードで、処理を分岐
if ( AppManager.mAutoNormalize ) {
    VsqTrack work = (VsqTrack)vsq_track.clone();
    AppManager.mAddingEvent.ID.type = VsqIDType.Anote;
    AppManager.mAddingEvent.ID.Dynamics = 64;
    AppManager.mAddingEvent.ID.VibratoHandle = vibrato;
    AppManager.mAddingEvent.ID.LyricHandle = lyric;
    AppManager.mAddingEvent.ID.VibratoDelay = vibrato_delay;

    boolean changed = true;
    while ( changed ) {
        changed = false;
        for ( int i = 0; i < work.getEventCount(); i++ ) {
            int start_clock = work.getEvent( i ).Clock;
            int end_clock = work.getEvent( i ).ID.getLength() + start_clock;
            if ( start_clock < AppManager.mAddingEvent.Clock && AppManager.mAddingEvent.Clock < end_clock ) {
                work.getEvent( i ).ID.setLength( AppManager.mAddingEvent.Clock - start_clock );
                changed = true;
            } else if ( start_clock == AppManager.mAddingEvent.Clock ) {
                work.removeEvent( i );
                changed = true;
                break;
            } else if ( AppManager.mAddingEvent.Clock < start_clock && start_clock < AppManager.mAddingEvent.Clock + AppManager.mAddingEvent.ID.getLength() ) {
                AppManager.mAddingEvent.ID.setLength( start_clock - AppManager.mAddingEvent.Clock );
                changed = true;
            }
        }
    }
    VsqEvent add = (VsqEvent)AppManager.mAddingEvent.clone();
    work.addEvent( add );
    CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected,
                                                                 work,
                                                                 AppManager.getVsqFile().AttachedCurves.get( selected - 1 ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
} else {
    VsqEvent[] items = new VsqEvent[1];
    AppManager.mAddingEvent.ID.type = VsqIDType.Anote;
    AppManager.mAddingEvent.ID.Dynamics = 64;
    items[0] = (VsqEvent)AppManager.mAddingEvent.clone();// new VsqEvent( 0, AppManager.addingEvent.ID );
    items[0].Clock = AppManager.mAddingEvent.Clock;
    items[0].ID.LyricHandle = lyric;
    items[0].ID.VibratoDelay = vibrato_delay;
    items[0].ID.VibratoHandle = vibrato;

    CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandEventAddRange( AppManager.getSelected(), items ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
}
        }

        /// <summary>
        /// 現在のツールバーの場所を保存します
        /// </summary>
        private void saveToolbarLocation()
        {
// TODO:
        }


        private static int doQuantize( int clock, int unit )
        {
int odd = clock % unit;
int new_clock = clock - odd;
if ( odd > unit / 2 ) {
    new_clock += unit;
}
return new_clock;
        }

        /// <summary>
        /// デフォルトのストロークを取得します
        /// </summary>
        /// <returns></returns>
        private BasicStroke getStrokeDefault()
        {
if ( mStrokeDefault == null ) {
    mStrokeDefault = new BasicStroke();
}
return mStrokeDefault;
        }

        /// <summary>
        /// 描画幅が2pxのストロークを取得します
        /// </summary>
        /// <returns></returns>
        private BasicStroke getStroke2px()
        {
if ( mStroke2px == null ) {
    mStroke2px = new BasicStroke( 2.0f );
}
return mStroke2px;
        }

        /// <summary>
        /// 選択された音符の長さを、指定したゲートタイム分長くします。
        /// </summary>
        /// <param name="delta_length"></param>
        private void lengthenSelectedEvent( int delta_length )
        {
if ( delta_length == 0 ) {
    return;
}

VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}

int selected = AppManager.getSelected();

Vector<VsqEvent> items = new Vector<VsqEvent>();
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    if ( item.editing.ID.type != VsqIDType.Anote &&
         item.editing.ID.type != VsqIDType.Aicon ) {
        continue;
    }

    // クレッシェンド、デクレッシェンドでないものを省く
    if ( item.editing.ID.type == VsqIDType.Aicon ) {
        if ( item.editing.ID.IconDynamicsHandle == null ) {
            continue;
        }
        if ( !item.editing.ID.IconDynamicsHandle.isCrescendType() &&
             !item.editing.ID.IconDynamicsHandle.isDecrescendType() ) {
            continue;
        }
    }

    // 長さを変える。0未満になると0に直す
    int length = item.editing.ID.getLength();
    int draft = length + delta_length;
    if ( draft < 0 ) {
        draft = 0;
    }
    if ( length == draft ) {
        continue;
    }

    // ビブラートの長さを変更
    VsqEvent add = (VsqEvent)item.editing.clone();
    Utility.editLengthOfVsqEvent( add, draft, AppManager.vibratoLengthEditingRule );
    items.add( add );
}

if ( items.size() <= 0 ) {
    return;
}

// コマンドを発行
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandEventReplaceRange(
        selected, items.toArray( new VsqEvent[] { } ) ) );
AppManager.editHistory.register( vsq.executeCommand( run ) );

// 編集されたものを再選択する
for ( Iterator<VsqEvent> itr = items.iterator(); itr.hasNext(); ) {
    VsqEvent item = itr.next();
    AppManager.itemSelection.addEvent( item.InternalID );
}

// 編集が施された。
setEdited( true );
updateDrawObjectList();

refreshScreen();
        }

        /// <summary>
        /// 選択された音符の音程とゲートタイムを、指定されたノートナンバーおよびゲートタイム分上下させます。
        /// </summary>
        /// <param name="delta_note"></param>
        /// <param name="delta_clock"></param>
        private void moveUpDownLeftRight( int delta_note, int delta_clock )
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}

Vector<VsqEvent> items = new Vector<VsqEvent>();
int selected = AppManager.getSelected();
int note_max = -1;
int note_min = 129;
int clock_max = Integer.MIN_VALUE;
int clock_min = Integer.MAX_VALUE;
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    if ( item.editing.ID.type != VsqIDType.Anote ) {
        continue;
    }
    VsqEvent add = null;

    // 音程
    int note = item.editing.ID.Note;
    if ( delta_note != 0 && 0 <= note + delta_note && note + delta_note <= 127 ) {
        add = (VsqEvent)item.editing.clone();
        add.ID.Note += delta_note;
        note_max = Math.max( note_max, add.ID.Note );
        note_min = Math.min( note_min, add.ID.Note );
    }

    // ゲートタイム
    int clockstart = item.editing.Clock;
    int clockend = clockstart + item.editing.ID.getLength();
    if ( delta_clock != 0 ) {
        if ( add == null ) {
            add = (VsqEvent)item.editing.clone();
        }
        add.Clock += delta_clock;
        clock_max = Math.max( clock_max, clockend + delta_clock );
        clock_min = Math.min( clock_min, clockstart );
    }

    if ( add != null ) {
        items.add( add );
    }
}
if ( items.size() <= 0 ) {
    return;
}

// コマンドを発行
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandEventReplaceRange(
        selected, items.toArray( new VsqEvent[] { } ) ) );
AppManager.editHistory.register( vsq.executeCommand( run ) );

// 編集されたものを再選択する
for ( Iterator<VsqEvent> itr = items.iterator(); itr.hasNext(); ) {
    VsqEvent item = itr.next();
    AppManager.itemSelection.addEvent( item.InternalID );
}

// 編集が施された。
setEdited( true );
updateDrawObjectList();

// 音符が見えるようにする。音程方向
if ( delta_note > 0 ) {
    note_max++;
    if ( 127 < note_max ) {
        note_max = 127;
    }
    ensureVisibleY( note_max );
} else if ( delta_note < 0 ) {
    note_min -= 2;
    if ( note_min < 0 ) {
        note_min = 0;
    }
    ensureVisibleY( note_min );
}

// 音符が見えるようにする。時間方向
if ( delta_clock > 0 ) {
    ensureVisible( clock_max );
} else if ( delta_clock < 0 ) {
    ensureVisible( clock_min );
}
refreshScreen();
        }

        /// <summary>
        /// マウス位置におけるIDを返します。該当するIDが無ければnullを返します
        /// rectには、該当するIDがあればその画面上での形状を、該当するIDがなければ、
        /// 画面上で最も近かったIDの画面上での形状を返します
        /// </summary>
        /// <param name="mouse_position"></param>
        /// <returns></returns>
        private VsqEvent getItemAtClickedPosition( Point mouse_position, ByRef<Rectangle> rect )
        {
rect.value = new Rectangle();
int width = pictPianoRoll.getWidth();
int height = pictPianoRoll.getHeight();
int key_width = AppManager.keyWidth;

// マウスが可視範囲になければ死ぬ
if ( mouse_position.x < key_width || width < mouse_position.x )
{
    return null;
}
if ( mouse_position.y < 0 || height < mouse_position.y )
{
    return null;
}

// 表示中のトラック番号が異常だったら死ぬ
int selected = AppManager.getSelected();
if ( selected < 1 )
{
    return null;
}
synchronized ( AppManager.mDrawObjects )
{
    Vector<DrawObject> dobj_list = AppManager.mDrawObjects.get( selected - 1 );
    int count = dobj_list.size();
    int start_to_draw_x = controller.getStartToDrawX();
    int start_to_draw_y = controller.getStartToDrawY();
    VsqFileEx vsq = AppManager.getVsqFile();
    VsqTrack vsq_track = vsq.Track.get( selected );

    for ( int i = 0; i < count; i++ )
    {
        DrawObject dobj = dobj_list.get( i );
        int x = dobj.mRectangleInPixel.x + key_width - start_to_draw_x;
        int y = dobj.mRectangleInPixel.y - start_to_draw_y;
        if ( mouse_position.x < x )
        {
            continue;
        }
        if ( x + dobj.mRectangleInPixel.width < mouse_position.x )
        {
            continue;
        }
        if ( width < x )
        {
            break;
        }
        if ( mouse_position.y < y )
        {
            continue;
        }
        if ( y + dobj.mRectangleInPixel.height < mouse_position.y )
        {
            continue;
        }
        int internal_id = dobj.mInternalID;
        for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); )
        {
            VsqEvent item = itr.next();
            if ( item.InternalID == internal_id )
            {
                rect.value = new Rectangle( x, y, dobj.mRectangleInPixel.width, dobj.mRectangleInPixel.height );
                return item;
            }
        }
    }
}
return null;
        }

        /// <summary>
        /// 真ん中ボタンで画面を移動させるときの、vScrollの値を計算します。
        /// 計算には、mButtonInitial, mMiddleButtonVScrollの値が使われます。
        /// </summary>
        /// <returns></returns>
        private int computeVScrollValueForMiddleDrag( int mouse_y )
        {
int dy = mouse_y - mButtonInitial.y;
int max = vScroll.getMaximum() - vScroll.getVisibleAmount();
int min = vScroll.getMinimum();
double new_vscroll_value = (double)mMiddleButtonVScroll - dy * max / (128.0 * (int)(100.0 * controller.getScaleY()) - (double)pictPianoRoll.getHeight());
int value = (int)new_vscroll_value;
if ( value < min ) {
    value = min;
} else if ( max < value ) {
    value = max;
}
return value;
        }

        /// <summary>
        /// 真ん中ボタンで画面を移動させるときの、hScrollの値を計算します。
        /// 計算には、mButtonInitial, mMiddleButtonHScrollの値が使われます。
        /// </summary>
        /// <returns></returns>
        private int computeHScrollValueForMiddleDrag( int mouse_x )
        {
int dx = mouse_x - mButtonInitial.x;
int max = hScroll.getMaximum() - hScroll.getVisibleAmount();
int min = hScroll.getMinimum();
double new_hscroll_value = (double)mMiddleButtonHScroll - (double)dx * controller.getScaleXInv();
int value = (int)new_hscroll_value;
if ( value < min ) {
    value = min;
} else if ( max < value ) {
    value = max;
}
return value;
        }

        /// <summary>
        /// 仮想スクリーン上でみた時の，現在のピアノロール画面の上端のy座標が指定した値とするための，vScrollの値を計算します
        /// calculateStartToDrawYの逆関数です
        /// </summary>
        private int calculateVScrollValueFromStartToDrawY( int start_to_draw_y )
        {
return (int)(start_to_draw_y / controller.getScaleY());
        }

        /// <summary>
        /// 現在表示されているピアノロール画面の右上の、仮想スクリーン上座標で見たときのy座標(pixel)を取得します
        /// </summary>
        private int calculateStartToDrawY( int vscroll_value )
        {
int min = vScroll.getMinimum();
int max = vScroll.getMaximum() - vScroll.getVisibleAmount();
int value = vscroll_value;
if ( value < min ) {
    value = min;
} else if ( max < value ) {
    value = max;
}
return (int)(value * controller.getScaleY());
        }

        /// <summary>
        /// デフォルトのショートカットキーを格納したリストを取得します
        /// </summary>
        public Vector<ValuePairOfStringArrayOfKeys> getDefaultShortcutKeys()
        {
BKeys ctrl = BKeys.Control;
Vector<ValuePairOfStringArrayOfKeys> ret = new Vector<ValuePairOfStringArrayOfKeys>( Arrays.asList(
    new ValuePairOfStringArrayOfKeys[]{
    new ValuePairOfStringArrayOfKeys( menuFileNew.getName(), new BKeys[]{ ctrl, BKeys.N } ),
    new ValuePairOfStringArrayOfKeys( menuFileOpen.getName(), new BKeys[]{ ctrl, BKeys.O } ),
    new ValuePairOfStringArrayOfKeys( menuFileOpenVsq.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileSave.getName(), new BKeys[]{ ctrl, BKeys.S } ),
    new ValuePairOfStringArrayOfKeys( menuFileQuit.getName(), new BKeys[]{ ctrl, BKeys.Q } ),
    new ValuePairOfStringArrayOfKeys( menuFileSaveNamed.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileImportVsq.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileOpenUst.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileImportMidi.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileExportWave.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuFileExportMidi.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuEditUndo.getName(), new BKeys[]{ ctrl, BKeys.Z } ),
    new ValuePairOfStringArrayOfKeys( menuEditRedo.getName(), new BKeys[]{ ctrl, BKeys.Shift, BKeys.Z } ),
    new ValuePairOfStringArrayOfKeys( menuEditCut.getName(), new BKeys[]{ ctrl, BKeys.X } ),
    new ValuePairOfStringArrayOfKeys( menuEditCopy.getName(), new BKeys[]{ ctrl, BKeys.C } ),
    new ValuePairOfStringArrayOfKeys( menuEditPaste.getName(), new BKeys[]{ ctrl, BKeys.V } ),
    new ValuePairOfStringArrayOfKeys( menuEditSelectAll.getName(), new BKeys[]{ ctrl, BKeys.A } ),
    new ValuePairOfStringArrayOfKeys( menuEditSelectAllEvents.getName(), new BKeys[]{ ctrl, BKeys.Shift, BKeys.A } ),
    new ValuePairOfStringArrayOfKeys( menuEditDelete.getName(), new BKeys[]{ BKeys.Back } ),
    new ValuePairOfStringArrayOfKeys( menuVisualMixer.getName(), new BKeys[]{ BKeys.F3 } ),
    new ValuePairOfStringArrayOfKeys( menuVisualWaveform.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualProperty.getName(), new BKeys[]{ BKeys.F6 } ),
    new ValuePairOfStringArrayOfKeys( menuVisualGridline.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualStartMarker.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualEndMarker.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualLyrics.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualNoteProperty.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualPitchLine.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuVisualIconPalette.getName(), new BKeys[]{ BKeys.F4 } ),
    new ValuePairOfStringArrayOfKeys( menuJobNormalize.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuJobInsertBar.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuJobDeleteBar.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuJobRandomize.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuJobConnect.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuJobLyric.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackOn.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackAdd.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackCopy.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackChangeName.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackDelete.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackRenderCurrent.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackRenderAll.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackOverlay.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackRendererVOCALOID1.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackRendererVOCALOID2.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuTrackRendererUtau.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuLyricExpressionProperty.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuLyricVibratoProperty.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuLyricDictionary.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuScriptUpdate.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuSettingPreference.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuSettingGameControlerSetting.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuSettingGameControlerLoad.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuSettingPaletteTool.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuSettingShortcut.getName(), new BKeys[]{} ),
    //new ValuePairOfStringArrayOfKeys( menuSettingSingerProperty.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuWindowMinimize.getName(), new BKeys[]{ ctrl, BKeys.M } ),
    new ValuePairOfStringArrayOfKeys( menuHelpAbout.getName(), new BKeys[]{} ),
    new ValuePairOfStringArrayOfKeys( menuHiddenEditLyric.getName(), new BKeys[]{ BKeys.F2 } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenEditFlipToolPointerPencil.getName(), new BKeys[]{ ctrl, BKeys.W } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenEditFlipToolPointerEraser.getName(), new BKeys[]{ ctrl, BKeys.E } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenVisualForwardParameter.getName(), new BKeys[]{ ctrl, BKeys.Alt, BKeys.PageDown } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenVisualBackwardParameter.getName(), new BKeys[]{ ctrl, BKeys.Alt, BKeys.PageUp } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenTrackNext.getName(), new BKeys[]{ ctrl, BKeys.PageDown } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenTrackBack.getName(), new BKeys[]{ ctrl, BKeys.PageUp } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenSelectBackward.getName(), new BKeys[]{ BKeys.Alt, BKeys.Left } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenSelectForward.getName(), new BKeys[]{ BKeys.Alt, BKeys.Right } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenMoveUp.getName(), new BKeys[]{ BKeys.Shift, BKeys.Up } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenMoveDown.getName(), new BKeys[]{ BKeys.Shift, BKeys.Down } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenMoveLeft.getName(), new BKeys[]{ BKeys.Shift, BKeys.Left } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenMoveRight.getName(), new BKeys[]{ BKeys.Shift, BKeys.Right } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenLengthen.getName(), new BKeys[]{ ctrl, BKeys.Right } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenShorten.getName(), new BKeys[]{ ctrl, BKeys.Left } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenGoToEndMarker.getName(), new BKeys[]{ ctrl, BKeys.End } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenGoToStartMarker.getName(), new BKeys[]{ ctrl, BKeys.Home } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenPlayFromStartMarker.getName(), new BKeys[]{ ctrl, BKeys.Enter } ),
    new ValuePairOfStringArrayOfKeys( menuHiddenFlipCurveOnPianorollMode.getName(), new BKeys[]{ BKeys.Tab } ),
} ) );
return ret;
        }

        /// <summary>
        /// マウスの真ん中ボタンが押されたかどうかを調べます。
        /// スペースキー+左ボタンで真ん中ボタンとみなすかどうか、というオプションも考慮される。
        /// </summary>
        /// <param name="button"></param>
        /// <returns></returns>
        public boolean isMouseMiddleButtonDowned( BMouseButtons button )
        {
boolean ret = false;
if ( AppManager.editorConfig.UseSpaceKeyAsMiddleButtonModifier ) {
    if ( mSpacekeyDowned && button == BMouseButtons.Left ) {
        ret = true;
    }
} else {
    if ( button == BMouseButtons.Middle ) {
        ret = true;
    }
}
return ret;
        }

        /// <summary>
        /// 画面を再描画します。
        /// 再描画間隔が設定値より短い場合再描画がスキップされます。
        /// </summary>
        public void refreshScreen( boolean force )
        {
//refreshScreenCore( this, null );
this.repaint();
//trackSelector.repaint();
        }

        public void refreshScreen()
        {
refreshScreen( false );
        }

        public void refreshScreenCore( Object sender, BEventArgs e )
        {
pictPianoRoll.repaint();
picturePositionIndicator.repaint();
trackSelector.repaint();
pictureBox2.repaint();
if ( menuVisualWaveform.isSelected() ) {
    waveView.repaint();
}
if ( AppManager.editorConfig.OverviewEnabled ) {
    panelOverview.repaint();
}
        }

        /// <summary>
        /// 現在のゲームコントローラのモードに応じてstripLblGameCtrlModeの表示状態を更新します。
        /// </summary>
        public void updateGameControlerStatus( Object sender, BEventArgs e )
        {
        }

        public int calculateStartToDrawX()
        {
return (int)(hScroll.getValue() * controller.getScaleX());
        }

        /// <summary>
        /// 現在選択されている音符よりも1個前方の音符を選択しなおします。
        /// </summary>
        public void selectBackward()
        {
int count = AppManager.itemSelection.getEventCount();
if ( count <= 0 ) {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );

// 選択されている音符のうち、最も前方にあるものがどれかを調べる
int min_clock = Integer.MAX_VALUE;
int internal_id = -1;
VsqIDType type = VsqIDType.Unknown;
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    if ( item.editing.Clock <= min_clock ) {
        min_clock = item.editing.Clock;
        internal_id = item.original.InternalID;
        type = item.original.ID.type;
    }
}
if ( internal_id == -1 || type == VsqIDType.Unknown ) {
    return;
}

// 1個前のアイテムのIDを検索
int last_id = -1;
int clock = AppManager.getCurrentClock();
for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
    VsqEvent item = itr.next();
    if ( item.ID.type != type ) {
        continue;
    }
    if ( item.InternalID == internal_id ) {
        break;
    }
    last_id = item.InternalID;
    clock = item.Clock;
}
if ( last_id == -1 ) {
    return;
}

// 選択しなおす
AppManager.itemSelection.clearEvent();
AppManager.itemSelection.addEvent( last_id );
ensureVisible( clock );
        }

        /// <summary>
        /// 現在選択されている音符よりも1個後方の音符を選択しなおします。
        /// </summary>
        public void selectForward()
        {
int count = AppManager.itemSelection.getEventCount();
if ( count <= 0 ) {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );

// 選択されている音符のうち、最も後方にあるものがどれかを調べる
int max_clock = Integer.MIN_VALUE;
int internal_id = -1;
VsqIDType type = VsqIDType.Unknown;
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    if ( max_clock <= item.editing.Clock ) {
        max_clock = item.editing.Clock;
        internal_id = item.original.InternalID;
        type = item.original.ID.type;
    }
}
if ( internal_id == -1 || type == VsqIDType.Unknown ) {
    return;
}

// 1個後ろのアイテムのIDを検索
int last_id = -1;
int clock = AppManager.getCurrentClock();
boolean break_next = false;
for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
    VsqEvent item = itr.next();
    if ( item.ID.type != type ) {
        continue;
    }
    if ( item.InternalID == internal_id ) {
        break_next = true;
        last_id = item.InternalID;
        clock = item.Clock;
        continue;
    }
    last_id = item.InternalID;
    clock = item.Clock;
    if ( break_next ) {
        break;
    }
}
if ( last_id == -1 ) {
    return;
}

// 選択しなおす
AppManager.itemSelection.clearEvent();
AppManager.itemSelection.addEvent( last_id );
ensureVisible( clock );
        }

        public void invalidatePictOverview( Object sender, BEventArgs e )
        {
panelOverview.invalidate();
        }

        public void updateBgmMenuState()
        {
menuTrackBgm.removeAll();
int count = AppManager.getBgmCount();
if ( count > 0 ) {
    for ( int i = 0; i < count; i++ ) {
        BgmFile item = AppManager.getBgm( i );
        BMenu menu = new BMenu();
        menu.setText( PortUtil.getFileName( item.file ) );
        menu.setToolTipText( item.file );

        BgmMenuItem menu_remove = new BgmMenuItem( i );
        menu_remove.setText( _( "Remove" ) );
        menu_remove.setToolTipText( item.file );
        menu_remove.clickEvent.add( new BEventHandler( this, "handleBgmRemove_Click" ) );
        menu.add( menu_remove );

        BgmMenuItem menu_start_after_premeasure = new BgmMenuItem( i );
        menu_start_after_premeasure.setText( _( "Start After Premeasure" ) );
        menu_start_after_premeasure.setName( "menu_start_after_premeasure" + i );
        menu_start_after_premeasure.setCheckOnClick( true );
        menu_start_after_premeasure.setSelected( item.startAfterPremeasure );
        menu_start_after_premeasure.checkedChangedEvent.add( new BEventHandler( this, "handleBgmStartAfterPremeasure_CheckedChanged" ) );
        menu.add( menu_start_after_premeasure );

        BgmMenuItem menu_offset_second = new BgmMenuItem( i );
        menu_offset_second.setText( _( "Set Offset Seconds" ) );
        menu_offset_second.setToolTipText( item.readOffsetSeconds + " " + _( "seconds" ) );
        menu_offset_second.clickEvent.add( new BEventHandler( this, "handleBgmOffsetSeconds_Click" ) );
        menu.add( menu_offset_second );

        menuTrackBgm.add( menu );
    }
    menuTrackBgm.addSeparator();
}
BMenuItem menu_add = new BMenuItem();
menu_add.setText( _( "Add" ) );
menu_add.clickEvent.add( new BEventHandler( this, "handleBgmAdd_Click" ) );
menuTrackBgm.add( menu_add );
        }


        public void updatePropertyPanelState( PanelState state )
        {
if ( state == PanelState.Docked ) {
    mPropertyPanelContainer.addComponent( AppManager.propertyPanel );
    menuVisualProperty.setSelected( true );
    AppManager.editorConfig.PropertyWindowStatus.State = PanelState.Docked;
    splitContainerProperty.setPanel1Hidden( false );
    splitContainerProperty.setSplitterFixed( false );
    splitContainerProperty.setDividerSize( _SPL_SPLITTER_WIDTH );
    serr.println( "fixme: FormMain#updatePropertyPanelState; Panel1MinSize not set" );
    int w = AppManager.editorConfig.PropertyWindowStatus.DockWidth;
    if( w < _PROPERTY_DOCK_MIN_WIDTH ){
        w = _PROPERTY_DOCK_MIN_WIDTH;
    }
    splitContainerProperty.setDividerLocation( w );
    AppManager.editorConfig.PropertyWindowStatus.WindowState = BFormWindowState.Minimized;
    int before = AppManager.propertyWindow.formClosingEvent.size();
    int after = before - 1;
    while( before != after ){
        before = AppManager.propertyWindow.formClosingEvent.size();
        AppManager.propertyWindow.formClosingEvent.remove( new BFormClosingEventHandler( this, "propertyWindow_FormClosing" ) );
        after = AppManager.propertyWindow.formClosingEvent.size();
    }
    AppManager.propertyWindow.close();
    AppManager.propertyWindow.formClosingEvent.add( new BFormClosingEventHandler( this, "propertyWindow_FormClosing" ) );
} else if ( state == PanelState.Hidden ) {
    AppManager.propertyWindow.setVisible( false );
    menuVisualProperty.setSelected( false );
    if ( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Docked ) {
        AppManager.editorConfig.PropertyWindowStatus.DockWidth = splitContainerProperty.getDividerLocation();
    }
    AppManager.editorConfig.PropertyWindowStatus.State = PanelState.Hidden;
    serr.println( "fixme: FormMain#updatePropertyPanelState; Panel1MinSize not set" );
    splitContainerProperty.setPanel1Hidden( true );
    splitContainerProperty.setDividerLocation( 0 );
    splitContainerProperty.setDividerSize( 0 );
    splitContainerProperty.setSplitterFixed( true );
} else if ( state == PanelState.Window ) {
    AppManager.propertyWindow.addComponent( AppManager.propertyPanel );
    Point parent = this.getLocation();
    XmlRectangle rc = AppManager.editorConfig.PropertyWindowStatus.Bounds;
    Point property = new Point( rc.x, rc.y );
    AppManager.propertyWindow.setBounds( new Rectangle( parent.x + property.x, parent.y + property.y, rc.width, rc.height ) );
    normalizeFormLocation( AppManager.propertyWindow );
    // setVisible -> NORMALとすると，javaの場合見栄えが悪くなる
    if ( AppManager.propertyWindow.getExtendedState() != BForm.NORMAL ) {
        AppManager.propertyWindow.setExtendedState( BForm.NORMAL );
    }
    AppManager.propertyWindow.setVisible( true );
    menuVisualProperty.setSelected( true );
    if ( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Docked ) {
        AppManager.editorConfig.PropertyWindowStatus.DockWidth = splitContainerProperty.getDividerLocation();
    }
    AppManager.editorConfig.PropertyWindowStatus.State = PanelState.Window;
    serr.println( "fixme: FormMain#updatePropertyPanelState; splitContainerProperty.Panel1MinSize not set" );
    splitContainerProperty.setPanel1Hidden( true );
    splitContainerProperty.setDividerLocation( 0 );
    splitContainerProperty.setDividerSize( 0 );
    splitContainerProperty.setSplitterFixed( true );
    AppManager.editorConfig.PropertyWindowStatus.WindowState = BFormWindowState.Normal;
}
        }


        /// <summary>
        /// メインメニュー項目の中から，Nameプロパティがnameであるものを検索します．見つからなければnullを返す．
        /// </summary>
        /// <param name="name"></param>
        /// <param name="parent"></param>
        /// <returns></returns>
        public Object searchMenuItemFromName( String name, ByRef<Object> parent )
        {
int count = menuStripMain.getMenuCount();
for ( int i = 0; i < count; i++ ) {
    Object tsi = menuStripMain.getMenu( i );
    Object ret = searchMenuItemRecurse( name, tsi, parent );
    if ( ret != null ) {
        if ( parent.value == null ) {
            parent.value = tsi;
        }
        return ret;
    }
}
return null;
        }

        /// <summary>
        /// 指定されたメニューアイテムから，Nameプロパティがnameであるものを再帰的に検索します．見つからなければnullを返す
        /// </summary>
        /// <param name="name"></param>
        /// <param name="tree"></param>
        /// <returns></returns>
        public Object searchMenuItemRecurse( String name, Object tree, ByRef<Object> parent )
        {
if( tree == null ){
    return null;
}
// 子メニューを持つ場合
if( tree instanceof JMenu ){
    JMenu jm = (JMenu)tree;
    int size = jm.getMenuComponentCount();
    for( int i = 0; i < size; i++ ){
        Component comp = jm.getMenuComponent( i );
        if( comp != null && comp instanceof JMenuItem ){
            JMenuItem jmi = (JMenuItem)comp;
            Object obj = searchMenuItemRecurse( name, jmi, parent );
            if ( obj != null ){
                parent.value = tree;
                return obj;
            }
        }
    }
}
// 自分自身が該当しないか？
if( tree instanceof JMenuItem ){
    JMenuItem jmi = (JMenuItem)tree;
    if( str.compare( name, jmi.getName() ) ){
        parent.value = null;
        return tree;
    }
}
// 該当しなかった
return null;
        }

        /// <summary>
        /// フォームをマウス位置に出す場合に推奨されるフォーム位置を計算します
        /// </summary>
        /// <param name="dlg"></param>
        /// <returns></returns>
        public Point getFormPreferedLocation( int dialogWidth, int dialogHeight )
        {
Point mouse = PortUtil.getMousePosition();
Rectangle rcScreen = PortUtil.getWorkingArea( this );
int top = mouse.y - dialogHeight / 2;
if ( top + dialogHeight > rcScreen.y + rcScreen.height )
{
    // ダイアログの下端が隠れる場合、位置をずらす
    top = rcScreen.y + rcScreen.height - dialogHeight;
}
if ( top < rcScreen.y )
{
    // ダイアログの上端が隠れる場合、位置をずらす
    top = rcScreen.y;
}
int left = mouse.x - dialogWidth / 2;
if ( left + dialogWidth > rcScreen.x + rcScreen.width )
{
    // ダイアログの右端が隠れる場合，位置をずらす
    left = rcScreen.x + rcScreen.width - dialogWidth;
}
if ( left < rcScreen.x )
{
    // ダイアログの左端が隠れる場合，位置をずらす
    left = rcScreen.x;
}
return new Point( left, top );
        }

        /// <summary>
        /// フォームをマウス位置に出す場合に推奨されるフォーム位置を計算します
        /// </summary>
        /// <param name="dlg"></param>
        /// <returns></returns>
        public Point getFormPreferedLocation( BDialog dlg )
        {
return getFormPreferedLocation( dlg.getWidth(), dlg.getHeight() );
        }

        public void updateLayout()
        {
int keywidth = AppManager.keyWidth;
pictureBox3.setPreferredSize( new Dimension( keywidth, 4 ) );
pictureBox3.setSize( new Dimension( keywidth, pictureBox3.getHeight() ) );
panelWaveformZoom.setPreferredSize( new Dimension( keywidth, 4 ) );
panelWaveformZoom.setSize( new Dimension( keywidth, panelWaveformZoom.getHeight() ) );

Dimension overview_pref_size = panelOverview.getPreferredSize();
if( AppManager.editorConfig.OverviewEnabled ){
    panel3.setPreferredSize( new Dimension( (int)overview_pref_size.getWidth(), _OVERVIEW_HEIGHT ) );
    //panel3.setHeight( _OVERVIEW_HEIGHT );
}else{
    panel3.setPreferredSize( new Dimension( (int)overview_pref_size.getWidth(), 0 ) );
    //panel3.setHeight( 0 );
}
jPanel1.doLayout();
panel1.doLayout();
panel21.doLayout();
int overview_width = this.getWidth();
panelOverview.updateCachedImage( overview_width );
refreshScreenCore( null, null );
        }

        public void updateRendererMenu()
        {
String wine_prefix = AppManager.editorConfig.WinePrefix;
String wine_top = AppManager.editorConfig.WineTop;
if ( !VSTiDllManager.isRendererAvailable( RendererKind.VOCALOID1, wine_prefix, wine_top ) ) {
    cMenuTrackTabRendererVOCALOID1.setIcon( new ImageIcon( Resources.get_slash() ) );
    menuTrackRendererVOCALOID1.setIcon( new ImageIcon( Resources.get_slash() ) );
} else {
    cMenuTrackTabRendererVOCALOID1.setIcon( null );
    menuTrackRendererVOCALOID1.setIcon( null );
}

if ( !VSTiDllManager.isRendererAvailable( RendererKind.VOCALOID2, wine_prefix, wine_top ) ) {
    cMenuTrackTabRendererVOCALOID2.setIcon( new ImageIcon( Resources.get_slash() ) );
    menuTrackRendererVOCALOID2.setIcon( new ImageIcon( Resources.get_slash() ) );
} else {
    cMenuTrackTabRendererVOCALOID2.setIcon( null );
    menuTrackRendererVOCALOID2.setIcon( null );
}

if ( !VSTiDllManager.isRendererAvailable( RendererKind.UTAU, wine_prefix, wine_top ) ) {
    cMenuTrackTabRendererUtau.setIcon( new ImageIcon( Resources.get_slash() ) );
    menuTrackRendererUtau.setIcon( new ImageIcon( Resources.get_slash() ) );
} else {
    cMenuTrackTabRendererUtau.setIcon( null );
    menuTrackRendererUtau.setIcon( null );
}

if ( !VSTiDllManager.isRendererAvailable( RendererKind.VCNT, wine_prefix, wine_top ) ) {
    cMenuTrackTabRendererStraight.setIcon( new ImageIcon( Resources.get_slash() ) );
    menuTrackRendererVCNT.setIcon( new ImageIcon( Resources.get_slash() ) );
} else {
    cMenuTrackTabRendererStraight.setIcon( null );
    menuTrackRendererVCNT.setIcon( null );
}

if ( !VSTiDllManager.isRendererAvailable( RendererKind.AQUES_TONE, wine_prefix, wine_top ) ) {
    cMenuTrackTabRendererAquesTone.setIcon( new ImageIcon( Resources.get_slash() ) );
    menuTrackRendererAquesTone.setIcon( new ImageIcon( Resources.get_slash() ) );
} else {
    cMenuTrackTabRendererAquesTone.setIcon( null );
    menuTrackRendererAquesTone.setIcon( null );
}

// UTAU用のサブアイテムを更新
int count = AppManager.editorConfig.getResamplerCount();
// サブアイテムの個数を整える
menuTrackRendererUtau.removeAll();
cMenuTrackTabRendererUtau.removeAll();
for( int i = 0; i < count; i++ ){
    String path = AppManager.editorConfig.getResamplerAt( i );
    String name = PortUtil.getFileNameWithoutExtension( path );

    BMenuItem item0 = new BMenuItem();
    item0.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
    item0.setText( name );
    item0.setToolTipText( path );
    menuTrackRendererUtau.add( item0 );

    BMenuItem item1 = new BMenuItem();
    item1.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
    item1.setText( name );
    item1.setToolTipText( path );
    cMenuTrackTabRendererUtau.add( item1 );
}
        }

        public void drawUtauVibrato( Graphics2D g, UstVibrato vibrato, int note, int clock_start, int clock_width )
        {
//SmoothingMode old = g.SmoothingMode;
//g.SmoothingMode = SmoothingMode.AntiAlias;
// 魚雷を描いてみる
int y0 = AppManager.yCoordFromNote( note - 0.5f );
int x0 = AppManager.xCoordFromClocks( clock_start );
int px_width = AppManager.xCoordFromClocks( clock_start + clock_width ) - x0;
int boxheight = (int)(vibrato.Depth * 2 / 100.0 * (int)(100.0 * controller.getScaleY()));
int px_shift = (int)(vibrato.Shift / 100.0 * vibrato.Depth / 100.0 * (int)(100.0 * controller.getScaleY()));

// vibrato in
int cl_vibin_end = clock_start + (int)(clock_width * vibrato.In / 100.0);
int x_vibin_end = AppManager.xCoordFromClocks( cl_vibin_end );
Point ul = new Point( x_vibin_end, y0 - boxheight / 2 - px_shift );
Point dl = new Point( x_vibin_end, y0 + boxheight / 2 - px_shift );
g.setColor( Color.black );
g.drawPolyline( new int[] { x0, ul.x, dl.x },
                new int[] { y0, ul.y, dl.y },
                3 );

// vibrato out
int cl_vibout_start = clock_start + clock_width - (int)(clock_width * vibrato.Out / 100.0);
int x_vibout_start = AppManager.xCoordFromClocks( cl_vibout_start );
Point ur = new Point( x_vibout_start, y0 - boxheight / 2 - px_shift );
Point dr = new Point( x_vibout_start, y0 + boxheight / 2 - px_shift );
g.drawPolyline( new int[] { x0 + px_width, ur.x, dr.x },
               new int[] { y0, ur.y, dr.y },
               3 );

// box
int boxwidth = x_vibout_start - x_vibin_end;
if ( boxwidth > 0 ) {
    g.drawPolyline( new int[] { ul.x, dl.x, dr.x, ur.x },
                   new int[] { ul.y, dl.y, dr.y, ur.y },
                   4 );
}

// buf1に、vibrato in/outによる増幅率を代入
float[] buf1 = new float[clock_width + 1];
for ( int clock = clock_start; clock <= clock_start + clock_width; clock++ ) {
    buf1[clock - clock_start] = 1.0f;
}
// vibin
if ( cl_vibin_end - clock_start > 0 ) {
    for ( int clock = clock_start; clock <= cl_vibin_end; clock++ ) {
        int i = clock - clock_start;
        buf1[i] *= i / (float)(cl_vibin_end - clock_start);
    }
}
if ( clock_start + clock_width - cl_vibout_start > 0 ) {
    for ( int clock = clock_start + clock_width; clock >= cl_vibout_start; clock-- ) {
        int i = clock - clock_start;
        float v = (clock_start + clock_width - clock) / (float)(clock_start + clock_width - cl_vibout_start);
        buf1[i] = buf1[i] * v;
    }
}

// buf2に、shiftによるy座標のシフト量を代入
float[] buf2 = new float[clock_width + 1];
for ( int i = 0; i < clock_width; i++ ) {
    buf2[i] = px_shift * buf1[i];
}
try {
    double phase = 2.0 * Math.PI * vibrato.Phase / 100.0;
    double omega = 2.0 * Math.PI / vibrato.Period;   //角速度(rad/msec)
    double msec = AppManager.getVsqFile().getSecFromClock( clock_start - 1 ) * 1000.0;
    float px_track_height = (int)(controller.getScaleY() * 100.0f);
    phase -= (AppManager.getVsqFile().getSecFromClock( clock_start ) * 1000.0 - msec) * omega;
    for ( int clock = clock_start; clock <= clock_start + clock_width; clock++ ) {
        int i = clock - clock_start;
        double t_msec = AppManager.getVsqFile().getSecFromClock( clock ) * 1000.0;
        phase += (t_msec - msec) * omega;
        msec = t_msec;
        buf2[i] += (float)(vibrato.Depth * 0.01f * px_track_height * buf1[i] * Math.sin( phase ));
    }
    int[] listx = new int[clock_width + 1];
    int[] listy = new int[clock_width + 1];
    for ( int clock = clock_start; clock <= clock_start + clock_width; clock++ ) {
        int i = clock - clock_start;
        listx[i] = AppManager.xCoordFromClocks( clock );
        listy[i] = (int)(y0 + buf2[i]);
    }
    if ( listx.length >= 2 ) {
        g.setColor( Color.red );
        g.drawPolyline( listx, listy, listx.length );
    }
    //g.SmoothingMode = old;
} catch ( Exception oex ) {
    Logger.write( FormMain.class + ".drawUtauVibato; ex=" + oex + "\n" );
}
        }


        public void updateCopyAndPasteButtonStatus()
        {
// copy cut deleteの表示状態更新
boolean selected_is_null = (AppManager.itemSelection.getEventCount() == 0) &&
                           (AppManager.itemSelection.getTempoCount() == 0) &&
                           (AppManager.itemSelection.getTimesigCount() == 0) &&
                           (AppManager.itemSelection.getPointIDCount() == 0);

int selected_point_id_count = AppManager.itemSelection.getPointIDCount();
cMenuTrackSelectorCopy.setEnabled( selected_point_id_count > 0 );
cMenuTrackSelectorCut.setEnabled( selected_point_id_count > 0 );
cMenuTrackSelectorDeleteBezier.setEnabled( (AppManager.isCurveMode() && AppManager.itemSelection.getLastBezier() != null) );
if ( selected_point_id_count > 0 ) {
    cMenuTrackSelectorDelete.setEnabled( true );
} else {
    SelectedEventEntry last = AppManager.itemSelection.getLastEvent();
    if ( last == null ) {
        cMenuTrackSelectorDelete.setEnabled( false );
    } else {
        cMenuTrackSelectorDelete.setEnabled( last.original.ID.type == VsqIDType.Singer );
    }
}

cMenuPianoCopy.setEnabled( !selected_is_null );
cMenuPianoCut.setEnabled( !selected_is_null );
cMenuPianoDelete.setEnabled( !selected_is_null );

menuEditCopy.setEnabled( !selected_is_null );
menuEditCut.setEnabled( !selected_is_null );
menuEditDelete.setEnabled( !selected_is_null );

ClipboardEntry ce = AppManager.clipboard.getCopiedItems();
int copy_started_clock = ce.copyStartedClock;
TreeMap<CurveType, VsqBPList> copied_curve = ce.points;
TreeMap<CurveType, Vector<BezierChain>> copied_bezier = ce.beziers;
boolean copied_is_null = (ce.events.size() == 0) &&
                      (ce.tempo.size() == 0) &&
                      (ce.timesig.size() == 0) &&
                      (copied_curve.size() == 0) &&
                      (copied_bezier.size() == 0);
boolean enabled = !copied_is_null;
if ( copied_curve.size() == 1 ) {
    // 1種類のカーブがコピーされている場合→コピーされているカーブの種類と、現在選択されているカーブの種類とで、最大値と最小値が一致している場合のみ、ペースト可能
    CurveType ct = CurveType.Empty;
    for ( Iterator<CurveType> itr = copied_curve.keySet().iterator(); itr.hasNext(); ) {
        CurveType c = itr.next();
        ct = c;
    }
    CurveType selected = trackSelector.getSelectedCurve();
    if ( ct.getMaximum() == selected.getMaximum() &&
         ct.getMinimum() == selected.getMinimum() &&
         !selected.isScalar() && !selected.isAttachNote() ) {
        enabled = true;
    } else {
        enabled = false;
    }
} else if ( copied_curve.size() >= 2 ) {
    // 複数種類のカーブがコピーされている場合→そのままペーストすればOK
    enabled = true;
}
cMenuTrackSelectorPaste.setEnabled( enabled );
cMenuPianoPaste.setEnabled( enabled );
menuEditPaste.setEnabled( enabled );

/*int copy_started_clock;
boolean copied_is_null = (AppManager.GetCopiedEvent().Count == 0) &&
                      (AppManager.GetCopiedTempo( out copy_started_clock ).Count == 0) &&
                      (AppManager.GetCopiedTimesig( out copy_started_clock ).Count == 0) &&
                      (AppManager.GetCopiedCurve( out copy_started_clock ).Count == 0) &&
                      (AppManager.GetCopiedBezier( out copy_started_clock ).Count == 0);
menuEditCut.isEnabled() = !selected_is_null;
menuEditCopy.isEnabled() = !selected_is_null;
menuEditDelete.isEnabled() = !selected_is_null;
//stripBtnCopy.isEnabled() = !selected_is_null;
//stripBtnCut.isEnabled() = !selected_is_null;

if ( AppManager.GetCopiedEvent().Count != 0 ) {
    menuEditPaste.isEnabled() = (AppManager.CurrentClock >= AppManager.VsqFile.getPreMeasureClocks());
    //stripBtnPaste.isEnabled() = (AppManager.CurrentClock >= AppManager.VsqFile.getPreMeasureClocks());
} else {
    menuEditPaste.isEnabled() = !copied_is_null;
    //stripBtnPaste.isEnabled() = !copied_is_null;
}*/
        }

        /// <summary>
        /// 現在の編集データを全て破棄する。DirtyCheckは行われない。
        /// </summary>
        public void clearExistingData()
        {
AppManager.editHistory.clear();
AppManager.itemSelection.clearBezier();
AppManager.itemSelection.clearEvent();
AppManager.itemSelection.clearTempo();
AppManager.itemSelection.clearTimesig();
if ( AppManager.isPlaying() ) {
    AppManager.setPlaying( false, this );
}
waveView.unloadAll();
        }

        /// <summary>
        /// 保存されていない編集内容があるかどうかチェックし、必要なら確認ダイアログを出す。
        /// </summary>
        /// <returns>保存されていない保存内容などない場合、または、保存する必要がある場合で（保存しなくてよいと指定された場合または保存が行われた場合）にtrueを返す</returns>
        public boolean dirtyCheck()
        {
if ( mEdited ) {
    String file = AppManager.getFileName();
    if ( str.compare( file, "" ) ) {
        file = "Untitled";
    } else {
        file = PortUtil.getFileName( file );
    }
    BDialogResult dr = AppManager.showMessageBox( _( "Save this sequence?" ),
                                                  _( "Affirmation" ),
                                                  org.kbinani.windows.forms.Utility.MSGBOX_YES_NO_CANCEL_OPTION,
                                                  org.kbinani.windows.forms.Utility.MSGBOX_QUESTION_MESSAGE );
    if ( dr == BDialogResult.YES ) {
        if ( str.compare( AppManager.getFileName(), "" ) ) {
            int dr2 = AppManager.showModalDialog( saveXmlVsqDialog, false, this );
            if ( dr2 == BFileChooser.APPROVE_OPTION ) {
                String sf = saveXmlVsqDialog.getSelectedFile();
                AppManager.saveTo( sf );
                return true;
            } else {
                return false;
            }
        } else {
            AppManager.saveTo( AppManager.getFileName() );
            return true;
        }
    } else if ( dr == BDialogResult.NO ) {
        return true;
    } else {
        return false;
    }
} else {
    return true;
}
        }

        /// <summary>
        /// waveView用のwaveファイルを読込むスレッドで使用する
        /// </summary>
        /// <param name="arg"></param>
        public class LoadWaveProc extends Thread {
public String file = "";
public int track;

public LoadWaveProc( int track, String file ){
    this.file = file;
    this.track = track;
}

public void run(){
    waveView.load( track, file );
}
        }

        public void loadWave( Object arg )
        {
Object[] argArr = (Object[])arg;
String file = (String)argArr[0];
int track = (Integer)argArr[1];
waveView.load( track, file );
        }

        /// <summary>
        /// AppManager.editorConfig.ViewWaveformの値をもとに、splitterContainer2の表示状態を更新します
        /// </summary>
        public void updateSplitContainer2Size( boolean save_to_config )
        {
if ( AppManager.editorConfig.ViewWaveform ) {
    splitContainer2.setPanel2MinSize( _SPL2_PANEL2_MIN_HEIGHT );
    splitContainer2.setSplitterFixed( false );
    splitContainer2.setPanel2Hidden( false );
    splitContainer2.setDividerSize( _SPL_SPLITTER_WIDTH );
    int lastloc = AppManager.editorConfig.SplitContainer2LastDividerLocation;
    if ( lastloc <= 0 || lastloc > splitContainer2.getHeight() ) {
        int draft = splitContainer2.getHeight() -  100;
        if( draft <= 0 ){
            draft = splitContainer2.getHeight() / 2;
        }
        splitContainer2.setDividerLocation( draft );
    } else {
        splitContainer2.setDividerLocation( lastloc );
    }
} else {
    if( save_to_config ){
        AppManager.editorConfig.SplitContainer2LastDividerLocation = splitContainer2.getDividerLocation();
    }
    splitContainer2.setPanel2MinSize( 0 );
    splitContainer2.setPanel2Hidden( true );
    splitContainer2.setDividerSize( 0 );
    splitContainer2.setDividerLocation( splitContainer2.getHeight() );
    splitContainer2.setSplitterFixed( true );
}
        }

        /// <summary>
        /// ウィンドウの表示内容に応じて、ウィンドウサイズの最小値を計算します
        /// </summary>
        /// <returns></returns>
        public Dimension getWindowMinimumSize()
        {
Dimension current_minsize = new Dimension( getMinimumSize().width, getMinimumSize().height );
Dimension client = getContentPane().getSize();
Dimension current = getSize();
return new Dimension( current_minsize.width,
                      splitContainer1.getPanel2MinSize() +
                      _SCROLL_WIDTH + _PICT_POSITION_INDICATOR_HEIGHT + pictPianoRoll.getMinimumSize().height +
                      menuStripMain.getHeight() +
                      (current.height - client.height) +
                      20 );
        }

        /// <summary>
        /// 現在のAppManager.mInputTextBoxの状態を元に、歌詞の変更を反映させるコマンドを実行します
        /// </summary>
        public void executeLyricChangeCommand()
        {
if ( !AppManager.mInputTextBox.isVisible() ) {
    return;
}
SelectedEventEntry last_selected_event = AppManager.itemSelection.getLastEvent();
boolean phonetic_symbol_edit_mode = AppManager.mInputTextBox.isPhoneticSymbolEditMode();

int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );

// 後続に、連続している音符が何個あるか検査
int maxcount = SymbolTable.getMaxDivisions(); // 音節の分割によって，高々maxcount個までにしか分割されない
boolean check_started = false;
int endclock = 0;  // 直前の音符の終了クロック
int count = 0;     // 後続音符の連続個数
int start_index = -1;
int indx = -1;
for ( Iterator<Integer> itr = vsq_track.indexIterator( IndexIteratorKind.NOTE ); itr.hasNext(); ) {
    indx = itr.next();
    VsqEvent itemi = vsq_track.getEvent( indx );
    if ( itemi.InternalID == last_selected_event.original.InternalID ) {
        check_started = true;
        endclock = itemi.Clock + itemi.ID.getLength();
        count = 1;
        start_index = indx;
        continue;
    }
    if ( check_started ) {
        if ( count + 1 > maxcount ) {
            break;
        }
        if ( itemi.Clock <= endclock ) {
            count++;
            endclock = itemi.Clock + itemi.ID.getLength();
        } else {
            break;
        }
    }
}

// 後続の音符をリストアップ
VsqEvent[] items = new VsqEvent[count];
String[] original_symbol = new String[count];
String[] original_phrase = new String[count];
boolean[] symbol_protected = new boolean[count];
indx = -1;
for ( Iterator<Integer> itr = vsq_track.indexIterator( IndexIteratorKind.NOTE ); itr.hasNext(); ) {
    int index = itr.next();
    if ( index < start_index ) {
        continue;
    }
    indx++;
    if ( count <= indx ) {
        break;
    }
    VsqEvent ve = vsq_track.getEvent( index );
    items[indx] = (VsqEvent)ve.clone();
    original_symbol[indx] = ve.ID.LyricHandle.L0.getPhoneticSymbol();
    original_phrase[indx] = ve.ID.LyricHandle.L0.Phrase;
    symbol_protected[indx] = ve.ID.LyricHandle.L0.PhoneticSymbolProtected;
}

String[] phrase = new String[count];
String[] phonetic_symbol = new String[count];
for ( int i = 0; i < count; i++ ) {
    phrase[i] = original_phrase[i];
    phonetic_symbol[i] = original_symbol[i];
}
String txt = AppManager.mInputTextBox.getText();
int txtlen = PortUtil.getStringLength( txt );
if ( txtlen > 0 ) {
    // 1文字目は，UTAUの連続音入力のハイフンの可能性があるので，無駄に置換されるのを回避
    phrase[0] = str.sub( txt, 0, 1 ) + ((txtlen > 1) ? str.sub( txt, 1 ).replace( "-", "" ) : "");
} else {
    phrase[0] = "";
}
if ( !phonetic_symbol_edit_mode ) {
    // 歌詞を編集するモードで、
    if ( AppManager.editorConfig.SelfDeRomanization ) {
        // かつローマ字の入力を自動でひらがなに展開する設定だった場合。
        // ローマ字をひらがなに展開
        phrase[0] = KanaDeRomanization.Attach( phrase[0] );
    }
}

// 発音記号または歌詞が変更された場合の処理
if ( (phonetic_symbol_edit_mode && !str.compare( AppManager.mInputTextBox.getText(), original_symbol[0] )) ||
     (!phonetic_symbol_edit_mode && !str.compare( phrase[0], original_phrase[0] )) ) {
    if ( phonetic_symbol_edit_mode ) {
        // 発音記号を編集するモード
        phrase[0] = AppManager.mInputTextBox.getBufferText();
        phonetic_symbol[0] = AppManager.mInputTextBox.getText();

        // 入力された発音記号のうち、有効なものだけをピックアップ
        String[] spl = PortUtil.splitString( phonetic_symbol[0], new char[] { ' ' }, true );
        Vector<String> list = new Vector<String>();
        for ( int i = 0; i < spl.length; i++ ) {
            String s = spl[i];
            if ( VsqPhoneticSymbol.isValidSymbol( s ) ) {
                list.add( s );
            }
        }

        // ピックアップした発音記号をスペース区切りで結合
        phonetic_symbol[0] = "";
        boolean first = true;
        for ( Iterator<String> itr = list.iterator(); itr.hasNext(); ) {
            String s = itr.next();
            if ( first ) {
                phonetic_symbol[0] += s;
                first = false;
            } else {
                phonetic_symbol[0] += " " + s;
            }
        }

        // 発音記号を編集すると、自動で「発音記号をプロテクトする」モードになるよ
        symbol_protected[0] = true;
    } else {
        // 歌詞を編集するモード
        if ( !symbol_protected[0] ) {
            // 発音記号をプロテクトしない場合、歌詞から発音記号を引当てる
            SymbolTableEntry entry = SymbolTable.attatch( phrase[0] );
            if ( entry == null ) {
                phonetic_symbol[0] = "a";
            } else {
                phonetic_symbol[0] = entry.getSymbol();
                // 分節の分割記号'-'が入っている場合
                if ( entry.Word.indexOf( '-' ) >= 0 ) {
                    String[] spl_phrase = PortUtil.splitString( entry.Word, '\t' );
                    if ( spl_phrase.length <= count ) {
                        // 分節の分割数が，後続の音符数と同じか少ない場合
                        String[] spl_symbol = PortUtil.splitString( entry.getRawSymbol(), '\t' );
                        for ( int i = 0; i < spl_phrase.length; i++ ) {
                            phrase[i] = spl_phrase[i];
                            phonetic_symbol[i] = spl_symbol[i];
                        }
                    } else {
                        // 後続の音符の個数が足りない
                        phrase[0] = entry.Word.replace( "\t", "" );
                    }
                }
            }
        } else {
            // 発音記号をプロテクトする場合、発音記号は最初のやつを使う
            phonetic_symbol[0] = original_symbol[0];
        }
    }

    for ( int j = 0; j < count; j++ ) {
        if ( phonetic_symbol_edit_mode ) {
            items[j].ID.LyricHandle.L0.setPhoneticSymbol( phonetic_symbol[j] );
        } else {
            items[j].ID.LyricHandle.L0.Phrase = phrase[j];
            items[j].ID.LyricHandle.L0.setPhoneticSymbol( phonetic_symbol[j] );
            AppManager.applyUtauParameter( vsq_track, items[j] );
        }
        if ( !str.compare( original_symbol[j], phonetic_symbol[j] ) ) {
            Vector<String> spl = items[j].ID.LyricHandle.L0.getPhoneticSymbolList();
            Vector<Integer> adjustment = new Vector<Integer>();
            for ( int i = 0; i < vec.size( spl ); i++ ) {
                String s = vec.get( spl, i );
                adjustment.add( VsqPhoneticSymbol.isConsonant( s ) ? 64 : 0 );
            }
            items[j].ID.LyricHandle.L0.setConsonantAdjustmentList( adjustment );
        }
    }

    CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandEventReplaceRange( selected, items ) );
    AppManager.editHistory.register( vsq.executeCommand( run ) );
    setEdited( true );
}
        }

        /// <summary>
        /// 識別済みのゲームコントローラを取り外します
        /// </summary>
        public void removeGameControler()
        {
        }

        /// <summary>
        /// PCに接続されているゲームコントローラを識別・接続します
        /// </summary>
        public void loadGameControler()
        {
        }

        /// <summary>
        /// MIDI入力句デバイスを再読込みします
        /// </summary>
        public void reloadMidiIn()
        {
if ( mMidiIn != null ) {
    mMidiIn.midiReceivedEvent.remove( new MidiReceivedEventHandler( this, "mMidiIn_MidiReceived" ) );
    mMidiIn.close();
    mMidiIn = null;
}
int portNumber = AppManager.editorConfig.MidiInPort.PortNumber;
int portNumberMtc = AppManager.editorConfig.MidiInPortMtc.PortNumber;
try {
    mMidiIn = new MidiInDevice( portNumber );
    mMidiIn.midiReceivedEvent.add( new MidiReceivedEventHandler( this, "mMidiIn_MidiReceived" ) );
    mMidiIn.setReceiveSystemCommonMessage( false );
    mMidiIn.setReceiveSystemRealtimeMessage( false );
} catch ( Exception ex ) {
    ex.printStackTrace();
    Logger.write( FormMain.class + ".reloadMidiIn; ex=" + ex + "\n" );
    serr.println( "FormMain#reloadMidiIn; ex=" + ex );
}

updateMidiInStatus();
        }

        public void updateMidiInStatus()
        {
int midiport = AppManager.editorConfig.MidiInPort.PortNumber;
Vector<MidiDevice.Info> devices = new Vector<MidiDevice.Info>();
for ( MidiDevice.Info info : MidiSystem.getMidiDeviceInfo() ){
    MidiDevice device = null;
    try{
        device = MidiSystem.getMidiDevice( info );
    }catch( Exception ex ){
        device = null;
    }
    if( device == null ) continue;
    int max = device.getMaxTransmitters();
    if( max > 0 || max == -1 ){
        devices.add( info );
    }
}
if ( midiport < 0 || vec.size( devices ) <= 0 ) {
    stripBtnStepSequencer.setEnabled( false );
} else {
    if ( midiport >= vec.size( devices ) ) {
        midiport = 0;
        AppManager.editorConfig.MidiInPort.PortNumber = midiport;
    }
    stripBtnStepSequencer.setEnabled( true );
}
        }

        /// <summary>
        /// 指定したノートナンバーが可視状態となるよう、縦スクロールバーを移動させます。
        /// </summary>
        /// <param name="note"></param>
        public void ensureVisibleY( int note )
        {
if ( note <= 0 ) {
    vScroll.setValue( vScroll.getMaximum() - vScroll.getVisibleAmount() );
    return;
} else if ( note >= 127 ) {
    vScroll.setValue( vScroll.getMinimum() );
    return;
}
int height = pictPianoRoll.getHeight();
int noteTop = AppManager.noteFromYCoord( 0 ); //画面上端でのノートナンバー
int noteBottom = AppManager.noteFromYCoord( height ); // 画面下端でのノートナンバー

int maximum = vScroll.getMaximum();
int track_height = (int)(100 * controller.getScaleY());
// ノートナンバーnoteの現在のy座標がいくらか？
int note_y = AppManager.yCoordFromNote( note );
if ( note < noteBottom ) {
    // ノートナンバーnoteBottomの現在のy座標が新しいnoteのy座標と同一になるよう，startToDrawYを変える
    // startToDrawYを次の値にする必要がある
    int new_start_to_draw_y = controller.getStartToDrawY() + (note_y - height);
    int value = calculateVScrollValueFromStartToDrawY( new_start_to_draw_y );
    vScroll.setValue( value );
} else if ( noteTop < note ) {
    // ノートナンバーnoteTopの現在のy座標が，ノートナンバーnoteの新しいy座標と同一になるよう，startToDrawYを変える
    int new_start_to_draw_y = controller.getStartToDrawY() + (note_y - 0);
    int value = calculateVScrollValueFromStartToDrawY( new_start_to_draw_y );
    vScroll.setValue( value );
}
        }

        /// <summary>
        /// 指定したゲートタイムがピアノロール上で可視状態となるよう、横スクロールバーを移動させます。
        /// </summary>
        /// <param name="clock"></param>
        public void ensureVisible( int clock )
        {
// カーソルが画面内にあるかどうか検査
int clock_left = AppManager.clockFromXCoord( AppManager.keyWidth );
int clock_right = AppManager.clockFromXCoord( pictPianoRoll.getWidth() );
int uwidth = clock_right - clock_left;
if ( clock < clock_left || clock_right < clock ) {
    int cl_new_center = (clock / uwidth) * uwidth + uwidth / 2;
    float f_draft = cl_new_center - (pictPianoRoll.getWidth() / 2 + 34 - 70) * controller.getScaleXInv();
    if ( f_draft < 0f ) {
        f_draft = 0;
    }
    int draft = (int)(f_draft);
    if ( draft < hScroll.getMinimum() ) {
        draft = hScroll.getMinimum();
    } else if ( hScroll.getMaximum() < draft ) {
        draft = hScroll.getMaximum();
    }
    if ( hScroll.getValue() != draft ) {
        AppManager.mDrawStartIndex[AppManager.getSelected() - 1] = 0;
        hScroll.setValue( draft );
    }
}
        }

        /// <summary>
        /// プレイカーソルが見えるようスクロールする
        /// </summary>
        public void ensureCursorVisible()
        {
ensureVisible( AppManager.getCurrentClock() );
        }

        /// <summary>
        /// 特殊なショートカットキーを処理します。
        /// </summary>
        /// <param name="e"></param>
        /// <param name="onPreviewKeyDown">PreviewKeyDownイベントから送信されてきた場合、true（送る側が設定する）</param>
        public void processSpecialShortcutKey( BKeyEventArgs e, boolean onPreviewKeyDown )
        {
// 歌詞入力用のテキストボックスが表示されていたら，何もしない
if ( AppManager.mInputTextBox.isVisible() ) {
    AppManager.mInputTextBox.requestFocus();
    return;
}

boolean flipPlaying = false; // 再生/停止状態の切り替えが要求されたらtrue

// 最初に、特殊な取り扱いが必要なショートカット、について、
// 該当するショートカットがあればそいつらを発動する。
int modifier = PortUtil.getCurrentModifierKey();
KeyStroke stroke = KeyStroke.getKeyStroke( e.KeyValue, modifier );
int keycode = e.KeyValue;

if ( onPreviewKeyDown && keycode != 0 ) {
    for ( SpecialShortcutHolder holder : mSpecialShortcutHolders ) {
        if ( stroke.equals( holder.shortcut ) ) {
            try {
                holder.menu.clickEvent.raise( holder.menu, new BEventArgs() );
            } catch ( Exception ex ) {
                Logger.write( FormMain.class + ".processSpecialShortcutKey; ex=" + ex + "\n" );
                serr.println( "FormMain#processSpecialShortcutKey; ex=" + ex );
            }
            if ( e.KeyValue == KeyEvent.VK_TAB ) {
                focusPianoRoll();
            }
            return;
        }
    }
}

if ( modifier != KeyEvent.VK_UNDEFINED ) {
    return;
}

EditMode edit_mode = AppManager.getEditMode();

if ( e.KeyValue == KeyEvent.VK_ENTER ) {
    // MIDIステップ入力のときの処理
    if ( controller.isStepSequencerEnabled() ) {
        if ( AppManager.mAddingEvent != null ) {
            fixAddingEvent();
            AppManager.mAddingEvent = null;
            refreshScreen( true );
        }
    }
} else if ( e.KeyValue == KeyEvent.VK_SPACE ) {
    if ( !AppManager.editorConfig.UseSpaceKeyAsMiddleButtonModifier ) {
        flipPlaying = true;
    }
} else if ( e.KeyValue == KeyEvent.VK_PERIOD ) {
    if ( !onPreviewKeyDown ) {

        if ( AppManager.isPlaying() ) {
            AppManager.setPlaying( false, this );
        } else {
            VsqFileEx vsq = AppManager.getVsqFile();
            if ( !vsq.config.StartMarkerEnabled ) {
                AppManager.setCurrentClock( 0 );
            } else {
                AppManager.setCurrentClock( vsq.config.StartMarker );
            }
            refreshScreen();
        }
    }
} else if( e.KeyValue == KeyEvent.VK_ADD || e.KeyValue == KeyEvent.VK_PLUS || e.KeyValue == KeyEvent.VK_RIGHT ) {
    if ( onPreviewKeyDown ) {
        forward();
    }
} else if ( e.KeyValue == KeyEvent.VK_MINUS || e.KeyValue == KeyEvent.VK_LEFT ) {
    if ( onPreviewKeyDown ) {
        rewind();
    }
} else if ( e.KeyValue == KeyEvent.VK_ESCAPE ) {
    // ステップ入力中の場合，入力中の音符をクリアする
    VsqEvent item = AppManager.mAddingEvent;
    if ( controller.isStepSequencerEnabled() && item != null ) {
        // 入力中だった音符の長さを取得し，
        int length = item.ID.getLength();
        AppManager.mAddingEvent = null;
        int clock = AppManager.getCurrentClock();
        int clock_draft = clock - length;
        if ( clock_draft < 0 ) {
            clock_draft = 0;
        }
        // その分だけソングポジションを戻す．
        AppManager.setCurrentClock( clock_draft );
        refreshScreen( true );
    }
} else {
    if ( !AppManager.isPlaying() ) {
        // 最初に戻る、の機能を発動
        BKeys[] specialGoToFirst = AppManager.editorConfig.SpecialShortcutGoToFirst;
        if ( specialGoToFirst != null && specialGoToFirst.length > 0 ) {
            KeyStroke ks = BKeysUtility.getKeyStrokeFromBKeys( specialGoToFirst );
            if( e.KeyCode == ks.getKeyCode() )
            {
                AppManager.setCurrentClock( 0 );
                ensureCursorVisible();
                refreshScreen();
            }
        }
    }
}
if ( !onPreviewKeyDown && flipPlaying ) {
    if ( AppManager.isPlaying() ) {
        double elapsed = PlaySound.getPosition();
        double threshold = AppManager.mForbidFlipPlayingThresholdSeconds;
        if ( threshold < 0 ) {
            threshold = 0.0;
        }
        if ( elapsed > threshold ) {
            timer.stop();
            AppManager.setPlaying( false, this );
        }
    } else {
        AppManager.setPlaying( true, this );
    }
}
if ( e.KeyValue == KeyEvent.VK_TAB ) {
    focusPianoRoll();
}
        }

        public void updateScrollRangeHorizontal()
        {
// コンポーネントの高さが0の場合，スクロールの設定が出来ないので．
int pwidth = pictPianoRoll.getWidth();
int hwidth = hScroll.getWidth();
if ( pwidth <= 0 || hwidth <= 0 ) {
    return;
}

VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) return;
int l = vsq.TotalClocks;
float scalex = controller.getScaleX();
int key_width = AppManager.keyWidth;
int pict_piano_roll_width = pwidth - key_width;
int large_change = (int)(pict_piano_roll_width / scalex);
int maximum = (int)(l + large_change);

int thumb_width = 40;
int box_width = (int)(large_change / (float)maximum * (hwidth - 2 * thumb_width));
if ( box_width < AppManager.editorConfig.MinimumScrollHandleWidth ) {
    box_width = AppManager.editorConfig.MinimumScrollHandleWidth;
    if ( hwidth - 2 * thumb_width > box_width ) {
        maximum = l * (hwidth - 2 * thumb_width) / (hwidth - 2 * thumb_width - box_width);
        large_change = l * box_width / (hwidth - 2 * thumb_width - box_width);
    }
}

if ( large_change <= 0 ) large_change = 1;
if ( maximum <= 0 ) maximum = 1;
hScroll.setVisibleAmount( large_change );
hScroll.setMaximum( maximum );
int unit_increment = large_change / 10;
if( unit_increment <= 0 ){
    unit_increment = 1;
}
hScroll.setUnitIncrement( unit_increment );
hScroll.setBlockIncrement( large_change );

int old_value = hScroll.getValue();
if ( old_value > maximum - large_change ) {
    hScroll.setValue( maximum - large_change );
}
        }

        public void updateScrollRangeVertical()
        {
// コンポーネントの高さが0の場合，スクロールの設定が出来ないので．
int pheight = pictPianoRoll.getHeight();
int vheight = vScroll.getHeight();
if ( pheight <= 0 || vheight <= 0 ) {
    return;
}

float scaley = controller.getScaleY();

int maximum = (int)(128 * (int)(100 * scaley) / scaley);
int large_change = (int)(pheight / scaley);

int thumb_height = 40;
int box_height = (int)(large_change / (float)maximum * (vheight - 2 * thumb_height));
if ( box_height < AppManager.editorConfig.MinimumScrollHandleWidth ) {
    box_height = AppManager.editorConfig.MinimumScrollHandleWidth;
    maximum = (int)(((128.0 * (int)(100 * scaley) - pheight) / scaley) * (vheight - 2 * thumb_height) / (vheight - 2 * thumb_height - box_height));
    large_change = (int)(((128.0 * (int)(100 * scaley) - pheight) / scaley) * box_height / (vheight - 2 * thumb_height - box_height));
}

if ( large_change <= 0 ) large_change = 1;
if ( maximum <= 0 ) maximum = 1;
vScroll.setVisibleAmount( large_change );
vScroll.setMaximum( maximum );

int unit_increment = large_change / 10;
if( unit_increment <= 0 ){
    unit_increment = 1;
}
vScroll.setUnitIncrement( unit_increment );
vScroll.setBlockIncrement( large_change );

int old_value = vScroll.getValue();
if ( old_value > maximum - large_change ) {
    vScroll.setValue( maximum - large_change );
}
        }

        /// <summary>
        /// コントロールトラックの表示・非表示状態を更新します
        /// </summary>
        public void flipControlCurveVisible( boolean visible )
        {
trackSelector.setCurveVisible( visible );
if ( visible ) {
    splitContainer1.setSplitterFixed( false );
    splitContainer1.setDividerSize( _SPL_SPLITTER_WIDTH );
    splitContainer1.setDividerLocation( splitContainer1.getHeight() - AppManager.mLastTrackSelectorHeight - splitContainer1.getDividerSize() );
    splitContainer1.setPanel2MinSize( trackSelector.getPreferredMinSize() );
} else {
    AppManager.mLastTrackSelectorHeight = splitContainer1.getHeight() - splitContainer1.getDividerLocation() - splitContainer1.getDividerSize();
    splitContainer1.setSplitterFixed( true );
    splitContainer1.setDividerSize( 0 );
    int panel2height = TrackSelector.OFFSET_TRACK_TAB * 2;
    splitContainer1.setDividerLocation( splitContainer1.getHeight() - panel2height - splitContainer1.getDividerSize() );
    splitContainer1.setPanel2MinSize( panel2height );
}
refreshScreen();
        }

        /// <summary>
        /// ミキサーダイアログの表示・非表示状態を更新します
        /// </summary>
        /// <param name="visible">表示状態にする場合true，そうでなければfalse</param>
        public void flipMixerDialogVisible( boolean visible )
        {
AppManager.mMixerWindow.setVisible( visible );
AppManager.editorConfig.MixerVisible = visible;
if( visible != menuVisualMixer.isSelected() ){
    menuVisualMixer.setSelected( visible );
}
        }

        /// <summary>
        /// アイコンパレットの表示・非表示状態を更新します
        /// </summary>
        public void flipIconPaletteVisible( boolean visible )
        {
AppManager.iconPalette.setVisible( visible );
AppManager.editorConfig.IconPaletteVisible = visible;
if( visible != menuVisualIconPalette.isSelected() ){
    menuVisualIconPalette.setSelected( visible );
}
        }

        /// <summary>
        /// メニューのショートカットキーを、AppManager.EditorConfig.ShorcutKeysの内容に応じて変更します
        /// </summary>
        public void applyShortcut()
        {
mSpecialShortcutHolders.clear();

TreeMap<String, BKeys[]> dict = AppManager.editorConfig.getShortcutKeysDictionary( this.getDefaultShortcutKeys() );
ByRef<Object> parent = new ByRef<Object>( null );
for ( Iterator<String> itr = dict.keySet().iterator(); itr.hasNext(); ) {
    String key = itr.next();
    if ( str.compare( key, "menuEditCopy" ) || str.compare( key, "menuEditCut" ) || str.compare( key, "menuEditPaste" ) || str.compare( key, "SpecialShortcutGoToFirst" ) ) {
        continue;
    }
    Object menu = searchMenuItemFromName( key, parent );
    if ( menu != null ) {
        String menu_name = "";
        if( menu instanceof Component ){
            menu_name = ((Component)menu).getName();
        }else{
            continue;
        }
        applyMenuItemShortcut( dict, menu, menu_name );
    }
}
if ( dict.containsKey( "menuEditCopy" ) ) {
    applyMenuItemShortcut( dict, menuHiddenCopy, "menuEditCopy" );
}
if ( dict.containsKey( "menuEditCut" ) ) {
    applyMenuItemShortcut( dict, menuHiddenCut, "menuEditCut" );
}
if ( dict.containsKey( "menuEditCopy" ) ) {
    applyMenuItemShortcut( dict, menuHiddenPaste, "menuEditPaste" );
}

Vector<ValuePair<String, BMenuItem[]>> work = new Vector<ValuePair<String, BMenuItem[]>>();
work.add( new ValuePair<String, BMenuItem[]>( "menuEditUndo", new BMenuItem[] { cMenuPianoUndo, cMenuTrackSelectorUndo } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditRedo", new BMenuItem[] { cMenuPianoRedo, cMenuTrackSelectorRedo } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditCut", new BMenuItem[] { cMenuPianoCut, cMenuTrackSelectorCut, menuEditCut } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditCopy", new BMenuItem[] { cMenuPianoCopy, cMenuTrackSelectorCopy, menuEditCopy } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditPaste", new BMenuItem[] { cMenuPianoPaste, cMenuTrackSelectorPaste, menuEditPaste } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditSelectAll", new BMenuItem[] { cMenuPianoSelectAll, cMenuTrackSelectorSelectAll } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditSelectAllEvents", new BMenuItem[] { cMenuPianoSelectAllEvents } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuEditDelete", new BMenuItem[] { menuEditDelete } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuVisualGridline", new BMenuItem[] { cMenuPianoGrid } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuJobLyric", new BMenuItem[] { cMenuPianoImportLyric } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuLyricExpressionProperty", new BMenuItem[] { cMenuPianoExpressionProperty } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuLyricVibratoProperty", new BMenuItem[] { cMenuPianoVibratoProperty } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackOn", new BMenuItem[] { cMenuTrackTabTrackOn } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackAdd", new BMenuItem[] { cMenuTrackTabAdd } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackCopy", new BMenuItem[] { cMenuTrackTabCopy } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackDelete", new BMenuItem[] { cMenuTrackTabDelete } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRenderCurrent", new BMenuItem[] { cMenuTrackTabRenderCurrent } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRenderAll", new BMenuItem[] { cMenuTrackTabRenderAll } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackOverlay", new BMenuItem[] { cMenuTrackTabOverlay } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRendererVOCALOID1", new BMenuItem[] { cMenuTrackTabRendererVOCALOID1 } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRendererVOCALOID2", new BMenuItem[] { cMenuTrackTabRendererVOCALOID2 } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRendererAquesTone", new BMenuItem[] { menuTrackRendererAquesTone } ) );
work.add( new ValuePair<String, BMenuItem[]>( "menuTrackRendererVCNT", new BMenuItem[] { menuTrackRendererVCNT } ) );
int c = work.size();
for ( int j = 0; j < c; j++ ) {
    ValuePair<String, BMenuItem[]> item = work.get( j );
    if ( dict.containsKey( item.getKey() ) ) {
        BKeys[] k = dict.get( item.getKey() );
        String s = Utility.getShortcutDisplayString( k );
    }
}

// ミキサーウィンドウ
if ( AppManager.mMixerWindow != null ) {
    if ( dict.containsKey( "menuVisualMixer" ) ) {
        KeyStroke shortcut = BKeysUtility.getKeyStrokeFromBKeys( dict.get( "menuVisualMixer" ) );
        AppManager.mMixerWindow.applyShortcut( shortcut );
    }
}

// アイコンパレット
if ( AppManager.iconPalette != null ) {
    if ( dict.containsKey( "menuVisualIconPalette" ) ) {
        KeyStroke shortcut = BKeysUtility.getKeyStrokeFromBKeys( dict.get( "menuVisualIconPalette" ) );
        AppManager.iconPalette.applyShortcut( shortcut );
    }
}

// プロパティ
if( AppManager.propertyWindow != null ){
    if( dict.containsKey( menuVisualProperty.getName() ) ){
        KeyStroke shortcut = BKeysUtility.getKeyStrokeFromBKeys( dict.get( menuVisualProperty.getName() ) );
        AppManager.propertyWindow.applyShortcut( shortcut );
    }
}

// スクリプトにショートカットを適用
MenuElement[] sub_menu_script = menuScript.getSubElements();
for ( int i = 0; i < sub_menu_script.length; i++ ) {
    MenuElement tsi = sub_menu_script[i];
    MenuElement[] sub_tsi = tsi.getSubElements();
    if ( sub_tsi.length == 1 ) {
        MenuElement dd_run = sub_tsi[0];
        if ( dict.containsKey( PortUtil.getComponentName( dd_run ) ) ) {
            applyMenuItemShortcut( dict, tsi, PortUtil.getComponentName( tsi ) );
        }
    }
}
        }

        /// <summary>
        /// dictの中から
        /// </summary>
        /// <param name="dict"></param>
        /// <param name="item"></param>
        /// <param name="item_name"></param>
        /// <param name="default_shortcut"></param>
        public void applyMenuItemShortcut( TreeMap<String, BKeys[]> dict, Object item, String item_name )
        {
if( item == null ){
    return;
}
if( item instanceof JMenu ){
    return;
}
if( !(item instanceof BMenuItem) ){
    return;
}
BMenuItem menu = (BMenuItem)item;
menu.setAccelerator( null );
if( !dict.containsKey( item_name ) ){
    return;
}
BKeys[] k = dict.get( item_name );
if( k == null ){
    return;
}
if( k.length <= 0 ){
    return;
}
try {
    sout.println( "FormMain#applyMenuItemShortcut; item_name=" + item_name );
    KeyStroke ks = BKeysUtility.getKeyStrokeFromBKeys( k );
    if( str.startsWith( item_name, "menuHidden" ) ){
        mSpecialShortcutHolders.add(
            new SpecialShortcutHolder( BKeysUtility.getKeyStrokeFromBKeys( k ), menu ) );
    }else{
        menu.setAccelerator( ks );
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class  + ".applyMenuItemShortcut; ex=" + ex + "\n" );
    ex.printStackTrace();
}
        }

        /// <summary>
        /// ソングポジションを1小節進めます
        /// </summary>
        public void forward()
        {
boolean playing = AppManager.isPlaying();
if ( playing ) {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
int cl_clock = AppManager.getCurrentClock();
int unit = QuantizeModeUtil.getQuantizeClock(
    AppManager.editorConfig.getPositionQuantize(),
    AppManager.editorConfig.isPositionQuantizeTriplet() );
int cl_new = doQuantize( cl_clock + unit, unit );

if ( cl_new <= hScroll.getMaximum() + (pictPianoRoll.getWidth() - AppManager.keyWidth) * controller.getScaleXInv() ) {
    // 表示の更新など
    AppManager.setCurrentClock( cl_new );

    // ステップ入力時の処理
    updateNoteLengthStepSequencer();

    ensureCursorVisible();
    AppManager.setPlaying( playing, this );
    refreshScreen();
}
        }

        /// <summary>
        /// ソングポジションを1小節戻します
        /// </summary>
        public void rewind()
        {
boolean playing = AppManager.isPlaying();
if ( playing ) {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
int cl_clock = AppManager.getCurrentClock();
int unit = QuantizeModeUtil.getQuantizeClock(
    AppManager.editorConfig.getPositionQuantize(),
    AppManager.editorConfig.isPositionQuantizeTriplet() );
int cl_new = doQuantize( cl_clock - unit, unit );
if ( cl_new < 0 ) {
    cl_new = 0;
}

AppManager.setCurrentClock( cl_new );

// ステップ入力時の処理
updateNoteLengthStepSequencer();

ensureCursorVisible();
AppManager.setPlaying( playing, this );
refreshScreen();
        }

        /// <summary>
        /// cMenuPianoの固定長音符入力の各メニューのチェック状態をm_pencil_modeを元に更新します
        /// </summary>
        public void updateCMenuPianoFixed()
        {
cMenuPianoFixed01.setSelected( false );
cMenuPianoFixed02.setSelected( false );
cMenuPianoFixed04.setSelected( false );
cMenuPianoFixed08.setSelected( false );
cMenuPianoFixed16.setSelected( false );
cMenuPianoFixed32.setSelected( false );
cMenuPianoFixed64.setSelected( false );
cMenuPianoFixed128.setSelected( false );
cMenuPianoFixedOff.setSelected( false );
cMenuPianoFixedTriplet.setSelected( false );
cMenuPianoFixedDotted.setSelected( false );
PencilModeEnum mode = mPencilMode.getMode();
if ( mode == PencilModeEnum.L1 ) {
    cMenuPianoFixed01.setSelected( true );
} else if ( mode == PencilModeEnum.L2 ) {
    cMenuPianoFixed02.setSelected( true );
} else if ( mode == PencilModeEnum.L4 ) {
    cMenuPianoFixed04.setSelected( true );
} else if ( mode == PencilModeEnum.L8 ) {
    cMenuPianoFixed08.setSelected( true );
} else if ( mode == PencilModeEnum.L16 ) {
    cMenuPianoFixed16.setSelected( true );
} else if ( mode == PencilModeEnum.L32 ) {
    cMenuPianoFixed32.setSelected( true );
} else if ( mode == PencilModeEnum.L64 ) {
    cMenuPianoFixed64.setSelected( true );
} else if ( mode == PencilModeEnum.L128 ) {
    cMenuPianoFixed128.setSelected( true );
} else if ( mode == PencilModeEnum.Off ) {
    cMenuPianoFixedOff.setSelected( true );
}
cMenuPianoFixedTriplet.setSelected( mPencilMode.isTriplet() );
cMenuPianoFixedDotted.setSelected( mPencilMode.isDot() );
        }

        public void clearTempWave()
        {
String tmppath = fsys.combine( AppManager.getCadenciiTempDir(), AppManager.getID() );
if ( !fsys.isDirectoryExists( tmppath ) ) {
    return;
}

// 今回このPCが起動されるよりも以前に，Cadenciiが残したデータを削除する
//TODO: システムカウンタは約49日でリセットされてしまい，厳密には実装できないようなので，保留．

// このFormMainのインスタンスが使用したデータを消去する
for ( int i = 1; i <= 16; i++ ) {
    String file = fsys.combine( tmppath, i + ".wav" );
    if ( fsys.isFileExists( file ) ) {
        for ( int error = 0; error < 100; error++ ) {
            try {
                PortUtil.deleteFile( file );
                break;
            } catch ( Exception ex ) {
                Logger.write( FormMain.class + ".clearTempWave; ex=" + ex + "\n" );

                try{
                    Thread.sleep( 100 );
                }catch( Exception ex2 ){
                    Logger.write( FormMain.class + ".clearTempWave; ex=" + ex2 + "\n" );
                }
            }
        }
    }
}
String whd = fsys.combine( tmppath, UtauWaveGenerator.FILEBASE + ".whd" );
if ( fsys.isFileExists( whd ) ) {
    try {
        PortUtil.deleteFile( whd );
    } catch ( Exception ex ) {
        Logger.write( FormMain.class + ".clearTempWave; ex=" + ex + "\n" );
    }
}
String dat = fsys.combine( tmppath, UtauWaveGenerator.FILEBASE + ".dat" );
if ( fsys.isFileExists( dat ) ) {
    try {
        PortUtil.deleteFile( dat );
    } catch ( Exception ex ) {
        Logger.write( FormMain.class + ".clearTempWave; ex=" + ex + "\n" );
    }
}
        }

        /// <summary>
        /// 鍵盤音キャッシュの中から指定したノートナンバーの音源を捜し、再生します。
        /// </summary>
        /// <param name="note">再生する音の高さを指定するノートナンバー</param>
        public void playPreviewSound( int note )
        {
KeySoundPlayer.play( note );
        }


        /// <summary>
        /// このコンポーネントの表示言語を、現在の言語設定に従って更新します。
        /// </summary>
        public void applyLanguage()
        {
openXmlVsqDialog.clearChoosableFileFilter();
try {
    openXmlVsqDialog.addFileFilter( _( "XML-VSQ Format(*.xvsq)|*.xvsq" ) );
    openXmlVsqDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    openXmlVsqDialog.addFileFilter( "XML-VSQ Format(*.xvsq)|*.xvsq" );
    openXmlVsqDialog.addFileFilter( "All Files(*.*)|*.*" );
}

saveXmlVsqDialog.clearChoosableFileFilter();
try {
    saveXmlVsqDialog.addFileFilter( _( "XML-VSQ Format(*.xvsq)|*.xvsq" ) );
    saveXmlVsqDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    saveXmlVsqDialog.addFileFilter( "XML-VSQ Format(*.xvsq)|*.xvsq" );
    saveXmlVsqDialog.addFileFilter( "All Files(*.*)|*.*" );
}

openUstDialog.clearChoosableFileFilter();
try {
    openUstDialog.addFileFilter( _( "UTAU Script Format(*.ust)|*.ust" ) );
    openUstDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    openUstDialog.addFileFilter( "UTAU Script Format(*.ust)|*.ust" );
    openUstDialog.addFileFilter( "All Files(*.*)|*.*" );
}

openMidiDialog.clearChoosableFileFilter();
try {
    openMidiDialog.addFileFilter( _( "MIDI Format(*.mid)|*.mid" ) );
    openMidiDialog.addFileFilter( _( "VSQ Format(*.vsq)|*.vsq" ) );
    openMidiDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    openMidiDialog.addFileFilter( "MIDI Format(*.mid)|*.mid" );
    openMidiDialog.addFileFilter( "VSQ Format(*.vsq)|*.vsq" );
    openMidiDialog.addFileFilter( "All Files(*.*)|*.*" );
}

saveMidiDialog.clearChoosableFileFilter();
try {
    saveMidiDialog.addFileFilter( _( "MIDI Format(*.mid)|*.mid" ) );
    saveMidiDialog.addFileFilter( _( "VSQ Format(*.vsq)|*.vsq" ) );
    saveMidiDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    saveMidiDialog.addFileFilter( "MIDI Format(*.mid)|*.mid" );
    saveMidiDialog.addFileFilter( "VSQ Format(*.vsq)|*.vsq" );
    saveMidiDialog.addFileFilter( "All Files(*.*)|*.*" );
}

openWaveDialog.clearChoosableFileFilter();
try {
    openWaveDialog.addFileFilter( _( "Wave File(*.wav)|*.wav" ) );
    openWaveDialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".applyLanguage; ex=" + ex + "\n" );
    openWaveDialog.addFileFilter( "Wave File(*.wav)|*.wav" );
    openWaveDialog.addFileFilter( "All Files(*.*)|*.*" );
}


updateGameControlerStatus( this, new BEventArgs() );

stripBtnPointer.setText( _( "Pointer" ) );
stripBtnPointer.setToolTipText( _( "Pointer" ) );
stripBtnPencil.setText( _( "Pencil" ) );
stripBtnPencil.setToolTipText( _( "Pencil" ) );
stripBtnLine.setText( _( "Line" ) );
stripBtnLine.setToolTipText( _( "Line" ) );
stripBtnEraser.setText( _( "Eraser" ) );
stripBtnEraser.setToolTipText( _( "Eraser" ) );
//stripBtnCurve.setText( _( "Curve" ) );
stripBtnCurve.setToolTipText( _( "Curve" ) );
//stripBtnGrid.setText( _( "Grid" ) );
stripBtnGrid.setToolTipText( _( "Grid" ) );
if ( AppManager.isPlaying() ) {
    stripBtnPlay.setText( _( "Stop" ) );
} else {
    stripBtnPlay.setText( _( "Play" ) );
}


menuFile.setText( _( "File" ) );
menuFile.setMnemonic( KeyEvent.VK_F );
menuFileNew.setText( _( "New" ) );
menuFileNew.setMnemonic( KeyEvent.VK_N );
menuFileOpen.setText( _( "Open" ) );
menuFileOpen.setMnemonic( KeyEvent.VK_O );
menuFileOpenVsq.setText( _( "Open VSQ/Vocaloid MIDI" ) );
menuFileOpenVsq.setMnemonic( KeyEvent.VK_V );
menuFileOpenUst.setText( _( "Open UTAU project file" ) );
menuFileOpenUst.setMnemonic( KeyEvent.VK_U );
menuFileSave.setText( _( "Save" ) );
menuFileSave.setMnemonic( KeyEvent.VK_S );
menuFileSaveNamed.setText( _( "Save as" ) );
menuFileSaveNamed.setMnemonic( KeyEvent.VK_A );
menuFileImport.setText( _( "Import" ) );
menuFileImport.setMnemonic( KeyEvent.VK_I );
menuFileImportVsq.setText( _( "VSQ / Vocaloid Midi" ) );
menuFileExport.setText( _( "Export" ) );
menuFileExport.setMnemonic( KeyEvent.VK_E );
menuFileExportWave.setText( _( "WAVE" ) );
menuFileExportParaWave.setText( _( "Serial numbered WAVEs" ) );
menuFileExportUst.setText( _( "UTAU project file" ) );
menuFileExportVxt.setText( _( "Metatext for vConnect" ) );
menuFileRecent.setText( _( "Open Recent" ) );
menuFileRecent.setMnemonic( KeyEvent.VK_R );
menuFileRecentClear.setText( _( "Clear Menu" ) );
menuFileQuit.setText( _( "Quit" ) );
menuFileQuit.setMnemonic( KeyEvent.VK_Q );

menuEdit.setText( _( "Edit" ) );
menuEdit.setMnemonic( KeyEvent.VK_E );
menuEditUndo.setText( _( "Undo" ) );
menuEditUndo.setMnemonic( KeyEvent.VK_U );
menuEditRedo.setText( _( "Redo" ) );
menuEditRedo.setMnemonic( KeyEvent.VK_R );
menuEditCut.setText( _( "Cut" ) );
menuEditCut.setMnemonic( KeyEvent.VK_T );
menuEditCopy.setText( _( "Copy" ) );
menuEditCopy.setMnemonic( KeyEvent.VK_C );
menuEditPaste.setText( _( "Paste" ) );
menuEditPaste.setMnemonic( KeyEvent.VK_P );
menuEditDelete.setText( _( "Delete" ) );
menuEditDelete.setMnemonic( KeyEvent.VK_D );
menuEditAutoNormalizeMode.setText( _( "Auto normalize mode" ) );
menuEditAutoNormalizeMode.setMnemonic( KeyEvent.VK_N );
menuEditSelectAll.setText( _( "Select All" ) );
menuEditSelectAll.setMnemonic( KeyEvent.VK_A );
menuEditSelectAllEvents.setText( _( "Select all events" ) );
menuEditSelectAllEvents.setMnemonic( KeyEvent.VK_E );

menuVisual.setText( _( "View" ) );
menuVisual.setMnemonic( KeyEvent.VK_V );
menuVisualControlTrack.setText( _( "Control track" ) );
menuVisualControlTrack.setMnemonic( KeyEvent.VK_C );
menuVisualMixer.setText( _( "Mixer" ) );
menuVisualMixer.setMnemonic( KeyEvent.VK_X );
menuVisualWaveform.setText( _( "Waveform" ) );
menuVisualWaveform.setMnemonic( KeyEvent.VK_W );
menuVisualProperty.setText( _( "Property window" ) );
menuVisualOverview.setText( _( "Navigation" ) );
menuVisualOverview.setMnemonic( KeyEvent.VK_V );
menuVisualGridline.setText( _( "Grid line" ) );
menuVisualGridline.setMnemonic( KeyEvent.VK_G );
menuVisualStartMarker.setText( _( "Start marker" ) );
menuVisualStartMarker.setMnemonic( KeyEvent.VK_S );
menuVisualEndMarker.setText( _( "End marker" ) );
menuVisualEndMarker.setMnemonic( KeyEvent.VK_E );
menuVisualLyrics.setText( _( "Lyrics/Phoneme" ) );
menuVisualLyrics.setMnemonic( KeyEvent.VK_L );
menuVisualNoteProperty.setText( _( "Note expression/vibrato" ) );
menuVisualNoteProperty.setMnemonic( KeyEvent.VK_N );
menuVisualPitchLine.setText( _( "Pitch line" ) );
menuVisualPitchLine.setMnemonic( KeyEvent.VK_P );
menuVisualPluginUi.setText( _( "VSTi plugin UI" ) );
menuVisualPluginUi.setMnemonic( KeyEvent.VK_U );
menuVisualIconPalette.setText( _( "Icon palette" ) );
menuVisualIconPalette.setMnemonic( KeyEvent.VK_I );

menuJob.setText( _( "Job" ) );
menuJob.setMnemonic( KeyEvent.VK_J );
menuJobNormalize.setText( _( "Normalize notes" ) );
menuJobNormalize.setMnemonic( KeyEvent.VK_N );
menuJobInsertBar.setText( _( "Insert bars" ) );
menuJobInsertBar.setMnemonic( KeyEvent.VK_I );
menuJobDeleteBar.setText( _( "Delete bars" ) );
menuJobDeleteBar.setMnemonic( KeyEvent.VK_D );
menuJobRandomize.setText( _( "Randomize" ) );
menuJobRandomize.setMnemonic( KeyEvent.VK_R );
menuJobConnect.setText( _( "Connect notes" ) );
menuJobConnect.setMnemonic( KeyEvent.VK_C );
menuJobLyric.setText( _( "Insert lyrics" ) );
menuJobLyric.setMnemonic( KeyEvent.VK_L );

menuTrack.setText( _( "Track" ) );
menuTrack.setMnemonic( KeyEvent.VK_T );
menuTrackOn.setText( _( "Track on" ) );
menuTrackOn.setMnemonic( KeyEvent.VK_K );
menuTrackAdd.setText( _( "Add track" ) );
menuTrackAdd.setMnemonic( KeyEvent.VK_A );
menuTrackCopy.setText( _( "Copy track" ) );
menuTrackCopy.setMnemonic( KeyEvent.VK_C );
menuTrackChangeName.setText( _( "Rename track" ) );
menuTrackDelete.setText( _( "Delete track" ) );
menuTrackDelete.setMnemonic( KeyEvent.VK_D );
menuTrackRenderCurrent.setText( _( "Render current track" ) );
menuTrackRenderCurrent.setMnemonic( KeyEvent.VK_T );
menuTrackRenderAll.setText( _( "Render all tracks" ) );
menuTrackRenderAll.setMnemonic( KeyEvent.VK_S );
menuTrackOverlay.setText( _( "Overlay" ) );
menuTrackOverlay.setMnemonic( KeyEvent.VK_O );
menuTrackRenderer.setText( _( "Renderer" ) );
menuTrackRenderer.setMnemonic( KeyEvent.VK_R );
menuTrackRendererVOCALOID1.setMnemonic( KeyEvent.VK_1 );
menuTrackRendererVOCALOID2.setMnemonic( KeyEvent.VK_3 );
menuTrackRendererUtau.setMnemonic( KeyEvent.VK_4 );
menuTrackRendererVCNT.setMnemonic( KeyEvent.VK_5 );
menuTrackRendererAquesTone.setMnemonic( KeyEvent.VK_6 );

menuLyric.setText( _( "Lyrics" ) );
menuLyric.setMnemonic( KeyEvent.VK_L );
menuLyricExpressionProperty.setText( _( "Note expression property" ) );
menuLyricExpressionProperty.setMnemonic( KeyEvent.VK_E );
menuLyricVibratoProperty.setText( _( "Note vibrato property" ) );
menuLyricVibratoProperty.setMnemonic( KeyEvent.VK_V );
menuLyricApplyUtauParameters.setText( _( "Apply UTAU Parameters" ) );
menuLyricApplyUtauParameters.setMnemonic( KeyEvent.VK_A );
menuLyricPhonemeTransformation.setText( _( "Phoneme transformation" ) );
menuLyricPhonemeTransformation.setMnemonic( KeyEvent.VK_T );
menuLyricDictionary.setText( _( "User word dictionary" ) );
menuLyricDictionary.setMnemonic( KeyEvent.VK_C );
menuLyricCopyVibratoToPreset.setText( _( "Copy vibrato config to preset" ) );
menuLyricCopyVibratoToPreset.setMnemonic( KeyEvent.VK_P );

menuScript.setText( _( "Script" ) );
menuScript.setMnemonic( KeyEvent.VK_C );
menuScriptUpdate.setText( _( "Update script list" ) );
menuScriptUpdate.setMnemonic( KeyEvent.VK_U );

menuSetting.setText( _( "Setting" ) );
menuSetting.setMnemonic( KeyEvent.VK_S );
menuSettingPreference.setText( _( "Preference" ) );
menuSettingPreference.setMnemonic( KeyEvent.VK_P );
menuSettingGameControler.setText( _( "Game controler" ) );
menuSettingGameControler.setMnemonic( KeyEvent.VK_G );
menuSettingGameControlerLoad.setText( _( "Load" ) );
menuSettingGameControlerLoad.setMnemonic( KeyEvent.VK_L );
menuSettingGameControlerRemove.setText( _( "Remove" ) );
menuSettingGameControlerRemove.setMnemonic( KeyEvent.VK_R );
menuSettingGameControlerSetting.setText( _( "Setting" ) );
menuSettingGameControlerSetting.setMnemonic( KeyEvent.VK_S );
menuSettingSequence.setText( _( "Sequence config" ) );
menuSettingSequence.setMnemonic( KeyEvent.VK_S );
menuSettingShortcut.setText( _( "Shortcut key" ) );
menuSettingShortcut.setMnemonic( KeyEvent.VK_K );
menuSettingDefaultSingerStyle.setText( _( "Singing style defaults" ) );
menuSettingDefaultSingerStyle.setMnemonic( KeyEvent.VK_D );
menuSettingPositionQuantize.setText( _( "Quantize" ) );
menuSettingPositionQuantize.setMnemonic( KeyEvent.VK_Q );
menuSettingPositionQuantizeOff.setText( _( "Off" ) );
menuSettingPositionQuantizeTriplet.setText( _( "Triplet" ) );
//menuSettingSingerProperty.setText( _( "Singer Properties" ) );
//menuSettingSingerProperty.setMnemonic( KeyEvent.VK_S );
menuSettingPaletteTool.setText( _( "Palette Tool" ) );
menuSettingPaletteTool.setMnemonic( KeyEvent.VK_T );
menuSettingVibratoPreset.setText( _( "Vibrato preset" ) );
menuSettingVibratoPreset.setMnemonic( KeyEvent.VK_V );

menuWindow.setText( _( "Window" ) );
menuWindowMinimize.setText( _( "Minimize" ) );

menuHelp.setText( _( "Help" ) );
menuHelp.setMnemonic( KeyEvent.VK_H );
menuHelpLog.setText( _( "Log" ) );
menuHelpLog.setMnemonic( KeyEvent.VK_L );
menuHelpLogSwitch.setText( Logger.isEnabled() ? _( "Disable" ) : _( "Enable" ) );
menuHelpLogSwitch.setMnemonic( KeyEvent.VK_L );
menuHelpLogOpen.setText( _( "Open" ) );
menuHelpLogOpen.setMnemonic( KeyEvent.VK_O );
menuHelpAbout.setText( _( "About Cadencii" ) );
menuHelpAbout.setMnemonic( KeyEvent.VK_A );
menuHelpManual.setText( _( "Manual" ) + " (PDF)" );

menuHiddenCopy.setText( _( "Copy" ) );
menuHiddenCut.setText( _( "Cut" ) );
menuHiddenEditFlipToolPointerEraser.setText( _( "Chagne tool pointer / eraser" ) );
menuHiddenEditFlipToolPointerPencil.setText( _( "Change tool pointer / pencil" ) );
menuHiddenEditLyric.setText( _( "Start lyric input" ) );
menuHiddenGoToEndMarker.setText( _( "GoTo end marker" ) );
menuHiddenGoToStartMarker.setText( _( "Goto start marker" ) );
menuHiddenLengthen.setText( _( "Lengthen" ) );
menuHiddenMoveDown.setText( _( "Move down" ) );
menuHiddenMoveLeft.setText( _( "Move left" ) );
menuHiddenMoveRight.setText( _( "Move right" ) );
menuHiddenMoveUp.setText( _( "Move up" ) );
menuHiddenPaste.setText( _( "Paste" ) );
menuHiddenPlayFromStartMarker.setText( _( "Play from start marker" ) );
menuHiddenSelectBackward.setText( _( "Select backward" ) );
menuHiddenSelectForward.setText( _( "Select forward" ) );
menuHiddenShorten.setText( _( "Shorten" ) );
menuHiddenTrackBack.setText( _( "Previous track" ) );
menuHiddenTrackNext.setText( _( "Next track" ) );
menuHiddenVisualBackwardParameter.setText( _( "Previous control curve" ) );
menuHiddenVisualForwardParameter.setText( _( "Next control curve" ) );
menuHiddenFlipCurveOnPianorollMode.setText( _( "Change pitch drawing mode" ) );

cMenuPianoPointer.setText( _( "Arrow" ) );
cMenuPianoPointer.setMnemonic( KeyEvent.VK_A );
cMenuPianoPencil.setText( _( "Pencil" ) );
cMenuPianoPencil.setMnemonic( KeyEvent.VK_W );
cMenuPianoEraser.setText( _( "Eraser" ) );
cMenuPianoEraser.setMnemonic( KeyEvent.VK_E );
cMenuPianoPaletteTool.setText( _( "Palette Tool" ) );

cMenuPianoCurve.setText( _( "Curve" ) );
cMenuPianoCurve.setMnemonic( KeyEvent.VK_V );

cMenuPianoFixed.setText( _( "Note Fixed Length" ) );
cMenuPianoFixed.setMnemonic( KeyEvent.VK_N );
cMenuPianoFixedTriplet.setText( _( "Triplet" ) );
cMenuPianoFixedOff.setText( _( "Off" ) );
cMenuPianoFixedDotted.setText( _( "Dot" ) );
cMenuPianoQuantize.setText( _( "Quantize" ) );
cMenuPianoQuantize.setMnemonic( KeyEvent.VK_Q );
cMenuPianoQuantizeTriplet.setText( _( "Triplet" ) );
cMenuPianoQuantizeOff.setText( _( "Off" ) );
cMenuPianoGrid.setText( _( "Show/Hide Grid Line" ) );
cMenuPianoGrid.setMnemonic( KeyEvent.VK_S );

cMenuPianoUndo.setText( _( "Undo" ) );
cMenuPianoUndo.setMnemonic( KeyEvent.VK_U );
cMenuPianoRedo.setText( _( "Redo" ) );
cMenuPianoRedo.setMnemonic( KeyEvent.VK_R );

cMenuPianoCut.setText( _( "Cut" ) );
cMenuPianoCut.setMnemonic( KeyEvent.VK_T );
cMenuPianoPaste.setText( _( "Paste" ) );
cMenuPianoPaste.setMnemonic( KeyEvent.VK_P );
cMenuPianoCopy.setText( _( "Copy" ) );
cMenuPianoCopy.setMnemonic( KeyEvent.VK_C );
cMenuPianoDelete.setText( _( "Delete" ) );
cMenuPianoDelete.setMnemonic( KeyEvent.VK_D );

cMenuPianoSelectAll.setText( _( "Select All" ) );
cMenuPianoSelectAll.setMnemonic( KeyEvent.VK_A );
cMenuPianoSelectAllEvents.setText( _( "Select All Events" ) );
cMenuPianoSelectAllEvents.setMnemonic( KeyEvent.VK_E );

cMenuPianoExpressionProperty.setText( _( "Note Expression Property" ) );
cMenuPianoExpressionProperty.setMnemonic( KeyEvent.VK_P );
cMenuPianoVibratoProperty.setText( _( "Note Vibrato Property" ) );
cMenuPianoImportLyric.setText( _( "Insert Lyrics" ) );
cMenuPianoImportLyric.setMnemonic( KeyEvent.VK_P );

cMenuTrackTabTrackOn.setText( _( "Track On" ) );
cMenuTrackTabTrackOn.setMnemonic( KeyEvent.VK_K );
cMenuTrackTabAdd.setText( _( "Add Track" ) );
cMenuTrackTabAdd.setMnemonic( KeyEvent.VK_A );
cMenuTrackTabCopy.setText( _( "Copy Track" ) );
cMenuTrackTabCopy.setMnemonic( KeyEvent.VK_C );
cMenuTrackTabChangeName.setText( _( "Rename Track" ) );
cMenuTrackTabDelete.setText( _( "Delete Track" ) );
cMenuTrackTabDelete.setMnemonic( KeyEvent.VK_D );

cMenuTrackTabRenderCurrent.setText( _( "Render Current Track" ) );
cMenuTrackTabRenderCurrent.setMnemonic( KeyEvent.VK_T );
cMenuTrackTabRenderAll.setText( _( "Render All Tracks" ) );
cMenuTrackTabRenderAll.setMnemonic( KeyEvent.VK_S );
cMenuTrackTabOverlay.setText( _( "Overlay" ) );
cMenuTrackTabOverlay.setMnemonic( KeyEvent.VK_O );
cMenuTrackTabRenderer.setText( _( "Renderer" ) );
cMenuTrackTabRenderer.setMnemonic( KeyEvent.VK_R );

cMenuTrackSelectorPointer.setText( _( "Arrow" ) );
cMenuTrackSelectorPointer.setMnemonic( KeyEvent.VK_A );
cMenuTrackSelectorPencil.setText( _( "Pencil" ) );
cMenuTrackSelectorPencil.setMnemonic( KeyEvent.VK_W );
cMenuTrackSelectorLine.setText( _( "Line" ) );
cMenuTrackSelectorLine.setMnemonic( KeyEvent.VK_L );
cMenuTrackSelectorEraser.setText( _( "Eraser" ) );
cMenuTrackSelectorEraser.setMnemonic( KeyEvent.VK_E );
cMenuTrackSelectorPaletteTool.setText( _( "Palette Tool" ) );

cMenuTrackSelectorCurve.setText( _( "Curve" ) );
cMenuTrackSelectorCurve.setMnemonic( KeyEvent.VK_V );

cMenuTrackSelectorUndo.setText( _( "Undo" ) );
cMenuTrackSelectorUndo.setMnemonic( KeyEvent.VK_U );
cMenuTrackSelectorRedo.setText( _( "Redo" ) );
cMenuTrackSelectorRedo.setMnemonic( KeyEvent.VK_R );

cMenuTrackSelectorCut.setText( _( "Cut" ) );
cMenuTrackSelectorCut.setMnemonic( KeyEvent.VK_T );
cMenuTrackSelectorCopy.setText( _( "Copy" ) );
cMenuTrackSelectorCopy.setMnemonic( KeyEvent.VK_C );
cMenuTrackSelectorPaste.setText( _( "Paste" ) );
cMenuTrackSelectorPaste.setMnemonic( KeyEvent.VK_P );
cMenuTrackSelectorDelete.setText( _( "Delete" ) );
cMenuTrackSelectorDelete.setMnemonic( KeyEvent.VK_D );
cMenuTrackSelectorDeleteBezier.setText( _( "Delete Bezier Point" ) );
cMenuTrackSelectorDeleteBezier.setMnemonic( KeyEvent.VK_B );

cMenuTrackSelectorSelectAll.setText( _( "Select All Events" ) );
cMenuTrackSelectorSelectAll.setMnemonic( KeyEvent.VK_E );

cMenuPositionIndicatorStartMarker.setText( _( "Set start marker" ) );
cMenuPositionIndicatorEndMarker.setText( _( "Set end marker" ) );


// Palette Tool
        }

        /// <summary>
        /// 歌詞の流し込みダイアログを開き，選択された音符を起点に歌詞を流し込みます
        /// </summary>
        public void importLyric()
        {
int start = 0;
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );
int selectedid = AppManager.itemSelection.getLastEvent().original.InternalID;
int numEvents = vsq_track.getEventCount();
for ( int i = 0; i < numEvents; i++ ) {
    if ( selectedid == vsq_track.getEvent( i ).InternalID ) {
        start = i;
        break;
    }
}
int count = vsq_track.getEventCount() - 1 - start + 1;
try {
    if ( mDialogImportLyric == null ) {
        mDialogImportLyric = new FormImportLyric( count );
    } else {
        mDialogImportLyric.setMaxNotes( count );
    }
    mDialogImportLyric.setLocation( getFormPreferedLocation( mDialogImportLyric ) );
    BDialogResult dr = AppManager.showModalDialog( mDialogImportLyric, this );
    if ( dr == BDialogResult.OK ) {
        String[] phrases = mDialogImportLyric.getLetters();
        int min = Math.min( count, phrases.length );
        Vector<String> new_phrases = new Vector<String>();
        Vector<String> new_symbols = new Vector<String>();
        for ( int i = 0; i < phrases.length; i++ ) {
            SymbolTableEntry entry = SymbolTable.attatch( phrases[i] );
            if ( new_phrases.size() + 1 > count ) {
                break;
            }
            if ( entry == null ) {
                new_phrases.add( phrases[i] );
                new_symbols.add( "a" );
            } else {
                if ( entry.Word.indexOf( '-' ) >= 0 ) {
                    // 分節に分割する必要がある
                    String[] spl = PortUtil.splitString( entry.Word, '\t' );
                    if ( new_phrases.size() + spl.length > count ) {
                        // 分節の全部を分割すると制限個数を超えてしまう
                        // 分割せずにハイフンを付けたまま登録
                        new_phrases.add( entry.Word.replace( "\t", "" ) );
                        new_symbols.add( entry.getSymbol() );
                    } else {
                        String[] spl_symbol = PortUtil.splitString( entry.getRawSymbol(), '\t' );
                        for ( int j = 0; j < spl.length; j++ ) {
                            new_phrases.add( spl[j] );
                            new_symbols.add( spl_symbol[j] );
                        }
                    }
                } else {
                    // 分節に分割しない
                    new_phrases.add( phrases[i] );
                    new_symbols.add( entry.getSymbol() );
                }
            }
        }
        VsqEvent[] new_events = new VsqEvent[new_phrases.size()];
        int indx = -1;
        for ( Iterator<Integer> itr = vsq_track.indexIterator( IndexIteratorKind.NOTE ); itr.hasNext(); ) {
            int index = itr.next();
            if ( index < start ) {
                continue;
            }
            indx++;
            VsqEvent item = vsq_track.getEvent( index );
            new_events[indx] = (VsqEvent)item.clone();
            new_events[indx].ID.LyricHandle.L0.Phrase = new_phrases.get( indx );
            new_events[indx].ID.LyricHandle.L0.setPhoneticSymbol( new_symbols.get( indx ) );
            AppManager.applyUtauParameter( vsq_track, new_events[indx] );
            if ( indx + 1 >= new_phrases.size() ) {
                break;
            }
        }
        CadenciiCommand run = new CadenciiCommand(
            VsqCommand.generateCommandEventReplaceRange( selected, new_events ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
        repaint();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".importLyric; ex=" + ex + "\n" );
} finally {
    mDialogImportLyric.setVisible( false );
}
        }

        /// <summary>
        /// 選択されている音符のビブラートを編集するためのダイアログを起動し、編集を行います。
        /// </summary>
        public void editNoteVibratoProperty()
        {
SelectedEventEntry item = AppManager.itemSelection.getLastEvent();
if ( item == null ) {
    return;
}

VsqEvent ev = item.original;
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
SynthesizerType type = SynthesizerType.VOCALOID2;
if ( kind == RendererKind.VOCALOID1 ) {
    type = SynthesizerType.VOCALOID1;
}
FormVibratoConfig dlg = null;
try {
    dlg = new FormVibratoConfig(
        ev.ID.VibratoHandle,
        ev.ID.getLength(),
        AppManager.editorConfig.DefaultVibratoLength,
        type,
        AppManager.editorConfig.UseUserDefinedAutoVibratoType );
    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dlg.getDialogResult() == BDialogResult.OK ) {
        VsqEvent edited = (VsqEvent)ev.clone();
        if ( dlg.getVibratoHandle() != null ) {
            edited.ID.VibratoHandle = (VibratoHandle)dlg.getVibratoHandle().clone();
            //edited.ID.VibratoHandle.setStartDepth( AppManager.editorConfig.DefaultVibratoDepth );
            //edited.ID.VibratoHandle.setStartRate( AppManager.editorConfig.DefaultVibratoRate );
            edited.ID.VibratoDelay = ev.ID.getLength() - dlg.getVibratoHandle().getLength();
        } else {
            edited.ID.VibratoHandle = null;
        }
        CadenciiCommand run = new CadenciiCommand(
            VsqCommand.generateCommandEventChangeIDContaints( selected, ev.InternalID, edited.ID ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
        refreshScreen();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".editNoteVibratoProperty; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".editNoteVibratoProperty; ex=" + ex2 + "\n" );
        }
    }
}
        }

        /// <summary>
        /// 選択されている音符の表情を編集するためのダイアログを起動し、編集を行います。
        /// </summary>
        public void editNoteExpressionProperty()
        {
SelectedEventEntry item = AppManager.itemSelection.getLastEvent();
if ( item == null ) {
    return;
}

VsqEvent ev = item.original;
SynthesizerType type = SynthesizerType.VOCALOID2;
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
if ( kind == RendererKind.VOCALOID1 ) {
    type = SynthesizerType.VOCALOID1;
}
FormNoteExpressionConfig dlg = null;
try {
    dlg = new FormNoteExpressionConfig( type, ev.ID.NoteHeadHandle );
    dlg.setPMBendDepth( ev.ID.PMBendDepth );
    dlg.setPMBendLength( ev.ID.PMBendLength );
    dlg.setPMbPortamentoUse( ev.ID.PMbPortamentoUse );
    dlg.setDEMdecGainRate( ev.ID.DEMdecGainRate );
    dlg.setDEMaccent( ev.ID.DEMaccent );

    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        VsqEvent edited = (VsqEvent)ev.clone();
        edited.ID.PMBendDepth = dlg.getPMBendDepth();
        edited.ID.PMBendLength = dlg.getPMBendLength();
        edited.ID.PMbPortamentoUse = dlg.getPMbPortamentoUse();
        edited.ID.DEMdecGainRate = dlg.getDEMdecGainRate();
        edited.ID.DEMaccent = dlg.getDEMaccent();
        edited.ID.NoteHeadHandle = dlg.getEditedNoteHeadHandle();
        CadenciiCommand run = new CadenciiCommand(
            VsqCommand.generateCommandEventChangeIDContaints( selected, ev.InternalID, edited.ID ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
        refreshScreen();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".editNoteExpressionProperty; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".editNoteExpressionProperty; ex=" + ex2 + "\n" );
        }
    }
}
        }

        /// <summary>
        /// マウスのスクロールによって受け取ったスクロール幅から、実際に縦スクロールバーに渡す値(候補値)を計算します。
        /// </summary>
        /// <param name="delta"></param>
        /// <returns></returns>
        public int computeScrollValueFromWheelDelta( int delta )
        {
double new_val = (double)hScroll.getValue() - delta * AppManager.editorConfig.WheelOrder / (5.0 * controller.getScaleX());
if ( new_val < 0.0 ) {
    new_val = 0;
}
int max = hScroll.getMaximum() - hScroll.getVisibleAmount();
int draft = (int)new_val;
if ( draft > max ) {
    draft = max;
} else if ( draft < hScroll.getMinimum() ) {
    draft = hScroll.getMinimum();
}
return draft;
        }

        public void selectAll()
        {

AppManager.itemSelection.clearEvent();
AppManager.itemSelection.clearTempo();
AppManager.itemSelection.clearTimesig();
AppManager.itemSelection.clearPoint();
int min = Integer.MAX_VALUE;
int max = Integer.MIN_VALUE;
int premeasure = AppManager.getVsqFile().getPreMeasureClocks();
Vector<Integer> add_required = new Vector<Integer>();
for ( Iterator<VsqEvent> itr = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getEventIterator(); itr.hasNext(); ) {
    VsqEvent ve = itr.next();
    if ( premeasure <= ve.Clock ) {
        add_required.add( ve.InternalID );
        min = Math.min( min, ve.Clock );
        max = Math.max( max, ve.Clock + ve.ID.getLength() );
    }
}
if ( add_required.size() > 0 ) {
    AppManager.itemSelection.addEventAll( add_required );
}
for ( CurveType vct : Utility.CURVE_USAGE ) {
    if ( vct.isScalar() || vct.isAttachNote() ) {
        continue;
    }
    VsqBPList target = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( vct.getName() );
    if ( target == null ) {
        continue;
    }
    int count = target.size();
    if ( count >= 1 ) {
        //int[] keys = target.getKeys();
        int max_key = target.getKeyClock( count - 1 );
        max = Math.max( max, target.getValue( max_key ) );
        for ( int i = 0; i < count; i++ ) {
            int key = target.getKeyClock( i );
            if ( premeasure <= key ) {
                min = Math.min( min, key );
                break;
            }
        }
    }
}
if ( min < premeasure ) {
    min = premeasure;
}
if ( min < max ) {
    //int stdx = AppManager.startToDrawX;
    //min = xCoordFromClocks( min ) + stdx;
    //max = xCoordFromClocks( max ) + stdx;
    AppManager.mWholeSelectedInterval = new SelectedRegion( min );
    AppManager.mWholeSelectedInterval.setEnd( max );
    AppManager.setWholeSelectedIntervalEnabled( true );
}
        }

        public void selectAllEvent()
        {
AppManager.itemSelection.clearTempo();
AppManager.itemSelection.clearTimesig();
AppManager.itemSelection.clearEvent();
AppManager.itemSelection.clearPoint();
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );
int premeasureclock = vsq.getPreMeasureClocks();
Vector<Integer> add_required = new Vector<Integer>();
for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
    VsqEvent ev = itr.next();
    if ( ev.ID.type == VsqIDType.Anote && ev.Clock >= premeasureclock ) {
        add_required.add( ev.InternalID );
    }
}
if ( add_required.size() > 0 ) {
    AppManager.itemSelection.addEventAll( add_required );
}
refreshScreen();
        }

        public void deleteEvent()
        {

if ( AppManager.mInputTextBox.isVisible() ) {
    return;
}
if ( AppManager.propertyPanel.isEditing() ) {
    return;
}

int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );

if ( AppManager.itemSelection.getEventCount() > 0 ) {
    Vector<Integer> ids = new Vector<Integer>();
    boolean contains_aicon = false;
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry ev = itr.next();
        ids.add( ev.original.InternalID );
        if ( ev.original.ID.type == VsqIDType.Aicon ) {
            contains_aicon = true;
        }
    }
    VsqCommand run = VsqCommand.generateCommandEventDeleteRange( selected, ids );
    if ( AppManager.isWholeSelectedIntervalEnabled() ) {
        VsqFileEx work = (VsqFileEx)vsq.clone();
        work.executeCommand( run );
        int stdx = controller.getStartToDrawX();
        int start_clock = AppManager.mWholeSelectedInterval.getStart();
        int end_clock = AppManager.mWholeSelectedInterval.getEnd();
        Vector<Vector<BPPair>> curves = new Vector<Vector<BPPair>>();
        Vector<CurveType> types = new Vector<CurveType>();
        VsqTrack work_vsq_track = work.Track.get( selected );
        for ( CurveType vct : Utility.CURVE_USAGE ) {
            if ( vct.isScalar() || vct.isAttachNote() ) {
                continue;
            }
            VsqBPList work_curve = work_vsq_track.getCurve( vct.getName() );
            Vector<BPPair> t = new Vector<BPPair>();
            t.add( new BPPair( start_clock, work_curve.getValue( start_clock ) ) );
            t.add( new BPPair( end_clock, work_curve.getValue( end_clock ) ) );
            curves.add( t );
            types.add( vct );
        }
        Vector<String> strs = new Vector<String>();
        for ( int i = 0; i < types.size(); i++ ) {
            strs.add( types.get( i ).getName() );
        }
        CadenciiCommand delete_curve = new CadenciiCommand(
            VsqCommand.generateCommandTrackCurveEditRange( selected, strs, curves ) );
        work.executeCommand( delete_curve );
        if ( contains_aicon ) {
            work.Track.get( selected ).reflectDynamics();
        }
        CadenciiCommand run2 = new CadenciiCommand( VsqCommand.generateCommandReplace( work ) );
        AppManager.editHistory.register( vsq.executeCommand( run2 ) );
        setEdited( true );
    } else {
        CadenciiCommand run2 = null;
        if ( contains_aicon ) {
            VsqFileEx work = (VsqFileEx)vsq.clone();
            work.executeCommand( run );
            VsqTrack vsq_track_copied = work.Track.get( selected );
            vsq_track_copied.reflectDynamics();
            run2 = VsqFileEx.generateCommandTrackReplace( selected,
                                                          vsq_track_copied,
                                                          work.AttachedCurves.get( selected - 1 ) );
        } else {
            run2 = new CadenciiCommand( run );
        }
        AppManager.editHistory.register( vsq.executeCommand( run2 ) );
        setEdited( true );
        AppManager.itemSelection.clearEvent();
    }
    repaint();
} else if ( AppManager.itemSelection.getTempoCount() > 0 ) {
    Vector<Integer> clocks = new Vector<Integer>();
    for ( Iterator<ValuePair<Integer, SelectedTempoEntry>> itr = AppManager.itemSelection.getTempoIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTempoEntry> item = itr.next();
        if ( item.getKey() <= 0 ) {
            String msg = _( "Cannot remove first symbol of track!" );
            statusLabel.setText( msg );
            return;
        }
        clocks.add( item.getKey() );
    }
    int[] dum = new int[clocks.size()];
    for ( int i = 0; i < dum.length; i++ ) {
        dum[i] = -1;
    }
    CadenciiCommand run = new CadenciiCommand(
        VsqCommand.generateCommandUpdateTempoRange( PortUtil.convertIntArray( clocks.toArray( new Integer[] { } ) ),
                                                    PortUtil.convertIntArray( clocks.toArray( new Integer[] { } ) ),
                                                    dum ) );
    AppManager.editHistory.register( vsq.executeCommand( run ) );
    setEdited( true );
    AppManager.itemSelection.clearTempo();
    repaint();
} else if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
    int[] barcounts = new int[AppManager.itemSelection.getTimesigCount()];
    int[] numerators = new int[AppManager.itemSelection.getTimesigCount()];
    int[] denominators = new int[AppManager.itemSelection.getTimesigCount()];
    int count = -1;
    for ( Iterator<ValuePair<Integer, SelectedTimesigEntry>> itr = AppManager.itemSelection.getTimesigIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTimesigEntry> item = itr.next();
        int key = item.getKey();
        SelectedTimesigEntry value = item.getValue();
        count++;
        barcounts[count] = key;
        if ( key <= 0 ) {
            String msg = "Cannot remove first symbol of track!";
            statusLabel.setText( _( msg ) );
            return;
        }
        numerators[count] = -1;
        denominators[count] = -1;
    }
    CadenciiCommand run = new CadenciiCommand(
        VsqCommand.generateCommandUpdateTimesigRange( barcounts, barcounts, numerators, denominators ) );
    AppManager.editHistory.register( vsq.executeCommand( run ) );
    setEdited( true );
    AppManager.itemSelection.clearTimesig();
    repaint();
}
if ( AppManager.itemSelection.getPointIDCount() > 0 ) {
    String curve;
    if ( !trackSelector.getSelectedCurve().isAttachNote() ) {
        curve = trackSelector.getSelectedCurve().getName();
        VsqBPList src = vsq_track.getCurve( curve );
        VsqBPList list = (VsqBPList)src.clone();
        Vector<Integer> remove_clock_queue = new Vector<Integer>();
        int count = list.size();
        for ( int i = 0; i < count; i++ ) {
            VsqBPPair item = list.getElementB( i );
            if ( AppManager.itemSelection.isPointContains( item.id ) ) {
                remove_clock_queue.add( list.getKeyClock( i ) );
            }
        }
        count = remove_clock_queue.size();
        for ( int i = 0; i < count; i++ ) {
            list.remove( remove_clock_queue.get( i ) );
        }
        CadenciiCommand run = new CadenciiCommand(
            VsqCommand.generateCommandTrackCurveReplace( selected,
                                                         trackSelector.getSelectedCurve().getName(),
                                                         list ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
    } else {
        //todo: FormMain+DeleteEvent; VibratoDepth, VibratoRateの場合
    }
    AppManager.itemSelection.clearPoint();
    refreshScreen();
}
        }

        public void pasteEvent()
        {
int clock = AppManager.getCurrentClock();
int unit = AppManager.getPositionQuantizeClock();
clock = doQuantize( clock, unit );

VsqCommand add_event = null; // VsqEventを追加するコマンド

ClipboardEntry ce = AppManager.clipboard.getCopiedItems();
int copy_started_clock = ce.copyStartedClock;
Vector<VsqEvent> copied_events = ce.events;
if ( copied_events.size() != 0 ) {
    // VsqEventのペーストを行うコマンドを発行
    int dclock = clock - copy_started_clock;
    if ( clock >= AppManager.getVsqFile().getPreMeasureClocks() ) {
        Vector<VsqEvent> paste = new Vector<VsqEvent>();
        int count = copied_events.size();
        for ( int i = 0; i < count; i++ ) {
            VsqEvent item = (VsqEvent)copied_events.get( i ).clone();
            item.Clock = copied_events.get( i ).Clock + dclock;
            paste.add( item );
        }
        add_event = VsqCommand.generateCommandEventAddRange(
            AppManager.getSelected(), paste.toArray( new VsqEvent[] { } ) );
    }
}
Vector<TempoTableEntry> copied_tempo = ce.tempo;
if ( copied_tempo.size() != 0 ) {
    // テンポ変更の貼付けを実行
    int dclock = clock - copy_started_clock;
    int count = copied_tempo.size();
    int[] clocks = new int[count];
    int[] tempos = new int[count];
    for ( int i = 0; i < count; i++ ) {
        TempoTableEntry item = copied_tempo.get( i );
        clocks[i] = item.Clock + dclock;
        tempos[i] = item.Tempo;
    }
    CadenciiCommand run = new CadenciiCommand(
        VsqCommand.generateCommandUpdateTempoRange( clocks, clocks, tempos ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
    refreshScreen();
    return;
}
Vector<TimeSigTableEntry> copied_timesig = ce.timesig;
if ( copied_timesig.size() > 0 ) {
    // 拍子変更の貼付けを実行
    int bar_count = AppManager.getVsqFile().getBarCountFromClock( clock );
    int min_barcount = copied_timesig.get( 0 ).BarCount;
    for ( Iterator<TimeSigTableEntry> itr = copied_timesig.iterator(); itr.hasNext(); ) {
        TimeSigTableEntry tste = itr.next();
        min_barcount = Math.min( min_barcount, tste.BarCount );
    }
    int dbarcount = bar_count - min_barcount;
    int count = copied_timesig.size();
    int[] barcounts = new int[count];
    int[] numerators = new int[count];
    int[] denominators = new int[count];
    for ( int i = 0; i < count; i++ ) {
        TimeSigTableEntry item = copied_timesig.get( i );
        barcounts[i] = item.BarCount + dbarcount;
        numerators[i] = item.Numerator;
        denominators[i] = item.Denominator;
    }
    CadenciiCommand run = new CadenciiCommand(
        VsqCommand.generateCommandUpdateTimesigRange(
            barcounts, barcounts, numerators, denominators ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
    refreshScreen();
    return;
}

// BPPairの貼付け
VsqCommand edit_bpcurve = null; // BPListを変更するコマンド
TreeMap<CurveType, VsqBPList> copied_curve = ce.points;
if ( copied_curve.size() > 0 ) {
    int dclock = clock - copy_started_clock;

    TreeMap<String, VsqBPList> work = new TreeMap<String, VsqBPList>();
    for ( Iterator<CurveType> itr = copied_curve.keySet().iterator(); itr.hasNext(); ) {
        CurveType curve = itr.next();
        VsqBPList list = copied_curve.get( curve );
        if ( curve.isScalar() ) {
            continue;
        }
        if ( list.size() <= 0 ) {
            continue;
        }
        if ( curve.isAttachNote() ) {
            //todo: FormMain+PasteEvent; VibratoRate, VibratoDepthカーブのペースト処理
        } else {
            VsqBPList target = (VsqBPList)AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( curve.getName() ).clone();
            int count = list.size();
            int min = list.getKeyClock( 0 ) + dclock;
            int max = list.getKeyClock( count - 1 ) + dclock;
            int valueAtEnd = target.getValue( max );
            for ( int i = 0; i < target.size(); i++ ) {
                int cl = target.getKeyClock( i );
                if ( min <= cl && cl <= max ) {
                    target.removeElementAt( i );
                    i--;
                }
            }
            int lastClock = min;
            for ( int i = 0; i < count - 1; i++ ) {
                lastClock = list.getKeyClock( i ) + dclock;
                target.add( lastClock, list.getElementA( i ) );
            }
            // 最後のやつ
            if ( lastClock < max - 1 ) {
                target.add( max - 1, list.getElementA( count - 1 ) );
            }
            target.add( max, valueAtEnd );
            if ( copied_curve.size() == 1 ) {
                work.put( trackSelector.getSelectedCurve().getName(), target );
            } else {
                work.put( curve.getName(), target );
            }
        }
    }
    if ( work.size() > 0 ) {
        String[] curves = new String[work.size()];
        VsqBPList[] bplists = new VsqBPList[work.size()];
        int count = -1;
        for ( Iterator<String> itr = work.keySet().iterator(); itr.hasNext(); ) {
            String s = itr.next();
            count++;
            curves[count] = s;
            bplists[count] = work.get( s );
        }
        edit_bpcurve = VsqCommand.generateCommandTrackCurveReplaceRange( AppManager.getSelected(), curves, bplists );
    }
    AppManager.itemSelection.clearPoint();
}

// ベジエ曲線の貼付け
CadenciiCommand edit_bezier = null;
TreeMap<CurveType, Vector<BezierChain>> copied_bezier = ce.beziers;
if ( copied_bezier.size() > 0 ) {
    int dclock = clock - copy_started_clock;
    BezierCurves attached_curve = (BezierCurves)AppManager.getVsqFile().AttachedCurves.get( AppManager.getSelected() - 1 ).clone();
    TreeMap<CurveType, Vector<BezierChain>> command_arg = new TreeMap<CurveType, Vector<BezierChain>>();
    for ( Iterator<CurveType> itr = copied_bezier.keySet().iterator(); itr.hasNext(); ) {
        CurveType curve = itr.next();
        if ( curve.isScalar() ) {
            continue;
        }
        for ( Iterator<BezierChain> itr2 = copied_bezier.get( curve ).iterator(); itr2.hasNext(); ) {
            BezierChain bc = itr2.next();
            BezierChain bc_copy = (BezierChain)bc.clone();
            for ( Iterator<BezierPoint> itr3 = bc_copy.points.iterator(); itr3.hasNext(); ) {
                BezierPoint bp = itr3.next();
                bp.setBase( new PointD( bp.getBase().getX() + dclock, bp.getBase().getY() ) );
            }
            attached_curve.mergeBezierChain( curve, bc_copy );
        }
        Vector<BezierChain> arg = new Vector<BezierChain>();
        for ( Iterator<BezierChain> itr2 = attached_curve.get( curve ).iterator(); itr2.hasNext(); ) {
            BezierChain bc = itr2.next();
            arg.add( bc );
        }
        command_arg.put( curve, arg );
    }
    edit_bezier = VsqFileEx.generateCommandReplaceAttachedCurveRange( AppManager.getSelected(), command_arg );
}

int commands = 0;
commands += (add_event != null) ? 1 : 0;
commands += (edit_bpcurve != null) ? 1 : 0;
commands += (edit_bezier != null) ? 1 : 0;

if ( commands == 1 ) {
    if ( add_event != null ) {
        CadenciiCommand run = new CadenciiCommand( add_event );
        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    } else if ( edit_bpcurve != null ) {
        CadenciiCommand run = new CadenciiCommand( edit_bpcurve );
        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    } else if ( edit_bezier != null ) {
        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( edit_bezier ) );
    }
    AppManager.getVsqFile().updateTotalClocks();
    setEdited( true );
    refreshScreen();
} else if ( commands > 1 ) {
    VsqFileEx work = (VsqFileEx)AppManager.getVsqFile().clone();
    if ( add_event != null ) {
        work.executeCommand( add_event );
    }
    if ( edit_bezier != null ) {
        work.executeCommand( edit_bezier );
    }
    if ( edit_bpcurve != null ) {
        // edit_bpcurveのVsqCommandTypeはTrackEditCurveRangeしかありえない
        work.executeCommand( edit_bpcurve );
    }
    CadenciiCommand run = VsqFileEx.generateCommandReplace( work );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    AppManager.getVsqFile().updateTotalClocks();
    setEdited( true );
    refreshScreen();
}
        }

        /// <summary>
        /// アイテムのコピーを行います
        /// </summary>
        public void copyEvent()
        {
int min = Integer.MAX_VALUE; // コピーされたアイテムの中で、最小の開始クロック

if ( AppManager.isWholeSelectedIntervalEnabled() ) {
    int stdx = controller.getStartToDrawX();
    int start_clock = AppManager.mWholeSelectedInterval.getStart();
    int end_clock = AppManager.mWholeSelectedInterval.getEnd();
    ClipboardEntry ce = new ClipboardEntry();
    ce.copyStartedClock = start_clock;
    ce.points = new TreeMap<CurveType, VsqBPList>();
    ce.beziers = new TreeMap<CurveType, Vector<BezierChain>>();
    for ( int i = 0; i < Utility.CURVE_USAGE.length; i++ ) {
        CurveType vct = Utility.CURVE_USAGE[i];
        VsqBPList list = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( vct.getName() );
        if ( list == null ) {
            continue;
        }
        Vector<BezierChain> tmp_bezier = new Vector<BezierChain>();
        copyCurveCor( AppManager.getSelected(),
                      vct,
                      start_clock,
                      end_clock,
                      tmp_bezier );
        VsqBPList tmp_bplist = new VsqBPList( list.getName(), list.getDefault(), list.getMinimum(), list.getMaximum() );
        int c = list.size();
        for ( int j = 0; j < c; j++ ) {
            int clock = list.getKeyClock( j );
            if ( start_clock <= clock && clock <= end_clock ) {
                tmp_bplist.add( clock, list.getElement( j ) );
            } else if ( end_clock < clock ) {
                break;
            }
        }
        ce.beziers.put( vct, tmp_bezier );
        ce.points.put( vct, tmp_bplist );
    }

    if ( AppManager.itemSelection.getEventCount() > 0 ) {
        Vector<VsqEvent> list = new Vector<VsqEvent>();
        for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
            SelectedEventEntry item = itr.next();
            if ( item.original.ID.type == VsqIDType.Anote ) {
                min = Math.min( item.original.Clock, min );
                list.add( (VsqEvent)item.original.clone() );
            }
        }
        ce.events = list;
    }
    AppManager.clipboard.setClipboard( ce );
} else if ( AppManager.itemSelection.getEventCount() > 0 ) {
    Vector<VsqEvent> list = new Vector<VsqEvent>();
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry item = itr.next();
        min = Math.min( item.original.Clock, min );
        list.add( (VsqEvent)item.original.clone() );
    }
    AppManager.clipboard.setCopiedEvent( list, min );
} else if ( AppManager.itemSelection.getTempoCount() > 0 ) {
    Vector<TempoTableEntry> list = new Vector<TempoTableEntry>();
    for ( Iterator<ValuePair<Integer, SelectedTempoEntry>> itr = AppManager.itemSelection.getTempoIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTempoEntry> item = itr.next();
        int key = item.getKey();
        SelectedTempoEntry value = item.getValue();
        min = Math.min( value.original.Clock, min );
        list.add( (TempoTableEntry)value.original.clone() );
    }
    AppManager.clipboard.setCopiedTempo( list, min );
} else if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
    Vector<TimeSigTableEntry> list = new Vector<TimeSigTableEntry>();
    for ( Iterator<ValuePair<Integer, SelectedTimesigEntry>> itr = AppManager.itemSelection.getTimesigIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTimesigEntry> item = itr.next();
        int key = item.getKey();
        SelectedTimesigEntry value = item.getValue();
        min = Math.min( value.original.Clock, min );
        list.add( (TimeSigTableEntry)value.original.clone() );
    }
    AppManager.clipboard.setCopiedTimesig( list, min );
} else if ( AppManager.itemSelection.getPointIDCount() > 0 ) {
    ClipboardEntry ce = new ClipboardEntry();
    ce.points = new TreeMap<CurveType, VsqBPList>();
    ce.beziers = new TreeMap<CurveType, Vector<BezierChain>>();

    ValuePair<Integer, Integer> t = trackSelector.getSelectedRegion();
    int start = t.getKey();
    int end = t.getValue();
    ce.copyStartedClock = start;
    Vector<BezierChain> tmp_bezier = new Vector<BezierChain>();
    copyCurveCor( AppManager.getSelected(),
                  trackSelector.getSelectedCurve(),
                  start,
                  end,
                  tmp_bezier );
    if ( tmp_bezier.size() > 0 ) {
        // ベジエ曲線が1個以上コピーされた場合
        // 範囲内のデータ点を追加する
        ce.beziers.put( trackSelector.getSelectedCurve(), tmp_bezier );
        CurveType curve = trackSelector.getSelectedCurve();
        VsqBPList list = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( curve.getName() );
        if ( list != null ) {
            VsqBPList tmp_bplist = new VsqBPList( list.getName(), list.getDefault(), list.getMinimum(), list.getMaximum() );
            int c = list.size();
            for ( int i = 0; i < c; i++ ) {
                int clock = list.getKeyClock( i );
                if ( start <= clock && clock <= end ) {
                    tmp_bplist.add( clock, list.getElement( i ) );
                } else if ( end < clock ) {
                    break;
                }
            }
            ce.points.put( curve, tmp_bplist );
        }
    } else {
        // ベジエ曲線がコピーされなかった場合
        // AppManager.selectedPointIDIteratorの中身のみを選択
        CurveType curve = trackSelector.getSelectedCurve();
        VsqBPList list = AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getCurve( curve.getName() );
        if ( list != null ) {
            VsqBPList tmp_bplist = new VsqBPList( curve.getName(), curve.getDefault(), curve.getMinimum(), curve.getMaximum() );
            for ( Iterator<Long> itr = AppManager.itemSelection.getPointIDIterator(); itr.hasNext(); ) {
                long id = itr.next();
                VsqBPPairSearchContext cxt = list.findElement( id );
                if ( cxt.index >= 0 ) {
                    tmp_bplist.add( cxt.clock, cxt.point.value );
                }
            }
            if ( tmp_bplist.size() > 0 ) {
                ce.copyStartedClock = tmp_bplist.getKeyClock( 0 );
                ce.points.put( curve, tmp_bplist );
            }
        }
    }
    AppManager.clipboard.setClipboard( ce );
}
        }

        public void cutEvent()
        {
// まずコピー
copyEvent();

int track = AppManager.getSelected();

// 選択されたノートイベントがあれば、まず、削除を行うコマンドを発行
VsqCommand delete_event = null;
boolean other_command_executed = false;
if ( AppManager.itemSelection.getEventCount() > 0 ) {
    Vector<Integer> ids = new Vector<Integer>();
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry item = itr.next();
        ids.add( item.original.InternalID );
    }
    delete_event = VsqCommand.generateCommandEventDeleteRange( AppManager.getSelected(), ids );
}

// Ctrlキーを押しながらドラッグしたか、そうでないかで分岐
if ( AppManager.isWholeSelectedIntervalEnabled() || AppManager.itemSelection.getPointIDCount() > 0 ) {
    int stdx = controller.getStartToDrawX();
    int start_clock, end_clock;
    if ( AppManager.isWholeSelectedIntervalEnabled() ) {
        start_clock = AppManager.mWholeSelectedInterval.getStart();
        end_clock = AppManager.mWholeSelectedInterval.getEnd();
    } else {
        start_clock = trackSelector.getSelectedRegion().getKey();
        end_clock = trackSelector.getSelectedRegion().getValue();
    }

    // クローンを作成
    VsqFileEx work = (VsqFileEx)AppManager.getVsqFile().clone();
    if ( delete_event != null ) {
        // 選択されたノートイベントがあれば、クローンに対して削除を実行
        work.executeCommand( delete_event );
    }

    // BPListに削除処理を施す
    for ( int i = 0; i < Utility.CURVE_USAGE.length; i++ ) {
        CurveType curve = Utility.CURVE_USAGE[i];
        VsqBPList list = work.Track.get( track ).getCurve( curve.getName() );
        if ( list == null ) {
            continue;
        }
        int c = list.size();
        Vector<Long> delete = new Vector<Long>();
        if ( AppManager.isWholeSelectedIntervalEnabled() ) {
            // 一括選択モード
            for ( int j = 0; j < c; j++ ) {
                int clock = list.getKeyClock( j );
                if ( start_clock <= clock && clock <= end_clock ) {
                    delete.add( list.getElementB( j ).id );
                } else if ( end_clock < clock ) {
                    break;
                }
            }
        } else {
            // 普通の範囲選択
            for ( Iterator<Long> itr = AppManager.itemSelection.getPointIDIterator(); itr.hasNext(); ) {
                long id = (Long)itr.next();
                delete.add( id );
            }
        }
        VsqCommand tmp = VsqCommand.generateCommandTrackCurveEdit2( track, curve.getName(), delete, new TreeMap<Integer, VsqBPPair>() );
        work.executeCommand( tmp );
    }

    // ベジエ曲線に削除処理を施す
    Vector<CurveType> target_curve = new Vector<CurveType>();
    if ( AppManager.isWholeSelectedIntervalEnabled() ) {
        // ctrlによる全選択モード
        for ( int i = 0; i < Utility.CURVE_USAGE.length; i++ ) {
            CurveType ct = Utility.CURVE_USAGE[i];
            if ( ct.isScalar() || ct.isAttachNote() ) {
                continue;
            }
            target_curve.add( ct );
        }
    } else {
        // 普通の選択モード
        target_curve.add( trackSelector.getSelectedCurve() );
    }
    work.AttachedCurves.get( AppManager.getSelected() - 1 ).deleteBeziers( target_curve, start_clock, end_clock );

    // コマンドを発行し、実行
    CadenciiCommand run = VsqFileEx.generateCommandReplace( work );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    this.setEdited( true );

    other_command_executed = true;
} else if ( AppManager.itemSelection.getTempoCount() > 0 ) {
    // テンポ変更のカット
    int count = -1;
    int[] dum = new int[AppManager.itemSelection.getTempoCount()];
    int[] clocks = new int[AppManager.itemSelection.getTempoCount()];
    for ( Iterator<ValuePair<Integer, SelectedTempoEntry>> itr = AppManager.itemSelection.getTempoIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTempoEntry> item = itr.next();
        int key = item.getKey();
        SelectedTempoEntry value = item.getValue();
        count++;
        dum[count] = -1;
        clocks[count] = value.original.Clock;
    }
    CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandUpdateTempoRange( clocks, clocks, dum ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
    other_command_executed = true;
} else if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
    // 拍子変更のカット
    int[] barcounts = new int[AppManager.itemSelection.getTimesigCount()];
    int[] numerators = new int[AppManager.itemSelection.getTimesigCount()];
    int[] denominators = new int[AppManager.itemSelection.getTimesigCount()];
    int count = -1;
    for ( Iterator<ValuePair<Integer, SelectedTimesigEntry>> itr = AppManager.itemSelection.getTimesigIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTimesigEntry> item = itr.next();
        int key = item.getKey();
        SelectedTimesigEntry value = item.getValue();
        count++;
        barcounts[count] = value.original.BarCount;
        numerators[count] = -1;
        denominators[count] = -1;
    }
    CadenciiCommand run = new CadenciiCommand(
        VsqCommand.generateCommandUpdateTimesigRange( barcounts, barcounts, numerators, denominators ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
    other_command_executed = true;
}

// 冒頭で作成した音符イベント削除以外に、コマンドが実行されなかった場合
if ( delete_event != null && !other_command_executed ) {
    CadenciiCommand run = new CadenciiCommand( delete_event );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
}

refreshScreen();
        }

        public void copyCurveCor(
int track,
CurveType curve_type,
int start,
int end,
Vector<BezierChain> copied_chain
        )
        {
for ( Iterator<BezierChain> itr = AppManager.getVsqFile().AttachedCurves.get( track - 1 ).get( curve_type ).iterator(); itr.hasNext(); ) {
    BezierChain bc = itr.next();
    int len = bc.points.size();
    if ( len < 2 ) {
        continue;
    }
    int chain_start = (int)bc.points.get( 0 ).getBase().getX();
    int chain_end = (int)bc.points.get( len - 1 ).getBase().getX();
    BezierChain add = null;
    if ( start < chain_start && chain_start < end && end < chain_end ) {
        // (1) chain_start ~ end をコピー
        try {
            add = bc.extractPartialBezier( chain_start, end );
        } catch ( Exception ex ) {
            Logger.write( FormMain.class + ".copyCurveCor; ex=" + ex + "\n" );
            add = null;
        }
    } else if ( chain_start <= start && end <= chain_end ) {
        // (2) start ~ endをコピー
        try {
            add = bc.extractPartialBezier( start, end );
        } catch ( Exception ex ) {
            Logger.write( FormMain.class + ".copyCurveCor; ex=" + ex + "\n" );
            add = null;
        }
    } else if ( chain_start < start && start < chain_end && chain_end <= end ) {
        // (3) start ~ chain_endをコピー
        try {
            add = bc.extractPartialBezier( start, chain_end );
        } catch ( Exception ex ) {
            Logger.write( FormMain.class + ".copyCurveCor; ex=" + ex + "\n" );
            add = null;
        }
    } else if ( start <= chain_start && chain_end <= end ) {
        // (4) 全部コピーでOK
        add = (BezierChain)bc.clone();
    }
    if ( add != null ) {
        copied_chain.add( add );
    }
}
        }

        /// <summary>
        /// トラック全体のコピーを行います。
        /// </summary>
        public void copyTrackCore()
        {
VsqFileEx vsq = AppManager.getVsqFile();
int selected = AppManager.getSelected();
VsqTrack track = (VsqTrack)vsq.Track.get( selected ).clone();
track.setName( track.getName() + " (1)" );
CadenciiCommand run = VsqFileEx.generateCommandAddTrack( track,
                                                         vsq.Mixer.Slave.get( selected - 1 ),
                                                         vsq.Track.size(),
                                                         vsq.AttachedCurves.get( selected - 1 ) ); ;
AppManager.editHistory.register( vsq.executeCommand( run ) );
setEdited( true );
AppManager.mMixerWindow.updateStatus();
refreshScreen();
        }

        /// <summary>
        /// トラックの名前変更を行います。
        /// </summary>
        public void changeTrackNameCore()
        {
InputBox ib = null;
try{
    int selected = AppManager.getSelected();
    VsqFileEx vsq = AppManager.getVsqFile();
    ib = new InputBox( _( "Input new name of track" ) );
    ib.setResult( vsq.Track.get( selected ).getName() );
    ib.setLocation( getFormPreferedLocation( ib ) );
    BDialogResult dr = AppManager.showModalDialog( ib, this );
    if( dr == BDialogResult.OK ){
        String ret = ib.getResult();
        CadenciiCommand run = new CadenciiCommand(
            VsqCommand.generateCommandTrackChangeName( selected, ret ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
        refreshScreen();
    }
}catch( Exception ex ){
}finally{
    if( ib != null ){
        ib.close();
    }
}

/*            if ( mTextBoxTrackName != null ) {
#if !JAVA
    if ( !mTextBoxTrackName.IsDisposed ) {
        mTextBoxTrackName.Dispose();
    }
#endif
    mTextBoxTrackName = null;
}
#if JAVA
mTextBoxTrackName = new LyricTextBox( this );
#else
mTextBoxTrackName = new LyricTextBox();
#endif
mTextBoxTrackName.setVisible( false );
int selector_width = trackSelector.getSelectorWidth();
int x = AppManager.keyWidth + (AppManager.getSelected() - 1) * selector_width;
mTextBoxTrackName.setLocation( x, trackSelector.getHeight() - TrackSelector.OFFSET_TRACK_TAB + 1 );
mTextBoxTrackName.setText( AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getName() );
#if JAVA
mTextBoxTrackName.keyUpEvent.add( new BKeyEventHandler( this, "mTextBoxTrackName_KeyUp" ) );
#else
mTextBoxTrackName.BorderStyle = System.Windows.Forms.BorderStyle.None;
mTextBoxTrackName.keyUpEvent.add( new System.Windows.Forms.KeyEventHandler( this, "mTextBoxTrackName_KeyUp" ) );
mTextBoxTrackName.Parent = trackSelector;
#endif
mTextBoxTrackName.setSize( selector_width, TrackSelector.OFFSET_TRACK_TAB );
mTextBoxTrackName.setVisible( true );
mTextBoxTrackName.requestFocus();
mTextBoxTrackName.selectAll();*/
        }

        /// <summary>
        /// トラックの削除を行います。
        /// </summary>
        public void deleteTrackCore()
        {
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
if ( AppManager.showMessageBox(
        PortUtil.formatMessage( _( "Do you wish to remove track? {0} : '{1}'" ), selected, vsq.Track.get( selected ).getName() ),
        _APP_NAME,
        org.kbinani.windows.forms.Utility.MSGBOX_YES_NO_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_QUESTION_MESSAGE ) == BDialogResult.YES ) {
    CadenciiCommand run = VsqFileEx.generateCommandDeleteTrack( selected );
    if ( selected >= 2 ) {
        AppManager.setSelected( selected - 1 );
    }
    AppManager.editHistory.register( vsq.executeCommand( run ) );
    updateDrawObjectList();
    setEdited( true );
    AppManager.mMixerWindow.updateStatus();
    refreshScreen();
}
        }

        /// <summary>
        /// トラックの追加を行います。
        /// </summary>
        public void addTrackCore()
        {
VsqFileEx vsq = AppManager.getVsqFile();
int i = vsq.Track.size();
String name = "Voice" + i;
String singer = AppManager.editorConfig.DefaultSingerName;
VsqTrack vsq_track = new VsqTrack( name, singer );

RendererKind kind = AppManager.editorConfig.DefaultSynthesizer;
String renderer = AppManager.getVersionStringFromRendererKind( kind );
Vector<VsqID> singers = AppManager.getSingerListFromRendererKind( kind );

vsq_track.changeRenderer( renderer, singers );
CadenciiCommand run = VsqFileEx.generateCommandAddTrack( vsq_track,
                                                         new VsqMixerEntry( 0, 0, 0, 0 ),
                                                         i,
                                                         new BezierCurves() );
AppManager.editHistory.register( vsq.executeCommand( run ) );
updateDrawObjectList();
setEdited( true );
AppManager.setSelected( i );
AppManager.mMixerWindow.updateStatus();
refreshScreen();
        }

        /// <summary>
        /// length, positionの各Quantizeモードに応じて、
        /// 関連する全てのメニュー・コンテキストメニューの表示状態を更新します。
        /// </summary>
        public void applyQuantizeMode()
        {
cMenuPianoQuantize04.setSelected( false );
cMenuPianoQuantize08.setSelected( false );
cMenuPianoQuantize16.setSelected( false );
cMenuPianoQuantize32.setSelected( false );
cMenuPianoQuantize64.setSelected( false );
cMenuPianoQuantize128.setSelected( false );
cMenuPianoQuantizeOff.setSelected( false );


menuSettingPositionQuantize04.setSelected( false );
menuSettingPositionQuantize08.setSelected( false );
menuSettingPositionQuantize16.setSelected( false );
menuSettingPositionQuantize32.setSelected( false );
menuSettingPositionQuantize64.setSelected( false );
menuSettingPositionQuantize128.setSelected( false );
menuSettingPositionQuantizeOff.setSelected( false );

if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p4 ) {
    cMenuPianoQuantize04.setSelected( true );
    menuSettingPositionQuantize04.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p8 ) {
    cMenuPianoQuantize08.setSelected( true );
    menuSettingPositionQuantize08.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p16 ) {
    cMenuPianoQuantize16.setSelected( true );
    menuSettingPositionQuantize16.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p32 ) {
    cMenuPianoQuantize32.setSelected( true );
    menuSettingPositionQuantize32.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p64 ) {
    cMenuPianoQuantize64.setSelected( true );
    menuSettingPositionQuantize64.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.p128 ) {
    cMenuPianoQuantize128.setSelected( true );
    menuSettingPositionQuantize128.setSelected( true );
} else if ( AppManager.editorConfig.getPositionQuantize() == QuantizeMode.off ) {
    cMenuPianoQuantizeOff.setSelected( true );
    menuSettingPositionQuantizeOff.setSelected( true );
}
cMenuPianoQuantizeTriplet.setSelected( AppManager.editorConfig.isPositionQuantizeTriplet() );
menuSettingPositionQuantizeTriplet.setSelected( AppManager.editorConfig.isPositionQuantizeTriplet() );
        }

        /// <summary>
        /// 現在選択されている編集ツールに応じて、メニューのチェック状態を更新します
        /// </summary>
        public void applySelectedTool()
        {
EditTool tool = AppManager.getSelectedTool();

int count = toolStripTool.getComponentCount();
for ( int i = 0; i < count; i++ ) {
    Object tsi = toolStripTool.getComponentAtIndex( i );
    if( tsi instanceof PaletteToolButton ){
        BToggleButton tsb = (PaletteToolButton)tsi;
        boolean sel = false;
        tsb.setSelected( sel );
    }
}
MenuElement[] items = cMenuTrackSelectorPaletteTool.getSubElements();
for ( MenuElement tsi : items ) {
    if ( tsi instanceof PaletteToolMenuItem ) {
        PaletteToolMenuItem tsmi = (PaletteToolMenuItem)tsi;
        String id = tsmi.getPaletteToolID();
        boolean sel = false;
        tsmi.setSelected( sel );
    }
}

items = cMenuPianoPaletteTool.getSubElements();
for ( MenuElement tsi : items ) {
    if ( tsi instanceof PaletteToolMenuItem ) {
        PaletteToolMenuItem tsmi = (PaletteToolMenuItem)tsi;
        String id = tsmi.getPaletteToolID();
        boolean sel = false;
        tsmi.setSelected( sel );
    }
}

EditTool selected_tool = AppManager.getSelectedTool();
cMenuPianoPointer.setSelected( (selected_tool == EditTool.ARROW) );
cMenuPianoPencil.setSelected( (selected_tool == EditTool.PENCIL) );
cMenuPianoEraser.setSelected( (selected_tool == EditTool.ERASER) );

cMenuTrackSelectorPointer.setSelected( (selected_tool == EditTool.ARROW) );
cMenuTrackSelectorPencil.setSelected( (selected_tool == EditTool.PENCIL) );
cMenuTrackSelectorLine.setSelected( (selected_tool == EditTool.LINE) );
cMenuTrackSelectorEraser.setSelected( (selected_tool == EditTool.ERASER) );

stripBtnPointer.setSelected( (selected_tool == EditTool.ARROW) );
stripBtnPencil.setSelected( (selected_tool == EditTool.PENCIL) );
stripBtnLine.setSelected( (selected_tool == EditTool.LINE) );
stripBtnEraser.setSelected( (selected_tool == EditTool.ERASER) );


cMenuPianoCurve.setSelected( AppManager.isCurveMode() );
cMenuTrackSelectorCurve.setSelected( AppManager.isCurveMode() );
stripBtnCurve.setSelected( AppManager.isCurveMode() );
        }

        /// <summary>
        /// 描画すべきオブジェクトのリスト，AppManager.drawObjectsを更新します
        /// </summary>
        public void updateDrawObjectList()
        {
// AppManager.m_draw_objects
if ( AppManager.mDrawObjects == null ) {
    AppManager.mDrawObjects = new Vector<Vector<DrawObject>>();
}
synchronized ( AppManager.mDrawObjects ) {
    if ( AppManager.getVsqFile() == null ) {
        return;
    }
    for ( int i = 0; i < AppManager.mDrawStartIndex.length; i++ ) {
        AppManager.mDrawStartIndex[i] = 0;
    }
    if ( AppManager.mDrawObjects != null ) {
        for ( Iterator<Vector<DrawObject>> itr = AppManager.mDrawObjects.iterator(); itr.hasNext(); ) {
            Vector<DrawObject> list = itr.next();
            list.clear();
        }
        AppManager.mDrawObjects.clear();
    }

    int xoffset = AppManager.keyOffset;// 6 + AppManager.keyWidth;
    int yoffset = (int)(127 * (int)(100 * controller.getScaleY()));
    float scalex = controller.getScaleX();
    Font SMALL_FONT = null;
    try {
        SMALL_FONT = new Font( AppManager.editorConfig.ScreenFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE8 );
        int track_height = (int)(100 * controller.getScaleY());
        VsqFileEx vsq = AppManager.getVsqFile();
        int track_count = vsq.Track.size();
        Polygon env = new Polygon( new int[7], new int[7], 7 );
        ByRef<Integer> overlap_x = new ByRef<Integer>( 0 );
        for ( int track = 1; track < track_count; track++ ) {
            VsqTrack vsq_track = vsq.Track.get( track );
            Vector<DrawObject> tmp = new Vector<DrawObject>();
            RendererKind kind = VsqFileEx.getTrackRendererKind( vsq_track );
            AppManager.mDrawIsUtau[track - 1] = kind == RendererKind.UTAU;

            // 音符イベント
            Iterator<VsqEvent> itr_note = vsq_track.getNoteEventIterator();
            VsqEvent item_prev = null;
            VsqEvent item = null;
            VsqEvent item_next = itr_note.hasNext() ? itr_note.next() : null;
            while ( item_prev != null || item != null || item_next != null ) {
                item_prev = item;
                item = item_next;
                if ( itr_note.hasNext() ) {
                    item_next = itr_note.next();
                } else {
                    item_next = null;
                }
                if ( item == null ) {
                    continue;
                }
                if ( item.ID.LyricHandle == null ) {
                    continue;
                }
                int timesig = item.Clock;
                int length = item.ID.getLength();
                int note = item.ID.Note;
                int x = (int)(timesig * scalex + xoffset);
                int y = -note * track_height + yoffset;
                int lyric_width = (int)(length * scalex);
                String lyric_jp = item.ID.LyricHandle.L0.Phrase;
                String lyric_en = item.ID.LyricHandle.L0.getPhoneticSymbol();
                String title = Utility.trimString( lyric_jp + " [" + lyric_en + "]", SMALL_FONT, lyric_width );
                int accent = item.ID.DEMaccent;
                int px_vibrato_start = x + lyric_width;
                int px_vibrato_end = x;
                int px_vibrato_delay = lyric_width * 2;
                int vib_delay = length;
                if ( item.ID.VibratoHandle != null ) {
                    vib_delay = item.ID.VibratoDelay;
                    double rate = (double)vib_delay / (double)length;
                    px_vibrato_delay = _PX_ACCENT_HEADER + (int)((lyric_width - _PX_ACCENT_HEADER) * rate);
                }
                VibratoBPList rate_bp = null;
                VibratoBPList depth_bp = null;
                int rate_start = 0;
                int depth_start = 0;
                if ( item.ID.VibratoHandle != null ) {
                    rate_bp = item.ID.VibratoHandle.getRateBP();
                    depth_bp = item.ID.VibratoHandle.getDepthBP();
                    rate_start = item.ID.VibratoHandle.getStartRate();
                    depth_start = item.ID.VibratoHandle.getStartDepth();
                }

                // analyzed/のSTFが引き当てられるかどうか
                // UTAUのWAVが引き当てられるかどうか
                boolean is_valid_for_utau = false;
                VsqEvent singer_at_clock = vsq_track.getSingerEventAt( timesig );
                int program = singer_at_clock.ID.IconHandle.Program;
                if ( 0 <= program && program < AppManager.editorConfig.UtauSingers.size() ) {
                    SingerConfig sc = AppManager.editorConfig.UtauSingers.get( program );
                    // 通常のUTAU音源
                    if ( AppManager.mUtauVoiceDB.containsKey( sc.VOICEIDSTR ) ) {
                        UtauVoiceDB db = AppManager.mUtauVoiceDB.get( sc.VOICEIDSTR );
                        OtoArgs oa = db.attachFileNameFromLyric( lyric_jp );
                        if ( oa.fileName == null ||
                            (oa.fileName != null && str.compare( oa.fileName, "" )) ) {
                            is_valid_for_utau = false;
                        } else {
                            is_valid_for_utau = fsys.isFileExists( fsys.combine( sc.VOICEIDSTR, oa.fileName ) );
                        }
                    }
                }
                int intensity = item.UstEvent == null ? 100 : item.UstEvent.getIntensity();

                //追加
                tmp.add( new DrawObject( DrawObjectType.Note,
                                         vsq,
                                         new Rectangle( x, y, lyric_width, track_height ),
                                         title,
                                         accent,
                                         item.ID.DEMdecGainRate,
                                         item.ID.Dynamics,
                                         item.InternalID,
                                         px_vibrato_delay,
                                         false,
                                         item.ID.LyricHandle.L0.PhoneticSymbolProtected,
                                         rate_bp,
                                         depth_bp,
                                         rate_start,
                                         depth_start,
                                         item.ID.Note,
                                         item.UstEvent.getEnvelope(),
                                         length,
                                         timesig,
                                         is_valid_for_utau,
                                         is_valid_for_utau, // vConnect-STANDはstfファイルを必要としないので，
                                         vib_delay,
                                         intensity ) );
            }

            // Dynaff, Crescendイベント
            for ( Iterator<VsqEvent> itr = vsq_track.getDynamicsEventIterator(); itr.hasNext(); ) {
                VsqEvent item_itr = itr.next();
                IconDynamicsHandle handle = item_itr.ID.IconDynamicsHandle;
                if ( handle == null ) {
                    continue;
                }
                int clock = item_itr.Clock;
                int length = item_itr.ID.getLength();
                if ( length <= 0 ) {
                    length = 1;
                }
                int raw_width = (int)(length * scalex);
                DrawObjectType type = DrawObjectType.Note;
                int width = 0;
                String str = "";
                if ( handle.isDynaffType() ) {
                    // 強弱記号
                    type = DrawObjectType.Dynaff;
                    width = AppManager.DYNAFF_ITEM_WIDTH;
                    int startDyn = handle.getStartDyn();
                    if ( startDyn == 120 ) {
                        str = "fff";
                    } else if ( startDyn == 104 ) {
                        str = "ff";
                    } else if ( startDyn == 88 ) {
                        str = "f";
                    } else if ( startDyn == 72 ) {
                        str = "mf";
                    } else if ( startDyn == 56 ) {
                        str = "mp";
                    } else if ( startDyn == 40 ) {
                        str = "p";
                    } else if ( startDyn == 24 ) {
                        str = "pp";
                    } else if ( startDyn == 8 ) {
                        str = "ppp";
                    } else {
                        str = "?";
                    }
                } else if ( handle.isCrescendType() ) {
                    // クレッシェンド
                    type = DrawObjectType.Crescend;
                    width = raw_width;
                    str = handle.IDS;
                } else if ( handle.isDecrescendType() ) {
                    // デクレッシェンド
                    type = DrawObjectType.Decrescend;
                    width = raw_width;
                    str = handle.IDS;
                }
                if ( type == DrawObjectType.Note ) {
                    continue;
                }
                int note = item_itr.ID.Note;
                int x = (int)(clock * scalex + xoffset);
                int y = -note * (int)(100 * controller.getScaleY()) + yoffset;
                tmp.add( new DrawObject( type,
                                         vsq,
                                         new Rectangle( x, y, width, track_height ),
                                         str,
                                         0,
                                         0,
                                         0,
                                         item_itr.InternalID,
                                         0,
                                         false,
                                         false,
                                         null,
                                         null,
                                         0,
                                         0,
                                         item_itr.ID.Note,
                                         null,
                                         length,
                                         clock,
                                         true,
                                         true,
                                         length,
                                         0 ) );
            }

            // 重複部分があるかどうかを判定
            int count = tmp.size();
            for ( int i = 0; i < count - 1; i++ ) {
                DrawObject itemi = tmp.get( i );
                DrawObjectType parent_type = itemi.mType;
                /*if ( itemi.type != DrawObjectType.Note ) {
                    continue;
                }*/
                boolean overwrapped = false;
                int istart = itemi.mClock;
                int iend = istart + itemi.mLength;
                if ( itemi.mIsOverlapped ) {
                    continue;
                }
                for ( int j = i + 1; j < count; j++ ) {
                    DrawObject itemj = tmp.get( j );
                    if ( (itemj.mType == DrawObjectType.Note && parent_type != DrawObjectType.Note) ||
                         (itemj.mType != DrawObjectType.Note && parent_type == DrawObjectType.Note) ) {
                        continue;
                    }
                    int jstart = itemj.mClock;
                    int jend = jstart + itemj.mLength;
                    if ( jstart <= istart ) {
                        if ( istart < jend ) {
                            overwrapped = true;
                            itemj.mIsOverlapped = true;
                            // breakできない．2個以上の重複を検出する必要があるので．
                        }
                    }
                    if ( istart <= jstart ) {
                        if ( jstart < iend ) {
                            overwrapped = true;
                            itemj.mIsOverlapped = true;
                        }
                    }
                }
                if ( overwrapped ) {
                    itemi.mIsOverlapped = true;
                }
            }
            Collections.sort( tmp );
            AppManager.mDrawObjects.add( tmp );
        }
    } catch ( Exception ex ) {
        Logger.write( FormMain.class + ".updateDrawObjectList; ex=" + ex + "\n" );
        serr.println( "FormMain#updateDrawObjectList; ex=" + ex );
        ex.printStackTrace();
    } finally {
    }
}
        }

        /// <summary>
        /// editorConfigのRecentFilesを元に，menuFileRecentのドロップダウンアイテムを更新します
        /// </summary>
        public void updateRecentFileMenu()
        {
int added = 0;
menuFileRecent.removeAll();
if ( AppManager.editorConfig.RecentFiles != null ) {
    for ( int i = 0; i < AppManager.editorConfig.RecentFiles.size(); i++ ) {
        String item = AppManager.editorConfig.RecentFiles.get( i );
        if ( item == null ) {
            continue;
        }
        if ( item != "" ) {
            String short_name = PortUtil.getFileName( item );
            boolean available = fsys.isFileExists( item );
            RecentFileMenuItem itm = new RecentFileMenuItem( item );
            itm.setText( short_name );
            String tooltip = "";
            if ( !available ) {
                tooltip = _( "[file not found]" ) + " ";
            }
            tooltip += item;
            itm.setToolTipText( tooltip );
            itm.setEnabled( available );
            itm.clickEvent.add( new BEventHandler( this, "handleRecentFileMenuItem_Click" ) );
            itm.mouseEnterEvent.add( new BEventHandler( this, "handleRecentFileMenuItem_MouseEnter" ) );
            menuFileRecent.add( itm );
            added++;
        }
    }
} else {
    AppManager.editorConfig.pushRecentFiles( "" );
}
menuFileRecent.addSeparator();
menuFileRecent.add( menuFileRecentClear );
menuFileRecent.setEnabled( true );
        }

        /// <summary>
        /// 最後に保存したときから変更されているかどうかを取得または設定します
        /// </summary>
        public boolean isEdited()
        {
return mEdited;
        }

        public void setEdited( boolean value )
        {
mEdited = value;
String file = AppManager.getFileName();
if ( str.compare( file, "" ) ) {
    file = "Untitled";
} else {
    file = PortUtil.getFileNameWithoutExtension( file );
}
if ( mEdited ) {
    file += " *";
}
String title = file + " - " + _APP_NAME;
if ( !str.compare( getTitle(), title ) ) {
    setTitle( title );
}
boolean redo = AppManager.editHistory.hasRedoHistory();
boolean undo = AppManager.editHistory.hasUndoHistory();
menuEditRedo.setEnabled( redo );
menuEditUndo.setEnabled( undo );
cMenuPianoRedo.setEnabled( redo );
cMenuPianoUndo.setEnabled( undo );
cMenuTrackSelectorRedo.setEnabled( redo );
cMenuTrackSelectorUndo.setEnabled( undo );
stripBtnUndo.setEnabled( undo );
stripBtnRedo.setEnabled( redo );
//AppManager.setRenderRequired( AppManager.getSelected(), true );
updateScrollRangeHorizontal();
updateDrawObjectList();
panelOverview.updateCachedImage();

AppManager.propertyPanel.updateValue( AppManager.getSelected() );
        }

        /// <summary>
        /// 入力用のテキストボックスを初期化します
        /// </summary>
        public void showInputTextBox( String phrase, String phonetic_symbol, Point position, boolean phonetic_symbol_edit_mode )
        {
hideInputTextBox();

AppManager.mInputTextBox.keyUpEvent.add( new BKeyEventHandler( this, "mInputTextBox_KeyUp" ) );
AppManager.mInputTextBox.keyDownEvent.add( new BKeyEventHandler( this, "mInputTextBox_KeyDown" ) );
//TODO: JAVA: AppManager.mInputTextBox.ImeModeChanged += mInputTextBox_ImeModeChanged;

AppManager.mInputTextBox.setImeModeOn( mLastIsImeModeOn );
if ( phonetic_symbol_edit_mode ) {
    AppManager.mInputTextBox.setBufferText( phrase );
    AppManager.mInputTextBox.setPhoneticSymbolEditMode( true );
    AppManager.mInputTextBox.setText( phonetic_symbol );
    AppManager.mInputTextBox.setBackground( mColorTextboxBackcolor );
} else {
    AppManager.mInputTextBox.setBufferText( phonetic_symbol );
    AppManager.mInputTextBox.setPhoneticSymbolEditMode( false );
    AppManager.mInputTextBox.setText( phrase );
    AppManager.mInputTextBox.setBackground( Color.white );
}
AppManager.mInputTextBox.setFont( new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE9 ) );
Point p = new Point( position.x + 4, position.y + 2 );
p = pictPianoRoll.pointToScreen( p );
AppManager.mInputTextBox.setLocation( p );

AppManager.mInputTextBox.setEnabled( true );
AppManager.mInputTextBox.setVisible( true );
AppManager.mInputTextBox.requestFocusInWindow();
AppManager.mInputTextBox.selectAll();
        }

        public void hideInputTextBox()
        {
AppManager.mInputTextBox.keyUpEvent.remove( new BKeyEventHandler( this, "mInputTextBox_KeyUp" ) );
AppManager.mInputTextBox.keyDownEvent.remove( new BKeyEventHandler( this, "mInputTextBox_KeyDown" ) );
// TODO: JAVA: AppManager.mInputTextBox.ImeModeChanged -= mInputTextBox_ImeModeChanged;
mLastSymbolEditMode = AppManager.mInputTextBox.isPhoneticSymbolEditMode();
AppManager.mInputTextBox.setVisible( false );
AppManager.mInputTextBox.setEnabled( false );
focusPianoRoll();
        }

        /// <summary>
        /// 歌詞入力用テキストボックスのモード（歌詞/発音記号）を切り替えます
        /// </summary>
        public void flipInputTextBoxMode()
        {
String new_value = AppManager.mInputTextBox.getText();
if ( !AppManager.mInputTextBox.isPhoneticSymbolEditMode() ) {
    AppManager.mInputTextBox.setBackground( mColorTextboxBackcolor );
} else {
    AppManager.mInputTextBox.setBackground( Color.white );
}
AppManager.mInputTextBox.setText( AppManager.mInputTextBox.getBufferText() );
AppManager.mInputTextBox.setBufferText( new_value );
AppManager.mInputTextBox.setPhoneticSymbolEditMode( !AppManager.mInputTextBox.isPhoneticSymbolEditMode() );
        }

        /// <summary>
        /// アンドゥ処理を行います
        /// </summary>
        public void undo()
        {
if ( AppManager.editHistory.hasUndoHistory() ) {
    AppManager.undo();
    menuEditRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    menuEditUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    cMenuPianoRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    cMenuPianoUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    cMenuTrackSelectorRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    cMenuTrackSelectorUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    AppManager.mMixerWindow.updateStatus();
    setEdited( true );
    updateDrawObjectList();

    if ( AppManager.propertyPanel != null ) {
        AppManager.propertyPanel.updateValue( AppManager.getSelected() );
    }
}
        }

        /// <summary>
        /// リドゥ処理を行います
        /// </summary>
        public void redo()
        {
if ( AppManager.editHistory.hasRedoHistory() ) {
    AppManager.redo();
    menuEditRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    menuEditUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    cMenuPianoRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    cMenuPianoUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    cMenuTrackSelectorRedo.setEnabled( AppManager.editHistory.hasRedoHistory() );
    cMenuTrackSelectorUndo.setEnabled( AppManager.editHistory.hasUndoHistory() );
    AppManager.mMixerWindow.updateStatus();
    setEdited( true );
    updateDrawObjectList();

    if ( AppManager.propertyPanel != null ) {
        AppManager.propertyPanel.updateValue( AppManager.getSelected() );
    }
}
        }

        /// <summary>
        /// xvsqファイルを開きます
        /// </summary>
        /// <returns>ファイルを開くのに成功した場合trueを，それ以外はfalseを返します</returns>
        public boolean openVsqCor( String file )
        {
if( AppManager.readVsq( file ) ){
    return true;
}
if ( AppManager.getVsqFile().Track.size() >= 2 ) {
    updateScrollRangeHorizontal();
}
AppManager.editorConfig.pushRecentFiles( file );
updateRecentFileMenu();
setEdited( false );
AppManager.editHistory.clear();
AppManager.mMixerWindow.updateStatus();

// キャッシュwaveなどの処理
if ( AppManager.editorConfig.UseProjectCache ) {
    VsqFileEx vsq = AppManager.getVsqFile();
    String cacheDir = vsq.cacheDir; // xvsqに保存されていたキャッシュのディレクトリ
    String dir = PortUtil.getDirectoryName( file );
    String name = PortUtil.getFileNameWithoutExtension( file );
    String estimatedCacheDir = fsys.combine( dir, name + ".cadencii" ); // ファイル名から推測されるキャッシュディレクトリ
    if ( cacheDir == null ) {
        cacheDir = "";
    }
    if ( !str.compare( cacheDir, "" ) && 
         fsys.isDirectoryExists( cacheDir ) &&
         !str.compare( estimatedCacheDir, "" ) &&
         !str.compare( cacheDir, estimatedCacheDir ) ) {
        // ファイル名から推測されるキャッシュディレクトリ名と
        // xvsqに指定されているキャッシュディレクトリと異なる場合
        // cacheDirの必要な部分をestimatedCacheDirに移す

        // estimatedCacheDirが存在しない場合、新しく作る
        if ( !fsys.isDirectoryExists( estimatedCacheDir ) ) {
            try {
                PortUtil.createDirectory( estimatedCacheDir );
            } catch ( Exception ex ) {
                Logger.write( FormMain.class + ".openVsqCor; ex=" + ex + "\n" );
                serr.println( "FormMain#openVsqCor; ex=" + ex );
                AppManager.showMessageBox( PortUtil.formatMessage( _( "cannot create cache directory: '{0}'" ), estimatedCacheDir ),
                                           _( "Info." ),
                                           PortUtil.OK_OPTION,
                                           org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
                return true;
            }
        }

        // ファイルを移す
        for ( int i = 1; i < vsq.Track.size(); i++ ) {
            String wavFrom = fsys.combine( cacheDir, i + ".wav" );
            String xmlFrom = fsys.combine( cacheDir, i + ".xml" );

            String wavTo = fsys.combine( estimatedCacheDir, i + ".wav" );
            String xmlTo = fsys.combine( estimatedCacheDir, i + ".xml" );
            if ( fsys.isFileExists( wavFrom ) ) {
                try {
                    PortUtil.moveFile( wavFrom, wavTo );
                } catch ( Exception ex ) {
                    Logger.write( FormMain.class + ".openVsqCor; ex=" + ex + "\n" );
                    serr.println( "FormMain#openVsqCor; ex=" + ex );
                }
            }
            if ( fsys.isFileExists( xmlFrom ) ) {
                try {
                    PortUtil.moveFile( xmlFrom, xmlTo );
                } catch ( Exception ex ) {
                    Logger.write( FormMain.class + ".openVsqCor; ex=" + ex + "\n" );
                    serr.println( "FormMain#openVsqCor; ex=" + ex );
                }
            }
        }
    }
    cacheDir = estimatedCacheDir;

    // キャッシュが無かったら作成
    if ( !fsys.isDirectoryExists( cacheDir ) ) {
        try {
            PortUtil.createDirectory( cacheDir );
        } catch ( Exception ex ) {
            Logger.write( FormMain.class + ".openVsqCor; ex=" + ex + "\n" );
            serr.println( "FormMain#openVsqCor; ex=" + ex );
            AppManager.showMessageBox( PortUtil.formatMessage( _( "cannot create cache directory: '{0}'" ), estimatedCacheDir ),
                                       _( "Info." ),
                                       PortUtil.OK_OPTION,
                                       org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
            return true;
        }
    }

    // RenderedStatusを読み込む
    for ( int i = 1; i < vsq.Track.size(); i++ ) {
        AppManager.deserializeRenderingStatus( cacheDir, i );
    }

    // キャッシュ内のwavを、waveViewに読み込む
    waveView.unloadAll();
    for ( int i = 1; i < vsq.Track.size(); i++ ) {
        String wav = fsys.combine( cacheDir, i + ".wav" );
        if ( !fsys.isFileExists( wav ) ) {
            continue;
        }
        waveView.load( i - 1, wav );
    }

    // 一時ディレクトリを、cachedirに変更
    AppManager.setTempWaveDir( cacheDir );
}
return false;
        }

        public void updateMenuFonts()
        {
if ( str.compare( AppManager.editorConfig.BaseFontName, "" ) ) {
    return;
}
Font font = AppManager.editorConfig.getBaseFont();
Util.applyFontRecurse( this, font );
Util.applyContextMenuFontRecurse( cMenuPiano, font );
Util.applyContextMenuFontRecurse( cMenuTrackSelector, font );
if ( AppManager.mMixerWindow != null ) {
    Util.applyFontRecurse( AppManager.mMixerWindow, font );
}
Util.applyContextMenuFontRecurse( cMenuTrackTab, font );
trackSelector.applyFont( font );
Util.applyToolStripFontRecurse( menuFile, font );
Util.applyToolStripFontRecurse( menuEdit, font );
Util.applyToolStripFontRecurse( menuVisual, font );
Util.applyToolStripFontRecurse( menuJob, font );
Util.applyToolStripFontRecurse( menuTrack, font );
Util.applyToolStripFontRecurse( menuLyric, font );
Util.applyToolStripFontRecurse( menuScript, font );
Util.applyToolStripFontRecurse( menuSetting, font );
Util.applyToolStripFontRecurse( menuHelp, font );
if ( mDialogPreference != null ) {
    Util.applyFontRecurse( mDialogPreference, font );
}

AppManager.superFont10Bold = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.BOLD, AppManager.FONT_SIZE10 );
AppManager.superFont8 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE8 );
AppManager.superFont10 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE10 );
AppManager.superFont9 = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE9 );
AppManager.superFont50Bold = new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.BOLD, AppManager.FONT_SIZE50 );
AppManager.superFont10OffsetHeight = Util.getStringDrawOffset( AppManager.superFont10 );
AppManager.superFont8OffsetHeight = Util.getStringDrawOffset( AppManager.superFont8 );
AppManager.superFont9OffsetHeight = Util.getStringDrawOffset( AppManager.superFont9 );
AppManager.superFont50OffsetHeight = Util.getStringDrawOffset( AppManager.superFont50Bold );
AppManager.superFont8Height = Util.measureString( Util.PANGRAM, AppManager.superFont8 ).height;
AppManager.superFont9Height = Util.measureString( Util.PANGRAM, AppManager.superFont9 ).height;
AppManager.superFont10Height = Util.measureString( Util.PANGRAM, AppManager.superFont10 ).height;
AppManager.superFont50Height = Util.measureString( Util.PANGRAM, AppManager.superFont50Bold ).height;
        }

        public void picturePositionIndicatorDrawTo( java.awt.Graphics g1 )
        {
Graphics2D g = (Graphics2D)g1;
Font SMALL_FONT = AppManager.superFont8;
int small_font_offset = AppManager.superFont8OffsetHeight;
try {
    int key_width = AppManager.keyWidth;
    int width = picturePositionIndicator.getWidth();
    int height = picturePositionIndicator.getHeight();
    VsqFileEx vsq = AppManager.getVsqFile();

    int dashed_line_step = AppManager.getPositionQuantizeClock();
    for ( Iterator<VsqBarLineType> itr = vsq.getBarLineIterator( AppManager.clockFromXCoord( width ) ); itr.hasNext(); ) {
        VsqBarLineType blt = itr.next();
        int local_clock_step = 480 * 4 / blt.getLocalDenominator();
        int x = AppManager.xCoordFromClocks( blt.clock() );
        if ( blt.isSeparator() ) {
            int current = blt.getBarCount() - vsq.getPreMeasure() + 1;
            g.setColor( mColorR105G105B105 );
            g.drawLine( x, 0, x, 49 );
            // 小節の数字
            //g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
            g.setColor( Color.black );
            g.setFont( SMALL_FONT );
            g.drawString( current + "", x + 4, 8 - small_font_offset + 1 );
            //g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.Default;
        } else {
            g.setColor( mColorR105G105B105 );
            g.drawLine( x, 11, x, 16 );
            g.drawLine( x, 26, x, 31 );
            g.drawLine( x, 41, x, 46 );
        }
        if ( dashed_line_step > 1 && AppManager.isGridVisible() ) {
            int numDashedLine = local_clock_step / dashed_line_step;
            for ( int i = 1; i < numDashedLine; i++ ) {
                int x2 = AppManager.xCoordFromClocks( blt.clock() + i * dashed_line_step );
                g.setColor( mColorR065G065B065 );
                g.drawLine( x2, 9 + 5, x2, 14 + 3 );
                g.drawLine( x2, 24 + 5, x2, 29 + 3 );
                g.drawLine( x2, 39 + 5, x2, 44 + 3 );
            }
        }
    }

    if ( vsq != null ) {
        int c = vsq.TimesigTable.size();
        for ( int i = 0; i < c; i++ ) {
            TimeSigTableEntry itemi = vsq.TimesigTable.get( i );
            int clock = itemi.Clock;
            int barcount = itemi.BarCount;
            int x = AppManager.xCoordFromClocks( clock );
            if ( width < x ) {
                break;
            }
            String s = itemi.Numerator + "/" + itemi.Denominator;
            g.setFont( SMALL_FONT );
            if ( AppManager.itemSelection.isTimesigContains( barcount ) ) {
                g.setColor( AppManager.getHilightColor() );
                g.drawString( s, x + 4, 40 - small_font_offset + 1 );
            } else {
                g.setColor( Color.black );
                g.drawString( s, x + 4, 40 - small_font_offset + 1 );
            }

            if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TIMESIG ) {
                if ( AppManager.itemSelection.isTimesigContains( barcount ) ) {
                    int edit_clock_x = AppManager.xCoordFromClocks( vsq.getClockFromBarCount( AppManager.itemSelection.getTimesig( barcount ).editing.BarCount ) );
                    g.setColor( mColorR187G187B255 );
                    g.drawLine( edit_clock_x - 1, 32,
                                edit_clock_x - 1, picturePositionIndicator.getHeight() - 1 );
                    g.setColor( mColorR007G007B151 );
                    g.drawLine( edit_clock_x, 32,
                                edit_clock_x, picturePositionIndicator.getHeight() - 1 );
                }
            }
        }

        g.setFont( SMALL_FONT );
        c = vsq.TempoTable.size();
        for ( int i = 0; i < c; i++ ) {
            TempoTableEntry itemi = vsq.TempoTable.get( i );
            int clock = itemi.Clock;
            int x = AppManager.xCoordFromClocks( clock );
            if ( width < x ) {
                break;
            }
            String s = PortUtil.formatDecimal( "#.00", 60e6 / (float)itemi.Tempo );
            if ( AppManager.itemSelection.isTempoContains( clock ) ) {
                g.setColor( AppManager.getHilightColor() );
                g.drawString( s, x + 4, 24 - small_font_offset + 1 );
            } else {
                g.setColor( Color.black );
                g.drawString( s, x + 4, 24 - small_font_offset + 1 );
            }

            if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TEMPO ) {
                if ( AppManager.itemSelection.isTempoContains( clock ) ) {
                    int edit_clock_x = AppManager.xCoordFromClocks( AppManager.itemSelection.getTempo( clock ).editing.Clock );
                    g.setColor( mColorR187G187B255 );
                    g.drawLine( edit_clock_x - 1, 18,
                                edit_clock_x - 1, 32 );
                    g.setColor( mColorR007G007B151 );
                    g.drawLine( edit_clock_x, 18,
                                edit_clock_x, 32 );
                }
            }
        }
    }

    // ソングポジション
    float xoffset = key_width + AppManager.keyOffset - controller.getStartToDrawX();
    int marker_x = (int)(AppManager.getCurrentClock() * controller.getScaleX() + xoffset);
    if ( key_width <= marker_x && marker_x <= width ) {
        g.setStroke( new BasicStroke( 2.0f ) );
        g.setColor( Color.white );
        g.drawLine( marker_x, 0, marker_x, height );
        g.setStroke( new BasicStroke() );
    }

    // スタートマーカーとエンドマーカー
    boolean right = false;
    boolean left = false;
    if ( vsq.config.StartMarkerEnabled ) {
        int x = AppManager.xCoordFromClocks( vsq.config.StartMarker );
        if ( x < key_width ) {
            left = true;
        } else if ( width < x ) {
            right = true;
        } else {
            g.drawImage(
                Resources.get_start_marker(), x, 3, this );
        }
    }
    if ( vsq.config.EndMarkerEnabled ) {
        int x = AppManager.xCoordFromClocks( vsq.config.EndMarker ) - 6;
        if ( x < key_width ) {
            left = true;
        } else if ( width < x ) {
            right = true;
        } else {
            g.drawImage(
                Resources.get_end_marker(), x, 3, this );
        }
    }

    // 範囲外にスタートマーカーとエンドマーカーがある場合のマーク
    if ( right ) {
        g.setColor( Color.white );
        g.fillPolygon(
            new int[] { width - 6, width, width - 6 },
            new int[] { 3, 10, 16 },
            3 );
    }
    if ( left ) {
        g.setColor( Color.white );
        g.fillPolygon(
            new int[] { key_width + 7, key_width + 1, key_width + 7 },
            new int[] { 3, 10, 16 },
            3 );
    }

    // TEMPO BEATの文字の部分。小節数が被っている可能性があるので、塗り潰す
    g.setColor( picturePositionIndicator.getBackground() );
    g.fillRect( 0, 0, AppManager.keyWidth, 48 );
    // 横ライン上
    g.setColor( new Color( 104, 104, 104 ) );
    g.drawLine( 0, 17, width, 17 );
    // 横ライン中央
    g.drawLine( 0, 32, width, 32 );
    // 横ライン下
    g.drawLine( 0, 47, width, 47 );
    // 縦ライン
    g.drawLine( AppManager.keyWidth, 0, AppManager.keyWidth, 48 );
    /* TEMPO&BEATとピアノロールの境界 */
    g.drawLine( AppManager.keyWidth, 48, width - 18, 48 );
    g.setFont( SMALL_FONT );
    g.setColor( Color.black );
    g.drawString( "TEMPO", 11, 24 - small_font_offset + 1 );
    g.drawString( "BEAT", 11, 40 - small_font_offset + 1 );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".picturePositionIndicatorDrawTo; ex=" + ex + "\n" );
    serr.println( "FormMain#picturePositionIndicatorDrawTo; ex=" + ex );
}
        }

        /// <summary>
        /// イベントハンドラを登録します。
        /// </summary>
        public void registerEventHandlers()
        {
this.loadEvent.add( new BEventHandler( this, "FormMain_Load" ) );
menuFileNew.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileNew.clickEvent.add( new BEventHandler( this, "handleFileNew_Click" ) );
menuFileOpen.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileOpen.clickEvent.add( new BEventHandler( this, "handleFileOpen_Click" ) );
menuFileSave.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileSave.clickEvent.add( new BEventHandler( this, "handleFileSave_Click" ) );
menuFileSaveNamed.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileSaveNamed.clickEvent.add( new BEventHandler( this, "menuFileSaveNamed_Click" ) );
menuFileOpenVsq.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileOpenVsq.clickEvent.add( new BEventHandler( this, "menuFileOpenVsq_Click" ) );
menuFileOpenUst.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileOpenUst.clickEvent.add( new BEventHandler( this, "menuFileOpenUst_Click" ) );
menuFileImport.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileImportMidi.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileImportMidi.clickEvent.add( new BEventHandler( this, "menuFileImportMidi_Click" ) );
menuFileImportUst.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileImportUst.clickEvent.add( new BEventHandler( this, "menuFileImportUst_Click" ) );
menuFileImportVsq.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileImportVsq.clickEvent.add( new BEventHandler( this, "menuFileImportVsq_Click" ) );
menuFileExport.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExport.dropDownOpeningEvent.add( new BEventHandler( this, "menuFileExport_DropDownOpening" ) );
menuFileExportWave.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportWave.clickEvent.add( new BEventHandler( this, "menuFileExportWave_Click" ) );
menuFileExportParaWave.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportParaWave.clickEvent.add( new BEventHandler( this, "menuFileExportParaWave_Click" ) );
menuFileExportMidi.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportMidi.clickEvent.add( new BEventHandler( this, "menuFileExportMidi_Click" ) );
menuFileExportMusicXml.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportMusicXml.clickEvent.add( new BEventHandler( this, "menuFileExportMusicXml_Click" ) );
menuFileExportUst.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportUst.clickEvent.add( new BEventHandler( this, "menuFileExportUst_Click" ) );
menuFileExportVsq.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportVsq.clickEvent.add( new BEventHandler( this, "menuFileExportVsq_Click" ) );
menuFileExportVxt.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileExportVxt.clickEvent.add( new BEventHandler( this, "menuFileExportVxt_Click" ) );
menuFileRecent.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileRecentClear.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileRecentClear.clickEvent.add( new BEventHandler( this, "menuFileRecentClear_Click" ) );
menuFileQuit.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuFileQuit.clickEvent.add( new BEventHandler( this, "menuFileQuit_Click" ) );
menuEdit.dropDownOpeningEvent.add( new BEventHandler( this, "menuEdit_DropDownOpening" ) );
menuEditUndo.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditUndo.clickEvent.add( new BEventHandler( this, "handleEditUndo_Click" ) );
menuEditRedo.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditRedo.clickEvent.add( new BEventHandler( this, "handleEditRedo_Click" ) );
menuEditCut.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditCut.clickEvent.add( new BEventHandler( this, "handleEditCut_Click" ) );
menuEditCopy.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditCopy.clickEvent.add( new BEventHandler( this, "handleEditCopy_Click" ) );
menuEditPaste.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditPaste.clickEvent.add( new BEventHandler( this, "handleEditPaste_Click" ) );
menuEditDelete.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditDelete.clickEvent.add( new BEventHandler( this, "menuEditDelete_Click" ) );
menuEditAutoNormalizeMode.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditAutoNormalizeMode.clickEvent.add( new BEventHandler( this, "menuEditAutoNormalizeMode_Click" ) );
menuEditSelectAll.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditSelectAll.clickEvent.add( new BEventHandler( this, "menuEditSelectAll_Click" ) );
menuEditSelectAllEvents.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuEditSelectAllEvents.clickEvent.add( new BEventHandler( this, "menuEditSelectAllEvents_Click" ) );
menuVisualOverview.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualControlTrack.checkedChangedEvent.add( new BEventHandler( this, "menuVisualControlTrack_CheckedChanged" ) );
menuVisualControlTrack.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualMixer.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualMixer.clickEvent.add( new BEventHandler( this, "menuVisualMixer_Click" ) );
menuVisualWaveform.checkedChangedEvent.add( new BEventHandler( this, "menuVisualWaveform_CheckedChanged" ) );
menuVisualWaveform.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualProperty.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualProperty.checkedChangedEvent.add( new BEventHandler( this, "menuVisualProperty_CheckedChanged" ) );
menuVisualGridline.checkedChangedEvent.add( new BEventHandler( this, "menuVisualGridline_CheckedChanged" ) );
menuVisualGridline.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualIconPalette.clickEvent.add( new BEventHandler( this, "menuVisualIconPalette_Click" ) );
menuVisualIconPalette.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualStartMarker.clickEvent.add( new BEventHandler( this, "handleStartMarker_Click" ) );
menuVisualStartMarker.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualEndMarker.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualEndMarker.clickEvent.add( new BEventHandler( this, "handleEndMarker_Click" ) );
menuVisualLyrics.checkedChangedEvent.add( new BEventHandler( this, "menuVisualLyrics_CheckedChanged" ) );
menuVisualLyrics.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualNoteProperty.checkedChangedEvent.add( new BEventHandler( this, "menuVisualNoteProperty_CheckedChanged" ) );
menuVisualNoteProperty.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualPitchLine.checkedChangedEvent.add( new BEventHandler( this, "menuVisualPitchLine_CheckedChanged" ) );
menuVisualPitchLine.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualPluginUi.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuVisualPluginUi.dropDownOpeningEvent.add( new BEventHandler( this, "menuVisualPluginUi_DropDownOpening" ) );
menuVisualPluginUiVocaloid1.clickEvent.add( new BEventHandler( this, "menuVisualPluginUiVocaloidCommon_Click" ) );
menuVisualPluginUiVocaloid2.clickEvent.add( new BEventHandler( this, "menuVisualPluginUiVocaloidCommon_Click" ) );
menuVisualPluginUiAquesTone.clickEvent.add( new BEventHandler( this, "menuVisualPluginUiAquesTone_Click" ) );
menuJob.dropDownOpeningEvent.add( new BEventHandler( this, "menuJob_DropDownOpening" ) );
menuJobNormalize.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobNormalize.clickEvent.add( new BEventHandler( this, "menuJobNormalize_Click" ) );
menuJobInsertBar.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobInsertBar.clickEvent.add( new BEventHandler( this, "menuJobInsertBar_Click" ) );
menuJobDeleteBar.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobDeleteBar.clickEvent.add( new BEventHandler( this, "menuJobDeleteBar_Click" ) );
menuJobRandomize.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobRandomize.clickEvent.add( new BEventHandler( this, "menuJobRandomize_Click" ) );
menuJobConnect.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobConnect.clickEvent.add( new BEventHandler( this, "menuJobConnect_Click" ) );
menuJobLyric.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuJobLyric.clickEvent.add( new BEventHandler( this, "menuJobLyric_Click" ) );
menuTrack.dropDownOpeningEvent.add( new BEventHandler( this, "menuTrack_DropDownOpening" ) );
menuTrackOn.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackBgm.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackOn.clickEvent.add( new BEventHandler( this, "handleTrackOn_Click" ) );
menuTrackAdd.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackAdd.clickEvent.add( new BEventHandler( this, "menuTrackAdd_Click" ) );
menuTrackCopy.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackCopy.clickEvent.add( new BEventHandler( this, "menuTrackCopy_Click" ) );
menuTrackChangeName.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackChangeName.clickEvent.add( new BEventHandler( this, "menuTrackChangeName_Click" ) );
menuTrackDelete.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackDelete.clickEvent.add( new BEventHandler( this, "menuTrackDelete_Click" ) );
menuTrackRenderCurrent.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRenderCurrent.clickEvent.add( new BEventHandler( this, "menuTrackRenderCurrent_Click" ) );
menuTrackRenderAll.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRenderAll.clickEvent.add( new BEventHandler( this, "handleTrackRenderAll_Click" ) );
menuTrackOverlay.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackOverlay.clickEvent.add( new BEventHandler( this, "menuTrackOverlay_Click" ) );
menuTrackRenderer.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRenderer.dropDownOpeningEvent.add( new BEventHandler( this, "menuTrackRenderer_DropDownOpening" ) );
menuTrackRendererVOCALOID1.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRendererVOCALOID1.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
menuTrackRendererVOCALOID2.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRendererVOCALOID2.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
menuTrackRendererUtau.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
//UTAUはresamplerを識別するのでmenuTrackRendererUtauのサブアイテムのClickイベントを拾う
//menuTrackRendererUtau.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
menuTrackRendererVCNT.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRendererVCNT.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
menuTrackRendererAquesTone.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuTrackRendererAquesTone.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
menuLyric.dropDownOpeningEvent.add( new BEventHandler( this, "menuLyric_DropDownOpening" ) );
menuLyricCopyVibratoToPreset.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuLyricExpressionProperty.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuLyricExpressionProperty.clickEvent.add( new BEventHandler( this, "menuLyricExpressionProperty_Click" ) );
menuLyricVibratoProperty.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuLyricVibratoProperty.clickEvent.add( new BEventHandler( this, "menuLyricVibratoProperty_Click" ) );
menuLyricPhonemeTransformation.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuLyricDictionary.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuLyricDictionary.clickEvent.add( new BEventHandler( this, "menuLyricDictionary_Click" ) );
menuLyricPhonemeTransformation.clickEvent.add( new BEventHandler( this, "menuLyricPhonemeTransformation_Click" ) );
menuLyricApplyUtauParameters.clickEvent.add( new BEventHandler( this, "menuLyricApplyUtauParameters_Click" ) );
menuScriptUpdate.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuScriptUpdate.clickEvent.add( new BEventHandler( this, "menuScriptUpdate_Click" ) );
menuSettingPreference.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingPreference.clickEvent.add( new BEventHandler( this, "menuSettingPreference_Click" ) );
menuSettingGameControler.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingGameControlerSetting.clickEvent.add( new BEventHandler( this, "menuSettingGameControlerSetting_Click" ) );
menuSettingGameControlerLoad.clickEvent.add( new BEventHandler( this, "menuSettingGameControlerLoad_Click" ) );
menuSettingGameControlerRemove.clickEvent.add( new BEventHandler( this, "menuSettingGameControlerRemove_Click" ) );
menuSettingSequence.clickEvent.add( new BEventHandler( this, "menuSettingSequence_Click" ) );
menuSettingSequence.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingShortcut.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingShortcut.clickEvent.add( new BEventHandler( this, "menuSettingShortcut_Click" ) );
menuSettingVibratoPreset.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingVibratoPreset.clickEvent.add( new BEventHandler( this, "menuSettingVibratoPreset_Click" ) );
menuSettingDefaultSingerStyle.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingDefaultSingerStyle.clickEvent.add( new BEventHandler( this, "menuSettingDefaultSingerStyle_Click" ) );
menuSettingPaletteTool.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingPositionQuantize.mouseEnterEvent.add( new BEventHandler( this, "handleMenuMouseEnter" ) );
menuSettingPositionQuantize04.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantize08.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantize16.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantize32.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantize64.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantize128.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantizeOff.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
menuSettingPositionQuantizeTriplet.clickEvent.add( new BEventHandler( this, "handlePositionQuantizeTriplet_Click" ) );
menuWindowMinimize.clickEvent.add( new BEventHandler( this, "menuWindowMinimize_Click" ) );
menuHelpAbout.clickEvent.add( new BEventHandler( this, "menuHelpAbout_Click" ) );
menuHelpManual.clickEvent.add( new BEventHandler( this, "menuHelpManual_Click" ) );
menuHelpLogSwitch.checkedChangedEvent.add( new BEventHandler( this, "menuHelpLogSwitch_CheckedChanged" ) );
menuHelpLogOpen.clickEvent.add( new BEventHandler( this, "menuHelpLogOpen_Click" ) );
menuHelpDebug.clickEvent.add( new BEventHandler( this, "menuHelpDebug_Click" ) );
menuHiddenEditLyric.clickEvent.add( new BEventHandler( this, "menuHiddenEditLyric_Click" ) );
menuHiddenEditFlipToolPointerPencil.clickEvent.add( new BEventHandler( this, "menuHiddenEditFlipToolPointerPencil_Click" ) );
menuHiddenEditFlipToolPointerEraser.clickEvent.add( new BEventHandler( this, "menuHiddenEditFlipToolPointerEraser_Click" ) );
menuHiddenVisualForwardParameter.clickEvent.add( new BEventHandler( this, "menuHiddenVisualForwardParameter_Click" ) );
menuHiddenVisualBackwardParameter.clickEvent.add( new BEventHandler( this, "menuHiddenVisualBackwardParameter_Click" ) );
menuHiddenTrackNext.clickEvent.add( new BEventHandler( this, "menuHiddenTrackNext_Click" ) );
menuHiddenTrackBack.clickEvent.add( new BEventHandler( this, "menuHiddenTrackBack_Click" ) );
menuHiddenCopy.clickEvent.add( new BEventHandler( this, "handleEditCopy_Click" ) );
menuHiddenPaste.clickEvent.add( new BEventHandler( this, "handleEditPaste_Click" ) );
menuHiddenCut.clickEvent.add( new BEventHandler( this, "handleEditCut_Click" ) );
menuHiddenSelectBackward.clickEvent.add( new BEventHandler( this, "menuHiddenSelectBackward_Click" ) );
menuHiddenSelectForward.clickEvent.add( new BEventHandler( this, "menuHiddenSelectForward_Click" ) );
menuHiddenMoveUp.clickEvent.add( new BEventHandler( this, "menuHiddenMoveUp_Click" ) );
menuHiddenMoveDown.clickEvent.add( new BEventHandler( this, "menuHiddenMoveDown_Click" ) );
menuHiddenMoveLeft.clickEvent.add( new BEventHandler( this, "menuHiddenMoveLeft_Click" ) );
menuHiddenMoveRight.clickEvent.add( new BEventHandler( this, "menuHiddenMoveRight_Click" ) );
menuHiddenLengthen.clickEvent.add( new BEventHandler( this, "menuHiddenLengthen_Click" ) );
menuHiddenShorten.clickEvent.add( new BEventHandler( this, "menuHiddenShorten_Click" ) );
menuHiddenGoToEndMarker.clickEvent.add( new BEventHandler( this, "menuHiddenGoToEndMarker_Click" ) );
menuHiddenGoToStartMarker.clickEvent.add( new BEventHandler( this, "menuHiddenGoToStartMarker_Click" ) );
menuHiddenPlayFromStartMarker.clickEvent.add( new BEventHandler( this, "menuHiddenPlayFromStartMarker_Click" ) );
menuHiddenPrintPoToCSV.clickEvent.add( new BEventHandler( this, "menuHiddenPrintPoToCSV_Click" ) );
menuHiddenFlipCurveOnPianorollMode.clickEvent.add( new BEventHandler( this, "menuHiddenFlipCurveOnPianorollMode_Click" ) );

cMenuPiano.openingEvent.add( new BCancelEventHandler( this, "cMenuPiano_Opening" ) );
cMenuPianoPointer.clickEvent.add( new BEventHandler( this, "cMenuPianoPointer_Click" ) );
cMenuPianoPencil.clickEvent.add( new BEventHandler( this, "cMenuPianoPencil_Click" ) );
cMenuPianoEraser.clickEvent.add( new BEventHandler( this, "cMenuPianoEraser_Click" ) );
cMenuPianoCurve.clickEvent.add( new BEventHandler( this, "cMenuPianoCurve_Click" ) );
cMenuPianoFixed01.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed01_Click" ) );
cMenuPianoFixed02.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed02_Click" ) );
cMenuPianoFixed04.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed04_Click" ) );
cMenuPianoFixed08.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed08_Click" ) );
cMenuPianoFixed16.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed16_Click" ) );
cMenuPianoFixed32.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed32_Click" ) );
cMenuPianoFixed64.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed64_Click" ) );
cMenuPianoFixed128.clickEvent.add( new BEventHandler( this, "cMenuPianoFixed128_Click" ) );
cMenuPianoFixedOff.clickEvent.add( new BEventHandler( this, "cMenuPianoFixedOff_Click" ) );
cMenuPianoFixedTriplet.clickEvent.add( new BEventHandler( this, "cMenuPianoFixedTriplet_Click" ) );
cMenuPianoFixedDotted.clickEvent.add( new BEventHandler( this, "cMenuPianoFixedDotted_Click" ) );
cMenuPianoQuantize04.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantize08.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantize16.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantize32.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantize64.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantize128.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantizeOff.clickEvent.add( new BEventHandler( this, "handlePositionQuantize" ) );
cMenuPianoQuantizeTriplet.clickEvent.add( new BEventHandler( this, "handlePositionQuantizeTriplet_Click" ) );
cMenuPianoGrid.clickEvent.add( new BEventHandler( this, "cMenuPianoGrid_Click" ) );
cMenuPianoUndo.clickEvent.add( new BEventHandler( this, "cMenuPianoUndo_Click" ) );
cMenuPianoRedo.clickEvent.add( new BEventHandler( this, "cMenuPianoRedo_Click" ) );
cMenuPianoCut.clickEvent.add( new BEventHandler( this, "cMenuPianoCut_Click" ) );
cMenuPianoCopy.clickEvent.add( new BEventHandler( this, "cMenuPianoCopy_Click" ) );
cMenuPianoPaste.clickEvent.add( new BEventHandler( this, "cMenuPianoPaste_Click" ) );
cMenuPianoDelete.clickEvent.add( new BEventHandler( this, "cMenuPianoDelete_Click" ) );
cMenuPianoSelectAll.clickEvent.add( new BEventHandler( this, "cMenuPianoSelectAll_Click" ) );
cMenuPianoSelectAllEvents.clickEvent.add( new BEventHandler( this, "cMenuPianoSelectAllEvents_Click" ) );
cMenuPianoImportLyric.clickEvent.add( new BEventHandler( this, "cMenuPianoImportLyric_Click" ) );
cMenuPianoExpressionProperty.clickEvent.add( new BEventHandler( this, "cMenuPianoProperty_Click" ) );
cMenuPianoVibratoProperty.clickEvent.add( new BEventHandler( this, "cMenuPianoVibratoProperty_Click" ) );
cMenuTrackTab.openingEvent.add( new BCancelEventHandler( this, "cMenuTrackTab_Opening" ) );
cMenuTrackTabTrackOn.clickEvent.add( new BEventHandler( this, "handleTrackOn_Click" ) );
cMenuTrackTabAdd.clickEvent.add( new BEventHandler( this, "cMenuTrackTabAdd_Click" ) );
cMenuTrackTabCopy.clickEvent.add( new BEventHandler( this, "cMenuTrackTabCopy_Click" ) );
cMenuTrackTabChangeName.clickEvent.add( new BEventHandler( this, "cMenuTrackTabChangeName_Click" ) );
cMenuTrackTabDelete.clickEvent.add( new BEventHandler( this, "cMenuTrackTabDelete_Click" ) );
cMenuTrackTabRenderCurrent.clickEvent.add( new BEventHandler( this, "cMenuTrackTabRenderCurrent_Click" ) );
cMenuTrackTabRenderAll.clickEvent.add( new BEventHandler( this, "handleTrackRenderAll_Click" ) );
cMenuTrackTabOverlay.clickEvent.add( new BEventHandler( this, "cMenuTrackTabOverlay_Click" ) );
cMenuTrackTabRenderer.dropDownOpeningEvent.add( new BEventHandler( this, "cMenuTrackTabRenderer_DropDownOpening" ) );
cMenuTrackTabRendererVOCALOID1.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
cMenuTrackTabRendererVOCALOID2.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
cMenuTrackTabRendererStraight.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
cMenuTrackTabRendererAquesTone.clickEvent.add( new BEventHandler( this, "handleChangeRenderer" ) );
cMenuTrackSelector.openingEvent.add( new BCancelEventHandler( this, "cMenuTrackSelector_Opening" ) );
cMenuTrackSelectorPointer.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorPointer_Click" ) );
cMenuTrackSelectorPencil.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorPencil_Click" ) );
cMenuTrackSelectorLine.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorLine_Click" ) );
cMenuTrackSelectorEraser.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorEraser_Click" ) );
cMenuTrackSelectorCurve.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorCurve_Click" ) );
cMenuTrackSelectorUndo.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorUndo_Click" ) );
cMenuTrackSelectorRedo.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorRedo_Click" ) );
cMenuTrackSelectorCut.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorCut_Click" ) );
cMenuTrackSelectorCopy.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorCopy_Click" ) );
cMenuTrackSelectorPaste.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorPaste_Click" ) );
cMenuTrackSelectorDelete.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorDelete_Click" ) );
cMenuTrackSelectorDeleteBezier.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorDeleteBezier_Click" ) );
cMenuTrackSelectorSelectAll.clickEvent.add( new BEventHandler( this, "cMenuTrackSelectorSelectAll_Click" ) );
cMenuPositionIndicatorEndMarker.clickEvent.add( new BEventHandler( this, "cMenuPositionIndicatorEndMarker_Click" ) );
cMenuPositionIndicatorStartMarker.clickEvent.add( new BEventHandler( this, "cMenuPositionIndicatorStartMarker_Click" ) );
trackBar.valueChangedEvent.add( new BEventHandler( this, "trackBar_ValueChanged" ) );
trackBar.enterEvent.add( new BEventHandler( this, "trackBar_Enter" ) );
bgWorkScreen.doWorkEvent.add( new BDoWorkEventHandler( this, "bgWorkScreen_DoWork" ) );
timer.tickEvent.add( new BEventHandler( this, "timer_Tick" ) );
pictKeyLengthSplitter.mouseMoveEvent.add( new BMouseEventHandler( this, "pictKeyLengthSplitter_MouseMove" ) );
pictKeyLengthSplitter.mouseDownEvent.add( new BMouseEventHandler( this, "pictKeyLengthSplitter_MouseDown" ) );
pictKeyLengthSplitter.mouseUpEvent.add( new BMouseEventHandler( this, "pictKeyLengthSplitter_MouseUp" ) );
panelOverview.keyUpEvent.add( new BKeyEventHandler( this, "handleSpaceKeyUp" ) );
panelOverview.keyDownEvent.add( new BKeyEventHandler( this, "handleSpaceKeyDown" ) );
vScroll.valueChangedEvent.add( new BEventHandler( this, "vScroll_ValueChanged" ) );
//this.resizeEvent.add( new BEventHandler( this, "handleVScrollResize" ) );
pictPianoRoll.resizeEvent.add( new BEventHandler( this, "handleVScrollResize" ) );
vScroll.enterEvent.add( new BEventHandler( this, "vScroll_Enter" ) );
hScroll.valueChangedEvent.add( new BEventHandler( this, "hScroll_ValueChanged" ) );
hScroll.resizeEvent.add( new BEventHandler( this, "hScroll_Resize" ) );
hScroll.enterEvent.add( new BEventHandler( this, "hScroll_Enter" ) );
picturePositionIndicator.previewKeyDownEvent.add( new BPreviewKeyDownEventHandler( this, "picturePositionIndicator_PreviewKeyDown" ) );
picturePositionIndicator.mouseMoveEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseMove" ) );
picturePositionIndicator.mouseClickEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseClick" ) );
picturePositionIndicator.mouseDoubleClickEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseDoubleClick" ) );
picturePositionIndicator.mouseDownEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseDown" ) );
picturePositionIndicator.mouseUpEvent.add( new BMouseEventHandler( this, "picturePositionIndicator_MouseUp" ) );
picturePositionIndicator.paintEvent.add( new BPaintEventHandler( this, "picturePositionIndicator_Paint" ) );
pictPianoRoll.previewKeyDownEvent.add( new BPreviewKeyDownEventHandler( this, "pictPianoRoll_PreviewKeyDown" ) );
pictPianoRoll.keyUpEvent.add( new BKeyEventHandler( this, "handleSpaceKeyUp" ) );
pictPianoRoll.keyUpEvent.add( new BKeyEventHandler( this, "pictPianoRoll_KeyUp" ) );
pictPianoRoll.mouseMoveEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseMove" ) );
pictPianoRoll.mouseDoubleClickEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseDoubleClick" ) );
pictPianoRoll.mouseClickEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseClick" ) );
pictPianoRoll.mouseDownEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseDown" ) );
pictPianoRoll.mouseUpEvent.add( new BMouseEventHandler( this, "pictPianoRoll_MouseUp" ) );
pictPianoRoll.keyDownEvent.add( new BKeyEventHandler( this, "handleSpaceKeyDown" ) );
waveView.mouseDoubleClickEvent.add( new BMouseEventHandler( this, "waveView_MouseDoubleClick" ) );
waveView.mouseDownEvent.add( new BMouseEventHandler( this, "waveView_MouseDown" ) );
waveView.mouseUpEvent.add( new BMouseEventHandler( this, "waveView_MouseUp" ) );
waveView.mouseMoveEvent.add( new BMouseEventHandler( this, "waveView_MouseMove" ) );

buttonVZoom.clickEvent.add( new BEventHandler( this, "buttonVZoom_Click" ) );
buttonVMooz.clickEvent.add( new BEventHandler( this, "buttonVMooz_Click" ) );
stripBtnFileNew.clickEvent.add( new BEventHandler( this, "handleFileNew_Click" ) );
stripBtnFileNew.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnFileOpen.clickEvent.add( new BEventHandler( this, "handleFileOpen_Click" ) );
stripBtnFileOpen.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnFileSave.clickEvent.add( new BEventHandler( this, "handleFileSave_Click" ) );
stripBtnFileSave.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnCut.clickEvent.add( new BEventHandler( this, "handleEditCut_Click" ) );
stripBtnCut.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnCopy.clickEvent.add( new BEventHandler( this, "handleEditCopy_Click" ) );
stripBtnCopy.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnPaste.clickEvent.add( new BEventHandler( this, "handleEditPaste_Click" ) );
stripBtnPaste.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnUndo.clickEvent.add( new BEventHandler( this, "handleEditUndo_Click" ) );
stripBtnUndo.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnRedo.clickEvent.add( new BEventHandler( this, "handleEditRedo_Click" ) );
stripBtnRedo.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );

stripBtnMoveTop.clickEvent.add( new BEventHandler( this, "stripBtnMoveTop_Click" ) );
stripBtnMoveTop.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnRewind.clickEvent.add( new BEventHandler( this, "stripBtnRewind_Click" ) );
stripBtnRewind.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnForward.clickEvent.add( new BEventHandler( this, "stripBtnForward_Click" ) );
stripBtnForward.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnMoveEnd.clickEvent.add( new BEventHandler( this, "stripBtnMoveEnd_Click" ) );
stripBtnMoveEnd.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnPlay.clickEvent.add( new BEventHandler( this, "stripBtnPlay_Click" ) );
stripBtnPlay.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnScroll.checkedChangedEvent.add( new BEventHandler( this, "stripBtnScroll_CheckedChanged" ) );
stripBtnScroll.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnLoop.checkedChangedEvent.add( new BEventHandler( this, "stripBtnLoop_CheckedChanged" ) );
stripBtnLoop.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );

stripBtnPointer.clickEvent.add( new BEventHandler( this, "stripBtnArrow_Click" ) );
stripBtnPointer.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnPencil.clickEvent.add( new BEventHandler( this, "stripBtnPencil_Click" ) );
stripBtnPencil.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnLine.clickEvent.add( new BEventHandler( this, "stripBtnLine_Click" ) );
stripBtnLine.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnEraser.clickEvent.add( new BEventHandler( this, "stripBtnEraser_Click" ) );
stripBtnEraser.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnGrid.clickEvent.add( new BEventHandler( this, "stripBtnGrid_Click" ) );
stripBtnGrid.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnCurve.clickEvent.add( new BEventHandler( this, "stripBtnCurve_Click" ) );
stripBtnCurve.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnStepSequencer.enterEvent.add( new BEventHandler( this, "handleStripButton_Enter" ) );
stripBtnStepSequencer.checkedChangedEvent.add( new BEventHandler( this, "stripBtnStepSequencer_CheckedChanged" ) );
this.deactivateEvent.add( new BEventHandler( this, "FormMain_Deactivate" ) );
this.activatedEvent.add( new BEventHandler( this, "FormMain_Activated" ) );
this.formClosedEvent.add( new BFormClosedEventHandler( this, "FormMain_FormClosed" ) );
this.formClosingEvent.add( new BFormClosingEventHandler( this, "FormMain_FormClosing" ) );
this.previewKeyDownEvent.add( new BPreviewKeyDownEventHandler( this, "FormMain_PreviewKeyDown" ) );
panelOverview.enterEvent.add( new BEventHandler( this, "panelOverview_Enter" ) );
        }

        public void setResources()
        {
try {

    stripBtnStepSequencer.setIcon( new ImageIcon( Resources.get_piano() ) );
    stripBtnFileNew.setIcon( new ImageIcon( Resources.get_disk__plus() ) );
    stripBtnFileOpen.setIcon( new ImageIcon( Resources.get_folder_horizontal_open() ) );
    stripBtnFileSave.setIcon( new ImageIcon( Resources.get_disk() ) );
    stripBtnCut.setIcon( new ImageIcon( Resources.get_scissors() ) );
    stripBtnCopy.setIcon( new ImageIcon( Resources.get_documents() ) );
    stripBtnPaste.setIcon( new ImageIcon( Resources.get_clipboard_paste() ) );
    stripBtnUndo.setIcon( new ImageIcon( Resources.get_arrow_skip_180() ) );
    stripBtnRedo.setIcon( new ImageIcon( Resources.get_arrow_skip() ) );

    stripBtnMoveTop.setIcon( new ImageIcon( Resources.get_control_stop_180() ) );
    stripBtnRewind.setIcon( new ImageIcon( Resources.get_control_double_180() ) );
    stripBtnForward.setIcon( new ImageIcon( Resources.get_control_double() ) );
    stripBtnMoveEnd.setIcon( new ImageIcon( Resources.get_control_stop() ) );
    stripBtnPlay.setIcon( new ImageIcon( Resources.get_control() ) );
    stripBtnScroll.setIcon( new ImageIcon( Resources.get_arrow_circle_double() ) );
    stripBtnLoop.setIcon( new ImageIcon( Resources.get_arrow_return() ) );

    stripBtnPointer.setIcon( new ImageIcon( Resources.get_arrow_135() ) );
    stripBtnPencil.setIcon( new ImageIcon( Resources.get_pencil() ) );
    stripBtnLine.setIcon( new ImageIcon( Resources.get_layer_shape_line() ) );
    stripBtnEraser.setIcon( new ImageIcon( Resources.get_eraser() ) );
    stripBtnGrid.setIcon( new ImageIcon( Resources.get_ruler_crop() ) );
    stripBtnCurve.setIcon( new ImageIcon( Resources.get_layer_shape_curve() ) );

    buttonVZoom.setIcon( new ImageIcon( Resources.get_plus8x8() ) );
    buttonVMooz.setIcon( new ImageIcon( Resources.get_minus8x8() ) );
    setIconImage( Resources.get_icon() );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".setResources; ex=" + ex + "\n" );
    serr.println( "FormMain#setResources; ex=" + ex );
}
        }

        public void menuWindowMinimize_Click( Object sender, BEventArgs e )
        {
int state = this.getExtendedState();
if( state != BForm.ICONIFIED ){
    setExtendedState( BForm.ICONIFIED );
}
        }
        
        //BOOKMARK: panelOverview
        public void panelOverview_Enter( Object sender, BEventArgs e )
        {
controller.navigationPanelGotFocus();
        }

        //BOOKMARK: inputTextBox
        public void mInputTextBox_KeyDown( Object sender, BKeyEventArgs e )
        {
int keycode = e.getKeyCode();
int modifiers = e.getModifiers();
boolean shift = (modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
boolean tab = keycode == KeyEvent.VK_TAB;
boolean enter = keycode == KeyEvent.VK_ENTER; 
if ( tab || enter ) {
    executeLyricChangeCommand();
    int selected = AppManager.getSelected();
    int index = -1;
    int width = pictPianoRoll.getWidth();
    int height = pictPianoRoll.getHeight();
    int key_width = AppManager.keyWidth;
    VsqTrack track = AppManager.getVsqFile().Track.get( selected );
    track.sortEvent();
    if( tab ) {
        int clock = 0;
        int search_index = AppManager.itemSelection.getLastEvent().original.InternalID;
        int c = track.getEventCount();
        for ( int i = 0; i < c; i++ ) {
            VsqEvent item = track.getEvent( i );
            if ( item.InternalID == search_index ) {
                index = i;
                clock = item.Clock;
                break;
            }
        }
        if( shift ) {
            // 1個前の音符イベントを検索
            int tindex = -1;
            for ( int i = track.getEventCount() - 1; i >= 0; i-- ) {
                VsqEvent ve = track.getEvent( i );
                if ( ve.ID.type == VsqIDType.Anote && i != index && ve.Clock <= clock ) {
                    tindex = i;
                    break;
                }
            }
            index = tindex;
        } else {
            // 1個後の音符イベントを検索
            int tindex = -1;
            int c2 = track.getEventCount();
            for ( int i = 0; i < c2; i++ ) {
                VsqEvent ve = track.getEvent( i );
                if ( ve.ID.type == VsqIDType.Anote && i != index && ve.Clock >= clock ) {
                    tindex = i;
                    break;
                }
            }
            index = tindex;
        }
    }
    if ( 0 <= index && index < track.getEventCount() ) {
        AppManager.itemSelection.clearEvent();
        VsqEvent item = track.getEvent( index );
        AppManager.itemSelection.addEvent( item.InternalID );
        int x = AppManager.xCoordFromClocks( item.Clock );
        int y = AppManager.yCoordFromNote( item.ID.Note );
        boolean phonetic_symbol_edit_mode = AppManager.mInputTextBox.isPhoneticSymbolEditMode();
        showInputTextBox(
            item.ID.LyricHandle.L0.Phrase,
            item.ID.LyricHandle.L0.getPhoneticSymbol(),
            new Point( x, y ),
            phonetic_symbol_edit_mode );
        int clWidth = (int)(AppManager.mInputTextBox.getWidth() * controller.getScaleXInv());

        // 画面上にAppManager.mInputTextBoxが見えるように，移動
        int SPACE = 20;
        // vScrollやhScrollをいじった場合はfalseにする．
        boolean refresh_screen = true;
        // X軸方向について，見えるように移動
        if ( x < key_width || width < x + AppManager.mInputTextBox.getWidth() ) {
            int clock, clock_x;
            if ( x < key_width ) {
                // 左に隠れてしまう場合
                clock = item.Clock;
            } else {
                // 右に隠れてしまう場合
                clock = item.Clock + clWidth;
            }
            if( shift ){
                // 左方向に移動していた場合
                // 右から３分の１の位置に移動させる
                clock_x = width - (width - key_width) / 3;
            }else{
                // 右方向に移動していた場合
                clock_x = key_width + (width - key_width) / 3;
            }
            double draft_d = (key_width + AppManager.keyOffset - clock_x) * controller.getScaleXInv() + clock;
            if ( draft_d < 0.0 ) {
                draft_d = 0.0;
            }
            int draft = (int)draft_d;
            if ( draft < hScroll.getMinimum() ) {
                draft = hScroll.getMinimum();
            } else if ( hScroll.getMaximum() < draft ) {
                draft = hScroll.getMaximum();
            }
            refresh_screen = false;
            hScroll.setValue( draft );
        }
        // y軸方向について，見えるように移動
        int track_height = (int)(100 * controller.getScaleY());
        if( y <= 0 || height - track_height  <= y ){
            int note = item.ID.Note;
            if( y <= 0 ){
                // 上にはみ出してしまう場合
                note = item.ID.Note + 1;
            }else{
                // 下にはみ出してしまう場合
                note = item.ID.Note - 2;
            }
            if( 127 < note ){
                note = 127;
            }
            if( note < 0 ){
                note = 0;
            }
            ensureVisibleY( note );
        }
        if ( refresh_screen ) {
            refreshScreen();
        }
    } else {
        int id = AppManager.itemSelection.getLastEvent().original.InternalID;
        AppManager.itemSelection.clearEvent();
        AppManager.itemSelection.addEvent( id );
        hideInputTextBox();
    }
}
        }

        public void mInputTextBox_KeyUp( Object sender, BKeyEventArgs e )
        {
boolean flip = (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) && ((e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK);
boolean hide = (e.getKeyCode() == KeyEvent.VK_ESCAPE);

if ( flip ) {
    if ( AppManager.mInputTextBox.isVisible() ) {
        flipInputTextBoxMode();
    }
} else if ( hide ) {
    hideInputTextBox();
}
        }

        public void mInputTextBox_ImeModeChanged( Object sender, BEventArgs e )
        {
mLastIsImeModeOn = AppManager.mInputTextBox.isImeModeOn();
        }

        public void mInputTextBox_KeyPress( Object sender, BKeyPressEventArgs e )
        {
        }

        //BOOKMARK: AppManager
        public void AppManager_EditedStateChanged( Object sender, boolean edited )
        {
setEdited( edited );
        }

        public void AppManager_GridVisibleChanged( Object sender, BEventArgs e )
        {
menuVisualGridline.setSelected( AppManager.isGridVisible() );
stripBtnGrid.setSelected( AppManager.isGridVisible() );
cMenuPianoGrid.setSelected( AppManager.isGridVisible() );
        }

        public void AppManager_MainWindowFocusRequired( Object sender, BEventArgs e )
        {
this.requestFocus();
        }

        public void AppManager_PreviewAborted( Object sender, BEventArgs e )
        {
stripBtnPlay.setIcon( new ImageIcon( Resources.get_control() ) );
stripBtnPlay.setText( _( "Play" ) );
timer.stop();

for ( int i = 0; i < AppManager.mDrawStartIndex.length; i++ ) {
    AppManager.mDrawStartIndex[i] = 0;
}
//MidiPlayer.stop();
        }

        public void AppManager_PreviewStarted( Object sender, BEventArgs e )
        {
AppManager.mAddingEvent = null;
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
RendererKind renderer = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
int clock = AppManager.getCurrentClock();
mLastClock = clock;
double now = PortUtil.getCurrentTime();
AppManager.mPreviewStartedTime = now;
timer.start();
stripBtnPlay.setIcon( new ImageIcon( Resources.get_control_pause() ) );
stripBtnPlay.setText( _( "Stop" ) );
        }

        public void AppManager_SelectedToolChanged( Object sender, BEventArgs e )
        {
applySelectedTool();
        }

        public void ItemSelectionModel_SelectedEventChanged( Object sender, boolean selected_event_is_null )
        {
menuEditCut.setEnabled( !selected_event_is_null );
menuEditPaste.setEnabled( !selected_event_is_null );
menuEditDelete.setEnabled( !selected_event_is_null );
cMenuPianoCut.setEnabled( !selected_event_is_null );
cMenuPianoCopy.setEnabled( !selected_event_is_null );
cMenuPianoDelete.setEnabled( !selected_event_is_null );
cMenuPianoExpressionProperty.setEnabled( !selected_event_is_null );
menuLyricVibratoProperty.setEnabled( !selected_event_is_null );
menuLyricExpressionProperty.setEnabled( !selected_event_is_null );
stripBtnCut.setEnabled( !selected_event_is_null );
stripBtnCopy.setEnabled( !selected_event_is_null );
        }

        public void AppManager_UpdateBgmStatusRequired( Object sender, BEventArgs e )
        {
updateBgmMenuState();
        }

        public void AppManager_WaveViewRealoadRequired( Object sender, WaveViewRealoadRequiredEventArgs arg )
        {
int track = arg.track;
String file = arg.file;
double sec_start = arg.secStart;
double sec_end = arg.secEnd;
if ( sec_start <= sec_end ) {
    waveView.reloadPartial( track - 1, file, sec_start, sec_end );
} else {
    waveView.load( track - 1, file );
}
        }

        //BOOKMARK: pictPianoRoll
        public void pictPianoRoll_KeyUp( Object sender, BKeyEventArgs e )
        {
processSpecialShortcutKey( e, false );
        }

        public void pictPianoRoll_MouseClick( Object sender, BMouseEventArgs e )
        {
int modefiers = PortUtil.getCurrentModifierKey();
EditMode edit_mode = AppManager.getEditMode();

boolean is_button_left = e.Button == BMouseButtons.Left;
int selected = AppManager.getSelected();

if ( e.Button == BMouseButtons.Left ) {

    // クリック位置にIDが無いかどうかを検査
    ByRef<Rectangle> out_id_rect = new ByRef<Rectangle>( new Rectangle() );
    VsqEvent item = getItemAtClickedPosition( new Point( e.X, e.Y ), out_id_rect );
    Rectangle id_rect = out_id_rect.value;
    if ( item != null &&
         edit_mode != EditMode.MOVE_ENTRY_WAIT_MOVE &&
         edit_mode != EditMode.MOVE_ENTRY &&
         edit_mode != EditMode.MOVE_ENTRY_WHOLE_WAIT_MOVE &&
         edit_mode != EditMode.MOVE_ENTRY_WHOLE &&
         edit_mode != EditMode.EDIT_LEFT_EDGE &&
         edit_mode != EditMode.EDIT_RIGHT_EDGE &&
         edit_mode != EditMode.MIDDLE_DRAG &&
         edit_mode != EditMode.CURVE_ON_PIANOROLL ) {
        if ( (modefiers & InputEvent.SHIFT_MASK) != InputEvent.SHIFT_MASK && (modefiers & s_modifier_key) != s_modifier_key ) {
            AppManager.itemSelection.clearEvent();
        }
        AppManager.itemSelection.addEvent( item.InternalID );
        int internal_id = item.InternalID;
        hideInputTextBox();
        if ( AppManager.getSelectedTool() == EditTool.ERASER ) {
            CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandEventDelete( selected, internal_id ) );
            AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
            setEdited( true );
            AppManager.itemSelection.clearEvent();
            return;
        }
    } else {
        if ( edit_mode != EditMode.MOVE_ENTRY_WAIT_MOVE &&
             edit_mode != EditMode.MOVE_ENTRY &&
             edit_mode != EditMode.MOVE_ENTRY_WHOLE_WAIT_MOVE &&
             edit_mode != EditMode.MOVE_ENTRY_WHOLE &&
             edit_mode != EditMode.EDIT_LEFT_EDGE &&
             edit_mode != EditMode.EDIT_RIGHT_EDGE &&
             edit_mode != EditMode.EDIT_VIBRATO_DELAY ) {
            if ( !AppManager.mIsPointerDowned ) {
                AppManager.itemSelection.clearEvent();
            }
            hideInputTextBox();
        }
        if ( AppManager.getSelectedTool() == EditTool.ERASER ) {
            // マウス位置にビブラートの波波があったら削除する
            int stdx = controller.getStartToDrawX();
            int stdy = controller.getStartToDrawY();
            for ( int i = 0; i < AppManager.mDrawObjects.get( selected - 1 ).size(); i++ ) {
                DrawObject dobj = AppManager.mDrawObjects.get( selected - 1 ).get( i );
                if ( dobj.mRectangleInPixel.x + controller.getStartToDrawX() + dobj.mRectangleInPixel.width - stdx < 0 ) {
                    continue;
                } else if ( pictPianoRoll.getWidth() < dobj.mRectangleInPixel.x + AppManager.keyWidth - stdx ) {
                    break;
                }
                Rectangle rc = new Rectangle( dobj.mRectangleInPixel.x + AppManager.keyWidth + dobj.mVibratoDelayInPixel - stdx,
                                              dobj.mRectangleInPixel.y + (int)(100 * controller.getScaleY()) - stdy,
                                              dobj.mRectangleInPixel.width - dobj.mVibratoDelayInPixel,
                                              (int)(100 * controller.getScaleY()) );
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    //ビブラートの範囲なのでビブラートを消す
                    VsqEvent item3 = null;
                    VsqID item2 = null;
                    int internal_id = -1;
                    internal_id = dobj.mInternalID;
                    for ( Iterator<VsqEvent> itr = AppManager.getVsqFile().Track.get( selected ).getNoteEventIterator(); itr.hasNext(); ) {
                        VsqEvent ve = itr.next();
                        if ( ve.InternalID == dobj.mInternalID ) {
                            item2 = (VsqID)ve.ID.clone();
                            item3 = ve;
                            break;
                        }
                    }
                    if ( item2 != null ) {
                        item2.VibratoHandle = null;
                        CadenciiCommand run = new CadenciiCommand(
                            VsqCommand.generateCommandEventChangeIDContaints( selected,
                                                                              internal_id,
                                                                              item2 ) );
                        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
                        setEdited( true );
                    }
                    break;
                }
            }
        }
    }
} else if ( e.Button == BMouseButtons.Right ) {
    boolean show_context_menu = (e.X > AppManager.keyWidth);
    show_context_menu = AppManager.showContextMenuWhenRightClickedOnPianoroll ? (show_context_menu && !mMouseMoved) : false;
    if ( show_context_menu ) {
        ByRef<Rectangle> out_id_rect = new ByRef<Rectangle>();
        VsqEvent item = getItemAtClickedPosition( new Point( e.X, e.Y ), out_id_rect );
        Rectangle id_rect = out_id_rect.value;
        if ( item != null ) {
            if ( !AppManager.itemSelection.isEventContains( AppManager.getSelected(), item.InternalID ) ) {
                AppManager.itemSelection.clearEvent();
            }
            AppManager.itemSelection.addEvent( item.InternalID );
        }
        boolean item_is_null = (item == null);
        cMenuPianoCopy.setEnabled( !item_is_null );
        cMenuPianoCut.setEnabled( !item_is_null );
        cMenuPianoDelete.setEnabled( !item_is_null );
        cMenuPianoImportLyric.setEnabled( !item_is_null );
        cMenuPianoExpressionProperty.setEnabled( !item_is_null );

        int clock = AppManager.clockFromXCoord( e.X );
        cMenuPianoPaste.setEnabled( ((AppManager.clipboard.getCopiedItems().events.size() != 0) && (clock >= AppManager.getVsqFile().getPreMeasureClocks())) );
        refreshScreen();

        mContextMenuOpenedPosition = new Point( e.X, e.Y );
        cMenuPiano.show( pictPianoRoll, e.X, e.Y );
    } else {
        ByRef<Rectangle> out_id_rect = new ByRef<Rectangle>();
        VsqEvent item = getItemAtClickedPosition( mButtonInitial, out_id_rect );
        Rectangle id_rect = out_id_rect.value;
        if ( item != null ) {
            int itemx = AppManager.xCoordFromClocks( item.Clock );
            int itemy = AppManager.yCoordFromNote( item.ID.Note );
        }
    }
} else if ( e.Button == BMouseButtons.Middle ) {
}
        }

        public void pictPianoRoll_MouseDoubleClick( Object sender, BMouseEventArgs e )
        {
ByRef<Rectangle> out_rect = new ByRef<Rectangle>();
VsqEvent item = getItemAtClickedPosition( new Point( e.X, e.Y ), out_rect );
Rectangle rect = out_rect.value;
int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
if ( item != null && item.ID.type == VsqIDType.Anote ) {

    {
        AppManager.itemSelection.clearEvent();
        AppManager.itemSelection.addEvent( item.InternalID );
        if ( !AppManager.editorConfig.KeepLyricInputMode ) {
            mLastSymbolEditMode = false;
        }
        showInputTextBox(
            item.ID.LyricHandle.L0.Phrase,
            item.ID.LyricHandle.L0.getPhoneticSymbol(),
            new Point( rect.x, rect.y ),
            mLastSymbolEditMode );
        refreshScreen();
        return;
    }
} else {
    AppManager.itemSelection.clearEvent();
    hideInputTextBox();
    if ( AppManager.editorConfig.ShowExpLine && AppManager.keyWidth <= e.X ) {
        int stdx = controller.getStartToDrawX();
        int stdy = controller.getStartToDrawY();
        for ( Iterator<DrawObject> itr = AppManager.mDrawObjects.get( selected - 1 ).iterator(); itr.hasNext(); ) {
            DrawObject dobj = itr.next();
            // 表情コントロールプロパティを表示するかどうかを決める
            rect = new Rectangle(
                dobj.mRectangleInPixel.x + AppManager.keyWidth - stdx,
                dobj.mRectangleInPixel.y - stdy + (int)(100 * controller.getScaleY()),
                21,
                (int)(100 * controller.getScaleY()) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), rect ) ) {
                VsqEvent selectedEvent = null;
                for ( Iterator<VsqEvent> itr2 = vsq.Track.get( selected ).getEventIterator(); itr2.hasNext(); ) {
                    VsqEvent ev = itr2.next();
                    if ( ev.InternalID == dobj.mInternalID ) {
                        selectedEvent = ev;
                        break;
                    }
                }
                if ( selectedEvent != null ) {
                    SynthesizerType type = SynthesizerType.VOCALOID2;
                    RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
                    if ( kind == RendererKind.VOCALOID1 ) {
                        type = SynthesizerType.VOCALOID1;
                    }
                    FormNoteExpressionConfig dlg = null;
                    try {
                        dlg = new FormNoteExpressionConfig( type, selectedEvent.ID.NoteHeadHandle );
                        dlg.setPMBendDepth( selectedEvent.ID.PMBendDepth );
                        dlg.setPMBendLength( selectedEvent.ID.PMBendLength );
                        dlg.setPMbPortamentoUse( selectedEvent.ID.PMbPortamentoUse );
                        dlg.setDEMdecGainRate( selectedEvent.ID.DEMdecGainRate );
                        dlg.setDEMaccent( selectedEvent.ID.DEMaccent );
                        dlg.setLocation( getFormPreferedLocation( dlg ) );
                        BDialogResult dr = AppManager.showModalDialog( dlg, this );
                        if ( dr == BDialogResult.OK ) {
                            VsqID id = (VsqID)selectedEvent.ID.clone();
                            id.PMBendDepth = dlg.getPMBendDepth();
                            id.PMBendLength = dlg.getPMBendLength();
                            id.PMbPortamentoUse = dlg.getPMbPortamentoUse();
                            id.DEMdecGainRate = dlg.getDEMdecGainRate();
                            id.DEMaccent = dlg.getDEMaccent();
                            id.NoteHeadHandle = dlg.getEditedNoteHeadHandle();
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandEventChangeIDContaints( selected, selectedEvent.InternalID, id ) );
                            AppManager.editHistory.register( vsq.executeCommand( run ) );
                            setEdited( true );
                            refreshScreen();
                        }
                    } catch ( Exception ex ) {
                        Logger.write( FormMain.class + ".pictPianoRoll_MouseDoubleClick; ex=" + ex + "\n" );
                        serr.println( FormMain.class + ".pictPianoRoll_MouseDoubleClick" + ex );
                    } finally {
                        if ( dlg != null ) {
                            try {
                                dlg.close();
                            } catch ( Exception ex2 ) {
                                Logger.write( FormMain.class + ".pictPianoRoll_MouseDoubleClick; ex=" + ex2 + "\n" );
                                serr.println( FormMain.class + ".pictPianoRoll_MouseDoubleClick" );
                            }
                        }
                    }
                    return;
                }
                break;
            }

            // ビブラートプロパティダイアログを表示するかどうかを決める
            rect = new Rectangle(
                dobj.mRectangleInPixel.x + AppManager.keyWidth - stdx + 21,
                dobj.mRectangleInPixel.y - stdy + (int)(100 * controller.getScaleY()),
                dobj.mRectangleInPixel.width - 21,
                (int)(100 * controller.getScaleY()) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), rect ) ) {
                VsqEvent selectedEvent = null;
                for ( Iterator<VsqEvent> itr2 = vsq.Track.get( selected ).getEventIterator(); itr2.hasNext(); ) {
                    VsqEvent ev = itr2.next();
                    if ( ev.InternalID == dobj.mInternalID ) {
                        selectedEvent = ev;
                        break;
                    }
                }
                if ( selectedEvent != null ) {
                    SynthesizerType type = SynthesizerType.VOCALOID2;
                    RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
                    if ( kind == RendererKind.VOCALOID1 ) {
                        type = SynthesizerType.VOCALOID1;
                    }
                    FormVibratoConfig dlg = null;
                    try {
                        dlg = new FormVibratoConfig(
                            selectedEvent.ID.VibratoHandle,
                            selectedEvent.ID.getLength(),
                            AppManager.editorConfig.DefaultVibratoLength,
                            type,
                            AppManager.editorConfig.UseUserDefinedAutoVibratoType );
                        dlg.setLocation( getFormPreferedLocation( dlg ) );
                        BDialogResult dr = AppManager.showModalDialog( dlg, this );
                        if ( dr == BDialogResult.OK ) {
                            VsqID t = (VsqID)selectedEvent.ID.clone();
                            VibratoHandle handle = dlg.getVibratoHandle();
                            if ( handle != null ) {
                                String iconid = handle.IconID;
                                int vibrato_length = handle.getLength();
                                int note_length = selectedEvent.ID.getLength();
                                t.VibratoDelay = note_length - vibrato_length;
                                t.VibratoHandle = handle;
                            } else {
                                t.VibratoHandle = null;
                            }
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandEventChangeIDContaints( 
                                    selected,
                                    selectedEvent.InternalID,
                                    t ) );
                            AppManager.editHistory.register( vsq.executeCommand( run ) );
                            setEdited( true );
                            refreshScreen();
                        }
                    } catch ( Exception ex ) {
                        Logger.write( FormMain.class + ".pictPianoRoll_MouseDoubleClick; ex=" + ex + "\n" );
                    } finally {
                        if ( dlg != null ) {
                            try {
                                dlg.close();
                            } catch ( Exception ex2 ) {
                                Logger.write( FormMain.class + ".pictPianoRoll_MouseDoubleClick; ex=" + ex2 + "\n" );
                            }
                        }
                    }
                    return;
                }
                break;
            }

        }
    }
}

if ( e.Button == BMouseButtons.Left ) {
    // 必要な操作が何も無ければ，クリック位置にソングポジションを移動
    if ( AppManager.keyWidth < e.X ) {
        int clock = doQuantize( AppManager.clockFromXCoord( e.X ), AppManager.getPositionQuantizeClock() );
        AppManager.setCurrentClock( clock );
    }
} else if ( e.Button == BMouseButtons.Middle ) {
    // ツールをポインター <--> 鉛筆に切り替える
    if ( AppManager.keyWidth < e.X ) {
        if ( AppManager.getSelectedTool() == EditTool.ARROW ) {
            AppManager.setSelectedTool( EditTool.PENCIL );
        } else {
            AppManager.setSelectedTool( EditTool.ARROW );
        }
    }
}
        }

        public void pictPianoRoll_MouseDown( Object sender, BMouseEventArgs e0 )
        {
BMouseButtons btn0 = e0.Button;
if ( isMouseMiddleButtonDowned( btn0 ) ) {
    btn0 = BMouseButtons.Middle;
}
BMouseEventArgs e = new BMouseEventArgs( btn0, e0.Clicks, e0.X, e0.Y, e0.Delta );

mMouseMoved = false;
if ( !AppManager.isPlaying() && 0 <= e.X && e.X <= AppManager.keyWidth ) {
    int note = AppManager.noteFromYCoord( e.Y );
    if ( 0 <= note && note <= 126 ) {
        if ( e.Button == BMouseButtons.Left ) {
            KeySoundPlayer.play( note );
        }
        return;
    }
}

AppManager.itemSelection.clearTempo();
AppManager.itemSelection.clearTimesig();
AppManager.itemSelection.clearPoint();
/*if ( e.Button == BMouseButtons.Left ) {
    AppManager.selectedRegionEnabled = false;
}*/

mMouseDowned = true;
mButtonInitial = new Point( e.X, e.Y );
int modefier = PortUtil.getCurrentModifierKey();

EditTool selected_tool = AppManager.getSelectedTool();
if ( e.Button == BMouseButtons.Middle ) {
    AppManager.setEditMode( EditMode.MIDDLE_DRAG );
    mMiddleButtonVScroll = vScroll.getValue();
    mMiddleButtonHScroll = hScroll.getValue();
    return;
}

int stdx = controller.getStartToDrawX();
int stdy = controller.getStartToDrawY();
if ( e.Button == BMouseButtons.Left && AppManager.mCurveOnPianoroll && (selected_tool == EditTool.PENCIL || selected_tool == EditTool.LINE) ) {
    pictPianoRoll.mMouseTracer.clear();
    pictPianoRoll.mMouseTracer.appendFirst( e.X + stdx, e.Y + stdy );
    setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
    AppManager.setEditMode( EditMode.CURVE_ON_PIANOROLL );
    return;
}

ByRef<Rectangle> out_rect = new ByRef<Rectangle>();
VsqEvent item = getItemAtClickedPosition( new Point( e.X, e.Y ), out_rect );
Rectangle rect = out_rect.value;


int selected = AppManager.getSelected();
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );
int key_width = AppManager.keyWidth;

// マウス位置にある音符を検索
if ( item == null ) {
    if ( e.Button == BMouseButtons.Left ) {
        AppManager.setWholeSelectedIntervalEnabled( false );
    }
    if ( AppManager.itemSelection.getLastEvent() != null ) {
        executeLyricChangeCommand();
    }
    boolean start_mouse_hover_generator = true;

    // CTRLキーを押しながら範囲選択
    if ( (modefier & s_modifier_key) == s_modifier_key ) {
        AppManager.setWholeSelectedIntervalEnabled( true );
        AppManager.setCurveSelectedIntervalEnabled( false );
        AppManager.itemSelection.clearPoint();
        int startClock = AppManager.clockFromXCoord( e.X );
        if ( AppManager.editorConfig.CurveSelectingQuantized ) {
            int unit = AppManager.getPositionQuantizeClock();
            startClock = doQuantize( startClock, unit );
        }
        AppManager.mWholeSelectedInterval = new SelectedRegion( startClock );
        AppManager.mWholeSelectedInterval.setEnd( startClock );
        AppManager.mIsPointerDowned = true;
    } else {
        DrawObject vibrato_dobj = null;
        if ( selected_tool == EditTool.LINE || selected_tool == EditTool.PENCIL ) {
            // ビブラート範囲の編集
            int px_vibrato_length = 0;
            mVibratoEditingId = -1;
            Rectangle pxFound = new Rectangle();
            Vector<DrawObject> target_list = AppManager.mDrawObjects.get( selected - 1 );
            int count = target_list.size();
            for ( int i = 0; i < count; i++ ) {
                DrawObject dobj = target_list.get( i );
                if ( dobj.mRectangleInPixel.width <= dobj.mVibratoDelayInPixel ) {
                    continue;
                }
                if ( dobj.mRectangleInPixel.x + key_width + dobj.mRectangleInPixel.width - stdx < 0 ) {
                    continue;
                } else if ( pictPianoRoll.getWidth() < dobj.mRectangleInPixel.x + key_width - stdx ) {
                    break;
                }
                Rectangle rc = new Rectangle( dobj.mRectangleInPixel.x + key_width + dobj.mVibratoDelayInPixel - stdx - _EDIT_HANDLE_WIDTH / 2,
                                    dobj.mRectangleInPixel.y + (int)(100 * controller.getScaleY()) - stdy,
                                    _EDIT_HANDLE_WIDTH,
                                    (int)(100 * controller.getScaleY()) );
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    vibrato_dobj = dobj;
                    //vibrato_found = true;
                    mVibratoEditingId = dobj.mInternalID;
                    pxFound.x = dobj.mRectangleInPixel.x;
                    pxFound.y = dobj.mRectangleInPixel.y;
                    pxFound.width = dobj.mRectangleInPixel.width;
                    pxFound.height = dobj.mRectangleInPixel.height;// = new Rectangle dobj.mRectangleInPixel;
                    pxFound.x += key_width;
                    px_vibrato_length = dobj.mRectangleInPixel.width - dobj.mVibratoDelayInPixel;
                    break;
                }
            }
            if ( vibrato_dobj != null ) {
                int clock = AppManager.clockFromXCoord( pxFound.x + pxFound.width - px_vibrato_length - stdx );
                int note = vibrato_dobj.mNote - 1;// AppManager.noteFromYCoord( pxFound.y + (int)(100 * AppManager.getScaleY()) - stdy );
                int length = vibrato_dobj.mClock + vibrato_dobj.mLength - clock;// (int)(pxFound.width * AppManager.getScaleXInv());
                AppManager.mAddingEvent = new VsqEvent( clock, new VsqID( 0 ) );
                AppManager.mAddingEvent.ID.type = VsqIDType.Anote;
                AppManager.mAddingEvent.ID.Note = note;
                AppManager.mAddingEvent.ID.setLength( length );
                AppManager.mAddingEventLength = vibrato_dobj.mLength;
                AppManager.mAddingEvent.ID.VibratoDelay = length - (int)(px_vibrato_length * controller.getScaleXInv());
                AppManager.setEditMode( EditMode.EDIT_VIBRATO_DELAY );
                start_mouse_hover_generator = false;
            }
        }
        if ( vibrato_dobj == null ) {
            if ( (selected_tool == EditTool.PENCIL || selected_tool == EditTool.LINE) &&
                e.Button == BMouseButtons.Left &&
                e.X >= key_width ) {
                int clock = AppManager.clockFromXCoord( e.X );
                if ( AppManager.getVsqFile().getPreMeasureClocks() - AppManager.editorConfig.PxTolerance * controller.getScaleXInv() <= clock ) {
                    //10ピクセルまでは許容範囲
                    if ( AppManager.getVsqFile().getPreMeasureClocks() > clock ) { //だけど矯正するよ。
                        clock = AppManager.getVsqFile().getPreMeasureClocks();
                    }
                    int note = AppManager.noteFromYCoord( e.Y );
                    AppManager.itemSelection.clearEvent();
                    int unit = AppManager.getPositionQuantizeClock();
                    int new_clock = doQuantize( clock, unit );
                    AppManager.mAddingEvent = new VsqEvent( new_clock, new VsqID( 0 ) );
                    // デフォルトの歌唱スタイルを適用する
                    AppManager.editorConfig.applyDefaultSingerStyle( AppManager.mAddingEvent.ID );
                    if ( mPencilMode.getMode() == PencilModeEnum.Off ) {
                        AppManager.setEditMode( EditMode.ADD_ENTRY );
                        mButtonInitial = new Point( e.X, e.Y );
                        AppManager.mAddingEvent.ID.setLength( 0 );
                        AppManager.mAddingEvent.ID.Note = note;
                        setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
                    } else {
                        AppManager.setEditMode( EditMode.ADD_FIXED_LENGTH_ENTRY );
                        AppManager.mAddingEvent.ID.setLength( mPencilMode.getUnitLength() );
                        AppManager.mAddingEvent.ID.Note = note;
                        setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
                    }
                } else {
                }
            } else if ( (selected_tool == EditTool.ARROW) && e.Button == BMouseButtons.Left ) {
                AppManager.setWholeSelectedIntervalEnabled( false );
                AppManager.itemSelection.clearEvent();
                AppManager.mMouseDownLocation = new Point( e.X + stdx, e.Y + stdy );
                AppManager.mIsPointerDowned = true;
            }
        }
    }
    if ( e.Button == BMouseButtons.Right && !AppManager.editorConfig.PlayPreviewWhenRightClick ) {
        start_mouse_hover_generator = false;
    }
} else {
    if ( AppManager.itemSelection.isEventContains( selected, item.InternalID ) ) {
        executeLyricChangeCommand();
    }
    hideInputTextBox();
    if ( selected_tool != EditTool.ERASER ) {
    }

    // まず、両端の編集モードに移行可能かどうか調べる
    if ( item.ID.type != VsqIDType.Aicon ||
         (item.ID.type == VsqIDType.Aicon && !item.ID.IconDynamicsHandle.isDynaffType()) ) {
        if ( selected_tool != EditTool.ERASER && e.Button == BMouseButtons.Left ) {
            int min_width = 4 * _EDIT_HANDLE_WIDTH;
            for ( Iterator<DrawObject> itr = AppManager.mDrawObjects.get( selected - 1 ).iterator(); itr.hasNext(); ) {
                DrawObject dobj = itr.next();

                int edit_handle_width = _EDIT_HANDLE_WIDTH;
                if ( dobj.mRectangleInPixel.width < min_width ) {
                    edit_handle_width = dobj.mRectangleInPixel.width / 4;
                }

                // 左端の"のり代"にマウスがあるかどうか
                Rectangle rc = new Rectangle( dobj.mRectangleInPixel.x - stdx + key_width,
                                              dobj.mRectangleInPixel.y - stdy,
                                              edit_handle_width,
                                              dobj.mRectangleInPixel.height );
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    AppManager.setWholeSelectedIntervalEnabled( false );
                    AppManager.setEditMode( EditMode.EDIT_LEFT_EDGE );
                    if ( !AppManager.itemSelection.isEventContains( selected, item.InternalID ) ) {
                        AppManager.itemSelection.clearEvent();
                    }
                    AppManager.itemSelection.addEvent( item.InternalID );
                    setCursor( new Cursor( Cursor.W_RESIZE_CURSOR ) );
                    refreshScreen();
                    return;
                }

                // 右端の糊代にマウスがあるかどうか
                rc = new Rectangle( dobj.mRectangleInPixel.x + key_width + dobj.mRectangleInPixel.width - stdx - edit_handle_width,
                                    dobj.mRectangleInPixel.y - stdy,
                                    edit_handle_width,
                                    dobj.mRectangleInPixel.height );
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    AppManager.setWholeSelectedIntervalEnabled( false );
                    AppManager.setEditMode( EditMode.EDIT_RIGHT_EDGE );
                    if ( !AppManager.itemSelection.isEventContains( selected, item.InternalID ) ) {
                        AppManager.itemSelection.clearEvent();
                    }
                    AppManager.itemSelection.addEvent( item.InternalID );
                    setCursor( new Cursor( Cursor.E_RESIZE_CURSOR ) );
                    refreshScreen();
                    return;
                }
            }
        }
    }

    if ( e.Button == BMouseButtons.Left || e.Button == BMouseButtons.Middle ) {
            if ( selected_tool != EditTool.ERASER ) {
                mMouseMoveInit = new Point( e.X + stdx, e.Y + stdy );
                int head_x = AppManager.xCoordFromClocks( item.Clock );
                mMouseMoveOffset = e.X - head_x;
                if ( (modefier & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ) {
                    // シフトキー同時押しによる範囲選択
                    Vector<Integer> add_required = new Vector<Integer>();
                    add_required.add( item.InternalID );

                    // 現在の選択アイテムがある場合，
                    // 直前に選択したアイテムと，現在選択しようとしているアイテムとの間にあるアイテムを
                    // 全部選択する
                    SelectedEventEntry sel = AppManager.itemSelection.getLastEvent();
                    if ( sel != null ) {
                        int last_id = sel.original.InternalID;
                        int last_clock = 0;
                        int this_clock = 0;
                        boolean this_found = false, last_found = false;
                        for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
                            VsqEvent ev = itr.next();
                            if ( ev.InternalID == last_id ) {
                                last_clock = ev.Clock;
                                last_found = true;
                            } else if ( ev.InternalID == item.InternalID ) {
                                this_clock = ev.Clock;
                                this_found = true;
                            }
                            if ( last_found && this_found ) {
                                break;
                            }
                        }
                        int start = Math.min( last_clock, this_clock );
                        int end = Math.max( last_clock, this_clock );
                        for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
                            VsqEvent ev = itr.next();
                            if ( start <= ev.Clock && ev.Clock <= end ) {
                                if ( !add_required.contains( ev.InternalID ) ) {
                                    add_required.add( ev.InternalID );
                                }
                            }
                        }
                    }
                    AppManager.itemSelection.addEventAll( add_required );
                } else if ( (modefier & s_modifier_key) == s_modifier_key ) {
                    // CTRLキーを押しながら選択／選択解除
                    if ( AppManager.itemSelection.isEventContains( selected, item.InternalID ) ) {
                        AppManager.itemSelection.removeEvent( item.InternalID );
                    } else {
                        AppManager.itemSelection.addEvent( item.InternalID );
                    }
                } else {
                    if ( !AppManager.itemSelection.isEventContains( selected, item.InternalID ) ) {
                        // MouseDownしたアイテムが、まだ選択されていなかった場合。当該アイテム単独に選択しなおす
                        AppManager.itemSelection.clearEvent();
                    }
                    AppManager.itemSelection.addEvent( item.InternalID );
                }

                // 範囲選択モードで、かつマウス位置の音符がその範囲に入っていた場合にのみ、MOVE_ENTRY_WHOLE_WAIT_MOVEに移行
                if ( AppManager.isWholeSelectedIntervalEnabled() &&
                     AppManager.mWholeSelectedInterval.getStart() <= item.Clock &&
                     item.Clock <= AppManager.mWholeSelectedInterval.getEnd() ) {
                    AppManager.setEditMode( EditMode.MOVE_ENTRY_WHOLE_WAIT_MOVE );
                    AppManager.mWholeSelectedIntervalStartForMoving = AppManager.mWholeSelectedInterval.getStart();
                } else {
                    AppManager.setWholeSelectedIntervalEnabled( false );
                    AppManager.setEditMode( EditMode.MOVE_ENTRY_WAIT_MOVE );
                }

                setCursor( new Cursor( java.awt.Cursor.HAND_CURSOR ) );
            }
    }
}
refreshScreen();
        }

        public void pictPianoRoll_MouseMove( Object sender, BMouseEventArgs e )
        {
if ( mFormActivated ) {
    if ( AppManager.mInputTextBox != null && !AppManager.mInputTextBox.isVisible() && !AppManager.propertyPanel.isEditing() ) {
        focusPianoRoll();
    }
}

EditMode edit_mode = AppManager.getEditMode();
int stdx = controller.getStartToDrawX();
int stdy = controller.getStartToDrawY();
int selected = AppManager.getSelected();
EditTool selected_tool = AppManager.getSelectedTool();

if ( edit_mode == EditMode.CURVE_ON_PIANOROLL && AppManager.mCurveOnPianoroll ) {
    pictPianoRoll.mMouseTracer.append( e.X + stdx, e.Y + stdy );
    if ( !timer.isRunning() ) {
        refreshScreen();
    }
    return;
}

if ( !mMouseMoved && edit_mode == EditMode.MIDDLE_DRAG ) {
    setCursor( new Cursor( java.awt.Cursor.MOVE_CURSOR ) );
}

if ( e.X != mButtonInitial.x || e.Y != mButtonInitial.y ) {
    mMouseMoved = true;
}
if ( !(edit_mode == EditMode.MIDDLE_DRAG) && AppManager.isPlaying() ) {
    return;
}

if ( edit_mode == EditMode.MOVE_ENTRY_WAIT_MOVE ||
     edit_mode == EditMode.MOVE_ENTRY_WHOLE_WAIT_MOVE ) {
    int x = e.X + stdx;
    int y = e.Y + stdy;
    if ( mMouseMoveInit.x != x || mMouseMoveInit.y != y ) {
        if ( edit_mode == EditMode.MOVE_ENTRY_WAIT_MOVE ) {
            AppManager.setEditMode( EditMode.MOVE_ENTRY );
            edit_mode = EditMode.MOVE_ENTRY;
        } else {
            AppManager.setEditMode( EditMode.MOVE_ENTRY_WHOLE );
            edit_mode = EditMode.MOVE_ENTRY_WHOLE;
        }
    }
}


int clock = AppManager.clockFromXCoord( e.X );
if ( mMouseDowned ) {
    if ( mExtDragX == ExtDragXMode.NONE ) {
        if ( AppManager.keyWidth > e.X ) {
            mExtDragX = ExtDragXMode.LEFT;
        } else if ( pictPianoRoll.getWidth() < e.X ) {
            mExtDragX = ExtDragXMode.RIGHT;
        }
    } else {
        if ( AppManager.keyWidth <= e.X && e.X <= pictPianoRoll.getWidth() ) {
            mExtDragX = ExtDragXMode.NONE;
        }
    }

    if ( mExtDragY == ExtDragYMode.NONE ) {
        if ( 0 > e.Y ) {
            mExtDragY = ExtDragYMode.UP;
        } else if ( pictPianoRoll.getHeight() < e.Y ) {
            mExtDragY = ExtDragYMode.DOWN;
        }
    } else {
        if ( 0 <= e.Y && e.Y <= pictPianoRoll.getHeight() ) {
            mExtDragY = ExtDragYMode.NONE;
        }
    }
} else {
    mExtDragX = ExtDragXMode.NONE;
    mExtDragY = ExtDragYMode.NONE;
}

if ( edit_mode == EditMode.MIDDLE_DRAG ) {
    mExtDragX = ExtDragXMode.NONE;
    mExtDragY = ExtDragYMode.NONE;
}

double now = 0, dt = 0;
if ( mExtDragX != ExtDragXMode.NONE || mExtDragY != ExtDragYMode.NONE ) {
    now = PortUtil.getCurrentTime();
    dt = now - mTimerDragLastIgnitted;
}
if ( mExtDragX == ExtDragXMode.RIGHT || mExtDragX == ExtDragXMode.LEFT ) {
    int px_move = AppManager.editorConfig.MouseDragIncrement;
    if ( px_move / dt > AppManager.editorConfig.MouseDragMaximumRate ) {
        px_move = (int)(dt * AppManager.editorConfig.MouseDragMaximumRate);
    }
    double d_draft;
    if ( mExtDragX == ExtDragXMode.LEFT ) {
        px_move *= -1;
    }
    int left_clock = AppManager.clockFromXCoord( AppManager.keyWidth );
    float inv_scale_x = controller.getScaleXInv();
    int dclock = (int)(px_move * inv_scale_x);
    d_draft = 5 * inv_scale_x + left_clock + dclock;
    if ( d_draft < 0.0 ) {
        d_draft = 0.0;
    }
    int draft = (int)d_draft;
    if ( hScroll.getMaximum() < draft ) {
        if ( edit_mode == EditMode.ADD_ENTRY ||
             edit_mode == EditMode.MOVE_ENTRY ||
             edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY ||
             edit_mode == EditMode.DRAG_DROP ) {
            hScroll.setMaximum( draft );
        } else {
            draft = hScroll.getMaximum();
        }
    }
    if ( draft < hScroll.getMinimum() ) {
        draft = hScroll.getMinimum();
    }
    hScroll.setValue( draft );
}
if ( mExtDragY == ExtDragYMode.UP || mExtDragY == ExtDragYMode.DOWN ) {
    int min = vScroll.getMinimum();
    int max = vScroll.getMaximum() - vScroll.getVisibleAmount();
    int px_move = AppManager.editorConfig.MouseDragIncrement;
    if ( px_move / dt > AppManager.editorConfig.MouseDragMaximumRate ) {
        px_move = (int)(dt * AppManager.editorConfig.MouseDragMaximumRate);
    }
    px_move += 50;
    if ( mExtDragY == ExtDragYMode.UP ) {
        px_move *= -1;
    }
    int draft = vScroll.getValue() + px_move;
    if ( draft < 0 ) {
        draft = 0;
    }
    int df = (int)draft;
    if ( df < min ) {
        df = min;
    } else if ( max < df ) {
        df = max;
    }
    vScroll.setValue( df );
}
if ( mExtDragX != ExtDragXMode.NONE || mExtDragY != ExtDragYMode.NONE ) {
    mTimerDragLastIgnitted = now;
}

// 選択範囲にあるイベントを選択．
if ( AppManager.mIsPointerDowned ) {
    if ( AppManager.isWholeSelectedIntervalEnabled() ) {
        int endClock = AppManager.clockFromXCoord( e.X );
        if ( AppManager.editorConfig.CurveSelectingQuantized ) {
            int unit = AppManager.getPositionQuantizeClock();
            endClock = doQuantize( endClock, unit );
        }
        AppManager.mWholeSelectedInterval.setEnd( endClock );
    } else {
        Point mouse = new Point( e.X + stdx, e.Y + stdy );
        int tx, ty, twidth, theight;
        int lx = AppManager.mMouseDownLocation.x;
        if ( lx < mouse.x ) {
            tx = lx;
            twidth = mouse.x - lx;
        } else {
            tx = mouse.x;
            twidth = lx - mouse.x;
        }
        int ly = AppManager.mMouseDownLocation.y;
        if ( ly < mouse.y ) {
            ty = ly;
            theight = mouse.y - ly;
        } else {
            ty = mouse.y;
            theight = ly - mouse.y;
        }

        Rectangle rect = new Rectangle( tx, ty, twidth, theight );
        Vector<Integer> add_required = new Vector<Integer>();
        int internal_id = -1;
        for ( Iterator<DrawObject> itr = AppManager.mDrawObjects.get( selected - 1 ).iterator(); itr.hasNext(); ) {
            DrawObject dobj = itr.next();
            int x0 = dobj.mRectangleInPixel.x + AppManager.keyWidth;
            int x1 = dobj.mRectangleInPixel.x + AppManager.keyWidth + dobj.mRectangleInPixel.width;
            int y0 = dobj.mRectangleInPixel.y;
            int y1 = dobj.mRectangleInPixel.y + dobj.mRectangleInPixel.height;
            internal_id = dobj.mInternalID;
            if ( x1 < tx ) {
                continue;
            }
            if ( tx + twidth < x0 ) {
                break;
            }
            boolean found = Utility.isInRect( new Point( x0, y0 ), rect ) |
                            Utility.isInRect( new Point( x0, y1 ), rect ) |
                            Utility.isInRect( new Point( x1, y0 ), rect ) |
                            Utility.isInRect( new Point( x1, y1 ), rect );
            if ( found ) {
                add_required.add( internal_id );
            } else {
                if ( x0 <= tx && tx + twidth <= x1 ) {
                    if ( ty < y0 ) {
                        if ( y0 <= ty + theight ) {
                            add_required.add( internal_id );
                        }
                    } else if ( y0 <= ty && ty < y1 ) {
                        add_required.add( internal_id );
                    }
                } else if ( y0 <= ty && ty + theight <= y1 ) {
                    if ( tx < x0 ) {
                        if ( x0 <= tx + twidth ) {
                            add_required.add( internal_id );
                        }
                    } else if ( x0 <= tx && tx < x1 ) {
                        add_required.add( internal_id );
                    }
                }
            }
        }
        Vector<Integer> remove_required = new Vector<Integer>();
        for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
            SelectedEventEntry selectedEvent = itr.next();
            if ( !add_required.contains( selectedEvent.original.InternalID ) ) {
                remove_required.add( selectedEvent.original.InternalID );
            }
        }
        if ( remove_required.size() > 0 ) {
            AppManager.itemSelection.removeEventRange( PortUtil.convertIntArray( remove_required.toArray( new Integer[] { } ) ) );
        }
        for ( Iterator<Integer> itr = add_required.iterator(); itr.hasNext(); ) {
            int id = itr.next();
            if ( AppManager.itemSelection.isEventContains( selected, id ) ) {
                itr.remove();
            }
        }
        AppManager.itemSelection.addEventAll( add_required );
    }
}

if ( edit_mode == EditMode.MIDDLE_DRAG ) {
    int drafth = computeHScrollValueForMiddleDrag( e.X );
    int draftv = computeVScrollValueForMiddleDrag( e.Y );
    boolean moved = false;
    if ( drafth != hScroll.getValue() ) {
        //moved = true;
        //hScroll.beQuiet();
        hScroll.setValue( drafth );
    }
    if ( draftv != vScroll.getValue() ) {
        //moved = true;
        //vScroll.beQuiet();
        vScroll.setValue( draftv );
    }
    //if ( moved ) {
    //    vScroll.setQuiet( false );
    //    hScroll.setQuiet( false );
    //    refreshScreen( true );
    //}
    refreshScreen( true );
    if ( AppManager.isPlaying() ) {
        return;
    }
    return;
} else if ( edit_mode == EditMode.ADD_ENTRY ) {
    int unit = AppManager.getLengthQuantizeClock();
    int length = clock - AppManager.mAddingEvent.Clock;
    int odd = length % unit;
    int new_length = length - odd;

    if ( unit * controller.getScaleX() > 10 ) { //これをしないと、グリッド2個分増えることがある
        int next_clock = AppManager.clockFromXCoord( e.X + 10 );
        int next_length = next_clock - AppManager.mAddingEvent.Clock;
        int next_new_length = next_length - (next_length % unit);
        if ( next_new_length == new_length + unit ) {
            new_length = next_new_length;
        }
    }

    if ( new_length <= 0 ) {
        new_length = 0;
    }
    AppManager.mAddingEvent.ID.setLength( new_length );
} else if ( edit_mode == EditMode.MOVE_ENTRY || edit_mode == EditMode.MOVE_ENTRY_WHOLE ) {
    if ( AppManager.itemSelection.getEventCount() > 0 ) {
        VsqEvent original = AppManager.itemSelection.getLastEvent().original;
        int note = AppManager.noteFromYCoord( e.Y );                           // 現在のマウス位置でのnote
        int note_init = original.ID.Note;
        int dnote = (edit_mode == EditMode.MOVE_ENTRY) ? note - note_init : 0;

        int tclock = AppManager.clockFromXCoord( e.X - mMouseMoveOffset );
        int clock_init = original.Clock;

        int dclock = tclock - clock_init;

        if ( AppManager.editorConfig.getPositionQuantize() != QuantizeMode.off ) {
            int unit = AppManager.getPositionQuantizeClock();
            int new_clock = doQuantize( original.Clock + dclock, unit );
            dclock = new_clock - clock_init;
        }

        AppManager.mWholeSelectedIntervalStartForMoving = AppManager.mWholeSelectedInterval.getStart() + dclock;

        for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
            SelectedEventEntry item = itr.next();
            int new_clock = item.original.Clock + dclock;
            int new_note = item.original.ID.Note + dnote;
            item.editing.Clock = new_clock;
            item.editing.ID.Note = new_note;
        }
    }
} else if ( edit_mode == EditMode.EDIT_LEFT_EDGE ) {
    int unit = AppManager.getLengthQuantizeClock();
    VsqEvent original = AppManager.itemSelection.getLastEvent().original;
    int clock_init = original.Clock;
    int dclock = clock - clock_init;
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry item = itr.next();
        int end_clock = item.original.Clock + item.original.ID.getLength();
        int new_clock = item.original.Clock + dclock;
        int new_length = doQuantize( end_clock - new_clock, unit );
        if ( new_length <= 0 ) {
            new_length = unit;
        }
        item.editing.Clock = end_clock - new_length;
        if ( AppManager.vibratoLengthEditingRule == VibratoLengthEditingRule.PERCENTAGE ) {
            double percentage = item.original.ID.VibratoDelay / (double)item.original.ID.getLength() * 100.0;
            int newdelay = (int)(new_length * percentage / 100.0);
            item.editing.ID.VibratoDelay = newdelay;
        }
        item.editing.ID.setLength( new_length );
    }
} else if ( edit_mode == EditMode.EDIT_RIGHT_EDGE ) {
    int unit = AppManager.getLengthQuantizeClock();

    VsqEvent original = AppManager.itemSelection.getLastEvent().original;
    int dlength = clock - (original.Clock + original.ID.getLength());
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry item = itr.next();
        int new_length = doQuantize( item.original.ID.getLength() + dlength, unit );
        if ( new_length <= 0 ) {
            new_length = unit;
        }
        if ( AppManager.vibratoLengthEditingRule == VibratoLengthEditingRule.PERCENTAGE ) {
            double percentage = item.original.ID.VibratoDelay / (double)item.original.ID.getLength() * 100.0;
            int newdelay = (int)(new_length * percentage / 100.0);
            item.editing.ID.VibratoDelay = newdelay;
        }
        item.editing.ID.setLength( new_length );
    }
} else if ( edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY ) {
    int note = AppManager.noteFromYCoord( e.Y );
    int unit = AppManager.getPositionQuantizeClock();
    int new_clock = doQuantize( AppManager.clockFromXCoord( e.X ), unit );
    AppManager.mAddingEvent.ID.Note = note;
    AppManager.mAddingEvent.Clock = new_clock;
} else if ( edit_mode == EditMode.EDIT_VIBRATO_DELAY ) {
    int new_vibrato_start = clock;
    int old_vibrato_end = AppManager.mAddingEvent.Clock + AppManager.mAddingEvent.ID.getLength();
    int new_vibrato_length = old_vibrato_end - new_vibrato_start;
    int max_length = (int)(AppManager.mAddingEventLength - _PX_ACCENT_HEADER * controller.getScaleXInv());
    if ( max_length < 0 ) {
        max_length = 0;
    }
    if ( new_vibrato_length > max_length ) {
        new_vibrato_start = old_vibrato_end - max_length;
        new_vibrato_length = max_length;
    }
    if ( new_vibrato_length < 0 ) {
        new_vibrato_start = old_vibrato_end;
        new_vibrato_length = 0;
    }
    AppManager.mAddingEvent.Clock = new_vibrato_start;
    AppManager.mAddingEvent.ID.setLength( new_vibrato_length );
    if ( !timer.isRunning() ) {
        refreshScreen();
    }
    return;
} else if ( edit_mode == EditMode.DRAG_DROP ) {
    // クオンタイズの処理
    int unit = AppManager.getPositionQuantizeClock();
    int clock1 = doQuantize( clock, unit );
    int note = AppManager.noteFromYCoord( e.Y );
    AppManager.mAddingEvent.Clock = clock1;
    AppManager.mAddingEvent.ID.Note = note;
}

// カーソルの形を決める
if ( !mMouseDowned &&
     edit_mode != EditMode.CURVE_ON_PIANOROLL &&
     !(AppManager.mCurveOnPianoroll && (selected_tool == EditTool.PENCIL || selected_tool == EditTool.LINE)) ) {
    boolean split_cursor = false;
    boolean hand_cursor = false;
    int min_width = 4 * _EDIT_HANDLE_WIDTH;
    for ( Iterator<DrawObject> itr = AppManager.mDrawObjects.get( selected - 1 ).iterator(); itr.hasNext(); ) {
        DrawObject dobj = itr.next();
        Rectangle rc;
        if ( dobj.mType != DrawObjectType.Dynaff ) {
            int edit_handle_width = _EDIT_HANDLE_WIDTH;
            if ( dobj.mRectangleInPixel.width < min_width ) {
                edit_handle_width = dobj.mRectangleInPixel.width / 4;
            }

            // 音符左側の編集領域
            rc = new Rectangle(
                                dobj.mRectangleInPixel.x + AppManager.keyWidth - stdx,
                                dobj.mRectangleInPixel.y - stdy,
                                edit_handle_width,
                                (int)(100 * controller.getScaleY()) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                split_cursor = true;
                break;
            }

            // 音符右側の編集領域
            rc = new Rectangle( dobj.mRectangleInPixel.x + AppManager.keyWidth + dobj.mRectangleInPixel.width - stdx - edit_handle_width,
                                dobj.mRectangleInPixel.y - stdy,
                                edit_handle_width,
                                (int)(100 * controller.getScaleY()) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                split_cursor = true;
                break;
            }
        }

        // 音符本体
        rc = new Rectangle( dobj.mRectangleInPixel.x + AppManager.keyWidth - stdx,
                            dobj.mRectangleInPixel.y - stdy,
                            dobj.mRectangleInPixel.width,
                            dobj.mRectangleInPixel.height );
        if ( dobj.mType == DrawObjectType.Note ) {
            if ( AppManager.editorConfig.ShowExpLine && !dobj.mIsOverlapped ) {
                rc.height *= 2;
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    // ビブラートの開始位置
                    rc = new Rectangle( dobj.mRectangleInPixel.x + AppManager.keyWidth + dobj.mVibratoDelayInPixel - stdx - _EDIT_HANDLE_WIDTH / 2,
                                        dobj.mRectangleInPixel.y + (int)(100 * controller.getScaleY()) - stdy,
                                        _EDIT_HANDLE_WIDTH,
                                        (int)(100 * controller.getScaleY()) );
                    if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                        split_cursor = true;
                        break;
                    } else {
                        hand_cursor = true;
                        break;
                    }
                }
            } else {
                if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                    hand_cursor = true;
                    break;
                }
            }
        } else {
            if ( Utility.isInRect( new Point( e.X, e.Y ), rc ) ) {
                hand_cursor = true;
                break;
            }
        }
    }

    if ( split_cursor ) {
        setCursor( new Cursor( java.awt.Cursor.E_RESIZE_CURSOR ) );
    } else if ( hand_cursor ) {
        setCursor( new Cursor( java.awt.Cursor.HAND_CURSOR ) );
    } else {
        setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
    }
}
if ( !timer.isRunning() ) {
    refreshScreen( true );
}
        }

        /// <summary>
        /// ピアノロールからマウスボタンが離れたときの処理
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void pictPianoRoll_MouseUp( Object sender, BMouseEventArgs e )
        {
AppManager.mIsPointerDowned = false;
mMouseDowned = false;

int modefiers = PortUtil.getCurrentModifierKey();

EditMode edit_mode = AppManager.getEditMode();
VsqFileEx vsq = AppManager.getVsqFile();
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
CurveType selected_curve = trackSelector.getSelectedCurve();
int stdx = controller.getStartToDrawX();
int stdy = controller.getStartToDrawY();
double d2_13 = 8192; // = 2^13
int track_height = (int)(100 * controller.getScaleY());
int half_track_height = track_height / 2;

if ( edit_mode == EditMode.CURVE_ON_PIANOROLL ) {
    if ( pictPianoRoll.mMouseTracer.size() > 1 ) {
        // マウスの軌跡の左右端(px)
        int px_start = pictPianoRoll.mMouseTracer.firstKey();
        int px_end = pictPianoRoll.mMouseTracer.lastKey();

        // マウスの軌跡の左右端(クロック)
        int cl_start = AppManager.clockFromXCoord( px_start - stdx );
        int cl_end = AppManager.clockFromXCoord( px_end - stdx );

        // 編集が行われたかどうか
        boolean edited = false;
        // 作業用のPITカーブのコピー
        VsqBPList pit = (VsqBPList)vsq_track.getCurve( "pit" ).clone();
        VsqBPList pbs = (VsqBPList)vsq_track.getCurve( "pbs" ); // こっちはクローンしないよ

        // トラック内の全音符に対して、マウス軌跡と被っている部分のPITを編集する
        for ( Iterator<VsqEvent> itr = vsq_track.getNoteEventIterator(); itr.hasNext(); ) {
            VsqEvent item = itr.next();
            int cl_item_start = item.Clock;
            if ( cl_end < cl_item_start ) {
                break;
            }
            int cl_item_end = cl_item_start + item.ID.getLength();
            if ( cl_item_end < cl_start ) {
                continue;
            }

            // ここに到達するってことは、pitに編集が加えられるってこと。
            edited = true;

            // マウス軌跡と被っている部分のPITを削除
            int cl_remove_start = Math.max( cl_item_start, cl_start );
            int cl_remove_end = Math.min( cl_item_end, cl_end );
            int value_at_remove_end = pit.getValue( cl_remove_end );
            int value_at_remove_start = pit.getValue( cl_remove_start );
            Vector<Integer> remove = new Vector<Integer>();
            for ( Iterator<Integer> itr2 = pit.keyClockIterator(); itr2.hasNext(); ) {
                int clock = itr2.next();
                if ( cl_remove_start <= clock && clock <= cl_remove_end ) {
                    remove.add( clock );
                }
            }
            for ( Iterator<Integer> itr2 = remove.iterator(); itr2.hasNext(); ) {
                int clock = itr2.next();
                pit.remove( clock );
            }
            remove = null;

            int px_item_start = AppManager.xCoordFromClocks( cl_item_start ) + stdx;
            int px_item_end = AppManager.xCoordFromClocks( cl_item_end ) + stdx;

            int lastv = value_at_remove_start;
            boolean cl_item_end_added = false;
            boolean cl_item_start_added = false;
            int last_px = 0, last_py = 0;
            for ( Iterator<Point> itr2 = pictPianoRoll.mMouseTracer.iterator(); itr2.hasNext(); ) {
                Point p = itr2.next();
                if ( p.x < px_item_start ) {
                    last_px = p.x;
                    last_py = p.y;
                    continue;
                }
                if ( px_item_end < p.x ) {
                    break;
                }

                int clock = AppManager.clockFromXCoord( p.x - stdx );
                if ( clock < cl_item_start ) {
                    last_px = p.x;
                    last_py = p.y;
                    continue;
                } else if ( cl_item_end < clock ) {
                    break;
                }
                double note = AppManager.noteFromYCoordDoublePrecision( p.y - stdy - half_track_height );
                int v_pit = (int)(d2_13 / (double)pbs.getValue( clock ) * (note - item.ID.Note));

                // 正規化
                if ( v_pit < pit.getMinimum() ) {
                    v_pit = pit.getMinimum();
                } else if ( pit.getMaximum() < v_pit ) {
                    v_pit = pit.getMaximum();
                }

                if ( cl_item_start < clock && !cl_item_start_added &&
                     cl_start <= cl_item_start && cl_item_start < cl_end ) {
                    // これから追加しようとしているデータ点の時刻が、音符の開始時刻よりも後なんだけれど、
                    // 音符の開始時刻におけるデータをまだ書き込んでない場合
                    double a = (p.y - last_py) / (double)(p.x - last_px);
                    double x_at_clock = AppManager.xCoordFromClocks( cl_item_start ) + stdx;
                    double ext_y = last_py + a * (x_at_clock - last_px);
                    double tnote = AppManager.noteFromYCoordDoublePrecision( (int)(ext_y - stdy - half_track_height) );
                    int t_vpit = (int)(d2_13 / (double)pbs.getValue( cl_item_start ) * (tnote - item.ID.Note));
                    pit.add( cl_item_start, t_vpit );
                    lastv = t_vpit;
                    cl_item_start_added = true;
                }

                // 直前の値と違っている場合にのみ追加
                if ( v_pit != lastv ) {
                    pit.add( clock, v_pit );
                    lastv = v_pit;
                    if ( clock == cl_item_end ) {
                        cl_item_end_added = true;
                    } else if ( clock == cl_item_start ) {
                        cl_item_start_added = true;
                    }
                }
            }

            if ( !cl_item_end_added &&
                 cl_start <= cl_item_end && cl_item_end <= cl_end ) {
                pit.add( cl_item_end, lastv );
            }

            pit.add( cl_remove_end, value_at_remove_end );
        }

        // 編集操作が行われた場合のみ、コマンドを発行
        if ( edited ) {
            CadenciiCommand run = new CadenciiCommand(
                VsqCommand.generateCommandTrackCurveReplace( selected, "PIT", pit ) );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
            setEdited( true );
        }
    }
    pictPianoRoll.mMouseTracer.clear();
    AppManager.setEditMode( EditMode.NONE );
    return;
}

if ( edit_mode == EditMode.MIDDLE_DRAG ) {
    setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
} else if ( edit_mode == EditMode.ADD_ENTRY || edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY ) {
    if ( AppManager.getSelected() >= 0 ) {
        if ( (edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY) ||
             (edit_mode == EditMode.ADD_ENTRY && (mButtonInitial.x != e.X || mButtonInitial.y != e.Y) && AppManager.mAddingEvent.ID.getLength() > 0) ) {
            if ( AppManager.mAddingEvent.Clock < vsq.getPreMeasureClocks() ) {
            } else {
                fixAddingEvent();
            }
        }
    }
} else if ( edit_mode == EditMode.MOVE_ENTRY ) {
    if ( AppManager.itemSelection.getEventCount() > 0 ) {
        SelectedEventEntry last_selected_event = AppManager.itemSelection.getLastEvent();
        VsqEvent original = last_selected_event.original;
        if ( original.Clock != last_selected_event.editing.Clock ||
             original.ID.Note != last_selected_event.editing.ID.Note ) {
            boolean out_of_range = false; // プリメジャーにめり込んでないかどうか
            boolean contains_dynamics = false; // Dynaff, Crescend, Desrecendが含まれているかどうか
            VsqTrack copied = (VsqTrack)vsq_track.clone();
            int clockAtPremeasure = vsq.getPreMeasureClocks();
            for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
                SelectedEventEntry ev = itr.next();
                int internal_id = ev.original.InternalID;
                if ( ev.editing.Clock < clockAtPremeasure ) {
                    out_of_range = true;
                    break;
                }
                if ( ev.editing.ID.Note < 0 || 128 < ev.editing.ID.Note ) {
                    out_of_range = true;
                    break;
                }
                for ( Iterator<VsqEvent> itr2 = copied.getEventIterator(); itr2.hasNext(); ) {
                    VsqEvent item = itr2.next();
                    if ( item.InternalID == internal_id ) {
                        item.Clock = ev.editing.Clock;
                        item.ID = (VsqID)ev.editing.ID.clone();
                        break;
                    }
                }
                if ( ev.original.ID.type == VsqIDType.Aicon ) {
                    contains_dynamics = true;
                }
            }
            if ( out_of_range ) {
            } else {
                if ( contains_dynamics ) {
                    copied.reflectDynamics();
                }
                CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected,
                                                                             copied,
                                                                             vsq.AttachedCurves.get( selected - 1 ) );
                AppManager.editHistory.register( vsq.executeCommand( run ) );
                AppManager.itemSelection.updateSelectedEventInstance();
                setEdited( true );
            }
        } else {
            /*if ( (modefier & Keys.Shift) == Keys.Shift || (modefier & Keys.Control) == Keys.Control ) {
                Rectangle rc;
                VsqEvent select = IdOfClickedPosition( e.Location, out rc );
                if ( select != null ) {
                    m_config.addSelectedEvent( item.InternalID );
                }
            }*/
        }
        synchronized ( AppManager.mDrawObjects ) {
            Collections.sort( AppManager.mDrawObjects.get( selected - 1 ) );
        }
    }
} else if ( edit_mode == EditMode.EDIT_LEFT_EDGE || edit_mode == EditMode.EDIT_RIGHT_EDGE ) {
    if ( mMouseMoved ) {
        VsqEvent original = AppManager.itemSelection.getLastEvent().original;
        int count = AppManager.itemSelection.getEventCount();
        int[] ids = new int[count];
        int[] clocks = new int[count];
        VsqID[] values = new VsqID[count];
        int i = -1;
        boolean contains_aicon = false; // dynaff, crescend, decrescendが含まれていればtrue
        for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
            SelectedEventEntry ev = itr.next();
            if ( ev.original.ID.type == VsqIDType.Aicon ) {
                contains_aicon = true;
            }
            i++;

            Utility.editLengthOfVsqEvent( ev.editing, ev.editing.ID.getLength(), AppManager.vibratoLengthEditingRule );
            ids[i] = ev.original.InternalID;
            clocks[i] = ev.editing.Clock;
            values[i] = ev.editing.ID;
        }

        CadenciiCommand run = null;
        if ( contains_aicon ) {
            VsqFileEx copied_vsq = (VsqFileEx)vsq.clone();
            VsqCommand vsq_command =
                VsqCommand.generateCommandEventChangeClockAndIDContaintsRange( selected,
                                                                               ids,
                                                                               clocks,
                                                                               values );
            copied_vsq.executeCommand( vsq_command );
            VsqTrack copied = (VsqTrack)copied_vsq.Track.get( selected ).clone();
            copied.reflectDynamics();
            run = VsqFileEx.generateCommandTrackReplace( selected,
                                                         copied,
                                                         vsq.AttachedCurves.get( selected - 1 ) );
        } else {
            run = new CadenciiCommand(
                VsqCommand.generateCommandEventChangeClockAndIDContaintsRange( selected,
                                                                     ids,
                                                                     clocks,
                                                                     values ) );
        }
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
    }
} else if ( edit_mode == EditMode.EDIT_VIBRATO_DELAY ) {
    if ( mMouseMoved ) {
        double max_length = AppManager.mAddingEventLength - _PX_ACCENT_HEADER * controller.getScaleXInv();
        double rate = AppManager.mAddingEvent.ID.getLength() / max_length;
        if ( rate > 0.99 ) {
            rate = 1.0;
        }
        int vibrato_length = (int)(AppManager.mAddingEventLength * rate);
        VsqEvent item = null;
        for ( Iterator<VsqEvent> itr = vsq_track.getNoteEventIterator(); itr.hasNext(); ) {
            VsqEvent ve = itr.next();
            if ( ve.InternalID == mVibratoEditingId ) {
                item = (VsqEvent)ve.clone();
                break;
            }
        }
        if ( item != null ) {
            if ( vibrato_length <= 0 ) {
                item.ID.VibratoHandle = null;
                item.ID.VibratoDelay = item.ID.getLength();
            } else {
                item.ID.VibratoHandle.setLength( vibrato_length );
                item.ID.VibratoDelay = item.ID.getLength() - vibrato_length;
            }
            CadenciiCommand run = new CadenciiCommand(
                VsqCommand.generateCommandEventChangeIDContaints( selected, mVibratoEditingId, item.ID ) );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
            setEdited( true );
        }
    }
} else if ( edit_mode == EditMode.MOVE_ENTRY_WHOLE ) {
    int src_clock_start = AppManager.mWholeSelectedInterval.getStart();
    int src_clock_end = AppManager.mWholeSelectedInterval.getEnd();
    int dst_clock_start = AppManager.mWholeSelectedIntervalStartForMoving;
    int dst_clock_end = dst_clock_start + (src_clock_end - src_clock_start);
    int dclock = dst_clock_start - src_clock_start;

    int num = AppManager.itemSelection.getEventCount();
    int[] selected_ids = new int[num]; // 後段での再選択用のInternalIDのリスト
    int last_selected_id = AppManager.itemSelection.getLastEvent().original.InternalID;

    // 音符イベントを移動
    VsqTrack work = (VsqTrack)vsq_track.clone();
    int k = 0;
    for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
        SelectedEventEntry item = itr.next();
        int internal_id = item.original.InternalID;
        selected_ids[k] = internal_id;
        k++;
        for ( Iterator<VsqEvent> itr2 = work.getNoteEventIterator(); itr2.hasNext(); ) {
            VsqEvent vsq_event = itr2.next();
            if ( internal_id == vsq_event.InternalID ) {
                vsq_event.Clock = item.editing.Clock;
                break;
            }
        }
    }

    // 全てのコントロールカーブのデータ点を移動
    for ( int i = 0; i < Utility.CURVE_USAGE.length; i++ ) {
        CurveType curve_type = Utility.CURVE_USAGE[i];
        VsqBPList bplist = work.getCurve( curve_type.getName() );
        if ( bplist == null ) {
            continue;
        }

        // src_clock_startからsrc_clock_endの範囲にあるデータ点をコピー＆削除
        VsqBPList copied = new VsqBPList( bplist.getName(), bplist.getDefault(), bplist.getMinimum(), bplist.getMaximum() );
        int size = bplist.size();
        for ( int j = size - 1; j >= 0; j-- ) {
            int clock = bplist.getKeyClock( j );
            if ( src_clock_start <= clock && clock <= src_clock_end ) {
                VsqBPPair bppair = bplist.getElementB( j );
                copied.add( clock, bppair.value );
                bplist.removeElementAt( j );
            }
        }

        // dst_clock_startからdst_clock_endの範囲にあるコントロールカーブのデータ点をすべて削除
        size = bplist.size();
        for ( int j = size - 1; j >= 0; j-- ) {
            int clock = bplist.getKeyClock( j );
            if ( dst_clock_start <= clock && clock <= dst_clock_end ) {
                bplist.removeElementAt( j );
            }
        }

        // コピーしたデータを、クロックをずらしながら追加
        size = copied.size();
        for ( int j = 0; j < size; j++ ) {
            int clock = copied.getKeyClock( j );
            VsqBPPair bppair = copied.getElementB( j );
            bplist.add( clock + dclock, bppair.value );
        }
    }

    // コマンドを作成＆実行
    CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected,
                                                                 work,
                                                                 vsq.AttachedCurves.get( selected - 1 ) );
    AppManager.editHistory.register( vsq.executeCommand( run ) );

    // 選択範囲を更新
    AppManager.mWholeSelectedInterval = new SelectedRegion( dst_clock_start );
    AppManager.mWholeSelectedInterval.setEnd( dst_clock_end );
    AppManager.mWholeSelectedIntervalStartForMoving = dst_clock_start;

    // 音符の再選択
    AppManager.itemSelection.clearEvent();
    Vector<Integer> list_selected_ids = new Vector<Integer>();
    for ( int i = 0; i < num; i++ ) {
        list_selected_ids.add( selected_ids[i] );
    }
    AppManager.itemSelection.addEventAll( list_selected_ids );
    AppManager.itemSelection.addEvent( last_selected_id );

    setEdited( true );
} else if ( AppManager.isWholeSelectedIntervalEnabled() ) {
    int start = AppManager.mWholeSelectedInterval.getStart();
    int end = AppManager.mWholeSelectedInterval.getEnd();
    AppManager.itemSelection.clearEvent();

    // 音符の選択状態を更新
    Vector<Integer> add_required_event = new Vector<Integer>();
    for ( Iterator<VsqEvent> itr = vsq_track.getEventIterator(); itr.hasNext(); ) {
        VsqEvent ve = itr.next();
        if ( start <= ve.Clock && ve.Clock + ve.ID.getLength() <= end ) {
            add_required_event.add( ve.InternalID );
        }
    }
    AppManager.itemSelection.addEventAll( add_required_event );

    // コントロールカーブ点の選択状態を更新
    Vector<Long> add_required_point = new Vector<Long>();
    VsqBPList list = vsq_track.getCurve( selected_curve.getName() );
    if ( list != null ) {
        int count = list.size();
        for ( int i = 0; i < count; i++ ) {
            int clock = list.getKeyClock( i );
            if ( clock < start ) {
                continue;
            } else if ( end < clock ) {
                break;
            } else {
                VsqBPPair v = list.getElementB( i );
                add_required_point.add( v.id );
            }
        }
    }
    if ( add_required_point.size() > 0 ) {
        AppManager.itemSelection.addPointAll( selected_curve,
                                        PortUtil.convertLongArray( add_required_point.toArray( new Long[] { } ) ) );
    }
}
        heaven:
AppManager.setEditMode( EditMode.NONE );
refreshScreen( true );
        }

        public void pictPianoRoll_MouseWheel( Object sender, BMouseEventArgs e )
        {
int modifier = PortUtil.getCurrentModifierKey();
boolean horizontal = (modifier & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
if ( AppManager.editorConfig.ScrollHorizontalOnWheel ) {
    horizontal = !horizontal;
}
if ( (modifier & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK ) {
    // ピアノロール拡大率を変更
    if ( horizontal ) {
        int max = trackBar.getMaximum();
        int min = trackBar.getMinimum();
        int width = max - min;
        int delta = (width / 10) * (e.Delta > 0 ? 1 : -1);
        int old_tbv = trackBar.getValue();
        int draft = old_tbv + delta;
        if ( draft < min ) {
            draft = min;
        }
        if ( max < draft ) {
            draft = max;
        }
        if ( old_tbv != draft ) {

            // マウス位置を中心に拡大されるようにしたいので．
            // マウスのスクリーン座標
            Point screen_p_at_mouse = PortUtil.getMousePosition();
            // ピアノロール上でのマウスのx座標
            int x_at_mouse = pictPianoRoll.pointToClient( screen_p_at_mouse ).x;
            // マウス位置でのクロック -> こいつが保存される
            int clock_at_mouse = AppManager.clockFromXCoord( x_at_mouse );
            // 古い拡大率
            float scale0 = controller.getScaleX();
            // 新しい拡大率
            float scale1 = getScaleXFromTrackBarValue( draft );
            // 古いstdx
            int stdx0 = controller.getStartToDrawX();
            int stdx1 = (int)(clock_at_mouse * (scale1 - scale0) + stdx0);
            // 新しいhScroll.Value
            int hscroll_value = (int)(stdx1 / scale1);
            if ( hscroll_value < hScroll.getMinimum() ) {
                hscroll_value = hScroll.getMinimum();
            }
            if ( hScroll.getMaximum() < hscroll_value ) {
                hscroll_value = hScroll.getMaximum();
            }

            controller.setScaleX( scale1 );
            controller.setStartToDrawX( stdx1 );
            hScroll.setValue( hscroll_value );
            trackBar.setValue( draft );
        }
    } else {
        zoomY( e.Delta > 0 ? 1 : -1 );
    }
} else {
    // スクロール操作
    if ( e.X <= AppManager.keyWidth || pictPianoRoll.getWidth() < e.X ) {
        horizontal = false;
    }
    if ( horizontal ) {
        hScroll.setValue( computeScrollValueFromWheelDelta( e.Delta ) );
    } else {
        double new_val = (double)vScroll.getValue() - e.Delta * 10;
        int min = vScroll.getMinimum();
        int max = vScroll.getMaximum() - vScroll.getVisibleAmount();
        if ( new_val > max ) {
            vScroll.setValue( max );
        } else if ( new_val < min ) {
            vScroll.setValue( min );
        } else {
            vScroll.setValue( (int)new_val );
        }
    }
}
refreshScreen();
        }

        public void pictPianoRoll_PreviewKeyDown( Object sender, BPreviewKeyDownEventArgs e )
        {
BKeyEventArgs e0 = new BKeyEventArgs( e.getRawEvent() );
processSpecialShortcutKey( e0, true );
        }

        //BOOKMARK: iconPalette
        public void iconPalette_LocationChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.FormIconPaletteLocation = new XmlPoint( AppManager.iconPalette.getLocation() );
        }

        public void iconPalette_FormClosing( Object sender, BFormClosingEventArgs e )
        {
flipIconPaletteVisible( AppManager.iconPalette.isVisible() );
        }

        //BOOKMARK: menuVisual
        public void menuVisualProperty_CheckedChanged( Object sender, BEventArgs e )
        {
if ( menuVisualProperty.isSelected() ) {
    if ( AppManager.editorConfig.PropertyWindowStatus.WindowState == BFormWindowState.Minimized ) {
        updatePropertyPanelState( PanelState.Docked );
    } else {
        updatePropertyPanelState( PanelState.Window );
    }
} else {
    updatePropertyPanelState( PanelState.Hidden );
}
        }

        public void menuVisualOverview_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.OverviewEnabled = menuVisualOverview.isSelected();
updateLayout();
        }

        public void menuVisualMixer_Click( Object sender, BEventArgs e )
        {
boolean v = !AppManager.editorConfig.MixerVisible;
flipMixerDialogVisible( v );
requestFocus();
        }

        public void menuVisualGridline_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.setGridVisible( menuVisualGridline.isSelected() );
refreshScreen();
        }

        public void menuVisualIconPalette_Click( Object sender, BEventArgs e )
        {
boolean v = !AppManager.editorConfig.IconPaletteVisible;
flipIconPaletteVisible( v );
        }

        public void menuVisualLyrics_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.ShowLyric = menuVisualLyrics.isSelected();
        }

        public void menuVisualNoteProperty_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.ShowExpLine = menuVisualNoteProperty.isSelected();
refreshScreen();
        }

        public void menuVisualPitchLine_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.ViewAtcualPitch = menuVisualPitchLine.isSelected();
        }

        public void menuVisualControlTrack_CheckedChanged( Object sender, BEventArgs e )
        {
flipControlCurveVisible( menuVisualControlTrack.isSelected() );
        }

        public void menuVisualWaveform_CheckedChanged( Object sender, BEventArgs e )
        {
AppManager.editorConfig.ViewWaveform = menuVisualWaveform.isSelected();
updateSplitContainer2Size( true );
        }

        public void menuVisualPluginUi_DropDownOpening( Object sender, BEventArgs e )
        {

        }

        public void menuVisualPluginUiVocaloidCommon_Click( Object sender, BEventArgs e )
        {
RendererKind search = RendererKind.NULL;
//int vocaloid = 0;
if ( sender == menuVisualPluginUiVocaloid1 ) {
    search = RendererKind.VOCALOID1;
    //vocaloid = 1;
} else if ( sender == menuVisualPluginUiVocaloid2 ) {
    search = RendererKind.VOCALOID2;
    //vocaloid = 2;
} else {
    return;
}

        }

        public void menuVisualPluginUiAquesTone_Click( Object sender, BEventArgs e )
        {
boolean visible = !menuVisualPluginUiAquesTone.isSelected();
menuVisualPluginUiAquesTone.setSelected( visible );

        }

        //BOOKMARK: mixerWindow
        public void mixerWindow_FormClosing( Object sender, BFormClosingEventArgs e )
        {
flipMixerDialogVisible( AppManager.mMixerWindow.isVisible() );
        }
        
        public void mixerWindow_SoloChanged( int track, boolean solo )
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
vsq.setSolo( track, solo );
if ( AppManager.mMixerWindow != null ) {
    AppManager.mMixerWindow.updateStatus();
}
        }

        public void mixerWindow_MuteChanged( int track, boolean mute )
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
if ( track < 0 ) {
    AppManager.getBgm( -track - 1 ).mute = mute ? 1 : 0;
} else {
    vsq.setMute( track, mute );
}
if ( AppManager.mMixerWindow != null ) {
    AppManager.mMixerWindow.updateStatus();
}
        }

        public void mixerWindow_PanpotChanged( int track, int panpot )
        {
if ( track == 0 ) {
    // master
    AppManager.getVsqFile().Mixer.MasterPanpot = panpot;
} else if ( track > 0 ) {
    // slave
    AppManager.getVsqFile().Mixer.Slave.get( track - 1 ).Panpot = panpot;
} else {
    AppManager.getBgm( -track - 1 ).panpot = panpot;
}
        }

        public void mixerWindow_FederChanged( int track, int feder )
        {
if ( track == 0 ) {
    AppManager.getVsqFile().Mixer.MasterFeder = feder;
} else if ( track > 0 ) {
    AppManager.getVsqFile().Mixer.Slave.get( track - 1 ).Feder = feder;
} else {
    AppManager.getBgm( -track - 1 ).feder = feder;
}
        }

        public void mPropertyPanelContainer_StateChangeRequired( Object sender, PanelState arg )
        {
updatePropertyPanelState( arg );
        }

        public void propertyPanel_CommandExecuteRequired( Object sender, CadenciiCommand command )
        {
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( command ) );
updateDrawObjectList();
refreshScreen();
setEdited( true );
        }

        //BOOKMARK: propertyWindow
        public void propertyWindow_FormClosing( Object sender, BFormClosingEventArgs e )
        {
updatePropertyPanelState( PanelState.Hidden );
        }

        public void propertyWindow_WindowStateChanged( Object sender, BEventArgs e )
        {
if ( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Window ) {
    if ( AppManager.propertyWindow.getExtendedState() == BForm.ICONIFIED ) {
        updatePropertyPanelState( PanelState.Docked );
    }
}
        }

        public void propertyWindow_LocationOrSizeChanged( Object sender, BEventArgs e )
        {
if ( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Window ) {
    if ( AppManager.propertyWindow.getExtendedState() != BForm.ICONIFIED ) {
        Point parent = this.getLocation();
        Point proeprty = AppManager.propertyWindow.getLocation();
        AppManager.editorConfig.PropertyWindowStatus.Bounds = 
            new XmlRectangle( proeprty.x - parent.x,
                              proeprty.y - parent.y,
                              AppManager.propertyWindow.getWidth(),
                              AppManager.propertyWindow.getHeight() );
    }
}
        }

        //BOOKMARK: FormMain
        public void handleDragExit()
        {
AppManager.setEditMode( EditMode.NONE );
mIconPaletteOnceDragEntered = false;
        }


        /// <summary>
        /// アイテムがドラッグされている最中の処理を行います
        /// </summary>
        public void handleDragOver( int screen_x, int screen_y )
        {
if ( AppManager.getEditMode() != EditMode.DRAG_DROP ) {
    return;
}
Point pt = pictPianoRoll.getLocationOnScreen();
if ( !mIconPaletteOnceDragEntered ) {
    int keywidth = AppManager.keyWidth;
    Rectangle rc = new Rectangle( pt.x + keywidth, pt.y, pictPianoRoll.getWidth() - keywidth, pictPianoRoll.getHeight() );
    if ( Utility.isInRect( new Point( screen_x, screen_y ), rc ) ) {
        mIconPaletteOnceDragEntered = true;
    } else {
        return;
    }
}
BMouseEventArgs e0 = new BMouseEventArgs( BMouseButtons.Left,
                                          1,
                                          screen_x - pt.x,
                                          screen_y - pt.y,
                                          0 );
pictPianoRoll_MouseMove( this, e0 );
        }


        /// <summary>
        /// ピアノロールにドロップされたIconDynamicsHandleの受け入れ処理を行います
        /// </summary>
        public void handleDragDrop( IconDynamicsHandle handle, int screen_x, int screen_y )
        {
if( handle == null ){
    return;
}
Point locPianoroll = pictPianoRoll.getLocationOnScreen();
// ドロップ位置を特定して，アイテムを追加する
int x = screen_x - locPianoroll.x;
int y = screen_y - locPianoroll.y;
int clock1 = AppManager.clockFromXCoord( x );

// クオンタイズの処理
int unit = AppManager.getPositionQuantizeClock();
int clock = doQuantize( clock1, unit );

int note = AppManager.noteFromYCoord( y );
VsqFileEx vsq = AppManager.getVsqFile();
int clockAtPremeasure = vsq.getPreMeasureClocks();
if ( clock < clockAtPremeasure ) {
    return;
}
if ( note < 0 || 128 < note ) {
    return;
}

int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
VsqTrack work = (VsqTrack)vsq_track.clone();

if ( AppManager.mAddingEvent == null ) {
    // ここは多分起こらない
    return;
}
VsqEvent item = (VsqEvent)AppManager.mAddingEvent.clone();
item.Clock = clock;
item.ID.Note = note;
work.addEvent( item );
work.reflectDynamics();
CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected, work, vsq.AttachedCurves.get( selected - 1 ) );
AppManager.editHistory.register( vsq.executeCommand( run ) );
setEdited( true );
AppManager.setEditMode( EditMode.NONE );
refreshScreen();
        }


        /// <summary>
        /// ドラッグの開始処理を行います
        /// </summary>
        public void handleDragEnter()
        {
AppManager.setEditMode( EditMode.DRAG_DROP );
mMouseDowned = true;
        }

        public void FormMain_FormClosed( Object sender, BFormClosedEventArgs e )
        {
clearTempWave();
String tempdir = fsys.combine( AppManager.getCadenciiTempDir(), AppManager.getID() );
if ( !fsys.isDirectoryExists( tempdir ) ) {
    PortUtil.createDirectory( tempdir );
}
String log = fsys.combine( tempdir, "run.log" );
try {
    if ( fsys.isFileExists( log ) ) {
        PortUtil.deleteFile( log );
    }
    PortUtil.deleteDirectory( tempdir, true );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".FormMain_FormClosed; ex=" + ex + "\n" );
    serr.println( "FormMain#FormMain_FormClosed; ex=" + ex );
}
AppManager.stopGenerator();
VSTiDllManager.terminate();
//MidiPlayer.stop();
if ( mMidiIn != null ) {
    mMidiIn.close();
}
PlaySound.kill();
Utility.cleanupUnusedAssemblyCache();
System.exit( 0 );
        }

        public void FormMain_FormClosing( Object sender, BFormClosingEventArgs e )
        {
// 設定値を格納
if( AppManager.editorConfig.ViewWaveform ){
    AppManager.editorConfig.SplitContainer2LastDividerLocation = splitContainer2.getDividerLocation();
}
if ( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Docked ) {
    AppManager.editorConfig.PropertyWindowStatus.DockWidth = splitContainerProperty.getDividerLocation();
}
boolean cancel = handleFormClosing();
e.Cancel = cancel;
if( !cancel ){
    dispose();
}
        }
        
        /// <summary>
        /// ウィンドウが閉じようとしているときの処理を行う
        /// 戻り値がtrueの場合，ウィンドウが閉じるのをキャンセルする処理が必要
        /// </summary>
        /// <returns></returns>
        public boolean handleFormClosing()
        {
if ( isEdited() ) {
    String file = AppManager.getFileName();
    if ( file.equals( "" ) ) {
        file = "Untitled";
    } else {
        file = PortUtil.getFileName( file );
    }
    BDialogResult ret = AppManager.showMessageBox( _( "Save this sequence?" ),
                                                   _( "Affirmation" ),
                                                   org.kbinani.windows.forms.Utility.MSGBOX_YES_NO_CANCEL_OPTION,
                                                   org.kbinani.windows.forms.Utility.MSGBOX_QUESTION_MESSAGE );
    if ( ret == BDialogResult.YES ) {
        if ( AppManager.getFileName().equals( "" ) ) {
            int dr = AppManager.showModalDialog( saveXmlVsqDialog, false, this );
            if ( dr == BFileChooser.APPROVE_OPTION ) {
                AppManager.saveTo( saveXmlVsqDialog.getSelectedFile() );
            } else {
                return true;
            }
        } else {
            AppManager.saveTo( AppManager.getFileName() );
        }

    } else if ( ret == BDialogResult.CANCEL ) {
        return true;
    }
}
AppManager.editorConfig.WindowMaximized = (getExtendedState() == BForm.MAXIMIZED_BOTH);
AppManager.saveConfig();
UtauWaveGenerator.clearCache();
VConnectWaveGenerator.clearCache();

if ( mMidiIn != null ) {
    mMidiIn.close();
}
return false;
        }

        public void FormMain_LocationChanged( Object sender, BEventArgs e )
        {
if ( getExtendedState() == BForm.NORMAL ) {
    AppManager.editorConfig.WindowRect = this.getBounds();
}
        }

        public void FormMain_Load( Object sender, BEventArgs e )
        {
applyLanguage();


updateSplitContainer2Size( false );

ensureVisibleY( 60 );


boolean init_key_sound_player_immediately = true; //FormGenerateKeySoundの終了を待たずにKeySoundPlayer.initするかどうか。

if ( init_key_sound_player_immediately ) {
    try {
        KeySoundPlayer.init();
    } catch ( Exception ex ) {
        Logger.write( FormMain.class + ".FormMain_Load; ex=" + ex + "\n" );
        serr.println( "FormMain#FormMain_Load; ex=" + ex );
    }
}
        }

        public void FormGenerateKeySound_FormClosed( Object sender, BFormClosedEventArgs e )
        {
try {
    KeySoundPlayer.init();
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".FormGenerateKeySound_FormClosed; ex=" + ex + "\n" );
    serr.println( "FormMain#FormGenerateKeySound_FormClosed; ex=" + ex );
}
        }

        public void FormMain_WindowStateChanged( Object sender, BEventArgs e )
        {
int state = getExtendedState();
if ( state == BForm.NORMAL || state == BForm.MAXIMIZED_BOTH ) {
    if( state == BForm.NORMAL ){
        AppManager.editorConfig.WindowRect = this.getBounds();
    }
    // プロパティウィンドウの状態を更新
    if( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Window ){
        if( AppManager.propertyWindow.getExtendedState() != BForm.NORMAL ){
            AppManager.propertyWindow.setExtendedState( BForm.NORMAL );
        }
        if( !AppManager.propertyWindow.isVisible() ){
            AppManager.propertyWindow.setVisible( true );
        }
    }
    // ミキサーウィンドウの状態を更新
    boolean vm = AppManager.editorConfig.MixerVisible;
    if( vm != AppManager.mMixerWindow.isVisible() ){
        AppManager.mMixerWindow.setVisible( vm );
    }
    
    // アイコンパレットの状態を更新
    if ( AppManager.iconPalette != null && menuVisualIconPalette.isSelected() ) {
        if( !AppManager.iconPalette.isVisible() ){
            AppManager.iconPalette.setVisible( true );
        }
    }
    updateLayout();
    this.requestFocus();
} else if ( state == BForm.ICONIFIED ) {
    AppManager.propertyWindow.setVisible( false );
    AppManager.mMixerWindow.setVisible( false );
    if ( AppManager.iconPalette != null ) {
        AppManager.iconPalette.setVisible( false );
    }
}/* else if ( state == BForm.MAXIMIZED_BOTH ) {
#if ENABLE_PROPERTY
    AppManager.propertyWindow.setExtendedState( BForm.NORMAL );
    AppManager.propertyWindow.setVisible( AppManager.editorConfig.PropertyWindowStatus.State == PanelState.Window );
#endif
    AppManager.mMixerWindow.setVisible( AppManager.editorConfig.MixerVisible );
    if ( AppManager.iconPalette != null && menuVisualIconPalette.isSelected() ) {
        AppManager.iconPalette.setVisible( true );
    }
    this.requestFocus();
}*/
        }

        public void FormMain_MouseWheel( Object sender, BMouseEventArgs e )
        {
if ( (PortUtil.getCurrentModifierKey() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ) {
    hScroll.setValue( computeScrollValueFromWheelDelta( e.Delta ) );
} else {
    int max = vScroll.getMaximum() - vScroll.getVisibleAmount();
    int min = vScroll.getMinimum();
    double new_val = (double)vScroll.getValue() - e.Delta;
    if ( new_val > max ) {
        vScroll.setValue( max );
    } else if ( new_val < min ) {
        vScroll.setValue( min );
    } else {
        vScroll.setValue( (int)new_val );
    }
}
refreshScreen();
        }

        public void FormMain_PreviewKeyDown( Object sender, BPreviewKeyDownEventArgs e )
        {
BKeyEventArgs ex = new BKeyEventArgs( e.getRawEvent() );
processSpecialShortcutKey( ex, true );
        }

        public void handleVScrollResize( Object sender, BEventArgs e )
        {
if ( getExtendedState() != BForm.ICONIFIED ) {
    updateScrollRangeVertical();
    controller.setStartToDrawY( calculateStartToDrawY( vScroll.getValue() ) );
}
        }

        public void FormMain_Deactivate( Object sender, BEventArgs e )
        {
mFormActivated = false;
        }

        public void FormMain_Activated( Object sender, BEventArgs e )
        {
mFormActivated = true;
        }


        //BOOKMARK: menuFile
        public void menuFileRecentClear_Click( Object sender, BEventArgs e )
        {
if( AppManager.editorConfig.RecentFiles != null ){
    AppManager.editorConfig.RecentFiles.clear();
}
updateRecentFileMenu();
        }
        
        public void menuFileSaveNamed_Click( Object sender, BEventArgs e )
        {
for ( int track = 1; track < AppManager.getVsqFile().Track.size(); track++ ) {
    if ( AppManager.getVsqFile().Track.get( track ).getEventCount() == 0 ) {
        AppManager.showMessageBox(
            PortUtil.formatMessage(
                _( "Invalid note data.\nTrack {0} : {1}\n\n-> Piano roll : Blank sequence." ), track, AppManager.getVsqFile().Track.get( track ).getName()
            ),
            _APP_NAME,
            org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
            org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
        return;
    }
}

String dir = AppManager.editorConfig.getLastUsedPathOut( "xvsq" );
saveXmlVsqDialog.setSelectedFile( dir );
int dr = AppManager.showModalDialog( saveXmlVsqDialog, false, this );
if ( dr == BFileChooser.APPROVE_OPTION ) {
    String file = saveXmlVsqDialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathOut( file, ".xvsq" );
    AppManager.saveTo( file );
    updateRecentFileMenu();
    setEdited( false );
}
        }

        public void menuFileQuit_Click( Object sender, BEventArgs e )
        {
close();
        }

        public void menuFileExport_DropDownOpening( Object sender, BEventArgs e )
        {
menuFileExportWave.setEnabled( (AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getEventCount() > 0) );
        }

        public void menuFileExportMidi_Click( Object sender, BEventArgs e )
        {
if ( mDialogMidiImportAndExport == null ) {
    mDialogMidiImportAndExport = new FormMidiImExport();
}
mDialogMidiImportAndExport.listTrack.clear();
VsqFileEx vsq = (VsqFileEx)AppManager.getVsqFile().clone();

for ( int i = 0; i < vsq.Track.size(); i++ ) {
    VsqTrack track = vsq.Track.get( i );
    int notes = 0;
    for ( Iterator<VsqEvent> itr = track.getNoteEventIterator(); itr.hasNext(); ) {
        VsqEvent obj = itr.next();
        notes++;
    }
    mDialogMidiImportAndExport.listTrack.addRow( new String[] { i + "", track.getName(), notes + "" }, true );
}
mDialogMidiImportAndExport.setMode( FormMidiImExport.FormMidiMode.EXPORT );
mDialogMidiImportAndExport.setLocation( getFormPreferedLocation( mDialogMidiImportAndExport ) );
BDialogResult dr = AppManager.showModalDialog( mDialogMidiImportAndExport, this );
if ( dr == BDialogResult.OK ) {
    if ( !mDialogMidiImportAndExport.isPreMeasure() ) {
        vsq.removePart( 0, vsq.getPreMeasureClocks() );
    }
    int track_count = 0;
    for ( int i = 0; i < mDialogMidiImportAndExport.listTrack.getItemCountRow(); i++ ) {
        if ( mDialogMidiImportAndExport.listTrack.isRowChecked( i ) ) {
            track_count++;
        }
    }
    if ( track_count == 0 ) {
        return;
    }

    String dir = AppManager.editorConfig.getLastUsedPathOut( "mid" );
    saveMidiDialog.setSelectedFile( dir );
    int dialog_result = AppManager.showModalDialog( saveMidiDialog, false, this );

    if ( dialog_result == BFileChooser.APPROVE_OPTION ) {
        RandomAccessFile fs = null;
        String filename = saveMidiDialog.getSelectedFile();
        AppManager.editorConfig.setLastUsedPathOut( filename, ".mid" );
        try {
            fs = new RandomAccessFile( filename, "rw" );
            // ヘッダー
            fs.write( new byte[] { 0x4d, 0x54, 0x68, 0x64 }, 0, 4 );
            //データ長
            fs.write( (byte)0x00 );
            fs.write( (byte)0x00 );
            fs.write( (byte)0x00 );
            fs.write( (byte)0x06 );
            //フォーマット
            fs.write( (byte)0x00 );
            fs.write( (byte)0x01 );
            //トラック数
            VsqFile.writeUnsignedShort( fs, track_count );
            //時間単位
            fs.write( (byte)0x01 );
            fs.write( (byte)0xe0 );
            int count = -1;
            for ( int i = 0; i < mDialogMidiImportAndExport.listTrack.getItemCountRow(); i++ ) {
                if ( !mDialogMidiImportAndExport.listTrack.isRowChecked( i ) ) {
                    continue;
                }
                VsqTrack track = vsq.Track.get( i );
                count++;
                fs.write( new byte[] { 0x4d, 0x54, 0x72, 0x6b }, 0, 4 );
                //データ長。とりあえず0を入れておく
                fs.write( new byte[] { 0x00, 0x00, 0x00, 0x00 }, 0, 4 );
                long first_position = fs.getFilePointer();
                //トラック名
                VsqFile.writeFlexibleLengthUnsignedLong( fs, 0 );//デルタタイム
                fs.write( (byte)0xff );//ステータスタイプ
                fs.write( (byte)0x03 );//イベントタイプSequence/Track Name
                byte[] track_name = PortUtil.getEncodedByte( "Shift_JIS", track.getName() );
                fs.write( (byte)track_name.length );
                fs.write( track_name, 0, track_name.length );

                Vector<MidiEvent> events = new Vector<MidiEvent>();

                // tempo
                boolean print_tempo = mDialogMidiImportAndExport.isTempo();
                if ( print_tempo && count == 0 ) {
                    Vector<MidiEvent> tempo_events = vsq.generateTempoChange();
                    for ( int j = 0; j < tempo_events.size(); j++ ) {
                        events.add( tempo_events.get( j ) );
                    }
                }

                // timesig
                if ( mDialogMidiImportAndExport.isTimesig() && count == 0 ) {
                    Vector<MidiEvent> timesig_events = vsq.generateTimeSig();
                    for ( int j = 0; j < timesig_events.size(); j++ ) {
                        events.add( timesig_events.get( j ) );
                    }
                }

                // Notes
                if ( mDialogMidiImportAndExport.isNotes() ) {
                    for ( Iterator<VsqEvent> itr = track.getNoteEventIterator(); itr.hasNext(); ) {
                        VsqEvent ve = itr.next();
                        int clock_on = ve.Clock;
                        int clock_off = ve.Clock + ve.ID.getLength();
                        if ( !print_tempo ) {
                            // テンポを出力しない場合、テンポを500000（120）と見なしてクロックを再計算
                            double time_on = vsq.getSecFromClock( clock_on );
                            double time_off = vsq.getSecFromClock( clock_off );
                            clock_on = (int)(960.0 * time_on);
                            clock_off = (int)(960.0 * time_off);
                        }
                        MidiEvent noteon = new MidiEvent();
                        noteon.clock = clock_on;
                        noteon.firstByte = 0x90;
                        noteon.data = new int[2];
                        noteon.data[0] = ve.ID.Note;
                        noteon.data[1] = ve.ID.Dynamics;
                        events.add( noteon );
                        MidiEvent noteoff = new MidiEvent();
                        noteoff.clock = clock_off;
                        noteoff.firstByte = 0x80;
                        noteoff.data = new int[2];
                        noteoff.data[0] = ve.ID.Note;
                        noteoff.data[1] = 0x7f;
                        events.add( noteoff );
                    }
                }

                // lyric
                if ( mDialogMidiImportAndExport.isLyric() ) {
                    for ( Iterator<VsqEvent> itr = track.getNoteEventIterator(); itr.hasNext(); ) {
                        VsqEvent ve = itr.next();
                        int clock_on = ve.Clock;
                        if ( !print_tempo ) {
                            double time_on = vsq.getSecFromClock( clock_on );
                            clock_on = (int)(960.0 * time_on);
                        }
                        MidiEvent add = new MidiEvent();
                        add.clock = clock_on;
                        add.firstByte = 0xff;
                        byte[] lyric = PortUtil.getEncodedByte( "Shift_JIS", ve.ID.LyricHandle.L0.Phrase );
                        add.data = new int[lyric.length + 1];
                        add.data[0] = 0x05;
                        for ( int j = 0; j < lyric.length; j++ ) {
                            add.data[j + 1] = lyric[j];
                        }
                        events.add( add );
                    }
                }

                // vocaloid metatext
                Vector<MidiEvent> meta;
                if ( mDialogMidiImportAndExport.isVocaloidMetatext() && i > 0 ) {
                    meta = vsq.generateMetaTextEvent( i, "Shift_JIS" );
                } else {
                    meta = new Vector<MidiEvent>();
                }

                // vocaloid nrpn
                Vector<MidiEvent> vocaloid_nrpn_midievent;
                if ( mDialogMidiImportAndExport.isVocaloidNrpn() && i > 0 ) {
                    VsqNrpn[] vsqnrpn = VsqFileEx.generateNRPN( (VsqFile)vsq, i, AppManager.editorConfig.PreSendTime );
                    NrpnData[] nrpn = VsqNrpn.convert( vsqnrpn );

                    vocaloid_nrpn_midievent = new Vector<MidiEvent>();
                    for ( int j = 0; j < nrpn.length; j++ ) {
                        MidiEvent me = new MidiEvent();
                        me.clock = nrpn[j].getClock();
                        me.firstByte = 0xb0;
                        me.data = new int[2];
                        me.data[0] = nrpn[j].getParameter();
                        me.data[1] = nrpn[j].Value;
                        vocaloid_nrpn_midievent.add( me );
                    }
                } else {
                    vocaloid_nrpn_midievent = new Vector<MidiEvent>();
                }

                // midi eventを出力
                Collections.sort( events );
                long last_clock = 0;
                int events_count = events.size();
                if ( events_count > 0 ) {
                    for ( int j = 0; j < events_count; j++ ) {
                        if ( events.get( j ).clock > 0 && meta.size() > 0 ) {
                            for ( int k = 0; k < meta.size(); k++ ) {
                                VsqFile.writeFlexibleLengthUnsignedLong( fs, 0 );
                                meta.get( k ).writeData( fs );
                            }
                            meta.clear();
                            last_clock = 0;
                        }
                        long clock = events.get( j ).clock;
                        while ( vocaloid_nrpn_midievent.size() > 0 && vocaloid_nrpn_midievent.get( 0 ).clock < clock ) {
                            VsqFile.writeFlexibleLengthUnsignedLong( fs, (long)(vocaloid_nrpn_midievent.get( 0 ).clock - last_clock) );
                            last_clock = vocaloid_nrpn_midievent.get( 0 ).clock;
                            vocaloid_nrpn_midievent.get( 0 ).writeData( fs );
                            vocaloid_nrpn_midievent.removeElementAt( 0 );
                        }
                        VsqFile.writeFlexibleLengthUnsignedLong( fs, (long)(events.get( j ).clock - last_clock) );
                        events.get( j ).writeData( fs );
                        last_clock = events.get( j ).clock;
                    }
                } else {
                    int c = vocaloid_nrpn_midievent.size();
                    for ( int k = 0; k < meta.size(); k++ ) {
                        VsqFile.writeFlexibleLengthUnsignedLong( fs, 0 );
                        meta.get( k ).writeData( fs );
                    }
                    meta.clear();
                    last_clock = 0;
                    for ( int j = 0; j < c; j++ ) {
                        MidiEvent item = vocaloid_nrpn_midievent.get( j );
                        long clock = item.clock;
                        VsqFile.writeFlexibleLengthUnsignedLong( fs, (long)(clock - last_clock) );
                        item.writeData( fs );
                        last_clock = clock;
                    }
                }

                // トラックエンドを記入し、
                VsqFile.writeFlexibleLengthUnsignedLong( fs, (long)0 );
                fs.write( (byte)0xff );
                fs.write( (byte)0x2f );
                fs.write( (byte)0x00 );
                // チャンクの先頭に戻ってチャンクのサイズを記入
                long pos = fs.getFilePointer();
                fs.seek( first_position - 4 );
                VsqFile.writeUnsignedInt( fs, pos - first_position );
                // ファイルを元の位置にseek
                fs.seek( pos );
            }
        } catch ( Exception ex ) {
            Logger.write( FormMain.class + ".menuFileExportMidi_Click; ex=" + ex + "\n" );
        } finally {
            if ( fs != null ) {
                try {
                    fs.close();
                } catch ( Exception ex2 ) {
                    Logger.write( FormMain.class + ".menuFileExportMidi_Click; ex=" + ex2 + "\n" );
                }
            }
        }
    }
}
        }

        public void menuFileExportMusicXml_Click( Object sender, BEventArgs e )
        {
BFileChooser dialog = null;
try {
    VsqFileEx vsq = AppManager.getVsqFile();
    if ( vsq == null ) {
        return;
    }
    String first = AppManager.editorConfig.getLastUsedPathOut( "xml" );
    dialog = new BFileChooser();
    dialog.setSelectedFile( first );
    dialog.addFileFilter( _( "MusicXML(*.xml)|*.xml" ) );
    dialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
    int result = AppManager.showModalDialog( dialog, false, this );
    if ( result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    String file = dialog.getSelectedFile();
    String software = "Cadencii version " + BAssemblyInfo.fileVersion;
    vsq.printAsMusicXml( file, "UTF-8", software );
    AppManager.editorConfig.setLastUsedPathOut( file, ".xml" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportMusicXml_Click; ex=" + ex + "\n" );
    serr.println( "FormMain#menuFileExportMusicXml_Click; ex=" + ex );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportMusicXml_Click; ex=" + ex2 + "\n" );
            serr.println( "FormMain#menuFileExportMusicXml_Click; ex2=" + ex2 );
        }
    }
}
        }

        public void menuFileExportParaWave_Click( Object sender, BEventArgs e )
        {
// 出力するディレクトリを選択
String dir = "";
BFolderBrowser file_dialog = null;
try {
    file_dialog = new BFolderBrowser();
    String initial_dir = AppManager.editorConfig.getLastUsedPathOut( "wav" );
    file_dialog.setDescription( _( "Choose destination directory" ) );
    file_dialog.setSelectedPath( initial_dir );
    BDialogResult ret = AppManager.showModalDialog( file_dialog, this );
    if ( ret != BDialogResult.OK ) {
        return;
    }
    dir = file_dialog.getSelectedPath();
    // 1.wavはダミー
    initial_dir = fsys.combine( dir, "1.wav" );
    AppManager.editorConfig.setLastUsedPathOut( initial_dir, ".wav" );
} catch ( Exception ex ) {
} finally {
    if ( file_dialog != null ) {
        try {
            file_dialog.close();
        } catch ( Exception ex2 ) {
        }
    }
}

// 全部レンダリング済みの状態にするためのキュー
VsqFileEx vsq = AppManager.getVsqFile();
Vector<Integer> tracks = new Vector<Integer>();
int size = vsq.Track.size();
for ( int i = 1; i < size; i++ ) {
    tracks.add( i );
}
Vector<PatchWorkQueue> queue = AppManager.patchWorkCreateQueue( tracks );

// 全トラックをファイルに出力するためのキュー
int clockStart = vsq.config.StartMarkerEnabled ? vsq.config.StartMarker : 0;
int clockEnd = vsq.config.EndMarkerEnabled ? vsq.config.EndMarker : vsq.TotalClocks + 240;
if ( clockStart > clockEnd ) {
    AppManager.showMessageBox(
        _( "invalid rendering region; start>=end" ),
        _( "Error" ),
        PortUtil.OK_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
    return;
}
for ( int i = 1; i < size; i++ ) {
    PatchWorkQueue q = new PatchWorkQueue();
    q.track = i;
    q.clockStart = clockStart;
    q.clockEnd = clockEnd;
    q.file = fsys.combine( dir, i + ".wav" );
    q.renderAll = true;
    q.vsq = vsq;
    queue.add( q );
}

// 合成ダイアログを出す
FormWorker fw = null;
try {
    fw = new FormWorker();
    fw.setupUi( new FormWorkerUi( fw ) );
    fw.getUi().setTitle( _( "Synthesize" ) );
    fw.getUi().setText( _( "now synthesizing..." ) );

    SynthesizeWorker worker = new SynthesizeWorker( this );

    for( int i  = 0; i < queue.size(); i++ ){
        PatchWorkQueue q = vec.get( queue, i );
        fw.addJob( worker, "processQueue", q.getMessage(), q.getJobAmount(), q );
    }

    fw.startJob();
    AppManager.showModalDialog( fw.getUi(), this );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportParaWave; ex=" + ex + "\n" );
} finally {
    if ( fw != null ) {
        try {
            fw.getUi().close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        public void menuFileExportUst_Click( Object sender, BEventArgs e )
        {
VsqFileEx vsq = (VsqFileEx)AppManager.getVsqFile().clone();

// どのトラックを出力するか決める
int selected = AppManager.getSelected();

// 出力先のファイル名を選ぶ
BFileChooser dialog = null;
int dialog_result = BFileChooser.CANCEL_OPTION;
String file_name = "";
try {
    String last_path = AppManager.editorConfig.getLastUsedPathOut( "ust" );
    dialog = new BFileChooser();
    dialog.setSelectedFile( last_path );
    dialog.setDialogTitle( _( "Export UTAU (*.ust)" ) );
    dialog.addFileFilter( _( "UTAU Script Format(*.ust)|*.ust" ) );
    dialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
    dialog_result = AppManager.showModalDialog( dialog, false, this );
    if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    file_name = dialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathOut( file_name, ".ust" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportUst_Click; ex=" + ex + "\n" );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportUst_Click; ex=" + ex2 + "\n" );
        }
    }
}

// 出力処理
vsq.removePart( 0, vsq.getPreMeasureClocks() );
UstFile ust = new UstFile( vsq, selected );
// voice dirを設定
VsqTrack vsq_track = vsq.Track.get( selected );
VsqEvent singer = vsq_track.getSingerEventAt( 0 );
String voice_dir = "";
if ( singer != null ) {
    int program = singer.ID.IconHandle.Program;
    int size = AppManager.editorConfig.UtauSingers.size();
    if ( 0 <= program && program < size ) {
        SingerConfig cfg = AppManager.editorConfig.UtauSingers.get( program );
        voice_dir = cfg.VOICEIDSTR;
    }
}
ust.setVoiceDir( voice_dir );
ust.setWavTool( AppManager.editorConfig.PathWavtool );
int resampler_index = VsqFileEx.getTrackResamplerUsed( vsq_track );
if ( 0 <= resampler_index && resampler_index < AppManager.editorConfig.getResamplerCount() ) {
    ust.setResampler(
        AppManager.editorConfig.getResamplerAt( resampler_index ) );
}
ust.write( file_name );
        }

        public void menuFileExportVsq_Click( Object sender, BEventArgs e )
        {
VsqFileEx vsq = AppManager.getVsqFile();

// 出力先のファイル名を選ぶ
BFileChooser dialog = null;
int dialog_result = BFileChooser.CANCEL_OPTION;
String file_name = "";
try {
    String last_path = AppManager.editorConfig.getLastUsedPathOut( "vsq" );
    dialog = new BFileChooser();
    dialog.setSelectedFile( last_path );
    dialog.setDialogTitle( _( "Export VSQ (*.vsq)" ) );
    dialog.addFileFilter( _( "VSQ Format(*.vsq)|*.vsq" ) );
    dialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
    dialog_result = AppManager.showModalDialog( dialog, false, this );
    if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    file_name = dialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathOut( file_name, ".vsq" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportVsq_Click; ex=" + ex + "\n" );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportVsq_Click; ex=" + ex2 + "\n" );
        }
    }
}

// 出力処理
VsqFile tvsq = (VsqFile)vsq;
tvsq.write( file_name, AppManager.editorConfig.PreSendTime, "Shift_JIS" );
        }

        public void menuFileExportVxt_Click( Object sender, BEventArgs e )
        {
// UTAUの歌手が登録されていない場合は警告を表示
if ( AppManager.editorConfig.UtauSingers.size() <= 0 ) {
    BDialogResult dr = AppManager.showMessageBox(
        _( "UTAU singer not registered yet.\nContinue ?" ),
        _( "Info" ),
        org.kbinani.windows.forms.Utility.MSGBOX_YES_NO_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
    if ( dr != BDialogResult.YES ) {
        return;
    }
}

VsqFileEx vsq = AppManager.getVsqFile();

// 出力先のファイル名を選ぶ
BFileChooser dialog = null;
int dialog_result = BFileChooser.CANCEL_OPTION;
String file_name = "";
try {
    String last_path = AppManager.editorConfig.getLastUsedPathOut( "txt" );
    dialog = new BFileChooser();
    dialog.setSelectedFile( last_path );
    dialog.setDialogTitle( _( "Metatext for vConnect" ) );
    dialog.addFileFilter( _( "Text File(*.txt)|*.txt" ) );
    dialog.addFileFilter( _( "All Files(*.*)|*.*" ) );
    dialog_result = AppManager.showModalDialog( dialog, false, this );
    if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    file_name = dialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathOut( file_name, ".txt" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportVxt_Click; ex=" + ex + "\n" );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportVxt_Click; ex=" + ex2 + "\n" );
        }
    }
}

// 出力処理
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
BufferedWriter bw = null;
try {
    bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file_name ), "UTF-8" ) );
    String oto_ini = AppManager.editorConfig.UtauSingers.get( 0 ).VOICEIDSTR;
    // 先頭に登録されている歌手変更を検出
    VsqEvent singer = null;
    int c = vsq_track.getEventCount();
    for ( int i = 0; i < c; i++ ) {
        VsqEvent itemi = vsq_track.getEvent( i );
        if ( itemi.ID.type == VsqIDType.Singer ) {
            singer = itemi;
            break;
        }
    }
    // 歌手のプログラムチェンジから，歌手の原音設定へのパスを取得する
    if ( singer != null ) {
        int indx = singer.ID.IconHandle.Program;
        if ( 0 <= indx && indx < AppManager.editorConfig.UtauSingers.size() ) {
            oto_ini = AppManager.editorConfig.UtauSingers.get( indx ).VOICEIDSTR;
        }
    }

    // oto.iniで終わってる？
    if ( !oto_ini.endsWith( "oto.ini" ) ) {
        oto_ini = fsys.combine( oto_ini, "oto.ini" );
    }

    // 出力
    VConnectWaveGenerator.prepareMetaText(
        bw, vsq_track, oto_ini, vsq.TotalClocks, false );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportVxt_Click; ex=" + ex + "\n" );
    serr.println( FormMain.class + ".menuFileExportVxt_Click; ex=" + ex );
} finally {
    if ( bw != null ) {
        try {
            bw.close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        public void menuFileExportWave_Click( Object sender, BEventArgs e )
        {
int dialog_result = BFileChooser.CANCEL_OPTION;
String filename = "";
BFileChooser sfd = null;
try {
    String last_path = AppManager.editorConfig.getLastUsedPathOut( "wav" );
    sfd = new BFileChooser();
    sfd.setSelectedFile( last_path );
    sfd.setDialogTitle( _( "Wave Export" ) );
    sfd.addFileFilter( _( "Wave File(*.wav)|*.wav" ) );
    sfd.addFileFilter( _( "All Files(*.*)|*.*" ) );
    dialog_result = AppManager.showModalDialog( sfd, false, this );
    if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    filename = sfd.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathOut( filename, ".wav" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportWave_Click; ex=" + ex + "\n" );
} finally {
    if ( sfd != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportWave_Click; ex=" + ex2 + "\n" );
        }
    }
}

VsqFileEx vsq = AppManager.getVsqFile();
int clockStart = vsq.config.StartMarkerEnabled ? vsq.config.StartMarker : 0;
int clockEnd = vsq.config.EndMarkerEnabled ? vsq.config.EndMarker : vsq.TotalClocks + 240;
if ( clockStart > clockEnd ) {
    AppManager.showMessageBox(
        _( "invalid rendering region; start>=end" ),
        _( "Error" ),
        PortUtil.OK_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
    return;
}
Vector<Integer> other_tracks = new Vector<Integer>();
int selected = AppManager.getSelected();
for ( int i = 1; i < vsq.Track.size(); i++ ) {
    if ( i != selected ) {
        other_tracks.add( i );
    }
}
Vector<PatchWorkQueue> queue =
    AppManager.patchWorkCreateQueue( other_tracks );
PatchWorkQueue q = new PatchWorkQueue();
q.track = selected;
q.clockStart = clockStart;
q.clockEnd = clockEnd;
q.file = filename;
q.renderAll = true;
q.vsq = vsq;
// 末尾に追加
queue.add( q );
double started = PortUtil.getCurrentTime();

FormWorker fs = null;
try {
    fs = new FormWorker();
    fs.setupUi( new FormWorkerUi( fs ) );
    fs.getUi().setTitle( _( "Synthesize" ) );
    fs.getUi().setText( _( "now synthesizing..." ) );

    SynthesizeWorker worker = new SynthesizeWorker( this );

    for ( PatchWorkQueue qb : queue ) {
        fs.addJob( worker, "processQueue", qb.getMessage(), qb.getJobAmount(), qb );
    }

    fs.startJob();
    AppManager.showModalDialog( fs.getUi(), this );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileExportWave_Click; ex=" + ex + "\n" );
} finally {
    if ( fs != null ) {
        try {
            fs.getUi().close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuFileExportWave_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuFileImportMidi_Click( Object sender, BEventArgs e )
        {
if ( mDialogMidiImportAndExport == null ) {
    mDialogMidiImportAndExport = new FormMidiImExport();
}
mDialogMidiImportAndExport.listTrack.clear();
mDialogMidiImportAndExport.setMode( FormMidiImExport.FormMidiMode.IMPORT );

String dir = AppManager.editorConfig.getLastUsedPathIn( "mid" );
openMidiDialog.setSelectedFile( dir );
int dialog_result = AppManager.showModalDialog( openMidiDialog, true, this );

if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
    return;
}
mDialogMidiImportAndExport.setLocation( getFormPreferedLocation( mDialogMidiImportAndExport ) );
MidiFile mf = null;
try {
    String filename = openMidiDialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathIn( filename, ".mid" );
    mf = new MidiFile( filename );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileImportMidi_Click; ex=" + ex + "\n" );
    AppManager.showMessageBox(
        _( "Invalid MIDI file." ),
        _( "Error" ),
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
if ( mf == null ) {
    AppManager.showMessageBox(
        _( "Invalid MIDI file." ),
        _( "Error" ),
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
int count = mf.getTrackCount();
//Encoding def_enc = Encoding.GetEncoding( 0 );
for ( int i = 0; i < count; i++ ) {
    String track_name = "";
    int notes = 0;
    Vector<MidiEvent> events = mf.getMidiEventList( i );
    int events_count = events.size();

    // トラック名を取得
    for ( int j = 0; j < events_count; j++ ) {
        MidiEvent item = events.get( j );
        if ( item.firstByte == 0xff && item.data.length >= 2 && item.data[0] == 0x03 ) {
            int[] d = new int[item.data.length];
            for ( int k = 0; k < item.data.length; k++ ) {
                d[k] = 0xff & item.data[k];
            }
            track_name = PortUtil.getDecodedString( "Shift_JIS", d, 1, item.data.length - 1 );
            break;
        }
    }

    // イベント数を数える
    for ( int j = 0; j < events_count; j++ ) {
        MidiEvent item = events.get( j );
        if ( (item.firstByte & 0xf0) == 0x90 && item.data.length > 1 && item.data[1] > 0x00 ) {
            notes++;
        }
    }
    mDialogMidiImportAndExport.listTrack.addRow(
        new String[] { i + "", track_name, notes + "" }, true );
}

BDialogResult dr = AppManager.showModalDialog( mDialogMidiImportAndExport, this );
if ( dr != BDialogResult.OK ) {
    return;
}

boolean secondBasis = mDialogMidiImportAndExport.isSecondBasis();
int offsetClocks = mDialogMidiImportAndExport.getOffsetClocks();
double offsetSeconds = mDialogMidiImportAndExport.getOffsetSeconds();
boolean importFromPremeasure = mDialogMidiImportAndExport.isPreMeasure();

// インポートするしないにかかわらずテンポと拍子を取得
VsqFileEx tempo = new VsqFileEx( "Miku", 2, 4, 4, 500000 ); //テンポリスト用のVsqFile。テンポの部分のみ使用
tempo.executeCommand( VsqCommand.generateCommandChangePreMeasure( 0 ) );
boolean tempo_added = false;
boolean timesig_added = false;
tempo.TempoTable.clear();
tempo.TimesigTable.clear();
int mf_getTrackCount = mf.getTrackCount();
for ( int i = 0; i < mf_getTrackCount; i++ ) {
    Vector<MidiEvent> events = mf.getMidiEventList( i );
    boolean t_tempo_added = false;   //第iトラックからテンポをインポートしたかどうか
    boolean t_timesig_added = false; //第iトラックから拍子をインポートしたかどうか
    int last_timesig_clock = 0; // 最後に拍子変更を検出したゲートタイム
    int last_num = 4; // 最後に検出した拍子変更の分子
    int last_den = 4; // 最後に検出した拍子変更の分母
    int last_barcount = 0;
    int events_Count = events.size();
    for ( int j = 0; j < events_Count; j++ ) {
        MidiEvent itemj = events.get( j );
        if ( !tempo_added && itemj.firstByte == 0xff && itemj.data.length >= 4 && itemj.data[0] == 0x51 ) {
            boolean contains_same_clock = false;
            int size = tempo.TempoTable.size();
            // 同時刻のテンポ変更は、最初以外無視する
            for ( int k = 0; k < size; k++ ) {
                if ( tempo.TempoTable.get( k ).Clock == itemj.clock ) {
                    contains_same_clock = true;
                    break;
                }
            }
            if ( !contains_same_clock ) {
                int vtempo = itemj.data[1] << 16 | itemj.data[2] << 8 | itemj.data[3];
                tempo.TempoTable.add( new TempoTableEntry( (int)itemj.clock, vtempo, 0.0 ) );
                t_tempo_added = true;
            }
        }
        if ( !timesig_added && itemj.firstByte == 0xff && itemj.data.length >= 5 && itemj.data[0] == 0x58 ) {
            int num = itemj.data[1];
            int den = 1;
            for ( int k = 0; k < itemj.data[2]; k++ ) {
                den = den * 2;
            }
            int clock_per_bar = last_num * 480 * 4 / last_den;
            int barcount_at_itemj = last_barcount + ((int)itemj.clock - last_timesig_clock) / clock_per_bar;
            // 同時刻の拍子変更は、最初以外無視する
            int size = tempo.TimesigTable.size();
            boolean contains_same_clock = false;
            for ( int k = 0; k < size; k++ ) {
                if ( tempo.TimesigTable.get( k ).Clock == itemj.clock ) {
                    contains_same_clock = true;
                    break;
                }
            }
            if ( !contains_same_clock ) {
                tempo.TimesigTable.add( new TimeSigTableEntry( (int)itemj.clock, num, den, barcount_at_itemj ) );
                last_timesig_clock = (int)itemj.clock;
                last_den = den;
                last_num = num;
                last_barcount = barcount_at_itemj;
                t_timesig_added = true;
            }
        }
    }
    if ( t_tempo_added ) {
        tempo_added = true;
    }
    if ( t_timesig_added ) {
        timesig_added = true;
    }
    if ( timesig_added && tempo_added ) {
        // 両方ともインポート済みならexit。2個以上のトラックから、重複してテンポや拍子をインポートするのはNG（たぶん）
        break;
    }
}
boolean contains_zero = false;
int c = tempo.TempoTable.size();
for ( int i = 0; i < c; i++ ) {
    if ( tempo.TempoTable.get( i ).Clock == 0 ) {
        contains_zero = true;
        break;
    }
}
if ( !contains_zero ) {
    tempo.TempoTable.add( new TempoTableEntry( 0, 500000, 0.0 ) );
}
contains_zero = false;
// =>
// Thanks, げっぺータロー.
// BEFORE:
// c = tempo.TempoTable.size();
// AFTER:
c = tempo.TimesigTable.size();
// <=
for ( int i = 0; i < c; i++ ) {
    if ( tempo.TimesigTable.get( i ).Clock == 0 ) {
        contains_zero = true;
        break;
    }
}
if ( !contains_zero ) {
    tempo.TimesigTable.add( new TimeSigTableEntry( 0, 4, 4, 0 ) );
}
VsqFileEx work = (VsqFileEx)AppManager.getVsqFile().clone(); //後でReplaceコマンドを発行するための作業用
int preMeasureClocks = work.getPreMeasureClocks();
double sec_at_premeasure = work.getSecFromClock( preMeasureClocks );
if ( !mDialogMidiImportAndExport.isPreMeasure() ) {
    sec_at_premeasure = 0.0;
}
VsqFileEx copy_src = (VsqFileEx)tempo.clone();
if ( sec_at_premeasure != 0.0 ) {
    int t = work.TempoTable.get( 0 ).Tempo;
    VsqFileEx.shift( copy_src, sec_at_premeasure, t );
}
tempo.updateTempoInfo();
tempo.updateTimesigInfo();

// tempoをインポート
boolean import_tempo = mDialogMidiImportAndExport.isTempo();
if ( import_tempo ) {
    // 最初に、workにある全てのイベント・コントロールカーブ・ベジエ曲線をtempoのテンポテーブルに合うように、シフトする
    //ShiftClockToMatchWith( work, copy_src, work.getSecFromClock( work.getPreMeasureClocks() ) );
    //ShiftClockToMatchWith( work, copy_src, copy_src.getSecFromClock( copy_src.getPreMeasureClocks() ) );
    if ( secondBasis ) {
        shiftClockToMatchWith( work, copy_src, sec_at_premeasure );
    }

    work.TempoTable.clear();
    Vector<TempoTableEntry> list = copy_src.TempoTable;
    int list_count = list.size();
    for ( int i = 0; i < list_count; i++ ) {
        TempoTableEntry item = list.get( i );
        work.TempoTable.add( new TempoTableEntry( item.Clock, item.Tempo, item.Time ) );
    }
    work.updateTempoInfo();
}

// timesig
if ( mDialogMidiImportAndExport.isTimesig() ) {
    work.TimesigTable.clear();
    Vector<TimeSigTableEntry> list = tempo.TimesigTable;
    int list_count = vec.size( list );
    for ( int i = 0; i < list_count; i++ ) {
        TimeSigTableEntry item = list.get( i );
        work.TimesigTable.add( 
            new TimeSigTableEntry(
                item.Clock,
                item.Numerator,
                item.Denominator,
                item.BarCount ) );
    }
    Collections.sort( work.TimesigTable );
    work.updateTimesigInfo();
}

for ( int i = 0; i < mDialogMidiImportAndExport.listTrack.getItemCountRow(); i++ ) {
    if ( !mDialogMidiImportAndExport.listTrack.isRowChecked( i ) ) {
        continue;
    }
    if ( vec.size( work.Track ) + 1 > 16 ) {
        break;
    }
    VsqTrack work_track = new VsqTrack( mDialogMidiImportAndExport.listTrack.getItemAt( i, 1 ), "Miku" );

    // デフォルトの音声合成システムに切り替え
    RendererKind kind = AppManager.editorConfig.DefaultSynthesizer;
    String renderer = AppManager.getVersionStringFromRendererKind( kind );
    Vector<VsqID> singers = AppManager.getSingerListFromRendererKind( kind );
    work_track.changeRenderer( renderer, singers );

    Vector<MidiEvent> events = mf.getMidiEventList( i );
    Collections.sort( events );
    int events_count = vec.size( events );

    // note
    if ( mDialogMidiImportAndExport.isNotes() ) {
        int[] onclock_each_note = new int[128];
        int[] velocity_each_note = new int[128];
        for ( int j = 0; j < 128; j++ ) {
            onclock_each_note[j] = -1;
            velocity_each_note[j] = 64;
        }
        int last_note = -1;
        for ( int j = 0; j < events_count; j++ ) {
            MidiEvent itemj = vec.get( events, j );
            int not_closed_note = -1;
            if ( (itemj.firstByte & 0xf0) == 0x90 && itemj.data.length >= 2 && itemj.data[1] > 0 ) {
                for ( int m = 0; m < 128; m++ ) {
                    if ( onclock_each_note[m] >= 0 ) {
                        not_closed_note = m;
                        break;
                    }
                }
            }
            if ( ((itemj.firstByte & 0xf0) == 0x90 && itemj.data.length >= 2 && itemj.data[1] == 0) ||
                 ((itemj.firstByte & 0xf0) == 0x80 && itemj.data.length >= 2) ||
                 not_closed_note >= 0 ) {
                int clock_off = (int)itemj.clock;
                int note = (int)itemj.data[0];
                if ( not_closed_note >= 0 ) {
                    note = not_closed_note;
                }
                if ( onclock_each_note[note] >= 0 ) {
                    int add_clock_on = onclock_each_note[note];
                    int add_clock_off = clock_off;
                    if ( secondBasis ) {
                        double time_clock_on = tempo.getSecFromClock( onclock_each_note[note] ) + sec_at_premeasure + offsetSeconds;
                        double time_clock_off = tempo.getSecFromClock( clock_off ) + sec_at_premeasure + offsetSeconds;
                        add_clock_on = (int)work.getClockFromSec( time_clock_on );
                        add_clock_off = (int)work.getClockFromSec( time_clock_off );
                    } else {
                        add_clock_on += (importFromPremeasure ? preMeasureClocks : 0) + offsetClocks;
                        add_clock_off += (importFromPremeasure ? preMeasureClocks : 0) + offsetClocks;
                    }
                    if ( add_clock_on < 0 ) {
                        add_clock_on = 0;
                    }
                    if ( add_clock_off < 0 ) {
                        continue;
                    }
                    VsqID vid = new VsqID( 0 );
                    vid.type = VsqIDType.Anote;
                    vid.setLength( add_clock_off - add_clock_on );
                    String phrase = "a";
                    if ( mDialogMidiImportAndExport.isLyric() ) {
                        for ( int k = 0; k < events_count; k++ ) {
                            MidiEvent itemk = vec.get( events, k );
                            if ( onclock_each_note[note] <= (int)itemk.clock && (int)itemk.clock <= clock_off ) {
                                if ( itemk.firstByte == 0xff && itemk.data.length >= 2 && itemk.data[0] == 0x05 ) {
                                    int[] d = new int[itemk.data.length - 1];
                                    for ( int m = 1; m < itemk.data.length; m++ ) {
                                        d[m - 1] = 0xff & itemk.data[m];
                                    }
                                    phrase = PortUtil.getDecodedString( "Shift_JIS", d, 0, itemk.data.length );
                                    break;
                                }
                            }
                        }
                    }
                    vid.LyricHandle = new LyricHandle( phrase, "a" );
                    vid.Note = note;
                    vid.Dynamics = velocity_each_note[note];
                    // デフォルとの歌唱スタイルを適用する
                    AppManager.editorConfig.applyDefaultSingerStyle( vid );

                    // ビブラート
                    if ( AppManager.editorConfig.EnableAutoVibrato ) {
                        int note_length = vid.getLength();
                        // 音符位置での拍子を調べる
                        Timesig timesig = work.getTimesigAt( add_clock_on );

                        // ビブラートを自動追加するかどうかを決める閾値
                        int threshold = AppManager.editorConfig.AutoVibratoThresholdLength;
                        if ( note_length >= threshold ) {
                            int vibrato_clocks = 0;
                            DefaultVibratoLengthEnum vib_length = AppManager.editorConfig.DefaultVibratoLength;
                            if ( vib_length == DefaultVibratoLengthEnum.L100 ) {
                                vibrato_clocks = note_length;
                            } else if ( vib_length == DefaultVibratoLengthEnum.L50 ) {
                                vibrato_clocks = note_length / 2;
                            } else if ( vib_length == DefaultVibratoLengthEnum.L66 ) {
                                vibrato_clocks = note_length * 2 / 3;
                            } else if ( vib_length == DefaultVibratoLengthEnum.L75 ) {
                                vibrato_clocks = note_length * 3 / 4;
                            }
                            // とりあえずVOCALOID2のデフォルトビブラートの設定を使用
                            vid.VibratoHandle = AppManager.editorConfig.createAutoVibrato( SynthesizerType.VOCALOID2, vibrato_clocks );
                            vid.VibratoDelay = note_length - vibrato_clocks;
                        }
                    }

                    VsqEvent ve = new VsqEvent( add_clock_on, vid );
                    work_track.addEvent( ve );
                    onclock_each_note[note] = -1;
                }
            }
            if ( (itemj.firstByte & 0xf0) == 0x90 && itemj.data.length >= 2 && itemj.data[1] > 0 ) {
                int note = itemj.data[0];
                onclock_each_note[note] = (int)itemj.clock;
                int vel = itemj.data[1];
                velocity_each_note[note] = vel;
                last_note = note;
            }
        }

        int track = vec.size( work.Track );
        CadenciiCommand run_add =
            VsqFileEx.generateCommandAddTrack(
                work_track,
                new VsqMixerEntry( 0, 0, 0, 0 ),
                track,
                new BezierCurves() );
        work.executeCommand( run_add );
    }
}

CadenciiCommand lastrun = VsqFileEx.generateCommandReplace( work );
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( lastrun ) );
setEdited( true );
refreshScreen();
        }

        public void menuFileImportUst_Click( Object sender, BEventArgs e )
        {
BFileChooser dialog = null;
try {
    // 読み込むファイルを選ぶ
    String dir = AppManager.editorConfig.getLastUsedPathIn( "ust" );
    dialog = new BFileChooser();
    dialog.setSelectedFile( dir );
    int dialog_result = AppManager.showModalDialog( dialog, true, this );
    if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
        return;
    }
    String file = dialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathIn( file, ".ust" );

    // ustを読み込む
    UstFile ust = new UstFile( file );

    // vsqに変換
    VsqFile vsq = new VsqFile( ust );
    vsq.insertBlank( 0, vsq.getPreMeasureClocks() );

    // RendererKindをUTAUに指定
    for ( int i = 1; i < vec.size( vsq.Track ); i++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, i );
        VsqFileEx.setTrackRendererKind( vsq_track, RendererKind.UTAU );
    }

    // unknownな歌手とresamplerを何とかする
    ByRef<String> ref_resampler = new ByRef<String>( ust.getResampler() );
    ByRef<String> ref_singer = new ByRef<String>( ust.getVoiceDir() );
    checkUnknownResamplerAndSinger( ref_resampler, ref_singer );

    // 歌手変更を何とかする
    int program = 0;
    for ( int i = 0; i < vec.size( AppManager.editorConfig.UtauSingers ); i++ ) {
        SingerConfig sc = vec.get( AppManager.editorConfig.UtauSingers, i );
        if ( sc == null ) {
            continue;
        }
        if ( str.compare( sc.VOICEIDSTR, ref_singer.value ) ) {
            program = i;
            break;
        }
    }
    // 歌手変更のテンプレートを作成
    VsqID singer_id = Utility.getSingerID( RendererKind.UTAU, program, 0 );
    if ( singer_id == null ) {
        singer_id = new VsqID();
        singer_id.type = VsqIDType.Singer;
        singer_id.IconHandle = new IconHandle();
        singer_id.IconHandle.Program = program;
        singer_id.IconHandle.IconID = "$0401" + PortUtil.toHexString( 0, 4 );
    }
    // トラックの歌手変更イベントをすべて置き換える
    for ( int i = 1; i < vec.size( vsq.Track ); i++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, i );
        int c = vsq_track.getEventCount();
        for ( int j = 0; j < c; j++ ) {
            VsqEvent itemj = vsq_track.getEvent( j );
            if ( itemj.ID.type == VsqIDType.Singer ) {
                itemj.ID = (VsqID)singer_id.clone();
            }
        }
    }

    // resamplerUsedを更新(可能なら)
    for ( int j = 1; j < vec.size( vsq.Track ); j++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, j );
        for ( int i = 0; i < AppManager.editorConfig.getResamplerCount(); i++ ) {
            String resampler = AppManager.editorConfig.getResamplerAt( i );
            if ( str.compare( resampler, ref_resampler.value ) ) {
                VsqFileEx.setTrackResamplerUsed( vsq_track, i );
                break;
            }
        }
    }

    // 読込先のvsqと，インポートするvsqではテンポテーブルがずれているので，
    // 読み込んだ方のvsqの内容を，現在のvsqと合致するように編集する
    VsqFileEx dst = (VsqFileEx)AppManager.getVsqFile().clone();
    vsq.adjustClockToMatchWith( dst.TempoTable );

    // トラック数の上限になるまで挿入を実行
    int size = vsq.Track.size();
    for ( int i = 1; i < size; i++ ) {
        if ( dst.Track.size() + 1 >= VsqFile.MAX_TRACKS + 1 ) {
            // トラック数の上限
            break;
        }
        dst.Track.add( vsq.Track.get( i ) );
        dst.AttachedCurves.add( new BezierCurves() );
        dst.Mixer.Slave.add( new VsqMixerEntry() );
    }

    // コマンドを発行して実行
    CadenciiCommand run = VsqFileEx.generateCommandReplace( dst );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    AppManager.mMixerWindow.updateStatus();
    setEdited( true );
    refreshScreen( true );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileImportUst_Click; ex=" + ex + "\t" );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex ) {
        }
    }
}
        }

        public void menuFileImportVsq_Click( Object sender, BEventArgs e )
        {
String dir = AppManager.editorConfig.getLastUsedPathIn( AppManager.editorConfig.LastUsedExtension );
openMidiDialog.setSelectedFile( dir );
int dialog_result = AppManager.showModalDialog( openMidiDialog, true, this );

if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
    return;
}
VsqFileEx vsq = null;
String filename = openMidiDialog.getSelectedFile();
AppManager.editorConfig.setLastUsedPathIn( filename, ".vsq" );
try {
    vsq = new VsqFileEx( filename, "Shift_JIS" );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileImportVsq_Click; ex=" + ex + "\n" );
    AppManager.showMessageBox( _( "Invalid VSQ/VOCALOID MIDI file" ), _( "Error" ), org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION, org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
if ( mDialogMidiImportAndExport == null ) {
    mDialogMidiImportAndExport = new FormMidiImExport();
}
mDialogMidiImportAndExport.listTrack.clear();
for ( int track = 1; track < vsq.Track.size(); track++ ) {
    mDialogMidiImportAndExport.listTrack.addRow( new String[] { 
        track + "", 
        vsq.Track.get( track ).getName(),
        vsq.Track.get( track ).getEventCount() + "" }, true );
}
mDialogMidiImportAndExport.setMode( FormMidiImExport.FormMidiMode.IMPORT_VSQ );
mDialogMidiImportAndExport.setTempo( false );
mDialogMidiImportAndExport.setTimesig( false );
mDialogMidiImportAndExport.setLocation( getFormPreferedLocation( mDialogMidiImportAndExport ) );
BDialogResult dr = AppManager.showModalDialog( mDialogMidiImportAndExport, this );
if ( dr != BDialogResult.OK ) {
    return;
}

Vector<Integer> add_track = new Vector<Integer>();
for ( int i = 0; i < mDialogMidiImportAndExport.listTrack.getItemCountRow(); i++ ) {
    if ( mDialogMidiImportAndExport.listTrack.isRowChecked( i ) ) {
        add_track.add( i + 1 );
    }
}
if ( add_track.size() <= 0 ) {
    return;
}

VsqFileEx replace = (VsqFileEx)AppManager.getVsqFile().clone();
double premeasure_sec_replace = replace.getSecFromClock( replace.getPreMeasureClocks() );
double premeasure_sec_vsq = vsq.getSecFromClock( vsq.getPreMeasureClocks() );

if ( mDialogMidiImportAndExport.isTempo() ) {
    shiftClockToMatchWith( replace, vsq, premeasure_sec_replace - premeasure_sec_vsq );
    // テンポテーブルを置き換え
    replace.TempoTable.clear();
    for ( int i = 0; i < vsq.TempoTable.size(); i++ ) {
        replace.TempoTable.add( (TempoTableEntry)vsq.TempoTable.get( i ).clone() );
    }
    replace.updateTempoInfo();
    replace.updateTotalClocks();
}

if ( mDialogMidiImportAndExport.isTimesig() ) {
    // 拍子をリプレースする場合
    replace.TimesigTable.clear();
    for ( int i = 0; i < vsq.TimesigTable.size(); i++ ) {
        replace.TimesigTable.add( (TimeSigTableEntry)vsq.TimesigTable.get( i ).clone() );
    }
    replace.updateTimesigInfo();
}

for ( Iterator<Integer> itr = add_track.iterator(); itr.hasNext(); ) {
    int track = itr.next();
    if ( replace.Track.size() + 1 >= 16 ) {
        break;
    }
    if ( !mDialogMidiImportAndExport.isTempo() ) {
        // テンポをリプレースしない場合。インポートするトラックのクロックを調節する
        for ( Iterator<VsqEvent> itr2 = vsq.Track.get( track ).getEventIterator(); itr2.hasNext(); ) {
            VsqEvent item = itr2.next();
            if ( item.ID.type == VsqIDType.Singer && item.Clock == 0 ) {
                continue;
            }
            int clock = item.Clock;
            double sec_start = vsq.getSecFromClock( clock ) - premeasure_sec_vsq + premeasure_sec_replace;
            double sec_end = vsq.getSecFromClock( clock + item.ID.getLength() ) - premeasure_sec_vsq + premeasure_sec_replace;
            int clock_start = (int)replace.getClockFromSec( sec_start );
            int clock_end = (int)replace.getClockFromSec( sec_end );
            item.Clock = clock_start;
            item.ID.setLength( clock_end - clock_start );
            if ( item.ID.VibratoHandle != null ) {
                double sec_vib_start = vsq.getSecFromClock( clock + item.ID.VibratoDelay ) - premeasure_sec_vsq + premeasure_sec_replace;
                int clock_vib_start = (int)replace.getClockFromSec( sec_vib_start );
                item.ID.VibratoDelay = clock_vib_start - clock_start;
                item.ID.VibratoHandle.setLength( clock_end - clock_vib_start );
            }
        }

        // コントロールカーブをシフト
        for ( CurveType ct : Utility.CURVE_USAGE ) {
            VsqBPList item = vsq.Track.get( track ).getCurve( ct.getName() );
            if ( item == null ) {
                continue;
            }
            VsqBPList repl = new VsqBPList( item.getName(), item.getDefault(), item.getMinimum(), item.getMaximum() );
            for ( int i = 0; i < item.size(); i++ ) {
                int clock = item.getKeyClock( i );
                int value = item.getElement( i );
                double sec = vsq.getSecFromClock( clock ) - premeasure_sec_vsq + premeasure_sec_replace;
                if ( sec >= premeasure_sec_replace ) {
                    int clock_new = (int)replace.getClockFromSec( sec );
                    repl.add( clock_new, value );
                }
            }
            vsq.Track.get( track ).setCurve( ct.getName(), repl );
        }

        // ベジエカーブをシフト
        for ( CurveType ct : Utility.CURVE_USAGE ) {
            Vector<BezierChain> list = vsq.AttachedCurves.get( track - 1 ).get( ct );
            if ( list == null ) {
                continue;
            }
            for ( Iterator<BezierChain> itr2 = list.iterator(); itr2.hasNext(); ) {
                BezierChain chain = itr2.next();
                for ( Iterator<BezierPoint> itr3 = chain.points.iterator(); itr3.hasNext(); ) {
                    BezierPoint point = itr3.next();
                    PointD bse = new PointD( replace.getClockFromSec( vsq.getSecFromClock( point.getBase().getX() ) - premeasure_sec_vsq + premeasure_sec_replace ),
                                             point.getBase().getY() );
                    PointD ctrl_r = new PointD( replace.getClockFromSec( vsq.getSecFromClock( point.controlLeft.getX() ) - premeasure_sec_vsq + premeasure_sec_replace ),
                                                point.controlLeft.getY() );
                    PointD ctrl_l = new PointD( replace.getClockFromSec( vsq.getSecFromClock( point.controlRight.getX() ) - premeasure_sec_vsq + premeasure_sec_replace ),
                                                point.controlRight.getY() );
                    point.setBase( bse );
                    point.controlLeft = ctrl_l;
                    point.controlRight = ctrl_r;
                }
            }
        }
    }
    replace.Mixer.Slave.add( new VsqMixerEntry() );
    replace.Track.add( vsq.Track.get( track ) );
    replace.AttachedCurves.add( vsq.AttachedCurves.get( track - 1 ) );
}

// コマンドを発行し、実行
CadenciiCommand run = VsqFileEx.generateCommandReplace( replace );
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
setEdited( true );
        }

        public void menuFileOpenUst_Click( Object sender, BEventArgs e )
        {
if ( !dirtyCheck() ) {
    return;
}

String dir = AppManager.editorConfig.getLastUsedPathIn( "ust" );
openUstDialog.setSelectedFile( dir );
int dialog_result = AppManager.showModalDialog( openUstDialog, true, this );

if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
    return;
}

try {
    String filename = openUstDialog.getSelectedFile();
    AppManager.editorConfig.setLastUsedPathIn( filename, ".ust" );
    
    // ust読み込み
    UstFile ust = new UstFile( filename );
    
    // vsqに変換
    VsqFileEx vsq = new VsqFileEx( ust );
    vsq.insertBlank( 0, vsq.getPreMeasureClocks() );
    
    // すべてのトラックの合成器指定をUTAUにする
    for ( int i = 1; i < vec.size( vsq.Track ); i++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, i );
        VsqFileEx.setTrackRendererKind( vsq_track, RendererKind.UTAU );
    }

    // unknownな歌手やresamplerを何とかする
    ByRef<String> ref_resampler = new ByRef<String>( ust.getResampler() );
    ByRef<String> ref_singer = new ByRef<String>( ust.getVoiceDir() );
    checkUnknownResamplerAndSinger( ref_resampler, ref_singer );

    // 歌手変更を何とかする
    int program = 0;
    for ( int i = 0; i < vec.size( AppManager.editorConfig.UtauSingers ); i++ ) {
        SingerConfig sc = vec.get( AppManager.editorConfig.UtauSingers, i );
        if ( sc == null ) {
            continue;
        }
        if ( str.compare( sc.VOICEIDSTR, ref_singer.value ) ) {
            program = i;
            break;
        }
    }
    // 歌手変更のテンプレートを作成
    VsqID singer_id = Utility.getSingerID( RendererKind.UTAU, program, 0 );
    if ( singer_id == null ) {
        singer_id = new VsqID();
        singer_id.type = VsqIDType.Singer;
        singer_id.IconHandle = new IconHandle();
        singer_id.IconHandle.Program = program;
        singer_id.IconHandle.IconID = "$0401" + PortUtil.toHexString( 0, 4 );
    }
    // トラックの歌手変更イベントをすべて置き換える
    for ( int i = 1; i < vec.size( vsq.Track ); i++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, i );
        int c = vsq_track.getEventCount();
        for ( int j = 0; j < c; j++ ) {
            VsqEvent itemj = vsq_track.getEvent( j );
            if ( itemj.ID.type == VsqIDType.Singer ) {
                itemj.ID = (VsqID)singer_id.clone();
            }
        }
    }

    // resamplerUsedを更新(可能なら)
    for ( int j = 1; j < vec.size( vsq.Track ); j++ ) {
        VsqTrack vsq_track = vec.get( vsq.Track, j );
        for ( int i = 0; i < AppManager.editorConfig.getResamplerCount(); i++ ) {
            String resampler = AppManager.editorConfig.getResamplerAt( i );
            if ( str.compare( resampler, ref_resampler.value ) ) {
                VsqFileEx.setTrackResamplerUsed( vsq_track, i );
                break;
            }
        }
    }
    
    clearExistingData();
    AppManager.setVsqFile( vsq );
    setEdited( true );
    AppManager.mMixerWindow.updateStatus();
    clearTempWave();
    updateDrawObjectList();
    refreshScreen();

} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileOpenUst_Click; ex=" + ex + "\n" );
}
        }

        public void menuFileOpenVsq_Click( Object sender, BEventArgs e )
        {
if ( !dirtyCheck() ) {
    return;
}

String[] filters = openMidiDialog.getChoosableFileFilter();
String filter = "";
for ( String f : filters ) {
    if ( f.endsWith( AppManager.editorConfig.LastUsedExtension ) ) {
        filter = f;
        break;
    }
}

openMidiDialog.setFileFilter( filter );
String dir = AppManager.editorConfig.getLastUsedPathIn( filter );
openMidiDialog.setSelectedFile( dir );
int dialog_result = AppManager.showModalDialog( openMidiDialog, true, this );
String ext = ".vsq";
if ( dialog_result == BFileChooser.APPROVE_OPTION ) {
    if ( openMidiDialog.getFileFilter().endsWith( ".mid" ) ) {
        AppManager.editorConfig.LastUsedExtension = ".mid";
        ext = ".mid";
    } else if ( openMidiDialog.getFileFilter().endsWith( ".vsq" ) ) {
        AppManager.editorConfig.LastUsedExtension = ".vsq";
        ext = ".vsq";
    }
} else {
    return;
}
try {
    String filename = openMidiDialog.getSelectedFile();
    VsqFileEx vsq = new VsqFileEx( filename, "Shift_JIS" );
    AppManager.editorConfig.setLastUsedPathIn( filename, ext );
    AppManager.setVsqFile( vsq );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuFileOpenVsq_Click; ex=" + ex + "\n" );
    AppManager.showMessageBox(
        _( "Invalid VSQ/VOCALOID MIDI file" ),
        _( "Error" ),
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
AppManager.setSelected( 1 );
clearExistingData();
setEdited( true );
AppManager.mMixerWindow.updateStatus();
clearTempWave();
updateDrawObjectList();
refreshScreen();
        }

        //BOOKMARK: menuSetting
        public void menuSettingDefaultSingerStyle_Click( Object sender, BEventArgs e )
        {
FormSingerStyleConfig dlg = null;
try {
    dlg = new FormSingerStyleConfig();
    dlg.setPMBendDepth( AppManager.editorConfig.DefaultPMBendDepth );
    dlg.setPMBendLength( AppManager.editorConfig.DefaultPMBendLength );
    dlg.setPMbPortamentoUse( AppManager.editorConfig.DefaultPMbPortamentoUse );
    dlg.setDEMdecGainRate( AppManager.editorConfig.DefaultDEMdecGainRate );
    dlg.setDEMaccent( AppManager.editorConfig.DefaultDEMaccent );

    int selected = AppManager.getSelected();
    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        AppManager.editorConfig.DefaultPMBendDepth = dlg.getPMBendDepth();
        AppManager.editorConfig.DefaultPMBendLength = dlg.getPMBendLength();
        AppManager.editorConfig.DefaultPMbPortamentoUse = dlg.getPMbPortamentoUse();
        AppManager.editorConfig.DefaultDEMdecGainRate = dlg.getDEMdecGainRate();
        AppManager.editorConfig.DefaultDEMaccent = dlg.getDEMaccent();
        if ( dlg.getApplyCurrentTrack() ) {
            VsqFileEx vsq = AppManager.getVsqFile();
            VsqTrack vsq_track = vsq.Track.get( selected );
            VsqTrack copy = (VsqTrack)vsq_track.clone();
            boolean changed = false;
            for ( int i = 0; i < copy.getEventCount(); i++ ) {
                if ( copy.getEvent( i ).ID.type == VsqIDType.Anote ) {
                    AppManager.editorConfig.applyDefaultSingerStyle( copy.getEvent( i ).ID );
                    changed = true;
                }
            }
            if ( changed ) {
                CadenciiCommand run = 
                    VsqFileEx.generateCommandTrackReplace(
                        selected,
                        copy,
                        vsq.AttachedCurves.get( selected - 1 ) );
                AppManager.editHistory.register( vsq.executeCommand( run ) );
                updateDrawObjectList();
                refreshScreen();
            }
        }
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuSettingDefaultSingerStyle_Click; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuSettingDefaultSingerStyle_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuSettingGameControlerLoad_Click( Object sender, BEventArgs e )
        {
loadGameControler();
        }

        public void menuSettingGameControlerRemove_Click( Object sender, BEventArgs e )
        {
removeGameControler();
        }

        public void menuSettingGameControlerSetting_Click( Object sender, BEventArgs e )
        {
FormGameControlerConfig dlg = null;
try {
    dlg = new FormGameControlerConfig();
    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        AppManager.editorConfig.GameControlerRectangle = dlg.getRectangle();
        AppManager.editorConfig.GameControlerTriangle = dlg.getTriangle();
        AppManager.editorConfig.GameControlerCircle = dlg.getCircle();
        AppManager.editorConfig.GameControlerCross = dlg.getCross();
        AppManager.editorConfig.GameControlL1 = dlg.getL1();
        AppManager.editorConfig.GameControlL2 = dlg.getL2();
        AppManager.editorConfig.GameControlR1 = dlg.getR1();
        AppManager.editorConfig.GameControlR2 = dlg.getR2();
        AppManager.editorConfig.GameControlSelect = dlg.getSelect();
        AppManager.editorConfig.GameControlStart = dlg.getStart();
        AppManager.editorConfig.GameControlPovDown = dlg.getPovDown();
        AppManager.editorConfig.GameControlPovLeft = dlg.getPovLeft();
        AppManager.editorConfig.GameControlPovUp = dlg.getPovUp();
        AppManager.editorConfig.GameControlPovRight = dlg.getPovRight();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuSettingGrameControlerSetting_Click; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuSettingGameControlerSetting_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuSettingPreference_Click( Object sender, BEventArgs e )
        {
try {
    if ( mDialogPreference == null ) {
        mDialogPreference = new Preference();
    }
    mDialogPreference.setBaseFont( new Font( AppManager.editorConfig.BaseFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE9 ) );
    mDialogPreference.setScreenFont( new Font( AppManager.editorConfig.ScreenFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE9 ) );
    mDialogPreference.setWheelOrder( AppManager.editorConfig.WheelOrder );
    mDialogPreference.setCursorFixed( AppManager.editorConfig.CursorFixed );
    mDialogPreference.setDefaultVibratoLength( AppManager.editorConfig.DefaultVibratoLength );
    mDialogPreference.setAutoVibratoThresholdLength( AppManager.editorConfig.AutoVibratoThresholdLength );
    mDialogPreference.setAutoVibratoType1( AppManager.editorConfig.AutoVibratoType1 );
    mDialogPreference.setAutoVibratoType2( AppManager.editorConfig.AutoVibratoType2 );
    mDialogPreference.setAutoVibratoTypeCustom( AppManager.editorConfig.AutoVibratoTypeCustom );
    mDialogPreference.setEnableAutoVibrato( AppManager.editorConfig.EnableAutoVibrato );
    mDialogPreference.setPreSendTime( AppManager.editorConfig.PreSendTime );
    mDialogPreference.setControlCurveResolution( AppManager.editorConfig.ControlCurveResolution );
    mDialogPreference.setDefaultSingerName( AppManager.editorConfig.DefaultSingerName );
    mDialogPreference.setScrollHorizontalOnWheel( AppManager.editorConfig.ScrollHorizontalOnWheel );
    mDialogPreference.setMaximumFrameRate( AppManager.editorConfig.MaximumFrameRate );
    mDialogPreference.setKeepLyricInputMode( AppManager.editorConfig.KeepLyricInputMode );
    mDialogPreference.setPxTrackHeight( AppManager.editorConfig.PxTrackHeight );
    mDialogPreference.setMouseHoverTime( AppManager.editorConfig.getMouseHoverTime() );
    mDialogPreference.setPlayPreviewWhenRightClick( AppManager.editorConfig.PlayPreviewWhenRightClick );
    mDialogPreference.setCurveSelectingQuantized( AppManager.editorConfig.CurveSelectingQuantized );
    mDialogPreference.setCurveVisibleAccent( AppManager.editorConfig.CurveVisibleAccent );
    mDialogPreference.setCurveVisibleBre( AppManager.editorConfig.CurveVisibleBreathiness );
    mDialogPreference.setCurveVisibleBri( AppManager.editorConfig.CurveVisibleBrightness );
    mDialogPreference.setCurveVisibleCle( AppManager.editorConfig.CurveVisibleClearness );
    mDialogPreference.setCurveVisibleDecay( AppManager.editorConfig.CurveVisibleDecay );
    mDialogPreference.setCurveVisibleDyn( AppManager.editorConfig.CurveVisibleDynamics );
    mDialogPreference.setCurveVisibleGen( AppManager.editorConfig.CurveVisibleGendorfactor );
    mDialogPreference.setCurveVisibleOpe( AppManager.editorConfig.CurveVisibleOpening );
    mDialogPreference.setCurveVisiblePit( AppManager.editorConfig.CurveVisiblePit );
    mDialogPreference.setCurveVisiblePbs( AppManager.editorConfig.CurveVisiblePbs );
    mDialogPreference.setCurveVisiblePor( AppManager.editorConfig.CurveVisiblePortamento );
    mDialogPreference.setCurveVisibleVel( AppManager.editorConfig.CurveVisibleVelocity );
    mDialogPreference.setCurveVisibleVibratoDepth( AppManager.editorConfig.CurveVisibleVibratoDepth );
    mDialogPreference.setCurveVisibleVibratoRate( AppManager.editorConfig.CurveVisibleVibratoRate );
    mDialogPreference.setCurveVisibleFx2Depth( AppManager.editorConfig.CurveVisibleFx2Depth );
    mDialogPreference.setCurveVisibleHarmonics( AppManager.editorConfig.CurveVisibleHarmonics );
    mDialogPreference.setCurveVisibleReso1( AppManager.editorConfig.CurveVisibleReso1 );
    mDialogPreference.setCurveVisibleReso2( AppManager.editorConfig.CurveVisibleReso2 );
    mDialogPreference.setCurveVisibleReso3( AppManager.editorConfig.CurveVisibleReso3 );
    mDialogPreference.setCurveVisibleReso4( AppManager.editorConfig.CurveVisibleReso4 );
    mDialogPreference.setCurveVisibleEnvelope( AppManager.editorConfig.CurveVisibleEnvelope );
    mDialogPreference.setMidiInPort( AppManager.editorConfig.MidiInPort.PortNumber );
    Vector<String> resamplers = new Vector<String>();
    Vector<Boolean> with_wine = new Vector<Boolean>();
    int size = AppManager.editorConfig.getResamplerCount();
    for ( int i = 0; i < size; i++ ) {
        resamplers.add( AppManager.editorConfig.getResamplerAt( i ) );
        with_wine.add( AppManager.editorConfig.isResamplerWithWineAt( i ) );
    }
    mDialogPreference.setResamplersConfig( resamplers, with_wine );
    mDialogPreference.setPathWavtool( AppManager.editorConfig.PathWavtool );
    mDialogPreference.setWavtoolWithWine( AppManager.editorConfig.WavtoolWithWine );
    mDialogPreference.setUtausingers( AppManager.editorConfig.UtauSingers );
    mDialogPreference.setSelfDeRomantization( AppManager.editorConfig.SelfDeRomanization );
    mDialogPreference.setAutoBackupIntervalMinutes( AppManager.editorConfig.AutoBackupIntervalMinutes );
    mDialogPreference.setUseSpaceKeyAsMiddleButtonModifier( AppManager.editorConfig.UseSpaceKeyAsMiddleButtonModifier );
    mDialogPreference.setPathAquesTone( AppManager.editorConfig.PathAquesTone );
    mDialogPreference.setUseProjectCache( AppManager.editorConfig.UseProjectCache );
    mDialogPreference.setAquesToneRequired( !AppManager.editorConfig.DoNotUseAquesTone );
    mDialogPreference.setVocaloid1Required( !AppManager.editorConfig.DoNotUseVocaloid1 );
    mDialogPreference.setVocaloid2Required( !AppManager.editorConfig.DoNotUseVocaloid2 );
    mDialogPreference.setBufferSize( AppManager.editorConfig.BufferSizeMilliSeconds );
    mDialogPreference.setDefaultSynthesizer( AppManager.editorConfig.DefaultSynthesizer );
    mDialogPreference.setUseUserDefinedAutoVibratoType( AppManager.editorConfig.UseUserDefinedAutoVibratoType );
    mDialogPreference.setWinePrefix( AppManager.editorConfig.WinePrefix );
    mDialogPreference.setWineTop( AppManager.editorConfig.WineTop );
    mDialogPreference.setWineBuiltin( AppManager.editorConfig.WineTopBuiltin );
    mDialogPreference.setEnableWideCharacterWorkaround( AppManager.editorConfig.UseWideCharacterWorkaround );

    String old_wine_prefix = AppManager.editorConfig.WinePrefix;
    String old_wine_top = AppManager.editorConfig.getWineTop();

    mDialogPreference.setLocation( getFormPreferedLocation( mDialogPreference ) );

    BDialogResult dr = AppManager.showModalDialog( mDialogPreference, this );
    if ( dr == BDialogResult.OK ) {
        String old_super_font_name = AppManager.editorConfig.BaseFontName;
        float old_super_font_size = AppManager.editorConfig.BaseFontSize;
        Font new_super_font = mDialogPreference.getBaseFont();
        if ( !old_super_font_name.equals( new_super_font.getName() ) ||
             old_super_font_size != new_super_font.getSize2D() ) {
            AppManager.editorConfig.BaseFontName = mDialogPreference.getBaseFont().getName();
            AppManager.editorConfig.BaseFontSize = mDialogPreference.getBaseFont().getSize2D();
            updateMenuFonts();
        }

        AppManager.editorConfig.ScreenFontName = mDialogPreference.getScreenFont().getName();
        AppManager.editorConfig.WheelOrder = mDialogPreference.getWheelOrder();
        AppManager.editorConfig.CursorFixed = mDialogPreference.isCursorFixed();

        AppManager.editorConfig.DefaultVibratoLength = mDialogPreference.getDefaultVibratoLength();
        AppManager.editorConfig.AutoVibratoThresholdLength = mDialogPreference.getAutoVibratoThresholdLength();
        AppManager.editorConfig.AutoVibratoType1 = mDialogPreference.getAutoVibratoType1();
        AppManager.editorConfig.AutoVibratoType2 = mDialogPreference.getAutoVibratoType2();
        AppManager.editorConfig.AutoVibratoTypeCustom = mDialogPreference.getAutoVibratoTypeCustom();

        AppManager.editorConfig.EnableAutoVibrato = mDialogPreference.isEnableAutoVibrato();
        AppManager.editorConfig.PreSendTime = mDialogPreference.getPreSendTime();
        AppManager.editorConfig.Language = mDialogPreference.getLanguage();
        if ( !Messaging.getLanguage().equals( AppManager.editorConfig.Language ) ) {
            Messaging.setLanguage( AppManager.editorConfig.Language );
            applyLanguage();
            mDialogPreference.applyLanguage();
            AppManager.mMixerWindow.applyLanguage();
            if ( mVersionInfo != null ) {
                mVersionInfo.applyLanguage();
            }
            AppManager.propertyWindow.applyLanguage();
            AppManager.propertyPanel.updateValue( AppManager.getSelected() );
            if( mDialogMidiImportAndExport != null ){
                mDialogMidiImportAndExport.applyLanguage();
            }
        }

        AppManager.editorConfig.ControlCurveResolution = mDialogPreference.getControlCurveResolution();
        AppManager.editorConfig.DefaultSingerName = mDialogPreference.getDefaultSingerName();
        AppManager.editorConfig.ScrollHorizontalOnWheel = mDialogPreference.isScrollHorizontalOnWheel();
        AppManager.editorConfig.MaximumFrameRate = mDialogPreference.getMaximumFrameRate();
        int fps = 1000 / AppManager.editorConfig.MaximumFrameRate;
        timer.setDelay( (fps <= 0) ? 1 : fps );
        applyShortcut();
        AppManager.editorConfig.KeepLyricInputMode = mDialogPreference.isKeepLyricInputMode();
        if ( AppManager.editorConfig.PxTrackHeight != mDialogPreference.getPxTrackHeight() ) {
            AppManager.editorConfig.PxTrackHeight = mDialogPreference.getPxTrackHeight();
            updateDrawObjectList();
        }
        AppManager.editorConfig.setMouseHoverTime( mDialogPreference.getMouseHoverTime() );
        AppManager.editorConfig.PlayPreviewWhenRightClick = mDialogPreference.isPlayPreviewWhenRightClick();
        AppManager.editorConfig.CurveSelectingQuantized = mDialogPreference.isCurveSelectingQuantized();

        AppManager.editorConfig.CurveVisibleAccent = mDialogPreference.isCurveVisibleAccent();
        AppManager.editorConfig.CurveVisibleBreathiness = mDialogPreference.isCurveVisibleBre();
        AppManager.editorConfig.CurveVisibleBrightness = mDialogPreference.isCurveVisibleBri();
        AppManager.editorConfig.CurveVisibleClearness = mDialogPreference.isCurveVisibleCle();
        AppManager.editorConfig.CurveVisibleDecay = mDialogPreference.isCurveVisibleDecay();
        AppManager.editorConfig.CurveVisibleDynamics = mDialogPreference.isCurveVisibleDyn();
        AppManager.editorConfig.CurveVisibleGendorfactor = mDialogPreference.isCurveVisibleGen();
        AppManager.editorConfig.CurveVisibleOpening = mDialogPreference.isCurveVisibleOpe();
        AppManager.editorConfig.CurveVisiblePit = mDialogPreference.isCurveVisiblePit();
        AppManager.editorConfig.CurveVisiblePbs = mDialogPreference.isCurveVisiblePbs();
        AppManager.editorConfig.CurveVisiblePortamento = mDialogPreference.isCurveVisiblePor();
        AppManager.editorConfig.CurveVisibleVelocity = mDialogPreference.isCurveVisibleVel();
        AppManager.editorConfig.CurveVisibleVibratoDepth = mDialogPreference.isCurveVisibleVibratoDepth();
        AppManager.editorConfig.CurveVisibleVibratoRate = mDialogPreference.isCurveVisibleVibratoRate();
        AppManager.editorConfig.CurveVisibleFx2Depth = mDialogPreference.isCurveVisibleFx2Depth();
        AppManager.editorConfig.CurveVisibleHarmonics = mDialogPreference.isCurveVisibleHarmonics();
        AppManager.editorConfig.CurveVisibleReso1 = mDialogPreference.isCurveVisibleReso1();
        AppManager.editorConfig.CurveVisibleReso2 = mDialogPreference.isCurveVisibleReso2();
        AppManager.editorConfig.CurveVisibleReso3 = mDialogPreference.isCurveVisibleReso3();
        AppManager.editorConfig.CurveVisibleReso4 = mDialogPreference.isCurveVisibleReso4();
        AppManager.editorConfig.CurveVisibleEnvelope = mDialogPreference.isCurveVisibleEnvelope();

        AppManager.editorConfig.MidiInPort.PortNumber = mDialogPreference.getMidiInPort();
        updateMidiInStatus();
        reloadMidiIn();

        Vector<String> new_resamplers = new Vector<String>();
        Vector<Boolean> new_with_wine = new Vector<Boolean>();
        mDialogPreference.copyResamplersConfig( new_resamplers, new_with_wine );
        AppManager.editorConfig.clearResampler();
        for ( int i = 0; i < new_resamplers.size(); i++ ) {
            AppManager.editorConfig.addResampler( new_resamplers.get( i ), new_with_wine.get( i ) );
        }
        AppManager.editorConfig.PathWavtool = mDialogPreference.getPathWavtool();
        AppManager.editorConfig.WavtoolWithWine = mDialogPreference.isWavtoolWithWine();

        AppManager.editorConfig.UtauSingers.clear();
        for ( Iterator<SingerConfig> itr = mDialogPreference.getUtausingers().iterator(); itr.hasNext(); ) {
            SingerConfig sc = itr.next();
            AppManager.editorConfig.UtauSingers.add( (SingerConfig)sc.clone() );
        }
        AppManager.reloadUtauVoiceDB();

        AppManager.editorConfig.SelfDeRomanization = mDialogPreference.isSelfDeRomantization();
        AppManager.editorConfig.AutoBackupIntervalMinutes = mDialogPreference.getAutoBackupIntervalMinutes();
        AppManager.editorConfig.UseSpaceKeyAsMiddleButtonModifier = mDialogPreference.isUseSpaceKeyAsMiddleButtonModifier();
        //AppManager.editorConfig.__revoked__WaveFileOutputFromMasterTrack = mDialogPreference.isWaveFileOutputFromMasterTrack();
        //AppManager.editorConfig.__revoked__WaveFileOutputChannel = mDialogPreference.getWaveFileOutputChannel();
        if ( AppManager.editorConfig.UseProjectCache && !mDialogPreference.isUseProjectCache() ) {
            // プロジェクト用キャッシュを使用していたが，使用しないように変更された場合.
            // プロジェクト用キャッシュが存在するなら，共用のキャッシュに移動する．
            String file = AppManager.getFileName();
            if ( file != null && !file.equals( "" ) ) {
                String dir = PortUtil.getDirectoryName( file );
                String name = PortUtil.getFileNameWithoutExtension( file );
                String projectCacheDir = fsys.combine( dir, name + ".cadencii" );
                String commonCacheDir = fsys.combine( AppManager.getCadenciiTempDir(), AppManager.getID() );
                if ( fsys.isDirectoryExists( projectCacheDir ) ) {
                    VsqFileEx vsq = AppManager.getVsqFile();
                    for ( int i = 1; i < vsq.Track.size(); i++ ) {
                        // wavを移動
                        String wavFrom = fsys.combine( projectCacheDir, i + ".wav" );
                        String wavTo = fsys.combine( commonCacheDir, i + ".wav" );
                        if ( !fsys.isFileExists( wavFrom ) ) {
                            continue;
                        }
                        if ( fsys.isFileExists( wavTo ) ) {
                            try {
                                PortUtil.deleteFile( wavTo );
                            } catch ( Exception ex ) {
                                Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
                                serr.println( "FormMain#menuSettingPreference_Click; ex=" + ex );
                                continue;
                            }
                        }
                        try {
                            PortUtil.moveFile( wavFrom, wavTo );
                        } catch ( Exception ex ) {
                            Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
                            serr.println( "FormMain#menuSettingPreference_Click; ex=" + ex );
                        }

                        // xmlを移動
                        String xmlFrom = fsys.combine( projectCacheDir, i + ".xml" );
                        String xmlTo = fsys.combine( commonCacheDir, i + ".xml" );
                        if ( !fsys.isFileExists( xmlFrom ) ) {
                            continue;
                        }
                        if ( fsys.isFileExists( xmlTo ) ) {
                            try {
                                PortUtil.deleteFile( xmlTo );
                            } catch ( Exception ex ) {
                                Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
                                serr.println( "FormMain#menuSettingPreference_Click; ex=" + ex );
                                continue;
                            }
                        }
                        try {
                            PortUtil.moveFile( xmlFrom, xmlTo );
                        } catch ( Exception ex ) {
                            Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
                            serr.println( "FormMain#menuSettingPreference_Click; ex=" + ex );
                        }
                    }

                    // projectCacheDirが空なら，ディレクトリごと削除する
                    String[] files = PortUtil.listFiles( projectCacheDir, "*.*" );
                    if ( files.length <= 0 ) {
                        try {
                            PortUtil.deleteDirectory( projectCacheDir );
                        } catch ( Exception ex ) {
                            Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
                            serr.println( "FormMain#menuSettingPreference_Click; ex=" + ex );
                        }
                    }

                    // キャッシュのディレクトリを再指定
                    AppManager.setTempWaveDir( commonCacheDir );
                }
            }
        }
        AppManager.editorConfig.UseProjectCache = mDialogPreference.isUseProjectCache();
        AppManager.editorConfig.DoNotUseAquesTone = !mDialogPreference.isAquesToneRequired();
        AppManager.editorConfig.DoNotUseVocaloid1 = !mDialogPreference.isVocaloid1Required();
        AppManager.editorConfig.DoNotUseVocaloid2 = !mDialogPreference.isVocaloid2Required();
        AppManager.editorConfig.BufferSizeMilliSeconds = mDialogPreference.getBufferSize();
        AppManager.editorConfig.DefaultSynthesizer = mDialogPreference.getDefaultSynthesizer();
        AppManager.editorConfig.UseUserDefinedAutoVibratoType = mDialogPreference.isUseUserDefinedAutoVibratoType();
        AppManager.editorConfig.WinePrefix = mDialogPreference.getWinePrefix();
        AppManager.editorConfig.WineTop = mDialogPreference.getWineTop();
        AppManager.editorConfig.WineTopBuiltin = mDialogPreference.isWineBuiltin();
        AppManager.editorConfig.UseWideCharacterWorkaround = mDialogPreference.isEnableWideCharacterWorkaround();

        // WinePrefix, WineTopのどちらかが変わっていたら，ドライバー・デーモンを再起動する
        if( !str.compare( old_wine_prefix, AppManager.editorConfig.WinePrefix ) ||
            !str.compare( old_wine_top, AppManager.editorConfig.getWineTop() ) ){
            VSTiDllManager.restartVocaloidrvDaemon();
        }

        trackSelector.prepareSingerMenu( VsqFileEx.getTrackRendererKind( AppManager.getVsqFile().Track.get( AppManager.getSelected() ) ) );
        trackSelector.updateVisibleCurves();

        updateRendererMenu();
        AppManager.updateAutoBackupTimerStatus();

        // editorConfig.PxTrackHeightが変更されている可能性があるので，更新が必要
        controller.setStartToDrawY( calculateStartToDrawY( vScroll.getValue() ) );

        if ( menuVisualControlTrack.isSelected() ) {
            splitContainer1.setPanel2MinSize( trackSelector.getPreferredMinSize() );
        }

        AppManager.saveConfig();
        applyLanguage();

        updateDrawObjectList();
        refreshScreen();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuSettingPreference_Click; ex=" + ex + "\n" );
    AppManager.debugWriteLine( "FormMain#menuSettingPreference_Click; ex=" + ex );
    ex.printStackTrace();
}
        }

        public void menuSettingShortcut_Click( Object sender, BEventArgs e )
        {
TreeMap<String, ValuePair<String, BKeys[]>> dict = new TreeMap<String, ValuePair<String, BKeys[]>>();
TreeMap<String, BKeys[]> configured = AppManager.editorConfig.getShortcutKeysDictionary( this.getDefaultShortcutKeys() );

// スクリプトのToolStripMenuITemを蒐集
Vector<String> script_shortcut = new Vector<String>();
MenuElement[] sub_menu_script = menuScript.getSubElements();
for ( int i = 0; i < sub_menu_script.length; i++ ) {
    MenuElement tsi = sub_menu_script[i];
    if ( tsi instanceof BMenuItem ) {
        BMenuItem tsmi = (BMenuItem)tsi;
        String name = tsmi.getName();
        script_shortcut.add( name );
        if ( !configured.containsKey( name ) ) {
            configured.put( name, new BKeys[] { } );
        }
    }
}

for ( Iterator<String> itr = configured.keySet().iterator(); itr.hasNext(); ) {
    String name = itr.next();
    ByRef<Object> owner = new ByRef<Object>( null );
    Object menu = searchMenuItemFromName( name, owner );
    JMenuItem casted_owner_item = null;
    if ( owner.value instanceof JMenuItem ) {
        casted_owner_item = (JMenuItem)owner.value;
    }
    if ( casted_owner_item == null ) {
        continue;
    }
    String parent = "";
    if ( !casted_owner_item.getName().equals( menuHidden.getName() ) ) {
        String s = casted_owner_item.getText();
        int i = s.indexOf( "(&" );
        if ( i > 0 ) {
            s = str.sub( s, 0, i );
        }
        parent = s + " -> ";
    }
    JMenuItem casted_menu = null;
    if( menu instanceof JMenuItem ){
        casted_menu = (JMenuItem)menu;
    }
    if ( casted_menu == null ) {
        continue;
    }
    String s1 = casted_menu.getText();
    int i1 = s1.indexOf( "(&" );
    if ( i1 > 0 ) {
        s1 = str.sub( s1, 0, i1 );
    }
    dict.put( parent + s1, new ValuePair<String, BKeys[]>( name, configured.get( name ) ) );
}

// 最初に戻る、のショートカットキー
BKeys[] keysGoToFirst = AppManager.editorConfig.SpecialShortcutGoToFirst;
if ( keysGoToFirst == null ) {
    keysGoToFirst = new BKeys[] { };
}
dict.put( _( "Go to the first" ), new ValuePair<String, BKeys[]>( "SpecialShortcutGoToFirst", keysGoToFirst ) );

FormShortcutKeys form = null;
try {
    form = new FormShortcutKeys( dict, this );
    form.setLocation( getFormPreferedLocation( form ) );
    BDialogResult dr = AppManager.showModalDialog( form, this );
    if ( dr == BDialogResult.OK ) {
        TreeMap<String, ValuePair<String, BKeys[]>> res = form.getResult();
        for ( Iterator<String> itr = res.keySet().iterator(); itr.hasNext(); ) {
            String display = itr.next();
            String name = res.get( display ).getKey();
            BKeys[] keys = res.get( display ).getValue();
            boolean found = false;
            if ( name.equals( "SpecialShortcutGoToFirst" ) ) {
                AppManager.editorConfig.SpecialShortcutGoToFirst = keys;
            } else {
                for ( int i = 0; i < AppManager.editorConfig.ShortcutKeys.size(); i++ ) {
                    if ( AppManager.editorConfig.ShortcutKeys.get( i ).Key.equals( name ) ) {
                        AppManager.editorConfig.ShortcutKeys.get( i ).Value = keys;
                        found = true;
                        break;
                    }
                }
                if ( !found ) {
                    AppManager.editorConfig.ShortcutKeys.add( new ValuePairOfStringArrayOfKeys( name, keys ) );
                }
            }
        }
        applyShortcut();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuSettingShortcut_Click; ex=" + ex + "\n" );
} finally {
    if ( form != null ) {
        try {
            form.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuSettingSHortcut_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuSettingVibratoPreset_Click( Object sender, BEventArgs e )
        {
FormVibratoPreset dialog = null;
try {
    dialog = new FormVibratoPreset( AppManager.editorConfig.AutoVibratoCustom );
    dialog.setLocation( getFormPreferedLocation( dialog ) );
    BDialogResult ret = AppManager.showModalDialog( dialog, this );
    if ( ret != BDialogResult.OK ) {
        return;
    }

    // ダイアログの結果を取得
    Vector<VibratoHandle> result = dialog.getResult();

    // ダイアログ結果を，設定値にコピー
    // ダイアログのコンストラクタであらかじめcloneされているので，
    // ここではcloneする必要はない．
    AppManager.editorConfig.AutoVibratoCustom.clear();
    for ( int i = 0; i < result.size(); i++ ) {
        AppManager.editorConfig.AutoVibratoCustom.add( result.get( i ) );
    }

    // メニューの表示状態を更新
    updateVibratoPresetMenu();
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuSettingVibratoPreset_Click; ex=" + ex + "\n" );
} finally {
    if ( dialog != null ) {
        try {
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        //BOOKMARK: menuEdit
        public void menuEditDelete_Click( Object sender, BEventArgs e )
        {
deleteEvent();
        }

        public void menuEdit_DropDownOpening( Object sender, BEventArgs e )
        {
updateCopyAndPasteButtonStatus();
        }

        public void handleEditUndo_Click( Object sender, BEventArgs e )
        {
undo();
refreshScreen();
        }


        public void handleEditRedo_Click( Object sender, BEventArgs e )
        {
redo();
refreshScreen();
        }

        public void menuEditSelectAllEvents_Click( Object sender, BEventArgs e )
        {
selectAllEvent();
        }

        public void menuEditSelectAll_Click( Object sender, BEventArgs e )
        {
selectAll();
        }

        public void menuEditAutoNormalizeMode_Click( Object sender, BEventArgs e )
        {
AppManager.mAutoNormalize = !AppManager.mAutoNormalize;
menuEditAutoNormalizeMode.setSelected( AppManager.mAutoNormalize );
        }

        //BOOKMARK: menuLyric
        public void menuLyric_DropDownOpening( Object sender, BEventArgs e )
        {
menuLyricCopyVibratoToPreset.setEnabled( false );

int num = AppManager.itemSelection.getEventCount();
if ( num <= 0 ) {
    return;
}
SelectedEventEntry item = AppManager.itemSelection.getEventIterator().next();
if ( item.original.ID.type != VsqIDType.Anote ) {
    return;
}
if ( item.original.ID.VibratoHandle == null ) {
    return;
}

menuLyricCopyVibratoToPreset.setEnabled( true );
        }

        public void menuLyricExpressionProperty_Click( Object sender, BEventArgs e )
        {
editNoteExpressionProperty();
        }

        public void menuLyricPhonemeTransformation_Click( Object sender, BEventArgs e )
        {
Vector<Integer> internal_ids = new Vector<Integer>();
Vector<VsqID> ids = new Vector<VsqID>();
VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq == null ) {
    return;
}
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
for ( Iterator<VsqEvent> itr = vsq_track.getNoteEventIterator(); itr.hasNext(); ) {
    VsqEvent item = itr.next();
    VsqID id = item.ID;
    if ( id.LyricHandle.L0.PhoneticSymbolProtected ) {
        continue;
    }
    String phrase = id.LyricHandle.L0.Phrase;
    String symbolOld = id.LyricHandle.L0.getPhoneticSymbol();
    String symbolResult = symbolOld;
    SymbolTableEntry entry = SymbolTable.attatch( phrase );
    if ( entry == null ) {
        continue;
    }
    symbolResult = entry.getSymbol();
    if ( symbolResult.equals( symbolOld ) ) {
        continue;
    }
    VsqID idNew = (VsqID)id.clone();
    idNew.LyricHandle.L0.setPhoneticSymbol( symbolResult );
    ids.add( idNew );
    internal_ids.add( item.InternalID );
}
if ( ids.size() <= 0 ) {
    return;
}
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandEventChangeIDContaintsRange(
        selected,
        PortUtil.convertIntArray( internal_ids.toArray( new Integer[] { } ) ),
        ids.toArray( new VsqID[] { } ) ) );
AppManager.editHistory.register( vsq.executeCommand( run ) );
setEdited( true );
        }

        /// <summary>
        /// 現在表示しているトラックの，選択状態の音符イベントについて，それぞれのイベントの
        /// 時刻でのUTAU歌手に応じて，UTAUの各種パラメータを原音設定のものにリセットします
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void menuLyricApplyUtauParameters_Click( Object sender, BEventArgs e )
        {
// 選択されているトラックの番号
int selected = AppManager.getSelected();
// シーケンス
VsqFileEx vsq = AppManager.getVsqFile();
VsqTrack vsq_track = vsq.Track.get( selected );

// 選択状態にあるイベントを取り出す
Vector<VsqEvent> replace = new Vector<VsqEvent>();
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry sel_item = itr.next();
    VsqEvent item = sel_item.original;
    if ( item.ID.type != VsqIDType.Anote ) {
        continue;
    }
    VsqEvent edit = (VsqEvent)item.clone();
    // UTAUのパラメータを適用
    AppManager.applyUtauParameter( vsq_track, edit );
    // 合成したとき，意味のある変更が行われたか？
    if ( edit.UstEvent.equalsForSynth( item.UstEvent ) ) {
        continue;
    }
    // 意味のある変更があったので，リストに登録
    replace.add( edit );
}

// コマンドを発行
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandEventReplaceRange( selected, replace.toArray( new VsqEvent[] { } ) ) );
// コマンドを実行
AppManager.editHistory.register( vsq.executeCommand( run ) );
setEdited( true );
        }

        public void menuLyricDictionary_Click( Object sender, BEventArgs e )
        {
FormWordDictionaryController dlg = null;
try {
    dlg = new FormWordDictionaryController();
    Point p = getFormPreferedLocation( dlg.getWidth(), dlg.getHeight() );
    dlg.setLocation( p.x, p.y );
    int dr = AppManager.showModalDialog( dlg.getUi(), this );
    if ( dr == 1 ) {
        Vector<ValuePair<String, Boolean>> result = dlg.getResult();
        SymbolTable.changeOrder( result );
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuLyricDictionary_Click; ex=" + ex + "\n" );
    serr.println( "FormMain#menuLyricDictionary_Click; ex=" + ex );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuLyricDictionary_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuLyricVibratoProperty_Click( Object sender, BEventArgs e )
        {
editNoteVibratoProperty();
        }

        //BOOKMARK: menuJob
        public void menuJobReloadVsti_Click( Object sender, BEventArgs e )
        {
//VSTiProxy.ReloadPlugin(); //todo: FormMain+menuJobReloadVsti_Click
        }

        public void menuJob_DropDownOpening( Object sender, BEventArgs e )
        {
if ( AppManager.itemSelection.getEventCount() <= 1 ) {
    menuJobConnect.setEnabled( false );
} else {
    // menuJobConnect(音符の結合)がEnableされるかどうかは、選択されている音符がピアノロール上で連続かどうかで決まる
    int[] list = new int[AppManager.itemSelection.getEventCount()];
    for ( int i = 0; i < AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getEventCount(); i++ ) {
        int count = -1;
        for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
            SelectedEventEntry item = itr.next();
            int key = item.original.InternalID;
            count++;
            if ( key == AppManager.getVsqFile().Track.get( AppManager.getSelected() ).getEvent( i ).InternalID ) {
                list[count] = i;
                break;
            }
        }
    }
    boolean changed = true;
    while ( changed ) {
        changed = false;
        for ( int i = 0; i < list.length - 1; i++ ) {
            if ( list[i] > list[i + 1] ) {
                int b = list[i];
                list[i] = list[i + 1];
                list[i + 1] = b;
                changed = true;
            }
        }
    }
    boolean continued = true;
    for ( int i = 0; i < list.length - 1; i++ ) {
        if ( list[i] + 1 != list[i + 1] ) {
            continued = false;
            break;
        }
    }
    menuJobConnect.setEnabled( continued );
}

menuJobLyric.setEnabled( AppManager.itemSelection.getLastEvent() != null );
        }

        public void menuJobLyric_Click( Object sender, BEventArgs e )
        {
importLyric();
        }

        public void menuJobConnect_Click( Object sender, BEventArgs e )
        {
int count = AppManager.itemSelection.getEventCount();
int[] clocks = new int[count];
VsqID[] ids = new VsqID[count];
int[] internalids = new int[count];
int i = -1;
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    i++;
    clocks[i] = item.original.Clock;
    ids[i] = (VsqID)item.original.ID.clone();
    internalids[i] = item.original.InternalID;
}
boolean changed = true;
while ( changed ) {
    changed = false;
    for ( int j = 0; j < clocks.length - 1; j++ ) {
        if ( clocks[j] > clocks[j + 1] ) {
            int b = clocks[j];
            clocks[j] = clocks[j + 1];
            clocks[j + 1] = b;
            VsqID a = ids[j];
            ids[j] = ids[j + 1];
            ids[j + 1] = a;
            changed = true;
            b = internalids[j];
            internalids[j] = internalids[j + 1];
            internalids[j + 1] = b;
        }
    }
}

for ( int j = 0; j < ids.length - 1; j++ ) {
    ids[j].setLength( clocks[j + 1] - clocks[j] );
}
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandEventChangeIDContaintsRange( AppManager.getSelected(), internalids, ids ) );
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
setEdited( true );
repaint();
        }

        public void menuJobInsertBar_Click( Object sender, BEventArgs e )
        {
int total_clock = AppManager.getVsqFile().TotalClocks;
int total_barcount = AppManager.getVsqFile().getBarCountFromClock( total_clock ) + 1;
FormInsertBar dlg = null;
try {
    dlg = new FormInsertBar( total_barcount );
    int current_clock = AppManager.getCurrentClock();
    int barcount = AppManager.getVsqFile().getBarCountFromClock( current_clock );
    int draft = barcount - AppManager.getVsqFile().getPreMeasure() + 1;
    if ( draft <= 0 ) {
        draft = 1;
    }
    dlg.setPosition( draft );

    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        int pos = dlg.getPosition() + AppManager.getVsqFile().getPreMeasure() - 1;
        int length = dlg.getLength();

        int clock_start = AppManager.getVsqFile().getClockFromBarCount( pos );
        int clock_end = AppManager.getVsqFile().getClockFromBarCount( pos + length );
        int dclock = clock_end - clock_start;
        VsqFileEx temp = (VsqFileEx)AppManager.getVsqFile().clone();

        for ( int track = 1; track < temp.Track.size(); track++ ) {
            BezierCurves newbc = new BezierCurves();
            for ( CurveType ct : Utility.CURVE_USAGE ) {
                int index = ct.getIndex();
                if ( index < 0 ) {
                    continue;
                }

                Vector<BezierChain> list = new Vector<BezierChain>();
                for ( Iterator<BezierChain> itr = temp.AttachedCurves.get( track - 1 ).get( ct ).iterator(); itr.hasNext(); ) {
                    BezierChain bc = itr.next();
                    if ( bc.size() < 2 ) {
                        continue;
                    }
                    int chain_start = (int)bc.points.get( 0 ).getBase().getX();
                    int chain_end = (int)bc.points.get( bc.points.size() - 1 ).getBase().getX();

                    if ( clock_start <= chain_start ) {
                        for ( int i = 0; i < bc.points.size(); i++ ) {
                            PointD t = bc.points.get( i ).getBase();
                            bc.points.get( i ).setBase( new PointD( t.getX() + dclock, t.getY() ) );
                        }
                        list.add( bc );
                    } else if ( chain_start < clock_start && clock_start < chain_end ) {
                        BezierChain adding1 = bc.extractPartialBezier( chain_start, clock_start );
                        BezierChain adding2 = bc.extractPartialBezier( clock_start, chain_end );
                        for ( int i = 0; i < adding2.points.size(); i++ ) {
                            PointD t = adding2.points.get( i ).getBase();
                            adding2.points.get( i ).setBase( new PointD( t.getX() + dclock, t.getY() ) );
                        }
                        adding1.points.get( adding1.points.size() - 1 ).setControlRightType( BezierControlType.None );
                        adding2.points.get( 0 ).setControlLeftType( BezierControlType.None );
                        for ( int i = 0; i < adding2.points.size(); i++ ) {
                            adding1.points.add( adding2.points.get( i ) );
                        }
                        adding1.id = bc.id;
                        list.add( adding1 );
                    } else {
                        list.add( (BezierChain)bc.clone() );
                    }
                }

                newbc.set( ct, list );
            }
            temp.AttachedCurves.set( track - 1, newbc );
        }

        for ( int track = 1; track < AppManager.getVsqFile().Track.size(); track++ ) {
            for ( int i = 0; i < temp.Track.get( track ).getEventCount(); i++ ) {
                if ( temp.Track.get( track ).getEvent( i ).Clock >= clock_start ) {
                    temp.Track.get( track ).getEvent( i ).Clock += dclock;
                }
            }
            for ( CurveType curve : Utility.CURVE_USAGE ) {
                if ( curve.isScalar() || curve.isAttachNote() ) {
                    continue;
                }
                VsqBPList target = temp.Track.get( track ).getCurve( curve.getName() );
                VsqBPList src = AppManager.getVsqFile().Track.get( track ).getCurve( curve.getName() );
                target.clear();
                for ( Iterator<Integer> itr = src.keyClockIterator(); itr.hasNext(); ) {
                    int key = itr.next();
                    if ( key >= clock_start ) {
                        target.add( key + dclock, src.getValue( key ) );
                    } else {
                        target.add( key, src.getValue( key ) );
                    }
                }
            }
        }
        for ( int i = 0; i < temp.TempoTable.size(); i++ ) {
            if ( temp.TempoTable.get( i ).Clock >= clock_start ) {
                temp.TempoTable.get( i ).Clock = temp.TempoTable.get( i ).Clock + dclock;
            }
        }
        for ( int i = 0; i < temp.TimesigTable.size(); i++ ) {
            if ( temp.TimesigTable.get( i ).Clock >= clock_start ) {
                temp.TimesigTable.get( i ).Clock = temp.TimesigTable.get( i ).Clock + dclock;
            }
        }
        temp.updateTempoInfo();
        temp.updateTimesigInfo();

        CadenciiCommand run = VsqFileEx.generateCommandReplace( temp );
        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
        setEdited( true );
        repaint();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuJobInsertBar_Click; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuJobInsertBar_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuJobChangePreMeasure_Click( Object sender, BEventArgs e )
        {
InputBox dialog = null;
try {
    dialog = new InputBox( _( "input pre-measure" ) );
    int old_pre_measure = AppManager.getVsqFile().getPreMeasure();
    dialog.setResult( old_pre_measure + "" );
    dialog.setLocation( getFormPreferedLocation( dialog ) );
    BDialogResult ret = AppManager.showModalDialog( dialog, this );
    if ( ret == BDialogResult.OK ) {
        String str_result = dialog.getResult();
        int result = old_pre_measure;
        try {
            result = str.toi( str_result );
        } catch ( Exception ex ) {
            result = old_pre_measure;
        }
        if ( result < AppManager.MIN_PRE_MEASURE ) {
            result = AppManager.MIN_PRE_MEASURE;
        }
        if ( result > AppManager.MAX_PRE_MEASURE ) {
            result = AppManager.MAX_PRE_MEASURE;
        }
        if ( old_pre_measure != result ) {
            CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandChangePreMeasure( result ) );
            AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
            AppManager.getVsqFile().updateTotalClocks();
            refreshScreen( true );
            setEdited( true );
        }
    }
} catch ( Exception ex ) {
    return;
} finally {
    if ( dialog != null ) {
        try {
            dialog.close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        public void menuSettingSequence_Click( Object sender, BEventArgs e )
        {
VsqFileEx vsq = AppManager.getVsqFile();

FormSequenceConfig dialog = new FormSequenceConfig();
int old_channels = vsq.config.WaveFileOutputChannel;
boolean old_output_master = vsq.config.WaveFileOutputFromMasterTrack;
int old_sample_rate = vsq.config.SamplingRate;
int old_pre_measure = vsq.getPreMeasure();

dialog.setWaveFileOutputChannel( old_channels );
dialog.setWaveFileOutputFromMasterTrack( old_output_master );
dialog.setSampleRate( old_sample_rate );
dialog.setPreMeasure( old_pre_measure );

dialog.setLocation( getFormPreferedLocation( dialog ) );
if ( AppManager.showModalDialog( dialog, this ) != BDialogResult.OK ) {
    return;
}

int new_channels = dialog.getWaveFileOutputChannel();
boolean new_output_master = dialog.isWaveFileOutputFromMasterTrack();
int new_sample_rate = dialog.getSampleRate();
int new_pre_measure = dialog.getPreMeasure();

CadenciiCommand run =
    VsqFileEx.generateCommandChangeSequenceConfig(
        new_sample_rate,
        new_channels,
        new_output_master,
        new_pre_measure );
AppManager.editHistory.register( vsq.executeCommand( run ) );
setEdited( true );
        }

        public void menuJobDeleteBar_Click( Object sender, BEventArgs e )
        {
int total_clock = AppManager.getVsqFile().TotalClocks;
int total_barcount = AppManager.getVsqFile().getBarCountFromClock( total_clock ) + 1;
int clock = AppManager.getCurrentClock();
int barcount = AppManager.getVsqFile().getBarCountFromClock( clock );
FormDeleteBar dlg = null;
try {
    dlg = new FormDeleteBar( total_barcount );
    int draft = barcount - AppManager.getVsqFile().getPreMeasure() + 1;
    if ( draft <= 0 ) {
        draft = 1;
    }
    dlg.setStart( draft );
    dlg.setEnd( draft + 1 );

    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        VsqFileEx temp = (VsqFileEx)AppManager.getVsqFile().clone();
        int start = dlg.getStart() + AppManager.getVsqFile().getPreMeasure() - 1;
        int end = dlg.getEnd() + AppManager.getVsqFile().getPreMeasure() - 1;
        int clock_start = temp.getClockFromBarCount( start );
        int clock_end = temp.getClockFromBarCount( end );
        int dclock = clock_end - clock_start;
        for ( int track = 1; track < temp.Track.size(); track++ ) {
            BezierCurves newbc = new BezierCurves();
            for ( int j = 0; j < Utility.CURVE_USAGE.length; j++ ) {
                CurveType ct = Utility.CURVE_USAGE[j];
                int index = ct.getIndex();
                if ( index < 0 ) {
                    continue;
                }

                Vector<BezierChain> list = new Vector<BezierChain>();
                for ( Iterator<BezierChain> itr = temp.AttachedCurves.get( track - 1 ).get( ct ).iterator(); itr.hasNext(); ) {
                    BezierChain bc = itr.next();
                    if ( bc.size() < 2 ) {
                        continue;
                    }
                    int chain_start = (int)bc.points.get( 0 ).getBase().getX();
                    int chain_end = (int)bc.points.get( bc.points.size() - 1 ).getBase().getX();

                    if ( clock_start < chain_start && chain_start < clock_end && clock_end < chain_end ) {
                        BezierChain adding = bc.extractPartialBezier( clock_end, chain_end );
                        adding.id = bc.id;
                        for ( int i = 0; i < adding.points.size(); i++ ) {
                            PointD t = adding.points.get( i ).getBase();
                            adding.points.get( i ).setBase( new PointD( t.getX() - dclock, t.getY() ) );
                        }
                        list.add( adding );
                    } else if ( chain_start < clock_start && clock_end < chain_end ) {
                        BezierChain adding1 = bc.extractPartialBezier( chain_start, clock_start );
                        adding1.id = bc.id;
                        adding1.points.get( adding1.points.size() - 1 ).setControlRightType( BezierControlType.None );
                        BezierChain adding2 = bc.extractPartialBezier( clock_end, chain_end );
                        adding2.points.get( 0 ).setControlLeftType( BezierControlType.None );
                        PointD t = adding2.points.get( 0 ).getBase();
                        adding2.points.get( 0 ).setBase( new PointD( t.getX() - dclock, t.getY() ) );
                        adding1.points.add( adding2.points.get( 0 ) );
                        for ( int i = 1; i < adding2.points.size(); i++ ) {
                            t = adding2.points.get( i ).getBase();
                            adding2.points.get( i ).setBase( new PointD( t.getX() - dclock, t.getY() ) );
                            adding1.points.add( adding2.points.get( i ) );
                        }
                        list.add( adding1 );
                    } else if ( chain_start < clock_start && clock_start < chain_end && chain_end < clock_end ) {
                        BezierChain adding = bc.extractPartialBezier( chain_start, clock_start );
                        adding.id = bc.id;
                        list.add( adding );
                    } else if ( clock_end <= chain_start || chain_end <= clock_start ) {
                        if ( clock_end <= chain_start ) {
                            for ( int i = 0; i < bc.points.size(); i++ ) {
                                PointD t = bc.points.get( i ).getBase();
                                bc.points.get( i ).setBase( new PointD( t.getX() - dclock, t.getY() ) );
                            }
                        }
                        list.add( (BezierChain)bc.clone() );
                    }
                }

                newbc.set( ct, list );
            }
            temp.AttachedCurves.set( track - 1, newbc );
        }

        temp.removePart( clock_start, clock_end );
        CadenciiCommand run = VsqFileEx.generateCommandReplace( temp );
        AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
        setEdited( true );
        repaint();
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuJobDeleteBar_Click; ex=" + ex + "\n" );
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuJobDeleteBar_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void menuJobNormalize_Click( Object sender, BEventArgs e )
        {
VsqFile work = (VsqFile)AppManager.getVsqFile().clone();
int track = AppManager.getSelected();
boolean changed = true;
boolean total_changed = false;

// 最初、開始時刻が同じになっている奴を削除
while ( changed ) {
    changed = false;
    for ( int i = 0; i < work.Track.get( track ).getEventCount() - 1; i++ ) {
        int clock = work.Track.get( track ).getEvent( i ).Clock;
        int id = work.Track.get( track ).getEvent( i ).InternalID;
        for ( int j = i + 1; j < work.Track.get( track ).getEventCount(); j++ ) {
            if ( clock == work.Track.get( track ).getEvent( j ).Clock ) {
                if ( id < work.Track.get( track ).getEvent( j ).InternalID ) { //内部IDが小さい＝より高年齢（音符追加時刻が古い）
                    work.Track.get( track ).removeEvent( i );
                } else {
                    work.Track.get( track ).removeEvent( j );
                }
                changed = true;
                total_changed = true;
                break;
            }
        }
        if ( changed ) {
            break;
        }
    }
}

changed = true;
while ( changed ) {
    changed = false;
    for ( int i = 0; i < work.Track.get( track ).getEventCount() - 1; i++ ) {
        int start_clock = work.Track.get( track ).getEvent( i ).Clock;
        int end_clock = work.Track.get( track ).getEvent( i ).ID.getLength() + start_clock;
        for ( int j = i + 1; j < work.Track.get( track ).getEventCount(); j++ ) {
            int this_start_clock = work.Track.get( track ).getEvent( j ).Clock;
            if ( this_start_clock < end_clock ) {
                work.Track.get( track ).getEvent( i ).ID.setLength( this_start_clock - start_clock );
                changed = true;
                total_changed = true;
            }
        }
    }
}

if ( total_changed ) {
    CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandReplace( work ) );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
    refreshScreen();
}
        }

        public void menuJobRandomize_Click( Object sender, BEventArgs e )
        {
FormRandomize dlg = null;
try {
    dlg = new FormRandomize();
    dlg.setLocation( getFormPreferedLocation( dlg ) );
    BDialogResult dr = AppManager.showModalDialog( dlg, this );
    if ( dr == BDialogResult.OK ) {
        VsqFileEx vsq = AppManager.getVsqFile();
        int preMeasure = vsq.getPreMeasure();
        int startBar = dlg.getStartBar() + (preMeasure - 1);
        int startBeat = dlg.getStartBeat() - 1;
        int endBar = dlg.getEndBar() + (preMeasure - 1);
        int endBeat = dlg.getEndBeat();
        int startBarClock = vsq.getClockFromBarCount( startBar );
        int endBarClock = vsq.getClockFromBarCount( endBar );
        Timesig startTimesig = vsq.getTimesigAt( startBarClock );
        Timesig endTimesig = vsq.getTimesigAt( endBarClock );
        int startClock = startBarClock + startBeat * 480 * 4 / startTimesig.denominator;
        int endClock = endBarClock + endBeat * 480 * 4 / endTimesig.denominator;

        int selected = AppManager.getSelected();
        VsqTrack vsq_track = vsq.Track.get( selected );
        VsqTrack work = (VsqTrack)vsq_track.clone();
        Random r = new Random();

        // 音符位置のシフト
        if ( dlg.isPositionRandomizeEnabled() ) {
            int[] sigmaPreset = new int[] { 10, 20, 30, 60, 120 };
            int sigma = sigmaPreset[dlg.getPositionRandomizeValue() - 1]; // 標準偏差

            int count = work.getEventCount(); // イベントの個数
            int lastItemIndex = -1;  // 直前に処理した音符イベントのインデクス
            int thisItemIndex = -1;  // 処理中の音符イベントのインデクス
            int nextItemIndex = -1;  // 処理中の音符イベントの次の音符イベントのインデクス
            double sqrt2 = Math.sqrt( 2.0 );
            int clockPreMeasure = vsq.getPreMeasureClocks(); // プリメジャーいちでのゲートタイム

            while ( true ) {
                // nextItemIndexを決定
                if ( nextItemIndex != -2 ) {
                    int start = nextItemIndex + 1;
                    nextItemIndex = -2;  // -2は、トラックの最後まで走査し終わった、という意味
                    for ( int i = start; i < count; i++ ) {
                        if ( work.getEvent( i ).ID.type == VsqIDType.Anote ) {
                            nextItemIndex = i;
                            break;
                        }
                    }
                }

                if ( thisItemIndex >= 0 ) {
                    // ここにメインの処理
                    VsqEvent lastItem = lastItemIndex >= 0 ? work.getEvent( lastItemIndex ) : null;
                    VsqEvent thisItem = work.getEvent( thisItemIndex );
                    VsqEvent nextItem = nextItemIndex >= 0 ? work.getEvent( nextItemIndex ) : null;
                    int lastItemClock = lastItem == null ? 0 : lastItem.Clock;
                    int lastItemLength = lastItem == null ? 0 : lastItem.ID.getLength();

                    int clock = thisItem.Clock;
                    int length = thisItem.ID.getLength();
                    if ( startClock <= thisItem.Clock && thisItem.Clock + thisItem.ID.getLength() <= endClock ) {
                        int draftClock = 0;
                        int draftLength = 0;
                        int draftLastItemLength = lastItemLength;
                        // 音符のめり込み等のチェックをクリアするまで、draft(Clock|Length|LastItemLength)をトライ＆エラーで決定する
                        while ( true ) {
                            int x = 3 * sigma;
                            while ( Math.abs( x ) > 2 * sigma ) {
                                double d = r.nextDouble();
                                double y = (d - 0.5) * 2.0;
                                x = (int)(sigma * sqrt2 * math.erfinv( y ));
                            }
                            draftClock = clock + x;
                            draftLength = clock + length - draftClock;
                            if ( lastItem != null ) {
                                if ( clock == lastItemClock + lastItemLength ) {
                                    // 音符が連続していた場合
                                    draftLastItemLength = lastItem.ID.getLength() + x;
                                }
                            }
                            // 音符がめり込んだりしてないかどうかをチェック
                            if ( draftClock < clockPreMeasure ) {
                                continue;
                            }
                            if ( lastItem != null ) {
                                if ( clock != lastItemClock + lastItemLength ) {
                                    // 音符が連続していなかった場合に、直前の音符にめり込んでいないかどうか
                                    if ( draftClock + draftLength < lastItem.Clock + lastItem.ID.getLength() ) {
                                        continue;
                                    }
                                }
                            }
                            // チェックにクリアしたのでループを抜ける
                            break;
                        }
                        // draft*の値を適用
                        thisItem.Clock = draftClock;
                        thisItem.ID.setLength( draftLength );
                        if ( lastItem != null ) {
                            lastItem.ID.setLength( draftLastItemLength );
                        }
                    } else if ( endClock < thisItem.Clock ) {
                        break;
                    }
                }

                // インデクスを移す
                lastItemIndex = thisItemIndex;
                thisItemIndex = nextItemIndex;

                if ( lastItemIndex == -2 && thisItemIndex == -2 && nextItemIndex == -2 ) {
                    // トラックの最後まで走査し終わったので抜ける
                    break;
                }
            }
        }

        // ピッチベンドのランダマイズ
        if ( dlg.isPitRandomizeEnabled() ) {
            int pattern = dlg.getPitRandomizePattern();
            int value = dlg.getPitRandomizeValue();
            double order = 1.0 / Math.pow( 2.0, 5.0 - value );
            int[] patternPreset = pattern == 1 ? Utility.getRandomizePitPattern1() : pattern == 2 ? Utility.getRandomizePitPattern2() : Utility.getRandomizePitPattern3();
            int resolution = dlg.getResolution();
            VsqBPList pit = work.getCurve( "pit" );
            VsqBPList pbs = work.getCurve( "pbs" );
            int pbsAtStart = pbs.getValue( startClock );
            int pbsAtEnd = pbs.getValue( endClock );

            // startClockからendClock - 1までのpit, pbsをクリアする
            int count = pit.size();
            for ( int i = count - 1; i >= 0; i-- ) {
                int keyClock = pit.getKeyClock( i );
                if ( startClock <= keyClock && keyClock < endClock ) {
                    pit.removeElementAt( i );
                }
            }
            count = pbs.size();
            for ( int i = count - 1; i >= 0; i-- ) {
                int keyClock = pbs.getKeyClock( i );
                if ( startClock <= keyClock && keyClock < endClock ) {
                    pbs.removeElementAt( i );
                }
            }

            // pbsをデフォルト値にする
            if ( pbsAtStart != 2 ) {
                pbs.add( startClock, 2 );
            }
            if ( pbsAtEnd != 2 ) {
                pbs.add( endClock, pbsAtEnd );
            }

            StringBuilder sb = new StringBuilder();
            count = pit.size();
            boolean first = true;
            for ( int i = 0; i < count; i++ ) {
                int clock = pit.getKeyClock( i );
                if ( clock < startClock ) {
                    int v = pit.getElementA( i );
                    sb.append( (first ? "" : ",") + (clock + "=" + v) );
                    first = false;
                } else if ( clock <= endClock ) {
                    break;
                }
            }
            double d = r.nextDouble();
            int start = (int)(d * (patternPreset.length - 1));
            for ( int clock = startClock; clock < endClock; clock += resolution ) {
                int copyIndex = start + (clock - startClock);
                int odd = copyIndex / patternPreset.length;
                copyIndex = copyIndex - patternPreset.length * odd;
                int v = (int)(patternPreset[copyIndex] * order);
                sb.append( (first ? "" : ",") + (clock + "=" + v) );
                first = false;
                //pit.add( clock, v );
            }
            for ( int i = 0; i < count; i++ ) {
                int clock = pit.getKeyClock( i );
                if ( endClock <= clock ) {
                    int v = pit.getElementA( i );
                    sb.append( (first ? "" : ",") + (clock + "=" + v) );
                    first = false;
                }
            }
            pit.setData( sb.toString() );
        }

        CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected, work, vsq.AttachedCurves.get( selected - 1 ) );
        AppManager.editHistory.register( vsq.executeCommand( run ) );
        setEdited( true );
    }
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".menuJobRandomize_Click; ex=" + ex + "\n" );
    serr.println( "FormMain#menuJobRandomize_Click; ex=" + ex );
    ex.printStackTrace();
} finally {
    if ( dlg != null ) {
        try {
            dlg.close();
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".menuJobRandomize_Click; ex=" + ex2 + "\n" );
            serr.println( "FormMain#menuJobRandomize; ex2=" + ex2 );
        }
    }
}
        }


        //BOOKMARK: menuScript
        public void menuScriptUpdate_Click( Object sender, BEventArgs e )
        {
        }

        //BOOKMARK: vScroll
        public void vScroll_Enter( Object sender, BEventArgs e )
        {
focusPianoRoll();
        }

        public void vScroll_ValueChanged( Object sender, BEventArgs e )
        {
controller.setStartToDrawY( calculateStartToDrawY( vScroll.getValue() ) );
if ( AppManager.getEditMode() != EditMode.MIDDLE_DRAG ) {
    // MIDDLE_DRAGのときは，pictPianoRoll_MouseMoveでrefreshScreenされるので，それ以外のときだけ描画・
    refreshScreen( true );
}
        }

        //BOOKMARK: waveView
        public void waveView_MouseDoubleClick( Object sender, BMouseEventArgs e )
        {
if ( e.Button == BMouseButtons.Middle ) {
    // ツールをポインター <--> 鉛筆に切り替える
    if ( e.Y < trackSelector.getHeight() - TrackSelector.OFFSET_TRACK_TAB * 2 ) {
        if ( AppManager.getSelectedTool() == EditTool.ARROW ) {
            AppManager.setSelectedTool( EditTool.PENCIL );
        } else {
            AppManager.setSelectedTool( EditTool.ARROW );
        }
    }
}
        }

        public void waveView_MouseDown( Object sender, BMouseEventArgs e )
        {
if ( isMouseMiddleButtonDowned( e.Button ) ) {
    mEditCurveMode = CurveEditMode.MIDDLE_DRAG;
    mButtonInitial = new Point( e.X, e.Y );
    mMiddleButtonHScroll = hScroll.getValue();
}
        }

        public void waveView_MouseUp( Object sender, BMouseEventArgs e )
        {
if ( mEditCurveMode == CurveEditMode.MIDDLE_DRAG ) {
    mEditCurveMode = CurveEditMode.NONE;
    setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
}
        }

        public void waveView_MouseMove( Object sender, BMouseEventArgs e )
        {
if ( mEditCurveMode == CurveEditMode.MIDDLE_DRAG ) {
    int draft = computeHScrollValueForMiddleDrag( e.X );
    if ( hScroll.getValue() != draft ) {
        hScroll.setValue( draft );
    }
}
        }

        //BOOKMARK: hScroll
        public void hScroll_Enter( Object sender, BEventArgs e )
        {
focusPianoRoll();
        }

        public void hScroll_Resize( Object sender, BEventArgs e )
        {
if ( getExtendedState() != BForm.ICONIFIED ) {
    updateScrollRangeHorizontal();
}
        }

        public void hScroll_ValueChanged( Object sender, BEventArgs e )
        {
int stdx = calculateStartToDrawX();
controller.setStartToDrawX( stdx );
if ( AppManager.getEditMode() != EditMode.MIDDLE_DRAG ) {
    // MIDDLE_DRAGのときは，pictPianoRoll_MouseMoveでrefreshScreenされるので，それ以外のときだけ描画・
    refreshScreen( true );
}
        }

        //BOOKMARK: picturePositionIndicator
        public void picturePositionIndicator_MouseWheel( Object sender, BMouseEventArgs e )
        {
hScroll.setValue( computeScrollValueFromWheelDelta( e.Delta ) );
        }

        public void picturePositionIndicator_MouseClick( Object sender, BMouseEventArgs e )
        {
if ( e.Button == BMouseButtons.Right && 0 < e.Y && e.Y <= 18 && AppManager.keyWidth < e.X ) {
    // クリックされた位置でのクロックを保存
    int clock = AppManager.clockFromXCoord( e.X );
    int unit = AppManager.getPositionQuantizeClock();
    clock = doQuantize( clock, unit );
    if ( clock < 0 ) {
        clock = 0;
    }
    mPositionIndicatorPopupShownClock = clock;
    cMenuPositionIndicator.show( picturePositionIndicator, e.X, e.Y );
}
        }

        public void picturePositionIndicator_MouseDoubleClick( Object sender, BMouseEventArgs e )
        {
if ( e.X < AppManager.keyWidth || getWidth() - 3 < e.X ) {
    return;
}
if ( e.Button == BMouseButtons.Left ) {
    VsqFileEx vsq = AppManager.getVsqFile();
    if ( 18 < e.Y && e.Y <= 32 ) {
        AppManager.itemSelection.clearEvent();
        AppManager.itemSelection.clearTimesig();

        if ( AppManager.itemSelection.getTempoCount() > 0 ) {
            int index = -1;
            int clock = AppManager.itemSelection.getLastTempoClock();
            for ( int i = 0; i < vsq.TempoTable.size(); i++ ) {
                if ( clock == vsq.TempoTable.get( i ).Clock ) {
                    index = i;
                    break;
                }
            }
            if ( index >= 0 ) {
                if ( AppManager.getSelectedTool() == EditTool.ERASER ) {
                    if ( vsq.TempoTable.get( index ).Clock == 0 ) {
                        String msg = _( "Cannot remove first symbol of track!" );
                        statusLabel.setText( msg );
                        return;
                    }
                    CadenciiCommand run = new CadenciiCommand(
                        VsqCommand.generateCommandUpdateTempo( vsq.TempoTable.get( index ).Clock,
                                                               vsq.TempoTable.get( index ).Clock,
                                                               -1 ) );
                    AppManager.editHistory.register( vsq.executeCommand( run ) );
                    setEdited( true );
                } else {
                    TempoTableEntry tte = vsq.TempoTable.get( index );
                    AppManager.itemSelection.clearTempo();
                    AppManager.itemSelection.addTempo( tte.Clock );
                    int bar_count = vsq.getBarCountFromClock( tte.Clock );
                    int bar_top_clock = vsq.getClockFromBarCount( bar_count );
                    //int local_denominator, local_numerator;
                    Timesig timesig = vsq.getTimesigAt( tte.Clock );
                    int clock_per_beat = 480 * 4 / timesig.denominator;
                    int clocks_in_bar = tte.Clock - bar_top_clock;
                    int beat_in_bar = clocks_in_bar / clock_per_beat + 1;
                    int clocks_in_beat = clocks_in_bar - (beat_in_bar - 1) * clock_per_beat;
                    FormTempoConfig dlg = null;
                    try {
                        dlg = new FormTempoConfig( bar_count, beat_in_bar, timesig.numerator, clocks_in_beat, clock_per_beat, (float)(6e7 / tte.Tempo), AppManager.getVsqFile().getPreMeasure() );
                        dlg.setLocation( getFormPreferedLocation( dlg ) );
                        BDialogResult dr = AppManager.showModalDialog( dlg, this );
                        if ( dr == BDialogResult.OK ) {
                            int new_beat = dlg.getBeatCount();
                            int new_clocks_in_beat = dlg.getClock();
                            int new_clock = bar_top_clock + (new_beat - 1) * clock_per_beat + new_clocks_in_beat;
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandUpdateTempo( new_clock, new_clock, (int)(6e7 / (double)dlg.getTempo()) ) );
                            AppManager.editHistory.register( vsq.executeCommand( run ) );
                            setEdited( true );
                            refreshScreen();
                        }
                    } catch ( Exception ex ) {
                        Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex + "\n" );
                        serr.println( "FormMain#picturePositionIndicator_MouseDoubleClick; ex=" + ex );
                    } finally {
                        if ( dlg != null ) {
                            try {
                                dlg.close();
                            } catch ( Exception ex2 ) {
                                Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex2 + "\n" );
                                serr.println( "FormMain#picturePositionIndicator_MouseDoubleClick; ex2=" + ex2 );
                            }
                        }
                    }
                }
            }
        } else {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTempo();
            AppManager.itemSelection.clearTimesig();
            EditTool selected = AppManager.getSelectedTool();
            if ( selected == EditTool.PENCIL ||
                selected == EditTool.LINE ) {
                int changing_clock = AppManager.clockFromXCoord( e.X );
                int changing_tempo = vsq.getTempoAt( changing_clock );
                int bar_count;
                int bar_top_clock;
                int local_denominator, local_numerator;
                bar_count = vsq.getBarCountFromClock( changing_clock );
                bar_top_clock = vsq.getClockFromBarCount( bar_count );
                int index2 = -1;
                for ( int i = 0; i < vsq.TimesigTable.size(); i++ ) {
                    if ( vsq.TimesigTable.get( i ).BarCount > bar_count ) {
                        index2 = i;
                        break;
                    }
                }
                if ( index2 >= 1 ) {
                    local_denominator = vsq.TimesigTable.get( index2 - 1 ).Denominator;
                    local_numerator = vsq.TimesigTable.get( index2 - 1 ).Numerator;
                } else {
                    local_denominator = vsq.TimesigTable.get( 0 ).Denominator;
                    local_numerator = vsq.TimesigTable.get( 0 ).Numerator;
                }
                int clock_per_beat = 480 * 4 / local_denominator;
                int clocks_in_bar = changing_clock - bar_top_clock;
                int beat_in_bar = clocks_in_bar / clock_per_beat + 1;
                int clocks_in_beat = clocks_in_bar - (beat_in_bar - 1) * clock_per_beat;
                FormTempoConfig dlg = null;
                try {
                    dlg = new FormTempoConfig( bar_count - vsq.getPreMeasure() + 1,
                                               beat_in_bar,
                                               local_numerator,
                                               clocks_in_beat,
                                               clock_per_beat,
                                               (float)(6e7 / changing_tempo),
                                               vsq.getPreMeasure() );
                    dlg.setLocation( getFormPreferedLocation( dlg ) );
                    BDialogResult dr = AppManager.showModalDialog( dlg, this );
                    if ( dr == BDialogResult.OK ) {
                        int new_beat = dlg.getBeatCount();
                        int new_clocks_in_beat = dlg.getClock();
                        int new_clock = bar_top_clock + (new_beat - 1) * clock_per_beat + new_clocks_in_beat;
                        CadenciiCommand run = new CadenciiCommand(
                            VsqCommand.generateCommandUpdateTempo( new_clock, new_clock, (int)(6e7 / (double)dlg.getTempo()) ) );
                        AppManager.editHistory.register( vsq.executeCommand( run ) );
                        setEdited( true );
                        refreshScreen();
                    }
                } catch ( Exception ex ) {
                    Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex + "\n" );
                } finally {
                    if ( dlg != null ) {
                        try {
                            dlg.close();
                        } catch ( Exception ex2 ) {
                            Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex2 + "\n" );
                        }
                    }
                }
            }
        }
        mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
    } else if ( 32 < e.Y && e.Y <= picturePositionIndicator.getHeight() - 1 ) {
        AppManager.itemSelection.clearEvent();
        AppManager.itemSelection.clearTempo();
        if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
            int index = 0;
            int last_barcount = AppManager.itemSelection.getLastTimesigBarcount();
            for ( int i = 0; i < vsq.TimesigTable.size(); i++ ) {
                if ( vsq.TimesigTable.get( i ).BarCount == last_barcount ) {
                    index = i;
                    break;
                }
            }
            if ( AppManager.getSelectedTool() == EditTool.ERASER ) {
                if ( vsq.TimesigTable.get( index ).Clock == 0 ) {
                    String msg = _( "Cannot remove first symbol of track!" );
                    statusLabel.setText( msg );
                    return;
                }
                int barcount = vsq.TimesigTable.get( index ).BarCount;
                CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandUpdateTimesig( barcount, barcount, -1, -1 ) );
                AppManager.editHistory.register( vsq.executeCommand( run ) );
                setEdited( true );
            } else {
                int pre_measure = vsq.getPreMeasure();
                int clock = AppManager.clockFromXCoord( e.X );
                int bar_count = vsq.getBarCountFromClock( clock );
                int total_clock = vsq.TotalClocks;
                Timesig timesig = vsq.getTimesigAt( clock );
                boolean num_enabled = !(bar_count == 0);
                FormBeatConfigController dlg = null;
                try {
                    dlg = new FormBeatConfigController( bar_count - pre_measure + 1, timesig.numerator, timesig.denominator, num_enabled, pre_measure );
                    Point p = getFormPreferedLocation( dlg.getWidth(), dlg.getHeight() );
                    dlg.setLocation( p.x, p.y );
                    int dr = AppManager.showModalDialog( dlg.getUi(), this );
                    if ( dr == 1 ) {
                        if ( dlg.isEndSpecified() ) {
                            int[] new_barcounts = new int[2];
                            int[] numerators = new int[2];
                            int[] denominators = new int[2];
                            int[] barcounts = new int[2];
                            new_barcounts[0] = dlg.getStart() + pre_measure - 1;
                            new_barcounts[1] = dlg.getEnd() + pre_measure - 1;
                            numerators[0] = dlg.getNumerator();
                            denominators[0] = dlg.getDenominator();
                            numerators[1] = timesig.numerator;
                            denominators[1] = timesig.denominator;
                            barcounts[0] = bar_count;
                            barcounts[1] = dlg.getEnd() + pre_measure - 1;
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandUpdateTimesigRange( barcounts, new_barcounts, numerators, denominators ) );
                            AppManager.editHistory.register( vsq.executeCommand( run ) );
                            setEdited( true );
                        } else {
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandUpdateTimesig( bar_count,
                                                                         dlg.getStart() + pre_measure - 1,
                                                                         dlg.getNumerator(),
                                                                         dlg.getDenominator() ) );
                            AppManager.editHistory.register( vsq.executeCommand( run ) );
                            setEdited( true );
                        }
                    }
                } catch ( Exception ex ) {
                    Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex + "\n" );
                    serr.println( "FormMain#picturePositionIndicator_MouseDoubleClick; ex=" + ex );
                } finally {
                    if ( dlg != null ) {
                        try {
                            dlg.close();
                        } catch ( Exception ex2 ) {
                            Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex2 + "\n" );
                            serr.println( "FormMain#picturePositionIndicator_MouseDoubleClic; ex2=" + ex2 );
                        }
                    }
                }
            }
        } else {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTempo();
            AppManager.itemSelection.clearTimesig();
            EditTool selected = AppManager.getSelectedTool();
            if ( selected == EditTool.PENCIL ||
                selected == EditTool.LINE ) {
                int pre_measure = AppManager.getVsqFile().getPreMeasure();
                int clock = AppManager.clockFromXCoord( e.X );
                int bar_count = AppManager.getVsqFile().getBarCountFromClock( clock );
                int numerator, denominator;
                Timesig timesig = AppManager.getVsqFile().getTimesigAt( clock );
                int total_clock = AppManager.getVsqFile().TotalClocks;
                //int max_barcount = AppManager.VsqFile.getBarCountFromClock( total_clock ) - pre_measure + 1;
                //int min_barcount = 1;
                FormBeatConfigController dlg = null;
                try {
                    dlg = new FormBeatConfigController( bar_count - pre_measure + 1, timesig.numerator, timesig.denominator, true, pre_measure );
                    Point p = getFormPreferedLocation( dlg.getWidth(), dlg.getHeight() );
                    dlg.setLocation( p.x, p.y );
                    int dr = AppManager.showModalDialog( dlg.getUi(), this );
                    if ( dr == 1 ) {
                        if ( dlg.isEndSpecified() ) {
                            int[] new_barcounts = new int[2];
                            int[] numerators = new int[2];
                            int[] denominators = new int[2];
                            int[] barcounts = new int[2];
                            new_barcounts[0] = dlg.getStart() + pre_measure - 1;
                            new_barcounts[1] = dlg.getEnd() + pre_measure - 1 + 1;
                            numerators[0] = dlg.getNumerator();
                            numerators[1] = timesig.numerator;

                            denominators[0] = dlg.getDenominator();
                            denominators[1] = timesig.denominator;

                            barcounts[0] = dlg.getStart() + pre_measure - 1;
                            barcounts[1] = dlg.getEnd() + pre_measure - 1 + 1;
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandUpdateTimesigRange( barcounts, new_barcounts, numerators, denominators ) );
                            AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
                            setEdited( true );
                        } else {
                            CadenciiCommand run = new CadenciiCommand(
                                VsqCommand.generateCommandUpdateTimesig( bar_count,
                                                               dlg.getStart() + pre_measure - 1,
                                                               dlg.getNumerator(),
                                                               dlg.getDenominator() ) );
                            AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
                            setEdited( true );
                        }
                    }
                } catch ( Exception ex ) {
                    Logger.write( FormMain.class + ".picutrePositionIndicator_MouseDoubleClick; ex=" + ex + "\n" );
                } finally {
                    if ( dlg != null ) {
                        try {
                            dlg.close();
                        } catch ( Exception ex2 ) {
                            Logger.write( FormMain.class + ".picturePositionIndicator_MouseDoubleClick; ex=" + ex2 + "\n" );
                        }
                    }
                }
            }
        }
        mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
    }
    picturePositionIndicator.repaint();
    pictPianoRoll.repaint();
}
        }

        public void picturePositionIndicator_MouseDown( Object sender, BMouseEventArgs e )
        {
if ( e.X < AppManager.keyWidth || getWidth() - 3 < e.X ) {
    return;
}

mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
int modifiers = PortUtil.getCurrentModifierKey();
VsqFileEx vsq = AppManager.getVsqFile();
if ( e.Button == BMouseButtons.Left ) {
    if ( 0 <= e.Y && e.Y <= 18 ) {
        int tolerance = AppManager.editorConfig.PxTolerance;
        int start_marker_width = Resources.get_start_marker().getWidth( this );
        int end_marker_width = Resources.get_end_marker().getWidth( this );
        int startx = AppManager.xCoordFromClocks( vsq.config.StartMarker );
        int endx = AppManager.xCoordFromClocks( vsq.config.EndMarker );
        
        // マウスの当たり判定が重なるようなら，判定幅を最小にする
        int start0 = startx - tolerance;
        int start1 = startx + start_marker_width + tolerance;
        int end0 = endx - end_marker_width - tolerance;
        int end1 = endx + tolerance;
        if ( vsq.config.StartMarkerEnabled && vsq.config.EndMarkerEnabled ) {
            if ( start0 < end1 && end1 < start1 ||
                start1 < end0 && end0 < start1 ) {
                start0 = startx;
                start1 = startx + start_marker_width;
                end0 = endx - end_marker_width;
                end1 = endx;
            }
        }

        if ( vsq.config.StartMarkerEnabled ) {
            if ( start0 <= e.X && e.X <= start1 ) {
                mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.MARK_START;
            }
        }
        if ( vsq.config.EndMarkerEnabled && mPositionIndicatorMouseDownMode != PositionIndicatorMouseDownMode.MARK_START ) {
            if ( end0 <= e.X && e.X <= end1 ) {
                mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.MARK_END;
            }
        }
    } else if ( 18 < e.Y && e.Y <= 32 ) {
        int index = -1;
        int count = AppManager.getVsqFile().TempoTable.size();
        for ( int i = 0; i < count; i++ ) {
            int clock = AppManager.getVsqFile().TempoTable.get( i ).Clock;
            int x = AppManager.xCoordFromClocks( clock );
            if ( x < 0 ) {
                continue;
            } else if ( getWidth() < x ) {
                break;
            }
            String s = PortUtil.formatDecimal( "#.00", 60e6 / (float)AppManager.getVsqFile().TempoTable.get( i ).Tempo );
            Dimension size = Util.measureString( s, new Font( AppManager.editorConfig.ScreenFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE8 ) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), new Rectangle( x, 14, (int)size.width, 14 ) ) ) {
                index = i;
                break;
            }
        }

        if ( index >= 0 ) {
            int clock = AppManager.getVsqFile().TempoTable.get( index ).Clock;
            if ( AppManager.getSelectedTool() != EditTool.ERASER ) {
                int mouse_clock = AppManager.clockFromXCoord( e.X );
                mTempoDraggingDeltaClock = mouse_clock - clock;
                mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.TEMPO;
            }
            if ( (modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ) {
                if ( AppManager.itemSelection.getTempoCount() > 0 ) {
                    int last_clock = AppManager.itemSelection.getLastTempoClock();
                    int start = Math.min( last_clock, clock );
                    int end = Math.max( last_clock, clock );
                    for ( int i = 0; i < AppManager.getVsqFile().TempoTable.size(); i++ ) {
                        int tclock = AppManager.getVsqFile().TempoTable.get( i ).Clock;
                        if ( tclock < start ) {
                            continue;
                        } else if ( end < tclock ) {
                            break;
                        }
                        if ( start <= tclock && tclock <= end ) {
                            AppManager.itemSelection.addTempo( tclock );
                        }
                    }
                } else {
                    AppManager.itemSelection.addTempo( clock );
                }
            } else if ( (modifiers & s_modifier_key) == s_modifier_key ) {
                if ( AppManager.itemSelection.isTempoContains( clock ) ) {
                    AppManager.itemSelection.removeTempo( clock );
                } else {
                    AppManager.itemSelection.addTempo( clock );
                }
            } else {
                if ( !AppManager.itemSelection.isTempoContains( clock ) ) {
                    AppManager.itemSelection.clearTempo();
                }
                AppManager.itemSelection.addTempo( clock );
            }
        } else {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTempo();
            AppManager.itemSelection.clearTimesig();
        }
    } else if ( 32 < e.Y && e.Y <= picturePositionIndicator.getHeight() - 1 ) {
        // クリック位置に拍子が表示されているかどうか検査
        int index = -1;
        for ( int i = 0; i < AppManager.getVsqFile().TimesigTable.size(); i++ ) {
            String s = AppManager.getVsqFile().TimesigTable.get( i ).Numerator + "/" + AppManager.getVsqFile().TimesigTable.get( i ).Denominator;
            int x = AppManager.xCoordFromClocks( AppManager.getVsqFile().TimesigTable.get( i ).Clock );
            Dimension size = Util.measureString( s, new Font( AppManager.editorConfig.ScreenFontName, java.awt.Font.PLAIN, AppManager.FONT_SIZE8 ) );
            if ( Utility.isInRect( new Point( e.X, e.Y ), new Rectangle( x, 28, (int)size.width, 14 ) ) ) {
                index = i;
                break;
            }
        }

        if ( index >= 0 ) {
            int barcount = AppManager.getVsqFile().TimesigTable.get( index ).BarCount;
            if ( AppManager.getSelectedTool() != EditTool.ERASER ) {
                int barcount_clock = AppManager.getVsqFile().getClockFromBarCount( barcount );
                int mouse_clock = AppManager.clockFromXCoord( e.X );
                mTimesigDraggingDeltaClock = mouse_clock - barcount_clock;
                mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.TIMESIG;
            }
            if ( (modifiers & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ) {
                if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
                    int last_barcount = AppManager.itemSelection.getLastTimesigBarcount();
                    int start = Math.min( last_barcount, barcount );
                    int end = Math.max( last_barcount, barcount );
                    for ( int i = 0; i < AppManager.getVsqFile().TimesigTable.size(); i++ ) {
                        int tbarcount = AppManager.getVsqFile().TimesigTable.get( i ).BarCount;
                        if ( tbarcount < start ) {
                            continue;
                        } else if ( end < tbarcount ) {
                            break;
                        }
                        if ( start <= tbarcount && tbarcount <= end ) {
                            AppManager.itemSelection.addTimesig( AppManager.getVsqFile().TimesigTable.get( i ).BarCount );
                        }
                    }
                } else {
                    AppManager.itemSelection.addTimesig( barcount );
                }
            } else if ( (modifiers & s_modifier_key) == s_modifier_key ) {
                if ( AppManager.itemSelection.isTimesigContains( barcount ) ) {
                    AppManager.itemSelection.removeTimesig( barcount );
                } else {
                    AppManager.itemSelection.addTimesig( barcount );
                }
            } else {
                if ( !AppManager.itemSelection.isTimesigContains( barcount ) ) {
                    AppManager.itemSelection.clearTimesig();
                }
                AppManager.itemSelection.addTimesig( barcount );
            }
        } else {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTempo();
            AppManager.itemSelection.clearTimesig();
        }
    }
}
refreshScreen();
        }

        public void picturePositionIndicator_MouseUp( Object sender, BMouseEventArgs e )
        {
int modifiers = PortUtil.getCurrentModifierKey();
if ( e.Button == BMouseButtons.Left ) {
    VsqFileEx vsq = AppManager.getVsqFile();
    if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.NONE ) {
        if ( 4 <= e.Y && e.Y <= 18 ) {
            int clock = AppManager.clockFromXCoord( e.X );
            if ( AppManager.editorConfig.getPositionQuantize() != QuantizeMode.off ) {
                int unit = AppManager.getPositionQuantizeClock();
                clock = doQuantize( clock, unit );
            }
            if ( AppManager.isPlaying() ) {
                AppManager.setPlaying( false, this );
                AppManager.setCurrentClock( clock );
                AppManager.setPlaying( true, this );
            } else {
                AppManager.setCurrentClock( clock );
            }
            refreshScreen();
        } else if ( 18 < e.Y && e.Y <= 32 ) {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTimesig();
            if ( AppManager.itemSelection.getTempoCount() > 0 ) {
                int index = -1;
                int clock = AppManager.itemSelection.getLastTempoClock();
                for ( int i = 0; i < vsq.TempoTable.size(); i++ ) {
                    if ( clock == vsq.TempoTable.get( i ).Clock ) {
                        index = i;
                        break;
                    }
                }
                if ( index >= 0 && AppManager.getSelectedTool() == EditTool.ERASER ) {
                    if ( vsq.TempoTable.get( index ).Clock == 0 ) {
                        String msg = _( "Cannot remove first symbol of track!" );
                        statusLabel.setText( msg );
                        return;
                    }
                    CadenciiCommand run = new CadenciiCommand(
                        VsqCommand.generateCommandUpdateTempo( vsq.TempoTable.get( index ).Clock,
                                                               vsq.TempoTable.get( index ).Clock,
                                                               -1 ) );
                    AppManager.editHistory.register( vsq.executeCommand( run ) );
                    setEdited( true );
                }
            }
            mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
        } else if ( 32 < e.Y && e.Y <= picturePositionIndicator.getHeight() - 1 ) {
            AppManager.itemSelection.clearEvent();
            AppManager.itemSelection.clearTempo();
            if ( AppManager.itemSelection.getTimesigCount() > 0 ) {
                int index = 0;
                int last_barcount = AppManager.itemSelection.getLastTimesigBarcount();
                for ( int i = 0; i < vsq.TimesigTable.size(); i++ ) {
                    if ( vsq.TimesigTable.get( i ).BarCount == last_barcount ) {
                        index = i;
                        break;
                    }
                }
                if ( AppManager.getSelectedTool() == EditTool.ERASER ) {
                    if ( vsq.TimesigTable.get( index ).Clock == 0 ) {
                        String msg = _( "Cannot remove first symbol of track!" );
                        statusLabel.setText( msg );
                        return;
                    }
                    int barcount = vsq.TimesigTable.get( index ).BarCount;
                    CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandUpdateTimesig( barcount, barcount, -1, -1 ) );
                    AppManager.editHistory.register( vsq.executeCommand( run ) );
                    setEdited( true );
                }
            }
        }
    } else if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TEMPO ) {
        int count = AppManager.itemSelection.getTempoCount();
        int[] clocks = new int[count];
        int[] new_clocks = new int[count];
        int[] tempos = new int[count];
        int i = -1;
        boolean contains_first_tempo = false;
        for ( Iterator<ValuePair<Integer, SelectedTempoEntry>> itr = AppManager.itemSelection.getTempoIterator(); itr.hasNext(); ) {
            ValuePair<Integer, SelectedTempoEntry> item = itr.next();
            int clock = item.getKey();
            i++;
            clocks[i] = clock;
            if ( clock == 0 ) {
                contains_first_tempo = true;
                break;
            }
            TempoTableEntry editing = AppManager.itemSelection.getTempo( clock ).editing;
            new_clocks[i] = editing.Clock;
            tempos[i] = editing.Tempo;
        }
        if ( contains_first_tempo ) {
        } else {
            CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandUpdateTempoRange( clocks, new_clocks, tempos ) );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
            setEdited( true );
        }
    } else if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TIMESIG ) {
        int count = AppManager.itemSelection.getTimesigCount();
        int[] barcounts = new int[count];
        int[] new_barcounts = new int[count];
        int[] numerators = new int[count];
        int[] denominators = new int[count];
        int i = -1;
        boolean contains_first_bar = false;
        for ( Iterator<ValuePair<Integer, SelectedTimesigEntry>> itr = AppManager.itemSelection.getTimesigIterator(); itr.hasNext(); ) {
            ValuePair<Integer, SelectedTimesigEntry> item = itr.next();
            int bar = item.getKey();
            i++;
            barcounts[i] = bar;
            if ( bar == 0 ) {
                contains_first_bar = true;
                break;
            }
            TimeSigTableEntry editing = AppManager.itemSelection.getTimesig( bar ).editing;
            new_barcounts[i] = editing.BarCount;
            numerators[i] = editing.Numerator;
            denominators[i] = editing.Denominator;
        }
        if ( contains_first_bar ) {
        } else {
            CadenciiCommand run = new CadenciiCommand(
                VsqCommand.generateCommandUpdateTimesigRange( barcounts, new_barcounts, numerators, denominators ) );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
            setEdited( true );
        }
    }
}
mPositionIndicatorMouseDownMode = PositionIndicatorMouseDownMode.NONE;
pictPianoRoll.repaint();
picturePositionIndicator.repaint();
        }

        public void picturePositionIndicator_MouseMove( Object sender, BMouseEventArgs e )
        {
VsqFileEx vsq = AppManager.getVsqFile();
if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TEMPO ) {
    int clock = AppManager.clockFromXCoord( e.X ) - mTempoDraggingDeltaClock;
    int step = AppManager.getPositionQuantizeClock();
    clock = doQuantize( clock, step );
    int last_clock = AppManager.itemSelection.getLastTempoClock();
    int dclock = clock - last_clock;
    for ( Iterator<ValuePair<Integer, SelectedTempoEntry>> itr = AppManager.itemSelection.getTempoIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTempoEntry> item = itr.next();
        int key = item.getKey();
        AppManager.itemSelection.getTempo( key ).editing.Clock = AppManager.itemSelection.getTempo( key ).original.Clock + dclock;
    }
    picturePositionIndicator.repaint();
} else if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.TIMESIG ) {
    int clock = AppManager.clockFromXCoord( e.X ) - mTimesigDraggingDeltaClock;
    int barcount = vsq.getBarCountFromClock( clock );
    int last_barcount = AppManager.itemSelection.getLastTimesigBarcount();
    int dbarcount = barcount - last_barcount;
    for ( Iterator<ValuePair<Integer, SelectedTimesigEntry>> itr = AppManager.itemSelection.getTimesigIterator(); itr.hasNext(); ) {
        ValuePair<Integer, SelectedTimesigEntry> item = itr.next();
        int bar = item.getKey();
        AppManager.itemSelection.getTimesig( bar ).editing.BarCount = AppManager.itemSelection.getTimesig( bar ).original.BarCount + dbarcount;
    }
    picturePositionIndicator.repaint();
} else if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.MARK_START ) {
    int clock = AppManager.clockFromXCoord( e.X );
    int unit = AppManager.getPositionQuantizeClock();
    clock = doQuantize( clock, unit );
    if ( clock < 0 ) {
        clock = 0;
    }
    int draft_start = Math.min( clock, vsq.config.EndMarker );
    int draft_end = Math.max( clock, vsq.config.EndMarker );
    if ( draft_start != vsq.config.StartMarker ) {
        vsq.config.StartMarker = draft_start;
        setEdited( true );
    }
    if ( draft_end != vsq.config.EndMarker ) {
        vsq.config.EndMarker = draft_end;
        setEdited( true );
    }
    refreshScreen();
} else if ( mPositionIndicatorMouseDownMode == PositionIndicatorMouseDownMode.MARK_END ) {
    int clock = AppManager.clockFromXCoord( e.X );
    int unit = AppManager.getPositionQuantizeClock();
    clock = doQuantize( clock, unit );
    if ( clock < 0 ) {
        clock = 0;
    }
    int draft_start = Math.min( clock, vsq.config.StartMarker );
    int draft_end = Math.max( clock, vsq.config.StartMarker );
    if ( draft_start != vsq.config.StartMarker ) {
        vsq.config.StartMarker = draft_start;
        setEdited( true );
    }
    if ( draft_end != vsq.config.EndMarker ) {
        vsq.config.EndMarker = draft_end;
        setEdited( true );
    }
    refreshScreen();
}
        }

        public void picturePositionIndicator_Paint( Object sender, BPaintEventArgs e )
        {
Graphics g = e.Graphics;
picturePositionIndicatorDrawTo( g );
        }

        public void picturePositionIndicator_PreviewKeyDown( Object sender, BPreviewKeyDownEventArgs e )
        {
BKeyEventArgs e0 = new BKeyEventArgs( e.getRawEvent() );
processSpecialShortcutKey( e0, true );
        }

        //BOOKMARK: trackBar
        public void trackBar_Enter( Object sender, BEventArgs e )
        {
focusPianoRoll();
        }

        public void trackBar_ValueChanged( Object sender, BEventArgs e )
        {
controller.setScaleX( getScaleXFromTrackBarValue( trackBar.getValue() ) );
controller.setStartToDrawX( calculateStartToDrawX() );
updateDrawObjectList();
repaint();
        }

        //BOOKMARK: menuHelp
        public void menuHelpAbout_Click( Object sender, BEventArgs e )
        {

String version_str = Utility.getVersion();
if ( mVersionInfo == null ) {
    mVersionInfo = new VersionInfo( _APP_NAME, version_str );
    mVersionInfo.setAuthorList( _CREDIT );
    mVersionInfo.setVisible( true );
} else {
    mVersionInfo.setVisible( true );
}
        }

        public void menuHelpDebug_Click( Object sender, BEventArgs e )
        {
        }

        public void menuHelpManual_Click( Object sender, BEventArgs e )
        {
// 現在のUI言語と同じ版のマニュアルファイルがあるかどうか探す
String lang = Messaging.getLanguage();
String pdf = fsys.combine( PortUtil.getApplicationStartupPath(), "manual_" + lang + ".pdf" );
if( !fsys.isFileExists( pdf ) ){
    // 無ければ英語版のマニュアルを表示することにする
    pdf = fsys.combine( PortUtil.getApplicationStartupPath(), "manual_en.pdf" );
}
if( !fsys.isFileExists( pdf ) ){
    AppManager.showMessageBox(
        _( "file not found" ),
        _APP_NAME,
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
try{
    // TODO: manual_*.pdfを開く．Linux版ではどうする？
    Runtime.getRuntime().exec( new String[]{ "open", pdf } );
}catch( Exception ex ){
    ex.printStackTrace();
}
        }

        public void menuHelpLogSwitch_CheckedChanged( Object sender, BEventArgs e )
        {
Logger.setEnabled( menuHelpLogSwitch.isSelected() );
if ( menuHelpLogSwitch.isSelected() ) {
    menuHelpLogSwitch.setText( _( "Enabled" ) );
} else {
    menuHelpLogSwitch.setText( _( "Disabled" ) );
}
        }

        public void menuHelpLogOpen_Click( Object sender, BEventArgs e )
        {
String file = Logger.getPath();
if ( file == null || (file != null && (!fsys.isFileExists( file ))) ) {
    // ログがまだできてないのでダイアログ出す
    AppManager.showMessageBox(
        _( "Log file has not generated yet." ),
        _( "Info" ),
        PortUtil.OK_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_INFORMATION_MESSAGE );
    return;
}

// ログファイルを開く
try{
    Runtime.getRuntime().exec( new String[] { "open", file } );
}catch( Exception ex ){
    ex.printStackTrace();
}
        }

        //BOOKMARK: trackSelector
        public void trackSelector_CommandExecuted( Object sender, BEventArgs e )
        {
setEdited( true );
refreshScreen();
        }

        public void trackSelector_MouseClick( Object sender, BMouseEventArgs e )
        {
if ( e.Button == BMouseButtons.Right ) {
    if ( AppManager.keyWidth < e.X && e.X < trackSelector.getWidth() ) {
        if ( trackSelector.getHeight() - TrackSelector.OFFSET_TRACK_TAB <= e.Y && e.Y <= trackSelector.getHeight() ) {
            cMenuTrackTab.show( trackSelector, e.X, e.Y );
        } else {
            cMenuTrackSelector.show( trackSelector, e.X, e.Y );
        }
    }
}
        }

        public void trackSelector_MouseDoubleClick( Object sender, BMouseEventArgs e )
        {
if ( e.Button == BMouseButtons.Middle ) {
    // ツールをポインター <--> 鉛筆に切り替える
    if ( AppManager.keyWidth < e.X &&
         e.Y < trackSelector.getHeight() - TrackSelector.OFFSET_TRACK_TAB * 2 ) {
        if ( AppManager.getSelectedTool() == EditTool.ARROW ) {
            AppManager.setSelectedTool( EditTool.PENCIL );
        } else {
            AppManager.setSelectedTool( EditTool.ARROW );
        }
    }
}
        }

        public void trackSelector_MouseDown( Object sender, BMouseEventArgs e )
        {
if ( AppManager.keyWidth < e.X ) {
    mMouseDownedTrackSelector = true;
    if ( isMouseMiddleButtonDowned( e.Button ) ) {
        mEditCurveMode = CurveEditMode.MIDDLE_DRAG;
        mButtonInitial = new Point( e.X, e.Y );
        mMiddleButtonHScroll = hScroll.getValue();
    }
}
        }

        public void trackSelector_MouseMove( Object sender, BMouseEventArgs e )
        {
if ( mFormActivated && AppManager.mInputTextBox != null ){
    boolean input_visible = AppManager.mInputTextBox.isVisible();
    boolean prop_editing = AppManager.propertyPanel.isEditing();
    if( !input_visible && !prop_editing ){
        trackSelector.requestFocus();
    }
}
if ( e.Button == BMouseButtons.None ) {
    if ( !timer.isRunning() ) {
        refreshScreen( true );
    }
    return;
}
int parent_width = ((TrackSelector)sender).getWidth();
if ( mEditCurveMode == CurveEditMode.MIDDLE_DRAG ) {
    if ( AppManager.isPlaying() ) {
        return;
    }

    int draft = computeHScrollValueForMiddleDrag( e.X );
    if ( hScroll.getValue() != draft ) {
        hScroll.setValue( draft );
    }
} else {
    if ( mMouseDownedTrackSelector ) {
        if ( mExtDragXTrackSelector == ExtDragXMode.NONE ) {
            if ( AppManager.keyWidth > e.X ) {
                mExtDragXTrackSelector = ExtDragXMode.LEFT;
            } else if ( parent_width < e.X ) {
                mExtDragXTrackSelector = ExtDragXMode.RIGHT;
            }
        } else {
            if ( AppManager.keyWidth <= e.X && e.X <= parent_width ) {
                mExtDragXTrackSelector = ExtDragXMode.NONE;
            }
        }
    } else {
        mExtDragXTrackSelector = ExtDragXMode.NONE;
    }

    if ( mExtDragXTrackSelector != ExtDragXMode.NONE ) {
        double now = PortUtil.getCurrentTime();
        double dt = now - mTimerDragLastIgnitted;
        mTimerDragLastIgnitted = now;
        int px_move = AppManager.editorConfig.MouseDragIncrement;
        if ( px_move / dt > AppManager.editorConfig.MouseDragMaximumRate ) {
            px_move = (int)(dt * AppManager.editorConfig.MouseDragMaximumRate);
        }
        px_move += 5;
        if ( mExtDragXTrackSelector == ExtDragXMode.LEFT ) {
            px_move *= -1;
        }
        double d_draft = hScroll.getValue() + px_move * controller.getScaleXInv();
        if ( d_draft < 0.0 ) {
            d_draft = 0.0;
        }
        int draft = (int)d_draft;
        if ( hScroll.getMaximum() < draft ) {
            hScroll.setMaximum( draft );
        }
        if ( draft < hScroll.getMinimum() ) {
            draft = hScroll.getMinimum();
        }
        hScroll.setValue( draft );
    }
}
if ( !timer.isRunning() ) {
    refreshScreen( true );
}
        }

        public void trackSelector_MouseUp( Object sender, BMouseEventArgs e )
        {
mMouseDownedTrackSelector = false;
if ( mEditCurveMode == CurveEditMode.MIDDLE_DRAG ) {
    mEditCurveMode = CurveEditMode.NONE;
    setCursor( new Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
}
        }

        public void trackSelector_MouseWheel( Object sender, BMouseEventArgs e )
        {
if ( (PortUtil.getCurrentModifierKey() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ) {
    double new_val = (double)vScroll.getValue() - e.Delta;
    int max = vScroll.getMaximum() - vScroll.getMinimum();
    int min = vScroll.getMinimum();
    if ( new_val > max ) {
        vScroll.setValue( max );
    } else if ( new_val < min ) {
        vScroll.setValue( min );
    } else {
        vScroll.setValue( (int)new_val );
    }
} else {
    hScroll.setValue( computeScrollValueFromWheelDelta( e.Delta ) );
}
refreshScreen();
        }

        public void trackSelector_PreferredMinHeightChanged( Object sender, BEventArgs e )
        {
if ( menuVisualControlTrack.isSelected() ) {
    splitContainer1.setPanel2MinSize( trackSelector.getPreferredMinSize() );
}
        }

        public void trackSelector_PreviewKeyDown( Object sender, BPreviewKeyDownEventArgs e )
        {
BKeyEventArgs e0 = new BKeyEventArgs( e.getRawEvent() );
processSpecialShortcutKey( e0, true );
        }

        public void trackSelector_RenderRequired( Object sender, int track )
        {
Vector<Integer> list = new Vector<Integer>();
list.add( track );
AppManager.patchWorkToFreeze( this, list );
/*int selected = AppManager.getSelected();
Vector<Integer> t = new Vector<Integer>( Arrays.asList( PortUtil.convertIntArray( tracks ) ) );
if ( t.contains( selected) ) {
    String file = fsys.combine( AppManager.getTempWaveDir(), selected + ".wav" );
    if ( PortUtil.isFileExists( file ) ) {
#if JAVA
        Thread loadwave_thread = new Thread( new LoadWaveProc( file ) );
        loadwave_thread.start();
#else
        Thread loadwave_thread = new Thread( new ParameterizedThreadStart( this.loadWave ) );
        loadwave_thread.IsBackground = true;
        loadwave_thread.Start( new Object[]{ file, selected - 1 } );
#endif
    }
}*/
        }

        public void trackSelector_SelectedCurveChanged( Object sender, CurveType type )
        {
refreshScreen();
        }

        public void trackSelector_SelectedTrackChanged( Object sender, int selected )
        {
AppManager.itemSelection.clearBezier();
AppManager.itemSelection.clearEvent();
AppManager.itemSelection.clearPoint();
updateDrawObjectList();
refreshScreen();
        }

        //BOOKMARK: cMenuPiano
        public void cMenuPianoDelete_Click( Object sender, BEventArgs e )
        {
deleteEvent();
        }

        public void cMenuPianoVibratoProperty_Click( Object sender, BEventArgs e )
        {
editNoteVibratoProperty();
        }

        public void cMenuPianoPaste_Click( Object sender, BEventArgs e )
        {
pasteEvent();
        }

        public void cMenuPianoCopy_Click( Object sender, BEventArgs e )
        {
copyEvent();
        }

        public void cMenuPianoCut_Click( Object sender, BEventArgs e )
        {
cutEvent();
        }

        public void cMenuPianoExpression_Click( Object sender, BEventArgs e )
        {
if ( AppManager.itemSelection.getEventCount() > 0 ) {
    VsqFileEx vsq = AppManager.getVsqFile();
    int selected = AppManager.getSelected();
    SynthesizerType type = SynthesizerType.VOCALOID2;
    RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( selected ) );
    if ( kind == RendererKind.VOCALOID1 ) {
        type = SynthesizerType.VOCALOID1;
    }
    VsqEvent original = AppManager.itemSelection.getLastEvent().original;
    FormNoteExpressionConfig dlg = null;
    try {
        dlg = new FormNoteExpressionConfig( type, original.ID.NoteHeadHandle );
        int id = AppManager.itemSelection.getLastEvent().original.InternalID;
        dlg.setPMBendDepth( original.ID.PMBendDepth );
        dlg.setPMBendLength( original.ID.PMBendLength );
        dlg.setPMbPortamentoUse( original.ID.PMbPortamentoUse );
        dlg.setDEMdecGainRate( original.ID.DEMdecGainRate );
        dlg.setDEMaccent( original.ID.DEMaccent );
        BDialogResult dr = AppManager.showModalDialog( dlg, this );
        if ( dr == BDialogResult.OK ) {
            VsqID copy = (VsqID)original.ID.clone();
            copy.PMBendDepth = dlg.getPMBendDepth();
            copy.PMBendLength = dlg.getPMBendLength();
            copy.PMbPortamentoUse = dlg.getPMbPortamentoUse();
            copy.DEMdecGainRate = dlg.getDEMdecGainRate();
            copy.DEMaccent = dlg.getDEMaccent();
            copy.NoteHeadHandle = dlg.getEditedNoteHeadHandle();
            CadenciiCommand run = new CadenciiCommand(
                VsqCommand.generateCommandEventChangeIDContaints( selected, id, copy ) );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
            setEdited( true );
        }
    } catch ( Exception ex ) {
        Logger.write( FormMain.class + ".cMenuPianoExpression_Click; ex=" + ex + "\n" );
    } finally {
        if ( dlg != null ) {
            try {
                dlg.close();
            } catch ( Exception ex2 ) {
                Logger.write( FormMain.class + ".cMenuPianoExpression_Click; ex=" + ex2 + "\n" );
            }
        }
    }
}
        }

        public void cMenuPianoPointer_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ARROW );
        }

        public void cMenuPianoPencil_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.PENCIL );
        }

        public void cMenuPianoEraser_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ERASER );
        }

        public void cMenuPianoGrid_Click( Object sender, BEventArgs e )
        {
boolean new_v = !AppManager.isGridVisible();
cMenuPianoGrid.setSelected( new_v );
AppManager.setGridVisible( new_v );
        }

        public void cMenuPianoUndo_Click( Object sender, BEventArgs e )
        {
undo();
        }

        public void cMenuPianoRedo_Click( Object sender, BEventArgs e )
        {
redo();
        }

        public void cMenuPianoSelectAllEvents_Click( Object sender, BEventArgs e )
        {
selectAllEvent();
        }

        public void cMenuPianoProperty_Click( Object sender, BEventArgs e )
        {
editNoteExpressionProperty();
        }

        public void cMenuPianoImportLyric_Click( Object sender, BEventArgs e )
        {
importLyric();
        }

        public void cMenuPiano_Opening( Object sender, BCancelEventArgs e )
        {
updateCopyAndPasteButtonStatus();
cMenuPianoImportLyric.setEnabled( AppManager.itemSelection.getLastEvent() != null );
        }

        public void cMenuPianoSelectAll_Click( Object sender, BEventArgs e )
        {
selectAll();
        }

        public void cMenuPianoFixed01_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L1 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed02_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L2 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed04_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L4 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed08_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L8 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed16_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L16 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed32_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L32 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed64_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L64 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixed128_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.L128 );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixedOff_Click( Object sender, BEventArgs e )
        {
mPencilMode.setMode( PencilModeEnum.Off );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixedTriplet_Click( Object sender, BEventArgs e )
        {
mPencilMode.setTriplet( !mPencilMode.isTriplet() );
updateCMenuPianoFixed();
        }

        public void cMenuPianoFixedDotted_Click( Object sender, BEventArgs e )
        {
mPencilMode.setDot( !mPencilMode.isDot() );
updateCMenuPianoFixed();
        }

        public void cMenuPianoCurve_Click( Object sender, BEventArgs e )
        {
AppManager.setCurveMode( !AppManager.isCurveMode() );
applySelectedTool();
        }

        //BOOKMARK: menuTrack
        public void menuTrack_DropDownOpening( Object sender, BEventArgs e )
        {
updateTrackMenuStatus();
        }

        public void menuTrackCopy_Click( Object sender, BEventArgs e )
        {
copyTrackCore();
        }

        public void menuTrackChangeName_Click( Object sender, BEventArgs e )
        {
changeTrackNameCore();
        }

        public void menuTrackDelete_Click( Object sender, BEventArgs e )
        {
deleteTrackCore();
        }

        public void menuTrackAdd_Click( Object sender, BEventArgs e )
        {
addTrackCore();
        }

        public void menuTrackOverlay_Click( Object sender, BEventArgs e )
        {
AppManager.setOverlay( !AppManager.isOverlay() );
refreshScreen();
        }

        public void menuTrackRenderCurrent_Click( Object sender, BEventArgs e )
        {
Vector<Integer> tracks = new Vector<Integer>();
tracks.add( AppManager.getSelected() );
AppManager.patchWorkToFreeze( this, tracks );
        }

        public void menuTrackRenderer_DropDownOpening( Object sender, BEventArgs e )
        {
updateRendererMenu();
        }

        //BOOKMARK: menuHidden
        public void menuHiddenVisualForwardParameter_Click( Object sender, BEventArgs e )
        {
trackSelector.SelectNextCurve();
        }

        public void menuHiddenVisualBackwardParameter_Click( Object sender, BEventArgs e )
        {
trackSelector.SelectPreviousCurve();
        }

        public void menuHiddenTrackNext_Click( Object sender, BEventArgs e )
        {
if ( AppManager.getSelected() == AppManager.getVsqFile().Track.size() - 1 ) {
    AppManager.setSelected( 1 );
} else {
    AppManager.setSelected( AppManager.getSelected() + 1 );
}
refreshScreen();
        }

        public void menuHiddenShorten_Click( Object sender, BEventArgs e )
        {
QuantizeMode qmode = AppManager.editorConfig.getLengthQuantize();
boolean triplet = AppManager.editorConfig.isLengthQuantizeTriplet();
int delta = -QuantizeModeUtil.getQuantizeClock( qmode, triplet );
lengthenSelectedEvent( delta );
        }

        public void menuHiddenTrackBack_Click( Object sender, BEventArgs e )
        {
if ( AppManager.getSelected() == 1 ) {
    AppManager.setSelected( AppManager.getVsqFile().Track.size() - 1 );
} else {
    AppManager.setSelected( AppManager.getSelected() - 1 );
}
refreshScreen();
        }

        public void menuHiddenEditPaste_Click( Object sender, BEventArgs e )
        {
pasteEvent();
        }

        public void menuHiddenFlipCurveOnPianorollMode_Click( Object sender, BEventArgs e )
        {
AppManager.mCurveOnPianoroll = !AppManager.mCurveOnPianoroll;
refreshScreen();
        }

        public void menuHiddenGoToEndMarker_Click( Object sender, BEventArgs e )
        {
if ( AppManager.isPlaying() ) {
    return;
}

VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq.config.EndMarkerEnabled ) {
    AppManager.setCurrentClock( vsq.config.EndMarker );
    ensureCursorVisible();
    refreshScreen();
}
        }

        public void menuHiddenGoToStartMarker_Click( Object sender, BEventArgs e )
        {
if ( AppManager.isPlaying() ) {
    return;
}

VsqFileEx vsq = AppManager.getVsqFile();
if ( vsq.config.StartMarkerEnabled ) {
    AppManager.setCurrentClock( vsq.config.StartMarker );
    ensureCursorVisible();
    refreshScreen();
}
        }

        public void menuHiddenLengthen_Click( Object sender, BEventArgs e )
        {
QuantizeMode qmode = AppManager.editorConfig.getLengthQuantize();
boolean triplet = AppManager.editorConfig.isLengthQuantizeTriplet();
int delta = QuantizeModeUtil.getQuantizeClock( qmode, triplet );
lengthenSelectedEvent( delta );
        }

        public void menuHiddenMoveDown_Click( Object sender, BEventArgs e )
        {
moveUpDownLeftRight( -1, 0 );
        }

        public void menuHiddenMoveUp_Click( Object sender, BEventArgs e )
        {
moveUpDownLeftRight( 1, 0 );
        }

        public void menuHiddenPlayFromStartMarker_Click( Object sender, BEventArgs e )
        {
if ( AppManager.isPlaying() ) {
    return;
}
VsqFileEx vsq = AppManager.getVsqFile();
if ( !vsq.config.StartMarkerEnabled ) {
    return;
}

AppManager.setCurrentClock( vsq.config.StartMarker );
AppManager.setPlaying( true, this );
        }

        void menuHiddenPrintPoToCSV_Click( Object sender, BEventArgs e )
        {

Vector<String> keys = new Vector<String>();
String[] langs = Messaging.getRegisteredLanguage();
for ( String lang : langs ) {
    for ( String key : Messaging.getKeys( lang ) ) {
        if ( !keys.contains( key ) ) {
            keys.add( key );
        }
    }
}

Collections.sort( keys );
String dir = PortUtil.getApplicationStartupPath();
String fname = fsys.combine( dir, "cadencii_trans.csv" );
String old_lang = Messaging.getLanguage();
BufferedWriter br = null;
try {
    br = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( fname ), "UTF-8" ) );
    String line = "\"en\"";
    for ( String lang : langs ) {
        line += ",\"" + lang + "\"";
    }
    br.write( line );
    br.newLine();
    for ( String key : keys ) {
        line = "\"" + key + "\"";
        for ( String lang : langs ) {
            Messaging.setLanguage( lang );
            line += ",\"" + Messaging.getMessage( key ) + "\"";
        }
        br.write( line );
        br.newLine();
    }
} catch ( Exception ex ) {
    serr.println( "FormMain#menuHiddenPrintPoToCSV_Click; ex=" + ex );
} finally {
    if ( br != null ) {
        try {
            br.close();
        } catch ( Exception ex2 ) {
        }
    }
}
Messaging.setLanguage( old_lang );
        }

        public void menuHiddenMoveLeft_Click( Object sender, BEventArgs e )
        {
QuantizeMode mode = AppManager.editorConfig.getPositionQuantize();
boolean triplet = AppManager.editorConfig.isPositionQuantizeTriplet();
int delta = -QuantizeModeUtil.getQuantizeClock( mode, triplet );
moveUpDownLeftRight( 0, delta );
        }

        public void menuHiddenMoveRight_Click( Object sender, BEventArgs e )
        {
QuantizeMode mode = AppManager.editorConfig.getPositionQuantize();
boolean triplet = AppManager.editorConfig.isPositionQuantizeTriplet();
int delta = QuantizeModeUtil.getQuantizeClock( mode, triplet );
moveUpDownLeftRight( 0, delta );
        }

        public void menuHiddenSelectBackward_Click( Object sender, BEventArgs e )
        {
selectBackward();
        }

        public void menuHiddenSelectForward_Click( Object sender, BEventArgs e )
        {
selectForward();
        }

        public void menuHiddenEditFlipToolPointerPencil_Click( Object sender, BEventArgs e )
        {
if ( AppManager.getSelectedTool() == EditTool.ARROW ) {
    AppManager.setSelectedTool( EditTool.PENCIL );
} else {
    AppManager.setSelectedTool( EditTool.ARROW );
}
refreshScreen();
        }

        public void menuHiddenEditFlipToolPointerEraser_Click( Object sender, BEventArgs e )
        {
if ( AppManager.getSelectedTool() == EditTool.ARROW ) {
    AppManager.setSelectedTool( EditTool.ERASER );
} else {
    AppManager.setSelectedTool( EditTool.ARROW );
}
refreshScreen();
        }

        public void menuHiddenEditLyric_Click( Object sender, BEventArgs e )
        {
boolean input_enabled = AppManager.mInputTextBox.isVisible();
if ( !input_enabled && AppManager.itemSelection.getEventCount() > 0 ) {
    VsqEvent original = AppManager.itemSelection.getLastEvent().original;
    int clock = original.Clock;
    int note = original.ID.Note;
    Point pos = new Point( AppManager.xCoordFromClocks( clock ), AppManager.yCoordFromNote( note ) );
    if ( !AppManager.editorConfig.KeepLyricInputMode ) {
        mLastSymbolEditMode = false;
    }
    showInputTextBox( original.ID.LyricHandle.L0.Phrase,
                      original.ID.LyricHandle.L0.getPhoneticSymbol(),
                      pos, mLastSymbolEditMode );
    refreshScreen();
} else if ( input_enabled ) {
    if ( AppManager.mInputTextBox.isPhoneticSymbolEditMode() ) {
        flipInputTextBoxMode();
    }
}
        }

        //BOOKMARK: cMenuTrackTab
        public void cMenuTrackTabCopy_Click( Object sender, BEventArgs e )
        {
copyTrackCore();
        }

        public void cMenuTrackTabChangeName_Click( Object sender, BEventArgs e )
        {
changeTrackNameCore();
        }

        public void cMenuTrackTabDelete_Click( Object sender, BEventArgs e )
        {
deleteTrackCore();
        }

        public void cMenuTrackTabAdd_Click( Object sender, BEventArgs e )
        {
addTrackCore();
        }

        public void cMenuTrackTab_Opening( Object sender, BCancelEventArgs e )
        {
updateTrackMenuStatus();
        }

        public void updateTrackMenuStatus()
        {
VsqFileEx vsq = AppManager.getVsqFile();
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
int tracks = vsq.Track.size();
cMenuTrackTabDelete.setEnabled( tracks >= 3 );
menuTrackDelete.setEnabled( tracks >= 3 );
cMenuTrackTabAdd.setEnabled( tracks <= 16 );
menuTrackAdd.setEnabled( tracks <= 16 );
cMenuTrackTabCopy.setEnabled( tracks <= 16 );
menuTrackCopy.setEnabled( tracks <= 16 );

boolean on = vsq_track.isTrackOn();
cMenuTrackTabTrackOn.setSelected( on );
menuTrackOn.setSelected( on );

if ( tracks > 2 ) {
    cMenuTrackTabOverlay.setEnabled( true );
    menuTrackOverlay.setEnabled( true );
    cMenuTrackTabOverlay.setSelected( AppManager.isOverlay() );
    menuTrackOverlay.setSelected( AppManager.isOverlay() );
} else {
    cMenuTrackTabOverlay.setEnabled( false );
    menuTrackOverlay.setEnabled( false );
    cMenuTrackTabOverlay.setSelected( false );
    menuTrackOverlay.setSelected( false );
}
cMenuTrackTabRenderCurrent.setEnabled( !AppManager.isPlaying() );
menuTrackRenderCurrent.setEnabled( !AppManager.isPlaying() );
cMenuTrackTabRenderAll.setEnabled( !AppManager.isPlaying() );
menuTrackRenderAll.setEnabled( !AppManager.isPlaying() );
cMenuTrackTabRendererVOCALOID1.setSelected( false );
menuTrackRendererVOCALOID1.setSelected( false );
cMenuTrackTabRendererVOCALOID2.setSelected( false );
menuTrackRendererVOCALOID2.setSelected( false );
cMenuTrackTabRendererUtau.setSelected( false );
menuTrackRendererUtau.setSelected( false );
cMenuTrackTabRendererStraight.setSelected( false );
menuTrackRendererVCNT.setSelected( false );
cMenuTrackTabRendererAquesTone.setSelected( false );
menuTrackRendererAquesTone.setSelected( false );

RendererKind kind = VsqFileEx.getTrackRendererKind( vsq_track );
if ( kind == RendererKind.VOCALOID1 ) {
    cMenuTrackTabRendererVOCALOID1.setSelected( true );
    menuTrackRendererVOCALOID1.setSelected( true );
} else if ( kind == RendererKind.VOCALOID2 ) {
    cMenuTrackTabRendererVOCALOID2.setSelected( true );
    menuTrackRendererVOCALOID2.setSelected( true );
} else if ( kind == RendererKind.UTAU ) {
    cMenuTrackTabRendererUtau.setSelected( true );
    menuTrackRendererUtau.setSelected( true );
} else if ( kind == RendererKind.VCNT ) {
    cMenuTrackTabRendererStraight.setSelected( true );
    menuTrackRendererVCNT.setSelected( true );
} else if ( kind == RendererKind.AQUES_TONE ) {
    cMenuTrackTabRendererAquesTone.setSelected( true );
    menuTrackRendererAquesTone.setSelected( true );
}
        }

        public void cMenuTrackTabOverlay_Click( Object sender, BEventArgs e )
        {
AppManager.setOverlay( !AppManager.isOverlay() );
refreshScreen();
        }

        public void cMenuTrackTabRenderCurrent_Click( Object sender, BEventArgs e )
        {
Vector<Integer> tracks = new Vector<Integer>();
tracks.add( AppManager.getSelected() );
AppManager.patchWorkToFreeze( this, tracks );
        }

        public void cMenuTrackTabRenderer_DropDownOpening( Object sender, BEventArgs e )
        {
updateRendererMenu();
        }

        public void cMenuPositionIndicatorStartMarker_Click( Object sender, BEventArgs e )
        {
int clock = mPositionIndicatorPopupShownClock;
VsqFileEx vsq = AppManager.getVsqFile();
vsq.config.StartMarkerEnabled = true;
vsq.config.StartMarker = clock;
if ( vsq.config.EndMarker < clock ) {
    vsq.config.EndMarker = clock;
}
menuVisualStartMarker.setSelected( true );
setEdited( true );
picturePositionIndicator.repaint();
        }

        public void cMenuPositionIndicatorEndMarker_Click( Object sender, BEventArgs e )
        {
int clock = mPositionIndicatorPopupShownClock;
VsqFileEx vsq = AppManager.getVsqFile();
vsq.config.EndMarkerEnabled = true;
vsq.config.EndMarker = clock;
if ( vsq.config.StartMarker > clock ) {
    vsq.config.StartMarker = clock;
}
menuVisualEndMarker.setSelected( true );
setEdited( true );
picturePositionIndicator.repaint();
        }

        //BOOKMARK: cMenuTrackSelector
        public void cMenuTrackSelector_Opening( Object sender, BCancelEventArgs e )
        {
updateCopyAndPasteButtonStatus();

// 選択ツールの状態に合わせて表示を更新
cMenuTrackSelectorPointer.setSelected( false );
cMenuTrackSelectorPencil.setSelected( false );
cMenuTrackSelectorLine.setSelected( false );
cMenuTrackSelectorEraser.setSelected( false );
EditTool tool = AppManager.getSelectedTool();
if ( tool == EditTool.ARROW ) {
    cMenuTrackSelectorPointer.setSelected( true );
} else if ( tool == EditTool.PENCIL ) {
    cMenuTrackSelectorPencil.setSelected( true );
} else if ( tool == EditTool.LINE ) {
    cMenuTrackSelectorLine.setSelected( true );
} else if ( tool == EditTool.ERASER ) {
    cMenuTrackSelectorEraser.setSelected( true );
}
cMenuTrackSelectorCurve.setSelected( AppManager.isCurveMode() );
        }

        public void cMenuTrackSelectorPointer_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ARROW );
refreshScreen();
        }

        public void cMenuTrackSelectorPencil_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.PENCIL );
refreshScreen();
        }

        public void cMenuTrackSelectorLine_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.LINE );
        }

        public void cMenuTrackSelectorEraser_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ERASER );
        }

        public void cMenuTrackSelectorCurve_Click( Object sender, BEventArgs e )
        {
AppManager.setCurveMode( !AppManager.isCurveMode() );
        }

        public void cMenuTrackSelectorSelectAll_Click( Object sender, BEventArgs e )
        {
selectAllEvent();
        }

        public void cMenuTrackSelectorCut_Click( Object sender, BEventArgs e )
        {
cutEvent();
        }

        public void cMenuTrackSelectorCopy_Click( Object sender, BEventArgs e )
        {
copyEvent();
        }

        public void cMenuTrackSelectorDelete_Click( Object sender, BEventArgs e )
        {
deleteEvent();
        }

        public void cMenuTrackSelectorDeleteBezier_Click( Object sender, BEventArgs e )
        {
for ( Iterator<SelectedBezierPoint> itr = AppManager.itemSelection.getBezierIterator(); itr.hasNext(); ) {
    SelectedBezierPoint sbp = itr.next();
    int chain_id = sbp.chainID;
    int point_id = sbp.pointID;
    VsqFileEx vsq = AppManager.getVsqFile();
    int selected = AppManager.getSelected();
    BezierChain chain = (BezierChain)vsq.AttachedCurves.get( selected - 1 ).getBezierChain( trackSelector.getSelectedCurve(), chain_id ).clone();
    int index = -1;
    for ( int i = 0; i < chain.points.size(); i++ ) {
        if ( chain.points.get( i ).getID() == point_id ) {
            index = i;
            break;
        }
    }
    if ( index >= 0 ) {
        chain.points.removeElementAt( index );
        if ( chain.points.size() == 0 ) {
            CadenciiCommand run = VsqFileEx.generateCommandDeleteBezierChain( selected,
                                                                              trackSelector.getSelectedCurve(),
                                                                              chain_id,
                                                                              AppManager.editorConfig.getControlCurveResolutionValue() );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
        } else {
            CadenciiCommand run = VsqFileEx.generateCommandReplaceBezierChain( selected,
                                                                               trackSelector.getSelectedCurve(),
                                                                               chain_id,
                                                                               chain,
                                                                               AppManager.editorConfig.getControlCurveResolutionValue() );
            AppManager.editHistory.register( vsq.executeCommand( run ) );
        }
        setEdited( true );
        refreshScreen();
        break;
    }
}
        }

        public void cMenuTrackSelectorPaste_Click( Object sender, BEventArgs e )
        {
pasteEvent();
        }

        public void cMenuTrackSelectorUndo_Click( Object sender, BEventArgs e )
        {
undo();
refreshScreen();
        }

        public void cMenuTrackSelectorRedo_Click( Object sender, BEventArgs e )
        {
redo();
refreshScreen();
        }

        public void buttonVZoom_Click( Object sender, BEventArgs e )
        {
zoomY( 1 );
        }

        public void buttonVMooz_Click( Object sender, BEventArgs e )
        {
zoomY( -1 );
        }




        //BOOKMARK: stripBtn
        public void stripBtnGrid_Click( Object sender, BEventArgs e )
        {
boolean new_v = !AppManager.isGridVisible();
stripBtnGrid.setSelected( new_v );
AppManager.setGridVisible( new_v );
        }

        public void stripBtnArrow_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ARROW );
        }

        public void stripBtnPencil_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.PENCIL );
        }

        public void stripBtnLine_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.LINE );
        }

        public void stripBtnEraser_Click( Object sender, BEventArgs e )
        {
AppManager.setSelectedTool( EditTool.ERASER );
        }

        public void stripBtnCurve_Click( Object sender, BEventArgs e )
        {
AppManager.setCurveMode( !AppManager.isCurveMode() );
        }

        public void stripBtnPlay_Click( Object sender, BEventArgs e )
        {
AppManager.setPlaying( !AppManager.isPlaying(), this );
focusPianoRoll();
        }

        public void stripBtnScroll_CheckedChanged( Object sender, BEventArgs e )
        {
boolean pushed = stripBtnScroll.isSelected();
AppManager.mAutoScroll = pushed;
focusPianoRoll();
        }

        public void stripBtnLoop_CheckedChanged( Object sender, BEventArgs e )
        {
boolean pushed = stripBtnLoop.isSelected();
AppManager.setRepeatMode( pushed );
focusPianoRoll();
        }

        public void stripBtnStepSequencer_CheckedChanged( Object sender, BEventArgs e )
        {
// AppManager.mAddingEventがnullかどうかで処理が変わるのでnullにする
AppManager.mAddingEvent = null;
// モードを切り替える
controller.setStepSequencerEnabled( stripBtnStepSequencer.isSelected() );

// MIDIの受信を開始
if ( controller.isStepSequencerEnabled() ) {
    mMidiIn.start();
} else {
    mMidiIn.stop();
}
        }

        public void stripBtnStop_Click( Object sender, BEventArgs e )
        {
AppManager.setPlaying( false, this );
timer.stop();
focusPianoRoll();
        }

        public void stripBtnMoveEnd_Click( Object sender, BEventArgs e )
        {
if ( AppManager.isPlaying() ) {
    AppManager.setPlaying( false, this );
}
AppManager.setCurrentClock( AppManager.getVsqFile().TotalClocks );
ensureCursorVisible();
refreshScreen();
        }

        public void stripBtnMoveTop_Click( Object sender, BEventArgs e )
        {
if ( AppManager.isPlaying() ) {
    AppManager.setPlaying( false, this );
}
AppManager.setCurrentClock( 0 );
ensureCursorVisible();
refreshScreen();
        }

        public void stripBtnRewind_Click( Object sender, BEventArgs e )
        {
rewind();
        }

        public void stripBtnForward_Click( Object sender, BEventArgs e )
        {
forward();
        }

        //BOOKMARK: pictKeyLengthSplitter
        public void pictKeyLengthSplitter_MouseDown( Object sender, BMouseEventArgs e )
        {
mKeyLengthSplitterMouseDowned = true;
mKeyLengthSplitterInitialMouse = PortUtil.getMousePosition();
mKeyLengthInitValue = AppManager.keyWidth;
mKeyLengthTrackSelectorRowsPerColumn = trackSelector.getRowsPerColumn();
mKeyLengthSplitterDistance = splitContainer1.getDividerLocation();
        }

        public void pictKeyLengthSplitter_MouseMove( Object sender, BMouseEventArgs e )
        {
if ( !mKeyLengthSplitterMouseDowned ) {
    return;
}
int dx = PortUtil.getMousePosition().x - mKeyLengthSplitterInitialMouse.x;
int draft = mKeyLengthInitValue + dx;
if ( draft < AppManager.MIN_KEY_WIDTH ) {
    draft = AppManager.MIN_KEY_WIDTH;
} else if ( AppManager.MAX_KEY_WIDTH < draft ) {
    draft = AppManager.MAX_KEY_WIDTH;
}
AppManager.keyWidth = draft;
int current = trackSelector.getRowsPerColumn();
if ( current >= mKeyLengthTrackSelectorRowsPerColumn ) {
    int max_divider_location = splitContainer1.getHeight() - splitContainer1.getDividerSize() - splitContainer1.getPanel2MinSize();
    if ( max_divider_location < mKeyLengthSplitterDistance ) {
        splitContainer1.setDividerLocation( max_divider_location );
    } else {
        splitContainer1.setDividerLocation( mKeyLengthSplitterDistance );
    }
}
updateLayout();
refreshScreen();
        }

        public void pictKeyLengthSplitter_MouseUp( Object sender, BMouseEventArgs e )
        {
mKeyLengthSplitterMouseDowned = false;
        }






        public void handleVibratoPresetSubelementClick( Object sender, BEventArgs e )
        {
if ( sender == null ) {
    return;
}
if ( !(sender instanceof BMenuItem) ) {
    return;
}

// イベントの送信元を特定
BMenuItem item = (BMenuItem)sender;
String text = item.getText();

// メニューの表示文字列から，どの設定値についてのイベントかを探す
VibratoHandle target = null;
int size = AppManager.editorConfig.AutoVibratoCustom.size();
for ( int i = 0; i < size; i++ ) {
    VibratoHandle handle = AppManager.editorConfig.AutoVibratoCustom.get( i );
    if ( text.equals( handle.getCaption() ) ) {
        target = handle;
        break;
    }
}

// ターゲットが特定できなかったらbailout
if ( target == null ) {
    return;
}

// 選択状態のアイテムを取得
Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator();
if ( !itr.hasNext() ) {
    // アイテムがないのでbailout
    return;
}
VsqEvent ev = itr.next().original;
if ( ev.ID.VibratoHandle == null ) {
    return;
}

// 設定値にコピー
VibratoHandle h = ev.ID.VibratoHandle;
target.setStartRate( h.getStartRate() );
target.setStartDepth( h.getStartDepth() );
if ( h.getRateBP() == null ) {
    target.setRateBP( null );
} else {
    target.setRateBP( (VibratoBPList)h.getRateBP().clone() );
}
if ( h.getDepthBP() == null ) {
    target.setDepthBP( null );
} else {
    target.setDepthBP( (VibratoBPList)h.getDepthBP().clone() );
}
        }

        public void timer_Tick( Object sender, BEventArgs e )
        {
if ( AppManager.isGeneratorRunning() ) {
    // !JAVAのときもこれで行けなイカ？
    double play_time = PlaySound.getPosition();
    double now = play_time + AppManager.mDirectPlayShift;
    int clock = (int)AppManager.getVsqFile().getClockFromSec( now );
    if ( mLastClock <= clock ) {
        mLastClock = clock;
        AppManager.setCurrentClock( clock );
        if ( AppManager.mAutoScroll ) {
            ensureCursorVisible();
        }
    }
} else {
    AppManager.setPlaying( false, this );
    int ending_clock = AppManager.getPreviewEndingClock();
    AppManager.setCurrentClock( ending_clock );
    if ( AppManager.mAutoScroll ) {
        ensureCursorVisible();
    }
    refreshScreen( true );
    if ( AppManager.isRepeatMode() ) {
        int dest_clock = 0;
        VsqFileEx vsq = AppManager.getVsqFile();
        if ( vsq.config.StartMarkerEnabled ) {
            dest_clock = vsq.config.StartMarker;
        }
        AppManager.setCurrentClock( dest_clock );
        AppManager.setPlaying( true, this );
    }
}
refreshScreen();
        }

        public void bgWorkScreen_DoWork( Object sender, BDoWorkEventArgs e )
        {
try {
    refreshScreenCore( this, new BEventArgs() );
} catch ( Exception ex ) {
    serr.println( "FormMain#bgWorkScreen_DoWork; ex=" + ex );
    Logger.write( FormMain.class + ".bgWorkScreen_DoWork; ex=" + ex + "\n" );
}
        }


        public void toolStripContainer_TopToolStripPanel_SizeChanged( Object sender, BEventArgs e )
        {
if ( getExtendedState() == BForm.ICONIFIED ) {
    return;
}
Dimension minsize = getWindowMinimumSize();
int wid = getWidth();
int hei = getHeight();
boolean change_size_required = false;
if ( minsize.width > wid ) {
    wid = minsize.width;
    change_size_required = true;
}
if ( minsize.height > hei ) {
    hei = minsize.height;
    change_size_required = true;
}
setMinimumSize( getWindowMinimumSize() );
if ( change_size_required ) {
    setSize( wid, hei );
}
        }

        public void handleRecentFileMenuItem_Click( Object sender, BEventArgs e )
        {
if ( sender instanceof RecentFileMenuItem ) {
    RecentFileMenuItem item = (RecentFileMenuItem)sender;
    String filename = item.getFilePath();
    if( !dirtyCheck() ){
        return;
    }
    openVsqCor( filename );
    clearExistingData();
    AppManager.mMixerWindow.updateStatus();
    clearTempWave();
    updateDrawObjectList();
    refreshScreen();
}
        }

        public void handleRecentFileMenuItem_MouseEnter( Object sender, BEventArgs e )
        {
if ( sender instanceof RecentFileMenuItem ) {
    RecentFileMenuItem item = (RecentFileMenuItem)sender;
    statusLabel.setText( item.getToolTipText() );
}
        }

        public void handleStripPaletteTool_Click( Object sender, BEventArgs e )
        {
String id = "";  //選択されたツールのID

int count = toolStripTool.getComponentCount();
for ( int i = 0; i < count; i++ ) {
    Object item = toolStripTool.getComponentAtIndex( i );
    if( item instanceof PaletteToolButton ){
        PaletteToolButton button = (PaletteToolButton)item;
        String tag = button.getPaletteToolID();
        if( tag != null ){
            button.setSelected( str.compare( id, tag ) );
        }else{
            button.setSelected( false );
        }
    }
}

MenuElement[] sub_cmenu_piano_palette_tool = cMenuPianoPaletteTool.getSubElements();
for ( int i = 0; i < sub_cmenu_piano_palette_tool.length; i++ ) {
    MenuElement item = sub_cmenu_piano_palette_tool[i];
    if ( item instanceof PaletteToolMenuItem ) {
        PaletteToolMenuItem menu = (PaletteToolMenuItem)item;
        String tagged_id = menu.getPaletteToolID();
        menu.setSelected( str.compare( id, tagged_id ) );
    }
}

MenuElement[] sub_cmenu_track_selectro_palette_tool = cMenuTrackSelectorPaletteTool.getSubElements();
for ( int i = 0; i < sub_cmenu_track_selectro_palette_tool.length; i++ ) {
    MenuElement item = sub_cmenu_track_selectro_palette_tool[i];
    if ( item instanceof PaletteToolMenuItem ) {
        PaletteToolMenuItem menu = (PaletteToolMenuItem)item;
        String tagged_id = menu.getPaletteToolID();
        menu.setSelected( str.compare( id, tagged_id ) );
    }
}
        }

        public void handleTrackOn_Click( Object sender, BEventArgs e )
        {
int selected = AppManager.getSelected();
VsqTrack vsq_track = AppManager.getVsqFile().Track.get( selected );
boolean old_status = vsq_track.isTrackOn();
boolean new_status = !old_status;
int last_play_mode = vsq_track.getCommon().LastPlayMode;
CadenciiCommand run = new CadenciiCommand(
    VsqCommand.generateCommandTrackChangePlayMode(
        selected,
        new_status ? last_play_mode : PlayMode.Off,
        last_play_mode ) );
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
menuTrackOn.setSelected( new_status );
cMenuTrackTabTrackOn.setSelected( new_status );
setEdited( true );
refreshScreen();
        }

        public void handleTrackRenderAll_Click( Object sender, BEventArgs e )
        {
Vector<Integer> list = new Vector<Integer>();
int c = AppManager.getVsqFile().Track.size();
for ( int i = 1; i < c; i++ ) {
    if ( AppManager.getRenderRequired( i ) ) {
        list.add( i );
    }
}
if ( list.size() <= 0 ) {
    return;
}
AppManager.patchWorkToFreeze( this, list );
        }

        public void handleEditorConfig_QuantizeModeChanged( Object sender, BEventArgs e )
        {
applyQuantizeMode();
        }

        public void handleFileSave_Click( Object sender, BEventArgs e )
        {
for ( int track = 1; track < AppManager.getVsqFile().Track.size(); track++ ) {
    if ( AppManager.getVsqFile().Track.get( track ).getEventCount() == 0 ) {
        AppManager.showMessageBox(
            PortUtil.formatMessage(
                _( "Invalid note data.\nTrack {0} : {1}\n\n-> Piano roll : Blank sequence." ),
                track,
                AppManager.getVsqFile().Track.get( track ).getName() ),
            _APP_NAME,
            org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
            org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
        return;
    }
}
String file = AppManager.getFileName();
if ( file.equals( "" ) ) {
    String last_file = AppManager.editorConfig.getLastUsedPathOut( "xvsq" );
    if ( !last_file.equals( "" ) ) {
        String dir = PortUtil.getDirectoryName( last_file );
        saveXmlVsqDialog.setSelectedFile( dir );
    }
    int dr = AppManager.showModalDialog( saveXmlVsqDialog, false, this );
    if ( dr == BFileChooser.APPROVE_OPTION ) {
        file = saveXmlVsqDialog.getSelectedFile();
        AppManager.editorConfig.setLastUsedPathOut( file, ".xvsq" );
    }
}
if ( file != "" ) {
    AppManager.saveTo( file );
    updateRecentFileMenu();
    setEdited( false );
}
        }

        public void handleFileOpen_Click( Object sender, BEventArgs e )
        {
if ( !dirtyCheck() ) {
    return;
}
String dir = AppManager.editorConfig.getLastUsedPathIn( "xvsq" );
openXmlVsqDialog.setSelectedFile( dir );
int dialog_result = AppManager.showModalDialog( openXmlVsqDialog, true, this );
if ( dialog_result != BFileChooser.APPROVE_OPTION ) {
    return;
}
if ( AppManager.isPlaying() ) {
    AppManager.setPlaying( false, this );
}
String file = openXmlVsqDialog.getSelectedFile();
AppManager.editorConfig.setLastUsedPathIn( file, ".xvsq" );
if( openVsqCor( file ) ){
    AppManager.showMessageBox(
        _( "Invalid XVSQ file" ),
        _( "Error" ),
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}
clearExistingData();

setEdited( false );
AppManager.mMixerWindow.updateStatus();
clearTempWave();
updateDrawObjectList();
refreshScreen();
        }

        public void handleStripButton_Enter( Object sender, BEventArgs e )
        {
focusPianoRoll();
        }

        public void handleFileNew_Click( Object sender, BEventArgs e )
        {
if ( !dirtyCheck() ) {
    return;
}
AppManager.setSelected( 1 );
VsqFileEx vsq = new VsqFileEx( AppManager.editorConfig.DefaultSingerName, 1, 4, 4, 500000 );

RendererKind kind = AppManager.editorConfig.DefaultSynthesizer;
String renderer = AppManager.getVersionStringFromRendererKind( kind );
Vector<VsqID> singers = AppManager.getSingerListFromRendererKind( kind );
vsq.Track.get( 1 ).changeRenderer( renderer, singers );

AppManager.setVsqFile( vsq );
clearExistingData();
for ( int i = 0; i < AppManager.mLastRenderedStatus.length; i++ ){
    AppManager.mLastRenderedStatus[i] = null;
}
setEdited( false );
AppManager.mMixerWindow.updateStatus();
clearTempWave();

// キャッシュディレクトリのパスを、デフォルトに戻す
AppManager.setTempWaveDir( fsys.combine( AppManager.getCadenciiTempDir(), AppManager.getID() ) );

updateDrawObjectList();
refreshScreen();
        }

        public void handleEditPaste_Click( Object sender, BEventArgs e )
        {
pasteEvent();
        }

        public void handleEditCopy_Click( Object sender, BEventArgs e )
        {
copyEvent();
        }

        public void handleEditCut_Click( Object sender, BEventArgs e )
        {
cutEvent();
        }

        public void handlePositionQuantize( Object sender, BEventArgs e )
        {
QuantizeMode qm = AppManager.editorConfig.getPositionQuantize();
if ( sender == cMenuPianoQuantize04 ||
 sender == menuSettingPositionQuantize04 ) {
    qm = QuantizeMode.p4;
} else if ( sender == cMenuPianoQuantize08 ||
 sender == menuSettingPositionQuantize08 ) {
    qm = QuantizeMode.p8;
} else if ( sender == cMenuPianoQuantize16 ||
 sender == menuSettingPositionQuantize16 ) {
    qm = QuantizeMode.p16;
} else if ( sender == cMenuPianoQuantize32 ||
 sender == menuSettingPositionQuantize32 ) {
    qm = QuantizeMode.p32;
} else if ( sender == cMenuPianoQuantize64 ||
 sender == menuSettingPositionQuantize64 ) {
    qm = QuantizeMode.p64;
} else if ( sender == cMenuPianoQuantize128 ||
 sender == menuSettingPositionQuantize128 ) {
    qm = QuantizeMode.p128;
} else if ( sender == cMenuPianoQuantizeOff ||
 sender == menuSettingPositionQuantizeOff ) {
    qm = QuantizeMode.off;
}
AppManager.editorConfig.setPositionQuantize( qm );
AppManager.editorConfig.setLengthQuantize( qm );
refreshScreen();
        }

        public void handlePositionQuantizeTriplet_Click( Object sender, BEventArgs e )
        {
boolean triplet = !AppManager.editorConfig.isPositionQuantizeTriplet();
AppManager.editorConfig.setPositionQuantizeTriplet( triplet );
AppManager.editorConfig.setLengthQuantizeTriplet( triplet );
refreshScreen();
        }

        public void handleStartMarker_Click( Object sender, BEventArgs e )
        {
VsqFileEx vsq = AppManager.getVsqFile();
vsq.config.StartMarkerEnabled = !vsq.config.StartMarkerEnabled;
menuVisualStartMarker.setSelected( vsq.config.StartMarkerEnabled );
setEdited( true );
focusPianoRoll();
refreshScreen();
        }

        public void handleEndMarker_Click( Object sender, BEventArgs e )
        {
VsqFileEx vsq = AppManager.getVsqFile();
vsq.config.EndMarkerEnabled = !vsq.config.EndMarkerEnabled;
menuVisualEndMarker.setSelected( vsq.config.EndMarkerEnabled );
setEdited( true );
focusPianoRoll();
refreshScreen();
        }

        /// <summary>
        /// メニューの説明をステータスバーに表示するための共通のイベントハンドラ
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void handleMenuMouseEnter( Object sender, BEventArgs e )
        {
if ( sender == null ) {
    return;
}

boolean notfound = false;
String text = "";
if ( sender == menuEditUndo ) {
    text = _( "Undo." );
} else if ( sender == menuEditRedo ) {
    text = _( "Redo." );
} else if ( sender == menuEditCut ) {
    text = _( "Cut selected items." );
} else if ( sender == menuEditCopy ) {
    text = _( "Copy selected items." );
} else if ( sender == menuEditPaste ) {
    text = _( "Paste copied items to current song position." );
} else if ( sender == menuEditDelete ) {
    text = _( "Delete selected items." );
} else if ( sender == menuEditAutoNormalizeMode ) {
    text = _( "Avoid automaticaly polyphonic editing." );
} else if ( sender == menuEditSelectAll ) {
    text = _( "Select all items and control curves of current track." );
} else if ( sender == menuEditSelectAllEvents ) {
    text = _( "Select all items of current track." );
} else if ( sender == menuVisualControlTrack ) {
    text = _( "Show/Hide control curves." );
} else if ( sender == menuVisualEndMarker ) {
    text = _( "Enable/Disable end marker." );
} else if ( sender == menuVisualGridline ) {
    text = _( "Show/Hide grid line." );
} else if ( sender == menuVisualIconPalette ) {
    text = _( "Show/Hide icon palette" );
} else if ( sender == menuVisualLyrics ) {
    text = _( "Show/Hide lyrics." );
} else if ( sender == menuVisualMixer ) {
    text = _( "Show/Hide mixer window." );
} else if ( sender == menuVisualNoteProperty ) {
    text = _( "Show/Hide expression lines." );
} else if ( sender == menuVisualOverview ) {
    text = _( "Show/Hide navigation view" );
} else if ( sender == menuVisualPitchLine ) {
    text = _( "Show/Hide pitch bend lines." );
} else if ( sender == menuVisualPluginUi ) {
    text = _( "Open VSTi plugin window" );
} else if ( sender == menuVisualProperty ) {
    text = _( "Show/Hide property window." );
} else if ( sender == menuVisualStartMarker ) {
    text = _( "Enable/Disable start marker." );
} else if ( sender == menuVisualWaveform ) {
    text = _( "Show/Hide waveform." );
} else if ( sender == menuFileNew ) {
    text = _( "Create new project." );
} else if ( sender == menuFileOpen ) {
    text = _( "Open Cadencii project." );
} else if ( sender == menuFileSave ) {
    text = _( "Save current project." );
} else if ( sender == menuFileSaveNamed ) {
    text = _( "Save current project with new name." );
} else if ( sender == menuFileOpenVsq ) {
    text = _( "Open VSQ / VOCALOID MIDI and create new project." );
} else if ( sender == menuFileOpenUst ) {
    text = _( "Open UTAU project and create new project." );
} else if ( sender == menuFileImport ) {
    text = _( "Import." );
} else if ( sender == menuFileImportMidi ) {
    text = _( "Import Standard MIDI." );
} else if ( sender == menuFileImportUst ) {
    text = _( "Import UTAU project" );
} else if ( sender == menuFileImportVsq ) {
    text = _( "Import VSQ / VOCALOID MIDI." );
} else if ( sender == menuFileExport ) {
    text = _( "Export." );
} else if ( sender == menuFileExportParaWave ) {
    text = _( "Export all tracks to serial numbered WAVEs" );
} else if ( sender == menuFileExportWave ) {
    text = _( "Export to WAVE file." );
} else if ( sender == menuFileExportMusicXml ) {
    text = _( "Export current track as Music XML" );
} else if ( sender == menuFileExportMidi ) {
    text = _( "Export to Standard MIDI." );
} else if ( sender == menuFileExportUst ) {
    text = _( "Export current track as UTAU project file" );
} else if ( sender == menuFileExportVsq ) {
    text = _( "Export to VSQ" );
} else if ( sender == menuFileExportVxt ) {
    text = _( "Export current track as Meta-text for vConnect" );
} else if ( sender == menuFileRecent ) {
    text = _( "Recent projects." );
} else if ( sender == menuFileQuit ) {
    text = _( "Close this window." );
} else if ( sender == menuJobConnect ) {
    text = _( "Lengthen note end to neighboring note." );
} else if ( sender == menuJobLyric ) {
    text = _( "Import lyric." );
} else if ( sender == menuJobNormalize ) {
    text = _( "Correct overlapped item." );
} else if ( sender == menuJobInsertBar ) {
    text = _( "Insert bar." );
} else if ( sender == menuJobDeleteBar ) {
    text = _( "Delete bar." );
} else if ( sender == menuJobRandomize ) {
    text = _( "Randomize items." );
} else if ( sender == menuLyricExpressionProperty ) {
    text = _( "Edit portamento/accent/decay of selected item" );
} else if ( sender == menuLyricVibratoProperty ) {
    text = _( "Edit vibrato length and type of selected item" );
} else if ( sender == menuLyricPhonemeTransformation ) {
    text = _( "Translate all phrase into phonetic symbol" );
} else if ( sender == menuLyricDictionary ){
    text = _( "Open configuration dialog for phonetic symnol dictionaries" );
} else if ( sender == menuLyricCopyVibratoToPreset ) {
    text = _( "Copy vibrato config of selected item into vibrato preset" );
} else if ( sender == menuScriptUpdate ){
    text = _( "Read and compile all scripts and update the list of them" );
} else if ( sender == menuSettingPreference ){
    text = _( "Open configuration dialog for editor configs" );
} else if ( sender == menuSettingPositionQuantize ) {
    text = _( "Change quantize resolution" );
} else if ( sender == menuSettingGameControler ){
    text = _( "Connect/Remove/Configure game controler" );
} else if ( sender == menuSettingPaletteTool ) {
    text = _( "Configuration of palette tool" );
} else if ( sender == menuSettingShortcut ) {
    text = _( "Open configuration dialog for shortcut key" );
} else if ( sender == menuSettingVibratoPreset ) {
    text = _( "Open configuration dialog for vibrato preset" );
} else if ( sender == menuSettingDefaultSingerStyle ) {
    text = _( "Edit default singer style" );
} else if ( sender == menuSettingSequence ) {
    text = _( "Configuration of this sequence." );
} else if ( sender == menuTrackAdd ) {
    text = _( "Add new track." );
} else if ( sender == menuTrackBgm ) {
    text = _( "Add/Remove/Edit background music" );
} else if ( sender == menuTrackOn ) {
    text = _( "Enable current track." );
} else if ( sender == menuTrackCopy ) {
    text = _( "Copy current track." );
} else if ( sender == menuTrackChangeName ) {
    text = _( "Change track name." );
} else if ( sender == menuTrackDelete ) {
    text = _( "Delete current track." );
} else if ( sender == menuTrackRenderCurrent ) {
    text = _( "Render current track." );
} else if ( sender == menuTrackRenderAll ) {
    text = _( "Render all tracks." );
} else if ( sender == menuTrackOverlay ) {
    text = _( "Show background items." );
} else if ( sender == menuTrackRenderer ) {
    text = _( "Select voice synthesis engine." );
} else if ( sender == menuTrackRendererAquesTone ) {
    text = _( "AquesTone" );
} else if ( sender == menuTrackRendererUtau ) {
    text = _( "UTAU" );
} else if ( sender == menuTrackRendererVCNT ) {
    text = _( "vConnect-STAND" );
} else if ( sender == menuTrackRendererVOCALOID1 ) {
    text = _( "VOCALOID1" );
} else if ( sender == menuTrackRendererVOCALOID2 ) {
    text = _( "VOCALOID2" );
} else if ( sender == menuFileRecentClear ) {
    text = _( "Clear menu items" );
} else {
    notfound = true;
}

statusLabel.setText( text );
        }

        public void handleSpaceKeyDown( Object sender, BKeyEventArgs e )
        {
if ( (e.KeyValue & KeyEvent.VK_SPACE) == KeyEvent.VK_SPACE ) {
    mSpacekeyDowned = true;
}
        }

        public void handleSpaceKeyUp( Object sender, BKeyEventArgs e )
        {
if ( (e.KeyValue & KeyEvent.VK_SPACE) == KeyEvent.VK_SPACE ) {
    mSpacekeyDowned = false;
}
        }

        public void handleChangeRenderer( Object sender, BEventArgs e )
        {
RendererKind kind = RendererKind.NULL;
int resampler_index = -1;
if ( sender == cMenuTrackTabRendererAquesTone || sender == menuTrackRendererAquesTone ) {
    kind = RendererKind.AQUES_TONE;
} else if ( sender == cMenuTrackTabRendererStraight || sender == menuTrackRendererVCNT ) {
    kind = RendererKind.VCNT;
} else if ( sender == cMenuTrackTabRendererVOCALOID1 || sender == menuTrackRendererVOCALOID1 ) {
    kind = RendererKind.VOCALOID1;
} else if ( sender == cMenuTrackTabRendererVOCALOID2 || sender == menuTrackRendererVOCALOID2 ) {
    kind = RendererKind.VOCALOID2;
} else {
    // イベント送信元のアイテムが，cMenuTrackTabRendererUtauまたは
    // menuTrackRendererUTAUのサブアイテムかどうかをチェック
    if ( sender instanceof BMenuItem ) {
        BMenuItem item = (BMenuItem)sender;
        MenuElement[] subc0 = cMenuTrackTabRendererUtau.getSubElements();
        if( subc0.length > 0 ){
            MenuElement[] subc1 = subc0[0].getSubElements();
            for ( int i = 0; i < subc1.length; i++ ){
                MenuElement c = subc1[i];
                if( c instanceof BMenuItem ){
                    BMenuItem b = (BMenuItem)c;
                    if( b == item ){
                        resampler_index = i;
                        break;
                    }
                }
            }
        }
        if( resampler_index < 0 ){
            MenuElement[] subm0 = menuTrackRendererUtau.getSubElements();
            if( subm0.length > 0 ){
                MenuElement[] subm1 = subm0[0].getSubElements();
                for( int i = 0; i < subm1.length; i++ ){
                    MenuElement c = subm1[i];
                    if( c instanceof BMenuItem ){
                        BMenuItem b = (BMenuItem)c;
                        if( b == item ){
                            resampler_index = i;
                            break;
                        }
                    }
                }
            }
        }
    }
    if ( resampler_index < 0 ) {
        // 検出できないのでbailout
        return;
    }

    // 検出できた
    // このばあいは確実にUTAU
    kind = RendererKind.UTAU;
}
VsqFileEx vsq = AppManager.getVsqFile();
int selected = AppManager.getSelected();
VsqTrack vsq_track = vsq.Track.get( selected );
RendererKind old = VsqFileEx.getTrackRendererKind( vsq_track );
int old_resampler_index = VsqFileEx.getTrackResamplerUsed( vsq_track );
boolean changed = (old != kind);
if ( !changed && kind == RendererKind.UTAU ) {
    changed = (old_resampler_index != resampler_index);
}
if ( changed ) {
    VsqTrack item = (VsqTrack)vsq_track.clone();
    Vector<VsqID> singers = AppManager.getSingerListFromRendererKind( kind );
    String renderer = AppManager.getVersionStringFromRendererKind( kind );
    if ( singers == null ) {
        serr.println( "FormMain#changeRendererCor; singers instanceof null" );
        return;
    }

    item.changeRenderer( renderer, singers );
    VsqFileEx.setTrackRendererKind( item, kind );
    if ( kind == RendererKind.UTAU ) {
        VsqFileEx.setTrackResamplerUsed( item, resampler_index );
    }
    CadenciiCommand run = VsqFileEx.generateCommandTrackReplace( selected,
                                                                 item,
                                                                 vsq.AttachedCurves.get( selected - 1 ) );
    AppManager.editHistory.register( vsq.executeCommand( run ) );
    cMenuTrackTabRendererVOCALOID1.setSelected( kind == RendererKind.VOCALOID1 );
    cMenuTrackTabRendererVOCALOID2.setSelected( kind == RendererKind.VOCALOID2 );
    cMenuTrackTabRendererUtau.setSelected( kind == RendererKind.UTAU );
    cMenuTrackTabRendererStraight.setSelected( kind == RendererKind.VCNT );
    menuTrackRendererVOCALOID1.setSelected( kind == RendererKind.VOCALOID1 );
    menuTrackRendererVOCALOID2.setSelected( kind == RendererKind.VOCALOID2 );
    menuTrackRendererUtau.setSelected( kind == RendererKind.UTAU );
    menuTrackRendererVCNT.setSelected( kind == RendererKind.VCNT );
    for( int i = 0; i < cMenuTrackTabRendererUtau.getComponentCount(); i++ ){
        Component c = cMenuTrackTabRendererUtau.getComponent( i );
        if( c instanceof BMenuItem ){
            ((BMenuItem)c).setSelected( (i == resampler_index) );
        }
    }
    for( int i = 0; i < menuTrackRendererUtau.getComponentCount(); i++ ){
        Component c = menuTrackRendererUtau.getComponent( i );
        if( c instanceof BMenuItem ){
            ((BMenuItem)c).setSelected( (i == resampler_index) );
        }
    }
    setEdited( true );
    refreshScreen();
}
        }

        public void handleBgmOffsetSeconds_Click( Object sender, BEventArgs e )
        {
if ( !(sender instanceof BgmMenuItem) ) {
    return;
}
BgmMenuItem menu = (BgmMenuItem)sender;
int index = menu.getBgmIndex();
InputBox ib = null;
try {
    ib = new InputBox( _( "Input Offset Seconds" ) );
    ib.setLocation( getFormPreferedLocation( ib ) );
    ib.setResult( AppManager.getBgm( index ).readOffsetSeconds + "" );
    BDialogResult dr = AppManager.showModalDialog( ib, this );
    if ( dr != BDialogResult.OK ) {
        return;
    }
    Vector<BgmFile> list = new Vector<BgmFile>();
    int count = AppManager.getBgmCount();
    BgmFile item = null;
    for ( int i = 0; i < count; i++ ) {
        if ( i == index ) {
            item = (BgmFile)AppManager.getBgm( i ).clone();
            list.add( item );
        } else {
            list.add( AppManager.getBgm( i ) );
        }
    }
    double draft;
    try {
        draft = str.tof( ib.getResult() );
        item.readOffsetSeconds = draft;
        menu.setToolTipText( draft + " " + _( "seconds" ) );
    } catch ( Exception ex3 ) {
        Logger.write( FormMain.class + ".handleBgmOffsetSeconds_Click; ex=" + ex3 + "\n" );
    }
    CadenciiCommand run = VsqFileEx.generateCommandBgmUpdate( list );
    AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
    setEdited( true );
} catch ( Exception ex ) {
    Logger.write( FormMain.class + ".handleBgmOffsetSeconds_Click; ex=" + ex + "\n" );
} finally {
    if ( ib != null ) {
        try {
        } catch ( Exception ex2 ) {
            Logger.write( FormMain.class + ".handleBgmOffsetSeconds_Click; ex=" + ex2 + "\n" );
        }
    }
}
        }

        public void handleBgmStartAfterPremeasure_CheckedChanged( Object sender, BEventArgs e )
        {
if ( !(sender instanceof BgmMenuItem) ) {
    return;
}
BgmMenuItem menu = (BgmMenuItem)sender;
int index = menu.getBgmIndex();
Vector<BgmFile> list = new Vector<BgmFile>();
int count = AppManager.getBgmCount();
for ( int i = 0; i < count; i++ ) {
    if ( i == index ) {
        BgmFile item = (BgmFile)AppManager.getBgm( i ).clone();
        item.startAfterPremeasure = menu.isSelected();
        list.add( item );
    } else {
        list.add( AppManager.getBgm( i ) );
    }
}
CadenciiCommand run = VsqFileEx.generateCommandBgmUpdate( list );
AppManager.editHistory.register( AppManager.getVsqFile().executeCommand( run ) );
setEdited( true );
        }

        public void handleBgmAdd_Click( Object sender, BEventArgs e )
        {
String dir = AppManager.editorConfig.getLastUsedPathIn( "wav" );
openWaveDialog.setSelectedFile( dir );
int ret = AppManager.showModalDialog( openWaveDialog, true, this );
if ( ret != BFileChooser.APPROVE_OPTION ) {
    return;
}

String file = openWaveDialog.getSelectedFile();
AppManager.editorConfig.setLastUsedPathIn( file, ".wav" );

// 既に開かれていたらキャンセル
int count = AppManager.getBgmCount();
boolean found = false;
for ( int i = 0; i < count; i++ ) {
    BgmFile item = AppManager.getBgm( i );
    if ( str.compare( file, item.file ) ) {
        found = true;
        break;
    }
}
if ( found ) {
    AppManager.showMessageBox(
        PortUtil.formatMessage( _( "file '{0}' instanceof already registered as BGM." ), file ),
        _( "Error" ),
        org.kbinani.windows.forms.Utility.MSGBOX_DEFAULT_OPTION,
        org.kbinani.windows.forms.Utility.MSGBOX_WARNING_MESSAGE );
    return;
}

// 登録
AppManager.addBgm( file );
setEdited( true );
updateBgmMenuState();
        }

        public void handleBgmRemove_Click( Object sender, BEventArgs e )
        {
if ( !(sender instanceof BgmMenuItem) ) {
    return;
}
BgmMenuItem parent = (BgmMenuItem)sender;
int index = parent.getBgmIndex();
BgmFile bgm = AppManager.getBgm( index );
if ( AppManager.showMessageBox( PortUtil.formatMessage( _( "remove '{0}'?" ), bgm.file ),
                      "Cadencii",
                      org.kbinani.windows.forms.Utility.MSGBOX_YES_NO_OPTION,
                      org.kbinani.windows.forms.Utility.MSGBOX_QUESTION_MESSAGE ) != BDialogResult.YES ) {
    return;
}
AppManager.removeBgm( index );
setEdited( true );
updateBgmMenuState();
        }

        public void handleSettingPaletteTool( Object sender, BEventArgs e )
        {
        }



        public void mMidiIn_MidiReceived( Object sender, javax.sound.midi.MidiMessage message )
        {
byte[] data = message.getMessage();
if ( data.length <= 2 ) {
    return;
}
if ( AppManager.isPlaying() ) {
    return;
}
if ( false == controller.isStepSequencerEnabled() ) {
    return;
}
int code = data[0] & 0xf0;
if ( code != 0x80 && code != 0x90 ) {
    return;
}
if ( code == 0x90 && data[2] == 0x00 ) {
    code = 0x80;//ベロシティ0のNoteOnはNoteOff
}

int note = (0xff & data[1]);

int clock = AppManager.getCurrentClock();
int unit = AppManager.getPositionQuantizeClock();
if ( unit > 1 ) {
    clock = doQuantize( clock, unit );
}

if ( code == 0x80 ) {
    /*if ( AppManager.mAddingEvent != null ) {
        int len = clock - AppManager.mAddingEvent.Clock;
        if ( len <= 0 ) {
            len = unit;
        }
        AppManager.mAddingEvent.ID.Length = len;
        int selected = AppManager.getSelected();
        CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandEventAdd( selected,
                                                                                       AppManager.mAddingEvent ) );
        AppManager.register( AppManager.getVsqFile().executeCommand( run ) );
        if ( !isEdited() ) {
            setEdited( true );
        }
        updateDrawObjectList();
    }*/
} else if ( code == 0x90 ) {
    if ( AppManager.mAddingEvent != null ) {
        // mAddingEventがnullでない場合は打ち込みの試行中(未確定の音符がある)
        // であるので，ノートだけが変わるようにする
        clock = AppManager.mAddingEvent.Clock;
    } else {
        AppManager.mAddingEvent = new VsqEvent();
    }
    AppManager.mAddingEvent.Clock = clock;
    if ( AppManager.mAddingEvent.ID == null ) {
        AppManager.mAddingEvent.ID = new VsqID();
    }
    AppManager.mAddingEvent.ID.type = VsqIDType.Anote;
    AppManager.mAddingEvent.ID.Dynamics = 64;
    AppManager.mAddingEvent.ID.VibratoHandle = null;
    if ( AppManager.mAddingEvent.ID.LyricHandle == null ) {
        AppManager.mAddingEvent.ID.LyricHandle = new LyricHandle( "a", "a" );
    }
    AppManager.mAddingEvent.ID.LyricHandle.L0.Phrase = "a";
    AppManager.mAddingEvent.ID.LyricHandle.L0.setPhoneticSymbol( "a" );
    AppManager.mAddingEvent.ID.Note = note;

    // 音符の長さを計算
    int length = QuantizeModeUtil.getQuantizeClock(
            AppManager.editorConfig.getLengthQuantize(),
            AppManager.editorConfig.isLengthQuantizeTriplet() );

    // 音符の長さを設定
    Utility.editLengthOfVsqEvent(
        AppManager.mAddingEvent,
        length,
        AppManager.vibratoLengthEditingRule );

    // 現在位置は，音符の末尾になる
    AppManager.setCurrentClock( clock + length );

    // 画面を再描画
    refreshScreen();
    // 鍵盤音を鳴らす
    KeySoundPlayer.play( note );
}
        }

        /// <summary>
        /// 文字列を、現在の言語設定に従って翻訳します。
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public static String _( String id )
        {
return Messaging.getMessage( id );
        }

        /// <summary>
        /// VsqEvent, VsqBPList, BezierCurvesの全てのクロックを、tempoに格納されているテンポテーブルに
        /// 合致するようにシフトします．ただし，このメソッド内ではtargetのテンポテーブルは変更せず，クロック値だけが変更される．
        /// </summary>
        /// <param name="work"></param>
        /// <param name="tempo"></param>
        public static void shiftClockToMatchWith( VsqFileEx target, VsqFile tempo, double shift_seconds )
        {
// テンポをリプレースする場合。
// まずクロック値を、リプレース後のモノに置き換え
for ( int track = 1; track < target.Track.size(); track++ ) {
    // ノート・歌手イベントをシフト
    for ( Iterator<VsqEvent> itr = target.Track.get( track ).getEventIterator(); itr.hasNext(); ) {
        VsqEvent item = itr.next();
        if ( item.ID.type == VsqIDType.Singer && item.Clock == 0 ) {
            continue;
        }
        int clock = item.Clock;
        double sec_start = target.getSecFromClock( clock ) + shift_seconds;
        double sec_end = target.getSecFromClock( clock + item.ID.getLength() ) + shift_seconds;
        int clock_start = (int)tempo.getClockFromSec( sec_start );
        int clock_end = (int)tempo.getClockFromSec( sec_end );
        item.Clock = clock_start;
        item.ID.setLength( clock_end - clock_start );
        if ( item.ID.VibratoHandle != null ) {
            double sec_vib_start = target.getSecFromClock( clock + item.ID.VibratoDelay ) + shift_seconds;
            int clock_vib_start = (int)tempo.getClockFromSec( sec_vib_start );
            item.ID.VibratoDelay = clock_vib_start - clock_start;
            item.ID.VibratoHandle.setLength( clock_end - clock_vib_start );
        }
    }

    // コントロールカーブをシフト
    for ( int j = 0; j < Utility.CURVE_USAGE.length; j++ ) {
        CurveType ct = Utility.CURVE_USAGE[j];
        VsqBPList item = target.Track.get( track ).getCurve( ct.getName() );
        if ( item == null ) {
            continue;
        }
        VsqBPList repl = new VsqBPList( item.getName(), item.getDefault(), item.getMinimum(), item.getMaximum() );
        for ( int i = 0; i < item.size(); i++ ) {
            int clock = item.getKeyClock( i );
            int value = item.getElement( i );
            double sec = target.getSecFromClock( clock ) + shift_seconds;
            if ( sec >= 0 ) {
                int clock_new = (int)tempo.getClockFromSec( sec );
                repl.add( clock_new, value );
            }
        }
        target.Track.get( track ).setCurve( ct.getName(), repl );
    }

    // ベジエカーブをシフト
    for ( int j = 0; j < Utility.CURVE_USAGE.length; j++ ) {
        CurveType ct = Utility.CURVE_USAGE[j];
        Vector<BezierChain> list = target.AttachedCurves.get( track - 1 ).get( ct );
        if ( list == null ) {
            continue;
        }
        for ( Iterator<BezierChain> itr = list.iterator(); itr.hasNext(); ) {
            BezierChain chain = itr.next();
            for ( Iterator<BezierPoint> itr2 = chain.points.iterator(); itr2.hasNext(); ) {
                BezierPoint point = itr2.next();
                PointD bse = new PointD( tempo.getClockFromSec( target.getSecFromClock( point.getBase().getX() ) + shift_seconds ),
                                         point.getBase().getY() );
                double rx = point.getBase().getX() + point.controlRight.getX();
                double new_rx = tempo.getClockFromSec( target.getSecFromClock( rx ) + shift_seconds );
                PointD ctrl_r = new PointD( new_rx - bse.getX(), point.controlRight.getY() );

                double lx = point.getBase().getX() + point.controlLeft.getX();
                double new_lx = tempo.getClockFromSec( target.getSecFromClock( lx ) + shift_seconds );
                PointD ctrl_l = new PointD( new_lx - bse.getX(), point.controlLeft.getY() );
                point.setBase( bse );
                point.controlLeft = ctrl_l;
                point.controlRight = ctrl_r;
            }
        }
    }
}
        }

        /// <summary>
        /// フォームのタイトルバーが画面内に入るよう、Locationを正規化します
        /// </summary>
        /// <param name="form"></param>
        public static void normalizeFormLocation( BForm dlg )
        {
Rectangle rcScreen = PortUtil.getWorkingArea( dlg );
int top = dlg.getY();
if ( top + dlg.getHeight() > rcScreen.y + rcScreen.height ) {
    // ダイアログの下端が隠れる場合、位置をずらす
    top = rcScreen.y + rcScreen.height - dlg.getHeight();
}
if ( top < rcScreen.y ) {
    // ダイアログの上端が隠れる場合、位置をずらす
    top = rcScreen.y;
}
int left = dlg.getX();
if ( left + dlg.getWidth() > rcScreen.x + rcScreen.width ) {
    left = rcScreen.x + rcScreen.width - dlg.getWidth();
}
if ( left < rcScreen.x ) {
    left = rcScreen.x;
}
dlg.setLocation( left, top );
        }


    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private BMenuBar menuStripMain = null;
    private BMenu menuFile = null;
    private BMenuItem menuFileNew = null;
    private BMenuItem menuFileOpen = null;
    private BMenuItem menuFileSave = null;
    private BMenuItem menuFileSaveNamed = null;
    private JSeparator menuFileSeparator1 = null;
    private BMenuItem menuFileOpenVsq = null;
    private BMenuItem menuFileOpenUst = null;
    private BMenu menuFileImport = null;
    private BMenu menuFileExport = null;
    private JSeparator menuFileSeparator2 = null;
    private BMenu menuFileRecent = null;
    private JSeparator menuFileSeparator3 = null;
    private BMenuItem menuFileQuit = null;
    private BMenuItem menuFileImportVsq = null;
    private BMenuItem menuFileImportMidi = null;
    private BMenuItem menuFileExportWave = null;
    private BMenuItem menuFileExportMidi = null;
    private BMenu menuEdit = null;
    private BMenuItem menuEditUndo = null;
    private BMenuItem menuEditRedo = null;
    private JSeparator toolStripMenuItem103 = null;
    private BMenuItem menuEditCut = null;
    private BMenuItem menuEditCopy = null;
    private BMenuItem menuEditPaste = null;
    private BMenuItem menuEditDelete = null;
    private JSeparator toolStripMenuItem104 = null;
    private BMenuItem menuEditAutoNormalizeMode = null;
    private JSeparator toolStripMenuItem1041 = null;
    private BMenuItem menuEditSelectAll = null;
    private BMenuItem menuEditSelectAllEvents = null;
    private BMenu menuVisual = null;
    private BMenuItem menuVisualControlTrack = null;
    private BMenuItem menuVisualMixer = null;
    private BMenuItem menuVisualWaveform = null;
    private BMenuItem menuVisualProperty = null;
    private BMenuItem menuVisualOverview = null;
    private JSeparator toolStripMenuItem1031 = null;
    private BMenuItem menuVisualGridline = null;
    private JSeparator toolStripMenuItem1032 = null;
    private BMenuItem menuVisualStartMarker = null;
    private BMenuItem menuVisualEndMarker = null;
    private JSeparator toolStripMenuItem1033 = null;
    private BMenuItem menuVisualNoteProperty = null;
    private BMenuItem menuVisualLyrics = null;
    private BMenuItem menuVisualPitchLine = null;
    private BMenu menuJob = null;
    private BMenuItem menuJobNormalize = null;
    private BMenuItem menuJobInsertBar = null;
    private BMenuItem menuJobDeleteBar = null;
    private BMenuItem menuJobRandomize = null;
    private BMenuItem menuJobConnect = null;
    private BMenuItem menuJobLyric = null;
    private BMenu menuTrack = null;
    private BMenuItem menuTrackOn = null;
    private JSeparator toolStripMenuItem10321 = null;
    private BMenuItem menuTrackAdd = null;
    private BMenuItem menuTrackCopy = null;
    private BMenuItem menuTrackChangeName = null;
    private BMenuItem menuTrackDelete = null;
    private JSeparator toolStripMenuItem10322 = null;
    private BMenuItem menuTrackRenderCurrent = null;
    private BMenuItem menuTrackRenderAll = null;
    private JSeparator toolStripMenuItem10323 = null;
    private BMenuItem menuTrackOverlay = null;
    private BMenu menuTrackRenderer = null;
    private JSeparator toolStripMenuItem10324 = null;
    private BMenu menuTrackBgm = null;
    private BMenu menuLyric = null;
    private BMenuItem menuLyricExpressionProperty = null;
    private BMenuItem menuLyricVibratoProperty = null;
    private BMenuItem menuLyricPhonemeTransformation = null;
    private BMenuItem menuLyricDictionary = null;
    private BMenu menuScript = null;
    private BMenuItem menuScriptUpdate = null;
    private BMenu menuSetting = null;
    private BMenuItem menuSettingPreference = null;
    private BMenu menuSettingGameControler = null;
    private BMenuItem menuSettingShortcut = null;
    private BMenuItem menuSettingVibratoPreset = null;
    private JSeparator toolStripMenuItem103211 = null;
    private BMenuItem menuSettingDefaultSingerStyle = null;
    private JSeparator toolStripMenuItem103212 = null;
    private BMenu menuSettingPositionQuantize = null;
    private BMenu menuSettingPaletteTool = null;
    private BMenuItem menuSettingPositionQuantize04 = null;
    private BMenuItem menuSettingPositionQuantize08 = null;
    private BMenuItem menuSettingPositionQuantize16 = null;
    private BMenuItem menuSettingPositionQuantize32 = null;
    private BMenuItem menuSettingPositionQuantize64 = null;
    private BMenuItem menuSettingPositionQuantize128 = null;
    private BMenuItem menuSettingPositionQuantizeOff = null;
    private JSeparator toolStripMenuItem1032121 = null;
    private BMenuItem menuSettingPositionQuantizeTriplet = null;
    private BMenu menuHelp = null;
    private BMenuItem menuHelpAbout = null;
    private BSplitPane splitContainer2 = null;
    private BPanel panel1 = null;
    private BPanel panel2A = null;
    private BSplitPane splitContainer1 = null;
    private TrackSelector trackSelector = null;
    private BSplitPane splitContainerProperty = null;
    private BPanel m_property_panel_container = null;
    private BToolBar toolStripFile = null;
    private BToolBarButton stripBtnFileNew = null;
    private BToolBarButton stripBtnFileOpen = null;
    private BToolBarButton stripBtnFileSave = null;
    private BToolBarButton stripBtnCut = null;
    private BToolBarButton stripBtnCopy = null;
    private BToolBarButton stripBtnPaste = null;
    private BToolBarButton stripBtnUndo = null;
    private BToolBarButton stripBtnRedo = null;
    private BToolBar toolStripPosition = null;
    private BToolBarButton stripBtnMoveTop = null;
    private BPanel BPanel = null;
    private BToolBarButton stripBtnRewind = null;
    private BToolBarButton stripBtnForward = null;
    private BToolBarButton stripBtnMoveEnd = null;
    private BToolBarButton stripBtnPlay = null;
    private BToolBarButton stripBtnScroll = null;
    private BToolBarButton stripBtnLoop = null;
    private BToolBar toolStripTool = null;
    private BToolBarButton stripBtnPointer = null;
    private BToolBarButton stripBtnPencil = null;
    private BToolBarButton stripBtnLine = null;
    private BToolBarButton stripBtnEraser = null;
    private BToolBarButton stripBtnGrid = null;
    private BToolBarButton stripBtnCurve = null;
    private BPopupMenu cMenuTrackSelector = null;  //  @jve:decl-index=0:visual-constraint="985,100"
    private BPopupMenu cMenuPiano = null;  //  @jve:decl-index=0:visual-constraint="984,231"
    private BPopupMenu cMenuTrackTab = null;  //  @jve:decl-index=0:visual-constraint="985,358"
    private BMenuItem cMenuTrackSelectorPointer = null;
    private BMenuItem cMenuTrackSelectorPencil = null;
    private BMenuItem cMenuTrackSelectorLine = null;
    private BMenuItem cMenuTrackSelectorEraser = null;
    private BMenuItem cMenuTrackSelectorPaletteTool = null;
    private BMenuItem cMenuTrackSelectorCurve = null;
    private BMenuItem cMenuTrackSelectorUndo = null;
    private BMenuItem cMenuTrackSelectorRedo = null;
    private BMenuItem cMenuTrackSelectorCut = null;
    private BMenuItem cMenuTrackSelectorCopy = null;
    private BMenuItem cMenuTrackSelectorPaste = null;
    private BMenuItem cMenuTrackSelectorDelete = null;
    private BMenuItem cMenuTrackSelectorDeleteBezier = null;
    private BMenuItem cMenuTrackSelectorSelectAll = null;
    private BMenuItem cMenuPianoPointer = null;
    private BMenuItem cMenuPianoPencil = null;
    private BMenuItem cMenuPianoEraser = null;
    private BMenuItem cMenuPianoPaletteTool = null;
    private BMenuItem cMenuPianoCurve = null;
    private BMenu cMenuPianoFixed = null;
    private BMenu cMenuPianoQuantize = null;
    private BMenuItem cMenuPianoGrid = null;
    private BMenuItem cMenuPianoUndo = null;
    private BMenuItem cMenuPianoRedo = null;
    private BMenuItem cMenuPianoCut = null;
    private BMenuItem cMenuPianoCopy = null;
    private BMenuItem cMenuPianoPaste = null;
    private BMenuItem cMenuPianoDelete = null;
    private BMenuItem cMenuPianoSelectAll = null;
    private BMenuItem cMenuPianoSelectAllEvents = null;
    private BMenuItem cMenuPianoImportLyric = null;
    private BMenuItem cMenuPianoExpressionProperty = null;
    private BMenuItem cMenuPianoVibratoProperty = null;
    private BMenuItem cMenuPianoFixed02 = null;
    private BMenuItem cMenuPianoFixed04 = null;
    private BMenuItem cMenuPianoFixed08 = null;
    private BMenuItem cMenuPianoFixed01 = null;
    private BMenuItem cMenuPianoFixed16 = null;
    private BMenuItem cMenuPianoFixed32 = null;
    private BMenuItem cMenuPianoFixed64 = null;
    private BMenuItem cMenuPianoFixed128 = null;
    private BMenuItem cMenuPianoFixedOff = null;
    private BMenuItem cMenuPianoFixedTriplet = null;
    private BMenuItem cMenuPianoFixedDotted = null;
    private BMenuItem cMenuPianoQuantize04 = null;
    private BMenuItem cMenuPianoQuantize08 = null;
    private BMenuItem cMenuPianoQuantize16 = null;
    private BMenuItem cMenuPianoQuantize32 = null;
    private BMenuItem cMenuPianoQuantize64 = null;
    private BMenuItem cMenuPianoQuantize128 = null;
    private BMenuItem cMenuPianoQuantizeTriplet = null;
    private BMenuItem cMenuTrackTabTrackOn = null;
    private BMenuItem cMenuTrackTabAdd = null;
    private BMenuItem cMenuTrackTabCopy = null;
    private BMenuItem cMenuTrackTabChangeName = null;
    private BMenuItem cMenuTrackTabDelete = null;
    private BMenuItem cMenuTrackTabRenderCurrent = null;
    private BMenuItem cMenuTrackTabRenderAll = null;
    private BMenuItem cMenuTrackTabOverlay = null;
    private BMenu cMenuTrackTabRenderer = null;
    private BMenuItem cMenuTrackTabRendererVOCALOID2 = null;
    private BMenuItem cMenuTrackTabRendererVOCALOID1 = null;
    private BMenu cMenuTrackTabRendererUtau = null;
    private BMenuItem cMenuTrackTabRendererStraight = null;
    private BMenuItem cMenuPianoQuantizeOff = null;
    private BPanel panel3 = null;
    private PictOverview panelOverview = null;
    public PictPianoRoll pictPianoRoll = null;
    private BVScrollBar vScroll = null;
    public BHScrollBar hScroll = null;
    private BPanel pictureBox3 = null;
    private BSlider trackBar = null;
    private BButton pictKeyLengthSplitter = null;
    private BPanel picturePositionIndicator = null;
    private BPanel jPanel1 = null;
    private BPanel jPanel3 = null;
    private BLabel statusLabel = null;
    private BMenu menuHidden = null;
    private BMenuItem menuHiddenEditLyric = null;
    private BMenuItem menuHiddenEditFlipToolPointerPencil = null;
    private BMenuItem menuHiddenEditFlipToolPointerEraser = null;
    private BMenuItem menuHiddenVisualForwardParameter = null;
    private BMenuItem menuHiddenVisualBackwardParameter = null;
    private BMenuItem menuHiddenTrackNext = null;
    private BMenuItem menuHiddenTrackBack = null;
    private BMenuItem menuHiddenCopy = null;
    private BMenuItem menuHiddenPaste = null;
    private BMenuItem menuHiddenCut = null;
    private BMenuItem menuTrackRendererVOCALOID1 = null;
    private BMenuItem menuTrackRendererVOCALOID2 = null;
    private BMenu menuTrackRendererUtau = null;
    private BMenuItem menuTrackRendererVCNT = null;
    private BMenuItem menuSettingGameControlerSetting = null;
    private BMenuItem menuSettingGameControlerLoad = null;
    private BMenuItem menuSettingGameControlerRemove = null;
    private BMenuItem menuHelpDebug = null;
    private BPanel pictureBox2 = null;
    private BMenu menuVisualPluginUi = null;
    private BMenuItem menuVisualPluginUiVocaloid1 = null;
    private BMenuItem menuVisualPluginUiVocaloid2 = null;
    private BMenuItem menuVisualPluginUiAquesTone = null;
    private BMenuItem menuHiddenSelectForward = null;
    private BMenuItem menuHiddenSelectBackward = null;
    private BMenuItem menuHiddenMoveUp = null;
    private BMenuItem menuHiddenMoveDown = null;
    private BMenuItem menuHiddenMoveLeft = null;
    private BMenuItem menuHiddenMoveRight = null;
    private BMenuItem menuHiddenLengthen = null;
    private BMenuItem menuHiddenShorten = null;
    private BMenuItem menuHiddenGoToStartMarker = null;
    private BMenuItem menuHiddenGoToEndMarker = null;
    private BMenuItem menuFileExportMusicXml = null;
    private BMenuItem menuTrackRendererAquesTone = null;
    private BMenuItem cMenuTrackTabRendererAquesTone = null;
    private BMenuItem menuVisualIconPalette = null;
    private BMenuItem menuHiddenPlayFromStartMarker = null;
    private BMenuItem menuHiddenFlipCurveOnPianorollMode = null;
    private BMenuItem menuFileImportUst = null;
    private BMenuItem menuFileExportParaWave = null;
    private BMenuItem menuFileExportVsq = null;
    private BMenuItem menuFileExportUst = null;
    private BMenuItem menuFileExportVxt = null;
    private BMenu menuLyricCopyVibratoToPreset = null;
    private BMenuItem menuSettingSequence = null;
    private BMenu menuHelpLog = null;
    private BMenuItem menuHelpLogSwitch = null;
    private BMenuItem menuHelpLogOpen = null;
    private BMenuItem menuHiddenPrintPoToCSV = null;
    private BToggleButton stripBtnStepSequencer = null;
    private JPanel jPanel2 = null;
    private BPopupMenu cMenuPositionIndicator = null;  //  @jve:decl-index=0:visual-constraint="991,295"
    private BMenuItem cMenuPositionIndicatorStartMarker = null;
    private BMenuItem cMenuPositionIndicatorEndMarker = null;
    private BButton buttonVZoom = null;
    private BButton buttonVMooz = null;
    private WaveformZoomUiImpl panelWaveformZoom = null;
    private WaveView waveView = null;
    private BMenu menuWindow = null;
    private BMenuItem menuWindowMinimize = null;
    private BPanel panel21 = null;
    private BMenuItem menuHelpManual = null;
    private BMenuItem menuFileRecentClear = null;
    private BMenuItem menuLyricApplyUtauParameters = null;
        BMenuItem stripDDBtnQuantize04 = null;
        BMenuItem stripDDBtnQuantize08 = null;
        BMenuItem stripDDBtnQuantize16 = null;
        BMenuItem stripDDBtnQuantize32 = null;
        BMenuItem stripDDBtnQuantize64 = null;
        BMenuItem stripDDBtnQuantize128 = null;
        BMenuItem stripDDBtnQuantizeOff = null;

    /**
     * 指定したメニューの名前を，そのオブジェクトのフィールド名に変更します．
     * メニューの子についても再帰的に処理を行います
     */
    private void setMenuName( JMenuItem menu )
    {
        if ( menu instanceof JMenu ){
            JMenu jm = (JMenu)menu;
            int count = jm.getMenuComponentCount();
            for( int i = 0; i < count; i++ ){
                Component comp = jm.getMenuComponent( i );
                if ( comp instanceof JMenuItem ){
                    setMenuName( (JMenuItem)comp );
                }
            }
        }
        setMenuNameCore( menu );
    }

    /**
     * 指定したメニューの名前を，そのオブジェクトのフィールド名に変更します
     * @param item
     */
    private void setMenuNameCore( Object item )
    {
        if ( item == null ){
            return;
        }
        if ( !(item instanceof JMenuItem) ){
            return;
        }
        for( Field f : FormMain.class.getDeclaredFields() ){
            if( item.getClass().isAssignableFrom( f.getType() ) ){
                // 型が同じ場合
                try{
                    Object obj = f.get( this );
                    if ( obj != null ){
                        JMenuItem jmi = (JMenuItem)obj;
                        if ( jmi == item ){
                            // nameプロパティにフィールド名を用いる
                            jmi.setName( f.getName() );
                        }
                    }
                }catch( Exception ex ){
                }
            }
        }
    }
    
    /**
     * This method initializes menuFileExportMusicXml	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileExportMusicXml() {
        if (menuFileExportMusicXml == null) {
            menuFileExportMusicXml = new BMenuItem();
            menuFileExportMusicXml.setText("MusicXML");
        }
        return menuFileExportMusicXml;
    }

    /**
     * This method initializes menuTrackRendererAquesTone	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuTrackRendererAquesTone() {
        if (menuTrackRendererAquesTone == null) {
            menuTrackRendererAquesTone = new BMenuItem();
            menuTrackRendererAquesTone.setText("AquesTone");
        }
        return menuTrackRendererAquesTone;
    }

    /**
     * This method initializes cMenuTrackTabRendererAquesTone	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getCMenuTrackTabRendererAquesTone() {
        if (cMenuTrackTabRendererAquesTone == null) {
            cMenuTrackTabRendererAquesTone = new BMenuItem();
            cMenuTrackTabRendererAquesTone.setText("AquesTone");
        }
        return cMenuTrackTabRendererAquesTone;
    }

    /**
     * This method initializes menuVisualIconPalette	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuVisualIconPalette() {
        if (menuVisualIconPalette == null) {
            menuVisualIconPalette = new BMenuItem();
            menuVisualIconPalette.setText("Icon Palette");
            menuVisualIconPalette.setCheckOnClick(true);
        }
        return menuVisualIconPalette;
    }

    /**
     * This method initializes menuHiddenPlayFromStartMarker	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenPlayFromStartMarker() {
        if (menuHiddenPlayFromStartMarker == null) {
            menuHiddenPlayFromStartMarker = new BMenuItem();
            menuHiddenPlayFromStartMarker.setText("Play From Start Marker");
        }
        return menuHiddenPlayFromStartMarker;
    }

    /**
     * This method initializes menuHiddenFlipCurveOnPianorollMode	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenFlipCurveOnPianorollMode() {
        if (menuHiddenFlipCurveOnPianorollMode == null) {
            menuHiddenFlipCurveOnPianorollMode = new BMenuItem();
            menuHiddenFlipCurveOnPianorollMode.setText("Change pitch drawing mode");
        }
        return menuHiddenFlipCurveOnPianorollMode;
    }

    /**
     * This method initializes menuFileImportUst	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileImportUst() {
        if (menuFileImportUst == null) {
            menuFileImportUst = new BMenuItem();
            menuFileImportUst.setText("UTAU project file");
        }
        return menuFileImportUst;
    }

    /**
     * This method initializes menuFileExportParaWave	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileExportParaWave() {
        if (menuFileExportParaWave == null) {
            menuFileExportParaWave = new BMenuItem();
            menuFileExportParaWave.setText("Serial numbered Wave");
        }
        return menuFileExportParaWave;
    }

    /**
     * This method initializes menuFileExportVsq	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileExportVsq() {
        if (menuFileExportVsq == null) {
            menuFileExportVsq = new BMenuItem();
            menuFileExportVsq.setText("VSQ File");
        }
        return menuFileExportVsq;
    }

    /**
     * This method initializes menuFileExportUst	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileExportUst() {
        if (menuFileExportUst == null) {
            menuFileExportUst = new BMenuItem();
            menuFileExportUst.setText("UTAU Project File (current track)");
        }
        return menuFileExportUst;
    }

    /**
     * This method initializes menuFileExportVxt	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileExportVxt() {
        if (menuFileExportVxt == null) {
            menuFileExportVxt = new BMenuItem();
            menuFileExportVxt.setText("Metatext for vConnect");
        }
        return menuFileExportVxt;
    }

    /**
     * This method initializes menuLyricCopyVibratoToPreset	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenu getMenuLyricCopyVibratoToPreset() {
        if (menuLyricCopyVibratoToPreset == null) {
            menuLyricCopyVibratoToPreset = new BMenu();
            menuLyricCopyVibratoToPreset.setText("Copy vibrato config to preset");
        }
        return menuLyricCopyVibratoToPreset;
    }

    /**
     * This method initializes menuSettingSequence	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuSettingSequence() {
        if (menuSettingSequence == null) {
            menuSettingSequence = new BMenuItem();
            menuSettingSequence.setText("Sequence config");
        }
        return menuSettingSequence;
    }

    /**
     * This method initializes menuHelpLog	
     * 	
     * @return org.kbinani.windows.forms.BMenu	
     */
    private BMenu getMenuHelpLog() {
        if (menuHelpLog == null) {
            menuHelpLog = new BMenu();
            menuHelpLog.setText("Log");
            menuHelpLog.add(getMenuHelpLogSwitch());
            menuHelpLog.add(getMenuHelpLogOpen());
        }
        return menuHelpLog;
    }

    /**
     * This method initializes menuHelpLogSwitch	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHelpLogSwitch() {
        if (menuHelpLogSwitch == null) {
            menuHelpLogSwitch = new BMenuItem();
            menuHelpLogSwitch.setText("Enable log");
            menuHelpLogSwitch.setCheckOnClick(true);
        }
        return menuHelpLogSwitch;
    }

    /**
     * This method initializes menuHelpLogOpen	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHelpLogOpen() {
        if (menuHelpLogOpen == null) {
            menuHelpLogOpen = new BMenuItem();
            menuHelpLogOpen.setText("Open");
        }
        return menuHelpLogOpen;
    }

    /**
     * This method initializes menuHiddenPrintPoToCSV	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenPrintPoToCSV() {
        if (menuHiddenPrintPoToCSV == null) {
            menuHiddenPrintPoToCSV = new BMenuItem();
            menuHiddenPrintPoToCSV.setText("Print language configs to CSV");
        }
        return menuHiddenPrintPoToCSV;
    }

    /**
     * This method initializes stripBtnStepSequencer	
     * 	
     * @return org.kbinani.windows.forms.BToggleButton	
     */
    private BToggleButton getStripBtnStepSequencer() {
        if (stripBtnStepSequencer == null) {
            stripBtnStepSequencer = new BToggleButton();
            stripBtnStepSequencer.setText("");
        }
        return stripBtnStepSequencer;
    }


    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(960, 636);
        this.setJMenuBar(getMenuStripMain());
        this.setContentPane(this.getJContentPane());
        this.setTitle("JFrame");
        this.getCMenuPiano();
        this.getCMenuTrackSelector();
        this.getCMenuTrackTab();
        this.getCMenuPositionIndicator();
        int count = menuStripMain.getMenuCount();
        for ( int i = 0; i < count; i++ ){
            JMenu jm = menuStripMain.getMenu( i );
            setMenuName( jm );
        }
        pictPianoRoll.setFocusTraversalKeysEnabled( false );
        trackSelector.setFocusTraversalKeysEnabled( false );
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.BPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.NORTH);
            jContentPane.add(getSplitContainerProperty(), BorderLayout.CENTER);
            jContentPane.add(getJPanel3(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes menuStripMain    
     *  
     * @return javax.swing.BMenuBar 
     */
    private BMenuBar getMenuStripMain() {
        if (menuStripMain == null) {
            menuStripMain = new BMenuBar();
            menuStripMain.add(getMenuFile());
            menuStripMain.add(getMenuEdit());
            menuStripMain.add(getMenuVisual());
            menuStripMain.add(getMenuJob());
            menuStripMain.add(getMenuTrack());
            menuStripMain.add(getMenuLyric());
            menuStripMain.add(getMenuScript());
            menuStripMain.add(getMenuSetting());
            menuStripMain.add(getMenuWindow());
            menuStripMain.add(getMenuHelp());
            menuStripMain.add(getMenuHidden());
        }
        return menuStripMain;
    }

    /**
     * This method initializes menuFile 
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuFile() {
        if (menuFile == null) {
            menuFile = new BMenu();
            menuFile.setText("File");
            menuFile.add(getMenuFileNew());
            menuFile.add(getMenuFileOpen());
            menuFile.add(getMenuFileSave());
            menuFile.add(getMenuFileSaveNamed());
            menuFile.add(getBMenuItem());
            menuFile.add(getBMenuItem2());
            menuFile.add(getBMenuItem3());
            menuFile.add(getMenuFileImport());
            menuFile.add(getMenuFileExport());
            menuFile.add(getMenuFileSeparator2());
            menuFile.add(getBMenuItem4());
            menuFile.add(getMenuFileSeparator3());
            menuFile.add(getBMenuItem5());
        }
        return menuFile;
    }

    /**
     * This method initializes menuFileNew  
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuFileNew() {
        if (menuFileNew == null) {
            menuFileNew = new BMenuItem();
            menuFileNew.setText("New");
        }
        return menuFileNew;
    }

    /**
     * This method initializes menuFileOpen 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuFileOpen() {
        if (menuFileOpen == null) {
            menuFileOpen = new BMenuItem();
            menuFileOpen.setText("Open");
        }
        return menuFileOpen;
    }

    /**
     * This method initializes menuFileSave 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuFileSave() {
        if (menuFileSave == null) {
            menuFileSave = new BMenuItem();
            menuFileSave.setText("Save");
        }
        return menuFileSave;
    }

    /**
     * This method initializes menuFileSaveNamed    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuFileSaveNamed() {
        if (menuFileSaveNamed == null) {
            menuFileSaveNamed = new BMenuItem();
            menuFileSaveNamed.setText("Save As");
        }
        return menuFileSaveNamed;
    }

    /**
     * This method initializes toolStripMenuItem10  
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getBMenuItem() {
        if (menuFileSeparator1 == null) {
            menuFileSeparator1 = new JSeparator();
        }
        return menuFileSeparator1;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem2() {
        if (menuFileOpenVsq == null) {
            menuFileOpenVsq = new BMenuItem();
            menuFileOpenVsq.setText("Open VSQ/Vocaloid Midi");
        }
        return menuFileOpenVsq;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem3() {
        if (menuFileOpenUst == null) {
            menuFileOpenUst = new BMenuItem();
            menuFileOpenUst.setText("Open UTAU Project File");
        }
        return menuFileOpenUst;
    }

    /**
     * This method initializes menuFileImport   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuFileImport() {
        if (menuFileImport == null) {
            menuFileImport = new BMenu();
            menuFileImport.setText("Import");
            menuFileImport.add(getBMenuItem6());
            menuFileImport.add(getBMenuItem7());
            menuFileImport.add(getMenuFileImportUst());
        }
        return menuFileImport;
    }

    /**
     * This method initializes menuFileExport   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuFileExport() {
        if (menuFileExport == null) {
            menuFileExport = new BMenu();
            menuFileExport.setText("Export");
            menuFileExport.add(getBMenuItem8());
            menuFileExport.add(getMenuFileExportParaWave());
            menuFileExport.add(getMenuFileExportVsq());
            menuFileExport.add(getBMenuItem9());
            menuFileExport.add(getMenuFileExportMusicXml());
            menuFileExport.add(getMenuFileExportUst());
            menuFileExport.add(getMenuFileExportVxt());
        }
        return menuFileExport;
    }

    /**
     * This method initializes menuFileSeparator2 
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getMenuFileSeparator2() {
        if (menuFileSeparator2 == null) {
            menuFileSeparator2 = new JSeparator();
        }
        return menuFileSeparator2;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenu getBMenuItem4() {
        if (menuFileRecent == null) {
            menuFileRecent = new BMenu();
            menuFileRecent.setText("Recent Files");
            menuFileRecent.add(getMenuFileRecentClear());
        }
        return menuFileRecent;
    }

    /**
     * This method initializes menuFileSeparator3 
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getMenuFileSeparator3() {
        if (menuFileSeparator3 == null) {
            menuFileSeparator3 = new JSeparator();
        }
        return menuFileSeparator3;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem5() {
        if (menuFileQuit == null) {
            menuFileQuit = new BMenuItem();
            menuFileQuit.setText("Quit");
        }
        return menuFileQuit;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem6() {
        if (menuFileImportVsq == null) {
            menuFileImportVsq = new BMenuItem();
            menuFileImportVsq.setText("VSQ / Vocaloid MIDI");
        }
        return menuFileImportVsq;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem7() {
        if (menuFileImportMidi == null) {
            menuFileImportMidi = new BMenuItem();
            menuFileImportMidi.setText("Standard MIDI");
        }
        return menuFileImportMidi;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem8() {
        if (menuFileExportWave == null) {
            menuFileExportWave = new BMenuItem();
            menuFileExportWave.setText("Wave");
        }
        return menuFileExportWave;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem9() {
        if (menuFileExportMidi == null) {
            menuFileExportMidi = new BMenuItem();
            menuFileExportMidi.setText("MIDI");
        }
        return menuFileExportMidi;
    }

    /**
     * This method initializes menuEdit 
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuEdit() {
        if (menuEdit == null) {
            menuEdit = new BMenu();
            menuEdit.setText("Edit");
            menuEdit.add(getBMenuItem10());
            menuEdit.add(getBMenuItem11());
            menuEdit.add(getToolStripMenuItem103());
            menuEdit.add(getBMenuItem12());
            menuEdit.add(getMenuEditCopy());
            menuEdit.add(getBMenuItem22());
            menuEdit.add(getBMenuItem13());
            menuEdit.add(getToolStripMenuItem104());
            menuEdit.add(getBMenuItem14());
            menuEdit.add(getToolStripMenuItem1041());
            menuEdit.add(getBMenuItem15());
            menuEdit.add(getMenuEditSelectAllEvents());
        }
        return menuEdit;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem10() {
        if (menuEditUndo == null) {
            menuEditUndo = new BMenuItem();
            menuEditUndo.setText("Undo");
        }
        return menuEditUndo;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem11() {
        if (menuEditRedo == null) {
            menuEditRedo = new BMenuItem();
            menuEditRedo.setText("Redo");
        }
        return menuEditRedo;
    }

    /**
     * This method initializes toolStripMenuItem103 
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem103() {
        if (toolStripMenuItem103 == null) {
            toolStripMenuItem103 = new JSeparator();
        }
        return toolStripMenuItem103;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem12() {
        if (menuEditCut == null) {
            menuEditCut = new BMenuItem();
            menuEditCut.setText("Cut");
        }
        return menuEditCut;
    }

    /**
     * This method initializes menuEditCopy 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuEditCopy() {
        if (menuEditCopy == null) {
            menuEditCopy = new BMenuItem();
            menuEditCopy.setText("Copy");
        }
        return menuEditCopy;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem22() {
        if (menuEditPaste == null) {
            menuEditPaste = new BMenuItem();
            menuEditPaste.setText("Paste");
        }
        return menuEditPaste;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem13() {
        if (menuEditDelete == null) {
            menuEditDelete = new BMenuItem();
            menuEditDelete.setText("Delete");
        }
        return menuEditDelete;
    }

    /**
     * This method initializes toolStripMenuItem104 
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem104() {
        if (toolStripMenuItem104 == null) {
            toolStripMenuItem104 = new JSeparator();
        }
        return toolStripMenuItem104;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem14() {
        if (menuEditAutoNormalizeMode == null) {
            menuEditAutoNormalizeMode = new BMenuItem();
            menuEditAutoNormalizeMode.setText("Auto Normalize Mode");
        }
        return menuEditAutoNormalizeMode;
    }

    /**
     * This method initializes toolStripMenuItem1041    
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem1041() {
        if (toolStripMenuItem1041 == null) {
            toolStripMenuItem1041 = new JSeparator();
        }
        return toolStripMenuItem1041;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem15() {
        if (menuEditSelectAll == null) {
            menuEditSelectAll = new BMenuItem();
            menuEditSelectAll.setText("Select All");
        }
        return menuEditSelectAll;
    }

    /**
     * This method initializes menuEditSelectAllEvents  
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuEditSelectAllEvents() {
        if (menuEditSelectAllEvents == null) {
            menuEditSelectAllEvents = new BMenuItem();
            menuEditSelectAllEvents.setText("Select All Events");
        }
        return menuEditSelectAllEvents;
    }

    /**
     * This method initializes menuVisual   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuVisual() {
        if (menuVisual == null) {
            menuVisual = new BMenu();
            menuVisual.setText("Visual");
            menuVisual.add(getBMenuItem16());
            menuVisual.add(getBMenuItem17());
            menuVisual.add(getMenuVisualWaveform());
            menuVisual.add(getMenuVisualIconPalette());
            menuVisual.add(getBMenuItem23());
            menuVisual.add(getBMenuItem32());
            menuVisual.add(getMenuVisualPluginUi());
            menuVisual.add(getToolStripMenuItem1031());
            menuVisual.add(getBMenuItem18());
            menuVisual.add(getToolStripMenuItem1032());
            menuVisual.add(getBMenuItem19());
            menuVisual.add(getMenuVisualEndMarker());
            menuVisual.add(getToolStripMenuItem1033());
            menuVisual.add(getMenuVisualLyrics());
            menuVisual.add(getBMenuItem20());
            menuVisual.add(getBMenuItem24());
        }
        return menuVisual;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem16() {
        if (menuVisualControlTrack == null) {
            menuVisualControlTrack = new BMenuItem();
            menuVisualControlTrack.setText("Control Track");
            menuVisualControlTrack.setSelected(true);
            menuVisualControlTrack.setCheckOnClick(true);
        }
        return menuVisualControlTrack;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem17() {
        if (menuVisualMixer == null) {
            menuVisualMixer = new BMenuItem();
            menuVisualMixer.setText("Mixer");
            menuVisualMixer.setCheckOnClick(true);
        }
        return menuVisualMixer;
    }

    /**
     * This method initializes menuVisualWaveform   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuVisualWaveform() {
        if (menuVisualWaveform == null) {
            menuVisualWaveform = new BMenuItem();
            menuVisualWaveform.setText("Waveform");
            menuVisualWaveform.setCheckOnClick(true);
        }
        return menuVisualWaveform;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem23() {
        if (menuVisualProperty == null) {
            menuVisualProperty = new BMenuItem();
            menuVisualProperty.setText("Property Window");
            menuVisualProperty.setCheckOnClick(true);
        }
        return menuVisualProperty;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem32() {
        if (menuVisualOverview == null) {
            menuVisualOverview = new BMenuItem();
            menuVisualOverview.setText("Navigation");
            menuVisualOverview.setCheckOnClick(true);
        }
        return menuVisualOverview;
    }

    /**
     * This method initializes toolStripMenuItem1031    
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem1031() {
        if (toolStripMenuItem1031 == null) {
            toolStripMenuItem1031 = new JSeparator();
        }
        return toolStripMenuItem1031;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem18() {
        if (menuVisualGridline == null) {
            menuVisualGridline = new BMenuItem();
            menuVisualGridline.setText("Grid Line");
            menuVisualGridline.setCheckOnClick(true);
        }
        return menuVisualGridline;
    }

    /**
     * This method initializes toolStripMenuItem1032    
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem1032() {
        if (toolStripMenuItem1032 == null) {
            toolStripMenuItem1032 = new JSeparator();
        }
        return toolStripMenuItem1032;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem19() {
        if (menuVisualStartMarker == null) {
            menuVisualStartMarker = new BMenuItem();
            menuVisualStartMarker.setText("Start Marker");
            menuVisualStartMarker.setCheckOnClick(false);
        }
        return menuVisualStartMarker;
    }

    /**
     * This method initializes menuVisualEndMarker  
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuVisualEndMarker() {
        if (menuVisualEndMarker == null) {
            menuVisualEndMarker = new BMenuItem();
            menuVisualEndMarker.setText("End Marker");
            menuVisualEndMarker.setCheckOnClick(false);
        }
        return menuVisualEndMarker;
    }

    /**
     * This method initializes toolStripMenuItem1033    
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem1033() {
        if (toolStripMenuItem1033 == null) {
            toolStripMenuItem1033 = new JSeparator();
        }
        return toolStripMenuItem1033;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem20() {
        if (menuVisualNoteProperty == null) {
            menuVisualNoteProperty = new BMenuItem();
            menuVisualNoteProperty.setText("Note Expression/Vibrato");
            menuVisualNoteProperty.setCheckOnClick(true);
        }
        return menuVisualNoteProperty;
    }

    /**
     * This method initializes menuVisualLyrics 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuVisualLyrics() {
        if (menuVisualLyrics == null) {
            menuVisualLyrics = new BMenuItem();
            menuVisualLyrics.setText("Lyrics/Phoneme");
            menuVisualLyrics.setCheckOnClick(true);
        }
        return menuVisualLyrics;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem24() {
        if (menuVisualPitchLine == null) {
            menuVisualPitchLine = new BMenuItem();
            menuVisualPitchLine.setText("Pitch Line");
            menuVisualPitchLine.setCheckOnClick(true);
        }
        return menuVisualPitchLine;
    }

    /**
     * This method initializes menuJob  
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuJob() {
        if (menuJob == null) {
            menuJob = new BMenu();
            menuJob.setText("Job");
            menuJob.add(getBMenuItem21());
            menuJob.add(getMenuJobInsertBar());
            menuJob.add(getBMenuItem25());
            menuJob.add(getBMenuItem33());
            menuJob.add(getBMenuItem42());
            menuJob.add(getBMenuItem52());
        }
        return menuJob;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem21() {
        if (menuJobNormalize == null) {
            menuJobNormalize = new BMenuItem();
            menuJobNormalize.setText("Normalize Notes");
        }
        return menuJobNormalize;
    }

    /**
     * This method initializes menuJobInsertBar 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuJobInsertBar() {
        if (menuJobInsertBar == null) {
            menuJobInsertBar = new BMenuItem();
            menuJobInsertBar.setText("Insert Bars");
        }
        return menuJobInsertBar;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem25() {
        if (menuJobDeleteBar == null) {
            menuJobDeleteBar = new BMenuItem();
            menuJobDeleteBar.setText("Delete Bars");
        }
        return menuJobDeleteBar;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem33() {
        if (menuJobRandomize == null) {
            menuJobRandomize = new BMenuItem();
            menuJobRandomize.setText("Randomize");
        }
        return menuJobRandomize;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem42() {
        if (menuJobConnect == null) {
            menuJobConnect = new BMenuItem();
            menuJobConnect.setText("Connect Notes");
        }
        return menuJobConnect;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem52() {
        if (menuJobLyric == null) {
            menuJobLyric = new BMenuItem();
            menuJobLyric.setText("Insert Lyrics");
        }
        return menuJobLyric;
    }

    /**
     * This method initializes menuTrack    
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuTrack() {
        if (menuTrack == null) {
            menuTrack = new BMenu();
            menuTrack.setText("Track");
            menuTrack.add(getBMenuItem27());
            menuTrack.add(getToolStripMenuItem10321());
            menuTrack.add(getMenuTrackAdd());
            menuTrack.add(getBMenuItem28());
            menuTrack.add(getBMenuItem34());
            menuTrack.add(getBMenuItem43());
            menuTrack.add(getToolStripMenuItem10322());
            menuTrack.add(getBMenuItem53());
            menuTrack.add(getBMenuItem63());
            menuTrack.add(getToolStripMenuItem10323());
            menuTrack.add(getBMenuItem73());
            menuTrack.add(getMenuTrackRenderer());
            menuTrack.add(getToolStripMenuItem10324());
            menuTrack.add(getMenuTrackBgm());
        }
        return menuTrack;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem27() {
        if (menuTrackOn == null) {
            menuTrackOn = new BMenuItem();
            menuTrackOn.setText("Track On");
        }
        return menuTrackOn;
    }

    /**
     * This method initializes toolStripMenuItem10321   
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem10321() {
        if (toolStripMenuItem10321 == null) {
            toolStripMenuItem10321 = new JSeparator();
        }
        return toolStripMenuItem10321;
    }

    /**
     * This method initializes menuTrackAdd 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuTrackAdd() {
        if (menuTrackAdd == null) {
            menuTrackAdd = new BMenuItem();
            menuTrackAdd.setText("Add Track");
        }
        return menuTrackAdd;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem28() {
        if (menuTrackCopy == null) {
            menuTrackCopy = new BMenuItem();
            menuTrackCopy.setText("Copy Track");
        }
        return menuTrackCopy;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem34() {
        if (menuTrackChangeName == null) {
            menuTrackChangeName = new BMenuItem();
            menuTrackChangeName.setText("Rename Track");
        }
        return menuTrackChangeName;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem43() {
        if (menuTrackDelete == null) {
            menuTrackDelete = new BMenuItem();
            menuTrackDelete.setText("Delete Track");
        }
        return menuTrackDelete;
    }

    /**
     * This method initializes toolStripMenuItem10322   
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem10322() {
        if (toolStripMenuItem10322 == null) {
            toolStripMenuItem10322 = new JSeparator();
        }
        return toolStripMenuItem10322;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem53() {
        if (menuTrackRenderCurrent == null) {
            menuTrackRenderCurrent = new BMenuItem();
            menuTrackRenderCurrent.setText("Render Current Track");
        }
        return menuTrackRenderCurrent;
    }

    /**
     * This method initializes BMenuItem6   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem63() {
        if (menuTrackRenderAll == null) {
            menuTrackRenderAll = new BMenuItem();
            menuTrackRenderAll.setText("Render All Tracks");
        }
        return menuTrackRenderAll;
    }

    /**
     * This method initializes toolStripMenuItem10323   
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem10323() {
        if (toolStripMenuItem10323 == null) {
            toolStripMenuItem10323 = new JSeparator();
        }
        return toolStripMenuItem10323;
    }

    /**
     * This method initializes BMenuItem7   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem73() {
        if (menuTrackOverlay == null) {
            menuTrackOverlay = new BMenuItem();
            menuTrackOverlay.setText("Overlay");
            menuTrackOverlay.setCheckOnClick(true);
        }
        return menuTrackOverlay;
    }

    /**
     * This method initializes menuTrackRenderer    
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuTrackRenderer() {
        if (menuTrackRenderer == null) {
            menuTrackRenderer = new BMenu();
            menuTrackRenderer.setText("Renderer");
            menuTrackRenderer.add(getMenuTrackRendererVOCALOID1());
            menuTrackRenderer.add(getMenuTrackRendererVOCALOID2());
            menuTrackRenderer.add(getMenuTrackRendererUtau());
            menuTrackRenderer.add(getMenuTrackRendererVCNT());
            menuTrackRenderer.add(getMenuTrackRendererAquesTone());
        }
        return menuTrackRenderer;
    }

    /**
     * This method initializes toolStripMenuItem10324   
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem10324() {
        if (toolStripMenuItem10324 == null) {
            toolStripMenuItem10324 = new JSeparator();
        }
        return toolStripMenuItem10324;
    }

    /**
     * This method initializes menuTrackBgm 
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuTrackBgm() {
        if (menuTrackBgm == null) {
            menuTrackBgm = new BMenu();
            menuTrackBgm.setText("BGM");
        }
        return menuTrackBgm;
    }

    /**
     * This method initializes menuLyric    
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuLyric() {
        if (menuLyric == null) {
            menuLyric = new BMenu();
            menuLyric.setText("Lyrics");
            menuLyric.add(getBMenuItem29());
            menuLyric.add(getMenuLyricVibratoProperty());
            menuLyric.add(getMenuLyricApplyUtauParameters());
            menuLyric.add(getBMenuItem210());
            menuLyric.add(getBMenuItem35());
            menuLyric.add(getMenuLyricCopyVibratoToPreset());
        }
        return menuLyric;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem29() {
        if (menuLyricExpressionProperty == null) {
            menuLyricExpressionProperty = new BMenuItem();
            menuLyricExpressionProperty.setText("Note Expression Propertry");
        }
        return menuLyricExpressionProperty;
    }

    /**
     * This method initializes menuLyricVibratoProperty 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuLyricVibratoProperty() {
        if (menuLyricVibratoProperty == null) {
            menuLyricVibratoProperty = new BMenuItem();
            menuLyricVibratoProperty.setText("Note Vibrato Property");
        }
        return menuLyricVibratoProperty;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem210() {
        if (menuLyricPhonemeTransformation == null) {
            menuLyricPhonemeTransformation = new BMenuItem();
            menuLyricPhonemeTransformation.setText("Phoneme Transformation");
        }
        return menuLyricPhonemeTransformation;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem35() {
        if (menuLyricDictionary == null) {
            menuLyricDictionary = new BMenuItem();
            menuLyricDictionary.setText("User Word Dictionary");
        }
        return menuLyricDictionary;
    }

    /**
     * This method initializes menuScript   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuScript() {
        if (menuScript == null) {
            menuScript = new BMenu();
            menuScript.setText("Script");
            menuScript.add(getBMenuItem30());
        }
        return menuScript;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem30() {
        if (menuScriptUpdate == null) {
            menuScriptUpdate = new BMenuItem();
            menuScriptUpdate.setText("Update Script List");
        }
        return menuScriptUpdate;
    }

    /**
     * This method initializes menuSetting  
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuSetting() {
        if (menuSetting == null) {
            menuSetting = new BMenu();
            menuSetting.setText("Setting");
            menuSetting.add(getBMenuItem31());
            menuSetting.add(getMenuSettingSequence());
            menuSetting.add(getMenuSettingPositionQuantize());
            menuSetting.add(getToolStripMenuItem103211());
            menuSetting.add(getMenuSettingGameControler());
            menuSetting.add(getMenuSettingPaletteTool());
            menuSetting.add(getBMenuItem211());
            menuSetting.add(getBMenuItem44());
            menuSetting.add(getToolStripMenuItem103212());
            menuSetting.add(getBMenuItem54());
        }
        return menuSetting;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem31() {
        if (menuSettingPreference == null) {
            menuSettingPreference = new BMenuItem();
            menuSettingPreference.setText("Preference");
        }
        return menuSettingPreference;
    }

    /**
     * This method initializes menuSettingGameControler 
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuSettingGameControler() {
        if (menuSettingGameControler == null) {
            menuSettingGameControler = new BMenu();
            menuSettingGameControler.setText("Game Controler");
            menuSettingGameControler.add(getMenuSettingGameControlerSetting());
            menuSettingGameControler.add(getMenuSettingGameControlerLoad());
            menuSettingGameControler.add(getMenuSettingGameControlerRemove());
        }
        return menuSettingGameControler;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem211() {
        if (menuSettingShortcut == null) {
            menuSettingShortcut = new BMenuItem();
            menuSettingShortcut.setText("Shortcut Key");
        }
        return menuSettingShortcut;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem44() {
        if (menuSettingVibratoPreset == null) {
            menuSettingVibratoPreset = new BMenuItem();
            menuSettingVibratoPreset.setText("Vibrato preset");
        }
        return menuSettingVibratoPreset;
    }

    /**
     * This method initializes toolStripMenuItem103211  
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem103211() {
        if (toolStripMenuItem103211 == null) {
            toolStripMenuItem103211 = new JSeparator();
        }
        return toolStripMenuItem103211;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem54() {
        if (menuSettingDefaultSingerStyle == null) {
            menuSettingDefaultSingerStyle = new BMenuItem();
            menuSettingDefaultSingerStyle.setText("Singing Style Defaults");
        }
        return menuSettingDefaultSingerStyle;
    }

    /**
     * This method initializes toolStripMenuItem103212  
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem103212() {
        if (toolStripMenuItem103212 == null) {
            toolStripMenuItem103212 = new JSeparator();
        }
        return toolStripMenuItem103212;
    }

    /**
     * This method initializes menuSettingPositionQuantize  
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuSettingPositionQuantize() {
        if (menuSettingPositionQuantize == null) {
            menuSettingPositionQuantize = new BMenu();
            menuSettingPositionQuantize.setText("Quantize");
            menuSettingPositionQuantize.add(getBMenuItem37());
            menuSettingPositionQuantize.add(getMenuSettingPositionQuantize08());
            menuSettingPositionQuantize.add(getBMenuItem212());
            menuSettingPositionQuantize.add(getBMenuItem38());
            menuSettingPositionQuantize.add(getBMenuItem45());
            menuSettingPositionQuantize.add(getBMenuItem55());
            menuSettingPositionQuantize.add(getBMenuItem65());
            menuSettingPositionQuantize.add(getToolStripMenuItem1032121());
            menuSettingPositionQuantize.add(getBMenuItem74());
        }
        return menuSettingPositionQuantize;
    }

    /**
     * This method initializes menuSettingPaletteTool   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuSettingPaletteTool() {
        if (menuSettingPaletteTool == null) {
            menuSettingPaletteTool = new BMenu();
            menuSettingPaletteTool.setText("Palette Tool");
        }
        return menuSettingPaletteTool;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem37() {
        if (menuSettingPositionQuantize04 == null) {
            menuSettingPositionQuantize04 = new BMenuItem();
            menuSettingPositionQuantize04.setText("1/4");
        }
        return menuSettingPositionQuantize04;
    }

    /**
     * This method initializes menuSettingPositionQuantize08    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getMenuSettingPositionQuantize08() {
        if (menuSettingPositionQuantize08 == null) {
            menuSettingPositionQuantize08 = new BMenuItem();
            menuSettingPositionQuantize08.setText("1/8");
        }
        return menuSettingPositionQuantize08;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem212() {
        if (menuSettingPositionQuantize16 == null) {
            menuSettingPositionQuantize16 = new BMenuItem();
            menuSettingPositionQuantize16.setText("1/16");
        }
        return menuSettingPositionQuantize16;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem38() {
        if (menuSettingPositionQuantize32 == null) {
            menuSettingPositionQuantize32 = new BMenuItem();
            menuSettingPositionQuantize32.setText("1/32");
        }
        return menuSettingPositionQuantize32;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem45() {
        if (menuSettingPositionQuantize64 == null) {
            menuSettingPositionQuantize64 = new BMenuItem();
            menuSettingPositionQuantize64.setText("1/64");
        }
        return menuSettingPositionQuantize64;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem55() {
        if (menuSettingPositionQuantize128 == null) {
            menuSettingPositionQuantize128 = new BMenuItem();
            menuSettingPositionQuantize128.setText("1/128");
        }
        return menuSettingPositionQuantize128;
    }

    /**
     * This method initializes BMenuItem6   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem65() {
        if (menuSettingPositionQuantizeOff == null) {
            menuSettingPositionQuantizeOff = new BMenuItem();
            menuSettingPositionQuantizeOff.setText("Off");
        }
        return menuSettingPositionQuantizeOff;
    }

    /**
     * This method initializes toolStripMenuItem1032121 
     *  
     * @return javax.swing.JSeparator   
     */
    private JSeparator getToolStripMenuItem1032121() {
        if (toolStripMenuItem1032121 == null) {
            toolStripMenuItem1032121 = new JSeparator();
        }
        return toolStripMenuItem1032121;
    }

    /**
     * This method initializes BMenuItem7   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem74() {
        if (menuSettingPositionQuantizeTriplet == null) {
            menuSettingPositionQuantizeTriplet = new BMenuItem();
            menuSettingPositionQuantizeTriplet.setText("Triplet");
        }
        return menuSettingPositionQuantizeTriplet;
    }

    /**
     * This method initializes menuHelp 
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getMenuHelp() {
        if (menuHelp == null) {
            menuHelp = new BMenu();
            menuHelp.setText("Help");
            menuHelp.add(getBMenuItem39());
            menuHelp.add(getMenuHelpManual());
            menuHelp.add(getMenuHelpLog());
            menuHelp.add(getMenuHelpDebug());
        }
        return menuHelp;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getBMenuItem39() {
        if (menuHelpAbout == null) {
            menuHelpAbout = new BMenuItem();
            menuHelpAbout.setText("About Cadencii");
        }
        return menuHelpAbout;
    }

    /**
     * This method initializes splitContainer2  
     *  
     * @return javax.swing.BSplitPane   
     */
    private BSplitPane getSplitContainer2() {
        if (splitContainer2 == null) {
            splitContainer2 = new BSplitPane();
            splitContainer2.setDividerSize(9);
            splitContainer2.setDividerLocation(200);
            splitContainer2.setEnabled(false);
            splitContainer2.setResizeWeight(1.0D);
            splitContainer2.setPanel2Hidden(true);
            splitContainer2.setContinuousLayout(true);
            splitContainer2.setBottomComponent(getPanel2A());
            splitContainer2.setTopComponent(getJPanel1());
            splitContainer2.setOrientation(BSplitPane.VERTICAL_SPLIT);
            splitContainer2.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        }
        return splitContainer2;
    }

    /**
     * This method initializes panel1   
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getPanel1() {
        if (panel1 == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 3;
            gridBagConstraints2.gridheight = 1;
            gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 0.0D;
            gridBagConstraints11.anchor = GridBagConstraints.EAST;
            gridBagConstraints11.gridwidth = 1;
            gridBagConstraints11.gridx = 2;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.fill = GridBagConstraints.BOTH;
            gridBagConstraints10.gridy = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.BOTH;
            gridBagConstraints9.gridy = 1;
            gridBagConstraints9.weighty = 0.0D;
            gridBagConstraints9.weightx = 1.0D;
            gridBagConstraints9.gridx = 1;
            panel1 = new BPanel();
            panel1.setLayout(new GridBagLayout());
            panel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel1.add(getPictureBox3(), gridBagConstraints10);
            panel1.add(getHScroll(), gridBagConstraints9);
            panel1.add(getTrackBar(), gridBagConstraints11);
            panel1.add(getJPanel22(), gridBagConstraints2);
        }
        return panel1;
    }

    /**
     * This method initializes panel2A   
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getPanel2A() {
        if (panel2A == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.fill = GridBagConstraints.BOTH;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.weighty = 1.0D;
            gridBagConstraints17.gridy = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.weighty = 1.0D;
            gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints5.gridy = 0;
            panel2A = new BPanel();
            panel2A.setLayout(new GridBagLayout());
            panel2A.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel2A.add(getPanel2(), gridBagConstraints5);
            panel2A.add(getWaveView(), gridBagConstraints17);
        }
        return panel2A;
    }

    /**
     * This method initializes splitContainer1  
     *  
     * @return javax.swing.BSplitPane   
     */
    private BSplitPane getSplitContainer1() {
        if (splitContainer1 == null) {
            splitContainer1 = new BSplitPane();
            splitContainer1.setDividerLocation(300);
            splitContainer1.setResizeWeight(1.0D);
            splitContainer1.setDividerSize(9);
            splitContainer1.setContinuousLayout(false);
            splitContainer1.setTopComponent(getSplitContainer2());
            splitContainer1.setBottomComponent(getTrackSelector());
            splitContainer1.setOrientation(BSplitPane.VERTICAL_SPLIT);
            splitContainer1.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        }
        return splitContainer1;
    }

    /**
     * This method initializes trackSelector    
     *  
     * @return javax.swing.BPanel   
     */
    private TrackSelector getTrackSelector() {
        if (trackSelector == null) {
            trackSelector = new TrackSelector();
            trackSelector.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return trackSelector;
    }

    /**
     * This method initializes splitContainerProperty   
     *  
     * @return javax.swing.BSplitPane   
     */
    private BSplitPane getSplitContainerProperty() {
        if (splitContainerProperty == null) {
            splitContainerProperty = new BSplitPane();
            splitContainerProperty.setDividerLocation(0);
            splitContainerProperty.setEnabled(false);
            splitContainerProperty.setDividerSize(0);
            splitContainerProperty.setResizeWeight(0.0D);
            splitContainerProperty.setContinuousLayout(false);
            splitContainerProperty.setRightComponent(getSplitContainer1());
            splitContainerProperty.setLeftComponent(getM_property_panel_container());
            splitContainerProperty.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        }
        return splitContainerProperty;
    }

    /**
     * This method initializes m_property_panel_container   
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getM_property_panel_container() {
        if (m_property_panel_container == null) {
            m_property_panel_container = new BPanel();
            m_property_panel_container.setLayout(new GridBagLayout());
            m_property_panel_container.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return m_property_panel_container;
    }

    /**
     * This method initializes toolStripFile    
     *  
     * @return javax.swing.BToolBar 
     */
    private BToolBar getToolStripFile() {
        if (toolStripFile == null) {
            toolStripFile = new BToolBar();
            toolStripFile.setName("toolStripFile");
            toolStripFile.add(getStripBtnFileNew());
            toolStripFile.add(getStripBtnFileOpen());
            toolStripFile.add(getStripBtnFileSave());
            toolStripFile.add(getStripBtnCut());
            toolStripFile.add(getStripBtnCopy());
            toolStripFile.add(getStripBtnPaste());
            toolStripFile.add(getStripBtnUndo());
            toolStripFile.add(getStripBtnRedo());
        }
        return toolStripFile;
    }

    /**
     * This method initializes stripBtnFileNew  
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnFileNew() {
        if (stripBtnFileNew == null) {
            stripBtnFileNew = new BToolBarButton();
            stripBtnFileNew.setText("");
            stripBtnFileNew.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnFileNew;
    }

    /**
     * This method initializes stripBtnFileOpen 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnFileOpen() {
        if (stripBtnFileOpen == null) {
            stripBtnFileOpen = new BToolBarButton();
            stripBtnFileOpen.setText("");
            stripBtnFileOpen.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnFileOpen;
    }

    /**
     * This method initializes stripBtnFileSave 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnFileSave() {
        if (stripBtnFileSave == null) {
            stripBtnFileSave = new BToolBarButton();
            stripBtnFileSave.setText("");
            stripBtnFileSave.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnFileSave;
    }

    /**
     * This method initializes stripBtnCut  
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnCut() {
        if (stripBtnCut == null) {
            stripBtnCut = new BToolBarButton();
            stripBtnCut.setText("");
            stripBtnCut.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnCut;
    }

    /**
     * This method initializes stripBtnCopy 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnCopy() {
        if (stripBtnCopy == null) {
            stripBtnCopy = new BToolBarButton();
            stripBtnCopy.setText("");
            stripBtnCopy.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnCopy;
    }

    /**
     * This method initializes stripBtnPaste    
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnPaste() {
        if (stripBtnPaste == null) {
            stripBtnPaste = new BToolBarButton();
            stripBtnPaste.setText("");
            stripBtnPaste.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnPaste;
    }

    /**
     * This method initializes stripBtnUndo 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnUndo() {
        if (stripBtnUndo == null) {
            stripBtnUndo = new BToolBarButton();
            stripBtnUndo.setText("");
            stripBtnUndo.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnUndo;
    }

    /**
     * This method initializes stripBtnRedo 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnRedo() {
        if (stripBtnRedo == null) {
            stripBtnRedo = new BToolBarButton();
            stripBtnRedo.setText("");
            stripBtnRedo.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnRedo;
    }

    /**
     * This method initializes toolStripPosition    
     *  
     * @return javax.swing.BToolBar 
     */
    private BToolBar getToolStripPosition() {
        if (toolStripPosition == null) {
            toolStripPosition = new BToolBar();
            toolStripPosition.setName("toolStripPosition");
            toolStripPosition.add(getStripBtnMoveTop());
            toolStripPosition.add(getStripBtnRewind());
            toolStripPosition.add(getStripBtnForward());
            toolStripPosition.add(getStripBtnMoveEnd());
            toolStripPosition.add(getStripBtnPlay());
            toolStripPosition.add(getStripBtnScroll());
            toolStripPosition.add(getStripBtnLoop());
        }
        return toolStripPosition;
    }

    /**
     * This method initializes stripBtnMoveTop  
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnMoveTop() {
        if (stripBtnMoveTop == null) {
            stripBtnMoveTop = new BToolBarButton();
            stripBtnMoveTop.setText("");
            stripBtnMoveTop.setSize(new Dimension(16, 16));
            stripBtnMoveTop.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnMoveTop;
    }

    /**
     * This method initializes BPanel   
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getJPanel() {
        if (BPanel == null) {
            GridLayout gridLayout4 = new GridLayout();
            gridLayout4.setRows(2);
            GridLayout gridLayout3 = new GridLayout();
            gridLayout3.setRows(2);
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(2);
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            BPanel = new BPanel();
            BPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.X_AXIS));
            BPanel.add(getToolStripFile(), null);
            BPanel.add(getToolStripPosition(), null);
            BPanel.add(getToolStripTool(), null);
        }
        return BPanel;
    }

    /**
     * This method initializes stripBtnRewind   
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnRewind() {
        if (stripBtnRewind == null) {
            stripBtnRewind = new BToolBarButton();
            stripBtnRewind.setText("");
            stripBtnRewind.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnRewind;
    }

    /**
     * This method initializes stripBtnForward  
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnForward() {
        if (stripBtnForward == null) {
            stripBtnForward = new BToolBarButton();
            stripBtnForward.setText("");
            stripBtnForward.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnForward;
    }

    /**
     * This method initializes stripBtnMoveEnd  
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnMoveEnd() {
        if (stripBtnMoveEnd == null) {
            stripBtnMoveEnd = new BToolBarButton();
            stripBtnMoveEnd.setText("");
            stripBtnMoveEnd.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnMoveEnd;
    }

    /**
     * This method initializes stripBtnPlay 
     *  
     * @return javax.swing.BButton  
     */
    private BToolBarButton getStripBtnPlay() {
        if (stripBtnPlay == null) {
            stripBtnPlay = new BToolBarButton();
            stripBtnPlay.setText("Play");
            stripBtnPlay.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnPlay;
    }

    /**
     * This method initializes stripBtnScroll   
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnScroll() {
        if (stripBtnScroll == null) {
            stripBtnScroll = new BToolBarButton();
            stripBtnScroll.setText("");
            stripBtnScroll.setCheckOnClick(true);
            stripBtnScroll.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnScroll;
    }

    /**
     * This method initializes stripBtnLoop 
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnLoop() {
        if (stripBtnLoop == null) {
            stripBtnLoop = new BToolBarButton();
            stripBtnLoop.setText("");
            stripBtnLoop.setCheckOnClick(true);
            stripBtnLoop.setPreferredSize(new Dimension(23, 22));
        }
        return stripBtnLoop;
    }

    /**
     * This method initializes toolStripTool    
     *  
     * @return javax.swing.BToolBar 
     */
    private BToolBar getToolStripTool() {
        if (toolStripTool == null) {
            toolStripTool = new BToolBar();
            toolStripTool.add(getStripBtnPointer());
            toolStripTool.add(getStripBtnPencil());
            toolStripTool.add(getStripBtnLine());
            toolStripTool.add(getStripBtnEraser());
            toolStripTool.add(getStripBtnGrid());
            toolStripTool.add(getStripBtnCurve());
            toolStripTool.add(getStripBtnStepSequencer());
        }
        return toolStripTool;
    }

    /**
     * This method initializes stripBtnPointer  
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnPointer() {
        if (stripBtnPointer == null) {
            stripBtnPointer = new BToolBarButton();
            stripBtnPointer.setText("Pointer");
            stripBtnPointer.setCheckOnClick(true);
        }
        return stripBtnPointer;
    }

    /**
     * This method initializes stripBtnPencil   
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnPencil() {
        if (stripBtnPencil == null) {
            stripBtnPencil = new BToolBarButton();
            stripBtnPencil.setText("Pencil");
            stripBtnPencil.setCheckOnClick(true);
        }
        return stripBtnPencil;
    }

    /**
     * This method initializes stripBtnLine 
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnLine() {
        if (stripBtnLine == null) {
            stripBtnLine = new BToolBarButton();
            stripBtnLine.setText("Line");
            stripBtnLine.setCheckOnClick(true);
        }
        return stripBtnLine;
    }

    /**
     * This method initializes stripBtnEraser   
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnEraser() {
        if (stripBtnEraser == null) {
            stripBtnEraser = new BToolBarButton();
            stripBtnEraser.setToolTipText("");
            stripBtnEraser.setCheckOnClick(true);
            stripBtnEraser.setText("Eraser");
        }
        return stripBtnEraser;
    }

    /**
     * This method initializes stripBtnGrid 
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnGrid() {
        if (stripBtnGrid == null) {
            stripBtnGrid = new BToolBarButton();
            stripBtnGrid.setText("");
            stripBtnGrid.setCheckOnClick(true);
        }
        return stripBtnGrid;
    }

    /**
     * This method initializes stripBtnCurve    
     *  
     * @return javax.swing.BToggleButton    
     */
    private BToolBarButton getStripBtnCurve() {
        if (stripBtnCurve == null) {
            stripBtnCurve = new BToolBarButton();
            stripBtnCurve.setText("");
            stripBtnCurve.setCheckOnClick(true);
        }
        return stripBtnCurve;
    }

    /**
     * This method initializes cMenuTrackSelector   
     *  
     * @return javax.swing.BPopupMenu   
     */
    private BPopupMenu getCMenuTrackSelector() {
        if (cMenuTrackSelector == null) {
            cMenuTrackSelector = new BPopupMenu();
            cMenuTrackSelector.add(getCMenuTrackSelectorPointer());
            cMenuTrackSelector.add(getCMenuTrackSelectorPencil());
            cMenuTrackSelector.add(getCMenuTrackSelectorLine());
            cMenuTrackSelector.add(getCMenuTrackSelectorEraser());
            cMenuTrackSelector.add(getCMenuTrackSelectorPaletteTool());
            cMenuTrackSelector.addSeparator();
            cMenuTrackSelector.add(getCMenuTrackSelectorCurve());
            cMenuTrackSelector.addSeparator();
            cMenuTrackSelector.add(getCMenuTrackSelectorUndo());
            cMenuTrackSelector.add(getCMenuTrackSelectorRedo());
            cMenuTrackSelector.addSeparator();
            cMenuTrackSelector.add(getCMenuTrackSelectorCut());
            cMenuTrackSelector.add(getCMenuTrackSelectorCopy());
            cMenuTrackSelector.add(getCMenuTrackSelectorPaste());
            cMenuTrackSelector.add(getCMenuTrackSelectorDelete());
            cMenuTrackSelector.add(getCMenuTrackSelectorDeleteBezier());
            cMenuTrackSelector.addSeparator();
            cMenuTrackSelector.add(getCMenuTrackSelectorSelectAll());
        }
        return cMenuTrackSelector;
    }

    /**
     * This method initializes cMenuPiano   
     *  
     * @return javax.swing.BPopupMenu   
     */
    private BPopupMenu getCMenuPiano() {
        if (cMenuPiano == null) {
            cMenuPiano = new BPopupMenu();
            cMenuPiano.add(getCMenuPianoPointer());
            cMenuPiano.add(getCMenuPianoPencil());
            cMenuPiano.add(getCMenuPianoEraser());
            cMenuPiano.add(getCMenuPianoPaletteTool());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoCurve());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoFixed());
            cMenuPiano.add(getCMenuPianoQuantize());
            cMenuPiano.add(getCMenuPianoGrid());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoUndo());
            cMenuPiano.add(getCMenuPianoRedo());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoCut());
            cMenuPiano.add(getCMenuPianoCopy());
            cMenuPiano.add(getCMenuPianoPaste());
            cMenuPiano.add(getCMenuPianoDelete());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoSelectAll());
            cMenuPiano.add(getCMenuPianoSelectAllEvents());
            cMenuPiano.addSeparator();
            cMenuPiano.add(getCMenuPianoImportLyric());
            cMenuPiano.add(getCMenuPianoExpressionProperty());
            cMenuPiano.add(getCMenuPianoVibratoProperty());
        }
        return cMenuPiano;
    }

    /**
     * This method initializes cMenuTrackTab    
     *  
     * @return javax.swing.BPopupMenu   
     */
    private BPopupMenu getCMenuTrackTab() {
        if (cMenuTrackTab == null) {
            cMenuTrackTab = new BPopupMenu();
            cMenuTrackTab.add(getCMenuTrackTabTrackOn());
            cMenuTrackTab.addSeparator();
            cMenuTrackTab.add(getCMenuTrackTabAdd());
            cMenuTrackTab.add(getCMenuTrackTabCopy());
            cMenuTrackTab.add(getCMenuTrackTabChangeName());
            cMenuTrackTab.add(getCMenuTrackTabDelete());
            cMenuTrackTab.addSeparator();
            cMenuTrackTab.add(getCMenuTrackTabRenderCurrent());
            cMenuTrackTab.add(getCMenuTrackTabRenderAll());
            cMenuTrackTab.addSeparator();
            cMenuTrackTab.add(getCMenuTrackTabOverlay());
            cMenuTrackTab.add(getCMenuTrackTabRenderer());
        }
        return cMenuTrackTab;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorPointer() {
        if (cMenuTrackSelectorPointer == null) {
            cMenuTrackSelectorPointer = new BMenuItem();
        }
        return cMenuTrackSelectorPointer;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorPencil() {
        if (cMenuTrackSelectorPencil == null) {
            cMenuTrackSelectorPencil = new BMenuItem();
        }
        return cMenuTrackSelectorPencil;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorLine() {
        if (cMenuTrackSelectorLine == null) {
            cMenuTrackSelectorLine = new BMenuItem();
        }
        return cMenuTrackSelectorLine;
    }

    /**
     * This method initializes cMenuTrackSelectorEraser 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorEraser() {
        if (cMenuTrackSelectorEraser == null) {
            cMenuTrackSelectorEraser = new BMenuItem();
        }
        return cMenuTrackSelectorEraser;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorPaletteTool() {
        if (cMenuTrackSelectorPaletteTool == null) {
            cMenuTrackSelectorPaletteTool = new BMenuItem();
        }
        return cMenuTrackSelectorPaletteTool;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorCurve() {
        if (cMenuTrackSelectorCurve == null) {
            cMenuTrackSelectorCurve = new BMenuItem();
        }
        return cMenuTrackSelectorCurve;
    }

    /**
     * This method initializes cMenuTrackSelectorUndo   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorUndo() {
        if (cMenuTrackSelectorUndo == null) {
            cMenuTrackSelectorUndo = new BMenuItem();
        }
        return cMenuTrackSelectorUndo;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorRedo() {
        if (cMenuTrackSelectorRedo == null) {
            cMenuTrackSelectorRedo = new BMenuItem();
        }
        return cMenuTrackSelectorRedo;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorCut() {
        if (cMenuTrackSelectorCut == null) {
            cMenuTrackSelectorCut = new BMenuItem();
        }
        return cMenuTrackSelectorCut;
    }

    /**
     * This method initializes cMenuTrackSelectorCopy   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorCopy() {
        if (cMenuTrackSelectorCopy == null) {
            cMenuTrackSelectorCopy = new BMenuItem();
        }
        return cMenuTrackSelectorCopy;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorPaste() {
        if (cMenuTrackSelectorPaste == null) {
            cMenuTrackSelectorPaste = new BMenuItem();
        }
        return cMenuTrackSelectorPaste;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorDelete() {
        if (cMenuTrackSelectorDelete == null) {
            cMenuTrackSelectorDelete = new BMenuItem();
        }
        return cMenuTrackSelectorDelete;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorDeleteBezier() {
        if (cMenuTrackSelectorDeleteBezier == null) {
            cMenuTrackSelectorDeleteBezier = new BMenuItem();
        }
        return cMenuTrackSelectorDeleteBezier;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackSelectorSelectAll() {
        if (cMenuTrackSelectorSelectAll == null) {
            cMenuTrackSelectorSelectAll = new BMenuItem();
        }
        return cMenuTrackSelectorSelectAll;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoPointer() {
        if (cMenuPianoPointer == null) {
            cMenuPianoPointer = new BMenuItem();
        }
        return cMenuPianoPointer;
    }

    /**
     * This method initializes cMenuPianoPencil 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoPencil() {
        if (cMenuPianoPencil == null) {
            cMenuPianoPencil = new BMenuItem();
        }
        return cMenuPianoPencil;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoEraser() {
        if (cMenuPianoEraser == null) {
            cMenuPianoEraser = new BMenuItem();
        }
        return cMenuPianoEraser;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoPaletteTool() {
        if (cMenuPianoPaletteTool == null) {
            cMenuPianoPaletteTool = new BMenuItem();
        }
        return cMenuPianoPaletteTool;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoCurve() {
        if (cMenuPianoCurve == null) {
            cMenuPianoCurve = new BMenuItem();
        }
        return cMenuPianoCurve;
    }

    /**
     * This method initializes cMenuPianoFixed  
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getCMenuPianoFixed() {
        if (cMenuPianoFixed == null) {
            cMenuPianoFixed = new BMenu();
            cMenuPianoFixed.add(getCMenuPianoFixed01());
            cMenuPianoFixed.add(getCMenuPianoFixed02());
            cMenuPianoFixed.add(getCMenuPianoFixed04());
            cMenuPianoFixed.add(getCMenuPianoFixed08());
            cMenuPianoFixed.add(getCMenuPianoFixed16());
            cMenuPianoFixed.add(getCMenuPianoFixed32());
            cMenuPianoFixed.add(getCMenuPianoFixed64());
            cMenuPianoFixed.add(getCMenuPianoFixed128());
            cMenuPianoFixed.add(getCMenuPianoFixedOff());
            cMenuPianoFixed.addSeparator();
            cMenuPianoFixed.add(getCMenuPianoFixedTriplet());
            cMenuPianoFixed.add(getCMenuPianoFixedDotted());
        }
        return cMenuPianoFixed;
    }

    /**
     * This method initializes cMenuPianoQuantize   
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getCMenuPianoQuantize() {
        if (cMenuPianoQuantize == null) {
            cMenuPianoQuantize = new BMenu();
            cMenuPianoQuantize.add(getCMenuPianoQuantize04());
            cMenuPianoQuantize.add(getCMenuPianoQuantize08());
            cMenuPianoQuantize.add(getCMenuPianoQuantize16());
            cMenuPianoQuantize.add(getCMenuPianoQuantize32());
            cMenuPianoQuantize.add(getCMenuPianoQuantize64());
            cMenuPianoQuantize.add(getCMenuPianoQuantize128());
            cMenuPianoQuantize.add(getCMenuPianoQuantizeOff());
            cMenuPianoQuantize.addSeparator();
            cMenuPianoQuantize.add(getCMenuPianoQuantizeTriplet());
        }
        return cMenuPianoQuantize;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoGrid() {
        if (cMenuPianoGrid == null) {
            cMenuPianoGrid = new BMenuItem();
        }
        return cMenuPianoGrid;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoUndo() {
        if (cMenuPianoUndo == null) {
            cMenuPianoUndo = new BMenuItem();
        }
        return cMenuPianoUndo;
    }

    /**
     * This method initializes cMenuPianoRedo   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoRedo() {
        if (cMenuPianoRedo == null) {
            cMenuPianoRedo = new BMenuItem();
        }
        return cMenuPianoRedo;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoCut() {
        if (cMenuPianoCut == null) {
            cMenuPianoCut = new BMenuItem();
        }
        return cMenuPianoCut;
    }

    /**
     * This method initializes cMenuPianoCopy   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoCopy() {
        if (cMenuPianoCopy == null) {
            cMenuPianoCopy = new BMenuItem();
        }
        return cMenuPianoCopy;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoPaste() {
        if (cMenuPianoPaste == null) {
            cMenuPianoPaste = new BMenuItem();
        }
        return cMenuPianoPaste;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoDelete() {
        if (cMenuPianoDelete == null) {
            cMenuPianoDelete = new BMenuItem();
        }
        return cMenuPianoDelete;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoSelectAll() {
        if (cMenuPianoSelectAll == null) {
            cMenuPianoSelectAll = new BMenuItem();
        }
        return cMenuPianoSelectAll;
    }

    /**
     * This method initializes cMenuPianoSelectAllEvents    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoSelectAllEvents() {
        if (cMenuPianoSelectAllEvents == null) {
            cMenuPianoSelectAllEvents = new BMenuItem();
        }
        return cMenuPianoSelectAllEvents;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoImportLyric() {
        if (cMenuPianoImportLyric == null) {
            cMenuPianoImportLyric = new BMenuItem();
        }
        return cMenuPianoImportLyric;
    }

    /**
     * This method initializes cMenuPianoExpressionProperty 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoExpressionProperty() {
        if (cMenuPianoExpressionProperty == null) {
            cMenuPianoExpressionProperty = new BMenuItem();
        }
        return cMenuPianoExpressionProperty;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoVibratoProperty() {
        if (cMenuPianoVibratoProperty == null) {
            cMenuPianoVibratoProperty = new BMenuItem();
        }
        return cMenuPianoVibratoProperty;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed02() {
        if (cMenuPianoFixed02 == null) {
            cMenuPianoFixed02 = new BMenuItem();
            cMenuPianoFixed02.setText("1/2");
        }
        return cMenuPianoFixed02;
    }

    /**
     * This method initializes cMenuPianoFixed04    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed04() {
        if (cMenuPianoFixed04 == null) {
            cMenuPianoFixed04 = new BMenuItem();
            cMenuPianoFixed04.setText("1/4");
        }
        return cMenuPianoFixed04;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed08() {
        if (cMenuPianoFixed08 == null) {
            cMenuPianoFixed08 = new BMenuItem();
            cMenuPianoFixed08.setText("1/8");
        }
        return cMenuPianoFixed08;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed01() {
        if (cMenuPianoFixed01 == null) {
            cMenuPianoFixed01 = new BMenuItem();
            cMenuPianoFixed01.setText("1/1");
        }
        return cMenuPianoFixed01;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed16() {
        if (cMenuPianoFixed16 == null) {
            cMenuPianoFixed16 = new BMenuItem();
            cMenuPianoFixed16.setText("1/16");
        }
        return cMenuPianoFixed16;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed32() {
        if (cMenuPianoFixed32 == null) {
            cMenuPianoFixed32 = new BMenuItem();
            cMenuPianoFixed32.setText("1/32");
        }
        return cMenuPianoFixed32;
    }

    /**
     * This method initializes BMenuItem6   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed64() {
        if (cMenuPianoFixed64 == null) {
            cMenuPianoFixed64 = new BMenuItem();
            cMenuPianoFixed64.setText("1/64");
        }
        return cMenuPianoFixed64;
    }

    /**
     * This method initializes BMenuItem7   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixed128() {
        if (cMenuPianoFixed128 == null) {
            cMenuPianoFixed128 = new BMenuItem();
            cMenuPianoFixed128.setText("1/128");
        }
        return cMenuPianoFixed128;
    }

    /**
     * This method initializes BMenuItem8   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixedOff() {
        if (cMenuPianoFixedOff == null) {
            cMenuPianoFixedOff = new BMenuItem();
            cMenuPianoFixedOff.setText("Off");
        }
        return cMenuPianoFixedOff;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixedTriplet() {
        if (cMenuPianoFixedTriplet == null) {
            cMenuPianoFixedTriplet = new BMenuItem();
            cMenuPianoFixedTriplet.setText("Triplet");
        }
        return cMenuPianoFixedTriplet;
    }

    /**
     * This method initializes cMenuPianoFixedDotted    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoFixedDotted() {
        if (cMenuPianoFixedDotted == null) {
            cMenuPianoFixedDotted = new BMenuItem();
            cMenuPianoFixedDotted.setText("Dot");
        }
        return cMenuPianoFixedDotted;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize04() {
        if (cMenuPianoQuantize04 == null) {
            cMenuPianoQuantize04 = new BMenuItem();
            cMenuPianoQuantize04.setText("1/4");
        }
        return cMenuPianoQuantize04;
    }

    /**
     * This method initializes cMenuPianoQuantize08 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize08() {
        if (cMenuPianoQuantize08 == null) {
            cMenuPianoQuantize08 = new BMenuItem();
            cMenuPianoQuantize08.setText("1/8");
        }
        return cMenuPianoQuantize08;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize16() {
        if (cMenuPianoQuantize16 == null) {
            cMenuPianoQuantize16 = new BMenuItem();
            cMenuPianoQuantize16.setText("1/16");
        }
        return cMenuPianoQuantize16;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize32() {
        if (cMenuPianoQuantize32 == null) {
            cMenuPianoQuantize32 = new BMenuItem();
            cMenuPianoQuantize32.setToolTipText("");
            cMenuPianoQuantize32.setText("1/32");
        }
        return cMenuPianoQuantize32;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize64() {
        if (cMenuPianoQuantize64 == null) {
            cMenuPianoQuantize64 = new BMenuItem();
            cMenuPianoQuantize64.setText("1/64");
        }
        return cMenuPianoQuantize64;
    }

    /**
     * This method initializes BMenuItem5   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantize128() {
        if (cMenuPianoQuantize128 == null) {
            cMenuPianoQuantize128 = new BMenuItem();
            cMenuPianoQuantize128.setText("1/128");
        }
        return cMenuPianoQuantize128;
    }

    /**
     * This method initializes BMenuItem6   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantizeTriplet() {
        if (cMenuPianoQuantizeTriplet == null) {
            cMenuPianoQuantizeTriplet = new BMenuItem();
            cMenuPianoQuantizeTriplet.setText("Triplet");
        }
        return cMenuPianoQuantizeTriplet;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabTrackOn() {
        if (cMenuTrackTabTrackOn == null) {
            cMenuTrackTabTrackOn = new BMenuItem();
            cMenuTrackTabTrackOn.setText("Track On");
        }
        return cMenuTrackTabTrackOn;
    }

    /**
     * This method initializes cMenuTrackTabAdd 
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabAdd() {
        if (cMenuTrackTabAdd == null) {
            cMenuTrackTabAdd = new BMenuItem();
        }
        return cMenuTrackTabAdd;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabCopy() {
        if (cMenuTrackTabCopy == null) {
            cMenuTrackTabCopy = new BMenuItem();
        }
        return cMenuTrackTabCopy;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabChangeName() {
        if (cMenuTrackTabChangeName == null) {
            cMenuTrackTabChangeName = new BMenuItem();
        }
        return cMenuTrackTabChangeName;
    }

    /**
     * This method initializes BMenuItem4   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabDelete() {
        if (cMenuTrackTabDelete == null) {
            cMenuTrackTabDelete = new BMenuItem();
        }
        return cMenuTrackTabDelete;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabRenderCurrent() {
        if (cMenuTrackTabRenderCurrent == null) {
            cMenuTrackTabRenderCurrent = new BMenuItem();
        }
        return cMenuTrackTabRenderCurrent;
    }

    /**
     * This method initializes cMenuTrackTabRenderAll   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabRenderAll() {
        if (cMenuTrackTabRenderAll == null) {
            cMenuTrackTabRenderAll = new BMenuItem();
        }
        return cMenuTrackTabRenderAll;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabOverlay() {
        if (cMenuTrackTabOverlay == null) {
            cMenuTrackTabOverlay = new BMenuItem();
        }
        return cMenuTrackTabOverlay;
    }

    /**
     * This method initializes cMenuTrackTabRenderer    
     *  
     * @return javax.swing.BMenu    
     */
    private BMenu getCMenuTrackTabRenderer() {
        if (cMenuTrackTabRenderer == null) {
            cMenuTrackTabRenderer = new BMenu();
            cMenuTrackTabRenderer.add(getCMenuTrackTabRendererVOCALOID1());
            cMenuTrackTabRenderer.add(getCMenuTrackTabRendererVOCALOID2());
            cMenuTrackTabRenderer.add(getCMenuTrackTabRendererUtau());
            cMenuTrackTabRenderer.add(getCMenuTrackTabRendererStraight());
            cMenuTrackTabRenderer.add(getCMenuTrackTabRendererAquesTone());
        }
        return cMenuTrackTabRenderer;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabRendererVOCALOID2() {
        if (cMenuTrackTabRendererVOCALOID2 == null) {
            cMenuTrackTabRendererVOCALOID2 = new BMenuItem();
            cMenuTrackTabRendererVOCALOID2.setText("VOCALOID2");
        }
        return cMenuTrackTabRendererVOCALOID2;
    }

    /**
     * This method initializes cMenuTrackTabRendererVOCALOID1   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabRendererVOCALOID1() {
        if (cMenuTrackTabRendererVOCALOID1 == null) {
            cMenuTrackTabRendererVOCALOID1 = new BMenuItem();
            cMenuTrackTabRendererVOCALOID1.setText("VOCALOID1");
        }
        return cMenuTrackTabRendererVOCALOID1;
    }

    /**
     * This method initializes BMenuItem2   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenu getCMenuTrackTabRendererUtau() {
        if (cMenuTrackTabRendererUtau == null) {
            cMenuTrackTabRendererUtau = new BMenu();
            cMenuTrackTabRendererUtau.setText("UTAU");
        }
        return cMenuTrackTabRendererUtau;
    }

    /**
     * This method initializes BMenuItem3   
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuTrackTabRendererStraight() {
        if (cMenuTrackTabRendererStraight == null) {
            cMenuTrackTabRendererStraight = new BMenuItem();
            cMenuTrackTabRendererStraight.setText("vConnect-STAND");
        }
        return cMenuTrackTabRendererStraight;
    }

    /**
     * This method initializes BMenuItem    
     *  
     * @return javax.swing.BMenuItem    
     */
    private BMenuItem getCMenuPianoQuantizeOff() {
        if (cMenuPianoQuantizeOff == null) {
            cMenuPianoQuantizeOff = new BMenuItem();
            cMenuPianoQuantizeOff.setText("Off");
        }
        return cMenuPianoQuantizeOff;
    }

    /**
     * This method initializes panel3   
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getPanel3() {
        if (panel3 == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.gridheight = 1;
            gridBagConstraints6.weighty = 1.0D;
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.gridy = 0;
            panel3 = new BPanel();
            panel3.setLayout(new GridBagLayout());
            panel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel3.add(getPanelOverview(), gridBagConstraints6);
        }
        return panel3;
    }

    /**
     * This method initializes panelOverview  
     *  
     * @return javax.swing.BPanel   
     */
    private PictOverview getPanelOverview() {
        if (panelOverview == null) {
            panelOverview = new PictOverview();
            panelOverview.setLayout(new GridBagLayout());
            panelOverview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panelOverview.setBackground(new Color(106, 108, 108));
            panelOverview.setPreferredSize(new Dimension(421, 50));
        }
        return panelOverview;
    }

    /**
     * This method initializes pictPianoRoll    
     *  
     * @return javax.swing.BPanel   
     */
    private PictPianoRoll getPictPianoRoll() {
        if (pictPianoRoll == null) {
            pictPianoRoll = new PictPianoRoll();
            pictPianoRoll.setLayout(new GridBagLayout());
            pictPianoRoll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return pictPianoRoll;
    }

    /**
     * This method initializes vScroll  
     *  
     * @return javax.swing.JScrollBar   
     */
    private BVScrollBar getVScroll() {
        if (vScroll == null) {
            vScroll = new BVScrollBar();
            vScroll.setPreferredSize(new Dimension(15, 50));
        }
        return vScroll;
    }

    /**
     * This method initializes hScroll  
     *  
     * @return javax.swing.JScrollBar   
     */
    private BHScrollBar getHScroll() {
        if (hScroll == null) {
            hScroll = new BHScrollBar();
            hScroll.setPreferredSize(new Dimension(48, 17));
        }
        return hScroll;
    }

    /**
     * This method initializes pictureBox3  
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getPictureBox3() {
        if (pictureBox3 == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.anchor = GridBagConstraints.EAST;
            gridBagConstraints12.gridy = 0;
            gridBagConstraints12.weightx = 1.0D;
            gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints12.insets = new Insets(0, 0, 0, 2);
            gridBagConstraints12.gridx = 0;
            pictureBox3 = new BPanel();
            pictureBox3.setLayout(new GridBagLayout());
            pictureBox3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            pictureBox3.setPreferredSize(new Dimension(68, 16));
            pictureBox3.setPreferredSize(new Dimension(68, 0));
            pictureBox3.setBackground(Color.lightGray);
            pictureBox3.add(getPictKeyLengthSplitter(), gridBagConstraints12);
        }
        return pictureBox3;
    }

    /**
     * This method initializes trackBar 
     *  
     * @return javax.swing.JSlider  
     */
    private BSlider getTrackBar() {
        if (trackBar == null) {
            trackBar = new BSlider();
            trackBar.setPreferredSize(new Dimension(98, 17));
            trackBar.setMinimum(17);
            trackBar.setMaximum(609);
        }
        return trackBar;
    }

    /**
     * This method initializes pictKeyLengthSplitter 
     *  
     * @return javax.swing.BButton  
     */
    private BButton getPictKeyLengthSplitter() {
        if (pictKeyLengthSplitter == null) {
            pictKeyLengthSplitter = new BButton();
            pictKeyLengthSplitter.setPreferredSize(new Dimension(16, 16));
            pictKeyLengthSplitter.setPreferredSize(new Dimension(16, 16));
        }
        return pictKeyLengthSplitter;
    }

    /**
     * This method initializes picturePositionIndicator 
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getPicturePositionIndicator() {
        if (picturePositionIndicator == null) {
            picturePositionIndicator = new BPanel();
            picturePositionIndicator.setLayout(new GridBagLayout());
            picturePositionIndicator.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            picturePositionIndicator.setPreferredSize(new Dimension(421, 48));
            picturePositionIndicator.setBackground(new Color(169, 169, 169));
        }
        return picturePositionIndicator;
    }

    /**
     * This method initializes jPanel1  
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.fill = GridBagConstraints.BOTH;
            gridBagConstraints16.weighty = 1.0D;
            gridBagConstraints16.gridy = 2;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.weightx = 1.0D;
            gridBagConstraints15.weighty = 0.0D;
            gridBagConstraints15.gridy = 3;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.weightx = 1.0D;
            gridBagConstraints14.gridy = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.anchor = GridBagConstraints.NORTH;
            gridBagConstraints13.weightx = 1.0D;
            gridBagConstraints13.gridy = 0;
            jPanel1 = new BPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jPanel1.add(getPanel3(), gridBagConstraints13);
            jPanel1.add(getPicturePositionIndicator(), gridBagConstraints14);
            jPanel1.add(getPanel1(), gridBagConstraints15);
            jPanel1.add(getPanel21(), gridBagConstraints16);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel3  
     *  
     * @return javax.swing.BPanel   
     */
    private BPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints.gridy = 0;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.gridx = 0;
            statusLabel = new BLabel();
            statusLabel.setText("");
            statusLabel.setPreferredSize(new Dimension(10, 27));
            jPanel3 = new BPanel();
            jPanel3.setPreferredSize(new Dimension(10, 27));
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jPanel3.add(statusLabel, gridBagConstraints);
        }
        return jPanel3;
    }

    /**
     * This method initializes menuHidden	
     * 	
     * @return javax.swing.JMenu	
     */
    private BMenu getMenuHidden() {
        if (menuHidden == null) {
            menuHidden = new BMenu();
            menuHidden.setText("Hidden");
            menuHidden.add(getMenuHiddenEditLyric());
            menuHidden.add(getMenuHiddenEditFlipToolPointerPencil());
            menuHidden.add(getMenuHiddenEditFlipToolPointerEraser());
            menuHidden.add(getMenuHiddenVisualForwardParameter());
            menuHidden.add(getMenuHiddenVisualBackwardParameter());
            menuHidden.add(getMenuHiddenTrackNext());
            menuHidden.add(getMenuHiddenTrackBack());
            menuHidden.add(getMenuHiddenCopy());
            menuHidden.add(getMenuHiddenPaste());
            menuHidden.add(getMenuHiddenCut());
            menuHidden.add(getMenuHiddenSelectForward());
            menuHidden.add(getMenuHiddenSelectBackward());
            menuHidden.add(getMenuHiddenMoveUp());
            menuHidden.add(getMenuHiddenMoveDown());
            menuHidden.add(getMenuHiddenMoveLeft());
            menuHidden.add(getMenuHiddenMoveRight());
            menuHidden.add(getMenuHiddenLengthen());
            menuHidden.add(getMenuHiddenShorten());
            menuHidden.add(getMenuHiddenGoToStartMarker());
            menuHidden.add(getMenuHiddenGoToEndMarker());
            menuHidden.add(getMenuHiddenPlayFromStartMarker());
            menuHidden.add(getMenuHiddenFlipCurveOnPianorollMode());
            menuHidden.add(getMenuHiddenPrintPoToCSV());
        }
        return menuHidden;
    }

    /**
     * This method initializes menuHiddenEditLyric	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenEditLyric() {
        if (menuHiddenEditLyric == null) {
            menuHiddenEditLyric = new BMenuItem();
            menuHiddenEditLyric.setText("Start Lyric Input");
        }
        return menuHiddenEditLyric;
    }

    /**
     * This method initializes menuHiddenEditFlipToolPointerPencil	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenEditFlipToolPointerPencil() {
        if (menuHiddenEditFlipToolPointerPencil == null) {
            menuHiddenEditFlipToolPointerPencil = new BMenuItem();
            menuHiddenEditFlipToolPointerPencil.setText("Change Tool Pointer / Pencil");
        }
        return menuHiddenEditFlipToolPointerPencil;
    }

    /**
     * This method initializes menuHiddenEditFlipToolPointerEraser	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenEditFlipToolPointerEraser() {
        if (menuHiddenEditFlipToolPointerEraser == null) {
            menuHiddenEditFlipToolPointerEraser = new BMenuItem();
            menuHiddenEditFlipToolPointerEraser.setText("Change Tool Pointer/ Eraser");
        }
        return menuHiddenEditFlipToolPointerEraser;
    }

    /**
     * This method initializes menuHiddenVisualForwardParameter	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenVisualForwardParameter() {
        if (menuHiddenVisualForwardParameter == null) {
            menuHiddenVisualForwardParameter = new BMenuItem();
            menuHiddenVisualForwardParameter.setText("Next Control Curve");
        }
        return menuHiddenVisualForwardParameter;
    }

    /**
     * This method initializes menuHiddenVisualBackwardParameter	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenVisualBackwardParameter() {
        if (menuHiddenVisualBackwardParameter == null) {
            menuHiddenVisualBackwardParameter = new BMenuItem();
            menuHiddenVisualBackwardParameter.setText("Previous Control Curve");
        }
        return menuHiddenVisualBackwardParameter;
    }

    /**
     * This method initializes menuHiddenTrackNext	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenTrackNext() {
        if (menuHiddenTrackNext == null) {
            menuHiddenTrackNext = new BMenuItem();
            menuHiddenTrackNext.setText("Next Track");
        }
        return menuHiddenTrackNext;
    }

    /**
     * This method initializes menuHiddenTrackBack	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenTrackBack() {
        if (menuHiddenTrackBack == null) {
            menuHiddenTrackBack = new BMenuItem();
            menuHiddenTrackBack.setText("Previous Track");
        }
        return menuHiddenTrackBack;
    }

    /**
     * This method initializes menuHiddenCopy	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenCopy() {
        if (menuHiddenCopy == null) {
            menuHiddenCopy = new BMenuItem();
            menuHiddenCopy.setText("Copy");
        }
        return menuHiddenCopy;
    }

    /**
     * This method initializes menuHiddenPaste	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenPaste() {
        if (menuHiddenPaste == null) {
            menuHiddenPaste = new BMenuItem();
            menuHiddenPaste.setText("Paste");
        }
        return menuHiddenPaste;
    }

    /**
     * This method initializes menuHiddenCut	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHiddenCut() {
        if (menuHiddenCut == null) {
            menuHiddenCut = new BMenuItem();
            menuHiddenCut.setText("Cut");
        }
        return menuHiddenCut;
    }

    /**
     * This method initializes menuTrackRendererVOCALOID1	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuTrackRendererVOCALOID1() {
        if (menuTrackRendererVOCALOID1 == null) {
            menuTrackRendererVOCALOID1 = new BMenuItem();
            menuTrackRendererVOCALOID1.setText("VOCALOID1");
        }
        return menuTrackRendererVOCALOID1;
    }

    /**
     * This method initializes menuTrackRendererVOCALOID2	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuTrackRendererVOCALOID2() {
        if (menuTrackRendererVOCALOID2 == null) {
            menuTrackRendererVOCALOID2 = new BMenuItem();
            menuTrackRendererVOCALOID2.setText("VOCALOID2");
        }
        return menuTrackRendererVOCALOID2;
    }

    /**
     * This method initializes menuTrackRendererUtau	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenu getMenuTrackRendererUtau() {
        if (menuTrackRendererUtau == null) {
            menuTrackRendererUtau = new BMenu();
            menuTrackRendererUtau.setText("UTAU");
        }
        return menuTrackRendererUtau;
    }

    /**
     * This method initializes menuTrackRendererVCNT	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuTrackRendererVCNT() {
        if (menuTrackRendererVCNT == null) {
            menuTrackRendererVCNT = new BMenuItem();
            menuTrackRendererVCNT.setText("vConnect-STAND");
        }
        return menuTrackRendererVCNT;
    }

    /**
     * This method initializes menuSettingGameControlerSetting	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuSettingGameControlerSetting() {
        if (menuSettingGameControlerSetting == null) {
            menuSettingGameControlerSetting = new BMenuItem();
            menuSettingGameControlerSetting.setText("Setting(&S)");
        }
        return menuSettingGameControlerSetting;
    }

    /**
     * This method initializes menuSettingGameControlerLoad	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuSettingGameControlerLoad() {
        if (menuSettingGameControlerLoad == null) {
            menuSettingGameControlerLoad = new BMenuItem();
            menuSettingGameControlerLoad.setText("Load(&L)");
        }
        return menuSettingGameControlerLoad;
    }

    /**
     * This method initializes menuSettingGameControlerRemove	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuSettingGameControlerRemove() {
        if (menuSettingGameControlerRemove == null) {
            menuSettingGameControlerRemove = new BMenuItem();
            menuSettingGameControlerRemove.setText("Remove(&R)");
        }
        return menuSettingGameControlerRemove;
    }

    /**
     * This method initializes menuHelpDebug	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private BMenuItem getMenuHelpDebug() {
        if (menuHelpDebug == null) {
            menuHelpDebug = new BMenuItem();
        }
        return menuHelpDebug;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private BPanel getJPanel2() {
        if (pictureBox2 == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.gridx = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridy = 2;
            pictureBox2 = new BPanel();
            pictureBox2.setLayout(new GridBagLayout());
            pictureBox2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            pictureBox2.setPreferredSize(new Dimension(15, 32));
            pictureBox2.add(getButtonVMooz(), gridBagConstraints8);
            pictureBox2.add(getVScroll(), gridBagConstraints3);
            pictureBox2.add(getButtonVZoom(), gridBagConstraints4);
        }
        return pictureBox2;
    }

    /**
     * This method initializes menuVisualPluginUi	
     * 	
     * @return javax.swing.JMenu	
     */
    private BMenu getMenuVisualPluginUi() {
        if (menuVisualPluginUi == null) {
            menuVisualPluginUi = new BMenu();
            menuVisualPluginUi.setText("VSTi Plugin UI");
            menuVisualPluginUi.add(getMenuVisualPluginUiVocaloid1());
            menuVisualPluginUi.add(getMenuVisualPluginUiVocaloid2());
            menuVisualPluginUi.add(getMenuVisualPluginUiAquesTone());
        }
        return menuVisualPluginUi;
    }

    /**
     * This method initializes menuVisualPluginUiVocaloid1	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuVisualPluginUiVocaloid1() {
        if (menuVisualPluginUiVocaloid1 == null) {
            menuVisualPluginUiVocaloid1 = new BMenuItem();
            menuVisualPluginUiVocaloid1.setText("VOCALOID1 [1.0]");
        }
        return menuVisualPluginUiVocaloid1;
    }

    /**
     * This method initializes menuVisualPluginUiVocaloid2	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuVisualPluginUiVocaloid2() {
        if (menuVisualPluginUiVocaloid2 == null) {
            menuVisualPluginUiVocaloid2 = new BMenuItem();
            menuVisualPluginUiVocaloid2.setText("VOCALOID2");
        }
        return menuVisualPluginUiVocaloid2;
    }

    /**
     * This method initializes menuVisualPluginUiAquesTone	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuVisualPluginUiAquesTone() {
        if (menuVisualPluginUiAquesTone == null) {
            menuVisualPluginUiAquesTone = new BMenuItem();
            menuVisualPluginUiAquesTone.setText("AquesTone");
        }
        return menuVisualPluginUiAquesTone;
    }

    /**
     * This method initializes menuHiddenSelectForward	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenSelectForward() {
        if (menuHiddenSelectForward == null) {
            menuHiddenSelectForward = new BMenuItem();
            menuHiddenSelectForward.setText("Select Forward");
        }
        return menuHiddenSelectForward;
    }

    /**
     * This method initializes menuHiddenSelectBackward	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenSelectBackward() {
        if (menuHiddenSelectBackward == null) {
            menuHiddenSelectBackward = new BMenuItem();
            menuHiddenSelectBackward.setText("Select Backward");
        }
        return menuHiddenSelectBackward;
    }

    /**
     * This method initializes menuHiddenMoveUp	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenMoveUp() {
        if (menuHiddenMoveUp == null) {
            menuHiddenMoveUp = new BMenuItem();
            menuHiddenMoveUp.setText("Move Up");
        }
        return menuHiddenMoveUp;
    }

    /**
     * This method initializes menuHiddenMoveDown	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenMoveDown() {
        if (menuHiddenMoveDown == null) {
            menuHiddenMoveDown = new BMenuItem();
            menuHiddenMoveDown.setText("Move Down");
        }
        return menuHiddenMoveDown;
    }

    /**
     * This method initializes menuHiddenMoveLeft	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenMoveLeft() {
        if (menuHiddenMoveLeft == null) {
            menuHiddenMoveLeft = new BMenuItem();
            menuHiddenMoveLeft.setText("Move Left");
        }
        return menuHiddenMoveLeft;
    }

    /**
     * This method initializes menuHiddenMoveRight	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenMoveRight() {
        if (menuHiddenMoveRight == null) {
            menuHiddenMoveRight = new BMenuItem();
            menuHiddenMoveRight.setText("Move Right");
        }
        return menuHiddenMoveRight;
    }

    /**
     * This method initializes menuHiddenLengthen	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenLengthen() {
        if (menuHiddenLengthen == null) {
            menuHiddenLengthen = new BMenuItem();
            menuHiddenLengthen.setText("Lengthen");
        }
        return menuHiddenLengthen;
    }

    /**
     * This method initializes menuHiddenShorten	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenShorten() {
        if (menuHiddenShorten == null) {
            menuHiddenShorten = new BMenuItem();
            menuHiddenShorten.setText("Shorten");
        }
        return menuHiddenShorten;
    }

    /**
     * This method initializes menuHiddenGoToStartMarker	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenGoToStartMarker() {
        if (menuHiddenGoToStartMarker == null) {
            menuHiddenGoToStartMarker = new BMenuItem();
            menuHiddenGoToStartMarker.setText("GoTo Start Marker");
        }
        return menuHiddenGoToStartMarker;
    }

    /**
     * This method initializes menuHiddenGoToEndMarker	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHiddenGoToEndMarker() {
        if (menuHiddenGoToEndMarker == null) {
            menuHiddenGoToEndMarker = new BMenuItem();
            menuHiddenGoToEndMarker.setText("GoTo End Marker");
        }
        return menuHiddenGoToEndMarker;
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel22() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return jPanel2;
    }

    /**
     * This method initializes cMenuPositionIndicator	
     * 	
     * @return org.kbinani.windows.forms.BPopupMenu	
     */
    private BPopupMenu getCMenuPositionIndicator() {
        if (cMenuPositionIndicator == null) {
            cMenuPositionIndicator = new BPopupMenu();
            cMenuPositionIndicator.add(getCMenuPositionIndicatorStartMarker());
            cMenuPositionIndicator.add(getCMenuPositionIndicatorEndMarker());
        }
        return cMenuPositionIndicator;
    }

    /**
     * This method initializes cMenuPositionIndicatorStartMarker	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getCMenuPositionIndicatorStartMarker() {
        if (cMenuPositionIndicatorStartMarker == null) {
            cMenuPositionIndicatorStartMarker = new BMenuItem();
            cMenuPositionIndicatorStartMarker.setText("Set start marker");
        }
        return cMenuPositionIndicatorStartMarker;
    }

    /**
     * This method initializes cMenuPositionIndicatorEndMarker	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getCMenuPositionIndicatorEndMarker() {
        if (cMenuPositionIndicatorEndMarker == null) {
            cMenuPositionIndicatorEndMarker = new BMenuItem();
            cMenuPositionIndicatorEndMarker.setText("Set end marker");
        }
        return cMenuPositionIndicatorEndMarker;
    }

    /**
     * This method initializes buttonVZoom	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getButtonVZoom() {
        if (buttonVZoom == null) {
            buttonVZoom = new BButton();
            buttonVZoom.setPreferredSize(new Dimension(15, 16));
            buttonVZoom.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        }
        return buttonVZoom;
    }

    /**
     * This method initializes buttonVMooz	
     * 	
     * @return org.kbinani.windows.forms.BButton	
     */
    private BButton getButtonVMooz() {
        if (buttonVMooz == null) {
            buttonVMooz = new BButton();
            buttonVMooz.setPreferredSize(new Dimension(15, 16));
        }
        return buttonVMooz;
    }

    /**
     * This method initializes panel2	
     * 	
     * @return org.kbinani.windows.forms.BPanel	
     */
    private WaveformZoomUiImpl getPanel2() {
        if (panelWaveformZoom == null) {
            panelWaveformZoom = new WaveformZoomUiImpl();
            panelWaveformZoom.setLayout(new GridBagLayout());
            panelWaveformZoom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panelWaveformZoom.setPreferredSize(new Dimension(68, 4));
        }
        return panelWaveformZoom;
    }

    /**
     * This method initializes waveView	
     * 	
     * @return org.kbinani.windows.forms.BPanel	
     */
    private WaveView getWaveView() {
        if (waveView == null) {
            waveView = new WaveView();
            waveView.setLayout(new GridBagLayout());
            waveView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
        return waveView;
    }

    /**
     * This method initializes menuWindow	
     * 	
     * @return org.kbinani.windows.forms.BMenu	
     */
    private BMenu getMenuWindow() {
        if (menuWindow == null) {
            menuWindow = new BMenu();
            menuWindow.setText("Window");
            menuWindow.add(getMenuWindowMinimize());
        }
        return menuWindow;
    }

    /**
     * This method initializes menuWindowMinimize	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuWindowMinimize() {
        if (menuWindowMinimize == null) {
            menuWindowMinimize = new BMenuItem();
            menuWindowMinimize.setText("Minimize");
        }
        return menuWindowMinimize;
    }

    /**
     * This method initializes panel21	
     * 	
     * @return org.kbinani.windows.forms.BPanel	
     */
    private BPanel getPanel21() {
        if (panel21 == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = GridBagConstraints.BOTH;
            gridBagConstraints7.gridwidth = 1;
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.weightx = 1.0D;
            gridBagConstraints7.weighty = 1.0D;
            gridBagConstraints7.gridheight = 1;
            panel21 = new BPanel();
            panel21.setLayout(new GridBagLayout());
            panel21.setPreferredSize(new Dimension(68, 4));
            panel21.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel21.add(getPictPianoRoll(), gridBagConstraints7);
            panel21.add(getJPanel2(), gridBagConstraints1);
        }
        return panel21;
    }

    /**
     * This method initializes menuHelpManual	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuHelpManual() {
        if (menuHelpManual == null) {
            menuHelpManual = new BMenuItem();
            menuHelpManual.setText("Manual (PDF)");
        }
        return menuHelpManual;
    }

    /**
     * This method initializes menuFileRecentClear	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuFileRecentClear() {
        if (menuFileRecentClear == null) {
            menuFileRecentClear = new BMenuItem();
            menuFileRecentClear.setText("Clear Menu");
        }
        return menuFileRecentClear;
    }

    /**
     * This method initializes menuLyricApplyUtauParameters	
     * 	
     * @return org.kbinani.windows.forms.BMenuItem	
     */
    private BMenuItem getMenuLyricApplyUtauParameters() {
        if (menuLyricApplyUtauParameters == null) {
            menuLyricApplyUtauParameters = new BMenuItem();
            menuLyricApplyUtauParameters.setText("Apply UTAU Parameters");
        }
        return menuLyricApplyUtauParameters;
    }


    }


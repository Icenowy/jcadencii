/*
 * EditorConfig.cs
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

import java.awt.*;
import java.util.*;
import java.io.*;
import org.kbinani.*;
import org.kbinani.vsq.*;
import org.kbinani.xml.*;
import org.kbinani.windows.forms.*;


    /// <summary>
    /// Cadenciiの環境設定
    /// </summary>
    public class EditorConfig
    {
        /// <summary>
        /// デフォルトで使用する歌手の名前
        /// </summary>
        public String DefaultSingerName = "Miku";
        /// <summary>
        /// デフォルトの横軸方向のスケール
        /// </summary>
        public int DefaultXScale = 65;
        public String BaseFontName = "MS UI Gothic";
        public float BaseFontSize = 9.0f;
        public String ScreenFontName = "MS UI Gothic";
        public int WheelOrder = 20;
        public boolean CursorFixed = false;
        /// <summary>
        /// RecentFilesに登録することの出来る最大のファイル数
        /// </summary>
        public int NumRecentFiles = 5;
        /// <summary>
        /// 最近使用したファイルのリスト
        /// </summary>
        @XmlGenericType( String.class )
        public Vector<String> RecentFiles = new Vector<String>();
        public int DefaultPMBendDepth = 8;
        public int DefaultPMBendLength = 0;
        public int DefaultPMbPortamentoUse = 3;
        public int DefaultDEMdecGainRate = 50;
        public int DefaultDEMaccent = 50;
        /// <summary>
        /// ピアノロール上に歌詞を表示するかどうか
        /// </summary>
        public boolean ShowLyric = true;
        /// <summary>
        /// ピアノロール上に，ビブラートとアタックの概略を表す波線を表示するかどうか
        /// </summary>
        public boolean ShowExpLine = true;
        public DefaultVibratoLengthEnum DefaultVibratoLength = DefaultVibratoLengthEnum.L66;
        /// <summary>
        /// デフォルトビブラートのRate
        /// バージョン3.3で廃止
        /// </summary>
        private int __revoked__DefaultVibratoRate = 64;
        /// <summary>
        /// デフォルトビブラートのDepth
        /// バージョン3.3で廃止
        /// </summary>
        private int __revoked__DefaultVibratoDepth = 64;
        /// <summary>
        /// ビブラートの自動追加を行うかどうかを決める音符長さの閾値．単位はclock
        /// <version>3.3+</version>
        /// </summary>
        public int AutoVibratoThresholdLength = 480;
        /// <summary>
        /// VOCALOID1用のデフォルトビブラート設定
        /// </summary>
        public String AutoVibratoType1 = "$04040001";
        /// <summary>
        /// VOCALOID2用のデフォルトビブラート設定
        /// </summary>
        public String AutoVibratoType2 = "$04040001";
        /// <summary>
        /// カスタムのデフォルトビブラート設定
        /// <version>3.3+</version>
        /// </summary>
        public String AutoVibratoTypeCustom = "$04040001";
        /// <summary>
        /// ユーザー定義のビブラート設定．
        /// <version>3.3+</version>
        /// </summary>
        @XmlGenericType( VibratoHandle.class )
        public Vector<VibratoHandle> AutoVibratoCustom = new Vector<VibratoHandle>();
        /// <summary>
        /// ビブラートの自動追加を行うかどうか
        /// </summary>
        public boolean EnableAutoVibrato = true;
        /// <summary>
        /// ピアノロール上での，音符の表示高さ(ピクセル)
        /// </summary>
        public int PxTrackHeight = 14;
        public int MouseDragIncrement = 50;
        public int MouseDragMaximumRate = 600;
        /// <summary>
        /// ミキサーウィンドウが表示された状態かどうか
        /// </summary>
        public boolean MixerVisible = false;
        /// <summary>
        /// アイコンパレットが表示された状態かどうか
        /// <version>3.3+</version>
        /// </summary>
        public boolean IconPaletteVisible = false;
        public int PreSendTime = 500;
        public ClockResolution ControlCurveResolution = ClockResolution.L30;
        /// <summary>
        /// 言語設定
        /// </summary>
        public String Language = "";
        /// <summary>
        /// マウスの操作などの許容範囲。プリメジャーにPxToleranceピクセルめり込んだ入力を行っても、エラーにならない。(補正はされる)
        /// </summary>
        public int PxTolerance = 10;
        /// <summary>
        /// マウスホイールでピアノロールを水平方向にスクロールするかどうか。
        /// </summary>
        public boolean ScrollHorizontalOnWheel = true;
        /// <summary>
        /// 画面描画の最大フレームレート
        /// </summary>
        public int MaximumFrameRate = 15;
        /// <summary>
        /// ユーザー辞書のOn/Offと順序
        /// </summary>
        @XmlGenericType( String.class )
        public Vector<String> UserDictionaries = new Vector<String>();
        /// <summary>
        /// 実行環境
        /// </summary>
        private PlatformEnum __revoked__Platform = PlatformEnum.Windows;
        /// <summary>
        /// ウィンドウが最大化された状態かどうか
        /// </summary>
        public boolean WindowMaximized = false;
        /// <summary>
        /// ウィンドウの位置とサイズ．
        /// 最小化された状態での値は，この変数に代入されない(ことになっている)
        /// </summary>
        public Rectangle WindowRect = new Rectangle( 0, 0, 970, 718 );
        /// <summary>
        /// hScrollのスクロールボックスの最小幅(px)
        /// </summary>
        public int MinimumScrollHandleWidth = 20;
        /// <summary>
        /// 発音記号入力モードを，維持するかどうか
        /// </summary>
        public boolean KeepLyricInputMode = false;
        /// <summary>
        /// ピアノロールの何もないところをクリックした場合、右クリックでもプレビュー音を再生するかどうか
        /// </summary>
        public boolean PlayPreviewWhenRightClick = false;
        /// <summary>
        /// ゲームコントローラで、異なるイベントと識別する最小の時間間隔(millisec)
        /// </summary>
        public int GameControlerMinimumEventInterval = 100;
        /// <summary>
        /// カーブの選択範囲もクオンタイズするかどうか
        /// </summary>
        public boolean CurveSelectingQuantized = true;

        private QuantizeMode m_position_quantize = QuantizeMode.p32;
        private boolean m_position_quantize_triplet = false;
        private QuantizeMode m_length_quantize = QuantizeMode.p32;
        private boolean m_length_quantize_triplet = false;
        private int m_mouse_hover_time = 500;
        /// <summary>
        /// Button index of "△"
        /// </summary>
        public int GameControlerTriangle = 0;
        /// <summary>
        /// Button index of "○"
        /// </summary>
        public int GameControlerCircle = 1;
        /// <summary>
        /// Button index of "×"
        /// </summary>
        public int GameControlerCross = 2;
        /// <summary>
        /// Button index of "□"
        /// </summary>
        public int GameControlerRectangle = 3;
        /// <summary>
        /// Button index of "L1"
        /// </summary>
        public int GameControlL1 = 4;
        /// <summary>
        /// Button index of "R1"
        /// </summary>
        public int GameControlR1 = 5;
        /// <summary>
        /// Button index of "L2"
        /// </summary>
        public int GameControlL2 = 6;
        /// <summary>
        /// Button index of "R2"
        /// </summary>
        public int GameControlR2 = 7;
        /// <summary>
        /// Button index of "SELECT"
        /// </summary>
        public int GameControlSelect = 8;
        /// <summary>
        /// Button index of "START"
        /// </summary>
        public int GameControlStart = 9;
        /// <summary>
        /// Button index of Left Stick
        /// </summary>
        public int GameControlStirckL = 10;
        /// <summary>
        /// Button index of Right Stick
        /// </summary>
        public int GameControlStirckR = 11;
        public boolean CurveVisibleVelocity = true;
        public boolean CurveVisibleAccent = true;
        public boolean CurveVisibleDecay = true;
        public boolean CurveVisibleVibratoRate = true;
        public boolean CurveVisibleVibratoDepth = true;
        public boolean CurveVisibleDynamics = true;
        public boolean CurveVisibleBreathiness = true;
        public boolean CurveVisibleBrightness = true;
        public boolean CurveVisibleClearness = true;
        public boolean CurveVisibleOpening = true;
        public boolean CurveVisibleGendorfactor = true;
        public boolean CurveVisiblePortamento = true;
        public boolean CurveVisiblePit = true;
        public boolean CurveVisiblePbs = true;
        public boolean CurveVisibleHarmonics = false;
        public boolean CurveVisibleFx2Depth = false;
        public boolean CurveVisibleReso1 = false;
        public boolean CurveVisibleReso2 = false;
        public boolean CurveVisibleReso3 = false;
        public boolean CurveVisibleReso4 = false;
        public boolean CurveVisibleEnvelope = false;
        public int GameControlPovRight = 9000;
        public int GameControlPovLeft = 27000;
        public int GameControlPovUp = 0;
        public int GameControlPovDown = 18000;
        /// <summary>
        /// wave波形を表示するかどうか
        /// </summary>
        public boolean ViewWaveform = false;
        /// <summary>
        /// スプリットコンテナのディバイダの位置
        /// <version>3.3+</version>
        /// </summary>
        public int SplitContainer2LastDividerLocation = -1;
        /// <summary>
        /// キーボードからの入力に使用するデバイス
        /// </summary>
        public MidiPortConfig MidiInPort = new MidiPortConfig();

        public boolean ViewAtcualPitch = false;
        private boolean __revoked__InvokeUtauCoreWithWine = false;
        @XmlGenericType( SingerConfig.class )
        public Vector<SingerConfig> UtauSingers = new Vector<SingerConfig>();
        /// <summary>
        /// UTAU互換の合成器のパス(1個目)
        /// </summary>
        public String PathResampler = "";
        /// <summary>
        /// UTAU互換の合成器の1個目を，wine経由で呼ぶかどうか
        /// version 3.3+
        /// </summary>
        public boolean ResamplerWithWine = false;
        /// <summary>
        /// UTAU互換の合成器のパス(2個目以降)
        /// </summary>
        @XmlGenericType( String.class )
        public Vector<String> PathResamplers = new Vector<String>();
        /// <summary>
        /// UTAU互換の合成器を，wine経由で呼ぶかどうか
        /// version 3.3+
        /// </summary>
        @XmlGenericType( Boolean.class )
        public Vector<Boolean> ResamplersWithWine = new Vector<Boolean>();
        /// <summary>
        /// UTAU用のwave切り貼りツール
        /// </summary>
        public String PathWavtool = "";
        /// <summary>
        /// wavtoolをwine経由で呼ぶかどうか
        /// version 3.3+
        /// </summary>
        public boolean WavtoolWithWine = false;
        /// <summary>
        /// ベジエ制御点を掴む時の，掴んだと判定する際の誤差．制御点の外輪からPxToleranceBezierピクセルずれていても，掴んだと判定する．
        /// </summary>
        public int PxToleranceBezier = 10;
        /// <summary>
        /// 歌詞入力においてローマ字が入力されたとき，Cadencii側でひらがなに変換するかどうか
        /// </summary>
        public boolean SelfDeRomanization = false;
        /// <summary>
        /// openMidiDialogで最後に選択された拡張子
        /// </summary>
        public String LastUsedExtension = ".vsq";
        /// <summary>
        /// ミキサーダイアログを常に手前に表示するかどうか
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__MixerTopMost = true;
        @XmlGenericType( ValuePairOfStringArrayOfKeys.class )
        public Vector<ValuePairOfStringArrayOfKeys> ShortcutKeys = new Vector<ValuePairOfStringArrayOfKeys>();
        public PropertyPanelState PropertyWindowStatus = new PropertyPanelState();
        /// <summary>
        /// 概観ペインが表示されているかどうか
        /// </summary>
        public boolean OverviewEnabled = false;
        public int OverviewScaleCount = 5;
        public FormMidiImExportConfig MidiImExportConfigExport = new FormMidiImExportConfig();
        public FormMidiImExportConfig MidiImExportConfigImport = new FormMidiImExportConfig();
        public FormMidiImExportConfig MidiImExportConfigImportVsq = new FormMidiImExportConfig();
        /// <summary>
        /// 自動バックアップする間隔．単位は分
        /// </summary>
        public int AutoBackupIntervalMinutes = 10;
        /// <summary>
        /// 鍵盤の表示幅、ピクセル,AppManager.keyWidthに代入。
        /// </summary>
        public int KeyWidth = 136;
        /// <summary>
        /// スペースキーを押しながら左クリックで、中ボタンクリックとみなす動作をさせるかどうか。
        /// </summary>
        public boolean UseSpaceKeyAsMiddleButtonModifier = false;
        /// <summary>
        /// AquesToneのVSTi dllへのパス
        /// </summary>
        public String PathAquesTone = "";
        /// <summary>
        /// アイコンパレット・ウィンドウの位置
        /// </summary>
        public XmlPoint FormIconPaletteLocation = new XmlPoint( 0, 0 );
        /// <summary>
        /// アイコンパレット・ウィンドウを常に手前に表示するかどうか
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__FormIconTopMost = true;
        /// <summary>
        /// 最初に戻る、のショートカットキー
        /// </summary>
        public BKeys[] SpecialShortcutGoToFirst = new BKeys[] { BKeys.Home };
        /// <summary>
        /// waveファイル出力時のチャンネル数（1または2）
        /// 3.3で廃止
        /// </summary>
        private int __revoked__WaveFileOutputChannel = 2;
        /// <summary>
        /// waveファイル出力時に、全トラックをmixして出力するかどうか
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__WaveFileOutputFromMasterTrack = false;
        /// <summary>
        /// MTCスレーブ動作を行う際使用するMIDI INポートの設定
        /// </summary>
        public MidiPortConfig MidiInPortMtc = new MidiPortConfig();
        /// <summary>
        /// プロジェクトごとのキャッシュディレクトリを使うかどうか
        /// </summary>
        public boolean UseProjectCache = true;
        /// <summary>
        /// 鍵盤用のキャッシュが無いとき、FormGenerateKeySoundを表示しないかどうか。
        /// trueなら表示しない、falseなら表示する（デフォルト）
        /// </summary>
        public boolean DoNotAskKeySoundGeneration = false;
        /// <summary>
        /// VOCALOID1 (1.0)のDLLを読み込まない場合true。既定ではfalse
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__DoNotUseVocaloid100 = false;
        /// <summary>
        /// VOCALOID1 (1.1)のDLLを読み込まない場合true。既定ではfalse
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__DoNotUseVocaloid101 = false;
        /// <summary>
        /// VOCALOID2のDLLを読み込まない場合true。既定ではfalse
        /// </summary>
        public boolean DoNotUseVocaloid2 = false;
        /// <summary>
        /// AquesToneのDLLを読み込まない場合true。既定ではfalse
        /// </summary>
        public boolean DoNotUseAquesTone = false;
        /// <summary>
        /// 2個目のVOCALOID1 DLLを読み込むかどうか。既定ではfalse
        /// 3.3で廃止
        /// </summary>
        private boolean __revoked__LoadSecondaryVocaloid1Dll = false;
        /// <summary>
        /// VOALOID1のDLLを読み込まない場合はtrue．既定ではfalse
        /// </summary>
        public boolean DoNotUseVocaloid1 = false;
        /// <summary>
        /// WAVE再生時のバッファーサイズ。既定では1000ms。
        /// </summary>
        public int BufferSizeMilliSeconds = 1000;
        /// <summary>
        /// トラックを新規作成するときのデフォルトの音声合成システム
        /// </summary>
        public RendererKind DefaultSynthesizer
= RendererKind.VOCALOID2;
        /// <summary>
        /// 自動ビブラートを作成するとき，ユーザー定義タイプのビブラートを利用するかどうか．デフォルトではfalse
        /// </summary>
        public boolean UseUserDefinedAutoVibratoType = false;
        /// <summary>
        /// 再生中に画面を描画するかどうか。デフォルトはfalse
        /// <version>3.3+</version>
        /// </summary>
        public boolean SkipDrawWhilePlaying = false;
        /// <summary>
        /// ピアノロール画面の縦方向のスケール.
        /// <verssion>3.3+</verssion>
        /// </summary>
        public int PianoRollScaleY = 0;
        /// <summary>
        /// ファイル・ツールバーのサイズ
        /// <version>3.3+</version>
        /// </summary>
        public int BandSizeFile = 236;
        /// <summary>
        /// ツール・ツールバーのサイズ
        /// <version>3.3+</version>
        /// </summary>
        public int BandSizeTool = 712;
        /// <summary>
        /// メジャー・ツールバーのサイズ
        /// <version>3.3+</version>
        /// </summary>
        public int BandSizeMeasure = 714;
        /// <summary>
        /// ポジション・ツールバーのサイズ
        /// <version>3.3+</version>
        /// </summary>
        public int BandSizePosition = 234;
        /// <summary>
        /// ファイル・ツールバーを新しい行に追加するかどうか
        /// <version>3.3+</version>
        /// </summary>
        public boolean BandNewRowFile = false;
        /// <summary>
        /// ツール・ツールバーを新しい行に追加するかどうか
        /// <version>3.3+</version>
        /// </summary>
        public boolean BandNewRowTool = false;
        /// <summary>
        /// メジャー・ツールバーを新しい行に追加するかどうか
        /// <version>3.3+</version>
        /// </summary>
        public boolean BandNewRowMeasure = false;
        /// <summary>
        /// ポジション・ツールバーを新しい行に追加するかどうか
        /// <version>3.3+</version>
        /// </summary>
        public boolean BandNewRowPosition = true;
        /// <summary>
        /// ファイル・ツールバーの順番
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        public int BandOrderFile = 0;
        /// <summary>
        /// ツール・ツールバーの順番
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        public int BandOrderTool = 1;
        /// <summary>
        /// メジャー・ツールバーの順番
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        public int BandOrderMeasure = 3;
        /// <summary>
        /// ポジション・ツールバーの順番
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        public int BandOrderPosition = 2;
        /// <summary>
        /// ツールバーのChevronの幅．
        /// Winodws 7(Aero): 17px
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        public int ChevronWidth = 17;
        /// <summary>
        /// 最後に入力したファイルパスのリスト
        /// リストに入る文字列は，拡張子+タブ文字+パスの形式にする
        /// 拡張子はピリオドを含めない
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        @XmlGenericType( String.class )
        public Vector<String> LastUsedPathIn = new Vector<String>();
        /// <summary>
        /// 最後に出力したファイルパスのリスト
        /// リストに入る文字列は，拡張子+タブ文字+パスの形式にする
        /// 拡張子はピリオドを含めない
        /// <remarks>version 3.3+</remarks>
        /// </summary>
        @XmlGenericType( String.class )
        public Vector<String> LastUsedPathOut = new Vector<String>();
        /// <summary>
        /// 使用するWINEPREFIX
        /// version 3.3+
        /// </summary>
        public String WinePrefix = "~/Library/Application Support/MikuInstaller/prefix/default";
        /// <summary>
        /// wineのトップディレクトリ
        /// version 3.3+
        /// </summary>
        public String WineTop = "/Applications/MikuInstaller.app/Contents/Resources/Wine.bundle/Contents/SharedSupport";
        /// <summary>
        /// Cadencii付属のWineを使う場合にtrue，そうでなければWineTopで指定されたWineが利用される
        /// version 3.3+
        /// </summary>
        public boolean WineTopBuiltin = true;
        /// <summary>
        /// UTAUのresampler用に，ジャンクション機能を使うかどうか
        /// version 3.3+
        /// </summary>
        public boolean UseWideCharacterWorkaround = false;

        /// <summary>
        /// バッファーサイズに設定できる最大値
        /// </summary>
        public static final int MAX_BUFFER_MILLISEC = 1000;
        /// <summary>
        /// バッファーサイズに設定できる最小値
        /// </summary>
        public static final int MIN_BUFFER_MILLIXEC = 100;
        /// <summary>
        /// ピアノロールの縦軸の拡大率を表す整数値の最大値
        /// </summary>
        public static final int MAX_PIANOROLL_SCALEY = 10;
        /// <summary>
        /// ピアノロールの縦軸の拡大率を表す整数値の最小値
        /// </summary>
        public static final int MIN_PIANOROLL_SCALEY = -4;

        private static XmlSerializer s_serializer = new XmlSerializer( EditorConfig.class );

        /// <summary>
        /// PositionQuantize, PositionQuantizeTriplet, LengthQuantize, LengthQuantizeTripletの描くプロパティのいずれかが
        /// 変更された時発生します
        /// </summary>
        public static BEvent<BEventHandler> quantizeModeChangedEvent = new BEvent<BEventHandler>();

        /// <summary>
        /// コンストラクタ．起動したOSによって動作を変える場合がある
        /// </summary>
        public EditorConfig()
        {

// 言語設定を，システムのデフォルトの言語を用いる
String lang = "";
String name = System.getProperty( "user.language" );
if( name != null ){
    lang = name;
}
this.Language = lang;
        }

        /// <summary>
        /// EditorConfigのインスタンスをxmlシリアライズするためのシリアライザを取得します
        /// </summary>
        /// <returns></returns>
        public static XmlSerializer getSerializer()
        {
return s_serializer;
        }

        private static String getLastUsedPathCore( Vector<String> list, String extension )
        {
if ( extension == null ) return "";
if ( PortUtil.getStringLength( extension ) <= 0 ) return "";
if ( extension.equals( "." ) ) return "";

if ( extension.startsWith( "." ) ) {
    extension = str.sub( extension, 1 );
}

int c = list.size();
for ( int i = 0; i < c; i++ ) {
    String s = list.get( i );
    if ( s.startsWith( extension ) ) {
        String[] spl = PortUtil.splitString( s, '\t' );
        if ( spl.length >= 2 ) {
            return spl[1];
        }
        break;
    }
}
return "";
        }

        private static void setLastUsedPathCore( Vector<String> list, String path, String ext_with_dot )
        {
String extension = ext_with_dot;
if ( extension == null ) return;
if ( extension.equals( "." ) ) return;
if ( extension.startsWith( "." ) ) {
    extension = str.sub( extension, 1 );
}

int c = list.size();
String entry = extension + "\t" + path;
for ( int i = 0; i < c; i++ ) {
    String s = list.get( i );
    if ( s.startsWith( extension ) ) {
        list.set( i, entry );
        return;
    }
}
list.add( entry );
        }

        /// <summary>
        /// 音符イベントに，デフォルトの歌唱スタイルを適用します
        /// </summary>
        /// <param name="item"></param>
        public void applyDefaultSingerStyle( VsqID item )
        {
if ( item == null ) return;
item.PMBendDepth = this.DefaultPMBendDepth;
item.PMBendLength = this.DefaultPMBendLength;
item.PMbPortamentoUse = this.DefaultPMbPortamentoUse;
item.DEMdecGainRate = this.DefaultDEMdecGainRate;
item.DEMaccent = this.DefaultDEMaccent;
        }

        /// <summary>
        /// 使用するWineのインストールディレクトリを取得します
        /// </summary>
        public String getWineTop()
        {
if( WineTopBuiltin ){
    return getBuiltinWineTop( "Wine.bundle" );
}else{
    return WineTop;
}
        }
        
        /// <summary>
        /// 指定した名前のバンドルの，Wineのインストールディレクトリを取得します
        /// </summary>
        private String getBuiltinWineTop( String bundle_name )
        {
String appstart = PortUtil.getApplicationStartupPath();
// Wine.bundleの場所は../Wine.bundleまたは./Wine.bundleのどちらか
// まず../Wine.bundleがあるかどうかチェック
String parent = PortUtil.getDirectoryName( appstart );
String ret = fsys.combine( parent, bundle_name );
if( !fsys.isDirectoryExists( ret ) ){
    // ../Wine.bundleが無い場合
    ret = fsys.combine( appstart, bundle_name );
}
ret = fsys.combine( ret, "Contents" );
ret = fsys.combine( ret, "SharedSupport" );
return ret;
        }

        public String getBuiltinWineMinimumExecutable()
        {
String ret = getBuiltinWineTop( "WineMinimum.bundle" );
ret = fsys.combine( ret, "bin" );
ret = fsys.combine( ret, "wine" );
return ret;
        }

        /// <summary>
        /// wineの実行ファイルのパスを取得します
        /// </summary>
        public String getBuiltinWineExecutable_gettext()
        {
String ret = getBuiltinWineTop( "Wine.bundle" );
ret = fsys.combine( ret, "bin" );
ret = fsys.combine( ret, "wine" );
return ret;
        }

        /// <summary>
        /// 登録されているUTAU互換合成器の個数を調べます
        /// </summary>
        /// <returns></returns>
        public int getResamplerCount()
        {
int ret = PathResamplers.size();
if ( !PathResampler.equals( "" ) ) {
    ret++;
}
return ret;
        }

        /// <summary>
        /// 登録されているUTAU互換合成器の登録を全て解除します
        /// </summary>
        public void clearResampler()
        {
PathResamplers.clear();
PathResampler = "";
ResamplersWithWine.clear();
        }

        /// <summary>
        /// 第index番目に登録されているresamplerをwine経由で呼ぶかどうかを表す値を取得します
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public boolean isResamplerWithWineAt( int index )
        {
if ( index == 0 ) {
    return ResamplerWithWine;
} else {
    index--;
    if ( 0 <= index && index < vec.size( ResamplersWithWine ) ) {
        return vec.get( ResamplersWithWine, index );
    }
    return false;
}
        }

        /// <summary>
        /// 第index番目に登録されているresamplerをwine経由で呼ぶかどうかを設定します
        /// </summary>
        /// <param name="index"></param>
        /// <param name="with_wine"></param>
        public void setResamplerWithWineAt( int index, boolean with_wine )
        {
if ( index == 0 ) {
    ResamplerWithWine = with_wine;
} else {
    index--;
    if ( 0 <= index && index < vec.size( ResamplersWithWine ) ) {
        vec.set( ResamplersWithWine, index, with_wine );
    }
}
        }

        /// <summary>
        /// 第index番目に登録されているUTAU互換合成器のパスを取得します
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public String getResamplerAt( int index )
        {
if ( index == 0 ) {
    return PathResampler;
} else {
    index--;
    if ( 0 <= index && index < PathResamplers.size() ) {
        return PathResamplers.get( index );
    }
}
return "";
        }

        /// <summary>
        /// 第index番目のUTAU互換合成器のパスを設定します
        /// </summary>
        /// <param name="index"></param>
        /// <param name="path"></param>
        public void setResamplerAt( int index, String path )
        {
if ( path == null ) {
    return;
}
if ( path.equals( "" ) ) {
    return;
}
if ( index == 0 ) {
    PathResampler = path;
} else {
    index--;
    if ( 0 <= index && index < PathResamplers.size() ) {
        PathResamplers.set( index, path );
    }
}
        }

        /// <summary>
        /// 第index番目のUTAU互換合成器を登録解除します
        /// </summary>
        /// <param name="index"></param>
        public void removeResamplerAt( int index )
        {
int size = PathResamplers.size();
if ( index == 0 ) {
    if ( size > 0 ) {
        PathResampler = PathResamplers.get( 0 );
        ResamplerWithWine = ResamplersWithWine.get( 0 );
        for ( int i = 0; i < size - 1; i++ ) {
            PathResamplers.set( i, PathResamplers.get( i + 1 ) );
            ResamplersWithWine.set( i, ResamplersWithWine.get( i + 1 ) );
        }
        PathResamplers.removeElementAt( size - 1 );
        ResamplersWithWine.removeElementAt( size - 1 );
    } else {
        PathResampler = "";
    }
} else {
    index--;
    if ( 0 <= index && index < size ) {
        for ( int i = 0; i < size - 1; i++ ) {
            PathResamplers.set( i, PathResamplers.get( i + 1 ) );
            ResamplersWithWine.set( i, ResamplersWithWine.get( i + 1 ) );
        }
        PathResamplers.removeElementAt( size - 1 );
        ResamplersWithWine.removeElementAt( size - 1 );
    }
}
        }

        /// <summary>
        /// 新しいUTAU互換合成器のパスを登録します
        /// </summary>
        /// <param name="path"></param>
        public void addResampler( String path, boolean with_wine )
        {
int count = getResamplerCount();
if ( count == 0 ) {
    PathResampler = path;
    ResamplerWithWine = with_wine;
} else {
    PathResamplers.add( path );
    ResamplersWithWine.add( with_wine );
}
        }

        /// <summary>
        /// 最後に出力したファイルのパスのうち，拡張子が指定したものと同じであるものを取得します
        /// </summary>
        /// <param name="extension"></param>
        /// <returns></returns>
        public String getLastUsedPathIn( String extension )
        {
String ret = getLastUsedPathCore( LastUsedPathIn, extension );
if ( ret.equals( "" ) ) {
    return getLastUsedPathCore( LastUsedPathOut, extension );
}
/*if ( !ret.Equals( "" ) ) {
    ret = PortUtil.getDirectoryName( ret );
}*/
return ret;
        }

        /// <summary>
        /// 最後に出力したファイルのパスを設定します
        /// </summary>
        /// <param name="path"></param>
        public void setLastUsedPathIn( String path, String ext_with_dot )
        {
setLastUsedPathCore( LastUsedPathIn, path, ext_with_dot );
        }

        /// <summary>
        /// 最後に入力したファイルのパスのうち，拡張子が指定したものと同じであるものを取得します．
        /// </summary>
        /// <param name="extension"></param>
        /// <returns></returns>
        public String getLastUsedPathOut( String extension )
        {
String ret = getLastUsedPathCore( LastUsedPathOut, extension );
if ( ret.equals( "" ) ) {
    ret = getLastUsedPathCore( LastUsedPathIn, extension );
}
/*if ( !ret.Equals( "" ) ) {
    ret = PortUtil.getDirectoryName( ret );
}*/
return ret;
        }

        /// <summary>
        /// 最後に入力したファイルのパスを設定します
        /// </summary>
        /// <param name="path"></param>
        /// <param name="ext_with_dot">ピリオド付きの拡張子（ex. ".txt"）</param>
        public void setLastUsedPathOut( String path, String ext_with_dot )
        {
setLastUsedPathCore( LastUsedPathOut, path, ext_with_dot );
        }

        /// <summary>
        /// 自動ビブラートを作成します
        /// </summary>
        /// <param name="type"></param>
        /// <param name="vibrato_clocks"></param>
        /// <returns></returns>
        public VibratoHandle createAutoVibrato( SynthesizerType type, int vibrato_clocks )
        {
if ( UseUserDefinedAutoVibratoType ) {
    if ( AutoVibratoCustom == null ) {
        AutoVibratoCustom = new Vector<VibratoHandle>();
    }

    // 下4桁からインデックスを取得
    int index = 0;
    if ( this.AutoVibratoTypeCustom == null ) {
        index = 0;
    } else {
        int trimlen = 4;
        int len = PortUtil.getStringLength( this.AutoVibratoTypeCustom );
        if ( len < 4 ) {
            trimlen = len;
        }
        if ( trimlen > 0 ) {
            String s = str.sub( this.AutoVibratoTypeCustom, len - trimlen, trimlen );
            try {
                index = (int)PortUtil.fromHexString( s );
                index--;
            } catch ( Exception ex ) {
                serr.println( EditorConfig.class + ".createAutoVibrato; ex=" + ex + "; AutoVibratoTypeCustom=" + AutoVibratoTypeCustom + "; s=" + s );
                index = 0;
            }
        }
    }

    VibratoHandle ret = null;
    if ( 0 <= index && index < this.AutoVibratoCustom.size() ) {
        ret = this.AutoVibratoCustom.get( index );
        if ( ret != null ) {
            ret = (VibratoHandle)ret.clone();
        }
    }
    if ( ret == null ) {
        ret = new VibratoHandle();
    }
    ret.IconID = "$0404" + PortUtil.toHexString( index + 1, 4 );
    ret.setLength( vibrato_clocks );
    return ret;
} else {
    String iconid = type == SynthesizerType.VOCALOID1 ? AutoVibratoType1 : AutoVibratoType2;
    VibratoHandle ret = VocaloSysUtil.getDefaultVibratoHandle( iconid,
                                                               vibrato_clocks,
                                                               type );
    if ( ret == null ) {
        ret = new VibratoHandle();
        ret.IconID = "$04040001";
        ret.setLength( vibrato_clocks );
    }
    return ret;
}
        }

        public int getControlCurveResolutionValue()
        {
return ClockResolutionUtility.getValue( ControlCurveResolution );
        }

        public TreeMap<String, BKeys[]> getShortcutKeysDictionary( Vector<ValuePairOfStringArrayOfKeys> defs )
        {
TreeMap<String, BKeys[]> ret = new TreeMap<String, BKeys[]>();
for ( int i = 0; i < ShortcutKeys.size(); i++ ) {
    ret.put( ShortcutKeys.get( i ).Key, ShortcutKeys.get( i ).Value );
}
for ( Iterator<ValuePairOfStringArrayOfKeys> itr = defs.iterator(); itr.hasNext(); ) {
    ValuePairOfStringArrayOfKeys item = itr.next();
    if ( !ret.containsKey( item.Key ) ) {
        ret.put( item.Key, item.Value );
    }
}
return ret;
        }

        public Font getBaseFont()
        {
return new Font( BaseFontName, Font.PLAIN, (int)BaseFontSize );
        }

        public int getMouseHoverTime()
        {
return m_mouse_hover_time;
        }

        public void setMouseHoverTime( int value )
        {
if ( value < 0 ) {
    m_mouse_hover_time = 0;
} else if ( 2000 < m_mouse_hover_time ) {
    m_mouse_hover_time = 2000;
} else {
    m_mouse_hover_time = value;
}
        }


        public QuantizeMode getPositionQuantize()
        {
return m_position_quantize;
        }

        public void setPositionQuantize( QuantizeMode value )
        {
if ( m_position_quantize != value ) {
    m_position_quantize = value;
    try {
        invokeQuantizeModeChangedEvent();
    } catch ( Exception ex ) {
        Logger.write( EditorConfig.class + ".getPositionQuantize; ex=" + ex + "\n" );
        serr.println( "EditorConfig#setPositionQuantize; ex=" + ex );
    }
}
        }


        public boolean isPositionQuantizeTriplet()
        {
return m_position_quantize_triplet;
        }

        public void setPositionQuantizeTriplet( boolean value )
        {
if ( m_position_quantize_triplet != value ) {
    m_position_quantize_triplet = value;
    try {
        invokeQuantizeModeChangedEvent();
    } catch ( Exception ex ) {
        serr.println( "EditorConfig#setPositionQuantizeTriplet; ex=" + ex );
        Logger.write( EditorConfig.class + ".setPositionQuantizeTriplet; ex=" + ex + "\n" );
    }
}
        }


        public QuantizeMode getLengthQuantize()
        {
return m_length_quantize;
        }

        public void setLengthQuantize( QuantizeMode value )
        {
if ( m_length_quantize != value ) {
    m_length_quantize = value;
    try {
        invokeQuantizeModeChangedEvent();
    } catch ( Exception ex ) {
        serr.println( "EditorConfig#setLengthQuantize; ex=" + ex );
        Logger.write( EditorConfig.class + ".setLengthQuantize; ex=" + ex + "\n" );
    }
}
        }


        public boolean isLengthQuantizeTriplet()
        {
return m_length_quantize_triplet;
        }

        public void setLengthQuantizeTriplet( boolean value )
        {
if ( m_length_quantize_triplet != value ) {
    m_length_quantize_triplet = value;
    try {
        invokeQuantizeModeChangedEvent();
    } catch ( Exception ex ) {
        serr.println( "EditorConfig#setLengthQuantizeTriplet; ex=" + ex );
        Logger.write( EditorConfig.class + ".setLengthQuantizeTriplet; ex=" + ex + "\n" );
    }
}
        }

        /// <summary>
        /// QuantizeModeChangedイベントを発行します
        /// </summary>
        private void invokeQuantizeModeChangedEvent()
throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException
        {
quantizeModeChangedEvent.raise( EditorConfig.class, new BEventArgs() );
        }


        /// <summary>
        /// 「最近使用したファイル」のリストに、アイテムを追加します
        /// </summary>
        /// <param name="new_file"></param>
        public void pushRecentFiles( String new_file )
        {
// NumRecentFilesは0以下かも知れない
if ( NumRecentFiles <= 0 ) {
    NumRecentFiles = 5;
}

// RecentFilesはnullかもしれない．
if ( RecentFiles == null ) {
    RecentFiles = new Vector<String>();
}

// 重複があれば消す
Vector<String> dict = new Vector<String>();
for ( Iterator<String> itr = RecentFiles.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    boolean found = false;
    for ( int i = 0; i < dict.size(); i++ ) {
        if ( s.equals( dict.get( i ) ) ) {
            found = true;
        }
    }
    if ( !found ) {
        dict.add( s );
    }
}
RecentFiles.clear();
for ( Iterator<String> itr = dict.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    RecentFiles.add( s );
}

// 現在登録されているRecentFilesのサイズが規定より大きければ，下の方から消す
if ( RecentFiles.size() > NumRecentFiles ) {
    for ( int i = RecentFiles.size() - 1; i > NumRecentFiles; i-- ) {
        RecentFiles.removeElementAt( i );
    }
}

// 登録しようとしているファイルは，RecentFilesの中に既に登録されているかs？
int index = -1;
for ( int i = 0; i < RecentFiles.size(); i++ ) {
    if ( RecentFiles.get( i ).equals( new_file ) ) {
        index = i;
        break;
    }
}

if ( index >= 0 ) {  // 登録されてる場合
    RecentFiles.removeElementAt( index );
}
RecentFiles.insertElementAt( new_file, 0 );
        }

        /// <summary>
        /// このインスタンスの整合性をチェックします．
        /// PathResamplersとPathResamplersWithWineの個数があってるかどうかなどのチェックを行う
        /// </summary>
        public void check()
        {
int count = SymbolTable.getCount();
for ( int i = 0; i < count; i++ ) {
    SymbolTable st = SymbolTable.getSymbolTable( i );
    boolean found = false;
    for ( Iterator<String> itr = UserDictionaries.iterator(); itr.hasNext(); ) {
        String s = itr.next();
        String[] spl = PortUtil.splitString( s, new char[] { '\t' }, 2 );
        if ( st.getName().equals( spl[0] ) ) {
            found = true;
            break;
        }
    }
    if ( !found ) {
        UserDictionaries.add( st.getName() + "\tT" );
    }
}

// key_widthを最大，最小の間に収める
int draft_key_width = this.KeyWidth;
if ( draft_key_width < AppManager.MIN_KEY_WIDTH ) {
    draft_key_width = AppManager.MIN_KEY_WIDTH;
} else if ( AppManager.MAX_KEY_WIDTH < draft_key_width ) {
    draft_key_width = AppManager.MAX_KEY_WIDTH;
}

// PathResamplersWithWineの個数があってるかどうかチェック
if ( PathResamplers == null ) {
    PathResamplers = new Vector<String>();
}
if ( ResamplersWithWine == null ) {
    ResamplersWithWine = new Vector<Boolean>();
}
if ( vec.size( PathResamplers ) != vec.size( ResamplersWithWine ) ) {
    int delta = vec.size( ResamplersWithWine ) - vec.size( PathResamplers );
    if ( delta > 0 ) {
        for ( int i = 0; i < delta; i++ ) {
            ResamplersWithWine.removeElementAt( vec.size( ResamplersWithWine ) - 1 );
        }
    } else if ( delta < 0 ) {
        for ( int i = 0; i < -delta; i++ ) {
            vec.add( ResamplersWithWine, false );
        }
    }
}

// SynthEngineの違いを識別しないように変更．VOALOID1に縮約する
if ( DefaultSynthesizer == RendererKind.VOCALOID1_100 ||
    DefaultSynthesizer == RendererKind.VOCALOID1_101 ) {
    DefaultSynthesizer = RendererKind.VOCALOID1;
}
        }
    }


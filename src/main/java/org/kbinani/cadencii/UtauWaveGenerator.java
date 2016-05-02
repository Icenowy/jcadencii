/*
 * UtauWaveGenerator.cs
 * Copyright © 2010-2011 kbinani
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
import java.io.*;
import java.util.*;
import org.kbinani.*;
import org.kbinani.media.*;
import org.kbinani.vsq.*;


    /// <summary>
    /// UTAUの合成器(または互換合成器)を用いて波形を合成する波形生成器
    /// </summary>
    public class UtauWaveGenerator extends WaveUnit implements WaveGenerator {
        public static final String FILEBASE = "temp.wav";
        private static final int MAX_CACHE = 512;
        private static final int BUFLEN = 1024;
        private static final int VERSION = 0;
        private static TreeMap<String, ValuePair<String, Double>> mCache = new TreeMap<String, ValuePair<String, Double>>();
        private static final int BASE_TEMPO = 120;

        private Vector<RenderQueue> mResamplerQueue = new Vector<RenderQueue>();
        private double[] mLeft;
        private double[] mRight;

        private VsqFileEx mVsq;
        private String mResampler;
        private String mWavtool;
        private String mTempDir;
        private boolean mResamplerWithWine;
        private boolean mWavtoolWithWine;
        //private boolean mAbortRequired = false;
        private boolean mRunning = false;
        private String mWine = "";

        private long mTotalSamples;
        private WaveReceiver mReceiver = null;
        private long mTotalAppend = 0;
        private int mTrack;
        private double[] mBufferL = new double[BUFLEN];
        private double[] mBufferR = new double[BUFLEN];
        private double mTrimRemainSeconds = 0.0;
        private int mSampleRate;
        /// <summary>
        /// whdから読み込んだサンプリングレート．
        /// 波形処理ラインのサンプリングレートと違う可能性がある
        /// </summary>
        private int mThisSampleRate = 44100;
        private RateConvertContext mContext = null;
        private WorkerState mState;
        private boolean mUseWideCharacterWorkaround = false;
        // 作成したジャンクションのリスト
        private Vector<String> mJunctions = new Vector<String>();


        public int getSampleRate()
        {
return mSampleRate;
        }

        public boolean isRunning()
        {
return mRunning;
        }

        public long getTotalSamples()
        {
return mTotalSamples;
        }

        public double getProgress()
        {
if ( mTotalSamples <= 0 ) {
    return 0.0;
} else {
    return mTotalAppend / (double)mTotalSamples;
}
        }

        /*public void stop()
        {
if ( mRunning ) {
    mAbortRequired = true;
    while ( mRunning ) {
#if JAVA
        try{
            Thread.sleep( 100 );
        }catch( Exception ex ){
        }
#else
        Thread.Sleep( 100 );
#endif
    }
}
        }*/

        public void setConfig( String parameter )
        {
// do nothing
        }

        public int getVersion()
        {
return VERSION;
        }

        /// <summary>
        /// 初期化メソッド．
        /// </summary>
        /// <param name="vsq"></param>
        /// <param name="track"></param>
        /// <param name="start_clock"></param>
        /// <param name="end_clock"></param>
        public void init( VsqFileEx vsq, int track, int start_clock, int end_clock, int sample_rate )
        {
mTrack = track;
int resampler_index = VsqFileEx.getTrackResamplerUsed( vsq.Track.get( track ) );
int resampler_count = mConfig.getResamplerCount();
if ( resampler_count <= resampler_index ) {
    resampler_index = resampler_count - 1;
}
if ( resampler_index < 0 ) {
    resampler_index = 0;
}
mUseWideCharacterWorkaround = mConfig.UseWideCharacterWorkaround;
mResampler = mConfig.getResamplerAt( resampler_index );
mWavtool = mConfig.PathWavtool;
mSampleRate = sample_rate;
String id = AppManager.getID();
mTempDir = fsys.combine( AppManager.getCadenciiTempDir(), id );
mResamplerWithWine = mConfig.isResamplerWithWineAt( resampler_index );
mWavtoolWithWine = mConfig.WavtoolWithWine;
mWine = mConfig.getBuiltinWineMinimumExecutable();

mVsq = (VsqFileEx)vsq.clone();
mVsq.updateTotalClocks();

if ( end_clock < vsq.TotalClocks ) {
    // 末尾の部分は不要なので削除
    mVsq.removePart( end_clock, mVsq.TotalClocks + 480 );
}

double trim_sec = 0.0;
if ( start_clock > 0 ) {
    // 途中からの合成が指示された場合
    // 0clockからstart_clockまでを削除する
    // もしstart_clock位置に音符があれば，その音符の先頭から合成し，trim_secを適切に設定する

    // まず，start_clockに音符があるかどうかを調べる
    // 音符があれば，trim_endに適切な値を代入
    VsqTrack vsq_track = mVsq.Track.get( track );
    int c = vsq_track.getEventCount();
    int trim_end = start_clock;
    for ( int i = 0; i < c; i++ ) {
        VsqEvent itemi = vsq_track.getEvent( i );
        if ( itemi.ID.type != VsqIDType.Anote ) {
            continue;
        }
        if ( itemi.Clock <= start_clock && start_clock < itemi.Clock + itemi.ID.getLength() ) {
            trim_end = itemi.Clock;
            break;
        }
    }

    if ( trim_end == start_clock ) {
        trim_sec = 0.0;
    } else {
        trim_sec = mVsq.getSecFromClock( start_clock ) - mVsq.getSecFromClock( trim_end );
    }

    // 必要ならトリムを実行
    if ( 0 < trim_end ) {
        mVsq.removePart( 0, trim_end );
    }
}
mVsq.adjustClockToMatchWith( BASE_TEMPO );
mVsq.updateTotalClocks();

mTrimRemainSeconds = trim_sec;
        }


        /*private static String getShortPathName( String path )
        {
#if DEBUG
String before = path;
#endif
#if !JAVA
if ( path == null ) {
    return "";
}
if ( path == "" ) {
    return path;
}
if ( fsys.isFileExists( path ) || fsys.isDirectoryExists( path ) ) {
    const int LEN = 260;
    System.Text.StringBuilder sb_path = new System.Text.StringBuilder( LEN );
    if ( win32.GetShortPathNameW( path, sb_path, LEN ) != 0 ) {
        path = sb_path.ToString();
    }
}
#endif
#if DEBUG
sout.println( "UtauWaveGenerator#getShortPathName; before=" + before + "; after=" + path );
#endif
return path;
        }*/

        public void setReceiver( WaveReceiver r )
        {
if ( mReceiver != null ) {
    mReceiver.end();
}
mReceiver = r;
        }

        public long getPosition()
        {
return mTotalAppend;
        }

        public static void clearCache()
        {
for ( Iterator<String> itr = mCache.keySet().iterator(); itr.hasNext(); ) {
    String key = itr.next();
    ValuePair<String, Double> value = mCache.get( key );
    String file = value.getKey();
    try {
        PortUtil.deleteFile( file );
    } catch ( Exception ex ) {
        serr.println( "UtauWaveGenerator#clearCache; ex=" + ex );
        Logger.write( "UtauWaveGenerator::clearCache; ex=" + ex + "\n" );
    }
}
mCache.clear();
        }

        public void begin( long total_samples, WorkerState state )
        {
mState = state;
mTotalSamples = total_samples;
try {
    double sample_length = mVsq.getSecFromClock( mVsq.TotalClocks ) * mSampleRate;
    //mAbortRequired = false;
    mRunning = true;
    if ( !fsys.isDirectoryExists( mTempDir ) ) {
        PortUtil.createDirectory( mTempDir );
    }

    // 原音設定を読み込み
    VsqTrack target = mVsq.Track.get( mTrack );

    String file = fsys.combine( mTempDir, FILEBASE );
    if ( fsys.isFileExists( file ) ) {
        PortUtil.deleteFile( file );
    }
    String file_whd = fsys.combine( mTempDir, FILEBASE + ".whd" );
    if ( fsys.isFileExists( file_whd ) ) {
        PortUtil.deleteFile( file_whd );
    }
    String file_dat = fsys.combine( mTempDir, FILEBASE + ".dat" );
    if ( fsys.isFileExists( file_dat ) ) {
        PortUtil.deleteFile( file_dat );
    }

    int count = -1;
    double sec_end = 0;
    double sec_end_old = 0;
    int program_change = 0;
    mResamplerQueue.clear();

    // 前後の音符の先行発音やオーバーラップやらを取得したいので、一度リストに格納する
    Vector<VsqEvent> events = new Vector<VsqEvent>();
    for ( Iterator<VsqEvent> itr = target.getNoteEventIterator(); itr.hasNext(); ) {
        VsqEvent itemi = itr.next();
        events.add( itemi );
    }

    int events_count = events.size();
    for ( int k = 0; k < events_count; k++ ) {
        VsqEvent item = events.get( k );
        VsqEvent singer_event = target.getSingerEventAt( item.Clock );
        if ( singer_event == null ) {
            program_change = 0;
        } else {
            program_change = singer_event.ID.IconHandle.Program;
        }
        String singer_raw = "";
        String singer = "";
        if ( 0 <= program_change && program_change < mConfig.UtauSingers.size() ) {
            singer_raw = mConfig.UtauSingers.get( program_change ).VOICEIDSTR;
            singer = singer_raw;
        }
        if ( state.isCancelRequested() ) {
            exitBegin();
            return;
        }
        count++;
        double sec_start = mVsq.getSecFromClock( item.Clock );
        double sec_start_act = sec_start - item.UstEvent.getPreUtterance() / 1000.0;
        sec_end_old = sec_end;
        sec_end = mVsq.getSecFromClock( item.Clock + item.ID.getLength() );
        double sec_end_act = sec_end;
        VsqEvent item_next = null;
        if ( k + 1 < events_count ) {
            item_next = events.get( k + 1 );
        }
        if ( item_next != null ) {
            double sec_start_act_next =
                mVsq.getSecFromClock( item_next.Clock ) - item_next.UstEvent.getPreUtterance() / 1000.0
                + item_next.UstEvent.getVoiceOverlap() / 1000.0;
            if ( sec_start_act_next < sec_end_act ) {
                sec_end_act = sec_start_act_next;
            }
        }
        //float t_temp = (float)(item.ID.getLength() / (sec_end - sec_start) / 8.0);
        if ( (count == 0 && sec_start > 0.0) || (sec_start > sec_end_old) ) {
            // 最初の音符，
            double sec_start2 = sec_end_old;
            double sec_end2 = sec_start;
            // t_temp2がBASE_TEMPOから大きく外れないように
            int draft_length = (int)((sec_end2 - sec_start2) * 8.0 * BASE_TEMPO);
            //float t_temp2 = (float)(draft_length / (sec_end2 - sec_start2) / 8.0);
            //String str_t_temp2 = PortUtil.formatDecimal( "0.00", t_temp2 );
            //double act_t_temp2 = PortUtil.parseDouble( str_t_temp2 );
            RenderQueue rq = new RenderQueue();
            //rq.WavtoolArgPrefix = "\"" + file + "\" \"" + fsys.combine( singer, "R.wav" ) + "\" 0 " + draft_length + "@" + BASE_TEMPO;
            rq.WavtoolArgPrefix.clear();
            rq.WavtoolArgPrefix.add( "\"" + file + "\"" );
            rq.WavtoolArgPrefix.add( "\"" + fsys.combine( singer, "R.wav" ) + "\"" );
            rq.WavtoolArgPrefix.add( "0" );
            rq.WavtoolArgPrefix.add( draft_length + "@" + BASE_TEMPO );
            //rq.WavtoolArgSuffix = " 0 0";
            rq.WavtoolArgSuffix.clear();
            rq.WavtoolArgSuffix.add( "0" );
            rq.WavtoolArgSuffix.add( "0" );
            rq.Oto = new OtoArgs();
            rq.FileName = "";
            rq.secStart = sec_start2;
            rq.ResamplerFinished = true;
            mResamplerQueue.add( rq );
            count++;
        }
        String lyric = item.ID.LyricHandle.L0.Phrase;
        String note = NoteStringFromNoteNumber( item.ID.Note );
        int millisec = (int)((sec_end_act - sec_start_act) * 1000) + 50;

        OtoArgs oa = new OtoArgs();
        if ( AppManager.mUtauVoiceDB.containsKey( singer_raw ) ) {
            UtauVoiceDB db = AppManager.mUtauVoiceDB.get( singer_raw );
            oa = db.attachFileNameFromLyric( lyric );
        }
        oa.msPreUtterance = item.UstEvent.getPreUtterance();
        oa.msOverlap = item.UstEvent.getVoiceOverlap();
        RenderQueue rq2 = new RenderQueue();
        String wavPath = "";
        if ( oa.fileName != null && str.length( oa.fileName ) > 0 ) {
            wavPath = fsys.combine( singer, oa.fileName );
        } else {
            wavPath = fsys.combine( singer, lyric + ".wav" );
        }
        String[] resampler_arg_prefix = new String[] { "\"" + wavPath + "\"" };
        String[] resampler_arg_suffix = new String[]{
            "\"" + note + "\"",
            "100",
            "\"" + item.UstEvent.Flags + "\"",
            oa.msOffset + "",
            millisec + "",
            oa.msConsonant + "",
            oa.msBlank + "",
            item.UstEvent.getIntensity() + "",
            item.UstEvent.getModuration() + "" };

        // ピッチを取得
        Vector<String> pitch = new Vector<String>();
        boolean allzero = true;
        int delta_clock = 5;  //ピッチを取得するクロック間隔
        int tempo = BASE_TEMPO;
        double delta_sec = delta_clock / (8.0 * tempo); //ピッチを取得する時間間隔

        // sec_start_act～sec_end_actまでの，item.ID.Note基準のピッチベンドを取得
        // ただしdelta_sec秒間隔で
        double sec = mVsq.getSecFromClock( item.Clock ) - (item.UstEvent.getPreUtterance() + item.UstEvent.getStartPoint()) / 1000.0;
        int indx = 0;
        int super_note = item.ID.Note;
        double sec_vibstart = mVsq.getSecFromClock( item.Clock + item.ID.VibratoDelay );
        int totalcount = 0;

        Iterator<PointD> vibitr = null;
        if ( item.ID.VibratoHandle != null ) {
            vibitr = new VibratoPointIteratorBySec(
                mVsq,
                item.ID.VibratoHandle.getRateBP(),
                item.ID.VibratoHandle.getStartRate(),
                item.ID.VibratoHandle.getDepthBP(),
                item.ID.VibratoHandle.getStartDepth(),
                item.Clock + item.ID.VibratoDelay,
                item.ID.getLength() - item.ID.VibratoDelay,
                (float)delta_sec );
        }
        
        while ( sec <= sec_end ) {
            // clockでの音符の音の高さを調べる
            // ピッチベンドを調べたい時刻
            int clock = (int)mVsq.getClockFromSec( sec );
            // dst_noteに，clockでの，音符のノートナンバー(あれば．なければ元の音符と同じ値)
            int dst_note = super_note;
            if ( k > 0 ) {
                VsqEvent prev = vec.get( events, k - 1 );
                dst_note = super_note;
            }
            for ( int i = indx; i < events_count; i++ ) {
                VsqEvent itemi = vec.get( events, i );
                if ( clock < itemi.Clock ) {
                    continue;
                }
                int itemi_length = itemi.ID.getLength();
                if ( itemi.Clock <= clock && clock < itemi.Clock + itemi_length ) {
                    dst_note = itemi.ID.Note;
                    indx = i;
                    break;
                }
            }

            // PIT, PBSによるピッチベンドを加味
            double pvalue = (dst_note - super_note) * 100.0 + target.getPitchAt( clock );

            // ビブラートがあれば，ビブラートによるピッチベンドを加味
            if ( sec_vibstart <= sec && vibitr != null && vibitr.hasNext() ) {
                PointD pd = vibitr.next();
                pvalue += pd.getY() * 100.0;
            }

            // リストに入れる
            if ( totalcount == 0 ) {
                vec.add( pitch, PortUtil.formatDecimal( "0.00", pvalue ) + "Q" + tempo );
            } else {
                vec.add( pitch, PortUtil.formatDecimal( "0.00", pvalue ) );
            }
            totalcount++;
            if ( pvalue != 0.0 ) {
                allzero = false;
            }

            // 次
            sec += delta_sec;
        }

        //4_あ_C#4_550.wav
        //String md5_src = "";
        rq2.hashSource = "";
        for ( String s : resampler_arg_prefix ) {
            rq2.hashSource += s + " ";
        }
        for ( String s : resampler_arg_suffix ) {
            rq2.hashSource += s + " ";
        }
        for ( String s : pitch ) {
            rq2.hashSource += s + " ";
        }
        rq2.hashSource += mResampler;
//#if DEBUG
//                    String filename =
//                        fsys.combine( mTempDir, k + "_" + PortUtil.getFileNameWithoutExtension( wavPath ) + "_" + note + ".wav" );
//#else
        String filename =
            fsys.combine( mTempDir, PortUtil.getMD5FromString( mCache.size() + rq2.hashSource ) + ".wav" );
//#endif

        rq2.appendArgRange( resampler_arg_prefix );
        rq2.appendArg( "\"" + filename + "\"" );
        rq2.appendArgRange( resampler_arg_suffix );
        if ( !allzero ) {
            rq2.appendArgRange( pitch.toArray( new String[0] ) );
        }

        boolean exist_in_cache = mCache.containsKey( rq2.hashSource );
        if ( !exist_in_cache ) {
            if ( mCache.size() + 1 >= MAX_CACHE ) {
                double old = PortUtil.getCurrentTime();
                String delfile = "";
                String delkey = "";
                for ( Iterator<String> itr = mCache.keySet().iterator(); itr.hasNext(); ) {
                    String key = itr.next();
                    ValuePair<String, Double> value = mCache.get( key );
                    if ( old < value.getValue() ) {
                        old = value.getValue();
                        delfile = value.getKey();
                        delkey = key;
                    }
                }
                try {
                    PortUtil.deleteFile( delfile );
                } catch ( Exception ex ) {
                    serr.println( "UtauWaveGenerator#begin; ex=" + ex );
                    Logger.write( "UtauWaveGenerator#begin(long): ex=" + ex + "\n" );
                }
                mCache.remove( delkey );
            }
            //mCache.put( search_key, new ValuePair<String, Double>( filename, PortUtil.getCurrentTime() ) );
            //->ここ，実際の合成が終わったタイミングで追加するようにする
        } else {
            filename = mCache.get( rq2.hashSource ).getKey();
        }

        String str_t_temp = PortUtil.formatDecimal( "0.00", BASE_TEMPO );
        //rq2.WavtoolArgPrefix = "\"" + file + "\" \"" + filename + "\" " + item.UstEvent.getStartPoint() + " " + item.ID.getLength() + "@" + str_t_temp;
        rq2.WavtoolArgPrefix.clear();
        rq2.WavtoolArgPrefix.add( "\"" + file + "\"" );
        rq2.WavtoolArgPrefix.add( "\"" + filename + "\"" );
        rq2.WavtoolArgPrefix.add( "" + item.UstEvent.getStartPoint() );
        rq2.WavtoolArgPrefix.add( "" + item.ID.getLength() + "@" + str_t_temp );
        UstEnvelope env = item.UstEvent.getEnvelope();
        if ( env == null ) {
            env = new UstEnvelope();
        }
        //rq2.WavtoolArgSuffix = " " + env.p1 + " " + env.p2 + " " + env.p3 + " " + env.v1 + " " + env.v2 + " " + env.v3 + " " + env.v4;
        //rq2.WavtoolArgSuffix += " " + oa.msOverlap + " " + env.p4 + " " + env.p5 + " " + env.v5;
        rq2.WavtoolArgSuffix.clear();
        rq2.WavtoolArgSuffix.add( "" + env.p1 );
        rq2.WavtoolArgSuffix.add( "" + env.p2 );
        rq2.WavtoolArgSuffix.add( "" + env.p3 );
        rq2.WavtoolArgSuffix.add( "" + env.v1 );
        rq2.WavtoolArgSuffix.add( "" + env.v2 );
        rq2.WavtoolArgSuffix.add( "" + env.v3 );
        rq2.WavtoolArgSuffix.add( "" + env.v4 );
        rq2.WavtoolArgSuffix.add( "" + oa.msOverlap );
        rq2.WavtoolArgSuffix.add( "" + env.p4 );
        rq2.WavtoolArgSuffix.add( "" + env.p5 );
        rq2.WavtoolArgSuffix.add( "" + env.v5 );
        rq2.Oto = oa;
        rq2.FileName = filename;
        rq2.secStart = sec_start_act;
        rq2.ResamplerFinished = exist_in_cache;
        mResamplerQueue.add( rq2 );
    }

    int num_queues = mResamplerQueue.size();
    int processed_sample = 0; //WaveIncomingで受け渡した波形の合計サンプル数
    int channel = 0; // .whdに記録されたチャンネル数
    int byte_per_sample = 0;
    // 引き続き、wavtoolを呼ぶ作業に移行
    boolean first = true;
    //int trim_remain = (int)( trimMillisec / 1000.0 * VSTiProxy.SAMPLE_RATE); //先頭から省かなければならないサンプル数の残り
    VsqBPList dyn_curve = mVsq.Track.get( mTrack ).getCurve( "dyn" );
    for ( int i = 0; i < num_queues; i++ ) {
        RenderQueue rq = mResamplerQueue.get( i );
        if ( !rq.ResamplerFinished ) {

            Vector<String> list = new Vector<String>();
            if( mResamplerWithWine ){
                list.add( mWine );
            }
            list.add( mResampler );
            for( String s : rq.getResamplerArg() ){
                if( s.startsWith( "\"" ) && s.endsWith( "\"" ) ){
                    s = str.sub( s, 1, s.length() -2 );
                }
                list.add( s );
            }
            ProcessBuilder pb = new ProcessBuilder( list );
            Process process = pb.start();
            boolean д = true;
            for( ; д; ){
                try{
                    int ecode = process.exitValue();
                }catch( Exception ex ){
                    //ex.printStackTrace();
                    Logger.write( UtauWaveGenerator.class + ".begin; ex=" + ex + "\n" );
                    continue;
                }
                break;
            }
            //process.waitFor();
        }
        if ( state.isCancelRequested() ) {
            break;
        }

        // wavtoolを起動
        double sec_fin; // 今回のwavtool起動によってレンダリングが完了したサンプル長さ
        RenderQueue p = mResamplerQueue.get( i );
        OtoArgs oa_next;
        if ( i + 1 < num_queues ) {
            oa_next = mResamplerQueue.get( i + 1 ).Oto;
        } else {
            oa_next = new OtoArgs();
        }

        // この後のwavtool呼び出しで，どこまで波形が確定するか？
        // 安全のために，wavtoolでくっつける音符の先頭位置までが確定するだろう，ということにする
        sec_fin = p.secStart;
        if ( i + 1 == num_queues ) {
            // 最後の音符だった場合は，最後まで読み取ることにする
            sec_fin = mTotalSamples / (double)mSampleRate;
        }
        float mten = p.Oto.msPreUtterance + oa_next.msOverlap - oa_next.msPreUtterance;
        //String arg_wavtool = p.WavtoolArgPrefix + (mten >= 0 ? ("+" + mten) : ("-" + (-mten))) + p.WavtoolArgSuffix;
        Vector<String> arg_wavtool = new Vector<String>();
        int size = vec.size( p.WavtoolArgPrefix );
        for ( int j = 0; j < size; j++ ) {
            String s = vec.get( p.WavtoolArgPrefix, j );
            if ( j == size - 1 ) {
                s += (mten >= 0 ? ("+" + mten) : ("-" + (-mten)));
            }
            vec.add( arg_wavtool, s );
        }
        size = vec.size( p.WavtoolArgSuffix );
        for ( int j = 0; j < size; j++ ) {
            vec.add( arg_wavtool, vec.get( p.WavtoolArgSuffix, j ) );
        }
        processWavtool( arg_wavtool, file, mTempDir, mWavtool, mWavtoolWithWine );

        // できたwavを読み取ってWaveIncomingイベントを発生させる
        int sample_end = (int)(sec_fin * mSampleRate);
        // whdを読みに行く
        if ( first ) {
            RandomAccessFile whd = null;
            // このファイルのサンプリングレート．ヘッダで読み込むけど初期値はコレにしとく
            mThisSampleRate = 44100;
            try {
                whd = new RandomAccessFile( file_whd, "r" );
                whd.seek( 0 );
                // RIFF
                byte[] buf = new byte[4];
                int gcount = whd.read( buf, 0, 4 );
                if ( buf[0] != 'R' || buf[1] != 'I' || buf[2] != 'F' || buf[3] != 'F' ) {
                    continue;
                }
                // ファイルサイズ
                whd.read( buf, 0, 4 );
                // WAVE
                whd.read( buf, 0, 4 );
                if ( buf[0] != 'W' || buf[1] != 'A' || buf[2] != 'V' || buf[3] != 'E' ) {
                    continue;
                }
                // fmt 
                whd.read( buf, 0, 4 );
                if ( buf[0] != 'f' || buf[1] != 'm' || buf[2] != 't' || buf[3] != ' ' ) {
                    continue;
                }
                // fmt チャンクのサイズ
                whd.read( buf, 0, 4 );
                long loc_end_of_fmt = whd.getFilePointer(); //fmtチャンクの終了位置．ここは一定値でない可能性があるので読込み
                loc_end_of_fmt += buf[0] | buf[1] << 8 | buf[2] << 16 | buf[3] << 24;
                // format ID
                whd.read( buf, 0, 2 );
                int id = buf[0] | buf[1] << 8;
                if ( id != 0x0001 ) { //0x0001はリニアPCM
                    continue;
                }
                // チャンネル数
                whd.read( buf, 0, 2 );
                channel = buf[1] << 8 | buf[0];
                // サンプリングレート
                whd.read( buf, 0, 4 );
                mThisSampleRate = PortUtil.make_int32_le( buf );//.__BBBBBBBBBAAAAAAAAAAAAAAAAAAAAAAAARRRRRRRR__stderr buf[0] | buf[1] << 8 | buf[2] << 16 | buf[3] << 24;
                // データ速度
                whd.read( buf, 0, 4 );
                // ブロックサイズ
                whd.read( buf, 0, 2 );
                // 1チャンネル、1サンプルあたりのビット数
                whd.read( buf, 0, 2 );
                int bit_per_sample = buf[1] << 8 | buf[0];
                byte_per_sample = bit_per_sample / 8;
                whd.seek( loc_end_of_fmt );
                // data
                whd.read( buf, 0, 4 );
                if ( buf[0] != 'd' || buf[1] != 'a' || buf[2] != 't' || buf[3] != 'a' ) {
                    continue;
                }
                // size of data chunk
                whd.read( buf, 0, 4 );
                //int size = buf[3] << 24 | buf[2] << 16 | buf[1] << 8 | buf[0];
                //int total_samples = size / (channel * byte_per_sample);
            } catch ( Exception ex ) {
                serr.println( "UtauWaveGenerator#begin; ex=" + ex );
                Logger.write( "UtauWaveGenerator::begin(long); ex=" + ex + "\n" );
            } finally {
                if ( whd != null ) {
                    try {
                        whd.close();
                    } catch ( Exception ex2 ) {
                        serr.println( "UtauWaveGenerator#begin; ex2=" + ex2 );
                        Logger.write( "UtauWaveGenerator::begin(long); ex=" + ex2 + "\n" );
                    }
                }
            }
            first = false;
        }

        // datを読みに行く
        int sampleFrames = sample_end - processed_sample;
        if ( channel > 0 && byte_per_sample > 0 && sampleFrames > 0 ) {
            int length = (sampleFrames > mSampleRate ? mSampleRate : sampleFrames);
            int remain = sampleFrames;
            mLeft = new double[length];
            mRight = new double[length];
            double k_inv64 = 1.0 / 64.0;
            double k_inv128 = 1.0 / 128.0;
            double k_inv32768 = 1.0 / 32768.0;
            int buflen = 1024;
            byte[] wavbuf = new byte[buflen];
            int pos = 0;
            RandomAccessFile dat = null;
            try {
                dat = new RandomAccessFile( file_dat, "r" );
                dat.seek( processed_sample * channel * byte_per_sample );
                double sec_start = processed_sample / (double)mSampleRate;
                double sec_per_sa = 1.0 / (double)mSampleRate;
                ByRef<Integer> index = new ByRef<Integer>( 0 );
                if ( byte_per_sample == 1 ) {
                    if ( channel == 1 ) {
                        while ( remain > 0 ) {
                            if ( state.isCancelRequested() ) {
                                break;
                            }
                            int len = dat.read( wavbuf, 0, buflen );
                            if ( len <= 0 ) {
                                break;
                            }
                            int c = 0;
                            while ( len > 0 && remain > 0 ) {
                                if ( state.isCancelRequested() ) {
                                    break;
                                }
                                len -= 1;
                                remain--;
                                double gtime_dyn = sec_start + pos * sec_per_sa;
                                int clock = (int)mVsq.getClockFromSec( gtime_dyn );
                                int dyn = dyn_curve.getValue( clock, index );
                                double amp = dyn * k_inv64;
                                double v = ((0xff & wavbuf[c]) - 128) * k_inv128 * amp;
                                c++;
                                mLeft[pos] = v;
                                mRight[pos] = v;
                                pos++;
                                if ( pos >= length ) {
                                    waveIncoming( mLeft, mRight, mLeft.length, mThisSampleRate );
                                    pos = 0;
                                }
                            }
                        }
                    } else {
                        while ( remain > 0 ) {
                            if ( state.isCancelRequested() ) {
                                break;
                            }
                            int len = dat.read( wavbuf, 0, buflen );
                            if ( len <= 0 ) {
                                break;
                            }
                            int c = 0;
                            while ( len > 0 && remain > 0 ) {
                                if ( state.isCancelRequested() ) {
                                    break;
                                }
                                len -= 2;
                                remain--;
                                double gtime_dyn = sec_start + pos * sec_per_sa;
                                int clock = (int)mVsq.getClockFromSec( gtime_dyn );
                                int dyn = dyn_curve.getValue( clock, index );
                                double amp = dyn * k_inv64;
                                double vl = ((0xff & wavbuf[c]) - 128) * k_inv128 * amp;
                                double vr = ((0xff & wavbuf[c + 1]) - 128) * k_inv128 * amp;
                                mLeft[pos] = vl;
                                mRight[pos] = vr;
                                c += 2;
                                pos++;
                                if ( pos >= length ) {
                                    waveIncoming( mLeft, mRight, mLeft.length, mThisSampleRate );
                                    pos = 0;
                                }
                            }
                        }
                    }
                } else if ( byte_per_sample == 2 ) {
                    if ( channel == 1 ) {
                        while ( remain > 0 ) {
                            if ( state.isCancelRequested() ) {
                                break;
                            }
                            int len = dat.read( wavbuf, 0, buflen );
                            if ( len <= 0 ) {
                                break;
                            }
                            int c = 0;
                            while ( len > 0 && remain > 0 ) {
                                if ( state.isCancelRequested() ) {
                                    break;
                                }
                                len -= 2;
                                remain--;
                                double gtime_dyn = sec_start + pos * sec_per_sa;
                                int clock = (int)mVsq.getClockFromSec( gtime_dyn );
                                int dyn = dyn_curve.getValue( clock, index );
                                double amp = dyn * k_inv64;
                                double v = ((short)(PortUtil.make_int16_le( wavbuf, c ))) * k_inv32768 * amp;
                                mLeft[pos] = v;
                                mRight[pos] = v;
                                c += 2;
                                pos++;
                                if ( pos >= length ) {
                                    waveIncoming( mLeft, mRight, mLeft.length, mThisSampleRate );
                                    pos = 0;
                                }
                            }
                        }
                    } else {
                        while ( remain > 0 ) {
                            if ( state.isCancelRequested() ) {
                                break;
                            }
                            int len = dat.read( wavbuf, 0, buflen );
                            if ( len <= 0 ) {
                                break;
                            }
                            int c = 0;
                            while ( len > 0 && remain > 0 ) {
                                if ( state.isCancelRequested() ) {
                                    break;
                                }
                                len -= 4;
                                remain--;
                                double gtime_dyn = sec_start + pos * sec_per_sa;
                                int clock = (int)mVsq.getClockFromSec( gtime_dyn );
                                int dyn = dyn_curve.getValue( clock, index );
                                double amp = dyn * k_inv64;
                                double vl = ((short)(PortUtil.make_int16_le( wavbuf, c ))) * k_inv32768 * amp;
                                double vr = ((short)(PortUtil.make_int16_le( wavbuf, c + 2))) * k_inv32768 * amp;
                                mLeft[pos] = vl;
                                mRight[pos] = vr;
                                c += 4;
                                pos++;
                                if ( pos >= length ) {
                                    waveIncoming( mLeft, mRight, mLeft.length, mThisSampleRate );
                                    pos = 0;
                                }
                            }
                        }
                    }
                }
            } catch ( Exception ex ) {
                serr.println( "UtauWaveGenerator#run; ex=" + ex );
                Logger.write( "UtauWaveGenerator::begin(long); ex=" + ex + "\n" );
            } finally {
                if ( dat != null ) {
                    try {
                        dat.close();
                    } catch ( Exception ex2 ) {
                        serr.println( "UtauWaveGenerator#run; ex2=" + ex2 );
                        Logger.write( UtauWaveGenerator.class + "::begin(long); ex=" + ex2 + "\n" );
                    }
                    dat = null;
                }
            }

            if ( state.isCancelRequested() ) {
                //mAbortRequired = false;
                exitBegin();
                return;
            }
            if ( pos > 0 ) {
                waveIncoming( mLeft, mRight, pos, mThisSampleRate );
            }
            mLeft = null;
            mRight = null;
            System.gc();
            processed_sample += (sampleFrames - remain);
        }
    }


    int tremain = (int)(mTotalSamples - mTotalAppend);
    for ( int i = 0; i < BUFLEN; i++ ) {
        mBufferL[i] = 0.0;
        mBufferR[i] = 0.0;
    }
    while ( tremain > 0 && !state.isCancelRequested() ) {
        int amount = (tremain > BUFLEN) ? BUFLEN : tremain;
        waveIncoming( mBufferL, mBufferR, amount, mThisSampleRate );
        tremain -= amount;
    }
} catch ( Exception ex ) {
    serr.println( "UtauWaveGenerator.begin; ex=" + ex );
    Logger.write( UtauWaveGenerator.class + ".begin; ex=" + ex + "\n" );
    ex.printStackTrace();
} finally {
    exitBegin();
    state.reportComplete();
}
        }

        private void debugWriteLine( String value )
        {
        }

        /// <summary>
        /// beginメソッドを抜けるときの共通処理を行います
        /// </summary>
        private void exitBegin()
        {
mRunning = false;
//mAbortRequired = false;
mReceiver.end();

// ジャンクションを消す
for ( String junction : mJunctions ) {
    PortUtil.deleteDirectory( junction );
}
        }

        private void processWavtool( Vector<String> arg, String filesuper, String temp_dir, String wavtool, boolean invoke_with_wine )
        {
Vector<String> args = new Vector<String>();
if( invoke_with_wine ){
    args.add( mWine );
}
args.add( wavtool.replace( "\\", "/" ) );
int size = vec.size( arg );
for( int i = 0; i < size; i++ ){
    vec.add( args, vec.get( arg, i ) );
}
size = vec.size( args );
for( int i = 0; i < size; i++ ){
    String s = vec.get( args, i );
    if( s.startsWith( "\"" ) && s.endsWith( "\"" ) ){
        s = str.sub( s, 1, s.length() - 2 );
    }
    vec.set( args, i, s );
}
ProcessBuilder pb = new ProcessBuilder( args );
try{
    Process process = pb.start();
    process.waitFor();
}catch( Exception ex ){
    Logger.write( UtauWaveGenerator.class + ".processWavtool; ex=" + ex + "\n" );
}
        }

        private void waveIncoming( double[] l, double[] r, int length, int sample_rate )
        {
int offset = 0;
int mTrimRemain = (int)(mTrimRemainSeconds * sample_rate);
if ( mTrimRemain > 0 ) {
    if ( length <= mTrimRemain ) {
        mTrimRemainSeconds -= (length / sample_rate);
       // mTrimRemain -= length;
        return;
    } else {
        mTrimRemainSeconds = 0.0;
        //mTrimRemain = 0;
        offset += length;// -= mTrimRemain;
    }
}
int remain = length - offset;

if ( mContext == null ) {
    try {
        mContext = new RateConvertContext( sample_rate, mSampleRate );
    } catch ( Exception ex ) {
        mContext = null;
    }
} else {
    if ( mContext.getSampleRateFrom() != sample_rate ||
         mContext.getSampleRateTo() != mSampleRate ) {
        mContext.dispose();
        mContext = null;
        try {
            mContext = new RateConvertContext( sample_rate, mSampleRate );
        } catch ( Exception ex ) {
            mContext = null;
        }
    }
}
if ( mContext == null ) {
    mTotalAppend += length;
    mState.reportProgress( mTotalAppend );
    return;
}

while ( remain > 0 ) {
    int amount = (remain > BUFLEN) ? BUFLEN : remain;
    for ( int i = 0; i < amount; i++ ) {
        mBufferL[i] = l[i + offset];
        mBufferR[i] = r[i + offset];
    }
    while ( RateConvertContext.convert( mContext, mBufferL, mBufferR, amount ) ) {
        mReceiver.push( mContext.bufferLeft, mContext.bufferRight, mContext.length );
        mTotalAppend += mContext.length;
        mState.reportProgress( mTotalAppend );
    }
    remain -= amount;
    offset += amount;
}
        }

        private static String NoteStringFromNoteNumber( int note_number )
        {
int odd = note_number % 12;
String head = (new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" })[odd];
return head + (note_number / 12 - 1);
        }
    }


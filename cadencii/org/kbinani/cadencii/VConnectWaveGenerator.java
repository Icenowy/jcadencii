/*
 * VConnectWaveGenerator.cs
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

import org.kbinani.*;

import org.kbinani.media.*;

import org.kbinani.vsq.*;

import java.awt.*;

import java.io.*;

import java.util.*;


/// <summary>
/// vConnect-STANDを使って音声合成を行う波形生成器
/// </summary>
public class VConnectWaveGenerator extends WaveUnit implements WaveGenerator {
    /// <summary>
    /// シンセサイザの実行ファイル名
    /// </summary>
    public static final String STRAIGHT_SYNTH = fsys.combine(fsys.combine(
                "usr", "bin"), "vconnect_stand");
    private static final int BUFLEN = 1024;
    private static final int VERSION = 0;
    private static final int TEMPO = 120;
    private static final int MAX_CACHE = 512;
    private static TreeMap<String, Double> mCache = new TreeMap<String, Double>();
    private double[] mBuffer2L = new double[BUFLEN];
    private double[] mBuffer2R = new double[BUFLEN];
    protected Object mLocker = null;
    protected boolean mRunning = false;
    protected long mTotalSamples = 0;

    /// <summary>
    /// WaveIncomingで追加されたサンプル数
    /// </summary>
    protected long mTotalAppend = 0;
    protected int mTrimRemain = 0;

    //protected boolean mAbortRequired = false;
    protected int mTrack = 0;
    protected int mTrimMillisec;
    protected int mSampleRate;
    private Vector<VConnectRenderingQueue> mQueue;
    private Vector<SingerConfig> mSingerConfigSys;
    private double mProgressPercent = 0.0;
    private TreeMap<String, UtauVoiceDB> mVoiceDBConfigs = new TreeMap<String, UtauVoiceDB>();
    private long mVsqLengthSamples;
    private double mStartedDate;

    /// <summary>
    /// 現在の処理速度．progress%/sec
    /// </summary>
    private double mRunningRate;
    private WaveReceiver mReceiver;
    private VsqFileEx mVsq;
    private WorkerState mState;

    public int getSampleRate() {
        return mSampleRate;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public long getPosition() {
        return mTotalAppend;
    }

    public long getTotalSamples() {
        return mTotalSamples;
    }

    public double getProgress() {
        if (mTotalSamples > 0) {
            return mTotalAppend / (double) mTotalSamples;
        } else {
            return 0.0;
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
    public int getVersion() {
        return VERSION;
    }

    public void setReceiver(WaveReceiver receiver) {
        mReceiver = receiver;
    }

    public void setConfig(String parameter) {
        //TODO:
    }

    public void init(VsqFileEx vsq, int track, int start_clock, int end_clock,
        int sample_rate) {
        // VSTiProxyの実装より
        mVsq = (VsqFileEx) vsq.clone();
        mVsq.updateTotalClocks();
        mSampleRate = sample_rate;

        if (end_clock < vsq.TotalClocks) {
            mVsq.removePart(end_clock, mVsq.TotalClocks + 480);
        }

        double trim_sec = 0.0; // レンダリング結果から省かなければならない秒数。

        if (start_clock < mVsq.getPreMeasureClocks()) {
            trim_sec = mVsq.getSecFromClock(start_clock);
        } else {
            mVsq.removePart(vsq.getPreMeasureClocks(), start_clock);
            trim_sec = mVsq.getSecFromClock(mVsq.getPreMeasureClocks());
        }

        mVsq.updateTotalClocks();

        mTrimMillisec = (int) (trim_sec * 1000.0);
        //以上VSTiProxyの実装

        // RenderingRunner.ctorの実装より
        mTrack = track;
        mSampleRate = sample_rate;

        mLocker = new Object();
        mRunning = false;
        mTotalAppend = 0;
        mTrimRemain = (int) (mTrimMillisec / 1000.0 * mSampleRate); //先頭から省かなければならないサンプル数の残り

        // StraightRenderingRunner.ctorの実装より
        mLocker = new Object();
        mQueue = new Vector<VConnectRenderingQueue>();

        if ((mConfig != null) && (mConfig.UtauSingers != null)) {
            mSingerConfigSys = mConfig.UtauSingers;
        } else {
            mSingerConfigSys = new Vector<SingerConfig>();
        }

        int midi_tempo = 60000000 / TEMPO;
        VsqFileEx work = (VsqFileEx) mVsq.clone();
        TempoVector tempo = new TempoVector();
        tempo.clear();
        tempo.add(new TempoTableEntry(0, midi_tempo, 0.0));
        tempo.updateTempoInfo();
        work.adjustClockToMatchWith(tempo);
        // テンポテーブルをクリア
        work.TempoTable.clear();
        work.TempoTable.add(new TempoTableEntry(0, midi_tempo, 0.0));
        work.updateTempoInfo();

        VsqTrack vsq_track = work.Track.get(track);
        Vector<VsqEvent> events = new Vector<VsqEvent>(); // 順次取得はめんどくさいので，一度eventsに格納してから処理しよう
        int count = vsq_track.getEventCount();
        VsqEvent current_singer_event = null;

        for (int i = 0; i < count; i++) {
            VsqEvent item = vsq_track.getEvent(i);

            if (item.ID.type == VsqIDType.Singer) {
                if ((events.size() > 0) && (current_singer_event != null)) {
                    // eventsに格納されたノートイベントについて，StraightRenderingQueueを順次作成し，登録
                    appendQueue(work, track, events, current_singer_event);
                    events.clear();
                }

                current_singer_event = item;
            } else if (item.ID.type == VsqIDType.Anote) {
                events.add(item);
            }
        }

        if ((events.size() > 0) && (current_singer_event != null)) {
            appendQueue(work, track, events, current_singer_event);
        }

        if (mQueue.size() > 0) {
            VConnectRenderingQueue q = mQueue.get(mQueue.size() - 1);
            mVsqLengthSamples = q.startSample + q.abstractSamples;
        }
    }

    public void begin(long samples, WorkerState state) {
        mState = state;
        mTotalSamples = samples;
        mStartedDate = PortUtil.getCurrentTime();
        mRunning = true;

        //mAbortRequired = false;
        double[] bufL = new double[BUFLEN];
        double[] bufR = new double[BUFLEN];
        String straight_synth = STRAIGHT_SYNTH;

        if (!fsys.isFileExists(straight_synth)) {
            exitBegin();

            return;
        }

        int count = mQueue.size();

        // 合計でレンダリングしなければならないサンプル数を計算しておく
        double total_samples = 0;

        for (int i = 0; i < count; i++) {
            total_samples += mQueue.get(i).abstractSamples;
        }

        mTrimRemain = (int) (mTrimMillisec / 1000.0 * mSampleRate); //先頭から省かなければならないサンプル数の残り

        long max_next_wave_start = mVsqLengthSamples;

        if (mQueue.size() > 0) {
            // 最初のキューが始まるまでの無音部分
            VConnectRenderingQueue queue = mQueue.get(0);

            if (queue.startSample > 0) {
                for (int i = 0; i < BUFLEN; i++) {
                    bufL[i] = 0.0;
                    bufR[i] = 0.0;
                }

                long remain = queue.startSample;

                while (remain > 0) {
                    if (state.isCancelRequested()) {
                        exitBegin();

                        return;
                    }

                    int len = (remain > BUFLEN) ? BUFLEN : (int) remain;
                    waveIncoming(bufL, bufR, len);
                    remain -= len;
                }
            }
        }

        double[] cached_data_l = new double[BUFLEN];
        double[] cached_data_r = new double[BUFLEN];
        int cached_data_length = 0;
        double processed_samples = 0.0;

        for (int i = 0; i < count; i++) {
            if (state.isCancelRequested()) {
                exitBegin();

                return;
            }

            VConnectRenderingQueue queue = mQueue.get(i);
            String tmp_dir = AppManager.getTempWaveDir();

            String tmp_file = fsys.combine(tmp_dir, "tmp.usq");
            String hash = "";
            BufferedWriter sw = null;

            try {
                sw = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(tmp_file), "Shift_JIS"));
                prepareMetaText(sw, queue.track, queue.oto_ini, queue.endClock);
            } catch (Exception ex) {
            } finally {
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (Exception ex2) {
                    }
                }
            }

            try {
                hash = PortUtil.getMD5(tmp_file).replace("_", "");
            } catch (Exception ex) {
            }

            try {
                PortUtil.copyFile(tmp_file, fsys.combine(tmp_dir, hash +
                        ".usq"));
                PortUtil.deleteFile(tmp_file);
            } catch (Exception ex) {
            }

            tmp_file = fsys.combine(tmp_dir, hash);

            if (!mCache.containsKey(hash) ||
                    !fsys.isFileExists(tmp_file + ".wav")) {
                String[] args = new String[] {
                        straight_synth.replace("\\", "\\" + "\\"),
                        tmp_file.replace("\\", "\\" + "\\") + ".usq",
                        tmp_file.replace("\\", "\\" + "\\") + ".wav"
                    };
                ProcessBuilder pb = new ProcessBuilder(args);
                pb.redirectErrorStream(true);

                try {
                    Process process = pb.start();
                    InputStream stream = process.getInputStream();

                    while ((stream.read() >= 0) && !state.isCancelRequested())
                        ;
                } catch (Exception ex) {
                    System.err.println("VConnectWaveGenerator#begin; ex=" + ex);
                }

                try {
                    PortUtil.deleteFile(tmp_file + ".usq");
                } catch (Exception ex) {
                }

                if (mCache.size() > MAX_CACHE) {
                    // キャッシュの許容個数を超えたので、古いものを削除
                    boolean first = true;
                    double old_date = PortUtil.getCurrentTime();
                    String old_key = "";

                    for (Iterator<String> itr = mCache.keySet().iterator();
                            itr.hasNext();) {
                        String key = itr.next();
                        double time = mCache.get(key);

                        if (first) {
                            old_date = time;
                            old_key = key;
                        } else {
                            if (old_date > time) {
                                old_date = time;
                                old_key = key;
                            }
                        }
                    }

                    mCache.remove(old_key);

                    try {
                        PortUtil.deleteFile(fsys.combine(tmp_dir,
                                old_key + ".wav"));
                    } catch (Exception ex) {
                    }
                }

                mCache.put(hash, PortUtil.getCurrentTime());
            }

            long next_wave_start = max_next_wave_start;

            if ((i + 1) < count) {
                VConnectRenderingQueue next_queue = mQueue.get(i + 1);
                next_wave_start = next_queue.startSample;
            }

            //WaveReader wr = null;
            WaveRateConverter wr = null;

            try {
                if (fsys.isFileExists(tmp_file + ".wav")) {
                    wr = new WaveRateConverter(new WaveReader(tmp_file +
                                ".wav"), mSampleRate);
                }
            } catch (Exception ex) {
                wr = null;
            }

            try {
                int wave_samples = 0;

                if (wr != null) {
                    wave_samples = (int) wr.getTotalSamples();
                }

                int overlapped = 0;

                if (next_wave_start <= (queue.startSample + wave_samples)) {
                    // 次のキューの開始位置が、このキューの終了位置よりも早い場合
                    // オーバーラップしているサンプル数
                    overlapped = (int) ((queue.startSample + wave_samples) -
                        next_wave_start);
                    wave_samples = (int) (next_wave_start - queue.startSample); //ここまでしか読み取らない
                }

                if (cached_data_length == 0) {
                    // キャッシュが残っていない場合
                    int remain = wave_samples;
                    long pos = 0;

                    while (remain > 0) {
                        if (state.isCancelRequested()) {
                            exitBegin();

                            return;
                        }

                        int len = (remain > BUFLEN) ? BUFLEN : remain;

                        if (wr != null) {
                            wr.read(pos, len, bufL, bufR);
                        } else {
                            for (int j = 0; j < BUFLEN; j++) {
                                bufL[j] = 0;
                                bufR[j] = 0;
                            }
                        }

                        waveIncoming(bufL, bufR, len);
                        pos += len;
                        remain -= len;
                    }

                    int rendererd_length = 0;

                    if (wr != null) {
                        rendererd_length = (int) wr.getTotalSamples();
                    }

                    if (wave_samples < rendererd_length) {
                        // 次のキューのためにデータを残す
                        if (wr != null) {
                            // 必要ならキャッシュを追加
                            if (cached_data_l.length < overlapped) {
                                cached_data_l = new double[overlapped];
                                cached_data_r = new double[overlapped];
                            }

                            // 長さが変わる
                            cached_data_length = overlapped;
                            // WAVEから読み込み
                            wr.read(pos, overlapped, cached_data_l,
                                cached_data_r);
                        }
                    } else if ((i + 1) < count) {
                        // 次のキューのためにデータを残す必要がない場合で、かつ、最後のキューでない場合。
                        // キュー間の無音部分を0で埋める
                        int silence_samples = (int) (next_wave_start -
                            (queue.startSample + rendererd_length));

                        for (int j = 0; j < BUFLEN; j++) {
                            bufL[j] = 0.0;
                            bufR[j] = 0.0;
                        }

                        while (silence_samples > 0) {
                            int amount = (silence_samples > BUFLEN) ? BUFLEN
                                                                    : silence_samples;
                            waveIncoming(bufL, bufR, amount);
                            silence_samples -= amount;
                        }
                    }
                } else {
                    // キャッシュが残っている場合
                    int rendered_length = 0;

                    if (wr != null) {
                        rendered_length = (int) wr.getTotalSamples();
                    }

                    if (rendered_length < cached_data_length) {
                        if (next_wave_start < (queue.startSample +
                                cached_data_length)) {
                            // PATTERN A
                            //  ----[*****************************]----------------->  cache
                            //  ----[*********************]------------------------->  renderd result
                            //  -----------------[******************************...->  next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[***********]----------------------------------->  append
                            //  -----------------[********][******]----------------->  new cache
                            //  
                            //                         OR
                            // PATTERN B
                            //  ----[*****************************]----------------->   cache
                            //  ----[***************]------------------------------->   rendered result
                            //  ----------------------------[*******************...->   next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[***************][*****]------------------------>   append
                            //  ----------------------------[*****]----------------->   new chache
                            //  
                            try {
                                // レンダリング結果とキャッシュをMIX
                                int remain = rendered_length;
                                int offset = 0;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        cached_data_l[j + offset] += bufL[j];
                                        cached_data_r[j + offset] += bufR[j];
                                    }

                                    offset += amount;
                                    remain -= amount;
                                }

                                int append_len = (int) (next_wave_start -
                                    queue.startSample);
                                waveIncoming(cached_data_l, cached_data_r,
                                    append_len);

                                // 送信したキャッシュの部分をシフト
                                // この場合，シフト後のキャッシュの長さは，元の長さより短くならないのでリサイズ不要
                                for (int j = append_len;
                                        j < cached_data_length; j++) {
                                    cached_data_l[j - append_len] = cached_data_l[j];
                                    cached_data_r[j - append_len] = cached_data_r[j];
                                }

                                cached_data_length -= append_len;
                            } catch (Exception ex) {
                                AppManager.debugWriteLine(
                                    "VConnectWaveGenerator#begin; (A),(B); ex=" +
                                    ex);
                            }
                        } else {
                            // PATTERN C
                            //  ----[*****************************]----------------->   cache
                            //  ----[***************]------------------------------->   rendered result
                            //  -----------------------------------------[******...->   next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[*****************************]----------------->   append
                            //  ---------------------------------------------------->   new chache -> NULL!!
                            //  -----------------------------------[****]----------->   append#2 (silence)
                            //  
                            try {
                                // レンダリング結果とキャッシュをMIX
                                int remain = rendered_length;
                                int offset = 0;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        cached_data_l[j + offset] += bufL[j];
                                        cached_data_r[j + offset] += bufR[j];
                                    }

                                    remain -= amount;
                                    offset += amount;
                                }

                                // MIXした分を送信
                                waveIncoming(cached_data_l, cached_data_r,
                                    cached_data_length);

                                // 隙間を無音で埋める
                                for (int j = 0; j < BUFLEN; j++) {
                                    bufL[j] = 0;
                                    bufR[j] = 0;
                                }

                                int silence_len = (int) (next_wave_start -
                                    (queue.startSample + cached_data_length));
                                remain = silence_len;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    waveIncoming(bufL, bufR, amount);
                                    remain -= amount;
                                }

                                // キャッシュの長さは0になる
                                cached_data_length = 0;
                            } catch (Exception ex) {
                                AppManager.debugWriteLine(
                                    "VConnectWaveGenerator#begin; (C); ex=" +
                                    ex);
                            }
                        }
                    } else {
                        if (next_wave_start < (queue.startSample +
                                cached_data_length)) {
                            // PATTERN D
                            //  ----[*************]--------------------------------->  cache
                            //  ----[*********************]------------------------->  renderd result
                            //  ------------[***********************************...->  next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[******]---------------------------------------->  append
                            //  ------------[*****][******]------------------------->  new cache
                            //  
                            try {
                                // 次のキューの直前の部分まで，レンダリング結果を読み込んでMIX，送信
                                int append_len = (int) (next_wave_start -
                                    queue.startSample);
                                int remain = append_len;
                                int offset = 0;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        bufL[j] += cached_data_l[j + offset];
                                        bufR[j] += cached_data_r[j + offset];
                                    }

                                    waveIncoming(bufL, bufR, amount);
                                    offset += amount;
                                    remain -= amount;
                                }

                                // まだMIXしていないcacheをシフト
                                for (int j = append_len;
                                        j < cached_data_length; j++) {
                                    cached_data_l[j - append_len] = cached_data_l[j];
                                    cached_data_r[j - append_len] = cached_data_r[j];
                                }

                                // 0で埋める
                                for (int j = cached_data_length - append_len;
                                        j < cached_data_l.length; j++) {
                                    cached_data_l[j] = 0;
                                    cached_data_r[j] = 0;
                                }

                                // キャッシュの長さを更新
                                int old_cache_length = cached_data_length;
                                int new_cache_len = (int) ((queue.startSample +
                                    rendered_length) - next_wave_start);

                                if (cached_data_l.length < new_cache_len) {
                                    cached_data_l = new double[new_cache_len];
                                    cached_data_r = new double[new_cache_len];
                                }

                                cached_data_length = new_cache_len;

                                // 残りのレンダリング結果をMIX
                                remain = rendered_length - append_len;
                                offset = append_len;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        cached_data_l[(j + offset) -
                                        append_len] += bufL[j];
                                        cached_data_r[(j + offset) -
                                        append_len] += bufR[j];
                                    }

                                    remain -= amount;
                                    offset += amount;
                                }
                            } catch (Exception ex) {
                                AppManager.debugWriteLine(
                                    "VConnectWaveGenerator#begin; (D); ex=" +
                                    ex);
                            }
                        } else if (next_wave_start < (queue.startSample +
                                rendered_length)) {
                            // PATTERN E
                            //  ----[*************]--------------------------------->  cache
                            //  ----[*********************]------------------------->  renderd result
                            //  ----------------------[*************************...->  next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[*************][*]------------------------------>  append
                            //  ----------------------[***]------------------------->  new cache
                            //  
                            try {
                                // キャッシュとレンダリング結果をMIX
                                int remain = cached_data_length;
                                int offset = 0;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        cached_data_l[j + offset] += bufL[j];
                                        cached_data_r[j + offset] += bufR[j];
                                    }

                                    remain -= amount;
                                    offset += amount;
                                }

                                // 送信
                                waveIncoming(cached_data_l, cached_data_r,
                                    cached_data_length);

                                // キャッシュと，次のキューの隙間の部分
                                // レンダリング結果をそのまま送信
                                remain = (int) (next_wave_start -
                                    (queue.startSample + cached_data_length));

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);
                                    waveIncoming(bufL, bufR, amount);
                                    remain -= amount;
                                    offset += amount;
                                }

                                // レンダリング結果と，次のキューが重なっている部分をキャッシュに残す
                                remain = (int) ((queue.startSample +
                                    rendered_length) - next_wave_start);

                                // キャッシュが足りなければ更新
                                if (cached_data_l.length < remain) {
                                    cached_data_l = new double[remain];
                                    cached_data_r = new double[remain];
                                }

                                cached_data_length = remain;
                                // レンダリング結果を読み込む
                                wr.read(offset, remain, cached_data_l,
                                    cached_data_r);
                            } catch (Exception ex) {
                                AppManager.debugWriteLine(
                                    "VConnectWaveGenerator#begin; (E); ex=" +
                                    ex);
                            }
                        } else {
                            // PATTERN F
                            //  ----[*************]--------------------------------->  cache
                            //  ----[*********************]------------------------->  renderd result
                            //  --------------------------------[***************...->  next rendering queue (not rendered yet)
                            //                  ||
                            //                  \/
                            //  ----[*************][******]------------------------->  append
                            //  ---------------------------------------------------->  new cache -> NULL!!
                            //  ---------------------------[***]-------------------->  append#2 (silence)
                            //  
                            try {
                                // レンダリング結果とキャッシュをMIXして送信
                                int remain = cached_data_length;
                                int offset = 0;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);

                                    for (int j = 0; j < amount; j++) {
                                        bufL[j] += cached_data_l[j + offset];
                                        bufR[j] += cached_data_r[j + offset];
                                    }

                                    waveIncoming(bufL, bufR, amount);
                                    remain -= amount;
                                    offset += amount;
                                }

                                // 残りのレンダリング結果を送信
                                remain = rendered_length - cached_data_length;

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    wr.read(offset, amount, bufL, bufR);
                                    waveIncoming(bufL, bufR, amount);
                                    offset += amount;
                                    remain -= amount;
                                }

                                // 無音部分を送信
                                remain = (int) (next_wave_start -
                                    (queue.startSample + rendered_length));

                                for (int j = 0; j < BUFLEN; j++) {
                                    bufL[j] = 0;
                                    bufR[j] = 0;
                                }

                                while (remain > 0) {
                                    int amount = (remain > BUFLEN) ? BUFLEN
                                                                   : remain;
                                    waveIncoming(bufL, bufR, amount);
                                    remain -= amount;
                                }

                                // キャッシュは無くなる
                                cached_data_length = 0;
                            } catch (Exception ex) {
                                AppManager.debugWriteLine(
                                    "VConnectWaveGenerator#begin; (F); ex=" +
                                    ex);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                serr.println("VConnectWaveGenerator#begin; ex=" + ex);
            } finally {
                if (wr != null) {
                    try {
                        wr.close();
                    } catch (Exception ex2) {
                        serr.println("VConnectWaveGenerator#begin; ex2=" + ex2);
                    }

                    wr = null;
                }
            }

            processed_samples += queue.abstractSamples;
            mProgressPercent = processed_samples / total_samples * 100.0;

            double elapsed = PortUtil.getCurrentTime() - mStartedDate;
            mRunningRate = mProgressPercent / elapsed;
        }

        // 足りない分を無音で埋める
        for (int i = 0; i < BUFLEN; i++) {
            bufL[i] = 0;
            bufR[i] = 0;
        }

        int tremain = (int) (mTotalSamples - mTotalAppend);

        while ((tremain > 0) && !state.isCancelRequested()) {
            int tlength = (tremain > BUFLEN) ? BUFLEN : tremain;
            waveIncoming(bufL, bufR, tlength);
            tremain -= tlength;
        }

        exitBegin();
        state.reportComplete();
    }

    /// <summary>
    /// beginメソッドから抜ける直前に行う処理
    /// </summary>
    private void exitBegin() {
        //mAbortRequired = false;
        mRunning = false;
        mReceiver.end();
    }

    private void waveIncoming(double[] L, double[] R, int length) {
        if (!mRunning) {
            return;
        }

        synchronized (mLocker) {
            int offset = 0;

            if (mTrimRemain > 0) {
                if (length <= mTrimRemain) {
                    mTrimRemain -= length;

                    return;
                } else {
                    offset = mTrimRemain;
                    mTrimRemain = 0;
                }
            }

            if (mReceiver != null) {
                int remain = length - offset;

                while (remain > 0) {
                    int amount = (remain > BUFLEN) ? BUFLEN : remain;

                    for (int i = 0; i < amount; i++) {
                        mBuffer2L[i] = L[i + offset];
                        mBuffer2R[i] = R[i + offset];
                    }

                    mReceiver.push(mBuffer2L, mBuffer2R, amount);
                    offset += amount;
                    remain -= amount;
                    mTotalAppend += amount;
                    mState.reportProgress(mTotalAppend);
                }
            }
        }
    }

    private void appendQueue(VsqFileEx vsq, int track, Vector<VsqEvent> events,
        VsqEvent singer_event) {
        int count = events.size();

        if (count <= 0) {
            return;
        }

        VsqEvent current = events.get(0);
        VsqEvent next = null;

        String singer = singer_event.ID.IconHandle.IDS;
        int num_singers = mSingerConfigSys.size();
        String singer_path = "";

        for (int i = 0; i < num_singers; i++) {
            SingerConfig sc = mSingerConfigSys.get(i);

            if (sc.VOICENAME.equals(singer)) {
                singer_path = sc.VOICEIDSTR;

                break;
            }
        }

        // 歌手のパスが取得できないので離脱
        if (singer_path.equals("")) {
            return;
        }

        String oto_ini = fsys.combine(singer_path, "oto.ini");

        if (!fsys.isFileExists(oto_ini)) {
            // STRAIGHT合成用のoto.iniが存在しないので離脱
            return;
        }

        // 原音設定を取得
        UtauVoiceDB voicedb = null;

        if (mVoiceDBConfigs.containsKey(oto_ini)) {
            voicedb = mVoiceDBConfigs.get(oto_ini);
        } else {
            SingerConfig sc = new SingerConfig();
            sc.VOICEIDSTR = PortUtil.getDirectoryName(oto_ini);
            sc.VOICENAME = singer;
            voicedb = new UtauVoiceDB(sc);
            mVoiceDBConfigs.put(oto_ini, voicedb);
        }

        // eventsのなかから、音源が存在しないものを削除
        for (int i = count - 1; i >= 0; i--) {
            VsqEvent item = events.get(i);
            String search = item.ID.LyricHandle.L0.Phrase;
            OtoArgs oa = voicedb.attachFileNameFromLyric(search);

            if ((oa.fileName == null) ||
                    ((oa.fileName != null) && oa.fileName.equals(""))) {
                events.removeElementAt(i);
            }
        }

        Vector<VsqEvent> list = new Vector<VsqEvent>();

        count = events.size();

        for (int i = 1; i < (count + 1); i++) {
            if (i == count) {
                next = null;
            } else {
                next = events.get(i);
            }

            double current_sec_start = vsq.getSecFromClock(current.Clock) -
                (current.UstEvent.getPreUtterance() / 1000.0);
            double current_sec_end = vsq.getSecFromClock(current.Clock +
                    current.ID.getLength());
            double next_sec_start = Double.MAX_VALUE;

            if (next != null) {
                // 次の音符の開始位置
                next_sec_start = vsq.getSecFromClock(next.Clock) -
                    (current.UstEvent.getPreUtterance() / 1000.0) +
                    (current.UstEvent.getVoiceOverlap() / 1000.0);

                if (next_sec_start < current_sec_end) {
                    // 先行発音によって，現在取り扱っている音符「current」の終了時刻がずれる.
                    current_sec_end = next_sec_start;
                }
            }

            list.add(current);

            // 前の音符との間隔が100ms以下なら，連続していると判断
            if (((next_sec_start - current_sec_end) > 0.1) &&
                    (list.size() > 0)) {
                appendQueueCor(vsq, track, list, oto_ini);
                list.clear();
            }

            // 処理後
            current = next;
        }

        if (list.size() > 0) {
            appendQueueCor(vsq, track, list, oto_ini);
        }
    }

    /// <summary>
    /// 連続した音符を元に，StraightRenderingQueueを作成
    /// </summary>
    /// <param name="list"></param>
    private void appendQueueCor(VsqFileEx vsq, int track,
        Vector<VsqEvent> list, String oto_ini) {
        if (list.size() <= 0) {
            return;
        }

        int OFFSET = 1920;
        CurveType[] CURVE = new CurveType[] {
                CurveType.PIT, CurveType.PBS, CurveType.DYN, CurveType.BRE,
                CurveType.GEN, CurveType.CLE, CurveType.BRI,
            };

        VsqTrack vsq_track = (VsqTrack) vsq.Track.get(track).clone();
        VsqEvent ve0 = list.get(0);
        int first_clock = ve0.Clock;
        int last_clock = ve0.Clock + ve0.ID.getLength();

        if (list.size() > 1) {
            VsqEvent ve9 = list.get(list.size() - 1);
            last_clock = ve9.Clock + ve9.ID.getLength();
        }

        double start_sec = vsq.getSecFromClock(first_clock); // 最初の音符の，曲頭からの時間
        int clock_shift = OFFSET - first_clock; // 最初の音符が，ダミー・トラックのOFFSET clockから始まるようシフトする．

        // listの内容を転写
        vsq_track.MetaText.Events.clear();

        int count = list.size();

        for (int i = 0; i < count; i++) {
            VsqEvent ev = (VsqEvent) list.get(i).clone();
            ev.Clock = ev.Clock + clock_shift;
            vsq_track.MetaText.Events.add(ev);
        }

        // コントロールカーブのクロックをシフトする
        count = CURVE.length;

        for (int i = 0; i < count; i++) {
            CurveType curve = CURVE[i];
            VsqBPList work = vsq_track.getCurve(curve.getName());

            if (work == null) {
                continue;
            }

            VsqBPList src = (VsqBPList) work.clone();
            int value_at_first_clock = work.getValue(first_clock);
            work.clear();
            work.add(0, value_at_first_clock);

            int num_points = src.size();

            for (int j = 0; j < num_points; j++) {
                int clock = src.getKeyClock(j);

                if ((0 <= (clock + clock_shift)) &&
                        ((clock + clock_shift) <= (last_clock + clock_shift +
                        1920))) { // 4拍分の余裕を持って・・・

                    int value = src.getElementA(j);
                    work.add(clock + clock_shift, value);
                }
            }
        }

        // 最後のクロックがいくつかを調べる
        int tlast_clock = 0;

        for (Iterator<VsqEvent> itr = vsq_track.getNoteEventIterator();
                itr.hasNext();) {
            VsqEvent item = itr.next();
            tlast_clock = item.Clock + item.ID.getLength();
        }

        double abstract_sec = tlast_clock / (8.0 * TEMPO);

        VConnectRenderingQueue queue = new VConnectRenderingQueue();
        // レンダリング結果の何秒後に音符が始まるか？
        queue.startSample = (int) ((start_sec - (OFFSET / (8.0 * TEMPO))) * mSampleRate);
        queue.oto_ini = oto_ini;
        queue.abstractSamples = (long) (abstract_sec * mSampleRate);
        queue.endClock = last_clock + clock_shift + 1920;
        queue.track = vsq_track;
        mQueue.add(queue);
    }

    /// <summary>
    /// 合成用のメタテキストを生成します
    /// </summary>
    /// <param name="writer">テキストの出力先</param>
    /// <param name="vsq_track">出力対象のトラック</param>
    /// <param name="oto_ini">原音設定ファイルのパス</param>
    /// <param name="end_clock"></param>
    public static void prepareMetaText(BufferedWriter writer,
        VsqTrack vsq_track, String oto_ini, int end_clock) {
        prepareMetaText(writer, vsq_track, oto_ini, end_clock, true);
    }

    /// <summary>
    /// 合成用のメタテキストを生成します
    /// </summary>
    /// <param name="writer"></param>
    /// <param name="vsq_track"></param>
    /// <param name="oto_ini"></param>
    /// <param name="end_clock"></param>
    /// <param name="world_mode"></param>
    public static void prepareMetaText(BufferedWriter writer,
        VsqTrack vsq_track, String oto_ini, int end_clock, boolean world_mode) {
        TreeMap<String, String> dict_singername_otoini = new TreeMap<String, String>();
        dict_singername_otoini.put("", oto_ini);
        prepareMetaText(writer, vsq_track, dict_singername_otoini, end_clock,
            world_mode);
    }

    /// <summary>
    /// 合成用のメタテキストを生成します
    /// </summary>
    /// <param name="writer"></param>
    /// <param name="vsq_track"></param>
    /// <param name="oto_ini"></param>
    /// <param name="end_clock"></param>
    private static void prepareMetaText(BufferedWriter writer,
        VsqTrack vsq_track, TreeMap<String, String> dict_singername_otoini,
        int end_clock, boolean world_mode) {
        CurveType[] CURVE = new CurveType[] {
                CurveType.PIT, CurveType.PBS, CurveType.DYN, CurveType.BRE,
                CurveType.GEN, CurveType.CLE, CurveType.BRI,
            };

        // メモリーストリームに出力
        try {
            writer.write("[Tempo]");
            writer.newLine();
            writer.write(TEMPO + "");
            writer.newLine();
            writer.write("[oto.ini]");
            writer.newLine();

            for (Iterator<String> itr = dict_singername_otoini.keySet()
                                                              .iterator();
                    itr.hasNext();) {
                String singername = itr.next();
                String oto_ini = dict_singername_otoini.get(singername);

                if (world_mode) {
                    writer.write(singername + "\t" + oto_ini);
                    writer.newLine();
                } else {
                    writer.write(oto_ini);
                    writer.newLine();

                    break;
                }
            }

            Vector<VsqHandle> handles = vsq_track.MetaText.writeEventList(writer,
                    end_clock);
            Vector<String> print_targets = new Vector<String>(Arrays.asList(
                        new String[] {
                            "Length", "Note#", "Dynamics", "DEMdecGainRate",
                            "DEMaccent", "PreUtterance", "VoiceOverlap",
                            "PMBendDepth", "PMBendLength", "PMbPortamentoUse",
                        }));

            for (Iterator<VsqEvent> itr = vsq_track.getEventIterator();
                    itr.hasNext();) {
                VsqEvent item = itr.next();
                item.write(writer, print_targets);
            }

            int count = handles.size();

            for (int i = 0; i < count; i++) {
                handles.get(i).write(writer);
            }

            count = CURVE.length;

            for (int i = 0; i < count; i++) {
                CurveType curve = CURVE[i];
                VsqBPList src = vsq_track.getCurve(curve.getName());

                if (src == null) {
                    continue;
                }

                String name = "";

                if (curve.equals(CurveType.PIT)) {
                    name = "[PitchBendBPList]";
                } else if (curve.equals(CurveType.PBS)) {
                    name = "[PitchBendSensBPList]";
                } else if (curve.equals(CurveType.DYN)) {
                    name = "[DynamicsBPList]";
                } else if (curve.equals(CurveType.BRE)) {
                    name = "[EpRResidualBPList]";
                } else if (curve.equals(CurveType.GEN)) {
                    name = "[GenderFactorBPList]";
                } else if (curve.equals(CurveType.BRI)) {
                    name = "[EpRESlopeBPList]";
                } else if (curve.equals(CurveType.CLE)) {
                    name = "[EpRESlopeDepthBPList]";
                } else {
                    continue;
                }

                src.print(writer, 0, name);
            }
        } catch (Exception ex) {
            Logger.write(VConnectWaveGenerator.class + ".prepareMetaText; ex=" +
                ex + "\n");
        }
    }

    public static void clearCache() {
        String tmp_dir = AppManager.getTempWaveDir();

        for (Iterator<String> itr = mCache.keySet().iterator(); itr.hasNext();) {
            String key = itr.next();

            try {
                PortUtil.deleteFile(fsys.combine(tmp_dir, key + ".wav"));
            } catch (Exception ex) {
            }
        }

        mCache.clear();
    }
}

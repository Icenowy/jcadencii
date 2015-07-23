/*
 * VocaloidWaveGenerator.cs
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


public class VocaloidWaveGenerator extends WaveUnit implements WaveGenerator {
    private static final int BUFLEN = 1024;
    private static final int VERSION = 0;
    private long mTotalAppend = 0;
    private VsqFileEx mVsq = null;
    private int mTrack;
    private int mStartClock;
    private int mEndClock;
    private long mTotalSamples;

    //private boolean mAbortRequired = false;
    private double[] mBufferL = new double[BUFLEN];
    private double[] mBufferR = new double[BUFLEN];
    private WaveReceiver mReceiver = null;
    private int mTrimRemain = 0;
    private boolean mRunning = false;

    /// <summary>
    /// 波形処理ラインのサンプリング周波数
    /// </summary>
    private int mSampleRate;

    /// <summary>
    /// VOCALOID VSTiの実際のサンプリング周波数
    /// </summary>
    private int mDriverSampleRate;

    /// <summary>
    /// サンプリング周波数変換器
    /// </summary>
    private RateConvertContext mContext;

    //private WorkerState mState;
    public int getSampleRate() {
        return mSampleRate;
    }

    public boolean isRunning() {
        return mRunning;
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

    public void setConfig(String parameter) {
        // do nothing
    }

    /// <summary>
    /// 初期化メソッド．
    /// </summary>
    /// <param name="vsq"></param>
    /// <param name="track"></param>
    /// <param name="start_clock"></param>
    /// <param name="end_clock"></param>
    /// <param name="sample_rate">波形処理ラインのサンプリング周波数</param>
    public void init(VsqFileEx vsq, int track, int start_clock, int end_clock,
        int sample_rate) {
        mVsq = vsq;
        mTrack = track;
        mStartClock = start_clock;
        mEndClock = end_clock;
        mSampleRate = sample_rate;
        mDriverSampleRate = 44100;

        try {
            mContext = new RateConvertContext(mDriverSampleRate, mSampleRate);
        } catch (Exception ex) {
            try {
                // 苦肉の策
                mContext = new RateConvertContext(mDriverSampleRate,
                        mDriverSampleRate);
            } catch (Exception ex2) {
            }
        }
    }

    public int getVersion() {
        return VERSION;
    }

    public void setReceiver(WaveReceiver r) {
        if (mReceiver != null) {
            mReceiver.end();
        }

        mReceiver = r;
    }

    /// <summary>
    /// VSTiドライバに呼んでもらう波形受け渡しのためのコールバック関数にして、IWaveIncomingインターフェースの実装。
    /// </summary>
    /// <param name="l"></param>
    /// <param name="r"></param>
    /// <param name="length"></param>
    public void waveIncomingImpl(double[] l, double[] r, int length,
        WorkerState state) {
        int offset = 0;

        if (mTrimRemain > 0) {
            // トリムしなくちゃいけない分がまだ残っている場合。トリム処理を行う。
            if (length <= mTrimRemain) {
                // 受け取った波形の長さをもってしても、トリム分が0にならない場合
                mTrimRemain -= length;

                return;
            } else {
                // 受け取った波形の内の一部をトリムし、残りを波形レシーバに渡す
                offset = mTrimRemain;
                // これにてトリム処理は終了なので。
                mTrimRemain = 0;
            }
        }

        int remain = length - offset;

        while (remain > 0) {
            if (state.isCancelRequested()) {
                return;
            }

            int amount = (remain > BUFLEN) ? BUFLEN : remain;

            for (int i = 0; i < amount; i++) {
                mBufferL[i] = l[i + offset];
                mBufferR[i] = r[i + offset];
            }

            while (RateConvertContext.convert(mContext, mBufferL, mBufferR,
                        amount)) {
                mReceiver.push(mContext.bufferLeft, mContext.bufferRight,
                    mContext.length);
                mTotalAppend += mContext.length;
                state.reportProgress(mTotalAppend);
            }

            remain -= amount;
            offset += amount;
        }

        return;
    }

    /// <summary>
    /// beginメソッドを抜けるときに共通する処理を行います
    /// </summary>
    private void exitBegin() {
        mReceiver.end();
        mRunning = false;
    }

    public void begin(long total_samples, WorkerState state) {
        // 渡されたVSQの、合成に不要な部分を削除する
        VsqFileEx split = (VsqFileEx) mVsq.clone();
        VsqTrack vsq_track = split.Track.get(mTrack);
        split.updateTotalClocks();

        if (mEndClock < mVsq.TotalClocks) {
            split.removePart(mEndClock, split.TotalClocks + 480);
        }

        double start_sec = mVsq.getSecFromClock(mStartClock);
        double end_sec = mVsq.getSecFromClock(mEndClock);

        // トラックの合成エンジンの種類
        RendererKind s_working_renderer = VsqFileEx.getTrackRendererKind(vsq_track);

        // VOCALOIDのドライバの場合，末尾に余分な音符を入れる
        int extra_note_clock = (int) mVsq.getClockFromSec((float) end_sec +
                10.0f);
        int extra_note_clock_end = (int) mVsq.getClockFromSec((float) end_sec +
                10.0f + 3.1f); //ブロックサイズが1秒分で、バッファの個数が3だから +3.1f。0.1fは安全のため。
        VsqEvent extra_note = new VsqEvent(extra_note_clock, new VsqID(0));
        extra_note.ID.type = VsqIDType.Anote;
        extra_note.ID.Note = 60;
        extra_note.ID.setLength(extra_note_clock_end - extra_note_clock);
        extra_note.ID.VibratoHandle = null;
        extra_note.ID.LyricHandle = new LyricHandle("a", "a");
        vsq_track.addEvent(extra_note);

        // VSTiが渡してくる波形のうち、先頭からtrim_sec秒分だけ省かないといけない
        // プリセンドタイムがあるので、無条件に合成開始位置以前のデータを削除すると駄目なので。
        double trim_sec = 0.0;

        if (mStartClock < split.getPreMeasureClocks()) {
            // 合成開始位置が、プリメジャーよりも早い位置にある場合。
            // VSTiにはクロック0からのデータを渡し、クロック0から合成開始位置までをこのインスタンスでトリム処理する
            trim_sec = split.getSecFromClock(mStartClock);
        } else {
            // 合成開始位置が、プリメジャー以降にある場合。
            // プリメジャーの終了位置から合成開始位置までのデータを削除する
            split.removePart(mVsq.getPreMeasureClocks(), mStartClock);
            trim_sec = split.getSecFromClock(split.getPreMeasureClocks());
        }

        split.updateTotalClocks();

        // NRPNを作成
        int ms_present = mConfig.PreSendTime;
        VsqNrpn[] vsq_nrpn = VsqFile.generateNRPN(split, mTrack, ms_present);
        NrpnData[] nrpn = VsqNrpn.convert(vsq_nrpn);

        // 最初のテンポ指定を検索
        // VOCALOID VSTiが返してくる波形にはなぜかずれがある。このズレは最初のテンポで決まるので。
        float first_tempo = 125.0f;

        if (split.TempoTable.size() > 0) {
            first_tempo = (float) (60e6 / (double) split.TempoTable.get(0).Tempo);
        }

        // ずれるサンプル数
        int errorSamples = VSTiDllManager.getErrorSamples(first_tempo);
        // 今後トリムする予定のサンプル数と、
        mTrimRemain = errorSamples + (int) (trim_sec * mDriverSampleRate);
        // 合計合成する予定のサンプル数を決める
        mTotalSamples = (long) ((end_sec - start_sec) * mDriverSampleRate) +
            errorSamples;

        // アボート要求フラグを初期化
        //mAbortRequired = false;
        int ver = (s_working_renderer == RendererKind.VOCALOID2) ? 2 : 1;
        VocaloidDaemon vd = VSTiDllManager.vocaloidrvDaemon[ver - 1];

        if (vd == null) {
            exitBegin();

            return;
        }

        // 停止処理用のファイルが残っていたら消去する
        String stp = fsys.combine(vd.getTempPathUnixName(), "stop");

        if (fsys.isFileExists(stp)) {
            try {
                PortUtil.deleteFile(stp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ここにきて初めて再生中フラグが立つ
        mRunning = true;

        // ドライバーに渡すイベントを準備
        // まず、マスタートラックに渡すテンポ変更イベントを作成
        int tempo_count = split.TempoTable.size();
        byte[] masterEventsSrc = new byte[tempo_count * 3];
        int[] masterClocksSrc = new int[tempo_count];
        int count = -3;

        for (int i = 0; i < tempo_count; i++) {
            count += 3;

            TempoTableEntry itemi = split.TempoTable.get(i);
            masterClocksSrc[i] = itemi.Clock;

            byte b0 = (byte) (0xff & (itemi.Tempo >> 16));
            long u0 = (long) (itemi.Tempo - (b0 << 16));
            byte b1 = (byte) (0xff & (u0 >> 8));
            byte b2 = (byte) (0xff & (u0 - (u0 << 8)));
            masterEventsSrc[count] = b0;
            masterEventsSrc[count + 1] = b1;
            masterEventsSrc[count + 2] = b2;
        }

        // 次に、合成対象トラックの音符イベントを作成
        int numEvents = nrpn.length;
        byte[] bodyEventsSrc = new byte[numEvents * 3];
        int[] bodyClocksSrc = new int[numEvents];
        count = -3;

        int last_clock = 0;

        for (int i = 0; i < numEvents; i++) {
            int c = nrpn[i].getClock();
            count += 3;
            bodyEventsSrc[count] = (byte) 0xb0;
            bodyEventsSrc[count + 1] = (byte) (0xff & nrpn[i].getParameter());
            bodyEventsSrc[count + 2] = (byte) (0xff & nrpn[i].Value);
            bodyClocksSrc[i] = c;
            last_clock = c;
        }

        // 合成を開始
        // 合成が終わるか、ドライバへのアボート要求が来るまでは制御は返らない
        try {
            BufferedOutputStream out = vd.outputStream; // process.getOutputStream();
            BufferedInputStream in = vd.inputStream;

            // もしかしたら前回レンダリング時のが残っているかもしれないので，取り除く
            int avail = in.available();

            for (int i = 0; i < avail; i++) {
                in.read();
            }

            // コマンドを送信
            // マスタートラック
            out.write(0x01);
            out.write(0x04);

            byte[] buf = PortUtil.getbytes_uint32_le(tempo_count);
            out.write(buf, 0, 4);
            out.flush();
            count = 0;

            for (int i = 0; i < tempo_count; i++) {
                buf = PortUtil.getbytes_uint32_le(masterClocksSrc[i]);
                out.write(buf, 0, 4);
                out.write(masterEventsSrc, count, 3);
                count += 3;
            }

            out.flush();
            // 本体トラック
            out.write(0x02);
            out.write(0x04);
            buf = PortUtil.getbytes_uint32_le(numEvents);
            out.write(buf, 0, 4);
            out.flush();
            count = 0;

            for (int i = 0; i < numEvents; i++) {
                buf = PortUtil.getbytes_uint32_le(bodyClocksSrc[i]);
                out.write(buf, 0, 4);
                out.write(bodyEventsSrc, count, 3);
                count += 3;
            }

            out.flush();

            // 合成開始コマンド
            long act_total_samples = mTotalSamples + mTrimRemain;
            out.write(0x03);
            out.write(0x08);
            buf = PortUtil.getbytes_int64_le(act_total_samples);
            out.write(buf, 0, 8);
            out.flush();

            long remain = act_total_samples;
            final int BUFLEN = 1024;
            double[] l = new double[BUFLEN];
            double[] r = new double[BUFLEN];

            while (remain > 0) {
                if (state.isCancelRequested()) {
                    break;
                }

                int amount = (remain > BUFLEN) ? BUFLEN : (int) remain;

                for (int i = 0; i < amount; i++) {
                    // 4バイト以上のデータが読み込めるようになるまで待機
                    while ((in.available() < 4) && !state.isCancelRequested()) {
                        Thread.sleep(100);
                    }

                    if (state.isCancelRequested()) {
                        break;
                    }

                    int lh = in.read();
                    int ll = in.read();
                    int rh = in.read();
                    int rl = in.read();
                    short il = (short) ((0xffff & ((0xff & lh) << 8)) |
                        (0xff & ll));
                    short ir = (short) ((0xffff & ((0xff & rh) << 8)) |
                        (0xff & rl));
                    l[i] = il / 32768.0;
                    r[i] = ir / 32768.0;
                }

                if (state.isCancelRequested()) {
                    break;
                }

                waveIncomingImpl(l, r, amount, state);
                remain -= amount;
            }

            if (state.isCancelRequested()) {
                // デーモンに合成処理の停止を要求
                String monitor_dir = vd.getTempPathUnixName();
                String stop = fsys.combine(monitor_dir, "stop");
                (new FileOutputStream(stop)).close();
            }

            // 途中でアボートした場合に備え，取り残しのstdoutを読み取っておく
            remain = in.available();

            for (long i = 0; i < remain; i++) {
                in.read();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ここに来るということは合成が終わったか、ドライバへのアボート要求が実行されたってこと。
        // このインスタンスが受け持っている波形レシーバに、処理終了を知らせる。
        exitBegin();

        if (state.isCancelRequested() == false) {
            state.reportComplete();
        }
    }

    public long getPosition() {
        return mTotalAppend;
    }
}

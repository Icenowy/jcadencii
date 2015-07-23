/*
 * EmptyWaveGenerator.cs
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


/// <summary>
/// 無音の波形を送信するWaveGenerator
/// </summary>
public class EmptyWaveGenerator extends WaveUnit implements WaveGenerator {
    private static final int VERSION = 0;
    private static final int BUFLEN = 1024;
    private WaveReceiver mReceiver = null;
    private boolean mAbortRequested = false;
    private boolean mRunning = false;
    private long mTotalAppend = 0L;
    private long mTotalSamples = 0L;
    private int mSampleRate = 0;

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
        if (mTotalSamples <= 0) {
            return 0.0;
        } else {
            return mTotalAppend / (double) mTotalSamples;
        }
    }

    public int getVersion() {
        return VERSION;
    }

    public void setConfig(String parameter) {
        // do nothing
    }

    public void begin(long samples, WorkerState state) {
        if (mReceiver == null) {
            return;
        }

        mRunning = true;
        mTotalSamples = samples;

        double[] l = new double[BUFLEN];
        double[] r = new double[BUFLEN];

        for (int i = 0; i < BUFLEN; i++) {
            l[i] = 0.0;
            r[i] = 0.0;
        }

        long remain = samples;

        while ((remain > 0) && !mAbortRequested) {
            int amount = (remain > BUFLEN) ? BUFLEN : (int) remain;
            mReceiver.push(l, r, amount);
            remain -= amount;
            mTotalAppend += amount;
        }

        mRunning = false;
        mReceiver.end();
    }

    public void setReceiver(WaveReceiver receiver) {
        mReceiver = receiver;
    }

    public void init(VsqFileEx vsq, int track, int start_clock, int end_clock,
        int sample_rate) {
        mSampleRate = sample_rate;
    }

    public void stop() {
        if (mRunning) {
            mAbortRequested = true;

            while (mRunning) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
        }
    }
}

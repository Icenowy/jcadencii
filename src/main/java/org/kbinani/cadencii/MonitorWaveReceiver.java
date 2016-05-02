/*
 * MonitorWaveReceiver.cs
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

import org.kbinani.media.*;

import java.awt.*;

import java.util.*;


/// <summary>
/// スピーカへの出力を行う波形受信器
/// </summary>
public class MonitorWaveReceiver extends WaveUnit implements WaveReceiver {
    private static final int BUFLEN = 1024;
    private static MonitorWaveReceiver mSingleton = null;
    private boolean mFirstCall = true;
    private double[] mBufferL = new double[BUFLEN];
    private double[] mBufferR = new double[BUFLEN];
    private double[] mBuffer2L = new double[BUFLEN];
    private double[] mBuffer2R = new double[BUFLEN];
    private WaveReceiver mReceiver = null;
    private int mVersion = 0;
    private int mSampleRate = 44100;
    private long mPosition = 0L;

    private MonitorWaveReceiver() {
    }

    public double getPlayTime() {
        return (double) mPosition / (double) mSampleRate;
    }

    public static MonitorWaveReceiver getInstance() {
        return mSingleton;
    }

    public static MonitorWaveReceiver prepareInstance() {
        if (mSingleton == null) {
            mSingleton = new MonitorWaveReceiver();
        }

        mSingleton.end();
        mSingleton.mFirstCall = true;
        mSingleton.mPosition = 0;

        return mSingleton;
    }

    public void setConfig(String parameter) {
        // do nothing
    }

    public int getVersion() {
        return mVersion;
    }

    public void setReceiver(WaveReceiver r) {
        if (mReceiver != null) {
            mReceiver.end();
        }

        mReceiver = r;
    }

    public void push(double[] l, double[] r, int length) {
        if (mFirstCall) {
            mSampleRate = mRoot.getSampleRate();
            PlaySound.init();
            PlaySound.prepare(mSampleRate);
            mFirstCall = false;
        }

        PlaySound.append(l, r, length);
        mPosition += length;

        if (mReceiver != null) {
            mReceiver.push(l, r, length);
        }
    }

    public void end() {
        //PlaySound.exitは，特殊扱い．
        //pushが終了していても，たいていの場合再生されずにキャッシュが残っているので．
        //PlaySound.exit();
        PlaySound.waitForExit();

        if (mReceiver != null) {
            mReceiver.end();
        }
    }
}

/*
 * Mixer.cs
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

import java.awt.*;

import java.util.*;


public class Mixer extends WaveUnit implements WaveSender, WaveReceiver {
    private static final int BUFLEN = 1024;
    private WaveReceiver mReceiver = null;
    private Vector<WaveSender> mSenders = new Vector<WaveSender>();
    private double[] mBufferL = new double[BUFLEN];
    private double[] mBufferR = new double[BUFLEN];
    private double[] mBuffer2L = new double[BUFLEN];
    private double[] mBuffer2R = new double[BUFLEN];
    private int mVersion = 0;

    public int getVersion() {
        return mVersion;
    }

    public void setConfig(String parameter) {
        // do nothing
    }

    public void push(double[] l, double[] r, int length) {
        int remain = length;
        int offset = 0;

        while (remain > 0) {
            int amount = (remain > BUFLEN) ? BUFLEN : remain;

            for (int i = 0; i < BUFLEN; i++) {
                mBufferL[i] = l[i + offset];
                mBufferR[i] = r[i + offset];
            }

            for (WaveSender s : mSenders) {
                s.pull(mBuffer2L, mBuffer2R, amount);

                for (int i = 0; i < BUFLEN; i++) {
                    mBufferL[i] += mBuffer2L[i];
                    mBufferR[i] += mBuffer2R[i];
                }
            }

            if (mReceiver != null) {
                mReceiver.push(mBufferL, mBufferR, amount);
            }

            remain -= amount;
            offset += amount;
        }
    }

    public void pull(double[] l, double[] r, int length) {
        int remain = length;
        int offset = 0;

        while (remain > 0) {
            int amount = (remain > BUFLEN) ? BUFLEN : remain;

            for (int i = 0; i < amount; i++) {
                mBuffer2L[i] = 0.0;
                mBuffer2R[i] = 0.0;
            }

            for (WaveSender s : mSenders) {
                if (s == null) {
                    continue;
                }

                s.pull(mBufferL, mBufferR, amount);

                for (int i = 0; i < amount; i++) {
                    mBuffer2L[i] += mBufferL[i];
                    mBuffer2R[i] += mBufferR[i];
                }
            }

            if (mReceiver != null) {
                mReceiver.push(mBuffer2L, mBuffer2R, amount);
            }

            for (int i = 0; i < amount; i++) {
                l[i + offset] = mBuffer2L[i];
                r[i + offset] = mBuffer2R[i];
            }

            remain -= amount;
            offset += amount;
        }
    }

    public void setReceiver(WaveReceiver r) {
        if (mReceiver != null) {
            mReceiver.end();
        }

        mReceiver = r;
    }

    public void setSender(WaveSender s) {
        addSender(s);
    }

    public void end() {
        if (mReceiver != null) {
            mReceiver.end();
        }

        for (WaveSender s : mSenders) {
            if (s != null) {
                s.end();
            }
        }
    }

    public void addSender(WaveSender s) {
        if (s == null) {
            return;
        }

        if (!mSenders.contains(s)) {
            mSenders.add(s);
        } else {
        }
    }
}

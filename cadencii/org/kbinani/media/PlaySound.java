/*
 * PlaySound.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.media.
 *
 * org.kbinani.media is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.media is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.media;

import org.kbinani.*;

import javax.sound.sampled.*;


public class PlaySound {
    private static final int UNIT_BUFFER = 512;
    private static SourceDataLine m_line;
    private static AudioFormat m_format;
    private static DataLine.Info m_info;
    private static byte[] m_buffer;
    private static boolean _is_initialized = false;

    public static void setResolution(int value) {
        //TODO: fixme PlaySound#setResolution
    }

    public static void init() {
        if (_is_initialized) {
            return;
        }

        m_buffer = new byte[UNIT_BUFFER * 4];
        _is_initialized = true;
    }

    public static void kill() {
        //TODO: fixme PlaySound#kill
    }

    public static double getPosition() {
        if (m_line == null) {
            return 0.0;
        } else {
            return m_line.getMicrosecondPosition() * 1e-6;
        }
    }

    public static void waitForExit() {
        if (m_line == null) {
            return;
        }

        m_line.drain();
    }

    public static void append(double[] left, double[] right, int length) {
        if (m_line == null) {
            return;
        }

        int remain = left.length;
        int off = 0;

        while (remain > 0) {
            int thislen = (remain > UNIT_BUFFER) ? UNIT_BUFFER : remain;
            int c = 0;

            for (int i = 0; i < thislen; i++) {
                short l = (short) (left[i + off] * 32767.0);
                m_buffer[c] = (byte) (0xff & l);
                m_buffer[c + 1] = (byte) (0xff & (l >>> 8));

                short r = (short) (right[i + off] * 32767.0);
                m_buffer[c + 2] = (byte) (0xff & r);
                m_buffer[c + 3] = (byte) (0xff & (r >>> 8));
                c += 4;
            }

            m_line.write(m_buffer, 0, thislen * 4);
            off += thislen;
            remain -= thislen;
        }
    }

    /// <summary>
    /// デバイスを初期化する
    /// </summary>
    /// <param name="sample_rate"></param>
    public static void prepare(int sample_rate) {
        m_format = new AudioFormat(sample_rate, 16, 2, true, false);
        m_info = new DataLine.Info(SourceDataLine.class, m_format);

        try {
            m_line = (SourceDataLine) AudioSystem.getLine(m_info);
            m_line.open(m_format);
            m_line.start();
        } catch (Exception ex) {
            m_line = null;
        }
    }

    /// <summary>
    /// 再生をとめる。
    /// </summary>
    public static void exit() {
        m_line.stop();
        m_line.close();
        m_info = new DataLine.Info(SourceDataLine.class, m_format);

        try {
            m_line = (SourceDataLine) AudioSystem.getLine(m_info);
            m_line.open(m_format);
            m_line.start();
        } catch (Exception ex) {
            serr.println("PlaySound#reset; ex=" + ex);
            m_line = null;
        }
    }

    public static void unprepare() {
        //TODO: fixme PlaySound#unprepare
    }
}

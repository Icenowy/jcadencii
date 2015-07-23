/*
 * UstVibrato.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.vsq.
 *
 * org.kbinani.vsq is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.vsq is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.vsq;

import org.kbinani.*;

import java.io.*;


public class UstVibrato implements Cloneable, Serializable {
    /// <summary>
    /// 音符の長さに対する、パーセントで表したビブラートの長さ。
    /// </summary>
    public float Length;

    /// <summary>
    /// ミリセカンドで表したビブラートによるピッチ振動の周期
    /// </summary>
    public float Period;

    /// <summary>
    /// Centで表した、ビブラートによるピッチ振動の振幅。Peak to Peakは2*Depthとなる。
    /// </summary>
    public float Depth;

    /// <summary>
    /// ビブラート長さに対する、パーセントで表したピッチ振動のフェードインの長さ。
    /// </summary>
    public float In;

    /// <summary>
    /// ビブラートの長さに対するパーセントで表したピッチ振動のフェードアウトの長さ。
    /// </summary>
    public float Out;

    /// <summary>
    /// ピッチ振動開始時の位相。2PIに対するパーセントで表す。
    /// </summary>
    public float Phase;

    /// <summary>
    /// ピッチ振動の中心値と、音符の本来の音の高さからのずれ。Depthに対するパーセントで表す。
    /// </summary>
    public float Shift;
    public float Unknown = 100;

    public UstVibrato(String line) {
        if (line.toLowerCase().startsWith("vbr=")) {
            String[] spl = PortUtil.splitString(line, '=');
            spl = PortUtil.splitString(spl[1], ',');

            //VBR=65,180,70,20.0,17.6,82.8,49.8,100
            if (spl.length >= 8) {
                Length = (float) str.tof(spl[0]);
                Period = (float) str.tof(spl[1]);
                Depth = (float) str.tof(spl[2]);
                In = (float) str.tof(spl[3]);
                Out = (float) str.tof(spl[4]);
                Phase = (float) str.tof(spl[5]);
                Shift = (float) str.tof(spl[6]);
                Unknown = (float) str.tof(spl[7]);
            }
        }
    }

    public UstVibrato() {
    }

    public float getLength() {
        return Length;
    }

    public void setLength(float value) {
        Length = value;
    }

    public String toString() {
        return "VBR=" + Length + "," + Period + "," + Depth + "," + In + "," +
        Out + "," + Phase + "," + Shift + "," + Unknown;
    }

    public Object clone() {
        UstVibrato ret = new UstVibrato();
        ret.setLength(Length);
        ret.Period = Period;
        ret.Depth = Depth;
        ret.In = In;
        ret.Out = Out;
        ret.Phase = Phase;
        ret.Shift = Shift;
        ret.Unknown = Unknown;

        return ret;
    }
}

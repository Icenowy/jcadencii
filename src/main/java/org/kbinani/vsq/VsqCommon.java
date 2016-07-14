/*
 * VsqCommon.cs
 * Copyright © 2008-2011 kbinani
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


/// <summary>
/// vsqファイルのメタテキストの[Common]セクションに記録される内容を取り扱う
/// </summary>
public class VsqCommon implements Cloneable, Serializable {
    public String Version;
    public String Name;
    public String Color;

    /// <summary>
    /// Dynamicsカーブを表示するモード(Expert)なら1、しない(Standard)なら0。
    /// </summary>
    public int DynamicsMode = org.kbinani.vsq.DynamicsMode.Expert;

    /// <summary>
    /// Play With Synthesisなら1、Play After Synthesiなら0、Offなら-1。
    /// </summary>
    public int PlayMode = org.kbinani.vsq.PlayMode.PlayWithSynth;

    /// <summary>
    /// PlayModeがOff(-1)にされる直前に，PlayAfterSynthかPlayWithSynthのどちらが指定されていたかを記憶しておく．
    /// </summary>
    public int LastPlayMode = org.kbinani.vsq.PlayMode.PlayWithSynth;

    /// <summary>
    /// 各パラメータを指定したコンストラクタ
    /// </summary>
    /// <param name="name">トラック名</param>
    /// <param name="color">Color値（意味は不明）</param>
    /// <param name="dynamics_mode">DynamicsMode（デフォルトは1）</param>
    /// <param name="play_mode">PlayMode（デフォルトは1）</param>
    public VsqCommon(String name, int red, int green, int blue,
        int dynamics_mode, int play_mode) {
        this.Version = "DSB301";
        this.Name = name;
        this.Color = red + "," + green + "," + blue;
        this.DynamicsMode = dynamics_mode;
        this.PlayMode = play_mode;
    }

    public VsqCommon() {
        this("Miku", 179, 181, 123, 1, 1);
    }

    /// <summary>
    /// MetaTextのテキストファイルからのコンストラクタ
    /// </summary>
    /// <param name="sr">読み込むテキストファイル</param>
    /// <param name="last_line">読み込んだ最後の行が返される</param>
    public VsqCommon(TextStream sr, ByRef<String> last_line) {
        Version = "";
        Name = "";
        Color = "0,0,0";
        DynamicsMode = 0;
        PlayMode = 1;
        last_line.value = sr.readLine();

        String[] spl;

        while (!last_line.value.startsWith("[")) {
            spl = PortUtil.splitString(last_line.value, new char[] { '=' });

            String search = spl[0];

            if (search.equals("Version")) {
                this.Version = spl[1];
            } else if (search.equals("Name")) {
                this.Name = spl[1];
            } else if (search.equals("Color")) {
                this.Color = spl[1];
            } else if (search.equals("DynamicsMode")) {
                this.DynamicsMode = str.toi(spl[1]);
            } else if (search.equals("PlayMode")) {
                this.PlayMode = str.toi(spl[1]);
            }

            if (!sr.ready()) {
                break;
            }

            last_line.value = sr.readLine();
        }
    }

    public Object clone() {
        String[] spl = PortUtil.splitString(Color, new char[] { ',' }, 3);
        int r = str.toi(spl[0]);
        int g = str.toi(spl[1]);
        int b = str.toi(spl[2]);
        VsqCommon res = new VsqCommon(Name, r, g, b, DynamicsMode, PlayMode);
        res.Version = Version;
        res.LastPlayMode = LastPlayMode;

        return res;
    }

    /// <summary>
    /// インスタンスの内容をテキストファイルに出力します
    /// </summary>
    /// <param name="sw">出力先</param>
    public void write(ITextWriter sw) throws java.io.IOException {
        sw.writeLine("[Common]");
        sw.writeLine("Version=" + Version);
        sw.writeLine("Name=" + Name);
        sw.writeLine("Color=" + Color);
        sw.writeLine("DynamicsMode=" + DynamicsMode);
        sw.writeLine("PlayMode=" + PlayMode);
    }
}

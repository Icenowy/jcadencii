/*
* VsqMixer.cs
* Copyright (C) 2008-2011 kbinani
*
* This file is part of org.kbinani.vsq.
*
* Boare.Lib.Vsq is free software; you can redistribute it and/or
* modify it under the terms of the BSD License.
*
* Boare.Lib.Vsq is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
package org.kbinani.vsq;

import org.kbinani.*;

import org.kbinani.xml.*;

import java.io.*;

import java.util.*;


/// <summary>
/// vsqファイルのメタテキストの[Mixer]セクションに記録される内容を取り扱う
/// </summary>
public class VsqMixer implements Cloneable, Serializable {
    public int MasterFeder;
    public int MasterPanpot;
    public int MasterMute;
    public int OutputMode;

    /// <summary>
    /// vsqファイルの各トラックのfader, panpot, muteおよびoutputmode値を保持します
    /// </summary>
    @XmlGenericType(VsqMixerEntry.class)
    public Vector<VsqMixerEntry> Slave = new Vector<VsqMixerEntry>();

    /// <summary>
    /// 各パラメータを指定したコンストラクタ
    /// </summary>
    /// <param name="master_fader">MasterFader値</param>
    /// <param name="master_panpot">MasterPanpot値</param>
    /// <param name="master_mute">MasterMute値</param>
    /// <param name="output_mode">OutputMode値</param>
    public VsqMixer(int master_fader, int master_panpot, int master_mute,
        int output_mode) {
        this.MasterFeder = master_fader;
        this.MasterMute = master_mute;
        this.MasterPanpot = master_panpot;
        this.OutputMode = output_mode;
        Slave = new Vector<VsqMixerEntry>();
    }

    public VsqMixer() {
        this(0, 0, 0, 0);
    }

    /// <summary>
    /// テキストファイルからのコンストラクタ
    /// </summary>
    /// <param name="sr">読み込み対象</param>
    /// <param name="last_line">最後に読み込んだ行が返されます</param>
    public VsqMixer(TextStream sr, ByRef<String> last_line) {
        MasterFeder = 0;
        MasterPanpot = 0;
        MasterMute = 0;
        OutputMode = 0;

        //Tracks = 1;
        int tracks = 0;
        String[] spl;
        String buffer = "";
        last_line.value = sr.readLine();

        while (!last_line.value.startsWith("[")) {
            spl = PortUtil.splitString(last_line.value, new char[] { '=' });

            if (spl[0].equals("MasterFeder")) {
                MasterFeder = str.toi(spl[1]);
            } else if (spl[0].equals("MasterPanpot")) {
                MasterPanpot = str.toi(spl[1]);
            } else if (spl[0].equals("MasterMute")) {
                MasterMute = str.toi(spl[1]);
            } else if (spl[0].equals("OutputMode")) {
                OutputMode = str.toi(spl[1]);
            } else if (spl[0].equals("Tracks")) {
                tracks = str.toi(spl[1]);
            } else {
                if (spl[0].startsWith("Feder") || spl[0].startsWith("Panpot") ||
                        spl[0].startsWith("Mute") || spl[0].startsWith("Solo")) {
                    buffer += (spl[0] + "=" + spl[1] + "\n");
                }
            }

            if (!sr.ready()) {
                break;
            }

            last_line.value = sr.readLine().toString();
        }

        Slave = new Vector<VsqMixerEntry>();

        for (int i = 0; i < tracks; i++) {
            Slave.add(new VsqMixerEntry(0, 0, 0, 0));
        }

        spl = PortUtil.splitString(buffer, new String[] { "\n" }, true);

        String[] spl2;

        for (int i = 0; i < spl.length; i++) {
            String ind = "";
            int index;
            spl2 = PortUtil.splitString(spl[i], new char[] { '=' });

            if (spl2[0].startsWith("Feder")) {
                ind = spl2[0].replace("Feder", "");
                index = str.toi(ind);
                Slave.get(index).Feder = str.toi(spl2[1]);
            } else if (spl2[0].startsWith("Panpot")) {
                ind = spl2[0].replace("Panpot", "");
                index = str.toi(ind);
                Slave.get(index).Panpot = str.toi(spl2[1]);
            } else if (spl2[0].startsWith("Mute")) {
                ind = spl2[0].replace("Mute", "");
                index = str.toi(ind);
                Slave.get(index).Mute = str.toi(spl2[1]);
            } else if (spl2[0].startsWith("Solo")) {
                ind = spl2[0].replace("Solo", "");
                index = str.toi(ind);
                Slave.get(index).Solo = str.toi(spl2[1]);
            }
        }
    }

    /// <summary>
    /// このクラスの指定した名前のプロパティをXMLシリアライズする際に使用する
    /// 要素名を取得します．
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public static String getXmlElementName(String name) {
        return name;
    }

    public Object clone() {
        VsqMixer res = new VsqMixer(MasterFeder, MasterPanpot, MasterMute,
                OutputMode);
        res.Slave = new Vector<VsqMixerEntry>();

        for (Iterator<VsqMixerEntry> itr = Slave.iterator(); itr.hasNext();) {
            VsqMixerEntry item = itr.next();
            res.Slave.add((VsqMixerEntry) item.clone());
        }

        return res;
    }

    /// <summary>
    /// このインスタンスをテキストファイルに出力します
    /// </summary>
    /// <param name="sw">出力対象</param>
    public void write(ITextWriter sw) throws java.io.IOException {
        sw.writeLine("[Mixer]");
        sw.writeLine("MasterFeder=" + MasterFeder);
        sw.writeLine("MasterPanpot=" + MasterPanpot);
        sw.writeLine("MasterMute=" + MasterMute);
        sw.writeLine("OutputMode=" + OutputMode);

        int count = Slave.size();
        sw.writeLine("Tracks=" + count);

        for (int i = 0; i < count; i++) {
            VsqMixerEntry item = Slave.get(i);
            sw.writeLine("Feder" + i + "=" + item.Feder);
            sw.writeLine("Panpot" + i + "=" + item.Panpot);
            sw.writeLine("Mute" + i + "=" + item.Mute);
            sw.writeLine("Solo" + i + "=" + item.Solo);
        }
    }
}

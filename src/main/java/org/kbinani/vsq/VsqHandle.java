/*
* VsqHandle.cs
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

import java.io.*;

import java.util.*;


/// <summary>
/// ハンドルを取り扱います。ハンドルにはLyricHandle、VibratoHandle、IconHandleおよびNoteHeadHandleがある
/// </summary>
public class VsqHandle implements Serializable {
    public VsqHandleType m_type;
    public int Index;
    public String IconID = "";
    public String IDS = "";
    public Lyric L0;
    public Vector<Lyric> Trailing = new Vector<Lyric>();
    public int Original;
    public String Caption = "";
    public int Length;
    public int StartDepth;
    public VibratoBPList DepthBP;
    public int StartRate;
    public VibratoBPList RateBP;
    public int Language;
    public int Program;
    public int Duration;
    public int Depth;
    public int StartDyn;
    public int EndDyn;
    public VibratoBPList DynBP;

    /// <summary>
    /// 歌詞・発音記号列の前後にクォーテーションマークを付けるかどうか
    /// </summary>
    public boolean addQuotationMark = true;

    public VsqHandle() {
    }

    /// <summary>
    /// FileStreamから読み込みながらコンストラクト
    /// </summary>
    /// <param name="sr">読み込み対象</param>
    public VsqHandle(TextStream sr, int value, ByRef<String> last_line) {
        this.Index = value;

        String[] spl;
        String[] spl2;

        // default値で梅
        m_type = VsqHandleType.Vibrato;
        IconID = "";
        IDS = "normal";
        L0 = new Lyric("");
        Original = 0;
        Caption = "";
        Length = 0;
        StartDepth = 0;
        DepthBP = null;
        StartRate = 0;
        RateBP = null;
        Language = 0;
        Program = 0;
        Duration = 0;
        Depth = 64;

        String tmpDepthBPX = "";
        String tmpDepthBPY = "";
        String tmpDepthBPNum = "";

        String tmpRateBPX = "";
        String tmpRateBPY = "";
        String tmpRateBPNum = "";

        String tmpDynBPX = "";
        String tmpDynBPY = "";
        String tmpDynBPNum = "";

        // "["にぶち当たるまで読込む
        last_line.value = sr.readLine().toString();

        while (!last_line.value.startsWith("[")) {
            spl = PortUtil.splitString(last_line.value, new char[] { '=' });

            String search = spl[0];

            if (search.equals("Language")) {
                m_type = VsqHandleType.Singer;
                Language = str.toi(spl[1]);
            } else if (search.equals("Program")) {
                Program = str.toi(spl[1]);
            } else if (search.equals("IconID")) {
                IconID = spl[1];
            } else if (search.equals("IDS")) {
                IDS = spl[1];
            } else if (search.equals("Original")) {
                Original = str.toi(spl[1]);
            } else if (search.equals("Caption")) {
                Caption = spl[1];

                for (int i = 2; i < spl.length; i++) {
                    Caption += ("=" + spl[i]);
                }
            } else if (search.equals("Length")) {
                Length = str.toi(spl[1]);
            } else if (search.equals("StartDepth")) {
                StartDepth = str.toi(spl[1]);
            } else if (search.equals("DepthBPNum")) {
                tmpDepthBPNum = spl[1];
            } else if (search.equals("DepthBPX")) {
                tmpDepthBPX = spl[1];
            } else if (search.equals("DepthBPY")) {
                tmpDepthBPY = spl[1];
            } else if (search.equals("StartRate")) {
                m_type = VsqHandleType.Vibrato;
                StartRate = str.toi(spl[1]);
            } else if (search.equals("RateBPNum")) {
                tmpRateBPNum = spl[1];
            } else if (search.equals("RateBPX")) {
                tmpRateBPX = spl[1];
            } else if (search.equals("RateBPY")) {
                tmpRateBPY = spl[1];
            } else if (search.equals("Duration")) {
                m_type = VsqHandleType.NoteHeadHandle;
                Duration = str.toi(spl[1]);
            } else if (search.equals("Depth")) {
                Depth = str.toi(spl[1]);
            } else if (search.equals("StartDyn")) {
                m_type = VsqHandleType.DynamicsHandle;
                StartDyn = str.toi(spl[1]);
            } else if (search.equals("EndDyn")) {
                m_type = VsqHandleType.DynamicsHandle;
                EndDyn = str.toi(spl[1]);
            } else if (search.equals("DynBPNum")) {
                tmpDynBPNum = spl[1];
            } else if (search.equals("DynBPX")) {
                tmpDynBPX = spl[1];
            } else if (search.equals("DynBPY")) {
                tmpDynBPY = spl[1];
            } else if (search.startsWith("L") &&
                    (PortUtil.getStringLength(search) >= 2)) {
                String num = str.sub(search, 1);
                ByRef<Integer> vals = new ByRef<Integer>(0);

                if (PortUtil.tryParseInt(num, vals)) {
                    Lyric lyric = new Lyric(spl[1]);
                    m_type = VsqHandleType.Lyric;

                    int index = vals.value;

                    if (index == 0) {
                        L0 = lyric;
                    } else {
                        if (Trailing.size() < index) {
                            for (int i = Trailing.size(); i < index; i++) {
                                Trailing.add(new Lyric("a", "a"));
                            }
                        }

                        Trailing.set(index - 1, lyric);
                    }
                }
            }

            if (!sr.ready()) {
                break;
            }

            last_line.value = sr.readLine().toString();
        }

        // RateBPX, RateBPYの設定
        if (m_type == VsqHandleType.Vibrato) {
            if (!tmpRateBPNum.equals("")) {
                RateBP = new VibratoBPList(tmpRateBPNum, tmpRateBPX, tmpRateBPY);
            } else {
                RateBP = new VibratoBPList();
            }

            // DepthBPX, DepthBPYの設定
            if (!tmpDepthBPNum.equals("")) {
                DepthBP = new VibratoBPList(tmpDepthBPNum, tmpDepthBPX,
                        tmpDepthBPY);
            } else {
                DepthBP = new VibratoBPList();
            }
        } else {
            DepthBP = new VibratoBPList();
            RateBP = new VibratoBPList();
        }

        if (!tmpDynBPNum.equals("")) {
            DynBP = new VibratoBPList(tmpDynBPNum, tmpDynBPX, tmpDynBPY);
        } else {
            DynBP = new VibratoBPList();
        }
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int value) {
        Length = value;
    }

    public LyricHandle castToLyricHandle() {
        LyricHandle ret = new LyricHandle();
        ret.L0 = L0;
        ret.Index = Index;
        ret.Trailing = Trailing;

        return ret;
    }

    public VibratoHandle castToVibratoHandle() {
        VibratoHandle ret = new VibratoHandle();
        ret.Index = Index;
        ret.setCaption(Caption);
        ret.setDepthBP((VibratoBPList) DepthBP.clone());
        ret.IconID = IconID;
        ret.IDS = IDS;
        ret.Index = Index;
        ret.setLength(Length);
        ret.Original = Original;
        ret.setRateBP((VibratoBPList) RateBP.clone());
        ret.setStartDepth(StartDepth);
        ret.setStartRate(StartRate);

        return ret;
    }

    public IconHandle castToIconHandle() {
        IconHandle ret = new IconHandle();
        ret.Index = Index;
        ret.Caption = Caption;
        ret.IconID = IconID;
        ret.IDS = IDS;
        ret.Index = Index;
        ret.Language = Language;
        ret.setLength(Length);
        ret.Original = Original;
        ret.Program = Program;

        return ret;
    }

    public NoteHeadHandle castToNoteHeadHandle() {
        NoteHeadHandle ret = new NoteHeadHandle();
        ret.setCaption(Caption);
        ret.setDepth(Depth);
        ret.setDuration(Duration);
        ret.IconID = IconID;
        ret.IDS = IDS;
        ret.setLength(getLength());
        ret.Original = Original;

        return ret;
    }

    public IconDynamicsHandle castToIconDynamicsHandle() {
        IconDynamicsHandle ret = new IconDynamicsHandle();
        ret.IDS = IDS;
        ret.IconID = IconID;
        ret.Original = Original;
        ret.setCaption(Caption);
        ret.setDynBP(DynBP);
        ret.setEndDyn(EndDyn);
        ret.setLength(getLength());
        ret.setStartDyn(StartDyn);

        return ret;
    }

    public static VsqHandle castFromLyricHandle(LyricHandle handle) {
        VsqHandle ret = new VsqHandle();
        ret.m_type = VsqHandleType.Lyric;
        ret.L0 = (Lyric) handle.L0.clone();
        ret.Trailing = handle.Trailing;
        ret.Index = handle.Index;

        return ret;
    }

    /// <summary>
    /// 歌手設定のインスタンスを、VsqHandleに型キャストします。
    /// </summary>
    /// <returns></returns>
    public static VsqHandle castFromIconHandle(IconHandle handle) {
        VsqHandle ret = new VsqHandle();
        ret.m_type = VsqHandleType.Singer;
        ret.Caption = handle.Caption;
        ret.IconID = handle.IconID;
        ret.IDS = handle.IDS;
        ret.Index = handle.Index;
        ret.Language = handle.Language;
        ret.setLength(handle.getLength());
        ret.Program = handle.Program;

        return ret;
    }

    /// <summary>
    /// インスタンスをストリームに書き込みます。
    /// encode=trueの場合、2バイト文字をエンコードして出力します。
    /// </summary>
    /// <param name="sw">書き込み対象</param>
    public void write(ITextWriter sw) throws IOException {
        sw.writeLine(this.toString());
    }

    public void write(BufferedWriter sw) throws IOException {
        write(new WrappedStreamWriter(sw));
    }

    /// <summary>
    /// ハンドル指定子（例えば"h#0123"という文字列）からハンドル番号を取得します
    /// </summary>
    /// <param name="_string">ハンドル指定子</param>
    /// <returns>ハンドル番号</returns>
    public static int HandleIndexFromString(String _string) {
        String[] spl = PortUtil.splitString(_string, new char[] { '#' });

        return str.toi(spl[1]);
    }

    /// <summary>
    /// インスタンスをテキストファイルに出力します
    /// </summary>
    /// <param name="sw">出力先</param>
    public void print(BufferedWriter sw) throws IOException {
        String result = toString();
        sw.write(result);
        sw.newLine();
    }

    /// <summary>
    /// インスタンスをコンソール画面に出力します
    /// </summary>
    private void print() {
        String result = toString();
        sout.println(result);
    }

    /// <summary>
    /// インスタンスを文字列に変換します
    /// </summary>
    /// <returns>インスタンスを変換した文字列</returns>
    public String toString() {
        String result = "";
        result += ("[h#" + PortUtil.formatDecimal("0000", Index) + "]");

        if (m_type == VsqHandleType.Lyric) {
            result += ("\n" + "L0=" + L0.toString(addQuotationMark));

            int c = Trailing.size();

            for (int i = 0; i < c; i++) {
                result += ("\n" + "L" + (i + 1) + "=" +
                Trailing.get(i).toString(addQuotationMark));
            }
        } else if (m_type == VsqHandleType.Vibrato) {
            result += ("\n" + "IconID=" + IconID + "\n");
            result += ("IDS=" + IDS + "\n");
            result += ("Original=" + Original + "\n");
            result += ("Caption=" + Caption + "\n");
            result += ("Length=" + Length + "\n");
            result += ("StartDepth=" + StartDepth + "\n");
            result += ("DepthBPNum=" + DepthBP.getCount() + "\n");

            if (DepthBP.getCount() > 0) {
                result += ("DepthBPX=" +
                PortUtil.formatDecimal("0.000000", DepthBP.getElement(0).X));

                for (int i = 1; i < DepthBP.getCount(); i++) {
                    result += ("," +
                    PortUtil.formatDecimal("0.000000", DepthBP.getElement(i).X));
                }

                result += ("\n" + "DepthBPY=" + DepthBP.getElement(0).Y);

                for (int i = 1; i < DepthBP.getCount(); i++) {
                    result += ("," + DepthBP.getElement(i).Y);
                }

                result += "\n";
            }

            result += ("StartRate=" + StartRate + "\n");
            result += ("RateBPNum=" + RateBP.getCount());

            if (RateBP.getCount() > 0) {
                result += ("\n" + "RateBPX=" +
                PortUtil.formatDecimal("0.000000", RateBP.getElement(0).X));

                for (int i = 1; i < RateBP.getCount(); i++) {
                    result += ("," +
                    PortUtil.formatDecimal("0.000000", RateBP.getElement(i).X));
                }

                result += ("\n" + "RateBPY=" + RateBP.getElement(0).Y);

                for (int i = 1; i < RateBP.getCount(); i++) {
                    result += ("," + RateBP.getElement(i).Y);
                }
            }
        } else if (m_type == VsqHandleType.Singer) {
            result += ("\n" + "IconID=" + IconID + "\n");
            result += ("IDS=" + IDS + "\n");
            result += ("Original=" + Original + "\n");
            result += ("Caption=" + Caption + "\n");
            result += ("Length=" + Length + "\n");
            result += ("Language=" + Language + "\n");
            result += ("Program=" + Program);
        } else if (m_type == VsqHandleType.NoteHeadHandle) {
            result += ("\n" + "IconID=" + IconID + "\n");
            result += ("IDS=" + IDS + "\n");
            result += ("Original=" + Original + "\n");
            result += ("Caption=" + Caption + "\n");
            result += ("Length=" + Length + "\n");
            result += ("Duration=" + Duration + "\n");
            result += ("Depth=" + Depth);
        } else if (m_type == VsqHandleType.DynamicsHandle) {
            result += ("\n" + "IconID=" + IconID + "\n");
            result += ("IDS=" + IDS + "\n");
            result += ("Original=" + Original + "\n");
            result += ("Caption=" + Caption + "\n");
            result += ("StartDyn=" + StartDyn + "\n");
            result += ("EndDyn=" + EndDyn + "\n");
            result += ("Length=" + Length + "\n");

            if (DynBP != null) {
                if (DynBP.getCount() <= 0) {
                    result += "DynBPNum=0";
                } else {
                    result += ("DynBPX=" +
                    PortUtil.formatDecimal("0.000000", DynBP.getElement(0).X));

                    int c = DynBP.getCount();

                    for (int i = 1; i < c; i++) {
                        result += ("," +
                        PortUtil.formatDecimal("0.000000", DynBP.getElement(i).X));
                    }

                    result += ("\n" + "DynBPY=" + DynBP.getElement(0).Y);

                    for (int i = 1; i < c; i++) {
                        result += ("," + DynBP.getElement(i).Y);
                    }
                }
            } else {
                result += "DynBPNum=0";
            }
        }

        return result;
    }
}

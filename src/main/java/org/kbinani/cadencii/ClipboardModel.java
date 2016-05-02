/*
 * ClipboardModel.cs
 * Copyright © 2011 kbinani
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

import org.kbinani.vsq.*;

import java.io.*;

import java.util.*;


/// <summary>
/// クリップボードを管理するクラスです．
/// </summary>
public class ClipboardModel {
    /// <summary>
    /// OSのクリップボードに貼り付ける文字列の接頭辞．
    /// これがついていた場合，クリップボードの文字列をCadenciiが使用できると判断する．
    /// </summary>
    public static final String CLIP_PREFIX = "CADENCIIOBJ";

    /// <summary>
    /// オブジェクトをシリアライズし，クリップボードに格納するための文字列を作成します
    /// </summary>
    /// <param name="obj">シリアライズするオブジェクト</param>
    /// <returns>シリアライズされた文字列</returns>
    private String getSerializedText(Object obj) throws IOException {
        String str = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);

        byte[] arr = outputStream.toByteArray();
        str = CLIP_PREFIX + ":" + obj.getClass().getName() + ":" +
            org.kbinani.Base64.encode(arr);

        return str;
    }

    /// <summary>
    /// クリップボードに格納された文字列を元に，デシリアライズされたオブジェクトを取得します
    /// </summary>
    /// <param name="s"></param>
    /// <returns></returns>
    private Object getDeserializedObjectFromText(String s) {
        if (s.startsWith(CLIP_PREFIX)) {
            int index = s.indexOf(":");
            index = s.indexOf(":", index + 1);

            Object ret = null;

            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(org.kbinani.Base64.decode(
                            str.sub(s, index + 1)));
                ObjectInputStream ois = new ObjectInputStream(bais);
                ret = ois.readObject();
            } catch (Exception ex) {
                ret = null;
                Logger.write(ClipboardModel.class +
                    ".getDeserializedObjectFromText; ex=" + ex + "\n");
            }

            return ret;
        } else {
            return null;
        }
    }

    /// <summary>
    /// クリップボードにオブジェクトを貼り付けます．
    /// </summary>
    /// <param name="item">貼り付けるオブジェクトを格納したClipboardEntryのインスタンス</param>
    public void setClipboard(ClipboardEntry item) {
        String clip = "";

        try {
            clip = getSerializedText(item);
        } catch (Exception ex) {
            serr.println("ClipboardModel#setClipboard; ex=" + ex);
            Logger.write(ClipboardModel.class + ".setClipboard; ex=" + ex +
                "\n");

            return;
        }

        PortUtil.setClipboardText(clip);
    }

    /// <summary>
    /// クリップボードにオブジェクトを貼り付けるためのユーティリティ．
    /// </summary>
    /// <param name="events"></param>
    /// <param name="tempo"></param>
    /// <param name="timesig"></param>
    /// <param name="curve"></param>
    /// <param name="bezier"></param>
    /// <param name="copy_started_clock"></param>
    private void setClipboard(Vector<VsqEvent> events,
        Vector<TempoTableEntry> tempo, Vector<TimeSigTableEntry> timesig,
        TreeMap<CurveType, VsqBPList> curve,
        TreeMap<CurveType, Vector<BezierChain>> bezier, int copy_started_clock) {
        ClipboardEntry ce = new ClipboardEntry();
        ce.events = events;
        ce.tempo = tempo;
        ce.timesig = timesig;
        ce.points = curve;
        ce.beziers = bezier;
        ce.copyStartedClock = copy_started_clock;

        String clip = "";

        try {
            clip = getSerializedText(ce);
        } catch (Exception ex) {
            serr.println("ClipboardModel#setClipboard; ex=" + ex);
            Logger.write(ClipboardModel.class + ".setClipboard; ex=" + ex +
                "\n");

            return;
        }

        PortUtil.setClipboardText(clip);
    }

    /// <summary>
    /// クリップボードに貼り付けられたアイテムを取得します．
    /// </summary>
    /// <returns>クリップボードに貼り付けられたアイテムを格納したClipboardEntryのインスタンス</returns>
    public ClipboardEntry getCopiedItems() {
        ClipboardEntry ce = null;
        String clip = PortUtil.getClipboardText();

        if ((clip != null) && str.startsWith(clip, CLIP_PREFIX)) {
            int index1 = clip.indexOf(":");
            int index2 = clip.indexOf(":", index1 + 1);
            String typename = str.sub(clip, index1 + 1, index2 - index1 - 1);

            if (typename.equals(ClipboardEntry.class.getName())) {
                try {
                    ce = (ClipboardEntry) getDeserializedObjectFromText(clip);
                } catch (Exception ex) {
                    Logger.write(ClipboardModel.class + ".getCopiedItems; ex=" +
                        ex + "\n");
                }
            }
        }

        if (ce == null) {
            ce = new ClipboardEntry();
        }

        if (ce.beziers == null) {
            ce.beziers = new TreeMap<CurveType, Vector<BezierChain>>();
        }

        if (ce.events == null) {
            ce.events = new Vector<VsqEvent>();
        }

        if (ce.points == null) {
            ce.points = new TreeMap<CurveType, VsqBPList>();
        }

        if (ce.tempo == null) {
            ce.tempo = new Vector<TempoTableEntry>();
        }

        if (ce.timesig == null) {
            ce.timesig = new Vector<TimeSigTableEntry>();
        }

        return ce;
    }

    /// <summary>
    /// VsqEventのリストをクリップボードにセットします．
    /// </summary>
    /// <param name="item">セットするVsqEventのリスト</param>
    /// <param name="copy_started_clock"></param>
    public void setCopiedEvent(Vector<VsqEvent> item, int copy_started_clock) {
        setClipboard(item, null, null, null, null, copy_started_clock);
    }

    /// <summary>
    /// テンポ変更イベント(TempoTableEntry)のリストをクリップボードにセットします．
    /// </summary>
    /// <param name="item">セットするTempoTableEntryのリスト</param>
    /// <param name="copy_started_clock"></param>
    public void setCopiedTempo(Vector<TempoTableEntry> item,
        int copy_started_clock) {
        setClipboard(null, item, null, null, null, copy_started_clock);
    }

    /// <summary>
    /// 拍子変更イベント(TimeSigTableEntry)のリストをクリップボードにセットします．
    /// </summary>
    /// <param name="item">セットする拍子変更イベントのリスト</param>
    /// <param name="copy_started_clock"></param>
    public void setCopiedTimesig(Vector<TimeSigTableEntry> item,
        int copy_started_clock) {
        setClipboard(null, null, item, null, null, copy_started_clock);
    }

    /// <summary>
    /// コントロールカーブをクリップボードにセットします．
    /// </summary>
    /// <param name="item">セットするコントロールカーブ</param>
    /// <param name="copy_started_clock"></param>
    public void setCopiedCurve(TreeMap<CurveType, VsqBPList> item,
        int copy_started_clock) {
        setClipboard(null, null, null, item, null, copy_started_clock);
    }

    /// <summary>
    /// ベジエ曲線をクリップボードにセットします．
    /// </summary>
    /// <param name="item">セットするベジエ曲線</param>
    /// <param name="copy_started_clock"></param>
    public void setCopiedBezier(TreeMap<CurveType, Vector<BezierChain>> item,
        int copy_started_clock) {
        setClipboard(null, null, null, null, item, copy_started_clock);
    }
}

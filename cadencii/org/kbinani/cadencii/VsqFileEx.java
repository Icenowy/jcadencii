/*
 * VsqFileEx.cs
 * Copyright © 2008-2011 kbinani
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

import org.kbinani.xml.*;

import java.io.*;

import java.util.*;


public class VsqFileEx extends VsqFile implements Cloneable, ICommandRunnable,
    Serializable {
    private static XmlSerializer mVsqSerializer;
    public static final String TAG_VSQEVENT_AQUESTONE_RELEASE = "org.kbinani.cadencii.AquesToneRelease";
    public static final String TAG_VSQTRACK_RENDERER_KIND = "org.kbinani.cadencii.RendererKind";

    /// <summary>
    /// トラックをUTAUモードで合成するとき，何番目の互換合成器で合成するかどうかを指定する
    /// </summary>
    public static final String TAG_VSQTRACK_RESAMPLER_USED = "org.kbinani.cadencii.ResamplerUsed";

    static {
        mVsqSerializer = new XmlSerializer(VsqFileEx.class);
    }

    public AttachedCurve AttachedCurves;
    @XmlGenericType(BgmFile.class)
    public Vector<BgmFile> BgmFiles = new Vector<BgmFile>();

    /// <summary>
    /// キャッシュ用ディレクトリのパス
    /// </summary>
    public String cacheDir = "";
    public EditorStatus editorStatus = new EditorStatus();

    /// <summary>
    /// シーケンスの設定
    /// <version>3.3+</version>
    /// </summary>
    public SequenceConfig config = new SequenceConfig();

    public VsqFileEx() {
        this("Miku", 1, 4, 4, 500000);
        Track.clear();
        TempoTable.clear();
        TimesigTable.clear();
    }

    public VsqFileEx(String singer, int pre_measure, int numerator,
        int denominator, int tempo) {
        super(singer, pre_measure, numerator, denominator, tempo);
        AttachedCurves = new AttachedCurve();

        int count = Track.size();

        for (int i = 1; i < count; i++) {
            AttachedCurves.add(new BezierCurves());
        }
    }

    public VsqFileEx(UstFile ust) {
        super(ust);
        AttachedCurves = new AttachedCurve();

        int count = Track.size();

        for (int i = 1; i < count; i++) {
            AttachedCurves.add(new BezierCurves());
        }
    }

    public VsqFileEx(String _fpath, String encoding)
        throws FileNotFoundException {
        super(_fpath, encoding);
        AttachedCurves = new AttachedCurve();

        String xml = fsys.combine(PortUtil.getDirectoryName(_fpath),
                PortUtil.getFileName(_fpath) + ".xml");

        for (int i = 1; i < Track.size(); i++) {
            AttachedCurves.add(new BezierCurves());
        }

        // UTAUでエクスポートしたIconHandleは、IDS=UTAUとなっているので探知する
        int count = Track.size();

        for (int i = 1; i < count; i++) {
            VsqTrack track = Track.get(i);

            for (Iterator<VsqEvent> itr = track.getSingerEventIterator();
                    itr.hasNext();) {
                VsqEvent ve = itr.next();

                if (((IconHandle) ve.ID.IconHandle).IDS.toLowerCase()
                                                           .equals("utau")) {
                    setTrackRendererKind(track, RendererKind.UTAU);

                    break;
                }
            }
        }
    }

    /// <summary>
    /// 指定したトラックに対して使用する，UTAU互換合成器の番号を取得します
    /// </summary>
    /// <param name="vsq_track"></param>
    /// <returns></returns>
    public static int getTrackResamplerUsed(VsqTrack vsq_track) {
        String str_indx = getTagCor(vsq_track.Tag, TAG_VSQTRACK_RESAMPLER_USED);
        int ret = 0;

        try {
            ret = str.toi(str_indx);
        } catch (Exception ex) {
            ret = 0;
        }

        return ret;
    }

    /// <summary>
    /// 指定したトラックに対して使用するUTAU互換合成器の番号を設定します
    /// </summary>
    /// <param name="vsq_track"></param>
    /// <param name="index"></param>
    public static void setTrackResamplerUsed(VsqTrack vsq_track, int index) {
        vsq_track.Tag = setTagCor(vsq_track.Tag, TAG_VSQTRACK_RESAMPLER_USED,
                index + "");
    }

    /// <summary>
    /// 指定したトラックの音声合成器の種類を取得します
    /// </summary>
    /// <param name="vsq_track"></param>
    /// <returns></returns>
    public static RendererKind getTrackRendererKind(VsqTrack vsq_track) {
        String str_kind = getTagCor(vsq_track.Tag, TAG_VSQTRACK_RENDERER_KIND);

        if ((str_kind != null) && !str_kind.equals("")) {
            RendererKind[] values = RendererKind.values();

            for (RendererKind kind : values) {
                if (str_kind.equals(kind + "")) {
                    return kind;
                }
            }
        }

        // タグからの判定ができないので、VsqTrackのVsqCommonから判定を試みる。
        VsqCommon vsq_common = vsq_track.getCommon();

        if (vsq_common == null) {
            // お手上げである。
            return RendererKind.VOCALOID2;
        }

        String version = vsq_common.Version;

        if (version == null) {
            // お手上げである。その２
            return RendererKind.VOCALOID2;
        }

        if (version.startsWith(VSTiDllManager.RENDERER_AQT0)) {
            return RendererKind.AQUES_TONE;
        } else if (version.startsWith(VSTiDllManager.RENDERER_DSB3)) {
            return RendererKind.VOCALOID2;
        } else if (version.startsWith(VSTiDllManager.RENDERER_STR0)) {
            return RendererKind.VCNT;
        } else if (version.startsWith(VSTiDllManager.RENDERER_UTU0)) {
            return RendererKind.UTAU;
        } else if (version.startsWith(VSTiDllManager.RENDERER_NULL)) {
            return RendererKind.NULL;
        } else {
            return RendererKind.VOCALOID1;
        }
    }

    public static void setTrackRendererKind(VsqTrack vsq_track,
        RendererKind renderer_kind) {
        vsq_track.Tag = setTagCor(vsq_track.Tag, TAG_VSQTRACK_RENDERER_KIND,
                renderer_kind + "");

        VsqCommon vsq_common = vsq_track.getCommon();

        if (vsq_common != null) {
            if (renderer_kind == RendererKind.AQUES_TONE) {
                vsq_common.Version = VSTiDllManager.RENDERER_AQT0;
            } else if (renderer_kind == RendererKind.VCNT) {
                vsq_common.Version = VSTiDllManager.RENDERER_STR0;
            } else if (renderer_kind == RendererKind.UTAU) {
                vsq_common.Version = VSTiDllManager.RENDERER_UTU0;
            } else if (renderer_kind == RendererKind.VOCALOID1) {
                vsq_common.Version = VSTiDllManager.RENDERER_DSB2;
            } else if (renderer_kind == RendererKind.VOCALOID2) {
                vsq_common.Version = VSTiDllManager.RENDERER_DSB3;
            } else if (renderer_kind == RendererKind.NULL) {
                vsq_common.Version = VSTiDllManager.RENDERER_NULL;
            }
        }
    }

    private static String getTagCor(String tag, String tag_name) {
        if (tag_name == null) {
            return "";
        }

        if (tag_name.equals("")) {
            return "";
        }

        if (tag == null) {
            return "";
        }

        if (tag.equals("")) {
            return "";
        }

        String[] spl = PortUtil.splitString(tag, ';');

        for (String s : spl) {
            String[] spl2 = PortUtil.splitString(s, ':');

            if (spl2.length == 2) {
                if (tag_name.equals(spl2[0])) {
                    return spl2[1];
                }
            }
        }

        return "";
    }

    private static String setTagCor(String old_tag, String name, String value) {
        if (name == null) {
            return old_tag;
        }

        if (name.equals("")) {
            return old_tag;
        }

        String v = value.replace(":", "").replace(";", "");

        if (old_tag == null) {
            return name + ":" + value;
        } else {
            String newtag = "";
            String[] spl = PortUtil.splitString(old_tag, ';');
            boolean is_first = true;
            boolean added = false;

            for (String s : spl) {
                String[] spl2 = PortUtil.splitString(s, ':');

                if (spl2.length == 2) {
                    String add = "";

                    if (name.equals(spl2[0])) {
                        add = name + ":" + v;
                        added = true;
                    } else {
                        add = spl2[0] + ":" + spl2[1];
                    }

                    newtag += ((is_first ? "" : ";") + add);
                    is_first = false;
                }
            }

            if (is_first) {
                newtag = name + ":" + v;
            } else if (!added) {
                newtag += (";" + name + ":" + v);
            }

            return newtag;
        }
    }

    public static String getEventTag(VsqEvent item, String name) {
        return getTagCor(item.Tag, name);
    }

    public static void setEventTag(VsqEvent item, String name, String value) {
        item.Tag = setTagCor(item.Tag, name, value);
    }

    /// <summary>
    /// 指定した位置に，指定した量の空白を挿入します
    /// </summary>
    /// <param name="clock_start">空白を挿入する位置</param>
    /// <param name="clock_amount">挿入する空白の量</param>
    public void insertBlank(int clock_start, int clock_amount) {
        super.insertBlank(clock_start, clock_amount);

        Vector<BezierCurves> curves = AttachedCurves.getCurves();
        int size = vec.size(curves);

        for (int i = 0; i < size; i++) {
            BezierCurves bcs = vec.get(curves, i);
            bcs.insertBlank(clock_start, clock_amount);
        }
    }

    /// <summary>
    /// 指定した位置に，指定した量の空白を挿入します
    /// </summary>
    /// <param name="track">挿入を行う対象のトラック</param>
    /// <param name="clock_start">空白を挿入する位置</param>
    /// <param name="clock_amount">挿入する空白の量</param>
    public void insertBlank(int track, int clock_start, int clock_amount) {
        VsqTrack vsq_track = vec.get(Track, track);
        vsq_track.insertBlank(clock_start, clock_amount);

        BezierCurves bcs = AttachedCurves.get(track - 1);
        bcs.insertBlank(clock_start, clock_amount);
    }

    /// <summary>
    /// VSQファイルの指定されたクロック範囲のイベント等を削除します
    /// </summary>
    /// <param name="clock_start">削除を行う範囲の開始クロック</param>
    /// <param name="clock_end">削除を行う範囲の終了クロック</param>
    public void removePart(int clock_start, int clock_end) {
        super.removePart(clock_start, clock_end);

        Vector<BezierCurves> curves = AttachedCurves.getCurves();
        int size = vec.size(curves);

        for (int i = 0; i < size; i++) {
            BezierCurves bcs = vec.get(curves, i);
            bcs.removePart(clock_start, clock_end);
        }
    }

    /// <summary>
    /// 指定したトラックの，指定した範囲のイベント等を削除します
    /// </summary>
    /// <param name="track">削除を行う対象のトラック</param>
    /// <param name="clock_start">削除を行う範囲の開始クロック</param>
    /// <param name="clock_end">削除を行う範囲の終了クロック</param>
    public void removePart(int track, int clock_start, int clock_end) {
        VsqTrack vsq_track = vec.get(Track, track);
        vsq_track.removePart(clock_start, clock_end);

        BezierCurves bcs = AttachedCurves.get(track - 1);
        bcs.removePart(clock_start, clock_end);
    }

    /// <summary>
    /// MasterMute, トラックのMute指定、トラックのSolo指定、トラックのPlayModeを考慮し、このVSQシーケンスの指定したトラックがミュートされた状態かどうかを判定します。
    /// </summary>
    /// <param name="track"></param>
    /// <returns></returns>
    public boolean getActualMuted(int track) {
        if ((track < 1) || (Track.size() <= track)) {
            return true;
        }

        if (getMasterMute()) {
            return true;
        }

        if (getMute(track)) {
            return true;
        }

        if (!Track.get(track).isTrackOn()) {
            return true;
        }

        boolean soloSpecificationExists = false;

        for (int i = 1; i < Track.size(); i++) {
            if (getSolo(i)) {
                soloSpecificationExists = true;

                break;
            }
        }

        if (soloSpecificationExists) {
            if (getSolo(track)) {
                return getMute(track);
            } else {
                return true;
            }
        } else {
            return getMute(track);
        }
    }

    /// <summary>
    /// このVSQシーケンスのマスタートラックをミュートするかどうかを取得します。
    /// </summary>
    /// <returns></returns>
    public boolean getMasterMute() {
        if (Mixer == null) {
            return false;
        }

        return Mixer.MasterMute == 1;
    }

    /// <summary>
    /// このVSQシーケンスのマスタートラックをミュートするかどうかを設定します。
    /// </summary>
    public void setMasterMute(boolean value) {
        if (Mixer == null) {
            return;
        }

        Mixer.MasterMute = value ? 1 : 0;
    }

    /// <summary>
    /// このVSQシーケンスの指定したトラックをミュートするかどうかを取得します。
    /// </summary>
    /// <param name="track"></param>
    /// <returns></returns>
    public boolean getMute(int track) {
        if (Mixer == null) {
            return false;
        }

        if (Mixer.Slave == null) {
            return false;
        }

        if (track < 0) {
            return false;
        }

        if (track == 0) {
            return Mixer.MasterMute == 1;
        } else if ((track - 1) < Mixer.Slave.size()) {
            return Mixer.Slave.get(track - 1).Mute == 1;
        } else {
            return false;
        }
    }

    /// <summary>
    /// このVSQシーケンスの指定したトラックをミュートするかどうかを設定します。
    /// </summary>
    /// <param name="track"></param>
    /// <param name="value"></param>
    public void setMute(int track, boolean value) {
        if (Mixer == null) {
            return;
        }

        if (Mixer.Slave == null) {
            return;
        }

        if (track < 0) {
            return;
        } else if (track == 0) {
            Mixer.MasterMute = value ? 1 : 0;
        } else if ((track - 1) < Mixer.Slave.size()) {
            Mixer.Slave.get(track - 1).Mute = value ? 1 : 0;
        }
    }

    /// <summary>
    /// このVSQシーケンスの指定したトラックをソロモードとするかどうかを取得します。
    /// </summary>
    /// <param name="track"></param>
    /// <returns></returns>
    public boolean getSolo(int track) {
        if (Mixer == null) {
            return false;
        }

        if (Mixer.Slave == null) {
            return false;
        }

        if (track < 0) {
            return false;
        }

        if (track == 0) {
            return false;
        } else if ((track - 1) < Mixer.Slave.size()) {
            return Mixer.Slave.get(track - 1).Solo == 1;
        } else {
            return false;
        }
    }

    /// <summary>
    /// このVSQシーケンスの指定したトラックをソロモードとするかどうかを設定します。
    /// </summary>
    /// <param name="track"></param>
    /// <param name="value"></param>
    public void setSolo(int track, boolean value) {
        if (Mixer == null) {
            return;
        }

        if (Mixer.Slave == null) {
            return;
        }

        if (track < 0) {
            return;
        } else if (track == 0) {
            return;
        } else if ((track - 1) < Mixer.Slave.size()) {
            Mixer.Slave.get(track - 1).Solo = value ? 1 : 0;

            if (value) {
                for (int i = 0; i < Mixer.Slave.size(); i++) {
                    if (((i + 1) != track) && (Mixer.Slave.get(i).Solo == 1)) {
                        Mixer.Slave.get(i).Solo = 0;
                    }
                }
            }
        }
    }

    /// <summary>
    /// VsqEvent, VsqBPList, BezierCurvesの全てのクロックを、tempoに格納されているテンポテーブルに
    /// 合致するようにシフトします
    /// </summary>
    /// <param name="work"></param>
    /// <param name="tempo"></param>
    public void adjustClockToMatchWith(TempoVector tempo) {
        super.adjustClockToMatchWith(tempo);

        double premeasure_sec_target = getSecFromClock(getPreMeasureClocks());
        double premeasure_sec_tempo = premeasure_sec_target;

        // テンポをリプレースする場合。
        // まずクロック値を、リプレース後のモノに置き換え
        for (int track = 1; track < this.Track.size(); track++) {
            // ベジエカーブをシフト
            for (int i = 0; i < Utility.CURVE_USAGE.length; i++) {
                CurveType ct = Utility.CURVE_USAGE[i];
                Vector<BezierChain> list = this.AttachedCurves.get(track - 1)
                                                              .get(ct);

                if (list == null) {
                    continue;
                }

                for (Iterator<BezierChain> itr = list.iterator();
                        itr.hasNext();) {
                    BezierChain chain = itr.next();

                    for (Iterator<BezierPoint> itr2 = chain.points.iterator();
                            itr2.hasNext();) {
                        BezierPoint point = itr2.next();
                        PointD bse = new PointD(tempo.getClockFromSec(this.getSecFromClock(
                                        point.getBase().getX()) -
                                    premeasure_sec_target +
                                    premeasure_sec_tempo),
                                point.getBase().getY());
                        double rx = point.getBase().getX() +
                            point.controlRight.getX();
                        double new_rx = tempo.getClockFromSec(this.getSecFromClock(
                                    rx) - premeasure_sec_target +
                                premeasure_sec_tempo);
                        PointD ctrl_r = new PointD(new_rx - bse.getX(),
                                point.controlRight.getY());

                        double lx = point.getBase().getX() +
                            point.controlLeft.getX();
                        double new_lx = tempo.getClockFromSec(this.getSecFromClock(
                                    lx) - premeasure_sec_target +
                                premeasure_sec_tempo);
                        PointD ctrl_l = new PointD(new_lx - bse.getX(),
                                point.controlLeft.getY());
                        point.setBase(bse);
                        point.controlLeft = ctrl_l;
                        point.controlRight = ctrl_r;
                    }
                }
            }
        }
    }

    /// <summary>
    /// 指定秒数分，アイテムの時間をずらす．
    /// </summary>
    /// <param name="vsq">編集対象</param>
    /// <param name="sec">ずらす秒数．正の場合アイテムは後ろにずれる</param>
    /// <param name="first_tempo">ずらす秒数が正の場合に，最初のテンポをいくらにするか</param>
    public static void shift(VsqFileEx vsq, double sec, int first_tempo) {
        boolean first = true; // 負になった最初のアイテムかどうか

        // 最初にテンポをずらす．
        // 古いのから情報をコピー
        VsqFile tempo = new VsqFile("Miku", vsq.getPreMeasure(), 4, 4, 500000);
        tempo.TempoTable.clear();

        for (Iterator<TempoTableEntry> itr = vsq.TempoTable.iterator();
                itr.hasNext();) {
            TempoTableEntry item = itr.next();
            tempo.TempoTable.add(item);
        }

        tempo.updateTempoInfo();

        int tempo_count = tempo.TempoTable.size();

        if (sec < 0.0) {
            first = true;

            for (int i = tempo_count - 1; i >= 0; i--) {
                TempoTableEntry item = tempo.TempoTable.get(i);

                if ((item.Time + sec) <= 0.0) {
                    if (first) {
                        first_tempo = item.Tempo;
                        first = false;
                    } else {
                        break;
                    }
                }
            }
        }

        vsq.TempoTable.clear();
        vsq.TempoTable.add(new TempoTableEntry(0, first_tempo, 0.0));

        for (int i = 0; i < tempo_count; i++) {
            TempoTableEntry item = tempo.TempoTable.get(i);
            double t = item.Time + sec;
            int new_clock = (int) vsq.getClockFromSec(t);
            double new_time = vsq.getSecFromClock(new_clock);
            vsq.TempoTable.add(new TempoTableEntry(new_clock, item.Tempo,
                    new_time));
        }

        vsq.updateTempoInfo();

        int tracks = vsq.Track.size();
        int pre_measure_clocks = vsq.getPreMeasureClocks();

        for (int i = 1; i < tracks; i++) {
            VsqTrack track = vsq.Track.get(i);
            Vector<Integer> remove_required_event = new Vector<Integer>(); // 削除が要求されたイベントのインデクス

            // 歌手変更・音符イベントをシフト
            // 時刻が負になる場合は，後で考える
            int events = track.getEventCount();
            first = true;

            for (int k = events - 1; k >= 0; k--) {
                VsqEvent item = track.getEvent(k);
                double t = vsq.getSecFromClock(item.Clock) + sec;
                int clock = (int) vsq.getClockFromSec(t);

                if (item.ID.type == VsqIDType.Anote) {
                    // 音符の長さ
                    double t_end = vsq.getSecFromClock(item.Clock +
                            item.ID.getLength()) + sec;
                    int clock_end = (int) vsq.getClockFromSec(t_end);
                    int length = clock_end - clock;

                    if (clock < pre_measure_clocks) {
                        if (pre_measure_clocks < clock_end) {
                            // 音符の開始位置がプリメジャーよりも早く，音符の開始位置がプリメジャーより後の場合
                            clock = pre_measure_clocks;
                            length = clock_end - pre_measure_clocks;

                            // ビブラート
                            if (item.ID.VibratoHandle != null) {
                                double vibrato_percent = item.ID.VibratoHandle.getLength() / (double) item.ID.getLength() * 100.0;
                                double t_clock = vsq.getSecFromClock(clock); // 音符の開始時刻
                                double t_vibrato = t_end -
                                    (((t_end - t_clock) * vibrato_percent) / 100.0); // ビブラートの開始時刻
                                int clock_vibrato_start = (int) vsq.getClockFromSec(t_vibrato);
                                item.ID.VibratoHandle.setLength(clock_end -
                                    clock_vibrato_start);
                                item.ID.VibratoDelay = clock_vibrato_start -
                                    clock;
                            }

                            item.Clock = clock;
                            item.ID.setLength(length);
                        } else {
                            // 範囲外なので削除
                            remove_required_event.add(k);
                        }
                    } else {
                        // ビブラート
                        if (item.ID.VibratoHandle != null) {
                            double t_vibrato_start = vsq.getSecFromClock((item.Clock +
                                    item.ID.getLength()) -
                                    item.ID.VibratoHandle.getLength()) + sec;
                            int clock_vibrato_start = (int) vsq.getClockFromSec(t_vibrato_start);
                            item.ID.VibratoHandle.setLength(clock_vibrato_start -
                                clock);
                            item.ID.VibratoDelay = clock_vibrato_start - clock;
                        }

                        item.Clock = clock;
                        item.ID.setLength(length);
                    }
                } else if (item.ID.type == VsqIDType.Singer) {
                    if (item.Clock <= 0) {
                        if (sec >= 0.0) {
                            clock = 0;
                            item.Clock = clock;
                        } else {
                            if (first) {
                                clock = 0;
                                first = false;
                                item.Clock = clock;
                            } else {
                                remove_required_event.add(k);
                            }
                        }
                    } else {
                        if (clock < 0) {
                            if (first) {
                                clock = 0;
                                first = false;
                            } else {
                                remove_required_event.add(k);
                            }
                        }

                        item.Clock = clock;
                    }
                }
            }

            // 削除が要求されたものを削除
            Collections.sort(remove_required_event);

            int count = remove_required_event.size();

            for (int j = count - 1; j >= 0; j--) {
                int index = remove_required_event.get(j);
                track.removeEvent(index);
            }

            // コントロールカーブをシフト
            for (int k = 0; k < Utility.CURVE_USAGE.length; k++) {
                CurveType ct = Utility.CURVE_USAGE[k];
                VsqBPList item = track.getCurve(ct.getName());

                if (item == null) {
                    continue;
                }

                VsqBPList repl = new VsqBPList(item.getName(),
                        item.getDefault(), item.getMinimum(), item.getMaximum());
                int c = item.size();
                first = true;

                for (int j = c - 1; j >= 0; j--) {
                    int clock = item.getKeyClock(j);
                    int value = item.getElement(j);
                    double t = vsq.getSecFromClock(clock) + sec;
                    int clock_new = (int) vsq.getClockFromSec(t);

                    if (clock_new < pre_measure_clocks) {
                        if (first) {
                            clock_new = pre_measure_clocks;
                            first = false;
                        } else {
                            break;
                        }
                    }

                    repl.add(clock_new, value);
                }

                track.setCurve(ct.getName(), repl);
            }

            // ベジエカーブをシフト
            for (int k = 0; k < Utility.CURVE_USAGE.length; k++) {
                CurveType ct = Utility.CURVE_USAGE[k];
                Vector<BezierChain> list = vsq.AttachedCurves.get(i - 1).get(ct);

                if (list == null) {
                    continue;
                }

                remove_required_event.clear(); //削除するBezierChainのID

                int list_count = list.size();

                for (int j = 0; j < list_count; j++) {
                    BezierChain chain = list.get(j);

                    for (Iterator<BezierPoint> itr2 = chain.points.iterator();
                            itr2.hasNext();) {
                        BezierPoint point = itr2.next();
                        PointD bse = new PointD(vsq.getClockFromSec(vsq.getSecFromClock(
                                        point.getBase().getX()) + sec),
                                point.getBase().getY());
                        double rx = point.getBase().getX() +
                            point.controlRight.getX();
                        double new_rx = vsq.getClockFromSec(vsq.getSecFromClock(
                                    rx) + sec);
                        PointD ctrl_r = new PointD(new_rx - bse.getX(),
                                point.controlRight.getY());

                        double lx = point.getBase().getX() +
                            point.controlLeft.getX();
                        double new_lx = vsq.getClockFromSec(vsq.getSecFromClock(
                                    lx) + sec);
                        PointD ctrl_l = new PointD(new_lx - bse.getX(),
                                point.controlLeft.getY());
                        point.setBase(bse);
                        point.controlLeft = ctrl_l;
                        point.controlRight = ctrl_r;
                    }

                    double start = chain.getStart();
                    double end = chain.getEnd();

                    if (start < pre_measure_clocks) {
                        if (pre_measure_clocks < end) {
                            // プリメジャーのところでカットし，既存のものと置き換える
                            BezierChain new_chain = null;

                            try {
                                new_chain = chain.extractPartialBezier(pre_measure_clocks,
                                        end);
                                new_chain.id = chain.id;
                                list.set(j, new_chain);
                            } catch (Exception ex) {
                                serr.println("VsqFileEx#shift; ex=" + ex);
                                Logger.write(VsqFileEx.class + ".shift; ex=" +
                                    ex + "\n");
                            }
                        } else {
                            remove_required_event.add(chain.id);
                        }
                    }
                }

                // 削除が要求されたベジエカーブを削除
                count = remove_required_event.size();

                for (int j = 0; j < count; j++) {
                    int id = remove_required_event.get(j);
                    list_count = list.size();

                    for (int m = 0; m < list_count; m++) {
                        if (id == list.get(m).id) {
                            list.removeElementAt(m);

                            break;
                        }
                    }
                }
            }
        }
    }

    public Object clone() {
        VsqFileEx ret = new VsqFileEx("Miku", 1, 4, 4, 500000);
        ret.Track = new Vector<VsqTrack>();

        int c = Track.size();

        for (int i = 0; i < c; i++) {
            ret.Track.add((VsqTrack) Track.get(i).clone());
        }

        ret.TempoTable = new TempoVector();
        c = TempoTable.size();

        for (int i = 0; i < c; i++) {
            ret.TempoTable.add((TempoTableEntry) TempoTable.get(i).clone());
        }

        ret.TimesigTable = new TimesigVector(); // Vector<TimeSigTableEntry>();
        c = TimesigTable.size();

        for (int i = 0; i < c; i++) {
            ret.TimesigTable.add((TimeSigTableEntry) TimesigTable.get(i).clone());
        }

        ret.TotalClocks = TotalClocks;
        ret.Master = (VsqMaster) Master.clone();
        ret.Mixer = (VsqMixer) Mixer.clone();
        ret.AttachedCurves = (AttachedCurve) AttachedCurves.clone();
        c = BgmFiles.size();

        for (int i = 0; i < c; i++) {
            ret.BgmFiles.add((BgmFile) BgmFiles.get(i).clone());
        }

        ret.cacheDir = cacheDir;
        ret.config = (SequenceConfig) this.config.clone();

        return ret;
    }

    /// <summary>
    /// BGMリストの内容を更新するコマンドを発行します
    /// </summary>
    /// <param name="list"></param>
    /// <returns></returns>
    public static CadenciiCommand generateCommandBgmUpdate(Vector<BgmFile> list) {
        CadenciiCommand command = new CadenciiCommand();
        command.type = CadenciiCommandType.BGM_UPDATE;
        command.args = new Object[1];

        Vector<BgmFile> copy = new Vector<BgmFile>();
        int count = list.size();

        for (int i = 0; i < count; i++) {
            copy.add((BgmFile) list.get(i).clone());
        }

        command.args[0] = copy;

        return command;
    }

    /// <summary>
    /// トラックを削除するコマンドを発行します。VstRendererを取り扱う関係上、VsqCommandを使ってはならない。
    /// </summary>
    /// <param name="track"></param>
    /// <returns></returns>
    public static CadenciiCommand generateCommandDeleteTrack(int track) {
        CadenciiCommand command = new CadenciiCommand();
        command.type = CadenciiCommandType.TRACK_DELETE;
        command.args = new Object[1];
        command.args[0] = track;

        return command;
    }

    public static CadenciiCommand generateCommandTrackReplace(int track,
        VsqTrack item, BezierCurves attached_curve) {
        CadenciiCommand command = new CadenciiCommand();
        command.type = CadenciiCommandType.TRACK_REPLACE;
        command.args = new Object[3];
        command.args[0] = track;
        command.args[1] = item.clone();
        command.args[2] = attached_curve.clone();

        return command;
    }

    /// <summary>
    /// トラックを追加するコマンドを発行します．VstRendererを取り扱う関係上、VsqCommandを使ってはならない。
    /// </summary>
    /// <param name="track"></param>
    /// <returns></returns>
    public static CadenciiCommand generateCommandAddTrack(VsqTrack track,
        VsqMixerEntry mixer, int position, BezierCurves attached_curve) {
        CadenciiCommand command = new CadenciiCommand();
        command.type = CadenciiCommandType.TRACK_ADD;
        command.args = new Object[4];
        command.args[0] = track.clone();
        command.args[1] = mixer;
        command.args[2] = position;
        command.args[3] = attached_curve.clone();

        return command;
    }

    public static CadenciiCommand generateCommandAddBezierChain(int track,
        CurveType curve_type, int chain_id, int clock_resolution,
        BezierChain chain) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.BEZIER_CHAIN_ADD;
        ret.args = new Object[5];
        ret.args[0] = track;
        ret.args[1] = curve_type;
        ret.args[2] = (BezierChain) chain.clone();
        ret.args[3] = clock_resolution;
        ret.args[4] = chain_id;

        return ret;
    }

    public static CadenciiCommand generateCommandChangeSequenceConfig(
        int sample_rate, int channels, boolean output_master, int pre_measure) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.CHANGE_SEQUENCE_CONFIG;
        ret.args = new Object[] {
                sample_rate, channels, output_master, pre_measure
            };

        return ret;
    }

    public static CadenciiCommand generateCommandDeleteBezierChain(int track,
        CurveType curve_type, int chain_id, int clock_resolution) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.BEZIER_CHAIN_DELETE;
        ret.args = new Object[4];
        ret.args[0] = track;
        ret.args[1] = curve_type;
        ret.args[2] = chain_id;
        ret.args[3] = clock_resolution;

        return ret;
    }

    public static CadenciiCommand generateCommandReplaceBezierChain(int track,
        CurveType curve_type, int chain_id, BezierChain chain,
        int clock_resolution) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.BEZIER_CHAIN_REPLACE;
        ret.args = new Object[5];
        ret.args[0] = track;
        ret.args[1] = curve_type;
        ret.args[2] = chain_id;
        ret.args[3] = chain;
        ret.args[4] = clock_resolution;

        return ret;
    }

    public static CadenciiCommand generateCommandReplace(VsqFileEx vsq) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.REPLACE;
        ret.args = new Object[1];
        ret.args[0] = (VsqFileEx) vsq.clone();

        return ret;
    }

    public static CadenciiCommand generateCommandReplaceAttachedCurveRange(
        int track, TreeMap<CurveType, Vector<BezierChain>> attached_curves) {
        CadenciiCommand ret = new CadenciiCommand();
        ret.type = CadenciiCommandType.ATTACHED_CURVE_REPLACE_RANGE;
        ret.args = new Object[2];
        ret.args[0] = track;

        TreeMap<CurveType, Vector<BezierChain>> copy = new TreeMap<CurveType, Vector<BezierChain>>();

        for (Iterator<CurveType> itr = attached_curves.keySet().iterator();
                itr.hasNext();) {
            CurveType ct = itr.next();
            Vector<BezierChain> list = attached_curves.get(ct);
            Vector<BezierChain> copy_list = new Vector<BezierChain>();

            for (Iterator<BezierChain> itr2 = list.iterator(); itr2.hasNext();) {
                copy_list.add((BezierChain) (itr2.next()).clone());
            }

            copy.put(ct, copy_list);
        }

        ret.args[1] = copy;

        return ret;
    }

    public ICommand executeCommand(ICommand com) {
        CadenciiCommand command = (CadenciiCommand) com;
        CadenciiCommand ret = null;

        if (command.type == CadenciiCommandType.VSQ_COMMAND) {
            ret = new CadenciiCommand();
            ret.type = CadenciiCommandType.VSQ_COMMAND;
            ret.vsqCommand = super.executeCommand(command.vsqCommand);

            // 再レンダリングが必要になったかどうかを判定
            VsqCommandType type = command.vsqCommand.Type;

            if ((type == VsqCommandType.CHANGE_PRE_MEASURE) ||
                    (type == VsqCommandType.REPLACE) ||
                    (type == VsqCommandType.UPDATE_TEMPO) ||
                    (type == VsqCommandType.UPDATE_TEMPO_RANGE)) {
                int count = Track.size();

                for (int i = 0; i < (count - 1); i++) {
                    editorStatus.renderRequired[i] = true;
                }
            } else if ((type == VsqCommandType.EVENT_ADD) ||
                    (type == VsqCommandType.EVENT_ADD_RANGE) ||
                    (type == VsqCommandType.EVENT_CHANGE_ACCENT) ||
                    (type == VsqCommandType.EVENT_CHANGE_CLOCK) ||
                    (type == VsqCommandType.EVENT_CHANGE_CLOCK_AND_ID_CONTAINTS) ||
                    (type == VsqCommandType.EVENT_CHANGE_CLOCK_AND_ID_CONTAINTS_RANGE) ||
                    (type == VsqCommandType.EVENT_CHANGE_CLOCK_AND_LENGTH) ||
                    (type == VsqCommandType.EVENT_CHANGE_CLOCK_AND_NOTE) ||
                    (type == VsqCommandType.EVENT_CHANGE_DECAY) ||
                    (type == VsqCommandType.EVENT_CHANGE_ID_CONTAINTS) ||
                    (type == VsqCommandType.EVENT_CHANGE_ID_CONTAINTS_RANGE) ||
                    (type == VsqCommandType.EVENT_CHANGE_LENGTH) ||
                    (type == VsqCommandType.EVENT_CHANGE_LYRIC) ||
                    (type == VsqCommandType.EVENT_CHANGE_NOTE) ||
                    (type == VsqCommandType.EVENT_CHANGE_VELOCITY) ||
                    (type == VsqCommandType.EVENT_DELETE) ||
                    (type == VsqCommandType.EVENT_DELETE_RANGE) ||
                    (type == VsqCommandType.EVENT_REPLACE) ||
                    (type == VsqCommandType.EVENT_REPLACE_RANGE) ||
                    (type == VsqCommandType.TRACK_CURVE_EDIT) ||
                    (type == VsqCommandType.TRACK_CURVE_EDIT_RANGE) ||
                    (type == VsqCommandType.TRACK_CURVE_EDIT2) ||
                    (type == VsqCommandType.TRACK_CURVE_REPLACE) ||
                    (type == VsqCommandType.TRACK_CURVE_REPLACE_RANGE) ||
                    (type == VsqCommandType.TRACK_REPLACE)) {
                int track = (Integer) command.vsqCommand.Args[0];
                editorStatus.renderRequired[track - 1] = true;
            } else if (type == VsqCommandType.TRACK_ADD) {
                int position = (Integer) command.vsqCommand.Args[2];

                for (int i = 15; i >= position; i--) {
                    editorStatus.renderRequired[i] = editorStatus.renderRequired[i -
                        1];
                }

                editorStatus.renderRequired[position - 1] = true;
            } else if (type == VsqCommandType.TRACK_DELETE) {
                int track = (Integer) command.vsqCommand.Args[0];

                for (int i = track - 1; i < 15; i++) {
                    editorStatus.renderRequired[i] = editorStatus.renderRequired[i +
                        1];
                }

                editorStatus.renderRequired[15] = false;
            }
        } else {
            if (command.type == CadenciiCommandType.BEZIER_CHAIN_ADD) {
                int track = (Integer) command.args[0];
                CurveType curve_type = (CurveType) command.args[1];
                BezierChain chain = (BezierChain) command.args[2];
                int clock_resolution = (Integer) command.args[3];
                int added_id = (Integer) command.args[4];
                AttachedCurves.get(track - 1)
                              .addBezierChain(curve_type, chain, added_id);
                ret = generateCommandDeleteBezierChain(track, curve_type,
                        added_id, clock_resolution);

                if (chain.size() >= 1) {
                    // ベジエ曲線が，時間軸方向のどの範囲にわたって指定されているか判定
                    int min = (int) chain.points.get(0).getBase().getX();
                    int max = min;
                    int points_size = chain.points.size();

                    for (int i = 1; i < points_size; i++) {
                        int x = (int) chain.points.get(i).getBase().getX();
                        min = Math.min(min, x);
                        max = Math.max(max, x);
                    }

                    int max_value = curve_type.getMaximum();
                    int min_value = curve_type.getMinimum();
                    VsqBPList list = Track.get(track)
                                          .getCurve(curve_type.getName());

                    if ((min <= max) && (list != null)) {
                        // minクロック以上maxクロック以下のコントロールカーブに対して，編集を実行

                        // 最初に，min <= clock <= maxとなる範囲のデータ点を抽出（削除コマンドに指定）
                        Vector<Long> delete = new Vector<Long>();
                        int list_size = list.size();

                        for (int i = 0; i < list_size; i++) {
                            int clock = list.getKeyClock(i);

                            if ((min <= clock) && (clock <= max)) {
                                VsqBPPair item = list.getElementB(i);
                                delete.add(item.id);
                            }
                        }

                        // 追加するデータ点を列挙
                        TreeMap<Integer, VsqBPPair> add = new TreeMap<Integer, VsqBPPair>();

                        if (chain.points.size() == 1) {
                            BezierPoint p = chain.points.get(0);
                            add.put((int) p.getBase().getX(),
                                new VsqBPPair((int) p.getBase().getY(),
                                    list.getMaxID() + 1));
                        } else {
                            int last_value = Integer.MAX_VALUE;
                            int index = 0;

                            for (int clock = min; clock <= max;
                                    clock += clock_resolution) {
                                int value = (int) chain.getValue((float) clock);

                                if (value < min_value) {
                                    value = min_value;
                                } else if (max_value < value) {
                                    value = max_value;
                                }

                                if (value != last_value) {
                                    index++;
                                    add.put(clock,
                                        new VsqBPPair(value,
                                            list.getMaxID() + index));
                                    last_value = value;
                                }
                            }
                        }

                        command.vsqCommand = VsqCommand.generateCommandTrackCurveEdit2(track,
                                curve_type.getName(), delete, add);
                    }
                }

                editorStatus.renderRequired[track - 1] = true;
            } else if (command.type == CadenciiCommandType.BEZIER_CHAIN_DELETE) {
                int track = (Integer) command.args[0];
                CurveType curve_type = (CurveType) command.args[1];
                int chain_id = (Integer) command.args[2];
                int clock_resolution = (Integer) command.args[3];
                BezierChain chain = (BezierChain) AttachedCurves.get(track - 1)
                                                                .getBezierChain(curve_type,
                        chain_id).clone();
                AttachedCurves.get(track - 1).remove(curve_type, chain_id);
                ret = generateCommandAddBezierChain(track, curve_type,
                        chain_id, clock_resolution, chain);

                int points_size = chain.points.size();
                int min = (int) chain.points.get(0).getBase().getX();
                int max = min;

                for (int i = 1; i < points_size; i++) {
                    int x = (int) chain.points.get(i).getBase().getX();
                    min = Math.min(min, x);
                    max = Math.max(max, x);
                }

                VsqBPList list = Track.get(track).getCurve(curve_type.getName());
                int list_size = list.size();
                Vector<Long> delete = new Vector<Long>();

                for (int i = 0; i < list_size; i++) {
                    int clock = list.getKeyClock(i);

                    if ((min <= clock) && (clock <= max)) {
                        delete.add(list.getElementB(i).id);
                    } else if (max < clock) {
                        break;
                    }
                }

                command.vsqCommand = VsqCommand.generateCommandTrackCurveEdit2(track,
                        curve_type.getName(), delete,
                        new TreeMap<Integer, VsqBPPair>());
                editorStatus.renderRequired[track - 1] = true;
            } else if (command.type == CadenciiCommandType.BEZIER_CHAIN_REPLACE) {
                int track = (Integer) command.args[0];
                CurveType curve_type = (CurveType) command.args[1];
                int chain_id = (Integer) command.args[2];
                BezierChain chain = (BezierChain) command.args[3];
                int clock_resolution = (Integer) command.args[4];
                BezierChain target = (BezierChain) AttachedCurves.get(track -
                        1).getBezierChain(curve_type, chain_id).clone();
                AttachedCurves.get(track - 1)
                              .setBezierChain(curve_type, chain_id, chain);

                VsqBPList list = Track.get(track).getCurve(curve_type.getName());
                ret = generateCommandReplaceBezierChain(track, curve_type,
                        chain_id, target, clock_resolution);

                if (chain.size() == 1) {
                    // リプレース後のベジエ曲線が，1個のデータ点のみからなる場合
                    int ex_min = (int) chain.points.get(0).getBase().getX();
                    int ex_max = ex_min;

                    if (target.points.size() > 1) {
                        // リプレースされる前のベジエ曲線が，どの時間範囲にあったか？
                        int points_size = target.points.size();

                        for (int i = 1; i < points_size; i++) {
                            int x = (int) target.points.get(i).getBase().getX();
                            ex_min = Math.min(ex_min, x);
                            ex_max = Math.max(ex_max, x);
                        }

                        if (ex_min < ex_max) {
                            // ex_min以上ex_max以下の範囲にあるデータ点を消す
                            Vector<Long> delete = new Vector<Long>();
                            int list_size = list.size();

                            for (int i = 0; i < list_size; i++) {
                                int clock = list.getKeyClock(i);

                                if ((ex_min <= clock) && (clock <= ex_max)) {
                                    delete.add(list.getElementB(i).id);
                                }

                                if (ex_max < clock) {
                                    break;
                                }
                            }

                            // リプレース後のデータ点は1個だけ
                            TreeMap<Integer, VsqBPPair> add = new TreeMap<Integer, VsqBPPair>();
                            PointD p = chain.points.get(0).getBase();
                            add.put((int) p.getX(),
                                new VsqBPPair((int) p.getY(),
                                    list.getMaxID() + 1));

                            command.vsqCommand = VsqCommand.generateCommandTrackCurveEdit2(track,
                                    curve_type.getName(), delete, add);
                        }
                    }
                } else if (chain.size() > 1) {
                    // リプレース後のベジエ曲線の範囲
                    int min = (int) chain.points.get(0).getBase().getX();
                    int max = min;
                    int points_size = chain.points.size();

                    for (int i = 1; i < points_size; i++) {
                        int x = (int) chain.points.get(i).getBase().getX();
                        min = Math.min(min, x);
                        max = Math.max(max, x);
                    }

                    // リプレース前のベジエ曲線の範囲
                    int ex_min = min;
                    int ex_max = max;

                    if (target.points.size() > 0) {
                        ex_min = (int) target.points.get(0).getBase().getX();
                        ex_max = ex_min;

                        int target_points_size = target.points.size();

                        for (int i = 1; i < target_points_size; i++) {
                            int x = (int) target.points.get(i).getBase().getX();
                            ex_min = Math.min(ex_min, x);
                            ex_max = Math.max(ex_max, x);
                        }
                    }

                    // 削除するのを列挙
                    Vector<Long> delete = new Vector<Long>();
                    int list_size = list.size();

                    for (int i = 0; i < list_size; i++) {
                        int clock = list.getKeyClock(i);

                        if ((ex_min <= clock) && (clock <= ex_max)) {
                            delete.add(list.getElementB(i).id);
                        }

                        if (ex_max < clock) {
                            break;
                        }
                    }

                    // 追加するのを列挙
                    int max_value = curve_type.getMaximum();
                    int min_value = curve_type.getMinimum();
                    TreeMap<Integer, VsqBPPair> add = new TreeMap<Integer, VsqBPPair>();

                    if (min < max) {
                        int last_value = Integer.MAX_VALUE;
                        int index = 0;

                        for (int clock = min; clock < max;
                                clock += clock_resolution) {
                            int value = (int) chain.getValue((float) clock);

                            if (value < min_value) {
                                value = min_value;
                            } else if (max_value < value) {
                                value = max_value;
                            }

                            if (last_value != value) {
                                index++;
                                add.put(clock,
                                    new VsqBPPair(value, list.getMaxID() +
                                        index));
                            }
                        }
                    }

                    command.vsqCommand = VsqCommand.generateCommandTrackCurveEdit2(track,
                            curve_type.getName(), delete, add);
                }

                editorStatus.renderRequired[track - 1] = true;
            } else if (command.type == CadenciiCommandType.REPLACE) {
                VsqFileEx vsq = (VsqFileEx) command.args[0];
                VsqFileEx inv = (VsqFileEx) this.clone();
                Track.clear();

                int c = vsq.Track.size();

                for (int i = 0; i < c; i++) {
                    Track.add((VsqTrack) vsq.Track.get(i).clone());
                }

                TempoTable.clear();
                c = vsq.TempoTable.size();

                for (int i = 0; i < c; i++) {
                    TempoTable.add((TempoTableEntry) vsq.TempoTable.get(i)
                                                                   .clone());
                }

                TimesigTable.clear();
                c = vsq.TimesigTable.size();

                for (int i = 0; i < c; i++) {
                    TimesigTable.add((TimeSigTableEntry) vsq.TimesigTable.get(i)
                                                                         .clone());
                }

                //m_tpq = vsq.m_tpq;
                TotalClocks = vsq.TotalClocks;
                //m_base_tempo = vsq.m_base_tempo;
                Master = (VsqMaster) vsq.Master.clone();
                Mixer = (VsqMixer) vsq.Mixer.clone();
                AttachedCurves = (AttachedCurve) vsq.AttachedCurves.clone();
                updateTotalClocks();
                ret = generateCommandReplace(inv);

                int count = Track.size();

                for (int i = 0; i < (count - 1); i++) {
                    editorStatus.renderRequired[i] = true;
                }

                for (int i = count - 1; i < 16; i++) {
                    editorStatus.renderRequired[i] = false;
                }
            } else if (command.type == CadenciiCommandType.ATTACHED_CURVE_REPLACE_RANGE) {
                int track = (Integer) command.args[0];
                TreeMap<CurveType, Vector<BezierChain>> curves = (TreeMap<CurveType, Vector<BezierChain>>) command.args[1];
                TreeMap<CurveType, Vector<BezierChain>> inv = new TreeMap<CurveType, Vector<BezierChain>>();

                for (Iterator<CurveType> itr = curves.keySet().iterator();
                        itr.hasNext();) {
                    CurveType ct = itr.next();
                    Vector<BezierChain> chains = new Vector<BezierChain>();
                    Vector<BezierChain> src = this.AttachedCurves.get(track -
                            1).get(ct);

                    for (int i = 0; i < src.size(); i++) {
                        chains.add((BezierChain) src.get(i).clone());
                    }

                    inv.put(ct, chains);

                    this.AttachedCurves.get(track - 1).get(ct).clear();

                    for (Iterator<BezierChain> itr2 = curves.get(ct).iterator();
                            itr2.hasNext();) {
                        BezierChain bc = itr2.next();
                        this.AttachedCurves.get(track - 1).get(ct).add(bc);
                    }
                }

                ret = generateCommandReplaceAttachedCurveRange(track, inv);

                editorStatus.renderRequired[track - 1] = true;
            } else if (command.type == CadenciiCommandType.TRACK_ADD) {
                VsqTrack track = (VsqTrack) command.args[0];
                VsqMixerEntry mixer = (VsqMixerEntry) command.args[1];
                int position = (Integer) command.args[2];
                BezierCurves attached_curve = (BezierCurves) command.args[3];
                ret = VsqFileEx.generateCommandDeleteTrack(position);

                if (Track.size() <= 17) {
                    Track.insertElementAt((VsqTrack) track.clone(), position);
                    AttachedCurves.insertElementAt(position - 1, attached_curve);
                    Mixer.Slave.insertElementAt((VsqMixerEntry) mixer.clone(),
                        position - 1);
                }

                for (int i = 15; i >= position; i--) {
                    editorStatus.renderRequired[i] = editorStatus.renderRequired[i -
                        1];
                }

                editorStatus.renderRequired[position - 1] = true;
            } else if (command.type == CadenciiCommandType.TRACK_DELETE) {
                int track = (Integer) command.args[0];
                ret = VsqFileEx.generateCommandAddTrack(Track.get(track),
                        Mixer.Slave.get(track - 1), track,
                        AttachedCurves.get(track - 1));
                Track.removeElementAt(track);
                AttachedCurves.removeElementAt(track - 1);
                Mixer.Slave.removeElementAt(track - 1);
                updateTotalClocks();

                for (int i = track - 1; i < 15; i++) {
                    editorStatus.renderRequired[i] = editorStatus.renderRequired[i +
                        1];
                }

                editorStatus.renderRequired[15] = false;
            } else if (command.type == CadenciiCommandType.TRACK_REPLACE) {
                int track = (Integer) command.args[0];
                VsqTrack item = (VsqTrack) command.args[1];
                BezierCurves bezier_curves = (BezierCurves) command.args[2];
                ret = VsqFileEx.generateCommandTrackReplace(track,
                        Track.get(track), AttachedCurves.get(track - 1));
                Track.set(track, item);
                AttachedCurves.set(track - 1, bezier_curves);
                updateTotalClocks();

                editorStatus.renderRequired[track - 1] = true;
            } else if (command.type == CadenciiCommandType.BGM_UPDATE) {
                Vector<BgmFile> list = (Vector<BgmFile>) command.args[0];
                ret = VsqFileEx.generateCommandBgmUpdate(BgmFiles);
                BgmFiles.clear();

                int count = list.size();

                for (int i = 0; i < count; i++) {
                    BgmFiles.add(list.get(i));
                }
            } else if (command.type == CadenciiCommandType.CHANGE_SEQUENCE_CONFIG) {
                int old_pre_measure = Master.PreMeasure;
                ret = VsqFileEx.generateCommandChangeSequenceConfig(config.SamplingRate,
                        config.WaveFileOutputChannel,
                        config.WaveFileOutputFromMasterTrack, old_pre_measure);

                int sample_rate = (Integer) command.args[0];
                int channels = (Integer) command.args[1];
                boolean output_master = (Boolean) command.args[2];
                int pre_measure = (Integer) command.args[3];
                config.SamplingRate = sample_rate;
                config.WaveFileOutputChannel = channels;
                config.WaveFileOutputFromMasterTrack = output_master;
                Master.PreMeasure = pre_measure;

                if (pre_measure != old_pre_measure) {
                    updateTimesigInfo();
                }
            }

            if ((command.vsqCommand != null) && (ret != null)) {
                ret.vsqCommand = super.executeCommand(command.vsqCommand);
            }
        }

        return ret;
    }

    public void writeAsXml(String file) {
        FileOutputStream xw = null;

        try {
            xw = new FileOutputStream(file);
            mVsqSerializer.serialize(xw, this);
        } catch (Exception ex) {
            serr.println("VsqFileEx#writeAsXml; ex=" + ex);
            Logger.write(VsqFileEx.class + ".writeAsXml; ex=" + ex + "\n");
        } finally {
            if (xw != null) {
                try {
                    xw.close();
                } catch (Exception ex2) {
                    serr.println("VsqFileEx#writeAsXml; ex2=" + ex2);
                    Logger.write(VsqFileEx.class + ".writeAsXml; ex=" + ex2 +
                        "\n");
                }
            }
        }
    }

    public static VsqFileEx readFromXml(String file) {
        VsqFileEx ret = null;
        FileInputStream fs = null;

        try {
            fs = new FileInputStream(file);
            ret = (VsqFileEx) mVsqSerializer.deserialize(fs);
        } catch (Exception ex) {
            serr.println("VsqFileEx#readFromXml; ex=" + ex);
            Logger.write(VsqFileEx.class + ".readFromXml; ex=" + ex + "\n");
            ex.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception ex2) {
                    serr.println("VsqFileEx#readFromXml; ex2=" + ex2);
                    Logger.write(VsqFileEx.class + ".readFromXml; ex=" + ex2 +
                        "\n");
                    ex2.printStackTrace();
                }
            }
        }

        if (ret == null) {
            return null;
        }

        // ベジエ曲線のIDを播番
        if (ret.AttachedCurves != null) {
            int numTrack = ret.Track.size();

            if ((ret.AttachedCurves.getCurves().size() + 1) != numTrack) {
                // ベジエ曲線のデータコンテナの個数と、トラックの個数が一致しなかった場合
                ret.AttachedCurves.getCurves().clear();

                for (int i = 1; i < numTrack; i++) {
                    ret.AttachedCurves.add(new BezierCurves());
                }
            } else {
                for (Iterator<BezierCurves> itr = ret.AttachedCurves.getCurves()
                                                                    .iterator();
                        itr.hasNext();) {
                    BezierCurves bc = itr.next();

                    for (int k = 0; k < Utility.CURVE_USAGE.length; k++) {
                        CurveType ct = Utility.CURVE_USAGE[k];
                        Vector<BezierChain> list = bc.get(ct);
                        int list_size = list.size();

                        for (int i = 0; i < list_size; i++) {
                            BezierChain chain = list.get(i);
                            chain.id = i + 1;

                            int points_size = chain.points.size();

                            for (int j = 0; j < points_size; j++) {
                                chain.points.get(j).setID(j + 1);
                            }
                        }
                    }
                }
            }
        } else {
            int count = ret.Track.size();

            for (int i = 1; i < count; i++) {
                ret.AttachedCurves.add(new BezierCurves());
            }
        }

        // VsqBPListのNameを更新
        int c = ret.Track.size();

        for (int i = 0; i < c; i++) {
            VsqTrack track = ret.Track.get(i);

            for (CurveType s : Utility.CURVE_USAGE) {
                VsqBPList list = track.getCurve(s.getName());

                if (list != null) {
                    list.setName(s.getName().toLowerCase());
                }
            }
        }

        return ret;
    }
}

/*
 * BezierCurves.cs
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

import org.kbinani.xml.*;

import java.util.*;


/// <summary>
/// AtatchedCurveをXMLシリアライズするためのクラス
/// であると同時に，各トラックに付随する情報を格納するためのクラス←New!
/// </summary>
public class BezierCurves implements Cloneable {
    public static final CurveType[] CURVES = new CurveType[] {
            CurveType.DYN, CurveType.BRE, CurveType.BRI, CurveType.CLE,
            CurveType.OPE, CurveType.GEN, CurveType.POR, CurveType.VibratoRate,
            CurveType.VibratoDepth, CurveType.harmonics, CurveType.fx2depth,
            CurveType.reso1freq, CurveType.reso1bw, CurveType.reso1amp,
            CurveType.reso2freq, CurveType.reso2bw, CurveType.reso2amp,
            CurveType.reso3freq, CurveType.reso3bw, CurveType.reso3amp,
            CurveType.reso4freq, CurveType.reso4bw, CurveType.reso4amp,
            CurveType.PIT, CurveType.PBS,
        };
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Dynamics;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Brethiness;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Brightness;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Clearness;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Opening;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> GenderFactor;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> PortamentoTiming;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> VibratoRate;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> VibratoDepth;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Harmonics;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> FX2Depth;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso1Freq;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso1BW;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso1Amp;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso2Freq;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso2BW;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso2Amp;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso3Freq;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso3BW;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso3Amp;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso4Freq;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso4BW;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> Reso4Amp;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> PitchBend;
    @XmlGenericType(BezierChain.class)
    public Vector<BezierChain> PitchBendSensitivity;

    public BezierCurves() {
        Dynamics = new Vector<BezierChain>();
        Brethiness = new Vector<BezierChain>();
        Brightness = new Vector<BezierChain>();
        Clearness = new Vector<BezierChain>();
        Opening = new Vector<BezierChain>();
        GenderFactor = new Vector<BezierChain>();
        PortamentoTiming = new Vector<BezierChain>();
        VibratoRate = new Vector<BezierChain>();
        VibratoDepth = new Vector<BezierChain>();
        Harmonics = new Vector<BezierChain>();
        FX2Depth = new Vector<BezierChain>();
        Reso1Freq = new Vector<BezierChain>();
        Reso1BW = new Vector<BezierChain>();
        Reso1Amp = new Vector<BezierChain>();
        Reso2Freq = new Vector<BezierChain>();
        Reso2BW = new Vector<BezierChain>();
        Reso2Amp = new Vector<BezierChain>();
        Reso3Freq = new Vector<BezierChain>();
        Reso3BW = new Vector<BezierChain>();
        Reso3Amp = new Vector<BezierChain>();
        Reso4Freq = new Vector<BezierChain>();
        Reso4BW = new Vector<BezierChain>();
        Reso4Amp = new Vector<BezierChain>();
        PitchBend = new Vector<BezierChain>();
        PitchBendSensitivity = new Vector<BezierChain>();
    }

    public BezierChain getBezierChain(CurveType curve_type, int chain_id) {
        Vector<BezierChain> list = this.get(curve_type);
        int count = list.size();

        for (int i = 0; i < count; i++) {
            if (list.get(i).id == chain_id) {
                return list.get(i);
            }
        }

        return null;
    }

    public void setBezierChain(CurveType curve_type, int chain_id,
        BezierChain item) {
        Vector<BezierChain> list = this.get(curve_type);
        int count = list.size();

        for (int i = 0; i < count; i++) {
            if (list.get(i).id == chain_id) {
                list.set(i, item);

                break;
            }
        }
    }

    /// <summary>
    /// 指定した種類のコントロールカーブにベジエ曲線を追加します。
    /// AddBezierChainとの違い、オーバーラップする部分があれば自動的に結合されます。
    /// chainには2個以上のデータ点が含まれている必要がある
    /// </summary>
    /// <param name="curve"></param>
    /// <param name="chain"></param>
    public void mergeBezierChain(CurveType curve, BezierChain chain) {
        if (chain.points.size() <= 1) {
            return;
        }

        int chain_start = (int) chain.getStart();
        int chain_end = (int) chain.getEnd();

        // まず、全削除する必要のあるBezierChainを検索
        Vector<Integer> delete_list = new Vector<Integer>();
        Vector<BezierChain> src = this.get(curve);

        //for ( int id : this[curve].Keys ) {
        for (int j = 0; j < src.size(); j++) {
            //BezierChain bc = this[curve][id];
            BezierChain bc = src.get(j);

            if (bc.points.size() <= 0) {
                continue;
            }

            int bc_start = (int) bc.getStart();
            int bc_end = (int) bc.getEnd();

            if ((chain_start <= bc_start) && (bc_end <= chain_end)) {
                delete_list.add(bc.id);
            }
        }

        // 削除を実行
        for (Iterator<Integer> itr = delete_list.iterator(); itr.hasNext();) {
            int id = itr.next();
            remove(curve, id);

            //this[curve].Remove( id );
        }

        // マージする必要があるかどうかを検査。
        boolean processed = true;

        while (processed) {
            processed = false;

            Vector<BezierChain> list = this.get(curve);

            //for ( int id : this[curve].Keys ) {
            for (int j = 0; j < list.size(); j++) {
                //BezierChain bc = this[curve][id];
                BezierChain bc = list.get(j);
                int id = bc.id;
                int start = (int) bc.getStart();
                int end = (int) bc.getEnd();

                // 被っている箇所が2箇所以上ある可能性があるので、ifでヒットしてもbreakしない
                if ((start < chain_start) && (chain_start <= end) &&
                        (end < chain_end)) {
                    // bcのchain_start ~ endを削除し、chain_startで結合
                    BezierChain bc_edit = null;

                    try {
                        bc_edit = bc.extractPartialBezier(start, chain_start);
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".mergeBezierChain; ex=" + ex + "\n");

                        continue;
                    }

                    bc_edit.id = bc.id;

                    int last = bc_edit.size() - 1;

                    // 接合部分では、制御点無しでステップ変化する
                    bc_edit.points.get(last)
                                  .setControlRightType(BezierControlType.None);
                    chain.points.get(0)
                                .setControlLeftType(BezierControlType.None);

                    int copy_start = 0;

                    if (bc_edit.points.get(last).getBase().getY() == chain.points.get(
                                0).getBase().getY()) {
                        // bcの終点とchainの始点の座標が一致している場合
                        if (bc_edit.points.get(last).getControlLeftType() != BezierControlType.None) {
                            bc_edit.points.get(last)
                                          .setControlLeftType(BezierControlType.Master);
                        }

                        bc_edit.points.get(last)
                                      .setControlRight(chain.points.get(0).controlLeft);

                        if (chain.points.get(0).getControlRightType() != BezierControlType.None) {
                            bc_edit.points.get(last)
                                          .setControlLeftType(BezierControlType.Master);
                        }

                        copy_start = 1;
                    }

                    for (int i = copy_start; i < chain.points.size(); i++) {
                        chain.points.get(i).setID(bc_edit.getNextId());
                        bc_edit.add(chain.points.get(i));
                    }

                    //this[curve].Remove( id );
                    remove(curve, id);
                    chain = bc_edit;
                    chain_start = (int) chain.getStart();
                    chain_end = (int) chain.getEnd();
                    processed = true;

                    break;
                } else if ((chain_start < start) && (start <= chain_end) &&
                        (chain_end < end)) {
                    // bcのstart ~ chain_endを削除し、chain_endで結合
                    BezierChain bc_edit = null;

                    try {
                        bc_edit = bc.extractPartialBezier(chain_end, end);
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".mergeBezierChain; ex=" + ex + "\n");

                        continue;
                    }

                    bc_edit.id = bc.id;

                    int last = chain.size() - 1;

                    // 接合部分では、制御点無しでステップ変化する
                    bc_edit.points.get(0)
                                  .setControlLeftType(BezierControlType.None);
                    chain.points.get(last)
                                .setControlRightType(BezierControlType.None);

                    int copy_end = last;

                    if (chain.points.get(last).getBase().getY() == bc_edit.points.get(
                                0).getBase().getY()) {
                        // bcの終点とchainの始点の座標が一致している場合
                        if (chain.points.get(last).getControlLeftType() != BezierControlType.None) {
                            chain.points.get(last)
                                        .setControlLeftType(BezierControlType.Master);
                        }

                        chain.points.get(last)
                                    .setControlRight(bc_edit.points.get(0).controlLeft);

                        if (bc_edit.points.get(0).getControlRightType() != BezierControlType.None) {
                            chain.points.get(last)
                                        .setControlLeftType(BezierControlType.Master);
                        }

                        copy_end = last - 1;
                    }

                    for (int i = 0; i <= copy_end; i++) {
                        chain.points.get(i).setID(bc_edit.getNextId());
                        bc_edit.add(chain.points.get(i));
                    }

                    //this[curve].Remove( id );
                    remove(curve, id);
                    chain = bc_edit;
                    chain_start = (int) chain.getStart();
                    chain_end = (int) chain.getEnd();
                    processed = true;

                    break;
                } else if ((start < chain_start) && (chain_end < end)) {
                    // bcのchain_start ~ chain_endをchainで置き換え
                    // left + chain + right
                    BezierChain left = null;

                    try {
                        left = bc.extractPartialBezier(start, chain_start);
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".mergeBezierChain; ex=" + ex + "\n");

                        continue;
                    }

                    BezierChain right = null;

                    try {
                        right = bc.extractPartialBezier(chain_end, end);
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".mergeBezierChain; ex=" + ex + "\n");

                        continue;
                    }

                    left.id = bc.id;

                    // 接合部ではステップ変化
                    left.points.get(left.size() - 1)
                               .setControlRightType(BezierControlType.None);
                    chain.points.get(0)
                                .setControlLeftType(BezierControlType.None);
                    chain.points.get(chain.size() - 1)
                                .setControlRightType(BezierControlType.None);
                    right.points.get(0)
                                .setControlLeftType(BezierControlType.None);

                    int copy_start = 0;
                    int copy_end = chain.size() - 1;

                    if (left.points.get(left.size() - 1).getBase().getY() == chain.points.get(
                                0).getBase().getY()) {
                        // bcの終点とchainの始点の座標が一致している場合
                        if (left.points.get(left.size() - 1).getControlLeftType() != BezierControlType.None) {
                            left.points.get(left.size() - 1)
                                       .setControlLeftType(BezierControlType.Master);
                        }

                        left.points.get(left.size() - 1)
                                   .setControlRight(chain.points.get(0).controlLeft);

                        if (chain.points.get(0).getControlRightType() != BezierControlType.None) {
                            left.points.get(left.size() - 1)
                                       .setControlLeftType(BezierControlType.Master);
                        }

                        copy_start = 1;
                    }

                    if (chain.points.get(chain.size() - 1).getBase().getY() == right.points.get(
                                0).getBase().getY()) {
                        // bcの終点とchainの始点の座標が一致している場合
                        if (chain.points.get(chain.size() - 1)
                                            .getControlLeftType() != BezierControlType.None) {
                            chain.points.get(chain.size() - 1)
                                        .setControlLeftType(BezierControlType.Master);
                        }

                        chain.points.get(chain.size() - 1)
                                    .setControlRight(right.points.get(0).controlLeft);

                        if (right.points.get(0).getControlRightType() != BezierControlType.None) {
                            chain.points.get(chain.size() - 1)
                                        .setControlLeftType(BezierControlType.Master);
                        }

                        copy_end = chain.size() - 2;
                    }

                    // 追加
                    for (int i = copy_start; i <= copy_end; i++) {
                        chain.points.get(i).setID(left.getNextId());
                        left.add(chain.points.get(i));
                    }

                    for (int i = 0; i < right.points.size(); i++) {
                        right.points.get(i).setID(left.getNextId());
                        left.add(right.points.get(i));
                    }

                    //this[curve].Remove( id );
                    remove(curve, id);
                    chain = left;
                    chain_start = (int) chain.getStart();
                    chain_end = (int) chain.getEnd();
                    processed = true;

                    break;
                }
            }
        }

        if (!processed) {
            chain.id = this.getNextId(curve);
        }

        //this[curve].Add( chain.ID, chain );
        addBezierChain(curve, chain, chain.id);
    }

    /// <summary>
    /// 指定した位置に，指定した量の空白を挿入します
    /// </summary>
    /// <param name="clock_start">空白を挿入する位置</param>
    /// <param name="clock_amount">挿入する空白の量</param>
    public void insertBlank(int clock_start, int clock_amount) {
        // 全種類のカーブ
        Vector<CurveType> target_curve = new Vector<CurveType>();

        for (CurveType ct : CURVES) {
            Vector<BezierChain> vbc = get(ct);

            for (Iterator<BezierChain> itr = vbc.iterator(); itr.hasNext();) {
                BezierChain bc = itr.next();
                int size = vec.size(bc.points);

                for (int i = 0; i < size; i++) {
                    BezierPoint bp = vec.get(bc.points, i);
                    PointD p = bp.getBase();

                    if (clock_start <= p.getX()) {
                        p.setX(p.getX() + clock_amount);
                        bp.setBase(p);
                    }
                }
            }
        }
    }

    /// <summary>
    /// 全種類のカーブの指定した範囲を削除します．
    /// 削除範囲以降の部分は，操作後には(clock_end - clock_start)だけシフトされます．
    /// </summary>
    /// <param name="clock_start"></param>
    /// <param name="clock_end"></param>
    public void removePart(int clock_start, int clock_end) {
        // 全種類のカーブが対象
        Vector<CurveType> target_curve = new Vector<CurveType>();

        for (CurveType ct : CURVES) {
            vec.add(target_curve, ct);
        }

        // 便利メソッドで削除をやる
        deleteBeziers(target_curve, clock_start, clock_end);

        // データ点のclockがclock_end以降になってるものについて，シフトを行う
        int delta = clock_end - clock_start;

        for (Iterator<CurveType> itr = target_curve.iterator(); itr.hasNext();) {
            CurveType ct = itr.next();
            Vector<BezierChain> vbc = get(ct);

            for (Iterator<BezierChain> itr_bc = vbc.iterator();
                    itr_bc.hasNext();) {
                BezierChain bc = itr_bc.next();
                int size = vec.size(bc.points);

                for (int i = 0; i < size; i++) {
                    BezierPoint bp = vec.get(bc.points, i);
                    PointD p = bp.getBase();

                    if (clock_end <= p.getX()) {
                        p.setX(p.getX() - delta);
                        bp.setBase(p);
                    }
                }
            }
        }
    }

    /// <summary>
    /// 指定したカーブ種類のベジエ曲線の，指定した範囲を削除します．
    /// 削除はclock_startからclock_endの範囲について行われ，clock_end以降のシフト操作は行われません．
    /// つまり，操作後にclock_end以降のイベントが(clock_end - clock_start)だけ前方にシフトしてくることはありません．
    /// </summary>
    /// <param name="target_curve"></param>
    /// <param name="clock_start"></param>
    /// <param name="clock_end"></param>
    /// <returns></returns>
    public boolean deleteBeziers(Vector<CurveType> target_curve,
        int clock_start, int clock_end) {
        boolean edited = false;

        for (Iterator<CurveType> itr1 = target_curve.iterator();
                itr1.hasNext();) {
            CurveType curve = itr1.next();

            if (curve.isScalar() || curve.isAttachNote()) {
                continue;
            }

            Vector<BezierChain> tmp = new Vector<BezierChain>();

            for (Iterator<BezierChain> itr = this.get(curve).iterator();
                    itr.hasNext();) {
                BezierChain bc = itr.next();
                int len = bc.points.size();

                if (len < 1) {
                    continue;
                }

                int chain_start = (int) bc.points.get(0).getBase().getX();
                int chain_end;

                if (len < 2) {
                    chain_end = chain_start;
                } else {
                    chain_end = (int) bc.points.get(len - 1).getBase().getX();
                }

                if ((clock_start < chain_start) && (chain_start < clock_end) &&
                        (clock_end < chain_end)) {
                    // end ~ chain_endを残す
                    try {
                        BezierChain chain = bc.extractPartialBezier(clock_end,
                                chain_end);
                        chain.id = bc.id;
                        tmp.add(chain);
                        edited = true;
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".deleteBeziers; ex=" + ex + "\n");
                    }
                } else if ((chain_start <= clock_start) &&
                        (clock_end <= chain_end)) {
                    // chain_start ~ startとend ~ chain_endを残す
                    try {
                        BezierChain chain1 = bc.extractPartialBezier(chain_start,
                                clock_start);
                        chain1.id = bc.id;

                        BezierChain chain2 = bc.extractPartialBezier(clock_end,
                                chain_end);
                        chain2.id = -1; // 後で番号をつける
                        tmp.add(chain1);
                        tmp.add(chain2);
                        edited = true;
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class +
                            ".deleteBeziers; ex=" + ex + "\n");
                    }
                } else if ((chain_start < clock_start) &&
                        (clock_start < chain_end) && (chain_end < clock_end)) {
                    // chain_start ~ startを残す
                    try {
                        BezierChain chain = bc.extractPartialBezier(chain_start,
                                clock_start);
                        chain.id = bc.id;
                        tmp.add(chain);
                        edited = true;
                    } catch (Exception ex) {
                        Logger.write(BezierCurves.class + ".deleteBeiers; ex=" +
                            ex + "\n");
                    }
                } else if ((clock_start <= chain_start) &&
                        (chain_end <= clock_end)) {
                    // 全体を削除
                    edited = true;
                } else {
                    // 全体を残す
                    tmp.add((BezierChain) bc.clone());
                }
            }

            this.get(curve).clear();

            for (Iterator<BezierChain> itr = tmp.iterator(); itr.hasNext();) {
                BezierChain bc = itr.next();

                if (bc.id >= 0) {
                    addBezierChain(curve, bc, bc.id);
                }
            }

            for (Iterator<BezierChain> itr = tmp.iterator(); itr.hasNext();) {
                BezierChain bc = itr.next();

                if (bc.id < 0) {
                    bc.id = this.getNextId(curve);
                    addBezierChain(curve, bc, bc.id);
                }
            }
        }

        return edited;
    }

    public void remove(CurveType curve_type, int chain_id) {
        Vector<BezierChain> list = this.get(curve_type);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == chain_id) {
                list.removeElementAt(i);

                break;
            }
        }
    }

    /// <summary>
    /// 指定したコントロールカーブにベジエ曲線を追加します。
    /// </summary>
    /// <param name="curve_type"></param>
    /// <param name="chain"></param>
    public void addBezierChain(CurveType curve_type, BezierChain chain,
        int chain_id) {
        BezierChain add = (BezierChain) chain.clone();
        add.id = chain_id;
        this.get(curve_type).add(add);
    }

    public Object clone() {
        BezierCurves ret = new BezierCurves();

        for (int j = 0; j < Utility.CURVE_USAGE.length; j++) {
            CurveType ct = Utility.CURVE_USAGE[j];
            Vector<BezierChain> src = this.get(ct);
            ret.set(ct, new Vector<BezierChain>());

            int count = src.size();

            for (int i = 0; i < count; i++) {
                ret.get(ct).add((BezierChain) src.get(i).clone());
            }
        }

        return ret;
    }

    public int getNextId(CurveType curve_type) {
        Vector<BezierChain> bc = this.get(curve_type);
        int ret = bc.size();
        boolean found = true;

        while (found) {
            found = false;

            for (int i = 0; i < bc.size(); i++) {
                if (bc.get(i).id == ret) {
                    found = true;
                    ret++;

                    break;
                }
            }
        }

        return ret;
    }

    public Vector<BezierChain> get(CurveType curve) {
        if (curve.equals(CurveType.BRE)) {
            return Brethiness;
        } else if (curve.equals(CurveType.BRI)) {
            return Brightness;
        } else if (curve.equals(CurveType.CLE)) {
            return Clearness;
        } else if (curve.equals(CurveType.DYN)) {
            return Dynamics;
        } else if (curve.equals(CurveType.fx2depth)) {
            return FX2Depth;
        } else if (curve.equals(CurveType.GEN)) {
            return GenderFactor;
        } else if (curve.equals(CurveType.harmonics)) {
            return Harmonics;
        } else if (curve.equals(CurveType.OPE)) {
            return Opening;
        } else if (curve.equals(CurveType.POR)) {
            return PortamentoTiming;
        } else if (curve.equals(CurveType.PIT)) {
            return PitchBend;
        } else if (curve.equals(CurveType.PBS)) {
            return PitchBendSensitivity;
        } else if (curve.equals(CurveType.reso1amp)) {
            return Reso1Amp;
        } else if (curve.equals(CurveType.reso1bw)) {
            return Reso1BW;
        } else if (curve.equals(CurveType.reso1freq)) {
            return Reso1Freq;
        } else if (curve.equals(CurveType.reso2amp)) {
            return Reso2Amp;
        } else if (curve.equals(CurveType.reso2bw)) {
            return Reso2BW;
        } else if (curve.equals(CurveType.reso2freq)) {
            return Reso2Freq;
        } else if (curve.equals(CurveType.reso3amp)) {
            return Reso3Amp;
        } else if (curve.equals(CurveType.reso3bw)) {
            return Reso3BW;
        } else if (curve.equals(CurveType.reso3freq)) {
            return Reso3Freq;
        } else if (curve.equals(CurveType.reso4amp)) {
            return Reso4Amp;
        } else if (curve.equals(CurveType.reso4bw)) {
            return Reso4BW;
        } else if (curve.equals(CurveType.reso4freq)) {
            return Reso4Freq;
        } else if (curve.equals(CurveType.VibratoDepth)) {
            return VibratoDepth;
        } else if (curve.equals(CurveType.VibratoRate)) {
            return VibratoRate;
        } else {
            return null;
        }
    }

    public void set(CurveType curve, Vector<BezierChain> value) {
        if (curve.equals(CurveType.BRE)) {
            Brethiness = value;
        } else if (curve.equals(CurveType.BRI)) {
            Brightness = value;
        } else if (curve.equals(CurveType.CLE)) {
            Clearness = value;
        } else if (curve.equals(CurveType.DYN)) {
            Dynamics = value;
        } else if (curve.equals(CurveType.fx2depth)) {
            FX2Depth = value;
        } else if (curve.equals(CurveType.GEN)) {
            GenderFactor = value;
        } else if (curve.equals(CurveType.harmonics)) {
            Harmonics = value;
        } else if (curve.equals(CurveType.OPE)) {
            Opening = value;
        } else if (curve.equals(CurveType.POR)) {
            PortamentoTiming = value;
        } else if (curve.equals(CurveType.PIT)) {
            PitchBend = value;
        } else if (curve.equals(CurveType.PBS)) {
            PitchBendSensitivity = value;
        } else if (curve.equals(CurveType.reso1amp)) {
            Reso1Amp = value;
        } else if (curve.equals(CurveType.reso1bw)) {
            Reso1BW = value;
        } else if (curve.equals(CurveType.reso1freq)) {
            Reso1Freq = value;
        } else if (curve.equals(CurveType.reso2amp)) {
            Reso2Amp = value;
        } else if (curve.equals(CurveType.reso2bw)) {
            Reso2BW = value;
        } else if (curve.equals(CurveType.reso2freq)) {
            Reso2Freq = value;
        } else if (curve.equals(CurveType.reso3amp)) {
            Reso3Amp = value;
        } else if (curve.equals(CurveType.reso3bw)) {
            Reso3BW = value;
        } else if (curve.equals(CurveType.reso3freq)) {
            Reso3Freq = value;
        } else if (curve.equals(CurveType.reso4amp)) {
            Reso4Amp = value;
        } else if (curve.equals(CurveType.reso4bw)) {
            Reso4BW = value;
        } else if (curve.equals(CurveType.reso4freq)) {
            Reso4Freq = value;
        } else if (curve.equals(CurveType.VibratoDepth)) {
            VibratoDepth = value;
        } else if (curve.equals(CurveType.VibratoRate)) {
            VibratoRate = value;
        }
    }
}

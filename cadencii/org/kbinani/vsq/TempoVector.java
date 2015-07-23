/*
 * TempoVector.cs
 * Copyright © 2010-2011 kbinani
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

import java.io.*;
import java.util.*;
import org.kbinani.*;


    /// <summary>
    /// テンポ情報を格納したテーブル．
    /// </summary>
    public class TempoVector extends Vector<TempoTableEntry> implements Serializable
    {
        /// <summary>
        /// 4分音符1拍あたりのゲートタイム
        /// </summary>
        protected static final int gatetimePerQuater = 480;
        /// <summary>
        /// デフォルトのテンポ値(4分音符1拍あたりのマイクロ秒)
        /// </summary>
        protected static final int superTempo = 500000;

        /// <summary>
        /// コンストラクタ
        /// </summary>
        public TempoVector()
        {
super();
        }

        /// <summary>
        /// 指定した時刻におけるゲートタイムを取得します
        /// </summary>
        /// <param name="time">ゲートタイムを取得する時刻(秒)</param>
        /// <returns>ゲートタイム</returns>
        public double getClockFromSec( double time )
        {
return getClockFromSec( time, null );
        }

        /// <summary>
        /// 指定した時刻におけるゲートタイムを取得します．
        /// このメソッドでは検索コンテキストを用い，取得したいtimeの値が順に大きくなる状況でこのメソッドの実行速度の高速化を図ります
        /// </summary>
        /// <param name="time">ゲートタイムを取得する時刻(秒)</param>
        /// <param name="context">計算を高速化するための検索コンテキスト</param>
        /// <returns>ゲートタイム</returns>
        public double getClockFromSec( double time, TempoVectorSearchContext context )
        {
int tempo = superTempo;
double super_clock = 0;
double super_time = 0.0;
int c = size();
if ( c == 0 ) {
    tempo = superTempo;
    super_clock = 0;
    super_time = 0.0;
} else if ( c == 1 ) {
    TempoTableEntry t0 = get( 0 );
    tempo = t0.Tempo;
    super_clock = t0.Clock;
    super_time = t0.Time;
} else {
    int i0 = 0;
    if ( context != null ) {
        if ( time >= context.mSec2ClockSec ) {
            // 探そうとしている時刻が前回検索時の時刻と同じかそれ以降の場合
            i0 = context.mSec2ClockIndex;
        } else {
            // リセットする
            context.mSec2ClockIndex = 0;
        }
        context.mSec2ClockSec = time;
    }
    TempoTableEntry prev = null;
    for ( int i = i0; i < c; i++ ) {
        TempoTableEntry item = get( i );
        if ( time <= item.Time ) {
            if ( context != null ) {
                context.mSec2ClockIndex = i > 0 ? i - 1 : 0;
            }
            break;
        }
        prev = item;
    }
    if ( prev != null ) {
        super_time = prev.Time;
        super_clock = prev.Clock;
        tempo = prev.Tempo;
    }
}
double dt = time - super_time;
return super_clock + dt * gatetimePerQuater * 1000000.0 / (double)tempo;
        }

        /// <summary>
        /// このテーブルに登録されているテンポ変更イベントのうち、時刻に関する情報を再計算します。
        /// 新しいテンポ変更イベントを登録したり、既存のイベントを変更した場合に、都度呼び出す必要があります
        /// </summary>
        public void updateTempoInfo()
        {
int c = size();
if ( c == 0 ) {
    add( new TempoTableEntry( 0, superTempo, 0.0 ) );
}
Collections.sort( this );
TempoTableEntry item0 = get( 0 );
if ( item0.Clock != 0 ) {
    item0.Time = (double)superTempo * (double)item0.Clock / (gatetimePerQuater * 1000000.0);
} else {
    item0.Time = 0.0;
}
double prev_time = item0.Time;
int prev_clock = item0.Clock;
int prev_tempo = item0.Tempo;
double inv_tpq_sec = 1.0 / (gatetimePerQuater * 1000000.0);
for ( int i = 1; i < c; i++ ) {
    TempoTableEntry itemi = get( i );
    itemi.Time = prev_time + (double)prev_tempo * (double)(itemi.Clock - prev_clock) * inv_tpq_sec;
    prev_time = itemi.Time;
    prev_tempo = itemi.Tempo;
    prev_clock = itemi.Clock;
}
        }

        /// <summary>
        /// 指定したゲートタイムにおける時刻を取得します
        /// </summary>
        /// <param name="clock">時刻を取得するゲートタイム</param>
        /// <returns>時刻(秒)</returns>
        public double getSecFromClock( double clock )
        {
int c = size();
for ( int i = c - 1; i >= 0; i-- ) {
    TempoTableEntry item = get( i );
    if ( item.Clock <= clock ) {
        double init = item.Time;
        double dclock = clock - item.Clock;
        double sec_per_clock1 = item.Tempo * 1e-6 / 480.0;
        return init + dclock * sec_per_clock1;
    }
}

double sec_per_clock = superTempo * 1e-6 / 480.0;
return clock * sec_per_clock;
        }
    }


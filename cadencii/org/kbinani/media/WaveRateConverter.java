/*
 * WaveRateConverter.cs
 * Copyright © 2010-2011 kbinani
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

import java.io.*;
import org.kbinani.*;

    /// <summary>
    /// サンプリングレートを変換しながらWaveファイルを読み込むくためのクラス。
    /// 接頭辞a: 単位が変換前のサンプル数になっている変数
    /// 接頭辞b: 単位が変換後のサンプル数になっている変数
    /// 接頭辞sec: 単位が秒になっている変数
    /// </summary>
    public class WaveRateConverter {
        /// <summary>
        /// Waveデータの読み込み元
        /// </summary>
        private WaveReader reader = null;
        /// <summary>
        /// 変換後のサンプリングレート
        /// </summary>
        private int aRate;
        /// <summary>
        /// 変換するデータ点ユニットのサイズ
        /// bUnit個のデータをaUnit個に変更する
        /// </summary>
        private int bUnit = 1;
        /// <summary>
        /// 変換ユニットの，変換後のサイズ
        /// bUnit個のデータをaUnit個に変更する
        /// </summary>
        private int aUnit = 1;
        /// <summary>
        /// 読み込み元のサンプルレート
        /// </summary>
        private int bRate;
        /// <summary>
        /// 変換後のサンプル数
        /// </summary>
        private long aTotalSamples;
        /// <summary>
        /// 左チャンネル用バッファ
        /// </summary>
        private double[] bufLeft;
        /// <summary>
        /// 右チャンネル用バッファ
        /// </summary>
        private double[] bufRight;
        /// <summary>
        /// バッファの長さ
        /// </summary>
        private int bBuflen;

        static final int MAX_BUFLEN = 1024;

        public WaveRateConverter( WaveReader wave_reader, int rate ) {
reader = wave_reader;
aRate = rate;
bRate = reader.getSampleRate();
int gcd = (int)math.gcd( aRate, bRate );
bUnit = bRate / gcd;
aUnit = aRate / gcd;
aTotalSamples = (long)(aRate * (double)reader.getTotalSamples() / (double)bRate);

// バッファの長さはbUnitの倍数にする（補間が楽なので）
int numUnit = MAX_BUFLEN / bUnit;
if ( numUnit <= 0 ) {
    numUnit = 1;
}
bBuflen = numUnit * bUnit;
bufLeft = new double[bBuflen];
bufRight = new double[bBuflen];
        }

        public String getFilePath() {
return reader.getFilePath();
        }

        public Object getTag() {
if ( reader == null ) {
    return null;
} else {
    return reader.getTag();
}
        }

        public void setTag( Object value ) {
if ( reader != null ) {
    reader.setTag( value );
}
        }

        public long getTotalSamples() {
return aTotalSamples;
        }

        public void read( long index, int length, double[] left, double[] right )
throws IOException
        {
if ( reader == null ) {
    return;
}
if ( bRate == aRate ) {
    reader.read( index, length, left, right );
} else {
    double secStart = index / (double)aRate;
    double secEnd = (index + length) / (double)aRate;
    // bIndexStartサンプルから、bIndexEndサンプルまでを読み込めばOK
    int aProcessed = 0;
    while ( aProcessed < length ) {
        int bIndexStart = (int)((double)bRate * (double)(index + aProcessed) / (double)aRate);
        int bIndexEnd = (int)((double)bRate * (double)(index + length) / (double)aRate) + 1;
        int bRemain = bIndexEnd - bIndexStart;
        bRemain = (bRemain > bBuflen) ? bBuflen : bRemain;
        reader.read( bIndexStart, bRemain, bufLeft, bufRight );
        // bufLeft[i]のとき bIndex = bIndexStart + bProcessed + i;
        // left[aProcessed], right[aProcessed]から処理を開始
        while ( true ) {
            int bIndexRequired = (int)((double)bRate * (double)(index + aProcessed) / (double)aRate);
            int bIndexLocal = bIndexRequired - bIndexStart;
            if ( bIndexLocal < 0 ) {
                break;
            }
            if ( bIndexLocal + 1 >= bBuflen ) {
                break;
            }
            double sec0 = bIndexRequired / (double)bRate;
            double sec1 = (bIndexRequired + 1) / (double)bRate;

            // left
            double y0 = bufLeft[bIndexLocal];
            double y1 = bufLeft[bIndexLocal + 1];
            double sec = (index + aProcessed) / (double)aRate;
            double a = (y1 - y0) / (sec1 - sec0);
            double y = y0 + a * (sec - sec0);
            left[aProcessed] = y;

            // right
            y0 = bufRight[bIndexLocal];
            y1 = bufRight[bIndexLocal + 1];
            a = (y1 - y0) / (sec1 - sec0);
            y = y0 + a * (sec - sec0);
            right[aProcessed] = y;

            aProcessed++;
            if ( aProcessed >= length ) {
                break;
            }
        }
    }
}
        }

        public void close() 
throws IOException
        {
if ( reader == null ) {
    return;
}
reader.close();
reader = null;
        }
    }


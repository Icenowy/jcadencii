/*
 * BPPair.cs
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

import java.io.*;

    /// <summary>
    /// ゲートタイムと、何らかのパラメータ値とのペアを表します。主にVsqBPListで使用します。
    /// </summary>
    public class BPPair implements Comparable<BPPair>, Serializable
    {
        public int Clock;
        public int Value;

        /// <summary>
        /// このインスタンスと、指定したオブジェクトを比較します
        /// </summary>
        /// <param name="item"></param>
        /// <returns></returns>
        public int compareTo( BPPair item )
        {
if ( Clock > item.Clock )
{
    return 1;
}
else if ( Clock < item.Clock )
{
    return -1;
}
else
{
    return 0;
}
        }


        /// <summary>
        /// 指定されたゲートタイムとパラメータ値を使って、新しいインスタンスを初期化します。
        /// </summary>
        /// <param name="clock_"></param>
        /// <param name="value_"></param>
        public BPPair( int clock_, int value_ )
        {
Clock = clock_;
Value = value_;
        }
    };


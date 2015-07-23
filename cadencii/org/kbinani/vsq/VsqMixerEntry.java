/*
* VsqMixerEntry.cs
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

import java.io.*;


/// <summary>
/// VsqMixerのSlave要素に格納される各エントリ
/// </summary>
public class VsqMixerEntry implements Cloneable, Serializable {
    public int Feder;
    public int Panpot;
    public int Mute;
    public int Solo;

    /// <summary>
    /// 各パラメータを指定したコンストラクタ
    /// </summary>
    /// <param name="feder">Feder値</param>
    /// <param name="panpot">Panpot値</param>
    /// <param name="mute">Mute値</param>
    /// <param name="solo">Solo値</param>
    public VsqMixerEntry(int feder, int panpot, int mute, int solo) {
        this.Feder = feder;
        this.Panpot = panpot;
        this.Mute = mute;
        this.Solo = solo;
    }

    public VsqMixerEntry() {
        this(0, 0, 0, 0);
    }

    public Object clone() {
        VsqMixerEntry res = new VsqMixerEntry(Feder, Panpot, Mute, Solo);

        return res;
    }
}

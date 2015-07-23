/*
 * EditorStatus.cs
 * Copyright © 2009-2011 kbinani
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

public class EditorStatus {
    static final int NUM_TRACK = 16;

    /// <summary>
    /// トラックのレンダリングが必要かどうかを表すフラグ
    /// </summary>
    public boolean[] renderRequired = new boolean[NUM_TRACK];

    public EditorStatus() {
        for (int i = 0; i < NUM_TRACK; i++) {
            renderRequired[i] = false;
        }
    }

    public EditorStatus clone() {
        EditorStatus ret = new EditorStatus();

        for (int i = 0; i < renderRequired.length; i++) {
            ret.renderRequired[i] = renderRequired[i];
        }

        return ret;
    }
}

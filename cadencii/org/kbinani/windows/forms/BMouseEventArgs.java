/*
 * BMouseEventArgs.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.windows.forms.
 *
 * org.kbinani.windows.forms is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.windows.forms is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.windows.forms;

import org.kbinani.BEventArgs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class BMouseEventArgs extends BEventArgs {
    public BMouseButtons Button;
    public int Clicks;
    public int X;
    public int Y;
    public int Delta;

    public BMouseEventArgs(BMouseButtons button, int clicks, int x, int y,
        int delta) {
        Button = button;
        Clicks = clicks;
        X = x;
        Y = y;
        Delta = delta;
    }

    public static BMouseEventArgs fromMouseEvent(MouseEvent e) {
        BMouseButtons btn = BMouseButtons.Left;

        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            btn = BMouseButtons.Left;

            break;

        case MouseEvent.BUTTON2:
            btn = BMouseButtons.Middle;

            break;

        case MouseEvent.BUTTON3:
            btn = BMouseButtons.Right;

            break;
        }

        return new BMouseEventArgs(btn, e.getClickCount(), e.getX(), e.getY(), 0);
    }

    public static BMouseEventArgs fromMouseWheelEvent(MouseWheelEvent e) {
        BMouseEventArgs ret = fromMouseEvent(e);
        // .NETのばあい，１回のホイールにつき120動く
        // javaは1のようなので.NETにあわせた
        ret.Delta = -120 * e.getWheelRotation();

        return ret;
    }
}

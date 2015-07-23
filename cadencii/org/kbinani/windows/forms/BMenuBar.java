/*
 * BMenuBar.cs
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

import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuBar;


public class BMenuBar extends JMenuBar implements MouseListener {
    private static final long serialVersionUID = 3226363259935125445L;

    // root impl of Mouse* event is in BButton
    public final BEvent<BMouseEventHandler> mouseClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDoubleClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDownEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseUpEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BEventHandler> mouseEnterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> mouseLeaveEvent = new BEvent<BEventHandler>();

    public BMenuBar() {
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        try {
            mouseClickEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));

            if (e.getClickCount() >= 2) {
                mouseDoubleClickEvent.raise(this,
                    BMouseEventArgs.fromMouseEvent(e));
            }
        } catch (Exception ex) {
            System.err.println("BButton#mouseClicked; ex=" + ex);
        }
    }

    public void mouseEntered(MouseEvent e) {
        try {
            mouseEnterEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#mouseEntered; ex=" + ex);
        }
    }

    public void mouseExited(MouseEvent e) {
        try {
            mouseLeaveEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#mouseExited; ex=" + ex);
        }
    }

    public void mousePressed(MouseEvent e) {
        try {
            mouseDownEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mousePressed; ex=" + ex);
        }
    }

    public void mouseReleased(MouseEvent e) {
        try {
            mouseUpEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mouseReleased; ex=" + ex);
        }
    }
}

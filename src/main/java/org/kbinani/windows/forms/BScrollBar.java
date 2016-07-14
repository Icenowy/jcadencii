/*
 * BScrollBar.cs
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

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollBar;


public class BScrollBar extends JScrollBar implements AdjustmentListener,
    ComponentListener, FocusListener {
    private static final long serialVersionUID = 1L;
    public final BEvent<BEventHandler> valueChangedEvent = new BEvent<BEventHandler>();

    // root impl of FocusListener is in BButton
    public final BEvent<BEventHandler> enterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> leaveEvent = new BEvent<BEventHandler>();

    // root impl of ComponentListener is in BButton
    public final BEvent<BEventHandler> visibleChangedEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> resizeEvent = new BEvent<BEventHandler>();

    public BScrollBar(int orientation) {
        super();
        addAdjustmentListener(this);
        addComponentListener(this);
        addFocusListener(this);
        setOrientation(orientation);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        try {
            valueChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BHScrollBar#adjustmentValueChanged; ex=" + ex);
        }
    }

    public void focusGained(FocusEvent e) {
        try {
            enterEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#focusGained; ex=" + ex);
        }
    }

    public void focusLost(FocusEvent e) {
        try {
            leaveEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#focusLost; ex=" + ex);
        }
    }

    public void componentHidden(ComponentEvent e) {
        try {
            visibleChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentHidden; ex=" + ex);
        }
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        try {
            resizeEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentResized; ex=" + ex);
        }
    }

    public void componentShown(ComponentEvent e) {
        try {
            visibleChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentShown; ex=" + ex);
        }
    }
}

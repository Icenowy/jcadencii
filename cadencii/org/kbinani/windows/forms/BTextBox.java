/*
 * BTextBox.cs
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class BTextBox extends JTextField implements KeyListener,
    DocumentListener {
    private static final long serialVersionUID = 7503633539526888136L;

    /* root impl of TextChanged event */
    // root impl of TextChanged event is in BTextBox
    public final BEvent<BEventHandler> textChangedEvent = new BEvent<BEventHandler>();

    // root impl of KeyListener is in BButton
    public final BEvent<BPreviewKeyDownEventHandler> previewKeyDownEvent = new BEvent<BPreviewKeyDownEventHandler>();
    public final BEvent<BKeyEventHandler> keyDownEvent = new BEvent<BKeyEventHandler>();
    public final BEvent<BKeyEventHandler> keyUpEvent = new BEvent<BKeyEventHandler>();
    public final BEvent<BKeyPressEventHandler> keyPressEvent = new BEvent<BKeyPressEventHandler>();

    public BTextBox() {
        super();
        addKeyListener(this);
        getDocument().addDocumentListener(this);
    }

    public void changedUpdate(DocumentEvent e) {
        updates(e);
    }

    public void insertUpdate(DocumentEvent e) {
        updates(e);
    }

    public void removeUpdate(DocumentEvent e) {
        updates(e);
    }

    public void updates(DocumentEvent e) {
        try {
            textChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BTextBox#updates; ex=" + ex);
        }
    }

    public boolean isImeModeOn() {
        try {
            InputContext ic = getInputContext();

            if (ic == null) {
                return false;
            }

            boolean ret = ic.isCompositionEnabled();

            return ret;
        } catch (Exception ex) {
            System.err.println("BTextBox#isImeModeOn; ex=" + ex);
        }

        return false;
    }

    public void setImeModeOn(boolean value) {
        try {
            InputContext ic = getInputContext();

            if (ic == null) {
                return;
            }

            ic.setCompositionEnabled(value);
        } catch (Exception ex) {
            System.err.println("BTextBox#setImeModeOn; ex=" + ex);
        }
    }

    public void keyPressed(KeyEvent e) {
        try {
            previewKeyDownEvent.raise(this, new BPreviewKeyDownEventArgs(e));
            keyDownEvent.raise(this, new BKeyEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyPressed; ex=" + ex);
        }
    }

    public void keyReleased(KeyEvent e) {
        try {
            keyUpEvent.raise(this, new BKeyEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyReleased; ex=" + ex);
        }
    }

    public void keyTyped(KeyEvent e) {
        try {
            previewKeyDownEvent.raise(this, new BPreviewKeyDownEventArgs(e));
            keyPressEvent.raise(this, new BKeyPressEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyType; ex=" + ex);
        }
    }
}

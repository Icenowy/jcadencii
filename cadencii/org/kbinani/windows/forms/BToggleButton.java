/*
 * BToggleButton.cs
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JToggleButton;


public class BToggleButton extends JToggleButton implements ActionListener,
    ItemListener, FocusListener {
    private static final long serialVersionUID = 6912088646788737458L;

    // root impl of FocusListener is in BButton
    public final BEvent<BEventHandler> enterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> leaveEvent = new BEvent<BEventHandler>();

    // root impl of ItemListener is in BCheckBox
    public final BEvent<BEventHandler> checkedChangedEvent = new BEvent<BEventHandler>();

    // root impl of Click event is in BButton
    public final BEvent<BEventHandler> clickEvent = new BEvent<BEventHandler>();

    public BToggleButton() {
        super();
        addActionListener(this);
        addItemListener(this);
        addFocusListener(this);
    }

    public void focusGained(FocusEvent e) {
        try {
            enterEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BToggleButton#focusGained; ex=" + ex);
        }
    }

    public void focusLost(FocusEvent e) {
        try {
            leaveEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BToggleButton#focusLost; ex=" + ex);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        try {
            checkedChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BCheckBox#itemStateChanged; ex=" + ex);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            clickEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#actionPerformed; ex=" + ex);
        }
    }
}

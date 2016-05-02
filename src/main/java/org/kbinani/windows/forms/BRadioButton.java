/*
 * BButton.cs
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButton;


public class BRadioButton extends JRadioButton implements ItemListener {
    private static final long serialVersionUID = 6869663294795814279L;

    // root impl of ItemListener is in BCheckBox
    public final BEvent<BEventHandler> checkedChangedEvent = new BEvent<BEventHandler>();

    public BRadioButton() {
        super();
        addItemListener(this);
    }

    public void itemStateChanged(ItemEvent e) {
        try {
            checkedChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BCheckBox#itemStateChanged; ex=" + ex);
        }
    }
}

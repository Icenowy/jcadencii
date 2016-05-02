/*
 * BTimer.cs
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

import javax.swing.Timer;


public class BTimer extends Timer implements ActionListener {
    private static final long serialVersionUID = 9174919033610117641L;
    public final BEvent<BEventHandler> tickEvent = new BEvent<BEventHandler>();

    public BTimer() {
        super(100, null);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            tickEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BTimer#actionPerformed; ex=" + ex);
        }
    }
}

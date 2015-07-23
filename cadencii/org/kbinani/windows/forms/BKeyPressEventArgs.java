/*
 * BKeyPressEventArgs.cs
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

import java.awt.event.KeyEvent;


public class BKeyPressEventArgs extends BEventArgs {
    private KeyEvent m_original = null;
    public boolean Alt;
    public boolean Control;
    public int KeyValue;
    public boolean Shift;

    public BKeyPressEventArgs(KeyEvent e) {
        m_original = e;

        Alt = m_original.isAltDown();
        Control = m_original.isControlDown();
        KeyValue = m_original.getKeyCode();
        Shift = m_original.isShiftDown();
    }
}

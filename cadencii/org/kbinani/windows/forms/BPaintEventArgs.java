/*
 * BPaintEventArgs.cs
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

import java.awt.Graphics;
import org.kbinani.BEventArgs;

public class BPaintEventArgs extends BEventArgs{
    public Graphics Graphics;

    public BPaintEventArgs(Graphics g1) {
        Graphics = g1;
    }

}

/*
 * BCancelEventArgs.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.componentmodel;

import org.kbinani.BEventArgs;


public class BCancelEventArgs extends BEventArgs {
    public boolean Cancel = false;

    public BCancelEventArgs(boolean value) {
        Cancel = value;
    }

    public BCancelEventArgs() {
        this(false);
    }
}

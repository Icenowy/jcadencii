/*
 * BProgressChangedEventHandler.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.componentmodel.
 *
 * org.kbinani.componentmodel is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.componentmodel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.componentmodel;

import org.kbinani.*;


public class BProgressChangedEventHandler extends BEventHandler {
    public BProgressChangedEventHandler(Object sender, String method_name) {
        super(sender, method_name, Void.TYPE, Object.class,
            BProgressChangedEventArgs.class);
    }

    public BProgressChangedEventHandler(Class<?> sender, String method_name) {
        super(sender, method_name, Void.TYPE, Object.class,
            BProgressChangedEventArgs.class);
    }
}

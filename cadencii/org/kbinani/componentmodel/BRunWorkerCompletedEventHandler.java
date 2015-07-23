/*
 * BRunWorkerCompletedEventHandler.cs
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

import org.kbinani.*;

public class BRunWorkerCompletedEventHandler extends BEventHandler{
    public BRunWorkerCompletedEventHandler( Object sender, String method_name ){
        super( sender, method_name, Void.TYPE, Object.class, BRunWorkerCompletedEventArgs.class );
    }

    public BRunWorkerCompletedEventHandler( Class<?> sender, String method_name ){
        super( sender, method_name, Void.TYPE, Object.class, BRunWorkerCompletedEventArgs.class );
    }
}

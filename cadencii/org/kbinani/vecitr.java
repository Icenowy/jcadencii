/*
 * vecitr.cs
 * Copyright c 2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.vsq is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.kbinani;

import java.util.Vector;


        public class vecitr<T>
        {
Vector<T> list;
int index = 0;

public vecitr( Vector<T> list )
{
    this.list = list;
    index = 0;
}

public boolean hasNext()
{
    if ( index + 1 < list.size() ) {
        return true;
    } else {
        return false;
    }
}

public T next()
{
    index++;
    return list.get( index );

}
        };


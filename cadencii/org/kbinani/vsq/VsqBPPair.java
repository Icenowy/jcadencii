/*
 * VsqBPPair.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.vsq.
 *
 * org.kbinani.vsq is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.vsq is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.vsq;

import java.io.*;

    public class VsqBPPair implements Cloneable, Serializable
    {
        public int value;
        public long id;

        public VsqBPPair( int value_, long id_ )
        {
value = value_;
id = id_;
        }

        public Object clone(){
return new VsqBPPair( value, id );
        }
    }


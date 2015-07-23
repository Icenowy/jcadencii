/*
 * IndexItertorKind.cs
 * Copyright © 2010-2011 kbinani
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


    public class IndexIteratorKind
    {
        public static final int SINGER = 1 << 0;
        public static final int NOTE = 1 << 1;
        public static final int CRESCEND = 1 << 2;
        public static final int DECRESCEND = 1 << 3;
        public static final int DYNAFF = 1 << 4;

        private IndexIteratorKind()
        {
        }
    }


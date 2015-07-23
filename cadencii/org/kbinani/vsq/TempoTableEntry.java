/*
 * TempoTableEntry.cs
 * Copyright © 2008-2011 kbinani
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

    public class TempoTableEntry implements Comparable<TempoTableEntry>, Cloneable, Serializable
    {
        public int Clock;
        public int Tempo;
        public double Time;

        public String toString()
        {
return "{Clock=" + Clock + ", Tempo=" + Tempo + ", Time=" + Time + "}";
        }


        public Object clone()
        {
return new TempoTableEntry( Clock, Tempo, Time );
        }


        public TempoTableEntry( int clock, int _tempo, double _time )
        {
this.Clock = clock;
this.Tempo = _tempo;
this.Time = _time;
        }

        public TempoTableEntry()
        {
        }

        public int compareTo( TempoTableEntry entry )
        {
return this.Clock - entry.Clock;
        }


        public boolean Equals( TempoTableEntry entry )
        {
if ( this.Clock == entry.Clock ) {
    return true;
} else {
    return false;
}
        }
    }


/*
 * EditedZoneUnit.cs
 * Copyright © 2010-2011 kbinani
 *
 * This file is part of org.kbinani.cadencii.
 *
 * org.kbinani.cadencii is free software; you can redistribute it and/or
 * modify it under the terms of the GPLv3 License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.cadencii;

import java.io.*;


    public class EditedZoneUnit implements Cloneable, Comparable<EditedZoneUnit> {
        public int mStart;
        public int mEnd;

        private EditedZoneUnit(){
        }

        public EditedZoneUnit( int start, int end ){
this.mStart = start;
this.mEnd = end;
        }

        public int compareTo( EditedZoneUnit item ) {
return this.mStart - item.mStart;
        }


        public Object clone() {
return new EditedZoneUnit( mStart, mEnd );
        }

    }


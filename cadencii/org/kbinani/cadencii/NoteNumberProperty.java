/*
 * NoteNumberProperty.cs
 * Copyright © 2009-2011 kbinani
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

import org.kbinani.componentmodel.*;


    @TypeConverterAnnotation( NoteNumberPropertyConverter.class )
    public class NoteNumberProperty
    {
        public int noteNumber = 60;


        public int hashCode()
        {
return Integer.valueOf( noteNumber ).hashCode();
        }


        public boolean equals( Object obj )
        {
if ( obj instanceof NoteNumberProperty ) {
    if ( noteNumber == ((NoteNumberProperty)obj).noteNumber ) {
        return true;
    } else {
        return false;
    }
} else {
    return super.equals( obj );
}
        }
    }


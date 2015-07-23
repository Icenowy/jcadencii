/*
 * SelectedEventEntryPropertyDescriptor.cs
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

import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.componentmodel.*;


    public class SelectedEventEntryPropertyDescriptor extends PropertyDescriptor 
    {

        public String getDisplayName( String name )
        {
if ( name.equals( "Clock" ) ) {
    return _( "Clock" );
} else if ( name.equals( "Length" ) ) {
    return _( "Length" );
} else if ( name.equals( "Note" ) ) {
    return _( "Note#" );
} else if ( name.equals( "Velocity" ) ) {
    return _( "Velocity" );
} else if ( name.equals( "BendDepth" ) ) {
    return _( "Bend Depth" );
} else if ( name.equals( "BendLength" ) ) {
    return _( "Bend Length" );
} else if ( name.equals( "Decay" ) ) {
    return _( "Decay" );
} else if ( name.equals( "Accent" ) ) {
    return _( "Accent" );
} else if ( name.equals( "UpPortamento" ) ) {
    return _( "Up-Portamento" );
} else if ( name.equals( "DownPortamento" ) ) {
    return _( "Down-Portamento" );
} else if ( name.equals( "VibratoLength" ) ) {
    return _( "Vibrato Length" );
} else if ( name.equals( "PhoneticSymbol" ) ) {
    return _( "Phonetic Symbol" );
} else if ( name.equals( "Phrase" ) ) {
    return _( "Phrase" );
} else if ( name.equals( "PreUtterance" ) ) {
    return _( "Pre Utterance" );
} else if ( name.equals( "Overlap" ) ) {
    return _( "Overlap" );
} else if ( name.equals( "Moduration" ) ) {
    return _( "Moduration" );
} else if ( name.equals( "Vibrato" ) ) {
    return _( "Vibrato" );
} else if ( name.equals( "Attack" ) ) {
    return _( "Attack" );
} else if ( name.equals( "AttackDuration" ) ) {
    return _( "Attack Duration" );
} else if ( name.equals( "AttackDepth" ) ) {
    return _( "Attack Depth" );
} else if ( str.compare( name, "StartPoint" ) ) {
    return _( "StartPoint" );
} else if ( str.compare( name, "Intensity" ) ) {
    return _( "Intensity" );
}
return _( name );
        }

        private static String _( String id ) {
return Messaging.getMessage( id );
        }
    }


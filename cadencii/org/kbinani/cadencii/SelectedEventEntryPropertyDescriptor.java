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
    return gettext( "Clock" );
} else if ( name.equals( "Length" ) ) {
    return gettext( "Length" );
} else if ( name.equals( "Note" ) ) {
    return gettext( "Note#" );
} else if ( name.equals( "Velocity" ) ) {
    return gettext( "Velocity" );
} else if ( name.equals( "BendDepth" ) ) {
    return gettext( "Bend Depth" );
} else if ( name.equals( "BendLength" ) ) {
    return gettext( "Bend Length" );
} else if ( name.equals( "Decay" ) ) {
    return gettext( "Decay" );
} else if ( name.equals( "Accent" ) ) {
    return gettext( "Accent" );
} else if ( name.equals( "UpPortamento" ) ) {
    return gettext( "Up-Portamento" );
} else if ( name.equals( "DownPortamento" ) ) {
    return gettext( "Down-Portamento" );
} else if ( name.equals( "VibratoLength" ) ) {
    return gettext( "Vibrato Length" );
} else if ( name.equals( "PhoneticSymbol" ) ) {
    return gettext( "Phonetic Symbol" );
} else if ( name.equals( "Phrase" ) ) {
    return gettext( "Phrase" );
} else if ( name.equals( "PreUtterance" ) ) {
    return gettext( "Pre Utterance" );
} else if ( name.equals( "Overlap" ) ) {
    return gettext( "Overlap" );
} else if ( name.equals( "Moduration" ) ) {
    return gettext( "Moduration" );
} else if ( name.equals( "Vibrato" ) ) {
    return gettext( "Vibrato" );
} else if ( name.equals( "Attack" ) ) {
    return gettext( "Attack" );
} else if ( name.equals( "AttackDuration" ) ) {
    return gettext( "Attack Duration" );
} else if ( name.equals( "AttackDepth" ) ) {
    return gettext( "Attack Depth" );
} else if ( str.compare( name, "StartPoint" ) ) {
    return gettext( "StartPoint" );
} else if ( str.compare( name, "Intensity" ) ) {
    return gettext( "Intensity" );
}
return gettext( name );
        }

        private static String gettext( String id ) {
return Messaging.getMessage( id );
        }
    }


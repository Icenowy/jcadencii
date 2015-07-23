/*
 * str.cs
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

import java.text.DecimalFormat;
import java.util.Vector;


        public class str
        {
private str()
{
}

public static char charAt( String s, int index )
{
    return s.charAt( index ); 
}                                    

public static String toUpper( String s )
{
    if ( s == null ) {
        return "";
    }else{
        return s.toUpperCase();
    }
}

public static String toLower( String s )
{
    if( s == null ){
        return "";
    }else{
        return s.toLowerCase();
    }
}

public static boolean endsWith( String s, String search )
{
    return s.endsWith( search );
}

public static boolean startsWith( String s, String search )
{
    return s.startsWith( search );
}

public static String sub( String s, int start )
{
    return s.substring( start );
}

public static String sub( String s, int start, int length )
{
    return s.substring( start, start + length );
}

public static int find( String s, String search )
{
    return find( s, search, 0 );
}


public static int find( String s, String search, int index )
{
    return s.indexOf( search, index );
}

public static int split( String s, Vector<String> dst, Vector<String> splitter, boolean ignore_empty )
{
    int len = vec.size( splitter );
    vec.clear( dst );
    if ( len == 0 ) {
        vec.add( dst, s );
        return 1;
    }
    String remain = s;
    int index = find( remain, vec.get( splitter, 0 ), 0 );
    int i = 1;
    while ( index < 0 && i < len ) {
        index = find( remain, vec.get( splitter, i ), 0 );
        i++;
    }
    int added_count = 0;
    while ( index >= 0 ) {
        if ( !ignore_empty || (ignore_empty && index > 0) ) {
            vec.add( dst, sub( remain, 0, index ) );
            added_count++;
        }
        remain = sub( remain, index + len );
        index = find( remain, vec.get( splitter, 0 ) );
        i = 1;
        while ( index < 0 && i < len ) {
            index = find( remain, vec.get( splitter, i ) );
            i++;
        }
    }
    if ( !ignore_empty || (ignore_empty && length( remain ) > 0) ) {
        vec.add( dst, remain );
    }
    return added_count;
}

public static int split( String s, Vector<String> dst, String s1, boolean ignore_empty )
{
    Vector<String> splitter = new Vector<String>();
    vec.add( splitter, s1 );
    return split( s, dst, splitter, ignore_empty );
}

public static double tof( String s )
{
    return Double.parseDouble( s );
}

public static int toi( String s )
{
    return Integer.parseInt( s );
}

public static int length( String a )
{
    return a.length();
}

public static boolean compare( String a, String b )
{
    if ( a == null || b == null ) {
        return false;
    }
    return a.equals( b );
}

public static String format( long value, int digits )
{
    return format( value, digits, 10 );
}

public static String format( long value, int digits, int super_num )
{
    String format = "";
    for( int i = 0; i < digits; i++ ){
        format += "0";
    }
    DecimalFormat df = new DecimalFormat( format );
    return df.format( value );
}

public static String replace( String value, String before, String after )
{
    if( value == null ){
        return null;
    }else{
        return value.replace( before, after );
    }
}
        };


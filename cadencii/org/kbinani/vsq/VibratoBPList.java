/*
 * VibratoBPList.cs
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

import java.util.*;
import java.io.*;
import org.kbinani.*;

    public class VibratoBPList implements Cloneable, Serializable
    {
        private Vector<VibratoBPPair> m_list;

        public VibratoBPList()
        {
m_list = new Vector<VibratoBPPair>();
        }

        public VibratoBPList( String strNum, String strBPX, String strBPY )
        {
int num = 0;
try {
    num = str.toi( strNum );
} catch ( Exception ex ) {
    serr.println( "org.kbinani.vsq.VibratoBPList#.ctor; ex=" + ex );
    num = 0;
}
String[] bpx = PortUtil.splitString( strBPX, ',' );
String[] bpy = PortUtil.splitString( strBPY, ',' );
int actNum = Math.min( num, Math.min( bpx.length, bpy.length ) );
if ( actNum > 0 ) {
    float[] x = new float[actNum];
    int[] y = new int[actNum];
    for ( int i = 0; i < actNum; i++ ) {
        try {
            x[i] = (float)str.tof( bpx[i] );
            y[i] = str.toi( bpy[i] );
        } catch ( Exception ex ) {
            serr.println( "org.kbinani.vsq.IconParameter#.ctor; ex=" + ex );
        }
    }

    int len = Math.min( x.length, y.length );
    m_list = new Vector<VibratoBPPair>( len );
    for ( int i = 0; i < len; i++ ) {
        m_list.add( new VibratoBPPair( x[i], y[i] ) );
    }
    Collections.sort( m_list );
} else {
    m_list = new Vector<VibratoBPPair>();
}
        }

        public VibratoBPList( float[] x, int[] y )
throws NullPointerException
        {
if ( x == null ) {
    throw new NullPointerException( "x" );
}
if ( y == null ) {
    throw new NullPointerException( "y" );
}
int len = Math.min( x.length, y.length );
m_list = new Vector<VibratoBPPair>( len );
for ( int i = 0; i < len; i++ ) {
    m_list.add( new VibratoBPPair( x[i], y[i] ) );
}
Collections.sort( m_list );
        }

        /// <summary>
        /// このインスタンスと，指定したVibratoBPListのインスタンスが等しいかどうかを調べます
        /// </summary>
        /// <param name="item"></param>
        /// <returns></returns>
        public boolean equals( VibratoBPList item )
        {
if ( item == null ) {
    return false;
}
int size = this.m_list.size();
if ( size != item.m_list.size() ) {
    return false;
}
for ( int i = 0; i < size; i++ ) {
    VibratoBPPair p0 = this.m_list.get( i );
    VibratoBPPair p1 = item.m_list.get( i );
    if ( p0.X != p1.X ) {
        return false;
    }
    if ( p0.Y != p1.Y ) {
        return false;
    }
}
return true;
        }

        public int getValue( float x, int default_value )
        {
if ( m_list.size() <= 0 ) {
    return default_value;
}
int index = -1;
int size = m_list.size();
for ( int i = 0; i < size; i++ ) {
    if ( x < m_list.get( i ).X ) {
        break;
    }
    index = i;
}
if ( index == -1 ) {
    return default_value;
} else {
    return m_list.get( index ).Y;
}
        }

        public Object clone()
        {
VibratoBPList ret = new VibratoBPList();
for ( int i = 0; i < m_list.size(); i++ ) {
    ret.m_list.add( new VibratoBPPair( m_list.get( i ).X, m_list.get( i ).Y ) );
}
return ret;
        }


        public int getCount()
        {
return m_list.size();
        }

        public VibratoBPPair getElement( int index )
        {
return m_list.get( index );
        }

        public void setElement( int index, VibratoBPPair value )
        {
m_list.set( index, value );
        }


        public String getData()
        {
String ret = "";
for ( int i = 0; i < m_list.size(); i++ ) {
    ret += (i == 0 ? "" : ",") + m_list.get( i ).X + "=" + m_list.get( i ).Y;
}
return ret;
        }

        public void setData( String value )
        {
m_list.clear();
String[] spl = PortUtil.splitString( value, ',' );
for ( int i = 0; i < spl.length; i++ ) {
    String[] spl2 = PortUtil.splitString( spl[i], '=' );
    if ( spl2.length < 2 ) {
        continue;
    }
    m_list.add( new VibratoBPPair( (float)str.tof( spl2[0] ), str.toi( spl2[1] ) ) );
}
        }
    }


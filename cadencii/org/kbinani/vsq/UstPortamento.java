/*
 * UstPortamento.cs
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
import org.kbinani.xml.*;


    public class UstPortamento implements Cloneable, Serializable
    {
        @XmlGenericType( UstPortamentoPoint.class )
        public Vector<UstPortamentoPoint> Points = new Vector<UstPortamentoPoint>();
        public int Start;
        /// <summary>
        /// PBSの末尾のセミコロンの後ろについている整数
        /// </summary>
        private int mUnknownInt;
        /// <summary>
        /// mUnknownIntが設定されているかどうか
        /// </summary>
        private boolean mIsUnknownIntSpecified = false;

        /// <summary>
        /// このクラスの指定した名前のプロパティをXMLシリアライズする際に使用する
        /// 要素名を取得します．
        /// </summary>
        /// <param name="name"></param>
        /// <returns></returns>
        public static String getXmlElementName( String name )
        {
return name;
        }

        public void print( ITextWriter sw )
throws IOException
        {
String pbw = "";
String pby = "";
String pbm = "";
int count = Points.size();
for ( int i = 0; i < count; i++ ) {
    String comma = (i == 0 ? "" : ",");
    pbw += comma + Points.get( i ).Step;
    pby += Points.get( i ).Value + ",";
    String type = "";
    UstPortamentoType ut = Points.get( i ).Type;
    if ( ut == UstPortamentoType.S ) {
        type = "";
    } else if ( ut == UstPortamentoType.Linear ) {
        type = "s";
    } else if ( ut == UstPortamentoType.R ) {
        type = "r";
    } else if ( ut == UstPortamentoType.J ) {
        type = "j";
    }
    pbm += comma + type;
}
sw.write( "PBW=" + pbw );
sw.newLine();
sw.write( "PBS=" + Start + (mIsUnknownIntSpecified ? (";" + mUnknownInt) : "") );
sw.newLine();
if ( vec.size( Points ) >= 2 ) {
    sw.write( "PBY=" + pby );
    sw.newLine();
    sw.write( "PBM=" + pbm );
    sw.newLine();
}
        }

        public Object clone()
        {
UstPortamento ret = new UstPortamento();
int count = Points.size();
for ( int i = 0; i < count; i++ ) {
    ret.Points.add( Points.get( i ) );
}
ret.Start = Start;
ret.mIsUnknownIntSpecified = mIsUnknownIntSpecified;
ret.mUnknownInt = mUnknownInt;
return ret;
        }


        /*
        PBW=50,50,46,48,56,50,50,50,50
        PBS=-87
        PBY=-15.9,-20,-31.5,-26.6
        PBM=,s,r,j,s,s,s,s,s
        */
        public void parseLine( String line )
        {
line = line.toLowerCase();
String[] spl = PortUtil.splitString( line, '=' );
if ( spl.length == 0 ) {
    return;
}
String[] values = PortUtil.splitString( spl[1], ',' );
if ( line.startsWith( "pbs=" ) ) {
    String v = values[0];
    int indx = str.find( values[0], ";" );
    if ( indx >= 0 ) {
        v = str.sub( values[0], 0, indx );
        if ( str.length( values[0] ) > indx + 1 ) {
            String unknown = str.sub( values[0], indx + 1 );
            mIsUnknownIntSpecified = true;
            mUnknownInt = str.toi( unknown );
        }
    }
    Start = str.toi( v );
} else if ( line.startsWith( "pbw=" ) ) {
    for ( int i = 0; i < values.length; i++ ) {
        if ( i >= Points.size() ) {
            Points.add( new UstPortamentoPoint() );
        }
        UstPortamentoPoint up = Points.get( i );
        up.Step = str.toi( values[i] );
        Points.set( i, up );
    }
} else if ( line.startsWith( "pby=" ) ) {
    for ( int i = 0; i < values.length; i++ ) {
        if ( str.length( values[i] ) <= 0 ) {
            continue;
        }
        if ( i >= Points.size() ) {
            Points.add( new UstPortamentoPoint() );
        }
        UstPortamentoPoint up = Points.get( i );
        up.Value = (float)str.tof( values[i] );
        Points.set( i, up );
    }
} else if ( line.startsWith( "pbm=" ) ) {
    for ( int i = 0; i < values.length; i++ ) {
        if ( i >= Points.size() ) {
            Points.add( new UstPortamentoPoint() );
        }
        UstPortamentoPoint up = Points.get( i );
        String search = values[i].toLowerCase();
        if ( str.compare( search, "s" ) ) {
            up.Type = UstPortamentoType.Linear;
        } else if ( str.compare( search, "r" ) ) {
            up.Type = UstPortamentoType.R;
        } else if ( str.compare( search, "j" ) ) {
            up.Type = UstPortamentoType.J;
        } else {
            up.Type = UstPortamentoType.S;
        }
        Points.set( i, up );
    }
} else if ( line.startsWith( "pbs=" ) ) {

}
        }
    }


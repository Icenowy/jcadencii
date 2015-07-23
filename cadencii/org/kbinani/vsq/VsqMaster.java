/*
* VsqMaster.cs
* Copyright (C) 2008-2011 kbinani
*
* This file is part of org.kbinani.vsq.
*
* Boare.Lib.Vsq is free software; you can redistribute it and/or
* modify it under the terms of the BSD License.
*
* Boare.Lib.Vsq is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/
package org.kbinani.vsq;

import java.io.*;
import org.kbinani.*;

    /// <summary>
    /// vsqファイルのメタテキストの[Master]に記録される内容を取り扱う
    /// </summary>
    public class VsqMaster implements Cloneable, Serializable
    {
        public int PreMeasure;

        public Object clone()
        {
VsqMaster res = new VsqMaster( PreMeasure );
return res;
        }


        public VsqMaster(){
this( 1 );
        }

        /// <summary>
        /// プリメジャー値を指定したコンストラクタ
        /// </summary>
        /// <param name="pre_measure"></param>
        public VsqMaster( int pre_measure )
        {
this.PreMeasure = pre_measure;
        }

        /// <summary>
        /// テキストファイルからのコンストラクタ
        /// </summary>
        /// <param name="sr">読み込み元</param>
        /// <param name="last_line">最後に読み込んだ行が返されます</param>
        public VsqMaster( TextStream sr, ByRef<String> last_line )
        {
PreMeasure = 0;
String[] spl;
last_line.value = sr.readLine();
while ( !last_line.value.startsWith( "[" ) ) {
    spl = PortUtil.splitString( last_line.value, new char[] { '=' } );
    if ( spl[0].equals( "PreMeasure" ) ) {
        this.PreMeasure = str.toi( spl[1] );
    }
    if ( !sr.ready() ) {
        break;
    }
    last_line.value = sr.readLine().toString();
}
        }

        /// <summary>
        /// インスタンスの内容をテキストファイルに出力します
        /// </summary>
        /// <param name="sw">出力先</param>
        public void write( ITextWriter sw )
throws java.io.IOException
        {
sw.writeLine( "[Master]" );
sw.writeLine( "PreMeasure=" + PreMeasure );
        }
    }


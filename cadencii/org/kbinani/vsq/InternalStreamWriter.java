/*
 * InternalStreamWriter.cs
 * Copyright © 2011 kbinani
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

    /// <summary>
    /// 改行コードに0x0d 0x0aを用いるテキストライター
    /// </summary>
    class InternalStreamWriter implements ITextWriter
    {
        private String mNL = "\n";
        private BufferedWriter mStream;

        public InternalStreamWriter( String path, String encoding )
throws java.io.FileNotFoundException,
       java.io.UnsupportedEncodingException
        {
mNL = new String( new char[]{ (char)0x0d, (char)0x0a } );
mStream = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( path ), encoding ) );
        }

        public void write( String s )
throws java.io.IOException
        {
mStream.write( s );
        }
        
        public void writeLine( String s )
throws java.io.IOException
        {
write( s );
newLine();
        }
        
        public void newLine()
throws java.io.IOException
        {
write( mNL );
        }
        
        public void close()
throws java.io.IOException
        {
mStream.close();
        }
    }


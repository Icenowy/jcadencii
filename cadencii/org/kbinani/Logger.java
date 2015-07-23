/*
 * Logger.cs
 * Copyright © 2010-2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.kbinani;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileWriter;


    public class Logger {
        private static BufferedWriter log = null;
        private static String path = "";
        private static boolean is_enabled = false;

        private Logger() {
        }

        public static boolean isEnabled() {
return is_enabled;
        }

        public static void setEnabled( boolean value ) {
is_enabled = value;
        }

        public static void write( String s ) {
if ( !is_enabled ) {
    return;
}

if ( log == null ) {
    if ( path == null || (path != null && path.equals( "" )) ) {
        path = PortUtil.createTempFile();
        //path = "C:\\log.txt";
    }
    try {
        log = new BufferedWriter( new FileWriter( path ) );
    } catch ( Exception ex ) {
        serr.println( "Logger#write; ex=" + ex );
    }
}

if ( log == null ) {
    return;
}
try {
    log.write( s );
    log.flush();
} catch ( Exception ex ) {
    serr.println( "Logger#write; ex=" + ex );
}
        }

        public static String getPath() {
return path;
        }

        public static void setPath( String file ) {
boolean append = false;
if ( log != null && !path.equals( file ) ) {
    try {
        log.close();
    } catch ( Exception ex ) {
        serr.println( "Logger#setPath; ex=" + ex );
    }
    log = null;
    if( fsys.isFileExists( file ) ){
        try{
            PortUtil.deleteFile( file );
        }catch( Exception ex ){
            serr.println( "Logger#setPath; ex=" + ex );
        }
    }
    try {
        PortUtil.moveFile( path, file );
    } catch ( Exception ex ) {
        serr.println( "Logger#setPath; ex=" + ex );
    }
    append = true;
}
path = file;

if ( is_enabled ) {
    try {
    log = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( path, append ), "UTF-8" ) );
    } catch ( Exception ex ) {
        serr.println( "Logger#setPath; ex=" + ex );
    }
}
        }
    }


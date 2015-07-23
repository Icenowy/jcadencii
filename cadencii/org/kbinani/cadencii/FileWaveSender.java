/*
 * FileWaveReceiver.cs
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

import java.awt.*;
import java.util.*;
import org.kbinani.*;
import org.kbinani.media.*;

    public class FileWaveSender extends WaveUnit implements WaveSender
    {
        private static final int BUFLEN = 1024;
        private double[] mBufferL = new double[BUFLEN];
        private double[] mBufferR = new double[BUFLEN];
        private WaveRateConverter mConverter = null;
        private long mPosition = 0;
        private int mVersion = 0;
        private WaveReader mReader = null;
        private Object mSyncRoot = new Object();

        public FileWaveSender( WaveReader reader )
        {
mReader = reader;
        }

        public int getVersion()
        {
return mVersion;
        }

        public void setConfig( String parameter )
        {
// do nothing
        }

        public void setSender( WaveSender s )
        {
// do nothing
        }

        public void pull( double[] l, double[] r, int length )
        {
synchronized ( mSyncRoot ) {
    if ( mConverter == null ) {
        int rate = mRoot.getSampleRate();
        mConverter = new WaveRateConverter( mReader, rate );
    }
    try {
        mConverter.read( mPosition, length, l, r );
        mPosition += length;
    } catch ( Exception ex ) {
        serr.println( "FileWaveSender#pull; ex=" + ex );
        Logger.write( FileWaveSender.class + ".pull; ex=" + ex + "\n" );
    }
}
        }

        public void end()
        {
synchronized ( mSyncRoot ) {
    try {
        if( mConverter != null ){
            mConverter.close();
        }else{
            if( mReader != null ){
                mReader.close();
            }
        }
    } catch ( Exception ex ) {
        sout.println( "FileWaveSender#end; ex=" + ex );
        Logger.write( FileWaveSender.class + ".end; ex=" + ex + "\n" );
    }
}
        }
    }


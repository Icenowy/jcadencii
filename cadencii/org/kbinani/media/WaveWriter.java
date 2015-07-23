/*
 * WaveWriter.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.media.
 *
 * org.kbinani.media is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.media is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.media;

import java.io.*;
import org.kbinani.*;

    public class WaveWriter implements IWaveReceiver {
        private int m_channel = 1;
        private int m_bit_per_sample;
        private int m_sample_rate;
        private long m_total_samples = 0;
        private RandomAccessFile m_stream = null;
        private String m_path = "";
        /// <summary>
        /// dataチャンクの開始位置。第1番目のデータが、このアドレスに書き込まれることになる。
        /// </summary>
        private long m_pos_data_chunk;

        public WaveWriter( String path ) 

throws IOException
        {
this ( path, 2, 16, 44100 );
        }

        public WaveWriter( String path, int channel, int bit_per_sample, int sample_rate ) 
throws IOException
        {
m_path = path;
m_stream = new RandomAccessFile( m_path, "rw" );
m_channel = channel;
m_bit_per_sample = bit_per_sample;
m_sample_rate = sample_rate;
writeHeader();
m_total_samples = (m_stream.length() - m_pos_data_chunk) / m_channel / (m_bit_per_sample / 8);
        }

        /// <summary>
        /// 第posサンプルからlengthサンプル分、指定した波形データで置き換えます
        /// </summary>
        /// <param name="pos"></param>
        /// <param name="length"></param>
        /// <param name="L"></param>
        /// <param name="R"></param>
        public void replace( long pos, int length, double[] L, double[] R ) 
throws IOException
        {
long lastPos = m_stream.getFilePointer();
long posFile = pos * m_channel * m_bit_per_sample / 8 + m_pos_data_chunk;
long streamLen = m_stream.length();
if ( streamLen < posFile ) {
    // ファイルの長さが足りていない場合、とりあえず0で埋める。
    m_stream.seek( streamLen - 1 );
    long remain = posFile - streamLen;
    int buflen = 1024;
    byte[] data = new byte[buflen];
    while ( remain > 0 ) {
        int delta = remain > buflen ? buflen : (int)remain;
        m_stream.write( data, 0, delta );
        remain -= delta;
    }
    m_total_samples = pos;
}
m_stream.seek( posFile );

// 書き込み
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < length; i++ ) {
            m_stream.writeByte( (int)((L[i] + R[i] + 2.0) * 63.75) );
        }
    } else {
        for ( int i = 0; i < length; i++ ) {
            m_stream.writeByte( (int)((L[i] + 1.0) * 127.5) );
            m_stream.writeByte( (int)((R[i] + 1.0) * 127.5) );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < length; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)((L[i] + R[i]) * 16384.0) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < length; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768.0) );
            writeByteArray( m_stream, buf, 2 );
            buf = PortUtil.getbytes_int16_le( (short)(R[i] * 32768.0) );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples = (m_total_samples < pos + length) ? (pos + length) : (m_total_samples);

// 最後にファイルポインタを戻す
m_stream.seek( lastPos );
        }


        /// <summary>
        /// Writes header of WAVE file
        /// </summary>
        private void writeHeader()
throws IOException
        {
// RIFF
m_stream.writeByte( 0x52 ); // loc=0x00
m_stream.writeByte( 0x49 );
m_stream.writeByte( 0x46 );
m_stream.writeByte( 0x46 );

// ファイルサイズ - 8最後に記入
m_stream.writeByte( 0x00 ); // loc=0x04
m_stream.writeByte( 0x00 );
m_stream.writeByte( 0x00 );
m_stream.writeByte( 0x00 );

// WAVE
m_stream.writeByte( 0x57 ); // loc=0x08
m_stream.writeByte( 0x41 );
m_stream.writeByte( 0x56 );
m_stream.writeByte( 0x45 );

// fmt 
m_stream.writeByte( 0x66 ); // loc=0x0c
m_stream.writeByte( 0x6d );
m_stream.writeByte( 0x74 );
m_stream.writeByte( 0x20 );

// fmt チャンクのサイズ
m_stream.writeByte( 0x12 ); // loc=0x10
m_stream.writeByte( 0x00 );
m_stream.writeByte( 0x00 );
m_stream.writeByte( 0x00 );

// format ID
m_stream.writeByte( 0x01 ); // loc=0x14
m_stream.writeByte( 0x00 );

// チャンネル数
if ( m_channel == 1 ) {
    m_stream.writeByte( 0x01 ); // loc=0x16
    m_stream.writeByte( 0x00 );
} else {
    m_stream.writeByte( 0x02 ); //loc=0x16
    m_stream.writeByte( 0x00 );
}

// サンプリングレート
byte[] buf = PortUtil.getbytes_uint32_le( m_sample_rate );
writeByteArray( m_stream, buf, 4 ); // 0x18

// データ速度
int block_size = (int)(m_bit_per_sample / 8 * (int)m_channel);
int data_rate = m_sample_rate * block_size;
buf = PortUtil.getbytes_uint32_le( data_rate );
writeByteArray( m_stream, buf, 4 );//loc=0x1c

// ブロックサイズ
buf = PortUtil.getbytes_uint16_le( block_size );
writeByteArray( m_stream, buf, 2 ); //0x20

// サンプルあたりのビット数
buf = PortUtil.getbytes_uint16_le( m_bit_per_sample );
writeByteArray( m_stream, buf, 2 ); //loc=0x22

// 拡張部分
m_stream.writeByte( 0x00 ); //loc=0x24
m_stream.writeByte( 0x00 );

// data
m_stream.writeByte( 0x64 ); //loc=0x26
m_stream.writeByte( 0x61 );
m_stream.writeByte( 0x74 );
m_stream.writeByte( 0x61 );

// size of data chunk
long size = block_size * m_total_samples;
buf = PortUtil.getbytes_uint32_le( size );
writeByteArray( m_stream, buf, 4 );
m_pos_data_chunk = m_stream.getFilePointer();
        }

        public void close() {
if ( m_stream == null ) {
    return;
}
try {
    // 最後にWAVEチャンクのサイズ
    int position = (int)m_stream.getFilePointer();
    m_stream.seek( 4 );
    byte[] buf = PortUtil.getbytes_uint32_le( position - 8 );
    writeByteArray( m_stream, buf, 4 );

    // size of data chunk
    int block_size = (int)(m_bit_per_sample / 8 * (int)m_channel);
    long size = block_size * m_total_samples;
    m_stream.seek( 42 );
    buf = PortUtil.getbytes_uint32_le( size );
    writeByteArray( m_stream, buf, 4 );

    m_stream.close();
} catch ( Exception ex ) {
    serr.println( "WaveWriter#close; ex=" + ex );
}
        }

        public int getSampleRate(){
return m_sample_rate;
        }

        public void append( float[] L )
throws IOException
        {
int total = L.length;
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (int)((L[i] + 1.0f) * 127.5f) );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            int b = (int)((L[i] + 1.0f) * 127.5f);
            m_stream.writeByte( b );
            m_stream.writeByte( b );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768f) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768f) );
            writeByteArray( m_stream, buf, 2 );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples += (int)total;
        }

        public void append( double[] L ) 
throws IOException
        {
int total = L.length;
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (int)((L[i] + 1.0) * 127.5) );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            int b = (int)((L[i] + 1.0) * 127.5);
            m_stream.writeByte( b );
            m_stream.writeByte( b );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768.0) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768.0) );
            writeByteArray( m_stream, buf, 2 );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples += (int)total;
        }

        public void append( float[] L, float[] R ) 
throws IOException
        {
int total = Math.min( L.length, R.length );
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (byte)((L[i] + R[i] + 2.0f) * 63.75f) );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (byte)((L[i] + 1.0f) * 127.5f) );
            m_stream.writeByte( (byte)((R[i] + 1.0f) * 127.5f) );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)((L[i] + R[i]) * 16384f) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768f) );
            writeByteArray( m_stream, buf, 2 );
            buf = PortUtil.getbytes_int16_le( (short)(R[i] * 32768f) );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples += (int)total;
        }

        public void append( double[] L, double[] R ) 
throws IOException
        {
int length = Math.min( L.length, R.length );
append( L, R, length );
        }

        public void append( double[] L, double[] R, int length ) {
try {
    if ( m_bit_per_sample == 8 ) {
        if ( m_channel == 1 ) {
            for ( int i = 0; i < length; i++ ) {
                m_stream.writeByte( (int)((L[i] + R[i] + 2.0) * 63.75) );
            }
        } else {
            for ( int i = 0; i < length; i++ ) {
                m_stream.writeByte( (int)((L[i] + 1.0) * 127.5) );
                m_stream.writeByte( (int)((R[i] + 1.0) * 127.5) );
            }
        }
    } else {
        byte[] buf;
        if ( m_channel == 1 ) {
            for ( int i = 0; i < length; i++ ) {
                buf = PortUtil.getbytes_int16_le( (short)((L[i] + R[i]) * 16384.0) );
                writeByteArray( m_stream, buf, 2 );
            }
        } else {
            for ( int i = 0; i < length; i++ ) {
                buf = PortUtil.getbytes_int16_le( (short)(L[i] * 32768.0) );
                writeByteArray( m_stream, buf, 2 );
                buf = PortUtil.getbytes_int16_le( (short)(R[i] * 32768.0) );
                writeByteArray( m_stream, buf, 2 );
            }
        }
    }
    m_total_samples += (int)length;
} catch ( Exception ex ) {
    serr.println( "WaveWriter#append(double[],double[],int); ex=" + ex );
}
        }

        public void append( byte[] L, byte[] R ) 
throws IOException
        {
int total = Math.min( L.length, R.length );
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( 0xff & ((L[i] + R[i]) / 2) );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( 0xff & L[i] );
            m_stream.writeByte( 0xff & R[i] );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)((L[i] + R[i]) * 128.5f - 32768f) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)(L[i] * 257f - 32768f) );
            writeByteArray( m_stream, buf, 2 );
            buf = PortUtil.getbytes_int16_le( (short)(R[i] * 257f - 32768f) );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples += (int)total;
        }

        public void append( short[] L, short[] R ) 
throws IOException
        {
int total = Math.min( L.length, R.length );
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (int)(((L[i] + R[i]) / 2f + 32768f) / 255f) );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            m_stream.writeByte( (int)((L[i] + 32768f) / 255f) );
            m_stream.writeByte( (int)((R[i] + 32768f) / 255f) );
        }
    }
} else {
    byte[] buf;
    if ( m_channel == 1 ) {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( (short)((L[i] + R[i]) / 2) );
            writeByteArray( m_stream, buf, 2 );
        }
    } else {
        for ( int i = 0; i < total; i++ ) {
            buf = PortUtil.getbytes_int16_le( L[i] );
            writeByteArray( m_stream, buf, 2 );
            buf = PortUtil.getbytes_int16_le( R[i] );
            writeByteArray( m_stream, buf, 2 );
        }
    }
}
m_total_samples += (int)total;
        }

        private static void writeByteArray( RandomAccessFile fs, byte[] dat, int limit )
throws IOException
        {
fs.write( dat, 0, (dat.length > limit) ? limit : dat.length );
if ( dat.length < limit ) {
    for ( int i = 0; i < limit - dat.length; i++ ) {
        fs.writeByte( 0x00 );
    }
}
        }
    }


/*
 * Wave.cs
 * Copyright © 2008-2011 kbinani
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


    public class Wave
 {
        public enum Channel {
Right,
Left,
        }

        public enum WaveChannel {
Monoral,
Stereo
        }

        private WaveChannel m_channel;
        private int m_bit_per_sample;
        private long m_sample_rate;
        private long m_total_samples;
        private byte[] L8;
        private byte[] R8;
        private short[] L16;
        private short[] R16;










        public void getNormalizedWave( int start_index, double[] conv ) {
int count = conv.length;
int cp_start = start_index;
int cp_end = start_index + count;
if ( start_index < 0 ) {
    for ( int i = 0; i < cp_start - start_index; i++ ) {
        conv[i] = 0.0;
    }
    cp_start = 0;
}
if ( m_total_samples <= cp_end ) {
    for ( int i = cp_end - start_index; i < count; i++ ) {
        conv[i] = 0.0;
    }
    cp_end = (int)m_total_samples - 1;
}
if ( m_channel == WaveChannel.Monoral ) {
    if ( m_bit_per_sample == 8 ) {
        for ( int i = cp_start; i < cp_end; i++ ) {
            conv[i - start_index] = (L8[i] - 64.0) / 64.0;
        }
    } else {
        for ( int i = cp_start; i < cp_end; i++ ) {
            conv[i - start_index] = L16[i] / 32768.0;
        }
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        for ( int i = cp_start; i < cp_end; i++ ) {
            conv[i - start_index] = ((L8[i] + R8[i]) * 0.5 - 64.0) / 64.0;
        }
    } else {
        for ( int i = cp_start; i < cp_end; i++ ) {
            conv[i - start_index] = (L16[i] + R16[i]) * 0.5 / 32768.0;
        }
    }
}
        }

        public double[] getNormalizedWave() {
return getNormalizedWave( 0, m_total_samples );
        }

        /// <summary>
        /// -1から1までに規格化された波形を取得します
        /// </summary>
        /// <returns></returns>
        public double[] getNormalizedWave( int start_index, long count ) {
double[] conv = new double[(int)count];
getNormalizedWave( start_index, conv );
return conv;
        }

        private void set( int index, Channel channel, int value ) {
if ( m_channel == WaveChannel.Monoral || channel == Channel.Left ) {
    if ( m_bit_per_sample == 8 ) {
        L8[index] = (byte)value;
    } else {
        L16[index] = (short)value;
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        R8[index] = (byte)value;
    } else {
        R16[index] = (short)value;
    }
}
        }

        private int get( int index, Channel channel ) {
if ( m_channel == WaveChannel.Monoral || channel == Channel.Left ) {
    if ( m_bit_per_sample == 8 ) {
        return L8[index];
    } else {
        return L16[index];
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        return R8[index];
    } else {
        return R16[index];
    }
}
        }

        private void set( int index, int value ) {
set( index, Channel.Left, value );
if ( m_channel == WaveChannel.Stereo ) {
    set( index, Channel.Right, value );
}
        }

        public double getDouble( int index ) {
if ( m_channel == WaveChannel.Monoral ) {
    if ( m_bit_per_sample == 8 ) {
        return (L8[index] - 64.0) / 64.0;
    } else {
        return L16[index] / 32768.0;
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        return ((L8[index] + R8[index]) * 0.5 - 64.0) / 64.0;
    } else {
        return (L16[index] + R16[index]) * 0.5 / 32768.0;
    }
}
        }

        public long getSampleRate() {
return m_sample_rate;
        }

        private void setTotalSamples( long value ) {
m_total_samples = value;
if ( m_channel == WaveChannel.Monoral ) {
    if ( m_bit_per_sample == 8 ) {
        if ( L8 == null ) {
            L8 = new byte[(int)m_total_samples];
        } else {
            L8 = resizeArray( L8, (int)m_total_samples );
        }
    } else {
        if ( L16 == null ) {
            L16 = new short[(int)m_total_samples];
        } else {
            L16 = resizeArray( L16, (int)m_total_samples );
        }
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        if ( L8 == null ) {
            L8 = new byte[(int)m_total_samples];
            R8 = new byte[(int)m_total_samples];
        } else {
            L8 = resizeArray( L8, (int)m_total_samples );
            R8 = resizeArray( R8, (int)m_total_samples );
        }
    } else {
        if ( L16 == null ) {
            L16 = new short[(int)m_total_samples];
            R16 = new short[(int)m_total_samples];
        } else {
            L16 = resizeArray( L16, (int)m_total_samples );
            R16 = resizeArray( R16, (int)m_total_samples );
        }
    }
}
        }

        public long getTotalSamples() {
return m_total_samples;
        }

        public void replace( Wave srcWave, int srcStart, int destStart, int count ) {
if ( m_channel != srcWave.m_channel || m_bit_per_sample != srcWave.m_bit_per_sample ) {
    return;
}
if ( m_channel == WaveChannel.Monoral ) {
    if ( m_bit_per_sample == 8 ) {
        if ( L8 == null || srcWave.L8 == null ) {
            return;
        }
    } else {
        if ( L16 == null || srcWave.L16 == null ) {
            return;
        }
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        if ( L8 == null || R8 == null || srcWave.L8 == null || srcWave.R8 == null ) {
            return;
        }
    } else {
        if ( L16 == null || R16 == null || srcWave.L16 == null || srcWave.R16 == null ) {
            return;
        }
    }
}

count = (int)((count > srcWave.getTotalSamples() - srcStart) ? srcWave.getTotalSamples() - srcStart : count);
long new_last_index = destStart + count;
if ( m_total_samples < new_last_index ) {
    if ( m_channel == WaveChannel.Monoral ) {
        if ( m_bit_per_sample == 8 ) {
            L8 = resizeArray( L8, (int)new_last_index );
        } else {
            L16 = resizeArray( L16, (int)new_last_index );
        }
    } else {
        if ( m_bit_per_sample == 8 ) {
            L8 = resizeArray( L8, (int)new_last_index );
            R8 = resizeArray( R8, (int)new_last_index );
        } else {
            L16 = resizeArray( L16, (int)new_last_index );
            R16 = resizeArray( R16, (int)new_last_index );
        }
    }
    m_total_samples = new_last_index;
}
if ( m_channel == WaveChannel.Monoral ) {
    if ( m_bit_per_sample == 8 ) {
        copyArray( srcWave.L8, srcStart, L8, destStart, count );
    } else {
        copyArray( srcWave.L16, srcStart, L16, destStart, count );
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        copyArray( srcWave.L8, srcStart, L8, destStart, count );
        copyArray( srcWave.R8, srcStart, R8, destStart, count );
    } else {
        copyArray( srcWave.L16, srcStart, L16, destStart, count );
        copyArray( srcWave.R16, srcStart, R16, destStart, count );
    }
}
        }


        public void replace( byte[] data, int start_index ) {
if ( m_channel != WaveChannel.Monoral || m_bit_per_sample != 8 || L8 == null ) {
    return;
}
long new_last_index = (long)(start_index + data.length);
if ( m_total_samples < new_last_index ) {
    L8 = resizeArray( L8, (int)new_last_index );
    m_total_samples = new_last_index;
}
for ( int i = 0; i < data.length; i++ ) {
    L8[i + start_index] = data[i];
}
        }


        public void replace( short[] data, int start_index ) {
if ( m_channel != WaveChannel.Monoral || m_bit_per_sample != 16 || L16 == null ) {
    return;
}
long new_last_index = (long)(start_index + data.length);
if ( m_total_samples < new_last_index ) {
    L16 = resizeArray( L16, (int)new_last_index );
    m_total_samples = new_last_index;
}
for ( int i = 0; i < data.length; i++ ) {
    L16[i + start_index] = data[i];
}
        }


        public void replace( byte[] left, byte[] right, int start_index ) {
if ( m_channel != WaveChannel.Stereo || m_bit_per_sample != 8 || L8 == null || R8 == null ) {
    return;
}
long new_last_index = (long)(start_index + left.length);
if ( m_total_samples < new_last_index ) {
    L8 = resizeArray( L8, (int)new_last_index );
    R8 = resizeArray( R8, (int)new_last_index );
    m_total_samples = new_last_index;
}
for ( int i = 0; i < left.length; i++ ) {
    L8[i + start_index] = left[i];
    R8[i + start_index] = right[i];
}
        }


        public void replace( short[] left, short[] right, int start_index ) {
if ( m_channel != WaveChannel.Stereo || m_bit_per_sample != 16 || L16 == null || R16 == null ) {
    return;
}
long new_last_index = (long)(start_index + left.length);
if ( m_total_samples < new_last_index ) {
    L16 = resizeArray( L16, (int)new_last_index );
    R16 = resizeArray( R16, (int)new_last_index );
    m_total_samples = new_last_index;
}
for ( int i = 0; i < left.length; i++ ) {
    L16[i + start_index] = left[i];
    R16[i + start_index] = right[i];
}
        }

        public void replace( float[] left, float[] right, int start_index ) {
long new_last_index = (long)(start_index + left.length);
if ( m_total_samples < new_last_index ) {
    if ( m_channel == WaveChannel.Monoral ) {
        if ( m_bit_per_sample == 8 ) {
            if ( L8 == null ) {
                return;
            }
            L8 = resizeArray( L8, (int)new_last_index );
        } else {
            if ( L16 == null ) {
                return;
            }
            L16 = resizeArray( L16, (int)new_last_index );
        }
    } else {
        if ( m_bit_per_sample == 8 ) {
            if ( L8 == null || R8 == null ) {
                return;
            }
            L8 = resizeArray( L8, (int)new_last_index );
            R8 = resizeArray( R8, (int)new_last_index );
        } else {
            if ( L16 == null || R16 == null ) {
                return;
            }
            L16 = resizeArray( L16, (int)new_last_index );
            R16 = resizeArray( R16, (int)new_last_index );
        }
    }
    m_total_samples = new_last_index;
}
if ( m_channel == WaveChannel.Monoral ) {
    float[] mono = new float[left.length];
    for ( int i = 0; i < left.length; i++ ) {
        mono[i] = (left[i] + right[i]) / 2f;
    }
    if ( m_bit_per_sample == 8 ) {
        for ( int i = 0; i < mono.length; i++ ) {
            L8[i + start_index] = (byte)((mono[i] + 1.0f) / 2f * 255f);
        }
    } else {
        for ( int i = 0; i < mono.length; i++ ) {
            L16[i + start_index] = (short)(mono[i] * 32768f);
        }
    }
} else {
    if ( m_bit_per_sample == 8 ) {
        for ( int i = 0; i < left.length; i++ ) {
            L8[i + start_index] = (byte)((left[i] + 1.0f) / 2f * 255f);
            R8[i + start_index] = (byte)((right[i] + 1.0f) / 2f * 255f);
        }
    } else {
        for ( int i = 0; i < left.length; i++ ) {
            L16[i + start_index] = (short)(left[i] * 32768f);
            R16[i + start_index] = (short)(right[i] * 32768f);
        }
    }
}
        }

        public void printToText( String path ) {
BufferedWriter sw = null;
try {
    sw = new BufferedWriter( new FileWriter( path ) );
    if ( m_channel == WaveChannel.Monoral ) {
        if ( m_bit_per_sample == 8 ) {
            for ( int i = 0; i < m_total_samples; i++ ) {
                sw.write( L8[i] + "" );
                sw.newLine();
            }
        } else {
            for ( int i = 0; i < m_total_samples; i++ ) {
                sw.write( L16[i] + "" );
                sw.newLine();
            }
        }
    } else {
        if ( m_bit_per_sample == 8 ) {
            for ( int i = 0; i < m_total_samples; i++ ) {
                sw.write( L8[i] + "\t" + R8[i] );
                sw.newLine();
            }
        } else {
            for ( int i = 0; i < m_total_samples; i++ ) {
                sw.write( L16[i] + "\t" + R16[i] );
                sw.newLine();
            }
        }
    }
} catch ( Exception ex ) {
} finally {
    if ( sw != null ) {
        try {
            sw.close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }


        public void dispose() {
L8 = null;
R8 = null;
L16 = null;
R16 = null;
System.gc();
        }

        /// <summary>
        /// サンプルあたりのビット数を8に変更する
        /// </summary>
        public void convertTo8Bit() {
if ( m_bit_per_sample == 8 ) {
    return;
}

// 先ず左チャンネル
L8 = new byte[L16.length];
for ( int i = 0; i < L16.length; i++ ) {
    double new_val = (L16[i] + 32768.0) / 65535.0 * 255.0;
    L8[i] = (byte)new_val;
}
L16 = null;

// 存在すれば右チャンネルも
if ( m_channel == WaveChannel.Stereo ) {
    R8 = new byte[R16.length];
    for ( int i = 0; i < R16.length; i++ ) {
        double new_val = (R16[i] + 32768.0) / 65535.0 * 255.0;
        R8[i] = (byte)new_val;
    }
    R16 = null;
}

m_bit_per_sample = 8;
        }

        /// <summary>
        /// ファイルに保存
        /// </summary>
        /// <param name="file"></param>
        public void write( String file ) {
RandomAccessFile fs = null;
try {
    fs = new RandomAccessFile( file, "rw" );
    // RIFF
    fs.write( 0x52 );
    fs.write( 0x49 );
    fs.write( 0x46 );
    fs.write( 0x46 );

    // ファイルサイズ - 8最後に記入
    fs.write( 0x00 );
    fs.write( 0x00 );
    fs.write( 0x00 );
    fs.write( 0x00 );

    // WAVE
    fs.write( 0x57 );
    fs.write( 0x41 );
    fs.write( 0x56 );
    fs.write( 0x45 );

    // fmt 
    fs.write( 0x66 );
    fs.write( 0x6d );
    fs.write( 0x74 );
    fs.write( 0x20 );

    // fmt チャンクのサイズ
    fs.write( 0x12 );
    fs.write( 0x00 );
    fs.write( 0x00 );
    fs.write( 0x00 );

    // format ID
    fs.write( 0x01 );
    fs.write( 0x00 );

    // チャンネル数
    if ( m_channel == WaveChannel.Monoral ) {
        fs.write( 0x01 );
        fs.write( 0x00 );
    } else {
        fs.write( 0x02 );
        fs.write( 0x00 );
    }

    // サンプリングレート
    byte[] buf = PortUtil.getbytes_uint32_le( m_sample_rate );
    writeByteArray( fs, buf, 4 );

    // データ速度
    int ichannel = (m_channel == WaveChannel.Monoral) ? 1 : 2;
    int block_size = (int)(m_bit_per_sample / 8 * ichannel);
    long data_rate = m_sample_rate * block_size;
    buf = PortUtil.getbytes_uint32_le( data_rate );
    writeByteArray( fs, buf, 4 );

    // ブロックサイズ
    buf = PortUtil.getbytes_uint16_le( block_size );
    writeByteArray( fs, buf, 2 );

    // サンプルあたりのビット数
    buf = PortUtil.getbytes_uint16_le( m_bit_per_sample );
    writeByteArray( fs, buf, 2 );

    // 拡張部分
    fs.write( 0x00 );
    fs.write( 0x00 );

    // data
    fs.write( 0x64 );
    fs.write( 0x61 );
    fs.write( 0x74 );
    fs.write( 0x61 );

    // size of data chunk
    long size = block_size * m_total_samples;
    buf = PortUtil.getbytes_uint32_le( size );
    writeByteArray( fs, buf, 4 );

    // 波形データ
    for ( int i = 0; i < m_total_samples; i++ ) {
        if ( m_bit_per_sample == 8 ) {
            fs.write( L8[i] );
            if ( m_channel == WaveChannel.Stereo ) {
                fs.write( R8[i] );
            }
        } else {
            buf = PortUtil.getbytes_int16_le( L16[i] );
            writeByteArray( fs, buf, 2 );
            if ( m_channel == WaveChannel.Stereo ) {
                buf = PortUtil.getbytes_int16_le( R16[i] );
                writeByteArray( fs, buf, 2 );
            }
        }
    }

    // 最後にWAVEチャンクのサイズ
    long position = fs.getFilePointer();
    fs.seek( 4 );
    buf = PortUtil.getbytes_uint32_le( position - 8 );
    writeByteArray( fs, buf, 4 );
} catch ( Exception ex ) {
} finally {
    if ( fs != null ) {
        try {
            fs.close();
        } catch ( Exception ex2 ) {
        }
    }
}
        }

        private static void writeByteArray( RandomAccessFile fs, byte[] dat, int limit )
throws IOException
 {
fs.write( dat, 0, (dat.length > limit) ? limit : dat.length );
if ( dat.length < limit ) {
    for ( int i = 0; i < limit - dat.length; i++ ) {
        fs.write( 0x00 );
    }
}
        }

        /// <summary>
        /// ステレオをモノラル化
        /// </summary>
        public void monoralize() {
if ( m_channel != WaveChannel.Stereo ) {
    return;
}
if ( m_bit_per_sample == 8 ) {
    for ( int i = 0; i < L8.length; i++ ) {
        L8[i] = (byte)((L8[i] + R8[i]) / 2);
    }
    R8 = null;
    m_channel = WaveChannel.Monoral;
} else {
    for ( int i = 0; i < L16.length; i++ ) {
        L16[i] = (short)((L16[i] + R16[i]) / 2);
    }
    R16 = null;
    m_channel = WaveChannel.Monoral;
}
        }


        /// <summary>
        /// 前後の無音部分を削除します
        /// </summary>
        public void trimSilence() {
if ( m_bit_per_sample == 8 ) {
    if ( m_channel == WaveChannel.Monoral ) {
        int non_silence_begin = 1;
        for ( int i = 1; i < L8.length; i++ ) {
            if ( L8[i] != 0x80 ) {
                non_silence_begin = i;
                break;
            }
        }
        int non_silence_end = L8.length - 1;
        for ( int i = L8.length - 1; i >= 0; i-- ) {
            if ( L8[i] != 0x80 ) {
                non_silence_end = i;
                break;
            }
        }
        int count = non_silence_end - non_silence_begin + 1;
        R8 = new byte[count];
        copyArray( L8, non_silence_begin, R8, 0, count );
        L8 = null;
        L8 = new byte[count];
        copyArray( R8, 0, L8, 0, count );
        R8 = null;
    } else {
        int non_silence_begin = 1;
        for ( int i = 1; i < L8.length; i++ ) {
            if ( L8[i] != 0x80 || R8[i] != 0x80 ) {
                non_silence_begin = i;
                break;
            }
        }
        int non_silence_end = L8.length - 1;
        for ( int i = L8.length - 1; i >= 0; i-- ) {
            if ( L8[i] != 0x80 || R8[i] != 0x80 ) {
                non_silence_end = i;
                break;
            }
        }
        int count = non_silence_end - non_silence_begin + 1;
        byte[] buf = new byte[count];
        copyArray( L8, non_silence_begin, buf, 0, count );
        L8 = null;
        L8 = new byte[count];
        copyArray( buf, 0, L8, 0, count );
        copyArray( R8, non_silence_begin, buf, 0, count );
        R8 = null;
        R8 = new byte[count];
        copyArray( buf, 0, R8, 0, count );
        buf = null;
    }
} else {
    if ( m_channel == WaveChannel.Monoral ) {
        int non_silence_begin = 1;
        for ( int i = 1; i < L16.length; i++ ) {
            if ( L16[i] != 0 ) {
                non_silence_begin = i;
                break;
            }
        }
        int non_silence_end = L16.length - 1;
        for ( int i = L16.length - 1; i >= 0; i-- ) {
            if ( L16[i] != 0 ) {
                non_silence_end = i;
                break;
            }
        }
        int count = non_silence_end - non_silence_begin + 1;
        R16 = new short[count];
        copyArray( L16, non_silence_begin, R16, 0, count );
        L16 = resizeArray( L16, count );
        copyArray( R16, 0, L16, 0, count );
        R16 = null;
    } else {
        int non_silence_begin = 1;
        for ( int i = 1; i < L16.length; i++ ) {
            if ( L16[i] != 0 || R16[i] != 0 ) {
                non_silence_begin = i;
                break;
            }
        }
        int non_silence_end = L16.length - 1;
        for ( int i = L16.length - 1; i >= 0; i-- ) {
            if ( L16[i] != 0 || R16[i] != 0 ) {
                non_silence_end = i;
                break;
            }
        }
        int count = non_silence_end - non_silence_begin + 1;
        short[] buf = new short[count];
        copyArray( L16, non_silence_begin, buf, 0, count );
        L16 = resizeArray( L16, count );
        copyArray( buf, 0, L16, 0, count );

        copyArray( R16, non_silence_begin, buf, 0, count );
        R16 = resizeArray( R16, count );
        copyArray( buf, 0, R16, 0, count );
        buf = null;
    }
}

if ( m_bit_per_sample == 8 ) {
    m_total_samples = (long)L8.length;
} else {
    m_total_samples = (long)L16.length;
}
        }

        public Wave() {
m_channel = WaveChannel.Stereo;
m_bit_per_sample = 16;
m_sample_rate = 44100;
        }

        public Wave( WaveChannel channels, int bit_per_sample, int sample_rate, int initial_capacity ) {
m_channel = channels;
m_bit_per_sample = bit_per_sample;
m_sample_rate = sample_rate;
m_total_samples = initial_capacity;
if ( m_bit_per_sample == 8 ) {
    L8 = new byte[(int)m_total_samples];
    if ( m_channel == WaveChannel.Stereo ) {
        R8 = new byte[(int)m_total_samples];
    }
} else if ( m_bit_per_sample == 16 ) {
    L16 = new short[(int)m_total_samples];
    if ( m_channel == WaveChannel.Stereo ) {
        R16 = new short[(int)m_total_samples];
    }
}
        }

        public void append( double[] L, double[] R ) {
int length = Math.min( L.length, R.length );
int old_length = L16.length;
if ( m_bit_per_sample == 16 && m_channel == WaveChannel.Stereo ) {
    L16 = resizeArray( L16, (int)(m_total_samples + length) );
    R16 = resizeArray( R16, (int)(m_total_samples + length) );
    m_total_samples += (long)length;
    for ( int i = old_length; i < m_total_samples; i++ ) {
        L16[i] = (short)(L[i - old_length] * 32768f);
        R16[i] = (short)(R[i - old_length] * 32768f);
    }
} //else ... TODO: Wave+Append他のbitpersec, channelのとき
        }

        public Wave( String path ) {
read( path );
        }

        private boolean parseAiffHeader( RandomAccessFile fs ) {
try {
    byte[] buf = new byte[4];
    fs.read( buf, 0, 4 );
    long chunk_size_form = PortUtil.make_uint32_be( buf );
    fs.read( buf, 0, 4 ); // AIFF
    String tag = new String( new char[] { (char)buf[0], (char)buf[1], (char)buf[2], (char)buf[3] } );
    if ( !tag.equals( "AIFF" ) ) {
        return false;
    }
    fs.read( buf, 0, 4 ); // COMM
    tag = new String( new char[] { (char)buf[0], (char)buf[1], (char)buf[2], (char)buf[3] } );
    if ( !tag.equals( "COMM" ) ) {
        return false;
    }
    fs.read( buf, 0, 4 ); // COMM chunk size
    long chunk_size_comm = PortUtil.make_uint32_be( buf );
    long chunk_loc_comm = fs.getFilePointer();
    fs.read( buf, 0, 2 ); // number of channel
    int num_channel = PortUtil.make_uint16_be( buf );
    if ( num_channel == 1 ) {
        m_channel = WaveChannel.Monoral;
    } else {
        m_channel = WaveChannel.Stereo;
    }
    fs.read( buf, 0, 4 ); // number of samples
    m_total_samples = PortUtil.make_uint32_be( buf );
    fs.read( buf, 0, 2 ); // block size
    m_bit_per_sample = PortUtil.make_uint16_be( buf );
    byte[] buf10 = new byte[10];
    fs.read( buf10, 0, 10 ); // sample rate
    m_sample_rate = (long)make_double_from_extended( buf10 );
    fs.seek( chunk_loc_comm + (long)chunk_size_comm );

    fs.read( buf, 0, 4 ); // SSND
    tag = new String( new char[] { (char)buf[0], (char)buf[1], (char)buf[2], (char)buf[3] } );
    if ( !tag.equals( "SSND" ) ) {
        return false;
    }
    fs.read( buf, 0, 4 ); // SSND chunk size
    long chunk_size_ssnd = PortUtil.make_uint32_be( buf );
} catch ( Exception ex ) {
    return false;
}
return true;
        }

        private static double make_double_from_extended( byte[] bytes ) {
double f;
long hiMant, loMant;

int expon = ((bytes[0] & 0x7F) << 8) | (bytes[1] & 0xFF);
hiMant = ((long)(bytes[2] & 0xFF) << 24)
       | ((long)(bytes[3] & 0xFF) << 16)
       | ((long)(bytes[4] & 0xFF) << 8)
       | ((long)(bytes[5] & 0xFF));
loMant = ((long)(bytes[6] & 0xFF) << 24)
       | ((long)(bytes[7] & 0xFF) << 16)
       | ((long)(bytes[8] & 0xFF) << 8)
       | ((long)(bytes[9] & 0xFF));

if ( expon == 0 && hiMant == 0 && loMant == 0 ) {
    f = 0;
} else {
    if ( expon == 0x7FFF ) {
        f = Double.MAX_VALUE;
    } else {
        expon -= 16383;
        //f = ldexp( double_from_uint( hiMant ), expon -= 31 );
        f = Math.pow( 2.0, expon - 31 ) * double_from_uint( hiMant );
        //f += ldexp( double_from_uint( loMant ), expon -= 32 );
        f += Math.pow( 2.0, expon - 32 ) * double_from_uint( loMant );
    }
}

if ( (bytes[0] & 0x80) != 0 ) {
    return -f;
} else {
    return f;
}
        }

        private static double double_from_uint( long u ) {
return (((double)((int)(u - 2147483647 - 1))) + 2147483648.0);
        }

        private boolean parseWaveHeader( RandomAccessFile fs ) {
try {
    byte[] buf = new byte[4];
    // detect size of RIFF chunk
    fs.read( buf, 0, 4 );
    long riff_chunk_size = PortUtil.make_uint32_le( buf );

    // check wave header
    fs.seek( 8 );
    fs.read( buf, 0, 4 );
    if ( buf[0] != 0x57 ||
        buf[1] != 0x41 ||
        buf[2] != 0x56 ||
        buf[3] != 0x45 ) {
        fs.close();
        return false;
    }

    // check fmt chunk header
    fs.read( buf, 0, 4 );
    if ( buf[0] != 0x66 ||
        buf[1] != 0x6d ||
        buf[2] != 0x74 ||
        buf[3] != 0x20 ) {
        fs.close();
        return false;
    }

    // detect size of fmt chunk
    long fmt_chunk_bytes;
    fs.read( buf, 0, 4 );
    fmt_chunk_bytes = PortUtil.make_uint32_le( buf );

    // get format ID
    fs.read( buf, 0, 2 );
    int format_id = PortUtil.make_uint16_le( buf );
    if ( format_id != 1 ) {
        fs.close();
        return false;
    }

    // get the number of channel(s)
    fs.read( buf, 0, 2 );
    int num_channels = PortUtil.make_uint16_le( buf );
    if ( num_channels == 1 ) {
        m_channel = WaveChannel.Monoral;
    } else if ( num_channels == 2 ) {
        m_channel = WaveChannel.Stereo;
    } else {
        fs.close();
        return false;
    }

    // get sampling rate
    fs.read( buf, 0, 4 );
    m_sample_rate = PortUtil.make_uint32_le( buf );

    // get bit per sample
    fs.seek( 0x22 );
    fs.read( buf, 0, 2 );
    m_bit_per_sample = PortUtil.make_uint16_le( buf );
    if ( m_bit_per_sample != 0x08 && m_bit_per_sample != 0x10 ) {
        fs.close();
        return false;
    }

    // move to the end of fmt chunk
    fs.seek( 0x14 + fmt_chunk_bytes );

    // move to the top of data chunk
    fs.read( buf, 0, 4 );
    String tag = new String( new char[] { (char)buf[0], (char)buf[1], (char)buf[2], (char)buf[3] } );
    while ( !tag.equals( "data" ) ) {
        fs.read( buf, 0, 4 );
        long size = PortUtil.make_uint32_le( buf );
        fs.seek( fs.getFilePointer() + size );
        fs.read( buf, 0, 4 );
        tag = new String( new char[] { (char)buf[0], (char)buf[1], (char)buf[2], (char)buf[3] } );
    }

    // get size of data chunk
    fs.read( buf, 0, 4 );
    long data_chunk_bytes = PortUtil.make_uint32_le( buf );
    m_total_samples = (long)(data_chunk_bytes / (num_channels * m_bit_per_sample / 8));

} catch ( Exception ex ) {
}
return true;
        }

        public boolean read( String path ) {
RandomAccessFile fs = null;
boolean ret = false;
try {
    fs = new RandomAccessFile( path, "r" );
    // check RIFF header
    byte[] buf = new byte[4];
    fs.read( buf, 0, 4 );
    boolean change_byte_order = false;
    if ( buf[0] == 0x52 && buf[1] == 0x49 && buf[2] == 0x46 && buf[3] == 0x46 ) {
        ret = parseWaveHeader( fs );
    } else if ( buf[0] == 0x46 && buf[1] == 0x4f && buf[2] == 0x52 && buf[3] == 0x4d ) {
        ret = parseAiffHeader( fs );
        change_byte_order = true;
    } else {
        ret = false;
    }
    // prepare data
    if ( m_bit_per_sample == 8 ) {
        L8 = new byte[(int)m_total_samples];
        if ( m_channel == WaveChannel.Stereo ) {
            R8 = new byte[(int)m_total_samples];
        }
    } else {
        L16 = new short[(int)m_total_samples];
        if ( m_channel == WaveChannel.Stereo ) {
            R16 = new short[(int)m_total_samples];
        }
    }

    // read data
    // TODO: big endianのときの読込み。
    byte[] buf2 = new byte[2];
    for ( int i = 0; i < m_total_samples; i++ ) {
        if ( m_bit_per_sample == 8 ) {
            fs.read( buf, 0, 1 );
            L8[i] = buf[0];
            if ( m_channel == WaveChannel.Stereo ) {
                fs.read( buf, 0, 1 );
                R8[i] = buf[0];
            }
        } else {
            fs.read( buf2, 0, 2 );
            if ( change_byte_order ) {
                byte b = buf2[0];
                buf2[0] = buf2[1];
                buf2[1] = b;
            }
            L16[i] = PortUtil.make_int16_le( buf2 );
            if ( m_channel == WaveChannel.Stereo ) {
                fs.read( buf2, 0, 2 );
                if ( change_byte_order ) {
                    byte b = buf2[0];
                    buf2[0] = buf2[1];
                    buf2[1] = b;
                }
                R16[i] = PortUtil.make_int16_le( buf2 );
            }
        }
    }
} catch ( Exception ex ) {
} finally {
    if ( fs != null ) {
        try {
            fs.close();
        } catch ( Exception ex2 ) {
        }
    }
}
return ret;
        }

        private static short[] resizeArray( short[] src, int length ) {
short[] ret = new short[length];
if ( src.length <= length ) {
    for ( int i = 0; i < src.length; i++ ) {
        ret[i] = src[i];
    }
} else {
    for ( int i = 0; i < length; i++ ) {
        ret[i] = src[i];
    }
}
return ret;
        }

        private static byte[] resizeArray( byte[] src, int length ) {
byte[] ret = new byte[length];
if ( src.length <= length ) {
    for ( int i = 0; i < src.length; i++ ) {
        ret[i] = src[i];
    }
} else {
    for ( int i = 0; i < length; i++ ) {
        ret[i] = src[i];
    }
}
return ret;
        }

        private static void copyArray( short[] src, int src_start, short[] dest, int dest_start, int length ) {
for ( int i = 0; i < length; i++ ) {
    dest[i + dest_start] = src[i + src_start];
}
        }

        private static void copyArray( byte[] src, int src_start, byte[] dest, int dest_start, int length ) {
for ( int i = 0; i < length; i++ ) {
    dest[i + dest_start] = src[i + src_start];
}
        }
    }


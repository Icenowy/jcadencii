/*
 * TextMemoryStream.cs
 * Copyright © 2008-2011 kbinani
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

import org.kbinani.*;

import java.io.*;

import java.util.*;


public class TextStream implements ITextWriter {
    static final int INIT_BUFLEN = 512;
    private char[] array = new char[INIT_BUFLEN];
    private int length = 0;
    private int position = -1;

    public int getPointer() {
        return position;
    }

    public void setPointer(int value) {
        position = value;
    }

    public char get() {
        position++;

        return array[position];
    }

    public String readLine() {
        StringBuilder sb = new StringBuilder();
        // '\n'が来るまで読み込み
        position++;

        for (; position < length; position++) {
            char c = array[position];

            if (c == '\n') {
                break;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    public boolean ready() {
        if ((0 <= (position + 1)) && ((position + 1) < length)) {
            return true;
        } else {
            return false;
        }
    }

    private void ensureCapacity(int length) {
        if (length > array.length) {
            int newLength = length;

            if (this.length <= 0) {
                newLength = (length * 3) >> 1;
            } else {
                int order = length / array.length;

                if (order <= 1) {
                    order = 2;
                }

                newLength = array.length * order;
            }

            char[] buf = new char[newLength];

            for (int i = 0; i < array.length; i++) {
                buf[i] = array[i];
            }

            array = buf;
        }
    }

    public void write(String str) {
        int len = PortUtil.getStringLength(str);
        int newSize = length + len;
        int offset = length;
        ensureCapacity(newSize);

        for (int i = 0; i < len; i++) {
            array[offset + i] = str.charAt(i);
        }

        length = newSize;
    }

    public void writeLine(String str) {
        write(str);
        newLine();
    }

    public void newLine() {
        int new_size = length + 1;
        int offset = length;
        ensureCapacity(new_size);
        array[offset] = '\n';
        length = new_size;
    }

    public void close() {
        array = null;
        length = 0;
    }
}

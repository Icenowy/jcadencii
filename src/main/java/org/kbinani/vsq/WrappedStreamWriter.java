/*
 * WrappedStreamWriter.cs
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

import java.io.*;


public class WrappedStreamWriter implements ITextWriter {
    BufferedWriter m_writer;

    public WrappedStreamWriter(BufferedWriter stream_writer) {
        m_writer = stream_writer;
    }

    public void newLine() throws java.io.IOException {
        m_writer.newLine();
    }

    public void write(String value) throws IOException {
        m_writer.write(value);
    }

    public void writeLine(String value) throws IOException {
        m_writer.write(value);
        m_writer.newLine();
    }

    public void close() throws IOException {
        m_writer.close();
    }
}

/*
 * IWaveReceiver.cs
 * Copyright © 2010-2011 kbinani
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

public interface IWaveReceiver {
    void append(double[] left, double[] right, int length);

    int getSampleRate();

    void close();
}

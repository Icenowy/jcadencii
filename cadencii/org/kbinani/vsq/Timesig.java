/*
 * Timesig.cs
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

public class Timesig {
    public int numerator;
    public int denominator;

    public Timesig() {
    }

    public Timesig(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
}

/*
 * VsqBarLineType.cs
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

public class VsqBarLineType {
    private int m_clock;
    private boolean m_is_separator;
    private int m_denominator;
    private int m_numerator;
    private int m_bar_count;

    public VsqBarLineType() {
    }

    public VsqBarLineType(int clock, boolean is_separator, int denominator,
        int numerator, int bar_count) {
        m_clock = clock;
        m_is_separator = is_separator;
        m_denominator = denominator;
        m_numerator = numerator;
        m_bar_count = bar_count;
    }

    public int getBarCount() {
        return m_bar_count;
    }

    public int getLocalDenominator() {
        return m_denominator;
    }

    public int getLocalNumerator() {
        return m_numerator;
    }

    public int clock() {
        return m_clock;
    }

    public boolean isSeparator() {
        return m_is_separator;
    }
}

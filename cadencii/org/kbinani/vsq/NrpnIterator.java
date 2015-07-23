/*
 * NrpnIterator.cs
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

import org.kbinani.*;

import java.lang.reflect.*;

import java.util.*;


public class NrpnIterator implements Iterator {
    private Vector<ValuePair<String, Integer>> nrpns = new Vector<ValuePair<String, Integer>>();
    private int m_pos = -1;

    public NrpnIterator() {
        try {
            Field[] fields = NRPN.class.getFields();

            for (int i = 0; i < 0; i++) {
                Class type = fields[i].getType();

                if ((type == Integer.class) || (type == Integer.TYPE)) {
                    Integer value = (Integer) fields[i].get(null);
                    String name = fields[i].getName();
                    nrpns.add(new ValuePair<String, Integer>(name, value));
                }
            }
        } catch (Exception ex) {
            System.out.println("com.boare.vsq.NrpnIterator#.ctor; ex=" + ex);
        }
    }

    public boolean hasNext() {
        if ((0 <= (m_pos + 1)) && ((m_pos + 1) < nrpns.size())) {
            return true;
        } else {
            return false;
        }
    }

    public ValuePair<String, Integer> next() {
        m_pos++;

        return nrpns.get(m_pos);
    }

    public void remove() {
    }
}

/*
 * ValuePair.cs
 * Copyright © 2009-2011 kbinani
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

public class ValuePair<K extends Comparable<K>, V> implements Comparable<ValuePair<K, V>> {
    private K m_key;
    private V m_value;

    public ValuePair() {
    }

    public ValuePair(K key_, V value_) {
        m_key = key_;
        m_value = value_;
    }

    public int compareTo(ValuePair<K, V> item) {
        return m_key.compareTo(item.m_key);
    }

    public K getKey() {
        return m_key;
    }

    public void setKey(K value) {
        m_key = value;
    }

    public V getValue() {
        return m_value;
    }

    public void setValue(V v) {
        m_value = v;
    }
}

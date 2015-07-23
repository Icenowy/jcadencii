/*
 * vec.cs
 * Copyright c 2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.vsq is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani;

import java.util.*;


public class dic {
    private dic() {
    }

    public static <K, V> int count(TreeMap<K, V> dict) {
        return dict.size();
    }

    public static <K, V> boolean containsKey(TreeMap<K, V> dict, K key) {
        return dict.containsKey(key);
    }

    public static <K, V> void put(TreeMap<K, V> dict, K key, V value) {
        dict.put(key, value);
    }

    public static <K, V> V get(TreeMap<K, V> dict, K key) {
        return dict.get(key);
    }
}
;

/*
 * vec.cs
 * Copyright © 2011 kbinani
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

import java.util.Collections;
import java.util.Vector;


public class vec {
    private vec() {
    }

    public static <T> void ensureCapacity(Vector<T> arr, int size) {
        arr.ensureCapacity(size);
    }

    public static <T extends Comparable<?super T>> void sort(Vector<T> arr) {
        Collections.sort(arr);
    }

    public static <T> T get(Vector<T> arr, int index) {
        return arr.get(index);
    }

    public static <T> void set(Vector<T> arr, int index, T item) {
        arr.set(index, item);
    }

    public static <T> int size(Vector<T> arr) {
        return arr.size();
    }

    public static <T> void add(Vector<T> arr, T item) {
        arr.add(item);
    }

    public static <T> boolean contains(Vector<T> arr, T item) {
        return arr.contains(item);
    }

    public static <T> void clear(Vector<T> arr) {
        arr.clear();
    }
}
;

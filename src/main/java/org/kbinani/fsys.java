/*
 * fsys.cs
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

import java.io.*;


public class fsys {
    private static String mSeparator = "";

    private fsys() {
    }

    public static String separator() {
        if (str.compare(mSeparator, "")) {
            mSeparator = File.separator;
        }

        return mSeparator;
    }

    public static String combine(String path1, String path2) {
        if (path1 == null) {
            path1 = "";
        }

        if (path2 == null) {
            path2 = "";
        }

        separator();

        if (str.endsWith(path1, mSeparator)) {
            path1 = str.sub(path1, 0, str.length(path1) - 1);
        }

        if (str.startsWith(path2, mSeparator)) {
            path2 = str.sub(path2, 1);
        }

        return path1 + mSeparator + path2;
    }

    public static boolean isDirectoryExists(String path) {
        File f = new File(path);

        if (f.exists()) {
            if (f.isFile()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isFileExists(String path) {
        File f = new File(path);

        return f.isFile();
    }
}
;

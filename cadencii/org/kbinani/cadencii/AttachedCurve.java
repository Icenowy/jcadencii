/*
 * AttachedCurve.cs
 * Copyright © 2008-2011 kbinani
 *
 * This file is part of org.kbinani.cadencii.
 *
 * org.kbinani.cadencii is free software; you can redistribute it and/or
 * modify it under the terms of the GPLv3 License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.cadencii;

import org.kbinani.*;

import org.kbinani.xml.*;

import java.util.*;


public class AttachedCurve implements Cloneable {
    private Vector<BezierCurves> mCurves = new Vector<BezierCurves>();

    @XmlGenericType(BezierCurves.class)
    public Vector<BezierCurves> getCurves() {
        return mCurves;
    }

    public void setCurves(Vector<BezierCurves> value) {
        mCurves = value;
    }

    public BezierCurves get(int index) {
        return mCurves.get(index);
    }

    public void set(int index, BezierCurves value) {
        mCurves.set(index, value);
    }

    public void add(BezierCurves item) {
        mCurves.add(item);
    }

    public void removeElementAt(int index) {
        mCurves.removeElementAt(index);
    }

    public void insertElementAt(int position, BezierCurves attached_curve) {
        mCurves.insertElementAt(attached_curve, position);
    }

    public Object clone() {
        AttachedCurve ret = new AttachedCurve();
        ret.mCurves.clear();

        int c = mCurves.size();

        for (int i = 0; i < c; i++) {
            ret.mCurves.add((BezierCurves) mCurves.get(i).clone());
        }

        return ret;
    }
}

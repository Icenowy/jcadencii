/*
 * BezierPoint.cs
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

import org.kbinani.xml.*;

import java.io.*;


/// <summary>
/// ベジエ曲線を構成するデータ点。
/// </summary>
public class BezierPoint implements Comparable<BezierPoint>, Cloneable,
    Serializable {
    @XmlIgnore
    public PointD controlLeft;
    @XmlIgnore
    public PointD controlRight;
    private int mID;
    private PointD mCenter;
    private BezierControlType mTypeLeft;
    private BezierControlType mTypeRight;

    public BezierPoint() {
    }

    public BezierPoint(PointD p1) {
        this(p1.getX(), p1.getY());
    }

    public BezierPoint(double x, double y) {
        PointD p = new PointD(x, y);
        mCenter = p;
        controlLeft = p;
        controlRight = p;
        mTypeLeft = BezierControlType.None;
        mTypeRight = BezierControlType.None;
    }

    public BezierPoint(PointD p1, PointD left, PointD right) {
        mCenter = p1;
        controlLeft = new PointD(left.getX() - mCenter.getX(),
                left.getY() - mCenter.getY());
        controlRight = new PointD(right.getX() - mCenter.getX(),
                right.getY() - mCenter.getY());
        mTypeLeft = BezierControlType.None;
        mTypeRight = BezierControlType.None;
    }

    @XmlIgnore
    public int getID() {
        return mID;
    }

    @XmlIgnore
    public void setID(int value) {
        mID = value;
    }

    public String toString() {
        return "m_super=" + mCenter.getX() + "," + mCenter.getY() + "\n" +
        "m_control_left=" + controlLeft.getX() + "," + controlLeft.getY() +
        "\n" + "m_control_right=" + controlRight.getX() + "," +
        controlRight.getY() + "\n" + "m_type_left=" + mTypeLeft + "\n" +
        "m_type_right=" + mTypeRight + "\n";
    }

    public Object clone() {
        BezierPoint result = new BezierPoint(this.getBase(),
                this.getControlLeft(), this.getControlRight());
        result.controlLeft = this.controlLeft;
        result.controlRight = this.controlRight;
        result.mTypeLeft = this.mTypeLeft;
        result.mTypeRight = this.mTypeRight;
        result.mID = this.mID;

        return result;
    }

    public int compareTo(BezierPoint item) {
        double thisx = this.getBase().getX();
        double itemx = item.getBase().getX();

        if (thisx > itemx) {
            return 1;
        } else if (thisx < itemx) {
            return -1;
        } else {
            if (this.getID() > item.getID()) {
                return 1;
            } else if (this.getID() < item.getID()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public PointD getBase() {
        return mCenter;
    }

    public void setBase(PointD value) {
        mCenter = value;
    }

    public void setPosition(BezierPickedSide picked_side, PointD new_position) {
        if (picked_side == BezierPickedSide.BASE) {
            this.setBase(new_position);
        } else if (picked_side == BezierPickedSide.LEFT) {
            this.controlLeft = new PointD(new_position.getX() -
                    this.getBase().getX(),
                    new_position.getY() - this.getBase().getY());
        } else {
            this.controlRight = new PointD(new_position.getX() -
                    this.getBase().getX(),
                    new_position.getY() - this.getBase().getY());
        }
    }

    public PointD getPosition(BezierPickedSide picked_side) {
        if (picked_side == BezierPickedSide.BASE) {
            return this.getBase();
        } else if (picked_side == BezierPickedSide.LEFT) {
            return this.getControlLeft();
        } else {
            return this.getControlRight();
        }
    }

    public BezierControlType getControlType(BezierPickedSide picked_side) {
        if (picked_side == BezierPickedSide.LEFT) {
            return this.getControlLeftType();
        } else if (picked_side == BezierPickedSide.RIGHT) {
            return this.getControlRightType();
        } else {
            return BezierControlType.None;
        }
    }

    public PointD getControlLeft() {
        if (mTypeLeft != BezierControlType.None) {
            return new PointD(mCenter.getX() + controlLeft.getX(),
                mCenter.getY() + controlLeft.getY());
        } else {
            return mCenter;
        }
    }

    public void setControlLeft(PointD value) {
        controlLeft = new PointD(value.getX() - mCenter.getX(),
                value.getY() - mCenter.getY());
    }

    public PointD getControlRight() {
        if (mTypeRight != BezierControlType.None) {
            return new PointD(mCenter.getX() + controlRight.getX(),
                mCenter.getY() + controlRight.getY());
        } else {
            return mCenter;
        }
    }

    public void setControlRight(PointD value) {
        controlRight = new PointD(value.getX() - mCenter.getX(),
                value.getY() - mCenter.getY());
    }

    public BezierControlType getControlLeftType() {
        return mTypeLeft;
    }

    public void setControlLeftType(BezierControlType value) {
        mTypeLeft = value;

        if ((mTypeLeft == BezierControlType.Master) &&
                (mTypeRight != BezierControlType.None)) {
            mTypeRight = BezierControlType.Master;
        }
    }

    public BezierControlType getControlRightType() {
        return mTypeRight;
    }

    public void setControlRightType(BezierControlType value) {
        mTypeRight = value;

        if ((mTypeRight == BezierControlType.Master) &&
                (mTypeLeft != BezierControlType.None)) {
            mTypeLeft = BezierControlType.Master;
        }
    }
}

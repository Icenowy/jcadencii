/*
 * PointD.cs
 * Copyright © 2009-2011 kbinani
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

import java.awt.*;

import java.io.*;


public class PointD implements Serializable {
    private double m_x;
    private double m_y;

    public PointD(PointD item) {
        m_x = item.getX();
        m_y = item.getY();
    }

    public PointD(double x, double y) {
        m_x = x;
        m_y = y;
    }

    public PointD() {
        m_x = 0;
        m_y = 0;
    }

    public Point toPoint() {
        return new Point((int) m_x, (int) m_y);
    }

    /*public System.Drawing.PointF toPointF() {
    return new System.Drawing.PointF( (float)m_x, (float)m_y );
    }*/
    public double getX() {
        return m_x;
    }

    public void setX(double value) {
        m_x = value;
    }

    public double getY() {
        return m_y;
    }

    public void setY(double value) {
        m_y = value;
    }
}

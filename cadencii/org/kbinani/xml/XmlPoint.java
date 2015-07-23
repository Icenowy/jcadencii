/*
 * XmlPoint.cs
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
package org.kbinani.xml;

import java.awt.*;

    public class XmlPoint {
        public int x;
        public int y;

        public XmlPoint() {
        }

        public XmlPoint( int x_, int y_ ) {
x = x_;
y = y_;
        }

        public XmlPoint( Point p ) {
x = p.x;
y = p.y;
        }

        public Point toPoint() {
return new Point( x, y );
        }

        public int getX() {
return x;
        }

        public void setX( int value ) {
x = value;
        }

        public int getY() {
return y;
        }

        public void setY( int value ) {
y = value;
        }


    }


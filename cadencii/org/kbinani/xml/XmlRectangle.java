/*
 * XmlRectangle.cs
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

    public class XmlRectangle{
        public int x;
        public int y;
        public int width;
        public int height;

        public XmlRectangle()
        {
        }

        public XmlRectangle( int x_, int y_, int width_, int height_ ){
x = x_;
y = y_;
width = width_;
height = height_;
        }

        public XmlRectangle( Rectangle rc ) {
x = rc.x;
y = rc.y;
width = rc.width;
height = rc.height;
        }

        public Rectangle toRectangle() {
return new Rectangle( x, y, width, height );
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

        public int getWidth() {
return width;
        }

        public void setWidth( int value ) {
width = value;
        }

        public int getHeight() {
return height;
        }

        public void setHeight( int value ) {
height = value;
        }

    }


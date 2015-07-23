/*
 * BVScrollBar.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.windows.forms.
 *
 * org.kbinani.windows.forms is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.windows.forms is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.windows.forms;

import javax.swing.JScrollBar;

public class BVScrollBar extends BScrollBar{
    private static final long serialVersionUID = 1L;

    public BVScrollBar(){
        super( JScrollBar.VERTICAL );
    }

    public void setVisibleAmount( int value ){
        super.setVisibleAmount( value );
        int unit_increment = value / 10;
        if( unit_increment <= 0 ){
            unit_increment = 1;
        }
        setUnitIncrement( unit_increment );
        setBlockIncrement( value );
    }
}

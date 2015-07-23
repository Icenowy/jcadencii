/*
 * BGroupBox.cs
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class BGroupBox extends JPanel {

	private static final long serialVersionUID = 1L;

    public BGroupBox(){
        super();
        initialize();
    }

	private void initialize() {
        this.setSize(new Dimension(352, 268));
        this.setTitle("");
        super.setBorder( getTitledBorder() );
	}

	private TitledBorder getTitledBorder(){
	    TitledBorder titledBorder = BorderFactory.createTitledBorder( null, 
                    "",
                     TitledBorder.DEFAULT_JUSTIFICATION,
                     TitledBorder.DEFAULT_POSITION,
                     new Font( "Dialog", Font.BOLD, 12 ),
                     new Color( 51, 51, 51 ) );
    	return titledBorder;
    }
    
    public String getTitle(){
        Object obj = super.getBorder();
        if( obj == null ){
            super.setBorder( getTitledBorder() );
            return "";
        }else{
            if( obj instanceof TitledBorder ){
                TitledBorder border = (TitledBorder)obj;
                return border.getTitle();
            }else{
                super.setBorder( getTitledBorder() );
                return "";
            }
        }
    }
    
    public void setTitle( String value ){
        Object obj = super.getBorder();
        if( obj == null ){
            TitledBorder border = getTitledBorder();
            border.setTitle( value );
            super.setBorder( border );
        }else{
            if( obj instanceof TitledBorder ){
                TitledBorder border = (TitledBorder)obj;
                border.setTitle( value );
            }else{
                TitledBorder border = getTitledBorder();
                border.setTitle( value );
                super.setBorder( border );
            }
        }
    }
}

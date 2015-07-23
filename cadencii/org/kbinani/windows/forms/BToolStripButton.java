/*
 * BToolStripButton.cs
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;
import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;

public class BToolStripButton extends JToggleButton implements ActionListener{
    private static final long serialVersionUID = -9098464491775550703L;
    public final BEvent<BEventHandler> clickEvent = new BEvent<BEventHandler>();
    private boolean checkOnClick = true;
    
    public BToolStripButton(){
        super();
        addActionListener( this );
    }
    
    public boolean isCheckOnClick(){
        return checkOnClick;
    }
    
    public void setCheckOnClick( boolean value ){
        checkOnClick = value;
    }
    
    public void actionPerformed( ActionEvent e ){
        if( checkOnClick ){
            this.setSelected( !this.isSelected() );
        }
        try{
            clickEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BToolStripButton#actionPerformed; ex=" + ex );
        }
    }
}

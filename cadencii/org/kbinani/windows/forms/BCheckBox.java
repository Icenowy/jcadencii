/*
 * BCheckBox.cs
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;

public class BCheckBox extends JCheckBox 
                       implements ActionListener, 
                                  MouseListener,
                                  ItemListener
{
    private static final long serialVersionUID = 1L;

    public BCheckBox(){
        super();
        addActionListener( this );
        addMouseListener( this );
        addItemListener( this );
    }

    public void setMnemonic( int value )
    {
        String text = getText();
        int index = text.indexOf( value );
        if( index < 0 ){
            text += " (" + Character.toString( (char)value ) + ")";
            index = text.lastIndexOf( value );
            setText( text );
        }
        super.setMnemonic( value );
        super.setDisplayedMnemonicIndex( index );
    }
    
    // root impl of Click event is in BButton
    public final BEvent<BEventHandler> clickEvent = new BEvent<BEventHandler>();
    public void actionPerformed( ActionEvent e ){
        try{
            clickEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#actionPerformed; ex=" + ex );
        }
    }
    
    // root impl of MouseListener is in BButton
    public final BEvent<BMouseEventHandler> mouseClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDoubleClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDownEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseUpEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BEventHandler> mouseEnterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> mouseLeaveEvent = new BEvent<BEventHandler>();
    public void mouseClicked( MouseEvent e ){
        try{
            mouseClickEvent.raise( this, BMouseEventArgs.fromMouseEvent( e ) );
            if( e.getClickCount() >= 2 ){
                mouseDoubleClickEvent.raise( this, BMouseEventArgs.fromMouseEvent( e ) );
            }
        }catch( Exception ex ){
            System.err.println( "BButton#mouseClicked; ex=" + ex );
        }
    }    
    public void mouseEntered( MouseEvent e ){
        try{
            mouseEnterEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#mouseEntered; ex=" + ex );
        }
    }    
    public void mouseExited( MouseEvent e ){
        try{
            mouseLeaveEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#mouseExited; ex=" + ex );
        }
    }    
    public void mousePressed( MouseEvent e ){
        try{
            mouseDownEvent.raise( this, BMouseEventArgs.fromMouseEvent( e ) );
        }catch( Exception ex ){
            System.err.println( "BButton#mousePressed; ex=" + ex );
        }
    }    
    public void mouseReleased( MouseEvent e ){
        try{
            mouseUpEvent.raise( this, BMouseEventArgs.fromMouseEvent( e ) );
        }catch( Exception ex ){
            System.err.println( "BButton#mouseReleased; ex=" + ex );
        }
    }

    /* root impl of ItemListener */
    // root impl of ItemListener is in BCheckBox
    public final BEvent<BEventHandler> checkedChangedEvent = new BEvent<BEventHandler>();
    public void itemStateChanged(ItemEvent e) {
        try{
            checkedChangedEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BCheckBox#itemStateChanged; ex=" + ex );
        }
    }

}

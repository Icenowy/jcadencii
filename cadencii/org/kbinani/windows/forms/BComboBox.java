/*
 * BComboBox.cs
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;

public class BComboBox extends JComboBox implements ItemListener{
    private static final long serialVersionUID = -7617550549292777417L;
    public final BEvent<BEventHandler> selectedIndexChangedEvent = new BEvent<BEventHandler>();

    public BComboBox()
    {
        super();
        addItemListener( this );
    }
    
    public void itemStateChanged( ItemEvent e )
    {
        try{
            selectedIndexChangedEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BComboBox#itemStateChanged; ex=" + ex );
        }
    }
}

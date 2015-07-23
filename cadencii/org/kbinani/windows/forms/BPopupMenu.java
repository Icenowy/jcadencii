/*
 * BPopupMenu.cs
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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;
import org.kbinani.componentmodel.BCancelEventArgs;
import org.kbinani.componentmodel.BCancelEventHandler;

public class BPopupMenu extends JPopupMenu 
                        implements ComponentListener,
                                   PopupMenuListener
{
    private static final long serialVersionUID = 363411779635481115L;

    public BPopupMenu()
    {
        super();
        addComponentListener( this );
        addPopupMenuListener( this );
    }

    /* root impl of PopupMenuListener */
    // root impl of PopupMenuListener is in BPopupMenu
    public final BEvent<BCancelEventHandler> openingEvent = new BEvent<BCancelEventHandler>();
    public void popupMenuCanceled( PopupMenuEvent e ){
    }
    public void popupMenuWillBecomeInvisible( PopupMenuEvent e ){
    }
    public void popupMenuWillBecomeVisible( PopupMenuEvent e ){
        try{
            BCancelEventArgs e1 = new BCancelEventArgs();
            openingEvent.raise( this, e1 );
            if( e1.Cancel ){
                setVisible( false );
            }
        }catch( Exception ex ){
            System.err.println( "BPopupMenu#popupMenuWillBecomeVisible; ex=" + ex );
        }
    }

    // root impl of ComponentListener is in BButton
    public final BEvent<BEventHandler> visibleChangedEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> resizeEvent = new BEvent<BEventHandler>();
    public void componentHidden(ComponentEvent e) {
        try{
            visibleChangedEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#componentHidden; ex=" + ex );
        }
    }
    public void componentMoved(ComponentEvent e) {
    }
    public void componentResized(ComponentEvent e) {
        try{
            resizeEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#componentResized; ex=" + ex );
        }
    }
    public void componentShown(ComponentEvent e) {
        try{
            visibleChangedEvent.raise( this, new BEventArgs() );
        }catch( Exception ex ){
            System.err.println( "BButton#componentShown; ex=" + ex );
        }
    }
}

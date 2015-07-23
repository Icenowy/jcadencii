/*
 * PropertyPanel.cs
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

import java.util.*;
import javax.swing.*;
import java.awt.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;
import org.kbinani.windows.forms.*;

    public class PropertyPanel extends BPanel
    {
        public final BEvent<CommandExecuteRequiredEventHandler> commandExecuteRequiredEvent = new BEvent<CommandExecuteRequiredEventHandler>();
        private Vector<SelectedEventEntry> m_items;
        private int m_track;
        private boolean m_editing;

        public PropertyPanel()
        {
super();
initialize();
registerEventHandlers();
setResources();
m_items = new Vector<SelectedEventEntry>();
Util.applyFontRecurse( this, AppManager.editorConfig.getBaseFont() );
        }

        public boolean isEditing()
        {
return m_editing;
        }

        public void setEditing( boolean value )
        {
m_editing = value;
        }

        private void popGridItemExpandStatus()
        {
        }


        private void pushGridItemExpandStatus()
        {
        }


        public void updateValue( int track )
        {
m_track = track;
m_items.clear();

// 現在のGridItemの展開状態を取得
pushGridItemExpandStatus();

Object[] objs = new Object[AppManager.itemSelection.getEventCount()];
int i = -1;
for ( Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator(); itr.hasNext(); ) {
    SelectedEventEntry item = itr.next();
    i++;
    objs[i] = item;
}

propertyGrid.setSelectedObjects( objs );
popGridItemExpandStatus();
setEditing( false );
        }

        public void propertyGrid_PropertyValueChanged( Object s, BPropertyValueChangedEventArgs e )
        {
Object[] selobj = propertyGrid.getSelectedObjects();
int len = selobj.length;
VsqEvent[] items = new VsqEvent[len];
for ( int i = 0; i < len; i++ ) {
    SelectedEventEntry proxy = (SelectedEventEntry)selobj[i];
    items[i] = proxy.editing;
}
CadenciiCommand run = new CadenciiCommand( VsqCommand.generateCommandEventReplaceRange( m_track, items ) );
try{
    commandExecuteRequiredEvent.raise( this, run );
}catch( Exception ex ){
    serr.println( PropertyPanel.class + ".propertyGridPropertyValueChanged; ex=" + ex );
}
for ( int i = 0; i < len; i++ ) {
    AppManager.itemSelection.addEvent( items[i].InternalID );
}
propertyGrid.repaint();//.Refresh();
setEditing( false );
        }




        public void propertyGrid_Enter( Object sender, BEventArgs e )
        {
setEditing( true );
        }

        public void propertyGrid_Leave( Object sender, BEventArgs e )
        {
setEditing( false );
        }

        private void registerEventHandlers()
        {
propertyGrid.leaveEvent.add( new BEventHandler( this, "propertyGrid_Leave" ) );
propertyGrid.enterEvent.add( new BEventHandler( this, "propertyGrid_Enter" ) );
propertyGrid.propertyValueChangedEvent.add( new BPropertyValueChangedEventHandler( this, "propertyGrid_PropertyValueChanged" ) );
        }

        private void setResources()
        {
        }

        private BPropertyGrid propertyGrid;

        private void initialize(){
if( propertyGrid == null ){
    propertyGrid = new BPropertyGrid();
    VsqEvent ve = new VsqEvent();
    ve.ID = new VsqID();
    propertyGrid.setSelectedObjects( 
        new SelectedEventEntry[]{ new SelectedEventEntry( 0, ve, ve ) } );
    propertyGrid.setSelectedObjects( new Object[]{} );
    propertyGrid.setColumnWidth( 154 );
}
GridBagLayout lm = new GridBagLayout();
this.setLayout( lm );
GridBagConstraints gc = new GridBagConstraints();
gc.fill = GridBagConstraints.BOTH;
gc.weightx = 1.0D;
gc.weighty = 1.0D;
this.add( propertyGrid, gc );
        }
    }


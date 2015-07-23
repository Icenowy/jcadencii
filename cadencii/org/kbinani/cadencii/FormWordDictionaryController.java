/*
 * FormWordDictionaryController.cs
 * Copyright © 2011 kbinani
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
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.vsq.*;


public class FormWordDictionaryController extends ControllerBase implements FormWordDictionaryUiListener
{
    private FormWordDictionaryUiImpl ui;
    private static int mColumnWidth = 256;
    private static int mWidth = 327;
    private static int mHeight = 404;

    public FormWordDictionaryController()
    {
        ui = new FormWordDictionaryUiImpl( this );
        applyLanguage();
        ui.setSize( mWidth, mHeight );
    }



    public void buttonCancelClick()
    {
        ui.setDialogResult( false );
    }

    public void buttonDownClick()
    {
        int index = ui.listDictionariesGetSelectedRow();
        if ( 0 <= index && index + 1 < ui.listDictionariesGetItemCountRow() )
        {
            try
            {
                ui.listDictionariesClear();
                String upper_name = ui.listDictionariesGetItemAt( index );
                boolean upper_enabled = ui.listDictionariesIsRowChecked( index );
                String lower_name = ui.listDictionariesGetItemAt( index + 1 );
                boolean lower_enabled = ui.listDictionariesIsRowChecked( index + 1 );

                ui.listDictionariesSetItemAt( index + 1, upper_name );
                ui.listDictionariesSetRowChecked( index + 1, upper_enabled );
                ui.listDictionariesSetItemAt( index, lower_name );
                ui.listDictionariesSetRowChecked( index, lower_enabled );

                ui.listDictionariesSetSelectedRow( index + 1 );
            }
            catch ( Exception ex )
            {
                serr.println( "FormWordDictionary#btnDown_Click; ex=" + ex );
            }
        }
    }

    public void buttonUpClick()
    {
        int index = ui.listDictionariesGetSelectedRow();
        if ( index >= 1 )
        {
            try
            {
                ui.listDictionariesClearSelection();
                String upper_name = ui.listDictionariesGetItemAt( index - 1 );
                boolean upper_enabled = ui.listDictionariesIsRowChecked( index - 1 );
                String lower_name = ui.listDictionariesGetItemAt( index );
                boolean lower_enabled = ui.listDictionariesIsRowChecked( index );

                ui.listDictionariesSetItemAt( index - 1, lower_name );
                ui.listDictionariesSetRowChecked( index - 1, lower_enabled );
                ui.listDictionariesSetItemAt( index, upper_name );
                ui.listDictionariesSetRowChecked( index, upper_enabled );

                ui.listDictionariesSetSelectedRow( index - 1 );
            }
            catch ( Exception ex )
            {
                serr.println( "FormWordDictionary#btnUp_Click; ex=" + ex );
            }
        }
    }

    public void buttonOkClick()
    {
        ui.setDialogResult( true );
    }

    public void formLoad()
    {
        ui.listDictionariesClear();
        for ( int i = 0; i < SymbolTable.getCount(); i++ )
        {
            String name = SymbolTable.getSymbolTable( i ).getName();
            boolean enabled = SymbolTable.getSymbolTable( i ).isEnabled();
            ui.listDictionariesAddRow( name, enabled );
        }
    }

    public void formClosing()
    {
        mWidth = ui.getWidth();
        mHeight = ui.getHeight();
    }




    public void close()
    {
        ui.close();
    }

    public UiBase getUi()
    {
        return ui;
    }

    public int getWidth()
    {
        return ui.getWidth();
    }

    public int getHeight()
    {
        return ui.getHeight();
    }

    public void setLocation( int x, int y )
    {
        ui.setLocation( x, y );
    }

    public void applyLanguage()
    {
        ui.setTitle( gettext( "User Dictionary Configuration" ) );
        ui.labelAvailableDictionariesSetText( gettext( "Available Dictionaries" ) );
        ui.buttonOkSetText( gettext( "OK" ) );
        ui.buttonCancelSetText( gettext( "Cancel" ) );
        ui.buttonUpSetText( gettext( "Up" ) );
        ui.buttonDownSetText( gettext( "Down" ) );
    }

    public Vector<ValuePair<String, Boolean>> getResult()
    {
        Vector<ValuePair<String, Boolean>> ret = new Vector<ValuePair<String, Boolean>>();
        int count = ui.listDictionariesGetItemCountRow();
        for ( int i = 0; i < count; i++ )
        {
            String name = ui.listDictionariesGetItemAt( i );

            ret.add( new ValuePair<String, Boolean>(
                ui.listDictionariesGetItemAt( i ), ui.listDictionariesIsRowChecked( i ) ) );
        }
        return ret;
    }




    private static String gettext( String id )
    {
        return Messaging.getMessage( id );
    }

}


/*
 * FormBeatConfigController.cs
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

import java.awt.event.*;
import org.kbinani.*;
import org.kbinani.apputil.*;
import org.kbinani.windows.forms.*;

    public class FormBeatConfigController extends ControllerBase implements FormBeatConfigUiListener
    {
        private FormBeatConfigUi mUi;

        public FormBeatConfigController( int bar_count, int numerator, int denominator, boolean num_enabled, int pre_measure )
        {
mUi = new FormBeatConfigUiImpl( this );

applyLanguage();

mUi.setEnabledStartNum( num_enabled );
mUi.setEnabledEndNum( num_enabled );
mUi.setEnabledEndCheckbox( num_enabled );
mUi.setMinimumStartNum( -pre_measure + 1 );
mUi.setMaximumStartNum( Integer.MAX_VALUE );
mUi.setMinimumEndNum( -pre_measure + 1 );
mUi.setMaximumEndNum( Integer.MAX_VALUE );

// 拍子の分母
mUi.removeAllItemsDenominatorCombobox();
mUi.addItemDenominatorCombobox( "1" );
int count = 1;
for ( int i = 1; i <= 5; i++ )
{
    count *= 2;
    mUi.addItemDenominatorCombobox( count + "" );
}
count = 0;
while ( denominator > 1 )
{
    count++;
    denominator /= 2;
}
mUi.setSelectedIndexDenominatorCombobox( count );

// 拍子の分子
if ( numerator < mUi.getMinimumNumeratorNum() )
{
    mUi.setValueNumeratorNum( mUi.getMinimumNumeratorNum() );
}
else if ( mUi.getMaximumNumeratorNum() < numerator )
{
    mUi.setValueNumeratorNum( mUi.getMaximumNumeratorNum() );
}
else
{
    mUi.setValueNumeratorNum( numerator );
}

// 始点
if ( bar_count < mUi.getMinimumStartNum() )
{
    mUi.setValueStartNum( mUi.getMinimumStartNum() );
}
else if ( mUi.getMaximumStartNum() < bar_count )
{
    mUi.setValueStartNum( mUi.getMaximumStartNum() );
}
else
{
    mUi.setValueStartNum( bar_count );
}

// 終点
if ( bar_count < mUi.getMinimumEndNum() )
{
    mUi.setValueEndNum( mUi.getMinimumEndNum() );
}
else if ( mUi.getMaximumEndNum() < bar_count )
{
    mUi.setValueEndNum( mUi.getMaximumEndNum() );
}
else
{
    mUi.setValueEndNum( bar_count );
}
mUi.setFont( AppManager.editorConfig.BaseFontName, AppManager.editorConfig.BaseFontSize );
        }




        public void buttonOkClickedSlot()
        {
mUi.setDialogResult( true );
        }

        public void buttonCancelClickedSlot()
        {
mUi.setDialogResult( false );
        }

        public void checkboxEndCheckedChangedSlot()
        {
mUi.setEnabledEndNum( mUi.isCheckedEndCheckbox() );
        }





        public void close()
        {
mUi.close();
        }

        public void setLocation( int x, int y )
        {
mUi.setLocation( x, y );
        }

        public int getWidth()
        {
return mUi.getWidth();
        }

        public int getHeight()
        {
return mUi.getHeight();
        }

        public FormBeatConfigUi getUi()
        {
return mUi;
        }

        public int getStart()
        {
return (int)mUi.getValueStartNum();
        }

        public boolean isEndSpecified()
        {
return mUi.isCheckedEndCheckbox();
        }

        public int getEnd()
        {
return (int)mUi.getValueEndNum();
        }

        public int getNumerator()
        {
return (int)mUi.getValueNumeratorNum();
        }

        public int getDenominator()
        {
int ret = 1;
for ( int i = 0; i < mUi.getSelectedIndexDenominatorCombobox(); i++ )
{
    ret *= 2;
}
return ret;
        }

        public void applyLanguage()
        {
mUi.setTitle( gettext( "Beat Change" ) );
mUi.setTextPositionGroup( gettext( "Position" ) );
mUi.setTextBeatGroup( gettext( "Beat" ) );
mUi.setTextOkButton( gettext( "OK" ) );
mUi.setTextCancelButton( gettext( "Cancel" ) );
mUi.setTextStartLabel( gettext( "From" ) );
//lblStart.setMnemonic( KeyEvent.VK_F, numStart );
mUi.setTextEndCheckbox( gettext( "To" ) );
//chkEnd.setDisplayedMnemonicIndex( 0 );
mUi.setTextBar1Label( gettext( "Measure" ) );
mUi.setTextBar2Label( gettext( "Measure" ) );
        }




        private static String gettext( String id )
        {
return Messaging.getMessage( id );
        }
    }


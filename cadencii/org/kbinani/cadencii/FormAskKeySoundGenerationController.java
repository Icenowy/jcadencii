/*
 * FormAskKeySoundGenerationController.cs
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


import org.kbinani.*;
import org.kbinani.windows.forms.*;
import org.kbinani.apputil.*;


    public class FormAskKeySoundGenerationController extends ControllerBase implements FormAskKeySoundGenerationUiListener
    {
        private FormAskKeySoundGenerationUi mUi = null;

        public void setupUi( FormAskKeySoundGenerationUi ui )
        {
mUi = ui;
applyLanguage();
        }

        public FormAskKeySoundGenerationUi getUi()
        {
return mUi;
        }

        public void applyLanguage()
        {
mUi.setMessageLabelText( gettext( "It seems some key-board sounds are missing. Do you want to re-generate them now?" ) );
mUi.setAlwaysPerformThisCheckCheckboxText( gettext( "Always perform this check when starting Cadencii." ) );
mUi.setYesButtonText( gettext( "Yes" ) );
mUi.setNoButtonText( gettext( "No" ) );
        }

        public void buttonCancelClickedSlot()
        {
mUi.close( true );
        }

        public void buttonOkClickedSlot()
        {
mUi.close( false );
        }

        private static String gettext( String message )
        {
return Messaging.getMessage( message );
        }
    }


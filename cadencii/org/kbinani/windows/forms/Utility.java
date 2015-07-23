/*
 * Utility.cs
 * Copyright © 2010-2011 kbinani
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

import javax.swing.*;


public class Utility {
    public static final int MSGBOX_DEFAULT_OPTION = -1;
    public static final int MSGBOX_YES_NO_OPTION = 0;
    public static final int MSGBOX_YES_NO_CANCEL_OPTION = 1;
    public static final int MSGBOX_OK_CANCEL_OPTION = 2;
    public static final int MSGBOX_ERROR_MESSAGE = 0;
    public static final int MSGBOX_INFORMATION_MESSAGE = 1;
    public static final int MSGBOX_WARNING_MESSAGE = 2;
    public static final int MSGBOX_QUESTION_MESSAGE = 3;
    public static final int MSGBOX_PLAIN_MESSAGE = -1;

    public static BDialogResult showMessageBox(String text, String caption,
        int optionType, int messageType) {
        BDialogResult ret = BDialogResult.CANCEL;
        int r = JOptionPane.showConfirmDialog(null, text, caption, optionType,
                messageType);

        if (r == JOptionPane.YES_OPTION) {
            ret = BDialogResult.YES;
        } else if (r == JOptionPane.NO_OPTION) {
            ret = BDialogResult.NO;
        } else if (r == JOptionPane.CANCEL_OPTION) {
            ret = BDialogResult.CANCEL;
        } else if (r == JOptionPane.OK_OPTION) {
            ret = BDialogResult.OK;
        } else if (r == JOptionPane.CLOSED_OPTION) {
            ret = BDialogResult.CANCEL;
        }

        return ret;
    }
}

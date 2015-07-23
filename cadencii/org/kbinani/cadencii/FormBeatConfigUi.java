/*
 * FormBeatConfigUi.cs
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

public interface FormBeatConfigUi extends UiBase {
    void setFont(String fontName, float fontSize);

    void setTitle(String value);

    void setDialogResult(boolean value);

    void setLocation(int x, int y);

    int getWidth();

    int getHeight();

    void close();

    void setTextBar1Label(String value);

    void setTextBar2Label(String value);

    void setTextStartLabel(String value);

    void setTextOkButton(String value);

    void setTextCancelButton(String value);

    void setTextBeatGroup(String value);

    void setTextPositionGroup(String value);

    void setEnabledStartNum(boolean value);

    void setMinimumStartNum(int value);

    void setMaximumStartNum(int value);

    int getMaximumStartNum();

    int getMinimumStartNum();

    void setValueStartNum(int value);

    int getValueStartNum();

    void setEnabledEndNum(boolean value);

    void setMinimumEndNum(int value);

    void setMaximumEndNum(int value);

    int getMaximumEndNum();

    int getMinimumEndNum();

    void setValueEndNum(int value);

    int getValueEndNum();

    boolean isCheckedEndCheckbox();

    void setEnabledEndCheckbox(boolean value);

    boolean isEnabledEndCheckbox();

    void setTextEndCheckbox(String value);

    void removeAllItemsDenominatorCombobox();

    void addItemDenominatorCombobox(String value);

    void setSelectedIndexDenominatorCombobox(int value);

    int getSelectedIndexDenominatorCombobox();

    int getMaximumNumeratorNum();

    int getMinimumNumeratorNum();

    void setValueNumeratorNum(int value);

    int getValueNumeratorNum();
}
;

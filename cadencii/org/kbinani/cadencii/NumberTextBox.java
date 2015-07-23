/*
 * NumberTextBox.cs
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

import org.kbinani.*;

import org.kbinani.windows.forms.*;

import java.awt.*;

import javax.swing.event.DocumentEvent;


public class NumberTextBox extends BTextBox {
    private ValueType m_value_type = ValueType.Double;
    private Color m_textcolor_normal = Color.black;
    private Color m_textcolor_invalid = Color.white;
    private Color m_backcolor_normal = Color.white;
    private Color m_backcolor_invalid = new Color(240, 128, 128);

    public ValueType getType() {
        return m_value_type;
    }

    public void setType(ValueType value) {
        m_value_type = value;
    }

    public void update(DocumentEvent e) {
        super.updates(e);
        validateText();
    }

    private void validateText() {
        boolean valid = false;
        String text = getText();

        if (m_value_type == ValueType.Double) {
            double dou;

            try {
                dou = str.tof(text);
                valid = true;
            } catch (Exception ex) {
                valid = false;
            }
        } else if (m_value_type == ValueType.Float) {
            float flo;

            try {
                flo = (float) str.tof(text);
                valid = true;
            } catch (Exception ex) {
                valid = false;
            }
        } else if (m_value_type == ValueType.Integer) {
            int inte;

            try {
                inte = str.toi(text);
                valid = true;
            } catch (Exception ex) {
                valid = false;
            }
        }

        if (valid) {
            setForeground(m_textcolor_normal);
            setBackground(m_backcolor_normal);
        } else {
            setForeground(m_textcolor_invalid);
            setBackground(m_backcolor_invalid);
        }
    }
    public enum ValueType {Double,
        Float,
        Integer;
    }
}

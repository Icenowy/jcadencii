/*
 * NoteNumberPropertyConverter.cs
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

import org.kbinani.componentmodel.*;

import org.kbinani.vsq.*;


public class NoteNumberPropertyConverter extends TypeConverter<NoteNumberProperty> {
    public String convertTo(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof NoteNumberProperty) {
            NoteNumberProperty nnp = (NoteNumberProperty) value;
            String ret = getNoteString(nnp.noteNumber);

            return ret;
        } else {
            return "";
        }
    }

    public NoteNumberProperty convertFrom(String value) {
        NoteNumberProperty obj = new NoteNumberProperty();
        obj.noteNumber = NoteNumberPropertyConverter.parse(value);

        return obj;
    }

    private static String getNoteString(int note) {
        String[] jp = new String[] {
                "ハ", "嬰ハ", "ニ", "変ホ", "ホ", "ヘ", "嬰へ", "ト", "嬰ト", "イ", "変ロ", "ロ"
            };
        String[] jpfixed = new String[] {
                "ド", "ド#", "レ", "ミb", "ミ", "ファ", "ファ#", "ソ", "ソ#", "ラ", "シb",
                "シ",
            };
        String[] de = {
                "C", "Cis", "D", "Es", "E", "F", "Fis", "G", "Gis", "A", "Hes",
                "H"
            };

        if (AppManager.editorConfig != null) {
            int odd = note % 12;
            int order = ((note - odd) / 12) - 2;
            NoteNumberExpressionType exp_type = AppManager.editorConfig.PropertyWindowStatus.LastUsedNoteNumberExpression;

            if (exp_type == NoteNumberExpressionType.Numeric) {
                return note + "";
            } else if (exp_type == NoteNumberExpressionType.International) {
                return VsqNote.getNoteString(note);
            } else if (exp_type == NoteNumberExpressionType.Japanese) {
                return jp[odd] + order;
            } else if (exp_type == NoteNumberExpressionType.JapaneseFixedDo) {
                return jpfixed[odd] + order;
            } else if (exp_type == NoteNumberExpressionType.Deutsche) {
                return de[odd] + order;
            }
        } else {
            return VsqNote.getNoteString(note);
        }

        return "";
    }

    public static int parse(String value) {
        if (value.equals("")) {
            return 60;
        }

        value = value.toUpperCase();

        try {
            int draft_note_number = str.toi(value);

            if (AppManager.editorConfig != null) {
                AppManager.editorConfig.PropertyWindowStatus.LastUsedNoteNumberExpression = NoteNumberExpressionType.Numeric;
            }

            return draft_note_number;
        } catch (Exception ex) {
        }

        int scale = 3;
        int offset = 0;
        boolean doubled = false;
        int odd = 0;
        boolean first = true;
        NoteNumberExpressionType exp_type = NoteNumberExpressionType.International;

        while (true) {
            int trim = 1;

            if (value.startsWith("AS")) {
                offset = -1;
                odd = 9;
                trim = 2;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("ASAS") || value.startsWith("ASES")) {
                offset = -1;
                doubled = true;
                odd = 9;
                trim = 4;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("ISIS")) {
                offset = 1;
                doubled = true;
                trim = 4;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("IS")) {
                offset = 1;
                trim = 2;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("ESES")) {
                if (first) {
                    odd = 4;
                }

                offset = -1;
                doubled = true;
                trim = 4;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("ES")) {
                if (first) {
                    offset = -1;
                    odd = 4;
                } else {
                    offset = -1;
                }

                trim = 2;
                exp_type = NoteNumberExpressionType.Deutsche;
            } else if (value.startsWith("嬰")) {
                offset = 1;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("変")) {
                offset = -1;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("重")) {
                doubled = true;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("C")) {
                odd = 0;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("ド") || value.startsWith("ど")) {
                odd = 0;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("は") || value.startsWith("ハ") ||
                    value.startsWith("ﾊ")) {
                odd = 0;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("ﾄﾞ")) {
                odd = 0;
                trim = 2;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("D")) {
                odd = 2;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("レ") || value.startsWith("れ") ||
                    value.startsWith("ﾚ")) {
                odd = 2;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("に") || value.startsWith("ニ") ||
                    value.startsWith("ﾆ")) {
                odd = 2;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("E")) {
                odd = 4;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("ミ") || value.startsWith("み") ||
                    value.startsWith("ﾐ")) {
                odd = 4;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("ほ") || value.startsWith("ホ") ||
                    value.startsWith("ﾎ")) {
                odd = 4;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("F")) {
                odd = 5;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("ヘ") || value.startsWith("へ") ||
                    value.startsWith("ﾍ")) {
                odd = 5;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("ファ") || value.startsWith("ふぁ") ||
                    value.startsWith("ﾌｧ")) {
                odd = 5;
                trim = 2;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("G")) {
                odd = 7;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("ソ") || value.startsWith("そ") ||
                    value.startsWith("ｿ")) {
                odd = 7;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("と") || value.startsWith("ト") ||
                    value.startsWith("ﾄ")) {
                odd = 7;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("A")) {
                odd = 9;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("ラ") || value.startsWith("ら") ||
                    value.startsWith("ﾗ")) {
                odd = 9;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("い") || value.startsWith("イ") ||
                    value.startsWith("ｲ")) {
                odd = 9;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("H")) {
                odd = 11;
                exp_type = NoteNumberExpressionType.International;
            } else if (value.startsWith("シ") || value.startsWith("し") ||
                    value.startsWith("ｼ")) {
                odd = 11;
                exp_type = NoteNumberExpressionType.JapaneseFixedDo;
            } else if (value.startsWith("ろ") || value.startsWith("ロ") ||
                    value.startsWith("ﾛ")) {
                odd = 11;
                exp_type = NoteNumberExpressionType.Japanese;
            } else if (value.startsWith("B")) {
                if (first) {
                    odd = 11;
                    exp_type = NoteNumberExpressionType.International;
                } else {
                    offset = -1;
                }
            } else if (value.startsWith("#") || value.startsWith("♯") ||
                    value.startsWith("＃")) {
                offset = 1;
            } else if (value.startsWith("♭")) {
                offset = -1;
            }

            first = false;

            int len = str.length(value);

            if (len == trim) {
                break;
            }

            value = str.sub(value, trim);

            int draft_scale;

            try {
                draft_scale = str.toi(value);
                scale = draft_scale;

                break;
            } catch (Exception ex) {
            }
        }

        if (AppManager.editorConfig != null) {
            if ((exp_type == NoteNumberExpressionType.International) &&
                    (AppManager.editorConfig.PropertyWindowStatus.LastUsedNoteNumberExpression == NoteNumberExpressionType.Deutsche)) {
                // do nothing
            } else {
                AppManager.editorConfig.PropertyWindowStatus.LastUsedNoteNumberExpression = exp_type;
            }
        }

        return (12 * scale) + (2 * 12) + odd + (offset * (doubled ? 2 : 1));
    }
}

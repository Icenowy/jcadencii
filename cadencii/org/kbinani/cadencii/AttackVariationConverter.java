/*
 * AttackVariationConverter.cs
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

import java.io.*;

import java.util.*;


public class AttackVariationConverter extends TypeConverter {
    @Override
    public String convertTo(Object value) {
        if (value instanceof AttackVariation) {
            return ((AttackVariation) value).mDescription;
        } else {
            return super.convertTo(value);
        }
    }

    @Override
    public AttackVariation convertFrom(String value) {
        if (value.equals(new AttackVariation().mDescription)) {
            return new AttackVariation();
        } else {
            SynthesizerType type = SynthesizerType.VOCALOID2;
            VsqFileEx vsq = AppManager.getVsqFile();

            if (vsq != null) {
                RendererKind kind = VsqFileEx.getTrackRendererKind(vsq.Track.get(
                            AppManager.getSelected()));

                if (kind == RendererKind.VOCALOID1) {
                    type = SynthesizerType.VOCALOID1;
                }

                String svalue = (String) value;

                for (Iterator<NoteHeadHandle> itr = VocaloSysUtil.attackConfigIterator(
                            type); itr.hasNext();) {
                    NoteHeadHandle aconfig = itr.next();
                    String display_string = aconfig.getDisplayString();

                    if (svalue.equals(display_string)) {
                        return new AttackVariation(display_string);
                    }
                }
            }

            return new AttackVariation();
        }
    }

    @Override
    public Vector<Object> getStandardValues() {
        SynthesizerType type = SynthesizerType.VOCALOID2;
        VsqFileEx vsq = AppManager.getVsqFile();

        if (vsq != null) {
            RendererKind kind = VsqFileEx.getTrackRendererKind(vsq.Track.get(
                        AppManager.getSelected()));

            if (kind == RendererKind.VOCALOID1) {
                type = SynthesizerType.VOCALOID1;
            }
        }

        Vector<Object> list = new Vector<Object>();
        list.add(new AttackVariation());

        for (Iterator<NoteHeadHandle> itr = VocaloSysUtil.attackConfigIterator(
                    type); itr.hasNext();) {
            NoteHeadHandle aconfig = itr.next();
            list.add(new AttackVariation(aconfig.getDisplayString()));
        }

        return list; //new StandardValuesCollection( list.toArray( new AttackVariation[] { } ) );
    }

    @Override
    public boolean isStandardValuesSupported() {
        return true;
    }
}

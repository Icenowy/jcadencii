/*
 * VibratoVariationConverter.cs
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
import org.kbinani.*;
import org.kbinani.componentmodel.*;
import org.kbinani.vsq.*;


    public class VibratoVariationConverter extends TypeConverter<VibratoVariation>
    {

        @Override
        public boolean isStandardValuesSupported()
        {
return true;
        }


        @Override
        public Vector<VibratoVariation> getStandardValues()
        {
// ビブラート種類の候補値を列挙
Vector<VibratoVariation> list = new Vector<VibratoVariation>();
list.add( new VibratoVariation( VibratoVariation.empty.description ) );

if ( AppManager.editorConfig.UseUserDefinedAutoVibratoType ) {
    // ユーザー定義の中から選ぶ場合
    int size = AppManager.editorConfig.AutoVibratoCustom.size();
    for ( int i = 0; i < size; i++ ) {
        VibratoHandle handle = AppManager.editorConfig.AutoVibratoCustom.get( i );
        list.add( new VibratoVariation( handle.getDisplayString() ) );
    }
} else {
    // VOCALOID1, VOCALOID2のシステム定義の中から選ぶ場合
    SynthesizerType type = SynthesizerType.VOCALOID2;
    VsqFileEx vsq = AppManager.getVsqFile();
    if ( vsq != null ) {
        RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( AppManager.getSelected() ) );
        if ( kind == RendererKind.VOCALOID1 ) {
            type = SynthesizerType.VOCALOID1;
        }
    }
    for ( Iterator<VibratoHandle> itr = VocaloSysUtil.vibratoConfigIterator( type ); itr.hasNext(); ) {
        VibratoHandle vconfig = itr.next();
        list.add( new VibratoVariation( vconfig.getDisplayString() ) );
    }
}
return list;
        }



        @Override
        public String convertTo( Object value )
        {
if( value == null ){
    return "";
}else{
    VibratoVariation vv = (VibratoVariation)value;
    return vv.description;
}
        }


        @Override
        public VibratoVariation convertFrom( String value )
        {
if ( value.equals( VibratoVariation.empty.description ) ) {
    return new VibratoVariation( VibratoVariation.empty.description );
} else {
    if ( AppManager.editorConfig.UseUserDefinedAutoVibratoType ) {
        int size = AppManager.editorConfig.AutoVibratoCustom.size();
        for ( int i = 0; i < size; i++ ) {
            String display_string = AppManager.editorConfig.AutoVibratoCustom.get( i ).getDisplayString();
            if ( value.equals( display_string ) ) {
                return new VibratoVariation( display_string );
            }
        }
    } else {
        SynthesizerType type = SynthesizerType.VOCALOID2;
        VsqFileEx vsq = AppManager.getVsqFile();
        if ( vsq != null ) {
            RendererKind kind = VsqFileEx.getTrackRendererKind( vsq.Track.get( AppManager.getSelected() ) );
            if ( kind == RendererKind.VOCALOID1 ) {
                type = SynthesizerType.VOCALOID1;
            }
            for ( Iterator<VibratoHandle> itr = VocaloSysUtil.vibratoConfigIterator( type ); itr.hasNext(); ) {
                VibratoHandle vconfig = itr.next();
                String display_string = vconfig.getDisplayString();
                if ( value.equals( display_string ) ) {
                    return new VibratoVariation( display_string );
                }
            }
        }
    }
    return new VibratoVariation( VibratoVariation.empty.description );
}
        }

    }


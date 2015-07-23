/*
 * VibratoVariation.cs
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

import org.kbinani.componentmodel.*;


    @TypeConverterAnnotation( VibratoVariationConverter.class )
    public class VibratoVariation
    {
        public static final VibratoVariation empty = new VibratoVariation();

        public String description = "";

        private VibratoVariation()
        {
description = "-";
        }

        public VibratoVariation( String description )
        {
this.description = description;
        }

        public boolean equals( Object obj )
        {
if ( obj != null && obj instanceof VibratoVariation ) {
    return ((VibratoVariation)obj).description.equals( description );
} else {
    return super.equals( obj );
}
        }



        public Object clone()
        {
return new VibratoVariation( this.description );
        }


    }


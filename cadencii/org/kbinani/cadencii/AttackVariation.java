/*
 * AttackVariation.cs
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


    @TypeConverterAnnotation( AttackVariationConverter.class )
    public class AttackVariation
    {
        public String mDescription = "";

        public AttackVariation() {
mDescription = "-";
        }

        public AttackVariation( String description ) {
this.mDescription = description;
        }

        public boolean equals( Object obj ) {
if ( obj != null && obj instanceof AttackVariation ) {
    return ((AttackVariation)obj).mDescription.equals( mDescription );
} else {
    return super.equals( obj );
}
        }



    }


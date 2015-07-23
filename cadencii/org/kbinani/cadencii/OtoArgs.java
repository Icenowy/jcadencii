/*
 * OtoArgs.cs
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

    /// <summary>
    /// 原音設定の引数．
    /// </summary>
    public class OtoArgs {
        public String fileName;
        public String Alias;
        public float msOffset;
        public float msConsonant;
        public float msBlank;
        public float msPreUtterance;
        public float msOverlap;

        public boolean equals( OtoArgs obj ) {
if ( obj == null ) {
    return false;
}
if ( this.fileName != null && this.fileName.equals( obj.fileName ) &&
     this.Alias != null && this.Alias.equals( obj.Alias ) &&
     this.msOffset == obj.msOffset &&
     this.msConsonant == obj.msConsonant &&
     this.msBlank == obj.msBlank &&
     this.msPreUtterance == obj.msPreUtterance &&
     this.msOverlap == obj.msOverlap ) {
    return true;
} else {
    return false;
}
        }
    }


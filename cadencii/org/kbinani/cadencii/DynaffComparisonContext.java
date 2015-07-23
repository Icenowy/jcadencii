/*
 * DynaffComparisonContext.cs
 * Copyright © 2010-2011 kbinani
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
import org.kbinani.vsq.*;

    /// <summary>
    /// 強弱記号を比較するための，比較用コンテキスト
    /// </summary>
    public class DynaffComparisonContext implements IComparisonContext {
        VsqTrack track1 = null;
        VsqTrack track2 = null;
        Iterator<Integer> it1 = null;
        Iterator<Integer> it2 = null;

        public DynaffComparisonContext( VsqTrack track1, VsqTrack track2 ) {
this.track1 = track1;
this.track2 = track2;
it1 = this.track1.indexIterator( IndexIteratorKind.DYNAFF );
it2 = this.track2.indexIterator( IndexIteratorKind.DYNAFF );
        }

        public int getNextIndex1() {
return it1.next();
        }

        public int getNextIndex2() {
return it2.next();
        }

        public Object getElementAt1( int index ) {
return track1.getEvent( index );
        }

        public Object getElementAt2( int index ) {
return track2.getEvent( index );
        }

        public boolean hasNext1() {
return it1.hasNext();
        }

        public boolean hasNext2() {
return it2.hasNext();
        }

        public int getClockFrom( Object obj ) {
if ( obj == null ) {
    return 0;
}
if ( obj instanceof VsqEvent ) {
    return ((VsqEvent)obj).Clock;
}
return 0;
        }

        public boolean equals( Object obj1, Object obj2 ) {
if ( obj1 == null || obj2 == null ) {
    return false;
}
if ( !(obj1 instanceof VsqEvent) || !(obj2 instanceof VsqEvent) ) {
    return false;
}
VsqEvent item1 = (VsqEvent)obj1;
VsqEvent item2 = (VsqEvent)obj2;
return (item1.ID.IconDynamicsHandle.IconID.equals( item2.ID.IconDynamicsHandle.IconID ));
        }
    }


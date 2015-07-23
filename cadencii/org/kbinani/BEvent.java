/*
 * BEvent.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.
 *
 * org.kbinani is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

public class BEvent<T extends BEventHandler>{
    private Vector<T> mDelegates;

    public BEvent(){
        mDelegates = new Vector<T>();
    }

    public int size()
    {
        return mDelegates.size();
    }
    
    public void add( T delegate ){
        synchronized( mDelegates ){
            if( delegate == null ){
                return;
            }
            mDelegates.add( delegate );
        }
    }

    public void remove( T delegate ){
        synchronized( mDelegates ){
            int count = mDelegates.size();
            for( int i = 0; i < count; i++ ){
                T item = mDelegates.get( i );
                if( delegate.equals( item ) ){
                    mDelegates.remove( i );
                    break;
                }
            }
        }
    }
    
    public void raise( Object... args ) 
        throws IllegalAccessException, InvocationTargetException
    {
        int count = mDelegates.size();
        for( int i = 0; i < count; i++ ){
            mDelegates.get( i ).invoke( args );
        }
    }
}

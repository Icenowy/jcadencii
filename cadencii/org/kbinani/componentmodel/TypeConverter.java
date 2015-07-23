/*
 * TypeConverter.cs
 * Copyright © 2011 kbinani
 *
 * This file is part of org.kbinani.componentmodel.
 *
 * org.kbinani.componentmodel is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.cadencii is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.componentmodel;

import java.util.Vector;

/**
 * オブジェクトの実際の型と，画面表示に使用する型との相互変換機能を提供します
 * @author kbinani
 *
 * @param <T> オブジェクトの実際の型
 */
public class TypeConverter<T> {
    public String convertTo( Object obj ){
        return "" + obj;
    }
    
    public T convertFrom( String obj ){
        return null;
    }

    public boolean isStandardValuesSupported()
    {
        return false;
    }

    public Vector<T> getStandardValues()
    {
        return null;
    }
}

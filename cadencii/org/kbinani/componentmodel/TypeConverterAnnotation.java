/*
 * TypeConverterAnnotation.cs
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface TypeConverterAnnotation
{
    Class<?> value();
}

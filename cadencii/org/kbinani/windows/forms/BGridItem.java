/*
 * BGridItem.cs
 * Copyright © 2011 kbinani
 *
 * This file is part of org.kbinani.windows.forms.
 *
 * org.kbinani.windows.forms is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.windows.forms is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.windows.forms;

import java.awt.Component;
import java.util.Vector;
import org.kbinani.componentmodel.TypeConverter;
import org.kbinani.xml.XmlMember;

public class BGridItem
{
    public XmlMember member;
    public Vector<XmlMember> memberStack = new Vector<XmlMember>();
    public Component editor;
    public Vector<BGridItem> children = new Vector<BGridItem>();
    public TypeConverter<?> converter = null;
    public Component expandMark = null;

    public boolean isExpandable()
    {
        return (expandMark != null);
    }
    
    public boolean isExpanded()
    {
        if( expandMark == null ){
            return false;
        }else{
            return ((BPropertyGridExpandMark)expandMark).isExpanded();
        }
    }
}

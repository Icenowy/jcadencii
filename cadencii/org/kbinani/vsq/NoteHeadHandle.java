/*
 * NoteHeadHandle.cs
 * Copyright © 2009-2011 kbinani
 *
 * This file is part of org.kbinani.vsq.
 *
 * org.kbinani.vsq is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.vsq is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.vsq;

import java.io.*;

    public class NoteHeadHandle extends IconParameter implements Cloneable, Serializable
    {
        public int Index;
        public String IconID = "";
        public String IDS = "";
        public int Original;

        public NoteHeadHandle()
        {
        }

        public NoteHeadHandle( String aic_file, String ids, String icon_id, int index )
        {
 super( aic_file )
;
IDS = ids;
IconID = icon_id;
Index = index;
        }

        public String toString()
        {
return getDisplayString();
        }



        public int getDepth()
        {
return depth;
        }

        public void setDepth( int value )
        {
depth = value;
        }


        public int getDuration()
        {
return duration;
        }

        public void setDuration( int value )
        {
duration = value;
        }


        public String getCaption()
        {
return caption;
        }

        public void setCaption( String value )
        {
caption = value;
        }


        public int getLength()
        {
return length;
        }

        public void setLength( int value )
        {
length = value;
        }

        public String getDisplayString()
        {
return IDS + caption;
        }


        public Object clone()
        {
NoteHeadHandle result = new NoteHeadHandle();
result.Index = Index;
result.IconID = IconID;
result.IDS = IDS;
result.Original = Original;
result.setCaption( getCaption() );
result.setLength( getLength() );
result.setDuration( getDuration() );
result.setDepth( getDepth() );
return result;
        }

        public VsqHandle castToVsqHandle()
        {
VsqHandle ret = new VsqHandle();
ret.m_type = VsqHandleType.NoteHeadHandle;
ret.Index = Index;
ret.IconID = IconID;
ret.IDS = IDS;
ret.Original = Original;
ret.Caption = getCaption();
ret.setLength( getLength() );
ret.Duration = getDuration();
ret.Depth = getDepth();
return ret;
        }
    }


/*
 * IconDynamicsHandle.cs
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

    /// <summary>
    /// 強弱記号設定を表します。
    /// </summary>
    public class IconDynamicsHandle extends IconParameter implements Cloneable, Serializable
    {
        /// <summary>
        /// 強弱記号の場合の、IconIDの最初の5文字。
        /// </summary>
        public static final String ICONID_HEAD_DYNAFF = "$0501";
        /// <summary>
        /// クレッシェンドの場合の、IconIDの最初の5文字。
        /// </summary>
        public static final String ICONID_HEAD_CRESCEND = "$0502";
        /// <summary>
        /// デクレッシェンドの場合の、IconIDの最初の5文字。
        /// </summary>
        public static final String ICONID_HEAD_DECRESCEND = "$0503";

        /// <summary>
        /// この強弱記号設定を一意に識別するためのIDです。
        /// </summary>
        public String IconID = "";
        /// <summary>
        /// ユーザ・フレンドリー名です。
        /// このフィールドの値は、他の強弱記号設定のユーザ・フレンドリー名と重複する場合があります。
        /// </summary>
        public String IDS = "";
        /// <summary>
        /// この強弱記号設定が他の強弱記号設定から派生したものである場合、派生元を特定するための番号です。
        /// </summary>
        public int Original;

        /// <summary>
        /// デフォルトの設定で、新しい強弱記号設定のインスタンスを初期化します。
        /// </summary>
        public IconDynamicsHandle()
        {
 super()
;
        }

        /// <summary>
        /// 指定されたパラメータを使って、新しい強弱記号設定のインスタンスを初期化します。
        /// </summary>
        /// <param name="aic_file">初期化に使用する設定ファイルのパス</param>
        /// <param name="ids">フィールドIDSの初期値</param>
        /// <param name="icon_id">フィールドIconIDの初期値</param>
        /// <param name="index">フィールドOriginalの初期値</param>
        public IconDynamicsHandle( String aic_file, String ids, String icon_id, int index )
        {
 super( aic_file )
;
IDS = ids;
IconID = icon_id;
Original = index;
        }

        /// <summary>
        /// このハンドルが強弱記号を表すものかどうかを表すブール値を取得します。
        /// </summary>
        /// <returns></returns>
        public boolean isDynaffType()
        {
if ( IconID != null ) {
    return IconID.startsWith( ICONID_HEAD_DYNAFF );
} else {
    return false;
}
        }

        /// <summary>
        /// このハンドルがクレッシェンドを表すものかどうかを表すブール値を取得します。
        /// </summary>
        /// <returns></returns>
        public boolean isCrescendType()
        {
if ( IconID != null ) {
    return IconID.startsWith( ICONID_HEAD_CRESCEND );
} else {
    return false;
}
        }

        /// <summary>
        /// このハンドルがデクレッシェンドを表すものかどうかを表すブール値を取得します。
        /// </summary>
        /// <returns></returns>
        public boolean isDecrescendType()
        {
if ( IconID != null ) {
    return IconID.startsWith( ICONID_HEAD_DECRESCEND );
} else {
    return false;
}
        }

        /// <summary>
        /// このインスタンスのコピーを作成します。
        /// </summary>
        /// <returns></returns>

        /// <summary>
        /// このインスタンスのコピーを作成します。
        /// </summary>
        /// <returns></returns>
        public Object clone()
        {
IconDynamicsHandle ret = new IconDynamicsHandle();
ret.IconID = IconID;
ret.IDS = IDS;
ret.Original = Original;
ret.setCaption( getCaption() );
ret.setStartDyn( getStartDyn() );
ret.setEndDyn( getEndDyn() );
if ( dynBP != null ) {
    ret.setDynBP( (VibratoBPList)dynBP.clone() );
}
ret.setLength( getLength() );
return ret;
        }

        /// <summary>
        /// この強弱記号設定のインスタンスを、VsqHandleに型キャストします。
        /// </summary>
        /// <returns></returns>
        public VsqHandle castToVsqHandle()
        {
VsqHandle ret = new VsqHandle();
ret.m_type = VsqHandleType.DynamicsHandle;
ret.IconID = IconID;
ret.IDS = IDS;
ret.Original = Original;
ret.Caption = getCaption();
ret.DynBP = getDynBP();
ret.EndDyn = getEndDyn();
ret.setLength( getLength() );
ret.StartDyn = getStartDyn();
return ret;
        }

        /// <summary>
        /// キャプションを取得します。
        /// </summary>
        /// <returns></returns>
        public String getCaption()
        {
return caption;
        }

        /// <summary>
        /// キャプションを設定します。
        /// </summary>
        /// <param name="value"></param>
        public void setCaption( String value )
        {
caption = value;
        }


        /// <summary>
        /// ゲートタイム長さを取得します。
        /// </summary>
        /// <returns></returns>
        public int getLength()
        {
return length;
        }

        /// <summary>
        /// ゲートタイム長さを設定します。
        /// </summary>
        /// <param name="value"></param>
        public void setLength( int value )
        {
length = value;
        }


        /// <summary>
        /// DYNの開始値を取得します。
        /// </summary>
        /// <returns></returns>
        public int getStartDyn()
        {
return startDyn;
        }

        /// <summary>
        /// DYNの開始値を設定します。
        /// </summary>
        /// <param name="value"></param>
        public void setStartDyn( int value )
        {
startDyn = value;
        }


        /// <summary>
        /// DYNの終了値を取得します。
        /// </summary>
        /// <returns></returns>
        public int getEndDyn()
        {
return endDyn;
        }

        /// <summary>
        /// DYNの終了値を設定します。
        /// </summary>
        /// <param name="value"></param>
        public void setEndDyn( int value )
        {
endDyn = value;
        }


        /// <summary>
        /// DYNカーブを表すリストを取得します。
        /// </summary>
        /// <returns></returns>
        public VibratoBPList getDynBP()
        {
return dynBP;
        }

        /// <summary>
        /// DYNカーブを表すリストを設定します。
        /// </summary>
        /// <param name="value"></param>
        public void setDynBP( VibratoBPList value )
        {
dynBP = value;
        }


    }


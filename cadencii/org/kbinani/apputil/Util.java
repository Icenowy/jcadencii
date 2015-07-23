/*
 * Misc.cs
 * Copyright © 2008-2011 kbinani
 *
 * This file is part of org.kbinani.apputil.
 *
 * org.kbinani.apputil is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.apputil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.apputil;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import org.kbinani.*;

    public class Util{
        public static final String PANGRAM = "cozy lummox gives smart squid who asks for job pen. 01234567890 THE QUICK BROWN FOX JUMPED OVER THE LAZY DOGS.";
        /// <summary>
        /// このクラスのメソッド'applyFontRecurse', 'applyToolStripFontRecurse', 'applyContextMenuFontRecurse'の呼び出しを有効とするかどうか。
        /// デフォルトではtrue
        /// </summary>
        public static boolean isApplyFontRecurseEnabled = true;


        public static void applyContextMenuFontRecurse( MenuElement item, Font font ){
if ( !isApplyFontRecurseEnabled ) {
    return;
}
applyToolStripFontRecurse( item, font );
        }

        public static void applyToolStripFontRecurse( MenuElement item, Font font ){
if ( !isApplyFontRecurseEnabled ) {
    return;
}
if( item instanceof Component ){
    ((Component)item).setFont( font );
}
for( MenuElement element : item.getSubElements() ){
    applyToolStripFontRecurse( element, font );
}
        }

        /// <summary>
        /// 指定したフォントを描画するとき、描画指定したy座標と、描かれる文字の中心線のズレを調べます
        /// </summary>
        /// <param name="font"></param>
        /// <returns></returns>
        public static int getStringDrawOffset( java.awt.Font font )
        {
int ret = 0;
java.awt.Dimension size = measureString( PANGRAM, font );
if ( size.height <= 0 )
{
    return 0;
}
java.awt.image.BufferedImage b = null;
java.awt.Graphics2D g = null;
java.awt.image.BufferedImage b2 = null;
try
{
    int string_desty = size.height * 2; // 文字列が書き込まれるy座標
    int w = size.width * 4;
    int h = size.height * 4;
    b = new java.awt.image.BufferedImage( w, h, java.awt.image.BufferedImage.TYPE_INT_BGR );
    g = b.createGraphics();
    g.setColor( java.awt.Color.white );
    g.fillRect( 0, 0, w, h );
    g.setFont( font );
    g.setColor( java.awt.Color.black );
    g.drawString( PANGRAM, size.width, string_desty );

    b2 = b;
    // 上端に最初に現れる色つきピクセルを探す
    int firsty = 0;
    boolean found = false;
    for ( int y = 0; y < h; y++ )
    {
        for ( int x = 0; x < w; x++ )
        {
            int ic = b2.getRGB( x, y );
            Color c = new Color( ic );
            if ( c.getRed() != 255 || c.getGreen() != 255 || c.getBlue() != 255 )
            {
                found = true;
                firsty = y;
                break;
            }
        }
        if ( found )
        {
            break;
        }
    }

    // 下端
    int endy = h - 1;
    found = false;
    for ( int y = h - 1; y >= 0; y-- )
    {
        for ( int x = 0; x < w; x++ )
        {
            int ic = b2.getRGB( x, y );
            Color c = new Color( ic );
            if ( c.getRed() != 255 || c.getGreen() != 255 || c.getBlue() != 255 )
            {
                found = true;
                endy = y;
                break;
            }
        }
        if ( found )
        {
            break;
        }
    }

    int center = (firsty + endy) / 2;
    ret = center - string_desty;
}
catch ( Exception ex )
{
    serr.println( "Util#getStringDrawOffset; ex=" + ex );
}
finally
{
}
return ret;
        }

        /// <summary>
        /// 指定した言語コードの表す言語が、右から左へ記述する言語かどうかを調べます
        /// </summary>
        /// <param name="language_code"></param>
        /// <returns></returns>
        public static boolean isRightToLeftLanguage( String language_code )
        {
language_code = language_code.toLowerCase();
if ( language_code.equals( "ar" ) ||
     language_code.equals( "he" ) ||
     language_code.equals( "iw" ) ||
     language_code.equals( "fa" ) ||
     language_code.equals( "ur" ) )
{
    return true;
}
else
{
    return false;
}
        }








        /// <summary>
        /// 指定された文字列を指定されたフォントで描画したときのサイズを計測します。
        /// </summary>
        /// <param name="text"></param>
        /// <param name="font"></param>
        /// <returns></returns>
        public static Dimension measureString( String text, Font font ){
BufferedImage dumy = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_BGR );
Graphics2D g = dumy.createGraphics();
g.setFont( font );
FontMetrics fm = g.getFontMetrics();
Dimension ret = new Dimension( fm.stringWidth( text ), fm.getHeight() );
g = null;
dumy = null;
return ret;
        }

        /// <summary>
        /// 指定したコントロールと、その子コントロールのフォントを再帰的に変更します
        /// </summary>
        /// <param name="c"></param>
        /// <param name="font"></param>
        public static void applyFontRecurse( Component c, Font font ){
if ( !isApplyFontRecurseEnabled )
{
    return;
}
c.setFont( font );
if( c instanceof Container ){
    Container container = (Container)c;
    int count = container.getComponentCount();
    for( int i = 0; i < count; i++ ){
        Component component = container.getComponent( i );
        applyFontRecurse( component, font );
    }
}
        }

    }


/*
 * FormWordDictionaryUi.cs
 * Copyright © 2011 kbinani
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


public interface FormWordDictionaryUi extends UiBase
{
    /// <summary>
    /// ウィンドウのタイトル文字列を設定します
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void setTitle( String value );

    /// <summary>
    /// ダイアログの戻り値を設定します．
    /// </summary>
    /// <param name="value">ダイアログの戻り値を「キャンセル」にする場合はfalseを，それ以外はtreuを設定します．</param>
    void setDialogResult( boolean value );

    /// <summary>
    /// TODO: comment
    /// </summary>
    /// <param name="width"></param>
    /// <param name="height"></param>
    void setSize( int width, int height );

    /// <summary>
    /// ウィンドウの幅を取得します
    /// </summary>
    /// <returns>ウィンドウの幅(単位はピクセル)</returns>
    int getWidth();

    /// <summary>
    /// ウィンドウの高さを取得します
    /// </summary>
    /// <returns>ウィンドウの高さ(単位はピクセル)</returns>
    int getHeight();

    /// <summary>
    /// ウィンドウの位置を設定します
    /// </summary>
    /// <param name="x">ウィンドウのx座標</param>
    /// <param name="y">ウィンドウのy座標</param>
    void setLocation( int x, int y );

    /// <summary>
    /// ウィンドウを閉じます
    /// </summary>
    void close();

    /// <summary>
    /// TODO: comment
    /// </summary>
    /// <returns></returns>
    int listDictionariesGetSelectedRow();

    /// <summary>
    /// リストに登録されたアイテムの個数を取得します
    /// </summary>
    /// <returns>アイテムの個数</returns>
    int listDictionariesGetItemCountRow();

    /// <summary>
    /// TODO: comment
    /// </summary>
    void listDictionariesClear();

    /// <summary>
    /// TODO: comment
    /// </summary>
    /// <param name="row"></param>
    /// <param name="column"></param>
    /// <returns></returns>
    String listDictionariesGetItemAt( int row );

    /// <summary>
    /// TODO: 
    /// </summary>
    /// <param name="row"></param>
    /// <returns></returns>
    boolean listDictionariesIsRowChecked( int row );

    /// <summary>
    /// TODO:
    /// </summary>
    /// <param name="row"></param>
    /// <param name="column"></param>
    /// <param name="value"></param>
    void listDictionariesSetItemAt( int row, String value );

    /// <summary>
    /// TODO:
    /// </summary>
    /// <param name="row"></param>
    /// <param name="value"></param>
    void listDictionariesSetRowChecked( int row, boolean isChecked );

    /// <summary>
    /// TODO:
    /// </summary>
    /// <param name="row"></param>
    void listDictionariesSetSelectedRow( int row );

    /// <summary>
    /// TODO: comment
    /// </summary>
    void listDictionariesClearSelection();

    /// <summary>
    /// TODO: comment
    /// </summary>
    /// <param name="value"></param>
    /// <param name="selected"></param>
    void listDictionariesAddRow( String value, boolean isChecked );

    /// <summary>
    /// 「利用可能な辞書」という意味の説明文の文字列を設定します．
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void labelAvailableDictionariesSetText( String value );

    /// <summary>
    /// OKボタンの表示文字列を設定します．
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void buttonOkSetText( String value );

    /// <summary>
    /// Cancelボタンの表示文字列を設定します．
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void buttonCancelSetText( String value );

    /// <summary>
    /// Upボタンの表示文字列を設定します．
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void buttonUpSetText( String value );

    /// <summary>
    /// Downボタンの表示文字列を設定します．
    /// </summary>
    /// <param name="value">設定する文字列</param>
    void buttonDownSetText( String value );
};


/*
 * ProgressBarWithLabel.cs
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

import org.kbinani.*;


    /// <summary>
    /// 実行内容を表示するためのラベル付きのプログレスバー
    /// </summary>
    public class ProgressBarWithLabel
    {
        private ProgressBarWithLabelUi ptrUi = null;
        private Object ptrArgument = null;


        public void setWidth( int value )
        {
if ( ptrUi == null ) return;
ptrUi.setWidth( value );
        }

        /// <summary>
        /// UIのセットアップを行います
        /// </summary>
        /// <param name="ui"></param>
        public void setupUi( ProgressBarWithLabelUi ui )
        {
if ( ptrUi == null ) {
    ptrUi = ui;
}
        }

        /// <summary>
        /// セットアップされているUIを取得します
        /// </summary>
        /// <returns></returns>
        public ProgressBarWithLabelUi getUi()
        {
return ptrUi;
        }

        /// <summary>
        /// ラベルのテキストを設定します
        /// </summary>
        /// <param name="value">設定するテキスト</param>
        public void setText( String value )
        {
if ( ptrUi == null ) {
    return;
}
ptrUi.setText( value );
        }

        /// <summary>
        /// ラベルのテキストを取得します
        /// </summary>
        /// <returns>ラベルのテキスト</returns>
        public String getText()
        {
if ( ptrUi == null ) {
    return "";
}
return ptrUi.getText();
        }

        /// <summary>
        /// 進捗状況をパーセントで設定します
        /// </summary>
        /// <param name="value">設定する値</param>
        public void setProgress( int value )
        {
if ( ptrUi == null ) return;
ptrUi.setProgress( value );
        }

        /// <summary>
        /// 進捗状況を表すパーセンテージを取得します
        /// </summary>
        /// <returns>進捗状況を表すパーセント値</returns>
        public int getProgress()
        {
if ( ptrUi == null ) {
    return 0;
}
return ptrUi.getProgress();
        }
    };


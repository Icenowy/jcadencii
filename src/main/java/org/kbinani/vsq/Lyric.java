/*
 * Lyric.cs
 * Copyright © 2008-2011 kbinani
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

import org.kbinani.*;

import org.kbinani.xml.*;

import java.io.*;

import java.util.*;


/// <summary>
/// VsqHandleに格納される歌詞の情報を扱うクラス。
/// </summary>
public class Lyric implements Serializable {
    /// <summary>
    /// この歌詞のフレーズ
    /// </summary>
    public String Phrase;
    private Vector<String> mPhoneticSymbols;
    public float UnknownFloat;
    private Vector<Integer> mConsonantAdjustments;
    public boolean PhoneticSymbolProtected;

    public Lyric() {
        UnknownFloat = 1.0f;
        mConsonantAdjustments = new Vector<Integer>();
        mPhoneticSymbols = new Vector<String>();
    }

    /// <summary>
    /// 歌詞、発音記号を指定したコンストラクタ
    /// </summary>
    /// <param name="phrase">歌詞</param>
    /// <param name="phonetic_symbol">発音記号</param>
    public Lyric(String phrase, String phonetic_symbol) {
        Phrase = phrase;
        setPhoneticSymbol(phonetic_symbol);
        UnknownFloat = 1.0f;
    }

    /// <summary>
    /// 文字列(ex."a","a",0.0000,0.0)からのコンストラクタ
    /// </summary>
    /// <param name="line"></param>
    public Lyric(String line) {
        int len = PortUtil.getStringLength(line);

        if (len == 0) {
            Phrase = "a";
            setPhoneticSymbol("a");
            UnknownFloat = 1.0f;
            PhoneticSymbolProtected = false;
            setConsonantAdjustment("0");

            return;
        }

        int indx = -1;
        int dquote_count = 0;
        String work = "";
        String consonant_adjustment = "";

        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);

            if (c == ',') {
                if ((dquote_count % 2) == 0) {
                    // ,の左側に偶数個の"がある場合→,は区切り文字
                    indx++;

                    if (indx == 0) {
                        // Phrase
                        work = work.replace("\"\"", "\""); // "は""として保存される

                        if (work.startsWith("\"") && work.endsWith("\"")) {
                            int l = str.length(work);

                            if (l > 2) {
                                Phrase = str.sub(work, 1, l - 2);
                            } else {
                                Phrase = "a";
                            }
                        } else {
                            Phrase = work;
                        }

                        work = "";
                    } else if (indx == 1) {
                        // symbols
                        String symbols = "";

                        if (work.startsWith("\"") && work.endsWith("\"")) {
                            int l = PortUtil.getStringLength(work);

                            if (l > 2) {
                                symbols = str.sub(work, 1, l - 2);
                            } else {
                                symbols = "a";
                            }
                        } else {
                            symbols = work;
                        }

                        setPhoneticSymbol(symbols);
                        work = "";
                    } else if (indx == 2) {
                        // UnknownFloat
                        UnknownFloat = (float) str.tof(work);
                        work = "";
                    } else {
                        if ((indx - 3) < mPhoneticSymbols.size()) {
                            // consonant adjustment
                            if ((indx - 3) == 0) {
                                consonant_adjustment += work;
                            } else {
                                consonant_adjustment += ("," + work);
                            }
                        } else {
                            // protected
                            PhoneticSymbolProtected = str.compare(work, "1");
                        }

                        work = "";
                    }
                } else {
                    // ,の左側に奇数個の"がある場合→,は歌詞等の一部
                    work += ("" + c);
                }
            } else {
                work += ("" + c);

                if (c == '"') {
                    dquote_count++;
                }
            }
        }

        setConsonantAdjustment(consonant_adjustment);
    }

    /// <summary>
    /// このクラスの指定した名前のプロパティをXMLシリアライズする際に使用する
    /// 要素名を取得します．
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public static String getXmlElementName(String name) {
        return name;
    }

    /// <summary>
    /// このオブジェクトのインスタンスと、指定されたアイテムが同じかどうかを調べます。
    /// 音声合成したときに影響のある範囲のフィールドしか比較されません。
    /// たとえば、PhoneticSymbolProtectedがthisとitemで違っていても、他が同一であればtrueが返る。
    /// </summary>
    /// <param name="item"></param>
    /// <returns></returns>
    public boolean equalsForSynth(Lyric item) {
        if (this.PhoneticSymbolProtected != item.PhoneticSymbolProtected) {
            return false;
        }

        if (!str.compare(this.getPhoneticSymbol(), item.getPhoneticSymbol())) {
            return false;
        }

        if (!str.compare(this.getConsonantAdjustment(),
                    item.getConsonantAdjustment())) {
            return false;
        }

        return true;
    }

    /// <summary>
    /// このオブジェクトのインスタンスと、指定されたオブジェクトが同じかどうかを調べます。
    /// </summary>
    /// <param name="item"></param>
    /// <returns></returns>
    public boolean equals(Lyric item) {
        if (!equalsForSynth(item)) {
            return false;
        }

        if (!str.compare(this.Phrase, item.Phrase)) {
            return false;
        }

        if (this.UnknownFloat != item.UnknownFloat) {
            return false;
        }

        return true;
    }

    /// <summary>
    /// Consonant Adjustmentの文字列形式を取得します。
    /// </summary>
    /// <returns></returns>
    public String getConsonantAdjustment() {
        String ret = "";
        Vector<Integer> arr = getConsonantAdjustmentList();
        int size = arr.size();

        for (int i = 0; i < size; i++) {
            int v = vec.get(arr, i);
            ret += (((i == 0) ? "" : " ") + v);
        }

        return ret;
    }

    /// <summary>
    /// Consonant Adjustmentを文字列形式で設定します。
    /// </summary>
    /// <param name="value"></param>
    public void setConsonantAdjustment(String value) {
        Vector<String> spl = new Vector<String>();
        Vector<String> tokenizer = new Vector<String>();
        vec.add(tokenizer, " ");
        vec.add(tokenizer, ",");
        str.split(value, spl, tokenizer, true);

        int size = vec.size(spl);
        Vector<Integer> arr = new Vector<Integer>();

        for (int i = 0; i < size; i++) {
            int v = 64;

            try {
                v = str.toi(vec.get(spl, i));
            } catch (Exception ex) {
                serr.println("Lyric#setCosonantAdjustment; ex=" + ex);
            }

            vec.add(arr, v);
        }

        setConsonantAdjustmentList(arr);
    }

    /// <summary>
    /// Consonant Adjustmentを、整数配列で取得します。
    /// </summary>
    /// <returns></returns>
    @XmlIgnore
    public Vector<Integer> getConsonantAdjustmentList() {
        if (mConsonantAdjustments != null) {
            return mConsonantAdjustments;
        }

        if (mPhoneticSymbols == null) {
            mConsonantAdjustments = new Vector<Integer>();

            return mConsonantAdjustments;
        }

        vec.clear(mConsonantAdjustments);

        for (int i = 0; i < mPhoneticSymbols.size(); i++) {
            int v = VsqPhoneticSymbol.isConsonant(vec.get(mPhoneticSymbols, i))
                ? 64 : 0;
            vec.add(mConsonantAdjustments, v);
        }

        return mConsonantAdjustments;
    }

    /// <summary>
    /// Consonant Adjustmentを、整数配列形式で設定します。
    /// </summary>
    /// <param name="value"></param>
    @XmlIgnore
    public void setConsonantAdjustmentList(Vector<Integer> value) {
        if (value == null) {
            return;
        }

        vec.clear(mConsonantAdjustments);

        for (int i = 0; i < value.size(); i++) {
            int v = vec.get(value, i);
            vec.add(mConsonantAdjustments, v);
        }
    }

    /// <summary>
    /// このオブジェクトの簡易コピーを取得します。
    /// </summary>
    /// <returns>このインスタンスの簡易コピー</returns>
    public Object clone() {
        Lyric result = new Lyric();
        result.Phrase = this.Phrase;
        result.mPhoneticSymbols = new Vector<String>();

        for (int i = 0; i < vec.size(mPhoneticSymbols); i++) {
            vec.add(result.mPhoneticSymbols, vec.get(mPhoneticSymbols, i));
        }

        result.UnknownFloat = this.UnknownFloat;

        if (mConsonantAdjustments != null) {
            result.mConsonantAdjustments = new Vector<Integer>();

            for (int i = 0; i < vec.size(mConsonantAdjustments); i++) {
                vec.add(result.mConsonantAdjustments,
                    vec.get(mConsonantAdjustments, i));
            }
        }

        result.PhoneticSymbolProtected = PhoneticSymbolProtected;

        return result;
    }

    /// <summary>
    /// この歌詞の発音記号を取得します。
    /// </summary>
    public String getPhoneticSymbol() {
        Vector<String> symbol = getPhoneticSymbolList();
        String ret = "";

        for (int i = 0; i < vec.size(symbol); i++) {
            ret += (((i == 0) ? "" : " ") + vec.get(symbol, i));
        }

        return ret;
    }

    /// <summary>
    /// この歌詞の発音記号を設定します。
    /// </summary>
    public void setPhoneticSymbol(String value) {
        String s = value.replace("  ", " ");

        // 古い発音記号を保持しておく
        Vector<String> old_symbol = null;

        if (mPhoneticSymbols != null) {
            int count = vec.size(mPhoneticSymbols);
            old_symbol = new Vector<String>();

            for (int i = 0; i < count; i++) {
                vec.add(old_symbol, vec.get(mPhoneticSymbols, i));
            }
        }

        // 古いconsonant adjustmentを保持しておく
        Vector<Integer> old_adjustment = null;

        if (mConsonantAdjustments != null) {
            old_adjustment = new Vector<Integer>();

            int count = vec.size(mConsonantAdjustments);

            for (int i = 0; i < count; i++) {
                vec.add(old_adjustment, vec.get(mConsonantAdjustments, i));
            }
        }

        String[] spl = PortUtil.splitString(s, new char[] { ' ' }, 16, true);

        if (mPhoneticSymbols == null) {
            mPhoneticSymbols = new Vector<String>();
        }

        int size = spl.length;
        vec.clear(mPhoneticSymbols);

        for (int i = 0; i < size; i++) {
            vec.add(mPhoneticSymbols, spl[i]);
        }

        for (int i = 0; i < vec.size(mPhoneticSymbols); i++) {
            vec.set(mPhoneticSymbols, i,
                vec.get(mPhoneticSymbols, i).replace("\\" + "\\", "\\"));
        }

        // consonant adjustmentを更新
        if ((mConsonantAdjustments == null) ||
                ((mConsonantAdjustments != null) &&
                (vec.size(mConsonantAdjustments) != vec.size(mPhoneticSymbols)))) {
            mConsonantAdjustments = new Vector<Integer>();

            for (int i = 0; i < vec.size(mPhoneticSymbols); i++) {
                vec.add(mConsonantAdjustments, 0);
            }
        }

        // 古い発音記号と同じなら、古い値を使う
        if (old_symbol != null) {
            for (int i = 0; i < vec.size(mPhoneticSymbols); i++) {
                if (i >= vec.size(old_symbol)) {
                    break;
                }

                String s0 = vec.get(mPhoneticSymbols, i);
                String s1 = vec.get(old_symbol, i);
                boolean use_old_value = ((old_symbol != null) &&
                    (i < vec.size(old_symbol))) && (str.compare(s0, s1)) &&
                    ((old_adjustment != null) &&
                    (i < vec.size(old_adjustment)));

                if (use_old_value) {
                    vec.set(mConsonantAdjustments, i,
                        VsqPhoneticSymbol.isConsonant(vec.get(
                                mPhoneticSymbols, i))
                        ? vec.get(old_adjustment, i) : 0);
                } else {
                    vec.set(mConsonantAdjustments, i,
                        VsqPhoneticSymbol.isConsonant(vec.get(
                                mPhoneticSymbols, i)) ? 64 : 0);
                }
            }
        }
    }

    public Vector<String> getPhoneticSymbolList() {
        if (mPhoneticSymbols == null) {
            mPhoneticSymbols = new Vector<String>();
        }

        return mPhoneticSymbols;
    }

    /// <summary>
    /// このインスタンスを文字列に変換します
    /// </summary>
    /// <param name="add_quatation_mark">クォーテーションマークを付けるかどうか</param>
    /// <returns>変換後の文字列</returns>
    public String toString(boolean add_quatation_mark) {
        String quot = (add_quatation_mark ? "\"" : "");
        String result;
        String phrase = (this.Phrase == null) ? ""
                                              : this.Phrase.replace("\"", "\"\"");
        result = quot + phrase + quot + ",";

        Vector<String> symbol = getPhoneticSymbolList();
        String strSymbol = getPhoneticSymbol();

        if (!add_quatation_mark) {
            if ((strSymbol == null) ||
                    ((strSymbol != null) && str.compare(strSymbol, ""))) {
                strSymbol = "u:";
            }
        }

        result += (quot + strSymbol + quot + "," +
        PortUtil.formatDecimal("0.000000", UnknownFloat));
        result = result.replace("\\" + "\\", "\\");

        if (mConsonantAdjustments == null) {
            mConsonantAdjustments = new Vector<Integer>();

            for (int i = 0; i < vec.size(symbol); i++) {
                vec.add(mConsonantAdjustments,
                    VsqPhoneticSymbol.isConsonant(vec.get(symbol, i)) ? 64 : 0);
            }
        }

        for (int i = 0; i < vec.size(mConsonantAdjustments); i++) {
            result += ("," + vec.get(mConsonantAdjustments, i));
        }

        if (PhoneticSymbolProtected) {
            result += ",1";
        } else {
            result += ",0";
        }

        return result;
    }
}

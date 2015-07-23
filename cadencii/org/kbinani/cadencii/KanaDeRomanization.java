/*
 * KanaDeRomanization.cs
 * Copyright © 2009-2011 kbinani
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

    public class KanaDeRomanization {
        static final int _MAX_MATCH = 4;

        /// <summary>
        /// ひらがなをカタカナに変換する
        /// </summary>
        /// <param name="maybe_katakana"></param>
        /// <returns></returns>
        public static String hiragana2katakana( String maybe_hiragana ) {
char[] arr = maybe_hiragana.toCharArray();
String ret = "";
int i = -1;
while ( i + 1 < arr.length ) {
    i++;
    char transformed = hiragana2katakanaCor( arr[i] );
    if ( i + 1 < arr.length ) {
        if ( arr[i + 1] == 'ﾞ' ) {
            if ( transformed == 'カ' ) {
                transformed = 'ガ';
            } else if ( transformed == 'キ' ) {
                transformed = 'ギ';
            } else if ( transformed == 'ク' ) {
                transformed = 'グ';
            } else if ( transformed == 'ケ' ) {
                transformed = 'ゲ';
            } else if ( transformed == 'コ' ) {
                transformed = 'ゴ';
            } else if ( transformed == 'サ' ) {
                transformed = 'ザ';
            } else if ( transformed == 'シ' ) {
                transformed = 'ジ';
            } else if ( transformed == 'ス' ) {
                transformed = 'ズ';
            } else if ( transformed == 'セ' ) {
                transformed = 'ゼ';
            } else if ( transformed == 'ソ' ) {
                transformed = 'ゾ';
            } else if ( transformed == 'タ' ) {
                transformed = 'ダ';
            } else if ( transformed == 'チ' ) {
                transformed = 'ヂ';
            } else if ( transformed == 'ツ' ) {
                transformed = 'ヅ';
            } else if ( transformed == 'テ' ) {
                transformed = 'デ';
            } else if ( transformed == 'ト' ) {
                transformed = 'ド';
            } else if ( transformed == 'ハ' ) {
                transformed = 'バ';
            } else if ( transformed == 'ヒ' ) {
                transformed = 'ビ';
            } else if ( transformed == 'フ' ) {
                transformed = 'ブ';
            } else if ( transformed == 'ヘ' ) {
                transformed = 'ベ';
            } else if ( transformed == 'ホ' ) {
                transformed = 'ボ';
            } else if ( transformed == 'ウ' ) {
                transformed = 'ヴ';
            }
            i++;
        } else if ( arr[i + 1] == 'ﾟ' ) {
            if ( transformed == 'ハ' ) {
                transformed = 'パ';
            } else if ( transformed == 'ヒ' ) {
                transformed = 'ピ';
            } else if ( transformed == 'フ' ) {
                transformed = 'プ';
            } else if ( transformed == 'ヘ' ) {
                transformed = 'ペ';
            } else if ( transformed == 'ホ' ) {
                transformed = 'ポ';
            }
            i++;
        }
    }
    ret = ret + transformed;
}
return ret;
        }

        private static char hiragana2katakanaCor( char maybe_hiragana ) {
if ( maybe_hiragana == 'あ' || maybe_hiragana == 'ｱ' ) {
    return 'ア';
} else if ( maybe_hiragana == 'い' || maybe_hiragana == 'ｲ' ) {
    return 'イ';
} else if ( maybe_hiragana == 'う' || maybe_hiragana == 'ｳ' ) {
    return 'ウ';
} else if ( maybe_hiragana == 'え' || maybe_hiragana == 'ｴ' ) {
    return 'エ';
} else if ( maybe_hiragana == 'お' || maybe_hiragana == 'ｵ' ) {
    return 'オ';
} else if ( maybe_hiragana == 'か' || maybe_hiragana == 'ｶ' ) {
    return 'カ';
} else if ( maybe_hiragana == 'き' || maybe_hiragana == 'ｷ' ) {
    return 'キ';
} else if ( maybe_hiragana == 'く' || maybe_hiragana == 'ｸ' ) {
    return 'ク';
} else if ( maybe_hiragana == 'け' || maybe_hiragana == 'ｹ' ) {
    return 'ケ';
} else if ( maybe_hiragana == 'こ' || maybe_hiragana == 'ｺ' ) {
    return 'コ';
} else if ( maybe_hiragana == 'さ' || maybe_hiragana == 'ｻ' ) {
    return 'サ';
} else if ( maybe_hiragana == 'し' || maybe_hiragana == 'ｼ' ) {
    return 'シ';
} else if ( maybe_hiragana == 'す' || maybe_hiragana == 'ｽ' ) {
    return 'ス';
} else if ( maybe_hiragana == 'せ' || maybe_hiragana == 'ｾ' ) {
    return 'セ';
} else if ( maybe_hiragana == 'そ' || maybe_hiragana == 'ｿ' ) {
    return 'ソ';
} else if ( maybe_hiragana == 'た' || maybe_hiragana == 'ﾀ' ) {
    return 'タ';
} else if ( maybe_hiragana == 'ち' || maybe_hiragana == 'ﾁ' ) {
    return 'チ';
} else if ( maybe_hiragana == 'つ' || maybe_hiragana == 'ﾂ' ) {
    return 'ツ';
} else if ( maybe_hiragana == 'て' || maybe_hiragana == 'ﾃ' ) {
    return 'テ';
} else if ( maybe_hiragana == 'と' || maybe_hiragana == 'ﾄ' ) {
    return 'ト';
} else if ( maybe_hiragana == 'な' || maybe_hiragana == 'ﾅ' ) {
    return 'ナ';
} else if ( maybe_hiragana == 'に' || maybe_hiragana == 'ﾆ' ) {
    return 'ニ';
} else if ( maybe_hiragana == 'ぬ' || maybe_hiragana == 'ﾇ' ) {
    return 'ヌ';
} else if ( maybe_hiragana == 'ね' || maybe_hiragana == 'ﾈ' ) {
    return 'ネ';
} else if ( maybe_hiragana == 'の' || maybe_hiragana == 'ﾉ' ) {
    return 'ノ';
} else if ( maybe_hiragana == 'は' || maybe_hiragana == 'ﾊ' ) {
    return 'ハ';
} else if ( maybe_hiragana == 'ひ' || maybe_hiragana == 'ﾋ' ) {
    return 'ヒ';
} else if ( maybe_hiragana == 'ふ' || maybe_hiragana == 'ﾌ' ) {
    return 'フ';
} else if ( maybe_hiragana == 'へ' || maybe_hiragana == 'ﾍ' ) {
    return 'ヘ';
} else if ( maybe_hiragana == 'ほ' || maybe_hiragana == 'ﾎ' ) {
    return 'ホ';
} else if ( maybe_hiragana == 'ま' || maybe_hiragana == 'ﾏ' ) {
    return 'マ';
} else if ( maybe_hiragana == 'み' || maybe_hiragana == 'ﾐ' ) {
    return 'ミ';
} else if ( maybe_hiragana == 'む' || maybe_hiragana == 'ﾑ' ) {
    return 'ム';
} else if ( maybe_hiragana == 'め' || maybe_hiragana == 'ﾒ' ) {
    return 'メ';
} else if ( maybe_hiragana == 'も' || maybe_hiragana == 'ﾓ' ) {
    return 'モ';
} else if ( maybe_hiragana == 'や' || maybe_hiragana == 'ﾔ' ) {
    return 'ヤ';
} else if ( maybe_hiragana == 'ゆ' || maybe_hiragana == 'ﾕ' ) {
    return 'ユ';
} else if ( maybe_hiragana == 'よ' || maybe_hiragana == 'ﾖ' ) {
    return 'ヨ';
} else if ( maybe_hiragana == 'ら' || maybe_hiragana == 'ﾗ' ) {
    return 'ラ';
} else if ( maybe_hiragana == 'り' || maybe_hiragana == 'ﾘ' ) {
    return 'リ';
} else if ( maybe_hiragana == 'る' || maybe_hiragana == 'ﾙ' ) {
    return 'ル';
} else if ( maybe_hiragana == 'れ' || maybe_hiragana == 'ﾚ' ) {
    return 'レ';
} else if ( maybe_hiragana == 'ろ' || maybe_hiragana == 'ﾛ' ) {
    return 'ロ';
} else if ( maybe_hiragana == 'わ' || maybe_hiragana == 'ﾜ' ) {
    return 'ワ';
} else if ( maybe_hiragana == 'を' || maybe_hiragana == 'ｦ' ) {
    return 'ヲ';
} else if ( maybe_hiragana == 'ん' || maybe_hiragana == 'ﾝ' ) {
    return 'ン';
} else if ( maybe_hiragana == 'が' ) {
    return 'ガ';
} else if ( maybe_hiragana == 'ぱ' ) {
    return 'パ';
} else if ( maybe_hiragana == 'ぁ' || maybe_hiragana == 'ｧ' ) {
    return 'ァ';
} else if ( maybe_hiragana == 'ぃ' || maybe_hiragana == 'ｨ' ) {
    return 'ィ';
} else if ( maybe_hiragana == 'ぅ' || maybe_hiragana == 'ｩ' ) {
    return 'ゥ';
} else if ( maybe_hiragana == 'ぇ' || maybe_hiragana == 'ｪ' ) {
    return 'ェ';
} else if ( maybe_hiragana == 'ぉ' || maybe_hiragana == 'ｫ' ) {
    return 'ォ';
} else if ( maybe_hiragana == 'ゃ' || maybe_hiragana == 'ｬ' ) {
    return 'ャ';
} else if ( maybe_hiragana == 'ゅ' || maybe_hiragana == 'ｭ' ) {
    return 'ュ';
} else if ( maybe_hiragana == 'ょ' || maybe_hiragana == 'ｮ' ) {
    return 'ョ';
} else if ( maybe_hiragana == 'っ' || maybe_hiragana == 'ｯ' ) {
    return 'ッ';
} else if ( maybe_hiragana == 'ゐ' ) {
    return 'ヰ';
} else if ( maybe_hiragana == 'ゑ' ) {
    return 'ヱ';
} else if ( maybe_hiragana == 'ぎ' ) {
    return 'ギ';
} else if ( maybe_hiragana == 'ぐ' ) {
    return 'グ';
} else if ( maybe_hiragana == 'げ' ) {
    return 'ゲ';
} else if ( maybe_hiragana == 'ご' ) {
    return 'ゴ';
} else if ( maybe_hiragana == 'ざ' ) {
    return 'ザ';
} else if ( maybe_hiragana == 'じ' ) {
    return 'ジ';
} else if ( maybe_hiragana == 'ず' ) {
    return 'ズ';
} else if ( maybe_hiragana == 'ぜ' ) {
    return 'ゼ';
} else if ( maybe_hiragana == 'ぞ' ) {
    return 'ゾ';
} else if ( maybe_hiragana == 'だ' ) {
    return 'ダ';
} else if ( maybe_hiragana == 'ぢ' ) {
    return 'ヂ';
} else if ( maybe_hiragana == 'づ' ) {
    return 'ヅ';
} else if ( maybe_hiragana == 'で' ) {
    return 'デ';
} else if ( maybe_hiragana == 'ど' ) {
    return 'ド';
} else if ( maybe_hiragana == 'ば' ) {
    return 'バ';
} else if ( maybe_hiragana == 'び' ) {
    return 'ビ';
} else if ( maybe_hiragana == 'ぶ' ) {
    return 'ブ';
} else if ( maybe_hiragana == 'べ' ) {
    return 'ベ';
} else if ( maybe_hiragana == 'ぼ' ) {
    return 'ボ';
} else if ( maybe_hiragana == 'ぴ' ) {
    return 'ピ';
} else if ( maybe_hiragana == 'ぷ' ) {
    return 'プ';
} else if ( maybe_hiragana == 'ぺ' ) {
    return 'ペ';
} else if ( maybe_hiragana == 'ぽ' ) {
    return 'ポ';
} else if ( maybe_hiragana == 'ゎ' ) {
    return 'ワ';
} else if ( maybe_hiragana == 'ヴ' ) {
    return 'ヴ';
}
return maybe_hiragana;
        }

        public static String Attach( String roman ) {
char[] arr = roman.toCharArray();
String ret = "";
int index = 0;
while ( index < arr.length ) {
    // _MAX_MATCH～2文字のマッチ
    boolean processed = false;
    for ( int i = _MAX_MATCH; i >= 2; i-- ) {
        if ( index + (i - 1) < arr.length ) {
            String s = "";
            for ( int j = 0; j < i; j++ ) {
                s += "" + arr[index + j];
            }
            ByRef<Boolean> trailing = new ByRef<Boolean>();
            String res = AttachCor( s, trailing );
            if ( res != s ) {
                if ( !trailing.value ) {
                    index = index + i;
                } else {
                    index = index + i - 1;
                }
                ret += res;
                processed = true;
                break;
            }
        }
    }
    if ( processed ) {
        continue;
    }

    // 1文字のマッチ
    ByRef<Boolean> trailing1 = new ByRef<Boolean>();
    ret += AttachCor( arr[index] + "", trailing1 );
    index++;
}
return ret;
        }

        private static String AttachCor( String roman, ByRef<Boolean> trailing ) {
String s = roman.toLowerCase();
trailing.value = false;
if ( s.equals( "a" ) ) {
    return "あ";
} else if ( s.equals( "i" ) ||
            s.equals( "yi" ) ) {
    return "い";
} else if ( s.equals( "u" ) ||
            s.equals( "wu" ) ) {
    return "う";
} else if ( s.equals( "e" ) ) {
    return "え";
} else if ( s.equals( "o" ) ) {
    return "お";
} else if ( s.equals( "ka" ) ||
            s.equals( "ca" ) ) {
    return "か";
} else if ( s.equals( "ki" ) ) {
    return "き";
} else if ( s.equals( "ku" ) ||
            s.equals( "cu" ) ||
            s.equals( "qu" ) ) {
    return "く";
} else if ( s.equals( "ke" ) ) {
    return "け";
} else if ( s.equals( "ko" ) ||
            s.equals( "co" ) ) {
    return "こ";
} else if ( s.equals( "sa" ) ) {
    return "さ";
} else if ( s.equals( "si" ) ||
            s.equals( "shi" ) ||
            s.equals( "ci" ) ) {
    return "し";
} else if ( s.equals( "su" ) ) {
    return "す";
} else if ( s.equals( "se" ) ||
            s.equals( "ce" ) ) {
    return "せ";
} else if ( s.equals( "so" ) ) {
    return "そ";
} else if ( s.equals( "ta" ) ) {
    return "た";
} else if ( s.equals( "chi" ) ||
            s.equals( "ti" ) ) {
    return "ち";
} else if ( s.equals( "tu" ) ||
            s.equals( "tsu" ) ) {
    return "つ";
} else if ( s.equals( "te" ) ) {
    return "て";
} else if ( s.equals( "to" ) ) {
    return "と";
} else if ( s.equals( "na" ) ) {
    return "な";
} else if ( s.equals( "ni" ) ) {
    return "に";
} else if ( s.equals( "nu" ) ) {
    return "ぬ";
} else if ( s.equals( "ne" ) ) {
    return "ね";
} else if ( s.equals( "no" ) ) {
    return "の";
} else if ( s.equals( "ha" ) ) {
    return "は";
} else if ( s.equals( "hi" ) ) {
    return "ひ";
} else if ( s.equals( "hu" ) ||
            s.equals( "fu" ) ) {
    return "ふ";
} else if ( s.equals( "he" ) ) {
    return "へ";
} else if ( s.equals( "ho" ) ) {
    return "ほ";
} else if ( s.equals( "ma" ) ) {
    return "ま";
} else if ( s.equals( "mi" ) ) {
    return "み";
} else if ( s.equals( "mu" ) ) {
    return "む";
} else if ( s.equals( "me" ) ) {
    return "め";
} else if ( s.equals( "mo" ) ) {
    return "も";
} else if ( s.equals( "ya" ) ) {
    return "や";
} else if ( s.equals( "yu" ) ) {
    return "ゆ";
} else if ( s.equals( "ye" ) ) {
    return "いぇ";
} else if ( s.equals( "yo" ) ) {
    return "よ";
} else if ( s.equals( "ra" ) ) {
    return "ら";
} else if ( s.equals( "ri" ) ) {
    return "り";
} else if ( s.equals( "ru" ) ) {
    return "る";
} else if ( s.equals( "re" ) ) {
    return "れ";
} else if ( s.equals( "ro" ) ) {
    return "ろ";
} else if ( s.equals( "wa" ) ) {
    return "わ";
} else if ( s.equals( "wi" ) ) {
    return "うぃ";
} else if ( s.equals( "wyi" ) ) {
    return "ゐ";
} else if ( s.equals( "we" ) ) {
    return "うぇ";
} else if ( s.equals( "wye" ) ) {
    return "ゑ";
} else if ( s.equals( "wo" ) ) {
    return "を";
} else if ( s.equals( "nn" ) ||
            s.equals( "n" ) ) {
    return "ん";
} else if ( s.equals( "ga" ) ) {
    return "が";
} else if ( s.equals( "gi" ) ) {
    return "ぎ";
} else if ( s.equals( "gu" ) ) {
    return "ぐ";
} else if ( s.equals( "ge" ) ) {
    return "げ";
} else if ( s.equals( "go" ) ) {
    return "ご";
} else if ( s.equals( "za" ) ) {
    return "ざ";
} else if ( s.equals( "zi" ) ||
            s.equals( "ji" ) ) {
    return "じ";
} else if ( s.equals( "zu" ) ) {
    return "ず";
} else if ( s.equals( "ze" ) ) {
    return "ぜ";
} else if ( s.equals( "zo" ) ) {
    return "ぞ";
} else if ( s.equals( "da" ) ) {
    return "だ";
} else if ( s.equals( "di" ) ) {
    return "ぢ";
} else if ( s.equals( "du" ) ) {
    return "づ";
} else if ( s.equals( "de" ) ) {
    return "で";
} else if ( s.equals( "do" ) ) {
    return "ど";
} else if ( s.equals( "ba" ) ) {
    return "ば";
} else if ( s.equals( "bi" ) ) {
    return "び";
} else if ( s.equals( "bu" ) ) {
    return "ぶ";
} else if ( s.equals( "be" ) ) {
    return "べ";
} else if ( s.equals( "bo" ) ) {
    return "ぼ";
} else if ( s.equals( "pa" ) ) {
    return "ぱ";
} else if ( s.equals( "pi" ) ) {
    return "ぴ";
} else if ( s.equals( "pu" ) ) {
    return "ぷ";
} else if ( s.equals( "pe" ) ) {
    return "ぺ";
} else if ( s.equals( "po" ) ) {
    return "ぽ";
} else if ( s.equals( "sha" ) ) {
    return "しゃ";
} else if ( s.equals( "shu" ) ) {
    return "しゅ";
} else if ( s.equals( "sho" ) ) {
    return "しょ";
} else if ( s.equals( "cha" ) ||
            s.equals( "tya" ) ) {
    return "ちゃ";
} else if ( s.equals( "chu" ) ||
            s.equals( "tyu" ) ) {
    return "ちゅ";
} else if ( s.equals( "cho" ) ||
            s.equals( "tyo" ) ) {
    return "ちょ";
} else if ( s.equals( "dya" ) ) {
    return "ぢゃ";
} else if ( s.equals( "dyu" ) ) {
    return "ぢゅ";
} else if ( s.equals( "dyo" ) ) {
    return "ぢょ";
} else if ( s.equals( "kwa" ) ) {
    return "くゎ";
} else if ( s.equals( "kwi" ) ) {
    return "くぃ";
} else if ( s.equals( "kwu" ) ) {
    return "くぅ";
} else if ( s.equals( "kwe" ) ) {
    return "くぇ";
} else if ( s.equals( "kwo" ) ) {
    return "くぉ";
} else if ( s.equals( "gwa" ) ) {
    return "ぐゎ";
} else if ( s.equals( "kya" ) ) {
    return "きゃ";
} else if ( s.equals( "kyu" ) ) {
    return "きゅ";
} else if ( s.equals( "kyo" ) ) {
    return "きょ";
} else if ( s.equals( "sya" ) ) {
    return "しゃ";
} else if ( s.equals( "syu" ) ) {
    return "しゅ";
} else if ( s.equals( "syo" ) ) {
    return "しょ";
} else if ( s.equals( "nya" ) ) {
    return "にゃ";
} else if ( s.equals( "nyu" ) ) {
    return "にゅ";
} else if ( s.equals( "nyo" ) ) {
    return "にょ";
} else if ( s.equals( "mya" ) ) {
    return "みゃ";
} else if ( s.equals( "myu" ) ) {
    return "みゅ";
} else if ( s.equals( "myo" ) ) {
    return "みょ";
} else if ( s.equals( "rya" ) ) {
    return "りゃ";
} else if ( s.equals( "ryu" ) ) {
    return "りゅ";
} else if ( s.equals( "ryo" ) ) {
    return "りょ";
} else if ( s.equals( "gya" ) ) {
    return "ぎゃ";
} else if ( s.equals( "gyu" ) ) {
    return "ぎゅ";
} else if ( s.equals( "gyo" ) ) {
    return "ぎょ";
} else if ( s.equals( "zya" ) ||
            s.equals( "ja" ) ) {
    return "じゃ";
} else if ( s.equals( "zyu" ) ||
            s.equals( "ju" ) ) {
    return "じゅ";
} else if ( s.equals( "zyo" ) ||
            s.equals( "jo" ) ) {
    return "じょ";
} else if ( s.equals( "bya" ) ) {
    return "びゃ";
} else if ( s.equals( "byu" ) ) {
    return "びゅ";
} else if ( s.equals( "byo" ) ) {
    return "びょ";
} else if ( s.equals( "pya" ) ) {
    return "ぴゃ";
} else if ( s.equals( "pyu" ) ) {
    return "ぴゅ";
} else if ( s.equals( "pyo" ) ) {
    return "ぴょ";
} else if ( s.equals( "la" ) ||
            s.equals( "xa" ) ) {
    return "ぁ";
} else if ( s.equals( "li" ) ||
            s.equals( "xi" ) ||
            s.equals( "lyi" ) ||
            s.equals( "xyi" ) ) {
    return "ぃ";
} else if ( s.equals( "lu" ) ||
            s.equals( "xu" ) ) {
    return "ぅ";
} else if ( s.equals( "le" ) ||
            s.equals( "xe" ) ||
            s.equals( "lye" ) ||
            s.equals( "xye" ) ) {
    return "ぇ";
} else if ( s.equals( "lo" ) ||
            s.equals( "xo" ) ) {
    return "ぉ";
} else if ( s.equals( "lya" ) ||
            s.equals( "xya" ) ) {
    return "ゃ";
} else if ( s.equals( "lyu" ) ||
            s.equals( "xyu" ) ) {
    return "ゅ";
} else if ( s.equals( "lyo" ) ||
            s.equals( "xyo" ) ) {
    return "ょ";
} else if ( s.equals( "lwa" ) ||
            s.equals( "xwa" ) ) {
    return "ゎ";
} else if ( s.equals( "ltu" ) ||
            s.equals( "xtu" ) ||
            s.equals( "xtsu" ) ||
            s.equals( "ltsu" ) ) {
    return "っ";
} else if ( s.equals( "va" ) ) {
    return "ゔぁ";
} else if ( s.equals( "vi" ) ) {
    return "ゔぃ";
} else if ( s.equals( "vu" ) ) {
    return "ゔ";
} else if ( s.equals( "ve" ) ) {
    return "ゔぇ";
} else if ( s.equals( "vo" ) ) {
    return "ゔぉ";
} else if ( s.equals( "fa" ) ) {
    return "ふぁ";
} else if ( s.equals( "fi" ) ) {
    return "ふぃ";
} else if ( s.equals( "fe" ) ) {
    return "ふぇ";
} else if ( s.equals( "fo" ) ) {
    return "ふぉ";
} else if ( s.equals( "qa" ) ) {
    return "くぁ";
} else if ( s.equals( "qi" ) ) {
    return "くぃ";
} else if ( s.equals( "qe" ) ) {
    return "くぇ";
} else if ( s.equals( "qo" ) ) {
    return "くぉ";
} else if ( s.equals( "vyu" ) ) {
    return "ゔゅ";
} else if ( s.equals( "qq" ) ||
            s.equals( "ww" ) ||
            s.equals( "rr" ) ||
            s.equals( "tt" ) ||
            s.equals( "yy" ) ||
            s.equals( "pp" ) ||
            s.equals( "ss" ) ||
            s.equals( "dd" ) ||
            s.equals( "ff" ) ||
            s.equals( "gg" ) ||
            s.equals( "hh" ) ||
            s.equals( "jj" ) ||
            s.equals( "kk" ) ||
            s.equals( "ll" ) ||
            s.equals( "zz" ) ||
            s.equals( "xx" ) ||
            s.equals( "cc" ) ||
            s.equals( "vv" ) ||
            s.equals( "bb" ) ||
            s.equals( "mm" ) ) {
    trailing.value = true;
    return "っ";
} else if ( s.equals( "-" ) ) {
    return "ー";
} else if ( s.equals( "tha" ) ) {
    return "てゃ";
} else if ( s.equals( "thi" ) ) {
    return "てぃ";
} else if ( s.equals( "thu" ) ) {
    return "てゅ";
} else if ( s.equals( "the" ) ) {
    return "てぇ";
} else if ( s.equals( "tho" ) ) {
    return "てょ";
} else if ( s.equals( "twa" ) ) {
    return "とぁ";
} else if ( s.equals( "twi" ) ) {
    return "とぃ";
} else if ( s.equals( "twu" ) ) {
    return "とぅ";
} else if ( s.equals( "twe" ) ) {
    return "とぇ";
} else if ( s.equals( "two" ) ) {
    return "とぉ";
} else if ( s.equals( "dha" ) ) {
    return "でゃ";
} else if ( s.equals( "dhi" ) ) {
    return "でぃ";
} else if ( s.equals( "dhu" ) ) {
    return "でゅ";
} else if ( s.equals( "dhe" ) ) {
    return "でぇ";
} else if ( s.equals( "dho" ) ) {
    return "でょ";
} else if ( s.equals( "wha" ) ) {
    return "うぁ";
} else if ( s.equals( "whi" ) ) {
    return "うぃ";
} else if ( s.equals( "whu" ) ) {
    return "う";
} else if ( s.equals( "whe" ) ) {
    return "うぇ";
} else if ( s.equals( "who" ) ) {
    return "うぉ";
} else if ( s.equals( "lka" ) ||
            s.equals( "xka" ) ) {
    return "ヵ";
} else if ( s.equals( "lke" ) ||
            s.equals( "xke" ) ) {
    return "ヶ";
} else if ( s.equals( "tsa" ) ) {
    return "つぁ";
} else if ( s.equals( "tsi" ) ) {
    return "つぃ";
} else if ( s.equals( "tse" ) ) {
    return "つぇ";
} else if ( s.equals( "tso" ) ) {
    return "つぉ";
} else if ( s.equals( "jya" ) ) {
    return "じゃ";
} else if ( s.equals( "jyu" ) ) {
    return "じゅ";
} else if ( s.equals( "jyo" ) ) {
    return "じょ";
} else if ( s.equals( "cya" ) ) {
    return "ちゃ";
} else if ( s.equals( "cyi" ) ) {
    return "ちぃ";
} else if ( s.equals( "cyu" ) ) {
    return "ちゅ";
} else if ( s.equals( "cye" ) ) {
    return "ちぇ";
} else if ( s.equals( "cyo" ) ) {
    return "ちょ";
} else if ( s.equals( "dwa" ) ) {
    return "どぁ";
} else if ( s.equals( "dwi" ) ) {
    return "どぃ";
} else if ( s.equals( "dwu" ) ) {
    return "どぅ";
} else if ( s.equals( "dwe" ) ) {
    return "どぇ";
} else if ( s.equals( "dwo" ) ) {
    return "どぉ";
} else if ( s.equals( "hwa" ) ) {
    return "ふぁ";
} else if ( s.equals( "hwi" ) ) {
    return "ふぃ";
} else if ( s.equals( "hwu" ) ) {
    return "ふぇ";
} else if ( s.equals( "hwo" ) ) {
    return "ふぉ";
} else if ( s.equals( "fyu" ) ||
            s.equals( "hwyu" ) ) {
    return "ふゅ";
}
return roman;
        }
    }


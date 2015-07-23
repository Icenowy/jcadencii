/*
 * VocaloSysUtil.s
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
import java.util.*;
import org.kbinani.*;

    /// <summary>
    /// VOCALOID / VOCALOID2システムについての情報を取得するユーティリティ。
    /// </summary>
    public class VocaloSysUtil
    {
        private static TreeMap<SynthesizerType, SingerConfigSys> s_singer_config_sys = new TreeMap<SynthesizerType, SingerConfigSys>();
        private static TreeMap<SynthesizerType, ExpressionConfigSys> s_exp_config_sys = new TreeMap<SynthesizerType, ExpressionConfigSys>();
        private static TreeMap<SynthesizerType, String> s_path_vsti = new TreeMap<SynthesizerType, String>();
        private static TreeMap<SynthesizerType, String> s_path_editor = new TreeMap<SynthesizerType, String>();
        private static Boolean isInitialized = false;
        /// <summary>
        /// VOCALOID1の、デフォルトのSynthesize Engineバージョン。1.0の場合100, 1.1の場合101。規定では100(1.0)。
        /// initメソッドにおいて、VOCALOID.iniから読み取る
        /// </summary>
        private static int defaultDseVersion = 100;
        /// <summary>
        /// VOCALOID1にて、バージョン1.1のSynthesize Engineが利用可能かどうか。
        /// 既定ではfalse。DSE1_1.dllが存在するかどうかで判定。
        /// </summary>
        private static boolean dseVersion101Available = false;
        private static final String header1 = "HKLM\\SOFTWARE\\VOCALOID";
        private static final String header2 = "HKLM\\SOFTWARE\\VOCALOID2";

        private VocaloSysUtil()
        {
        }

        public static SingerConfigSys getSingerConfigSys( SynthesizerType type )
        {
if ( s_singer_config_sys.containsKey( type ) ) {
    return s_singer_config_sys.get( type );
} else {
    return null;
}
        }

        /// <summary>
        /// VOCALOID1にて、バージョン1.1のSynthesize Engineが利用可能かどうか。
        /// 既定ではfalse。DSE1_1.dllが存在するかどうかで判定。
        /// </summary>
        public static boolean isDSEVersion101Available()
        {
return dseVersion101Available;
        }

        /// <summary>
        /// VOCALOID1の、デフォルトのSynthesize Engineバージョンを取得します。
        /// 1.0の場合100, 1.1の場合101。規定では100(1.0)。
        /// </summary>
        public static int getDefaultDseVersion()
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getDefaultDseVersion; not initialized yet" );
    return 0;
}
return defaultDseVersion;
        }


        /// <summary>
        /// WINEPREFIXと，その内部のWindows形式の絶対パスを結合し，実際のファイルの絶対パスを取得します
        /// </summary>
        public static String combineWinePath( String wine_prefix, String full_path )
        {
if( wine_prefix == null ){
    wine_prefix = "";
}
if( wine_prefix.indexOf( "~" ) >= 0 ){
    String usr = System.getProperty( "user.name" );
    wine_prefix = wine_prefix.replace( "~", "/Users/" + usr );
}
if( full_path == null ){
    return wine_prefix;
}
int full_path_len = str.length( full_path );
if( full_path_len <= 0 ){
    return wine_prefix;
}
char drive_letter = str.charAt( full_path, 0 );
String drive = str.toLower( new String( new char[]{ drive_letter } ) );
String inner_path = (full_path_len >= 3) ? str.sub( full_path, 2 ).replace( "\\", "/" ) : "";
return fsys.combine( fsys.combine( wine_prefix, "drive_" + drive ), inner_path );
        }

        /// <summary>
        /// Windowsのレジストリ・エントリを列挙した文字列のリストを指定し，初期化します
        /// パラメータreg_listの中身は，例えば
        /// "HKLM\SOFTWARE\VOCALOID2\DATABASE\EXPRESSION\\tEXPRESSIONDIR\\tC:\Program Files\VOCALOID2\expdbdir"
        /// のような文字列です．
        /// </summary>
        /// <param name="reg_list">レジストリ・エントリのリスト</param>
        /// <param name="wine_prefix">wineを使う場合，WINEPREFIXを指定する．そうでなければ空文字を指定</param>
        public static void init( Vector<String> reg_list, String wine_prefix )
        {
if( reg_list == null ){
    return;
}
if( isInitialized ){
    return;
}

// reg_listを，VOCALOIDとVOCALOID2の部分に分離する
Vector<String> dir1 = new Vector<String>();
Vector<String> dir2 = new Vector<String>();
for( String s : reg_list ){
    if( str.startsWith( s, header1 + "\\" ) ||
        str.startsWith( s, header1 + "\t" ) ){
        dir1.add( s );
    }else if( str.startsWith( s, header2 + "\\" ) ||
              str.startsWith( s, header2 + "\t" ) ){
        dir2.add( s );
    }
}

ExpressionConfigSys exp_config_sys1 = null;
try {
    ByRef<String> path_voicedb1 = new ByRef<String>( "" );
    ByRef<String> path_expdb1 = new ByRef<String>( "" );
    Vector<String> installed_singers1 = new Vector<String>();

    // テキストファイルにレジストリの内容をプリントアウト
    boolean close = false;
    ByRef<String> path_vsti = new ByRef<String>( "" );
    ByRef<String> path_editor = new ByRef<String>( "" );
    initExtract( dir1,
             header1,
             path_vsti,
             path_voicedb1,
             path_expdb1,
             path_editor,
             installed_singers1 );
    s_path_vsti.put( SynthesizerType.VOCALOID1, path_vsti.value );
    s_path_editor.put( SynthesizerType.VOCALOID1, path_editor.value );
    String[] act_installed_singers1 = installed_singers1.toArray( new String[]{} );
    String act_path_voicedb1 = path_voicedb1.value;
    String act_path_editor1 = path_editor.value;
    String act_path_expdb1 = path_expdb1.value;
    String act_vsti1 = path_vsti.value;
    if( str.length( wine_prefix ) > 0 ){
        for( int i = 0; i < act_installed_singers1.length; i++ ){
            act_installed_singers1[i] = combineWinePath( wine_prefix, act_installed_singers1[i] );
        }
        act_path_voicedb1 = combineWinePath( wine_prefix, act_path_voicedb1 );
        act_path_editor1 = combineWinePath( wine_prefix, act_path_editor1 );
        act_path_expdb1 = combineWinePath( wine_prefix, act_path_expdb1 );
        act_vsti1 = combineWinePath( wine_prefix, act_vsti1 );
    }
    String expression_map1 = fsys.combine( act_path_expdb1, "expression.map" );
    SingerConfigSys singer_config_sys =
        new SingerConfigSys( act_path_voicedb1, act_installed_singers1 );
    if ( fsys.isFileExists( expression_map1 ) ) {
        exp_config_sys1 = new ExpressionConfigSys( act_path_editor1, act_path_expdb1 );
    }
    s_singer_config_sys.put( SynthesizerType.VOCALOID1, singer_config_sys );

    // DSE1_1.dllがあるかどうか？
    if ( !act_vsti1.equals( "" ) ) {
        String path_dll = PortUtil.getDirectoryName( act_vsti1 );
        String dse1_1 = fsys.combine( path_dll, "DSE1_1.dll" );
        dseVersion101Available = fsys.isFileExists( dse1_1 );
    } else {
        dseVersion101Available = false;
    }

    // VOCALOID.iniから、DSEVersionを取得
    if ( act_path_editor1 != null && !act_path_editor1.equals( "" ) &&
         fsys.isFileExists( act_path_editor1 ) ) {
        String dir = PortUtil.getDirectoryName( act_path_editor1 );
        String ini = fsys.combine( dir, "VOCALOID.ini" );
        if ( fsys.isFileExists( ini ) ) {
            BufferedReader br = null;
            try {
                br = new BufferedReader( new InputStreamReader( new FileInputStream( ini ), "Shift_JIS" ) );
                while ( br.ready() ) {
                    String line = br.readLine();
                    if ( line == null ) continue;
                    if ( line.equals( "" ) ) continue;
                    if ( line.startsWith( "DSEVersion" ) ) {
                        String[] spl = PortUtil.splitString( line, '=' );
                        if ( spl.length >= 2 ) {
                            String str_dse_version = spl[1];
                            try {
                                defaultDseVersion = str.toi( str_dse_version );
                            } catch ( Exception ex ) {
                                serr.println( "VocaloSysUtil#init; ex=" + ex );
                                ex.printStackTrace();
                                defaultDseVersion = 100;
                            }
                        }
                        break;
                    }
                }
            } catch ( Exception ex ) {
                serr.println( "VocaloSysUtil#init; ex=" + ex );
                ex.printStackTrace();
            } finally {
                if ( br != null ) {
                    try {
                        br.close();
                    } catch ( Exception ex2 ) {
                        serr.println( "VocaloSysUtil#init; ex2=" + ex2 );
                    }
                }
            }
        }
    }
} catch ( Exception ex ) {
    serr.println( "VocaloSysUtil#init; ex=" + ex );
    ex.printStackTrace();
    SingerConfigSys singer_config_sys = new SingerConfigSys( "", new String[] { } );
    exp_config_sys1 = null;
    s_singer_config_sys.put( SynthesizerType.VOCALOID1, singer_config_sys );
}
if ( exp_config_sys1 == null ) {
    exp_config_sys1 = ExpressionConfigSys.getVocaloid1Default();
}
s_exp_config_sys.put( SynthesizerType.VOCALOID1, exp_config_sys1 );

ExpressionConfigSys exp_config_sys2 = null;
try {
    ByRef<String> path_voicedb2 = new ByRef<String>( "" );
    ByRef<String> path_expdb2 = new ByRef<String>( "" );
    Vector<String> installed_singers2 = new Vector<String>();

    // レジストリの中身をファイルに出力
    boolean close = false;
    ByRef<String> path_vsti = new ByRef<String>( "" );
    ByRef<String> path_editor = new ByRef<String>( "" );
    initExtract( dir2,
             header2,
             path_vsti,
             path_voicedb2,
             path_expdb2,
             path_editor,
             installed_singers2 );
    s_path_vsti.put( SynthesizerType.VOCALOID2, path_vsti.value );
    s_path_editor.put( SynthesizerType.VOCALOID2, path_editor.value );
    String[] act_installed_singers2 = installed_singers2.toArray( new String[]{} );
    String act_path_expdb2 = path_expdb2.value;
    String act_path_voicedb2 = path_voicedb2.value;
    String act_path_editor2 = path_editor.value;
    String act_vsti2 = path_vsti.value;
    if( str.length( wine_prefix ) > 0 ){
        for( int i = 0; i < act_installed_singers2.length; i++ ){
            act_installed_singers2[i] = combineWinePath( wine_prefix, act_installed_singers2[i] );
        }
        act_path_expdb2 = combineWinePath( wine_prefix, act_path_expdb2 );
        act_path_voicedb2 = combineWinePath( wine_prefix, act_path_voicedb2 );
        act_path_editor2 = combineWinePath( wine_prefix, act_path_editor2 );
        act_vsti2 = combineWinePath( wine_prefix, act_vsti2 );
    }
    String expression_map2 = fsys.combine( act_path_expdb2, "expression.map" );
    SingerConfigSys singer_config_sys = new SingerConfigSys( act_path_voicedb2, act_installed_singers2 );
    if ( fsys.isFileExists( expression_map2 ) ) {
        exp_config_sys2 = new ExpressionConfigSys( act_path_editor2, act_path_expdb2 );
    }
    s_singer_config_sys.put( SynthesizerType.VOCALOID2, singer_config_sys );
} catch ( Exception ex ) {
    serr.println( "VocaloSysUtil..cctor; ex=" + ex );
    ex.printStackTrace();
    SingerConfigSys singer_config_sys = new SingerConfigSys( "", new String[] { } );
    exp_config_sys2 = null;
    s_singer_config_sys.put( SynthesizerType.VOCALOID2, singer_config_sys );
}
if ( exp_config_sys2 == null ) {
    exp_config_sys2 = ExpressionConfigSys.getVocaloid2Default();
}
s_exp_config_sys.put( SynthesizerType.VOCALOID2, exp_config_sys2 );

isInitialized = true;
        }

        /// <summary>
        /// ビブラートのプリセットタイプから，VibratoHandleを作成します
        /// </summary>
        /// <param name="icon_id"></param>
        /// <param name="vibrato_length"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public static VibratoHandle getDefaultVibratoHandle( String icon_id, int vibrato_length, SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getDefaultVibratoHandle; not initialized yet" );
    return null;
}
if ( s_exp_config_sys.containsKey( type ) ) {
    for ( Iterator<VibratoHandle> itr = s_exp_config_sys.get( type ).vibratoConfigIterator(); itr.hasNext(); ) {
        VibratoHandle vconfig = itr.next();
        if ( vconfig.IconID.equals( icon_id ) ) {
            VibratoHandle ret = (VibratoHandle)vconfig.clone();
            ret.setLength( vibrato_length );
            return ret;
        }
    }
}
VibratoHandle empty = new VibratoHandle();
empty.IconID = "$04040000";
return empty;
        }

        private static void initExtract( Vector<String> dir,
                         String header,
                         ByRef<String> path_vsti,
                         ByRef<String> path_voicedb,
                         ByRef<String> path_expdb,
                         ByRef<String> path_editor,
                         Vector<String> installed_singers )
        {
Vector<String> application = new Vector<String>();
Vector<String> expression = new Vector<String>();
Vector<String> voice = new Vector<String>();
path_vsti.value = "";
path_expdb.value = "";
path_voicedb.value = "";
path_editor.value = "";
for ( Iterator<String> itr = dir.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    if ( s.startsWith( header + "\\APPLICATION" ) ) {
        application.add( str.sub( s, PortUtil.getStringLength( header + "\\APPLICATION" ) ) );
    } else if ( s.startsWith( header + "\\DATABASE\\EXPRESSION" ) ) {
        expression.add( str.sub( s, PortUtil.getStringLength( header + "\\DATABASE\\EXPRESSION" ) ) );
    } else if ( s.startsWith( header + "\\DATABASE\\VOICE" ) ) {
        voice.add( str.sub( s, PortUtil.getStringLength( header + "\\DATABASE\\VOICE\\" ) ) );
    }
}

// path_vstiを取得
for ( Iterator<String> itr = application.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    String[] spl = PortUtil.splitString( s, '\t' );
    if ( spl.length >= 3 && spl[1].equals( "PATH" ) ) {
        if ( spl[2].toLowerCase().endsWith( ".dll" ) ) {
            path_vsti.value = spl[2];
        } else if ( spl[2].toLowerCase().endsWith( ".exe" ) ) {
            path_editor.value = spl[2];
        }
    }
}

// path_vicedbを取得
TreeMap<String, String> install_dirs = new TreeMap<String, String>();
for ( Iterator<String> itr = voice.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    String[] spl = PortUtil.splitString( s, '\t' );
    if ( spl.length < 2 ) {
        continue;
    }

    if ( spl[0].equals( "VOICEDIR" ) ) {
        path_voicedb.value = spl[1];
    } else if ( spl.length >= 3 ) {
        String[] spl2 = PortUtil.splitString( spl[0], '\\' );
        if ( spl2.length == 1 ) {
            String id = spl2[0]; // BHXXXXXXXXXXXXみたいなシリアル
            if ( !install_dirs.containsKey( id ) ) {
                install_dirs.put( id, "" );
            }
            if ( spl[1].equals( "INSTALLDIR" ) ) {
                // VOCALOID1の場合は、ここには到達しないはず
                String installdir = spl[2];
                install_dirs.put( id, fsys.combine( installdir, id ) );
            }
        }
    }
}

// installed_singersに追加
for ( Iterator<String> itr = install_dirs.keySet().iterator(); itr.hasNext(); ) {
    String id = itr.next();
    String install = install_dirs.get( id );
    if ( install.equals( "" ) ) {
        install = fsys.combine( path_voicedb.value, id );
    }
    installed_singers.add( install );
}

// path_expdbを取得
Vector<String> exp_ids = new Vector<String>();
// 最初はpath_expdbの取得と、id（BHXXXXXXXXXXXXXXXX）のようなシリアルを取得
for ( Iterator<String> itr = expression.iterator(); itr.hasNext(); ) {
    String s = itr.next();
    String[] spl = PortUtil.splitString( s, new char[] { '\t' }, true );
    if ( spl.length >= 3 ) {
        if ( spl[1].equals( "EXPRESSIONDIR" ) ) {
            path_expdb.value = spl[2];
        } else if ( spl.length >= 3 ) {
            String[] spl2 = PortUtil.splitString( spl[0], '\\' );
            if ( spl2.length == 1 ) {
                if ( !exp_ids.contains( spl2[0] ) ) {
                    exp_ids.add( spl2[0] );
                }
            }
        }
    }
}
        }

        /// <summary>
        /// レジストリkey内の値を再帰的に検索し、ファイルfpに順次出力する
        /// </summary>
        /// <param name="reg_key_name"></param>
        /// <param name="parent_name"></param>
        /// <param name="list"></param>
        private static void initPrint( String reg_key_name, String parent_name, Vector<String> list )
        {
        }

        /// <summary>
        /// アタック設定を順に返す反復子を取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static Iterator<NoteHeadHandle> attackConfigIterator( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#attackConfigIterator; not initialized yet" );
    return null;
}
if ( s_exp_config_sys.containsKey( type ) ) {
    return s_exp_config_sys.get( type ).attackConfigIterator();
} else {
    return (new Vector<NoteHeadHandle>()).iterator();
}
        }

        /// <summary>
        /// ビブラート設定を順に返す反復子を取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static Iterator<VibratoHandle> vibratoConfigIterator( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#vibratoConfigIterator; not initialized yet" );
    return null;
}
if ( s_exp_config_sys.containsKey( type ) ) {
    return s_exp_config_sys.get( type ).vibratoConfigIterator();
} else {
    return (new Vector<VibratoHandle>()).iterator();
}
        }

        /// <summary>
        /// 強弱記号設定を順に返す反復子を取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static Iterator<IconDynamicsHandle> dynamicsConfigIterator( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#dynamicsConfigIterator; not initialized yet" );
    return null;
}
if ( s_exp_config_sys.containsKey( type ) ) {
    return s_exp_config_sys.get( type ).dynamicsConfigIterator();
} else {
    return (new Vector<IconDynamicsHandle>()).iterator();
}
        }

        /// <summary>
        /// 指定した歌声合成システムに登録されている指定した名前の歌手について、その派生元の歌手名を取得します。
        /// </summary>
        /// <param name="language"></param>
        /// <param name="program"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public static String getOriginalSinger( int language, int program, SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getOriginalSinger; not initialized yet" );
    return null;
}
String voiceidstr = "";
if ( !s_singer_config_sys.containsKey( type ) ) {
    return "";
}
SingerConfigSys scs = s_singer_config_sys.get( type );
SingerConfig[] singer_configs = scs.getSingerConfigs();
for ( int i = 0; i < singer_configs.length; i++ ) {
    if ( language == singer_configs[i].Language && program == singer_configs[i].Program ) {
        voiceidstr = singer_configs[i].VOICEIDSTR;
        break;
    }
}
if ( voiceidstr.equals( "" ) ) {
    return "";
}
SingerConfig[] installed_singers = scs.getInstalledSingers();
for ( int i = 0; i < installed_singers.length; i++ ) {
    if ( voiceidstr.equals( installed_singers[i].VOICEIDSTR ) ) {
        return installed_singers[i].VOICENAME;
    }
}
return "";
        }

        /// <summary>
        /// 指定した歌声合成システムに登録されている指定した名前の歌手について、その歌手を表現するVsqIDのインスタンスを取得します。
        /// </summary>
        /// <param name="language"></param>
        /// <param name="program"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public static VsqID getSingerID( int language, int program, SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getSingerID; not initialized yet" );
    return null;
}
if ( !s_singer_config_sys.containsKey( type ) ) {
    return null;
} else {
    return s_singer_config_sys.get( type ).getSingerID( language, program );
}
        }

        /// <summary>
        /// 指定した歌声合成システムの、エディタの実行ファイルのパスを取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static String getEditorPath( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getEditorPath; not initialized yet" );
    return "";
}
if ( !s_path_editor.containsKey( type ) ) {
    return "";
} else {
    return s_path_editor.get( type );
}
        }

        /// <summary>
        /// 指定した歌声合成システムの、VSTi DLL本体のパスを取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static String getDllPathVsti( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getDllPathVsti; not initialized yet" );
    return "";
}
if ( !s_path_vsti.containsKey( type ) ) {
    return "";
} else {
    return s_path_vsti.get( type );
}
        }

        /// <summary>
        /// 指定された歌声合成システムに登録されている歌手設定のリストを取得します。
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        public static SingerConfig[] getSingerConfigs( SynthesizerType type )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getSingerConfigs; not initialized yet" );
    return new SingerConfig[]{};
}
if ( !s_singer_config_sys.containsKey( type ) ) {
    return new SingerConfig[] { };
} else {
    return s_singer_config_sys.get( type ).getSingerConfigs();
}
        }

        /// <summary>
        /// 指定した名前の歌手の歌唱言語を取得します。
        /// </summary>
        /// <param name="name">name of singer</param>
        /// <returns></returns>
        public static VsqVoiceLanguage getLanguageFromName( String name )
        {
if ( !isInitialized ) {
    serr.println( "VocaloSysUtil#getLanguageFromName; not initialized yet" );
    return VsqVoiceLanguage.Japanese;
}
String search = name.toLowerCase();
if ( search.equals( "meiko" ) ||
    search.equals( "kaito" ) ||
    search.equals( "miku" ) ||
    search.equals( "rin" ) ||
    search.equals( "len" ) ||
    search.equals( "rin_act2" ) ||
    search.equals( "len_act2" ) ||
    search.equals( "gackpoid" ) ||
    search.equals( "luka_jpn" ) ||
    search.equals( "megpoid" ) ||
    search.equals( "sfa2_miki" ) ||
    search.equals( "yuki" ) ||
    search.equals( "kiyoteru" ) ||
    search.equals( "miku_sweet" ) ||
    search.equals( "miku_dark" ) ||
    search.equals( "miku_soft" ) ||
    search.equals( "miku_light" ) ||
    search.equals( "miku_vivid" ) ||
    search.equals( "miku_solid" ) ) {
    return VsqVoiceLanguage.Japanese;
} else if ( search.equals( "sweet_ann" ) ||
    search.equals( "prima" ) ||
    search.equals( "luka_eng" ) ||
    search.equals( "sonika" ) ||
    search.equals( "lola" ) ||
    search.equals( "leon" ) ||
    search.equals( "miriam" ) ||
    search.equals( "big_al" ) ) {
    return VsqVoiceLanguage.English;
}
return VsqVoiceLanguage.Japanese;
        }

        /// <summary>
        /// 指定したPAN値における、左チャンネルの増幅率を取得します。
        /// </summary>
        /// <param name="pan"></param>
        /// <returns></returns>
        public static double getAmplifyCoeffFromPanLeft( int pan )
        {
return pan / -64.0 + 1.0;
        }

        /// <summary>
        /// 指定したPAN値における、右チャンネルの増幅率を取得します。
        /// </summary>
        /// <param name="pan"></param>
        /// <returns></returns>
        public static double getAmplifyCoeffFromPanRight( int pan )
        {
return pan / 64.0 + 1.0;
        }

        /// <summary>
        /// 指定したFEDER値における、増幅率を取得します。
        /// </summary>
        /// <param name="feder"></param>
        /// <returns></returns>
        public static double getAmplifyCoeffFromFeder( int feder )
        {
return Math.exp( 1.18448420e-01 * feder / 10.0 );
        }
    }


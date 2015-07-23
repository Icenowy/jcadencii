/*
 * SingerConfig.cs
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

import java.io.*;

import java.util.*;


public class SingerConfig implements Cloneable {
    public String ID = "";
    public String FORMAT = "";

    /// <summary>
    /// VOCALOIDの場合，音源のディレクトリへのパス．
    /// UTAUの場合，oto.iniが保存されているディレクトリへのパス
    /// </summary>
    public String VOICEIDSTR = "";
    public String VOICENAME = "Unknown";
    public int Breathiness;
    public int Brightness;
    public int Clearness;
    public int Opening;
    public int GenderFactor;
    public int Program;
    public int Resonance1Amplitude;
    public int Resonance1Frequency;
    public int Resonance1BandWidth;
    public int Resonance2Amplitude;
    public int Resonance2Frequency;
    public int Resonance2BandWidth;
    public int Resonance3Amplitude;
    public int Resonance3Frequency;
    public int Resonance3BandWidth;
    public int Resonance4Amplitude;
    public int Resonance4Frequency;
    public int Resonance4BandWidth;
    public int Harmonics;
    public String VvdPath = "";
    public int Language;

    public SingerConfig() {
    }

    public SingerConfig(String voiceName, int language, int program) {
        VOICENAME = voiceName;
        Language = language;
        Program = program;
    }

    public Object clone() {
        SingerConfig ret = new SingerConfig();
        ret.ID = ID;
        ret.FORMAT = FORMAT;
        ret.VOICEIDSTR = VOICEIDSTR;
        ret.VOICENAME = VOICENAME;
        ret.Breathiness = Breathiness;
        ret.Brightness = Brightness;
        ret.Clearness = Clearness;
        ret.Opening = Opening;
        ret.GenderFactor = GenderFactor;
        ret.Program = Program;
        ret.Resonance1Amplitude = Resonance1Amplitude;
        ret.Resonance1Frequency = Resonance1Frequency;
        ret.Resonance1BandWidth = Resonance1BandWidth;
        ret.Resonance2Amplitude = Resonance2Amplitude;
        ret.Resonance2Frequency = Resonance2Frequency;
        ret.Resonance2BandWidth = Resonance2BandWidth;
        ret.Resonance3Amplitude = Resonance3Amplitude;
        ret.Resonance3Frequency = Resonance3Frequency;
        ret.Resonance3BandWidth = Resonance3BandWidth;
        ret.Resonance4Amplitude = Resonance4Amplitude;
        ret.Resonance4Frequency = Resonance4Frequency;
        ret.Resonance4BandWidth = Resonance4BandWidth;
        ret.Harmonics = Harmonics;
        ret.VvdPath = VvdPath;
        ret.Language = Language;

        return ret;
    }

    public static SingerConfig fromVvd(String file, int language, int program) {
        SingerConfig sc = new SingerConfig();
        sc.ID = "VOCALOID:VIRTUAL:VOICE";
        sc.FORMAT = "2.0.0.0";
        sc.VOICEIDSTR = "";
        sc.VOICENAME = "Miku";
        sc.Breathiness = 0;
        sc.Brightness = 0;
        sc.Clearness = 0;
        sc.Opening = 0;
        sc.GenderFactor = 0;
        sc.VvdPath = file;
        sc.Language = language;
        sc.Program = program;

        RandomAccessFile fs = null;

        try {
            fs = new RandomAccessFile(file, "r");

            int length = (int) fs.length();
            byte[] dat = new byte[length];
            fs.read(dat, 0, length);
            TransCodeUtil.decodeBytes(dat);

            int[] idat = new int[length];

            for (int i = 0; i < length; i++) {
                idat[i] = dat[i];
            }

            String str1 = PortUtil.getDecodedString("Shift_JIS", idat);
            String crlf = "" + (char) 0x0d + "" + (char) 0x0a;
            String[] spl = PortUtil.splitString(str1, new String[] { crlf },
                    true);

            int count = spl.length;

            for (int i = 0; i < spl.length; i++) {
                String s = spl[i];
                int first = s.indexOf('"');
                int first_end = get_quated_string(s, first);
                int second = s.indexOf('"', first_end + 1);
                int second_end = get_quated_string(s, second);
                char[] chs = s.toCharArray();
                String id = new String(chs, first, first_end - first + 1);
                String value = new String(chs, second, second_end - second + 1);
                id = str.sub(id, 1, PortUtil.getStringLength(id) - 2);
                value = str.sub(value, 1, PortUtil.getStringLength(value) - 2);
                value = value.replace("\\" + "\"", "\"");

                int parsed_int = 64;

                try {
                    parsed_int = str.toi(value);
                } catch (Exception ex) {
                }

                if (id.equals("ID")) {
                    sc.ID = value;
                } else if (id.equals("FORMAT")) {
                    sc.FORMAT = value;
                } else if (id.equals("VOICEIDSTR")) {
                    sc.VOICEIDSTR = value;
                } else if (id.equals("VOICENAME")) {
                    sc.VOICENAME = value;
                } else if (id.equals("Breathiness") || id.equals("Noise")) {
                    sc.Breathiness = parsed_int;
                } else if (id.equals("Brightness")) {
                    sc.Brightness = parsed_int;
                } else if (id.equals("Clearness")) {
                    sc.Clearness = parsed_int;
                } else if (id.equals("Opening")) {
                    sc.Opening = parsed_int;
                } else if (id.equals("Gender:Factor")) {
                    sc.GenderFactor = parsed_int;
                } else if (id.equals("Resonance1:Frequency")) {
                    sc.Resonance1Frequency = parsed_int;
                } else if (id.equals("Resonance1:Band:Width")) {
                    sc.Resonance1BandWidth = parsed_int;
                } else if (id.equals("Resonance1:Amplitude")) {
                    sc.Resonance1Amplitude = parsed_int;
                } else if (id.equals("Resonance2:Frequency")) {
                    sc.Resonance2Frequency = parsed_int;
                } else if (id.equals("Resonance2:Band:Width")) {
                    sc.Resonance2BandWidth = parsed_int;
                } else if (id.equals("Resonance2:Amplitude")) {
                    sc.Resonance2Amplitude = parsed_int;
                } else if (id.equals("Resonance3:Frequency")) {
                    sc.Resonance3Frequency = parsed_int;
                } else if (id.equals("Resonance3:Band:Width")) {
                    sc.Resonance3BandWidth = parsed_int;
                } else if (id.equals("Resonance3:Amplitude")) {
                    sc.Resonance3Amplitude = parsed_int;
                } else if (id.equals("Resonance4:Frequency")) {
                    sc.Resonance4Frequency = parsed_int;
                } else if (id.equals("Resonance4:Band:Width")) {
                    sc.Resonance4BandWidth = parsed_int;
                } else if (id.equals("Resonance4:Amplitude")) {
                    sc.Resonance4Amplitude = parsed_int;
                } else if (id.equals("Harmonics")) {
                    sc.Harmonics = parsed_int;
                }
            }
        } catch (Exception ex) {
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception ex2) {
                }
            }
        }

        return sc;
    }

    /// <summary>
    /// 位置positionにある'"'から，次に現れる'"'の位置を調べる．エスケープされた\"はスキップされる．'"'が見つからなかった場合-1を返す
    /// </summary>
    /// <param name="s"></param>
    /// <param name="position"></param>
    /// <returns></returns>
    static int get_quated_string(String s, int position) {
        if (position < 0) {
            return -1;
        }

        char[] chs = s.toCharArray();

        if (position >= chs.length) {
            return -1;
        }

        if (chs[position] != '"') {
            return -1;
        }

        int end = -1;

        for (int i = position + 1; i < chs.length; i++) {
            if ((chs[i] == '"') && (chs[i - 1] != '\\')) {
                end = i;

                break;
            }
        }

        return end;
    }

    public String[] ToStringArray() {
        Vector<String> ret = new Vector<String>();
        ret.add("\"ID\":=:\"" + ID + "\"");
        ret.add("\"FORMAT\":=:\"" + FORMAT + "\"");
        ret.add("\"VOICEIDSTR\":=:\"" + VOICEIDSTR + "\"");
        ret.add("\"VOICENAME\":=:\"" + VOICENAME.replace("\"", "\\\"") + "\"");
        ret.add("\"Breathiness\":=:\"" + Breathiness + "\"");
        ret.add("\"Brightness\":=:\"" + Brightness + "\"");
        ret.add("\"Clearness\":=:\"" + Clearness + "\"");
        ret.add("\"Opening\":=:\"" + Opening + "\"");
        ret.add("\"Gender:Factor\":=:\"" + GenderFactor + "\"");

        return ret.toArray(new String[] {  });
    }

    public String toString() {
        String[] r = ToStringArray();
        String ret = "";
        int count = r.length;

        for (int i = 0; i < count; i++) {
            String s = r[i];
            ret += (s + "\n");
        }

        return ret;
    }
}

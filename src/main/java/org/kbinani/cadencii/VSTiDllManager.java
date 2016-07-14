/*
 * VSTiDllManager.cs
 * Copyright © 2008-2011 kbinani
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

import org.kbinani.media.*;

import org.kbinani.vsq.*;

import java.io.*;

import java.util.*;


class VocaloidDaemon {
    public BufferedOutputStream outputStream;
    public BufferedInputStream inputStream;
    private Process mProcess;

    // 一時ディレクトリの実際のパス
    private String mTempPathUnixName;

    public VocaloidDaemon(Process p, String temp_path_unix_name) {
        if (p == null) {
            return;
        }

        mProcess = p;
        outputStream = new BufferedOutputStream(mProcess.getOutputStream());
        inputStream = new BufferedInputStream(mProcess.getInputStream());
        mTempPathUnixName = temp_path_unix_name;
    }

    public String getTempPathUnixName() {
        return mTempPathUnixName;
    }

    public void terminate() {
        if (fsys.isDirectoryExists(mTempPathUnixName)) {
            String stop = fsys.combine(mTempPathUnixName, "stop");

            if (fsys.isFileExists(stop)) {
                try {
                    PortUtil.deleteFile(stop);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try {
                PortUtil.deleteDirectory(mTempPathUnixName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mProcess == null) {
            return;
        }

        mProcess.destroy();
    }
}


/// <summary>
/// VSTiのDLLを管理するクラス
/// </summary>
public class VSTiDllManager {
    public static final String RENDERER_DSB2 = "DSB2";
    public static final String RENDERER_DSB3 = "DSB3";
    public static final String RENDERER_UTU0 = "UTU0";
    public static final String RENDERER_STR0 = "STR0";
    public static final String RENDERER_AQT0 = "AQT0";

    /// <summary>
    /// EmtryRenderingRunnerが使用される
    /// </summary>
    public static final String RENDERER_NULL = "NUL0";

    //public static int SAMPLE_RATE = 44100;
    static final float a0 = -17317.563f;
    static final float a1 = 86.7312112f;
    static final float a2 = -0.237323499f;

    /// <summary>
    /// 使用するボカロの最大バージョン．2までリリースされているので今は2
    /// </summary>
    static final int MAX_VOCALO_VERSION = 2;

    /// <summary>
    /// Wineでインストールされている（かもしれない）AquesToneのvsti dllのパス．windowsのパス区切り形式で代入すること
    /// </summary>
    public static String WineAquesToneDll = "C:\\Program Files\\Steinberg\\VSTplugins\\AquesTone.dll";

    /// <summary>
    /// vocaloidrv.exeのプロセス
    /// </summary>
    public static VocaloidDaemon[] vocaloidrvDaemon = null;

    /// <summary>
    /// 指定した合成器の種類に合致する合成器の新しいインスタンスを取得します
    /// </summary>
    /// <param name="kind">合成器の種類</param>
    /// <returns>指定した種類の合成器の新しいインスタンス</returns>
    public static WaveGenerator getWaveGenerator(RendererKind kind) {
        if (kind == RendererKind.AQUES_TONE) {
        } else if (kind == RendererKind.VCNT) {
            return new VConnectWaveGenerator();
        } else if (kind == RendererKind.UTAU) {
            return new UtauWaveGenerator();
        } else if ((kind == RendererKind.VOCALOID1) ||
                (kind == RendererKind.VOCALOID2)) {
            return new VocaloidWaveGenerator();
        }

        return new EmptyWaveGenerator();
    }

    /// <summary>
    /// createtempdir.exeユーティリティを呼び出して，wine内の一時ディレクトリに
    /// 新しいディレクトリを作成します．drive_cから直接作ってもいいけど，
    /// 一時ディレクトリがどこかはwindowsでGetTempPathを呼ばない限り分からないので．
    /// </summary>
    private static String createTempPath() {
        Vector<String> list = AppManager.getWineProxyArgument();
        list.add(fsys.combine(PortUtil.getApplicationStartupPath(),
                "createtempdir.exe"));

        try {
            Process p = Runtime.getRuntime().exec(list.toArray(new String[0]));
            p.waitFor();

            InputStream i = p.getInputStream();
            int avail = i.available();
            char[] c = new char[avail];

            for (int j = 0; j < avail; j++) {
                c[j] = (char) i.read();
            }

            String ret = new String(c);

            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public static void restartVocaloidrvDaemon() {
        if (vocaloidrvDaemon == null) {
            vocaloidrvDaemon = new VocaloidDaemon[MAX_VOCALO_VERSION];
        }

        for (int i = 0; i < vocaloidrvDaemon.length; i++) {
            VocaloidDaemon vd = vocaloidrvDaemon[i];

            if (vd == null) {
                continue;
            }

            vd.terminate();
        }

        Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int ver = 1; ver <= vocaloidrvDaemon.length;
                                ver++) {
                            // /bin/sh vocaloidrv.sh WINEPREFIX WINETOP vocaloidrv.exe midi_master.bin midi_body.bin TOTAL_SAMPLES
                            Vector<String> list = AppManager.getWineProxyArgument();
                            String vocaloidrv_exe = Utility.normalizePath(fsys.combine(
                                        PortUtil.getApplicationStartupPath(),
                                        "vocaloidrv.exe"));
                            list.add(vocaloidrv_exe);

                            SynthesizerType st = (ver == 1)
                                ? SynthesizerType.VOCALOID1
                                : SynthesizerType.VOCALOID2;
                            String dll = Utility.normalizePath(VocaloSysUtil.getDllPathVsti(
                                        st));
                            list.add(dll);
                            list.add("-e");

                            String tmp = createTempPath();
                            list.add(Utility.normalizePath(tmp));

                            try {
                                Process p = Runtime.getRuntime()
                                                   .exec(list.toArray(
                                            new String[0]));
                                String tmp_unix = VocaloSysUtil.combineWinePath(Utility.normalizePath(
                                            AppManager.editorConfig.WinePrefix),
                                        tmp);
                                vocaloidrvDaemon[ver - 1] = new VocaloidDaemon(p,
                                        tmp_unix);

                                final InputStream iserr = p.getErrorStream();
                                Thread t2 = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    final int BUFLEN = 1024;
                                                    byte[] buffer = new byte[BUFLEN];

                                                    while (true) {
                                                        while (iserr.available() < BUFLEN) {
                                                            Thread.sleep(100);
                                                        }

                                                        int i = iserr.read(buffer);

                                                        if (i < BUFLEN) {
                                                            break;
                                                        }
                                                    }
                                                } catch (Exception ex2) {
                                                    ex2.printStackTrace();
                                                }
                                            }
                                        });
                                t2.start(); //*/
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                vocaloidrvDaemon[ver - 1] = null;
                            }
                        }
                    }
                });
        t.start();
    }

    public static void init() {
        restartVocaloidrvDaemon();
    }

    public static boolean isRendererAvailable(RendererKind renderer,
        String wine_prefix, String wine_top) {
        if ((renderer == RendererKind.VOCALOID2) ||
                (renderer == RendererKind.VOCALOID1_100) ||
                (renderer == RendererKind.VOCALOID1_101)) {
            String dll = (renderer == RendererKind.VOCALOID2)
                ? VocaloSysUtil.getDllPathVsti(SynthesizerType.VOCALOID2)
                : VocaloSysUtil.getDllPathVsti(SynthesizerType.VOCALOID1);

            if ((dll != null) && (dll.length() > 3)) {
                String act_dll = VocaloSysUtil.combineWinePath(wine_prefix, dll);
                String wine_exe = fsys.combine(fsys.combine(wine_top, "bin"),
                        "wine");

                return fsys.isFileExists(wine_exe) &&
                fsys.isFileExists(act_dll);
            }
        }

        if (renderer == RendererKind.UTAU) {
            // ここでは，resamplerの内どれかひとつでも使用可能であればOKの判定にする
            boolean resampler_exists = false;
            int size = AppManager.editorConfig.getResamplerCount();

            for (int i = 0; i < size; i++) {
                String path = AppManager.editorConfig.getResamplerAt(i);

                if (fsys.isFileExists(path)) {
                    resampler_exists = true;

                    break;
                }
            }

            if (resampler_exists &&
                    !AppManager.editorConfig.PathWavtool.equals("") &&
                    fsys.isFileExists(AppManager.editorConfig.PathWavtool)) {
                if (AppManager.editorConfig.UtauSingers.size() > 0) {
                    return true;
                }
            }
        }

        if (renderer == RendererKind.VCNT) {
            String synth_path = fsys.combine(PortUtil.getApplicationStartupPath(),
                    VConnectWaveGenerator.STRAIGHT_SYNTH);

            if (fsys.isFileExists(synth_path)) {
                int count = AppManager.editorConfig.UtauSingers.size();

                if (count > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void terminate() {
        for (int i = 0; i < vocaloidrvDaemon.length; i++) {
            VocaloidDaemon vd = vocaloidrvDaemon[i];

            if (vd == null) {
                continue;
            }

            vd.terminate();
        }
    }

    public static int getErrorSamples(float tempo) {
        if (tempo <= 240) {
            return 4666;
        } else {
            float x = tempo - 240;

            return (int) ((((a2 * x) + a1) * x) + a0);
        }
    }

    public static float getPlayTime() {
        double pos = PlaySound.getPosition();

        return (float) pos;
    }
}

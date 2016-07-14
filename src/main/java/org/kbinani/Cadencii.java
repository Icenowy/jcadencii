/*
 * Cadencii.cs
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
package org.kbinani;
import org.kbinani.apputil.Messaging;
import org.kbinani.cadencii.AppManager;
import org.kbinani.cadencii.BAssemblyInfo;
import org.kbinani.cadencii.ExceptionNotifyFormController;
import org.kbinani.cadencii.FormMain;
import org.kbinani.cadencii.FormMainController;
import org.kbinani.cadencii.Resources;


public class Cadencii implements Thread.UncaughtExceptionHandler {
    private static String mPathVsq = "";
    private static String mPathResource = "";
    private static boolean mPrintVersion = false;

    /**
     * 起動時に渡されたコマンドライン引数を評価します。
     * 戻り値は、コマンドライン引数のうちVSQ,またはXVSQファイルとして指定された引数、または空文字です。
     * @param arg
     */
    private static void parseArguments(String[] arg) {
        String currentparse = "";

        for (int i = 0; i < arg.length; i++) {
            String argi = arg[i];

            if (str.startsWith(argi, "-")) {
                currentparse = argi;

                if (str.compare(argi, "--version")) {
                    mPrintVersion = true;
                    currentparse = "";
                }
            } else {
                if (str.compare(currentparse, "")) {
                    mPathVsq = argi;
                } else if (str.compare(currentparse, "-resources")) {
                    mPathResource = argi;
                }

                currentparse = "";
            }
        }
    }

    private static void handleUnhandledException(Exception ex) {
        ExceptionNotifyFormController controller = new ExceptionNotifyFormController();
        controller.setReportTarget(ex);
        controller.getUi().showDialog(null);
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Cadencii());

        // 引数を解釈
        parseArguments(args);

        if (mPrintVersion) {
            System.out.print(BAssemblyInfo.fileVersion);

            return;
        }

        String file = mPathVsq;

        if (!str.compare(mPathResource, "")) {
            Resources.setBasePath(mPathResource);
        }

        try {
            Messaging.loadMessages();
        } catch (Exception ex) {
            Logger.write(Cadencii.class + ".main; ex=" + ex + "\n");
            serr.println("Cadencii.main; ex=" + ex);
        }

        AppManager.init();
        AppManager.mMainWindowController = new FormMainController();
        AppManager.mMainWindow = new FormMain(AppManager.mMainWindowController,
                file);
        AppManager.mMainWindow.setVisible(true);
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {
        Exception ex = new Exception(
                "unknown exception handled at 'Cadencii::Cadencii_UnhandledException");

        if ((arg1 != null) && arg1 instanceof Exception) {
            ex = (Exception) arg1;
        }

        handleUnhandledException(ex);
    }
}

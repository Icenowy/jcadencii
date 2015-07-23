/*
 * ExceptionNotifyFormController.cs
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

import org.kbinani.apputil.*;

import java.io.*;

import java.net.*;


public class ExceptionNotifyFormController extends ControllerBase
    implements ExceptionNotifyFormUiListener {
    protected ExceptionNotifyFormUi ui;
    protected String exceptionMessage = "";

    public ExceptionNotifyFormController() {
        ui = (ExceptionNotifyFormUi) new ExceptionNotifyFormUiImpl(this);
        this.applyLanguage();
    }

    public void setReportTarget(Exception ex) {
        int count = 0;
        String message = "";
        message += ("[version]\r\n" +
        str.replace(Utility.getVersion(), "\n\n", "\n") + "\r\n");
        message += ("[system]\r\n" + this.getSystemInfo() + "\r\n");
        message += this.extractMessageString(ex, count);
        this.exceptionMessage = message;
        this.ui.setExceptionMessage(this.exceptionMessage);
    }

    public ExceptionNotifyFormUi getUi() {
        return this.ui;
    }

    public void sendButtonClick() {
        String url = "http://www.kbinani.info/cadenciiProblemReport.php";

        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            connection.setDoOutput(true);

            OutputStream stream = connection.getOutputStream();
            String postData = "message=" +
                URLEncoder.encode(this.exceptionMessage, "UTF-8");
            PrintStream printStream = new PrintStream(stream);
            printStream.print(postData);
            printStream.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream));
            String str = "";
            String s;

            while ((s = reader.readLine()) != null) {
                str += s;
            }

            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ui.close();
    }

    public void cancelButtonClick() {
        ui.close();
    }

    /// <summary>
    /// 例外からその情報を再帰的に取り出す
    /// </summary>
    /// <param name="ex"></param>
    /// <returns></returns>
    protected String extractMessageString(Exception ex, int count) {
        String str = "[exception-" + count + "]\r\n" + ex.getMessage() +
            "\r\n";
        StringWriter stream = new StringWriter();
        ex.printStackTrace(new PrintWriter(stream));
        str += (stream.toString() + "\r\n");

        Throwable t = ex.getCause();

        if ((t != null) && t instanceof Exception) {
            str += extractMessageString((Exception) t, ++count);
        }

        return str;
    }

    /// <summary>
    /// システムの情報を取得する
    /// </summary>
    /// <returns></returns>
    protected String getSystemInfo() {
        return "OSVersion=" + System.getProperty("os.name") + "\njavaVersion=" +
        System.getProperty("java.version");
    }

    protected void applyLanguage() {
        this.ui.setTitle(gettext("Problem Report for Cadencii"));
        this.ui.setDescription(gettext("Problem Details"));
        this.ui.setCancelButtonText(gettext("Cancel"));
        this.ui.setSendButtonText(gettext("Send to Developper"));
    }

    protected String gettext(String id) {
        return Messaging.getMessage(id);
    }
}

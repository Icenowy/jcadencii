/*
 * MessageBody.cs
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

import org.kbinani.ByRef;
import org.kbinani.PortUtil;
import org.kbinani.str;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;


public class MessageBody {
    public String lang;
    public String poHeader = "";
    public TreeMap<String, MessageBodyEntry> list = new TreeMap<String, MessageBodyEntry>();

    public MessageBody(String lang_) {
        lang = lang_;
    }

    public MessageBody(String lang, String[] ids, String[] messages) {
        this.lang = lang;
        list = new TreeMap<String, MessageBodyEntry>();

        for (int i = 0; i < ids.length; i++) {
            list.put(ids[i],
                new MessageBodyEntry(messages[i], new String[] {  }));
        }
    }

    public MessageBody(String lang_, String file) {
        lang = lang_;
        poHeader = "";

        BufferedReader sr = null;

        try {
            sr = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), "UTF-8"));

            String line2 = "";

            while ((line2 = sr.readLine()) != null) {
                ByRef<String> msgid = new ByRef<String>("");
                String first_line = line2;
                ByRef<String[]> location = new ByRef<String[]>();
                String last_line = readTillMessageEnd(sr, first_line, "msgid",
                        msgid, location);
                ByRef<String> msgstr = new ByRef<String>("");
                ByRef<String[]> location_dumy = new ByRef<String[]>();
                last_line = readTillMessageEnd(sr, last_line, "msgstr", msgstr,
                        location_dumy);

                if (PortUtil.getStringLength(msgid.value) > 0) {
                    list.put(msgid.value,
                        new MessageBodyEntry(msgstr.value, location.value));
                } else {
                    poHeader = msgstr.value;

                    String[] spl = PortUtil.splitString(poHeader,
                            new char[] { (char) 0x0d, (char) 0x0a }, true);
                    poHeader = "";

                    int count = 0;

                    for (int i = 0; i < spl.length; i++) {
                        String line = spl[i];
                        String[] spl2 = PortUtil.splitString(line,
                                new char[] { ':' }, 2);

                        if (spl2.length == 2) {
                            String name = spl2[0].trim();
                            String ct = "Content-Type";
                            String cte = "Content-Transfer-Encoding";

                            if (name.toLowerCase().equals(ct.toLowerCase())) {
                                poHeader += (((count == 0) ? "" : "\n") +
                                "Content-Type: text/plain; charset=UTF-8");
                            } else if (name.toLowerCase()
                                               .equals(cte.toLowerCase())) {
                                poHeader += (((count == 0) ? "" : "\n") +
                                "Content-Transfer-Encoding: 8bit");
                            } else {
                                poHeader += (((count == 0) ? "" : "\n") + line);
                            }
                        } else {
                            poHeader += (((count == 0) ? "" : "\n") + line);
                        }

                        count++;
                    }
                }
            }
        } catch (Exception ex) {
        } finally {
            if (sr != null) {
                try {
                    sr.close();
                } catch (Exception ex2) {
                }
            }
        }
    }

    public String getMessage(String id) {
        if (list.containsKey(id)) {
            String ret = list.get(id).message;

            if (ret.equals("")) {
                return id;
            } else {
                return list.get(id).message;
            }
        }

        return id;
    }

    public MessageBodyEntry getMessageDetail(String id) {
        if (list.containsKey(id)) {
            String ret = list.get(id).message;

            if (ret.equals("")) {
                return new MessageBodyEntry(id, new String[] {  });
            } else {
                return list.get(id);
            }
        }

        return new MessageBodyEntry(id, new String[] {  });
    }

    public void write(String file) {
        BufferedWriter sw = null;

        try {
            sw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file), "UTF-8"));

            if (!poHeader.equals("")) {
                sw.write("msgid \"\"");
                sw.newLine();
                sw.write("msgstr \"\"");
                sw.newLine();

                String[] spl = PortUtil.splitString(poHeader,
                        new char[] { (char) 0x0d, (char) 0x0a }, true);

                for (int i = 0; i < spl.length; i++) {
                    String line = spl[i];
                    sw.write("\"" + line + "\\" + "n\"");
                    sw.newLine();
                }

                sw.newLine();
            } else {
                sw.write("msgid \"\"");
                sw.newLine();
                sw.write("msgstr \"\"");
                sw.newLine();
                sw.write("\"Content-Type: text/plain; charset=UTF-8\\" + "n\"");
                sw.newLine();
                sw.write("\"Content-Transfer-Encoding: 8bit\\" + "n\"");
                sw.newLine();
                sw.newLine();
            }

            for (Iterator<String> itr = list.keySet().iterator();
                    itr.hasNext();) {
                String key = itr.next();
                String skey = key.replace("\n", "\\n\"\n\"");
                MessageBodyEntry mbe = list.get(key);
                String s = mbe.message;
                Vector<String> location = mbe.location;
                int count = location.size();

                for (int i = 0; i < count; i++) {
                    sw.write("#: " + location.get(i));
                    sw.newLine();
                }

                sw.write("msgid \"" + skey + "\"");
                sw.newLine();
                s = s.replace("\n", "\\n\"\n\"");
                sw.write("msgstr \"" + s + "\"");
                sw.newLine();
                sw.newLine();
            }
        } catch (Exception ex) {
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception ex2) {
                }
            }
        }
    }

    private static void separateEntryAndMessage(String source,
        ByRef<String> entry, ByRef<String> message) {
        String line = source.trim();
        entry.value = "";
        message.value = "";

        if (PortUtil.getStringLength(line) <= 0) {
            return;
        }

        int index_space = line.indexOf(' ');
        int index_dquoter = line.indexOf('"');
        int index = Math.min(index_dquoter, index_space);
        entry.value = str.sub(line, 0, index);
        message.value = str.sub(line, index_dquoter + 1);
        message.value = str.sub(message.value, 0,
                PortUtil.getStringLength(message.value) - 1);
    }

    private static String readTillMessageEnd(java.io.BufferedReader sr,
        String first_line, String entry, ByRef<String> msg,
        ByRef<String[]> locations) throws IOException {
        msg.value = "";

        String line = first_line;
        Vector<String> location = new Vector<String>();
        boolean entry_found = false;

        if (line.startsWith(entry)) {
            // 1行目がすでに"entry"の行だった場合
            ByRef<String> dum = new ByRef<String>("");
            ByRef<String> dum2 = new ByRef<String>("");
            separateEntryAndMessage(line, dum, dum2);
            msg.value += dum2.value;
        } else {
            while ((line = sr.readLine()) != null) {
                if (line.startsWith("#:")) {
                    line = str.sub(line, 2).trim();
                    location.add(line);
                } else if (line.startsWith(entry)) {
                    ByRef<String> dum = new ByRef<String>("");
                    ByRef<String> dum2 = new ByRef<String>("");
                    separateEntryAndMessage(line, dum, dum2);
                    msg.value += dum2.value;

                    break;
                }
            }
        }

        locations.value = location.toArray(new String[] {  });

        String ret = "";

        while ((line = sr.readLine()) != null) {
            if (!line.startsWith("\"")) {
                msg.value = msg.value.replace("\\\"", "\"");
                msg.value = msg.value.replace("\\n", "\n");

                return line;
            }

            int index = line.lastIndexOf("\"");
            msg.value += str.sub(line, 1, index - 1);
        }

        msg.value = msg.value.replace("\\\"", "\"");
        msg.value = msg.value.replace("\\n", "\n");

        if (line == null) {
            line = "";
        }

        return line;
    }
}

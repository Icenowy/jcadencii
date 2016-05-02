/*
 * VsqEventList.cs
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

import org.kbinani.xml.*;

import java.io.*;

import java.util.*;


/// <summary>
/// 固有ID付きのVsqEventのリストを取り扱う
/// </summary>
public class VsqEventList implements Serializable {
    @XmlGenericType(VsqEvent.class)
    public Vector<VsqEvent> Events;
    private Vector<Integer> m_ids;

    /// <summary>
    /// コンストラクタ
    /// </summary>
    public VsqEventList() {
        Events = new Vector<VsqEvent>();
        m_ids = new Vector<Integer>();
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

    public int findIndexFromID(int internal_id) {
        int c = Events.size();

        for (int i = 0; i < c; i++) {
            VsqEvent item = Events.get(i);

            if (item.InternalID == internal_id) {
                return i;
            }
        }

        return -1;
    }

    public VsqEvent findFromID(int internal_id) {
        int index = findIndexFromID(internal_id);

        if ((0 <= index) && (index < Events.size())) {
            return Events.get(index);
        } else {
            return null;
        }
    }

    public void setForID(int internal_id, VsqEvent value) {
        int c = Events.size();

        for (int i = 0; i < c; i++) {
            if (Events.get(i).InternalID == internal_id) {
                Events.set(i, value);

                break;
            }
        }
    }

    public void sort() {
        synchronized (this) {
            Collections.sort(Events);
            updateIDList();
        }
    }

    public void clear() {
        Events.clear();
        m_ids.clear();
    }

    public Iterator<VsqEvent> iterator() {
        updateIDList();

        return Events.iterator();
    }

    public int add(VsqEvent item) {
        int id = getNextId(0);
        add(item, id);
        Collections.sort(Events);

        int count = Events.size();

        for (int i = 0; i < count; i++) {
            m_ids.set(i, Events.get(i).InternalID);
        }

        return id;
    }

    public void add(VsqEvent item, int internal_id) {
        updateIDList();
        item.InternalID = internal_id;
        Events.add(item);
        m_ids.add(internal_id);
    }

    public void removeAt(int index) {
        updateIDList();
        Events.removeElementAt(index);
        m_ids.removeElementAt(index);
    }

    private int getNextId(int next) {
        updateIDList();

        int index = -1;
        Vector<Integer> current = new Vector<Integer>(m_ids);
        int nfound = 0;

        while (true) {
            index++;

            if (!current.contains(index)) {
                nfound++;

                if (nfound == (next + 1)) {
                    return index;
                } else {
                    current.add(index);
                }
            }
        }
    }

    public int getCount() {
        return Events.size();
    }

    public VsqEvent getElement(int index) {
        return Events.get(index);
    }

    public void setElement(int index, VsqEvent value) {
        value.InternalID = Events.get(index).InternalID;
        Events.set(index, value);
    }

    public void updateIDList() {
        if (m_ids.size() != Events.size()) {
            m_ids.clear();

            int count = Events.size();

            for (int i = 0; i < count; i++) {
                m_ids.add(Events.get(i).InternalID);
            }
        } else {
            int count = Events.size();

            for (int i = 0; i < count; i++) {
                m_ids.set(i, Events.get(i).InternalID);
            }
        }
    }
}

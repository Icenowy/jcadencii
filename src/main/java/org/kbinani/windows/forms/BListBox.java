/*
 * BListBox.cs
 * Copyright © 2011 kbinani
 *
 * This file is part of org.kbinani.windows.forms.
 *
 * org.kbinani.windows.forms is free software; you can redistribute it and/or
 * modify it under the terms of the BSD License.
 *
 * org.kbinani.windows.forms is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.kbinani.windows.forms;

import org.kbinani.*;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class BListBox extends JList implements ListSelectionListener {
    private static final long serialVersionUID = 3749301116724119106L;
    private DefaultListModel mModel = null;

    /* root impl of SelectedIndexChanged event */
    // root impl of SelectedIndexChanged event is in BListBox
    public final BEvent<BEventHandler> selectedIndexChangedEvent = new BEvent<BEventHandler>();

    public BListBox() {
        super();
        mModel = new DefaultListModel();
        super.setModel(mModel);
        addListSelectionListener(this);
    }

    public int getItemCount() {
        return mModel.getSize();
    }

    public Object getItemAt(int index) {
        return mModel.getElementAt(index);
    }

    public void setItemAt(int index, Object item) {
        mModel.setElementAt(item, index);
    }

    public void removeItemAt(int index) {
        mModel.removeElementAt(index);
    }

    public void addItem(Object item) {
        mModel.addElement(item);
    }

    public void valueChanged(ListSelectionEvent e) {
        try {
            selectedIndexChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BListBox#valueChanged; ex=" + ex);
        }
    }
}

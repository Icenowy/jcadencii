/*
 * FormIconPalette.cs
 * Copyright © 2010-2011 kbinani
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

import org.kbinani.vsq.*;

import org.kbinani.windows.forms.*;
import org.kbinani.windows.forms.BDialog;
import org.kbinani.windows.forms.BMenu;
import org.kbinani.windows.forms.BMenuBar;
import org.kbinani.windows.forms.BMenuItem;

import java.awt.*;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

import java.io.*;

import java.util.*;

import javax.imageio.*;

import javax.swing.*;
import javax.swing.JPanel;


class DraggableBButton extends BButton {
    private IconDynamicsHandle mHandle = null;

    public DraggableBButton() {
        super();

        int drag_action = DnDConstants.ACTION_COPY;
        new DragSource().createDefaultDragGestureRecognizer(this, drag_action,
            new DragGestureListener() {
                //@Override
                public void dragGestureRecognized(DragGestureEvent e) {
                    // 1) cursor
                    Cursor dragCursor = DragSource.DefaultCopyDrop;

                    // 2) transfer data
                    // タグにはIconDynamicsHandleが格納されている
                    if (mHandle == null) {
                        return;
                    }

                    String icon_id = mHandle.IconID;
                    StringSelection transferable = new StringSelection(ClipboardModel.CLIP_PREFIX +
                            ":" + icon_id);

                    // 3) start drag
                    e.startDrag(dragCursor, transferable);
                }
            });
    }

    public IconDynamicsHandle getHandle() {
        return mHandle;
    }

    public void setHandle(IconDynamicsHandle value) {
        mHandle = value;
    }
}


public class FormIconPalette extends BForm {
    private static final long serialVersionUID = 1L;
    private Vector<BButton> dynaffButtons = new Vector<BButton>();
    private Vector<BButton> crescendButtons = new Vector<BButton>();
    private Vector<BButton> decrescendButtons = new Vector<BButton>();
    private int buttonWidth = 40;
    private FormMain mMainWindow = null;
    private boolean mPreviousAlwaysOnTop;
    private BMenuBar myMenuBar = null;
    private BMenu menuWindow = null;
    private BMenuItem menuWindowHide = null;
    private JPanel jPanel = null;

    public FormIconPalette(FormMain main_window) {
        super();
        initialize();
        mMainWindow = main_window;
        applyLanguage();
        Util.applyFontRecurse(this, AppManager.editorConfig.getBaseFont());
        init();
        registerEventHandlers();

        TreeMap<String, BKeys[]> dict = AppManager.editorConfig.getShortcutKeysDictionary(mMainWindow.getDefaultShortcutKeys());

        if (dict.containsKey("menuVisualIconPalette")) {
            BKeys[] keys = dict.get("menuVisualIconPalette");
            KeyStroke shortcut = BKeysUtility.getKeyStrokeFromBKeys(keys);
            menuWindowHide.setAccelerator(shortcut);
        }
    }

    /// <summary>
    /// AlwaysOnTopが強制的にfalseにされる直前の，AlwaysOnTop値を取得します．
    /// </summary>
    public boolean getPreviousAlwaysOnTop() {
        return mPreviousAlwaysOnTop;
    }

    /// <summary>
    /// AlwaysOnTopが強制的にfalseにされる直前の，AlwaysOnTop値を設定しておきます．
    /// </summary>
    public void setPreviousAlwaysOnTop(boolean value) {
        mPreviousAlwaysOnTop = value;
    }

    public void applyLanguage() {
        setTitle(gettext("Icon Palette"));
    }

    public void applyShortcut(KeyStroke shortcut) {
        menuWindowHide.setAccelerator(shortcut);
    }

    private static String gettext(String id) {
        return Messaging.getMessage(id);
    }

    private void registerEventHandlers() {
        this.loadEvent.add(new BEventHandler(this, "FormIconPalette_Load"));
        this.formClosingEvent.add(new BFormClosingEventHandler(this,
                "FormIconPalette_FormClosing"));
        menuWindowHide.clickEvent.add(new BEventHandler(this,
                "menuWindowHide_Click"));
    }

    private void init() {
        for (Iterator<IconDynamicsHandle> itr = VocaloSysUtil.dynamicsConfigIterator(
                    SynthesizerType.VOCALOID1); itr.hasNext();) {
            IconDynamicsHandle handle = itr.next();
            String icon_id = handle.IconID;
            DraggableBButton btn = new DraggableBButton();
            btn.setName(icon_id);
            btn.setHandle(handle);

            String buttonIconPath = handle.getButtonImageFullPath();

            boolean setimg = fsys.isFileExists(buttonIconPath);

            if (setimg) {
                Image img = null;

                try {
                    img = ImageIO.read(new File(buttonIconPath));
                } catch (Exception ex) {
                    Logger.write(FormIconPalette.class + "; ex=" + ex + "\n");
                    serr.println("FormIconPalette#init; ex=" + ex);
                }

                btn.setIcon(new ImageIcon(img));
            } else {
                Image img = null;
                String str = "";
                String caption = handle.IDS;

                if (caption.equals("cresc_1")) {
                    img = Resources.get_cresc1();
                } else if (caption.equals("cresc_2")) {
                    img = Resources.get_cresc2();
                } else if (caption.equals("cresc_3")) {
                    img = Resources.get_cresc3();
                } else if (caption.equals("cresc_4")) {
                    img = Resources.get_cresc4();
                } else if (caption.equals("cresc_5")) {
                    img = Resources.get_cresc5();
                } else if (caption.equals("dim_1")) {
                    img = Resources.get_dim1();
                } else if (caption.equals("dim_2")) {
                    img = Resources.get_dim2();
                } else if (caption.equals("dim_3")) {
                    img = Resources.get_dim3();
                } else if (caption.equals("dim_4")) {
                    img = Resources.get_dim4();
                } else if (caption.equals("dim_5")) {
                    img = Resources.get_dim5();
                } else if (caption.equals("Dynaff11")) {
                    str = "fff";
                } else if (caption.equals("Dynaff12")) {
                    str = "ff";
                } else if (caption.equals("Dynaff13")) {
                    str = "f";
                } else if (caption.equals("Dynaff21")) {
                    str = "mf";
                } else if (caption.equals("Dynaff22")) {
                    str = "mp";
                } else if (caption.equals("Dynaff31")) {
                    str = "p";
                } else if (caption.equals("Dynaff32")) {
                    str = "pp";
                } else if (caption.equals("Dynaff33")) {
                    str = "ppp";
                }

                if (img != null) {
                    btn.setIcon(new ImageIcon(img));
                } else {
                    btn.setText(str);
                }
            }

            btn.mouseDownEvent.add(new BMouseEventHandler(this,
                    "handleCommonMouseDown"));
            btn.setPreferredSize(new Dimension(buttonWidth, buttonWidth));

            int iw = 0;
            int ih = 0;

            if (icon_id.startsWith(IconDynamicsHandle.ICONID_HEAD_DYNAFF)) {
                // dynaff
                dynaffButtons.add(btn);
                ih = 0;
                iw = dynaffButtons.size() - 1;
            } else if (icon_id.startsWith(
                        IconDynamicsHandle.ICONID_HEAD_CRESCEND)) {
                // crescend
                crescendButtons.add(btn);
                ih = 1;
                iw = crescendButtons.size() - 1;
            } else if (icon_id.startsWith(
                        IconDynamicsHandle.ICONID_HEAD_DECRESCEND)) {
                // decrescend
                decrescendButtons.add(btn);
                ih = 2;
                iw = decrescendButtons.size() - 1;
            } else {
                continue;
            }

            LayoutManager lm = jPanel.getLayout();
            GridBagLayout gbl = null;

            if ((lm != null) && lm instanceof GridBagLayout) {
                gbl = (GridBagLayout) lm;
            } else {
                gbl = new GridBagLayout();
                jPanel.setLayout(gbl);
            }

            GridBagConstraints g = new GridBagConstraints();
            g.gridx = iw;
            g.gridy = ih;
            gbl.setConstraints(btn, g);
            jPanel.add(btn);
        }

        // ウィンドウのサイズを固定化する
        int height = 0;
        int width = 0;

        if (dynaffButtons.size() > 0) {
            height += buttonWidth;
        }

        width = Math.max(width, buttonWidth * dynaffButtons.size());

        if (crescendButtons.size() > 0) {
            height += buttonWidth;
        }

        width = Math.max(width, buttonWidth * crescendButtons.size());

        if (decrescendButtons.size() > 0) {
            height += buttonWidth;
        }

        width = Math.max(width, buttonWidth * decrescendButtons.size());
        pack();

        Insets i = getInsets();
        Dimension size = new Dimension(width + i.left + i.right,
                height + i.top + i.bottom);
        setPreferredSize(size);
        setSize(size);
        setResizable(false);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    public void FormIconPalette_Load(Object sender, BEventArgs e) {
        // コンストラクタから呼ぶと、スレッドが違うので（たぶん）うまく行かない
        setAlwaysOnTop(true);
    }

    public void FormIconPalette_FormClosing(Object sender,
        BFormClosingEventArgs e) {
        setVisible(false);
    }

    public void menuWindowHide_Click(Object sender, BEventArgs e) {
        setVisible(false);
    }

    public void handleCommonMouseDown(Object sender, BMouseEventArgs e) {
        if (AppManager.getEditMode() != EditMode.NONE) {
            return;
        }

        DraggableBButton btn = (DraggableBButton) sender;

        if (mMainWindow != null) {
            mMainWindow.toFront();
        }

        IconDynamicsHandle handle = btn.getHandle();
        VsqEvent item = new VsqEvent();
        item.Clock = 0;
        item.ID.Note = 60;
        item.ID.type = VsqIDType.Aicon;
        item.ID.IconDynamicsHandle = (IconDynamicsHandle) handle.clone();

        int length = handle.getLength();

        if (length <= 0) {
            length = 1;
        }

        item.ID.setLength(length);
        AppManager.mAddingEvent = item;

        //TODO: fixme FormIconPalette#handleCommonMouseDown
    }

    private void initialize() {
        this.setSize(new Dimension(275, 178));
        this.setContentPane(getJPanel());
        this.setJMenuBar(getMyMenuBar());
    }

    /**
     * This method initializes menuBar
     *
     * @return org.kbinani.windows.forms.BMenuBar
     */
    private BMenuBar getMyMenuBar() {
        if (myMenuBar == null) {
            myMenuBar = new BMenuBar();
            myMenuBar.add(getMenuWindow());
        }

        return myMenuBar;
    }

    /**
     * This method initializes menuWindow
     *
     * @return javax.swing.JMenu
     */
    private BMenu getMenuWindow() {
        if (menuWindow == null) {
            menuWindow = new BMenu();
            menuWindow.setText("Window");
            menuWindow.add(getMenuWindowHide());
        }

        return menuWindow;
    }

    /**
     * This method initializes menuWindowHide
     *
     * @return javax.swing.JMenuItem
     */
    private BMenuItem getMenuWindowHide() {
        if (menuWindowHide == null) {
            menuWindowHide = new BMenuItem();
            menuWindowHide.setText("Hide");
        }

        return menuWindowHide;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
        }

        return jPanel;
    }
}

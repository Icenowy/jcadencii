/*
 * PictPianoRoll.cs
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

import org.kbinani.apputil.*;

import org.kbinani.vsq.*;

import org.kbinani.windows.forms.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.geom.*;

import java.util.*;


/// <summary>
/// ピアノロール用のコンポーネント
/// </summary>
public class PictPianoRoll extends BPanel {
    /// <summary>
    /// 表情線の先頭部分のピクセル幅
    /// </summary>
    private static final int PX_ACCENT_HEADER = 21;
    private final Color COLOR_R192G192B192 = new Color(192, 192, 192);
    private final Color COLOR_A098R000G000B000 = new Color(0, 0, 0, 98);
    private final Color COLOR_R106G108B108 = new Color(106, 108, 108);
    private final Color COLOR_R180G180B180 = new Color(180, 180, 180);
    private final Color COLOR_R212G212B212 = new Color(212, 212, 212);
    private final Color COLOR_R125G123B124 = new Color(125, 123, 124);
    private final Color COLOR_R240G240B240 = new Color(240, 240, 240);
    private final Color COLOR_R072G077B098 = new Color(72, 77, 98);
    private final Color COLOR_R153G153B153 = new Color(153, 153, 153);
    private final Color COLOR_R147G147B147 = new Color(147, 147, 147);
    private final Color COLOR_LINE_LU = new Color(106, 52, 255, 128);
    private final Color COLOR_LINE_RD = new Color(40, 47, 255, 204);
    private final Color COLOR_R051G051B000 = new Color(51, 51, 0);
    private final Color COLOR_ADDING_NOTE_BORDER = new Color(0, 0, 0, 136);
    private final Color COLOR_ADDING_NOTE_FILL = new Color(255, 0, 0, 136);
    private final Color COLOR_VOCALO2_BLACK = new Color(212, 212, 212);
    private final Color COLOR_VOCALO2_WHITE = new Color(240, 240, 240);
    private final Color COLOR_VOCALO2_BAR = new Color(161, 157, 136);
    private final Color COLOR_VOCALO2_BEAT = new Color(209, 204, 172);
    private final Color COLOR_VOCALO1_BLACK = new Color(210, 205, 172);
    private final Color COLOR_VOCALO1_WHITE = new Color(240, 235, 214);
    private final Color COLOR_VOCALO1_BAR = new Color(161, 157, 136);
    private final Color COLOR_VOCALO1_BEAT = new Color(209, 204, 172);
    private final Color COLOR_UTAU_BLACK = new Color(212, 212, 212);
    private final Color COLOR_UTAU_WHITE = new Color(240, 240, 240);
    private final Color COLOR_UTAU_BAR = new Color(255, 64, 255);
    private final Color COLOR_UTAU_BEAT = new Color(128, 128, 255);
    private final Color COLOR_VCNT_BLACK = new Color(212, 212, 212);
    private final Color COLOR_VCNT_WHITE = new Color(240, 240, 240);
    private final Color COLOR_VCNT_BAR = new Color(255, 153, 0);
    private final Color COLOR_VCNT_BEAT = new Color(128, 128, 255);
    private final Color COLOR_AQUESTONE_BLACK = new Color(212, 212, 212);
    private final Color COLOR_AQUESTONE_WHITE = new Color(240, 240, 240);
    private final Color COLOR_AQUESTONE_BAR = new Color(7, 107, 175);
    private final Color COLOR_AQUESTONE_BEAT = new Color(234, 190, 62);
    public final Color[] COLORS_HIDDEN = new Color[] {
            new Color(181, 162, 123), new Color(179, 181, 123),
            new Color(157, 181, 123), new Color(135, 181, 123),
            new Color(123, 181, 133), new Color(123, 181, 154),
            new Color(123, 181, 176), new Color(123, 164, 181),
            new Color(123, 142, 181), new Color(125, 123, 181),
            new Color(169, 123, 181), new Color(181, 123, 171),
            new Color(181, 123, 149), new Color(181, 123, 127),
            new Color(181, 140, 123), new Color(181, 126, 123)
        };
    private final Color COLOR_NOTE_FILL = new Color(181, 220, 86);
    private final Color COLOR_DYNAFF_FILL = PortUtil.Pink;
    private final Color COLOR_DYNAFF_FILL_HIGHLIGHT = new Color(66, 193, 169);
    private final Font FONT_9PT = new Font("SansSerif", java.awt.Font.PLAIN,
            AppManager.FONT_SIZE9);

    /// <summary>
    /// ピアノ上のマウスのトレーサ
    /// </summary>
    public MouseTracer mMouseTracer = new MouseTracer();

    /// <summary>
    /// 幅が2ピクセルのストローク
    /// </summary>
    private BasicStroke mStroke2px = null;

    /// <summary>
    /// デフォルトのストローク
    /// </summary>
    private BasicStroke mStrokeDefault = null;

    /// <summary>
    /// 破線を表すストローク
    /// </summary>
    private BasicStroke mStrokeDashed = null;

    /// <summary>
    /// 共用の折れ線描画プラクシー
    /// </summary>
    private PolylineDrawer mCommonPolylineDrawer = null;

    /// <summary>
    /// メイン画面への参照
    /// </summary>
    private FormMain mMainForm = null;
    Object tag = null;

    public PictPianoRoll() {
        super();

        final int action = DnDConstants.ACTION_COPY;
        new DropTarget(this, action,
            new DropTargetListener() {
                public void drop(DropTargetDropEvent e) {
                    e.acceptDrop(action);

                    Transferable tr = e.getTransferable();

                    IconDynamicsHandle data = null;

                    try {
                        String icon_id = null;

                        if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                            String s = (String) tr.getTransferData(DataFlavor.stringFlavor);
                            String prefix = ClipboardModel.CLIP_PREFIX + ":";

                            if (str.startsWith(s, prefix)) {
                                icon_id = str.sub(s, str.length(prefix));
                            }
                        }

                        if (mMainForm != null) {
                            for (Iterator<IconDynamicsHandle> itr = VocaloSysUtil.dynamicsConfigIterator(
                                        SynthesizerType.VOCALOID1);
                                    itr.hasNext();) {
                                IconDynamicsHandle handle = itr.next();
                                String id = handle.IconID;

                                if (str.compare(id, icon_id)) {
                                    data = (IconDynamicsHandle) handle.clone();

                                    break;
                                }
                            }

                            Point p = pointToScreen(e.getLocation());
                            mMainForm.handleDragDrop(data, p.x, p.y);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        e.dropComplete((data != null));
                    }
                }

                //@Override
                public void dragOver(DropTargetDragEvent dtde) {
                    if (mMainForm != null) {
                        Point p = pointToScreen(dtde.getLocation());
                        mMainForm.handleDragOver(p.x, p.y);
                    }
                }

                //@Override
                public void dragExit(DropTargetEvent dte) {
                    if (mMainForm != null) {
                        mMainForm.handleDragExit();
                    }
                }

                //@Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    if (mMainForm != null) {
                        mMainForm.handleDragEnter();
                    }
                }

                //@Override
                public void dropActionChanged(DropTargetDragEvent dtde) {
                }
            });
    }

    /// <summary>
    /// メイン画面への参照を設定します
    /// </summary>
    /// <param name="form"></param>
    public void setMainForm(FormMain form) {
        mMainForm = form;
    }

    // root implementation is in BForm.cs
    public java.awt.Point pointToScreen(java.awt.Point point_on_client) {
        java.awt.Point p = getLocationOnScreen();

        return new java.awt.Point(p.x + point_on_client.x,
            p.y + point_on_client.y);
    }

    public java.awt.Point pointToClient(java.awt.Point point_on_screen) {
        java.awt.Point p = getLocationOnScreen();

        return new java.awt.Point(point_on_screen.x - p.x,
            point_on_screen.y - p.y);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object value) {
        tag = value;
    }

    /// <summary>
    /// 幅が2ピクセルのストロークを取得します
    /// </summary>
    /// <returns></returns>
    private BasicStroke getStroke2px() {
        if (mStroke2px == null) {
            mStroke2px = new BasicStroke(2.0f);
        }

        return mStroke2px;
    }

    /// <summary>
    /// デフォルトのストロークを取得します
    /// </summary>
    /// <returns></returns>
    private BasicStroke getStrokeDefault() {
        if (mStrokeDefault == null) {
            mStrokeDefault = new BasicStroke();
        }

        return mStrokeDefault;
    }

    /// <summary>
    /// 3ドット間隔の破線を表すストロークを取得します
    /// </summary>
    /// <returns></returns>
    private BasicStroke getStrokeDashed() {
        if (mStrokeDashed == null) {
            mStrokeDashed = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 10.0f, new float[] { 3.0f, 3.0f },
                    0.0f);
        }

        return mStrokeDashed;
    }

    /// <summary>
    /// 折れ線の描画装置を取得します
    /// </summary>
    /// <param name="g"></param>
    /// <returns></returns>
    private PolylineDrawer getCommonPolylineDrawer(Graphics2D g) {
        if (mCommonPolylineDrawer == null) {
            mCommonPolylineDrawer = new PolylineDrawer(g, 1024);
        } else {
            mCommonPolylineDrawer.clear();
            mCommonPolylineDrawer.setGraphics(g);
        }

        return mCommonPolylineDrawer;
    }

    /// <summary>
    /// 描画ルーチン
    /// </summary>
    /// <param name="g1"></param>
    public void paint(Graphics g1) {
        if (mMainForm == null) {
            return;
        }

        Graphics2D g = (Graphics2D) g1;

        int width = getWidth();
        int height = getHeight();

        // 再生中に画面を描画しない設定なら飛ばす
        if (AppManager.editorConfig.SkipDrawWhilePlaying &&
                AppManager.isPlaying()) {
            PortUtil.drawStringEx(g1, "(hidden for performance)",
                AppManager.superFont10, new Rectangle(0, 0, width, height),
                PortUtil.STRING_ALIGN_CENTER, PortUtil.STRING_ALIGN_CENTER);

            return;
        }

        try {
            PolylineDrawer commonDrawer = getCommonPolylineDrawer(g);
            VsqFileEx vsq = AppManager.getVsqFile();
            int selected = AppManager.getSelected();
            VsqTrack vsq_track = vsq.Track.get(selected);

            Point mouse_position = pointToClient(PortUtil.getMousePosition());
            int stdx = AppManager.mMainWindowController.getStartToDrawX();
            int stdy = AppManager.mMainWindowController.getStartToDrawY();
            int key_width = AppManager.keyWidth;

            int track_height = (int) (AppManager.mMainWindowController.getScaleY() * 100);
            int half_track_height = track_height / 2;

            // [screen_x] = 67 + [clock] * ScaleX - StartToDrawX + 6
            // [screen_y] = -1 * ([note] - 127) * TRACK_HEIGHT - StartToDrawY
            //
            // [screen_x] = [clock] * _scalex + 73 - StartToDrawX
            // [screen_y] = -[note] * TRACK_HEIGHT + 127 * TRACK_HEIGHT - StartToDrawY
            int xoffset = (AppManager.keyOffset + key_width) - stdx;
            int yoffset = (127 * track_height) - stdy;

            //      ↓
            // [screen_x] = [clock] * _scalex + xoffset
            // [screen_y] = -[note] * TRACK_HEIGHT + yoffset
            int y;

            //      ↓
            // [screen_x] = [clock] * _scalex + xoffset
            // [screen_y] = -[note] * TRACK_HEIGHT + yoffset
            int dy;
            float scalex = AppManager.mMainWindowController.getScaleX();
            float inv_scalex = AppManager.mMainWindowController.getScaleXInv();

            if ((AppManager.itemSelection.getEventCount() > 0) &&
                    AppManager.mInputTextBox.isVisible()) {
                VsqEvent original = AppManager.itemSelection.getLastEvent().original;
                int event_x = (int) ((original.Clock * scalex) + xoffset);
                int event_y = (-original.ID.Note * track_height) + yoffset;
                AppManager.mInputTextBox.setLocation(pointToScreen(
                        new Point(event_x + 4, event_y + 2)));
            }

            Color black = COLOR_VOCALO2_BLACK;
            Color white = COLOR_VOCALO2_WHITE;
            Color bar = COLOR_VOCALO2_BAR;
            Color beat = COLOR_VOCALO2_BEAT;
            RendererKind renderer = RendererKind.VOCALOID2;

            EditMode edit_mode = AppManager.getEditMode();

            if (vsq != null) {
                renderer = VsqFileEx.getTrackRendererKind(vsq_track);
            }

            if (renderer == RendererKind.UTAU) {
                black = COLOR_UTAU_BLACK;
                white = COLOR_UTAU_WHITE;
                bar = COLOR_UTAU_BAR;
                beat = COLOR_UTAU_BEAT;
            } else if (renderer == RendererKind.VOCALOID1) {
                black = COLOR_VOCALO1_BLACK;
                white = COLOR_VOCALO1_WHITE;
                bar = COLOR_VOCALO1_BAR;
                beat = COLOR_VOCALO1_BEAT;
            } else if (renderer == RendererKind.VCNT) {
                black = COLOR_VCNT_BLACK;
                white = COLOR_VCNT_WHITE;
                bar = COLOR_VCNT_BAR;
                beat = COLOR_VCNT_BEAT;
            } else if (renderer == RendererKind.AQUES_TONE) {
                black = COLOR_AQUESTONE_BLACK;
                white = COLOR_AQUESTONE_WHITE;
                bar = COLOR_AQUESTONE_BAR;
                beat = COLOR_AQUESTONE_BEAT;
            }

            // スクロール画面背景
            if (height > 0) {
                g.setColor(Color.white);
                g.fillRect(3, 0, width, height);
                g.setColor(COLOR_R240G240B240);
                g.fillRect(3, 0, key_width, height);
            }

            if (vsq != null) {
                int odd = -1;
                y = (128 * track_height) - stdy;
                dy = -track_height;

                for (int i = 0; i <= 127; i++) {
                    odd++;

                    if (odd == 12) {
                        odd = 0;
                    }

                    int order = ((i - odd) / 12) - 2;
                    y += dy;

                    if (y > height) {
                        continue;
                    } else if (0 > (y + track_height)) {
                        break;
                    }

                    boolean note_is_whitekey = VsqNote.isNoteWhiteKey(i);

                    Color b = Color.black;
                    Color border;
                    boolean paint_required = true;

                    if ((order == -2) || (order == -1) ||
                            ((6 <= order) && (order <= 8))) {
                        if (note_is_whitekey) {
                            b = COLOR_R180G180B180;
                        } else {
                            b = COLOR_R106G108B108;
                        }

                        border = COLOR_R106G108B108;
                    } else if ((order == 5) || (order == 0)) {
                        if (note_is_whitekey) {
                            b = COLOR_R212G212B212;
                        } else {
                            b = COLOR_R180G180B180;
                        }

                        border = new Color(150, 152, 150);
                    } else {
                        if (note_is_whitekey) {
                            //paint_required = false;
                            b = white; // s_brs_240_240_240;
                        } else {
                            b = black; // s_brs_212_212_212;
                        }

                        border = new Color(210, 205, 172);
                    }

                    if (paint_required) {
                        g.setColor(b);
                        g.fillRect(key_width, y, width - key_width,
                            track_height + 1);
                    }

                    if ((odd == 0) || (odd == 5)) {
                        g.setColor(border);
                        g.drawLine(key_width, y + track_height, width,
                            y + track_height);
                    }

                    int premeasure_start_x = xoffset;

                    if (premeasure_start_x < key_width) {
                        premeasure_start_x = key_width;
                    }

                    int premeasure_end_x = (int) ((vsq.getPreMeasureClocks() * scalex) +
                        xoffset);

                    if (premeasure_end_x >= key_width) {
                        if (note_is_whitekey) {
                            g.setColor(COLOR_R153G153B153);
                            g.fillRect(premeasure_start_x, y,
                                premeasure_end_x - premeasure_start_x,
                                track_height + 1);
                        } else {
                            g.setColor(COLOR_R106G108B108);
                            g.fillRect(premeasure_start_x, y,
                                premeasure_end_x - premeasure_start_x,
                                track_height + 1);
                        }

                        if ((odd == 0) || (odd == 5)) {
                            g.setColor(COLOR_R106G108B108);
                            g.drawLine(premeasure_start_x, y + track_height,
                                premeasure_end_x, y + track_height);
                        }
                    }
                }
            }

            //ピアノロールと鍵盤部分の縦線
            int hilighted_note = -1;
            g.setColor(COLOR_R212G212B212);
            g.drawLine(key_width, 0, key_width, height);

            int odd2 = -1;
            y = (128 * track_height) - stdy;
            dy = -track_height;

            for (int i = 0; i <= 127; i++) {
                odd2++;

                if (odd2 == 12) {
                    odd2 = 0;
                }

                y += dy;

                if (y > height) {
                    continue;
                } else if ((y + track_height) < 0) {
                    break;
                }

                g.setColor(COLOR_R212G212B212);
                g.drawLine(0, y, key_width, y);

                boolean hilighted = false;

                if (edit_mode == EditMode.ADD_ENTRY) {
                    if (AppManager.mAddingEvent.ID.Note == i) {
                        hilighted = true;
                        hilighted_note = i;
                    }
                } else if ((edit_mode == EditMode.EDIT_LEFT_EDGE) ||
                        (edit_mode == EditMode.EDIT_RIGHT_EDGE)) {
                    if (AppManager.itemSelection.getLastEvent().original.ID.Note == i) { //TODO: ここでNullpointer exception
                        hilighted = true;
                        hilighted_note = i;
                    }
                } else {
                    if ((3 <= mouse_position.x) &&
                            (mouse_position.x <= (width - 17)) &&
                            (0 <= mouse_position.y) &&
                            (mouse_position.y <= (height - 1))) {
                        if ((y <= mouse_position.y) &&
                                (mouse_position.y < (y + track_height))) {
                            hilighted = true;
                            hilighted_note = i;
                        }
                    }
                }

                if (hilighted) {
                    g.setColor(AppManager.getHilightColor());
                    g.fillRect(35, y, key_width - 35, track_height);
                }

                if ((odd2 == 0) || hilighted) {
                    g.setColor(COLOR_R072G077B098);
                    g.setFont(AppManager.superFont8);
                    g.drawString(VsqNote.getNoteString(i), 42,
                        (y + half_track_height) -
                        AppManager.superFont8OffsetHeight + 1);
                }

                if (!VsqNote.isNoteWhiteKey(i)) {
                    g.setColor(COLOR_R125G123B124);
                    g.fillRect(0, y, 34, track_height);
                }
            }

            g.setClip(null);

            g.clipRect(key_width, 0, width - key_width, height);

            if (vsq != null) {
                int dashed_line_step = AppManager.getPositionQuantizeClock();

                for (Iterator<VsqBarLineType> itr = vsq.getBarLineIterator(
                            AppManager.clockFromXCoord(width)); itr.hasNext();) {
                    VsqBarLineType blt = itr.next();
                    int local_clock_step = 1920 / blt.getLocalDenominator();
                    int x = (int) ((blt.clock() * scalex) + xoffset);
                    g.setStroke(getStrokeDefault());

                    if (blt.isSeparator()) {
                        //ピアノロール上
                        g.setColor(bar);
                        g.drawLine(x, 0, x, height);
                    } else {
                        //ピアノロール上
                        g.setColor(beat);
                        g.drawLine(x, 0, x, height);
                    }

                    if ((dashed_line_step > 1) && AppManager.isGridVisible()) {
                        int numDashedLine = local_clock_step / dashed_line_step;
                        g.setColor(beat);
                        g.setStroke(getStrokeDashed());

                        for (int i = 1; i < numDashedLine; i++) {
                            int x2 = (int) (((blt.clock() +
                                (i * dashed_line_step)) * scalex) + xoffset);
                            g.drawLine(x2, 0, x2, height);
                        }

                        g.setStroke(getStrokeDefault());
                    }
                }
            }

            // 現在選択されている歌声合成システムの名前をオーバーレイ表示する
            if (AppManager.drawOverSynthNameOnPianoroll) {
                g.setFont(AppManager.superFont50Bold);
                g.setColor(new Color(0, 0, 0, 32));

                String str = "VOCALOID2";

                if (renderer == RendererKind.AQUES_TONE) {
                    str = "AquesTone";
                } else if (renderer == RendererKind.VOCALOID1) {
                    str = "VOCALOID1";
                } else if (renderer == RendererKind.VCNT) {
                    str = "vConnect-STAND";
                } else if (renderer == RendererKind.UTAU) {
                    str = "UTAU";
                }

                g.drawString(str, key_width + 10,
                    (10 + (AppManager.superFont50Height / 2)) -
                    AppManager.superFont50OffsetHeight + 1);
            }

            if (AppManager.mDrawObjects != null) {
                if (AppManager.isOverlay()) {
                    // まず、選択されていないトラックの簡易表示を行う
                    synchronized (AppManager.mDrawObjects) {
                        int c = AppManager.mDrawObjects.size();

                        for (int i = 0; i < c; i++) {
                            if (i == (selected - 1)) {
                                continue;
                            }

                            Vector<DrawObject> target_list = AppManager.mDrawObjects.get(i);
                            int j_start = AppManager.mDrawStartIndex[i];
                            boolean first = true;
                            int shift_center = half_track_height;
                            int target_list_count = target_list.size();

                            for (int j = j_start; j < target_list_count; j++) {
                                DrawObject dobj = target_list.get(j);

                                if (dobj.mType != DrawObjectType.Note) {
                                    continue;
                                }

                                int x = (dobj.mRectangleInPixel.x + key_width) -
                                    stdx;
                                y = dobj.mRectangleInPixel.y - stdy;

                                int lyric_width = dobj.mRectangleInPixel.width;

                                if ((x + lyric_width) < 0) {
                                    continue;
                                } else if (width < x) {
                                    break;
                                }

                                if (AppManager.isPlaying() && first) {
                                    AppManager.mDrawStartIndex[i] = j;
                                    first = false;
                                }

                                if (((y + track_height) < 0) || (y > height)) {
                                    continue;
                                }

                                g.setColor(AppManager.HILIGHT[i]);
                                g.drawLine(x + 1, y + shift_center,
                                    (x + lyric_width) - 1, y + shift_center);
                                g.setColor(COLORS_HIDDEN[i]);
                                g.drawPolyline(new int[] {
                                        x, x + 1, (x + lyric_width) - 1,
                                        x + lyric_width, (x + lyric_width) - 1,
                                        x + 1, x
                                    },
                                    new int[] {
                                        y + shift_center, (y + shift_center) -
                                        1, (y + shift_center) - 1,
                                        y + shift_center, y + shift_center + 1,
                                        y + shift_center + 1, y + shift_center
                                    }, 7);
                            }
                        }
                    }
                }

                // 選択されているトラックの表示を行う
                boolean show_lyrics = AppManager.editorConfig.ShowLyric;
                boolean show_exp_line = AppManager.editorConfig.ShowExpLine;

                if (selected >= 1) {
                    Shape r = g.getClip();
                    g.clipRect(key_width, 0, width - key_width, height);

                    int j_start = AppManager.mDrawStartIndex[selected - 1];

                    boolean first = true;

                    synchronized (AppManager.mDrawObjects) { //ここでロックを取得しないと、描画中にUpdateDrawObjectのサイズが0になる可能性がある

                        if ((selected - 1) < AppManager.mDrawObjects.size()) {
                            Vector<DrawObject> target_list = AppManager.mDrawObjects.get(selected -
                                    1);
                            VsqBPList pit = vsq_track.MetaText.PIT;
                            VsqBPList pbs = vsq_track.MetaText.PBS;
                            ByRef<Integer> indx_pit = new ByRef<Integer>(0);
                            ByRef<Integer> indx_pbs = new ByRef<Integer>(0);

                            int c = target_list.size();

                            for (int j = j_start; j < c; j++) {
                                DrawObject dobj = target_list.get(j);
                                int x = (dobj.mRectangleInPixel.x + key_width) -
                                    stdx;
                                y = dobj.mRectangleInPixel.y - stdy;

                                int lyric_width = dobj.mRectangleInPixel.width;

                                if ((x + lyric_width) < 0) {
                                    continue;
                                } else if (width < x) {
                                    break;
                                }

                                if (AppManager.isPlaying() && first) {
                                    AppManager.mDrawStartIndex[selected - 1] = j;
                                    first = false;
                                }

                                if (((y + (2 * track_height)) < 0) ||
                                        (y > height)) {
                                    continue;
                                }

                                if (dobj.mType == DrawObjectType.Note) {
                                    Color id_fill = COLOR_NOTE_FILL;

                                    if ((!dobj.mIsValidForUtau &&
                                            (renderer == RendererKind.UTAU)) ||
                                            (!dobj.mIsValidForStraight &&
                                            (renderer == RendererKind.VCNT))) {
                                        id_fill = AppManager.getAlertColor();
                                    }

                                    if (AppManager.itemSelection.getEventCount() > 0) {
                                        boolean found = AppManager.itemSelection.isEventContains(selected,
                                                dobj.mInternalID);

                                        if (found) {
                                            id_fill = AppManager.getHilightColor();

                                            if ((!dobj.mIsValidForUtau &&
                                                    (renderer == RendererKind.UTAU)) ||
                                                    (!dobj.mIsValidForStraight &&
                                                    (renderer == RendererKind.VCNT))) {
                                                id_fill = AppManager.getAlertHilightColor();
                                            }
                                        }
                                    }

                                    g.setColor(id_fill);
                                    g.fillRect(x, y + 1, lyric_width,
                                        track_height - 1);

                                    Font lyric_font = dobj.mIsSymbolProtected
                                        ? AppManager.superFont10Bold
                                        : AppManager.superFont10;

                                    if (dobj.mIsOverlapped) {
                                        g.setColor(COLOR_R125G123B124);
                                        g.drawRect(x, y + 1, lyric_width,
                                            track_height - 1);

                                        if (show_lyrics) {
                                            g.setFont(lyric_font);

                                            if ((!dobj.mIsValidForUtau &&
                                                    (renderer == RendererKind.UTAU)) ||
                                                    (!dobj.mIsValidForStraight &&
                                                    (renderer == RendererKind.VCNT))) {
                                                g.setColor(Color.white);
                                            } else {
                                                g.setColor(COLOR_R147G147B147);
                                            }

                                            g.drawString(dobj.mText, x + 1,
                                                (y + half_track_height) -
                                                AppManager.superFont10OffsetHeight +
                                                1);
                                        }
                                    } else {
                                        g.setColor(COLOR_R125G123B124);
                                        g.drawRect(x, y + 1, lyric_width,
                                            track_height - 1);

                                        if (show_exp_line &&
                                                (lyric_width > 21)) {
                                            drawAccentLine(g,
                                                new Point(x,
                                                    y + track_height + 1),
                                                dobj.mAccent);

                                            int vibrato_start = x +
                                                lyric_width;
                                            int vibrato_end = x;

                                            if (dobj.mVibratoDelayInPixel <= lyric_width) {
                                                int vibrato_delay = dobj.mVibratoDelayInPixel;
                                                int vibrato_width = dobj.mRectangleInPixel.width -
                                                    vibrato_delay;
                                                vibrato_start = x +
                                                    vibrato_delay;
                                                vibrato_end = x +
                                                    vibrato_delay +
                                                    vibrato_width;

                                                if ((vibrato_start - x) < 21) {
                                                    vibrato_start = x + 21;
                                                }
                                            }

                                            g.setColor(COLOR_R051G051B000);
                                            g.drawLine(x + 21,
                                                y + track_height + 7,
                                                vibrato_start,
                                                y + track_height + 7);

                                            if (dobj.mVibratoDelayInPixel <= lyric_width) {
                                                int next_draw = vibrato_start;

                                                if (vibrato_start < vibrato_end) {
                                                    drawVibratoLine(g,
                                                        vibrato_start,
                                                        y + track_height + 1,
                                                        vibrato_end -
                                                        vibrato_start);
                                                }
                                            }
                                        }

                                        if (AppManager.editorConfig.ViewAtcualPitch ||
                                                AppManager.mCurveOnPianoroll) {
                                            int cl_start = dobj.mClock;
                                            int cl_end = cl_start +
                                                dobj.mLength;

                                            commonDrawer.clear();

                                            Color color_normal_picthbend = PortUtil.DarkOrchid;
                                            Color color_thin_pitchbend = new Color(color_normal_picthbend.getRed(),
                                                    color_normal_picthbend.getGreen(),
                                                    color_normal_picthbend.getBlue(),
                                                    128);
                                            int viblength = dobj.mLength -
                                                dobj.mVibDelay;
                                            int lasty = Integer.MIN_VALUE;
                                            g.setStroke(getStroke2px());

                                            // cl_start位置での、pit, pbsの値検索開始位置を調べておく
                                            pit.getValue(cl_start, indx_pit);

                                            int indx_pit_at_start = indx_pit.value;
                                            pbs.getValue(cl_start, indx_pbs);

                                            int indx_pbs_at_start = indx_pbs.value;

                                            // ビブラート部分の、ビブラートなしの場合のピッチベンドを描画
                                            if (viblength > 0) {
                                                g.setColor(color_thin_pitchbend);
                                                //indx_pit = new ByRef<Integer>( 0 );
                                                //indx_pbs = new ByRef<Integer>( 0 );
                                                cl_start = dobj.mClock +
                                                    dobj.mVibDelay;

                                                for (int cl = cl_start;
                                                        cl < cl_end; cl++) {
                                                    int vpit = pit.getValue(cl,
                                                            indx_pit);
                                                    int vpbs = pbs.getValue(cl,
                                                            indx_pbs);

                                                    float delta = (vpit * (float) vpbs) / 8192.0f;
                                                    float note = dobj.mNote +
                                                        delta;

                                                    int py = AppManager.yCoordFromNote(note) +
                                                        half_track_height;

                                                    if ((cl + 1) == cl_end) {
                                                        int px = AppManager.xCoordFromClocks(cl +
                                                                1);
                                                        commonDrawer.append(px,
                                                            lasty);
                                                    } else {
                                                        if (py == lasty) {
                                                            continue;
                                                        }

                                                        int px = AppManager.xCoordFromClocks(cl);

                                                        if (cl != cl_start) {
                                                            commonDrawer.append(px,
                                                                lasty);
                                                        }

                                                        commonDrawer.append(px,
                                                            py);
                                                        lasty = py;
                                                    }
                                                }

                                                commonDrawer.flush();
                                            }

                                            // この音符の範囲についてのみ，ピッチベンド曲線を描く
                                            g.setColor(color_normal_picthbend);
                                            cl_start = dobj.mClock;
                                            commonDrawer.clear();
                                            lasty = Integer.MIN_VALUE;
                                            indx_pit.value = indx_pit_at_start;
                                            indx_pbs.value = indx_pbs_at_start;

                                            int i = -1;

                                            for (int cl = cl_start;
                                                    cl < cl_end; cl++) {
                                                int vpit = pit.getValue(cl,
                                                        indx_pit);
                                                int vpbs = pbs.getValue(cl,
                                                        indx_pbs);

                                                float delta = (vpit * (float) vpbs) / 8192.0f;

                                                if ((cl >= (dobj.mClock +
                                                        dobj.mVibDelay)) &&
                                                        (dobj.mVibratoPit != null)) {
                                                    i++;

                                                    if (i < dobj.mVibratoPit.length) {
                                                        delta += dobj.mVibratoPit[i];
                                                    }
                                                }

                                                float note = dobj.mNote +
                                                    delta;

                                                int py = AppManager.yCoordFromNote(note) +
                                                    half_track_height;

                                                if ((cl + 1) == cl_end) {
                                                    int px = AppManager.xCoordFromClocks(cl +
                                                            1);
                                                    commonDrawer.append(px,
                                                        lasty);
                                                } else {
                                                    if (py == lasty) {
                                                        continue;
                                                    }

                                                    int px = AppManager.xCoordFromClocks(cl);

                                                    if (cl != cl_start) {
                                                        commonDrawer.append(px,
                                                            lasty);
                                                    }

                                                    commonDrawer.append(px, py);
                                                    lasty = py;
                                                }
                                            }

                                            commonDrawer.flush();
                                            g.setStroke(getStrokeDefault());
                                        }

                                        if (show_lyrics) {
                                            g.setFont(lyric_font);

                                            if ((!dobj.mIsValidForUtau &&
                                                    (renderer == RendererKind.UTAU)) ||
                                                    (!dobj.mIsValidForStraight &&
                                                    (renderer == RendererKind.VCNT))) {
                                                g.setColor(Color.white);
                                            } else {
                                                g.setColor(Color.black);
                                            }

                                            g.drawString(dobj.mText, x + 1,
                                                (y + half_track_height) -
                                                AppManager.superFont10OffsetHeight +
                                                1);
                                        }
                                    }
                                } else if (dobj.mType == DrawObjectType.Dynaff) {
                                    Color fill = COLOR_DYNAFF_FILL;

                                    if (AppManager.itemSelection.isEventContains(
                                                selected, dobj.mInternalID)) {
                                        fill = COLOR_DYNAFF_FILL_HIGHLIGHT;
                                    }

                                    g.setColor(fill);
                                    g.fillRect(x, y, 40, track_height);
                                    g.setColor(COLOR_R125G123B124);
                                    g.drawRect(x, y, 40, track_height);
                                    g.setColor(Color.black);
                                    g.setFont(AppManager.superFont10);

                                    if (dobj.mIsOverlapped) {
                                        g.setColor(COLOR_R147G147B147);
                                    }

                                    String str = dobj.mText;
                                    g.drawString(str, x + 1,
                                        (y + half_track_height) -
                                        AppManager.superFont10OffsetHeight + 1);
                                } else {
                                    int xend = x + lyric_width;
                                    Color fill = COLOR_DYNAFF_FILL;

                                    if (AppManager.itemSelection.isEventContains(
                                                selected, dobj.mInternalID)) {
                                        fill = COLOR_DYNAFF_FILL_HIGHLIGHT;
                                    }

                                    g.setColor(fill);
                                    g.fillRect(x, y, xend - x, track_height);
                                    g.setColor(COLOR_R125G123B124);
                                    g.drawRect(x, y, xend - x, track_height);

                                    if (dobj.mIsOverlapped) {
                                        g.setColor(COLOR_R147G147B147);
                                    } else {
                                        g.setColor(Color.black);
                                    }

                                    g.setFont(AppManager.superFont10);

                                    String str = dobj.mText;
                                    g.drawString(str, x + 1,
                                        (y + track_height + half_track_height) -
                                        AppManager.superFont10OffsetHeight + 1);

                                    if (dobj.mType == DrawObjectType.Crescend) {
                                        g.drawLine(xend - 2, y + 4, x + 3,
                                            y + half_track_height);
                                        g.drawLine(x + 3,
                                            y + half_track_height, xend - 2,
                                            (y + track_height) - 3);
                                    } else if (dobj.mType == DrawObjectType.Decrescend) {
                                        g.drawLine(x + 3, y + 4, xend - 2,
                                            y + half_track_height);
                                        g.drawLine(xend - 2,
                                            y + half_track_height, x + 3,
                                            (y + track_height) - 3);
                                    }
                                }
                            }
                        }
                    }

                    g.setClip(r);
                }

                // 編集中のエントリを表示
                if ((edit_mode == EditMode.ADD_ENTRY) ||
                        (edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY) ||
                        (edit_mode == EditMode.DRAG_DROP) ||
                        AppManager.mMainWindowController.isStepSequencerEnabled()) {
                    if (AppManager.mAddingEvent != null) {
                        int x = (int) ((AppManager.mAddingEvent.Clock * scalex) +
                            xoffset);
                        y = (-AppManager.mAddingEvent.ID.Note * track_height) +
                            yoffset + 1;

                        int length = (int) (AppManager.mAddingEvent.ID.getLength() * scalex);

                        if (AppManager.mAddingEvent.ID.type == VsqIDType.Aicon) {
                            if (AppManager.mAddingEvent.ID.IconDynamicsHandle.isDynaffType()) {
                                length = AppManager.DYNAFF_ITEM_WIDTH;
                            }
                        }

                        if (AppManager.mAddingEvent.ID.getLength() <= 0) {
                            g.setColor(new Color(171, 171, 171));
                            g.drawRect(x, y, 10, track_height - 1);
                        } else {
                            g.setColor(COLOR_ADDING_NOTE_BORDER);
                            g.drawRect(x, y, length, track_height - 1);
                            g.setColor(COLOR_ADDING_NOTE_FILL);
                            g.fillRect(x, y, length, track_height - 1);
                        }
                    }
                } else if (edit_mode == EditMode.EDIT_VIBRATO_DELAY) {
                    int x = (int) ((AppManager.mAddingEvent.Clock * scalex) +
                        xoffset);
                    y = (-AppManager.mAddingEvent.ID.Note * track_height) +
                        yoffset + 1;

                    int length = (int) (AppManager.mAddingEvent.ID.getLength() * scalex);
                    g.setColor(COLOR_ADDING_NOTE_BORDER);
                    g.drawRect(x, y, length, track_height - 1);
                } else if (((edit_mode == EditMode.MOVE_ENTRY) ||
                        (edit_mode == EditMode.MOVE_ENTRY_WHOLE) ||
                        (edit_mode == EditMode.EDIT_LEFT_EDGE) ||
                        (edit_mode == EditMode.EDIT_RIGHT_EDGE)) &&
                        (AppManager.itemSelection.getEventCount() > 0)) {
                    for (Iterator<SelectedEventEntry> itr = AppManager.itemSelection.getEventIterator();
                            itr.hasNext();) {
                        SelectedEventEntry ev = itr.next();
                        int x = (int) ((ev.editing.Clock * scalex) + xoffset);
                        y = (-ev.editing.ID.Note * track_height) + yoffset + 1;

                        if (ev.editing.ID.type == VsqIDType.Aicon) {
                            if (ev.editing.ID.IconDynamicsHandle == null) {
                                continue;
                            }

                            int length = 0;

                            if (ev.editing.ID.IconDynamicsHandle.isDynaffType()) {
                                length = AppManager.DYNAFF_ITEM_WIDTH;
                            } else {
                                length = (int) (ev.editing.ID.getLength() * scalex);
                            }

                            g.setColor(COLOR_ADDING_NOTE_BORDER);
                            g.drawRect(x, y, length, track_height - 1);
                        } else {
                            if (ev.editing.ID.getLength() == 0) {
                                g.setColor(new Color(171, 171, 171));
                                g.setStroke(getStrokeDashed());
                                g.drawRect(x, y, 10, track_height - 1);
                                g.setStroke(getStrokeDefault());
                            } else {
                                int length = (int) (ev.editing.ID.getLength() * scalex);
                                g.setColor(COLOR_ADDING_NOTE_BORDER);
                                g.drawRect(x, y, length, track_height - 1);
                            }
                        }
                    }

                    if (edit_mode == EditMode.MOVE_ENTRY_WHOLE) {
                        int clock_start = AppManager.mWholeSelectedInterval.getStart();
                        int clock_end = AppManager.mWholeSelectedInterval.getEnd();
                        int x_start = AppManager.xCoordFromClocks(AppManager.mWholeSelectedIntervalStartForMoving);
                        int x_end = AppManager.xCoordFromClocks(AppManager.mWholeSelectedIntervalStartForMoving +
                                (clock_end - clock_start));
                        g.setColor(COLOR_A098R000G000B000);
                        g.drawLine(x_start, 0, x_start, height);
                        g.drawLine(x_end, 0, x_end, height);
                    }
                }
            }

            g.setClip(null);

            if (edit_mode == EditMode.ADD_ENTRY) {
                int x = (int) ((AppManager.mAddingEvent.Clock * scalex) +
                    xoffset);
                y = (-AppManager.mAddingEvent.ID.Note * track_height) +
                    yoffset + 1;

                int length;

                if (AppManager.mAddingEvent.ID.getLength() == 0) {
                    length = 10;
                } else {
                    length = (int) (AppManager.mAddingEvent.ID.getLength() * scalex);
                }

                x += length;
                g.setColor(COLOR_LINE_LU);
                g.drawLine(x, 0, x, y - 1);
                g.drawLine(x, y + track_height, x, height);
                g.setColor(COLOR_LINE_RD);
                g.drawLine(x + 1, 0, x + 1, y - 1);
                g.drawLine(x + 1, y + track_height, x + 1, height);
            } else if ((edit_mode == EditMode.MOVE_ENTRY) ||
                    (edit_mode == EditMode.MOVE_ENTRY_WAIT_MOVE)) {
                if (AppManager.itemSelection.getEventCount() > 0) {
                    VsqEvent last = AppManager.itemSelection.getLastEvent().editing;
                    int x = (int) ((last.Clock * scalex) + xoffset);
                    y = (-last.ID.Note * track_height) + yoffset + 1;

                    int length = (int) (last.ID.getLength() * scalex);

                    if (last.ID.type == VsqIDType.Aicon) {
                        if (last.ID.IconDynamicsHandle.isDynaffType()) {
                            length = AppManager.DYNAFF_ITEM_WIDTH;
                        }
                    }

                    // 縦線
                    g.setColor(COLOR_LINE_LU);
                    g.drawLine(x, 0, x, y - 1);
                    g.drawLine(x, y + track_height, x, height);
                    // 横線
                    g.drawLine(key_width, (y + half_track_height) - 2, x - 1,
                        (y + half_track_height) - 2);
                    g.drawLine(x + length + 1, (y + half_track_height) - 2,
                        width, (y + half_track_height) - 2);
                    // 縦線
                    g.setColor(COLOR_LINE_RD);
                    g.drawLine(x + 1, 0, x + 1, y - 1);
                    g.drawLine(x + 1, y + track_height, x + 1, height);
                    // 横線
                    g.drawLine(key_width, (y + half_track_height) - 1, x - 1,
                        (y + half_track_height) - 1);
                    g.drawLine(x + length + 1, (y + half_track_height) - 1,
                        width, (y + half_track_height) - 1);
                }
            } else if ((edit_mode == EditMode.ADD_FIXED_LENGTH_ENTRY) ||
                    (edit_mode == EditMode.DRAG_DROP)) {
                int x = (int) ((AppManager.mAddingEvent.Clock * scalex) +
                    xoffset);
                y = (-AppManager.mAddingEvent.ID.Note * track_height) +
                    yoffset + 1;

                int length = (int) (AppManager.mAddingEvent.ID.getLength() * scalex);

                if (AppManager.mAddingEvent.ID.type == VsqIDType.Aicon) {
                    if (AppManager.mAddingEvent.ID.IconDynamicsHandle.isDynaffType()) {
                        length = AppManager.DYNAFF_ITEM_WIDTH;
                    }
                }

                // 縦線
                g.setColor(COLOR_LINE_LU);
                g.drawLine(x, 0, x, y - 1);
                g.drawLine(x, y + track_height, x, height);
                // 横線
                g.drawLine(key_width, (y + half_track_height) - 2, x - 1,
                    (y + half_track_height) - 2);
                g.drawLine(x + length + 1, (y + half_track_height) - 2, width,
                    (y + half_track_height) - 2);
                // 縦線
                g.setColor(COLOR_LINE_RD);
                g.drawLine(x + 1, 0, x + 1, y - 1);
                g.drawLine(x + 1, y + track_height, x + 1, height);
                // 横線
                g.drawLine(key_width, (y + half_track_height) - 1, x - 1,
                    (y + half_track_height) - 1);
                g.drawLine(x + length + 1, (y + half_track_height) - 1, width,
                    (y + half_track_height) - 1);
            } else if (edit_mode == EditMode.EDIT_LEFT_EDGE) {
                VsqEvent last = AppManager.itemSelection.getLastEvent().editing;
                int x = (int) ((last.Clock * scalex) + xoffset);
                y = (-last.ID.Note * track_height) + yoffset + 1;
                g.setColor(COLOR_LINE_LU);
                g.drawLine(x, 0, x, y - 1);
                g.drawLine(x, y + track_height, x, height);
                g.setColor(COLOR_LINE_RD);
                g.drawLine(x + 1, 0, x + 1, y - 1);
                g.drawLine(x + 1, y + track_height, x + 1, height);
            } else if (edit_mode == EditMode.EDIT_RIGHT_EDGE) {
                VsqEvent last = AppManager.itemSelection.getLastEvent().editing;
                int x = (int) ((last.Clock * scalex) + xoffset);
                y = (-last.ID.Note * track_height) + yoffset + 1;

                int length = (int) (last.ID.getLength() * scalex);
                x += length;
                g.setColor(COLOR_LINE_LU);
                g.drawLine(x, 0, x, y - 1);
                g.drawLine(x, y + track_height, x, height);
                g.setColor(COLOR_LINE_RD);
                g.drawLine(x + 1, 0, x + 1, y - 1);
                g.drawLine(x + 1, y + track_height, x + 1, height);
            } else if (edit_mode == EditMode.EDIT_VIBRATO_DELAY) {
                int x = (int) ((AppManager.mAddingEvent.Clock * scalex) +
                    xoffset);
                y = (-AppManager.mAddingEvent.ID.Note * track_height) +
                    yoffset + 1;
                g.setColor(COLOR_LINE_LU);
                g.drawLine(x, 0, x, y - 1);
                g.drawLine(x, y + track_height, x, height);
                g.setColor(COLOR_LINE_RD);
                g.drawLine(x + 1, 0, x + 1, y - 1);
                g.drawLine(x + 1, y + track_height, x + 1, height);

                double max_length = AppManager.mAddingEventLength -
                    (PX_ACCENT_HEADER / scalex);
                double drate = AppManager.mAddingEvent.ID.getLength() / max_length;

                if (drate > 0.99) {
                    drate = 1.00;
                }

                int rate = (int) (drate * 100.0);
                String percent = rate + "%";
                Dimension size = Util.measureString(percent,
                        AppManager.superFont9);
                int delay_x = (int) ((((AppManager.mAddingEvent.Clock +
                    AppManager.mAddingEvent.ID.getLength()) -
                    AppManager.mAddingEventLength +
                    AppManager.mAddingEvent.ID.VibratoDelay) * scalex) +
                    xoffset);
                Rectangle pxArea = new Rectangle(delay_x,
                        (int) (y + (track_height * 2.5)),
                        (int) (size.width * 1.2), (int) (size.height * 1.2));
                g.setColor(COLOR_R192G192B192);
                g.fillRect(pxArea.x, pxArea.y, pxArea.width, pxArea.height);
                g.setColor(Color.black);
                g.drawRect(pxArea.x, pxArea.y, pxArea.width, pxArea.height);
                // StringFormat sf = new StringFormat();
                //sf.Alignment = StringAlignment.Center;
                //sf.LineAlignment = StringAlignment.Center;
                g.setFont(AppManager.superFont9);
                g.drawString(percent, pxArea.x + 3,
                    (pxArea.y + (AppManager.superFont9Height / 2)) -
                    AppManager.superFont9OffsetHeight + 2);
            }

            // マウス位置での音階名
            if (hilighted_note >= 0) {
                int align = 1;
                int valign = 0;
                g.setColor(Color.black);
                PortUtil.drawStringEx(g, VsqNote.getNoteString(hilighted_note),
                    AppManager.superFont10Bold,
                    new Rectangle(mouse_position.x - 110,
                        mouse_position.y - 50, 100, 100), align, valign);
            }

            if (AppManager.isWholeSelectedIntervalEnabled()) {
                int start = (int) (AppManager.mWholeSelectedInterval.getStart() * scalex) +
                    xoffset;

                if (start < key_width) {
                    start = key_width;
                }

                int end = (int) (AppManager.mWholeSelectedInterval.getEnd() * scalex) +
                    xoffset;

                if (start < end) {
                    g.setColor(new Color(0, 0, 0, 98));
                    g.fillRect(start, 0, end - start, getHeight());
                }
            } else if (AppManager.mIsPointerDowned) {
                // 選択範囲を半透明で塗りつぶす
                Point mouse = pointToClient(PortUtil.getMousePosition());

                // 描く四角形の位置とサイズ
                int tx;

                // 描く四角形の位置とサイズ
                int ty;

                // 描く四角形の位置とサイズ
                int twidth;

                // 描く四角形の位置とサイズ
                int theight;

                // 上下左右の枠を表示していいかどうか
                boolean vtop = true;
                boolean vbottom = true;
                boolean vleft = true;
                boolean vright = true;

                // マウスが下りた位置のx座標
                int lx = AppManager.mMouseDownLocation.x - stdx;

                if (lx < mouse.x) {
                    tx = lx;
                    twidth = mouse.x - lx;
                } else {
                    tx = mouse.x;
                    twidth = lx - mouse.x;
                }

                int ly = AppManager.mMouseDownLocation.y - stdy;

                if (ly < mouse.y) {
                    ty = ly;
                    theight = mouse.y - ly;
                } else {
                    ty = mouse.y;
                    theight = ly - mouse.y;
                }

                if (tx < key_width) {
                    int txold = tx;
                    tx = key_width;
                    twidth -= (tx - txold);
                    vleft = false;
                }

                if (width < (tx + twidth)) {
                    vright = false;
                    twidth = width - tx;
                }

                if (ty < 0) {
                    vtop = false;

                    int tyold = ty;
                    ty = 0;
                    theight -= (ty - tyold);
                }

                if (height < (ty + theight)) {
                    vbottom = false;
                    theight = height - ty;
                }

                Rectangle rc = new Rectangle(tx, ty, twidth, theight);
                Color pen = new Color(0, 0, 0, 200);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(tx, ty, twidth, theight);
                g.setColor(pen);

                if (vtop && (twidth > 1) && (ty < height)) {
                    g.drawLine(tx, ty, (tx + twidth) - 1, ty);
                }

                if (vbottom && (twidth > 1)) {
                    g.drawLine(tx, ty + theight, (tx + twidth) - 1, ty +
                        theight);
                }

                if (vleft && (theight > 1) && (tx < width)) {
                    g.drawLine(tx, ty, tx, (ty + theight) - 1);
                }

                if (vright && (theight > 1) && (key_width < (tx + twidth))) {
                    g.drawLine(tx + twidth, ty, tx + twidth, (ty + theight) -
                        1);
                }
            }

            if (AppManager.mCurveOnPianoroll) {
                g.setClip(null);

                Area fillarea = new Area(new Rectangle(key_width, 0,
                            width - key_width, height)); // 塗りつぶす領域．最後に処理する
                                                         //g.setColor( new Color( 255, 255, 255, 64 ) );
                                                         //g.fillRect( key_width, 0, width - key_width, height );

                VsqBPList pbs = vsq_track.MetaText.PBS;

                if (pbs == null) {
                    pbs = new VsqBPList(CurveType.PBS.getName(),
                            CurveType.PBS.getDefault(),
                            CurveType.PBS.getMinimum(),
                            CurveType.PBS.getMaximum());
                }

                Color pitline = PortUtil.MidnightBlue;
                g.setStroke(getStroke2px());

                synchronized (AppManager.mDrawObjects) {
                    Vector<DrawObject> list = AppManager.mDrawObjects.get(selected -
                            1);
                    int j_start = AppManager.mDrawStartIndex[selected - 1];
                    int c = list.size();
                    int last_x = key_width;

                    int pbs_count = pbs.size();
                    double a = 1.0 / 8192.0;

                    //ByRef<Integer> pit_index = new ByRef<Integer>( 0 );
                    //ByRef<Integer> pbs_index_for_pit = new ByRef<Integer>( 0 );
                    for (int j = j_start; j < c; j++) {
                        DrawObject dobj = list.get(j);
                        int clock = dobj.mClock;
                        int x_at_clock = (int) ((clock * scalex) + xoffset);
                        last_x = x_at_clock;

                        // 音符の区間中に，PBSがあるかもしれないのでPBSのデータ点を探しながら塗りつぶす
                        ByRef<Integer> pbs_index = new ByRef<Integer>(0);
                        int last_pbs_value = pbs.getValue(clock, pbs_index);

                        if (pbs_count <= 0) {
                            // データ点が無い場合
                            double delta_note = 8192.0 * last_pbs_value * a;
                            int y_top = (int) ((-((dobj.mNote + delta_note) -
                                0.5) * track_height) + yoffset);
                            int y_bottom = (int) ((-(dobj.mNote - delta_note -
                                0.5) * track_height) + yoffset);

                            if (last_x < key_width) {
                                last_x = key_width;
                            }

                            int x = (int) (((clock + dobj.mLength) * scalex) +
                                xoffset);
                            fillarea.subtract(new Area(
                                    new Rectangle(last_x, y_top, x - last_x,
                                        y_bottom - y_top)));
                            last_x = x;
                        } else {
                            // データ点がある場合
                            for (; pbs_index.value < pbs_count;
                                    pbs_index.value++) {
                                int pbs_clock;
                                int pbs_value;

                                if ((0 <= (pbs_index.value + 1)) &&
                                        ((pbs_index.value + 1) < pbs_count)) {
                                    pbs_clock = pbs.getKeyClock(pbs_index.value +
                                            1);

                                    if (pbs_clock > (clock + dobj.mLength)) {
                                        pbs_clock = clock + dobj.mLength;
                                    }

                                    pbs_value = pbs.getElement(pbs_index.value +
                                            1);
                                } else {
                                    pbs_clock = clock + dobj.mLength;
                                    pbs_value = last_pbs_value;
                                }

                                double delta_note = 8192.0 * last_pbs_value * a;

                                int y_top = (int) ((-((dobj.mNote + delta_note) -
                                    0.5) * track_height) + yoffset);
                                int y_bottom = (int) ((-(dobj.mNote -
                                    delta_note - 0.5) * track_height) +
                                    yoffset);
                                int x = (int) ((pbs_clock * scalex) + xoffset);

                                if (x < key_width) {
                                    x = key_width;
                                    last_x = x;
                                    last_pbs_value = pbs_value;

                                    continue;
                                }

                                if (last_x < key_width) {
                                    last_x = key_width;
                                }

                                fillarea.subtract(new Area(
                                        new Rectangle(last_x, y_top,
                                            x - last_x, y_bottom - y_top)));

                                last_x = x;
                                last_pbs_value = pbs_value;
                            }
                        }
                    }
                }

                Color fill = new Color(0, 0, 0, 128);
                g.setColor(fill);
                g.fill(fillarea);

                if (mMouseTracer.size() > 1) {
                    commonDrawer.clear();
                    g.setColor(PortUtil.Orchid);
                    g.setStroke(getStroke2px());

                    for (Iterator<Point> itr = mMouseTracer.iterator();
                            itr.hasNext();) {
                        Point pt = itr.next();
                        commonDrawer.append(pt.x - stdx, pt.y - stdy);
                    }

                    commonDrawer.flush();
                }
            }

            // マーカー
            int marker_x = (int) (((AppManager.getCurrentClock() * scalex) +
                AppManager.keyOffset + key_width) - stdx);

            if ((key_width <= marker_x) && (marker_x <= width)) {
                g.setColor(Color.white);
                g.setStroke(getStroke2px());
                g.drawLine(marker_x, 0, marker_x, getHeight());
                g.setStroke(getStrokeDefault());
            }
        } catch (Exception ex) {
        }
    }

    /// <summary>
    /// アクセントを表す表情線を、指定された位置を基準点として描き込みます
    /// </summary>
    /// <param name="g"></param>
    /// <param name="accent"></param>
    private void drawAccentLine(Graphics g, Point origin, int accent) {
        int x0 = origin.x + 1;
        int y0 = origin.y + 10;
        int height = 4 + ((accent * 4) / 100);
        //SmoothingMode sm = g.SmoothingMode;
        //g.SmoothingMode = SmoothingMode.AntiAlias;
        g.setColor(Color.black);
        g.drawPolyline(new int[] { x0, x0 + 2, x0 + 8, x0 + 13, x0 + 16, x0 +
                20 }, new int[] { y0, y0, y0 - height, y0, y0, y0 - 4 }, 6);

        //g.SmoothingMode = sm;
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="g">描画に使用するグラフィクス</param>
    /// <param name="rate">Vibrato Rate</param>
    /// <param name="start_rate">Vibrato Rateの開始値</param>
    /// <param name="depth">Vibrato Depth</param>
    /// <param name="start_depth">Vibrato Depthの開始値</param>
    /// <param name="note">描画する音符のノートナンバー</param>
    /// <param name="clock_start">ビブラートが始まるクロック位置</param>
    /// <param name="clock_width">ビブラートのクロック長さ</param>
    private void drawVibratoPitchbend(PolylineDrawer drawer,
        VibratoBPList rate, int start_rate, VibratoBPList depth,
        int start_depth, int note, int x_start, int px_width) {
        if ((rate == null) || (depth == null)) {
            return;
        }

        int y0 = AppManager.yCoordFromNote(note - 0.5f);
        float px_track_height = (int) (AppManager.mMainWindowController.getScaleY() * 100);
        VsqFileEx vsq = AppManager.getVsqFile();
        int clock_start = AppManager.clockFromXCoord(x_start);
        int clock_end = AppManager.clockFromXCoord(x_start + px_width);
        int tempo = vsq.getTempoAt(clock_start);

        drawer.clear();

        Iterator<PointD> itr = new VibratoPointIteratorBySec(vsq, rate,
                start_rate, depth, start_depth, clock_start,
                clock_end - clock_start, (float) ((tempo * 1e-6) / 480.0));
        Graphics2D g = drawer.getGraphics();
        g.setColor(Color.blue);

        int lx = 0;

        for (; itr.hasNext();) {
            PointD p = itr.next();
            int x = AppManager.xCoordFromClocks(vsq.getClockFromSec(p.getX()));
            int y = (int) ((p.getY() * px_track_height) + y0);

            if ((x - lx) > 0) {
                continue;
            }

            drawer.append(x, y);
            lx = x;
        }

        drawer.flush();
    }

    private void drawVibratoLine(Graphics g, int origin_x, int origin_y,
        int vibrato_length) {
        int x0 = origin_x + 1;
        int y0 = origin_y + 10;
        int clipx = origin_x + 1;
        int clip_length = vibrato_length;

        if (clipx < AppManager.keyWidth) {
            clipx = AppManager.keyWidth;
            clip_length = (origin_x + 1 + vibrato_length) -
                AppManager.keyWidth;

            if (clip_length <= 0) {
                return;
            }
        }

        //SmoothingMode sm = g.SmoothingMode;
        //g.SmoothingMode = SmoothingMode.AntiAlias;
        int _UWID = 10;
        int count = (vibrato_length / _UWID) + 1;
        int[] _BASE_X = new int[] {
                x0 - _UWID, (x0 + 2) - _UWID, (x0 + 4) - _UWID, (x0 + 7) -
                _UWID, (x0 + 9) - _UWID, (x0 + 10) - _UWID
            };
        int[] _BASE_Y = new int[] { y0 - 4, y0 - 7, y0 - 7, y0 - 1, y0 - 1, y0 -
                4 };
        Shape old = g.getClip();
        g.clipRect(clipx, (origin_y + 10) - 8, clip_length, 10);
        g.setColor(Color.black);

        for (int i = 0; i < count; i++) {
            for (int j = 0; j < _BASE_X.length; j++) {
                _BASE_X[j] += _UWID;
            }

            g.drawPolyline(_BASE_X, _BASE_Y, _BASE_X.length);
        }

        //g.SmoothingMode = sm;
        g.setClip(old);
    }
}

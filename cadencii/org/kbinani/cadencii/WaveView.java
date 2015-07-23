/*
 * WaveView.cs
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

import org.kbinani.media.*;

import org.kbinani.windows.forms.*;

import java.awt.*;
import java.awt.image.*;


/// <summary>
/// トラック16個分の波形描画コンテキストを保持し、それらの描画を行うコンポーネントです。
/// </summary>
public class WaveView extends BPanel {
    private static final float MAX_SCALE = 10.0f;
    private static final float MIN_SCALE = 1.0f;

    /// <summary>
    /// 波形描画用のコンテキスト
    /// </summary>
    private WaveDrawContext[] mDrawer = new WaveDrawContext[16];

    /// <summary>
    /// グラフィクスオブジェクトのキャッシュ
    /// </summary>
    private Graphics2D mGraphics = null;

    /// <summary>
    /// 縦軸方向のスケール
    /// </summary>
    private float mScale = MIN_SCALE;

    /// <summary>
    /// 左側のボタン部との境界線の色
    /// </summary>
    private Color mBorderColor = new Color(105, 105, 105);

    /// <summary>
    /// 縦軸のスケールを自動最大化するかどうか
    /// </summary>
    private boolean mAutoMaximize = false;

    /// <summary>
    /// 幅2ピクセルのストローク
    /// </summary>
    private BasicStroke mStroke2px = null;

    /// <summary>
    /// デフォルトのストローク
    /// </summary>
    private BasicStroke mStrokeDefault = null;

    /// <summary>
    /// コンストラクタ
    /// </summary>
    public WaveView() {
        super();
    }

    /// <summary>
    /// 縦軸を自動最大化するかどうかを取得します
    /// </summary>
    /// <returns></returns>
    public boolean isAutoMaximize() {
        return mAutoMaximize;
    }

    /// <summary>
    /// 縦軸を自動最大化するかどうかを設定します
    /// </summary>
    /// <param name="value"></param>
    public void setAutoMaximize(boolean value) {
        mAutoMaximize = value;
    }

    /// <summary>
    /// コンポーネントの描画関数です
    /// </summary>
    /// <param name="g"></param>
    public void paint(Graphics g1) {
        int width = getWidth();
        int height = getHeight();
        Rectangle rc = new Rectangle(0, 0, width, height);

        Graphics2D g = (Graphics2D) g1;

        // 背景を塗りつぶす
        g.setStroke(getStrokeDefault());
        g.setColor(Color.gray);
        g.fillRect(rc.x, rc.y, rc.width, rc.height);

        if (AppManager.skipDrawingWaveformWhenPlaying &&
                AppManager.isPlaying()) {
            // 左側のボタン部との境界線
            g.setColor(mBorderColor);
            g.drawLine(0, 0, 0, height);

            g.setColor(Color.black);
            PortUtil.drawStringEx(g, "(hidden for performance)",
                AppManager.superFont8, rc, PortUtil.STRING_ALIGN_CENTER,
                PortUtil.STRING_ALIGN_CENTER);

            return;
        }

        // スケール線を描く
        int half_height = height / 2;
        g.setColor(Color.black);
        g.drawLine(0, half_height, width, half_height);

        // 描画コンテキストを用いて波形を描画
        int selected = AppManager.getSelected();
        WaveDrawContext context = mDrawer[selected - 1];

        if (context != null) {
            if (mAutoMaximize) {
                context.draw(g, Color.black, rc,
                    AppManager.clockFromXCoord(AppManager.keyWidth),
                    AppManager.clockFromXCoord(AppManager.keyWidth + width),
                    AppManager.getVsqFile().TempoTable,
                    AppManager.mMainWindowController.getScaleX());
            } else {
                context.draw(g, Color.black, rc,
                    AppManager.clockFromXCoord(AppManager.keyWidth),
                    AppManager.clockFromXCoord(AppManager.keyWidth + width),
                    AppManager.getVsqFile().TempoTable,
                    AppManager.mMainWindowController.getScaleX(), mScale);
            }
        }

        // 左側のボタン部との境界線
        g.setColor(mBorderColor);
        g.drawLine(0, 0, 0, height);

        // ソングポジション
        int song_pos_x = AppManager.xCoordFromClocks(AppManager.getCurrentClock()) -
            AppManager.keyWidth;

        if (0 < song_pos_x) {
            g.setColor(Color.white);
            g.setStroke(getStroke2px());
            g.drawLine(song_pos_x, 0, song_pos_x, height);
        }
    }

    private BasicStroke getStrokeDefault() {
        if (mStrokeDefault == null) {
            mStrokeDefault = new BasicStroke();
        }

        return mStrokeDefault;
    }

    private BasicStroke getStroke2px() {
        if (mStroke2px == null) {
            mStroke2px = new BasicStroke(2.0f);
        }

        return mStroke2px;
    }

    /// <summary>
    /// 縦方向の描画倍率を取得します。
    /// </summary>
    /// <seealso cref="getScale"/>
    /// <returns></returns>
    public float scale() {
        return mScale;
    }

    /// <summary>
    /// 縦方向の描画倍率を取得します。
    /// </summary>
    /// <returns></returns>
    public float getScale() {
        return mScale;
    }

    /// <summary>
    /// 縦方向の描画倍率を設定します。
    /// </summary>
    /// <param name="value"></param>
    public void setScale(float value) {
        if (value < MIN_SCALE) {
            mScale = MIN_SCALE;
        } else if (MAX_SCALE < value) {
            mScale = MAX_SCALE;
        } else {
            mScale = value;
        }
    }

    /// <summary>
    /// 全ての波形描画コンテキストが保持しているデータをクリアします
    /// </summary>
    public void unloadAll() {
        for (int i = 0; i < mDrawer.length; i++) {
            WaveDrawContext context = mDrawer[i];

            if (context == null) {
                continue;
            }

            context.unload();
        }
    }

    /// <summary>
    /// 波形描画コンテキストに、指定したWAVEファイルの指定区間を再度読み込みます。
    /// </summary>
    /// <param name="index">読込を行わせる波形描画コンテキストのインデックス</param>
    /// <param name="file">読み込むWAVEファイルのパス</param>
    /// <param name="sec_from">読み込み区間の開始秒時</param>
    /// <param name="sec_to">読み込み区間の終了秒時</param>
    public void reloadPartial(int index, String file, double sec_from,
        double sec_to) {
        if ((index < 0) || (mDrawer.length <= index)) {
            return;
        }

        if (mDrawer[index] == null) {
            mDrawer[index] = new WaveDrawContext();
            mDrawer[index].load(file);
        } else {
            mDrawer[index].reloadPartial(file, sec_from, sec_to);
        }
    }

    /// <summary>
    /// 波形描画コンテキストに、指定したWAVEファイルを読み込みます。
    /// </summary>
    /// <param name="index">読込を行わせる波形描画コンテキストのインデックス</param>
    /// <param name="wave_path">読み込むWAVEファイルのパス</param>
    public void load(int index, String wave_path) {
        if ((index < 0) || (mDrawer.length <= index)) {
            return;
        }

        if (mDrawer[index] == null) {
            mDrawer[index] = new WaveDrawContext();
        }

        mDrawer[index].load(wave_path);
    }
}

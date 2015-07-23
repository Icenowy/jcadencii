/*
 * IconParader.cs
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

import org.kbinani.windows.forms.*;

import java.awt.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.*;


/// <summary>
/// 起動時のスプラッシュウィンドウに表示されるアイコンパレードの、1個のアイコンを表現します
/// </summary>
public class IconParader extends BPictureBox {
    static final int RADIUS = 6; // 角の丸み
    static final int DIAMETER = 2 * RADIUS;
    public static final int ICON_WIDTH = 48;
    public static final int ICON_HEIGHT = 48;

    public IconParader() {
        Dimension d = new Dimension(ICON_WIDTH, ICON_HEIGHT);
        setSize(d);
        setMaximumSize(d);
        setMinimumSize(d);
        setPreferredSize(d);
    }

    public static Image createIconImage(String path_image, String singer_name) {
        Image ret = null;

        if (fsys.isFileExists(path_image)) {
            try {
                ret = ImageIO.read(new File(path_image));
            } catch (Exception ex) {
                ret = null;
                System.out.println("IconParader#createIconImage; ex=" + ex);
            }
        }

        if (ret == null) {
            // 画像ファイルが無かったか，読み込みに失敗した場合

            // 歌手名が描かれた画像をセットする
            BufferedImage bmp = new BufferedImage(ICON_WIDTH, ICON_HEIGHT,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bmp.createGraphics();
            g.clearRect(0, 0, ICON_WIDTH, ICON_HEIGHT);

            Font font = new Font("Arial", 0, 10);
            PortUtil.drawStringEx((Graphics) g, singer_name, font,
                new Rectangle(1, 1, ICON_WIDTH - 2, ICON_HEIGHT - 2),
                PortUtil.STRING_ALIGN_NEAR, PortUtil.STRING_ALIGN_NEAR);
            ret = bmp;
        }

        return ret;
    }

    public void setImage(Image img) {
        BufferedImage bmp = new BufferedImage(ICON_WIDTH, ICON_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = null;

        try {
            g = bmp.createGraphics();

            if (img != null) {
                int img_width = img.getWidth(null);
                int img_height = img.getHeight(null);
                double a = img_height / (double) img_width;
                double aspecto = ICON_HEIGHT / (double) ICON_WIDTH;

                int x = 0;
                int y = 0;
                int w = ICON_WIDTH;
                int h = ICON_HEIGHT;

                if (a >= aspecto) {
                    // アイコンより縦長
                    double act_width = ICON_WIDTH / a;
                    x = (int) ((ICON_WIDTH - act_width) / 2.0);
                    w = (int) act_width;
                } else {
                    // アイコンより横長
                    double act_height = ICON_HEIGHT * a;
                    y = (int) ((ICON_HEIGHT - act_height) / 2.0);
                    h = (int) act_height;
                }

                g.drawImage(img, x, y, w, h, null);
            }
        } catch (Exception ex) {
            System.err.println("IconParader#setImage; ex=" + ex);
        } finally {
        }

        super.setImage(bmp);
    }
}

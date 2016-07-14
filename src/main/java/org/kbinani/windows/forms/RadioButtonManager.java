/*
 * RadioButtonManager.cs
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Vector;


/**
 * BRadioButtonが同時に２個以上チェックされた状態になるのを防ぐための管理クラス
 * @author kbinani
 *
 */
public class RadioButtonManager implements ItemListener {
    public Vector<BRadioButton> mButtons = new Vector<BRadioButton>();

    public void add(BRadioButton button) {
        synchronized (mButtons) {
            int size = mButtons.size();

            for (int i = 0; i < size; i++) {
                if (button == mButtons.get(i)) {
                    // 既に登録済み
                    return;
                }
            }

            button.addItemListener(this);
            mButtons.add(button);
        }
    }

    public void itemStateChanged(ItemEvent arg0) {
        synchronized (mButtons) {
            int state = arg0.getStateChange();

            // イベントの送信元を特定
            int size = mButtons.size();
            Object src = arg0.getItem();
            int index = -1;

            for (int i = 0; i < size; i++) {
                BRadioButton ab = mButtons.get(i);

                if (ab == null) {
                    continue;
                }

                if (ab == src) {
                    index = i;

                    break;
                }
            }

            if (index < 0) {
                return;
            }

            if (state == ItemEvent.SELECTED) {
                // アイテムが選択状態に変化した場合
                // ほかのアイテムのチェックを外す
                for (int i = 0; i < size; i++) {
                    if (i == index) {
                        continue;
                    }

                    BRadioButton ab = mButtons.get(i);

                    if (ab == null) {
                        continue;
                    }

                    ab.removeItemListener(this);
                    ab.setSelected(i == index);
                    ab.addItemListener(this);
                }
            } else {
                // アイテムが非選択状態に変化した場合
                // ほかのアイテムが全て非選択状態の場合、選択状態に戻す
                boolean no_one_selected = true;

                for (int i = 0; i < size; i++) {
                    if (mButtons.get(i).isSelected()) {
                        no_one_selected = false;

                        break;
                    }
                }

                if (no_one_selected) {
                    BRadioButton btn = mButtons.get(index);
                    btn.removeItemListener(this);
                    btn.setSelected(true);
                    btn.addItemListener(this);
                }
            }
        }
    }
}

/*
 * BButton.cs
 * Copyright © 2009-2011 kbinani
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

import org.kbinani.BEvent;
import org.kbinani.BEventArgs;
import org.kbinani.BEventHandler;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;


public class BButton extends JButton implements ActionListener, MouseListener,
    MouseMotionListener, FocusListener, KeyListener, ComponentListener,
    MouseWheelListener {
    private static final long serialVersionUID = 1L;

    /* root impl of MouseWheel event */
    // root impl of MouseWheel event is in BButton
    public final BEvent<BMouseEventHandler> mouseWheelEvent = new BEvent<BMouseEventHandler>();

    /* root impl of Paint event */
    // root impl of Paint event is in BButton
    public final BEvent<BPaintEventHandler> paintEvent = new BEvent<BPaintEventHandler>();

    /* root impl of Click event */
    // root impl of Click event is in BButton
    public final BEvent<BEventHandler> clickEvent = new BEvent<BEventHandler>();

    /* root impl of MouseListener */
    // root impl of MouseListener is in BButton
    public final BEvent<BMouseEventHandler> mouseClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDoubleClickEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseDownEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BMouseEventHandler> mouseUpEvent = new BEvent<BMouseEventHandler>();
    public final BEvent<BEventHandler> mouseEnterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> mouseLeaveEvent = new BEvent<BEventHandler>();

    /* root impl of MouseMotionListener */
    // root impl of MouseMotionListener is in BButton
    public final BEvent<BMouseEventHandler> mouseMoveEvent = new BEvent<BMouseEventHandler>();

    /* root impl of FocusListener */
    // root impl of FocusListener is in BButton
    public final BEvent<BEventHandler> enterEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> leaveEvent = new BEvent<BEventHandler>();

    /* root impl of KeyListener */
    // root impl of KeyListener is in BButton
    public final BEvent<BPreviewKeyDownEventHandler> previewKeyDownEvent = new BEvent<BPreviewKeyDownEventHandler>();
    public final BEvent<BKeyEventHandler> keyDownEvent = new BEvent<BKeyEventHandler>();
    public final BEvent<BKeyEventHandler> keyUpEvent = new BEvent<BKeyEventHandler>();
    public final BEvent<BKeyPressEventHandler> keyPressEvent = new BEvent<BKeyPressEventHandler>();

    /* root impl of ComponentListener */
    // root impl of ComponentListener is in BButton
    public final BEvent<BEventHandler> visibleChangedEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> resizeEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> sizeChangedEvent = new BEvent<BEventHandler>();
    public final BEvent<BEventHandler> locationChangedEvent = new BEvent<BEventHandler>();

    public BButton() {
        super();
        addActionListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
        addKeyListener(this);
        addComponentListener(this);
        addMouseWheelListener(this);
    }

    public void setMnemonic(int value) {
        String text = getText();
        int index = text.indexOf(value);

        if (index < 0) {
            text += (" (" + Character.toString((char) value) + ")");
            index = text.lastIndexOf(value);
            setText(text);
        }

        super.setMnemonic(value);
        super.setDisplayedMnemonicIndex(index);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        BMouseButtons btn = BMouseButtons.Middle;

        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            btn = BMouseButtons.Left;

            break;

        case MouseEvent.BUTTON2:
            btn = BMouseButtons.Middle;

            break;

        case MouseEvent.BUTTON3:
            btn = BMouseButtons.Right;

            break;
        }

        BMouseEventArgs ev = new BMouseEventArgs(btn, e.getClickCount(),
                e.getX(), e.getY(), e.getWheelRotation() * e.getUnitsToScroll());

        try {
            mouseWheelEvent.raise(this, ev);
        } catch (Exception ex) {
            System.err.println("BButton#mouseWheelMoved; ex=" + ex);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        BPaintEventArgs e = new BPaintEventArgs(g);

        try {
            paintEvent.raise(this, e);
        } catch (Exception ex) {
            System.err.println("BButton#paint; ex=" + ex);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            clickEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#actionPerformed; ex=" + ex);
        }
    }

    public void mouseClicked(MouseEvent e) {
        try {
            mouseClickEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));

            if (e.getClickCount() >= 2) {
                mouseDoubleClickEvent.raise(this,
                    BMouseEventArgs.fromMouseEvent(e));
            }
        } catch (Exception ex) {
            System.err.println("BButton#mouseClicked; ex=" + ex);
        }
    }

    public void mouseEntered(MouseEvent e) {
        try {
            mouseEnterEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#mouseEntered; ex=" + ex);
        }
    }

    public void mouseExited(MouseEvent e) {
        try {
            mouseLeaveEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#mouseExited; ex=" + ex);
        }
    }

    public void mousePressed(MouseEvent e) {
        try {
            mouseDownEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mousePressed; ex=" + ex);
        }
    }

    public void mouseReleased(MouseEvent e) {
        try {
            mouseUpEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mouseReleased; ex=" + ex);
        }
    }

    public void mouseDragged(MouseEvent e) {
        try {
            mouseMoveEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mouseDragged; ex=" + ex);
        }
    }

    public void mouseMoved(MouseEvent e) {
        try {
            mouseMoveEvent.raise(this, BMouseEventArgs.fromMouseEvent(e));
        } catch (Exception ex) {
            System.err.println("BButton#mouseMoved; ex=" + ex);
        }
    }

    public void focusGained(FocusEvent e) {
        try {
            enterEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#focusGained; ex=" + ex);
        }
    }

    public void focusLost(FocusEvent e) {
        try {
            leaveEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#focusLost; ex=" + ex);
        }
    }

    public void keyPressed(KeyEvent e) {
        try {
            previewKeyDownEvent.raise(this, new BPreviewKeyDownEventArgs(e));
            keyDownEvent.raise(this, new BKeyEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyPressed; ex=" + ex);
        }
    }

    public void keyReleased(KeyEvent e) {
        try {
            keyUpEvent.raise(this, new BKeyEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyReleased; ex=" + ex);
        }
    }

    public void keyTyped(KeyEvent e) {
        try {
            previewKeyDownEvent.raise(this, new BPreviewKeyDownEventArgs(e));
            keyPressEvent.raise(this, new BKeyPressEventArgs(e));
        } catch (Exception ex) {
            System.err.println("BButton#keyType; ex=" + ex);
        }
    }

    public void componentHidden(ComponentEvent e) {
        try {
            visibleChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentHidden; ex=" + ex);
        }
    }

    public void componentMoved(ComponentEvent e) {
        try {
            locationChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentMoved; ex=" + ex);
        }
    }

    public void componentResized(ComponentEvent e) {
        try {
            resizeEvent.raise(this, new BEventArgs());
            sizeChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentResized; ex=" + ex);
        }
    }

    public void componentShown(ComponentEvent e) {
        try {
            visibleChangedEvent.raise(this, new BEventArgs());
        } catch (Exception ex) {
            System.err.println("BButton#componentShown; ex=" + ex);
        }
    }
}

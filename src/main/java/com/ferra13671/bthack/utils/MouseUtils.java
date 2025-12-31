package com.ferra13671.bthack.utils;

import com.ferra13671.bthack.mixins.IMouseHandler;

public class MouseUtils implements Mc {

    public static int getMouseX() {
        return (int) ((IMouseHandler) mc.mouseHandler).bthack$$$getXPos();
    }

    public static int getMouseY() {
        return (int) ((IMouseHandler) mc.mouseHandler).bthack$$$getYPos();
    }
}

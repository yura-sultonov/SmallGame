package com.sorokin.dev.smallgame;

import android.graphics.RectF;

public class Cube {
    int x, y;
    boolean isFly;

    public Cube(int fieldWidth){
        y = 0;
        x = (int)(Math.random() * (float)fieldWidth);
        isFly = true;
    }



}

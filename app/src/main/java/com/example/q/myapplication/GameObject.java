package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {

    protected Bitmap imageLeft;
    protected Bitmap imageRight;
    protected int direction;
    protected int x, y;
    protected final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    protected final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public GameObject(Bitmap l, Bitmap r) {
        imageLeft = l;
        imageRight = r;
    }

    public void draw(Canvas canvas) {
        if (direction > 0) {
            canvas.drawBitmap(imageRight, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
        }
        else {
            canvas.drawBitmap(imageLeft, x - imageLeft.getWidth() / 2, y - imageLeft.getHeight() / 2, null);
        }
    }

    // Must be overwritten.
    public void update() {
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setDir(int dir) {
        direction = dir;
    }

    public int getHeight() { return imageLeft.getHeight(); }
    public int getWidth() { return imageLeft.getWidth(); }

    public boolean isInside(int eventX, int eventY) {
        return (x - imageLeft.getWidth()/2 <= eventX) && (eventX <= x + imageLeft.getWidth()/2)
                && (y - imageLeft.getHeight()/2 <= eventY) && (eventY <= y + imageLeft.getHeight()/2);
    }
}

package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {

    protected Bitmap image;
    protected int x, y;
    protected final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    protected final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public GameObject(Bitmap bitmap) {
        image = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
    }

    // Must be overwritten.
    public void update() {
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getHeight() { return image.getHeight(); }
    public int getWidth() { return image.getWidth(); }

    public boolean isInside(int eventX, int eventY) {
        return (x - image.getWidth()/2 <= eventX) && (eventX <= x + image.getWidth()/2)
                && (y - image.getHeight()/2 <= eventY) && (eventY <= y + image.getHeight()/2);
    }
}

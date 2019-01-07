package com.example.q.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Button;

public class ButtonObject {
    private Bitmap image;
    private int x, y;

    public ButtonObject(Bitmap bitmap) {
        image = bitmap;
    }

    public void draw(Canvas canvas) {
            canvas.drawBitmap(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);
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

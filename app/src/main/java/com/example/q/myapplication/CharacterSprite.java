package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {
    protected Bitmap image;
    protected int x, y;
    protected int xVelocity, yVelocity;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
        xVelocity = 0;
        yVelocity = 0;
    }

    public void setXY(int x_, int y_) {
        x = x_;
        y = y_;
    }

    public void setVelocity(int xV, int yV) {
        xVelocity = xV;
        yVelocity = yV;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getXVelocity() {return xVelocity;}
    public int getYVelocity() {return yVelocity;}
    public int getHeight() { return image.getHeight(); }
    public int getWidth() { return image.getWidth(); }

    public boolean isInside(int eventX, int eventY) {
        //System.out.printf("%d %d %d %d %d %d\n",eventX, eventY, x, image.getWidth(), y, image.getHeight());
        return (x - image.getWidth()/2 <= eventX) && (eventX <= x + image.getWidth()/2)
                && (y - image.getHeight()/2 <= eventY) && (eventY <= y + image.getHeight()/2);
    }

}

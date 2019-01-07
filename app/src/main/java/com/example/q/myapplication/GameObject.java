package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {

    protected Bitmap image;
    protected int x, y;
    protected int vX, vY;
    protected boolean isJump;

    private final int INIT_VELOCITY = 40;
    private final int JUMP_VELOCITY = 70;
    private final int JUMP_ACCELERATION = -70;
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public GameObject(Bitmap bitmap) {
        image = bitmap;
        vX = 0;
        vY = 0;
        isJump = false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
    }

    public void update() {
        if (isJump) {
            x += vX;
            vX += JUMP_ACCELERATION;
            if (x <= -JUMP_VELOCITY) {
                isJump = false;
            }
        }
        y += vY;

        if (x < image.getWidth()/2) {
            x = image.getWidth()/2;
            isJump = false;
        }
        else if (x > screenWidth) {
            x = screenWidth;
            vX = 0;
        }

        if (y < 0) {
            y = 0;
        }
        else if (y > screenHeight) {
            y = screenHeight;
        }
    }

    public void jump() {
        if (!isJump) {
            isJump = true;
            vX = JUMP_VELOCITY;
        }
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dir) {
        if (dir == 0) {
            this.vY = -INIT_VELOCITY;
        }
        else if (dir == 1) {
            this.vY = INIT_VELOCITY;
        }
    }

    public void stop() {
        vY = 0;
    }

    public boolean isInside(int eventX, int eventY) {
        return (x - image.getWidth()/2 <= eventX) && (eventX <= x + image.getWidth()/2)
                && (y - image.getHeight()/2 <= eventY) && (eventY <= y + image.getHeight()/2);
    }
}

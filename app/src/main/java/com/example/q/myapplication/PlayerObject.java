package com.example.q.myapplication;

import android.graphics.Bitmap;

public class PlayerObject extends GameObject {
    protected int vX, vY;
    protected boolean isJump;
    private final int INIT_VELOCITY = 40;
    private final int JUMP_VELOCITY = -70;
    private final int JUMP_ACCELERATION = 70;

    public PlayerObject(Bitmap bitmapLeft, Bitmap bitmapRight) {
        super(bitmapLeft, bitmapRight);
        isJump = false;
        vY = 0;
        vX = 0;
    }

    @Override
    public void update() {
        if (isJump) {
            y += vY;
            vY += JUMP_ACCELERATION;
            if (y <= -JUMP_VELOCITY) {
                isJump = false;
            }
        }
        x += direction*vX;

        if (y < imageLeft.getHeight()/2) {
            x = imageLeft.getHeight()/2;
            isJump = false;
        }
        else if (y > 1140 - getHeight()/2) {
            y = 1140 - getHeight()/2;
            vY = 0;
        }

        if (x < 0) {
            x = 0;
        }
        else if (x > screenHeight) {
            x = screenHeight;
        }
    }

    public void jump() {
        if (!isJump) {
            isJump = true;
            vY = JUMP_VELOCITY;
        }
    }

    public void move(int dir) {
        vX = INIT_VELOCITY;
        direction = dir;
    }

    public void stop() {
        vX = 0;
    }

    public int getDir() {
        return direction;
    }
}

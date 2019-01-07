package com.example.q.myapplication;

import android.graphics.Bitmap;

public class PlayerObject extends GameObject {
    protected int vX, vY;
    protected boolean isJump;
    private final int INIT_VELOCITY = 40;
    private final int JUMP_VELOCITY = 70;
    private final int JUMP_ACCELERATION = -70;

    public PlayerObject(Bitmap bitmap) {
        super(bitmap);
        isJump = false;
        vX = 0;
        vY = 0;
    }

    @Override
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

    public void move(int dir) {
        this.vY = INIT_VELOCITY * dir;
    }

    public void stop() {
        vY = 0;
    }

    public int getDir() {
        if (vY < 0) return 0;
        else return 1;
    }
}

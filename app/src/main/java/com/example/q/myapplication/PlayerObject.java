package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class PlayerObject extends GameObject {
    protected int vX, vY;
    protected boolean isJump;
    private final int INIT_VELOCITY = 40;
    private final int JUMP_VELOCITY = -180;
    private final int JUMP_ACCELERATION = 45;
    protected int hp = 100;//toDo


    private Bitmap imageLeft2;
    private Bitmap imageLeft3;
    private Bitmap imageRight2;
    private Bitmap imageRight3;

    public PlayerObject(Tab3View view, Bitmap bitmapLeft, Bitmap bitmapRight) {
        super(bitmapLeft, bitmapRight);
        isJump = false;
        vY = 0;
        vX = 0;
        imageLeft2 = BitmapFactory.decodeResource(view.getResources(),R.drawable.player_left2);
        imageLeft3 = BitmapFactory.decodeResource(view.getResources(),R.drawable.player_left3);
        imageRight2 = BitmapFactory.decodeResource(view.getResources(),R.drawable.player_right2);
        imageRight3 = BitmapFactory.decodeResource(view.getResources(),R.drawable.player_right3);
    }

    @Override
    public void draw(Canvas canvas) {
        if (direction > 0) {
            if ((x % 9) < 3) {
                canvas.drawBitmap(imageRight, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
            else if ((x % 9) < 6) {
                canvas.drawBitmap(imageRight2, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
            else {
                canvas.drawBitmap(imageRight3, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
        }
        else {
            if ((x % 9) < 3) {
                canvas.drawBitmap(imageLeft, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
            else if ((x % 9) < 6) {
                canvas.drawBitmap(imageLeft2, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
            else {
                canvas.drawBitmap(imageLeft3, x - imageRight.getWidth() / 2, y - imageRight.getHeight() / 2, null);
            }
        }
    }

    @Override
    public void update() {
        if (isJump) {
            System.out.println(vY);
            y += vY;
            vY += JUMP_ACCELERATION;
        }
        x += direction*vX;

        if (y < getHeight()/2) {
            y = getHeight()/2;
        }
        else if (y >  850 - getHeight()/2) {
            y = 850 - getHeight()/2;
            isJump = false;
        }

        if (x < 0) {
            x = 0;
        }
        else if (x > screenWidth) {
            x = screenWidth;
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

    public void attacked() {
        hp -= 10;
    }

    public int getHp(){
        return hp;
    }
}

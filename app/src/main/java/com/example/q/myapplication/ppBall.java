package com.example.q.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;

import java.util.Random;

public class ppBall extends CharacterSprite {
    private int xVelocity;
    private int yVelocity;
    private boolean hadCollideA;
    private boolean hadCollideB;
    private boolean hadCollideRightWall;
    private boolean hadCollideLeftWall;
    private int maxVelocity = 70;
    private int initVelocity = 40;
    private int radius = image.getWidth()/2;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public ppBall(Bitmap bmp) {
        super(bmp);
        init();
    }

    public void init() {
        xVelocity = initVelocity;
        yVelocity = initVelocity;
        Random generator = new Random(System.nanoTime());
        int randX = generator.nextFloat() > 0.5 ? -1 : 1;
        int randY = generator.nextFloat() > 0.5 ? -1 : 1;
        xVelocity *= randX;
        yVelocity *= randY;
        hadCollideA = false;
        hadCollideB = false;
        hadCollideRightWall = false;
        hadCollideLeftWall = false;
    }

    public void update(CharacterSprite plateA, CharacterSprite plateB) {
        if (!hadCollideRightWall && (x >= screenWidth - radius)) {
            xVelocity *= -1;
            hadCollideRightWall = true;
            hadCollideLeftWall = false;
        }
        else if (!hadCollideLeftWall && (x <= radius)) {
            xVelocity *= -1;
            hadCollideLeftWall = true;
            hadCollideRightWall = false;
        }
        if (!hadCollideA) {
            int leftBound = plateA.getX() - plateA.getWidth()/2;
            int rightBound = plateA.getX() + plateA.getWidth()/2;
            int upperBound = plateA.getY() - plateA.getHeight()/2;
            if ((upperBound - y <= radius) && (upperBound >= y) && (x + radius >= leftBound) && (x - radius <= rightBound)) {
                y = upperBound - radius;
                if (plateA.getYVelocity() == 0) {
                    yVelocity *= -1;
                }
                else {
                    xVelocity = plateA.getXVelocity();
                    yVelocity = plateA.getYVelocity();
                }
                hadCollideA = true;
                hadCollideB = false;
                hadCollideLeftWall = false;
                hadCollideRightWall = false;
            }
        }
        if (!hadCollideB) {
            int leftBound = plateB.getX() - plateB.getWidth()/2;
            int rightBound = plateB.getX() + plateB.getWidth()/2;
            int lowerBound = plateB.getY() + plateB.getHeight()/2;
            if ((y - lowerBound <= radius) && (lowerBound <= y) && (x + radius >= leftBound) && (x - radius <= rightBound)) {
                y = lowerBound + radius;
                if (plateB.getYVelocity() == 0) {
                    yVelocity *= -1;
                }
                else {
                    xVelocity = plateB.getXVelocity();
                    yVelocity = plateB.getYVelocity();
                }
                hadCollideB = true;
                hadCollideA = false;
                hadCollideLeftWall = false;
                hadCollideRightWall = false;
            }
        }
        xVelocity = xVelocity > maxVelocity ? maxVelocity : xVelocity;
        xVelocity = xVelocity < -maxVelocity ? -maxVelocity : xVelocity;
        yVelocity = yVelocity > maxVelocity ? maxVelocity : yVelocity;
        yVelocity = yVelocity < -maxVelocity ? -maxVelocity : yVelocity;

        x += xVelocity;
        y += yVelocity;
    }

    public int gameOver() {
        if (y + radius >= screenHeight) return 1;
        else if (y <= radius) return 2;
        else return 0;
    }

}

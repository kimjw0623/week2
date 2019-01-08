package com.example.q.myapplication;

import android.graphics.Bitmap;

public class BulletObject extends GameObject {
    private int vX = 50;
    private Tab3View view;
    private int ownPlayer;

    public BulletObject(Bitmap bitmapLeft, Bitmap bitmapRight, Tab3View tab3View, int player, int dir) {
        super(bitmapLeft, bitmapRight);
        view = tab3View;
        ownPlayer = player;
        direction = dir;
    }

    @Override
    public void update() {
        x += direction*vX;
        if (x >= screenWidth || x <= 0) {
            view.removeABullet(this);
            view.removeBBullet(this);
        }
    }
}

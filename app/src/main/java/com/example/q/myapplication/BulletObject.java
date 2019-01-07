package com.example.q.myapplication;

import android.graphics.Bitmap;

public class BulletObject extends GameObject {
    private int vY = 50;
    private Tab3View view;

    public BulletObject(Bitmap bitmap, Tab3View tab3View) {
        super(bitmap);
        view = tab3View;
    }

    @Override
    public void update() {
        y += vY;
        if (y >= screenHeight || y <= 0) {
            view.removeBullet(this);
        }
    }

    public void setDir(int direction) {
        vY *= direction;
    }
}

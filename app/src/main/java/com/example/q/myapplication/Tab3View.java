package com.example.q.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Tab3View extends SurfaceView implements SurfaceHolder.Callback {

    private GThread thread;
    public Tab3View(Context context){
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        android.graphics.Canvas c = null;
        try {
            c = getHolder().lockCanvas(null);

            synchronized(getHolder())
            {
                android.graphics.Paint p = new android.graphics.Paint();
                p.setColor(android.graphics.Color.RED);
                c.drawRect(0,0,100,100, p);
            }
        }
        finally
        {
            if(c != null) getHolder().unlockCanvasAndPost(c);
        }
        thread = new GThread();
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //we will do something here

                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //do something here
                break;
        }
        return true;
    }
}

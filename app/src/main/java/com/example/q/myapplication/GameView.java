package com.example.q.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.MainThread;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.VelocityTracker;

import static com.example.q.myapplication.mainThread.canvas;
import static java.lang.Thread.sleep;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private mainThread thread;

    private ppBall ball;
    private CharacterSprite plateA;
    private CharacterSprite plateB;
    private boolean onPlateA;
    private boolean onPlateB;
    private int idPlateA;
    private int idPlateB;
    private VelocityTracker velocityTracker;
    private Paint paint;
    private Paint paintBig;

    private boolean gameStarted;
    private int playerLose;

    private int screenWidth;
    private int screenHeight;
    private int averageFPS;
    private long startTime;
    private long timeElapsed;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        thread = new mainThread(getHolder(), this);

        ball = new ppBall(BitmapFactory.decodeResource(getResources(),R.drawable.ball));
        plateA = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.plate_a));
        plateB = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.plate_b));

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.MONOSPACE);

        paintBig = new Paint();
        paintBig.setColor(Color.WHITE);
        paintBig.setTextSize(200);
        paintBig.setTypeface(Typeface.SANS_SERIF);

        setFocusable(true);
    }

    public void init_game() {
        ball.setXY(screenWidth/2, screenHeight/2);
        ball.init();
        plateA.setXY(screenWidth/2, screenHeight - 200);
        plateA.setVelocity(0, 0);
        plateB.setXY(screenWidth/2, 200);
        plateB.setVelocity(0, 0);
        onPlateA = false;
        onPlateB = false;
        gameStarted = false;
        averageFPS = 0;
        playerLose = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init_game();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update(double _averageFPS) {
        if (gameStarted) {
            averageFPS = (int)_averageFPS;
            ball.update(plateA, plateB);
            int temp;
            if ((temp = ball.gameOver()) != 0) {
                init_game();
                playerLose = temp;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            if (playerLose == 0) {
                int fps = gameStarted ? averageFPS : 0;
                timeElapsed = gameStarted ? (System.nanoTime() - startTime) / 1000000000 : 0;
                canvas.drawText("FPS: "+Integer.toString(fps),
                                200, screenHeight/2, paint);
                canvas.drawText("Time: " + Long.toString(timeElapsed),
                                screenWidth - 450, screenHeight / 2, paint);
                ball.draw(canvas);
                plateA.draw(canvas);
                plateB.draw(canvas);
            }
            else {
                canvas.drawText("GAME OVER",
                                screenWidth/2-550, screenHeight/2-50, paintBig);
                canvas.drawText("Playtime: "+Long.toString(timeElapsed)+"s",
                                screenWidth/2-250, screenHeight/2+100, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (playerLose != 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                playerLose = 0;
            }
            return true;
        }
        else if (!gameStarted) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gameStarted = true;
                startTime = System.nanoTime();
            }
            return true;
        }

        int pointer_count = event.getPointerCount();
        pointer_count = pointer_count > 2 ? 2 : pointer_count;

        int eventX, eventY;
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                for (int i = 0 ; i < pointer_count ; i++) {
                    eventX = (int)event.getX(i);
                    eventY = (int)event.getY(i);
                    if (plateA.isInside(eventX, eventY)) {
                        onPlateA = true;
                        idPlateA = event.getPointerId(i);
                        if (velocityTracker == null) {
                            velocityTracker = VelocityTracker.obtain();
                        }
                        else {
                            velocityTracker.clear();
                        }
                        velocityTracker.addMovement(event);
                    }
                    else if (plateB.isInside(eventX, eventY)) {
                        onPlateB = true;
                        idPlateB = event.getPointerId(i);
                        if (velocityTracker == null) {
                            velocityTracker = VelocityTracker.obtain();
                        }
                        else {
                            velocityTracker.clear();
                        }
                        velocityTracker.addMovement(event);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (onPlateA || onPlateB) {
                    velocityTracker.addMovement(event);
                    velocityTracker.computeCurrentVelocity(1000);
                }

                for (int i = 0 ; i < pointer_count ; i++) {
                    int currId = event.getPointerId(i);
                    eventX = (int)event.getX(i);
                    eventY = (int)event.getY(i);

                    if (onPlateA && (idPlateA == currId)) {
                        plateA.setXY(eventX, eventY);
                        int vX = (int)(velocityTracker.getXVelocity(idPlateA));
                        int vY = (int)(velocityTracker.getYVelocity(idPlateA)*1.5);
                        vY = vY < 0 ? vY : 0;
                        plateA.setVelocity(vX, vY);
                    }
                    else if (onPlateB && (idPlateB == currId)) {
                        plateB.setXY(eventX,eventY);
                        int vX = (int)(velocityTracker.getXVelocity(idPlateB));
                        int vY = (int)(velocityTracker.getYVelocity(idPlateB)*1.5);
                        vY = vY > 0 ? vY : 0;
                        plateB.setVelocity(vX, vY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0 ; i < pointer_count ; i++) {
                    int currId = event.getPointerId(i);

                    if (onPlateA && (idPlateA == currId)) {
                        onPlateA = false;
                    }
                    else if (onPlateB && (idPlateB == currId)) {
                        onPlateB = false;
                    }
                }
                if (!onPlateA && !onPlateB && (velocityTracker != null)) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;

        }
        return true;
    }

}

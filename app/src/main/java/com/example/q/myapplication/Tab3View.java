package com.example.q.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Tab3View extends SurfaceView implements SurfaceHolder.Callback {

    private GThread thread;
    private Socket socket;
    private SocketHelper socketHelper;

    private int currentPlayer;
    private PlayerObject playerA;
    private PlayerObject playerB;
    private ArrayList<BulletObject> bulletListA;
    private ArrayList<BulletObject> bulletListB;
    private Bitmap background;

    private ButtonObject buttonLeft;
    private ButtonObject buttonRight;
    private ButtonObject buttonJump;
    private ButtonObject buttonFire;

    private int screenHeight;
    private int screenWidth;

    private Paint paint;
    private int isEnd;
    private int cool=15;

    public Tab3View(Context context){
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        socket = GameDataHandler.getSocket();
        currentPlayer = GameDataHandler.getPlayerId();

        paint = new Paint();
        isEnd = 0;

        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);

        playerA = new PlayerObject(this, BitmapFactory.decodeResource(getResources(),R.drawable.player_left),
                                    BitmapFactory.decodeResource(getResources(),R.drawable.player_right));
        playerB = new PlayerObject(this, BitmapFactory.decodeResource(getResources(),R.drawable.player_left),
                                    BitmapFactory.decodeResource(getResources(),R.drawable.player_right));
        bulletListA = new ArrayList<>();
        bulletListB = new ArrayList<>();

        playerA.setXY(200, 850 - playerA.getHeight()/2);
        playerA.setDir(1);
        playerB.setXY(screenWidth-200, 850 - playerB.getHeight()/2);
        playerB.setDir(-1);

        socketHelper = new SocketHelper(this);
        socket.on("MOVE", socketHelper.onMoveReceived);
        socket.on("STOP", socketHelper.onStopReceived);
        socket.on("JUMP", socketHelper.onJumpReceived);
        socket.on("FIRE", socketHelper.onFireReceived);
        buttonLeft = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.icon_left));
        buttonLeft.setXY(200, 970);
        buttonRight = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.icon_right));
        buttonRight.setXY(500, 970);
        buttonJump = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
        buttonJump.setXY(screenWidth-450, 970);
        buttonFire = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
        buttonFire.setXY(screenWidth-200, 970);
    }

    public void update() {
        playerA.update();
        playerB.update();
        for (int i = 0 ; i < bulletListA.size(); i++) {
            bulletListA.get(i).update();
        }
        for (int i = 0 ; i < bulletListB.size(); i++) {
            bulletListB.get(i).update();
        }
        //when attacked
        for(int i = 0 ; i < bulletListB.size(); i++){
            BulletObject temp = bulletListB.get(i);
            if(Math.abs(playerA.getX()-temp.getX()) < (playerA.getWidth()/2 + temp.getWidth()/2) &&
                    Math.abs(playerA.getY()-temp.getY()) < (playerA.getHeight()/2 + temp.getHeight()/2)){
                bulletListB.remove(i);
                playerA.attacked();
            }
        }
        for(int i = 0 ; i < bulletListA.size(); i++){
            BulletObject temp = bulletListA.get(i);
            if(Math.abs(playerB.getX()-temp.getX()) < (playerB.getWidth()/2 + temp.getWidth()/2) &&
                    Math.abs(playerB.getY()-temp.getY()) < (playerB.getHeight()/2 + temp.getHeight()/2)){
                bulletListA.remove(i);
                playerB.attacked();
            }
        }////bullet must have id
        if(playerA.getHp()<=0){
            isEnd = 1;
        }
        if(playerB.getHp()<=0){
            isEnd = 2;
        }
        if(cool==0){
            cool=15;
        }
        if(cool!=1){
            cool-=1;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            if(isEnd==1){
                //A win
                paint.setColor(Color.WHITE);
                paint.setTextSize(200);
                canvas.drawText("PLAYER A win!",
                        screenWidth/2-650, screenHeight/2, paint);
            }
            else if(isEnd==2){
                //B win
                paint.setColor(Color.WHITE);
                paint.setTextSize(200);
                canvas.drawText("PLAYER B win!",
                        screenWidth/2-650, screenHeight/2, paint);
            }
            else {
                canvas.drawBitmap(background, 0, 0, null);
                playerA.draw(canvas);

                playerB.draw(canvas);

                for (int i = 0; i < bulletListA.size(); i++) {
                    bulletListA.get(i).draw(canvas);
                }
                for (int i = 0; i < bulletListB.size(); i++) {
                    bulletListB.get(i).draw(canvas);
                }
                buttonLeft.draw(canvas);
                buttonRight.draw(canvas);
                buttonJump.draw(canvas);
                buttonFire.draw(canvas);

                paint.setColor(Color.BLACK);
                canvas.drawRect(playerA.getX() - playerA.getWidth() / 2, playerA.getY() - playerA.getWidth() / 2 - 25,
                        playerA.getX() + playerA.getWidth() / 2, playerA.getY() - playerA.getWidth() / 2 - 40, paint);
                paint.setColor(Color.RED);
                canvas.drawRect(playerA.getX() - playerA.getWidth() / 2, playerA.getY() - playerA.getWidth() / 2 - 25,
                        playerA.getX() - playerA.getWidth() / 2 + (playerA.getWidth()*(playerA.getHp()))/100, playerA.getY() - playerA.getWidth() / 2 - 40, paint);
                paint.setColor(Color.BLACK);
                canvas.drawRect(playerB.getX() - playerB.getWidth() / 2, playerB.getY() - playerB.getWidth() / 2 - 25,
                        playerB.getX() + playerB.getWidth() / 2, playerB.getY() - playerB.getWidth() / 2 - 40, paint);
                paint.setColor(Color.RED);
                canvas.drawRect(playerB.getX() - playerB.getWidth() / 2, playerB.getY() - playerB.getWidth() / 2 - 25,
                        playerB.getX() - playerB.getWidth() / 2 + (playerB.getWidth()*(playerB.getHp()))/100, playerB.getY() - playerB.getWidth() / 2 - 40, paint);

            }
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread = new GThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        socket.disconnect();
        socket.off("MOVE", socketHelper.onMoveReceived);
        socket.off("STOP", socketHelper.onStopReceived);
        socket.off("JUMP", socketHelper.onJumpReceived);
        socket.off("FIRE", socketHelper.onFireReceived);
        socket.close();
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
    public boolean onTouchEvent(MotionEvent event) {
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
                    if (buttonLeft.isInside(eventX, eventY)) {
                        //Toast.makeText(getContext(), "Button down", Toast.LENGTH_SHORT).show();
                        socketHelper.sendMove(currentPlayer, -1);
                    }
                    if (buttonRight.isInside(eventX, eventY)) {
                        //Toast.makeText(getContext(), "Button down", Toast.LENGTH_SHORT).show();
                        socketHelper.sendMove(currentPlayer, 1);
                    }
                    if (buttonFire.isInside(eventX, eventY)) {
                        //Toast.makeText(getContext(), "Button down", Toast.LENGTH_SHORT).show();
                        if(cool==1) {
                            socketHelper.sendFire(currentPlayer);
                            cool-=1;
                        }
                    }
                    if (buttonJump.isInside(eventX, eventY)) {
                        //Toast.makeText(getContext(), "Button down", Toast.LENGTH_SHORT).show();
                        socketHelper.sendJump(currentPlayer);
                    }

                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0 ; i < pointer_count ; i++) {
                    eventX = (int)event.getX(i);
                    eventY = (int)event.getY(i);
                    if (buttonLeft.isInside(eventX, eventY)) {
                        socketHelper.sendStop(currentPlayer);
                    }
                    if (buttonRight.isInside(eventX, eventY)) {
                        socketHelper.sendStop(currentPlayer);
                    }
                }
                break;

        }
        return true;
    }

    public void removeABullet(BulletObject bullet) {
        bulletListA.remove(bullet);
    }
    public void removeBBullet(BulletObject bullet) {
        bulletListB.remove(bullet);
    }

    public Socket getSocket() {
        return socket;
    }
    public PlayerObject getPlayerA() {return playerA;}
    public PlayerObject getPlayerB() {return playerB;}
    public ArrayList<BulletObject> getBulletListA() {return bulletListA;}
    public ArrayList<BulletObject> getBulletListB() {return bulletListB;}
}

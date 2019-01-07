package com.example.q.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    private ArrayList<BulletObject> bulletList;
    private Bitmap background;

    private ButtonObject buttonLeft;
    private ButtonObject buttonRight;
    private ButtonObject buttonJump;
    private ButtonObject buttonFire;

    private int screenHeight;
    private int screenWidth;

    public Tab3View(Context context){
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);

        playerA = new PlayerObject(BitmapFactory.decodeResource(getResources(),R.drawable.player_left),
                                    BitmapFactory.decodeResource(getResources(),R.drawable.player_right));
        playerB = new PlayerObject(BitmapFactory.decodeResource(getResources(),R.drawable.player_left),
                                    BitmapFactory.decodeResource(getResources(),R.drawable.player_right));
        bulletList = new ArrayList<>();

        playerA.setXY(200, 1140 - playerA.getHeight()/2);
        playerA.setDir(1);
        playerB.setXY(1000, 1140 - playerB.getHeight()/2);
        playerB.setDir(-1);


        currentPlayer = 0;

        try {
            String serverUrl = "http://socrip4.kaist.ac.kr:680/";
            socketHelper = new SocketHelper(this);
            socket = IO.socket(serverUrl);
            socket.on("MOVE", socketHelper.onMoveReceived);
            socket.on("STOP", socketHelper.onStopReceived);
            socket.on("JUMP", socketHelper.onJumpReceived);
            socket.on("FIRE", socketHelper.onFireReceived);
            socket.connect();

            buttonLeft = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.icon_left));
            buttonLeft.setXY(200, 1300);
            buttonRight = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.icon_right));
            buttonRight.setXY(500, 1300);
            buttonJump = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
            buttonJump.setXY(screenWidth-450, 1300);
            buttonFire = new ButtonObject(BitmapFactory.decodeResource(getResources(), R.drawable.circle));
            buttonFire.setXY(screenWidth-200, 1300);
            Toast.makeText(getContext(), "Server connected", Toast.LENGTH_SHORT).show();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Server failure", Toast.LENGTH_SHORT).show();
        }
    }

    public void update() {
        playerA.update();
        playerB.update();
        for (int i = 0 ; i < bulletList.size(); i++) {
            bulletList.get(i).update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawBitmap(background, 0, 0, null);
            playerA.draw(canvas);
            playerB.draw(canvas);
            for (int i = 0 ; i < bulletList.size(); i++) {
                bulletList.get(i).draw(canvas);
            }
            buttonLeft.draw(canvas);
            buttonRight.draw(canvas);
            buttonJump.draw(canvas);
            buttonFire.draw(canvas);
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
                        socketHelper.sendMove(currentPlayer, -1);
                    }
                    if (buttonRight.isInside(eventX, eventY)) {
                        socketHelper.sendMove(currentPlayer, 1);
                    }
                    if (buttonFire.isInside(eventX, eventY)) {
                        socketHelper.sendFire(currentPlayer);
                    }
                    if (buttonJump.isInside(eventX, eventY)) {
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

    public void removeBullet(BulletObject bullet) {
        bulletList.remove(bullet);
    }

    public Socket getSocket() {
        return socket;
    }
    public PlayerObject getPlayerA() {return playerA;}
    public PlayerObject getPlayerB() {return playerB;}
    public ArrayList<BulletObject> getBulletList() {return bulletList;}
}

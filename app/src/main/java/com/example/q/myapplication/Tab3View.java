package com.example.q.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

    public Tab3View(Context context){
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        playerA = new PlayerObject(/*BITMAP FILE*/);
        playerB = new PlayerObject(/*BITMAP FILE*/);
        bulletList.clear();

        try {
            String serverUrl = "http://socrip4.kaist.ac.kr:680/";
            socketHelper = new SocketHelper(this);
            socket = IO.socket(serverUrl);
            socket.on("MOVE", socketHelper.onMoveReceived);
            socket.on("STOP", socketHelper.onStopReceived);
            socket.on("JUMP", socketHelper.onJumpReceived);
            socket.on("FIRE", socketHelper.onFireReceived);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void update() {

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
        thread = new GThread(getHolder(), this);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        socket.disconnect();
        socket.close();
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

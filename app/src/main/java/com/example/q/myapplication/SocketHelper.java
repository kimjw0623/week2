package com.example.q.myapplication;

import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketHelper {
    private Tab3View view;
    private Socket socket;
    private PlayerObject playerA;
    private PlayerObject playerB;
    private ArrayList<BulletObject> bulletListA;
    private ArrayList<BulletObject> bulletListB;

    public SocketHelper(Tab3View tab3View) {
        this.view = tab3View;
        this.socket = view.getSocket();
        this.playerA = view.getPlayerA();
        this.playerB = view.getPlayerB();
        this.bulletListA = view.getBulletListA();
        this.bulletListB = view.getBulletListB();
    }

    public Emitter.Listener onMoveReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject receivedData = (JSONObject) args[0];
                int playerId = receivedData.getInt("playerId");
                int direction = receivedData.getInt("direction");
                if (playerId == 0) {
                    playerA.move(direction);
                }
                else if (playerId == 1) {
                    playerB.move(direction);
                }
                else {
                    throw new JSONException("playerId Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener onStopReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject receivedData = (JSONObject) args[0];
                int playerId = receivedData.getInt("playerId");
                if (playerId == 0) {
                    playerA.stop();
                }
                else if (playerId == 1) {
                    playerB.stop();
                }
                else {
                    throw new JSONException("playerId Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener onJumpReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject receivedData = (JSONObject) args[0];
                int playerId = receivedData.getInt("playerId");
                if (playerId == 0) {
                    playerA.jump();
                }
                else if (playerId == 1) {
                    playerB.jump();
                }
                else {
                    throw new JSONException("playerId Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Emitter.Listener onFireReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject receivedData = (JSONObject) args[0];
                int playerId = receivedData.getInt("playerId");


                if (playerId == 0) {
                    BulletObject bullet = new BulletObject(BitmapFactory.decodeResource(view.getResources(),R.drawable.bullet_left),
                            BitmapFactory.decodeResource(view.getResources(),R.drawable.bullet_right), view, playerId, playerA.getDir());
                    bullet.setXY(playerA.x + playerA.getDir()*playerA.getWidth()/2, playerA.y+50);
                    bulletListA.add(bullet);
                }
                else if (playerId == 1) {
                    BulletObject bullet = new BulletObject(BitmapFactory.decodeResource(view.getResources(),R.drawable.bullet_left),
                            BitmapFactory.decodeResource(view.getResources(),R.drawable.bullet_right),view, playerId, playerB.getDir());
                    bullet.setXY(playerB.x+ playerB.getDir()*playerB.getWidth()/2, playerB.y+50 );
                    bulletListB.add(bullet);
                }
                else {
                    throw new JSONException("playerId Error");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void sendMove(int playerId, int direction) {
        socket.emit("MOVE", playerId, direction);
    }

    public void sendStop(int playerId) {
        socket.emit("STOP", playerId);
    }

    public void sendJump(int playerId) {
        socket.emit("JUMP", playerId);
    }

    public void sendFire(int playerId) {
        socket.emit("FIRE", playerId);
    }
}
package com.example.q.myapplication;

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
    private ArrayList<BulletObject> bulletList;

    public SocketHelper(Tab3View tab3View) {
        this.view = tab3View;
        this.socket = view.getSocket();
        this.playerA = view.getPlayerA();
        this.playerB = view.getPlayerB();
        this.bulletList = view.getBulletList();
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

                BulletObject bullet = new BulletObject(/*BITMAP IMAGE*/, view);

                if (playerId == 0) {
                    bullet.setDir(playerA.getDir());
                    bullet.setXY(playerA.x, playerA.y + playerA.getDir()*playerA.getHeight()/2);
                }
                else if (playerId == 1) {
                    bullet.setDir(playerB.getDir());
                    bullet.setXY(playerB.x, playerB.y + playerB.getDir()*playerB.getHeight()/2);
                }
                else {
                    throw new JSONException("playerId Error");
                }

                bulletList.add(bullet);

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
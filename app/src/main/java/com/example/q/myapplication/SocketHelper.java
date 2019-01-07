package com.example.q.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class SocketHelper {
    private GameObject playerA;
    private GameObject playerB;

    public SocketHelper(GameObject playerA, GameObject playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
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
                    playerA.stop();
                }
                else if (playerId == 1) {
                    playerB.stop();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
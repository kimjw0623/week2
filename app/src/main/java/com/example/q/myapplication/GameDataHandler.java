package com.example.q.myapplication;

import io.socket.client.Socket;

public class GameDataHandler {
    private static Socket socket;
    private static String gameNum;
    private static int playerId;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        GameDataHandler.socket = socket;
    }

    public static synchronized String getGameNum() {
        return gameNum;
    }

    public static synchronized void setGameNum(String gameNum) {
        GameDataHandler.gameNum = gameNum;
    }

    public static synchronized int getPlayerId() {
        return playerId;
    }

    public static synchronized void setPlayerId(int playerId) {
        GameDataHandler.playerId = playerId;
    }
}

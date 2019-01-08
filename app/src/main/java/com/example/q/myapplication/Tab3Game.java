package com.example.q.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Tab3Game extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FrameLayout mine;
    private int playerId;
    private String gameNum;
    private Socket socket;

    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public int getPlayerId() { return playerId; }
    public Socket getSocket() {return socket;}
    public void setGameNum(String gameNum) {this.gameNum = gameNum;}
    public String getGameNum() {return gameNum;}

    public void startGame() {
        Intent data = new Intent(getActivity(),Game.class);
        GameDataHandler.setGameNum(getGameNum());
        GameDataHandler.setPlayerId(getPlayerId());
        GameDataHandler.setSocket(getSocket());
        startActivity(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab3game, container, false);

        try {
            Button newGame = rootView.findViewById(R.id.newGame);
            Button enterGame = rootView.findViewById(R.id.enterGame);
            Button refreshServer = rootView.findViewById(R.id.refreshServer);

            final Emitter.Listener onGameNum = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        final String game_num = (String) args[0];
                        setGameNum(game_num);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Button newGame = rootView.findViewById(R.id.newGame);
                                newGame.setVisibility(View.GONE);
                                TextView view = rootView.findViewById(R.id.randNum);
                                view.setText("New game number:\n\n"+game_num);
                                view.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };

            final Emitter.Listener onGameStart = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        boolean success = (boolean) args[0];
                        if (success) {
                            startGame();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            final String serverUrl = "http://socrip4.kaist.ac.kr:580/";
            try {
                Socket mSocket = GameDataHandler.getSocket();
                mSocket.disconnect();
                mSocket.off("GAME_NUM", onGameNum);
                mSocket.off("GAME_START", onGameStart);
                mSocket.close();
            } catch(Exception e) {}
            socket = IO.socket(serverUrl);
            socket.on("GAME_NUM", onGameNum);
            socket.on("GAME_START", onGameStart);
            socket.connect();
            GameDataHandler.setSocket(socket);
            Toast.makeText(getContext(), "Server connected", Toast.LENGTH_SHORT).show();

            newGame.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    socket.emit("NEW_GAME");
                    setPlayerId(0);
                }
            });

            enterGame.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    TextView textView = rootView.findViewById(R.id.gameNum);
                    String gameNum = textView.getText().toString();
                    socket.emit("ENTER_GAME", gameNum);
                    setGameNum(gameNum);
                    setPlayerId(1);
                }
            });

            refreshServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        try {
                            Socket msocket = GameDataHandler.getSocket();
                            msocket.disconnect();
                            msocket.close();
                        } catch (Exception e) {
                        }
                        socket = IO.socket(serverUrl);
                        socket.on("GAME_NUM", onGameNum);
                        socket.on("GAME_START", onGameStart);
                        socket.connect();
                        GameDataHandler.setSocket(socket);
                        Toast.makeText(getContext(), "Server connected", Toast.LENGTH_SHORT).show();
                        TextView view = rootView.findViewById(R.id.randNum);
                        view.setVisibility(View.GONE);
                        Button newGame = rootView.findViewById(R.id.newGame);
                        newGame.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Server failure", Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }

}
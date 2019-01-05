package com.example.q.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;


public class MineSweeper extends Activity {

    static TextView bomb;
    static TextView timer;
    static TextView win;
    static TextView lose;
    static LottieAnimationView animationView;
    static LottieAnimationView loseanimation;
    static CountDownTimer count;
    static long remain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        setContentView(R.layout.activity_mine_sweeper);
        bomb = findViewById(R.id.bomb);
        timer = findViewById(R.id.timer);
        win = findViewById(R.id.win);
        lose = findViewById(R.id.lose);
        animationView = findViewById(R.id.animation_view);
        loseanimation = findViewById(R.id.lose_animation);
        win.bringToFront() ;
        lose.bringToFront();
        animationView.bringToFront();
        loseanimation.bringToFront();
        GameEngine.getInstance().createGrid(MineSweeper.this);
        bomb.setText(GameEngine.BOMB_NUMBER + "");
        animationView.setAnimation("confetti_blast.json");
        loseanimation.setAnimation("uh_oh.json");
        if(count!=null){
            count.cancel();
        }
        count = new CountDownTimer(100000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                remain = millisUntilFinished/1000;
                timer.setText(remain+"");
            }
            @Override
            public void onFinish() {
                count.cancel();
                timer.setText(0);
                GameEngine.getInstance().onGameLost();
            }

        }.start();
    }

    public TextView getBomb(){
        return bomb;
    }

    public void onResetClicked(View v){
        count.cancel();
        count.start();
        GameEngine.getInstance().createGrid(MineSweeper.this);
        bomb.setText(GameEngine.BOMB_NUMBER+"");
        MineSweeper.win.setVisibility(View.INVISIBLE);
        MineSweeper.lose.setVisibility(View.INVISIBLE);
        MineSweeper.animationView.setVisibility(View.INVISIBLE);
        MineSweeper.loseanimation.setVisibility(View.INVISIBLE);
    }

}

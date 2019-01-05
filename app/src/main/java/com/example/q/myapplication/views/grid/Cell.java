package com.example.q.myapplication.views.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.q.myapplication.GameEngine;
import com.example.q.myapplication.R;

public class Cell extends BaseCell implements View.OnClickListener, View.OnLongClickListener {
    public Cell (Context context, int position){
        super(context);
        Log.e("cell","여기니..");
        setPosition(position);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        GameEngine.getInstance().click( getXpos(), getYpos() );
        if (GameEngine.getInstance().getCellAt(getXpos(),getYpos()).isBomb()){
            GameEngine.getInstance().onGameLost();
        }else{
            GameEngine.getInstance().checkEnd();}
    }

    @Override
    public boolean onLongClick(View v) {
        if( !isClicked()){
            GameEngine.getInstance().flag(getXpos(), getYpos());
            GameEngine.getInstance().checkEnd();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButton(canvas);

        if( isFlagged() ){
            drawFlag(canvas);
            Log.e("Minesweeper","f");
        }else if (isRevealed() && isBomb()  && !isClicked()){
            drawNormalBomb(canvas);
            Log.e("Minesweeper","c");
        }else{
            if( isClicked()){
                if(getValue() == -1){
                    drawBombExploded(canvas);
                    Log.e("Minesweeper","bbb");
                }
                else{
                    drawNumber (canvas);
                    Log.e("Minesweeper","num");
                }
            }else{
                drawButton(canvas);
                Log.e("Minesweeper","normal");
            }
        }
    }

    private void drawNormalBomb (Canvas canvas){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb);
        drawable.setBounds(0,0,getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawBombExploded (Canvas canvas){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bomb_exploded);
        drawable.setBounds(0,0,getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawFlag (Canvas canvas){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.flagged);
        drawable.setBounds(0,0,getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void drawButton (Canvas canvas){
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.normal);
        drawable.setBounds(0,0,getWidth(), getHeight());
        drawable.draw(canvas);
    }


    private void drawNumber( Canvas canvas){
        Drawable drawable = null;

        switch (getValue()){
            case 0:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_0);
                break;
            case 1:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_1);
                break;
            case 2:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_2);
                break;
            case 3:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_3);
                break;
            case 4:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_4);
                break;
            case 5:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_5);
                break;
            case 6:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_6);
                break;
            case 7:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_7);
                break;
            case 8:
                drawable=ContextCompat.getDrawable(getContext(), R.drawable.number_8);
                break;
        }

        drawable.setBounds(0,0,getWidth(),getHeight());
        drawable.draw(canvas);
    }

}

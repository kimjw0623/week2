package com.example.q.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;

public class Full_Image extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        ImageView imageView = findViewById(R.id.full_image_view);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mAttacher.setMinimumScale(0);
        Intent i = getIntent();
        // Selected image id
        String id=i.getExtras().getString("position");
        //ArrayList<String> images=i.getExtras().getStringArrayList("array");

        Glide.with(this).load(id).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground).fitCenter())
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
}


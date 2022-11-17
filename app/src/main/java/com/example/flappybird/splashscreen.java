package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity {
    private Handler handler;
    private Animation topanim,bottomanim;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        topanim= AnimationUtils.loadAnimation(this,R.anim.top);
        bottomanim= AnimationUtils.loadAnimation(this,R.anim.bot);

        imageView=findViewById(R.id.imageView7);
        textView=findViewById(R.id.textViewas);

        imageView.setAnimation(topanim);
        textView.setAnimation(bottomanim);


        handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashscreen.this,giris.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable,4000);
    }
}
package com.example.flappybird;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score,txt_best_score,txt_score_over,txt_start;
    public static RelativeLayout rl_game_over,rl_menu;
    public static Button btn_start,btnperson,btnleader,btninfo,btnexit;
    ToggleButton toggleButton;
    static ImageView imageView,volumectrl2,volumectrl;
    public GameView gv;
    Boolean ismute=true;
    static MediaPlayer mediaPlayer;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        constants.SCREEN_WIDTH = dm.widthPixels;
        constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main);
        txt_score=findViewById(R.id.txt_score);
        txt_best_score=findViewById(R.id.txt_best_score);
        txt_score_over=findViewById(R.id.txt_score_over);
        rl_game_over=findViewById(R.id.rl_game_over);
        rl_menu=findViewById(R.id.rl_menu);
        txt_start=findViewById(R.id.txt_start);
        gv=findViewById(R.id.gv);
        btnperson = findViewById(R.id.btnperson);
        btnleader = findViewById(R.id.btnleader);
        btninfo = findViewById(R.id.btninfo);
        btnexit = findViewById(R.id.btnexit);
        toggleButton = findViewById(R.id.toggleButton);
        imageView = findViewById(R.id.imageView);
        volumectrl = findViewById(R.id.imgsound);
        volumectrl2 = findViewById(R.id.imgmusic);
        dialog = new Dialog(this);

        mediaPlayer=MediaPlayer.create(this,R.raw.sillychipsong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog2();
            }
        });

        if (ismute){
            volumectrl2.setImageResource(R.drawable.ic_baseline_music_note_24);
        }
        else {
            volumectrl2.setImageResource(R.drawable.ic_baseline_music_off_24);
        }

        volumectrl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ismute){
                    volumectrl2.setImageResource(R.drawable.ic_baseline_music_note_24);
                    ismute=false;
                    mediaPlayer.start();
                }
                else {
                    ismute=true;
                    mediaPlayer.pause();
                    volumectrl2.setImageResource(R.drawable.ic_baseline_music_off_24);
                }
            }
        });

        if (GameView.loadedsound){
            volumectrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        else {
            volumectrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }

        volumectrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GameView.loadedsound){
                    volumectrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    GameView.loadedsound=false;
                }
                else {
                    GameView.loadedsound=true;
                    volumectrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
                }
            }
        });



        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    imageView.setVisibility(View.VISIBLE);
                    btnperson.setVisibility(View.VISIBLE);
                    btnleader.setVisibility(View.VISIBLE);
                    btninfo.setVisibility(View.VISIBLE);
                    btnexit.setVisibility(View.VISIBLE);
                    imageView.setAlpha(0f);
                    imageView.setTranslationY(-20);
                    imageView.animate().alpha(1f).translationYBy(20).setDuration(750);
                    btnperson.setAlpha(0f);
                    btnperson.setTranslationY(-20);
                    btnperson.animate().alpha(1f).translationYBy(20).setDuration(750);
                    btnleader.setAlpha(0f);
                    btnleader.setTranslationY(-20);
                    btnleader.animate().alpha(1f).translationYBy(20).setDuration(750);
                    btninfo.setAlpha(0f);
                    btninfo.setTranslationY(-20);
                    btninfo.animate().alpha(1f).translationYBy(20).setDuration(750);
                    btnexit.setAlpha(0f);
                    btnexit.setTranslationY(-20);
                    btnexit.animate().alpha(1f).translationYBy(20).setDuration(750);
                }
                else {
                    imageView.setVisibility(View.INVISIBLE);
                    btnperson.setVisibility(View.INVISIBLE);
                    btnleader.setVisibility(View.INVISIBLE);
                    btninfo.setVisibility(View.INVISIBLE);
                    btnexit.setVisibility(View.INVISIBLE);
                }
            }
        });

        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gv.setStart(true);
                txt_score.setVisibility(View.VISIBLE);
                rl_menu.setVisibility(View.INVISIBLE);
            }
        });
        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_menu.setVisibility(View.VISIBLE);
                rl_game_over.setVisibility(View.INVISIBLE);
                gv.setStart(false);
                gv.reset();
            }
        });
    }
    @Override
    public void onBackPressed() {
        volumectrl2 = findViewById(R.id.imgmusic);
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        ismute=true;
        mediaPlayer.pause();
        volumectrl2.setImageResource(R.drawable.ic_baseline_music_off_24);
    }



    public void hesap(View view) {
        startActivity(new Intent(this, hesap.class));
        MainActivity.mediaPlayer.stop();
        this.finish();
    }
    public void leader(View view) {
        startActivity(new Intent(this, leader.class));
        MainActivity.mediaPlayer.stop();
        this.finish();
    }
    public void hakkimizda(View view) {
        startActivity(new Intent(this, hakkimizda.class));
        MainActivity.mediaPlayer.stop();
        this.finish();
    }
    public void cikis(View view) {

        MainActivity.mediaPlayer.stop();
        this.finish();
    }
    private void openWinDialog2() {
        dialog.setContentView(R.layout.exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imgclose = dialog.findViewById(R.id.imgclose);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnClose = dialog.findViewById(R.id.btnclose);
        Button btnevet = dialog.findViewById(R.id.btnevet);
        btnevet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cikis(view);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
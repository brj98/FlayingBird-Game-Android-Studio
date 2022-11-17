package com.example.flappybird;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GameView extends View {
    private Bird bird;
    private android.os.Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrayPipe;
    private int sumpipe,distanse;
    private int score,bestscore=0;
    private boolean start;
    private Context context;
    private int soundJump;
    private float valume;
    static boolean loadedsound;
    private SoundPool soundPool;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://leaderbord-1c278-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReferenc;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance("https://leaderbord-1c278-default-rtdb.firebaseio.com/").getReference();
        databaseReference.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){

                    String x;
                    x=snapshot.child("score").getValue().toString();
                    bestscore=Integer.parseInt(x);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        score = 0;
        start = false;
        initBird();
        initPipe();
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool.Builder builder=new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool=builder.build();
        }else {
            soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                loadedsound=true;
            }
        });
        soundJump = this.soundPool.load(context,R.raw.jump_02,1);
    }

    private void initPipe() {
        sumpipe=4                                                          ;
        distanse=350*constants.SCREEN_HEIGHT/1920;
        arrayPipe=new ArrayList<>();
        for(int i =0;i<sumpipe;i++){
            if(i<sumpipe/2){
                this.arrayPipe.add(new Pipe(constants.SCREEN_WIDTH+i*((constants.SCREEN_WIDTH+200*constants.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0,200*constants.SCREEN_WIDTH/1080,constants.SCREEN_HEIGHT/2));
                this.arrayPipe.get(this.arrayPipe.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrayPipe.get(this.arrayPipe.size()-1).randomY();

            }else{
                this.arrayPipe.add(new Pipe(this.arrayPipe.get(i-sumpipe/2).getX(),this.arrayPipe.get(i-sumpipe/2).getY()
                        +this.arrayPipe.get(i-sumpipe/2).getHeight()+this.distanse,200*constants.SCREEN_WIDTH/1080,constants.SCREEN_HEIGHT/2));
                this.arrayPipe.get(this.arrayPipe.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe1));
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setWidth(100*constants.SCREEN_WIDTH/1080);
        bird.setHeight(100*constants.SCREEN_HEIGHT/1920);
        bird.setX(100*constants.SCREEN_WIDTH/1080);
        bird.setY(constants.SCREEN_HEIGHT/2- bird.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird2));
        bird.setArrBms(arrBms);
    }
    private void initBird1() {
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird3));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird3));
        bird.setArrBms(arrBms);
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start){
            bird.draw(canvas);
            for(int j = 0; j < sumpipe;j++){
                if(bird.getRect().intersect(arrayPipe.get(j).getRect())||bird.getY()-bird.getHeight()<0||bird.getY()>constants.SCREEN_HEIGHT){
                    initBird1();
                    bird.getDrop();
                    Pipe.speed=0;
                    MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                    MainActivity.txt_best_score.setText("En İyi Score : " + bestscore);
                    MainActivity.txt_score.setVisibility(INVISIBLE);
                }
                if(this.bird.getX()+this.bird.getWidth()>arrayPipe.get(j).getX()+arrayPipe.get(j).getWidth()/2
                        &&this.bird.getX()+this.bird.getWidth()<=arrayPipe.get(j).getX()+arrayPipe.get(j).getWidth()/2+Pipe.speed
                        &&j<sumpipe/2){
                    score++;
                    if(score>bestscore){
                        bestscore = score;
                        String uid = namescore.emad;
                        HashMap<String, Object> hashMap=new HashMap<String, Object>();
                        hashMap.put("score",bestscore);
                        databaseReference.child("users").child(uid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                if(this.arrayPipe.get(j).getX() < -arrayPipe.get(j).getWidth()){
                    this.arrayPipe.get(j).setX(constants.SCREEN_WIDTH);
                    if(j<sumpipe/2){
                        arrayPipe.get(j).randomY();
                    }else{
                        arrayPipe.get(j).setY(this.arrayPipe.get(j-sumpipe/2).getY()
                                +this.arrayPipe.get(j-sumpipe/2).getHeight()+this.distanse);
                    }

                }
                this.arrayPipe.get(j).draw(canvas);
            }
        }else {
            if(bird.getY()>constants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*constants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }

        handler.postDelayed(r,10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundJump,(float)0.5,(float)0.5,1,0,1f );
            }
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    public void reset(){
        MainActivity.txt_score.setText("0");
        score=0;
        initPipe();
        initBird();
    }
}
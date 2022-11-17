package com.example.flappybird;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class leader extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<scoreData> list;
    MyAdapter adapter;
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference reference=db.getReference().child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        recyclerView=findViewById(R.id.leaderbordrc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        list=new ArrayList<>();
        adapter = new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);

        reference.orderByChild("score").limitToLast(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String a = dataSnapshot.child("score").getValue().toString();
                    String b = dataSnapshot.child("kullanciad").getValue().toString();
                    scoreData user = new scoreData();
                    user.kulcad=b;
                    user.score=Integer.parseInt(a);


                    list.add(user);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void anasayfa3(View view) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
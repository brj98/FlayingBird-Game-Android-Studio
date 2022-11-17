package com.example.flappybird;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class hesap extends AppCompatActivity {
    EditText getmail, getadsoyad, getscore;
    TextView txtedit, textViewsil;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        constants.SCREEN_WIDTH = dm.widthPixels;
        constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_hesap);
        getmail = findViewById(R.id.txtkad4);
        getadsoyad = findViewById(R.id.txtadsoyad);
        txtedit = findViewById(R.id.txtkaydet);
        textViewsil = findViewById(R.id.textViewsil);
        dialog = new Dialog(this);

        getmail.setShowSoftInputOnFocus(false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ck();

        textViewsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWinDialog();
            }
        });
    }

    private void ck() {
        databaseReference = FirebaseDatabase.getInstance("https://leaderbord-1c278-default-rtdb.firebaseio.com/").getReference();
        databaseReference.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    getmail.setText(snapshot.child("email").getValue().toString());
                    getadsoyad.setText(snapshot.child("kullanciad").getValue().toString());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void kaydetme(View view) {
        String uid = namescore.emad;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (getadsoyad.getText().toString().isEmpty()) {
            Toast.makeText(this, "Lütfen Kullanıcı Adı Giriniz", Toast.LENGTH_SHORT).show();
        } else {
            hashMap.put("kullanciad", getadsoyad.getText().toString() + "");
            databaseReference.child("users").child(uid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                }
            });
            Toast.makeText(this, "Bilgileriniz Güncellenmiştir", Toast.LENGTH_SHORT).show();
        }
    }

    public void anasayfa(View view) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    public void girisekran(View view) {
        startActivity(new Intent(this, giris.class));
        this.finish();
    }
    private void openWinDialog() {
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
                girisekran(view);
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
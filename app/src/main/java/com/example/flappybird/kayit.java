package com.example.flappybird;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class kayit extends AppCompatActivity {
    EditText txtkulad,txtmail,txtsifre,txtstifretekrar;
    TextView btnkayit,tvgr;
    String emailKontrol= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://leaderbord-1c278-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        constants.SCREEN_WIDTH = dm.widthPixels;
        constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_kayit);

        txtmail=findViewById(R.id.txtkad2);
        txtkulad=findViewById(R.id.txtadsoyad);
        txtsifre=findViewById(R.id.txtparola2);
        txtstifretekrar=findViewById(R.id.txtparolat);
        btnkayit=findViewById(R.id.textView5);
        tvgr=findViewById(R.id.textView8);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();


        progressDialog=new ProgressDialog(this);

        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kulAuth();
            }
        });

        tvgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(kayit.this,giris.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private void kulAuth() {
        String mail=txtmail.getText().toString();
        String kulad=txtkulad.getText().toString();
        String sifre=txtsifre.getText().toString();
        String sifretekrar=txtstifretekrar.getText().toString();
        if(!mail.matches(emailKontrol)){
            txtmail.setError("Doğru E_mail Giriniz");
        }else if(sifre.isEmpty()||sifre.length()<8){
            txtsifre.setError("Şifre 8 Haneden Küçük Olamaz");
        }
        else if(kulad.isEmpty()){
            txtkulad.setError("Kullanıcı Adı Giriniz");
        }
        else if (!sifre.equals(sifretekrar)){
            txtstifretekrar.setError("Şifre Uyuşmuyor");
        }
        else{
            progressDialog.setMessage("Lütfen Bekleyiniz ...");
            progressDialog.setTitle("Kayıt");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        String uid=mAuth.getCurrentUser().getUid();
                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(uid)){
                                    Toast.makeText(kayit.this,"ayni mailde hesap var ",Toast.LENGTH_SHORT).show();
                                }else {

                                    databaseReference.child("users").child(uid).child("kullanciad").setValue(kulad);
                                    databaseReference.child("users").child(uid).child("score").setValue(0);
                                    databaseReference.child("users").child(uid).child("email").setValue(mail);


                                    Intent intent = new Intent(kayit.this,giris.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(kayit.this,"İslem Basarılı",Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(kayit.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
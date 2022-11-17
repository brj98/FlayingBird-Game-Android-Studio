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

public class giris extends AppCompatActivity {

    EditText txtmail1,txtsifre1;
    TextView btngiris,tvko;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://leaderbord-1c278-default-rtdb.firebaseio.com/");
    String emailKontrol= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        constants.SCREEN_WIDTH = dm.widthPixels;
        constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_giris);
        txtmail1=findViewById(R.id.txtkad3);
        txtsifre1=findViewById(R.id.txtparola3);
        btngiris=findViewById(R.id.txtgiris);
        tvko=findViewById(R.id.txtkayit);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);

        btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preforLogin();
            }



        });
        tvko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(giris.this,kayit.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void preforLogin() {
        String mail=txtmail1.getText().toString();
        String sifre=txtsifre1.getText().toString();
        if(!mail.matches(emailKontrol)){
            txtmail1.setError("Doğru E_mail Giriniz");
        }else if(sifre.isEmpty()||sifre.length()<8){
            txtsifre1.setError("Şifre 8 Haneden Küçük Olamaz");
        }

        else {
            progressDialog.setMessage("Lütfen Bekleyiniz ...");
            progressDialog.setTitle("Giriş");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String uid=mAuth.getCurrentUser().getUid();
                        namescore.emad=uid;
                        progressDialog.dismiss();
                        Toast.makeText(giris.this, "Giriş Başarlı", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(giris.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(giris.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
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
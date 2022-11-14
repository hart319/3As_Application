package com.example.a3a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText ntxtEmail,ntextPwd;
    Button mLogin;
    TextView mRegister;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ntxtEmail=findViewById(R.id.email);
        ntextPwd= findViewById(R.id.password);
        fAuth=FirebaseAuth.getInstance();
        mLogin=findViewById(R.id.btnLogin);
        mRegister=findViewById(R.id.lnkRegister);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLoginActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(openLoginActivity);
            }
        });



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ntxtEmail.getText().toString().trim();
                String password = ntextPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    ntxtEmail.setError("Email is required ");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ntxtEmail.setError("password is required ");
                    return;


                }
                if (password.length() < 6) {
                    ntextPwd.setError("there must be atleast 6 characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(LoginActivity.this,"Logged in Succcesfully", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(getApplicationContext(),MainActivity.class));
                          finish();
                      }else {
                          Toast.makeText(LoginActivity.this,"Error !", Toast.LENGTH_SHORT).show();

                      }


                    }
                });

            }
        });

    }
}

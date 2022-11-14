package com.example.a3a;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3a.models.SubjectCollection;
import com.example.a3a.models.User;
import com.example.a3a.models.UserSubjects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText ntxtName, ntxtEmail, ntextPwd;
    Button nbtnRegister;
    TextView nlnkLogin;
    RadioGroup nyear;
    CheckBox nphy, nchem, ncm, nbio;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ntxtName = findViewById(R.id.txtName);
        ntxtEmail = findViewById(R.id.txtEmail);
        ntextPwd = findViewById(R.id.txtPwd);
        nbtnRegister = findViewById(R.id.btnRegister);
        nlnkLogin = findViewById(R.id.lnkLogin);
        nyear = findViewById(R.id.year);
        nphy = findViewById(R.id.phy);
        nchem = findViewById(R.id.chem);
        ncm = findViewById(R.id.cm);
        nbio = findViewById(R.id.bio);
        fAuth = FirebaseAuth.getInstance();


        nlnkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(openLoginActivity);
            }
        });


        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

            finish();
        }


        nbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ntxtName.getText().toString().trim();
                String email = ntxtEmail.getText().toString().trim();
                String password = ntextPwd.getText().toString().trim();

                int year = 2021;
                switch (nyear.getCheckedRadioButtonId()) {
                    case R.id.year1:
                        year = 2021;
                        break;
                    case R.id.year2:
                        year = 2022;
                        break;
                    case R.id.year3:
                        year = 2023;
                        break;
                }
                boolean isPhy = nphy.isChecked();
                boolean isChem = nchem.isChecked();
                boolean isCM = ncm.isChecked();
                boolean isBio = nbio.isChecked();

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

                User user = new User(
                        "",
                        name,
                        email,
                        year
                );

                UserSubjects userSubjects = new UserSubjects(
                        "",
                        name,
                        isPhy,
                        isChem,
                        isCM,
                        isBio
                );

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("Subject").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SubjectCollection subjects = task.getResult().getValue(SubjectCollection.class);

                        if (userSubjects.phy.isDoing) {
                            userSubjects.phy.topics = subjects.Physics.to_topics();
                        }

                        if (userSubjects.cm.isDoing) {
                            userSubjects.cm.topics = subjects.Maths.to_topics();
                        }

                        if (userSubjects.chem.isDoing) {
                            userSubjects.chem.topics = subjects.Chemistry.to_topics();
                        }

                        if (userSubjects.bio.isDoing) {
                            userSubjects.bio.topics = subjects.Biology.to_topics();
                        }

                        Log.d("Register", user.toString());
                        registerUser(email, password, user, userSubjects);
                    } else {
                        Log.e("Register", "Error" + task.getException().toString());
                    }
                });

            }
        });
    }

    void registerUser(String email, String password, User user, UserSubjects userSubjects) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser authUser = fAuth.getCurrentUser();
                user.uid = authUser.getUid();
                userSubjects.uid = authUser.getUid();

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Tasks.whenAll(
                        db.child("User").child(user.uid).setValue(user),
                        db.child("UserSubject").child(user.uid).setValue(userSubjects)
                ).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Log.d("Register", "Error  !" + task1.getException().getMessage());
                        Toast.makeText(RegisterActivity.this, "Error  !" + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Error  !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


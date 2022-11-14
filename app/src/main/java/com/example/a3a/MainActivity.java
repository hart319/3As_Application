package com.example.a3a;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.example.a3a.models.User;
import com.example.a3a.models.UserSubjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button ngrupbtn, lgrupbtn, ppbtn, logout;
    CardView subCM, subChem, subPhy, subBio;
    TextView perCM, perChem, perPhy, perBio, cd_days, cd_hours, cd_mins, cd_sec;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ValueEventListener dbRef;
    UserSubjects userMainSubects;

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUserData();
        getUserSubjectData();
        initUI();

        subCM = findViewById(R.id.subCM);
        subChem = findViewById(R.id.subChem);
        subPhy = findViewById(R.id.subPhy);
        subBio = findViewById(R.id.subBio);

        perCM = findViewById(R.id.subCMPer);
        perChem = findViewById(R.id.subChemPer);
        perPhy = findViewById(R.id.subPhyPer);
        perBio = findViewById(R.id.subBioPer);

        ngrupbtn = findViewById(R.id.newgroup);
        lgrupbtn = findViewById(R.id.group);
        logout = findViewById(R.id.logout);
        ppbtn=findViewById(R.id.pastp);


        ppbtn.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), PasspActivity.class);
            if (userMainSubects.bio.isDoing) {
                openLoginActivity.putExtra("bio", "YES");
            } else {
                openLoginActivity.putExtra("bio", "NO");
            }
            if (userMainSubects.chem.isDoing) {
                openLoginActivity.putExtra("chem", "YES");
            } else {
                openLoginActivity.putExtra("chem", "NO");
            }
            if (userMainSubects.cm.isDoing) {
                openLoginActivity.putExtra("cm", "YES");
            } else {
                openLoginActivity.putExtra("cm", "NO");
            }
            if (userMainSubects.phy.isDoing) {
                openLoginActivity.putExtra("phy", "YES");
            } else {
                openLoginActivity.putExtra("phy", "NO");
            }
            startActivity(openLoginActivity);
        });


        ngrupbtn.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(openLoginActivity);
        });

        lgrupbtn.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(openLoginActivity);
        });

        subCM.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), TopicActivity.class);
            openLoginActivity.putExtra("topic", "cm");
            openLoginActivity.putExtra("name", "Combined Maths");
            startActivity(openLoginActivity);
        });

        subChem.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), TopicActivity.class);
            openLoginActivity.putExtra("topic", "chem");
            openLoginActivity.putExtra("name", "Chemistry");
            startActivity(openLoginActivity);
        });

        subPhy.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), TopicActivity.class);
            openLoginActivity.putExtra("topic", "phy");
            openLoginActivity.putExtra("name", "Physics");
            startActivity(openLoginActivity);
        });

        subBio.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), TopicActivity.class);
            openLoginActivity.putExtra("topic", "bio");
            openLoginActivity.putExtra("name", "Biology");
            startActivity(openLoginActivity);
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        if (user != null && dbRef != null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            dbRef = db.child("User").child(user.uid).addValueEventListener(dbRef);
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.removeCallbacks(runnable);
        countDownStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        cd_days = findViewById(R.id.tv_days);
        cd_hours = findViewById(R.id.tv_hour);
        cd_mins = findViewById(R.id.tv_mins);
        cd_sec = findViewById(R.id.tv_sec);
    }

    private void getUserData() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("User").child(authUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = task.getResult().getValue(User.class);
                countDownStart();
            } else  {
                Log.d("MainActivity", "Error: " + task.getException().toString());
            }
        });
    }

    private void getUserSubjectData() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserSubjects userSubjects = dataSnapshot.getValue(UserSubjects.class);

                userMainSubects = userSubjects;
                updateSubjects(userSubjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        dbRef = db.child("UserSubject").child(authUser.getUid()).addValueEventListener(postListener);
    }

    private void countDownStart() {
        if (user == null) {
            return;
        }

        String EXAM_DATE_TIME = user.year + "-09-01 00:00:00";

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EXAM_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        cd_days.setText(String.format("%02d", Days));
                        cd_hours.setText(String.format("%02d", Hours));
                        cd_mins.setText(String.format("%02d", Minutes));
                        cd_sec.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    void updateSubjects(UserSubjects userSubjects) {
        if (userSubjects.cm.isDoing) {
            subCM.setVisibility(View.VISIBLE);
            perCM.setText(String.format("%.0f",userSubjects.cm.calcPercentage()) + "%");
        }
        if (userSubjects.chem.isDoing) {
            subChem.setVisibility(View.VISIBLE);
            perChem.setText(String.format("%.0f", userSubjects.chem.calcPercentage()) + "%");
        }
        if (userSubjects.phy.isDoing) {
            subPhy.setVisibility(View.VISIBLE);
            perPhy.setText(String.format("%.0f", userSubjects.phy.calcPercentage()) + "%");
        }
        if (userSubjects.bio.isDoing) {
            subBio.setVisibility(View.VISIBLE);
            perBio.setText(String.format("%.0f", userSubjects.bio.calcPercentage()) + "%");
        }
    }


}

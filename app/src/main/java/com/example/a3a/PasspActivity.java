package com.example.a3a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasspActivity extends AppCompatActivity {

    TextView ppsub1, ppsub2, ppsub3;
    CardView ppsub1btn, ppsub2btn, ppsub3btn;
    private String subTopicname;
    private RecyclerView passpaperistRV;
    private PasspAdapter adapter;
    private ChildEventListener passpaperListener;
    private List<String> isDoingSubectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passp);

        ppsub1 = findViewById(R.id.ppSub1);
        ppsub2 = findViewById(R.id.ppSub2);
        ppsub3 = findViewById(R.id.ppSub3);

        ppsub1btn = findViewById(R.id.ppSub1btn);
        ppsub2btn = findViewById(R.id.ppSub2btn);
        ppsub3btn = findViewById(R.id.ppSub3btn);

        String Biology = getIntent().getStringExtra("bio");
        String Chemistry = getIntent().getStringExtra("chem");
        String Combined_Maths = getIntent().getStringExtra("cm");
        String physics = getIntent().getStringExtra("phy");
//        subTopicname = getIntent().getStringExtra("name");

        if (Biology.equals("YES")) {
            isDoingSubectList.add("Biology");
        }
        if (Combined_Maths.equals("YES")) {
            isDoingSubectList.add("Combined Maths");
        }
        if (Chemistry.equals("YES")) {
            isDoingSubectList.add("Chemistry");
        }
        if (physics.equals("YES")) {
            isDoingSubectList.add("physics");
        }
        ppsub1.setText(isDoingSubectList.get(0));
        ppsub2.setText(isDoingSubectList.get(1));
        ppsub3.setText(isDoingSubectList.get(2));

        Intent openLoginActivity = new Intent(getApplicationContext(), KeyPP.class);

        ppsub1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity.putExtra("topicFrom", isDoingSubectList.get(0));
                startActivity(openLoginActivity);
            }
        });
        ppsub2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity.putExtra("topicFrom", isDoingSubectList.get(1));
                startActivity(openLoginActivity);
            }
        });
        ppsub3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity.putExtra("topicFrom", isDoingSubectList.get(2));
                startActivity(openLoginActivity);
            }
        });

    }
}

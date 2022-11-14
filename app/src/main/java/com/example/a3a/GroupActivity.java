package com.example.a3a;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.User;
import com.example.a3a.models.UserSubjects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.*;

public class GroupActivity extends AppCompatActivity {
    Button searchF;
    private User user;
    private TextView groupNameTxt;
    private RecyclerView groupUserListRV;
    private GroupUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupNameTxt = findViewById(R.id.group_name);

        groupUserListRV = findViewById(R.id.group_list);
        groupUserListRV.setLayoutManager(new LinearLayoutManager(this));
        //groupUserListRV.hasFixedSize();

        adapter = new GroupUserAdapter();
        groupUserListRV.setAdapter(adapter);

        searchF=findViewById(R.id.serchF);

        getUserData();

        searchF.setOnClickListener(v -> {
            Intent openLoginActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(openLoginActivity);
        });

    }




    private void getUserData() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("User").child(authUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = task.getResult().getValue(User.class);
                groupNameTxt.setText(user.name + "'s group");
                getGroupMembers(user);
            } else {
                Log.d("GroupActivity", "Error: " + task.getException().toString());
            }
        });
    }

    private void getGroupMembers(User user) {
        ArrayList<String> users = new ArrayList<>();

        users.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (user != null && user.group != null) {
            users.addAll(user.group.keySet());
        }

        ArrayList<Task> tasks = new ArrayList<>();
        for (String userId : users) {
            tasks.add(getUserSubjects(userId));
        }

        Tasks.whenAllSuccess(tasks.toArray(new Task[tasks.size()])).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (Object result : task.getResult()) {
                    UserSubjects userSubjects = ((DataSnapshot) result).getValue(UserSubjects.class);
                    adapter.addData(userSubjects);
                }
            } else {
                Log.d("GroupActivity", "Error: " + task.getException().toString());
            }
        });
        Log.d("GroupActivity", users.toString());
    }

    private Task<DataSnapshot> getUserSubjects(String userId) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        return db.child("UserSubject").child(userId).get();
    }


}

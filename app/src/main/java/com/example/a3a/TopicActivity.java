package com.example.a3a;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.KeyTopic;
import com.example.a3a.models.Topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {
    private String topic, topicName;
    private TextView topicNameTxt;
    private RecyclerView topicListRV;
    private TopicAdapter adapter;
    private ChildEventListener topicListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topic = getIntent().getStringExtra("topic");
        topicName = getIntent().getStringExtra("name");

        topicNameTxt = findViewById(R.id.topic_name);
        topicNameTxt.setText(topicName);

        topicListRV = findViewById(R.id.topic_list);
        topicListRV.setLayoutManager(new LinearLayoutManager(this));
        topicListRV.hasFixedSize();

        adapter = new TopicAdapter(topic);
        topicListRV.setAdapter(adapter);

        fetchTopics();
    }

    @Override
    protected void onDestroy() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("UserSubject").child(authUser.getUid()).child(topic).child("topics").removeEventListener(topicListener);

        super.onDestroy();
    }

    public void fetchTopics() {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = authUser.getUid();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference topicsRef = db.child("UserSubject").child(userId).child(topic).child("topics");

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KeyTopic topic = snapshot.getValue(KeyTopic.class);
                topic.key = snapshot.getKey();
                adapter.addItem(topic);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KeyTopic topic = snapshot.getValue(KeyTopic.class);
                topic.key = snapshot.getKey();
                adapter.updateItem(topic);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        this.topicListener = topicsRef.addChildEventListener(listener);
    }
}

package com.example.a3a;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyPP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pplistview);

        String topic = getIntent().getStringExtra("topicFrom");

        fetchPastPapers(topic);
    }

    public void fetchPastPapers(String topic) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference topicsRef = db.child("Passpapers").child(topic);
        List<String> subTopicsList = new ArrayList<>();
        List<String> subTopicsValuesList = new ArrayList<>();
        Map<String, String> subTopicWithValues = new HashMap<>();

        topicsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String valuesFromFirebase = dataSnapshot.getValue(String.class);
                        subTopicsList.add(dataSnapshot.getKey());
                        subTopicsValuesList.add(valuesFromFirebase);
                        subTopicWithValues.put(dataSnapshot.getKey(), valuesFromFirebase);
                    }
                    executeListView(subTopicsList, subTopicsValuesList);
                }
            }
        });
    }

    private void executeListView(List<String> subTopicsList, List<String> subTopicsValuesList) {
        PasspAdapter adapter = new PasspAdapter(this, subTopicsList, subTopicsValuesList);
        ListView listView = (ListView) findViewById(R.id.pp_list);
        listView.setAdapter(adapter);
    }
}
package com.example.a3a;

import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<User> users;
    private SearchView searchTxt;
    private RecyclerView searchListRV;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTxt = findViewById(R.id.search);
        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });

        searchListRV = findViewById(R.id.search_list);
        searchListRV.setLayoutManager(new LinearLayoutManager(this));
        searchListRV.hasFixedSize();

        adapter = new SearchAdapter();
        searchListRV.setAdapter(adapter);

        fetchUsers();
    }

    public void fetchUsers() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("User").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String, User>>() {};
                HashMap<String, User> users = task.getResult().getValue(t);
                this.users = new ArrayList<>(users.values());

                String query = searchTxt.getQuery().toString();
                filterUsers(query);
                Log.d("SearchActivity", this.users.toString());
            } else {
                Log.e("SearchActivity", "Error:" + task.getException().toString());
            }
        });
    }

    public void filterUsers(String query) {
        Log.d("SearchActivity", query + " | " + users.toString());
        if (users == null) {
            return;
        }

        if (query.isEmpty()) {
            adapter.setData(new ArrayList<>());
        } else {
            ArrayList<User> filtered = new ArrayList<>();

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            for (User user : users) {
                if (!user.uid.equals(currentUserId) && user.name.contains(query)) {
                    filtered.add(user);
                }
            }

            adapter.setData(filtered);
        }
    }
}

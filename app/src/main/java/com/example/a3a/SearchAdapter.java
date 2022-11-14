package com.example.a3a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.KeyTopic;
import com.example.a3a.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<User> mDataset = new ArrayList();
    private Context context;

    public SearchAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View searchView = inflater.inflate(R.layout.search, parent, false);
        return new SearchViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SearchViewHolder) holder).bindData(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<User> newDataset) {
        mDataset.clear();
        mDataset.addAll(newDataset);
        notifyDataSetChanged();
    }
}

class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView userName;
    private Button userAdd;
    private User userData;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.search_name);
        userAdd = itemView.findViewById(R.id.search_add);

        userAdd.setOnClickListener(this);
    }

    public void bindData(User user) {
        this.userData = user;
        userName.setText(user.name);
    }

    @Override
    public void onClick(View v) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = authUser.getUid();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Tasks.whenAll(
                db.child("User").child(userId).child("group").child(userData.uid).setValue(true),
                db.child("User").child(userData.uid).child("joinedGroups").child(userId).setValue(true)
        ).addOnCompleteListener(task -> {
            Toast.makeText(this.itemView.getContext(), "User Added to the Group", Toast.LENGTH_SHORT).show();
        });
    }
}

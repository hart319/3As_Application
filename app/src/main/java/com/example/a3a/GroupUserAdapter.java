package com.example.a3a;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.User;
import com.example.a3a.models.UserSubjects;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserSubjects> mDataset = new ArrayList();
    private Context context;

    public GroupUserAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View groupView = inflater.inflate(R.layout.listgroup, parent, false);
        return new GroupUserViewHolder(groupView, removedMember -> {
            mDataset.remove(removedMember);
            notifyDataSetChanged();
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GroupUserViewHolder) holder).bindData(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addData(UserSubjects newSubjects) {
        mDataset.add(newSubjects);
        notifyDataSetChanged();
    }
}

interface GroupItemAction {
    void onRemoveItem(UserSubjects removedMember);
}


class GroupUserViewHolder extends RecyclerView.ViewHolder {
    private TextView userName;
    private TextView[] subTxts = new TextView[6];
    private UserSubjects userData;
    private ImageButton remove;
    private GroupItemAction onRemove;

    public GroupUserViewHolder(@NonNull View itemView, GroupItemAction onRemove) {
        super(itemView);
        userName = itemView.findViewById(R.id.group_user);
        subTxts[0] = itemView.findViewById(R.id.sub1per);
        subTxts[1] = itemView.findViewById(R.id.sub1name);
        subTxts[2] = itemView.findViewById(R.id.sub2per);
        subTxts[3] = itemView.findViewById(R.id.sub2name);
        subTxts[4] = itemView.findViewById(R.id.sub3per);
        subTxts[5] = itemView.findViewById(R.id.sub3name);
        remove = itemView.findViewById(R.id.remove);

        this.onRemove = onRemove;
    }

    public void bindData(UserSubjects userSubjects) {
        this.userData = userSubjects;

        userName.setText(userSubjects.name);

        int idx = 0;
        if (userSubjects.bio.isDoing) {
            subTxts[idx].setText(String.format("%.0f", userSubjects.bio.calcPercentage()) + "%");
            subTxts[idx + 1].setText(userSubjects.bio.name);
            idx += 2;
        }

        if (userSubjects.cm.isDoing) {
            subTxts[idx].setText(String.format("%.0f",userSubjects.cm.calcPercentage()) + "%");
            subTxts[idx + 1].setText(userSubjects.cm.name);
            idx += 2;
        }

        if (userSubjects.chem.isDoing) {
            subTxts[idx].setText(String.format("%.0f",userSubjects.chem.calcPercentage()) + "%");
            subTxts[idx + 1].setText(userSubjects.chem.name);
            idx += 2;
        }

        if (userSubjects.phy.isDoing) {
            subTxts[idx].setText(String.format("%.0f", userSubjects.phy.calcPercentage() )+ "%");
            subTxts[idx + 1].setText(userSubjects.phy.name);
            idx += 2;
        }

        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = authUser.getUid();

        if (userData.uid.equals(userId)) {
            remove.setVisibility(View.GONE);
        } else {
            remove.setVisibility(View.VISIBLE);
            remove.setOnClickListener(v -> {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Tasks.whenAll(
                        db.child("User").child(userId).child("group").child(userData.uid).removeValue(),
                        db.child("User").child(userData.uid).child("joinedGroups").child(userId).removeValue()
                ).addOnCompleteListener(task -> {
                    Toast.makeText(this.itemView.getContext(), "User removed from the Group", Toast.LENGTH_SHORT).show();
                    this.onRemove.onRemoveItem(this.userData);
                });
            });
        }
    }
}

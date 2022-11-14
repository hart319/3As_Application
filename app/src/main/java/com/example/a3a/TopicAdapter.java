package com.example.a3a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a3a.models.KeyTopic;
import com.example.a3a.models.Topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String topic;
    private List<KeyTopic> mDataset = new ArrayList();
    private Context context;

    public TopicAdapter(String topic) {
        this.topic = topic;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View topicView = inflater.inflate(R.layout.topic, parent, false);
        return new TopicViewHolder(topic, topicView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TopicViewHolder) holder).bindData(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<KeyTopic> newDataset) {
        mDataset.clear();
        mDataset.addAll(newDataset);
        notifyDataSetChanged();
    }

    public void addItem(KeyTopic topic) {
        mDataset.add(topic);
        notifyDataSetChanged();
    }

    public void updateItem(KeyTopic topic) {
        int idx = -1;
        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).key == topic.key) {
                idx = i;
            }
        }

        if (idx != -1) {
            mDataset.remove(idx);
            mDataset.add(idx, topic);
            notifyItemChanged(idx);
        }
    }
}

class TopicViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
    private String topic;
    private TextView topicTxt;
    private CheckBox topicCheck;
    private KeyTopic topicData;

    public TopicViewHolder(String topic, @NonNull View itemView) {
        super(itemView);
        this.topic = topic;

        topicTxt = itemView.findViewById(R.id.topic_title);
        topicCheck = itemView.findViewById(R.id.topic_check);

        topicCheck.setOnCheckedChangeListener(this);
    }

    public void bindData(KeyTopic topic) {
        this.topicData = topic;

        topicTxt.setText(topic.name);
        topicCheck.setChecked(topic.done);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("UserSubject")
                .child(authUser.getUid())
                .child(topic)
                .child("topics")
                .child(topicData.key)
                .child("done")
                .setValue(isChecked);
    }
}

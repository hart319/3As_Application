package com.example.a3a;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PasspAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> subTopicNamesList;
    private final List<String> subTopicValuesList;

    public PasspAdapter(Activity context, List<String> subTopicNamesList, List<String> subTopicValuesList) {
        super(context, R.layout.pplist, subTopicNamesList);

        this.context = context;
        this.subTopicNamesList = subTopicNamesList;
        this.subTopicValuesList = subTopicValuesList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.pplist, null, true);
        TextView subTopicName = (TextView) rowView.findViewById(R.id.ppTopic);
        TextView subTopicValues = (TextView) rowView.findViewById(R.id.ppYear);

        subTopicName.setText(subTopicNamesList.get(position));
        subTopicValues.setText(subTopicValuesList.get(position));
        return rowView;
    }
}
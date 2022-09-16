package com.example.networkrequestapplication;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    public WordAdapter(Activity context, ArrayList<Word> words){
        super(context,0,words);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Word word = getItem(position);

        TextView richer = (TextView) listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(word.getmMagnitude());
        richer.setText((formattedMagnitude));


        TextView city = (TextView) listItemView.findViewById(R.id.place1);
        city.setText(word.getmPLace1());

        TextView city1 = (TextView) listItemView.findViewById(R.id.place2);
        city1.setText(word.getmPlace2());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(word.getMdate());

        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(word.getmTime());

        return listItemView;

    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);

    }
}

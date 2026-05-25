package com.example.emptyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    int resource;
    private List<Post> listStatus;

    public PostAdapter(Context context, int resource, List<Post> listStatus) {
        super(context, resource, listStatus);
        this.resource = resource;
        this.listStatus = listStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(this.resource, null);
        }

        Post currentStatus = getItem(position);

        if (currentStatus != null) {
            TextView tvName = (TextView) v.findViewById(R.id.tvName);
            TextView tvContent = (TextView) v.findViewById(R.id.tvContent);
            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);


            if (tvName != null) {
                tvName.setText(currentStatus.getAuthor().getName());
            }
            if (tvContent != null) {
                tvContent.setText(currentStatus.getContent());
            }
            if (tvDate != null) {
                String rawDate = currentStatus.getCreatedAt();
                if (rawDate != null && rawDate.contains("T")) {
                    try {
                        String[] parts = rawDate.split("T");
                        String datePart = parts[0];
                        String timePart = parts[1].substring(0, 5);
                        String[] dateElements = datePart.split("-");
                        String formattedDate = dateElements[2] + "/" + dateElements[1] + "/" + dateElements[0] + " " + timePart;

                        tvDate.setText(formattedDate);
                    } catch (Exception e) {
                        tvDate.setText(rawDate);
                    }
                } else {
                    tvDate.setText(rawDate != null ? rawDate : "Vừa xong");
                }
            }
        }
        return v;
    }
}
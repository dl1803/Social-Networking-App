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

public class StatusAdapter extends ArrayAdapter<Status> {
    int resource;
    private List<Status> listStatus;

    public StatusAdapter(Context context, int resource, List<Status> listStatus) {
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

        Status currentStatus = getItem(position);

        if (currentStatus != null) {
            TextView tvName = (TextView) v.findViewById(R.id.tvName);
            TextView tvContent = (TextView) v.findViewById(R.id.tvContent);
            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);


            if (tvName != null) {
                tvName.setText(currentStatus.getName());
            }
            if (tvContent != null) {
                tvContent.setText(currentStatus.getContent());
            }
            if (tvDate != null) {
                tvDate.setText(currentStatus.getDate());
            }
        }
        return v;
    }
}
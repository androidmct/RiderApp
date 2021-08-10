package com.sage.cabapp.ui.tripreceipt.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.tripreceipt.HelpClickCallback;
import com.sage.cabapp.ui.tripreceipt.model.HelpNeededModel;

import java.util.ArrayList;
import java.util.List;

public class HelpNeededAdapter extends RecyclerView.Adapter<HelpNeededAdapter.MyViewHolder> {

    List<HelpNeededModel> helpNeededModelList = new ArrayList<>();
    private Activity activity;
    HelpClickCallback helpClickCallback;

    public HelpNeededAdapter(List<HelpNeededModel> helpNeededModelList, Activity activity) {
        this.helpNeededModelList = helpNeededModelList;
        this.activity = activity;
    }

    public void setHelpClickCallback(HelpClickCallback helpClickCallback) {
        this.helpClickCallback = helpClickCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_needed_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HelpNeededModel helpNeededModel = helpNeededModelList.get(position);
        holder.help_text.setText(helpNeededModel.getHelpText());

        holder.help_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpClickCallback != null) {
                    helpClickCallback.openHelp(helpNeededModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpNeededModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView help_text;
        RelativeLayout help_item;

        MyViewHolder(View view) {
            super(view);
            help_text = view.findViewById(R.id.help_text);
            help_item = view.findViewById(R.id.help_item);
        }
    }
}

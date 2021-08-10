package com.sage.cabapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Maroof Ahmed Siddique on 13-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class SavedPlacesHomeAdapter extends RecyclerView.Adapter<SavedPlacesHomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<GetAllPlacesData> getAllPlacesDataList = new ArrayList<>();
    SavedPlacesClick savedPlacesClick;

    public void setSavedPlacesClick(SavedPlacesClick savedPlacesClick) {
        this.savedPlacesClick = savedPlacesClick;
    }

    public SavedPlacesHomeAdapter(Context mContext, List<GetAllPlacesData> getAllPlacesDataList) {
        this.mContext = mContext;
        this.getAllPlacesDataList = getAllPlacesDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.home_place_item_layout, parent, false);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetAllPlacesData getAllPlacesData = getAllPlacesDataList.get(position);

        if (getAllPlacesData.getType().equalsIgnoreCase("home")) {
            holder.place_name.setText("Home");
            Glide.with(mContext).load(R.drawable.ic_home_saved).into(holder.place_image);
        } else if (getAllPlacesData.getType().equalsIgnoreCase("work")) {
            holder.place_name.setText("Work");
            Glide.with(mContext).load(R.drawable.ic_work_saved).into(holder.place_image);
        }

        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedPlacesClick != null){
                    savedPlacesClick.openSavedPlace(getAllPlacesData);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return getAllPlacesDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView place_image;
        AppCompatTextView place_name;
        CardView item_click;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            place_image = itemView.findViewById(R.id.place_image);
            place_name = itemView.findViewById(R.id.place_name);
            item_click = itemView.findViewById(R.id.item_click);
        }
    }
}

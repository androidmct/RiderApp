package com.sage.cabapp.ui.triphistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.triphistory.TripHistoryClick;
import com.sage.cabapp.ui.triphistory.model.TripHistoryResponseData;
import com.sage.cabapp.utils.Constant;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.MyViewHolder> {


    List<TripHistoryResponseData> tripHistoryResponseDataList = new ArrayList<>();
    Context context;

    public TripHistoryAdapter(List<TripHistoryResponseData> tripHistoryResponseDataList, Context context) {
        this.tripHistoryResponseDataList = tripHistoryResponseDataList;
        this.context = context;
    }

    TripHistoryClick tripHistoryClick;

    public void setTripHistoryClick(TripHistoryClick tripHistoryClick) {
        this.tripHistoryClick = tripHistoryClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_history_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TripHistoryResponseData tripHistoryResponseData = tripHistoryResponseDataList.get(position);

        holder.trip_date.setText(Constant.convertDate(tripHistoryResponseData.getCreatedAt()));
        holder.trip_fare.setText(tripHistoryResponseData.getRideFare());

        float rating = tripHistoryResponseData.getTripRating();

        holder.trip_rating.setRating(rating);

        if (tripHistoryResponseData.getDriverData() != null && tripHistoryResponseData.getDriverData().size() > 0) {
            Glide.with(context).load(tripHistoryResponseData.getDriverData().get(0).getProfile()).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(holder.driver_image);
        } else {
            Glide.with(context).load(R.drawable.profile_avatar).into(holder.driver_image);
        }

        holder.history_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tripHistoryClick != null) {
                    tripHistoryClick.openTripHistory(tripHistoryResponseData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripHistoryResponseDataList.size();
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

        RelativeLayout history_item;
        AppCompatTextView trip_date;
        AppCompatTextView trip_fare;
        ScaleRatingBar trip_rating;
        CircleImageView driver_image;


        MyViewHolder(View view) {
            super(view);

            history_item = view.findViewById(R.id.history_item);
            trip_fare = view.findViewById(R.id.trip_fare);
            trip_date = view.findViewById(R.id.trip_date);
            trip_rating = view.findViewById(R.id.trip_rating);
            driver_image = view.findViewById(R.id.driver_image);
        }
    }
}

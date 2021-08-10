package com.sage.cabapp.ui.setaddress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.setaddress.DestinationAddressClickCallback;
import com.sage.cabapp.ui.setaddress.model.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Maroof Ahmed Siddique on 02-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class DestinationPlacesAutoCompleteAdapter extends RecyclerView.Adapter<DestinationPlacesAutoCompleteAdapter.PredictionHolder>{
    private static final String TAG = "PlacesAutoAdapter";
    private ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();

    private Context mContext;
    private DestinationAddressClickCallback destinationAddressClickCallback;

    public void setDestinationAddressClickCallback(DestinationAddressClickCallback destinationAddressClickCallback) {
        this.destinationAddressClickCallback = destinationAddressClickCallback;
    }

    public DestinationPlacesAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    public void addAll(ArrayList<PlaceAutocomplete> mResults) {

        if (mResultList != null && mResultList.size() > 0) {
            mResultList.clear();
        }

        if (mResults != null && mResults.size()>0){
            Objects.requireNonNull(mResultList).addAll(mResults);
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.place_recycler_item_layout, viewGroup, false);
        return new PredictionHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder mPredictionHolder, final int position) {

        PlaceAutocomplete placeAutocomplete = mResultList.get(position);

        mPredictionHolder.address_places.setText(placeAutocomplete.address);
        mPredictionHolder.street_name.setText(placeAutocomplete.area);

        if (position == mResultList.size() - 1) {
            mPredictionHolder.viewline.setVisibility(View.GONE);
        } else {
            mPredictionHolder.viewline.setVisibility(View.VISIBLE);
        }

        mPredictionHolder.itemview.setOnClickListener(v -> {
            if (destinationAddressClickCallback != null) {
                destinationAddressClickCallback.destinationAddressClick(placeAutocomplete);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    public class PredictionHolder extends RecyclerView.ViewHolder  {
        private AppCompatTextView address_places, street_name;
        private View viewline;
        private LinearLayout itemview;

        PredictionHolder(View itemView) {
            super(itemView);
            address_places = itemView.findViewById(R.id.address_places);
            street_name = itemView.findViewById(R.id.street_name);
            viewline = itemView.findViewById(R.id.viewline);
            itemview = itemView.findViewById(R.id.itemview);
        }
    }


}

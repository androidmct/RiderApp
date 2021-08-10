package com.sage.cabapp.ui.requestforcar.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.requestforcar.CallbackCarDetails;
import com.sage.cabapp.ui.requestforcar.CallbackSelectCarDetails;
import com.sage.cabapp.ui.requestforcar.model.AllCarsModel;
import com.sage.cabapp.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maroof Ahmed Siddique on 16-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class RequestCarItemAdapter extends RecyclerView.Adapter<RequestCarItemAdapter.MyViewHolder> {

    private Activity mContext;
    List<AllCarsModel> allCarsModelList = new ArrayList<>();
    CallbackCarDetails callbackCarDetails;
    CallbackSelectCarDetails callbackSelectCarDetails;

    public void setCallbackSelectCarDetails(CallbackSelectCarDetails callbackSelectCarDetails) {
        this.callbackSelectCarDetails = callbackSelectCarDetails;
    }

    public RequestCarItemAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void setCallbackCarDetails(CallbackCarDetails callbackCarDetails) {
        this.callbackCarDetails = callbackCarDetails;
    }

    public RequestCarItemAdapter(Activity mContext, List<AllCarsModel> allCarsModelList) {
        this.mContext = mContext;
        this.allCarsModelList = allCarsModelList;
    }

    public void updatePosition(List<AllCarsModel> allCarsModel) {

        if (allCarsModelList != null && allCarsModelList.size() > 0) {
            allCarsModelList.clear();
        }

        Objects.requireNonNull(allCarsModelList).addAll(allCarsModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.request_car_item_layout, parent, false);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestCarItemAdapter.MyViewHolder holder, int position) {

        AllCarsModel allCarsModel = allCarsModelList.get(position);

        if (allCarsModel.isSelected()) {
            holder.mainview.setBackgroundResource(R.drawable.car_background_gradient);
            holder.car_name.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.car_time.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.car_fare.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.car_seats.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.car_seats.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car_users, 0, 0, 0);
        } else {
            holder.mainview.setBackgroundResource(R.drawable.card_background_white);
            holder.car_name.setTextColor(ContextCompat.getColor(mContext, R.color.heading));
            holder.car_time.setTextColor(ContextCompat.getColor(mContext, R.color.heading));
            holder.car_fare.setTextColor(ContextCompat.getColor(mContext, R.color.heading));
            holder.car_seats.setTextColor(ContextCompat.getColor(mContext, R.color.car_seats_color));
            holder.car_seats.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car_users_gray, 0, 0, 0);
        }

        Glide.with(mContext).load(allCarsModel.getCarImage()).into(holder.car_image);

        holder.car_name.setText(allCarsModel.getCarName());
        holder.car_time.setText(Constant.convertCarTime(allCarsModel.getCarTime()));
        holder.car_fare.setText(String.format("$%s", allCarsModel.getCarFare()));
        holder.car_seats.setText(allCarsModel.getCarSeats());

    }

    @Override
    public int getItemCount() {
        return allCarsModelList.size();
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

        @BindView(R.id.mainview)
        RelativeLayout mainview;

        @BindView(R.id.cars_view)
        RelativeLayout cars_view;

        @BindView(R.id.dummy_view)
        RelativeLayout dummy_view;

        @BindView(R.id.car_image)
        AppCompatImageView car_image;

        @BindView(R.id.car_name)
        AppCompatTextView car_name;

        @BindView(R.id.car_time)
        AppCompatTextView car_time;

        @BindView(R.id.car_fare)
        AppCompatTextView car_fare;

        @BindView(R.id.car_seats)
        AppCompatTextView car_seats;

        @OnClick(R.id.mainview)
        void onClickmain_rl(View view) {

            AllCarsModel bean = allCarsModelList.get(getAdapterPosition());

            if (bean.isSelected()) {
                if (callbackCarDetails != null) {
                    callbackCarDetails.openCarDetails(bean);
                }
            }

            /*if (bean.isSelected()) {
                if (callbackCarDetails != null) {
                    callbackCarDetails.openCarDetails(bean);
                }
            }

            for (AllCarsModel bean1 :
                    allCarsModelList) {
                bean1.setSelected(false);
            }

            if (callbackSelectCarDetails != null) {
                callbackSelectCarDetails.selectCarDetails(bean);
            }
            bean.setSelected(true)

            notifyDataSetChanged();*/
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setLayoutParams(new ViewGroup.LayoutParams(Constant.getWidthAndHeight(mContext, true) / 2 + Constant.getWidthAndHeight(mContext, true) / 4, mContext.getResources().getDimensionPixelSize(R.dimen.margin_150)));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainview.getLayoutParams();
            params.setMargins(0, 0, 40, 0);
            mainview.setLayoutParams(params);

        }
    }
}

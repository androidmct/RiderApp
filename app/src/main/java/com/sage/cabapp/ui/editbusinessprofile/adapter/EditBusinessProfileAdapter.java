package com.sage.cabapp.ui.editbusinessprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.editbusinessprofile.ChangeBusinessPaymentMethod;
import com.sage.cabapp.ui.editbusinessprofile.model.EditBusinessProfileModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditBusinessProfileAdapter extends RecyclerView.Adapter<EditBusinessProfileAdapter.MyViewHolder> {

    List<EditBusinessProfileModel> editBusinessProfileModelList = new ArrayList<>();
    Context context;
    ChangeBusinessPaymentMethod changeBusinessPaymentMethod;

    public void setChangeBusinessPaymentMethod(ChangeBusinessPaymentMethod changeBusinessPaymentMethod) {
        this.changeBusinessPaymentMethod = changeBusinessPaymentMethod;
    }

    public EditBusinessProfileAdapter(List<EditBusinessProfileModel> editBusinessProfileModelList, Context context) {
        this.editBusinessProfileModelList = editBusinessProfileModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_profile_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final EditBusinessProfileModel newBusinessProfileModel = editBusinessProfileModelList.get(position);

        holder.vehicle_name.setText(newBusinessProfileModel.getCardNumber());
        holder.car_image.setBackgroundResource(newBusinessProfileModel.getCardImg());

        if (newBusinessProfileModel.isTicked()) {
            holder.right_tick_view.setVisibility(View.VISIBLE);
        } else {
            holder.right_tick_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return editBusinessProfileModelList.size();
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

        @BindView(R.id.vehicle_name)
        AppCompatTextView vehicle_name;

        @BindView(R.id.right_tick_view)
        AppCompatImageView right_tick_view;

        @BindView(R.id.car_image)
        AppCompatImageView car_image;

        @BindView(R.id.vehicle_rl)
        RelativeLayout vehicle_rl;

        @OnClick(R.id.vehicle_rl)
        void onClickmain_rl(View view) {

            for (EditBusinessProfileModel bean :
                    editBusinessProfileModelList) {
                bean.setTicked(false);
            }

            EditBusinessProfileModel bean = editBusinessProfileModelList.get(getAdapterPosition());
            bean.setTicked(true);

            if (changeBusinessPaymentMethod != null){
                changeBusinessPaymentMethod.setBusinessPayment(bean);
            }
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

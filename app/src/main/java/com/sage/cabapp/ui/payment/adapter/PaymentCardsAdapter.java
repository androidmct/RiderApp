package com.sage.cabapp.ui.payment.adapter;

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
import com.sage.cabapp.ui.payment.CallbackOpenPaymentCard;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.utils.Base64EncyrptDecrypt;

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
public class PaymentCardsAdapter extends RecyclerView.Adapter<PaymentCardsAdapter.MyViewHolder> {

    List<AllCardsData> getAllPaymentsCardDataList = new ArrayList<>();
    Context context;
    CallbackOpenPaymentCard callbackOpenPaymentCard;

    public void setCallbackOpenPaymentCard(CallbackOpenPaymentCard callbackOpenPaymentCard) {
        this.callbackOpenPaymentCard = callbackOpenPaymentCard;
    }

    public PaymentCardsAdapter(List<AllCardsData> getAllPaymentsCardDataList, Context context) {
        this.getAllPaymentsCardDataList = getAllPaymentsCardDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments_card_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AllCardsData getAllPaymentsCardData = getAllPaymentsCardDataList.get(position);

        holder.vehicle_name.setText(String.format("•••• •••• •••• %s", getAllPaymentsCardData.getCard().getLast4()));

        if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("visa")) {
            holder.car_image.setBackgroundResource(R.drawable.payment_ic_visa);
        } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("mastercard")) {
            holder.car_image.setBackgroundResource(R.drawable.payment_ic_master_card);
        } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("amex")) {
            holder.car_image.setBackgroundResource(R.drawable.payment_ic_amex);
        } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("discover")) {
            holder.car_image.setBackgroundResource(R.drawable.payment_ic_discover);
        }else {
            holder.car_image.setBackgroundResource(R.drawable.payment_ic_method);
        }

    }

    @Override
    public int getItemCount() {
        return getAllPaymentsCardDataList.size();
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

        @BindView(R.id.car_image)
        AppCompatImageView car_image;

        @BindView(R.id.vehicle_rl)
        RelativeLayout vehicle_rl;

        @OnClick(R.id.vehicle_rl)
        void onClickmain_rl(View view) {

            AllCardsData getAllPaymentsCardData = getAllPaymentsCardDataList.get(getAdapterPosition());

            if (callbackOpenPaymentCard != null){
                callbackOpenPaymentCard.openPaymentCard(getAllPaymentsCardData);
            }
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public String decodeBase64(String message) {
        return Base64EncyrptDecrypt.decrypt(message);
    }
}

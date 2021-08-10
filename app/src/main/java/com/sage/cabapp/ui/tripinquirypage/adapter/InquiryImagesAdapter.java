package com.sage.cabapp.ui.tripinquirypage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.tripinquirypage.InquiryImageDelete;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Maroof Ahmed Siddique on 13-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class InquiryImagesAdapter extends RecyclerView.Adapter<InquiryImagesAdapter.MyViewHolder> {

    List<File> imagesList = new ArrayList<>();
    Context context;
    InquiryImageDelete inquiryImageDelete;

    public void setInquiryImageDelete(InquiryImageDelete inquiryImageDelete) {
        this.inquiryImageDelete = inquiryImageDelete;
    }

    public InquiryImagesAdapter(Context context) {
        this.context = context;
    }

    public void addImages(List<File> images) {

        if (imagesList != null && imagesList.size() > 0) {
            imagesList.clear();
        }

        Objects.requireNonNull(imagesList).addAll(images);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inquiry_images_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        File file = imagesList.get(position);

        Glide.with(context).load(file).into(holder.insurance_photo);

        holder.delete_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inquiryImageDelete != null) {
                    inquiryImageDelete.deleteImage(file);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
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

        AppCompatImageView insurance_photo;
        AppCompatImageView delete_insurance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            insurance_photo = itemView.findViewById(R.id.insurance_photo);
            delete_insurance = itemView.findViewById(R.id.delete_insurance);
        }
    }
}

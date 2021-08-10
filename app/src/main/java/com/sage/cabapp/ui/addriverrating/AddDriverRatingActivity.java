package com.sage.cabapp.ui.addriverrating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAddDriverRatingBinding;
import com.sage.cabapp.ui.addrivertip.AddDriverTipActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.willy.ratingbar.BaseRatingBar;

import javax.inject.Inject;

public class AddDriverRatingActivity extends BaseActivity<ActivityAddDriverRatingBinding, AddDriverRatingViewModel> implements AddDriverRatingNavigator {

    @Inject
    ViewModelProviderFactory factory;
    AddDriverRatingViewModel addDriverRatingViewModel;
    ActivityAddDriverRatingBinding activityAddDriverRatingBinding;
    float previous_rating = 0;


    @Override
    public int getBindingVariable() {
        return BR.addDriverRatingViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_driver_rating;
    }

    @Override
    public AddDriverRatingViewModel getViewModel() {
        addDriverRatingViewModel = ViewModelProviders.of(this, factory).get(AddDriverRatingViewModel.class);
        return addDriverRatingViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddDriverRatingBinding = getViewDataBinding();
        addDriverRatingViewModel.setNavigator(this);

        String profilePic = SharedData.getString(this, Constant.DRIVER_PIC);
        Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityAddDriverRatingBinding.driverPic);

        String driverName = SharedData.getString(this, Constant.DRIVER_NAME);
        activityAddDriverRatingBinding.ratingText.setText(String.format("Please rate your experience with %s. Thanks", driverName));
        // setRating();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("ClickableViewAccessibility")
    void setRating() {

        activityAddDriverRatingBinding.rate.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {

            }
        });

        activityAddDriverRatingBinding.rate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    previous_rating = activityAddDriverRatingBinding.rate.getRating();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (previous_rating == activityAddDriverRatingBinding.rate.getRating()) {
                        activityAddDriverRatingBinding.rate.setIsIndicator(true);
                        //pressed same star as before, do your action here

                        Intent intent = new Intent(getApplicationContext(), AddDriverTipActivity.class);
                        intent.putExtra("RATING", activityAddDriverRatingBinding.rate.getRating());
                        startActivity(intent);
                        slideRightToLeft();

                        return true; //we have consumed this event
                    }
                }
                return false; //do whatever ratingBar has to do
            }
        });
    }

    @Override
    public void onBackPressed() {
        openActivity(this, HomeActivity.class, false, true);
        slideLeftToRight();
    }

    @Override
    public void onBack() {
        openActivity(this, HomeActivity.class, false, true);
        slideLeftToRight();
    }

    @Override
    public void next() {
        Intent intent = new Intent(getApplicationContext(), AddDriverTipActivity.class);
        intent.putExtra("RATING", activityAddDriverRatingBinding.rate.getRating());
        startActivity(intent);
        slideRightToLeft();
    }

}

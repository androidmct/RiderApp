package com.sage.cabapp.ui.addbusinessprofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAddBusinessProfileBinding;
import com.sage.cabapp.ui.base.BaseActivity;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddBusinessProfileActivity extends BaseActivity<ActivityAddBusinessProfileBinding, AddBusinessProfileViewModel> implements AddBusinessProfileNavigator {

    @Inject
    ViewModelProviderFactory factory;
    AddBusinessProfileViewModel addBusinessProfileViewModel;
    ActivityAddBusinessProfileBinding activityAddBusinessProfileBinding;

    @Override
    public int getBindingVariable() {
        return BR.addBusinessViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_business_profile;
    }

    @Override
    public AddBusinessProfileViewModel getViewModel() {
        addBusinessProfileViewModel = ViewModelProviders.of(this, factory).get(AddBusinessProfileViewModel.class);
        return addBusinessProfileViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBusinessProfileBinding = getViewDataBinding();
        addBusinessProfileViewModel.setNavigator(this);


        activityAddBusinessProfileBinding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddBusinessProfileBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddBusinessProfileBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        activityAddBusinessProfileBinding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAddBusinessProfileBinding.etEmail.getRight() - activityAddBusinessProfileBinding.etEmail.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAddBusinessProfileBinding.etEmail.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void submitProfile() {
        String email = Objects.requireNonNull(activityAddBusinessProfileBinding.etEmail.getText()).toString().trim();
        if (!addBusinessProfileViewModel.isEnterEmail(email)) {
            activityAddBusinessProfileBinding.errorMessage.setText(getResources().getString(R.string.enter_email));
            activityAddBusinessProfileBinding.errorMessage.setVisibility(View.VISIBLE);
        } else if (!addBusinessProfileViewModel.isEmailValid(email)) {
            activityAddBusinessProfileBinding.errorMessage.setText(getResources().getString(R.string.enter_email_incorrect));
            activityAddBusinessProfileBinding.errorMessage.setVisibility(View.VISIBLE);
        } else {
            activityAddBusinessProfileBinding.errorMessage.setVisibility(View.GONE);
            hideKeyboard();
            Intent intent = new Intent(this, NewBusinessProfileActivity.class);
            intent.putExtra("BUSINESS_EMAIL", email);
            startActivity(intent);
            finish();
            slideRightToLeft();
        }
    }
}

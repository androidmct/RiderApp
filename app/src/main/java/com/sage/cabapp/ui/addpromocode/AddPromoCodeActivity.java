package com.sage.cabapp.ui.addpromocode;

import android.annotation.SuppressLint;
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
import com.sage.cabapp.databinding.ActivityAddPromoCodeBinding;
import com.sage.cabapp.ui.base.BaseActivity;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddPromoCodeActivity extends BaseActivity<ActivityAddPromoCodeBinding,AddPromoCodeViewModel> implements AddPromoCodeNavigator {

    @Inject
    ViewModelProviderFactory factory;
    AddPromoCodeViewModel addPromoCodeViewModel;
    ActivityAddPromoCodeBinding activityAddPromoCodeBinding;

    @Override
    public int getBindingVariable() {
        return BR.addPromoCodeViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_promo_code;
    }

    @Override
    public AddPromoCodeViewModel getViewModel() {
        addPromoCodeViewModel = ViewModelProviders.of(this,factory).get(AddPromoCodeViewModel.class);
        return addPromoCodeViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddPromoCodeBinding = getViewDataBinding();
        addPromoCodeViewModel.setNavigator(this);

        activityAddPromoCodeBinding.etPromoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddPromoCodeBinding.etPromoCode.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddPromoCodeBinding.etPromoCode.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAddPromoCodeBinding.etPromoCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAddPromoCodeBinding.etPromoCode.getRight() - activityAddPromoCodeBinding.etPromoCode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAddPromoCodeBinding.etPromoCode.setText("");
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
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void submitCode() {

    }
}

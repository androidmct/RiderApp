package com.sage.cabapp.ui.addemailsignup;

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
import com.sage.cabapp.databinding.ActivityAddEmailSignupBinding;
import com.sage.cabapp.ui.addnamesignup.AddNameSignupActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class AddEmailSignupActivity extends BaseActivity<ActivityAddEmailSignupBinding, AddEmailSignupViewModel> implements AddEmailSignupNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private ActivityAddEmailSignupBinding activityAddEmailSignupBinding;
    private AddEmailSignupViewModel addEmailSignupViewModel;

    @Override
    public int getBindingVariable() {
        return BR.emailViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_email_signup;
    }

    @Override
    public AddEmailSignupViewModel getViewModel() {
        addEmailSignupViewModel = ViewModelProviders.of(this, factory).get(AddEmailSignupViewModel.class);
        return addEmailSignupViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddEmailSignupBinding = getViewDataBinding();
        addEmailSignupViewModel.setNavigator(this);


        activityAddEmailSignupBinding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddEmailSignupBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddEmailSignupBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAddEmailSignupBinding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAddEmailSignupBinding.etEmail.getRight() - activityAddEmailSignupBinding.etEmail.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAddEmailSignupBinding.etEmail.setText("");
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
    public void backButton() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void nextButton() {

        String email = Objects.requireNonNull(activityAddEmailSignupBinding.etEmail.getText()).toString().trim();
        if (!addEmailSignupViewModel.isEnterEmail(email)) {
            activityAddEmailSignupBinding.errorMessage.setText(getResources().getString(R.string.enter_email));
            activityAddEmailSignupBinding.errorMessage.setVisibility(View.VISIBLE);
        } else if (!addEmailSignupViewModel.isEmailValid(email)) {
            activityAddEmailSignupBinding.errorMessage.setText(getResources().getString(R.string.enter_email_incorrect));
            activityAddEmailSignupBinding.errorMessage.setVisibility(View.VISIBLE);
        } else {
            activityAddEmailSignupBinding.errorMessage.setVisibility(View.GONE);
            hideKeyboard();

            SharedData.saveString(this, Constant.TEMP_EMAIL, email);
            openActivity(this, AddNameSignupActivity.class,false,false);
            slideRightToLeft();
        }
    }
}

package com.sage.cabapp.ui.editemail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAccountEditEmailBinding;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditEmailActivity extends BaseActivity<ActivityAccountEditEmailBinding, EditEmailViewModel> implements EditEmailNavigator {

    @Inject
    ViewModelProviderFactory factory;
    EditEmailViewModel editEmailViewModel;
    ActivityAccountEditEmailBinding activityAccountEditEmailBinding;

    @Override
    public int getBindingVariable() {
        return BR.editEmailViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_edit_email;
    }

    @Override
    public EditEmailViewModel getViewModel() {
        editEmailViewModel = ViewModelProviders.of(this, factory).get(EditEmailViewModel.class);
        return editEmailViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountEditEmailBinding = getViewDataBinding();
        editEmailViewModel.setNavigator(this);

        updateProfileData();

        activityAccountEditEmailBinding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAccountEditEmailBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAccountEditEmailBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAccountEditEmailBinding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                activityAccountEditEmailBinding.etEmail.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAccountEditEmailBinding.etEmail.getRight() - activityAccountEditEmailBinding.etEmail.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAccountEditEmailBinding.etEmail.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityAccountEditEmailBinding.etEmail.postDelayed(() -> activityAccountEditEmailBinding.etEmail.setSelection(Objects.requireNonNull(activityAccountEditEmailBinding.etEmail.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    void updateProfileData() {

        String email = SharedData.getString(this, Constant.EMAIL);
        activityAccountEditEmailBinding.etEmail.setText(email);


        activityAccountEditEmailBinding.etEmail.setCursorVisible(false);
        activityAccountEditEmailBinding.etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

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
    public void nextEmail() {
        String email = Objects.requireNonNull(activityAccountEditEmailBinding.etEmail.getText()).toString().trim();
        if (!editEmailViewModel.isEnterEmail(email)) {
            activityAccountEditEmailBinding.errorMessage.setText(getResources().getString(R.string.enter_email));
            activityAccountEditEmailBinding.errorMessage.setVisibility(View.VISIBLE);
        } else if (!editEmailViewModel.isEmailValid(email)) {
            activityAccountEditEmailBinding.errorMessage.setText(getResources().getString(R.string.enter_email_incorrect));
            activityAccountEditEmailBinding.errorMessage.setVisibility(View.VISIBLE);
        } else {

            hideKeyboard();
            if (isNetworkConnected()) {

                showLoading("");
                editEmailViewModel.updateEmailWS(this, encodeBase64(email));
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }
    }

    @Override
    public void successAPI(ProfileUpdateResponse profileUpdateResponse) {
        hideLoading();
        try {
            if (profileUpdateResponse.getStatus() != null && profileUpdateResponse.getStatus().equalsIgnoreCase("true")) {

                SharedData.saveBool(this, Constant.PROFILE_UPDATED, true);
                finish();
                slideLeftToRight();
            } else {
                vibrate();
                Constant.showErrorToast(profileUpdateResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }
}

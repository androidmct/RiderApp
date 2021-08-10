package com.sage.cabapp.ui.editname;

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
import com.sage.cabapp.databinding.ActivityAccountEditNameBinding;
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
public class EditNameActivity extends BaseActivity<ActivityAccountEditNameBinding, EditNameViewModel> implements EditNameNavigator {

    @Inject
    ViewModelProviderFactory factory;
    EditNameViewModel editNameViewModel;
    ActivityAccountEditNameBinding activityAccountEditNameBinding;

    @Override
    public int getBindingVariable() {
        return BR.editNameViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_edit_name;
    }

    @Override
    public EditNameViewModel getViewModel() {
        editNameViewModel = ViewModelProviders.of(this, factory).get(EditNameViewModel.class);
        return editNameViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAccountEditNameBinding = getViewDataBinding();
        editNameViewModel.setNavigator(this);

        updateProfileData();

        activityAccountEditNameBinding.etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAccountEditNameBinding.etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAccountEditNameBinding.etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAccountEditNameBinding.etLastName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                activityAccountEditNameBinding.etLastName.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAccountEditNameBinding.etLastName.getRight() - activityAccountEditNameBinding.etLastName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAccountEditNameBinding.etLastName.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityAccountEditNameBinding.etLastName.postDelayed(() -> activityAccountEditNameBinding.etLastName.setSelection(Objects.requireNonNull(activityAccountEditNameBinding.etLastName.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        activityAccountEditNameBinding.etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAccountEditNameBinding.etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAccountEditNameBinding.etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAccountEditNameBinding.etFirstName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                activityAccountEditNameBinding.etFirstName.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAccountEditNameBinding.etFirstName.getRight() - activityAccountEditNameBinding.etFirstName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAccountEditNameBinding.etFirstName.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                try {
                    activityAccountEditNameBinding.etFirstName.postDelayed(() -> activityAccountEditNameBinding.etFirstName.setSelection(Objects.requireNonNull(activityAccountEditNameBinding.etFirstName.getText()).toString().length()), 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        
    }

    void updateProfileData() {

        String firstName = SharedData.getString(this, Constant.FIRST_NAME);
        String lastName = SharedData.getString(this, Constant.LAST_NAME);
        activityAccountEditNameBinding.etFirstName.setText(firstName);
        activityAccountEditNameBinding.etLastName.setText(lastName);


        activityAccountEditNameBinding.etFirstName.setCursorVisible(false);
        activityAccountEditNameBinding.etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);


        activityAccountEditNameBinding.etLastName.setCursorVisible(false);
        activityAccountEditNameBinding.etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

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
    public void saveName() {

        String firstName = Objects.requireNonNull(activityAccountEditNameBinding.etFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(activityAccountEditNameBinding.etLastName.getText()).toString().trim();

        if (!editNameViewModel.isFirstNameValid(firstName)) {
            vibrate();
            Constant.showErrorToast("Please enter first name", getApplicationContext());
        } else if (!editNameViewModel.isLastNameValid(lastName)) {
            vibrate();
            Constant.showErrorToast("Please enter last name", getApplicationContext());
        } else {

            hideKeyboard();
            if (isNetworkConnected()) {

                showLoading("");
                editNameViewModel.updateNameWS(this, firstName, lastName);
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

                SharedData.saveBool(this,Constant.PROFILE_UPDATED,true);
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

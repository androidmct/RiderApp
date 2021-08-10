package com.sage.cabapp.ui.addnamesignup;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAddNameSignupBinding;
import com.sage.cabapp.ui.addnamesignup.model.RegisterMainResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.howtopay.HowToPayActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class AddNameSignupActivity extends BaseActivity<ActivityAddNameSignupBinding, AddNameSignupViewModel> implements AddNameSignUpNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private AddNameSignupViewModel addNameSignupViewModel;
    private ActivityAddNameSignupBinding activityAddNameSignupBinding;

    @Override
    public int getBindingVariable() {
        return BR.nameViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_name_signup;
    }

    @Override
    public AddNameSignupViewModel getViewModel() {
        addNameSignupViewModel = ViewModelProviders.of(this, factory).get(AddNameSignupViewModel.class);
        return addNameSignupViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNameSignupBinding = getViewDataBinding();
        addNameSignupViewModel.setNavigator(this);

        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AddNameSignupActivity.this, instanceIdResult -> {
                SharedData.saveString(AddNameSignupActivity.this, Constant.REFRESHED_TOKEN, instanceIdResult.getToken());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setTextWatchers();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTextWatchers() {

        activityAddNameSignupBinding.etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddNameSignupBinding.etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddNameSignupBinding.etFirstName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });

        activityAddNameSignupBinding.etFirstName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAddNameSignupBinding.etFirstName.getRight() - activityAddNameSignupBinding.etFirstName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAddNameSignupBinding.etFirstName.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

        activityAddNameSignupBinding.etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityAddNameSignupBinding.etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAddNameSignupBinding.etLastName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityAddNameSignupBinding.etLastName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAddNameSignupBinding.etLastName.getRight() - activityAddNameSignupBinding.etLastName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAddNameSignupBinding.etLastName.setText("");
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

        String firstName = Objects.requireNonNull(activityAddNameSignupBinding.etFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(activityAddNameSignupBinding.etLastName.getText()).toString().trim();

        if (!addNameSignupViewModel.isFirstNameValid(firstName)) {
            vibrate();
            Constant.showErrorToast("Please enter first name", getApplicationContext());
        } else if (!addNameSignupViewModel.isLastNameValid(lastName)) {
            vibrate();
            Constant.showErrorToast("Please enter last name", getApplicationContext());
        } else {
            hideKeyboard();
            if (isNetworkConnected()) {

                showLoading("");

                try {
                    String number = SharedData.getString(this, Constant.TEMP_PHONE_NUMBER);
                    String email = SharedData.getString(this, Constant.TEMP_EMAIL);
                    String devicetoken = SharedData.getString(this, Constant.REFRESHED_TOKEN);

                    addNameSignupViewModel.registerUserWS(encodeBase64(number), encodeBase64(email), firstName, lastName,devicetoken);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoading();
                    Constant.showErrorToast(e.getMessage(), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }
    }

    @Override
    public void successAPI(RegisterMainResponse registerMainResponse) {
        hideLoading();
        try {
            if (registerMainResponse.getStatus() != null && registerMainResponse.getStatus().equalsIgnoreCase("true")) {

                SharedData.clearSharedPreference(this);

                SharedData.saveString(this, Constant.PHONE_NUMBER, decodeBase64(registerMainResponse.getData().getUserMobile()));
                SharedData.saveString(this, Constant.EMAIL, decodeBase64(registerMainResponse.getData().getUserEmail()));
                SharedData.saveString(this, Constant.USERID, registerMainResponse.getData().getUserid());
                SharedData.saveString(this, Constant.FIRST_NAME, registerMainResponse.getData().getFname());
                SharedData.saveString(this, Constant.LAST_NAME, registerMainResponse.getData().getLname());

                openActivity(this, HowToPayActivity.class, false, true);
                slideRightToLeft();
            } else {
                vibrate();
                Constant.showErrorToast(registerMainResponse.getMessage(), this);
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

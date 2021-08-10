package com.sage.cabapp.ui.editphone;

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
import com.sage.cabapp.databinding.ActivityAccountEditPhoneBinding;
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
public class EditPhoneActivity extends BaseActivity<ActivityAccountEditPhoneBinding, EditPhoneViewModel> implements EditPhoneNavigator {

    @Inject
    ViewModelProviderFactory factory;
    ActivityAccountEditPhoneBinding activityAccountEditPhoneBinding;
    EditPhoneViewModel editPhoneViewModel;

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 12; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 10; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 4; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = ' ';

    @Override
    public int getBindingVariable() {
        return BR.editPhoneViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_edit_phone;
    }

    @Override
    public EditPhoneViewModel getViewModel() {
        editPhoneViewModel = ViewModelProviders.of(this, factory).get(EditPhoneViewModel.class);
        return editPhoneViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAccountEditPhoneBinding = getViewDataBinding();
        editPhoneViewModel.setNavigator(this);

        String phone = SharedData.getString(this, Constant.PHONE_NUMBER);
        String newPhone = phone.substring(phone.length() - 10);

        activityAccountEditPhoneBinding.etPhonenumber.setText(newPhone);
        activityAccountEditPhoneBinding.etPhonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
        activityAccountEditPhoneBinding.etPhonenumber.setCursorVisible(false);
        activityAccountEditPhoneBinding.etPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    activityAccountEditPhoneBinding.etPhonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityAccountEditPhoneBinding.etPhonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() < 11) {
                    if (!isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
                        s.replace(0, s.length(), concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
                    }
                }

            }
        });

        activityAccountEditPhoneBinding.etPhonenumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                activityAccountEditPhoneBinding.etPhonenumber.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityAccountEditPhoneBinding.etPhonenumber.getRight() - activityAccountEditPhoneBinding.etPhonenumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityAccountEditPhoneBinding.etPhonenumber.setText("");
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
    public void nextNumber() {
        String phoneNumber = Objects.requireNonNull(activityAccountEditPhoneBinding.etPhonenumber.getText()).toString().trim();
        phoneNumber = phoneNumber.replace(" ", "");

        if (!editPhoneViewModel.isPhoneValid(phoneNumber)) {
            vibrate();
            activityAccountEditPhoneBinding.errorMessage.setText("No phone number provided. Please enter a phone number.");
            activityAccountEditPhoneBinding.errorMessage.setVisibility(View.VISIBLE);
        } else if (phoneNumber.length() < 10) {
            vibrate();
            activityAccountEditPhoneBinding.errorMessage.setText("Entered phone number is incorrect.");
            activityAccountEditPhoneBinding.errorMessage.setVisibility(View.VISIBLE);
        } else {
            hideKeyboard();
            activityAccountEditPhoneBinding.errorMessage.setVisibility(View.GONE);
            if (isNetworkConnected()) {
                Intent intent = new Intent(this, UpdatePhoneActivity.class);
                intent.putExtra("MOBILE_NUMBER", "+91" + phoneNumber);
                startActivity(intent);
                finish();
                slideRightToLeft();
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }
    }

    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }


}

package com.sage.cabapp.ui.phoneverification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityPhoneVerificationBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.verifyotp.VerifyOTPActivity;
import com.sage.cabapp.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class PhoneVerificationActivity extends BaseActivity<ActivityPhoneVerificationBinding, PhoneViewModel> implements PhoneNavigator {


    @Inject
    ViewModelProviderFactory factory;
    private PhoneViewModel phoneViewModel;
    private ActivityPhoneVerificationBinding activityPhoneVerificationBinding;


    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 12; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 10; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 4; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = ' ';


    @Override
    public int getBindingVariable() {
        return BR.phoneViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_verification;
    }

    @Override
    public PhoneViewModel getViewModel() {
        phoneViewModel = ViewModelProviders.of(this, factory).get(PhoneViewModel.class);
        return phoneViewModel;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhoneVerificationBinding = getViewDataBinding();
        phoneViewModel.setNavigator(this);

        activityPhoneVerificationBinding.subview.setVisibility(View.GONE);
        activityPhoneVerificationBinding.mainview.setVisibility(View.VISIBLE);

        activityPhoneVerificationBinding.etPhonenumber.setOnTouchListener((v, event) -> {

            activityPhoneVerificationBinding.subview.setVisibility(View.VISIBLE);
            activityPhoneVerificationBinding.mainview.setVisibility(View.GONE);
            return false;
        });

        activityPhoneVerificationBinding.etPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityPhoneVerificationBinding.etPhonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityPhoneVerificationBinding.etPhonenumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
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

        activityPhoneVerificationBinding.etPhonenumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityPhoneVerificationBinding.etPhonenumber.getRight() - activityPhoneVerificationBinding.etPhonenumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityPhoneVerificationBinding.etPhonenumber.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

        setSpannableTermsConditions();

    }

    private void setSpannableTermsConditions() {

        SpannableString spannableString = new SpannableString("By continuing you agree to our\nTerms and Privacy Policy");
        spannableString.setSpan(new myClickableSpan(1), 31, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new myClickableSpan(2), 41, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        activityPhoneVerificationBinding.termsAndConditions.setText(spannableString);
        activityPhoneVerificationBinding.termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        activityPhoneVerificationBinding.termsAndConditions.setBackgroundResource(R.drawable.blank);
    }

    public class myClickableSpan extends ClickableSpan {

        int pos;

        public myClickableSpan(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(@NotNull View widget) {

            /*if (pos == 1) {
                successSnackBar("Terms");
            } else if (pos == 2) {
                successSnackBar("Privacy Policy");
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void backButton() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void nextButton() {

        String phoneNumber = Objects.requireNonNull(activityPhoneVerificationBinding.etPhonenumber.getText()).toString().trim();
        phoneNumber = phoneNumber.replace(" ", "");

        if (!phoneViewModel.isPhoneValid(phoneNumber)) {
            vibrate();
            activityPhoneVerificationBinding.errorMessage.setText("No phone number provided. Please enter a phone number.");
            activityPhoneVerificationBinding.errorMessage.setVisibility(View.VISIBLE);
        } else if (phoneNumber.length() < 10) {
            vibrate();
            activityPhoneVerificationBinding.errorMessage.setText("Entered phone number is incorrect.");
            activityPhoneVerificationBinding.errorMessage.setVisibility(View.VISIBLE);
        } else {
            hideKeyboard();
            activityPhoneVerificationBinding.errorMessage.setVisibility(View.GONE);
            if (isNetworkConnected()) {
                Intent intent = new Intent(this, VerifyOTPActivity.class);
                intent.putExtra("MOBILE_NUMBER", "+91" + phoneNumber);
                startActivity(intent);
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

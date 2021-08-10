package com.sage.cabapp.ui.editphone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.androidnetworking.error.ANError;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityUpdateNumberBinding;
import com.sage.cabapp.twilio.sms.PhoneNumberVerifier;
import com.sage.cabapp.twilio.sms.PrefsHelper;
import com.sage.cabapp.twilio.sms.SMSApiHelper;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 10-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class UpdatePhoneActivity extends BaseActivity<ActivityUpdateNumberBinding,UpdatePhoneViewModel> implements UpdatePhoneNavigator,View.OnKeyListener, TextWatcher {

    @Inject
    ViewModelProviderFactory factory;
    UpdatePhoneViewModel updatePhoneViewModel;
    ActivityUpdateNumberBinding activityUpdateNumberBinding;
    
    
    private FirebaseAuth mAuth;
    private String verificationId;
    private String FORMAT = "%02d:%02d";
    private boolean enableResend = false;
    private String phone = "";

    private VerificationStatusChangeReceiver verificationReceiver;
    private PrefsHelper prefs;
    private String verifiedPhoneNo = "";
    private SMSApiHelper smsApiHelper;


    @Override
    public int getBindingVariable() {
        return BR.updateOTPViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_number;
    }

    @Override
    public UpdatePhoneViewModel getViewModel() {
        updatePhoneViewModel = ViewModelProviders.of(this,factory).get(UpdatePhoneViewModel.class);
        return updatePhoneViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUpdateNumberBinding = getViewDataBinding();
        updatePhoneViewModel.setNavigator(this);
        mAuth = FirebaseAuth.getInstance();

        verificationReceiver = new VerificationStatusChangeReceiver();
        smsApiHelper = new SMSApiHelper(this);

        prefs = getPrefs();
        prefs.removeVerified();
        prefs.removePhoneNumber();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PhoneNumberVerifier.ACTION_VERIFICATION_STATUS_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(verificationReceiver, intentFilter);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MOBILE_NUMBER")) {
            phone = intent.getStringExtra("MOBILE_NUMBER");
        }

        if (isNetworkConnected()) {

            PhoneNumberVerifier.startActionVerify(this, phone);

            activityUpdateNumberBinding.textOtpDesc.setText(String.format("Check your SMS message.\nWe have sent a code to %s", phone));

            new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    enableResend = false;
                    activityUpdateNumberBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                }

                @Override
                public void onFinish() {
                    enableResend = true;
                    activityUpdateNumberBinding.resendcode.setText("Resend code");
                }
            }.start();

            //sendVerificationCode(phone);
        }else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }

        setPINListeners();
    }

    private PrefsHelper getPrefs() {
        if (prefs == null) {
            prefs = new PrefsHelper(this);
        }
        return prefs;
    }

    class VerificationStatusChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (PhoneNumberVerifier.ACTION_VERIFICATION_STATUS_CHANGE.equals(intent.getAction())) {
                updateStatus();
            }
        }
    }

    protected void updateStatus() {

        hideLoading();
        boolean isVerified = prefs.getVerified(false);
        verifiedPhoneNo = prefs.getPhoneNumber(null);
        if (isVerified) {
            if (verifiedPhoneNo != null && !verifiedPhoneNo.equalsIgnoreCase("")) {
                Constant.showSuccessToast("Phone verification successful", getApplicationContext());
                if (isNetworkConnected()) {
                    try {
                        showLoading("");
                        updatePhoneViewModel.updateNumberWS(this,encodeBase64(phone));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (verificationReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(verificationReceiver);
        }
    }
    private void setPINListeners() {

        activityUpdateNumberBinding.pinHiddenEdittext.addTextChangedListener(this);

        activityUpdateNumberBinding.pinFirstEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });
        activityUpdateNumberBinding.pinSecondEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });
        activityUpdateNumberBinding.pinThirdEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });
        activityUpdateNumberBinding.pinForthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });
        activityUpdateNumberBinding.pinFifthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });
        activityUpdateNumberBinding.pinSixthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityUpdateNumberBinding.pinHiddenEdittext);
                showSoftKeyboard(activityUpdateNumberBinding.pinHiddenEdittext);
            }
        });

        activityUpdateNumberBinding.pinFirstEdittext.setOnKeyListener(this);
        activityUpdateNumberBinding.pinSecondEdittext.setOnKeyListener(this);
        activityUpdateNumberBinding.pinThirdEdittext.setOnKeyListener(this);
        activityUpdateNumberBinding.pinForthEdittext.setOnKeyListener(this);
        activityUpdateNumberBinding.pinFifthEdittext.setOnKeyListener(this);
        activityUpdateNumberBinding.pinSixthEdittext.setOnKeyListener(this);

        activityUpdateNumberBinding.pinHiddenEdittext.setOnKeyListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (Objects.requireNonNull(activityUpdateNumberBinding.pinHiddenEdittext.getText()).length() == 6)
                            activityUpdateNumberBinding.pinSixthEdittext.setText("");
                        else if (activityUpdateNumberBinding.pinHiddenEdittext.getText().length() == 5)
                            activityUpdateNumberBinding.pinFifthEdittext.setText("");
                        else if (Objects.requireNonNull(activityUpdateNumberBinding.pinHiddenEdittext.getText()).length() == 4)
                            activityUpdateNumberBinding.pinForthEdittext.setText("");
                        else if (activityUpdateNumberBinding.pinHiddenEdittext.getText().length() == 3)
                            activityUpdateNumberBinding.pinThirdEdittext.setText("");
                        else if (activityUpdateNumberBinding.pinHiddenEdittext.getText().length() == 2)
                            activityUpdateNumberBinding.pinSecondEdittext.setText("");
                        else if (activityUpdateNumberBinding.pinHiddenEdittext.getText().length() == 1)
                            activityUpdateNumberBinding.pinFirstEdittext.setText("");

                        if (activityUpdateNumberBinding.pinHiddenEdittext.length() > 0)
                            activityUpdateNumberBinding.pinHiddenEdittext.setText(activityUpdateNumberBinding.pinHiddenEdittext.getText().subSequence(0, activityUpdateNumberBinding.pinHiddenEdittext.length() - 1));

                        return true;
                    }
                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() == 0) {
            activityUpdateNumberBinding.pinFirstEdittext.setText("");
        } else if (s.length() == 1) {
            activityUpdateNumberBinding.pinFirstEdittext.setText(String.format("%s", s.charAt(0)));
            activityUpdateNumberBinding.pinSecondEdittext.setText("");
            activityUpdateNumberBinding.pinThirdEdittext.setText("");
            activityUpdateNumberBinding.pinForthEdittext.setText("");
            activityUpdateNumberBinding.pinFifthEdittext.setText("");
            activityUpdateNumberBinding.pinSixthEdittext.setText("");
        } else if (s.length() == 2) {

            activityUpdateNumberBinding.pinSecondEdittext.setText(String.format("%s", s.charAt(1)));
            activityUpdateNumberBinding.pinThirdEdittext.setText("");
            activityUpdateNumberBinding.pinForthEdittext.setText("");
            activityUpdateNumberBinding.pinFifthEdittext.setText("");
            activityUpdateNumberBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 3) {

            activityUpdateNumberBinding.pinThirdEdittext.setText(String.format("%s", s.charAt(2)));
            activityUpdateNumberBinding.pinForthEdittext.setText("");
            activityUpdateNumberBinding.pinFifthEdittext.setText("");
            activityUpdateNumberBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 4) {
            activityUpdateNumberBinding.pinForthEdittext.setText(String.format("%s", s.charAt(3)));
            activityUpdateNumberBinding.pinFifthEdittext.setText("");
            activityUpdateNumberBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 5) {
            activityUpdateNumberBinding.pinFifthEdittext.setText(String.format("%s", s.charAt(4)));
            activityUpdateNumberBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 6) {
            activityUpdateNumberBinding.pinSixthEdittext.setText(String.format("%s", s.charAt(5)));
            hideSoftKeyboard(activityUpdateNumberBinding.pinSixthEdittext);

            if (isNetworkConnected()) {
                String otp = Objects.requireNonNull(activityUpdateNumberBinding.pinHiddenEdittext.getText()).toString().trim();

                showLoading("");
                PhoneNumberVerifier.startActionVerifyMessage(this, otp);
                //verifyCode(otp);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
            }
        }
    }

    /**
     * Sets focus on a specific AppCompatEditText field.
     *
     * @param editText AppCompatEditText to set focus on
     */
    public static void setFocus(AppCompatEditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public void hideSoftKeyboard(AppCompatEditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(AppCompatEditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, 0);
        }
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
    public void updateNumber() {
        String otp = Objects.requireNonNull(activityUpdateNumberBinding.pinHiddenEdittext.getText()).toString().trim();

        if (!updatePhoneViewModel.isOTPValid(otp)) {
            vibrate();
            Constant.showErrorToast("Please enter OTP", getApplicationContext());
        } else if (otp.length() < 6) {
            vibrate();
            Constant.showErrorToast("Please enter correct OTP", getApplicationContext());
        }else {
            hideKeyboard();

            if (isNetworkConnected()) {

                showLoading("");
                PhoneNumberVerifier.startActionVerifyMessage(this, otp);
               // verifyCode(otp);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
            }
        }
    }

    @Override
    public void resendCode() {
        if (enableResend) {
            if (isNetworkConnected()) {

                activityUpdateNumberBinding.pinHiddenEdittext.setText("");
                activityUpdateNumberBinding.pinFirstEdittext.setText("");
                activityUpdateNumberBinding.pinSecondEdittext.setText("");
                activityUpdateNumberBinding.pinThirdEdittext.setText("");
                activityUpdateNumberBinding.pinForthEdittext.setText("");
                activityUpdateNumberBinding.pinFifthEdittext.setText("");
                activityUpdateNumberBinding.pinSixthEdittext.setText("");

                //sendVerificationCode(phone);

                PhoneNumberVerifier.stopActionVerify(this);
                prefs.removeVerified();
                prefs.removePhoneNumber();

                resendNumber(phone);
            }
        }
    }

    private void resendNumber(String phone) {
        smsApiHelper.reset(phone,
                new SMSApiHelper.ResetResponse() {
                    @Override
                    public void onResponse(boolean success) {
                        if (!success) {
                            Constant.showErrorToast(getString(R.string.toast_unable_to_reset), getApplicationContext());
                        }else {
                            new CountDownTimer(30000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    enableResend = false;
                                    activityUpdateNumberBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                                }

                                @Override
                                public void onFinish() {
                                    enableResend = true;
                                    activityUpdateNumberBinding.resendcode.setText("Resend code");
                                }
                            }.start();
                        }
                    }
                }, new SMSApiHelper.ApiError() {
                    @Override
                    public void onError(VolleyError error) {
                        // Do something else.
                        Constant.showErrorToast(getString(R.string.toast_something_wrong), getApplicationContext());
                    }
                });
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

    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    hideLoading();

                    if (task.isSuccessful()) {

                        Constant.showSuccessToast("Phone verification successful", getApplicationContext());
                        if (isNetworkConnected()) {
                            try {
                                showLoading("");
                                if (isNetworkConnected()) {

                                    showLoading("");
                                    updatePhoneViewModel.updateNumberWS(this, encodeBase64(phone));
                                } else {
                                    vibrate();
                                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            vibrate();
                            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
                        }
                    } else {

                        vibrate();
                        Constant.showErrorToast("Incorrect OTP : Please enter correct OTP or try again!", getApplicationContext());
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            hideKeyboard();
            hideLoading();
            verificationId = s;
            Constant.showSuccessToast("Code Sent", getApplicationContext());

            new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    enableResend = false;
                    activityUpdateNumberBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                }

                @Override
                public void onFinish() {
                    enableResend = true;
                    activityUpdateNumberBinding.resendcode.setText("Resend code");
                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            activityUpdateNumberBinding.pinHiddenEdittext.setText(code);

            hideLoading();

            if (isNetworkConnected()) {

                showLoading("");
                verifyCode(code);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            hideLoading();
            vibrate();
            Constant.showErrorToast(e.getMessage(), getApplicationContext());
        }
    };
}

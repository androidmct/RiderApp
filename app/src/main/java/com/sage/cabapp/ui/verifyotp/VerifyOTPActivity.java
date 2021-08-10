package com.sage.cabapp.ui.verifyotp;

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
import com.sage.cabapp.databinding.ActivityOtpVerificationBinding;
import com.sage.cabapp.twilio.sms.PhoneNumberVerifier;
import com.sage.cabapp.twilio.sms.PrefsHelper;
import com.sage.cabapp.twilio.sms.SMSApiHelper;
import com.sage.cabapp.ui.addemailsignup.AddEmailSignupActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainResponse;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class VerifyOTPActivity extends BaseActivity<ActivityOtpVerificationBinding, VerifyOTPViewModel> implements VerifyOTPNavigator, View.OnKeyListener, TextWatcher {

    @Inject
    ViewModelProviderFactory factory;
    private VerifyOTPViewModel verifyOTPViewModel;
    private ActivityOtpVerificationBinding activityOtpVerificationBinding;
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
        return BR.otpViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otp_verification;
    }

    @Override
    public VerifyOTPViewModel getViewModel() {

        verifyOTPViewModel = ViewModelProviders.of(this, factory).get(VerifyOTPViewModel.class);
        return verifyOTPViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpVerificationBinding = getViewDataBinding();
        verifyOTPViewModel.setNavigator(this);

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

            activityOtpVerificationBinding.textOtpDesc.setText(String.format("Check your SMS message.\nWe have sent a code to %s", phone));

            new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    enableResend = false;
                    activityOtpVerificationBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                }

                @Override
                public void onFinish() {
                    enableResend = true;
                    activityOtpVerificationBinding.resendcode.setText("Resend code");
                }
            }.start();
            // sendVerificationCode(phone);
        } else {
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
                        verifyOTPViewModel.checkPhoneWS(encodeBase64(phone));
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

        activityOtpVerificationBinding.pinHiddenEdittext.addTextChangedListener(this);

        activityOtpVerificationBinding.pinFirstEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });
        activityOtpVerificationBinding.pinSecondEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });
        activityOtpVerificationBinding.pinThirdEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });
        activityOtpVerificationBinding.pinForthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });
        activityOtpVerificationBinding.pinFifthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });
        activityOtpVerificationBinding.pinSixthEdittext.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setFocus(activityOtpVerificationBinding.pinHiddenEdittext);
                showSoftKeyboard(activityOtpVerificationBinding.pinHiddenEdittext);
            }
        });

        activityOtpVerificationBinding.pinFirstEdittext.setOnKeyListener(this);
        activityOtpVerificationBinding.pinSecondEdittext.setOnKeyListener(this);
        activityOtpVerificationBinding.pinThirdEdittext.setOnKeyListener(this);
        activityOtpVerificationBinding.pinForthEdittext.setOnKeyListener(this);
        activityOtpVerificationBinding.pinFifthEdittext.setOnKeyListener(this);
        activityOtpVerificationBinding.pinSixthEdittext.setOnKeyListener(this);

        activityOtpVerificationBinding.pinHiddenEdittext.setOnKeyListener(this);
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
                        if (Objects.requireNonNull(activityOtpVerificationBinding.pinHiddenEdittext.getText()).length() == 6)
                            activityOtpVerificationBinding.pinSixthEdittext.setText("");
                        else if (activityOtpVerificationBinding.pinHiddenEdittext.getText().length() == 5)
                            activityOtpVerificationBinding.pinFifthEdittext.setText("");
                        else if (Objects.requireNonNull(activityOtpVerificationBinding.pinHiddenEdittext.getText()).length() == 4)
                            activityOtpVerificationBinding.pinForthEdittext.setText("");
                        else if (activityOtpVerificationBinding.pinHiddenEdittext.getText().length() == 3)
                            activityOtpVerificationBinding.pinThirdEdittext.setText("");
                        else if (activityOtpVerificationBinding.pinHiddenEdittext.getText().length() == 2)
                            activityOtpVerificationBinding.pinSecondEdittext.setText("");
                        else if (activityOtpVerificationBinding.pinHiddenEdittext.getText().length() == 1)
                            activityOtpVerificationBinding.pinFirstEdittext.setText("");

                        if (activityOtpVerificationBinding.pinHiddenEdittext.length() > 0)
                            activityOtpVerificationBinding.pinHiddenEdittext.setText(activityOtpVerificationBinding.pinHiddenEdittext.getText().subSequence(0, activityOtpVerificationBinding.pinHiddenEdittext.length() - 1));

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
            activityOtpVerificationBinding.pinFirstEdittext.setText("");
        } else if (s.length() == 1) {
            activityOtpVerificationBinding.pinFirstEdittext.setText(String.format("%s", s.charAt(0)));
            activityOtpVerificationBinding.pinSecondEdittext.setText("");
            activityOtpVerificationBinding.pinThirdEdittext.setText("");
            activityOtpVerificationBinding.pinForthEdittext.setText("");
            activityOtpVerificationBinding.pinFifthEdittext.setText("");
            activityOtpVerificationBinding.pinSixthEdittext.setText("");
        } else if (s.length() == 2) {

            activityOtpVerificationBinding.pinSecondEdittext.setText(String.format("%s", s.charAt(1)));
            activityOtpVerificationBinding.pinThirdEdittext.setText("");
            activityOtpVerificationBinding.pinForthEdittext.setText("");
            activityOtpVerificationBinding.pinFifthEdittext.setText("");
            activityOtpVerificationBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 3) {

            activityOtpVerificationBinding.pinThirdEdittext.setText(String.format("%s", s.charAt(2)));
            activityOtpVerificationBinding.pinForthEdittext.setText("");
            activityOtpVerificationBinding.pinFifthEdittext.setText("");
            activityOtpVerificationBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 4) {
            activityOtpVerificationBinding.pinForthEdittext.setText(String.format("%s", s.charAt(3)));
            activityOtpVerificationBinding.pinFifthEdittext.setText("");
            activityOtpVerificationBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 5) {
            activityOtpVerificationBinding.pinFifthEdittext.setText(String.format("%s", s.charAt(4)));
            activityOtpVerificationBinding.pinSixthEdittext.setText("");

        } else if (s.length() == 6) {
            activityOtpVerificationBinding.pinSixthEdittext.setText(String.format("%s", s.charAt(5)));
            hideSoftKeyboard(activityOtpVerificationBinding.pinSixthEdittext);

            if (isNetworkConnected()) {
                String otp = Objects.requireNonNull(activityOtpVerificationBinding.pinHiddenEdittext.getText()).toString().trim();

                showLoading("");

                PhoneNumberVerifier.startActionVerifyMessage(this, otp);
                // verifyCode(otp);
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
    public void backButton() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void nextButton() {

        String otp = Objects.requireNonNull(activityOtpVerificationBinding.pinHiddenEdittext.getText()).toString().trim();

        if (!verifyOTPViewModel.isOTPValid(otp)) {
            vibrate();
            Constant.showErrorToast("Please enter OTP", getApplicationContext());
        } else if (otp.length() < 6) {
            vibrate();
            Constant.showErrorToast("Please enter correct OTP", getApplicationContext());
        } else {
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

                activityOtpVerificationBinding.pinHiddenEdittext.setText("");
                activityOtpVerificationBinding.pinFirstEdittext.setText("");
                activityOtpVerificationBinding.pinSecondEdittext.setText("");
                activityOtpVerificationBinding.pinThirdEdittext.setText("");
                activityOtpVerificationBinding.pinForthEdittext.setText("");
                activityOtpVerificationBinding.pinFifthEdittext.setText("");
                activityOtpVerificationBinding.pinSixthEdittext.setText("");

                PhoneNumberVerifier.stopActionVerify(this);
                prefs.removeVerified();
                prefs.removePhoneNumber();

                resendNumber(phone);
                //sendVerificationCode(phone);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
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
                        } else {
                            new CountDownTimer(30000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    enableResend = false;
                                    activityOtpVerificationBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                                }

                                @Override
                                public void onFinish() {
                                    enableResend = true;
                                    activityOtpVerificationBinding.resendcode.setText("Resend code");
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
    public void successAPI(CheckPhoneMainResponse checkPhoneMainResponse) {

        hideLoading();
        try {
            if (checkPhoneMainResponse.getStatus() != null && checkPhoneMainResponse.getStatus().equalsIgnoreCase("true")) {
                if (checkPhoneMainResponse.getData() != null && checkPhoneMainResponse.getData().size() > 0) {

                    SharedData.clearSharedPreference(this);

                    SharedData.saveString(this, Constant.PHONE_NUMBER, decodeBase64(checkPhoneMainResponse.getData().get(0).getUserMobile()));
                    SharedData.saveString(this, Constant.EMAIL, decodeBase64(checkPhoneMainResponse.getData().get(0).getUserEmail()));
                    SharedData.saveString(this, Constant.USERID, checkPhoneMainResponse.getData().get(0).getUserid());
                    SharedData.saveString(this, Constant.FIRST_NAME, checkPhoneMainResponse.getData().get(0).getFname());
                    SharedData.saveString(this, Constant.LAST_NAME, checkPhoneMainResponse.getData().get(0).getLname());

                    openActivity(this, HomeActivity.class, false, true);
                    slideRightToLeft();
                } else {

                    SharedData.saveString(this, Constant.TEMP_PHONE_NUMBER, phone);
                    openActivity(this, AddEmailSignupActivity.class, false, true);
                    slideRightToLeft();
                }
            } else {
                SharedData.saveString(this, Constant.TEMP_PHONE_NUMBER, phone);
                openActivity(this, AddEmailSignupActivity.class, false, true);
                slideRightToLeft();
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
                                verifyOTPViewModel.checkPhoneWS(encodeBase64(phone));
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
                    activityOtpVerificationBinding.resendcode.setText(String.format("Resend code in %s", String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))));
                }

                @Override
                public void onFinish() {
                    enableResend = true;
                    activityOtpVerificationBinding.resendcode.setText("Resend code");
                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            activityOtpVerificationBinding.pinHiddenEdittext.setText(code);

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

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }


}

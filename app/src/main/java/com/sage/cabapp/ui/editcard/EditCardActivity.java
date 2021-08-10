package com.sage.cabapp.ui.editcard;

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

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityEditCardBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.editcard.fragment.DeleteCardFragment;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardResponse;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.utils.Constant;

import java.util.Objects;
import java.util.StringTokenizer;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditCardActivity extends BaseActivity<ActivityEditCardBinding, EditCardViewModel> implements EditCardNavigator {

    @Inject
    ViewModelProviderFactory factory;
    EditCardViewModel editCardViewModel;
    ActivityEditCardBinding activityEditCardBinding;
    AllCardsData getAllPaymentsCardData = null;

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    @Override
    public int getBindingVariable() {
        return BR.editCardViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_card;
    }

    @Override
    public EditCardViewModel getViewModel() {
        editCardViewModel = ViewModelProviders.of(this, factory).get(EditCardViewModel.class);
        return editCardViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditCardBinding = getViewDataBinding();
        editCardViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CARD_DETAILS")) {
            getAllPaymentsCardData = (AllCardsData) intent.getSerializableExtra("CARD_DETAILS");
        }

        if (getAllPaymentsCardData != null) {
            activityEditCardBinding.etCardNumber.setText(String.format("•••• •••• •••• %s", getAllPaymentsCardData.getCard().getLast4()));

            String year = String.valueOf(getAllPaymentsCardData.getCard().getExpYear());
            String month = String.valueOf(getAllPaymentsCardData.getCard().getExpMonth());

            activityEditCardBinding.etCardDate.setText(String.format("%s/%s", month, year.substring(2)));

            String name = getAllPaymentsCardData.getBillingDetails().getName();

            if (name != null && !name.equalsIgnoreCase("")) {
                activityEditCardBinding.etCardholderName.setText(name);
            }

            activityEditCardBinding.etCardholderName.setCursorVisible(false);
            activityEditCardBinding.etCardholderName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);

            if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("visa")) {
                activityEditCardBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_visa), null, null, null);
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("mastercard")) {
                activityEditCardBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_master_card), null, null, null);
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("amex")) {
                activityEditCardBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_amex), null, null, null);
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("discover")) {
                activityEditCardBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_discover), null, null, null);
            } else {
                activityEditCardBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_method), null, null, null);
            }

            activityEditCardBinding.etCardNumber.setCursorVisible(false);
        } else {
            vibrate();
            Constant.showErrorToast("No card found", this);
            finish();
            slideLeftToRight();
        }

        addCardTextWatcher();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addCardTextWatcher() {

        activityEditCardBinding.etCardDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                    s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
                }
            }
        });

        activityEditCardBinding.etCardholderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityEditCardBinding.etCardholderName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityEditCardBinding.etCardholderName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityEditCardBinding.etCardholderName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityEditCardBinding.etCardholderName.getRight() - activityEditCardBinding.etCardholderName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityEditCardBinding.etCardholderName.setText("");
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

    private void submitCardToServer() {
        String cardName = Objects.requireNonNull(activityEditCardBinding.etCardholderName.getText()).toString().trim();
        String cardDate = Objects.requireNonNull(activityEditCardBinding.etCardDate.getText()).toString().trim();

        String delims = "/";
        StringTokenizer st = new StringTokenizer(cardDate, delims);
        String month = st.nextToken();
        String year = st.nextToken();

        editCardViewModel.updateCardWS(cardName, month, "20" + year, getAllPaymentsCardData.getId(), this);
    }

    void deleteCardUpdated() {

        Bundle bundle = new Bundle();
        bundle.putString("paymentId", getAllPaymentsCardData.getId());

        DeleteCardFragment deleteCardFragment = DeleteCardFragment.newInstance();
        deleteCardFragment.setArguments(bundle);
        deleteCardFragment.setCancelable(false);
        deleteCardFragment.show(getSupportFragmentManager(), "deletecard");
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
    public void saveCard() {
        String cardNumber = Objects.requireNonNull(activityEditCardBinding.etCardNumber.getText()).toString().trim();
        String cardName = Objects.requireNonNull(activityEditCardBinding.etCardholderName.getText()).toString().trim();
        String cardDate = Objects.requireNonNull(activityEditCardBinding.etCardDate.getText()).toString().trim();
        String cardCVV = Objects.requireNonNull(activityEditCardBinding.etCardCvv.getText()).toString().trim();

        if (!editCardViewModel.isEnterCardNumber(cardNumber)) {
            Constant.showErrorToast("Please enter card number", this);
        } else if (cardNumber.length() < 16) {
            Constant.showErrorToast("Entered card number is incorrect", this);
        } else if (!editCardViewModel.isEnterCardName(cardName)) {
            Constant.showErrorToast("Please enter card holder name", this);
        } else if (!editCardViewModel.isEnterCardDate(cardDate)) {
            Constant.showErrorToast("Please enter card expiry date", this);
        } else if (cardDate.length() < 5) {
            Constant.showErrorToast("Entered card expiry date is incorrect", this);
        } else if (!editCardViewModel.isEnterCardCVV(cardCVV)) {
            Constant.showErrorToast("Please enter card cvv", this);
        } else if (cardCVV.length() < 3) {
            Constant.showErrorToast("Entered cvv is incorrect", this);
        } else {
            hideKeyboard();
            if (isNetworkConnected()) {
                showLoading("");
                submitCardToServer();
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
            }
        }
    }

    @Override
    public void deleteCard() {
        deleteCardUpdated();
    }

    @Override
    public void successAPI(EditUpdateCardResponse response) {

        hideLoading();

        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                Constant.showSuccessToast(response.getMessage(), this);
                finish();
                slideLeftToRight();
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
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

package com.sage.cabapp.ui.howtopay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityHowToPayBinding;
import com.sage.cabapp.ui.addpaymentmethod.model.CardValidator;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
import com.sage.cabapp.ui.addpaymentmethod.model.CreditCard;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.howtopay.model.HowToPayMainResponse;
import com.sage.cabapp.ui.howtopay.model.PaypalTokenMainResponse;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.utils.Constant;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.CardBrand;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.card.payment.CardIOActivity;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HowToPayActivity extends BaseActivity<ActivityHowToPayBinding, HowToPayViewModel> implements HowToPayNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private HowToPayViewModel howToPayViewModel;
    private ActivityHowToPayBinding activityHowToPayBinding;

    private String currentText = "";

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;
    static final int REQUEST_CODE_SCAN_CARD = 100;
    String cardType = "";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWiCry6CMXUKUEPkfTi_esu90eAQor01CDelhVfYS9bSvuJTs__BgTDqmV2utDImXpCEGoktaTiIVg87";


    private static final int REQUEST_CODE_FUTURE_PAYMENT = 1122;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Sage")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    @Override
    public int getBindingVariable() {
        return BR.howToPayViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_how_to_pay;
    }

    @Override
    public HowToPayViewModel getViewModel() {
        howToPayViewModel = ViewModelProviders.of(this, factory).get(HowToPayViewModel.class);
        return howToPayViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHowToPayBinding = getViewDataBinding();
        howToPayViewModel.setNavigator(this);

        activityHowToPayBinding.cardTypeText.setText("Card Detail");

        activityHowToPayBinding.creditCardLayout.setVisibility(View.VISIBLE);
        addCardTextWatcher();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addCardTextWatcher() {

        activityHowToPayBinding.etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence source, int start, int before, int count) {
                source = source.toString().replace("-", "");

                if (source.length() > 0) {
                    CreditCard card = setCardIcon(source.toString());
                } else {
                    activityHowToPayBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                }

                if (!source.toString().equals(currentText)) {
                    source = source.toString().replace(" ", "");
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < source.length(); i++) {
                        if (i % 4 == 0 && i != 0) {
                            result.append(" ");
                        }

                        result.append(source.charAt(i));
                    }
                    currentText = result.toString();
                    activityHowToPayBinding.etCardNumber.setText(result.toString());
                    activityHowToPayBinding.etCardNumber.setSelection(currentText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activityHowToPayBinding.etCardDate.addTextChangedListener(new TextWatcher() {
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

        activityHowToPayBinding.etCardCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
                    s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
                }
            }
        });


        activityHowToPayBinding.etCardholderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    activityHowToPayBinding.etCardholderName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    activityHowToPayBinding.etCardholderName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_close_grey), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        activityHowToPayBinding.etCardholderName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityHowToPayBinding.etCardholderName.getRight() - activityHowToPayBinding.etCardholderName.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityHowToPayBinding.etCardholderName.setText("");
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

        activityHowToPayBinding.etCardNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    try {
                        if (event.getRawX() >= (activityHowToPayBinding.etCardNumber.getRight() - activityHowToPayBinding.etCardNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            activityHowToPayBinding.etCardNumber.setText("");
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

    private CreditCard setCardIcon(String source) {
        CreditCard card = new CardValidator(source).guessCard();

        InputFilter[] FilterArray = new InputFilter[1];
        if (card != null) {
            int maxLength = Integer.parseInt(String.valueOf(card.getMaxLength()));
            FilterArray[0] = new InputFilter.LengthFilter(getSpacedPanLength(maxLength));
            activityHowToPayBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, card.getDrawable()), null, null, null);
        } else {
            FilterArray[0] = new InputFilter.LengthFilter(getSpacedPanLength(19));
            activityHowToPayBinding.etCardNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.payment_ic_method), null, null, null);
        }

        activityHowToPayBinding.etCardNumber.setFilters(FilterArray);

        return card;
    }

    private int getSpacedPanLength(int length) {
        return (length % 4 == 0) ? length + (length / 4) - 1 : length + (length / 4);
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
    public void clickCreditCard() {
        activityHowToPayBinding.creditCard.setBackgroundResource(R.drawable.rounded_corner_colorprimary);
        activityHowToPayBinding.paypal.setBackgroundResource(R.drawable.rounded_corner_grey_white);
        activityHowToPayBinding.googlePay.setBackgroundResource(R.drawable.rounded_corner_grey_white);

        activityHowToPayBinding.creditCardIcon.setBackgroundResource(R.drawable.ic_credit_card_gray);
        activityHowToPayBinding.paypalIcon.setBackgroundResource(R.drawable.ic_paypal);
        activityHowToPayBinding.gpayIcon.setBackgroundResource(R.drawable.ic_google_pay);

        activityHowToPayBinding.cardTypeText.setText("Card Detail");

        activityHowToPayBinding.creditCardLayout.setVisibility(View.VISIBLE);
        activityHowToPayBinding.paypalLayout.setVisibility(View.GONE);
        activityHowToPayBinding.googlepayLayout.setVisibility(View.GONE);

    }

    @Override
    public void clickPaypal() {
        activityHowToPayBinding.paypal.setBackgroundResource(R.drawable.rounded_corner_colorprimary);
        activityHowToPayBinding.creditCard.setBackgroundResource(R.drawable.rounded_corner_grey_white);
        activityHowToPayBinding.googlePay.setBackgroundResource(R.drawable.rounded_corner_grey_white);

        activityHowToPayBinding.creditCardIcon.setBackgroundResource(R.drawable.ic_credit_card);
        activityHowToPayBinding.paypalIcon.setBackgroundResource(R.drawable.ic_paypal_grey);
        activityHowToPayBinding.gpayIcon.setBackgroundResource(R.drawable.ic_google_pay);

        activityHowToPayBinding.cardTypeText.setText("Login in to Paypal");

        activityHowToPayBinding.creditCardLayout.setVisibility(View.GONE);
        activityHowToPayBinding.paypalLayout.setVisibility(View.VISIBLE);
        activityHowToPayBinding.googlepayLayout.setVisibility(View.GONE);

    }

    @Override
    public void clickGooglePay() {
        activityHowToPayBinding.googlePay.setBackgroundResource(R.drawable.rounded_corner_colorprimary);
        activityHowToPayBinding.creditCard.setBackgroundResource(R.drawable.rounded_corner_grey_white);
        activityHowToPayBinding.paypal.setBackgroundResource(R.drawable.rounded_corner_grey_white);

        activityHowToPayBinding.creditCardIcon.setBackgroundResource(R.drawable.ic_credit_card);
        activityHowToPayBinding.paypalIcon.setBackgroundResource(R.drawable.ic_paypal);
        activityHowToPayBinding.gpayIcon.setBackgroundResource(R.drawable.ic_google_pay_grey);

        activityHowToPayBinding.cardTypeText.setText("Activation Google Pay");

        activityHowToPayBinding.creditCardLayout.setVisibility(View.GONE);
        activityHowToPayBinding.paypalLayout.setVisibility(View.GONE);
        activityHowToPayBinding.googlepayLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void submitCreditCard() {

        String cardNumber = Objects.requireNonNull(activityHowToPayBinding.etCardNumber.getText()).toString().trim();
        String cardName = Objects.requireNonNull(activityHowToPayBinding.etCardholderName.getText()).toString().trim();
        String cardDate = Objects.requireNonNull(activityHowToPayBinding.etCardDate.getText()).toString().trim();
        String cardCVV = Objects.requireNonNull(activityHowToPayBinding.etCardCvv.getText()).toString().trim();

        if (!howToPayViewModel.isEnterCardNumber(cardNumber)) {
            Constant.showErrorToast("Please enter card number", this);
        } else if (cardNumber.length() < 16) {
            Constant.showErrorToast("Entered card number is incorrect", this);
        } else if (!howToPayViewModel.isEnterCardName(cardName)) {
            Constant.showErrorToast("Please enter card holder name", this);
        } else if (!howToPayViewModel.isEnterCardDate(cardDate)) {
            Constant.showErrorToast("Please enter card expiry date", this);
        } else if (cardDate.length() < 5) {
            Constant.showErrorToast("Entered card expiry date is incorrect", this);
        } else if (!howToPayViewModel.isEnterCardCVV(cardCVV)) {
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

            /*if (isNetworkConnected()) {
                showLoading("");
                cardNumber = cardNumber.replace("-", "");
                String delims = "/";
                StringTokenizer st = new StringTokenizer(cardDate, delims);
                Integer month = Integer.valueOf(st.nextToken());
                Integer year = Integer.valueOf(st.nextToken());
                CreateToken(cardNumber, cardCVV, month, year, cardName);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }*/
        }
    }

    @Override
    public void submitPaypal() {

        hideKeyboard();
        Constant.showInfoToast("Coming soon", this);

       /* String paypalEmail = Objects.requireNonNull(activityHowToPayBinding.etPaypalEmail.getText()).toString().trim();
        String paypalPassword = Objects.requireNonNull(activityHowToPayBinding.etPaypalPassword.getText()).toString().trim();

        if (!howToPayViewModel.isEnterPaypalEmail(paypalEmail)) {
            Constant.showErrorToast("Please enter paypal email address", this);
        } else if (!howToPayViewModel.isPaypalEmailValid(paypalEmail)) {
            Constant.showErrorToast("Entered paypal email address is incorrect", this);
        } else if (!howToPayViewModel.isEnterPaypalPassword(paypalPassword)) {
            Constant.showErrorToast("Please enter paypal password", this);
        } else {
            hideKeyboard();
            Constant.showInfoToast("Coming soon", this);
            *//*if (isNetworkConnected()) {
                showLoading("");
                howToPayViewModel.howToPayPaypalWS(encodeBase64(paypalEmail), encodeBase64(paypalPassword), this);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }*//*
        }*/

       /* if (isNetworkConnected()) {
            onFuturePaymentPressed();
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
*/


    }

    @Override
    public void submitGooglePay() {

        hideKeyboard();
        Constant.showInfoToast("Coming soon", this);
        /*if (isNetworkConnected()) {
            showLoading("");
            howToPayViewModel.howToGooglePayWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }*/
    }

    @Override
    public void clickCardCamera() {
        Intent intent = new Intent(this, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true)
                .putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, true)
                .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD);
    }


    @Override
    public void successAPI(HowToPayMainResponse howToPayMainResponse) {

        hideLoading();
        try {
            if (howToPayMainResponse.getStatus() != null && howToPayMainResponse.getStatus().equalsIgnoreCase("true")) {
                if (isNetworkConnected()) {
                    howToPayViewModel.changePaymentMethodWS(this, "personal");
                }
            } else {
                vibrate();
                Constant.showErrorToast(howToPayMainResponse.getMessage(), this);
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

    @Override
    public void successDefaultMethodAPI(ChangeDefaultMethodResponse howToPayMainResponse) {
        try {
            if (howToPayMainResponse.getStatus() != null && howToPayMainResponse.getStatus().equalsIgnoreCase("true")) {
                openActivity(this, HomeActivity.class, false, true);
                slideRightToLeft();
            } else {
                vibrate();
                Constant.showErrorToast(howToPayMainResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successPaypalTokenAPI(PaypalTokenMainResponse howToPayMainResponse) {
        try {
            if (howToPayMainResponse.getStatus() != null && howToPayMainResponse.getStatus().equalsIgnoreCase("true")) {
                openActivity(this, HomeActivity.class, false, true);
                slideRightToLeft();
            } else {
                vibrate();
                Constant.showErrorToast(howToPayMainResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void CreateToken(String cardNumber, String cvv, Integer month, Integer year, String name) {

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("CardMultilineView");

        CardBrand cardBrand = CardBrand.Unknown;

        if (cardType.equalsIgnoreCase("VISA")) {
            cardBrand = CardBrand.Visa;
        } else if (cardType.equalsIgnoreCase("MASTERCARD")) {
            cardBrand = CardBrand.MasterCard;
        } else if (cardType.equalsIgnoreCase("AMEX")) {
            cardBrand = CardBrand.AmericanExpress;
        }

        Card card = new Card(cardNumber, cvv, month, year, name, "", "", "", "", "", "", "", "", "",
                cardBrand, "", "", "", "", "", "", "", arrayList, null, null);

        Stripe stripe = new Stripe(getApplicationContext(), getString(R.string.publishablekey));
        stripe.createCardToken(card, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                // Constant.showSuccessToast(token.getId(), getApplicationContext());
                hideLoading();

                if (isNetworkConnected()) {
                    showLoading("");
                    submitCardToServer();
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
                }
            }

            @Override
            public void onError(@NotNull Exception e) {
                Constant.showErrorToast(e.getMessage(), getApplicationContext());
                hideLoading();
            }
        });
    }

    private void submitCardToServer() {

        String cardNumber = Objects.requireNonNull(activityHowToPayBinding.etCardNumber.getText()).toString().trim();
        String cardName = Objects.requireNonNull(activityHowToPayBinding.etCardholderName.getText()).toString().trim();
        String cardDate = Objects.requireNonNull(activityHowToPayBinding.etCardDate.getText()).toString().trim();
        String cardCVV = Objects.requireNonNull(activityHowToPayBinding.etCardCvv.getText()).toString().trim();

        cardNumber = cardNumber.replace(" ", "");
        cardNumber = cardNumber.replace("-", "");

        String delims = "/";
        StringTokenizer st = new StringTokenizer(cardDate, delims);
        String month = st.nextToken();
        String year = st.nextToken();

        howToPayViewModel.howToPayCreditCardWS(cardNumber, cardName, month, "20" + year, cardCVV, cardType, this);
    }

    private SparseArray<Pattern> mCCPatterns = null;

    private void init() {
        if (mCCPatterns == null) {
            mCCPatterns = new SparseArray<>();
            // With spaces for credit card masking
           /* mCCPatterns.put(R.drawable.ic_visa_icon, Pattern.compile(
                    "^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(R.drawable.ic_mastercard_icon, Pattern.compile(
                    "^5[1-5][0-9]{1,14}$"));
            mCCPatterns.put(R.drawable.ic_visa_icon, Pattern.compile(
                    "^3[47][0-9]{1,13}$"));*/

            mCCPatterns.put(1, Pattern.compile(
                    "^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(2, Pattern.compile(
                    "^5[1-5][0-9]{1,14}$"));
            mCCPatterns.put(3, Pattern.compile(
                    "^3[47][0-9]{1,13}$"));
        }
    }

    public void onFuturePaymentPressed() {
        Intent intent = new Intent(HowToPayActivity.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_CARD && data != null
                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            io.card.payment.CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {

                activityHowToPayBinding.etCardNumber.setText(result.getFormattedCardNumber());
                activityHowToPayBinding.etCardCvv.setText(result.cvv);
                activityHowToPayBinding.etCardholderName.setText(result.cardholderName);
                if (result.expiryMonth < 10) {
                    activityHowToPayBinding.etCardDate.setText(String.format("0%d/%s", result.expiryMonth, String.valueOf(result.expiryYear).substring(2)));
                } else {
                    activityHowToPayBinding.etCardDate.setText(String.format("%d/%s", result.expiryMonth, String.valueOf(result.expiryYear).substring(2)));
                }
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {

                        String authorization_code = auth.getAuthorizationCode();
                        if (authorization_code != null && !authorization_code.equalsIgnoreCase("")) {
                            if (isNetworkConnected()) {
                                showLoading("");
                                howToPayViewModel.sendPaypalTokenWS(this, authorization_code);
                            } else {
                                vibrate();
                                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getApplicationContext());
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {

            }
        }
    }

    public void onFuturePaymentPurchasePressed() {
        // Get the Client Metadata ID from the SDK
        String metadataId = PayPalConfiguration.getClientMetadataId(this);

        Log.i("FuturePaymentExample", "Client Metadata ID: " + metadataId);

        // TODO: Send metadataId and transaction details to your server for processing with
        // PayPal...
    }

}

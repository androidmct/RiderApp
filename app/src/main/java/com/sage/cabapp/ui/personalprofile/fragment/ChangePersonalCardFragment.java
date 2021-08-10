package com.sage.cabapp.ui.personalprofile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.addbusinessprofile.model.CreateBusinessProfileResponse;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodRequest;
import com.sage.cabapp.ui.addpaymentmethod.model.ChangeDefaultMethodResponse;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesDatum;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesMainRequest;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesRequestData;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePersonalCardFragment extends BlurDialogMaroofFragment {

    private String paymentId = "";
    private String brand = "";

    public static ChangePersonalCardFragment newInstance() {
        ChangePersonalCardFragment fragment = new ChangePersonalCardFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_change_personal_card, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            paymentId = bundle.getString("paymentId");
            brand = bundle.getString("brand");
        }
    }

    @OnClick({R.id.yes_card, R.id.no_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yes_card:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    getPersonalProfileWS(getActivity(), paymentId, brand);
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            case R.id.no_card:
                dismiss();
                break;

            default:
                break;
        }
    }


    void getPersonalProfileWS(Context context, String paymentId, String brand) {

        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        ChangeCardPreferencesMainRequest changeCardPreferencesMainRequest = new ChangeCardPreferencesMainRequest();
        ChangeCardPreferencesRequestData changeCardPreferencesRequestData = new ChangeCardPreferencesRequestData();
        ChangeCardPreferencesDatum changeCardPreferencesDatum = new ChangeCardPreferencesDatum();

        changeCardPreferencesDatum.setUserid(userid);
        changeCardPreferencesDatum.setDevicetype("Android");
        changeCardPreferencesDatum.setPaymentId(paymentId);
        changeCardPreferencesDatum.setBrand(brand);

        changeCardPreferencesRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        changeCardPreferencesRequestData.setRequestType("paymentMethodActiveDeactive");
        changeCardPreferencesRequestData.setData(changeCardPreferencesDatum);

        changeCardPreferencesMainRequest.setRequestData(changeCardPreferencesRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(changeCardPreferencesMainRequest)
                .setTag("paymentMethodActiveDeactive")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ChangeCardPreferencesResponse.class, new ParsedRequestListener<ChangeCardPreferencesResponse>() {

                    @Override
                    public void onResponse(ChangeCardPreferencesResponse response) {
                        getDeleteCardSuccessAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    private void getDeleteCardSuccessAPI(ChangeCardPreferencesResponse response) {
        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                Constant.showSuccessToast(response.getMessage(), getActivity());

                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    createBusinessProfileWS(getActivity(), "personal");
                }
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createBusinessProfileWS(Context context, String defaultmethod) {

        String userid = SharedData.getString(context, Constant.USERID);

        ChangeDefaultMethodRequest changeDefaultMethodRequest = new ChangeDefaultMethodRequest();
        changeDefaultMethodRequest.setDefaultmethod(defaultmethod);
        changeDefaultMethodRequest.setUserid(userid);


        AndroidNetworking.post(ApiConstants.defaultCardType)
                .addApplicationJsonBody(changeDefaultMethodRequest)
                .setTag("defaultCardType")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(ChangeDefaultMethodResponse.class, new ParsedRequestListener<ChangeDefaultMethodResponse>() {

                    @Override
                    public void onResponse(ChangeDefaultMethodResponse response) {
                        successDefaultMethodAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    public void successDefaultMethodAPI(ChangeDefaultMethodResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                dismiss();
                Objects.requireNonNull(getActivity()).finish();
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(anError.getMessage(), getActivity());
    }
}

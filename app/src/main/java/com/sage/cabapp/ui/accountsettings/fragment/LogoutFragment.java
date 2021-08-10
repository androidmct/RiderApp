package com.sage.cabapp.ui.accountsettings.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.manojbhadane.QButton;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateDatum;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateMainRequest;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateRequestData;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.splash.SplashActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maroof Ahmed Siddique on 08-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class LogoutFragment extends BlurDialogMaroofFragment {

    @BindView(R.id.logout_no)
    AppCompatTextView logout_no;

    @BindView(R.id.logout_yes)
    QButton logout_yes;

    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_logout, container, false);

        ButterKnife.bind(this, view);
        return view;
    }



    @OnClick({R.id.logout_yes, R.id.logout_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_yes:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    logoutWS(getActivity());
                } else {
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            case R.id.logout_no:
                dismiss();
                break;

            default:
                break;
        }
    }

    void logoutWS(Context context) {


        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        ProfileUpdateMainRequest profileUpdateMainRequest = new ProfileUpdateMainRequest();
        ProfileUpdateRequestData profileUpdateRequestData = new ProfileUpdateRequestData();
        ProfileUpdateDatum profileUpdateDatum = new ProfileUpdateDatum();

        profileUpdateDatum.setUserid(userid);
        profileUpdateDatum.setDevicetype("Android");

        profileUpdateRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        profileUpdateRequestData.setRequestType("riderlogoutRider");
        profileUpdateRequestData.setData(profileUpdateDatum);

        profileUpdateMainRequest.setRequestData(profileUpdateRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(profileUpdateMainRequest)
                .setTag("riderlogoutRider")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ProfileUpdateResponse.class, new ParsedRequestListener<ProfileUpdateResponse>() {

                    @Override
                    public void onResponse(ProfileUpdateResponse response) {
                        successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    private void successAPI(ProfileUpdateResponse response) {

        hideLoading();

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                dismiss();
                SharedData.clearSharedPreference(Objects.requireNonNull(getActivity()));
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
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

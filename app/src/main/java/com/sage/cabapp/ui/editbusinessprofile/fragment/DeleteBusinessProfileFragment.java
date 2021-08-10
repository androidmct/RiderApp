package com.sage.cabapp.ui.editbusinessprofile.fragment;

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
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.editbusinessprofile.model.DeleteBusinessProfileDatum;
import com.sage.cabapp.ui.editbusinessprofile.model.DeleteBusinessProfileMainRequest;
import com.sage.cabapp.ui.editbusinessprofile.model.DeleteBusinessProfileRequestData;
import com.sage.cabapp.ui.editbusinessprofile.model.DeleteBusinessProfileResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteBusinessProfileFragment extends BlurDialogMaroofFragment {

    public static DeleteBusinessProfileFragment newInstance() {
        DeleteBusinessProfileFragment fragment = new DeleteBusinessProfileFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_delete_business_profile, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.delete_yes,R.id.delete_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_yes:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    deleteBusinessProfileWS(getActivity());
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            case R.id.delete_no:
                dismiss();
                break;

            default:
                break;
        }
    }

    void deleteBusinessProfileWS(Context context) {

        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        DeleteBusinessProfileMainRequest deleteBusinessProfileMainRequest = new DeleteBusinessProfileMainRequest();
        DeleteBusinessProfileRequestData deleteBusinessProfileRequestData = new DeleteBusinessProfileRequestData();
        DeleteBusinessProfileDatum deleteBusinessProfileDatum = new DeleteBusinessProfileDatum();

        deleteBusinessProfileDatum.setUserid(userid);
        deleteBusinessProfileDatum.setDevicetype("Android");

        deleteBusinessProfileRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        deleteBusinessProfileRequestData.setRequestType("removeBusinessCard");
        deleteBusinessProfileRequestData.setData(deleteBusinessProfileDatum);

        deleteBusinessProfileMainRequest.setRequestData(deleteBusinessProfileRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(deleteBusinessProfileMainRequest)
                .setTag("removeBusinessCard")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DeleteBusinessProfileResponse.class, new ParsedRequestListener<DeleteBusinessProfileResponse>() {

                    @Override
                    public void onResponse(DeleteBusinessProfileResponse response) {
                        getDeleteCardSuccessAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    private void getDeleteCardSuccessAPI(DeleteBusinessProfileResponse response) {
        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                dismiss();

                Constant.showSuccessToast(response.getMessage(), getActivity());
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
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), getActivity());
    }
}

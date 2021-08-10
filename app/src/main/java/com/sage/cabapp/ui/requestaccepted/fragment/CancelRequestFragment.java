package com.sage.cabapp.ui.requestaccepted.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageDatum;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageMainRequest;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageRequestData;
import com.sage.cabapp.ui.requestforcar.model.CancelSageResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelRequestFragment extends BlurDialogMaroofFragment {

    public static CancelRequestFragment newInstance() {
        CancelRequestFragment fragment = new CancelRequestFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_cancel_request, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.cancel,R.id.dont_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    cancelRequestedWS();
                } else {
                    Constant.showErrorToast(getActivity().getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            case R.id.dont_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }

    void cancelRequestedWS() {

        showLoading("");

        String requestID = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.TEMP_REQUEST_ID);
        String userid = SharedData.getString(getActivity(), Constant.USERID);

        CancelSageMainRequest cancelSageMainRequest = new CancelSageMainRequest();
        CancelSageRequestData cancelSageRequestData = new CancelSageRequestData();
        CancelSageDatum cancelSageDatum = new CancelSageDatum();

        cancelSageDatum.setRequestId(requestID);
        cancelSageDatum.setUserid(userid);
        cancelSageDatum.setRideSatus("RC");
        cancelSageDatum.setDevicetype("Android");

        cancelSageRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        cancelSageRequestData.setRequestType("riderDeclineRequest");
        cancelSageRequestData.setData(cancelSageDatum);

        cancelSageMainRequest.setRequestData(cancelSageRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(cancelSageMainRequest)
                .setTag("riderDeclineRequest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CancelSageResponse.class, new ParsedRequestListener<CancelSageResponse>() {

                    @Override
                    public void onResponse(CancelSageResponse response) {
                        cancelSage(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });
    }

    private void cancelSage(CancelSageResponse response) {

        hideLoading();

        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")){

                Constant.showSuccessToast(response.getMessage(),getActivity());
                dismiss();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
            }else {
                vibrate();
                Constant.showErrorToast(response.getMessage(),getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void errorAPI(ANError anError) {

        vibrate();
        hideLoading();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), getActivity());
    }

}

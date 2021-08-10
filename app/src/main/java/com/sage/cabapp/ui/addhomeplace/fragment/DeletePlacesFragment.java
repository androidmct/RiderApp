package com.sage.cabapp.ui.addhomeplace.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.addhomeplace.model.DeletePlacesDatum;
import com.sage.cabapp.ui.addhomeplace.model.DeletePlacesMainRequest;
import com.sage.cabapp.ui.addhomeplace.model.DeletePlacesRequestData;
import com.sage.cabapp.ui.addhomeplace.model.DeletePlacesResponse;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maroof Ahmed Siddique on 08-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class DeletePlacesFragment extends BlurDialogMaroofFragment {

    private String placeType = "";

    public static DeletePlacesFragment newInstance() {
        DeletePlacesFragment fragment = new DeletePlacesFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_delete_places, container, false);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            placeType = bundle.getString("placeType");
        }
    }

    @OnClick({R.id.delete_yes, R.id.delete_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_yes:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    deletePlaceWS(getActivity());
                } else {
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

    void deletePlaceWS(Context context) {

        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        DeletePlacesMainRequest deletePlacesMainRequest = new DeletePlacesMainRequest();
        DeletePlacesRequestData deletePlacesRequestData = new DeletePlacesRequestData();
        DeletePlacesDatum deletePlacesDatum = new DeletePlacesDatum();

        deletePlacesDatum.setType(placeType);
        deletePlacesDatum.setUserid(userid);
        deletePlacesDatum.setDevicetype("Android");

        deletePlacesRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        deletePlacesRequestData.setRequestType("deletePlacesStatus");
        deletePlacesRequestData.setData(deletePlacesDatum);

        deletePlacesMainRequest.setRequestData(deletePlacesRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(deletePlacesMainRequest)
                .setTag("deletePlacesStatus")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DeletePlacesResponse.class, new ParsedRequestListener<DeletePlacesResponse>() {

                    @Override
                    public void onResponse(DeletePlacesResponse response) {
                        successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    private void successAPI(DeletePlacesResponse response) {

        hideLoading();

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                dismiss();
                Objects.requireNonNull(getActivity()).finish();
                getActivity().overridePendingTransition(R.anim.enter_left,R.anim.exit_right);
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

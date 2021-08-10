package com.sage.cabapp.ui.updateroute.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedActivity;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsDatum;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsMainRequest;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoryDetailsRequestData;
import com.sage.cabapp.ui.tripreceipt.model.TripHistoyDetailsResponse;
import com.sage.cabapp.ui.updateroute.model.UpdateRouteDatum;
import com.sage.cabapp.ui.updateroute.model.UpdateRouteMainRequest;
import com.sage.cabapp.ui.updateroute.model.UpdateRouteRequestData;
import com.sage.cabapp.ui.updateroute.model.UpdateRouteResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateRouteFragment extends BlurDialogMaroofFragment {


    private LatLng sourceAddressLatLng = null;
    private LatLng destinationAddressLatLng = null;
    private LatLng addStopAddressLatLng = null;
    String source = "";
    String destination = "";
    String addstop = "";
    String requestID = "";

    public static UpdateRouteFragment newInstance() {
        UpdateRouteFragment fragment = new UpdateRouteFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            source = bundle.getString("SOURCE_ADD");
            destination = bundle.getString("DEST_ADD");
            addstop = bundle.getString("ADDSTOP_ADD");
            requestID = bundle.getString("requestID");
            sourceAddressLatLng = bundle.getParcelable("SOURCE_LATLNG");
            destinationAddressLatLng = bundle.getParcelable("DEST_LATLNG");
            addStopAddressLatLng = bundle.getParcelable("ADDSTOP_LATLNG");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_update_route, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.cancel, R.id.update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;

            case R.id.update:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    updateRouteWS(getActivity(), requestID);
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            default:
                break;
        }
    }

    void updateRouteWS(Context context, String requestID) {

        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        UpdateRouteMainRequest updateRouteMainRequest = new UpdateRouteMainRequest();
        UpdateRouteRequestData updateRouteRequestData = new UpdateRouteRequestData();
        UpdateRouteDatum updateRouteDatum = new UpdateRouteDatum();

        updateRouteDatum.setDevicetype("Android");
        updateRouteDatum.setUserid(userid);
        updateRouteDatum.setRequestId(requestID);
        updateRouteDatum.setDAddress(destination);
        if (destinationAddressLatLng != null) {
            updateRouteDatum.setDLat(String.valueOf(destinationAddressLatLng.latitude));
            updateRouteDatum.setDLng(String.valueOf(destinationAddressLatLng.longitude));
        } else {
            updateRouteDatum.setDLat("");
            updateRouteDatum.setDLng("");
        }
        updateRouteDatum.setExAddress(addstop);
        if (addStopAddressLatLng != null) {
            updateRouteDatum.setExLat(String.valueOf(addStopAddressLatLng.latitude));
            updateRouteDatum.setExLng(String.valueOf(addStopAddressLatLng.longitude));
        } else {
            updateRouteDatum.setExLat("");
            updateRouteDatum.setExLng("");
        }

        updateRouteRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        updateRouteRequestData.setRequestType("riderRequestUpdate");
        updateRouteRequestData.setData(updateRouteDatum);

        updateRouteMainRequest.setRequestData(updateRouteRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(updateRouteMainRequest)
                .setTag("riderRequestUpdate")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(UpdateRouteResponse.class, new ParsedRequestListener<UpdateRouteResponse>() {

                    @Override
                    public void onResponse(UpdateRouteResponse response) {
                        successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });
    }

    private void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
    }

    private void successAPI(UpdateRouteResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                dismiss();
                Intent intent = new Intent(getActivity(), RequestAcceptedActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finishAffinity();
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

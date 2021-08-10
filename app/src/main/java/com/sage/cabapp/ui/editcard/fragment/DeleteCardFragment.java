package com.sage.cabapp.ui.editcard.fragment;

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
import com.sage.cabapp.ui.editcard.model.DeleteCardDatum;
import com.sage.cabapp.ui.editcard.model.DeleteCardMainRequest;
import com.sage.cabapp.ui.editcard.model.DeleteCardRequestData;
import com.sage.cabapp.ui.editcard.model.DeleteCardResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteCardFragment extends BlurDialogMaroofFragment {

    private String paymentId = "";

    public static DeleteCardFragment newInstance() {
        DeleteCardFragment fragment = new DeleteCardFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_delete_card, container, false);

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
        }
    }

    @OnClick({R.id.yes_card, R.id.no_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yes_card:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    deletePaymentsCardWS(getActivity());
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


    void deletePaymentsCardWS(Context context) {

        showLoading("");

        String userid = SharedData.getString(context, Constant.USERID);

        DeleteCardMainRequest deleteCardMainRequest = new DeleteCardMainRequest();
        DeleteCardRequestData deleteCardRequestData = new DeleteCardRequestData();
        DeleteCardDatum deleteCardDatum = new DeleteCardDatum();

        deleteCardDatum.setUserid(userid);
        deleteCardDatum.setPaymentId(paymentId);
        deleteCardDatum.setDevicetype("Android");

        deleteCardRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        deleteCardRequestData.setRequestType("DeleteStripeCard");
        deleteCardRequestData.setData(deleteCardDatum);

        deleteCardMainRequest.setRequestData(deleteCardRequestData);

        AndroidNetworking.post(ApiConstants.paymentMethod)
                .addApplicationJsonBody(deleteCardMainRequest)
                .setTag("DeleteStripeCard")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DeleteCardResponse.class, new ParsedRequestListener<DeleteCardResponse>() {

                    @Override
                    public void onResponse(DeleteCardResponse response) {
                        getDeleteCardSuccessAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });

    }

    private void getDeleteCardSuccessAPI(DeleteCardResponse response) {
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

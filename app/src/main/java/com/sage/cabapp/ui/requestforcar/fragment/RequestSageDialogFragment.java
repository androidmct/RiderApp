package com.sage.cabapp.ui.requestforcar.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.manojbhadane.QButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedActivity;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageDatum;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageMainRequest;
import com.sage.cabapp.ui.requestaccepted.model.CancelSageRequestData;
import com.sage.cabapp.ui.requestforcar.model.CancelSageResponse;
import com.sage.cabapp.ui.requestforcar.model.CheckSageDatum;
import com.sage.cabapp.ui.requestforcar.model.CheckSageMainRequest;
import com.sage.cabapp.ui.requestforcar.model.CheckSageRequestData;
import com.sage.cabapp.ui.requestforcar.model.CheckSageResponse;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentDatum;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentMainRequest;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentRequestData;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.ui.requestforcar.model.SocketDatum;
import com.sage.cabapp.ui.requestforcar.model.SocketMainRequest;
import com.sage.cabapp.ui.requestforcar.model.SocketMainResponse;
import com.sage.cabapp.ui.requestforcar.model.SocketRequestData;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;
import com.sage.cabapp.utils.SharedData;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maroof Ahmed Siddique on 18-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class RequestSageDialogFragment extends BlurDialogMaroofFragment {

    public static RequestSageDialogFragment newInstance() {
        RequestSageDialogFragment fragment = new RequestSageDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    ArrayList<String> driverList = new ArrayList<>();
    int timeout = 0;
    int maxtimeout = 300 * 1000;

    @BindView(R.id.circularProgressBar)
    CircularProgressBar circularProgressBar;

    @BindView(R.id.cancel)
    QButton cancel;

    @BindView(R.id.progress_percentage)
    AppCompatTextView progress_percentage;

    @BindView(R.id.text_driver)
    AppCompatTextView text_driver;

    @BindView(R.id.first_view)
    RelativeLayout first_view;

    @BindView(R.id.second_view)
    AppCompatImageView second_view;

    @BindView(R.id.third_view)
    AppCompatImageView third_view;

    int i = 1;
    int nextPosition = 0;

    // implement either onCreateView or onCreateDialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_request_sage, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            driverList = bundle.getStringArrayList("drivers");
            timeout = bundle.getInt("timeout");
            timeout = timeout * 1000;
            maxtimeout = timeout * driverList.size();
        }

        setTimer();
        sendAnotherDriverTimer();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
            checkSageStatusWS();
        } else {
            Constant.showErrorToast(getActivity().getResources().getString(R.string.internet_not_available), getActivity());
        }
    }

    @OnClick({R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                    cancelRequestedWS();
                } else {
                    Constant.showErrorToast(getActivity().getResources().getString(R.string.internet_not_available), getActivity());
                }
                break;

            default:
                break;
        }
    }

    private CountDownTimer waitingTimer = null;

    private void setTimer() {

        i = 1;
        int countInterval = maxtimeout / 100;

        waitingTimer = new CountDownTimer(maxtimeout, countInterval) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                i = i + 1;
                circularProgressBar.setProgress(i);
                progress_percentage.setText(String.format("%d", i));
            }

            @Override
            public void onFinish() {

                this.cancel();
                dismiss();
            }
        }.start();
    }

    private void startAnimation(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);
        view.setScaleX(0.f);
        view.setScaleY(0.f);
        view.animate()
                .alpha(1.f)
                .scaleX(1.f).scaleY(1.f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        new CountDownTimer(1000, 200) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                second_view.setVisibility(View.GONE);
                                startAnimationAgain(third_view);
                                this.cancel();

                            }
                        }.start();


                    }
                })
                .start();
    }

    private void startAnimationAgain(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);
        view.setScaleX(0.f);
        view.setScaleY(0.f);
        view.animate()
                .alpha(1.f)
                .scaleX(1.f).scaleY(1.f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        new CountDownTimer(1000, 200) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                this.cancel();

                                String requestID = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.TEMP_REQUEST_ID);
                                SharedData.saveString(getActivity(), Constant.PERM_REQUEST_ID, requestID);

                                Intent intent = new Intent(getActivity(), RequestAcceptedActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
                                getActivity().finishAffinity();
                            }
                        }.start();


                    }
                })
                .start();
    }

    void checkSageStatusWS() {

        String requestID = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.TEMP_REQUEST_ID);

        CheckSageMainRequest checkSageMainRequest = new CheckSageMainRequest();
        CheckSageRequestData checkSageRequestData = new CheckSageRequestData();
        CheckSageDatum acceptSageDatum = new CheckSageDatum();

        acceptSageDatum.setRequestId(requestID);
        acceptSageDatum.setDevicetype("Android");

        checkSageRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        checkSageRequestData.setRequestType("rideRequestStatusCheck");
        checkSageRequestData.setData(acceptSageDatum);

        checkSageMainRequest.setRequestData(checkSageRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(checkSageMainRequest)
                .setTag("rideRequestStatusCheck")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CheckSageResponse.class, new ParsedRequestListener<CheckSageResponse>() {

                    @Override
                    public void onResponse(CheckSageResponse response) {
                        sageStatusResponse(response);
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
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), getActivity());
    }

    private void sageStatusResponse(CheckSageResponse response) {

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (waitingTimer != null) {
                    waitingTimer.cancel();
                    waitingTimer = null;

                    circularProgressBar.setProgress(100);
                    progress_percentage.setText(String.format("%d", 100));
                    first_view.setVisibility(View.GONE);
                    startAnimation(second_view);
                    text_driver.setText("We got a driver for you!");
                    cancel.setVisibility(View.GONE);
                }
            } else {
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        this.cancel();
                        if (isAdded() && isVisible()) {
                            if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
                                checkSageStatusWS();
                            }
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                Constant.showSuccessToast(response.getMessage(), getActivity());
                dismiss();
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void socketRequestWS(Context context) {

        String driverID = "";
        try {
            driverID = driverList.get(nextPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (driverID != null && !driverID.equalsIgnoreCase("")) {
            String userid = SharedData.getString(context, Constant.USERID);
            String firstName = SharedData.getString(context, Constant.FIRST_NAME);
            String requestID = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.TEMP_REQUEST_ID);

            SocketMainRequest socketMainRequest = new SocketMainRequest();
            SocketRequestData socketRequestData = new SocketRequestData();
            SocketDatum socketDatum = new SocketDatum();

            socketDatum.setRiderId(userid);
            socketDatum.setRequestId(requestID);
            socketDatum.setStatus("0");
            socketDatum.setDriverId(driverID);
            socketDatum.setRider_name(firstName);
            socketDatum.setDeviceType("Android");

            socketRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
            socketRequestData.setRequestType("checkRideAccepted");
            socketRequestData.setData(socketDatum);

            socketMainRequest.setRequestData(socketRequestData);

            AndroidNetworking.post(ApiConstants.RequestSage)
                    .addApplicationJsonBody(socketMainRequest)
                    .setTag("checkRideAccepted")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(SocketMainResponse.class, new ParsedRequestListener<SocketMainResponse>() {

                        @Override
                        public void onResponse(SocketMainResponse response) {
                            successSentAnotherRequestAPI(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            errorAPI(anError);
                        }
                    });
        }
    }

    private void successSentAnotherRequestAPI(SocketMainResponse response) {

        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                if (response.getAccepted() == 0) {
                    nextPosition = nextPosition + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAnotherDriverTimer() {

        if (NetworkAvailability.checkNetworkStatus(Objects.requireNonNull(getActivity()))) {
            socketRequestWS(getActivity());
        }
        new CountDownTimer(timeout, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                this.cancel();
                if (isAdded() && isVisible()) {
                    sendAnotherDriverTimer();
                }
            }
        }.start();
    }

}

package com.sage.cabapp.ui.splash;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.JsonIOException;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivitySplashBinding;
import com.sage.cabapp.twilio.sms.AppSignatureHelper;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedActivity;
import com.sage.cabapp.ui.splash.model.HomeApiDatum;
import com.sage.cabapp.ui.splash.model.HomeApiMainRequest;
import com.sage.cabapp.ui.splash.model.HomeApiRequestData;
import com.sage.cabapp.ui.splash.model.HomeApiResponse;
import com.sage.cabapp.ui.tutorial.TutorialActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.branch.referral.Branch;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Maroof Ahmed Siddique on 15-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private SplashViewModel splashViewModel;
    private ActivitySplashBinding activitySplashBinding;

    @Override
    public int getBindingVariable() {
        return BR.splashViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        splashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel.class);
        return splashViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySplashBinding = getViewDataBinding();
        splashViewModel.setNavigator(this);

        AppCenter.start(getApplication(), "4cc59ffc-4bfa-4e31-927c-31501cf17b71",
                Analytics.class, Crashes.class);

        SharedData.saveBool(this, Constant.NO_CARS, false);
        String userid = SharedData.getString(SplashActivity.this, Constant.USERID);

        Branch.getAutoInstance(this);
        Branch.getInstance().initSession((referringParams, error) -> {
            if (error == null) {
                String referral_code = "";
                try {
                    if (referringParams != null) {
                        if (referringParams.has("~stage")) {
                            referral_code = referringParams.getString("~stage");
                        }
                        SharedData.saveString(getApplicationContext(), Constant.REFERRAL_CODE, referral_code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, this.getIntent().getData(), this);

        clearAllNotifications();

        AppSignatureHelper signatureHelper = new AppSignatureHelper(this);
        Log.d("HASH KEY MIL GAYI", signatureHelper.getAppSignatures().get(0));

        ArrayList<String> appSignatures = signatureHelper.getAppSignatures();
        if (!appSignatures.isEmpty()) {
            Log.d("HASH KEY MIL GAYI", appSignatures.get(0));
        }

        if (userid != null && !userid.equalsIgnoreCase("")) {

            SharedData.saveString(this, Constant.DRIVER_USERID, "");
            SharedData.saveString(this, Constant.DRIVER_NAME, "");
            SharedData.saveString(this, Constant.DRIVER_PIC, "");

            if (isNetworkConnected()) {
                checkRiderStatusWS();
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                doSomeWork();
            }
        } else {
            doSomeWork();
        }
    }

    @Override
    public void leaveSplash(boolean bool) {

        if (bool) {
            passToMain();
            innToOut();
        }
    }

    private void doSomeWork() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<? extends Long> getObservable() {
        return Observable.timer(1, TimeUnit.SECONDS);
    }

    private Observer<Long> getObserver() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                splashViewModel.leaveSplash();
            }
        };
    }

    void checkRiderStatusWS() {

        String userid = SharedData.getString(this, Constant.USERID);

        HomeApiMainRequest homeApiMainRequest = new HomeApiMainRequest();
        HomeApiRequestData homeApiRequestData = new HomeApiRequestData();
        HomeApiDatum homeApiDatum = new HomeApiDatum();

        homeApiDatum.setDevicetype("Android");
        homeApiDatum.setUserid(userid);

        homeApiRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        homeApiRequestData.setRequestType("riderUserScreenHome");
        homeApiRequestData.setData(homeApiDatum);

        homeApiMainRequest.setRequestData(homeApiRequestData);

        AndroidNetworking.post(ApiConstants.RequestSage)
                .addApplicationJsonBody(homeApiMainRequest)
                .setTag("riderUserScreenHome")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(HomeApiResponse.class, new ParsedRequestListener<HomeApiResponse>() {

                    @Override
                    public void onResponse(HomeApiResponse response) {
                        successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });
    }

    private void errorAPI(ANError anError) {

        passToMain();
        innToOut();
    }

    private void successAPI(HomeApiResponse response) {

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getRequestId() != null && !response.getRequestId().equalsIgnoreCase("")) {
                    SharedData.saveString(this, Constant.PERM_REQUEST_ID, response.getRequestId());
                    SharedData.saveString(this, Constant.PERM_REQUEST_STATUS, response.getRideStatus());
                    SharedData.saveString(this, Constant.PERM_REQUEST_CURRENT_STATUS, response.getCurrentStatus());

                    String status = response.getRideStatus();
                    String currentStatus = response.getCurrentStatus();

                    if (status.equalsIgnoreCase("start")) {
                        openActivity(SplashActivity.this, RequestAcceptedActivity.class, false, true);
                        SharedData.saveBool(this, Constant.RIDE_STARTED, true);
                    } else if (status.equalsIgnoreCase("accept")) {
                        openActivity(SplashActivity.this, RequestAcceptedActivity.class, false, true);
                        SharedData.saveBool(this, Constant.RIDE_STARTED, false);
                    } else {
                        passToMain();
                    }

                    innToOut();
                } else {
                    passToMain();

                    innToOut();
                }
            } else {
                passToMain();

                innToOut();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void passToMain() {
        String userid = SharedData.getString(SplashActivity.this, Constant.USERID);

        if (userid != null && !userid.equalsIgnoreCase("")) {

            openActivity(SplashActivity.this, HomeActivity.class, false, true);
            // openActivity(SplashActivity.this, ChatModuleActivity.class, false, true);
        } else {
            openActivity(SplashActivity.this, TutorialActivity.class, false, true);
        }
    }
}

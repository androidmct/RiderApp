package com.sage.cabapp.ui.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityTutorialBinding;
import com.sage.cabapp.ui.addemailsignup.AddEmailSignupActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.phoneverification.PhoneVerificationActivity;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneDatum;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainRequest;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneMainResponse;
import com.sage.cabapp.ui.verifyotp.model.CheckPhoneRequestData;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class TutorialActivity extends BaseActivity<ActivityTutorialBinding, TutorialViewModel> implements TutorialNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private TutorialViewModel tutorialViewModel;
    private ActivityTutorialBinding activityTutorialBinding;

    private MyViewPagerAdapter myViewPagerAdapter;
    private AppCompatTextView[] dots;
    private int[] layouts;
    private int position = 0;
    private CountDownTimer tutorialTimer = null;

    @Override
    public int getBindingVariable() {
        return BR.tutorialViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tutorial;
    }

    @Override
    public TutorialViewModel getViewModel() {
        tutorialViewModel = ViewModelProviders.of(this, factory).get(TutorialViewModel.class);
        return tutorialViewModel;
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTutorialBinding = getViewDataBinding();
        tutorialViewModel.setNavigator(this);

        layouts = new int[]{
                R.layout.tutorial_sage1,
                R.layout.tutorial_sage2,
                R.layout.tutorial_sage3};

        addBottomDots(position);

        myViewPagerAdapter = new MyViewPagerAdapter();
        activityTutorialBinding.viewPager.setAdapter(myViewPagerAdapter);
        activityTutorialBinding.viewPager.setPageTransformer(true, new AccordionTransformer());
        activityTutorialBinding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        activityTutorialBinding.viewPager.setScrollDuration(1000);

        setUpTimer();

      /*  if (isNetworkConnected()) {
            checkPhoneWS("+911234567890");
        }*/
    }

    private void setUpTimer() {

        tutorialTimer = new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (position == 2) {
                    position = -1;
                    activityTutorialBinding.viewPager.setScrollDuration(0);
                }
                activityTutorialBinding.viewPager.setCurrentItem(position + 1);
                addBottomDots(position);
                activityTutorialBinding.viewPager.setScrollDuration(1000);
                this.start();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tutorialTimer != null) {
            tutorialTimer.cancel();
            tutorialTimer = null;
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new AppCompatTextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        activityTutorialBinding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new AppCompatTextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            activityTutorialBinding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int pos) {

            position = pos;
            addBottomDots(pos);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void getStarted() {

        openActivity(this, PhoneVerificationActivity.class, false, false);
        slideRightToLeft();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = Objects.requireNonNull(layoutInflater).inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    void checkPhoneWS(String number) {

        CheckPhoneMainRequest checkPhoneMainRequest = new CheckPhoneMainRequest();
        CheckPhoneRequestData checkPhoneRequestData = new CheckPhoneRequestData();
        CheckPhoneDatum checkPhoneDatum = new CheckPhoneDatum();

        checkPhoneDatum.setUserMobile(number);
        checkPhoneDatum.setDevicetype("Android");

        checkPhoneRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        checkPhoneRequestData.setRequestType("userSignupCheck");
        checkPhoneRequestData.setData(checkPhoneDatum);

        checkPhoneMainRequest.setRequestData(checkPhoneRequestData);

        AndroidNetworking.post(ApiConstants.UserRegistration)
                .addApplicationJsonBody(checkPhoneMainRequest)
                .setTag("userSignupCheck")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CheckPhoneMainResponse.class, new ParsedRequestListener<CheckPhoneMainResponse>() {

                    @Override
                    public void onResponse(CheckPhoneMainResponse response) {
                        successAPI(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void successAPI(CheckPhoneMainResponse checkPhoneMainResponse) {

        hideLoading();
        try {
            if (checkPhoneMainResponse.getStatus() != null && checkPhoneMainResponse.getStatus().equalsIgnoreCase("true")) {
                if (checkPhoneMainResponse.getData() != null && checkPhoneMainResponse.getData().size() > 0) {

                    SharedData.clearSharedPreference(this);

                    SharedData.saveString(this, Constant.PHONE_NUMBER, decodeBase64(checkPhoneMainResponse.getData().get(0).getUserMobile()));
                    SharedData.saveString(this, Constant.EMAIL, decodeBase64(checkPhoneMainResponse.getData().get(0).getUserEmail()));
                    SharedData.saveString(this, Constant.USERID, checkPhoneMainResponse.getData().get(0).getUserid());
                    SharedData.saveString(this, Constant.FIRST_NAME, checkPhoneMainResponse.getData().get(0).getFname());
                    SharedData.saveString(this, Constant.LAST_NAME, checkPhoneMainResponse.getData().get(0).getLname());

                    openActivity(this, HomeActivity.class, false, true);
                    slideRightToLeft();
                } else {

                    SharedData.saveString(this, Constant.TEMP_PHONE_NUMBER, "+911234567890");
                    openActivity(this, AddEmailSignupActivity.class, false, true);
                    slideRightToLeft();
                }
            } else {
                SharedData.saveString(this, Constant.TEMP_PHONE_NUMBER, "+911234567890");
                openActivity(this, AddEmailSignupActivity.class, false, true);
                slideRightToLeft();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.sage.cabapp.ui.paymentprofile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityChangePaymentProfileBinding;
import com.sage.cabapp.ui.addbusinessprofile.AddBusinessProfileActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.editbusinessprofile.EditBusinessProfileActivity;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.ui.personalprofile.PersonalProfileActivity;
import com.sage.cabapp.utils.Constant;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class ChangePaymentProfileActivity extends BaseActivity<ActivityChangePaymentProfileBinding, ChangePaymentProfileViewModel> implements ChangePaymentProfileNavigator {

    @Inject
    ViewModelProviderFactory factory;
    ChangePaymentProfileViewModel changePaymentProfileViewModel;
    ActivityChangePaymentProfileBinding activityChangePaymentProfileBinding;

    @Override
    public int getBindingVariable() {
        return BR.changePaymentViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_payment_profile;
    }

    @Override
    public ChangePaymentProfileViewModel getViewModel() {
        changePaymentProfileViewModel = ViewModelProviders.of(this, factory).get(ChangePaymentProfileViewModel.class);
        return changePaymentProfileViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityChangePaymentProfileBinding = getViewDataBinding();
        changePaymentProfileViewModel.setNavigator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            changePaymentProfileViewModel.getAllPaymentsCardWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void personalProfile() {
        openActivity(this, PersonalProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void businessProfile() {

        openActivity(this, AddBusinessProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void editbusinessProfile() {
        openActivity(this, EditBusinessProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void successAPI(GetAllPaymentsCardResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData().getData() != null && response.getData().getData().size() > 0) {
                    paymentCardsData(response.getData().getData(), response.getData().getPersonal(), response.getData().getBusiness());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paymentCardsData(List<AllCardsData> getAllPaymentsCardDataList, String personal, String business) {

        for (int i = 0; i < getAllPaymentsCardDataList.size(); i++) {

            AllCardsData allCardsData = getAllPaymentsCardDataList.get(i);

            if (personal != null && !personal.equalsIgnoreCase("")) {
                if (personal.equalsIgnoreCase(allCardsData.getId())) {
                    String capitalizeWord = "";
                    String first = allCardsData.getCard().getBrand().substring(0, 1);
                    String afterfirst = allCardsData.getCard().getBrand().substring(1);
                    capitalizeWord += first.toUpperCase() + afterfirst + " ";
                    activityChangePaymentProfileBinding.personalCard.setText(String.format("%s • %s", capitalizeWord, allCardsData.getCard().getLast4()));
                }
            }

            if (business != null && !business.equalsIgnoreCase("")) {
                if (business.equalsIgnoreCase(allCardsData.getId())) {
                    String capitalizeWord = "";
                    String first = allCardsData.getCard().getBrand().substring(0, 1);
                    String afterfirst = allCardsData.getCard().getBrand().substring(1);
                    capitalizeWord += first.toUpperCase() + afterfirst + " ";
                    activityChangePaymentProfileBinding.businessCard.setText(String.format("%s • %s", capitalizeWord, allCardsData.getCard().getLast4()));
                }
            }
        }

        if (business != null && !business.equalsIgnoreCase("")) {
            activityChangePaymentProfileBinding.createBusinessLayout.setVisibility(View.GONE);
            activityChangePaymentProfileBinding.editBusinessLayout.setVisibility(View.VISIBLE);
        } else {
            activityChangePaymentProfileBinding.createBusinessLayout.setVisibility(View.VISIBLE);
            activityChangePaymentProfileBinding.editBusinessLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }
}

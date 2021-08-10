package com.sage.cabapp.ui.personalprofile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityPersonalProfileBinding;
import com.sage.cabapp.ui.addpaymentmethod.AddPaymentMethodActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.payment.model.AllCardsData;
import com.sage.cabapp.ui.payment.model.GetAllPaymentsCardResponse;
import com.sage.cabapp.ui.personalprofile.adapter.PersonalProfileAdapter;
import com.sage.cabapp.ui.personalprofile.fragment.ChangePersonalCardFragment;
import com.sage.cabapp.ui.personalprofile.model.ChangeCardPreferencesResponse;
import com.sage.cabapp.ui.personalprofile.model.PersonalProfileModel;
import com.sage.cabapp.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PersonalProfileActivity extends BaseActivity<ActivityPersonalProfileBinding, PersonalProfileViewModel> implements PersonalProfileNavigator, ChangeActiveCardCallback {

    @Inject
    ViewModelProviderFactory factory;
    PersonalProfileViewModel personalProfileViewModel;
    ActivityPersonalProfileBinding activityPersonalProfileBinding;

    @Override
    public int getBindingVariable() {
        return BR.personalProfileViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_profile;
    }

    @Override
    public PersonalProfileViewModel getViewModel() {
        personalProfileViewModel = ViewModelProviders.of(this, factory).get(PersonalProfileViewModel.class);
        return personalProfileViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPersonalProfileBinding = getViewDataBinding();
        personalProfileViewModel.setNavigator(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkConnected()) {
            showLoading("");
            personalProfileViewModel.getAllPaymentsCardWS(this);
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
    public void addPaymentMethod() {

        openActivity(this, AddPaymentMethodActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void successAPI(ChangeCardPreferencesResponse response) {
        hideLoading();

        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                if (isNetworkConnected()) {
                    showLoading("");
                    personalProfileViewModel.getAllPaymentsCardWS(this);
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void getAllCardssuccessAPI(GetAllPaymentsCardResponse response) {
        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getData().getData() != null && response.getData().getData().size() > 0) {
                    
                    if (response.getData().getPaymentId() != null && !response.getData().getPaymentId().equalsIgnoreCase("")){
                        paymentCardsData(response.getData().getData(),response.getData().getPaymentId());
                    }
                    
                } else {
                    vibrate();
                    Constant.showErrorToast(response.getMessage(), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paymentCardsData(List<AllCardsData> getAllPaymentsCardDataList,String paymentID) {


        List<PersonalProfileModel> personalProfileModelList = new ArrayList<>();

        for (int i = 0; i < getAllPaymentsCardDataList.size(); i++) {

            final AllCardsData getAllPaymentsCardData = getAllPaymentsCardDataList.get(i);

            String number = getAllPaymentsCardData.getCard().getLast4();

            if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("visa")) {
                if (getAllPaymentsCardData.getId().equalsIgnoreCase(paymentID)) {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number), getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_visa, true));
                } else {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_visa, false));
                }
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("mastercard")) {
                if (getAllPaymentsCardData.getId().equalsIgnoreCase(paymentID)) {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_master_card, true));
                } else {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_master_card, false));
                }

            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("amex")) {
                if (getAllPaymentsCardData.getId().equalsIgnoreCase(paymentID)) {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_amex, true));
                } else {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_amex, false));
                }
            } else if (getAllPaymentsCardData.getCard().getBrand().equalsIgnoreCase("discover")) {
                if (getAllPaymentsCardData.getId().equalsIgnoreCase(paymentID)) {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_discover, true));
                } else {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_discover, false));
                }
            }else {
                if (getAllPaymentsCardData.getId().equalsIgnoreCase(paymentID)) {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_method, true));
                } else {
                    personalProfileModelList.add(new PersonalProfileModel(getAllPaymentsCardData.getId(),String.format("•••• •••• •••• %s", number),  getAllPaymentsCardData.getCard().getBrand(), R.drawable.payment_ic_method, false));
                }
            }
        }
        activityPersonalProfileBinding.cardList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        PersonalProfileAdapter personalProfileAdapter = new PersonalProfileAdapter(personalProfileModelList, this);
        personalProfileAdapter.setChangeActiveCardCallback(this);
        activityPersonalProfileBinding.cardList.setAdapter(personalProfileAdapter);
    }

    @Override
    public void changeActiveCard(PersonalProfileModel personalProfileModel) {

        Bundle bundle = new Bundle();
        bundle.putString("paymentId", personalProfileModel.getPaymentId());
        bundle.putString("brand", personalProfileModel.getCardType());

        ChangePersonalCardFragment changePersonalCardFragment = ChangePersonalCardFragment.newInstance();
        changePersonalCardFragment.setArguments(bundle);
        changePersonalCardFragment.setCancelable(false);
        changePersonalCardFragment.show(getSupportFragmentManager(), "changePersonalCardFragment");
    }
}

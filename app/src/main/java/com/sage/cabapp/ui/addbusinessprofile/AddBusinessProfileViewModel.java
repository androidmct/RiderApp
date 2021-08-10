package com.sage.cabapp.ui.addbusinessprofile;

import android.text.TextUtils;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 13-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddBusinessProfileViewModel extends BaseViewModel<AddBusinessProfileNavigator> {

    public AddBusinessProfileViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    boolean isEnterEmail(String email) {
        return !TextUtils.isEmpty(email);
    }

    boolean isEmailValid(String email) {
        return Constant.isEmailValid(email);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void submitProfile(){
        getNavigator().submitProfile();
    }
}

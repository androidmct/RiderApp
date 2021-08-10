package com.sage.cabapp.ui.phoneverification;

import android.text.TextUtils;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class PhoneViewModel extends BaseViewModel<PhoneNavigator> {
    public PhoneViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack(){
        getNavigator().backButton();
    }

    public void checkNumber(){
        getNavigator().nextButton();
    }

    boolean isPhoneValid(String phone) {
        return !TextUtils.isEmpty(phone);
    }
}

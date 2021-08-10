package com.sage.cabapp.ui.editphone;

import android.text.TextUtils;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class EditPhoneViewModel extends BaseViewModel<EditPhoneNavigator> {
    public EditPhoneViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }


    boolean isPhoneValid(String phone) {
        return !TextUtils.isEmpty(phone);
    }

    public void nextNumber(){
        getNavigator().nextNumber();
    }
}

package com.sage.cabapp.ui.addemailsignup;

import android.text.TextUtils;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 19-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class AddEmailSignupViewModel extends BaseViewModel<AddEmailSignupNavigator> {

    public AddEmailSignupViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    boolean isEnterEmail(String email) {
        return !TextUtils.isEmpty(email);
    }

    boolean isEmailValid(String email) {
        return Constant.isEmailValid(email);
    }

    public void goBack(){
        getNavigator().backButton();
    }

    public void nextEmail(){
        getNavigator().nextButton();
    }
}

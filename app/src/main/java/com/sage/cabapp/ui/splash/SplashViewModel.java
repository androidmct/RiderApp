package com.sage.cabapp.ui.splash;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 15-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    void leaveSplash(){
        getNavigator().leaveSplash(true);
    }
}

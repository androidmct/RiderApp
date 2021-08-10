package com.sage.cabapp.ui.freerides;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class FreeRidesViewModel extends BaseViewModel<FreeRidesNavigator> {
    public FreeRidesViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void shareText(){
        getNavigator().shareText();
    }

    public void shareEmail(){
        getNavigator().shareEmail();
    }

    public void shareFacebook(){
        getNavigator().shareFacebook();
    }

    public void shareOtherApps(){
        getNavigator().shareOtherApps();
    }
}

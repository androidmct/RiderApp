package com.sage.cabapp.ui.locationonmap;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 03-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class LocationOnMapViewModel extends BaseViewModel<LocationOnMapNavigator> {

    public LocationOnMapViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack(){
        getNavigator().onBack();
    }

    public void clickNavigation(){
        getNavigator().clickNavigation();
    }

    public void clickConfirm(){
        getNavigator().clickConfirm();
    }
}

package com.sage.cabapp.ui.confirmpickup;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 03-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class ConfirmPickupMapViewModel extends BaseViewModel<ConfirmPickupMapNavigator> {

    public ConfirmPickupMapViewModel(SchedulerProvider schedulerProvider) {
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

    public void clickSearch(){
        getNavigator().clickSearch();
    }
}

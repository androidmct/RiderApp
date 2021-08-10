package com.sage.cabapp.ui.addpromocode;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AddPromoCodeViewModel extends BaseViewModel<AddPromoCodeNavigator> {

    public AddPromoCodeViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void submitCode(){
        getNavigator().submitCode();
    }
}

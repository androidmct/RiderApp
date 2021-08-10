package com.sage.cabapp.ui.promocode;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PromoCodeViewModel extends BaseViewModel<PromoCodeNavigator> {

    public PromoCodeViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void addPromoCode(){
        getNavigator().addPromoCode();
    }
}

package com.sage.cabapp.ui.setaddressconfirmpickup;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 17-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SetAddressPickupViewModel extends BaseViewModel<SetAddressPickupNavigator> {

    public SetAddressPickupViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack(){
        getNavigator().onBack();
    }
}

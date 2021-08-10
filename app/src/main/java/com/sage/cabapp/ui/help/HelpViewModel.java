package com.sage.cabapp.ui.help;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class HelpViewModel extends BaseViewModel<HelpNavigator> {
    public HelpViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack(){
        getNavigator().onBack();
    }

    public void generalInquiry(){
        getNavigator().generalInquiry();
    }
}

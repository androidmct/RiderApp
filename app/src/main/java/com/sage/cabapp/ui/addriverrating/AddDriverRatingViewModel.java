package com.sage.cabapp.ui.addriverrating;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

public class AddDriverRatingViewModel extends BaseViewModel<AddDriverRatingNavigator> {
    public AddDriverRatingViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void next(){
        getNavigator().next();
    }
}

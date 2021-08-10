package com.sage.cabapp.ui.tutorial;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 18-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class TutorialViewModel extends BaseViewModel<TutorialNavigator> {

    public TutorialViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onGetStartedClick() {
        getNavigator().getStarted();
    }

}

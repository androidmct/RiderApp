package com.sage.cabapp.ui.chatmodulenew;

import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

/**
 * Created by Maroof Ahmed Siddique on 24-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class ChatModuleNewViewModel extends BaseViewModel<ChatModuleNewNavigator> {
    public ChatModuleNewViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void goBack(){
        getNavigator().onBack();
    }

    public void callPhone(){
        getNavigator().callPhone();
    }

}

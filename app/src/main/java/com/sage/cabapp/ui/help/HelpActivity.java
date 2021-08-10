package com.sage.cabapp.ui.help;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityHelpBinding;
import com.sage.cabapp.ui.base.BaseActivity;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class HelpActivity extends BaseActivity<ActivityHelpBinding,HelpViewModel> implements HelpNavigator {

    @Inject
    ViewModelProviderFactory factory;
    HelpViewModel helpViewModel;
    ActivityHelpBinding activityHelpBinding;

    @Override
    public int getBindingVariable() {
        return BR.helpViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public HelpViewModel getViewModel() {
        helpViewModel = ViewModelProviders.of(this,factory).get(HelpViewModel.class);
        return helpViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHelpBinding = getViewDataBinding();
        helpViewModel.setNavigator(this);
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void generalInquiry() {
        openActivity(this,GeneralInquiryActivity.class,false,false);
        slideRightToLeft();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }
}

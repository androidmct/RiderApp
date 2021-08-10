package com.sage.cabapp.ui.promocode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityPromoCodeBinding;
import com.sage.cabapp.ui.addpromocode.AddPromoCodeActivity;
import com.sage.cabapp.ui.base.BaseActivity;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 11-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class PromoCodeActivity extends BaseActivity<ActivityPromoCodeBinding, PromoCodeViewModel> implements PromoCodeNavigator {

    @Inject
    ViewModelProviderFactory factory;
    PromoCodeViewModel promoCodeViewModel;
    ActivityPromoCodeBinding activityPromoCodeBinding;

    @Override
    public int getBindingVariable() {
        return BR.promoCodeViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_promo_code;
    }

    @Override
    public PromoCodeViewModel getViewModel() {
        promoCodeViewModel = ViewModelProviders.of(this, factory).get(PromoCodeViewModel.class);
        return promoCodeViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityPromoCodeBinding = getViewDataBinding();
        promoCodeViewModel.setNavigator(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void addPromoCode() {

        openActivity(this, AddPromoCodeActivity.class, false, false);
        slideRightToLeft();
    }
}

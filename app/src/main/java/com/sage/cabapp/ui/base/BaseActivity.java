package com.sage.cabapp.ui.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.snackbar.Snackbar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.sage.cabapp.R;
import com.sage.cabapp.ui.callback.CallbackBaseActivity;
import com.sage.cabapp.utils.Base64EncyrptDecrypt;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.NetworkAvailability;

import java.util.Objects;

import dagger.android.AndroidInjection;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity implements CallbackBaseActivity {

    private ProgressDialog mProgressDialog;
    private Vibrator vibrator;
    private View rootView;
    private T mViewDataBinding;
    private V mViewModel;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        performDataBinding();
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        rootView = getWindow().getDecorView().getRootView();

        AppCenter.start(getApplication(), "4cc59ffc-4bfa-4e31-927c-31501cf17b71",
                Analytics.class, Crashes.class);
    }

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public void showLoading(String message) {

        try {
            hideLoading();
            mProgressDialog = Constant.showLoadingDialog(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void hideLoading() {

        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public boolean isNetworkConnected() {

        return NetworkAvailability.checkNetworkStatus(this);
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void slideTopToBottom() {
        overridePendingTransition(R.anim.enter_top, R.anim.exit_bottom);
    }

    @Override
    public void slideBottomToTop() {
        overridePendingTransition(R.anim.enter_bottom, R.anim.exit_top);
    }

    @Override
    public void slideLeftToRight() {
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right);
    }

    @Override
    public void innToOut() {
        overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
    }

    @Override
    public void slideRightToLeft() {
        overridePendingTransition(R.anim.enter_right, R.anim.exit_left);
    }

    @Override
    public void openActivity(Context packageContext, Class<?> cls, boolean isFinish, boolean isFinishAffinity) {
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
        if (isFinish) {
            finish();
        } else if (isFinishAffinity) {
            finishAffinity();
        }
    }

    @Override
    public void vibrate() {
        vibrator.vibrate(100);
    }

    @Override
    public void vibrateLong() {
        vibrator.vibrate(200);
    }

    @Override
    public void errorSnackBar(String message) {

        try {
            Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(16);
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successSnackBar(String message) {
        try {
            Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(16);
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.success_green));
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String encodeBase64(String message) {
        return Base64EncyrptDecrypt.encyrpt(message);
    }

    @Override
    public String decodeBase64(String message) {
        return Base64EncyrptDecrypt.decrypt(message);
    }

    @Override
    public void callPhone(String phone, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void clearAllNotifications() {
        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(nMgr).cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

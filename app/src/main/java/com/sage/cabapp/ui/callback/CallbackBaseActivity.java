package com.sage.cabapp.ui.callback;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public interface CallbackBaseActivity {

    void showLoading(String message);

    void hideLoading();

    void openActivityOnTokenExpire();

    void onError(String message);

    void showMessage(String message);

    boolean isNetworkConnected();

    void hideKeyboard();

    void slideTopToBottom();

    void slideBottomToTop();

    void slideLeftToRight();

    void slideRightToLeft();

    void innToOut();

    void openActivity(Context packageContext, Class<?> cls, boolean isFinish, boolean isFinishAffinity);

    void vibrate();

    void vibrateLong();

    void errorSnackBar(String message);

    void successSnackBar(String message);

    void onFragmentAttached();

    void onFragmentDetached(String tag);

    String encodeBase64(String message);

    String decodeBase64(String message);

    void callPhone(String phone, Activity activity);

    void clearAllNotifications();
}

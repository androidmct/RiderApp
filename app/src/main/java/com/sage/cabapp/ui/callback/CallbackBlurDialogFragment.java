package com.sage.cabapp.ui.callback;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface CallbackBlurDialogFragment {

    void showLoading(String message);

    void hideLoading();

    void vibrate();
}

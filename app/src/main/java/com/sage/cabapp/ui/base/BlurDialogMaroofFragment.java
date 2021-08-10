package com.sage.cabapp.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import com.sage.cabapp.ui.callback.CallbackBlurDialogFragment;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SmartAsyncPolicyHolder;

import java.util.Objects;

/**
 * Created by Maroof Ahmed Siddique on 27-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class BlurDialogMaroofFragment extends BlurDialogFragment implements CallbackBlurDialogFragment {

    private ProgressDialog mProgressDialog;
    private Vibrator vibrator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showLoading(String message) {
        try {
            hideLoading();
            mProgressDialog = Constant.showLoadingDialog(getActivity());
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
    public void vibrate() {
        try {
            vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(Color.argb(215, 255, 255, 255))  // semi-transparent white color
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true)
                .build();
    }
}

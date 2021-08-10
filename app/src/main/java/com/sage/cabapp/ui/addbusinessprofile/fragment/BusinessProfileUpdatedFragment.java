package com.sage.cabapp.ui.addbusinessprofile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;

import butterknife.ButterKnife;

public class BusinessProfileUpdatedFragment extends BlurDialogMaroofFragment {

    public static BusinessProfileUpdatedFragment newInstance() {
        BusinessProfileUpdatedFragment fragment = new BusinessProfileUpdatedFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_business_profile_updated, container, false);

        ButterKnife.bind(this, view);
        return view;
    }




}

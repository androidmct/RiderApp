package com.sage.cabapp.ui.accountsettings.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;

import butterknife.ButterKnife;

public class ProfileUpdatedFragment extends BlurDialogMaroofFragment {

    public static ProfileUpdatedFragment newInstance() {
        ProfileUpdatedFragment fragment = new ProfileUpdatedFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_profile_updated, container, false);

        ButterKnife.bind(this, view);
        return view;
    }



}

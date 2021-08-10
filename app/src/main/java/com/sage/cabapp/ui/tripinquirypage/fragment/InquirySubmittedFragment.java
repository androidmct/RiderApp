package com.sage.cabapp.ui.tripinquirypage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.base.BlurDialogMaroofFragment;

import butterknife.ButterKnife;

public class InquirySubmittedFragment extends BlurDialogMaroofFragment {

    public static InquirySubmittedFragment newInstance() {
        InquirySubmittedFragment fragment = new InquirySubmittedFragment();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.EtsyBlurDialogTheme);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_inquiry_submitted, container, false);

        ButterKnife.bind(this, view);
        return view;
    }
}

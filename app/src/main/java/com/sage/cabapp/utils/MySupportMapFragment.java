package com.sage.cabapp.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Maroof Ahmed Siddique on 13-05-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class MySupportMapFragment extends SupportMapFragment {
    private View mOriginalContentView;

    public MySupportMapFragment() {
        mOriginalContentView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, container, savedInstanceState);
        TouchableFrameLayout frameLayout = new TouchableFrameLayout(getActivity());
        frameLayout.addView(mOriginalContentView);
        return frameLayout;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }


}

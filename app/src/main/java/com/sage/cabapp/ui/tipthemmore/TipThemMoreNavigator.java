package com.sage.cabapp.ui.tipthemmore;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.tipthemmore.model.TipThemResponse;

/**
 * Created by Maroof Ahmed Siddique on 08-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface TipThemMoreNavigator {

    void onBack();

    void tipDriverResponse(TipThemResponse response);

    void errorAPI(ANError anError);

    void tipThem();
}

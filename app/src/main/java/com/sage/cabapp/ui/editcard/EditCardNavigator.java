package com.sage.cabapp.ui.editcard;

import com.androidnetworking.error.ANError;
import com.sage.cabapp.ui.editcard.model.EditUpdateCardResponse;

/**
 * Created by Maroof Ahmed Siddique on 28-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public interface EditCardNavigator {

    void onBack();

    void saveCard();

    void deleteCard();

    void successAPI(EditUpdateCardResponse response);

    void errorAPI(ANError anError);
}

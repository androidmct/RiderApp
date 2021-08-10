package com.sage.cabapp.ui.tripinquirypage;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.ui.base.BaseViewModel;
import com.sage.cabapp.ui.tripinquirypage.model.TripInquiryResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.AppLogger;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.rx.SchedulerProvider;

import java.io.File;
import java.util.List;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class TripInquiryViewModel extends BaseViewModel<TripInquiryNavigator> {
    public TripInquiryViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public void onBack() {
        getNavigator().onBack();
    }

    public void submitInquiry() {
        getNavigator().submitInquiry();
    }

    public void addImagesClick() {
        getNavigator().addImagesClick();
    }

    void inquirySubmissionWS(Context context, String usermobile, String useremail, String requestId, String problemtype, String problemdetail, List<File> imagesList) {

        String userid = SharedData.getString(context, Constant.USERID);

        File image1 = null;
        File image2 = null;
        File image3 = null;
        File image4 = null;
        File image5 = null;
        File image6 = null;

        if (imagesList != null && imagesList.size() > 0) {
            for (int i = 0; i < imagesList.size(); i++) {
                if (i == 0) {
                    image1 = imagesList.get(i);
                } else if (i == 1) {
                    image2 = imagesList.get(i);
                } else if (i == 2) {
                    image3 = imagesList.get(i);
                } else if (i == 3) {
                    image4 = imagesList.get(i);
                } else if (i == 4) {
                    image5 = imagesList.get(i);
                } else if (i == 5) {
                    image6 = imagesList.get(i);
                }
            }
        }

        try {
            AndroidNetworking.upload(ApiConstants.generalInquiry)
                    .addMultipartFile("image1", image1)
                    .addMultipartFile("image2", image2)
                    .addMultipartFile("image3", image3)
                    .addMultipartFile("image4", image4)
                    .addMultipartFile("image5", image5)
                    .addMultipartFile("image6", image6)
                    .addMultipartParameter("userid", userid)
                    .addMultipartParameter("problemtype", problemtype)
                    .addMultipartParameter("problemdetail", problemdetail)
                    .addMultipartParameter("usermobile", usermobile)
                    .addMultipartParameter("useremail", usermobile)
                    .addMultipartParameter("type", "Specific Trip")
                    .addMultipartParameter("status", "pending")
                    .addMultipartParameter("devicetype", "Android")
                    .addMultipartParameter("apikey", "aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                        // do anything with progress
                        AppLogger.i("TIME", bytesUploaded);
                    })
                    .getAsObject(TripInquiryResponse.class, new ParsedRequestListener<TripInquiryResponse>() {

                        @Override
                        public void onResponse(TripInquiryResponse response) {

                            getNavigator().successAPI(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                            getNavigator().errorAPI(anError);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

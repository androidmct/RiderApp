package com.sage.cabapp.ui.help;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.error.ANError;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityGeneralInquiryBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.tripinquirypage.InquiryImageDelete;
import com.sage.cabapp.ui.tripinquirypage.adapter.InquiryImagesAdapter;
import com.sage.cabapp.ui.tripinquirypage.fragment.InquirySubmittedFragment;
import com.sage.cabapp.ui.tripinquirypage.model.TripInquiryResponse;
import com.sage.cabapp.utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 25-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class GeneralInquiryActivity extends BaseActivity<ActivityGeneralInquiryBinding, GeneralInquiryFormViewModel> implements GeneralInquiryFormNavigator, InquiryImageDelete {

    @Inject
    ViewModelProviderFactory factory;
    GeneralInquiryFormViewModel generalInquiryFormViewModel;
    ActivityGeneralInquiryBinding activityGeneralInquiryBinding;

    private InquiryImagesAdapter inquiryImagesAdapter = null;
    List<File> imagesList = new ArrayList<>();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean isGallery = true;

    @Override
    public int getBindingVariable() {
        return BR.generalInquiryViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_general_inquiry;
    }

    @Override
    public GeneralInquiryFormViewModel getViewModel() {
        generalInquiryFormViewModel = ViewModelProviders.of(this, factory).get(GeneralInquiryFormViewModel.class);
        return generalInquiryFormViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityGeneralInquiryBinding = getViewDataBinding();
        generalInquiryFormViewModel.setNavigator(this);
        addFile();
    }

    private void openDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.inquiry_bottom_sheet, null);

        LinearLayout select_from_gallery = dialogView.findViewById(R.id.select_from_gallery);
        LinearLayout click_camera = dialogView.findViewById(R.id.click_camera);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        select_from_gallery.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = true;
            if (checkPermissions()) {
                ImagePicker.Companion.with(GeneralInquiryActivity.this).crop().galleryOnly().start();
            } else {
                requestPermissions();
            }
        });

        click_camera.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = false;
            if (checkPermissions()) {
                ImagePicker.Companion.with(GeneralInquiryActivity.this).crop().cameraOnly().start();
            } else {
                requestPermissions();
            }
        });

        dialog.show();
    }

    private boolean checkPermissions() {
        int SD_CARD = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (SD_CARD != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        return listPermissionsNeeded.isEmpty();
    }

    private void requestPermissions() {

        boolean shouldProvideRationaleSD_CARD =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean shouldProvideRationaleCAMERA =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationaleSD_CARD || shouldProvideRationaleCAMERA) {
            showSnackbar(R.string.sdcard_permission,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(GeneralInquiryActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(GeneralInquiryActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (checkPermissions()) {
                    if (isGallery) {
                        ImagePicker.Companion.with(GeneralInquiryActivity.this).galleryOnly().start();
                    } else {
                        ImagePicker.Companion.with(GeneralInquiryActivity.this).cameraOnly().start();
                    }
                } else {
                    requestPermissions();
                }
            } else {
                // Permission denied.

                showSnackbar(R.string.loc_permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {

        try {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).setActionTextColor(Color.WHITE);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(14);
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // File object will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);

            imagesList.add(file);
            inquiryImagesAdapter.addImages(imagesList);

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Constant.showErrorToast(ImagePicker.Companion.getError(data), this);
        }
    }


    private void addFile() {

        inquiryImagesAdapter = new InquiryImagesAdapter(this);
        inquiryImagesAdapter.setInquiryImageDelete(this);
        activityGeneralInquiryBinding.inquiryImagesRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityGeneralInquiryBinding.inquiryImagesRecyclerview.setAdapter(inquiryImagesAdapter);
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void submitQuery() {

        if (isNetworkConnected()) {

            String problemDetail = Objects.requireNonNull(activityGeneralInquiryBinding.commentsEt.getText()).toString().trim();
            String problemType = Objects.requireNonNull(activityGeneralInquiryBinding.etSubject.getText()).toString().trim();

            if (problemType.isEmpty()) {
                Constant.showErrorToast("Please enter inquiry subject!", this);
                return;
            } else if (problemDetail.isEmpty()) {
                Constant.showErrorToast("Please enter inquiry message!", this);
                return;
            }

            hideKeyboard();
            showLoading("");

            if (imagesList != null && imagesList.size() > 0) {
                generalInquiryFormViewModel.inquirySubmissionWS(this, problemType, problemDetail, imagesList);
            } else {
                generalInquiryFormViewModel.inquirySubmissionWS(this, problemType, problemDetail, null);
            }
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void addImagesClick() {
        if (imagesList != null && imagesList.size() == 6) {
            Constant.showErrorToast("Maximum 6 images are allow to submit!", this);
        } else {
            openDialog();
        }
    }

    @Override
    public void successAPI(TripInquiryResponse response) {

        hideLoading();

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                showInquirySubmitted();
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        vibrate();
        Constant.showErrorToast(anError.getMessage(), this);
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    InquirySubmittedFragment inquirySubmittedFragment = null;

    void showInquirySubmitted() {

        inquirySubmittedFragment = InquirySubmittedFragment.newInstance();
        inquirySubmittedFragment.setCancelable(true);
        inquirySubmittedFragment.show(getSupportFragmentManager(), "inquirySubmitted");

        new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (inquirySubmittedFragment != null && inquirySubmittedFragment.isVisible()) {
                    inquirySubmittedFragment.dismiss();
                    inquirySubmittedFragment = null;
                    this.cancel();
                }

                finish();
                slideLeftToRight();
            }
        }.start();
    }

    @Override
    public void deleteImage(File file) {
        imagesList.remove(file);
        inquiryImagesAdapter.addImages(imagesList);
    }
}

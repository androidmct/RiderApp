package com.sage.cabapp.ui.accountsettings;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.BuildConfig;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityAccountSettingsBinding;
import com.sage.cabapp.ui.accountsettings.fragment.LogoutFragment;
import com.sage.cabapp.ui.accountsettings.fragment.ProfileUpdatedFragment;
import com.sage.cabapp.ui.accountsettings.model.ProfileUpdateResponse;
import com.sage.cabapp.ui.accountsettings.model.UpdatedProfileData;
import com.sage.cabapp.ui.accountsettings.model.UpdatedProfileResponse;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.editemail.EditEmailActivity;
import com.sage.cabapp.ui.editname.EditNameActivity;
import com.sage.cabapp.ui.editphone.EditPhoneActivity;
import com.sage.cabapp.ui.splash.SplashActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.AppLogger;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 09-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AccountSettingsActivity extends BaseActivity<ActivityAccountSettingsBinding, AccountSettingsViewModel> implements AccountSettingsNavigator {

    @Inject
    ViewModelProviderFactory factory;
    AccountSettingsViewModel accountSettingsViewModel;
    ActivityAccountSettingsBinding activityAccountSettingsBinding;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean isGallery = true;
    private File image = null;


    @Override
    public int getBindingVariable() {
        return BR.accountSettingsViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_settings;
    }

    @Override
    public AccountSettingsViewModel getViewModel() {
        accountSettingsViewModel = ViewModelProviders.of(this, factory).get(AccountSettingsViewModel.class);
        return accountSettingsViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAccountSettingsBinding = getViewDataBinding();
        accountSettingsViewModel.setNavigator(this);

        SharedData.saveBool(this, Constant.PROFILE_UPDATED, false);

        updateProfileData();

        if (isNetworkConnected()) {

            showLoading("");
            accountSettingsViewModel.getProfileDataWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void logout() {
        LogoutFragment fragment = LogoutFragment.newInstance();
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "logout");
    }

    @Override
    public void changeprofilepic() {
        openDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isUpdated = SharedData.getBool(this, Constant.PROFILE_UPDATED);
        if (isUpdated) {
            SharedData.saveBool(this, Constant.PROFILE_UPDATED, false);
            showProfileUpdated();
            if (isNetworkConnected()) {

                showLoading("");

                accountSettingsViewModel.getProfileDataWS(this);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }
    }

    @Override
    public void editName() {
        openActivity(this, EditNameActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void editEmail() {
        openActivity(this, EditEmailActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void editPhone() {
        openActivity(this, EditPhoneActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void successAPI(UpdatedProfileResponse profileUpdateResponse) {

        hideLoading();

        try {

            if (profileUpdateResponse.getStatus() != null && profileUpdateResponse.getStatus().equalsIgnoreCase("true")) {

                if (profileUpdateResponse.getData() != null) {

                    UpdatedProfileData updatedProfileData = profileUpdateResponse.getData();

                    SharedData.saveString(this, Constant.PROFILE_PIC, updatedProfileData.getUserProfile());
                    SharedData.saveString(this, Constant.FIRST_NAME, updatedProfileData.getFname());
                    SharedData.saveString(this, Constant.LAST_NAME, updatedProfileData.getLname());
                    SharedData.saveString(this, Constant.PHONE_NUMBER, decodeBase64(updatedProfileData.getUserMobile()));
                    SharedData.saveString(this, Constant.EMAIL, decodeBase64(updatedProfileData.getUserEmail()));

                    updateProfileData();
                } else {
                    vibrate();
                    Constant.showErrorToast(profileUpdateResponse.getMessage(), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(profileUpdateResponse.getMessage(), this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSuccessAPI(ProfileUpdateResponse profileUpdateResponse) {
        hideLoading();

        try {

            if (profileUpdateResponse.getStatus() != null && profileUpdateResponse.getStatus().equalsIgnoreCase("true")) {
                if (isNetworkConnected()) {
                    accountSettingsViewModel.getProfileDataWS(this);
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                }

            } else {
                vibrate();
                Constant.showErrorToast(profileUpdateResponse.getMessage(), this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    private void showLogout() {

        BottomSheetMaterialDialog mAnimatedBottomSheetDialog;

        try {

            mAnimatedBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                    .setTitle("Logout?")
                    .setMessage("Are you sure want to logout this profile?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", R.drawable.ic_logout, (dialogInterface, i) -> {

                        dialogInterface.dismiss();
                        SharedData.clearSharedPreference(getApplicationContext());
                        openActivity(getApplicationContext(), SplashActivity.class, false, true);
                        slideLeftToRight();
                    })
                    .setNegativeButton("Cancel", R.drawable.ic_close, (dialogInterface, which) -> dialogInterface.dismiss())
                    .setAnimation("logout.json")
                    .build();
            mAnimatedBottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.profile_bottom_sheet, null);

        LinearLayout select_from_gallery = dialogView.findViewById(R.id.select_from_gallery);
        LinearLayout click_camera = dialogView.findViewById(R.id.click_camera);
        LinearLayout delete_photo = dialogView.findViewById(R.id.delete_photo);

        String profilePic = SharedData.getString(this, Constant.PROFILE_PIC);
        if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
            delete_photo.setVisibility(View.VISIBLE);
        } else {
            delete_photo.setVisibility(View.GONE);
        }

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        select_from_gallery.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = true;
            if (checkPermissions()) {
                ImagePicker.Companion.with(AccountSettingsActivity.this).cropSquare().maxResultSize(512, 512).galleryOnly().start();
            } else {
                requestPermissions();
            }
        });

        click_camera.setOnClickListener(v -> {
            dialog.cancel();
            isGallery = false;
            if (checkPermissions()) {
                ImagePicker.Companion.with(AccountSettingsActivity.this).cropSquare().maxResultSize(512, 512).cameraOnly().start();
            } else {
                requestPermissions();
            }
        });

        delete_photo.setOnClickListener(v -> {
            dialog.cancel();

            if (isNetworkConnected()) {
                showLoading("");
                accountSettingsViewModel.deletePhotoWS(this);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // File object will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);
            image = file;

            if (isNetworkConnected()) {
                uploadPhotoWS();
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Constant.showErrorToast(ImagePicker.Companion.getError(data), this);
        }

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
                            ActivityCompat.requestPermissions(AccountSettingsActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(AccountSettingsActivity.this,
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
                        ImagePicker.Companion.with(AccountSettingsActivity.this).cropSquare().maxResultSize(512, 512).galleryOnly().start();
                    } else {
                        ImagePicker.Companion.with(AccountSettingsActivity.this).cropSquare().maxResultSize(512, 512).cameraOnly().start();
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


    void uploadPhotoWS() {

        showLoading("");

        String userid = SharedData.getString(this, Constant.USERID);

        try {
            AndroidNetworking.upload(ApiConstants.Profile_Image)
                    .addMultipartFile("image", image)
                    .addMultipartParameter("userid", userid)
                    .addMultipartParameter("devicetype", "Android")
                    .addMultipartParameter("apikey", "aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz")
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                        // do anything with progress
                        AppLogger.i("TIME", bytesUploaded);
                    })
                    .getAsObject(ProfileUpdateResponse.class, new ParsedRequestListener<ProfileUpdateResponse>() {

                        @Override
                        public void onResponse(ProfileUpdateResponse response) {

                            successUploadAPI(response);
                        }

                        @Override
                        public void onError(ANError anError) {

                            errorAPI(anError);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successUploadAPI(ProfileUpdateResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                showProfileUpdated();

                if (isNetworkConnected()) {
                    accountSettingsViewModel.getProfileDataWS(this);
                } else {
                    vibrate();
                    Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ProfileUpdatedFragment profileUpdatedFragment = null;

    void showProfileUpdated() {

        profileUpdatedFragment = ProfileUpdatedFragment.newInstance();
        profileUpdatedFragment.setCancelable(true);
        profileUpdatedFragment.show(getSupportFragmentManager(), "profileupdated");

        new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (profileUpdatedFragment != null && profileUpdatedFragment.isVisible()) {
                    profileUpdatedFragment.dismiss();
                    profileUpdatedFragment = null;
                    this.cancel();
                }
            }
        }.start();
    }

    void updateProfileData() {
        String profilePic = SharedData.getString(this, Constant.PROFILE_PIC);
        if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
            Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityAccountSettingsBinding.profileImage);
        } else {
            Glide.with(this).load(R.drawable.profile_avatar).into(activityAccountSettingsBinding.profileImage);
        }

        String firstName = SharedData.getString(this, Constant.FIRST_NAME);
        String lastName = SharedData.getString(this, Constant.LAST_NAME);
        activityAccountSettingsBinding.etName.setText(String.format("%s %s", firstName, lastName));

        String phone = SharedData.getString(this, Constant.PHONE_NUMBER);
        activityAccountSettingsBinding.etPhone.setText(phone);

        String email = SharedData.getString(this, Constant.EMAIL);
        activityAccountSettingsBinding.etEmail.setText(email);
    }
}

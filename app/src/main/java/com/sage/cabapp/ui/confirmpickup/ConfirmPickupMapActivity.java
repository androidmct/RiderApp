package com.sage.cabapp.ui.confirmpickup;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityConfirmPickupMapBinding;
import com.sage.cabapp.services.FetchAddressIntentService;
import com.sage.cabapp.services.LocationUtils;
import com.sage.cabapp.services.SageLocationService;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.setaddressconfirmpickup.SetAddressPickupActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.TouchableFrameLayout;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 03-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class ConfirmPickupMapActivity extends BaseActivity<ActivityConfirmPickupMapBinding, ConfirmPickupMapViewModel> implements ConfirmPickupMapNavigator, OnMapReadyCallback, TouchableFrameLayout.OnChangeListener {

    @Inject
    ViewModelProviderFactory factory;
    private ConfirmPickupMapViewModel confirmPickupMapViewModel;
    private ActivityConfirmPickupMapBinding activityConfirmPickupMapBinding;
    private GoogleMap mGoogleMap;
    private LatLng sourceAddressLatLng = null;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private LatLng tapLocation = null;
    private Boolean mRequestingLocationUpdates = false;
    private boolean isRedirect = false;
    String mAddressOutput = "";

    @Override
    public void onInteractionEnd() {
        if (activityConfirmPickupMapBinding.navigationIcon.getVisibility() == View.GONE)
            activityConfirmPickupMapBinding.navigationIcon.setVisibility(View.VISIBLE);
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(SageLocationService.EXTRA_LOCATION);
            if (location != null) {

                if (sourceAddressLatLng != null) {
                    if (mGoogleMap != null && !isRedirect) {
                        isRedirect = true;

                        tapLocation = sourceAddressLatLng;
                        startIntentService();

                       /* mGoogleMap.clear();
                        MarkerOptions marker = new MarkerOptions().position(sourceAddressLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin));
                        mGoogleMap.addMarker(marker);*/
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceAddressLatLng, Constant.ZOOM_LOCATION));
                    }

                } else if (mCurrentLocation == null) {

                    if (mGoogleMap != null) {
                        mCurrentLocation = location;
                        tapLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        startIntentService();
                       /* mGoogleMap.clear();
                        MarkerOptions marker = new MarkerOptions().position(tapLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin));
                        mGoogleMap.addMarker(marker);*/
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tapLocation, Constant.ZOOM_LOCATION));
                    }

                }

                mCurrentLocation = location;
            }
        }
    }

    private AddressResultReceiver mResultReceiver;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private SageLocationService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SageLocationService.LocalBinder binder = (SageLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    public int getBindingVariable() {
        return BR.confirmMapViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_pickup_map;
    }

    @Override
    public ConfirmPickupMapViewModel getViewModel() {
        confirmPickupMapViewModel = ViewModelProviders.of(this, factory).get(ConfirmPickupMapViewModel.class);
        return confirmPickupMapViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.semi_tranparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        super.onCreate(savedInstanceState);

        myReceiver = new MyReceiver();

        mResultReceiver = new AddressResultReceiver(new Handler());

        activityConfirmPickupMapBinding = getViewDataBinding();
        confirmPickupMapViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SOURCELATLNG")) {
            sourceAddressLatLng = (LatLng) Objects.requireNonNull(intent.getExtras()).get("SOURCELATLNG");
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();

        if (LocationUtils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        initializeMap();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(new Intent(this, SageLocationService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if (mService != null) {
                mService.requestLocationUpdates();
            } else {
                new CountDownTimer(2000, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (mService != null) {
                            mService.requestLocationUpdates();
                        }
                    }
                }.start();

            }
        }

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
    }

    private void initializeMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            //  Objects.requireNonNull(mapFragment.getView()).setClickable(false);
            mapFragment.setRetainInstance(true);
            mapFragment.getMapAsync(ConfirmPickupMapActivity.this);
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map_view, Objects.requireNonNull(mapFragment));
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            try {
                boolean success = mGoogleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.uber_style));
            } catch (Resources.NotFoundException e) {

            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

            double lastLat = SharedData.getDouble(this, Constant.LAST_LATITUDE);
            double lastLng = SharedData.getDouble(this, Constant.LAST_LONGITUDE);

            if (lastLat != 0.0 && lastLng != 0.0) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLat, lastLng), Constant.ZOOM_LOCATION));
            }
        }

        Objects.requireNonNull(mGoogleMap).setOnCameraIdleListener(() -> {

            LatLng tapLoc = mGoogleMap.getCameraPosition().target;

            tapLocation = mGoogleMap.getCameraPosition().target;
            startIntentService();
        });

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;

            case 999:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mAddressOutput = Objects.requireNonNull(data).getStringExtra("address");
                        tapLocation = Objects.requireNonNull(data).getParcelableExtra("latlng");
                        activityConfirmPickupMapBinding.addressSelectLocation.setText(mAddressOutput);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {

                  /*  //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                    mRequestingLocationUpdates = true;*/
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(ConfirmPickupMapActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Constant.showErrorToast(sie.getMessage(), getApplicationContext());
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Constant.showErrorToast(errorMessage, getApplicationContext());
                            // mRequestingLocationUpdates = false;
                            break;
                    }

                });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(SageLocationService.ACTION_BROADCAST));

    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

        super.onPause();

        // Remove location updates to save battery.
        //stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }

        super.onStop();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackbar(R.string.location_permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ConfirmPickupMapActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(ConfirmPickupMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(ConfirmPickupMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            }
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

                mService.requestLocationUpdates();

                /*if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }*/
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
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onBack() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void clickNavigation() {
        if (mCurrentLocation != null) {

            if (mGoogleMap != null) {

                tapLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                startIntentService();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tapLocation, Constant.ZOOM_LOCATION));

                activityConfirmPickupMapBinding.navigationIcon.setVisibility(View.GONE);
            }

        } else {
            Constant.showErrorToast(getResources().getString(R.string.location_not_found), this);
        }
    }

    @Override
    public void clickConfirm() {

        if (tapLocation != null){
            String mAddressOutputFinal = activityConfirmPickupMapBinding.addressSelectLocation.getText().toString().trim();

            Intent returnIntent = new Intent();
            returnIntent.putExtra("address", mAddressOutputFinal);
            returnIntent.putExtra("latlng", tapLocation);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            slideLeftToRight();
        }else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.location_not_found),this);
        }
    }

    @Override
    public void clickSearch() {
        Intent intent = new Intent(this, SetAddressPickupActivity.class);
        intent.putExtra("ADDRESS",mAddressOutput);
        startActivityForResult(intent,999);
        slideBottomToTop();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constant.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, tapLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constant.RESULT_DATA_KEY);

            // Show a toast message if an address was found.
            if (resultCode == Constant.SUCCESS_RESULT) {
                activityConfirmPickupMapBinding.addressSelectLocation.setText(mAddressOutput);
            } else {
                Constant.showErrorToast(mAddressOutput, getApplicationContext());
            }
        }
    }

}

package com.sage.cabapp.ui.main;

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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.ms_square.etsyblur.BlurSupport;
import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.SageApp;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityHomeBinding;
import com.sage.cabapp.services.FetchAddressIntentService;
import com.sage.cabapp.services.LocationUtils;
import com.sage.cabapp.services.SageLocationService;
import com.sage.cabapp.twilio.chat.ChatClientManager;
import com.sage.cabapp.twilio.chat.TaskCompletionListener;
import com.sage.cabapp.ui.accountsettings.AccountSettingsActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.freerides.FreeRidesActivity;
import com.sage.cabapp.ui.help.HelpActivity;
import com.sage.cabapp.ui.main.fragment.NoCarFragment;
import com.sage.cabapp.ui.main.model.PlotDriversData;
import com.sage.cabapp.ui.main.model.PlotDriversDatum;
import com.sage.cabapp.ui.main.model.PlotDriversMainRequest;
import com.sage.cabapp.ui.main.model.PlotDriversMainResponse;
import com.sage.cabapp.ui.main.model.PlotDriversRequestData;
import com.sage.cabapp.ui.main.model.UpdateDeviceTokenResponse;
import com.sage.cabapp.ui.payment.PaymentActivity;
import com.sage.cabapp.ui.promocode.PromoCodeActivity;
import com.sage.cabapp.ui.savedplaces.SavedPlacesActivity;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesData;
import com.sage.cabapp.ui.savedplaces.model.GetAllPlacesResponse;
import com.sage.cabapp.ui.setaddress.SetSourceAddressActivity;
import com.sage.cabapp.ui.triphistory.TripHistoryActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.TouchableFrameLayout;
import com.twilio.chat.ChatClient;
import com.twilio.chat.StatusListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


/**
 * Created by Maroof Ahmed Siddique on 20-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements HomeNavigator, OnMapReadyCallback, TouchableFrameLayout.OnChangeListener, SavedPlacesClick {

    private ChatClientManager clientManager;

    @Inject
    ViewModelProviderFactory factory;
    private HomeViewModel homeViewModel;
    private ActivityHomeBinding activityHomeBinding;
    private GoogleMap mGoogleMap;

    private List<PlotDriversData> plotDriversDataList = new ArrayList<>();
    Marker mPositionMarker;

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
    private LatLng tapLocation = null;
    private LatLng desttapLocation = null;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = false;

    String mAddressOutput = "";
    String mDestAddressOutput = "";

    @Override
    public void onInteractionEnd() {

        if (!mGoogleMap.isMyLocationEnabled()){
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        if (activityHomeBinding.navigationIcon.getVisibility() == View.GONE)
            activityHomeBinding.navigationIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void openSavedPlace(GetAllPlacesData getAllPlacesData) {
        double lat = Double.parseDouble(getAllPlacesData.getLat());
        double lng = Double.parseDouble(getAllPlacesData.getLng());
        desttapLocation = new LatLng(lat, lng);
        mDestAddressOutput = getAllPlacesData.getAddress();

        if (tapLocation != null && !mAddressOutput.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, SetSourceAddressActivity.class);
            intent.putExtra("ADDRESS", mAddressOutput);
            intent.putExtra("LAT_LNG", tapLocation);
            intent.putExtra("DEST_LAT_LNG", desttapLocation);
            intent.putExtra("DEST_ADDRESS", mDestAddressOutput);
            startActivity(intent);
            slideBottomToTop();
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.location_not_found), this);
        }

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(SageLocationService.EXTRA_LOCATION);
            if (location != null) {

                if (mCurrentLocation == null) {

                    if (mGoogleMap != null) {
                        mGoogleMap.setMyLocationEnabled(true);
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), Constant.ZOOM_LOCATION));
                    }
                    mCurrentLocation = location;
                }

                if (mGoogleMap != null) {
                    if (!mGoogleMap.isMyLocationEnabled()){
                        mGoogleMap.setMyLocationEnabled(true);
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }

                    if (isNetworkConnected()) {
                        plotDriversWS(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    }
                }

                tapLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                startIntentService();

                /*if (mPositionMarker == null) {

                    mPositionMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .flat(true)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(getBitmap(R.drawable.ic_navigation)))
                            .anchor(0.5f, 0.5f)
                            .position(
                                    new LatLng(location.getLatitude(), location
                                            .getLongitude())));
                }

                animateMarker(mPositionMarker, location);*/

            }
        }
    }

    private AddressResultReceiver mResultReceiver;

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
        }
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

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
        return BR.homeViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel.class);
        return homeViewModel;
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
        activityHomeBinding = getViewDataBinding();
        homeViewModel.setNavigator(this);

        AppCenter.start(getApplication(), "4cc59ffc-4bfa-4e31-927c-31501cf17b71",
                Analytics.class, Crashes.class);

        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(HomeActivity.this, instanceIdResult -> {
                SharedData.saveString(HomeActivity.this, Constant.REFRESHED_TOKEN, instanceIdResult.getToken());

                if (isNetworkConnected()) {
                    homeViewModel.updateDeviceTokenWS(this);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientManager = SageApp.get().getChatClientManager();

        setupNavMenu();
        BlurSupport.addTo(activityHomeBinding.drawerLayout);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();
        // Check that the user hasn't revoked permissions by going to Settings.
        if (LocationUtils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        activityHomeBinding.helloName.setText(String.format("Hello, %s", SharedData.getString(this, Constant.FIRST_NAME)));
        initializeMap();

        if (isNetworkConnected()) {
            initializeChatClient();
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }

        clearAllNotifications();
    }


    private void initializeChatClient() {
        clientManager.connectClient(new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Constant.showSuccessToast("Twilio connected", getApplicationContext());
                registerToken();

            }

            @Override
            public void onError(String errorMessage) {
                //Constant.showErrorToast("Twilio error : " + errorMessage, getApplicationContext());
            }
        });
    }

    private void registerToken() {
        String token = SharedData.getString(this, Constant.REFRESHED_TOKEN);

        if (token != null && !token.equalsIgnoreCase("")) {
            ChatClient.FCMToken fcmToken = new ChatClient.FCMToken(token);
            clientManager.getChatClient().registerFCMToken(fcmToken, new StatusListener() {
                @Override
                public void onSuccess() {
                    Log.e("Registered", "Successful");
                }
            });
        }
    }

    private void setupNavMenu() {

        activityHomeBinding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
                openActivity(getApplicationContext(), AccountSettingsActivity.class, false, false);
                slideBottomToTop();
            }
        });

        activityHomeBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NotNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NotNull View view) {
            }

            @Override
            public void onDrawerClosed(@NotNull View view) {
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            finish();
        }
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
            mapFragment.getMapAsync(HomeActivity.this);
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

       /* Objects.requireNonNull(mGoogleMap).setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                activityHomeBinding.navigationIcon.setVisibility(View.VISIBLE);
            }
        });

        Objects.requireNonNull(mGoogleMap).setOnCameraIdleListener(() -> {

            LatLng tapLoc = mGoogleMap.getCameraPosition().target;
            if (mCurrentLocation != null) {
                if (Math.floor(mCurrentLocation.getLatitude() * 10000) == Math.floor(tapLoc.latitude * 10000)) {
                    activityHomeBinding.navigationIcon.setVisibility(View.GONE);
                } else {
                    activityHomeBinding.navigationIcon.setVisibility(View.VISIBLE);
                }
            } else {
                activityHomeBinding.navigationIcon.setVisibility(View.VISIBLE);
            }
        });*/
    }

    @Override
    public void backButton() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void openMenu() {
        activityHomeBinding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void clickNavigation() {
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constant.ZOOM_LOCATION));
            activityHomeBinding.navigationIcon.setVisibility(View.GONE);
        } else {
            Constant.showErrorToast(getResources().getString(R.string.location_not_found), this);
        }
    }

    @Override
    public void successAPI(UpdateDeviceTokenResponse updateDeviceTokenResponse) {

    }

    @Override
    public void errorAPI(ANError anError) {

    }

    @Override
    public void navClickTripHistory() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), TripHistoryActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickPayments() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), PaymentActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickSavedPlaces() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), SavedPlacesActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickPromo() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), PromoCodeActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickFreeRides() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), FreeRidesActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickHelp() {
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), HelpActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void successSavedPlacesAPI(GetAllPlacesResponse getAllPlacesResponse) {
        try {

            if (getAllPlacesResponse.getStatus() != null && getAllPlacesResponse.getStatus().equalsIgnoreCase("true")) {
                if (getAllPlacesResponse.getData() != null && getAllPlacesResponse.getData().size() > 0) {
                    SavedPlacesHomeAdapter savedPlacesHomeAdapter = new SavedPlacesHomeAdapter(this, getAllPlacesResponse.getData());
                    activityHomeBinding.savedPlacesRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    savedPlacesHomeAdapter.setSavedPlacesClick(this);
                    activityHomeBinding.savedPlacesRecyclerview.setAdapter(savedPlacesHomeAdapter);
                    savedPlacesHomeAdapter.notifyDataSetChanged();
                    activityHomeBinding.savedPlacesRecyclerview.setVisibility(View.VISIBLE);

                } else {
                    activityHomeBinding.savedPlacesRecyclerview.setVisibility(View.GONE);
                }
            } else {
                activityHomeBinding.savedPlacesRecyclerview.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
     * Creates a callback for receiving location events.
     */

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
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
        }
    }

    public boolean isGPSEnabled(Context context) {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return Objects.requireNonNull(manager).isProviderEnabled(LocationManager.GPS_PROVIDER);
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
                                rae.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
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

        updateProfileData();

        if (isNetworkConnected()) {
            homeViewModel.getAllPlacesWS(this);
        }
    }

    private void updateProfileData() {

        String profilePic = SharedData.getString(this, Constant.PROFILE_PIC);
        if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
            Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityHomeBinding.ivProfilePic);
        } else {
            Glide.with(this).load(R.drawable.profile_avatar).into(activityHomeBinding.ivProfilePic);
        }

        String email = SharedData.getString(this, Constant.EMAIL);
        String fname = SharedData.getString(this, Constant.FIRST_NAME);
        String lname = SharedData.getString(this, Constant.LAST_NAME);
        String phone = SharedData.getString(this, Constant.PHONE_NUMBER);

        activityHomeBinding.tvEmail.setText(phone);
        activityHomeBinding.tvName.setText(fname);
    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

        super.onPause();
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
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this,
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

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    void plotDriversWS(double lat, double lng) {

        String userid = SharedData.getString(this, Constant.USERID);

        PlotDriversMainRequest plotDriversMainRequest = new PlotDriversMainRequest();
        PlotDriversRequestData plotDriversRequestData = new PlotDriversRequestData();
        PlotDriversDatum plotDriversDatum = new PlotDriversDatum();

        plotDriversDatum.setLat(String.valueOf(lat));
        plotDriversDatum.setLng(String.valueOf(lng));
        plotDriversDatum.setUserid(userid);
        plotDriversDatum.setDevicetype("Android");

        plotDriversRequestData.setApikey("aGVhbHRoYm90d2l0aG1pbmRjcmV3dGVjaG5vbG9naWVz");
        plotDriversRequestData.setRequestType("nearByMyDriver");
        plotDriversRequestData.setData(plotDriversDatum);

        plotDriversMainRequest.setRequestData(plotDriversRequestData);

        AndroidNetworking.post(ApiConstants.UpdateLocation)
                .addApplicationJsonBody(plotDriversMainRequest)
                .setTag("nearByMyDriver")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(PlotDriversMainResponse.class, new ParsedRequestListener<PlotDriversMainResponse>() {

                    @Override
                    public void onResponse(PlotDriversMainResponse response) {
                        successAPIPlotDrivers(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPIPlotDrivers(anError);
                    }
                });
    }

    private void successAPIPlotDrivers(PlotDriversMainResponse response) {

        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (plotDriversDataList != null && plotDriversDataList.size() > 0) {
                    plotDriversDataList.clear();
                }

                plotDriversDataList = response.getData();

                if (plotDriversDataList != null && plotDriversDataList.size() > 0) {

                    if (mGoogleMap != null) {
                        mGoogleMap.clear();

                        for (int i = 0; i < plotDriversDataList.size(); i++) {
                            addMarkerDrivers(plotDriversDataList.get(i).getLat(), plotDriversDataList.get(i).getLng());
                        }
                    }
                } else {
                    if (mGoogleMap != null) {
                        mGoogleMap.clear();
                    }
                }
            } else {

                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                }

                if (!SharedData.getBool(this, Constant.NO_CARS)) {
                    SharedData.saveBool(this, Constant.NO_CARS, true);

                    NoCarFragment fragment = NoCarFragment.newInstance();
                    fragment.setCancelable(false);
                    fragment.show(getSupportFragmentManager(), "nocar");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMarkerDrivers(String lat, String lng) {

        mGoogleMap.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getBitmap(R.drawable.ic_driver)))
                .anchor(0.5f, 0.5f)
                .position(
                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
    }

    public void errorAPIPlotDrivers(ANError anError) {
        hideLoading();
        vibrate();
    }


    @Override
    public void chooseDestination() {

        if (tapLocation != null && !mAddressOutput.equalsIgnoreCase("")) {
            Intent intent = new Intent(this, SetSourceAddressActivity.class);
            intent.putExtra("ADDRESS", mAddressOutput);
            intent.putExtra("LAT_LNG", tapLocation);
            startActivity(intent);
            slideBottomToTop();
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.location_not_found), this);
        }


        /*Intent intent = new Intent(this, ChatModuleNewActivity.class);
        startActivity(intent);
        slideBottomToTop();*/
    }
}

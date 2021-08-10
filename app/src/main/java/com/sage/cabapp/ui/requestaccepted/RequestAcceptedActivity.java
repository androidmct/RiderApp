package com.sage.cabapp.ui.requestaccepted;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
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
import com.sage.cabapp.databinding.ActivityRequestAcceptedBinding;
import com.sage.cabapp.services.LocationUtils;
import com.sage.cabapp.services.SageLocationService;
import com.sage.cabapp.twilio.chat.ChatClientManager;
import com.sage.cabapp.twilio.chat.TaskCompletionListener;
import com.sage.cabapp.ui.accountsettings.AccountSettingsActivity;
import com.sage.cabapp.ui.addriverrating.AddDriverRatingActivity;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.chatmodulenew.ChatModuleNewActivity;
import com.sage.cabapp.ui.chatmodulenew.model.CheckLastActiveChannelResponse;
import com.sage.cabapp.ui.driverprofile.DriverProfileActivity;
import com.sage.cabapp.ui.freerides.FreeRidesActivity;
import com.sage.cabapp.ui.help.HelpActivity;
import com.sage.cabapp.ui.main.HomeActivity;
import com.sage.cabapp.ui.payment.PaymentActivity;
import com.sage.cabapp.ui.promocode.PromoCodeActivity;
import com.sage.cabapp.ui.requestaccepted.fragment.CancelRequestFragment;
import com.sage.cabapp.ui.requestaccepted.fragment.CancelRequestPaidFragment;
import com.sage.cabapp.ui.requestaccepted.model.CheckAcceptedRideStatusResponse;
import com.sage.cabapp.ui.requestaccepted.model.CheckCancelTimeMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedDriverData;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedMainResponse;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedResponseData;
import com.sage.cabapp.ui.requestaccepted.model.RideAcceptedVehicleData;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.ui.savedplaces.SavedPlacesActivity;
import com.sage.cabapp.ui.setaddress.SetSourceAddressActivity;
import com.sage.cabapp.ui.triphistory.TripHistoryActivity;
import com.sage.cabapp.ui.updateroute.UpdateRouteActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.TouchableFrameLayout;
import com.twilio.chat.ChatClient;
import com.twilio.chat.StatusListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RequestAcceptedActivity extends BaseActivity<ActivityRequestAcceptedBinding, RequestAcceptedViewModel> implements RequestAcceptedNavigator, OnMapReadyCallback, TouchableFrameLayout.OnChangeListener {

    private ChatClientManager clientManager;

    @Inject
    ViewModelProviderFactory factory;
    private RequestAcceptedViewModel requestAcceptedViewModel;
    ActivityRequestAcceptedBinding activityRequestAcceptedBinding;
    private GoogleMap mGoogleMap;
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

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates = false;
    ArrayList<LatLng> markerPoints = new <LatLng>ArrayList();

    @Override
    public void onInteractionEnd() {
        if (activityRequestAcceptedBinding.navigationIcon.getVisibility() == View.GONE && activityRequestAcceptedBinding.emergency911.getVisibility() == View.VISIBLE)
            activityRequestAcceptedBinding.navigationIcon.setVisibility(View.VISIBLE);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(SageLocationService.EXTRA_LOCATION);
            if (location != null) {
                mCurrentLocation = location;
            }
        }
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


    RideAcceptedDriverData rideAcceptedDriverData = null;
    RideAcceptedResponseData rideAcceptedResponseData = null;
    RideAcceptedVehicleData rideAcceptedVehicleData = null;

    LatLng sourceAddressLatLng = null;
    LatLng addStopAddressLatLng = null;
    LatLng destinationAddressLatLng = null;
    LatLng driverLatLng = null;
    String arriveText = "Driver arrives in ";

    @Override
    public int getBindingVariable() {
        return BR.requestAcceptViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_accepted;
    }

    @Override
    public RequestAcceptedViewModel getViewModel() {
        requestAcceptedViewModel = ViewModelProviders.of(this, factory).get(RequestAcceptedViewModel.class);
        return requestAcceptedViewModel;
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
        activityRequestAcceptedBinding = getViewDataBinding();
        requestAcceptedViewModel.setNavigator(this);

        AppCenter.start(getApplication(), "4cc59ffc-4bfa-4e31-927c-31501cf17b71",
                Analytics.class, Crashes.class);

        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RequestAcceptedActivity.this, instanceIdResult -> {
                SharedData.saveString(RequestAcceptedActivity.this, Constant.REFRESHED_TOKEN, instanceIdResult.getToken());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientManager = SageApp.get().getChatClientManager();

        setupNavMenu();
        BlurSupport.addTo(activityRequestAcceptedBinding.drawerLayout);

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

        initializeMap();

        boolean isRideStarted = SharedData.getBool(this, Constant.RIDE_STARTED);
        if (!isRideStarted) {
            activityRequestAcceptedBinding.cancelRequest.setEnabled(true);
            activityRequestAcceptedBinding.cancelRequest.setClickable(true);
            activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            activityRequestAcceptedBinding.cancelRequest.setEnabled(false);
            activityRequestAcceptedBinding.cancelRequest.setClickable(false);
            activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }

        if (isNetworkConnected()) {

            String requestID = SharedData.getString(this, Constant.PERM_REQUEST_ID);
            if (requestID != null && !requestID.equalsIgnoreCase("")) {
                requestAcceptedViewModel.acceptedRequestedWS(this);
            } else {
                vibrate();
                Constant.showErrorToast("No request found", this);
                finish();
                slideLeftToRight();
            }
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            finish();
            slideLeftToRight();
        }

        if (isNetworkConnected()) {
            requestAcceptedViewModel.getDefaultPaymentWS(this);
            initializeChatClient();
        }
    }

    private void initializeChatClient() {
        clientManager.connectClient(new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Constant.showSuccessToast("Twilio connected", getApplicationContext());
                registerToken();
            }

            @Override
            public void onError(String errorMessage) {
                // Constant.showErrorToast("Twilio error : " + errorMessage, getApplicationContext());
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

                }
            });
        }
    }

    private void updateProfileData() {

        String profilePic = SharedData.getString(this, Constant.PROFILE_PIC);
        if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
            Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityRequestAcceptedBinding.ivProfilePic);
        } else {
            Glide.with(this).load(R.drawable.profile_avatar).into(activityRequestAcceptedBinding.ivProfilePic);
        }

        String email = SharedData.getString(this, Constant.EMAIL);
        String fname = SharedData.getString(this, Constant.FIRST_NAME);
        String lname = SharedData.getString(this, Constant.LAST_NAME);
        String phone = SharedData.getString(this, Constant.PHONE_NUMBER);

        activityRequestAcceptedBinding.tvEmail.setText(phone);
        activityRequestAcceptedBinding.tvName.setText(fname);
    }

    private void setupNavMenu() {

        activityRequestAcceptedBinding.driverProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(getApplicationContext(), DriverProfileActivity.class, false, false);
                slideRightToLeft();
            }
        });

        activityRequestAcceptedBinding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
                openActivity(getApplicationContext(), AccountSettingsActivity.class, false, false);
                slideBottomToTop();
            }
        });

        activityRequestAcceptedBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
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
    public void openMenu() {
        activityRequestAcceptedBinding.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void cancelRequest() {

        if (isNetworkConnected()) {
            showLoading("");
            requestAcceptedViewModel.cancelRequestTimeWS(this);
        } else {
            vibrate();
            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
        }
    }

    Route finalRoute = null;

    @Override
    public void call911() {
        callPhone("911", this);
    }

    @Override
    public void clickNavigation() {
        if (mCurrentLocation != null) {

            if (mGoogleMap != null && finalRoute != null) {
                // LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constant.ZOOM_LOCATION));

                activityRequestAcceptedBinding.navigationIcon.setVisibility(View.GONE);
                try {
                    setCameraWithCoordinationBounds(finalRoute);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Constant.showErrorToast(getResources().getString(R.string.location_not_found), this);
        }
    }

    @Override
    public void phoneCall() {
        if (rideAcceptedDriverData != null) {
            String mobile = decodeBase64(rideAcceptedDriverData.getMobile());
            callPhone(mobile, this);
        } else {
            Constant.showErrorToast("No driver number found", this);
        }

    }

    @Override
    public void clickUpdateAddress() {
        if (rideAcceptedResponseData != null) {

            String exAddress = "";

            double slat = Double.parseDouble(rideAcceptedResponseData.getSLat());
            double slng = Double.parseDouble(rideAcceptedResponseData.getSLng());
            LatLng sLatLng = new LatLng(slat, slng);

            double dlat = Double.parseDouble(rideAcceptedResponseData.getDLat());
            double dlng = Double.parseDouble(rideAcceptedResponseData.getDLng());
            LatLng dLatLng = new LatLng(dlat, dlng);

            double exlat = 0.0;
            double exlng = 0.0;
            LatLng exLatLng = null;

            if (rideAcceptedResponseData.getExAddress() != null && !rideAcceptedResponseData.getExAddress().equalsIgnoreCase("")) {
                exAddress = rideAcceptedResponseData.getExAddress();
                exlat = Double.parseDouble(rideAcceptedResponseData.getExLat());
                exlng = Double.parseDouble(rideAcceptedResponseData.getExLng());
                exLatLng = new LatLng(exlat, exlng);
            }

            Intent intent = new Intent(this, UpdateRouteActivity.class);
            intent.putExtra("ADDRESS", rideAcceptedResponseData.getSAddress());
            intent.putExtra("LAT_LNG", sLatLng);
            intent.putExtra("DEST_LAT_LNG", dLatLng);
            intent.putExtra("DEST_ADDRESS", rideAcceptedResponseData.getDAddress());
            intent.putExtra("EX_LAT_LNG", exLatLng);
            intent.putExtra("EX_ADDRESS", exAddress);
            intent.putExtra("requestID", rideAcceptedResponseData.getRequestId());
            startActivity(intent);
            slideBottomToTop();
        } else {
            Intent intent = new Intent(this, UpdateRouteActivity.class);
            startActivity(intent);
            slideBottomToTop();
        }
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String time = mdformat.format(calendar.getTime());
        return time;
    }


    @Override
    public void chatMessage() {

        if (rideAcceptedDriverData != null && rideAcceptedResponseData != null) {
            if (isNetworkConnected()) {
                showLoading("");
                requestAcceptedViewModel.riderTwilioWS(this);
            } else {
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        } else {
            vibrate();
            Constant.showErrorToast("No driver data available for chat", this);
        }
    }


    @Override
    public void openDetails() {

        if (activityRequestAcceptedBinding.detailsLayout.getVisibility() == View.GONE) {
            activityRequestAcceptedBinding.detailsLayout.setVisibility(View.VISIBLE);
            activityRequestAcceptedBinding.arrowUpDown.setBackgroundResource(R.drawable.ic_arrow_down);
            activityRequestAcceptedBinding.emergency911.setVisibility(View.GONE);
            activityRequestAcceptedBinding.navigationIcon.setVisibility(View.GONE);
        } else {
            activityRequestAcceptedBinding.detailsLayout.setVisibility(View.GONE);
            activityRequestAcceptedBinding.arrowUpDown.setBackgroundResource(R.drawable.ic_arrow_up);
            activityRequestAcceptedBinding.emergency911.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkDriver() {
        openActivity(this, DriverProfileActivity.class, false, false);
        slideRightToLeft();
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
            mapFragment.getMapAsync(RequestAcceptedActivity.this);
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
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), Constant.ZOOM_LOCATION));

            }
        };
    }

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
                                rae.startResolutionForResult(RequestAcceptedActivity.this, REQUEST_CHECK_SETTINGS);
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
                            ActivityCompat.requestPermissions(RequestAcceptedActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(RequestAcceptedActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(RequestAcceptedActivity.this,
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
    public void sageAcceptedResponse(RideAcceptedMainResponse response) {

        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getRequestData() != null && response.getDriverData() != null) {

                    rideAcceptedDriverData = response.getDriverData();
                    rideAcceptedResponseData = response.getRequestData();
                    rideAcceptedVehicleData = response.getDriverVehicle();

                    SharedData.saveString(this, Constant.DRIVER_USERID, rideAcceptedDriverData.getUserid());
                    SharedData.saveString(this, Constant.DRIVER_NAME, rideAcceptedDriverData.getFname());
                    SharedData.saveString(this, Constant.DRIVER_PIC, rideAcceptedDriverData.getProfile());

                    activityRequestAcceptedBinding.driverName.setText(rideAcceptedDriverData.getFname());

                    String profilePic = rideAcceptedDriverData.getProfile();
                    if (profilePic != null && !profilePic.equalsIgnoreCase("")) {
                        Glide.with(this).load(profilePic).apply(new RequestOptions().placeholder(R.drawable.profile_avatar)).into(activityRequestAcceptedBinding.driverProfilePic);
                    } else {
                        Glide.with(this).load(R.drawable.profile_avatar).into(activityRequestAcceptedBinding.driverProfilePic);
                    }

                    activityRequestAcceptedBinding.sourceAddress.setText(rideAcceptedResponseData.getSAddress());
                    activityRequestAcceptedBinding.destinationAddress.setText(rideAcceptedResponseData.getDAddress());
                    activityRequestAcceptedBinding.tripFare.setText(rideAcceptedResponseData.getRideFare());

                    activityRequestAcceptedBinding.driverRating.setText(rideAcceptedVehicleData.getDriverRating());
                    activityRequestAcceptedBinding.driverPlateNumber.setText(rideAcceptedVehicleData.getVehiclePlateNumber());
                    activityRequestAcceptedBinding.vehicleModel.setText(String.format("%s %s", rideAcceptedVehicleData.getVehicleMake(), rideAcceptedVehicleData.getVehicleModel()));

                    String status = rideAcceptedResponseData.getRideStatus();

                    if (status.equalsIgnoreCase("start")) {
                        SharedData.saveBool(this, Constant.RIDE_STARTED, true);
                    } else {
                        SharedData.saveBool(this, Constant.RIDE_STARTED, false);
                    }

                    boolean isRideStarted = SharedData.getBool(this, Constant.RIDE_STARTED);
                    if (!isRideStarted) {
                        activityRequestAcceptedBinding.cancelRequest.setEnabled(true);
                        activityRequestAcceptedBinding.cancelRequest.setClickable(true);
                        activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        arriveText = "Driver arrives in ";
                        if (isNetworkConnected()) {
                            maroofRouteBeforeStarted();
                        }
                    } else {
                        activityRequestAcceptedBinding.cancelRequest.setEnabled(false);
                        activityRequestAcceptedBinding.cancelRequest.setClickable(false);
                        activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                    }

                    if (isNetworkConnected()) {
                        requestAcceptedViewModel.checkRideStatusWS(getApplicationContext(), true);
                    }

                } else {
                    Constant.showErrorToast(response.getMessage(), this);
                }
            } else {
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void maroofRouteBeforeStarted() {

        if (rideAcceptedResponseData.getExLat() != null && !rideAcceptedResponseData.getExLat().equalsIgnoreCase("")) {
            sourceAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getSLat()), Double.parseDouble(rideAcceptedResponseData.getSLng()));
            addStopAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getExLat()), Double.parseDouble(rideAcceptedResponseData.getExLng()));
            destinationAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getDLat()), Double.parseDouble(rideAcceptedResponseData.getDLng()));
            driverLatLng = new LatLng(Double.parseDouble(rideAcceptedDriverData.getLat()), Double.parseDouble(rideAcceptedDriverData.getLng()));

            addRouteWaypointsBeforeStarted();
        } else {

            sourceAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getSLat()), Double.parseDouble(rideAcceptedResponseData.getSLng()));
            destinationAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getDLat()), Double.parseDouble(rideAcceptedResponseData.getDLng()));
            driverLatLng = new LatLng(Double.parseDouble(rideAcceptedDriverData.getLat()), Double.parseDouble(rideAcceptedDriverData.getLng()));

            addRouteBeforeStarted();
        }
    }

    private void maroofRoute() {

        if (rideAcceptedResponseData.getExLat() != null && !rideAcceptedResponseData.getExLat().equalsIgnoreCase("")) {
            sourceAddressLatLng = driverLatLng;
            addStopAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getExLat()), Double.parseDouble(rideAcceptedResponseData.getExLng()));
            destinationAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getDLat()), Double.parseDouble(rideAcceptedResponseData.getDLng()));

            addRouteWaypoints();
        } else {

            sourceAddressLatLng = driverLatLng;
            destinationAddressLatLng = new LatLng(Double.parseDouble(rideAcceptedResponseData.getDLat()), Double.parseDouble(rideAcceptedResponseData.getDLng()));

            addRoute();
        }
    }

    @Override
    public void checkCancelTime(CheckCancelTimeMainResponse checkCancelTimeMainResponse) {
        hideLoading();
        try {

            if (checkCancelTimeMainResponse.getStatus() != null && checkCancelTimeMainResponse.getStatus().equalsIgnoreCase("true")) {
                if (checkCancelTimeMainResponse.getMinutes() != null && checkCancelTimeMainResponse.getMinutes().equalsIgnoreCase("true")) {

                    CancelRequestPaidFragment fragment = CancelRequestPaidFragment.newInstance();
                    fragment.setCancelable(false);
                    fragment.show(getSupportFragmentManager(), "cancelpaid");
                } else {
                    CancelRequestFragment fragment = CancelRequestFragment.newInstance();
                    fragment.setCancelable(false);
                    fragment.show(getSupportFragmentManager(), "cancel");
                }
            } else {
                vibrate();
                Constant.showErrorToast(checkCancelTimeMainResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (activityRequestAcceptedBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (fiveSecondsTimer != null) {
            fiveSecondsTimer.cancel();
            fiveSecondsTimer = null;
        }
    }

    @Override
    public void errorAPI(ANError anError) {

        hideLoading();
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void errorAPIStatus(ANError anError) {

        add5SecTimer();
    }

    @Override
    public void sageSageStatusResponse(CheckAcceptedRideStatusResponse checkAcceptedRideStatusResponse, boolean value) {
        try {

            if (checkAcceptedRideStatusResponse.getStatus() != null && checkAcceptedRideStatusResponse.getStatus().equalsIgnoreCase("true")) {

                if (value && !valueDone) {
                    valueDone = true;
                    if (checkAcceptedRideStatusResponse.getDlat() != null && !checkAcceptedRideStatusResponse.getDlat().equalsIgnoreCase("")) {

                        if (checkAcceptedRideStatusResponse.getRideCompleted().equalsIgnoreCase("true")) {
                            SharedData.saveString(this, Constant.RIDE_FARE, checkAcceptedRideStatusResponse.getTripFare());
                            if (getWindow().getDecorView().isShown()) {
                                openActivity(this, AddDriverRatingActivity.class, false, true);
                                slideRightToLeft();
                            }
                        } else if (checkAcceptedRideStatusResponse.getRideCancelled().equalsIgnoreCase("true")) {
                            openActivity(this, HomeActivity.class, false, true);
                            innToOut();
                        } else if (checkAcceptedRideStatusResponse.getRideStarted().equalsIgnoreCase("true")) {

                            if (fiveSecondsTimer != null) {
                                fiveSecondsTimer.cancel();
                                fiveSecondsTimer = null;
                            }

                            add5SecTimer();

                            arriveText = "Drop-off in ";

                            SharedData.saveBool(this, Constant.RIDE_STARTED, true);

                            boolean isRideStarted = SharedData.getBool(this, Constant.RIDE_STARTED);
                            if (!isRideStarted) {
                                activityRequestAcceptedBinding.cancelRequest.setEnabled(true);
                                activityRequestAcceptedBinding.cancelRequest.setClickable(true);
                                activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

                            } else {
                                activityRequestAcceptedBinding.cancelRequest.setEnabled(false);
                                activityRequestAcceptedBinding.cancelRequest.setClickable(false);
                                activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                            }

                            driverLatLng = new LatLng(Double.parseDouble(checkAcceptedRideStatusResponse.getDlat()), Double.parseDouble(checkAcceptedRideStatusResponse.getDlng()));

                            if (isNetworkConnected()) {
                                maroofRoute();
                            }

                        } else {
                            if (fiveSecondsTimer != null) {
                                fiveSecondsTimer.cancel();
                                fiveSecondsTimer = null;
                            }

                            add5SecTimer();

                            driverLatLng = new LatLng(Double.parseDouble(checkAcceptedRideStatusResponse.getDlat()), Double.parseDouble(checkAcceptedRideStatusResponse.getDlng()));
                            arriveText = "Driver arrives in ";
                            if (isNetworkConnected()) {
                                maroofRouteBeforeStarted();
                            }
                        }
                    }
                } else {
                    if (fiveSecondsTimer != null) {
                        fiveSecondsTimer.cancel();
                        fiveSecondsTimer = null;
                        if (checkAcceptedRideStatusResponse.getDlat() != null && !checkAcceptedRideStatusResponse.getDlat().equalsIgnoreCase("")) {

                            if (checkAcceptedRideStatusResponse.getRideCompleted().equalsIgnoreCase("true")) {
                                SharedData.saveString(this, Constant.RIDE_FARE, checkAcceptedRideStatusResponse.getTripFare());
                                if (getWindow().getDecorView().isShown()) {
                                    openActivity(this, AddDriverRatingActivity.class, false, true);
                                    slideRightToLeft();
                                }
                            } else if (checkAcceptedRideStatusResponse.getRideCancelled().equalsIgnoreCase("true")) {
                                openActivity(this, HomeActivity.class, false, true);
                                innToOut();
                            } else if (checkAcceptedRideStatusResponse.getRideStarted().equalsIgnoreCase("true")) {

                                if (fiveSecondsTimer != null) {
                                    fiveSecondsTimer.cancel();
                                    fiveSecondsTimer = null;
                                }

                                add5SecTimer();

                                arriveText = "Drop-off in ";

                                SharedData.saveBool(this, Constant.RIDE_STARTED, true);

                                boolean isRideStarted = SharedData.getBool(this, Constant.RIDE_STARTED);
                                if (!isRideStarted) {
                                    activityRequestAcceptedBinding.cancelRequest.setEnabled(true);
                                    activityRequestAcceptedBinding.cancelRequest.setClickable(true);
                                    activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

                                } else {
                                    activityRequestAcceptedBinding.cancelRequest.setEnabled(false);
                                    activityRequestAcceptedBinding.cancelRequest.setClickable(false);
                                    activityRequestAcceptedBinding.cancelRequest.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                                }

                                driverLatLng = new LatLng(Double.parseDouble(checkAcceptedRideStatusResponse.getDlat()), Double.parseDouble(checkAcceptedRideStatusResponse.getDlng()));

                                if (isNetworkConnected()) {
                                    maroofRoute();
                                }

                            } else {
                                if (fiveSecondsTimer != null) {
                                    fiveSecondsTimer.cancel();
                                    fiveSecondsTimer = null;
                                }

                                add5SecTimer();

                                driverLatLng = new LatLng(Double.parseDouble(checkAcceptedRideStatusResponse.getDlat()), Double.parseDouble(checkAcceptedRideStatusResponse.getDlng()));
                                arriveText = "Driver arrives in ";
                                if (isNetworkConnected()) {
                                    maroofRouteBeforeStarted();
                                }
                            }
                        }
                    }
                }

            } else {
                if (fiveSecondsTimer != null) {
                    fiveSecondsTimer.cancel();
                    fiveSecondsTimer = null;
                }

                add5SecTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (fiveSecondsTimer != null) {
                fiveSecondsTimer.cancel();
                fiveSecondsTimer = null;
            }

            add5SecTimer();
        }
    }

    @Override
    public void navClickTripHistory() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), TripHistoryActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickPayments() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), PaymentActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickSavedPlaces() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), SavedPlacesActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickPromo() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), PromoCodeActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickFreeRides() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), FreeRidesActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void navClickHelp() {
        activityRequestAcceptedBinding.drawerLayout.closeDrawer(GravityCompat.START);
        openActivity(getApplicationContext(), HelpActivity.class, false, false);
        slideBottomToTop();
    }

    @Override
    public void successPaymentsAPI(GetDefaultPaymentResponse response) {

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getPersonalBrand() != null && !response.getPersonalBrand().equalsIgnoreCase("")) {
                    if (response.getPersonalBrand().equalsIgnoreCase("visa")) {
                        activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_visa);
                    } else if (response.getPersonalBrand().equalsIgnoreCase("mastercard")) {
                        activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_master_card);
                    } else if (response.getPersonalBrand().equalsIgnoreCase("amex")) {
                        activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_amex);
                    } else if (response.getPersonalBrand().equalsIgnoreCase("discover")) {
                        activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_discover);
                    } else {
                        activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_method);
                    }
                } else {
                    activityRequestAcceptedBinding.visaIcon.setBackgroundResource(R.drawable.payment_ic_method);
                }

                activityRequestAcceptedBinding.cardNumber.setText(String.format("•••• •••• •••• %s", response.getPersonalId()));
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void checkLastTwilioChatResponse(CheckLastActiveChannelResponse response) {

        hideLoading();
        try {
            if (response.getStatus() != null && !response.getStatus().equalsIgnoreCase("true")) {
                if (response.getResult() != null) {
                    Intent intent = new Intent(this, ChatModuleNewActivity.class);
                    intent.putExtra("REQUEST_ID", rideAcceptedResponseData.getRequestId());
                    intent.putExtra("DRIVER_NAME", rideAcceptedDriverData.getFname());
                    intent.putExtra("DRIVER_ID", rideAcceptedDriverData.getUserid());
                    intent.putExtra("DRIVER_PIC", rideAcceptedDriverData.getProfile());
                    startActivity(intent);
                    slideBottomToTop();
                } else {
                    Intent intent = new Intent(this, ChatModuleNewActivity.class);
                    intent.putExtra("REQUEST_ID", rideAcceptedResponseData.getRequestId());
                    intent.putExtra("DRIVER_NAME", rideAcceptedDriverData.getFname());
                    intent.putExtra("DRIVER_ID", rideAcceptedDriverData.getUserid());
                    intent.putExtra("DRIVER_PIC", rideAcceptedDriverData.getProfile());
                    startActivity(intent);
                    slideBottomToTop();
                }
            } else {
                Intent intent = new Intent(this, ChatModuleNewActivity.class);
                intent.putExtra("REQUEST_ID", rideAcceptedResponseData.getRequestId());
                intent.putExtra("DRIVER_NAME", rideAcceptedDriverData.getFname());
                intent.putExtra("DRIVER_ID", rideAcceptedDriverData.getUserid());
                intent.putExtra("DRIVER_PIC", rideAcceptedDriverData.getProfile());
                startActivity(intent);
                slideBottomToTop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRouteWaypointsBeforeStarted() {

        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(driverLatLng)
                .and(sourceAddressLatLng)
                .and(addStopAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirectionWayPoints11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void addRouteBeforeStarted() {

        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(driverLatLng)
                .and(sourceAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirection11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void onDirection(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        mGoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, ContextCompat.getColor(this, R.color.colorPrimary)));

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(driverLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_driver)));
        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirection11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);
        finalRoute = route;

        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        ArrayList<LatLng> directionPositionList1 = route.getLegList().get(1).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng addStop = directionPositionList.get(directionPositionList.size() - 1);
        LatLng dest = directionPositionList1.get(directionPositionList1.size() - 1);

        ArrayList<LatLng> finalPositionsList = new ArrayList<>();
        finalPositionsList.addAll(directionPositionList);
        finalPositionsList.addAll(directionPositionList1);
        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) finalPositionsList.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) finalPositionsList.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) finalPositionsList.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < finalPositionsList.size() - 1; i++) {
            builder.include(finalPositionsList.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(finalPositionsList.get(i)).add(finalPositionsList.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_driver)));
        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStop).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirectionWayPoints(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        int legCount = route.getLegList().size();

        for (int i = 0; i < legCount; i++) {
            Leg leg = route.getLegList().get(i);
            List<Step> stepList = leg.getStepList();
            List<PolylineOptions> polylineOptionList = new ArrayList<>();

            if (i == 0) {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, ContextCompat.getColor(this, R.color.colorPrimary), 3, Color.BLUE);
            } else {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            }

            for (PolylineOptions polylineOption : polylineOptionList) {
                if (mGoogleMap != null) {
                    mGoogleMap.addPolyline(polylineOption);
                }
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(driverLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_driver)));
        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStopAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirectionWayPoints11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);
        finalRoute = route;

        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        ArrayList<LatLng> directionPositionList1 = route.getLegList().get(1).getDirectionPoint();
        ArrayList<LatLng> directionPositionList2 = route.getLegList().get(2).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng addStop = directionPositionList1.get(0);
        LatLng addStop1 = directionPositionList1.get(directionPositionList1.size() - 1);
        LatLng dest = directionPositionList2.get(directionPositionList2.size() - 1);

        ArrayList<LatLng> directionPositionListFinal = new ArrayList<>();
        directionPositionListFinal.addAll(directionPositionList);
        directionPositionListFinal.addAll(directionPositionList1);
        directionPositionListFinal.addAll(directionPositionList2);

        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) directionPositionListFinal.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) directionPositionListFinal.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) directionPositionListFinal.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < directionPositionListFinal.size() - 1; i++) {
            builder.include(directionPositionListFinal.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(directionPositionListFinal.get(i)).add(directionPositionListFinal.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_driver)));
        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStop1).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStop).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRouteWaypoints() {

        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(sourceAddressLatLng)
                .and(addStopAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirectionWayPointsStarted11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void onDirectionWayPointsStarted(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        finalRoute = route;
        int legCount = route.getLegList().size();

        for (int i = 0; i < legCount; i++) {
            Leg leg = route.getLegList().get(i);
            List<Step> stepList = leg.getStepList();
            List<PolylineOptions> polylineOptionList = new ArrayList<>();

            if (i == 0) {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, ContextCompat.getColor(this, R.color.colorPrimary), 3, Color.BLUE);
            } else {
                polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            }

            for (PolylineOptions polylineOption : polylineOptionList) {
                if (mGoogleMap != null) {
                    mGoogleMap.addPolyline(polylineOption);
                }
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStopAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Polyline line;
    List<PolylineOptions> gradientpoly = new ArrayList<>();

    private void onDirectionWayPointsStarted11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);
        finalRoute = route;
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        ArrayList<LatLng> directionPositionList1 = route.getLegList().get(1).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng addStop = directionPositionList1.get(0);
        LatLng dest = directionPositionList1.get(directionPositionList1.size() - 1);

        ArrayList<LatLng> directionPositionListFinal = new ArrayList<>();
        directionPositionListFinal.addAll(directionPositionList);
        directionPositionListFinal.addAll(directionPositionList1);

        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) directionPositionListFinal.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) directionPositionListFinal.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) directionPositionListFinal.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < directionPositionListFinal.size() - 1; i++) {
            builder.include(directionPositionListFinal.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(directionPositionListFinal.get(i)).add(directionPositionListFinal.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStop).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0x0ff;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0x0ff;
    }

    private int getBlue(int rgb) {
        return (rgb) & 0x0ff;
    }

    private int getIntFromColor(float Red, float Green, float Blue) {
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);
        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;
        return 0xFF000000 | R | G | B;
    }

    private void addRoute() {

        GoogleDirection.withServerKey("AIzaSyAXTjpjJS6iEyHr_-LV1ZargDtGPo9B6yI")
                .from(sourceAddressLatLng)
                .to(destinationAddressLatLng)

                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        if (direction.isOK()) {
                            // Do something
                            onDirectionTripStarted11(direction);
                        } else {
                            // Do something
                            Constant.showErrorToast(direction.getErrorMessage(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                        Constant.showErrorToast(t.getMessage(), getApplicationContext());
                    }
                });
    }

    private void onDirectionTripStarted(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        mGoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, ContextCompat.getColor(this, R.color.colorPrimary)));

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirectionTripStarted11(Direction direction) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        if (gradientpoly != null && gradientpoly.size() > 0) {
            gradientpoly.clear();
        }

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

        LatLng source = directionPositionList.get(0);
        LatLng dest = directionPositionList.get(directionPositionList.size() - 1);

        int end_red = 100;
        int end_green = 100;
        int end_blue = 100;
        int red = 0;
        int green = 0;
        int blue = 0;

        end_red = getRed(Color.parseColor("#CC2F3E"));
        end_green = getGreen(Color.parseColor("#CC2F3E"));
        end_blue = getBlue(Color.parseColor("#CC2F3E"));

        red = getRed(Color.parseColor("#42B029"));
        green = getGreen(Color.parseColor("#42B029"));
        blue = getBlue(Color.parseColor("#42B029"));

        float redSteps;
        float greenSteps;
        float blueSteps;

        redSteps = ((float) (end_red - red) / 255) / (float) directionPositionList.size();
        greenSteps = ((float) (end_green - green) / 255) / (float) directionPositionList.size();
        blueSteps = ((float) (end_blue - blue) / 255) / (float) directionPositionList.size();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < directionPositionList.size() - 1; i++) {
            builder.include(directionPositionList.get(i));
            float redColor = ((float) red / 255) + (redSteps * i);
            float greenColor = ((float) green / 255) + (greenSteps * i);
            float blueColor = ((float) blue / 255) + (blueSteps * i);

            int color = getIntFromColor(redColor, greenColor, blueColor);
            gradientpoly.add(new PolylineOptions().width(8).color(color).geodesic(false).add(directionPositionList.get(i)).add(directionPositionList.get(i + 1)));
        }

        if (gradientpoly != null && gradientpoly.size() > 0) {
            for (PolylineOptions po : gradientpoly) {
                line = mGoogleMap.addPolyline(po);
                line.setStartCap(new RoundCap());
                line.setEndCap(new RoundCap());
            }
        }

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(dest).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.1);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        String time1 = route.getLegList().get(0).getDuration().getText();
        activityRequestAcceptedBinding.driverArriveMinutes.setText(arriveText + time1);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private CountDownTimer fiveSecondsTimer = null;
    private boolean valueDone = false;

    private void add5SecTimer() {

        fiveSecondsTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (fiveSecondsTimer != null) {
                    if (isNetworkConnected()) {
                        requestAcceptedViewModel.checkRideStatusWS(getApplicationContext(), false);
                    }
                }
            }
        }.start();
    }

}

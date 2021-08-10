package com.sage.cabapp.ui.requestforcar;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityRequestForCarBinding;
import com.sage.cabapp.services.LocationUtils;
import com.sage.cabapp.services.SageLocationService;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.confirmpickup.ConfirmPickupMapActivity;
import com.sage.cabapp.ui.main.model.PlotDriversData;
import com.sage.cabapp.ui.main.model.PlotDriversDatum;
import com.sage.cabapp.ui.main.model.PlotDriversMainRequest;
import com.sage.cabapp.ui.main.model.PlotDriversMainResponse;
import com.sage.cabapp.ui.main.model.PlotDriversRequestData;
import com.sage.cabapp.ui.paymentprofile.ChangePaymentProfileActivity;
import com.sage.cabapp.ui.requestforcar.adapter.RequestCarItemAdapter;
import com.sage.cabapp.ui.requestforcar.fragment.RequestSageDialogFragment;
import com.sage.cabapp.ui.requestforcar.model.AllCarsModel;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsData;
import com.sage.cabapp.ui.requestforcar.model.GetAllCarsResponse;
import com.sage.cabapp.ui.requestforcar.model.GetDefaultPaymentResponse;
import com.sage.cabapp.ui.requestforcar.model.NearestDriverDummy;
import com.sage.cabapp.ui.requestforcar.model.RequestSageMainResponseNew;
import com.sage.cabapp.ui.requestforcar.model.RequestSageResponse;
import com.sage.cabapp.ui.tripfare.TripFareActivity;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.sage.cabapp.utils.TouchableFrameLayout;
import com.takusemba.multisnaprecyclerview.MultiSnapHelper;
import com.takusemba.multisnaprecyclerview.SnapGravity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 16-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class RequestForCarActivity extends BaseActivity<ActivityRequestForCarBinding, RequestForCarViewModel> implements RequestForCarNavigator, OnMapReadyCallback, CallbackCarDetails, CallbackSelectCarDetails, TouchableFrameLayout.OnChangeListener {

    @Inject
    ViewModelProviderFactory factory;
    RequestForCarViewModel requestForCarViewModel;
    ActivityRequestForCarBinding activityRequestForCarBinding;

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

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    private Boolean mRequestingLocationUpdates = false;

    String carSeats = "4";
    private List<PlotDriversData> plotDriversDataList = new ArrayList<>();

    @Override
    public void selectCarDetails(AllCarsModel allCarsModel) {
        carSeats = allCarsModel.getCarSeats();
    }

    @Override
    public void onInteractionEnd() {
        if (activityRequestForCarBinding.navigationIcon.getVisibility() == View.GONE)
            activityRequestForCarBinding.navigationIcon.setVisibility(View.VISIBLE);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(SageLocationService.EXTRA_LOCATION);
            if (location != null) {

                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mCurrentLocation = location;
            }

            if (mGoogleMap != null && mCurrentLocation != null) {
                if (isNetworkConnected()) {
                    plotDriversWS(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }
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

    @Override
    public int getBindingVariable() {
        return BR.requestForCarViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_for_car;
    }

    @Override
    public RequestForCarViewModel getViewModel() {

        requestForCarViewModel = ViewModelProviders.of(this, factory).get(RequestForCarViewModel.class);
        return requestForCarViewModel;
    }

    private LatLng sourceAddressLatLng = null;
    private LatLng destinationAddressLatLng = null;
    private LatLng addStopAddressLatLng = null;
    private LatLng currentLatLng = null;

    private String sAddress = "";
    private String dAddress = "";
    private String addStopAddress = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.semi_tranparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        super.onCreate(savedInstanceState);

        myReceiver = new MyReceiver();

        activityRequestForCarBinding = getViewDataBinding();
        requestForCarViewModel.setNavigator(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SOURCE_ADD")) {
            sAddress = intent.getStringExtra("SOURCE_ADD");
            sourceAddressLatLng = intent.getParcelableExtra("SOURCE_LATLNG");

            dAddress = intent.getStringExtra("DEST_ADD");
            destinationAddressLatLng = intent.getParcelableExtra("DEST_LATLNG");

            addStopAddress = intent.getStringExtra("ADDSTOP_ADD");
            addStopAddressLatLng = intent.getParcelableExtra("ADDSTOP_LATLNG");
        }

        if (isNetworkConnected()) {
            showLoading("");

            String sourceAddressLat = String.valueOf(sourceAddressLatLng.latitude);
            String sourceAddressLng = String.valueOf(sourceAddressLatLng.longitude);

            String destinationAddressLat = String.valueOf(destinationAddressLatLng.latitude);
            String destinationAddressLng = String.valueOf(destinationAddressLatLng.longitude);

            String addStopAddressLat = "";
            String addStopAddressLng = "";

            if (addStopAddressLatLng != null) {
                addStopAddressLat = String.valueOf(addStopAddressLatLng.latitude);
                addStopAddressLng = String.valueOf(addStopAddressLatLng.longitude);
            }

            requestForCarViewModel.getAllCars(this, sourceAddressLat, sourceAddressLng, destinationAddressLat, destinationAddressLng, addStopAddressLat, addStopAddressLng);
        }

        activityRequestForCarBinding.sourceAddress.setText(sAddress);
        activityRequestForCarBinding.destinationAddress.setText(dAddress);

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

        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RequestForCarActivity.this, instanceIdResult -> {
                SharedData.saveString(RequestForCarActivity.this, Constant.REFRESHED_TOKEN, instanceIdResult.getToken());
            });
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

        /*if (mGoogleMap != null) {
            mGoogleMap.clear();
        }*/

        Route route = direction.getRouteList().get(0);
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

        mGoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, ContextCompat.getColor(this, R.color.colorPrimary)));

        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Polyline line;
    List<PolylineOptions> gradientpoly = new ArrayList<>();

    Route finalRoute = null;

    private Marker destinationMarker = null;

    private void onDirection11(Direction direction) {

        try {
            if (mGoogleMap != null) {
                mGoogleMap.clear();
            }

            Route route = direction.getRouteList().get(0);
            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            finalRoute = route;
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

            destinationAddressLatLng = dest;
            if (dest != null) {
                View marker_view2 = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker, null);

                AppCompatTextView etaTxt = marker_view2.findViewById(R.id.etaTxt);

                String time1 = route.getLegList().get(0).getDuration().getText();

                etaTxt.setText(time1);

                MarkerOptions marker_opt_des = new MarkerOptions().position(dest);
                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_view2))).anchor(0.00f, 0.20f);
                destinationMarker = mGoogleMap.addMarker(marker_opt_des);
            }

            Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(Objects.requireNonNull(dest)).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
            mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));

            try {
                setCameraWithCoordinationBounds(route);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            }
        } catch (Exception | OutOfMemoryError e) {
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

    private void onDirectionWayPoints(Direction direction) {

        /*if (mGoogleMap != null) {
            mGoogleMap.clear();
        }*/

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


        Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(destinationAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(addStopAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
        mGoogleMap.addMarker(new MarkerOptions().position(sourceAddressLatLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
        try {
            setCameraWithCoordinationBounds(route);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDirectionWayPoints11(Direction direction) {

        try {
            if (mGoogleMap != null) {
                mGoogleMap.clear();
            }

            if (gradientpoly != null && gradientpoly.size() > 0) {
           /* if (line != null) {
                line.remove();
            }*/
                gradientpoly.clear();
            }

            Route route = direction.getRouteList().get(0);
            finalRoute = route;

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            ArrayList<LatLng> directionPositionList1 = route.getLegList().get(1).getDirectionPoint();

            LatLng source = directionPositionList.get(0);
            LatLng addStop = directionPositionList.get(directionPositionList.size() - 1);
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

            destinationAddressLatLng = dest;
            if (dest != null) {
                View marker_view2 = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker, null);

                AppCompatTextView etaTxt = marker_view2.findViewById(R.id.etaTxt);

                String time1 = route.getLegList().get(0).getDuration().getText();

                etaTxt.setText(time1);

                MarkerOptions marker_opt_des = new MarkerOptions().position(dest);
                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_view2))).anchor(0.00f, 0.20f);
                destinationMarker = mGoogleMap.addMarker(marker_opt_des);
            }

            Objects.requireNonNull(mGoogleMap).addMarker(new MarkerOptions().position(Objects.requireNonNull(dest)).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
            mGoogleMap.addMarker(new MarkerOptions().position(addStop).icon(bitmapDescriptorFromVector(this, R.drawable.ic_destination_location)));
            mGoogleMap.addMarker(new MarkerOptions().position(source).icon(bitmapDescriptorFromVector(this, R.drawable.ic_source_location)));
            try {
                setCameraWithCoordinationBounds(route);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            }
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * Constant.CAMERA_BOUNDS);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        String time = String.valueOf(route.getTotalDuration());
        String distance = String.valueOf(route.getTotalDistance());
        String time1 = route.getLegList().get(0).getDuration().getText();
        String distance1 = route.getLegList().get(0).getDistance().getText();

        distance1 = distance1.replace(" km", "");
        distance1 = distance1.replace(" m", "");

        activityRequestForCarBinding.navigationIcon.setVisibility(View.GONE);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
            mapFragment.getMapAsync(RequestForCarActivity.this);
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

            if (isNetworkConnected()) {
                if (addStopAddressLatLng != null) {
                    addRouteWaypoints();
                } else {
                    addRoute();
                }
            }
        }

        mGoogleMap.setOnCameraIdleListener(() -> {

            if (destinationMarker != null && destinationAddressLatLng != null) {
                Point PickupPoint = googleMap.getProjection().toScreenLocation(destinationAddressLatLng);
                destinationMarker.setAnchor(PickupPoint.x < dpToPx(this, 200) ? 0.00f : 1.00f, PickupPoint.y < dpToPx(this, 100) ? 0.20f : 1.20f);
            }
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

            case 12345:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        sAddress = Objects.requireNonNull(data).getStringExtra("address");
                        sourceAddressLatLng = Objects.requireNonNull(data).getParcelableExtra("latlng");
                        if (isNetworkConnected()) {

                            String devicetoken = SharedData.getString(this, Constant.REFRESHED_TOKEN);
                            String userid = SharedData.getString(this, Constant.USERID);

                            showLoading("");

                            requestForCarViewModel.requestSageWS(sAddress, dAddress, addStopAddress, sourceAddressLatLng, destinationAddressLatLng, addStopAddressLatLng,
                                    currentLatLng, devicetoken, userid, allCarsModelFinal.getCarName());
                        } else {
                            vibrate();
                            Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;

            default:
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

                    //noinspection MissingPermission
                  /*  mFusedLocationClient.requestLocationUpdates(mLocationRequest,
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
                                rae.startResolutionForResult(RequestForCarActivity.this, REQUEST_CHECK_SETTINGS);
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

        if (isNetworkConnected()) {
            requestForCarViewModel.getDefaultPaymentWS(this);
        }

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
                            ActivityCompat.requestPermissions(RequestForCarActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(RequestForCarActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(RequestForCarActivity.this,
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
    public void onBackPressed() {
        finish();
        slideLeftToRight();
    }

    @Override
    public void requestSageClick() {

        if (sourceAddressLatLng != null) {
            Intent intent = new Intent(this, ConfirmPickupMapActivity.class);
            intent.putExtra("SOURCELATLNG", sourceAddressLatLng);
            startActivityForResult(intent, 12345);
            slideRightToLeft();
        }
    }

    @Override
    public void clickNavigation() {
        if (mCurrentLocation != null) {

            if (mGoogleMap != null && finalRoute != null) {
                // LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constant.ZOOM_LOCATION));

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
    public void clickTripFare() {
        Intent intent = new Intent(this, TripFareActivity.class);
        intent.putExtra("SEATS", carSeats);
        startActivity(intent);
        slideRightToLeft();
    }

    @Override
    public void closeCarDetails() {

        activityRequestForCarBinding.mainLayout.setVisibility(View.VISIBLE);
        activityRequestForCarBinding.carDetails.setVisibility(View.GONE);
    }

    @Override
    public void clickPaymentMethod() {
        openActivity(this, ChangePaymentProfileActivity.class, false, false);
        slideRightToLeft();
    }

    @Override
    public void successAPI(RequestSageResponse requestSageResponse) {

        hideLoading();

        try {

            if (requestSageResponse.getStatus() != null && requestSageResponse.getStatus().equalsIgnoreCase("true")) {

                SharedData.saveString(this, Constant.TEMP_REQUEST_ID, requestSageResponse.getRequestId());

                RequestSageDialogFragment fragment = RequestSageDialogFragment.newInstance();
                fragment.setCancelable(false);
                fragment.show(getSupportFragmentManager(), "request");
            } else {
                vibrate();
                Constant.showErrorToast(requestSageResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorAPI(ANError anError) {
        hideLoading();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }

    @Override
    public void successAllCarsAPI(GetAllCarsResponse response) {

        hideLoading();
        try {

            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {
                if (response.getData() != null && response.getData().size() > 0) {
                    addCars(response.getData());
                } else {
                    if (mGoogleMap != null) {
                        mGoogleMap.clear();
                    }
                    vibrate();
                    Constant.showErrorToast(response.getMessage(), this);
                }
            } else {
                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                }
                vibrate();
                Constant.showErrorToast(response.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successPaymentsAPI(GetDefaultPaymentResponse response) {

        hideLoading();

        try {
            if (response.getStatus() != null && response.getStatus().equalsIgnoreCase("true")) {

                if (response.getDefaultmethod() != null && !response.getDefaultmethod().equalsIgnoreCase("")) {
                    if (response.getDefaultmethod().equalsIgnoreCase("personal")) {
                        if (response.getPersonalBrand() != null && !response.getPersonalBrand().equalsIgnoreCase("")) {
                            if (response.getPersonalBrand().equalsIgnoreCase("visa")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_visa);
                            } else if (response.getPersonalBrand().equalsIgnoreCase("mastercard")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_master_card);
                            } else if (response.getPersonalBrand().equalsIgnoreCase("amex")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_amex);
                            } else if (response.getPersonalBrand().equalsIgnoreCase("discover")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_discover);
                            } else {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_method);
                            }
                        } else {
                            activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_method);
                        }

                        activityRequestForCarBinding.cardNumber.setText(String.format("•••• •••• •••• %s", response.getPersonalId()));
                        activityRequestForCarBinding.profileType.setText("Personal");
                    } else if (response.getDefaultmethod().equalsIgnoreCase("business")) {
                        if (response.getBusinessBrand() != null && !response.getBusinessBrand().equalsIgnoreCase("")) {
                            if (response.getBusinessBrand().equalsIgnoreCase("visa")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_visa);
                            } else if (response.getBusinessBrand().equalsIgnoreCase("mastercard")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_master_card);
                            } else if (response.getBusinessBrand().equalsIgnoreCase("amex")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_amex);
                            } else if (response.getBusinessBrand().equalsIgnoreCase("discover")) {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_discover);
                            } else {
                                activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_method);
                            }
                        } else {
                            activityRequestForCarBinding.cardIcon.setBackgroundResource(R.drawable.payment_ic_method);
                        }
                        activityRequestForCarBinding.cardNumber.setText(String.format("•••• •••• •••• %s", response.getBusinessId()));
                        activityRequestForCarBinding.profileType.setText("Business");
                    }
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successRequestSageNew(RequestSageMainResponseNew requestSageResponse) {
        hideLoading();

        try {

            if (requestSageResponse.getStatus() != null && requestSageResponse.getStatus().equalsIgnoreCase("true")) {

                SharedData.saveString(this, Constant.TEMP_REQUEST_ID, requestSageResponse.getRequestID());

                if (requestSageResponse.getData() != null && requestSageResponse.getData().size() > 0) {

                    ArrayList<String> driverList = new ArrayList<>();
                    List<NearestDriverDummy> nearestDriverDummyList = new ArrayList<>();

                    for (int i = 0; i < requestSageResponse.getData().size(); i++) {
                        try {
                            double lat = Double.parseDouble(requestSageResponse.getData().get(i).getLat());
                            double lng = Double.parseDouble(requestSageResponse.getData().get(i).getLng());
                            LatLng driver = new LatLng(lat, lng);
                            double difference = Constant.distanceBetween(sourceAddressLatLng, driver);
                            nearestDriverDummyList.add(new NearestDriverDummy(requestSageResponse.getData().get(i).getDriverId(), difference));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (nearestDriverDummyList.size() > 0) {
                        Collections.sort(nearestDriverDummyList, (c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()));

                        for (int i = 0; i < nearestDriverDummyList.size(); i++) {
                            driverList.add(nearestDriverDummyList.get(i).getDriverID());
                        }
                    }

/*
                    for (int i = 0; i < requestSageResponse.getData().size(); i++) {
                        driverList.add(requestSageResponse.getData().get(i).getDriverId());
                    }*/
                    Bundle bundle = new Bundle();
                    bundle.putInt("timeout", requestSageResponse.getRequestTimeout());
                    bundle.putStringArrayList("drivers", driverList);

                    RequestSageDialogFragment fragment = RequestSageDialogFragment.newInstance();
                    fragment.setArguments(bundle);
                    fragment.setCancelable(false);
                    fragment.show(getSupportFragmentManager(), "request");
                } else {
                    vibrate();
                    Constant.showErrorToast(requestSageResponse.getMessage(), this);
                }
            } else {
                vibrate();
                Constant.showErrorToast(requestSageResponse.getMessage(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCars(List<GetAllCarsData> data) {

        List<AllCarsModel> allCarsModelList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            AllCarsModel allCarsModel = new AllCarsModel();
            allCarsModel.setCarImage(data.get(i).getCarImage());
            allCarsModel.setCarName(data.get(i).getCarType());
            allCarsModel.setCarSeats(data.get(i).getSeats());
            allCarsModel.setCarFare(data.get(i).getEstimateFee());
            allCarsModel.setCarTime(data.get(i).getTimeToTravel());

            if (i == 0) {
                allCarsModel.setSelected(true);
            } else {
                allCarsModel.setSelected(false);
            }
            allCarsModelList.add(allCarsModel);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // RequestCarItemAdapter requestCarItemAdapter = new RequestCarItemAdapter(this, allCarsModelList);
        RequestCarItemAdapter requestCarItemAdapter = new RequestCarItemAdapter(this);
        requestCarItemAdapter.updatePosition(allCarsModelList);
        requestCarItemAdapter.setCallbackCarDetails(this);
        requestCarItemAdapter.setCallbackSelectCarDetails(this);
        activityRequestForCarBinding.carsRecyclerview.setLayoutManager(linearLayoutManager);
        activityRequestForCarBinding.carsRecyclerview.setAdapter(requestCarItemAdapter);

        MultiSnapHelper multiSnapHelper = new MultiSnapHelper(SnapGravity.START, 1, 100);
        multiSnapHelper.attachToRecyclerView(activityRequestForCarBinding.carsRecyclerview);

        /*SnapHelper snapHelperStart = new StartSnapHelper();
        snapHelperStart.attachToRecyclerView(activityRequestForCarBinding.carsRecyclerview);*/

        allCarsModelFinal = allCarsModelList.get(0);

        activityRequestForCarBinding.carsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    try {
                        View centerView = multiSnapHelper.findSnapView(linearLayoutManager);
                        int pos = linearLayoutManager.getPosition(Objects.requireNonNull(centerView));
                        if (allCarsModelList != null && allCarsModelList.size() > 0 && pos != 3) {
                            for (AllCarsModel bean1 :
                                    allCarsModelList) {
                                bean1.setSelected(false);
                            }
                            allCarsModelList.get(pos).setSelected(true);

                            allCarsModelFinal = allCarsModelList.get(pos);

                            requestCarItemAdapter.updatePosition(allCarsModelList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    AllCarsModel allCarsModelFinal = null;

    @Override
    public void openCarDetails(AllCarsModel allCarsModel) {

        activityRequestForCarBinding.mainLayout.setVisibility(View.GONE);
        activityRequestForCarBinding.carName.setText(allCarsModel.getCarName());
        activityRequestForCarBinding.carFare.setText(String.format("$%s", allCarsModel.getCarFare()));
        activityRequestForCarBinding.carSeats.setText(String.format("%s seats", allCarsModel.getCarSeats()));
        activityRequestForCarBinding.carTime.setText(Constant.convertCarTime(allCarsModel.getCarTime()));
        Glide.with(this).load(allCarsModel.getCarImage()).into(activityRequestForCarBinding.carImage);
        startAnimation(activityRequestForCarBinding.carDetails);
    }

    private void startAnimation(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);
        view.setScaleX(0.f);
        view.setScaleY(0.f);
        view.animate()
                .alpha(1.f)
                .scaleX(1.f).scaleY(1.f)
                .setDuration(600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    private void stopAnimation(View view) {
        view.setAlpha(1.f);
        view.animate()
                .alpha(0.f)
                .setDuration(600)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
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

                    for (int i = 0; i < plotDriversDataList.size(); i++) {
                        addMarkerDrivers(plotDriversDataList.get(i).getLat(), plotDriversDataList.get(i).getLng());
                    }
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
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
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

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private int dpToPx(Context context, float dpValue) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dpValue * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}

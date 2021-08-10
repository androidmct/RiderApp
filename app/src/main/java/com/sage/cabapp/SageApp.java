package com.sage.cabapp;

import android.app.Activity;
import android.app.Application;

import androidx.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;
import com.sage.cabapp.di.component.DaggerAppComponent;
import com.sage.cabapp.twilio.chat.ChatClientManager;
import com.sage.cabapp.utils.AppLogger;
import com.sage.cabapp.utils.SmartAsyncPolicyHolder;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SageApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    private static SageApp instance;
    private ChatClientManager basicClient;

    public static SageApp get() {
        return instance;
    }

    public ChatClientManager getChatClientManager() {
        return this.basicClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);

        AndroidNetworking.initialize(getApplicationContext());

        ObjectBox.init(this);

        SageApp.instance = this;
        basicClient = new ChatClientManager(getApplicationContext());

        /*if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }*/

        MultiDex.install(this);
        SmartAsyncPolicyHolder.INSTANCE.init(getApplicationContext());

        AppLogger.init();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}

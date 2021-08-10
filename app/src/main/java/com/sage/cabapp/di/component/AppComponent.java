package com.sage.cabapp.di.component;

import android.app.Application;

import com.sage.cabapp.SageApp;
import com.sage.cabapp.di.builder.ActivityBuilder;
import com.sage.cabapp.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
@Singleton
@Component(modules = { AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class })
public interface AppComponent extends AndroidInjector<SageApp> {

    void inject(SageApp application);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
package com.suspedeal.makeitbig.base;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * TODO: Add a class header comment!
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}

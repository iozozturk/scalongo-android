package com.ismet.durt;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ismet on 27/01/16.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }


}

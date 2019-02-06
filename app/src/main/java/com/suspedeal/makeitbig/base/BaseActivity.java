package com.suspedeal.makeitbig.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.suspedeal.makeitbig.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivityView {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initializeAds();
        TypefaceProvider.registerDefaultIconSets();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initializeAds() {
        MobileAds.initialize(this, "ca-app-pub-7748272108558557~2120568631");
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    protected abstract int getLayoutId();
}

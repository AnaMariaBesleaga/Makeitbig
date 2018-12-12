package com.suspedeal.makeitbig.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.ads.MobileAds;
import com.suspedeal.makeitbig.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initializeAds();
        TypefaceProvider.registerDefaultIconSets();
    }

    private void initializeAds() {
        MobileAds.initialize(this, "ca-app-pub-7748272108558557~2120568631");
    }

    protected abstract int getLayoutId();
}

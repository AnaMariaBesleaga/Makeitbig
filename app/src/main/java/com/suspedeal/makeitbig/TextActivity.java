package com.suspedeal.makeitbig;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    private TextView mAutofitOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_text);
        setUpViews();
        mAutofitOutput.setText(getIntent().getStringExtra("text"));
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setUpViews() {
        mAutofitOutput = (TextView)findViewById(R.id.output_autofit);
    }
}

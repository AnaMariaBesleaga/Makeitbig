package com.suspedeal.makeitbig;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.suspedeal.makeitbig.base.BaseActivity;

import butterknife.BindView;

public class MakeItBigActivity extends BaseActivity {

    @BindView(R.id.bigText)
    TextView bigText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        super.onCreate(savedInstanceState);
        bigText.setText(getIntent().getStringExtra("text"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text;
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
    }
}

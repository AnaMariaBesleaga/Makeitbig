package com.suspedeal.makeitbig;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.model.BigText;

import butterknife.BindView;

public class MakeItBigActivity extends BaseActivity {

    @BindView(R.id.bigText)
    TextView mBigText;
    @BindView(R.id.background)
    FrameLayout mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        super.onCreate(savedInstanceState);
        BigText bigText = (BigText) getIntent().getSerializableExtra("textObject");
        mBigText.setTextColor(ContextCompat.getColor(this, bigText.getTextColour()));
        mBackground.setBackgroundColor(ContextCompat.getColor(this, bigText.getBackgroundColour()));
        mBigText.setText(bigText.getText());
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

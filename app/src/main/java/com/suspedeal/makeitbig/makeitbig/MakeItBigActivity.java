package com.suspedeal.makeitbig.makeitbig;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suspedeal.makeitbig.R;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.base.IBaseActivityView;
import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.utils.NetworkStatus;

import butterknife.BindView;

public class MakeItBigActivity extends BaseActivity {

    @BindView(R.id.bigText)
    TextView mBigText;
    @BindView(R.id.background)
    ImageView mBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        super.onCreate(savedInstanceState);
        BigText bigText = (BigText) getIntent().getSerializableExtra("textObject");

        if(NetworkStatus.getInstance(this).isOnline()){
            Picasso.get().load(bigText.getBackgroundUrl()).into(mBackgroundImageView);
        }

        mBigText.setTextColor(Color.parseColor(bigText.getTextColour()));
        mBigText.setText(bigText.getText());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text;
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public IBaseActivityView getInstance() {
        return this;
    }
}

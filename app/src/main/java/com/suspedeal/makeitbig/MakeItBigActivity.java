package com.suspedeal.makeitbig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.base.IBaseActivityView;
import com.suspedeal.makeitbig.model.BigText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        BigText bigText = (BigText) getIntent().getSerializableExtra("textObject");
        Picasso.get().load(bigText.getBackgroundUrl()).into(mBackgroundImageView);
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
    }


    @Override
    public IBaseActivityView getInstance() {
        return this;
    }
}

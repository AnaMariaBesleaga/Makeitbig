package com.suspedeal.makeitbig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    FrameLayout mBackground;
    Bitmap mBackgroundBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        super.onCreate(savedInstanceState);
        BigText bigText = (BigText) getIntent().getSerializableExtra("textObject");
        setBackground(bigText.getBackgroundUrl());
        mBigText.setTextColor(Color.parseColor(bigText.getTextColour()));
        mBigText.setText(bigText.getText());
    }

    private void setBackground(final String uri) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    try {
                        url = new URL(uri);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    try {
                        mBackgroundBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Drawable image = new BitmapDrawable(getResources(), mBackgroundBitmap);
                            mBackground.setBackground(image);
                        }
                    });


                }
            }).start();
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

package com.suspedeal.makeitbig;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private TextView tvBig;
    private Button btnEnlarge;
    private RelativeLayout rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);


        etInput = (EditText) findViewById(R.id.etInput);
        tvBig = (TextView) findViewById(R.id.tvBig);
        btnEnlarge = (Button) findViewById(R.id.btnEnlarge);

        rootView = (RelativeLayout) findViewById(R.id.content);

        rootView = (RelativeLayout) findViewById(R.id.content);
        rootView.post(new Runnable()
        {

            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, "Width of RootView is: " + rootView.getWidth(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Enlarge(View v){
        tvBig.setText(etInput.getText().toString());
        tvBig.setVisibility(View.VISIBLE);


            tvBig.setTextSize(90);

        tvBig.post(new Runnable()
        {

            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, "Height of TextView is: " + tvBig.getHeight(), Toast.LENGTH_SHORT).show();

            }
        });

        etInput.setVisibility(View.GONE);
        btnEnlarge.setVisibility(View.GONE);
        //Grow();

    }

    public void Grow(){
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);

        myAnim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
                //Toast.makeText(MainActivity.this, "Width of TextView is: " + tvBig.getWidth(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {

//                Rect bounds = new Rect();
//                Paint textPaint = tvBig.getPaint();
//                textPaint.getTextBounds(tvBig.getText().toString(),0,tvBig.getText().toString().length(),bounds);
//                int height = bounds.height();
//                int width = bounds.width();
//
//                if(width > 100){
//                    tvBig.setWidth(100);
//                }
            }
        });

        //tvBig.startAnimation(myAnim);
    }

    public void decreaseFontSize(){




    }

}

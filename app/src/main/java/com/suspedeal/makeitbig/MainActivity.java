package com.suspedeal.makeitbig;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private TextView tvBig;
    private Button btnEnlarge;
    private ConstraintLayout rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        etInput = (EditText) findViewById(R.id.etInput);
        tvBig = (TextView) findViewById(R.id.tvBig);
        btnEnlarge = (Button) findViewById(R.id.btnEnlarge);

        rootView = (ConstraintLayout) findViewById(R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //rootView.getHeight(); //height is ready
                Toast.makeText(MainActivity.this, "Width of RootView is: " + rootView.getLayoutParams().width, Toast.LENGTH_SHORT).show();
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
        etInput.setVisibility(View.GONE);
        btnEnlarge.setVisibility(View.GONE);
        Grow();

    }

    public void Grow(){
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        tvBig.measure(0, 0);

        try{
            tvBig.startAnimation(myAnim);
        }finally {
            Toast.makeText(MainActivity.this, "Width of TextView is: " + tvBig.getMeasuredWidth(), Toast.LENGTH_SHORT).show();
        }
    }

}

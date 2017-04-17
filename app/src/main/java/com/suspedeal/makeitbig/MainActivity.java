package com.suspedeal.makeitbig;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.suspedeal.makeitbig.utils.RecyclerViewEmptySupport;
import com.suspedeal.makeitbig.views.adapters.MyAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private Button btnMakeBig;
    private View mEmptyView;
    private RecyclerViewEmptySupport recyclerHistory;
    private RecyclerViewEmptySupport.Adapter mAdapter;
    private RecyclerViewEmptySupport.LayoutManager mLayoutManager;
    private ArrayList<String> mHistoryArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        setUpViews();

        btnMakeBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTextActivity();
                addToHistory();
                emptyInput();


            }

            private void emptyInput() {
                etInput.setText("");
            }

            private void startTextActivity() {
                Intent i = new Intent(MainActivity.this, TextActivity.class);
                i.putExtra("text", etInput.getText().toString());
                startActivity(i);
            }

            private void addToHistory() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mHistoryArray.add(etInput.getText().toString());

                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setUpViews() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        etInput = (EditText) findViewById(R.id.input);
        btnMakeBig = (Button) findViewById(R.id.btnMakeBig);
        mEmptyView = findViewById(R.id.list_empty);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        recyclerHistory = (RecyclerViewEmptySupport) findViewById(R.id.recycle_history);
        recyclerHistory.setHasFixedSize(true);
        recyclerHistory.setEmptyView(mEmptyView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerHistory.setLayoutManager(mLayoutManager);
        mHistoryArray = new ArrayList<>();
        mHistoryArray.add("Item 1");
        mAdapter = new MyAdapter(this, mHistoryArray);
        recyclerHistory.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.suspedeal.makeitbig;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.suspedeal.makeitbig.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnTextClickListener {

    private static final String HISTORY_PREF_FILE = "MBHistory";
    @BindView(R.id.edit)
    BootstrapEditText edit;
    @BindView(R.id.btnMakeBig)
    BootstrapButton makeItBig;
    @BindView(R.id.recycle_history)
    RecyclerView historyList;
    @BindView(R.id.list_empty)
    TextView emptyListView;

    private TextAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("  " + getString(R.string.app_name));
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        setUpRecyclerView();
        getHistoryList();
    }

    private void getHistoryList() {
        SharedPreferences prefs = getSharedPreferences(HISTORY_PREF_FILE, Context.MODE_PRIVATE);
        int size = prefs.getInt("array_size", 0);
        for(int i=0; i<size; i++)
            adapter.add(prefs.getString("array_" + i, null));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_main;
    }

    private void emptyInput() {
        edit.setText("");
    }

    private void startTextActivity(String text) {

            Intent i = new Intent(MainActivity.this, MakeItBigActivity.class);
            i.putExtra("text", text);
            startActivity(i);

    }

    private void addToHistory() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(edit.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {
        historyList.setHasFixedSize(true);
        LayoutManager mLayoutManager = new LinearLayoutManager(this);
        historyList.setLayoutManager(mLayoutManager);
        adapter = new TextAdapter(this);
        historyList.setAdapter(adapter);
    }

    @OnClick(R.id.btnMakeBig) void makeItBig(){
        if(!edit.getText().toString().isEmpty()){
            addToHistory();
            addToSharedPreferences();
            startTextActivity(getInputText());
            emptyInput();
        }else{
            showSnack(getString(R.string.no_input));
        }
       
    }

    private String getInputText() {

        return edit.getText().toString();
    }

    private void addToSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(HISTORY_PREF_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt("array_size", adapter.getItemCount());
        for(int i=0;i<adapter.getItemCount(); i++)
            editor.putString("array_" + i, adapter.getTextListPosition(i));
        editor.apply();
    }

    private void showSnack(String string) {
        Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();
    }

    @Override
    public void OnTextClicked(String text) {
        startTextActivity(text);

    }
}

package com.suspedeal.makeitbig;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.utils.RatingDialogCustom;

import butterknife.BindView;
import butterknife.OnClick;

import static com.suspedeal.makeitbig.constants.Constants.*;

/**
 * The text which are made big are saved in shared preferences for peristence. The in memory save is
 * done inside the TextAdapter class
 */
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
    @BindView(R.id.adView)
    AdView adView;

    private TextAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("  " + getString(R.string.app_name));
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        initializeRating();
        setUpRecyclerView();
        addTextsFromStorageToAdapterAndShow();
        checkAndShowAppropiateView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_main;
    }

    private void addTextsFromStorageToAdapterAndShow() {
        SharedPreferences prefs = getSharedPreferences();
        int size = prefs.getInt("array_size", 0);
        if (size != 0) {
            for (int i = 0; i < size; i++)
                adapter.add(prefs.getString("array_" + i, null));
        }
        historyList.setAdapter(adapter);
    }

    /**
     * Save to storage (shared prefs) with data from the List in the TextAdapter class
     */
    private void updateSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences();
        Editor editor = prefs.edit();
        editor.putInt("array_size", adapter.getItemCount());
        for (int i = 0; i < adapter.getItemCount(); i++)
            editor.putString("array_" + i, adapter.getTextListPosition(i));
        editor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences(HISTORY_PREF_FILE, Context.MODE_PRIVATE);
    }

    private void clearInputField() {
        edit.setText("");
    }

    private void startTextActivity(String text) {
        Intent i = new Intent(MainActivity.this, MakeItBigActivity.class);
        i.putExtra("text", text);
        startActivity(i);
    }

    private void addNewEntryToAdapter() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(getInputText());
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

    }

    @OnClick(R.id.btnMakeBig)
    void makeItBig() {
        if (!getInputText().isEmpty()) {
            addNewEntryToAdapter();
            updateSharedPreferences();
            checkAndShowAppropiateView();
            startTextActivity(getInputText());
            clearInputField();
        } else {
            showSnack(getString(R.string.no_input));
        }
    }

    /**
     * The appropriate text entry is removed from inside the adapter. We only need to call the notify
     * data set changed method to refresh the list
     */
    @Override
    public void OnItemDeleted() {
        adapter.notifyDataSetChanged();
        //the new list must be saved back to shared preferences; even if zero entries because shared
        // preferences must reflect that there are no entries.
        updateSharedPreferences();
        checkAndShowAppropiateView();
    }

    private String getInputText() {
        return edit.getText().toString();
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

    private void checkAndShowAppropiateView() {

        if (adapter.getItemCount() != 0) {
            hideEmptyView();
        } else {
            showEmptyView();
        }
    }

    private void showEmptyView() {
        emptyListView.setVisibility(View.VISIBLE);
        historyList.setVisibility(View.GONE);
    }

    private void hideEmptyView() {
        emptyListView.setVisibility(View.GONE);
        historyList.setVisibility(View.VISIBLE);
    }

    private void initializeRating() {

        final RatingDialogCustom ratingDialog = (RatingDialogCustom) new RatingDialogCustom.BuilderCustom(this)
                .threshold(STAR_RATING_THRESHOLD)
                .title(getString(R.string.rating_text))
                .session(SESSION_SHOW)
                .positiveButtonText(getString(R.string.not_now))
                .formTitle(getString(R.string.rating_send_message))
                .formHint(getString(R.string.rating_improve_question))
                .formSubmitText(getString(R.string.rating_send))
                .formCancelText(getString(R.string.rating_dialog_cancel))
                .negativeButtonText("")
                .onRatingBarFormSumbit(new RatingDialogCustom.BuilderCustom.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        sendFeedbackToTrello(feedback);
                    }
                }).build();

        ratingDialog.show();
    }

    private void sendFeedbackToTrello(final String feedback) {
        new sendFeedbackToTrello().execute(feedback);
    }

    private class sendFeedbackToTrello extends AsyncTask<String, Integer, Card> {

        Trello trelloApi = new TrelloImpl(TRELLO_APP_KEY, TRELLO_ACCESS_TOKEN);

        @Override
        protected Card doInBackground(String... params) {
            Card feedBack = new Card();
            feedBack.setName(params[0]);
            return trelloApi.createCard(TRELLO_FEEDBACK_LIST, feedBack);
        }

        @Override
        protected void onPostExecute(Card result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, getString(R.string.feedback_sent), Toast.LENGTH_SHORT).show();
        }
    }
}
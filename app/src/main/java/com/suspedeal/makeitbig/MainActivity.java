package com.suspedeal.makeitbig;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;
import com.suspedeal.makeitbig.addTheme.AddNewThemeActivity;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.base.IBaseActivityView;
import com.suspedeal.makeitbig.constants.Constants;
import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.utils.RatingDialogCustom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.suspedeal.makeitbig.constants.Constants.SESSION_SHOW;
import static com.suspedeal.makeitbig.constants.Constants.STAR_RATING_THRESHOLD;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_ACCESS_TOKEN;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_APP_KEY;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_FEEDBACK_LIST;

/**
 * The text which are made big are saved in shared preferences for persistence. The in memory save is
 * done inside the TextAdapter class
 */
public class MainActivity extends BaseActivity implements OnTextClickListener, IMainActivity {

    private static final String HISTORY_PREF_FILE = "MBHistory";
    private static final String EMPTY = "";
    private TextAdapter adapter;
    private ArrayList<BigText> mThemes = new ArrayList<>();

    @BindView(R.id.edit)
    BootstrapEditText edit;
    @BindView(R.id.btnMakeBig)
    BootstrapButton makeItBig;
    @BindView(R.id.recycle_history)
    RecyclerView historyList;
    @BindView(R.id.list_empty)
    TextView emptyListView;
    @BindView(R.id.adBannerLayout)
    LinearLayout adBannerLayout;
    @BindView(R.id.btnAddNewTheme)
    BootstrapButton btnAddNewTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showAddThemeButtonIfDebug();
        getThemesFromDatabase();
        showAds();
        setUpActionBar();
        initializeRating();
        setUpRecyclerView();
        addTextsFromStorageToAdapterAndShow();
        checkAndShowAppropiateView();
    }

    private void showAddThemeButtonIfDebug() {
        if(BuildConfig.DEBUG){
            btnAddNewTheme.setVisibility(View.VISIBLE);
        }
    }

    private void getThemesFromDatabase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference themesRef = database.getReference("themes");

        Query query = themesRef.orderByChild("uid");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot themeSnapshot : dataSnapshot.getChildren()) {
                    BigText theme = themeSnapshot.getValue(BigText.class);
                    if (theme != null) {
                        mThemes.add(theme);
                    }
                }

                Toast.makeText(MainActivity.this, String.format("Loaded %s themes", String.valueOf(mThemes.size())), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }

    private void setUpActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("  " + getString(R.string.app_name));
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
    }

    private void showAds() {
        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        if (BuildConfig.DEBUG) {
            mAdView.setAdUnitId(Constants.AD_UNIT_ID_TEST);
        } else {
            mAdView.setAdUnitId(Constants.AD_UNIT_ID_PROD);
        }
        adBannerLayout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

    private void startTextActivity(BigText bigText) {
        Intent i = new Intent(MainActivity.this, MakeItBigActivity.class);
        i.putExtra("textObject", bigText);
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
            startTextActivity(mThemes.get(0));
            clearInputField();
        } else {
            showSnack(getString(R.string.no_input));
        }
    }

    @OnClick(R.id.btnAddNewTheme)
    void addNewTheme() {
       startActivity(new Intent(MainActivity.this, AddNewThemeActivity.class));
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
        //startTextActivity(new BigText(text, R.color.white, R.color.amber));
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

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBaseActivityView getInstance() {
        return this;
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
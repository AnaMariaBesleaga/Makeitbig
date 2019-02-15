package com.suspedeal.makeitbig.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;
import com.suspedeal.makeitbig.BuildConfig;
import com.suspedeal.makeitbig.R;
import com.suspedeal.makeitbig.addTheme.AddNewThemeActivity;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.base.IBaseActivityView;
import com.suspedeal.makeitbig.constants.Constants;
import com.suspedeal.makeitbig.makeitbig.MakeItBigActivity;
import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.repository.ThemeRepository;
import com.suspedeal.makeitbig.utils.NetworkStatus;
import com.suspedeal.makeitbig.utils.RatingDialogCustom;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.suspedeal.makeitbig.constants.Constants.SESSION_SHOW;
import static com.suspedeal.makeitbig.constants.Constants.STAR_RATING_THRESHOLD;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_ACCESS_TOKEN;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_APP_KEY;
import static com.suspedeal.makeitbig.constants.Constants.TRELLO_FEEDBACK_LIST;

/**
 * The text which are made big are saved in shared preferences for persistence. The in memory save is
 * done inside the HistoryAdapter class
 */
public class MainActivity extends BaseActivity implements OnHistoryTextClickListener, IMainActivity {

    private static final String HISTORY_PREF_FILE = "MBHistory";
    private static final String TAG = MainActivity.class.getSimpleName();
    private HistoryAdapter mHistoryAdapter;
    private ThemeAdapter mThemeAdapter;
    private BigText mCurrentSelectedTheme;
    //presenter
    private MainPresenter mMainPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.edit)
    BootstrapEditText edit;
    @BindView(R.id.btnMakeBig)
    BootstrapButton btnMakeBig;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.btnAddNewTheme)
    BootstrapButton btnAddNewTheme;
    @BindView(R.id.single_theme_background)
    ImageView singleThemeBackground;
    @BindView(R.id.current_theme_text)
    TextView currentThemeText;
    @BindView(R.id.btnHistory)
    BootstrapButton btnHistory;
    @BindView(R.id.btnThemes)
    BootstrapButton btnThemes;
    @BindView(R.id.recycle_list)
    RecyclerView recycleList;
    @BindView(R.id.list_empty)
    TextView listEmpty;
    @BindView(R.id.adBannerLayout)
    LinearLayout adBannerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter = new MainPresenter(this, new ThemeRepository());
        showAddThemeButtonIfDebug();
        setUpThemesAdapter();
        getThemesFromDatabase();
        showAds();
        setUpActionBar();
        initializeRating();
        setUpRecyclerView();
        addTextsFromStorageToAdapterAndShow();
        showEmptyListViewIfNoEntriesLeft();

        btnHistory.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                if (isChecked) {
                    showEmptyListViewIfNoEntriesLeft();
                    recycleList.setAdapter(mHistoryAdapter);
                }

            }
        });

        btnThemes.setOnCheckedChangedListener(new BootstrapButton.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(BootstrapButton bootstrapButton, boolean isChecked) {
                if (isChecked) {
                    recycleList.setVisibility(View.VISIBLE);
                    listEmpty.setVisibility(View.GONE);
                    recycleList.setAdapter(mThemeAdapter);
                }
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCurrentThemePreview(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_main;
    }

    private void setUpThemesAdapter() {
        mThemeAdapter = new ThemeAdapter(new OnThemeClickedListener() {
            @Override
            public void onThemeClicked(BigText bigText) {
                String currentText = mCurrentSelectedTheme.getText();
                mCurrentSelectedTheme = bigText;
                mCurrentSelectedTheme.setText(currentText);
                updateCurrentThemePreview(true);
                Log.d(TAG, "Selected: " + mCurrentSelectedTheme.getName());
            }
        }, this);
    }

    private void updateCurrentThemePreview(boolean refreshBackground) {


        if (refreshBackground && isOnline()) {

            if (mCurrentSelectedTheme.getName().equals("Black on white")) {
                singleThemeBackground.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                singleThemeBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            Glide.with(this).load(mCurrentSelectedTheme.getBackgroundUrl()).into(singleThemeBackground);
        }

        currentThemeText.setTextColor(Color.parseColor(mCurrentSelectedTheme.getTextColour()));

        if(edit.getText().toString().equals("")){
            currentThemeText.setText(mCurrentSelectedTheme.getText());
        }else{
            currentThemeText.setText(edit.getText().toString());
        }

    }

    private boolean isOnline() {
        return NetworkStatus.getInstance(this).isOnline();
    }

    private void showAddThemeButtonIfDebug() {
        if (!BuildConfig.DEBUG) {
            btnAddNewTheme.setVisibility(View.GONE);
        }
    }

    private void getThemesFromDatabase() {

        if(isOnline()){
            mMainPresenter.getThemes();
        }else{
            createAndShowDefaultTheme();
            selectFirstThemeAsDefault();
            showToast(getString(R.string.no_internet));
        }

    }

    private void createAndShowDefaultTheme() {
        mThemeAdapter.addToStart(new BigText("text", "Offline theme", "#ffffff", true, true, true));
    }

    @Override
    public void selectFirstThemeAsDefault() {
        mCurrentSelectedTheme = mThemeAdapter.getThemes().get(0);
        mCurrentSelectedTheme.setText(getString(R.string.preview_text));
        updateCurrentThemePreview(true);
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

    private void addTextsFromStorageToAdapterAndShow() {
        SharedPreferences prefs = getSharedPreferences();
        int size = prefs.getInt("array_size", 0);
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                mHistoryAdapter.add(prefs.getString("array_" + i, null));
            }
            mHistoryAdapter.reverse();
        }

        recycleList.setAdapter(mHistoryAdapter);
    }

    /**
     * Save to storage (shared prefs) with data from the List in the HistoryAdapter class
     */
    private void updateSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences();
        Editor editor = prefs.edit();
        editor.putInt("array_size", mHistoryAdapter.getItemCount());
        for (int i = 0; i < mHistoryAdapter.getItemCount(); i++)
            editor.putString("array_" + i, mHistoryAdapter.getTextListPosition(i));
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
        //log event to see with theme was used more

        if(!BuildConfig.DEBUG){
            Bundle bundle = new Bundle();
            bundle.putString("theme_name", bigText.getName());
            bundle.putString("mib_text", bigText.getText());
            getFirebaseAnalytics().logEvent("theme_used", bundle);
        }

        startActivity(i);
    }

    private void addNewEntryToAdapter() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHistoryAdapter.add(getInputText());
                mHistoryAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHistoryAdapter != null) {
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {
        recycleList.setHasFixedSize(true);
        LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(mLayoutManager);
        //show history by default
        mHistoryAdapter = new HistoryAdapter(this);

    }

    @OnClick(R.id.btnMakeBig)
    void makeItBig() {
        if (!getInputText().isEmpty()) {
            addNewEntryToAdapter();
            updateSharedPreferences();
            showEmptyListViewIfNoEntriesLeft();
            setTextOnBigTextObject(getInputText());
            startTextActivity(mCurrentSelectedTheme);
        } else {
            showSnack(getString(R.string.no_input));
        }
    }

    private void setTextOnBigTextObject(String text) {
        mCurrentSelectedTheme.setText(text);
    }

    @OnClick(R.id.btnAddNewTheme)
    void addNewTheme() {
        startActivity(new Intent(MainActivity.this, AddNewThemeActivity.class));
    }

    /**
     * The appropriate text entry is removed from inside the mHistoryAdapter. We only need to call the notify
     * data set changed method to refresh the list
     * @param position
     */
    @Override
    public void OnItemDeleted(int position) {
        mHistoryAdapter.notifyItemRemoved(position);
        mHistoryAdapter.notifyItemRangeChanged(position, mHistoryAdapter.getItemCount() - position);
        //the new list must be saved back to shared preferences; even if zero entries because shared
        // preferences must reflect that there are no entries.
        updateSharedPreferences();
        showEmptyListViewIfNoEntriesLeft();
        showToast(getString(R.string.item_deleted));
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
        setTextOnBigTextObject(text);
        currentThemeText.setText(text);
        startTextActivity(mCurrentSelectedTheme);
    }

    private void showEmptyListViewIfNoEntriesLeft() {

        if (mHistoryAdapter.getItemCount() != 0) {
            hideEmptyView();
        } else {
            showEmptyView();
        }
    }

    private void showEmptyView() {
        listEmpty.setVisibility(View.VISIBLE);
        recycleList.setVisibility(View.GONE);
    }

    private void hideEmptyView() {
        listEmpty.setVisibility(View.GONE);
        recycleList.setVisibility(View.VISIBLE);
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
    public void clearThemesList() {
        mThemeAdapter.clear();
    }

    @Override
    public void setNewThemeList(List<BigText> themes) {
        mThemeAdapter.setThemes(themes);
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
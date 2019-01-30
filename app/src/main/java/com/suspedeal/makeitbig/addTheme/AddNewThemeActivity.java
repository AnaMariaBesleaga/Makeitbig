package com.suspedeal.makeitbig.addTheme;

import android.os.Bundle;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.suspedeal.makeitbig.R;
import com.suspedeal.makeitbig.base.BaseActivity;
import com.suspedeal.makeitbig.base.IBaseActivityView;
import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.repository.ThemeRepository;

import butterknife.BindView;
import butterknife.OnClick;

public class AddNewThemeActivity extends BaseActivity implements IAddNewThemeActivityView {

    @BindView(R.id.btnAddStorageUrl)
    BootstrapButton btnAddStorageUrl;
    @BindView(R.id.btnAddNewTheme)
    BootstrapButton btnAddNewTheme;
    @BindView(R.id.etTextColour)
    BootstrapEditText etTextColour;
    @BindView(R.id.etNewThemeUid)
    BootstrapEditText etNewThemeUid;

    private AddNewThemePresenter mAddNewThemePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddNewThemePresenter = new AddNewThemePresenter(new ThemeRepository(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_new_theme;
    }

    @OnClick(R.id.btnAddNewTheme)
    public void addNewTheme() {

        String newColor = etTextColour.getText().toString();
        if (newColor.equals("")) {
            showToast("Please enter the desired text colour");
            return;
        }
        BigText bigText = new BigText("", newColor, true, true);

        mAddNewThemePresenter.addThemeToDB(bigText);
    }

    @OnClick(R.id.btnAddStorageUrl)
    public void addStorageUrl() {
        mAddNewThemePresenter.addBackgroundURLToObject();

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addUidToEditText(String uid) {
        etNewThemeUid.setText(uid);
    }

    @Override
    public void removeUidFromEditText() {
        etNewThemeUid.setText("");
    }

    @Override
    public IBaseActivityView getInstance() {
        return null;
    }
}

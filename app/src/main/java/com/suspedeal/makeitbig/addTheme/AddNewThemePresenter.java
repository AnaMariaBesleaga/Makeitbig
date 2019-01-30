package com.suspedeal.makeitbig.addTheme;

import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.repository.ThemeRepository;

public class AddNewThemePresenter implements IAddNewThemePresenter {

    private ThemeRepository mThemeRepository;
    private IAddNewThemeActivityView mView;

    public AddNewThemePresenter(ThemeRepository themeRepository, IAddNewThemeActivityView instance) {
        mThemeRepository = themeRepository;
        mView = instance;
    }

    @Override
    public void addThemeToDB(BigText bigText) {
        mThemeRepository.addThemeToDB(new OnThemeAddedToDBCallback() {

            @Override
            public void onSuccess(String uid) {
                mView.showToast(uid);
                mView.addUidToEditText(uid);
            }

            @Override
            public void onFailed(String error) {
                mView.showToast(error);
            }
        }, bigText);
    }

    @Override
    public void addBackgroundURLToObject() {
        mThemeRepository.getDownloadUrl(new OnAddUrlToObjectListener() {
            @Override
            public void onSuccess() {
                mView.showToast("Storage link added!");
                mView.removeUidFromEditText();
            }

            @Override
            public void onFailed(String error) {
                mView.showToast("Storage link could not be added. Reason: " + error);
            }
        });
    }
}

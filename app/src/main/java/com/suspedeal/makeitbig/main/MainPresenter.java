package com.suspedeal.makeitbig.main;

import com.suspedeal.makeitbig.model.BigText;
import com.suspedeal.makeitbig.repository.IThemeRepository;

import java.util.ArrayList;

/**
 * TODO: Add a class header comment!
 */
class MainPresenter implements IMainPresenter {

    private IThemeRepository mThemeRepository;
    private IMainActivity mMainActivityView;

    public MainPresenter(IMainActivity mainActivityView, IThemeRepository themeRepository) {
        mMainActivityView = mainActivityView;
        mThemeRepository = themeRepository;
    }

    @Override
    public void getThemes() {
        mThemeRepository.getThemesFromDB(new OnGetThemeCallback(){

            @Override
            public void onDataChanged(ArrayList<BigText> list) {
                mMainActivityView.clearThemesList();
                mMainActivityView.setNewThemeList(list);
                mMainActivityView.selectFirstThemeAsDefault();
                //i need to call this here after the themes have been fetched
                mMainActivityView.checkIfNewThemePush();
            }
        });
    }
}

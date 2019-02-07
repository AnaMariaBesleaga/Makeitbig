package com.suspedeal.makeitbig.main;

import com.suspedeal.makeitbig.model.BigText;

import java.util.List;

interface IMainActivity {
    void showToast(String message);
    void clearThemesList();
    void setNewThemeList(List<BigText> themes);
    void selectFirstThemeAsDefault();
}

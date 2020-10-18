package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.view.ProfileView;
import javafx.stage.Stage;

public class ProfilePresenter {

    private ProfileView view;

    public ProfilePresenter(){
        view = new ProfileView();
    }

    public ProfileView getView() {
        return view;
    }
}

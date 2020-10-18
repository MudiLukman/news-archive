package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.view.AnalyticsView;

public class AnalyticsPresenter {

    private AnalyticsView view;

    public AnalyticsPresenter(){
        view = new AnalyticsView();
    }

    public AnalyticsView getView(){
        return view;
    }

}

package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.view.ListOfNewsView;

public class ListOfNewsPresenter {

    private ListOfNewsView view;

    public ListOfNewsPresenter(){
        view = new ListOfNewsView();
    }

    public ListOfNewsView getView(){
        return view;
    }

}

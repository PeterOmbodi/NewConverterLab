package com.peterombodi.newconverterlab.presentation.screen.presenter;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.domain.IDomain;
import com.peterombodi.newconverterlab.presentation.screen.IMainActivity;
import com.peterombodi.newconverterlab.presentation.screen.view.MainActivity;

import java.util.ArrayList;

/**
 * Created by Admin on 21.11.2016.
 */

public class MainActivityPresenter implements IMainActivity.IPresenter {

    private static final String TAG = "MainScreenPresenter";

    private IDomain mDomain;
    private MainActivity mView;
    private ArrayList<OrganizationRV> rvArrayList;

    @Override
    public void domainCallTest(String _text) {
        //// TODO: 23.11.2016 load from JSON
//        mDomain = new DomainImpl();
//        mDomain.getData(this);
        //// TODO: 22.11.2016 Load from DB
//        DownloadData downloadData = new DownloadDataImpl();
//        rvArrayList = downloadData.loadDbData();
//        mView.setBankListFragment(rvArrayList);
    }

    @Override
    public void registerView(MainActivity _view) {
        this.mView = _view;
    }

    @Override
    public void unRegisterView() {
        mView=null;
    }


}

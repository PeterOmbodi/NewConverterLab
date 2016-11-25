package com.peterombodi.newconverterlab.presentation.screen.main.presenter;

import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.Organization;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.domain.Domain;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 21.11.2016.
 */

public class MainScreenPresenter implements IMainScreen.IPresenter, IMainScreen.ResponseCallback<DataResponse> {

    private static final String TAG = "MainScreenPresenter";

    private Domain mDomain;
    private IMainScreen.IView mView;
    private ArrayList<OrganizationRV> rvArrayList;

    private IMainScreen.ResponseCallback responseCallback;

    @Override
    public void domainCallTest(String _text) {
        //// TODO: 23.11.2016 load from JSON
//        mDomain = new DomainImpl();
//        mDomain.getData(this);
        //// TODO: 22.11.2016 Load from DB
//        DownloadData downloadData = new DownloadDataImpl();
//        rvArrayList = downloadData.getDbData();
//        mView.setBankListFragment(rvArrayList);
    }




    @Override
    public void registerView(IMainScreen.IView _view) {
        this.mView = _view;
    }

    @Override
    public void unRegisterView() {
        mView=null;
    }

    @Override
    public void dataAnswerTest(String _answer) {
//        mView.setText(_answer);
    }

    // TODO: 25.11.2016  почистить!
    @Override
    public void onResponse(DataResponse _data) {
        ArrayList<Organization> organisations = _data.getOrganizations();
        HashMap<String, String> cities = _data.getCities();
        HashMap<String, Currency> currencyHashMap = _data.getOrganizations().get(1).getCurrencies();

//        mView.setText(currencyHashMap.toString()+"**");
    }

    @Override
    public void onFailure() {
        // TODO: 25.11.2016  почистить!
 //       mView.setText("retrofit.onRefreshFailure");
    }


}

package com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.domain.Domain;
import com.peterombodi.newconverterlab.domain.DomainImpl;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

import java.util.ArrayList;

/**
 * Created by Admin on 23.11.2016.
 */

public class BankListPresenter implements IListFragment.IPresenter, IListFragment.ResponseCallback<DataResponse> {

    private static final String TAG = "BankListPresenter";
    private static final int REQUEST_PERMISSIONS_CALL = 0;

    // package for calling changed for API SDK>=21
    private static final String CALL_PACKAGE = (Build.VERSION.SDK_INT >= 21) ?
            "com.android.server.telecom" : "com.android.phone";
    private static final String[] PERMISSIONS_CALL = {Manifest.permission.CALL_PHONE};
    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";

    private IListFragment.IView mView;

    @Override
    public void registerView(IListFragment.IView _view) {
        Log.d(TAG, "------------------ registerView");
        this.mView = _view;
    }


    @Override
    public void unRegisterView() {
        Log.d(TAG, "------------------ unRegisterView");
        mView = null;
    }

    @Override
    public ArrayList<OrganizationRV> getBankList(String _filter) {
        Log.d(TAG, "getBankList >>>>--");
        DownloadData downloadData = new DownloadDataImpl();
        ArrayList<OrganizationRV> bankArrayList = downloadData.getDbData(_filter);
        Log.d(TAG, "getBankList >>>>--" + bankArrayList.size());
        mView.setRvArrayList(bankArrayList);
        Log.d(TAG, "++++++++getBankList  mView isnull = " + (mView == null));
        return bankArrayList;
    }

    @Override
    public void refreshData() {
        Domain mDomain = new DomainImpl();
        mDomain.getData(this);
        Log.d(TAG, "++++++++ refreshData  mView isnull = " + (mView == null));
    }


    @Override
    public void onRefreshResponse(DataResponse _data) {
        Log.d(TAG, "onRefreshResponse >>>>--" + _data.getOrganizations().size());

    }

    @Override
    public void onSaveData() {
        Log.d(TAG, "onSaveData >>>>--");
        getBankList(null);
    }

    @Override
    public void onRefreshFailure() {

    }

    @Override
    public void openDetail(OrganizationRV _organizationRV) {

    }

    @Override
    public void openLink(String _url) {
        Log.d(TAG, "openLink _url =" + _url);
        mView.openLink(_url);
    }

    @Override
    public void openMap(String _region, String _city, String _address) {
        mView.openMap(_region, _city, _address);
    }

    @Override
    public void openCaller(String _phone) {

    }


}

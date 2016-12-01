package com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter;

import android.Manifest;
import android.os.Build;
import android.util.Log;

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
    private Domain mDomain;

    @Override
    public void registerView(IListFragment.IView _view) {
        this.mView = _view;
        Log.d(TAG, "------------------ registerView mView isnull = " + (mView == null));
        if (mDomain!=null) mDomain.setCallback(this);

    }


    @Override
    public void unRegisterView() {
        Log.d(TAG, "------------------ unRegisterView");
        mView = null;
       if (mDomain!=null) mDomain.releaseCallback();
    }

    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
        Log.d(TAG,"------------------ setRvArrayList _rvArrayList.size()="+_rvArrayList.size());
        if (mView != null) mView.setRvArrayList(_rvArrayList);
    }

    @Override
    public void refreshData() {
        mDomain = new DomainImpl();
        mDomain.getData(this);
        Log.d(TAG, "------------------ refreshData  mView isnull = " + (mView == null));
    }


    @Override
    public void onRefreshResponse(DataResponse _data) {
        // TODO: 26.11.2016 здесь получаем колво
        Log.d(TAG, "onRefreshResponse >>>>------------------" + _data.getOrganizations().size());
    }

    @Override
    public void onSavedData(int _records) {
        Log.d(TAG, "onSavedData >>>>------------------" + _records + "/ mView isnull = " + (mView == null));
        //getBankList(null);
        if (mView != null) mView.getDbData(null);

    }

    @Override
    public void onSaveRefresh(int _itemNo, int _itemTotal) {
        Log.d(TAG, "------------------ _savedCount = " + _itemNo + "/ mView isnull = " + (mView == null));
        if (mView != null) mView.showProgress(_itemNo, _itemTotal);
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
    public void openMap(String _region, String _city, String _address, String _title) {
        mView.openMap(_region, _city, _address, _title);
    }

    @Override
    public void openCaller(String _phone) {
        mView.openCaller(_phone);
    }


}

package com.peterombodi.newconverterlab.presentation.screen.organisationList.presenter;

import android.util.Log;

import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.domain.IDomain;
import com.peterombodi.newconverterlab.presentation.Application;
import com.peterombodi.newconverterlab.presentation.ObjectGraph;
import com.peterombodi.newconverterlab.presentation.screen.base.ResponseCallback;
import com.peterombodi.newconverterlab.presentation.screen.organisationList.IListFragment;

import java.util.ArrayList;

/**
 * Created by Admin on 23.11.2016.
 */

public class BankListPresenter implements IListFragment.IPresenter, ResponseCallback<DataResponse> {

    private static final String TAG = "BankListPresenter";

    private IListFragment.IView mView;
    private IDomain mDomain;
    private ObjectGraph mGraph;
    private boolean isSavingData;

    @Override
    public void registerView(IListFragment.IView _view) {
        this.mView = _view;
        Log.d(TAG, ">>>>>>> registerView mView isnull = " + (mView == null));
        if (mDomain == null){

            mGraph = ObjectGraph.getInstance(Application.getContext());
            mDomain = mGraph.getDomainModule();
//            mDomain = new DomainImpl();
            mDomain.setCallback(this);
        }
//        if (mDomain != null) mDomain.setCallback(this);

    }


    @Override
    public void unRegisterView() {
        Log.d(TAG, ">>>>>>>>> unRegisterView");
        mView = null;
//        if (mDomain != null) mDomain.releaseCallback();
    }

    @Override
    public void presenterSetRV(ArrayList<OrganizationRV> _rvArrayList) {
        Log.d(TAG, ">>>>>>> presenterSetRV _rvArrayList.size()=" + _rvArrayList.size());
        if (mView != null) mView.setRvArrayList(_rvArrayList);
    }

    @Override
    public void refreshData() {
        Log.d(TAG, ">>>>>>>>> refreshData  mView isnull = " + (mView == null));

        mGraph = ObjectGraph.getInstance(Application.getContext());
        mDomain = mGraph.getDomainModule();
        mDomain.getData(this, null);
        isSavingData = true;

    }

    @Override
    public void getDbData(String _filter) {
        if (mView != null && !isSavingData ) mView.getDbData(_filter);
    }


    @Override
    public void onRefreshResponse(DataResponse _data) {
        Log.d(TAG, "onRefreshResponse >>>>------------------" + _data.getOrganizations().size());
    }

    @Override
    public void onSavedData(int _records, String _bankId, String _updateDate) {
        Log.d(TAG, "onSavedData >>>>------------------" + _records + "/ mView isnull = " + (mView == null));
        //getBankList(null);
        isSavingData = false;
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
    public void presenterOpenDetail(OrganizationRV _organizationRV) {
        Log.d(TAG, "presenterOpenDetail mView isnull - " + (mView == null));
        mView.viewOpenDetail(_organizationRV);
    }

    @Override
    public void presenterOpenLink(String _url) {
        Log.d(TAG, "presenterOpenLink _url =" + _url);
        mView.viewOpenLink(_url);
    }

    @Override
    public void presenterOpenMap(String _region, String _city, String _address, String _title) {
        mView.viewOpenMap(_region, _city, _address, _title);
    }

    @Override
    public void presenterOpenCaller(String _phone) {
        mView.viewOpenCaller(_phone);
    }


}

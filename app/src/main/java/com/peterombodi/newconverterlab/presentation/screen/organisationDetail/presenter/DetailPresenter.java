package com.peterombodi.newconverterlab.presentation.screen.organisationDetail.presenter;

import android.util.Log;

import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.domain.IDomain;
import com.peterombodi.newconverterlab.domain.DomainImpl;
import com.peterombodi.newconverterlab.presentation.screen.base.ResponseCallback;
import com.peterombodi.newconverterlab.presentation.screen.organisationDetail.IDetailFragment;

import java.util.ArrayList;

/**
 * Created by Admin on 02.12.2016.
 */

public class DetailPresenter implements IDetailFragment.IPresenter, ResponseCallback<DataResponse> {

    private static final String TAG = "DetailPresenter";
    private IDetailFragment.IView mView;
    private IDomain mDomain;

    @Override
    public void registerView(IDetailFragment.IView _view) {
        this.mView = _view;
        Log.d(TAG, "------------------ registerView");
        if (mDomain == null) mDomain = new DomainImpl();
        mDomain.setCallback(this);
    }

    @Override
    public void unRegisterView() {
        mView = null;
        if (mDomain != null) mDomain.releaseCallback();
    }

    @Override
    public void presenterSetRV(ArrayList<Currency> _rvArrayList) {
        if (mView != null) mView.setRvArrayList(_rvArrayList);
    }

    @Override
    public void refreshData(String _bankId) {
        mDomain = new DomainImpl();
        //mDomain.getDetailData((IDetailFragment.ResponseCallback) this, _bankId);
        Log.d(TAG, "*************** refreshData");
        mDomain.getData(this, _bankId);
    }


    @Override
    public void presenterOpenLink(String _url) {
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


    // данные получены
    @Override
    public void onRefreshResponse(DataResponse _data) {
        Log.d(TAG, "onRefreshResponse >>>>------------------" + _data.getOrganizations().size());
    }

    // данные записаны
    @Override
    public void onSavedData(int _records, String _bankId, String _updateDate) {
        mView.getDbData(_bankId);
        mView.setUpdateDate(_updateDate);
    }

    @Override
    public void onSaveRefresh(int _itemNo, int _itemTotal) {

    }

    @Override
    public void onRefreshFailure() {

    }
}

package com.peterombodi.newconverterlab.presentation.screen.organisationList;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public interface IListFragment {

    interface IPresenter {
        void registerView(IListFragment.IView _view);
        void unRegisterView();

        void presenterSetRV(ArrayList<OrganizationRV> _rvArrayList);
        void refreshData();

        void presenterOpenDetail(OrganizationRV _organizationRV);
        void presenterOpenLink(String _url);
        void presenterOpenMap(String _region, String _city, String _address, String _title);
        void presenterOpenCaller(String _phone);

    }

    interface IView {
        void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList);

        void viewOpenDetail(OrganizationRV _organizationRV);
        void viewOpenLink(String _url);
        void viewOpenMap(String _region, String _city, String _address, String _title);
        void viewOpenCaller(String _phone);

        void showProgress(int _itemNo, int _itemTotal);
        void getDbData(String _filter);
    }

//    interface ResponseCallback<V> {
//        void onRefreshResponse(V _data);
//        void onSavedData(int _records);
//        void onSaveRefresh(int _itemNo, int _itemTotal);
//        void onRefreshFailure();
//    }

}

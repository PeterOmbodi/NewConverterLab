package com.peterombodi.newconverterlab.presentation.screen.organisation_list;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public interface IListFragment {

    interface IFragment {
        void onRefreshRV(ArrayList<OrganizationRV> _arrayList);
    }


    interface IPresenter {
        void registerView(IListFragment.IView _view);
        void unRegisterView();
        ArrayList<OrganizationRV> getBankList(String _filter);
        void refreshData();
        void openDetail(OrganizationRV _organizationRV);
        void openLink(String _url);
        void openMap(String _region, String _city, String _address);
        void openCaller(String _phone);

    }

    interface IView {
        void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList);
        void openDetail(OrganizationRV _organizationRV);
        void openLink(String _url);
        void openMap(String _region, String _city, String _address);
        void openCaller(String _phone);
        void showProgress(int _itemNo, int _itemTotal);
    }

    interface ResponseCallback<V> {
        void onRefreshResponse(V _data);
        void onSavedData();
        void onSaveRefresh(int _itemNo, int _itemTotal);
        void onRefreshFailure();
    }

}

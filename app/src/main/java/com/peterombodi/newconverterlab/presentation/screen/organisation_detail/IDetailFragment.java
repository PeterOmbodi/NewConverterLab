package com.peterombodi.newconverterlab.presentation.screen.organisation_detail;

import com.peterombodi.newconverterlab.data.model.Currency;

import java.util.ArrayList;

/**
 * Created by Admin on 02.12.2016.
 */

public interface IDetailFragment {
    interface IPresenter {

        void registerView(IDetailFragment.IView _view);
        void unRegisterView();

        void presenterSetRV(ArrayList<Currency> _rvArrayList);
        void refreshData(String _bankId);

        void presenterOpenLink(String _url);
        void presenterOpenMap(String _region, String _city, String _address, String _title);
        void presenterOpenCaller(String _phone);

    }

    interface IView {
        void setRvArrayList(ArrayList<Currency> _rvArrayList);

        void viewOpenLink(String _url);
        void viewOpenMap(String _region, String _city, String _address, String _title);
        void viewOpenCaller(String _phone);

        void showProgress(int _itemNo, int _itemTotal);
        void getDbData(String _filter);
    }

}

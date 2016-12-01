package com.peterombodi.newconverterlab.presentation.screen.main;

import android.location.Address;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;

import java.util.ArrayList;

/**
 * Created by Admin on 21.11.2016.
 */

public interface IMainScreen {

    interface IView {
        void setBankListFragment(ArrayList<OrganizationRV> rvArrayList);
    }

    interface IPresenter {
        void registerView(IView _view);

        void unRegisterView();

        void domainCallTest(String _text);

        void dataAnswerTest(String _answer);

    }

    interface ResponseCallback<V> {
        void onResponse(V _data);

        void onFailure();
    }

    interface IGetAction {
        void openDetail(OrganizationRV _organizationRV);

        void openLink(String _url);

        void openMap(Address _address);

        void openCaller(String _phone);
    }

}

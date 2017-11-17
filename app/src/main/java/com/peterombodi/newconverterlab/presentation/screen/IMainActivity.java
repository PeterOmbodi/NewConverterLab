package com.peterombodi.newconverterlab.presentation.screen;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.screen.view.MainActivity;

/**
 * Created by Admin on 21.11.2016.
 */

public interface IMainActivity {

    interface IPresenter {
        void registerView(MainActivity _view);

        void unRegisterView();

        void domainCallTest(String _text);
    }


    interface IGetAction {
        void openDetail(OrganizationRV _organizationRV);

        void openLink(String _url);

        void openMap(String _region, String _city, String _address, String _title);

        void openCaller(String _phone);
    }

}

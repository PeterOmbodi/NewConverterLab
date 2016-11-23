package com.peterombodi.newconverterlab.presentation.screen.organisation_list;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public interface IListFragment {

    interface IFragment {
        void onRefreshRV(ArrayList<OrganizationRV> arrayList);
    }


    interface IPresenter {
        void registerView(IMainScreen.IView _view);
        void unRegisterView();
    }

    interface IView {
        void setRvArrayList(ArrayList<OrganizationRV> rvArrayList);
    }

}

package com.peterombodi.newconverterlab.domain;

import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

/**
 * Created by Admin on 18.11.2016.
 */

public interface Domain {
    void getData(IListFragment.ResponseCallback _callback);
    void releaseCallback();
    void setCallback(IListFragment.ResponseCallback _callback);
}

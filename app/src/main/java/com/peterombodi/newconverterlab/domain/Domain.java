package com.peterombodi.newconverterlab.domain;

import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

/**
 * Created by Admin on 18.11.2016.
 */

public interface Domain {
    String testString(String test);
    void getData(IListFragment.ResponseCallback _callback);
}

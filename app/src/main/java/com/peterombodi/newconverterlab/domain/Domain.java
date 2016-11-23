package com.peterombodi.newconverterlab.domain;

import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;

/**
 * Created by Admin on 18.11.2016.
 */

public interface Domain {
    String testString(String test);
    void getData(IMainScreen.ResponseCallback _callback);
}

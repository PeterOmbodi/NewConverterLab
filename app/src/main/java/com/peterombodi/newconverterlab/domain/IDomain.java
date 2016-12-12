package com.peterombodi.newconverterlab.domain;

import com.peterombodi.newconverterlab.presentation.screen.base.ResponseCallback;

/**
 * Created by Admin on 18.11.2016.
 */

public interface IDomain<V> {
    void getData(ResponseCallback _callback,String _bankId);
//    void getData(V _callback);
    void releaseCallback();
    void setCallback(ResponseCallback _callback);
}

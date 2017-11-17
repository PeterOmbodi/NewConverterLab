package com.peterombodi.newconverterlab.domain;

import com.peterombodi.newconverterlab.presentation.screen.ResponseCallback;

/**
 * Created by Admin on 18.11.2016.
 */

public interface IDomain<V> {
	void getData(ResponseCallback _callback, String _bankId);

	void releaseCallback();

	void setCallback(ResponseCallback _callback);
}

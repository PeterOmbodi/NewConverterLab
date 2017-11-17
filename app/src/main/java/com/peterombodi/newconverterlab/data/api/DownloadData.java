package com.peterombodi.newconverterlab.data.api;

import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.presentation.screen.ResponseCallback;

/**
 * Created by Admin on 18.11.2016.
 */

public interface DownloadData {
    void downloadData(ResponseCallback<DataResponse> _callback,String _bankId);

    void setCallback(ResponseCallback<DataResponse> _callback);

    void releaseCallback();

}

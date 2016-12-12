package com.peterombodi.newconverterlab.domain;


import android.util.Log;

import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.presentation.screen.base.ResponseCallback;

public class DomainImpl implements IDomain {

    private static final String TAG = "DomainImpl";
    private DownloadData downloadData;

     @Override
    public void getData(ResponseCallback _callback,String _bankId) {
        Log.d(TAG,">>>>>>>>>>>>>>>>>>=- getData");
        downloadData = new DownloadDataImpl();
        downloadData.downloadData(_callback,_bankId);
    }

    @Override
    public void releaseCallback() {
        Log.d(TAG,">>>>>>>>>>>>>>>>>>=- releaseCallback");
        if (downloadData!=null) downloadData.releaseCallback();
    }

    @Override
    public void setCallback(ResponseCallback _callback) {
        Log.d(TAG,">>>>>>>>>>>>>>>>>>=- setCallback");
        if (downloadData!=null) downloadData.setCallback(_callback);
    }


}

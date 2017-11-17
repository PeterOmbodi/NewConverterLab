package com.peterombodi.newconverterlab.domain;


import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.presentation.screen.ResponseCallback;

public class DomainImpl implements IDomain {

    private static final String TAG = "DomainImpl";
    private DownloadData downloadData;

     @Override
    public void getData(ResponseCallback _callback,String _bankId) {
        downloadData = new DownloadDataImpl();
        downloadData.downloadData(_callback,_bankId);
    }

    @Override
    public void releaseCallback() {
        if (downloadData!=null) downloadData.releaseCallback();
    }

    @Override
    public void setCallback(ResponseCallback _callback) {
        if (downloadData!=null) downloadData.setCallback(_callback);
    }


}

package com.peterombodi.newconverterlab.domain;


import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

public class DomainImpl implements Domain {

    private static final String TAG = "DomainImpl";
    private DownloadData downloadData;

     @Override
    public void getData(IListFragment.ResponseCallback _callback) {
        downloadData = new DownloadDataImpl();
        downloadData.downloadData(_callback);
    }

    @Override
    public void releaseCallback() {
        if (downloadData!=null) downloadData.releaseCallback();
    }

    @Override
    public void setCallback(IListFragment.ResponseCallback _callback) {
        if (downloadData!=null) downloadData.setCallback(_callback);
    }

}

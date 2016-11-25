package com.peterombodi.newconverterlab.domain;


import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

public class DomainImpl implements Domain {

    private static final String TAG = "DomainImpl";

    @Override
    public String testString(String _test) {
        return _test + ". answer from -" + this.getClass().getName();
    }

    @Override
    public void getData(IListFragment.ResponseCallback _callback) {
        DownloadData downloadData = new DownloadDataImpl();
        downloadData.downloadData(_callback);
    }
}

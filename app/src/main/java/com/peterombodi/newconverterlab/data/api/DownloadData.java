package com.peterombodi.newconverterlab.data.api;

import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

/**
 * Created by Admin on 18.11.2016.
 */

public interface DownloadData {
    void downloadData(IListFragment.ResponseCallback<DataResponse> _callback);
    void releaseCallback();
    void setCallback(IListFragment.ResponseCallback<DataResponse> _callback);

}

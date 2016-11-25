package com.peterombodi.newconverterlab.data.api;

import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

import java.util.ArrayList;

/**
 * Created by Admin on 18.11.2016.
 */

public interface DownloadData {
    void downloadData(IListFragment.ResponseCallback<DataResponse> _callback);

    ArrayList<OrganizationRV> getDbData(String _filter);
}

package com.peterombodi.newconverterlab.data.api;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;

import java.util.ArrayList;

/**
 * Created by Admin on 18.11.2016.
 */

public interface DownloadData {
    void downloadData(IMainScreen.ResponseCallback _callback);
    ArrayList<OrganizationRV> getDbData();
}

package com.peterombodi.newconverterlab.presentation.screen.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.peterombodi.newconverterlab.presentation.R;

/**
 * Created by Admin on 24.11.2016.
 */

public class LegalNoticesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);

        TextView legal=(TextView)findViewById(R.id.legal);
        // TODO: 24.11.2016 !!!!!!!!!!!!!! 
        legal.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
    }
}

package com.peterombodi.newconverterlab.data.geo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Admin on 25.11.2016.
 */

public class GeoCoderLoader extends AsyncTaskLoader<LatLng> {

    private static final String TAG = "GeoCoderLoader";
    private String strAddress;
    private Context context;
    public final static String ARGS_ADDRESS = "address";

    public GeoCoderLoader(Context context, Bundle args) {
        super(context);
        this.context = context;
        if (args != null)
            strAddress = args.getString(ARGS_ADDRESS);
            Log.d(TAG,"strAddress = "+strAddress);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public LatLng loadInBackground() {

        Log.d(TAG,"loadInBackground 1 strAddress = "+strAddress);

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            for (int i = 1; i < 6; i++) {
                Log.d(TAG,"loadInBackground 3 strAddress = "+strAddress);
                address = coder.getFromLocationName(strAddress, 1);
                if (address == null || address.size() == 0) {
                    strAddress = strAddress.substring(0, strAddress.length() - 1);
                } else {
                    Address location = address.get(0);
                    location.getLatitude();
                    location.getLongitude();
                    p1 = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG,p1.toString());
                    return p1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }
}

package com.peterombodi.newconverterlab.data.geo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by Admin on 25.11.2016.
 */

public class GeoCoderLoader<A> extends AsyncTaskLoader<Address> {

    private static final String TAG = "GeoCoderLoader";
    public static final String ARGS_REGION = "ARGS_REGION";
    public final static String ARGS_ADDRESS = "ARGS_ADDRESS";
    public static final String ARGS_CITY = "ARGS_CITY";
    public static final String ARGS_TITLE = "ARGS_TITLE";

    private String strAddress;
    private String region;
    private String city;
    private String address;
    private String title;
    private Context context;


    public GeoCoderLoader(Context context, Bundle args) {
        super(context);
        this.context = context;
        if (args != null) {
            region = args.getString(ARGS_REGION);
            city = args.getString(ARGS_CITY);
            address = args.getString(ARGS_ADDRESS);
            title = args.getString(ARGS_TITLE);
            strAddress = region + "," + city + "," + address;
        } else {
            strAddress = "Ужгород, Шандора Петефи, 47" ;
        }
//        Log.d(TAG, "strAddress = " + strAddress);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "++++ onStartLoading ");
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public Address loadInBackground() {

        Log.d(TAG, "++++ loadInBackground 1 strAddress = " + strAddress);

        Geocoder coder = new Geocoder(context);
        List<Address> listAddress;
        Address location = null;
        try {
            for (int i = 1; i < 6; i++) {
                Log.d(TAG, "loadInBackground 3 strAddress = " + strAddress);
                listAddress = coder.getFromLocationName(strAddress, 1);

                if (listAddress == null || listAddress.size() == 0) {
                    strAddress = strAddress.substring(0, strAddress.length() - 1);
                } else {

                    location = listAddress.get(0);

                    Bundle bundle = new Bundle();
                    bundle.putString(GeoCoderLoader.ARGS_CITY, city);
                    bundle.putString(GeoCoderLoader.ARGS_ADDRESS, address);
                    bundle.putString(GeoCoderLoader.ARGS_TITLE, title);
                    location.setExtras(bundle);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return location;
    }
}

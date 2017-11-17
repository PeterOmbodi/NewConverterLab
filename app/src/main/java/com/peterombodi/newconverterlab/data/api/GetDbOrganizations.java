package com.peterombodi.newconverterlab.data.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.Application;

import java.util.ArrayList;

import static com.peterombodi.newconverterlab.data.database.DBHelper.ADDRESS_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.CITY_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.LINK_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.PHONE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.REGION_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TITLE_COLUMN;

/**
 * Created by Admin on 28.11.2016.
 */

public class GetDbOrganizations extends AsyncTaskLoader<ArrayList<OrganizationRV>> {

    private static final String TAG = "GetDbOrganizations";
    public final static String ARGS_FILTER = "filter";
    private String filter;
    private ArrayList<OrganizationRV> organizationRVList;

    public GetDbOrganizations(Context context, Bundle args) {
        super(context);
        if (args != null)
            filter = args.getString(ARGS_FILTER);
    }

    @Override
    protected void onStartLoading() {
        if (organizationRVList==null){
            forceLoad();
        } else {
            deliverResult(organizationRVList);
        }
        super.onStartLoading();
    }

    @Override
    public ArrayList<OrganizationRV> loadInBackground() {
        return getDbData(filter);
    }



    private ArrayList<OrganizationRV> getDbData(String _filter) {
        CoursesLabDb db;
        db = new CoursesLabDb(Application.getContext());
        db.open();
        ArrayList<OrganizationRV> rvArrayList = new ArrayList<>();
        Cursor banks = db.getData4RV(_filter);
        if (banks.getCount() > 0) {
            banks.moveToFirst();
            while (!banks.isAfterLast()) {
                OrganizationRV bank4RV = new OrganizationRV();
                bank4RV.setId(banks.getString(banks.
                        getColumnIndex(ID_COLUMN)));
                bank4RV.setTitle(banks.getString(banks.
                        getColumnIndex(TITLE_COLUMN)));
                bank4RV.setRegion(banks.getString(banks.
                        getColumnIndex(REGION_COLUMN)));
                bank4RV.setCity(banks.getString(banks.
                        getColumnIndex(CITY_COLUMN)));
                bank4RV.setPhone(banks.getString(banks.
                        getColumnIndex(PHONE_COLUMN)));
                bank4RV.setAddress(banks.getString(banks.
                        getColumnIndex(ADDRESS_COLUMN)));
                bank4RV.setLink(banks.getString(banks.
                        getColumnIndex(LINK_COLUMN)));
                bank4RV.setDate(banks.getString(banks.
                        getColumnIndex(DATE_COLUMN)));
                bank4RV.setDateDelta(banks.getString(banks.
                        getColumnIndex(DATE_DELTA_COLUMN)));
                rvArrayList.add(bank4RV);
                banks.moveToNext();
            }
            banks.close();
            db.close();
        }
        organizationRVList=rvArrayList;
        return rvArrayList;
    }


}

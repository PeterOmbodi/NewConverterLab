package com.peterombodi.newconverterlab.data.api;

import android.database.Cursor;
import android.util.Log;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.Organization;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.Application;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.peterombodi.newconverterlab.data.database.DBHelper.*;

/**
 * Created by Admin on 18.11.2016.
 */

public class DownloadDataImpl implements DownloadData {

    private static final String TAG = "DownloadDataImpl";
    private Call<DataResponse> dataResponseCall;
    private Retrofit retrofit;
    private DataResponse dataResponse;
    private ArrayList<OrganizationRV> rvArrayList;


    public DownloadDataImpl() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://resources.finance.ua")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Override
    public void downloadData(final IMainScreen.ResponseCallback _callback) {
        Log.d(TAG, "getData1");

        ApiRest getData = retrofit.create(ApiRest.class);
        dataResponseCall = getData.connect();
        dataResponseCall.enqueue(new Callback<DataResponse>() {

            @Override
            public void onResponse(Call<DataResponse> _call, Response<DataResponse> _response) {
                DataResponse dataResponse = _response.body();
                Log.d(TAG, "*-* " + "retrofit success:" + dataResponse.toString());
                saveData(dataResponse);
                if (_callback != null) _callback.onResponse(dataResponse);
            }

            @Override
            public void onFailure(Call<DataResponse> _call, Throwable _t) {
                Log.d(TAG, "*-* " + "retrofit onFailure:" + _t.toString());
                if (_callback != null){
                    _callback.onFailure();
                }
            }
        });

        Log.d(TAG, "getData2");
    }

    private void saveData(final DataResponse _dataResponse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CoursesLabDb db;
                db = new CoursesLabDb(Application.getContext());
                db.open();

                Cursor cursor = db.getLastUpdate();
                cursor.moveToFirst();

                String lastDBUpdate = (cursor.getCount() > 0) ?
                        cursor.getString(cursor.getColumnIndex(DATE_COLUMN)) : "new data";
                String lastJsonUpdate = _dataResponse.getDate();
                int bankCount = _dataResponse.getOrganizations().size();

                Log.d(TAG, "lastDBUpdate = " + lastDBUpdate);
                Log.d(TAG, "lastJsonUpdate = " + lastJsonUpdate);
                Log.d(TAG, "bankCount = " + bankCount);

                if (!lastDBUpdate.equals(lastJsonUpdate)) {
                    db.setLastUpdate(lastJsonUpdate);
                    for (Organization organization : _dataResponse.getOrganizations()) {
                        String currentOrgId = organization.getId();

                        //update currencies course
                        HashMap<String, Currency> currencies = organization.getCurrencies();
                        for (Map.Entry<String, Currency> entry : currencies.entrySet()) {
                            String key = entry.getKey();
                            Currency currency = entry.getValue();
                            db.updateCourse(currentOrgId, key, currency.getAsk(), currency.getBid());
                        }

                        // delete old courses, that which is not in new data
                        // don work properly
                        // db.deleteOldCourses(currentOrgId);
                        //update organizations data
                        db.updateOrg(currentOrgId, organization.getTitle(), organization.getRegionId(),
                                organization.getCityId(), organization.getPhone(), organization.getAddress(),
                                organization.getLink(), lastJsonUpdate);

                    }
                    dictionUpdate(db, TBL_CURRENCIES, _dataResponse.getCurrencies());
                    dictionUpdate(db, TBL_CITIES, _dataResponse.getCities());
                    dictionUpdate(db, TBL_REGIONS, _dataResponse.getRegions());
                }
                db.close();
            }
        }).start();
    }

    private void dictionUpdate(CoursesLabDb db, String table, HashMap<String, String> dictionArrayList) {
        for (Map.Entry<String, String> entry : dictionArrayList.entrySet()) {
            db.updateDiction(table, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public ArrayList<OrganizationRV> getDbData() {
        Log.d(TAG, " ************ getDbData ++++++++++++++++++++++++ ");
        CoursesLabDb db;
        db = new CoursesLabDb(Application.getContext());
        db.open();
        rvArrayList = new ArrayList<>();
        Cursor banks = db.getData4RV();
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
        } else {
            //// TODO: 24.04.2016 show message?
            Log.d(TAG, "--------- getDbData = 0");
        }
        return rvArrayList;
    }

}

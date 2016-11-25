package com.peterombodi.newconverterlab.data.api;

import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.Organization;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.Application;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.peterombodi.newconverterlab.data.database.DBHelper.ADDRESS_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.CITY_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.LINK_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.PHONE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.REGION_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CITIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CURRENCIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_REGIONS;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TITLE_COLUMN;

/**
 * Created by Admin on 18.11.2016.
 */

public class DownloadDataImpl implements DownloadData {

    private static final String TAG = "DownloadDataImpl";
    private Call<DataResponse> dataResponseCall;
    private Retrofit retrofit;
    private DataResponse dataResponse;
    private ArrayList<OrganizationRV> rvArrayList;
    private Handler handler;
    private IListFragment.ResponseCallback<DataResponse> callback;

    public DownloadDataImpl() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://resources.finance.ua")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                callback.onSaveData();
            };
        };

    }

    @Override
    public void downloadData(final IListFragment.ResponseCallback<DataResponse> _callback) {
        Log.d(TAG, "getData1");
        callback = _callback;
        ApiRest getData = retrofit.create(ApiRest.class);
        dataResponseCall = getData.connect();
        dataResponseCall.enqueue(new Callback<DataResponse>() {

            @Override
            public void onResponse(Call<DataResponse> _call, Response<DataResponse> _response) {
                DataResponse dataResponse = _response.body();
                Log.d(TAG, "*-* " + "retrofit success:" + dataResponse.toString());
                saveData(dataResponse,_callback);
                if (_callback != null) _callback.onRefreshResponse(dataResponse);
            }

            @Override
            public void onFailure(Call<DataResponse> _call, Throwable _t) {
                Log.d(TAG, "*-* " + "retrofit onRefreshFailure:" + _t.toString());
                if (_callback != null){
                    _callback.onRefreshFailure();
                }
            }
        });

        Log.d(TAG, "getData2");
    }

    private void saveData(final DataResponse _dataResponse,final IListFragment.ResponseCallback _callback) {
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
                if (callback != null) handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void dictionUpdate(CoursesLabDb db, String table, HashMap<String, String> dictionArrayList) {
        for (Map.Entry<String, String> entry : dictionArrayList.entrySet()) {
            db.updateDiction(table, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public ArrayList<OrganizationRV> getDbData(String _filter) {
        Log.d(TAG, " ************ getDbData ++++++++++++++++++++++++ ");
        CoursesLabDb db;
        db = new CoursesLabDb(Application.getContext());
        db.open();
        rvArrayList = new ArrayList<>();
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
        } else {
            //// TODO: 24.04.2016 show message?
            Log.d(TAG, "--------- getDbData = 0");
        }
        return rvArrayList;
    }

}

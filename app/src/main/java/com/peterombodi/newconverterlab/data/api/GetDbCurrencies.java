package com.peterombodi.newconverterlab.data.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.presentation.Application;

import java.util.ArrayList;

import static com.peterombodi.newconverterlab.data.database.DBHelper.ASK_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ASK_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.BID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.BID_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.NAME_COLUMN;

/**
 * Created by Admin on 01.12.2016.
 */

public class GetDbCurrencies extends AsyncTaskLoader<ArrayList<Currency>> {

    private static final String TAG = "GetDbCurrencies";
    public static final String ARGS_BANK_ID = "ARGS_BANK_ID";
    private ArrayList<Currency> currencies;
    private String bankId;

//    private Context context;

    public GetDbCurrencies(Context context, Bundle args) {
        super(context);
        if (args != null)
            bankId = args.getString(ARGS_BANK_ID);
    }

    @Override
    protected void onStartLoading() {
        if (currencies ==null){
            forceLoad();
        } else {
            deliverResult(currencies);
        }
        super.onStartLoading();
    }

    @Override
    public ArrayList<Currency> loadInBackground() {
        return getDbData(bankId);
    }



    private ArrayList<Currency> getDbData(String _filter) {
        Log.d(TAG, " ************ getDbData ++++++++++++++++++++++++ ");
        CoursesLabDb db;
        db = new CoursesLabDb(Application.getContext());
        db.open();
        ArrayList<Currency> currencies = new ArrayList<>();
        Cursor cursor = db.getCourses(_filter);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Currency currency = new Currency();

                currency.setCurrency(cursor.getString(
                        cursor.getColumnIndex(NAME_COLUMN)));
                currency.setAsk(cursor.getString(
                        cursor.getColumnIndex(ASK_COLUMN)));
                currency.setBid(cursor.getString(
                        cursor.getColumnIndex(BID_COLUMN)));
                currency.setAskDelta(cursor.getString(
                        cursor.getColumnIndex(ASK_DELTA_COLUMN)));
                currency.setBidDelta(cursor.getString(
                        cursor.getColumnIndex(BID_DELTA_COLUMN)));

                currencies.add(currency);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        this.currencies =currencies;
        return currencies;
    }


}
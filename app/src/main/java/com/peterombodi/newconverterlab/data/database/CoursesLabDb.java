package com.peterombodi.newconverterlab.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.peterombodi.newconverterlab.global.Constants;

import java.util.Locale;

import static com.peterombodi.newconverterlab.data.database.DBHelper.*;

/**
 * Created by Admin on 22.11.2016.
 */

public class CoursesLabDb {

    //private static Context mContext;
    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public CoursesLabDb(Context _context) {
        mContext = _context;
    }

    // open connect
    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // begin Transaction
    public void beginTransaction() {
        mDB.beginTransaction();
    }

    // end Transaction
    public void endTransaction() {
        mDB.endTransaction();
    }

    // set Transaction Successful
    public void setTransactionSuccessful() {
        mDB.setTransactionSuccessful() ;
    }


    // close connect
    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }


    // update (or add if need) record in TBL_ORGANIZATIONS
    public void updateOrg(String id, String title, String regionId, String cityId, String phone,
                          String address, String link, String date) {

        String select = "SELECT [" + TBL_ORGANIZATIONS + "].[" + DATE_COLUMN + "] " +
                "FROM [" + TBL_ORGANIZATIONS + "]" +
                "WHERE [" + TBL_ORGANIZATIONS + "].[" + ID_COLUMN + "] = ? ";

        //get date bank last update
        Cursor findEntry = mDB.rawQuery(select, new String[]{id});
        String date_delta = "";
        if (findEntry.getCount() > 0) {
            findEntry.moveToFirst();
            date_delta = findEntry.getString(findEntry.getColumnIndex(DATE_COLUMN));
        }
        findEntry.close();
        ContentValues cv = new ContentValues();
        cv.put(TITLE_COLUMN, TextUtils.isEmpty(title) ? "" : title);
        cv.put(REGION_ID_COLUMN, TextUtils.isEmpty(regionId) ? "" : regionId);
        cv.put(CITY_ID_COLUMN, TextUtils.isEmpty(cityId) ? "" : cityId);
        cv.put(PHONE_COLUMN, TextUtils.isEmpty(phone) ? "" : phone);
        cv.put(ADDRESS_COLUMN, TextUtils.isEmpty(address) ? "" : address);
        cv.put(LINK_COLUMN, TextUtils.isEmpty(link) ? "" : link);
        cv.put(DATE_COLUMN, date);
        cv.put(DATE_DELTA_COLUMN, date_delta);
        int updCount = mDB.update(TBL_ORGANIZATIONS, cv, ID_COLUMN + " = '" + id + "'", null);
        cv.put(ID_COLUMN, id);
        if (updCount == 0) mDB.insert(TBL_ORGANIZATIONS, null, cv);
    }

    // update (or add if need) record in diction
    public void updateDiction(String diction, String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, value);
        int updCount = mDB.update(diction, cv, ID_COLUMN + " = '" + key + "'", null);
        cv.put(ID_COLUMN, key);
        if (updCount == 0) mDB.insert(diction, null, cv);
    }

    // getting last update
    public Cursor getLastUpdate() {
        return mDB.query(TBL_UPDATES, null, null, null, null, null, DATE_COLUMN + " DESC");
    }

    // set last update
    public void setLastUpdate(String _date, boolean _fromUI) {
        ContentValues cv = new ContentValues();
        cv.put(DATE_COLUMN, _date);
        cv.put(UPDATE_FROM_UI_COLUMN, _fromUI ? 1 : 0);
        if (Constants.DEBUG_MODE) {
            mDB.insert(TBL_UPDATES, null, cv);
        } else {
            int updCount = mDB.update(TBL_UPDATES, cv, null, null);
            if (updCount == 0) mDB.insert(TBL_UPDATES, null, cv);
        }
    }

    public void updateCourse(String orgId, String currencyId, String ask, String bid) {

//        String select = " SELECT [organizations].[date]," +
//                "[courses].[ask]," +
//                "[courses].[bid]," +
//                "[courses].[currencyId]" +
//                "FROM [courses]" +
//                "INNER JOIN [organizations] ON [organizations].[id] = [courses].[orgId]" +
//                "WHERE [organizations].[id] = '"+orgId+"'and [courses].[currencyId] = '"+currencyId+"'";


        String select = " SELECT [" + TBL_ORGANIZATIONS + "].[" + DATE_COLUMN + "]," +
                "[" + TBL_COURSES + "].[" + ASK_COLUMN + "]," +
                "[" + TBL_COURSES + "].[" + BID_COLUMN + "]," +
                "[" + TBL_COURSES + "].[" + CURRENCY_ID_COLUMN + "]" +
                "FROM [" + TBL_COURSES + "]" +
                "INNER JOIN [" + TBL_ORGANIZATIONS + "] ON " +
                "[" + TBL_ORGANIZATIONS + "].[" + ID_COLUMN + "] = " +
                "[" + TBL_COURSES + "].[" + ORG_ID_COLUMN + "]" +
                "WHERE [" + TBL_ORGANIZATIONS + "].[" + ID_COLUMN + "] = ? " +
                "and [" + TBL_COURSES + "].[" + CURRENCY_ID_COLUMN + "] = ? ";


        //get old course value
        Cursor findEntry = mDB.rawQuery(select, new String[]{orgId, currencyId});
        String deltaAsk = "0.00";
        String deltaBid = "0.00";
        String previousDate = "";
        if (findEntry.getCount() > 0) {
            findEntry.moveToFirst();
            String askOld = findEntry.getString(findEntry.getColumnIndex(ASK_COLUMN));
            String bidOld = findEntry.getString(findEntry.getColumnIndex(BID_COLUMN));
            deltaAsk = String.format(Locale.US, "%.2f", Float.valueOf(ask) - Float.valueOf(askOld));
            deltaBid = String.format(Locale.US, "%.2f", Float.valueOf(bid) - Float.valueOf(bidOld));
            previousDate = findEntry.getString(findEntry.getColumnIndex(DATE_COLUMN));
        }
        findEntry.close();

        //delete old course value
        mDB.delete(TBL_COURSES, "[orgId] = ? and [courses].[currencyId] = ?", new String[]{orgId, currencyId});

        //insert new course value
        ContentValues cv = new ContentValues();
        cv.put(ORG_ID_COLUMN, orgId);
        cv.put(CURRENCY_ID_COLUMN, currencyId);
        cv.put(ASK_COLUMN, ask);
        cv.put(BID_COLUMN, bid);
        cv.put(ASK_DELTA_COLUMN, deltaAsk);
        cv.put(BID_DELTA_COLUMN, deltaBid);
        //saving date for *_Delta columns
        cv.put(DATE_COLUMN, previousDate);

        mDB.insert(TBL_COURSES, null, cv);

    }

    // getting data for RecyclerView
    public Cursor getData4RV(String _filter) {

//        SELECT [organizations].[id],
//        [organizations].[title],
//        [organizations].[phone],
//        [organizations].[address],
//        [organizations].[link],
//        [organizations].[date],
//        [organizations].[dateDelta],
//        [regions].[name] AS [region],
//        [cities].[name] AS [city]
//        FROM [organizations]
//        INNER JOIN [cities] ON [organizations].[cityId] = [cities].[id]
//        INNER JOIN [regions] ON [organizations].[regionId] = [regions].[id]


        String select = "SELECT [" + TBL_ORGANIZATIONS + "].[" + ID_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + TITLE_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + PHONE_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + ADDRESS_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + LINK_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + DATE_COLUMN + "]," +
                "[" + TBL_ORGANIZATIONS + "].[" + DATE_DELTA_COLUMN + "]," +
                "[" + TBL_REGIONS + "].[" + NAME_COLUMN + "] AS [" + REGION_COLUMN + "]," +
                "[" + TBL_CITIES + "].[" + NAME_COLUMN + "] AS [" + CITY_COLUMN + "]" +
                "FROM [" + TBL_ORGANIZATIONS + "]" +
                "INNER JOIN [" + TBL_CITIES + "] ON " +
                "[" + TBL_ORGANIZATIONS + "].[" + CITY_ID_COLUMN + "] " +
                "= [" + TBL_CITIES + "].[" + ID_COLUMN + "]" +
                "INNER JOIN [" + TBL_REGIONS + "] ON " +
                "[" + TBL_ORGANIZATIONS + "].[" + REGION_ID_COLUMN + "] " +
                "= [" + TBL_REGIONS + "].[" + ID_COLUMN + "]";
        if (_filter != null) {
            select = select + " WHERE  like(\"%" + _filter + "%\"," + TITLE_COLUMN + ") " +
                    " || like(\"%" + _filter + "%\"," + REGION_COLUMN + ")" +
                    " || like(\"%" + _filter + "%\"," + CITY_COLUMN + ")";
        }

        return mDB.rawQuery(select, null);
    }


}

package com.peterombodi.newconverterlab.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.peterombodi.newconverterlab.global.Constants;

import java.util.Locale;

import static com.peterombodi.newconverterlab.data.database.DBHelper.ADDRESS_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ASK_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ASK_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.BID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.BID_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.CITY_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.CITY_ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.CURRENCY_ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_DELTA_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DB_NAME;
import static com.peterombodi.newconverterlab.data.database.DBHelper.DB_VERSION;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.LINK_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.NAME_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.ORG_ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.PHONE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.REGION_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.REGION_ID_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CITIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_COURSES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CURRENCIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_ORGANIZATIONS;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_REGIONS;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_UPDATES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TITLE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.UPDATE_COUNT_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.UPDATE_FROM_UI_COLUMN;

/**
 * Created by Admin on 22.11.2016.
 */

public class CoursesLabDb {

	private static final String TAG = "CoursesLabDb";
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
		mDB.setTransactionSuccessful();
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
		return mDB.query(TBL_UPDATES, null, null, null, null, null, DATE_COLUMN + " DESC", "1");
	}

	// set last update
	public void setLastUpdate(String _date, int _count, boolean _fromUI) {
		ContentValues cv = new ContentValues();
		cv.put(DATE_COLUMN, _date);
		cv.put(UPDATE_COUNT_COLUMN, _count);
		cv.put(UPDATE_FROM_UI_COLUMN, _fromUI ? 1 : 0);
		if (Constants.DEBUG_MODE) {
			mDB.insert(TBL_UPDATES, null, cv);
		} else {
			int updCount = mDB.update(TBL_UPDATES, cv, null, null);
			if (updCount == 0)
				mDB.insert(TBL_UPDATES, null, cv);
		}
	}

	public boolean updateCourse(String orgId, String currencyId, String ask, String bid, String _lastJsonUpdate) {

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
		Boolean needUpdate = true;

		if (findEntry.getCount() > 0) {
			findEntry.moveToFirst();
			String askOld = findEntry.getString(findEntry.getColumnIndex(ASK_COLUMN));
			String bidOld = findEntry.getString(findEntry.getColumnIndex(BID_COLUMN));
			Float dAsk = Float.valueOf(ask) - Float.valueOf(askOld);
			Float dBid = Float.valueOf(bid) - Float.valueOf(bidOld);
			needUpdate = (dAsk != 0 || dBid != 0);
			deltaAsk = String.format(Locale.US, "%.2f", dAsk);
			deltaBid = String.format(Locale.US, "%.2f", dBid);
			previousDate = _lastJsonUpdate;
			//findEntry.getString(findEntry.getColumnIndex(DATE_COLUMN));
		}

		findEntry.close();
		if (needUpdate) {
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
		return needUpdate;
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

	public Cursor getCourses(String id) {

//        SELECT [courses].[currencyId],
//        [currencies].[name],
//        [courses].[ask],
//        [courses].[bid],
//        [courses].[askDelta],
//        [courses].[bidDelta]
//        FROM [courses]
//        INNER JOIN [currencies] ON [courses].
//        [currencyId] = [currencies].[id]
//        WHERE [courses].[orgId]='7oiylpmiow8iy1smaze'

		String select = "SELECT " +
			"[" + TBL_COURSES + "].[" + CURRENCY_ID_COLUMN + "]," +
			"[" + TBL_CURRENCIES + "].[" + NAME_COLUMN + "]," +
			"[" + TBL_COURSES + "].[" + ASK_COLUMN + "]," +
			"[" + TBL_COURSES + "].[" + BID_COLUMN + "]," +
			"[" + TBL_COURSES + "].[" + ASK_DELTA_COLUMN + "]," +
			"[" + TBL_COURSES + "].[" + BID_DELTA_COLUMN + "]" +
			"FROM [" + TBL_COURSES + "]" +
			"INNER JOIN [" + TBL_CURRENCIES + "] ON [" + TBL_COURSES + "].[" +
			CURRENCY_ID_COLUMN + "] = [" + TBL_CURRENCIES + "].[" + ID_COLUMN + "]" +
			"WHERE [" + TBL_COURSES + "].[" + ORG_ID_COLUMN + "]=?";

		Cursor cursor = mDB.rawQuery(select, new String[]{id});
		return cursor;

	}

}

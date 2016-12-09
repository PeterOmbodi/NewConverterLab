package com.peterombodi.newconverterlab.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 22.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "coursesLab.db";
    static final int DB_VERSION = 2;
    //tables
    static final String TBL_ORGANIZATIONS = "organizations";
    static final String TBL_COURSES = "courses";
    public static final String TBL_CURRENCIES = "currencies";
    public static final String TBL_REGIONS = "regions";
    public static final String TBL_CITIES = "cities";
    static final String TBL_UPDATES = "updates";
    //columns
    private static final String KEY_ID_COLUMN = "_id";
    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    static final String REGION_ID_COLUMN = "regionId";
    static final String CITY_ID_COLUMN = "cityId";
    public static final String PHONE_COLUMN = "phone";
    public static final String ADDRESS_COLUMN = "address";
    public static final String LINK_COLUMN = "link";
    static final String ORG_ID_COLUMN = "orgId";
    public static final String CURRENCY_ID_COLUMN = "currencyId";
    public static final String ASK_COLUMN = "ask";
    public static final String BID_COLUMN = "bid";
    public static final String ASK_DELTA_COLUMN = "askDelta";
    public static final String BID_DELTA_COLUMN = "bidDelta";
    public static final String NAME_COLUMN = "name";
    public static final String DATE_COLUMN = "date";
    public static final String REGION_COLUMN = "region";
    public static final String CITY_COLUMN = "city";
    public static final String DATE_DELTA_COLUMN = "dateDelta";
    public static final String UPDATE_FROM_UI_COLUMN = "updateUI";


    private static final String CREATE_ORGANIZATIONS = "create table "
            + TBL_ORGANIZATIONS + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + ID_COLUMN + " text not null, "
            + TITLE_COLUMN + " text not null, "
            + REGION_ID_COLUMN + " text not null, "
            + CITY_ID_COLUMN + " text not null ,"
            + PHONE_COLUMN + " text not null ,"
            + ADDRESS_COLUMN + " text not null ,"
            + LINK_COLUMN + " text not null ,"
            + DATE_COLUMN + " text not null ,"
            + DATE_DELTA_COLUMN + " text not null "
            + ");";

    private static final String CREATE_COURSES = "create table "
            + TBL_COURSES + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + ORG_ID_COLUMN + " text not null, "
            + CURRENCY_ID_COLUMN + " text not null, "
            + ASK_COLUMN + " text not null, "
            + BID_COLUMN + " text not null ,"
            + ASK_DELTA_COLUMN + " text not null ,"
            + BID_DELTA_COLUMN + " text not null ,"
            + DATE_COLUMN + " text not null "
            + ");";

    private static final String CREATE_CURRENCIES = "create table "
            + TBL_CURRENCIES + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + ID_COLUMN + " text not null, "
            + NAME_COLUMN + " text not null "
            + ");";

    private static final String CREATE_REGIONS = "create table "
            + TBL_REGIONS + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + ID_COLUMN + " text not null, "
            + NAME_COLUMN + " text not null "
            + ");";

    private static final String CREATE_CITIES = "create table "
            + TBL_CITIES + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + ID_COLUMN + " text not null, "
            + NAME_COLUMN + " text not null "
            + ");";

    private static final String CREATE_UPDATES = "create table "
            + TBL_UPDATES + " (" + KEY_ID_COLUMN + " integer primary key autoincrement, "
            + UPDATE_FROM_UI_COLUMN + " integer not null, "
            + DATE_COLUMN + " text not null "
            + ");";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ORGANIZATIONS);
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_CURRENCIES);
        db.execSQL(CREATE_REGIONS);
        db.execSQL(CREATE_CITIES);
        db.execSQL(CREATE_UPDATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ORGANIZATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CURRENCIES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_REGIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_UPDATES);
        onCreate(db);
    }
}

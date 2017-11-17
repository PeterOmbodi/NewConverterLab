package com.peterombodi.newconverterlab.data.api;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.UpdateInfo;
import com.peterombodi.newconverterlab.presentation.Application;

import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.UPDATE_COUNT_COLUMN;

/**
 * Created by Peter on 14.11.2017.
 */

public class GetDbUpdate extends AsyncTaskLoader<UpdateInfo> {

	private UpdateInfo updateInfo;

	public GetDbUpdate(Context context) {
		super(context);
	}

	@Override
	public UpdateInfo loadInBackground() {
		return getDbData();
	}

	@Override
	protected void onStartLoading() {
		if (updateInfo == null) {
			forceLoad();
		} else {
			deliverResult(updateInfo);
		}
		super.onStartLoading();
	}

	private UpdateInfo getDbData() {
		CoursesLabDb db;
		db = new CoursesLabDb(Application.getContext());
		db.open();
		UpdateInfo updateInfo = new UpdateInfo();
		Cursor cursor = db.getLastUpdate();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			updateInfo.setUpdateDate(cursor.getString(cursor.getColumnIndex(DATE_COLUMN)));
			updateInfo.setUpdateCount(cursor.getInt(cursor.getColumnIndex(UPDATE_COUNT_COLUMN)));
			cursor.close();
			db.close();
		}
		return updateInfo;
	}


}

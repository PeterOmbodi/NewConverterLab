package com.peterombodi.newconverterlab.data.api;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.peterombodi.newconverterlab.data.database.CoursesLabDb;
import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.Organization;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.Application;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.ResponseCallback;
import com.peterombodi.newconverterlab.presentation.screen.view.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.peterombodi.newconverterlab.data.database.DBHelper.DATE_COLUMN;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CITIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_CURRENCIES;
import static com.peterombodi.newconverterlab.data.database.DBHelper.TBL_REGIONS;

/**
 * Created by Admin on 18.11.2016.
 */

public class DownloadDataImpl implements DownloadData {

	private static final String TAG = "DownloadDataImpl";

	private static final int STATUS_DATA_UPDATE = 1;
	private static final int STATUS_SAVE_REFRESH = 2;
	private static final int STATUS_DATA_SAVED = 3;

	private static final int REFRESH_ON = 20;
	private static final boolean USE_TRANSACTION = true;
	private static final int KEY_NOTIFICATION = 111;
	private Retrofit retrofit;
	private ResponseCallback<DataResponse> callback;
	private String bankId;
	private String updateDate;

	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotifyManager;
	private Context context;
	private Handler handler;

	public DownloadDataImpl() {

		retrofit = new Retrofit.Builder()
			.baseUrl(Constants.PARAM_BASE_URL_FINANCE_UA)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		context = Application.getContext();

	}

	@Override
	public void downloadData(final ResponseCallback<DataResponse> _callback, final String _bankId) {
		callback = _callback;
		bankId = _bankId;
		ApiRest getData = retrofit.create(ApiRest.class);
		Call<DataResponse> dataResponseCall = getData.connect();
		dataResponseCall.enqueue(new Callback<DataResponse>() {

			@Override
			public void onResponse(Call<DataResponse> _call, Response<DataResponse> _response) {
				DataResponse dataResponse = _response.body();
				saveData(dataResponse, callback, _bankId);
				if (callback != null) callback.onRefreshResponse(dataResponse);

			}

			@Override
			public void onFailure(Call<DataResponse> _call, Throwable _t) {
				Log.d(TAG, "*-* " + "retrofit onRefreshFailure:" + _t.toString());
				if (callback != null) {
					callback.onRefreshFailure();
				}
			}
		});
	}


	public void setCallback(ResponseCallback<DataResponse> _callback) {
		this.callback = _callback;
	}

	@Override
	public void releaseCallback() {
		this.callback = null;
	}


	private void saveData(final DataResponse _dataResponse, final ResponseCallback<DataResponse> _callback, String _bankId) {
		if (_callback != null) setHandler();
		Executor executor = Executors.newCachedThreadPool();
		executor.execute(new RunnableSaveTask(_dataResponse, _callback, _bankId) {
		});
	}

	private void dictionUpdate(CoursesLabDb db, String table, HashMap<String, String> dictionArrayList) {
		for (Map.Entry<String, String> entry : dictionArrayList.entrySet()) {
			db.updateDiction(table, entry.getKey(), entry.getValue());
		}
	}

	private void showNotification() {
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	notificationIntent, 0);

		mNotifyManager =
			(NotificationManager) Application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(Application.getContext());
		mBuilder
			.setContentTitle(Application.getContext().getResources().getString(R.string.app_name))
			.setContentText(Application.getContext().getResources().getString(R.string.download_in_progress))
			.setContentIntent(pendingIntent)
			.setSmallIcon(android.R.drawable.stat_sys_download)
			.setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icn_converter_lab_w), 128, 128, false));
	}


	private void updateNotification(int _progress, int _total) {
		if (mBuilder != null) {
			mBuilder.setProgress(_total, _progress, false);
			String contentText = context.getResources().getString(R.string.download_in_progress)
				+ ", " + _progress + " - " + _total;
			mBuilder.setContentText(contentText);
			mNotifyManager.notify(KEY_NOTIFICATION, mBuilder.build());
		}
	}

	private void setHandler() {
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case STATUS_DATA_UPDATE:
						showNotification();
						break;
					case STATUS_SAVE_REFRESH:
						if (callback != null) callback.onSaveRefresh(msg.arg1, msg.arg2);
						updateNotification(msg.arg1, msg.arg2);
						break;
					case STATUS_DATA_SAVED:
						if (callback != null) callback.onSavedData(msg.arg1, bankId, updateDate);
						if (mBuilder != null) {
							mBuilder
								.setContentText(Application.getContext().getResources().getString(R.string.download_complete))
								.setSmallIcon(android.R.drawable.stat_sys_download_done);
							mNotifyManager.notify(KEY_NOTIFICATION, mBuilder.build());
						}
						break;
				}
			}
		};
	}

	private class RunnableSaveTask implements Runnable {

		DataResponse dataResponse;
		ResponseCallback<DataResponse> callback;
		String bankId;

		private final boolean saveAll;

		RunnableSaveTask(DataResponse _dataResponse, ResponseCallback<DataResponse> _callback, String _bankId) {
			this.dataResponse = _dataResponse;
			this.callback = _callback;
			this.bankId = _bankId;
			saveAll = (_bankId == null);
		}

		@Override
		public void run() {
			CoursesLabDb db;
			db = new CoursesLabDb(Application.getContext());
			db.open();
			if (USE_TRANSACTION) db.beginTransaction();
			Cursor cursor = db.getLastUpdate();
			cursor.moveToFirst();
			String lastDBUpdate = (cursor.getCount() > 0) ?
				cursor.getString(cursor.getColumnIndex(DATE_COLUMN)) : "new data";
			String lastJsonUpdate = dataResponse.getDate();
			int bankCount = dataResponse.getOrganizations().size();

			Log.d(TAG, "lastDBUpdate = " + lastDBUpdate);
			Log.d(TAG, "lastJsonUpdate = " + lastJsonUpdate);
			Log.d(TAG, "bankCount = " + bankCount);
// TODO: 04.12.2016 Алгоритм д.б. приблизительно таким:
			/* 1 проверить есть ли изменение хоть в одной паре по банку
            *  2 только если есть:
            *           - поменять все пары
            *           - поменять дату обновления
            *  3 в новой посылке могут быть другие справочники
            *    проверить и обновить ключи!
            *  4 заменить дату проверки(обновления)
            * */

			if (!lastDBUpdate.equals(lastJsonUpdate)) {
				updateDate = lastJsonUpdate;
				if (handler != null) handler.sendEmptyMessage(STATUS_DATA_UPDATE);
				db.setLastUpdate(lastJsonUpdate, bankCount, (callback != null));
				int i = 0;
				for (Organization organization : dataResponse.getOrganizations()) {
					String currentOrgId = organization.getId();
					if (saveAll || currentOrgId.equals(bankId)) {
						//update currencies course
						HashMap<String, Currency> currencies = organization.getCurrencies();
						boolean needUpdate = false;
						for (Map.Entry<String, Currency> entry : currencies.entrySet()) {
							String key = entry.getKey();
							Currency currency = entry.getValue();
							needUpdate = (db.updateCourse(currentOrgId, key, currency.getAsk(), currency.getBid(), lastJsonUpdate)
								|| needUpdate);
						}
						// delete old courses, that which is not in new data
						// don work properly
						// db.deleteOldCourses(currentOrgId);
						//update organizations data
//                        if (needUpdate)

						db.updateOrg(currentOrgId, organization.getTitle(), organization.getRegionId(),
							organization.getCityId(), organization.getPhone(), organization.getAddress(),
							organization.getLink(), lastJsonUpdate);

						i++;
						if ((i % REFRESH_ON == 0 || i == 1 || i == bankCount) && (handler != null)) {
							Message msg = handler.obtainMessage(STATUS_SAVE_REFRESH, i, bankCount);
							handler.sendMessage(msg);
						}
					}
				}
				dictionUpdate(db, TBL_CURRENCIES, dataResponse.getCurrencies());
				dictionUpdate(db, TBL_CITIES, dataResponse.getCities());
				dictionUpdate(db, TBL_REGIONS, dataResponse.getRegions());
			}

			if (USE_TRANSACTION) db.setTransactionSuccessful();
			if (USE_TRANSACTION) db.endTransaction();

			db.close();
			if (handler != null) {
				Message msg = handler.obtainMessage(STATUS_DATA_SAVED, bankCount);
				handler.sendMessage(msg);
			}
		}
	}


}


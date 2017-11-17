package com.peterombodi.newconverterlab.services;

/**
 * Created by Admin on 18.11.2016.
 */

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;
import com.peterombodi.newconverterlab.presentation.ObjectGraph;
import com.peterombodi.newconverterlab.presentation.screen.presenter.BankListPresenter;

public class DownloaderJobService extends JobService {

	private static final String TAG = "DownloaderJobService";

	@Override
	public boolean onStartJob(JobParameters job) {
		Log.d(TAG, ">>>>>>>>>>> onStartJob job.getTag() = " + job.getTag());
		ObjectGraph mGraph = ObjectGraph.getInstance();
		BankListPresenter presenter = mGraph.getPresenterModule();
		DownloadData downloadData = new DownloadDataImpl();
		downloadData.downloadData(presenter, null);
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters job) {
		return false;
	}

}

package com.peterombodi.newconverterlab.data.downloader;

/**
 * Created by Admin on 18.11.2016.
 */

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.peterombodi.newconverterlab.data.api.DownloadData;
import com.peterombodi.newconverterlab.data.api.DownloadDataImpl;

public class DownloaderJobService extends JobService {

    private static final String TAG = "DownloaderJobService";
    private DownloadData downloadData;

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG,">>>>>>>>>>> onStartJob job.getTag()="+job.getTag()+" / job.getLifetime() ="+job.getLifetime());
        this.downloadData = new DownloadDataImpl();
        downloadData.downloadData(null,null);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG,">>>>>>>>>>> onStopJob");
        return false;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,">>>>>>>>>>> onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,">>>>>>>>>>> onDestroy");
        super.onDestroy();
    }
}
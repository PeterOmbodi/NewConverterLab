package com.peterombodi.newconverterlab.presentation;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.peterombodi.newconverterlab.services.DownloaderJobService;

/**
 * Created by Admin on 21.11.2016.
 */

public class Application extends android.app.Application {

    private static final String TAG = "Application";
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ObjectGraph.getInstance();   //init ObjectGraph when application created

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(DownloaderJobService.class) // the JobService that will be called
                .setTag("newConverterLab")               // uniquely identifies the job
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
//                .setTrigger(Trigger.executionWindow(30*60,35*60))
                .setTrigger(Trigger.executionWindow(0,60))
                .setConstraints(Constraint.ON_ANY_NETWORK,
                    Constraint.DEVICE_IDLE)
                .build();

        //dispatcher.mustSchedule(myJob);
        final int result = dispatcher.schedule(myJob);
        if (result != FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS) {
            Log.d(TAG,"result != FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS");
        }
    }


    public static Context getContext() {
        return instance;
    }
}

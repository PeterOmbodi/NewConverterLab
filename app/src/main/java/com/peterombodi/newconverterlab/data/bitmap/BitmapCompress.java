package com.peterombodi.newconverterlab.data.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Admin on 08.12.2016.
 */

public class BitmapCompress extends AsyncTaskLoader<File> {

    public static final java.lang.String ARGS_BITMAP = "ARGS_BITMAP";
    private static final String TAG = "BitmapCompress";
    private Bitmap bitmap;
    private File file;
    private boolean success;

    public BitmapCompress(Context context, Bundle args) {
        super(context);
        if (args != null) {
            bitmap = args.getParcelable(ARGS_BITMAP);
            Log.d(TAG,"_______ bitmap isnull =  " + (bitmap ==null));
        }
    }


    @Override
    protected void onStartLoading() {
        Log.d(TAG,"_______ onStartLoading ");
        if (file==null){
            Log.d(TAG,"_______ onStartLoading.forceLoad ");
            forceLoad();
        } else {
            deliverResult(file);
        }
        super.onStartLoading();
    }


    @Override
    public File loadInBackground() {

        Log.d(TAG,"_______ loadInBackground  ");

        File tmpFile = new File(Environment.getExternalStorageDirectory() + "/CourseLab.png");
        try {
            success = bitmap.compress(Bitmap.CompressFormat.PNG, 90, new FileOutputStream(tmpFile));
            Log.d(TAG,"_______ success = "+success);
        } catch (Exception e) {
            Log.d(TAG,"_______ Exception = "+e.toString());
            e.printStackTrace();
            return null;
        }
        return tmpFile;
    }
}

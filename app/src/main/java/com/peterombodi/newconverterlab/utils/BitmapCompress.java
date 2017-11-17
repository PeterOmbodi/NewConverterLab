package com.peterombodi.newconverterlab.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;

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

    public BitmapCompress(Context context, Bundle args) {
        super(context);
        if (args != null) {
            bitmap = args.getParcelable(ARGS_BITMAP);
        }
    }


    @Override
    protected void onStartLoading() {
        if (file==null){
            forceLoad();
        } else {
            deliverResult(file);
        }
        super.onStartLoading();
    }


    @Override
    public File loadInBackground() {
        File tmpFile = new File(Environment.getExternalStorageDirectory() + "/CourseLab.png");
        try {
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 90, new FileOutputStream(tmpFile));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return tmpFile;
    }
}

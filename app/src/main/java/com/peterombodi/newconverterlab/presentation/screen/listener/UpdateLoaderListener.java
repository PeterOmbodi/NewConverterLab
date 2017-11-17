package com.peterombodi.newconverterlab.presentation.screen.listener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.peterombodi.newconverterlab.data.api.GetDbUpdate;
import com.peterombodi.newconverterlab.data.model.UpdateInfo;
import com.peterombodi.newconverterlab.presentation.screen.IListFragment;

/**
 * Created by Peter on 16.11.2017.
 */

public class UpdateLoaderListener implements LoaderManager.LoaderCallbacks<UpdateInfo>  {

	private Context context;
	private IListFragment.IPresenter presenter;

	public UpdateLoaderListener(Context context, IListFragment.IPresenter presenter) {
		this.context = context;
		this.presenter = presenter;
	}

	@Override
	public Loader<UpdateInfo> onCreateLoader(int id, Bundle args) {
		return new GetDbUpdate(context);
	}

	@Override
	public void onLoadFinished(Loader<UpdateInfo> loader, UpdateInfo data) {
		presenter.onLoadDbUpdateInfo(data);
	}

	@Override
	public void onLoaderReset(Loader<UpdateInfo> loader) {

	}
}

package com.peterombodi.newconverterlab.presentation.screen.listener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.peterombodi.newconverterlab.data.api.GetDbOrganizations;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.screen.IListFragment;

import java.util.ArrayList;

/**
 * Created by Peter on 16.11.2017.
 */

public class OrganizationsLoaderListener implements LoaderManager.LoaderCallbacks<ArrayList<OrganizationRV>> {

	private Context context;
	private IListFragment.IPresenter presenter;

	public OrganizationsLoaderListener(Context context, IListFragment.IPresenter presenter) {
		this.context = context;
		this.presenter = presenter;
	}

	@Override
	public Loader<ArrayList<OrganizationRV>> onCreateLoader(int id, Bundle args) {
		return new GetDbOrganizations(context, args);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<OrganizationRV>> loader, ArrayList<OrganizationRV> data) {
		presenter.onLoadDbData(data);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<OrganizationRV>> loader) {

	}
}

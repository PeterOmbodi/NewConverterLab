package com.peterombodi.newconverterlab.presentation.screen.view;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.peterombodi.newconverterlab.data.api.GetDbOrganizations;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.ObjectGraph;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.IMainActivity;
import com.peterombodi.newconverterlab.presentation.screen.listener.OrganizationsLoaderListener;
import com.peterombodi.newconverterlab.presentation.screen.listener.UpdateLoaderListener;
import com.peterombodi.newconverterlab.presentation.screen.recycleView.BankRVAdapter;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankListFragment extends Fragment implements
	IListFragment.IView,
	SearchView.OnQueryTextListener {

	private static final String TAG = "BankListFragment";
	static final int LOADER_DB_ORGANIZATIONS = 2;
	static final int LOADER_DB_UPDATE_INFO = 3;
	private static final String SEARCH_KEY = "SEARCH_KEY";
	private static final String SUBTITLE_KEY = "SUBTITLE_KEY";

	private BankRVAdapter rvAdapter;
	private Context context;
	private RecyclerView recyclerView;
	private IListFragment.IPresenter presenter;
	private IMainActivity.IGetAction iGetAction;
	private SwipeRefreshLayout swipeContainer;
	private ProgressDialog progressDialog;
	private SearchView searchView;
	private String searchQuery;
	private MenuItem item;
	private Toolbar toolbar;

	private OrganizationsLoaderListener organizationsLoaderListener;
	private UpdateLoaderListener updateLoaderListener;

	public BankListFragment() {
	}


	@Override
	public void onAttach(Context context) {
		if (presenter == null) {
			ObjectGraph mGraph = ObjectGraph.getInstance();
			presenter = mGraph.getPresenterModule();
		}
		super.onAttach(context);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {

        /* Если передать в него true, то при пересоздании фрагмента не будут вызваны методы
		onDestroy и onCreate, и не будет создан новый экземпляр класса Fragment. */
		//setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		context = getActivity();

		presenter.registerView(this);
		iGetAction = (IMainActivity.IGetAction) context;

		toolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		toolbar.setTitle(getResources().getString(R.string.app_name));
		setHasOptionsMenu(true);
		recyclerView = (RecyclerView) view.findViewById(R.id.rv_banks_FL);


		// Lookup the swipe container view
		swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer_FL);
		if (swipeContainer != null) {
			swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					item.collapseActionView();
					presenter.refreshData();
				}
			});
			// Configure the refreshing colors
			swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_bright);
		}
		organizationsLoaderListener = new OrganizationsLoaderListener(context, presenter);
		updateLoaderListener = new UpdateLoaderListener(context, presenter);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.menu_option, menu);
		item = menu.findItem(R.id.search);

		searchView = (SearchView) MenuItemCompat.getActionView(item);
		SearchManager searchManager =
			(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(
			searchManager.getSearchableInfo(getActivity().getComponentName()));

		searchView.setOnQueryTextListener(this);
		if (searchQuery != null) {
			if (!TextUtils.isEmpty(searchQuery)) item.expandActionView();
			searchView.setQuery(searchQuery, true);
		}
	}


	@Override
	public void onResume() {
		if (getLoaderManager().getLoader(LOADER_DB_ORGANIZATIONS) == null) presenter.loadDbData(null);
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (getLoaderManager().getLoader(LOADER_DB_ORGANIZATIONS) != null) {
			getLoaderManager().destroyLoader(LOADER_DB_ORGANIZATIONS);
		}
		presenter.unRegisterView();
		iGetAction = null;
		super.onDestroyView();
	}

	@Override
	public boolean onQueryTextSubmit(String _query) {
		return true;
	}

	@Override
	public boolean onQueryTextChange(String _query) {
		if ((getLoaderManager().getLoader(LOADER_DB_ORGANIZATIONS) == null)
			|| !_query.isEmpty() || (searchQuery != null && !searchQuery.isEmpty()))
			presenter.loadDbData(_query);
		searchQuery = _query;
		return false;
	}


	@Override
	public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
		if (recyclerView != null && recyclerView.getAdapter() != null) {
			rvAdapter.animateTo(_rvArrayList);
			recyclerView.scrollToPosition(0);
		} else {
			rvAdapter = new BankRVAdapter(presenter, _rvArrayList);
			recyclerView.setAdapter(rvAdapter);
		}
		if (swipeContainer != null) swipeContainer.setRefreshing(false);
	}


	@Override
	public void viewOpenDetail(OrganizationRV _organizationRV) {
		iGetAction.openDetail(_organizationRV);
	}

	@Override
	public void viewOpenLink(String _url) {
		iGetAction.openLink(_url);
	}


	@Override
	public void viewOpenCaller(String _phone) {
		iGetAction.openCaller(_phone);
	}


	@Override
	public void viewOpenMap(String _region, String _city, String _address, String _title) {
		iGetAction.openMap(_region, _city, _address, _title);
	}

	@Override
	public void getDbData(String _string) {
		Bundle bundle = new Bundle();
		bundle.putString(GetDbOrganizations.ARGS_FILTER, _string);
		if (getLoaderManager().getLoader(LOADER_DB_ORGANIZATIONS) == null) {
			getLoaderManager().initLoader(LOADER_DB_ORGANIZATIONS, bundle, organizationsLoaderListener);
		} else {
			getLoaderManager().restartLoader(LOADER_DB_ORGANIZATIONS, bundle, organizationsLoaderListener);
		}
	}

	@Override
	public void getDbUpdateInfo() {
		if (getLoaderManager().getLoader(LOADER_DB_UPDATE_INFO) == null) {
			getLoaderManager().initLoader(LOADER_DB_UPDATE_INFO, null, updateLoaderListener);
		} else {
			getLoaderManager().restartLoader(LOADER_DB_UPDATE_INFO, null, updateLoaderListener);
		}
	}

	@Override
	public void showUpdateInfo(String date, int itemsCount) {
		toolbar.setSubtitle(getResources().getString(R.string.last_update_date)
			+ " " + date + ", " + getResources().getString(R.string.last_update_count) + " " + itemsCount);
	}

	@Override
	public void showMessage(int resourceId) {
		Toast.makeText(context, getResources().getString(resourceId), Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean isSearching() {
		return searchView != null && !searchView.getQuery().toString().isEmpty();
	}

	@Override
	public void showProgress(int _itemNo, int _itemTotal) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(R.string.loading_data_msg));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(_itemTotal);
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.show();
		}
		progressDialog.setProgress(_itemNo);
		if (_itemNo == _itemTotal) progressDialog.cancel();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		searchQuery = (searchView != null) ? searchView.getQuery().toString() : null;
		String subTitle = (toolbar != null && toolbar.getSubtitle() != null) ? toolbar.getSubtitle().toString() : "";
		outState.putString(SEARCH_KEY, searchQuery);
		outState.putString(SUBTITLE_KEY, subTitle);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			searchQuery = savedInstanceState.getString(SEARCH_KEY);
			toolbar.setSubtitle(savedInstanceState.getString(SUBTITLE_KEY));
		}
		super.onViewStateRestored(savedInstanceState);
	}

}



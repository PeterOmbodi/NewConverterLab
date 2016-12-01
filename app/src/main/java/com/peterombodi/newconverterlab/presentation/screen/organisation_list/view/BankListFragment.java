package com.peterombodi.newconverterlab.presentation.screen.organisation_list.view;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.peterombodi.newconverterlab.data.api.GetDbData;
import com.peterombodi.newconverterlab.data.geo.GeoCoderLoader;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.main.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter.BankListPresenter;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankListFragment extends Fragment implements
        IListFragment.IView,
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = "BankListFragment";
    static final int LOADER_GEOCODER_ID = 1;
    static final int LOADER_DATABASE_ID = 2;
    private static final String SAVED_LAYOUT_MANAGER = "SAVED_LAYOUT_MANAGER";
    private static final String SEARCH_KEY = "SEARCH_KEY";


    private BankRVAdapter rvAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private IListFragment.IPresenter presenter;
    private IMainScreen.IGetAction iGetAction;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog progressDialog;
    private Parcelable layoutManagerSavedState;
    private SearchView searchView;
    private MenuItem item;
    private String searchQuery;
    private boolean firstRun;


    public BankListFragment() {
    }


    @Override
    public void onAttach(Context context) {
         Log.d(TAG,">>>>>>>>>>> onAttach");
        if (presenter == null) presenter = new BankListPresenter();



        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /* Если передать в него true, то при пересоздании фрагмента не будут вызваны методы
        onDestroy и onCreate, и не будет создан новый экземпляр класса Fragment. */
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, ">>>> onCreateView");

        view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_banks_FL);

        presenter.registerView(this);
        iGetAction = (IMainScreen.IGetAction) context;
        setHasOptionsMenu(true);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer_FL);
        if (swipeContainer != null) {
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    presenter.refreshData();
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                    android.R.color.holo_red_dark);
        }
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
            ///searchView.clearFocus();
            Log.d(TAG, ">>>> onCreateOptionsMenu query = " + searchView.getQuery() + "/ searchQuery = " + searchQuery);
        }

    }


    @Override
    public void onResume() {
        Log.d(TAG, ">>>>----- onResume ");
        getDbData(null);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, ">>>>----- onDestroyView");
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (getLoaderManager().getLoader(LOADER_GEOCODER_ID) != null) {
            getLoaderManager().destroyLoader(LOADER_GEOCODER_ID);
        }
        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) != null) {
            getLoaderManager().destroyLoader(LOADER_DATABASE_ID);
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
        getDbData(_query);
        return false;
    }


    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
        Log.d(TAG, ">>>>----- setRvArrayList");
        if (recyclerView != null && recyclerView.getLayoutManager() != null && recyclerView.getAdapter() != null) {
            rvAdapter.animateTo(_rvArrayList);
            recyclerView.scrollToPosition(0);
        } else {
            rvAdapter = new BankRVAdapter(presenter, _rvArrayList);
            recyclerView.setAdapter(rvAdapter);
        }
        if (swipeContainer != null) swipeContainer.setRefreshing(false);
    }

    @Override
    public void openDetail(OrganizationRV _organizationRV) {

    }

    @Override
    public void openLink(String _url) {
        Log.d(TAG, "openLink _url =" + _url);
        iGetAction.openLink(_url);
    }



    @Override
    public void openCaller(String _phone) {
        iGetAction.openCaller(_phone);
    }


    @Override
    public void openMap(String _region, String _city, String _address, String _title) {
        if (checkPlayServices()) {

            Bundle bundle = new Bundle();
            bundle.putString(GeoCoderLoader.ARGS_REGION, _region);
            bundle.putString(GeoCoderLoader.ARGS_CITY, _city);
            bundle.putString(GeoCoderLoader.ARGS_ADDRESS, _address);
            bundle.putString(GeoCoderLoader.ARGS_TITLE, _title);
            if (getLoaderManager().getLoader(LOADER_GEOCODER_ID) == null) {
                getLoaderManager().initLoader(LOADER_GEOCODER_ID, bundle, this);
            } else {
                getLoaderManager().restartLoader(LOADER_GEOCODER_ID, bundle, this);
            }
        }
    }

    @Override
    public void getDbData(String _string) {
        Bundle bundle = new Bundle();
        bundle.putString(GetDbData.ARGS_FILTER, _string);
        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) == null) {
            getLoaderManager().initLoader(LOADER_DATABASE_ID, bundle, this);
        } else {
            getLoaderManager().restartLoader(LOADER_DATABASE_ID, bundle, this);
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_GEOCODER_ID:
                return new GeoCoderLoader(context, args);
            case LOADER_DATABASE_ID:
                return new GetDbData(context, args);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object _data) {
        switch (loader.getId()) {
            case LOADER_GEOCODER_ID:
                if (_data != null) {
                    iGetAction.openMap((Address) _data);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.msg_no_latlng), Toast.LENGTH_SHORT).show();
                }
                break;
            case LOADER_DATABASE_ID:
                if (_data != null) {
                    presenter.setRvArrayList((ArrayList<OrganizationRV>) _data);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.msg_no_latlng), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset: " + loader.hashCode());
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result, 9999).show();
            }
            return false;
        }
        return true;
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
        searchQuery = searchView.getQuery().toString();
        outState.putString(SEARCH_KEY, searchQuery);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_KEY);
        }
        super.onViewStateRestored(savedInstanceState);


    }




}



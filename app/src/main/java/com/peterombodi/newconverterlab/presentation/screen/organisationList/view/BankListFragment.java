package com.peterombodi.newconverterlab.presentation.screen.organisationList.view;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.peterombodi.newconverterlab.data.api.GetDbOrganizations;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.mainActivity.IMainScreen;
import com.peterombodi.newconverterlab.presentation.screen.organisationList.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisationList.presenter.BankListPresenter;

import java.util.ArrayList;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankListFragment extends Fragment implements
        IListFragment.IView,
        SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = "BankListFragment";
    static final int LOADER_DATABASE_ID = 2;
    private static final String SEARCH_KEY = "SEARCH_KEY";

    private BankRVAdapter rvAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private IListFragment.IPresenter presenter;
    private IMainScreen.IGetAction iGetAction;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private String searchQuery;
    private MenuItem item;


    public BankListFragment() {
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, ">>>>>>>>>>> onAttach");
        if (presenter == null) presenter = new BankListPresenter();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, ">>>> onCreate ");

        /* Если передать в него true, то при пересоздании фрагмента не будут вызваны методы
        onDestroy и onCreate, и не будет создан новый экземпляр класса Fragment. */
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, ">>>> onCreateView ");
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = getActivity();

        if (savedInstanceState==null) {
            presenter.registerView(this);
            iGetAction = (IMainScreen.IGetAction) context;
        }


        Toolbar mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mActionBarToolbar);
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
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, ">>>> onCreateOptionsMenu ");
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

        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) == null) getDbData(null);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, ">>>>----- onDestroyView");
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
//        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) != null) {
//            getLoaderManager().destroyLoader(LOADER_DATABASE_ID);
//        }


        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.unRegisterView();
        iGetAction = null;
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String _query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String _query) {
        Log.d(TAG, ">>>>>>>> onQueryTextChange");
        if ((getLoaderManager().getLoader(LOADER_DATABASE_ID) == null)
                || !_query.isEmpty() || (searchQuery != null && !searchQuery.isEmpty()))
            getDbData(_query);
        searchQuery = _query;
        return false;
    }


    @Override
    public void setRvArrayList(ArrayList<OrganizationRV> _rvArrayList) {
        Log.d(TAG, ">>>>----- presenterSetRV");
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
        if (getLoaderManager().getLoader(LOADER_DATABASE_ID) == null) {
            Log.d(TAG, ">>>>>>>>> initLoader");
            getLoaderManager().initLoader(LOADER_DATABASE_ID, bundle, this);
        } else {
            Log.d(TAG, ">>>>>>>> restartLoader");
            getLoaderManager().restartLoader(LOADER_DATABASE_ID, bundle, this);
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new GetDbOrganizations(context, args);
    }

    @Override
    public void onLoadFinished(Loader loader, Object _data) {
        Log.d(TAG, "++++* onLoadFinished loader id = " + loader.getId());
        if (_data != null) {
            ArrayList<OrganizationRV> arrayList = (ArrayList<OrganizationRV>) _data;
            if (arrayList.size() == 0
                    && (((searchView != null) && searchView.getQuery().toString().isEmpty()) || (searchView == null))) {
                presenter.refreshData();
            } else {
                presenter.presenterSetRV(arrayList);
            }

        } else {
            Toast.makeText(context, getResources().getString(R.string.msg_no_data_geted), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "onLoaderReset: " + loader.hashCode());
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
        Log.d(TAG, ">>>>>>> onSaveInstanceState ");
        super.onSaveInstanceState(outState);
        searchQuery = (searchView != null) ? searchView.getQuery().toString() : null;
        outState.putString(SEARCH_KEY, searchQuery);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, ">>>>>> onViewStateRestored savedInstanceState isnull=" + (savedInstanceState == null));
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_KEY);
        }
        super.onViewStateRestored(savedInstanceState);
    }


}



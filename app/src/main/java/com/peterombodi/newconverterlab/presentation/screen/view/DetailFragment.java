package com.peterombodi.newconverterlab.presentation.screen.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.peterombodi.newconverterlab.data.api.GetDbCurrencies;
import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.IMainActivity;
import com.peterombodi.newconverterlab.presentation.screen.IDetailFragment;
import com.peterombodi.newconverterlab.presentation.screen.recycleView.CourseRVAdapter;
import com.peterombodi.newconverterlab.presentation.screen.presenter.DetailPresenter;
import com.peterombodi.newconverterlab.presentation.screen.dialog.ShareFragment;

import java.util.ArrayList;

import static com.peterombodi.newconverterlab.global.Constants.KEY_ARRAY_LIST;
import static com.peterombodi.newconverterlab.global.Constants.TAG_DIALOG_SHARE_FRAGMENT;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Currency>>, IDetailFragment.IView {

	private static final String TAG = "DetailFragment";
	static final int LOADER_DATABASE_ID = 7;
	private static final String KEY_BANK_DATA = "KEY_BANK_DATA";
	private View view;
	private OrganizationRV bank;
	private RecyclerView recyclerView;
	private CourseRVAdapter rvAdapter;
	private ArrayList<Currency> courseList;
	private IMainActivity.IGetAction iActivity;
	private FloatingActionMenu menuFloat;
	private FloatingActionButton fabLink;
	private FloatingActionButton fabMap;
	private FloatingActionButton fabPhone;
	private Context context;
	private IDetailFragment.IPresenter presenter;
	private SwipeRefreshLayout swipeContainer;
	private Toolbar toolbar;

	public DetailFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (presenter == null) presenter = new DetailPresenter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_detail, container, false);
		context = getActivity();
		presenter.registerView(this);
		toolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		setHasOptionsMenu(true);
		if (getArguments() != null && savedInstanceState == null) {
			bank = getArguments().getParcelable(Constants.KEY_BANK);
			if (bank != null) getDbData(bank.getId());
		}
		if (savedInstanceState == null)
			initializeViews();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_share, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_share:
				showShareDialog();
				break;
			case android.R.id.home:
				getActivity().onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showShareDialog() {
		DialogFragment dialog = ShareFragment.newInstance(bank, courseList);
		dialog.show(getActivity().getSupportFragmentManager(), TAG_DIALOG_SHARE_FRAGMENT);
	}

	@Override
	public void onDestroyView() {
		presenter.unRegisterView();
		super.onDestroyView();
	}

	@Override
	public void setRvArrayList(ArrayList<Currency> _rvArrayList) {
		courseList = _rvArrayList;
		if (recyclerView != null && recyclerView.getAdapter() != null) {
			rvAdapter.animateTo(_rvArrayList);
			recyclerView.scrollToPosition(0);
		} else {
			rvAdapter = new CourseRVAdapter(_rvArrayList);
			recyclerView.setAdapter(rvAdapter);
		}
		if (swipeContainer != null) swipeContainer.setRefreshing(false);

	}

	@Override
	public void setUpdateDate(String _date) {
		TextView textView = (TextView) view.findViewById(R.id.tv_update_FD);
		if (_date != null) {
			String date = _date.substring(0, 16).replace("T", " ");
			String dateDelta = bank.getDateDelta();
			dateDelta = (dateDelta.length() == 0) ? "" :
				bank.getDateDelta().substring(0, 16).replace("T", " ");
			String updateDate = date
				+ "\n (" + dateDelta + ")";
			textView.setText(updateDate);
		}
	}

	@Override
	public void viewOpenLink(String _url) {
		iActivity.openLink(bank.getLink());
	}

	@Override
	public void viewOpenMap(String _region, String _city, String _address, String _title) {
		iActivity.openMap(_region, _city, _address, _title);
	}

	@Override
	public void viewOpenCaller(String _phone) {
		iActivity.openCaller(bank.getPhone());
	}

	public void getDbData(String _string) {
		Bundle bundle = new Bundle();
		bundle.putString(GetDbCurrencies.ARGS_BANK_ID, _string);
		if (getLoaderManager().getLoader(LOADER_DATABASE_ID) == null) {
			getLoaderManager().initLoader(LOADER_DATABASE_ID, bundle, this);
		} else {
			getLoaderManager().restartLoader(LOADER_DATABASE_ID, bundle, this);
		}
	}


	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.fab_link_FD:
					presenter.presenterOpenLink(bank.getLink());
					break;
				case R.id.fab_map_FD:
					presenter.presenterOpenMap(bank.getRegion(), bank.getCity(), bank.getAddress(), bank.getTitle());
					break;
				case R.id.fab_phone_FD:
					presenter.presenterOpenCaller(bank.getPhone());
					break;

			}
		}
	};

	private void createCustomAnimation() {
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuFloat.getMenuIconView(), "scaleX", 1.0f, 0.2f);
		ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuFloat.getMenuIconView(), "scaleY", 1.0f, 0.2f);
		ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuFloat.getMenuIconView(), "scaleX", 0.2f, 1.0f);
		ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuFloat.getMenuIconView(), "scaleY", 0.2f, 1.0f);
		scaleOutX.setDuration(50);
		scaleOutY.setDuration(50);
		scaleInX.setDuration(150);
		scaleInY.setDuration(150);
		scaleInX.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				menuFloat.getMenuIconView().setImageResource(menuFloat.isOpened()
					? R.drawable.ic_view : R.drawable.ic_close);
			}
		});
		set.play(scaleOutX).with(scaleOutY);
		set.play(scaleInX).with(scaleInY).after(scaleOutX);
		set.setInterpolator(new OvershootInterpolator(2));
		menuFloat.setIconToggleAnimatorSet(set);
	}

	private void initializeViews() {
		if (toolbar != null) {
			toolbar.setTitle(bank.getTitle());
			toolbar.setSubtitle(bank.getCity());
			toolbar.setNavigationIcon(R.drawable.ic_arrow);
		}

		recyclerView = (RecyclerView) view.findViewById(R.id.rv_currencies_FD);
		iActivity = (IMainActivity.IGetAction) getActivity();
		menuFloat = (FloatingActionMenu) view.findViewById(R.id.menu_float);
		fabLink = (FloatingActionButton) view.findViewById(R.id.fab_link_FD);
		fabMap = (FloatingActionButton) view.findViewById(R.id.fab_map_FD);
		fabPhone = (FloatingActionButton) view.findViewById(R.id.fab_phone_FD);

		TextView textView = (TextView) view.findViewById(R.id.tv_name_FD);
		textView.setText(bank.getTitle());
		textView = (TextView) view.findViewById(R.id.tv_address_FD);
		String text = bank.getCity() + ", " + bank.getAddress();
		textView.setText(text);
		textView = (TextView) view.findViewById(R.id.tv_phone_FD);
		textView.setText(bank.getPhone());

		textView = (TextView) view.findViewById(R.id.tv_update_FD);

		String date = bank.getDate().substring(0, 16).replace("T", " ");
		String dateDelta = bank.getDateDelta();
		dateDelta = (dateDelta.length() == 0) ? "" :
			bank.getDateDelta().substring(0, 16).replace("T", " ");
		String updateDate = date
			+ "\n (" + dateDelta + ")";
		textView.setText(updateDate);

		menuFloat.setOnMenuButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fabLink.setOnClickListener(clickListener);
				fabMap.setOnClickListener(clickListener);
				fabPhone.setOnClickListener(clickListener);
				menuFloat.toggle(true);
			}
		});
		createCustomAnimation();

		swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer_FD);
		if (swipeContainer != null) {
			swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					presenter.refreshData(bank.getId());

				}
			});
			// Configure the refreshing colors
			swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark,
				android.R.color.holo_orange_light);
		}

	}

	@Override
	public Loader<ArrayList<Currency>> onCreateLoader(int id, Bundle args) {
		return new GetDbCurrencies<ArrayList<Currency>>(context, args);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Currency>> loader, ArrayList<Currency> _data) {
		if (_data != null) {
			presenter.presenterSetRV(_data);
		} else {
			Toast.makeText(context, getResources().getString(R.string.msg_no_data_geted), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoaderReset(Loader loader) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_BANK_DATA, bank);
		outState.putParcelableArrayList(KEY_ARRAY_LIST, courseList);

	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			bank = savedInstanceState.getParcelable(KEY_BANK_DATA);
			courseList = savedInstanceState.getParcelableArrayList(KEY_ARRAY_LIST);
			initializeViews();
			presenter.presenterSetRV(courseList);
		}
		super.onViewStateRestored(savedInstanceState);
	}
}

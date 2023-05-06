package com.imooc.imooc_voice.view.cloud;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.MainEventBean;
import com.imooc.imooc_voice.model.newapi.personal.UserEventBean;
import com.imooc.imooc_voice.view.cloud.adapter.EventAdapter;
import com.imooc.lib_common_ui.app.Netease;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.ArrayList;
import java.util.List;



public class CloudVillageFragment extends NeteaseLoadingDelegate implements SwipeRefreshLayout.OnRefreshListener{

	SwipeRefreshLayout refreshLayout;
	RecyclerView mRecyclerView;

	private MainEventBean mRecommandData;
	private List<UserEventBean.EventsBean> mDatas = new ArrayList<>();

	private EventAdapter mAdapter;


	@Override
	public void initView() {
		refreshLayout = rootView.findViewById(R.id.refresh_layout_delegate_friend);
		mRecyclerView = rootView.findViewById(R.id.rv_delegate_friend);
		refreshLayout.setColorSchemeColors(getResources().getColor(R.color.red));
		requestData();
	}

	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_friend;
	}

	@Override
	public void onRefresh() {
		//requestData();
		Netease.getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setRefreshing(false);
			}
		}, 2000);
	}


	private void requestData(){

		RequestCenter.getMainEvent(new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {
				mRecommandData = (MainEventBean) responseObj;
				mDatas = mRecommandData.getEvent();
				mAdapter = new EventAdapter(mDatas);
				mRecyclerView.setAdapter(mAdapter);
				mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
				addRootView();
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}

}

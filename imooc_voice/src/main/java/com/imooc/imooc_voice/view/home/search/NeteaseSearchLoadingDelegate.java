package com.imooc.imooc_voice.view.home.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.model.event.KeywordsEvent;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class NeteaseSearchLoadingDelegate extends NeteaseLoadingDelegate {

	public RecyclerView mRecyclerView;
	private String keywords ="";
	public BaseQuickAdapter mAdapter;



	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) throws Exception {
		frameLayout = view.findViewById(com.imooc.lib_common_ui.R.id.loadframe);
		rootView = LayoutInflater.from(getContext()).inflate(setLoadingViewLayout(), frameLayout, false);
		addLoadingView();
		EventBus.getDefault().postSticky(new KeywordsEvent(keywords));
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		keywords = "";
	}

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void onGetKeywordsEvent(KeywordsEvent event){
		if(!keywords.equals(event.getKeyword())){
			keywords = event.getKeyword();
			reloadSearchResult(keywords);
			if(event.isNeedLoading()){
				addLoadingView();
			}

		}
	}

	@Override
	public void initView() {
		//mRecyclerView = rootView.findViewById(R.id.rv_delegate_normal);
		EventBus.getDefault().postSticky(new KeywordsEvent(keywords));
	}

	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_recyclerview_normal;
	}

	public abstract void reloadSearchResult(String keyword);






}

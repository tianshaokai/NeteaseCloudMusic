package com.imooc.imooc_voice.view.home.search.sort;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.search.RadioSearchBean;
import com.imooc.imooc_voice.util.SearchUtil;
import com.imooc.imooc_voice.view.discory.radio.detail.RadioDetailDelegate;
import com.imooc.imooc_voice.view.home.search.NeteaseSearchLoadingDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.List;

public class RadioSearchDelegate extends NeteaseSearchLoadingDelegate {


	@Override
	public void reloadSearchResult(final String keyword) {

		RequestCenter.getRadioSearch(keyword, 1009, new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {
				RadioSearchBean bean = (RadioSearchBean) responseObj;
				List<RadioSearchBean.ResultBean.DjRadiosBean> radios = bean.getResult().getDjRadios();
				mAdapter = new RadioSearchAdapter(keyword, radios);
				mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
						RadioSearchBean.ResultBean.DjRadiosBean entity = (RadioSearchBean.ResultBean.DjRadiosBean) baseQuickAdapter.getItem(i);
						getParentDelegate().getSupportDelegate().start(RadioDetailDelegate.newInstance(entity.getId()));
					}
				});
				mRecyclerView = rootView.findViewById(R.id.rv_delegate_normal);
				mRecyclerView.setAdapter(mAdapter);
				mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
				addRootView();
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}

	static class RadioSearchAdapter extends BaseQuickAdapter<RadioSearchBean.ResultBean.DjRadiosBean, BaseViewHolder> {

		private String keyword;

		public RadioSearchAdapter(String keywords, @Nullable List<RadioSearchBean.ResultBean.DjRadiosBean> data) {
			super(R.layout.item_mine_gedan_content, data);
			this.keyword = keywords;
		}

		@Override
		protected void convert(@NonNull BaseViewHolder adapter, RadioSearchBean.ResultBean.DjRadiosBean item) {
			adapter.setText(R.id.tv_item_gedan_content_toptext, SearchUtil.getMatchingKeywords(item.getName(), keyword));
			adapter.setText(R.id.tv_item_gedan_content_bottomtext, SearchUtil.getMatchingKeywords(item.getDj().getNickname(), keyword));
			ImageLoaderManager.getInstance().displayImageForCorner((ImageView) adapter.getView(R.id.iv_item_gedan_content_img), item.getPicUrl());
			//不显示更多 选项
			adapter.setVisible(R.id.iv_item_gedan_more, false);
		}
	}
}

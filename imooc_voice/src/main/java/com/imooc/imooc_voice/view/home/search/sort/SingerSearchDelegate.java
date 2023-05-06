package com.imooc.imooc_voice.view.home.search.sort;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.search.SingerSearchBean;
import com.imooc.imooc_voice.util.SearchUtil;
import com.imooc.imooc_voice.view.home.search.NeteaseSearchLoadingDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.List;

public class SingerSearchDelegate extends NeteaseSearchLoadingDelegate {


	@Override
	public void reloadSearchResult(final String keywords) {
			RequestCenter.getSingerSearch(keywords, 100, new DisposeDataListener() {
				@Override
				public void onSuccess(Object responseObj) {
					SingerSearchBean bean = (SingerSearchBean) responseObj;
					List<SingerSearchBean.ResultBean.ArtistsBean> artists = bean.getResult().getArtists();
					mAdapter = new SingerSearchAdapter(keywords, artists);
					mRecyclerView = rootView.findViewById(R.id.rv_delegate_normal);
					mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
					mRecyclerView.setAdapter(mAdapter);
					addRootView();

				}

				@Override
				public void onFailure(Object reasonObj) {

				}
			});

	}


	static class SingerSearchAdapter extends BaseQuickAdapter<SingerSearchBean.ResultBean.ArtistsBean, BaseViewHolder>{

		private String keywords;

		SingerSearchAdapter(String keyword,@Nullable List<SingerSearchBean.ResultBean.ArtistsBean> data) {
			super(R.layout.item_singer_normal, data);
			this.keywords = keyword;
		}

		@Override
		protected void convert(@NonNull BaseViewHolder adapter, SingerSearchBean.ResultBean.ArtistsBean item) {

			adapter.setText(R.id.tv_singer_name, SearchUtil.getMatchingKeywords(item.getName(),keywords));
			if(item.getAlias() != null && item.getAlias().size() != 0){
				adapter.setText(R.id.tv_singer_alias, "("+SearchUtil.getMatchingKeywords(item.getAlias().get(0),keywords)+")");
			}
			ImageLoaderManager.getInstance().displayImageForCircle((ImageView) adapter.getView(R.id.iv_singer_avatar), item.getPicUrl());

		}
	}
}

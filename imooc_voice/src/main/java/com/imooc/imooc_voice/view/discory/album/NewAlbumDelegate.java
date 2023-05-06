package com.imooc.imooc_voice.view.discory.album;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.R2;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.search.AlbumSearchBean;
import com.imooc.imooc_voice.view.discory.square.detail.SongListDetailDelegate;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.List;

import butterknife.OnClick;

import static com.imooc.imooc_voice.Constants.ALBUM;

public class NewAlbumDelegate extends NeteaseLoadingDelegate {


	private RecyclerView mRecyclerViewNewAlbum;
	private NewAlbumAdapter mAdapter;

	@Override
	public Object setLayout() {
		return R.layout.delegate_new_album;
	}

	@Override
	public void initView() {
		mRecyclerViewNewAlbum = rootView.findViewById(R.id.rv_delegate_normal);
		RequestCenter.getNewAlbum(new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {
				AlbumSearchBean.ResultBean bean = (AlbumSearchBean.ResultBean) responseObj;
				List<AlbumSearchBean.ResultBean.AlbumsBean> albums = bean.getAlbums();
				mAdapter = new NewAlbumAdapter(albums);
				mAdapter.setHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_discover_album_header, null, false));
				mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
						AlbumSearchBean.ResultBean.AlbumsBean entity = (AlbumSearchBean.ResultBean.AlbumsBean) baseQuickAdapter.getItem(i);
						getSupportDelegate().start(SongListDetailDelegate.newInstance(ALBUM, Long.parseLong(entity.getId())));
					}
				});
				mRecyclerViewNewAlbum.setLayoutManager(new GridLayoutManager(getContext(),2));
				mRecyclerViewNewAlbum.setAdapter(mAdapter);
				addRootView();
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});
	}

	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_recyclerview_normal;
	}


	@OnClick(R2.id.img_radio_back)
	void onClickBack(){
		getSupportDelegate().pop();
	}

	static class NewAlbumAdapter extends BaseQuickAdapter<AlbumSearchBean.ResultBean.AlbumsBean, BaseViewHolder>{
		private ImageLoaderManager manager;

		NewAlbumAdapter(@Nullable List<AlbumSearchBean.ResultBean.AlbumsBean> data) {
			super(R.layout.item_discover_album, data);
			manager = ImageLoaderManager.getInstance();
		}

		@Override
		protected void convert(@NonNull BaseViewHolder adapter, AlbumSearchBean.ResultBean.AlbumsBean item) {
			adapter.setVisible(R.id.iv_item_album_icon, true);
			manager.displayImageForCorner((ImageView) adapter.getView(R.id.iv_item_album_song), item.getPicUrl());
			adapter.setText(R.id.tv_item_album_song_name, item.getName());
			adapter.setText(R.id.tv_item_album_song_artist, item.getArtist().getName());
		}
	}
}

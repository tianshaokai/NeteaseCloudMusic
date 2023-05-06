package com.imooc.imooc_voice.view.mine.local.music;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.model.info.MusicInfo;
import com.imooc.imooc_voice.util.IConstants;
import com.imooc.imooc_voice.util.MusicUtils;
import com.imooc.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;

import java.util.ArrayList;
import java.util.List;

public class MusicDelegate extends NeteaseLoadingDelegate {

	private static final String TAG = "MusicDelegate";
	/**
	 * ui
	 */

	private View headerView;
	private RecyclerView recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private MusicAdapter mAdapter;

	/**
	 * data
	 */
	private int currentMusicPosition = -1;


	@SuppressLint("SetTextI18n")
	public void initView() {

		recyclerView = rootView.findViewById(R.id.recyclerview);
		linearLayoutManager = new LinearLayoutManager(getContext());

		ArrayList<MusicInfo> songList = (ArrayList) MusicUtils.queryMusic(getProxyActivity(), IConstants.START_FROM_LOCAL);
		mAdapter = new MusicAdapter(songList);
		headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_music_header, null, false);
		((TextView)headerView.findViewById(R.id.play_all_number)).setText("(共" + songList.size() +"首)");
		//添加头布局
		mAdapter.setHeaderView(headerView);
		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

				final MusicInfo item = (MusicInfo) adapter.getItem(position);
				assert item != null;
				AudioController.getInstance().addAudio(new AudioBean(String.valueOf(item.songId), item.data, item.musicName, item.artist, item.albumName, item.albumData, item.albumData, String.valueOf(item.duration)));
				//设置当前播放状态 取消上个播放状态
				final ImageView playView = view.findViewById(R.id.play_state);
				playView.setVisibility(View.VISIBLE);
				if (currentMusicPosition != -1) {
					//mAdapter.notifyDataSetChanged();
					mAdapter.notifyItemChanged(currentMusicPosition + 1);
				}
				currentMusicPosition = position;
			}
		});
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(linearLayoutManager);
		addRootView();
	}

	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_music_recyclerview;
	}



	class MusicAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> {

		MusicAdapter(@Nullable List<MusicInfo> data) {
			super(R.layout.item_music_common, data);
		}

		@Override
		protected void convert(BaseViewHolder helper, final MusicInfo item) {
			//歌曲名
			helper.setText(R.id.viewpager_list_toptext, item.musicName);
			helper.setText(R.id.viewpager_list_bottom_text, item.artist);
			final ImageView listView = helper.getView(R.id.viewpager_list_button);
			listView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//弹出菜单
					new MusicMoreDialog(getContext(), item).show();
				}
			});
			final ImageView playView = helper.getView(R.id.play_state);

			//当前音乐是正在播放的音乐
			if (AudioController.getInstance().getNowPlaying().getName().equals(item.musicName)) {
				playView.setVisibility(View.VISIBLE);
			} else {
				playView.setVisibility(View.GONE);
			}

		}

	}
}

package com.imooc.imooc_voice.view.discory.radio.rank;

import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.dj.DjTopListBean;
import com.imooc.imooc_voice.model.newapi.dj.DjToplistEntity;
import com.imooc.imooc_voice.util.SearchUtil;
import com.imooc.imooc_voice.view.user.UserDetailDelegate;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.ArrayList;
import java.util.List;

//主播榜
public class DjRankDelegate extends NeteaseLoadingDelegate implements BaseQuickAdapter.OnItemClickListener{

    private RecyclerView mRvDjHours;
    private RecyclerView mRvDjHot;

    private DjHoursAdapter mHoursAdapter;
    private DjPopularAdapter mPopularAdapter;
	private ArrayList<DjToplistEntity> entities = new ArrayList<>();

	@Override
	public void initView() {
		mRvDjHours = rootView.findViewById(R.id.rv_radio_dj_rank_123);
		mRvDjHot = rootView.findViewById(R.id.rv_radio_dj_rank_popular);
		//24小时榜 3个
		RequestCenter.getRadioTopHours(3, new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {
				DjTopListBean bean = (DjTopListBean) responseObj;
				List<DjTopListBean.List> list = bean.getData().getList();
				entities.add(new DjToplistEntity(true, "24小时榜"));
				for (int i = 0; i < list.size(); i++) {
					entities.add(new DjToplistEntity(list.get(i)));
				}
				entities.add(new DjToplistEntity(true, "新人榜"));
				//新人榜
				RequestCenter.getRadioTopNewComer(3, new DisposeDataListener() {
					@Override
					public void onSuccess(Object responseObj) {
						DjTopListBean bean = (DjTopListBean) responseObj;
						List<DjTopListBean.List> list = bean.getData().getList();
						for (int j = 0; j < list.size(); j++) {
							entities.add(new DjToplistEntity(list.get(j)));
						}
                        mHoursAdapter = new DjHoursAdapter(entities);
						//点击事件
						mHoursAdapter.setOnItemClickListener(DjRankDelegate.this);
						//横向
                        mRvDjHours.setLayoutManager(new GridLayoutManager(getContext(),3));
                        mRvDjHours.setAdapter(mHoursAdapter);
						//最热主播
						RequestCenter.getRadioTopPopular(20, new DisposeDataListener() {
							@Override
							public void onSuccess(Object responseObj) {
								DjTopListBean bean = (DjTopListBean) responseObj;
								List<DjTopListBean.List> popularData = bean.getData().getList();
								mPopularAdapter = new DjPopularAdapter(popularData);
								mPopularAdapter.setOnItemClickListener(DjRankDelegate.this);
								mPopularAdapter.setHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_rank_dj_popular_header, null, false));
								mRvDjHot.setAdapter(mPopularAdapter);
								LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()){
									@Override
									public boolean canScrollVertically() {
										return false;
									}
								};
								mRvDjHot.setLayoutManager(linearLayoutManager);
								addRootView();
							}

							@Override
							public void onFailure(Object reasonObj) {

							}
						});
					}

					@Override
					public void onFailure(Object reasonObj) {

					}
				});
			}

			@Override
			public void onFailure(Object reasonObj) {

			}
		});


	}


	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_radio_dj_rank;
	}

	@Override
	public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
		if(baseQuickAdapter.getItem(i) instanceof DjTopListBean.List){
			//最热主播数据
			DjTopListBean.List entity = (DjTopListBean.List) baseQuickAdapter.getItem(i);
			getParentDelegate().getSupportDelegate().start(UserDetailDelegate.newInstance(entity.getId()));;
		}else{
			//排行榜个人详情
			DjToplistEntity entity = (DjToplistEntity) baseQuickAdapter.getItem(i);
			if(entity.t != null){
				getParentDelegate().getSupportDelegate().start(UserDetailDelegate.newInstance(entity.t.getId()));;
			}else{
				//点击了头部 查看排行榜详情
			}
		}

	}

	static class DjHoursAdapter extends BaseSectionQuickAdapter<DjToplistEntity, BaseViewHolder>{
        private ImageLoaderManager manager;

        DjHoursAdapter(List<DjToplistEntity> data) {
            super(R.layout.item_dj_rank_123, R.layout.item_dj_rank_hours_header, data);
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convertHead(BaseViewHolder adapter, DjToplistEntity item) {
            adapter.setText(R.id.tv_item_dj_rank_hours_title, item.header);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder adapter, DjToplistEntity item) {
            DjTopListBean.List entity = item.t;
            //用户昵称
            adapter.setText(R.id.tv_item_rank_123_name, entity.getNickName());
            //分数
            adapter.setText(R.id.tv_item_rank_123_hot, SearchUtil.getCorresPondingString(entity.getScore()));
            if(entity.getUserType() == 4 ){
            	adapter.setVisible(R.id.iv_item_rank_user_tag, true);
            	adapter.setImageResource(R.id.iv_item_rank_user_tag, R.drawable.yinyueren);
			}else if(entity.getUserType() == 10 || entity.getUserType() == 1){
				adapter.setVisible(R.id.iv_item_rank_user_tag, true);
				adapter.setImageResource(R.id.iv_item_rank_user_tag, R.drawable.guanfang);
			}
            manager.displayImageForCircle((ImageView) adapter.getView(R.id.iv_item_rank_123_avatar), entity.getAvatarUrl());

        }
    }

    static class DjPopularAdapter extends BaseQuickAdapter<DjTopListBean.List, BaseViewHolder>{
		private ImageLoaderManager manager;
        DjPopularAdapter(@Nullable List<DjTopListBean.List> data) {
            super(R.layout.item_dj_rank_normal, data);
			manager = ImageLoaderManager.getInstance();
        }

        @Override
        protected void convert(@NonNull BaseViewHolder adapter, DjTopListBean.List list) {
            adapter.setText(R.id.tv_item_dj_rank_name, list.getNickName());
            //排名变化
			int diffRank = list.getLastRank() - list.getRank();
			if(diffRank != 0){
				if(diffRank > 0){
					adapter.setImageResource(R.id.iv_item_dj_diff, R.drawable.up);
				}else{
					adapter.setImageResource(R.id.iv_item_dj_diff, R.drawable.down);
				}
			}else{
				adapter.setImageResource(R.id.iv_item_dj_diff, R.drawable.zero);
			}
            String diff  = String.valueOf(Math.abs(diffRank));
            adapter.setText(R.id.tv_item_dj_rank_diff, diff);
            adapter.setText(R.id.tv_item_dj_rank_score, SearchUtil.getCorresPondingString(list.getScore()));
			//userType
			if(list.getUserType() == 4){
				adapter.setVisible(R.id.iv_item_dj_tag, true);
				adapter.setImageResource(R.id.iv_item_dj_tag, R.drawable.yinyueren);
			}else if(list.getUserType() == 10 || list.getUserType() == 1){
				adapter.setVisible(R.id.iv_item_dj_tag, true);
				adapter.setImageResource(R.id.iv_item_dj_tag, R.drawable.guanfang);
			}
			if(list.getRank() < 4){
				adapter.setTextColor(R.id.tv_item_dj_rank_rank, Color.RED);
			}
			if(list.getRank() > 9){
				((TextView)adapter.getView(R.id.tv_item_dj_rank_rank)).setTextSize(16);
				((TextView)adapter.getView(R.id.tv_item_dj_rank_rank)).setTypeface(Typeface.DEFAULT);
			}
            adapter.setText(R.id.tv_item_dj_rank_rank, String.valueOf(list.getRank()));
			manager.displayImageForCircle((ImageView) adapter.getView(R.id.iv_item_dj_avatar), list.getAvatarUrl());

        }
    }


}

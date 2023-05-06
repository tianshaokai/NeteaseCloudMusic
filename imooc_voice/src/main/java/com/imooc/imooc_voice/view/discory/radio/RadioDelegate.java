package com.imooc.imooc_voice.view.discory.radio;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.R2;
import com.imooc.imooc_voice.api.RequestCenter;
import com.imooc.imooc_voice.model.newapi.DjBannerBean;
import com.imooc.imooc_voice.model.newapi.dj.DjRecommendBean;
import com.imooc.imooc_voice.view.discory.radio.detail.RadioDetailDelegate;
import com.imooc.imooc_voice.view.discory.radio.pay.RadioPayDelegate;
import com.imooc.imooc_voice.view.discory.radio.rank.RankDelegate;
import com.imooc.lib_common_ui.bannder.BannerCreator;
import com.imooc.lib_common_ui.delegate.NeteaseLoadingDelegate;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.imooc.lib_network.listener.DisposeDataListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RadioDelegate extends NeteaseLoadingDelegate implements View.OnClickListener{


	@BindView(R2.id.banner_radio_recycler_item)
	ConvenientBanner banner;

	private RecyclerView mRvRadioRecommend;
	private DjRecommendAdapter mRecommedAdapter;

	private List<DjRecommendBean.DjRadiosBean> djRadios = new ArrayList<>();
	private List<DjRecommendBean.DjRadiosBean> djRadiosone = new ArrayList<>();
	private List<DjRecommendBean.DjRadiosBean> djRadiostwo = new ArrayList<>();
	private List<DjRecommendBean.DjRadiosBean> djRadiosthree = new ArrayList<>();

	private int recommendDataPosition = 0;

	@Override
	public Object setLayout() {
		return R.layout.delegate_radio;
	}


	@Override
	public void initView() {
		rootView.findViewById(R.id.tv_radio_recommend_header_change).setOnClickListener(this);
		mRvRadioRecommend = rootView.findViewById(R.id.rv_radio_loading);
		RequestCenter.getRadioBanner(new DisposeDataListener() {
			@Override
			public void onSuccess(Object responseObj) {

				DjBannerBean bannerBean = (DjBannerBean) responseObj;
				List<DjBannerBean.BannersBean> banners = bannerBean.getBanners();
				ArrayList<String> bannerList = new ArrayList<>();
				for(DjBannerBean.BannersBean item: banners){
					bannerList.add(item.getPic());
				}
				BannerCreator.setDefault(banner, bannerList, null);
			}

			@Override
			public void onFailure(Object reasonObj) {
			}
		});
		initRadioView();
	}

	@Override
	public int setLoadingViewLayout() {
		return R.layout.delegate_radio_loading;
	}


	@SuppressLint("StaticFieldLeak")
	private void initRadioView() {
		new AsyncTask<Void,Void,Void>(){

			@Override
			protected Void doInBackground(Void... voids) {

				RequestCenter.getRadioRecommend(new DisposeDataListener() {
					@Override
					public void onSuccess(Object responseObj) {
						DjRecommendBean bean = (DjRecommendBean) responseObj;
						djRadios = bean.getDjRadios();
						for(int i=0;i<3;i++){
							djRadiosone.add(djRadios.get(i));
						}
						for(int j=3;j<6;j++){
							djRadiostwo.add(djRadios.get(j));
						}
						for(int k=6;k<9;k++){
							djRadiosthree.add(djRadios.get(k));
						}
						mRecommedAdapter = new DjRecommendAdapter(djRadiosone);
						recommendDataPosition += 1;
						mRecommedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
							@Override
							public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
								DjRecommendBean.DjRadiosBean entity = (DjRecommendBean.DjRadiosBean)baseQuickAdapter.getItem(i);
								getSupportDelegate().start(RadioDetailDelegate.newInstance(entity.getId()));
							}
						});

						mRvRadioRecommend.setLayoutManager(new GridLayoutManager(getContext(), 3));
						mRvRadioRecommend.setAdapter(mRecommedAdapter);
						addRootView();
					}

					@Override
					public void onFailure(Object reasonObj) {

					}
				});
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
			}
		}.execute();
	}


	@OnClick(R2.id.ll_radio_pay)
	void onClickRadioPay(){
		//付费精品
		getSupportDelegate().start(new RadioPayDelegate());
	}

	@OnClick(R2.id.img_radio_back)
	void onClickBack(){
		getSupportDelegate().pop();
	}

	@OnClick(R2.id.ll_radio_sort)
	void onClickRadioSort(){
		//电台分类
		getSupportDelegate().start(new RadioSortDelegate());
	}

	@OnClick(R2.id.ll_radio_rank)
	void onClickRank(){
		getSupportDelegate().start(new RankDelegate());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_radio_recommend_header_change:
				changeRecommendRadio();
				break;
		}
	}

	private void changeRecommendRadio() {
		if(recommendDataPosition == 3){
			recommendDataPosition = 0;
		}
		switch (recommendDataPosition){
			case 0:
				mRecommedAdapter.replaceData(djRadiosone);
				break;
			case 1:
				mRecommedAdapter.replaceData(djRadiostwo);
				break;
			case 2:
				mRecommedAdapter.replaceData(djRadiosthree);
				break;

		}
		recommendDataPosition += 1;
	}

	static class DjRecommendAdapter extends BaseQuickAdapter<DjRecommendBean.DjRadiosBean, BaseViewHolder>{

		private ImageLoaderManager manager;
		DjRecommendAdapter(@Nullable List<DjRecommendBean.DjRadiosBean> data) {
			super(R.layout.item_radio_recommend, data);
			manager = ImageLoaderManager.getInstance();
		}

		@Override
		protected void convert(@NonNull BaseViewHolder adapter, DjRecommendBean.DjRadiosBean bean) {
			adapter.setText(R.id.iv_radio_recommend_name, bean.getName());
			adapter.setText(R.id.iv_radio_recommend_des, bean.getRcmdtext());
			manager.displayImageForCorner((ImageView) adapter.getView(R.id.iv_radio_recommend_img), bean.getPicUrl(), 5);
		}
	}
}

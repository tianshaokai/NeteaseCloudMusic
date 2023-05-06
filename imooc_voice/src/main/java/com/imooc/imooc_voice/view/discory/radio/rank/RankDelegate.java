package com.imooc.imooc_voice.view.discory.radio.rank;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.R2;
import com.imooc.lib_common_ui.delegate.MultiFragmentPagerAdapter;
import com.imooc.lib_common_ui.delegate.NeteaseDelegate;
import com.imooc.lib_common_ui.navigator.CommonNavigatorCreater;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

//主播电台排行榜
public class RankDelegate extends NeteaseDelegate {

	@BindView(R2.id.magic_indicator_tab)
	MagicIndicator mTabMagicIndicator;
	@BindView(R2.id.view_pager_tab)
	ViewPager mTabViewPager;

	private static final String[] mTitleDataList = {"主播榜", "节目榜", "电台榜"};

	private MultiFragmentPagerAdapter mPagerAdapter;
	private ArrayList<NeteaseDelegate> mDelegateList = new ArrayList<>();

	@Override
	public Object setLayout() {
		return R.layout.delegate_radio_rank;
	}

	@Override
	public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) throws Exception {
		mDelegateList.add(new DjRankDelegate());
		mDelegateList.add(new ProgramRankDelegate());
		mDelegateList.add(new RadioRankDelegate());

		mPagerAdapter = new MultiFragmentPagerAdapter(getChildFragmentManager());
		mPagerAdapter.init(mDelegateList);
		mTabViewPager.setAdapter(mPagerAdapter);
		mTabViewPager.setCurrentItem(1);
		mTabViewPager.setOffscreenPageLimit(3);
		initMagicIndicator();
	}

	private void initMagicIndicator() {
		mTabMagicIndicator.setBackgroundColor(Color.WHITE);
		CommonNavigator commonNavigator = CommonNavigatorCreater.setDefaultNavigator(getContext(), mTitleDataList, mTabViewPager);
		commonNavigator.setAdjustMode(true);
		mTabMagicIndicator.setNavigator(commonNavigator);
		mTabMagicIndicator.onPageSelected(1);
		ViewPagerHelper.bind(mTabMagicIndicator, mTabViewPager);

	}


	@OnClick(R2.id.img_tab_back)
	void onClickBack(){
		getSupportDelegate().pop();
	}
}

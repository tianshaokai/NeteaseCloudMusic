package com.imooc.imooc_voice.view.mine.local;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.imooc.imooc_voice.view.mine.local.artist.ArtistDelegate;
import com.imooc.imooc_voice.view.mine.local.music.MusicDelegate;

public class LocalMusicAdapter extends FragmentPagerAdapter {


	public LocalMusicAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i){
			case 0:
				return new MusicDelegate();
			case 1:
				return new ArtistDelegate();
			default:
				return new MusicDelegate();
		}
	}

	@Override
	public int getCount() {
		return 4;
	}
}

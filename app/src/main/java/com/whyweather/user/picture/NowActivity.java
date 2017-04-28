package com.whyweather.user.picture;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.whyweather.user.picture.weather.WeatherMain;
import com.whyweather.user.picture.forecast.Forecast;
import com.whyweather.user.picture.forecast.Title;

import java.util.ArrayList;

public class NowActivity extends FragmentActivity {

    private NowFragment mNowFragment;
    private ForecastFragment mForecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now);

        TabLayout mTab = (TabLayout) findViewById(R.id.map_tab);
        ViewPager mPager = (ViewPager) findViewById(R.id.map_pager);

        WeatherMain data = (WeatherMain) getIntent().getSerializableExtra("data");
        Forecast forecast = (Forecast) getIntent().getSerializableExtra("forecast");

        MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mPager);

        mNowFragment = NowFragment.newInstance(data);
        mForecastFragment = ForecastFragment.newInstance((ArrayList<Title>) forecast.getTitle());

    }

    public void onClick(View view) {
        finish();
    }

    private class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
    }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mNowFragment;
                case 1:
                    return mForecastFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "현재 날씨";
                case 1:
                    return "날씨 예보";
            }
            return null;
        }
    }
}

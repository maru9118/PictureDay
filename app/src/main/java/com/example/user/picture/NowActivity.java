package com.example.user.picture;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.user.picture.Weather.WeatherMain;

public class NowActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mPager;
    private MyAdater mAdater;
    private NowFragment mNowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now);

        mTab = (TabLayout) findViewById(R.id.map_tab);
        mPager = (ViewPager) findViewById(R.id.map_pager);

        WeatherMain data = (WeatherMain) getIntent().getSerializableExtra("data");

        mAdater = new MyAdater(getSupportFragmentManager());

        mPager.setAdapter(mAdater);
        mTab.setupWithViewPager(mPager);

        mNowFragment = new NowFragment().newInstance(data);
    }

    public void onClick(View view) {
        finish();
    }

    public class MyAdater extends FragmentPagerAdapter {

        public MyAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mNowFragment;
                case 1:
                    return new ForecastFragment();
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
                    String a = "현재 날씨";
                    return a;
                case 1:
                    String b = "날씨 예보";
                    return b;
            }
            return null;
        }
    }
}

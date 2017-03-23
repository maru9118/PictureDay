package com.example.user.picture;

import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.picture.Weather.WeatherMain;

import java.util.Locale;

/**
 * Created by user on 2017-03-23.
 */

public class NowFragment extends Fragment {

    private ImageView mMainImage;

    private TextView mRiseText;
    private TextView mSetText;

    private TextView mSpeedText;
    private ImageView mWayImage;

    private TextView mWeater;
    private TextView mTempText;

    private TextView mAtmoText;
    private TextView mHumText;
    private TextView mVisText;

    public static NowFragment newInstance(WeatherMain data) {

        NowFragment fragment = new NowFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public NowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now, container, false);

        Bundle bundle = getArguments();
        WeatherMain data = (WeatherMain) bundle.getSerializable("data");

        mMainImage = (ImageView) view.findViewById(R.id.main_image);

        mRiseText = (TextView) view.findViewById(R.id.rise_text);
        mSetText = (TextView) view.findViewById(R.id.set_text);

        mSpeedText = (TextView) view.findViewById(R.id.speed_text);
        mWayImage = (ImageView) view.findViewById(R.id.way_image);

        mWeater = (TextView) view.findViewById(R.id.weater_text);
        mTempText = (TextView) view.findViewById(R.id.temp_text);

        mAtmoText = (TextView) view.findViewById(R.id.atmo_text);
        mHumText = (TextView) view.findViewById(R.id.hum_text);
        mVisText = (TextView) view.findViewById(R.id.vis_text);

        // 일출
        SimpleDateFormat sunRise = new SimpleDateFormat("hh:mm", Locale.KOREA);
        sunRise.setTimeZone(TimeZone.getTimeZone("UTC"));

        // 일몰
        SimpleDateFormat sunSet = new SimpleDateFormat("kk:mm", Locale.KOREA);
        sunSet.setTimeZone(TimeZone.getTimeZone("UTC"));

        mRiseText.setText(sunRise.format(data.getSys().getSunrise() * 1000L));
        mSetText.setText(sunSet.format(data.getSys().getSunset() * 1000L));

        mSpeedText.setText(data.getWind().getSpeed());

        mWeater.setText(data.getClouds().getAll());

        Double tempChange = data.getMain().getTemp() - 273.15;

        DecimalFormat form = new DecimalFormat("#.##");
        double dNumber = tempChange;

//        String temp = Double.toString();

        mTempText.setText(form.format(dNumber));

        mAtmoText.setText(data.getMain().getPressure());
        mHumText.setText(data.getMain().getHumidity());
        mVisText.setText(data.getVisibility());

        return view;
    }
}

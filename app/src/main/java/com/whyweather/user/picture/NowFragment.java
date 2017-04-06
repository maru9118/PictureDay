package com.whyweather.user.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyweather.user.picture.weather.WeatherMain;

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

        switch (data.getWeather().get(0).getMain()) {
            case "Clear":
                mMainImage.setImageResource(R.drawable.sun);
                break;
            case "Clouds":
                mMainImage.setImageResource(R.drawable.clouds);
                break;
            case "Snow":
                mMainImage.setImageResource(R.drawable.snow);
                break;
            case "Rain":
                mMainImage.setImageResource(R.drawable.rain);
                break;
            case "Haze":
            case "Mist":
                mMainImage.setImageResource(R.drawable.mist);
                break;
        }


        mRiseText.setText(data.getSys().getSunrise());
        mSetText.setText(data.getSys().getSunset());

        mSpeedText.setText(data.getWind().getSpeed() + "m/s");


        mWayImage = (ImageView) view.findViewById(R.id.way_image);
        mWayImage.setImageBitmap(rotateImage(
                BitmapFactory.decodeResource(getResources(), R.drawable.wind), data.getWind().getDeg()));

        mWeater.setText(data.getWeather().get(0).getMain());

        switch (data.getWeather().get(0).getMain()) {
            case "Clear":
                mWeater.setText("맑음");
                break;
            case "Clouds":
                mWeater.setText("구름많음");
                break;
            case "Snow":
                mWeater.setText("눈");
                break;
            case "Rain":
                mWeater.setText("비");
                break;
            case "Haze":
            case "Mist":
                mWeater.setText("안개");
                break;
            default:
                mWeater.setText("버그");
                break;
        }

        Double tempChange = data.getMain().getTemp() - 273.15;

        DecimalFormat form = new DecimalFormat("#.##");
        double dNumber = tempChange;

        mTempText.setText(form.format(dNumber) + "˚C");

        mAtmoText.setText(data.getMain().getPressure());
        mHumText.setText(data.getMain().getHumidity() + "%");
        mVisText.setText(data.getVisibility() + "m");

        return view;
    }

    // 이미지 회전 함수
    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }
}

package com.whyweather.user.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyweather.user.picture.forecast.Title;

import java.util.List;

/**
 * Created by user on 2017-03-23.
 */

public class ExpAdapter extends BaseExpandableListAdapter {

    private List<Title> GroupData;

    public ExpAdapter(List<Title> groupData) {
        GroupData = groupData;
    }

    @Override
    public int getGroupCount() {
        return GroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return GroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_exp, parent, false);
            viewHolder.groupText = (TextView) convertView.findViewById(R.id.item_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.groupText.setText(GroupData.get(groupPosition).getDtTxt());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_child, parent, false);

            viewHolder.mainImage = (ImageView) convertView.findViewById(R.id.main_image);

            viewHolder.weatherText = (TextView) convertView.findViewById(R.id.weater_item);
            viewHolder.tempText = (TextView) convertView.findViewById(R.id.temp_item);

            viewHolder.speedText = (TextView) convertView.findViewById(R.id.speed_item);
            viewHolder.wayImage = (ImageView) convertView.findViewById(R.id.way_item);


            viewHolder.atmoText = (TextView) convertView.findViewById(R.id.atmo_item);
            viewHolder.humText = (TextView) convertView.findViewById(R.id.hum_item);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (GroupData.get(groupPosition).getWeather().get(childPosition).getMain()) {
            case "Clear":
                viewHolder.mainImage.setImageResource(R.drawable.sun);
                break;
            case "Clouds":
                viewHolder.mainImage.setImageResource(R.drawable.clouds);
                break;
            case "Snow":
                viewHolder.mainImage.setImageResource(R.drawable.snow);
                break;
            case "Rain":
                viewHolder.mainImage.setImageResource(R.drawable.rain);
                break;
            case "Haze":
            case "Mist":
                viewHolder.mainImage.setImageResource(R.drawable.mist);
                break;
        }

        switch (GroupData.get(groupPosition).getWeather().get(childPosition).getMain()) {
            case "Clear":
                viewHolder.weatherText.setText("맑음");
                break;
            case "Clouds":
                viewHolder.weatherText.setText("구름많음");
                break;
            case "Snow":
                viewHolder.weatherText.setText("눈");
                break;
            case "Rain":
                viewHolder.weatherText.setText("비");
                break;
            case "Haze":
            case "Mist":
                viewHolder.weatherText.setText("안개");
                break;
            default:
                viewHolder.weatherText.setText("버그");
                break;
        }

        Double tempChange = GroupData.get(groupPosition).getMain().getTemp() - 273.15;
        DecimalFormat form = new DecimalFormat("#.##");
        double dNumber = tempChange;

        viewHolder.tempText.setText(form.format(dNumber) + "˚C");

        viewHolder.speedText.setText(GroupData.get(groupPosition).getWind().getSpeed() + "m/s");

        viewHolder.wayImage.setImageBitmap(rotateImage(
                BitmapFactory.decodeResource(parent.getContext().getResources(), R.drawable.wind),
                GroupData.get(groupPosition).getWind().getDeg()));

        viewHolder.atmoText.setText(GroupData.get(groupPosition).getMain().getPressure());
        viewHolder.humText.setText(GroupData.get(groupPosition).getMain().getHumidity() + "%");

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private class ViewHolder {
        private TextView groupText;

        private ImageView mainImage;
        private TextView weatherText;
        private TextView tempText;
        private TextView speedText;
        private ImageView wayImage;
        private TextView atmoText;
        private TextView humText;
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

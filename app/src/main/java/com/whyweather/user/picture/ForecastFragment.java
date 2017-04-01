package com.whyweather.user.picture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.whyweather.user.picture.forecast.Title;

import java.util.ArrayList;

/**
 * Created by user on 2017-03-23.
 */

public class ForecastFragment extends Fragment {

    private ArrayList<Title> mData;
    private ExpandableListView mExpadableListView;

    public static ForecastFragment newInstance(ArrayList<Title> data) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ForecastFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        Bundle bundle = getArguments();
        mData = (ArrayList<Title>) bundle.getSerializable("data");
        mExpadableListView = (ExpandableListView) view.findViewById(R.id.expanded_menu);

        ExpAdapter adapter = new ExpAdapter(mData);
        mExpadableListView.setAdapter(adapter);

        return view;
    }
}

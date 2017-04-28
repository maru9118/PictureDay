package com.whyweather.user.picture;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2017-04-28.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<History> mData;

    public HistoryAdapter(List<History> mData) {
        this.mData = mData;
//        Log.d(TAG, "HistoryAdapter: " + mData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        History item = mData.get(position);

        if (item != null) {
            holder.domicileText.setText(item.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView domicileText;

        public ViewHolder(View itemView) {
            super(itemView);

            domicileText = (TextView) itemView.findViewById(R.id.item_domicile);

        }
    }
}

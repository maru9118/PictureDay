package com.whyweather.user.picture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private List<History> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
//        initData();
    }

//    @Override
//    protected void onStop() {
//        savePrefence();
//        super.onStop();
//    }

//    private void initData() {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String jsonData = pref.getString("historyData", "");
//
//        if (!jsonData.isEmpty()) {
//            Type type = new TypeToken<List<History>>() {
//            }.getType();
//
//            mData = new Gson().fromJson(jsonData, type);
//        } else {
//            mData = new ArrayList<>();
//        }
//
//    }

    private void initView() {

        mData = (List<History>) getIntent().getSerializableExtra("address");

        RecyclerView historyList = (RecyclerView) findViewById(R.id.history_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        HistoryAdapter adapter = new HistoryAdapter(mData);

        historyList.setLayoutManager(manager);
        historyList.setAdapter(adapter);

        findViewById(R.id.exit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private void savePrefence() {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putString("historyData", new Gson().toJson(mData));
//        editor.apply();
//    }
}

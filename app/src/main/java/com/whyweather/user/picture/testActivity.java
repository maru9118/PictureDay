package com.whyweather.user.picture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = (TextView) findViewById(R.id.text);
        String a = getIntent().getStringExtra("test");

        textView.setText(a);
    }
}

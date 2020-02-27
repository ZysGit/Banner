package com.zys.banner.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.zys.banner.Banner;
import com.zys.banner.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = findViewById(R.id.banner);
        List<String> strings = new ArrayList<>();
        strings.add("0");
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        strings.add("6");
        banner.setAdapter(new MyBannerAdapter(strings));
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void onBannerClicked(Object data, int position) {
                Log.d("zys", "onBannerClicked: " + position + "|" + data);
            }

            @Override
            public void onBannerChanged(int position) {
                Log.d("zys", "onBannerChanged: " + position);
            }
        });
        banner.start();
    }
}

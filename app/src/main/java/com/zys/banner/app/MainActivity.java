package com.zys.banner.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zys.banner.Banner;

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
        banner.start();
    }
}

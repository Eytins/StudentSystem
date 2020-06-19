package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class AdvertisementActivity extends AppCompatActivity {

    private TextView timeOver;
    Timer timer = new Timer();
    int   num   = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        RelativeLayout advertisementLayout = findViewById(R.id.advertisementLayout);
        advertisementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bing.com"));
                startActivity(intent);
                AdvertisementActivity.this.finish();
            }
        });

        //多线程
        timeOver = findViewById(R.id.timeover);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        num--;
                        timeOver.setText("- " + num + "秒 -\n跳过");
                        if (num < 1) {
                            timer.cancel();
                            Intent intent = new Intent();
                            intent.setClass(AdvertisementActivity.this, LoginActivity.class);
                            startActivity(intent);
                            AdvertisementActivity.this.finish();
                        }
                    }
                });
            }
        };

        timeOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                //startActivity(new Intent().setClass(AdvertisementActivity.this, LoginActivity.class));
                Intent intent = new Intent();
                intent.setClass(AdvertisementActivity.this, LoginActivity.class);
                startActivity(intent);
                AdvertisementActivity.this.finish();
            }
        });

        timer.schedule(task, 1000, 1000);
    }
}
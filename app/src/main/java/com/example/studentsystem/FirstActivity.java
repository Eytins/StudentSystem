package com.example.studentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class FirstActivity extends AppCompatActivity {

    boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //读取数据
        SharedPreferences sharedPreferences = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirst = sharedPreferences.getBoolean("isFirst", true);
        //如果是第一次进入，进入滑动翻页，否则进入倒计时页面
        if (isFirst) {
            startActivity(new Intent(FirstActivity.this, WelcomeActivity.class));
        } else {
            startActivity(new Intent(FirstActivity.this, AdvertisementActivity.class));
        }
        finish();
        //实例化编辑器
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //存入数据
        editor.putBoolean("isFirst", false);
        //提交修改
        editor.commit();
    }

}

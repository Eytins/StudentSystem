package com.example.studentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {

    private TextView    input;
    private RadioGroup  radioGroup1;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private ViewPager   viewPager;

    ArrayList<View> viewPagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        input = findViewById(R.id.textView12);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        viewPager = findViewById(R.id.viewPager);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });


        //设置某一个界面的填充布局
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View           view1          = layoutInflater.inflate(R.layout.s1, null);
        View           view2          = layoutInflater.inflate(R.layout.s2, null);
        View           view3          = layoutInflater.inflate(R.layout.s3, null);
        viewPagerList = new ArrayList<View>();
        viewPagerList.add(view1);
        viewPagerList.add(view2);
        viewPagerList.add(view3);

        //适配器Adapter
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewPagerList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            //获取当前界面
            @NonNull
            public Object instantiateItem(ViewGroup viewGroup, int position) {
                viewGroup.addView(viewPagerList.get(position));
                Uri uri = null;
                switch (position) {
                    case 0:
                        uri = Uri.parse("http://www.taobao.com");
                        break;
                    case 1:
                        uri = Uri.parse("http://www.suning.com");
                        break;
                    case 2:
                        uri = Uri.parse("http://www.jd.com");
                        break;
                }
                final Uri finalUri = uri;
                viewPagerList.get(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, finalUri);
                        startActivity(intent);
                    }
                });
                return viewPagerList.get(position);
            }

            //销毁上一个显示界面
            public void destroyItem(ViewGroup viewGroup, int position, Object object) {
                viewGroup.removeView(viewPagerList.get(position));
            }

        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioButton1.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case 1:
                        radioButton2.setChecked(true);
                        input.setVisibility(View.GONE);
                        break;
                    case 2:
                        radioButton3.setChecked(true);
                        input.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButton1.getId() == checkedId) {
                    viewPager.setCurrentItem(0);
                } else if (radioButton2.getId() == checkedId) {
                    viewPager.setCurrentItem(1);
                } else {
                    viewPager.setCurrentItem(2);
                }
            }
        });
    }
}

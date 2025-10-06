package com.example.tripapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tripapp.Adapter.MyFragmentPagerAdapter;
import com.example.tripapp.Fragment.HomeFragment;
import com.example.tripapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    List<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (v, insets) -> {
            // 去掉系统自动加的底部 padding
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), 0);
            return insets;
        });

        initView();
        initData();
        initEvent();

        // 创建适配器
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this, fragmentList);

        // 设置适配器
        viewPager.setAdapter(adapter);

        // 可选：禁止用户滑动（如果只想通过底部导航切换）
        // viewPager.setUserInputEnabled(false);


    }

    private void initView() {
        // 找到 ViewPager2
        viewPager = findViewById(R.id.view_pager);
    }
    private void initData() {
        // 创建 Fragment 列表
        fragmentList = new ArrayList<>();
        // 1.添加主页fragment
        fragmentList.add(new HomeFragment());
    }
    private void initEvent() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
            } else if (item.getItemId() == R.id.nav_favorite) {
                viewPager.setCurrentItem(1, true);
            } else if (item.getItemId() == R.id.nav_calendar) {
                viewPager.setCurrentItem(2, true);
            }else if (item.getItemId() == R.id.nav_profile) {
                viewPager.setCurrentItem(3, true);
            }

            return true;
        });

        // 监听 ViewPager2 滑动更新 BottomNavigationView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // 设置 BottomNavigationView 的选中状态
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_favorite);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_calendar);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
    }
}
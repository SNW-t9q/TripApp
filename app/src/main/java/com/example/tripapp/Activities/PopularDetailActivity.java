package com.example.tripapp.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.tripapp.Adapter.MyFragmentPagerAdapter;
import com.example.tripapp.Bean.PopularItem;
import com.example.tripapp.Fragment.ItemFragment.DetailFragment;
import com.example.tripapp.Fragment.ItemFragment.IntroductionFragment;
import com.example.tripapp.Fragment.ItemFragment.TourGuideFragment;
import com.example.tripapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class PopularDetailActivity extends AppCompatActivity {

    ImageView imageView,back,like;
    // TextView 控件
    TextView tv_title,tv_location,ratingText,money;
    RatingBar ratingBar;
    TabLayout tablayout;
    ViewPager2 viewpager;
    AppCompatButton bt_purchase;
    String description,duration, distance,guideName;
    int bed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_detail);
        initView();
        recieveData();
        initEvent();
    }


    private void initView() {
        imageView = findViewById(R.id.imageView);
        back = findViewById(R.id.back);
        like = findViewById(R.id.like);
        tv_title = findViewById(R.id.tv_title);
        tv_location = findViewById(R.id.tv_location);
        ratingText = findViewById(R.id.ratingText);
        money = findViewById(R.id.money);
        ratingBar = findViewById(R.id.ratingBar);
        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);
        bt_purchase = findViewById(R.id.bt_purchase);
    }

    private void recieveData() {
        // 接收传来的对象
        PopularItem item = (PopularItem) getIntent().getSerializableExtra("popular_item");

        if (item != null) {
            description = item.getDescription();
            duration = item.getDuration();
            distance = item.getDistance();
            bed = item.getBed();
            guideName = item.getTourGuideName().equals("") ? "暂无" : item.getTourGuideName();

            tv_title.setText(item.getTitle());
            tv_location.setText(item.getAddress());
            ratingText.setText(item.getScore() + "分");
            money.setText("￥" + item.getPrice());
            ratingBar.setRating((float) item.getScore());

            if (item.getPic() != null && !item.getPic().isEmpty()) {
                Glide.with(this).load(item.getPic().get(0)).into(imageView);
            }
        }
    }

    private void initEvent() {
        back.setOnClickListener(v -> {
            finish();
        });
        like.setOnClickListener(v -> {
            like.setSelected(!like.isSelected());
        });

        setupViewPagerAndTabs();
    }

    private void setupViewPagerAndTabs() {
        // 创建 Fragment 列表
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(IntroductionFragment.newInstance(description,""));
        fragments.add(DetailFragment.newInstance(duration,distance,Integer.toString( bed)));
        fragments.add(TourGuideFragment.newInstance(guideName,""));

        // 创建标题列表
        List<String> titles = new ArrayList<>();
        titles.add("简介");
        titles.add("详细信息");
        titles.add("导游");

        // 设置 Adapter
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this, fragments);
        viewpager.setAdapter(adapter);

        // 禁止左右滑动（如果你想允许滑动就删掉这一行）
        // viewpager.setUserInputEnabled(true);

        // TabLayout 与 ViewPager2 联动
        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            tab.setText(titles.get(position));
        }).attach();
    }
}
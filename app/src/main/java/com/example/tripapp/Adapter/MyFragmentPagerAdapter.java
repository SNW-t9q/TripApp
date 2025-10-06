// MyFragmentPagerAdapter.java
package com.example.tripapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList;

    // 构造函数，传入Fragment列表
    public MyFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 根据位置返回对应的Fragment
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        // 返回Fragment数量
        return fragmentList.size();
    }
}


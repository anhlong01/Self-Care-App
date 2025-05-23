package com.lph.selfcareapp.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DatLichPageAdapter extends FragmentStateAdapter {

    private final int NUM_PAGE = 2;
    private final String[] tabTitles = new String[]{"Đặt lịch theo bác sĩ", "Đặt lịch theo chuyên khoa"};

    public DatLichPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChonBacSiFragment();
            case 1:
                return new ChonChuyenKhoaFragment();
            default:
                return null;
        }
    }



    @Override
    public int getItemCount() {
        return NUM_PAGE;
    }

    public String getTabTitle(int position) {
        return tabTitles[position];
    }
}

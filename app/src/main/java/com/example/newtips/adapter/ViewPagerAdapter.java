package com.example.newtips.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.newtips.page.page1;
import com.example.newtips.page.page2;
import com.example.newtips.page.page3;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return  new page1();
            case 1:
                return  new page2();
            default:
                return  new page3();
        }
    }
    @Override
    public int getItemCount() {return 3; }
}
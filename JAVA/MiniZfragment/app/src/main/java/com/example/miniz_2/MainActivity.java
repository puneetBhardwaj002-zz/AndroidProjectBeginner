package com.example.miniz_2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    Toolbar toolbar;
    TabLayout tabLayout;
    TabLayout.OnTabSelectedListener listener=null;
    MyFragmentAdapter adapter;
    private static final int FRAGMENT_NUMBER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.veg_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.non_veg_title));
        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.addOnTabSelectedListener(listener);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabLayout.removeOnTabSelectedListener(listener);
    }
    public class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d("Fragment/getItem","Item at " +  position + " position");
            return MyFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return FRAGMENT_NUMBER;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.veg_title);
            } else {
                return getString(R.string.non_veg_title);
            }
        }
    }
}

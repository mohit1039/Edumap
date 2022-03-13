package com.edumap.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.edumap.Adapter.FragmentAdapter;
import com.edumap.R;
import com.google.android.material.tabs.TabLayout;

public class ShowCollegeDetails extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    private String collegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_college_details);

        tabLayout = findViewById(R.id.collegeDetailsTablayout);
        viewPager2 = findViewById(R.id.collegeDetailsViewPager2);

        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("Courses"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));


        if (getIntent() != null) {
            collegeID = getIntent().getStringExtra("collegeID");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager,getLifecycle(),collegeID);
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

}
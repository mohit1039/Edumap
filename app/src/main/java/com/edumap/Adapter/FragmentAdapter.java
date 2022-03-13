package com.edumap.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edumap.Activity.AboutFragment;
import com.edumap.Activity.CoursesFragment;
import com.edumap.Activity.EventsFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    String collegeID;

    public FragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, String collegeID){
        super(fragmentManager,lifecycle);
        this.collegeID = collegeID;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1 ){
            Bundle bundle = new Bundle();
            bundle.putString("collegeID", collegeID);
            CoursesFragment fragobj = new CoursesFragment();
            fragobj.setArguments(bundle);
            return fragobj;
        }
        else if (position == 2 ){
            return new EventsFragment();
        }
        return new AboutFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

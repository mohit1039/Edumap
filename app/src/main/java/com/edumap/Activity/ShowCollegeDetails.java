package com.edumap.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.edumap.Adapter.FragmentAdapter;
import com.edumap.Interface.OnGetDataListener;
import com.edumap.Model.College;
import com.edumap.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ShowCollegeDetails extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;
    private String collegeID;
    private DatabaseReference courseRef;
    FirebaseDatabase mainfdb;
    College college;
    private TextView collageName,collegeAddress;
    private FloatingActionButton directionFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_college_details);

        tabLayout = findViewById(R.id.collegeDetailsTablayout);
        viewPager2 = findViewById(R.id.collegeDetailsViewPager2);
        directionFAB = findViewById(R.id.directionButton);
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("Courses"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Review"));

        mainfdb = FirebaseDatabase.getInstance();
        courseRef = mainfdb.getReference("College");
        collageName = findViewById(R.id.collegeName);
        collegeAddress = findViewById(R.id.collegeAddress);
        if (getIntent() != null) {
            collegeID = getIntent().getStringExtra("collegeID");
            if (collegeID != null){
                Common.readData(courseRef.child(collegeID), new OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        college = dataSnapshot.getValue(College.class);
                        if (college != null){
                            collageName.setText(StringUtils.capitalize(college.getFullname()));
                            collegeAddress.setText(college.getAddress());
                        }
                    }
                    @Override
                    public void onFailure() {

                    }
                });
            }

        }

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),getLifecycle(),collegeID);
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

        directionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", college.getLatitude(),college.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }

    public void startCall(View view) {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+college.getPhone()));//change the number
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + college.getPhone()));//change the number
                    startActivity(callIntent);

                } else {
                    Toast.makeText(this, "Phone Permission not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
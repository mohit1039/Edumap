package com.edumap.Activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edumap.Adapter.CourseAdapter;
import com.edumap.Adapter.SpinAdapter;
import com.edumap.Model.Course;
import com.edumap.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CoursesFragment extends Fragment {

    View v;
    private String collegeID;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private CourseAdapter adapter;
    ArrayList<Course> courseArrayList = new ArrayList<>();
    ArrayList<Course> courses = new ArrayList<>();
    FirebaseDatabase mainfdb;
    DatabaseReference courseRef;
    FloatingActionButton addcourse;
    private Spinner courseSpinner;
    SpinAdapter courseAdapter;
    private Course updatedCourse;
    ArrayList<String> collegeIDs = new ArrayList<>();

    public CoursesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;

        if (getArguments() != null) {
            collegeID = getArguments().getString("collegeID");
        }
        mainfdb = FirebaseDatabase.getInstance();
        courseRef = mainfdb.getReference("Course");
         courseAdapter = new SpinAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                courseArrayList);
        addcourse = v.findViewById(R.id.addCourseCollegeButton);
        recyclerView = v.findViewById(R.id.courseRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
        checkboxRecyclerView();
        initCourse();
        if (!Common.checkAdmin(v.getContext())) {
            addcourse.setVisibility(View.GONE);
        }
        addcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddialog();
            }
        });

    }


    private void checkboxRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CourseAdapter(getContext(),courses);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }
    private void initCourse() {
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Course i = dataSnapshot1.getValue(Course.class);
                        courseArrayList.add(i);
                    }
                    filterCourses();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void filterCourses() {
        for (Course course : courseArrayList){
            if (course.getCollegeID() != null){
                if (course.getCollegeID().contains(collegeID)){
                    courses.add(course);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 3) {
            int position = -1;
            position = adapter.getPosition();
            if (item.getTitle().equals(Common.Delete)) {
                Course course = courses.get(position);
                ArrayList<String> courseCollegeID = course.getCollegeID();
                courseCollegeID.remove(collegeID);
                course.setCollegeID(courseCollegeID);
                courseRef.child(courses.get(position).getId()).setValue(course);
                courses.clear();
                courseArrayList.clear();
            }
        }
        return super.onContextItemSelected(item);
    }

    private void loaddialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Add new Course");
        alertDialog.setMessage("Please fill complete information");
        LayoutInflater inflater = this.getLayoutInflater();
        View addcategory = inflater.inflate(R.layout.addcourse,null);

        courseSpinner = addcategory.findViewById(R.id.courseCollegeSpinner);
        courseSpinner.setAdapter(courseAdapter);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    updatedCourse = courseAdapter.getItem(i);
                }
                else {
                    updatedCourse = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        alertDialog.setView(addcategory);
        alertDialog.setIcon(R.drawable.add);
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (updatedCourse != null){
                    if (updatedCourse.getCollegeID() != null ){
                        collegeIDs = updatedCourse.getCollegeID();

                        if (!updatedCourse.getCollegeID().contains(collegeID)){

                        collegeIDs.add(collegeID);
                        updatedCourse.setCollegeID(collegeIDs);
                        courseRef.child(updatedCourse.getId()).setValue(updatedCourse);
                        updatedCourse = null;
                        courseArrayList.clear();
                        courses.clear();
                        collegeIDs.clear();
                        dialog.dismiss();
                        }
                        else {
                            Toast.makeText(getContext(), "Already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        collegeIDs.add(collegeID);
                        updatedCourse.setCollegeID(collegeIDs);
                        courseRef.child(updatedCourse.getId()).setValue(updatedCourse);
                        updatedCourse = null;
                        courseArrayList.clear();
                        courses.clear();
                        collegeIDs.clear();
                        dialog.dismiss();
                    }

                }
                else {
                    Toast.makeText(getContext(), "Course not selected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
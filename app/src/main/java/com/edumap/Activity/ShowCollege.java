package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edumap.AddCollegeActivity;
import com.edumap.Model.College;
import com.edumap.Model.Stream;
import com.edumap.R;
import com.edumap.ViewHolder.CollegeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowCollege extends AppCompatActivity {

    private FloatingActionButton add;
    FirebaseAuth auth;
    private EditText addStream;
    private Button submit;
    private Stream stream;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<College> fac;
    private FirebaseRecyclerAdapter<College, CollegeViewHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_college);
        auth = FirebaseAuth.getInstance();
        add = findViewById(R.id.floatingActionButton);
        if (!Common.checkAdmin(this)) {
            add.setVisibility(View.GONE);
        }
        fdb = FirebaseDatabase.getInstance();
        databaseReference = fdb.getReference("College");
        recyclerView = findViewById(R.id.showCollegeRecycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loaddetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuStreamItem = menu.findItem(R.id.Stream);
        MenuItem menuCourseItem = menu.findItem(R.id.course);
        if (Common.checkAdmin(this)){
            menuCourseItem.setVisible(true);
            menuStreamItem.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.MenuLogout:
                auth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                SharedPreferences preferences = getSharedPreferences("Admin", 0);
                preferences.edit().remove("Admin").apply();
                finish();
                return true;
            case R.id.Stream:
                loaddialog();
                return true;
            case R.id.course:
                startCourseActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void add(View view) {
        startActivity(new Intent(this, AddCollegeActivity.class));
    }

    private void startCourseActivity(){
        startActivity(new Intent(this, AddCourseActivity.class));
        finish();
    }

    private void loaddialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowCollege.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please fill complete information");

        LayoutInflater inflater = this.getLayoutInflater();
        View addcategory = inflater.inflate(R.layout.addstream,null);

        addStream = addcategory.findViewById(R.id.addStreamName);
        alertDialog.setView(addcategory);
        alertDialog.setIcon(R.drawable.ic_add_img);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stream");
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = addStream.getText().toString().trim();
                if (!name.isEmpty()){
                    String key = reference.push().getKey();
                    stream = new Stream(key,name);
                    assert key != null;
                    reference.child(key).setValue(stream);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(ShowCollege.this, "Name is empty", Toast.LENGTH_SHORT).show();
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

    private void loaddetails() {
        fac = new FirebaseRecyclerOptions.Builder<College>().setQuery(
                databaseReference
                ,College.class).build();

        adapter = new FirebaseRecyclerAdapter<College, CollegeViewHolder>(fac) {
            @Override
            protected void onBindViewHolder(@NonNull CollegeViewHolder collegeViewHolder, int i, @NonNull College college) {
                collegeViewHolder.collegeName.setText(college.getFullname());
                collegeViewHolder.setItemClickListener((view, position, isLongClick) -> {
                    Intent college1 =new Intent(ShowCollege.this,ShowCollegeDetails.class);
                    college1.putExtra("collegeID",adapter.getRef(position).getKey());
                    startActivity(college1);
                });
            }

            @NonNull
            @Override
            public CollegeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collegecardview,parent,false);
                return new CollegeViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.Update)){
            Intent college1 =new Intent(ShowCollege.this,AddCollegeActivity.class);
            college1.putExtra("collegeID",adapter.getRef(item.getOrder()).getKey());
            startActivity(college1);
        }
        return super.onContextItemSelected(item);
    }
}
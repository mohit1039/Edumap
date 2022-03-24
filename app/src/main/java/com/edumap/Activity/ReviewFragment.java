package com.edumap.Activity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edumap.Adapter.CourseAdapter;
import com.edumap.Adapter.ReviewAdapter;
import com.edumap.Interface.OnGetDataListener;
import com.edumap.Model.Academic;
import com.edumap.Model.Course;
import com.edumap.Model.Event;
import com.edumap.Model.Review;
import com.edumap.Model.Stream;
import com.edumap.Model.Users;
import com.edumap.R;
import com.edumap.ViewHolder.EventViewHolder;
import com.edumap.ViewHolder.ReviewViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ReviewFragment extends Fragment {
    View v;

    private String collegeID;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase mainfdb;
    DatabaseReference reviewRef;
    FirebaseAuth mauth;
    FloatingActionButton addReviewFAB;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Review> reviewArrayList = new ArrayList<>();

    private TextView userReview, date,collegeRatingText,userratingsTextView;
    private RatingBar userRating,collegeRating;
    private CardView userReviewCardView;
    Review review;
    private String userID;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;
        if (getArguments() != null) {
            collegeID = getArguments().getString("collegeID");
        }
        mauth = FirebaseAuth.getInstance();
        userID = mauth.getUid();
        mainfdb = FirebaseDatabase.getInstance();
        reviewRef = mainfdb.getReference("Review");

        userratingsTextView = v.findViewById(R.id.userratingsTextView);
        userReviewCardView = v.findViewById(R.id.userReviewCardview);
        collegeRatingText = v.findViewById(R.id.collegeRatingText);
        collegeRating = v.findViewById(R.id.collegeRatingBar);
        userReview = v.findViewById(R.id.userReview);
        userRating = v.findViewById(R.id.userRatingBar);
        userReviewCardView = v.findViewById(R.id.userReviewCardview);
        date = v.findViewById(R.id.userDate);
        addReviewFAB = v.findViewById(R.id.addReviewButton);
        recyclerView = v.findViewById(R.id.reviewRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        reviewAdapter = new ReviewAdapter(getContext(),reviews);
        recyclerView.setHasFixedSize(true);
        if (Common.checkAdmin(v.getContext())) {
            addReviewFAB.setVisibility(View.GONE);
        }
        userReviewCardView.setVisibility(View.GONE);
        addReviewFAB.setEnabled(false);
        initUserReview();
        loaddetails();
        addReviewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddialog();
            }
        });
        userReviewCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userReview.getMaxLines() == 2){
                    userReview.setMaxLines(Integer.MAX_VALUE);
                    userReview.setEllipsize(null);
                }else {
                    userReview.setMaxLines(2);
                    userReview.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        });

    }

    private void initUserReview() {
        Common.readQueryData(reviewRef.orderByChild("userID").equalTo(mauth.getUid()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Review i = dataSnapshot1.getValue(Review.class);
                    if (i != null){
                        if (i.getCollegeID().equals(collegeID)){
                            review = i;
                        }
                    }
                }
                if (review != null){
                    userReview.setText(review.getReview());
                    userRating.setRating(review.getRating());
                    date.setText(review.getDate());
                    userReviewCardView.setVisibility(View.VISIBLE);
                    addReviewFAB.setImageResource(R.drawable.edit);
                }
                addReviewFAB.setEnabled(true);
            }

            @Override
            public void onFailure() {
                addReviewFAB.setEnabled(true);
            }
        });
    }

    private void loaddetails() {
        Common.readQueryData(reviewRef.orderByChild("collegeID").equalTo(collegeID), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                reviews.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Review i = dataSnapshot1.getValue(Review.class);
                    reviewArrayList.add(i);
                    if (i != null && !i.getUserID().equals(userID)) {
                        reviews.add(i);
                    }
                }
                performCalculation();
                recyclerView.setAdapter(reviewAdapter);
            }
            @Override
            public void onFailure() {
                collegeRating.setRating(0);
                collegeRatingText.setText("No ratings yet");
                userratingsTextView.setText("Be the first one to review college");
                userReviewCardView.setVisibility(View.GONE);
            }
        });
    }

    private void performCalculation() {
        float rating = 0;
        if (reviewArrayList.size() > 0){
            userReviewCardView.setVisibility(View.VISIBLE);
            userratingsTextView.setText("User ratings");
            for (Review review : reviewArrayList){
                rating = rating + review.getRating();
            }
            rating = (float) rating/reviewArrayList.size();
            collegeRatingText.setText(String.format("College Rating : %.2f",rating));
            collegeRating.setRating(rating);
            rating = 0;
            reviewArrayList.clear();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    private void loaddialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Rating and Review");
        alertDialog.setMessage("Please fill information");
        LayoutInflater inflater = this.getLayoutInflater();
        View addreview = inflater.inflate(R.layout.addreview,null);
        RatingBar ratingBar = addreview.findViewById(R.id.addRatingBar);
        EditText addReviewEditText = addreview.findViewById(R.id.addReviewEditText);
        alertDialog.setIcon(R.drawable.add);
        if (review != null) {
            ratingBar.setRating(review.getRating());
            addReviewEditText.setText(review.getReview());
            alertDialog.setIcon(R.drawable.edit);
        }
        alertDialog.setView(addreview);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userReview = addReviewEditText.getText().toString().trim();
                savedata(userReview,ratingBar.getRating());
                dialog.dismiss();
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

    private void savedata(String userReview, float rating) {
        DatabaseReference userRef = mainfdb.getReference("Users");
        Common.readData(userRef.child(Objects.requireNonNull(mauth.getUid())), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                if (users != null){
                    Date date = new Date();
                    String key = reviewRef.push().getKey();
                    if (review != null){
                        if (review.getReviewID() != null){
                            key = review.getReviewID();
                        }
                    }
                    Review newReview = new Review(users.getName() + " " + users.getSurname(),
                            users.getUserID(),userReview,formatter.format(date),collegeID,key,rating);
                    if (key != null) {
                        reviewRef.child(key).setValue(newReview);
                    }
                    reviews.clear();
                    review = null;
                    reviewArrayList.clear();
                    initUserReview();

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
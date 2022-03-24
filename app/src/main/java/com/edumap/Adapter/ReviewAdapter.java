package com.edumap.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Model.Review;
import com.edumap.Model.Stream;
import com.edumap.R;

import java.util.ArrayList;

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    View view;
    private Context context;
    private ArrayList<Review> reviews;


    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.reviewcardview,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        if (reviews != null){
            holder.ratingBar.setRating(reviews.get(position).getRating());
            holder.date.setText(reviews.get(position).getDate());
            holder.reviewersName.setText(reviews.get(position).getFullname());
            holder.review.setText(reviews.get(position).getReview());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.review.getMaxLines() == 2){
                        holder.review.setMaxLines(Integer.MAX_VALUE);
                        holder.review.setEllipsize(null);
                    }else {
                        holder.review.setMaxLines(2);
                        holder.review.setEllipsize(TextUtils.TruncateAt.END);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reviewersName, review, date;
        public RatingBar ratingBar;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewersName = itemView.findViewById(R.id.reviewersName);
            review = itemView.findViewById(R.id.review);
            date = itemView.findViewById(R.id.date);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            cardView = itemView.findViewById(R.id.reviewersCardView);

        }
    }
}

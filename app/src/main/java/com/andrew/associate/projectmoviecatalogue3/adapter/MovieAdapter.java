package com.andrew.associate.projectmoviecatalogue3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.activity.ActivityDetailMovie;
import com.andrew.associate.projectmoviecatalogue3.entity.Movie;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieItems;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.utils.DateFormat;
import com.andrew.associate.projectmoviecatalogue3.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieResult> movieResultList = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_playing_list,
                        parent, false)
        );
    }

    public void setMovieResult(List<MovieResult> movieResult) {
        this.movieResultList = movieResult;
    }

    public List<MovieResult> getList(){
        return movieResultList;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bindView(movieResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieResultList.size();
    }

    //View Holder
    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item_photo)
        ImageView item_poster;
        @BindView(R.id.btn_set_share)
        Button item_share;
        @BindView(R.id.tv_item_title)
        TextView item_title;
        @BindView(R.id.btn_set_detail)
        Button item_detail;
        @BindView(R.id.tv_item_date)
        TextView item_date;
        @BindView(R.id.tv_item_overview)
        TextView item_overview;
        @BindString(R.string.share)
        String share;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        void bindView(final MovieResult movieResult) {
            item_title.setText(movieResult.getTitle());
            item_overview.setText(movieResult.getOverview());
            item_date.setText(DateFormat.getDateDay(movieResult.getReleaseDate()));
            Picasso.get()
                    .load(Utils.BASE_POSTER_URL + movieResult.getPosterPath())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(item_poster);

            item_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(item_detail.getContext(), ActivityDetailMovie.class);
                    intent.putExtra(Utils.MOVIE_DETAIL, movieResult);
                    item_detail.getContext().startActivity(intent);
                }
            });
            item_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, share + ": " + movieResult.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
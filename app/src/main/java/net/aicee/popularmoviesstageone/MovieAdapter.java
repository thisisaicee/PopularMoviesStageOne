package net.aicee.popularmoviesstageone;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieModel> movieList;
    private Context context;

    public MovieAdapter(List<MovieModel> movieList, Context context){
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MovieModel movieModel = movieList.get(position);

        Picasso.with(this.context)
                .load(movieModel.getPosterPath())
                .into(holder.mMovieItemImageView);

        holder.mMovieItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieModel movieModel = movieList.get(position);

                Intent detailsIntent = new Intent(view.getContext(), DetailsActivity.class);
                detailsIntent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movieModel.getMovieId());
                view.getContext().startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.movie_item)
        ImageView mMovieItemImageView;

        public ViewHolder(View movieItemView) {
            super(movieItemView);

            ButterKnife.bind(this, movieItemView);
        }
    }
}


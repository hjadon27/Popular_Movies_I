package movies.mopular.bloom.mosi.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import movies.mopular.bloom.mosi.popularmoviesi.async.tasks.JSONClient;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView ivPoster;
    TextView tvTitle, tvReleaseDate, tvVote, tvAverage, tvPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ivPoster = (ImageView) findViewById(R.id.movie_detail_poster);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvReleaseDate = (TextView) findViewById(R.id.tv_releasedate);
        tvVote = (TextView) findViewById(R.id.tv_vote);
        tvAverage = (TextView) findViewById(R.id.tv_average);
        tvPlot = (TextView) findViewById(R.id.tv_plot);
        Intent intent = getIntent();

        int position = intent.getIntExtra("position",0);
        JSONObject movie = JSONClient.json_movie_list.get(position);

        //if strings are immutable, is it good to keep poster_path locally?
        String posterPath = "http://image.tmdb.org/t/p/w185/";
        Picasso.with(this).load(posterPath + movie.optString("poster_path").toString()).
                resize(300, 0).into(ivPoster);

        tvTitle.setText(movie.optString("title").toString());
        tvReleaseDate.setText("Relased on : " +movie.optString("release_date").toString());
        tvVote.setText("Vote : " +movie.optString("vote_count").toString());
        tvAverage.setText("Average : " +movie.optString("vote_average").toString());
        tvPlot.setText(movie.optString("overview").toString());

    }
}

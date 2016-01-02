package movies.mopular.bloom.mosi.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONObject;

import movies.mopular.bloom.mosi.popularmoviesi.async.tasks.GetJSONListener;
import movies.mopular.bloom.mosi.popularmoviesi.async.tasks.JSONClient;

public class MovieMainActivity extends AppCompatActivity{

    GridView gridview;
    String sortByPopularity = "popularity.desc";
    String sortByRating = "highestrated.desc";
    private static String apiKey = "please register your own key!!!";
    public static String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        gridview = (GridView) findViewById(R.id.grid_view);
        sortBy(sortByPopularity);
        JSONClient client = new JSONClient(this, jsonListener);
        client.execute(url);

        gridview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(MovieMainActivity.this, MovieDetailActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                }
        );
    }

    void setUrl(String sortBy){
        url = "http://api.themoviedb.org/3/discover/movie?sort_by="+ sortBy +"&api_key=" + apiKey;
    }

    GetJSONListener jsonListener = new GetJSONListener(){

        @Override
        public void onRemoteCallComplete(JSONObject jsonFromNet) {
            // add code to act on the JSON object that is returned
            gridview.setAdapter(new ImageAdapter(getBaseContext()));
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_popularity) {
            sortBy(sortByPopularity);
        }if (id == R.id.action_sort_ratings) {
            sortBy(sortByRating);
        }
        return super.onOptionsItemSelected(item);
    }

    void sortBy(String sortBy){
        setUrl(sortBy);
        if(JSONClient.json_movie_list!=null) {
            JSONClient client = new JSONClient(this, jsonListener);
            client.execute(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

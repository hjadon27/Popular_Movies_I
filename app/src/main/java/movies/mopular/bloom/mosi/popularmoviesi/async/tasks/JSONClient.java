package movies.mopular.bloom.mosi.popularmoviesi.async.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.mopular.bloom.mosi.popularmoviesi.MovieMainActivity;

/**
 * Created by Harendra Kumar on 29-12-2015.
 */
public class JSONClient extends AsyncTask<String, Void, JSONObject> {

    ProgressDialog progressDialog ;
    GetJSONListener getJSONListener;
    Context curContext;
    HttpURLConnection urlConnection;
    BufferedReader reader = null;
    String jasonString;
    public static List<JSONObject> json_movie_list;

    public JSONClient(Context context, GetJSONListener listener){
        this.getJSONListener = listener;
        curContext = context;
    }

    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog(curContext);
        progressDialog.setMessage("Loading.. ");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }


    @Override
    protected JSONObject doInBackground(String... params) {
        makeJason(getJasonString());
        return null;
    }

    String getJasonString(){
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            URL url = new URL(MovieMainActivity.url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jasonString = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return jasonString;
    }

    void makeJason(String jasonString){
        try {
            json_movie_list = new ArrayList<>();
            JSONObject jsonRootObject = new JSONObject(jasonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("results");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                json_movie_list.add(jsonObject);
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    @Override
    protected void onPostExecute(JSONObject json ) {
        getJSONListener.onRemoteCallComplete(json);
        progressDialog.dismiss();
    }

}

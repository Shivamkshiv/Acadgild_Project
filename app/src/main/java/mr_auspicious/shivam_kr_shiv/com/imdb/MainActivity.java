package mr_auspicious.shivam_kr_shiv.com.imdb;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {


    String url = "http://api.themoviedb.org/3/movie/now_playing?api_key=8496be0b2149805afa458ab8ec27560c";

    ListView listView;
    DetailAdapter detailAdapter;
    ArrayList<Detail> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyTask myTask = new MyTask();
        myTask.execute(url);


    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Internet Connection not Available", Toast.LENGTH_LONG).show();
        }
    }


    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String json = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "n");
                }

                json = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            JSONObject object = null;
            try {
                object = new JSONObject(s);
                JSONArray jsonArray =  object.getJSONArray("results");

                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject properties = jsonArray.getJSONObject(i);

                    String title = properties.getString("title");
                    String date = properties.getString("release_date");
                    String popularity = properties.getString("popularity");
                    String average = properties.getString("vote_average");
                    String vote_Count = properties.getString("vote_count");
                    String image = properties.getString("poster_path");

                    String image1 = "http://image.tmdb.org/t/p/w500" + image;



                    Detail detail = new Detail(title, date, popularity, average, vote_Count,image1);

                    info.add(detail);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            listView = (ListView) findViewById(R.id.list);
            detailAdapter = new DetailAdapter(MainActivity.this, info);
            listView.setAdapter(detailAdapter);


        }


    }
}





package mr_auspicious.shivam_kr_shiv.com.imdb;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class Most_Popular_Movies extends AppCompatActivity {


    String url = "http://api.themoviedb.org/3/movie/popular?api_key=8496be0b2149805afa458ab8ec27560c";

    ListView listView;
    DetailAdapter detailAdapter;
    ArrayList<Detail> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        new MyTask().execute(url);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Most_Popular_Movies.this,FullDetails.class);
                intent.putExtra("ID",info.get(i).getID());
                startActivity(intent);
            }
        });
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
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(1000);
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
                    String date = "Released on " +properties.getString("release_date");
                    String average = "Average : " + properties.getString("vote_average");
                    String vote_Count = "Vote Count : " + properties.getString("vote_count");
                    String image = "http://image.tmdb.org/t/p/w500" + properties.getString("poster_path");
                    String id = properties.getString("id");



                    Detail detail = new Detail(title, date, average, vote_Count,image,id);
                    info.add(detail);



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            detailAdapter = new DetailAdapter(Most_Popular_Movies.this, info);
            listView.setAdapter(detailAdapter);








        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.option1:
                //DO NOTHING
                return true;
            case R.id.option2:
                Intent intent1 = new Intent(this,Upcoming_Movies.class);
                startActivity(intent1);
                return true;
            case R.id.option3:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.option4:
                Intent intent2 = new Intent(this,Top_Rated.class);
                startActivity(intent2);
                return true;

            case R.id.option6:
                //do nothing
                return true;

            case R.id.option7:
                //do nothing
                return true;

            case R.id.option8:
                DetailAdapter detailAdapter = new DetailAdapter(Most_Popular_Movies.this,info);
                listView.setAdapter(detailAdapter);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}

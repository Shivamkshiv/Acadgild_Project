package mr_auspicious.shivam_kr_shiv.com.imdb;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FullDetails extends AppCompatActivity {

    private static String url = "http://api.themoviedb.org/3/movie/"; //base_url
    private static String casts_url ; //cast_url
    private static String full_Movie_Details_Url ; //detail_url
    private static String trailer_url ; //trailer_url
    private static String posters_Url ; //poster_url
    TextView Movie_Title; //Movie_Title
    TextView Budget; //Movie_Budget
    TextView Revenue; //Movie_Revenue
    TextView Status; //Movie_Status
    TextView Vote_count; //Vote Count
    TextView overview; //Overview_Title
    TextView Description;  //Movie_Description
    ImageView image; //Movie_Poster
    String key;// Youtube video Key
    ImageView favorite; //Favorite image
    ImageView watchlist ; //WatchList image
    Detail detail;//Model
    Bundle bundle; //Bundle
    List<Detail> list; //A List


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fulldetails);

        bundle = getIntent().getExtras(); //Getting the extras from the intent in the variable extras(Bundle).
        String id = bundle.getString("ID"); //Parsing the  extras to String - id.

        /*Full Movie Details URL */
        full_Movie_Details_Url =  url + id + "?api_key=8496be0b2149805afa458ab8ec27560c"; //Initializing the movie details URL variable.

        /*Posters_Movie_URL */
        posters_Url = url + id + "/images?api_key=8496be0b2149805afa458ab8ec27560c"; //Initializing the posters URL variable.

        /*Casts_Movie_URL */
        casts_url = url + id + "/credits?api_key=8496be0b2149805afa458ab8ec27560c"; //Initializing the casts URL variable.

        /*trailers_url */
        trailer_url = url + id + "/videos?api_key=8496be0b2149805afa458ab8ec27560c"; //Initializing the trailers URL variable.


        new MovieTask().execute(full_Movie_Details_Url);
        new GetPosters().execute(posters_Url);
        new GetCasts().execute(casts_url);
        new getTrailers().execute(trailer_url);


        Movie_Title = (TextView) findViewById(R.id.Movie_title);
        Budget = (TextView) findViewById(R.id.text);
        Revenue = (TextView) findViewById(R.id.text2);
        Status = (TextView) findViewById(R.id.text3);
        Vote_count = (TextView) findViewById(R.id.text4);
        overview = (TextView) findViewById(R.id.overview);
        overview.setText(getString(R.string.ov));
        Description = (TextView) findViewById(R.id.desciption);
        image = (ImageView) findViewById(R.id.image);
        favorite = (ImageView) findViewById(R.id.b1);
        watchlist = (ImageView) findViewById(R.id.b2);

         detail = new Detail();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMovie(bundle.getString("id"));
                Object tag =  favorite.getTag();

                detail.mID = list.get(0).getID();
                detail.mTextView = list.get(0).mTextView;
                detail.mTextView1 = list.get(0).mTextView1;
                detail.mTextView2 = list.get(0).mTextView2;
                detail.mTextView3 = list.get(0).mTextView3;
                detail.mImageView = list.get(0).mImageView;
                if (tag == "disable") {
                    favorite.setImageResource(android.R.drawable.btn_star_big_on);
                    favorite.setTag("enable");

                    Toast.makeText(getBaseContext(),"Added to Favourite",Toast.LENGTH_LONG).show();
                    MovieHelper db = new MovieHelper(FullDetails.this);
                    boolean check = db.checkMovie(detail.getID());
                    if (check)
                        db.updateMovieF(detail);
                    else
                        db.addMovies(detail);
                } else {
                    favorite.setImageResource(android.R.drawable.btn_star);
                    favorite.setTag("disable");
                    detail.setmFavorite(String.valueOf(0));
                    MovieHelper db = new MovieHelper(FullDetails.this);
                    db.updateMovieF(detail);
                    db.deleteNonFavWatchMovie();
                }
            }
        });

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMovie(bundle.getString("id"));
                Object tag =  watchlist.getTag();
                detail.mID = list.get(0).getID();
                detail.mTextView = list.get(0).mTextView;
                detail.mTextView1 = list.get(0).mTextView1;
                detail.mTextView2 = list.get(0).mTextView2;
                detail.mTextView3 = list.get(0).mTextView3;
                detail.mImageView = list.get(0).mImageView;
                if (tag == "disable") {
                    watchlist.setImageResource(android.R.drawable.presence_video_online);
                    watchlist.setTag("enable");
                    Toast.makeText(getBaseContext(),"Added to Watchlist ",Toast.LENGTH_LONG).show();
                    MovieHelper db = new MovieHelper(FullDetails.this);
                    boolean check = db.checkMovie(detail.getID());
                    if (check)
                        db.updateMovieF(detail);
                    else
                        db.addMovies(detail);
                } else {
                    watchlist.setImageResource(android.R.drawable.presence_video_busy);
                    watchlist.setTag("disable");
                    detail.setmFavorite(String.valueOf(0));
                    MovieHelper db = new MovieHelper(FullDetails.this);
                    db.updateMovieF(detail);
                    db.deleteNonFavWatchMovie();
                }

            }
        });


    }

    private void checkMovie(String id) {

        MovieHelper db = new MovieHelper(FullDetails.this);
        db.deleteNonFavWatchMovie();
        Boolean check = db.checkMovie(id);
        if (!check) { //checks if movie does not existing in database
            favorite.setImageResource(android.R.drawable.btn_star);
            favorite.setTag("disable");
            watchlist.setImageResource(android.R.drawable.presence_video_busy);
            watchlist.setTag("disable");
        } else { //if movie does exist
            Detail details = db.getMovie(id);
            if (details.getmFavorite().equals("0")) { //set image based on database value
                favorite.setImageResource(android.R.drawable.btn_star);
                favorite.setTag("disable");
                detail.setmFavorite(String.valueOf(0));

            } else  {
                favorite.setImageResource(android.R.drawable.btn_star_big_on);
                favorite.setTag("enable");
                detail.setmFavorite(String.valueOf(1));
            }

            if (details.getmWatchList().equals("0")) { //set image based on database value
                watchlist.setImageResource(android.R.drawable.presence_video_busy);
                watchlist.setTag("disable");
                detail.setmWatchList(String.valueOf(0));
            } else {
                watchlist.setImageResource(android.R.drawable.presence_video_online);
                watchlist.setTag("enable");
                detail.setmWatchList(String.valueOf(1));
            }
        }
    }


    /* Async Task Class in which we load the JSON data from internet and parse it to the required data */
    private class MovieTask extends AsyncTask<String,Void,String >{

        HttpURLConnection urlConnection;
        String json;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
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

            list = new ArrayList<>();

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(json);
                String movieTitle = jsonObject.getString("original_title");
                String budget = "Budget : "+"Rs." +jsonObject.getString("budget");
                String status = "Status : "+jsonObject.getString("status");
                String revenue = "Revenue : "+jsonObject.getString("revenue");
                String average = "(" +jsonObject.getString("vote_average")+ "/10)";
                String description = jsonObject.getString("overview");
                String poster = "http://image.tmdb.org/t/p/w500"+ jsonObject.getString("poster_path");
                String vote_count = jsonObject.getString("vote_count");
                String date = jsonObject.getString("release_date");
                String id = jsonObject.getString("id");

                Movie_Title.setText(movieTitle);
                Budget.setText(budget);
                Status.setText(status);
                Revenue.setText(revenue);
                Vote_count.setText(average);
                Description.setText(description);
                Picasso.with(getBaseContext()).load(poster).into(image);

                detail = new Detail(movieTitle,date,average,vote_count,poster,id);
                list.add(detail);


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    private class GetPosters extends AsyncTask<String,Void,String>{



        HttpURLConnection urlConnection;
        String json;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(2000);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
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


            JSONArray posters;
            try {
                JSONObject jsonObj = new JSONObject(s);

                posters = jsonObj.getJSONArray("backdrops");
                for (int i = 0; i < posters.length(); i++) {
                    JSONObject c = posters.getJSONObject(i);

                    String file_path ="http://image.tmdb.org/t/p/w500" + c.getString("file_path");

                    LinearLayout sv = (LinearLayout) findViewById (R.id.lin_layout);
                    ImageView iv = new ImageView (FullDetails.this);
                    LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageViewLayoutParams.leftMargin=8;
                    iv.setPadding(4,4,4,4);
                    iv.setLayoutParams(imageViewLayoutParams);
                    iv.setId(i);
                    Picasso.with(getBaseContext()).load(file_path).into(iv);
                    sv.addView(iv);


                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }




    }

    private class GetCasts extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... params) {





            String json = null;
            HttpURLConnection urlConnection;
            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "n");
                }

                json = sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject js = new JSONObject(s);
                JSONArray ja = js.getJSONArray("cast");
                JSONArray ja1 = js.getJSONArray("crew");

                for(int i=0;i<ja.length();i++){

                    //Data for Casts
                    JSONObject cast = ja.getJSONObject(i);
                    String character = cast.getString("character");
                    String castName = cast.getString("name");
                    String castProfilePath = "http://image.tmdb.org/t/p/w500" + cast.getString("profile_path");



                    /* Displaying casts from the parsed data above */

                    /* Parent View  */
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin_layout1);

                    View cell = getLayoutInflater().inflate(R.layout.posters,null);  //Child View

                    ImageView imageView = (ImageView) cell.findViewById(R.id.images);
                    Picasso.with(getBaseContext()).load(castProfilePath).into(imageView);

                    TextView text = (TextView) cell.findViewById(R.id.name);
                    text.setText(castName);

                    TextView text1 = (TextView) cell.findViewById(R.id.character);
                    text1.setText(character);

                    linearLayout.addView(cell);  //adding child view in parent view



                }

                for(int j=0;j<ja1.length();j++) {

                    //Data for Crews
                    JSONObject cast1 = ja1.getJSONObject(j);
                    String name = cast1.getString("name");
                    String job = cast1.getString("job");
                    String text3 = cast1.getString("department");


                /* Displaying crews from the parsed data */

                    /* Parent View  */
                    LinearLayout lin = (LinearLayout) findViewById(R.id.lin_layout2);

                    View crew = getLayoutInflater().inflate(R.layout.crews_design, null);  //Child View

                    TextView name_crew = (TextView) crew.findViewById(R.id.crewName);
                    name_crew.setText(name);

                    TextView job_crew = (TextView) crew.findViewById(R.id.crewJob);
                    job_crew.setText(job);

                    TextView department_crew = (TextView) crew.findViewById(R.id.crewDepartment);
                    department_crew.setText(text3);

                    lin.addView(crew);  //adding child view in parent view


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



    }

    private class getTrailers extends AsyncTask<String,Void,String>{

        String json="";
        HttpURLConnection urlConnection;
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(2000);
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
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

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i=0; i<jsonArray.length();i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    String name = jsonObject1.getString("name");
                    key = jsonObject1.getString("key");  //Initializing

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin_layout3);
                    View view = getLayoutInflater().inflate(R.layout.trailers_design,null);

                    TextView textView = (TextView) view.findViewById(R.id.trailer_name);
                    textView.setText(name);

                   textView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+key));
                           startActivity(intent);
                       }
                   });

                    linearLayout.addView(view);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}


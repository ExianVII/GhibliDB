package com.example.ghiblidb;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class GhibliDB extends AppCompatActivity {

    //Volley variable
    private RequestQueue requestQueue;

    //Display variables, in order of appearance from top to bottom.
    private EditText movie_entry;
    private Button search;
    private TextView movie_title;
    private TextView movie_desc;
    private TextView movie_dir;
    private TextView movie_year;
    private ImageView movieImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie_entry = findViewById(R.id.EnterMovie);
        search = findViewById(R.id.Go);
        movie_title = findViewById(R.id.MovieTitle);
        movie_desc = findViewById(R.id.MovieDesc);
        movie_dir = findViewById(R.id.MovieDirector);
        movie_year = findViewById(R.id.MovieYear);
        movieImg = findViewById(R.id.movieImg);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    getMovieData();
            }
        });

    }

    //Behold....Better code!
    public void getMovieData()
    {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET,
                "https://ghibliapi.herokuapp.com/films?title="+movie_entry.getText().toString(),
                null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject responseObj;
                if(response == null || response.length() == 0)
                {

                    movie_title.setText("Error! Movie not found. Please read the tip " +
                            "and try again.");
                    //erase the result of previous valid queries.
                    movie_desc.setText("");
                    movie_year.setText("");
                    movie_dir.setText("");
                    movieImg.setImageResource(R.drawable.download);
                }
                else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            responseObj = response.getJSONObject(i);


                            Log.d("json", "on response: array movie: " + i + "movie title is " +
                                    responseObj.getString("title"));
                            String resourceName = responseObj.getString("title")
                                    .toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
                            int resID = getResources().getIdentifier(resourceName,"drawable",getPackageName());

                            movie_title.setText(responseObj.getString("title"));
                            movie_desc.setText(responseObj.getString("description"));
                            movie_dir.setText(responseObj.getString("director"));
                            movie_year.setText(responseObj.getString("release_date"));
                            movieImg.setImageResource(resID);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                movie_title.setText("Something else went wrong.");
                Log.d("json", "on response: ERRROR "+ error);
            }
        });
        requestQueue.add(objectRequest);
    }

}
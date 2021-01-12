package com.example.testtiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "https://app.fakejson.com/q/Wr7VfBZu?token=GoZCA87_WI-FRyjQVu8GIQ";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_TITLE = "com.example.testtiles.EXTRA_TITLE";
    public static final String EXTRA_URL = "com.example.testtiles.EXTRA_URL";
    private List<DataModel> dataModelList;
    TilesAdapter tilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        dataModelList = new ArrayList<>();

        tilesAdapter = new TilesAdapter(this, dataModelList);
        recyclerView.setAdapter(tilesAdapter);

        LinearLayout noConnectionLayout = (LinearLayout) findViewById(R.id.network_failed_layout);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null){
            noConnectionLayout.setVisibility(View.VISIBLE);
        } else if(!networkInfo.isConnected()){
            noConnectionLayout.setVisibility(View.VISIBLE);
        }else{
            noConnectionLayout.setVisibility(View.GONE);
        }

        tilesAdapter.setOnItemClickListener(new TilesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataModel dataModel) {
                Intent openDetails = new Intent(MainActivity.this, Details.class);
                openDetails.putExtra(EXTRA_TITLE, dataModel.getTitle());
                openDetails.putExtra(EXTRA_URL, dataModel.getImgUrl());
                startActivity(openDetails);
            }
        });

        getData();
    }

    private void getData() {

        int METHOD = Request.Method.GET;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(METHOD, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray assetsArray = response.getJSONArray("assets");

                    if(assetsArray.length() > 0){
                        for(int i = 0; i<assetsArray.length(); i++){

                            DataModel dataModel = new DataModel();

                            JSONObject asset = assetsArray.getJSONObject(i);
                            String id = asset.getString("id");
                            String title = asset.getString("title");
                            String imageLink = asset.getString("img_src");

                            Log.d(LOG_TAG, "onResponse: \n Title: "+title+"\n Link: "+imageLink);

                            dataModel.setTitle(title);
                            dataModel.setImgUrl(imageLink);

                            dataModelList.add(dataModel);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tilesAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }
}

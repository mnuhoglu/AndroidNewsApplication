package com.example.muratnuhoglucs310homwork3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView newsRecView;
    List<NewsItem> data;
    ProgressDialog prgDialog;
    NewsAdapter adp;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = findViewById(R.id.spinner);
        data = new ArrayList<>();
        newsRecView = findViewById(R.id.recViewNews);
        String[] categories = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> adpsp =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categories);
        sp.setAdapter(adpsp);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NewsTask tsk1 = new NewsTask();
                if(position == 0){
                    tsk1.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }else if(position == 1){
                    tsk1.execute(" http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/4");
                }else if(position == 2){
                    tsk1.execute(" http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/6");
                }else if(position == 3){
                    tsk1.execute(" http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/5");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setTitle("News");
        adp = new NewsAdapter(this, data, new NewsAdapter.NewsItemClickListener() {
                    @Override
                    public void NewsItemClicked(NewsItem selectedNewsItem) {
                        Intent i = new Intent(MainActivity.this,NewsDetailActivity.class);
                        i.putExtra("selectednews",selectedNewsItem);
                        startActivity(i);
                    }
                });
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);

    }

    class NewsTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();
            try {
                JSONObject obj = new JSONObject(s);
                if(obj.getInt("serviceMessageCode") == 1){

                    JSONArray arr = obj.getJSONArray("items");

                    for(int i= 0 ; i<arr.length() ; i++){

                        JSONObject current = (JSONObject) arr.get(i);

                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem item = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate);
                        data.add(item);
                    }

                }else{

                }
                adp.notifyDataSetChanged();
                prgDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

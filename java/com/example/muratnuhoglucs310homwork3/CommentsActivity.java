package com.example.muratnuhoglucs310homwork3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView commentsRecView;
    List<CommentItem> data;
    ProgressDialog prgDialog;
    CommentsAdapter adp;
    int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        data = new ArrayList<>();
        commentsRecView = findViewById(R.id.commentsrecview);
        setTitle("Comments");
        adp = new CommentsAdapter(this,data);
        commentsRecView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecView.setAdapter(adp);
        selectedID = getIntent().getExtras().getInt("id");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_24px);

        CommentsTask tsk = new CommentsTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/"+selectedID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.comments_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.mn_add_comment){
            Intent i = new Intent(this,PostCommentActivity.class);
            i.putExtra("id",selectedID);
            startActivity(i);
        }
        return true;
    }

    class CommentsTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(CommentsActivity.this);
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


                        CommentItem item = new CommentItem(current.getInt("id"),
                                current.getInt("news_id"),
                                current.getString("text"),
                                current.getString("name"));
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

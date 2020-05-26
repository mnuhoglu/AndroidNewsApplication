package com.example.muratnuhoglucs310homwork3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostCommentActivity extends AppCompatActivity {

    EditText txtname;
    EditText txtcomment;
    Button btnpost;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        txtname = findViewById(R.id.txtpostname);
        txtcomment = findViewById(R.id.txtpostcomment);
        btnpost = findViewById(R.id.btnpost);
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_24px);
        setTitle("Post Comment");
  }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    public void PostCommentClicked (View v){
        String name = txtname.getText().toString();
        String comment = txtcomment.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(PostCommentActivity.this);
        if(name.length()==0 || comment.length()==0){
        builder.setTitle("Warning!").setMessage("You should fill all the spaces!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();}
        else {
            PostCommentTask tsk = new PostCommentTask();
            tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment", txtname.getText().toString(), txtcomment.getText().toString());
        }
    }



    class PostCommentTask extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(PostCommentActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            String urlStr = strings[0];
            String name = strings[1];
            String comment = strings[2];
            int newsID = getIntent().getExtras().getInt("id");
            StringBuilder strBuilder = new StringBuilder();

            JSONObject obj = new JSONObject();
            try {
                obj.put("name", name);
                obj.put("text", comment);
                obj.put("news_id",newsID);
            }catch (JSONException e){
                e.printStackTrace();
            }

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Intent i = new Intent(PostCommentActivity.this,CommentsActivity.class);
                    startActivity(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            prgDialog.dismiss();
        }
    }
}

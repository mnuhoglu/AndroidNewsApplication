package com.example.muratnuhoglucs310homwork3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class NewsDetailActivity extends AppCompatActivity {

    NewsItem selectednews;
    TextView txtdate;
    TextView txtdetails;
    ImageView imgnews;
    TextView txttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        txtdate = findViewById(R.id.txtdate);
        txtdetails = findViewById(R.id.txtdetails);
        txttitle = findViewById(R.id.txttitle);
        imgnews = findViewById(R.id.imgnews);

        selectednews = (NewsItem)getIntent().getSerializableExtra("selectednews");


        ImageDownloadTask tsk = new ImageDownloadTask(imgnews);
        tsk.execute(selectednews);
        txtdetails.setText(selectednews.getText());
        txtdate.setText(new SimpleDateFormat("dd/MM/yyy").format(selectednews.getNewsDate()));
        txttitle.setText(selectednews.getTitle());

        getSupportActionBar().setTitle("News Details");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_24px);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.mn_comments){
            Intent i = new Intent(this,CommentsActivity.class);
            i.putExtra("id",selectednews.getId());
            startActivity(i);
        }
        return true;
    }
}

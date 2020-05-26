package com.example.muratnuhoglucs310homwork3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsVievHolder> {

    Context context;
    List<NewsItem> news;
    NewsItemClickListener listener;

    public NewsAdapter(Context context, List<NewsItem> news, NewsItemClickListener listener) {
        this.context = context;
        this.news = news;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsVievHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.news_row_layout,parent,false);
        return new NewsVievHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsVievHolder holder, final int position) {

            holder.txtDate.setText(new SimpleDateFormat("dd/MM/yyy").format(news.get(position).getNewsDate()));
            holder.txtTitle.setText(news.get(position).getTitle());

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.NewsItemClicked(news.get(position));
                }
            });


        if (news.get(position).getBitmap()==null){
                new ImageDownloadTask(holder.imgNews).execute(news.get(position));
            }else{
                holder.imgNews.setImageBitmap(news.get(position).getBitmap());
            }


    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public interface NewsItemClickListener{
        public void NewsItemClicked(NewsItem selectedNewsItem);
    }

    class NewsVievHolder extends RecyclerView.ViewHolder{

        ImageView imgNews;
        TextView txtTitle;
        TextView txtDate;
        ConstraintLayout root;


        public NewsVievHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.imgnews);
            txtTitle = itemView.findViewById(R.id.txtnews);
            txtDate = itemView.findViewById(R.id.txtdate);
            root = itemView.findViewById(R.id.container);

        }
    }

}

package com.example.muratnuhoglucs310homwork3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.commentsViewHolder> {

    Context context;
    List<CommentItem> comments;

    public CommentsAdapter(Context context, List<CommentItem> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public commentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comments_row_layout,parent,false);
        return new commentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull commentsViewHolder holder, int position) {

        holder.txtname.setText(comments.get(position).getName());
        holder.txtcomment.setText(comments.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }



    class commentsViewHolder extends RecyclerView.ViewHolder{

    TextView txtname;
    TextView txtcomment;
    ConstraintLayout root;

    public commentsViewHolder(@NonNull View itemView) {
        super(itemView);
        txtname = itemView.findViewById(R.id.txtname);
        txtcomment = itemView.findViewById(R.id.txtcomment);
        root = itemView.findViewById(R.id.containerr);
         }
}

}

package com.example.swimfo.unorganized.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.Chapter;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.topic.TopicContent;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapters = new ArrayList<>();

    public void setChapters(List<Chapter> chaptersList) {
        chapters = chaptersList;
        notifyDataSetChanged();
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.chapterNameTextView.setText(chapter.getChapterTitle());
        holder.chapterDescTextView.setText(chapter.getChapterDesc());
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {

        public TextView chapterNameTextView;
        public TextView chapterDescTextView;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            chapterNameTextView = itemView.findViewById(R.id.textViewChapterName);
            chapterDescTextView = itemView.findViewById(R.id.textViewChapterDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Chapter chapter = chapters.get(position);
                        // Proceed to the actual content of the chapter
                        Intent intent = new Intent(v.getContext(), TopicContent.class);
                        intent.putExtra("chapId", chapter.getChapterId());
                        intent.putExtra("chapTitle", chapter.getChapterTitle());
                        intent.putExtra("chapDesc", chapter.getChapterDesc());
                        intent.putExtra("chapDemo", chapter.getChapterDemo());
                        intent.putExtra("vUrl", chapter.getVideoUrl());
                        v.getContext().startActivity(intent);

                    }
                }
            });
        }
    }
}
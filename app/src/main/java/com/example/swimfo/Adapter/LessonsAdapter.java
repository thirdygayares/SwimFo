package com.example.swimfo.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.Teacher.topic.Lesson;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.topic.AddChapter;

import java.util.ArrayList;
import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>{
    private List<Lesson> lessons = new ArrayList<>();
    Boolean isAdmin;
    public void setLessons(List<Lesson> lessons, Boolean isAdmin) {
        this.lessons = lessons;
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
    }
    @Override
    public LessonsAdapter.LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LessonsAdapter.LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        if (isAdmin){
            holder.add.setVisibility(View.VISIBLE);
        }
        holder.lessonNameTextView.setText(lesson.getLessonName());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddChapter.class);
                intent.putExtra("topicId", lesson.getTopicId());
                intent.putExtra("lessonId", lesson.getLessonId());
                v.getContext().startActivity(intent);
            }
        });


        // Set up the inner RecyclerView for chapters
        ChapterAdapter chapterAdapter = new ChapterAdapter();
        holder.chapterRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.chapterRecyclerView.setAdapter(chapterAdapter);
        chapterAdapter.setChapters(lesson.getChapters());
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {

        public TextView lessonNameTextView;
        public RecyclerView chapterRecyclerView;
        ImageView add;
        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonNameTextView = itemView.findViewById(R.id.textViewLessonName);
            chapterRecyclerView = itemView.findViewById(R.id.recyclerViewChapters);
            add = itemView.findViewById(R.id.addChapter);
        }
    }
}

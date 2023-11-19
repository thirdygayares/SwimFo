package com.example.swimfo.Teacher.topic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.swimfo.Adapter.TopicAdapter;
import com.example.swimfo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicManagement extends Fragment {

    View view;
    RecyclerView recyclerView;
    ArrayList<TopicModel> list;
    TopicAdapter topicAdapter;
    Button add;
    FirebaseDatabase node;
    DatabaseReference topRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_topic_management, container, false);

        recyclerView = view.findViewById(R.id.rv);
        add = view.findViewById(R.id.addBtn);


        list = new ArrayList<>();


        node = FirebaseDatabase.getInstance();
//        topicAdapter = new TopicAdapter(this, list, "teacher");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(topicAdapter);

        topRef = node.getReference("topics");

        setupRecyclerView();
        fetchTopics();
//        topRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                list.clear();
//                if (dataSnapshot.hasChildren()){
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        TopicModel topic = snapshot.getValue(TopicModel.class);
//
//                        list.add(topic);
//                    }
//
//                }else{
//                    Toast.makeText(TopicManagement.this, "No Topics Yet", Toast.LENGTH_SHORT).show();
//                }
//
//
//                topicAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("YourActivity", "Failed to retrieve topics: " + databaseError.getMessage());
//            }
//        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddTopic.class);
                intent.putExtra("isUpdate", false);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchTopics() {
        topRef = FirebaseDatabase.getInstance().getReference("topics");
        topRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TopicModel> topicList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TopicModel topic = snapshot.getValue(TopicModel.class);
                    topicList.add(topic);
                }

                topicAdapter.list = topicList; // Update the adapter's data
                topicAdapter.getFilter().filter(""); // Reset the filter
                topicAdapter.notifyDataSetChanged(); // Notify the adapter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle this error condition
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topicAdapter = new TopicAdapter(getContext(), new ArrayList<>(), "teacher");
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(topicAdapter);
    }
}
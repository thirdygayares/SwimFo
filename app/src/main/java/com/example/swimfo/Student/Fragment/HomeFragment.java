package com.example.swimfo.Student.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.swimfo.ProfileActivity;
import com.example.swimfo.R;
import com.example.swimfo.unorganized.Adapter.TopicAdapter;
import com.example.swimfo.Teacher.topic.TopicModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private View homeView;
    private TextView person;
    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;
    private FirebaseDatabase node;
    private DatabaseReference topRef;
    private CircleImageView profile;
    EditText searchEditText;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews();
        setupProfile();
        setupRecyclerView();
        fetchTopics();

        profile.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                topicAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this implementation
            }
        });

        return homeView;
    }

    private void initializeViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();
        String profilePictureUrl = photoUrl.toString();

        profile = homeView.findViewById(R.id.profile_image);
        person = homeView.findViewById(R.id.person_name);
        recyclerView = homeView.findViewById(R.id.rv);

         searchEditText= homeView.findViewById(R.id.search_view);


    }

    private void setupProfile() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("studentlist").child(userId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String imgPath = dataSnapshot.child("imgUrl").getValue(String.class);

                    person.setText(name);
                    Glide.with(getContext()).load(imgPath).into(profile);
                } else {
                    // Handle this case accordingly
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle this error condition
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topicAdapter = new TopicAdapter(getContext(), new ArrayList<>(), "student");
        recyclerView.setAdapter(topicAdapter);
    }

    private void fetchTopics() {
        topRef = FirebaseDatabase.getInstance().getReference("topics");

    // Query the records with topicHidden set to false
            Query query = topRef.orderByChild("topicHidden").equalTo(false);

            query.addValueEventListener(new ValueEventListener() {
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


//        topRef = FirebaseDatabase.getInstance().getReference("topics");
//        topRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<TopicModel> topicList = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    TopicModel topic = snapshot.getValue(TopicModel.class);
//                    topicList.add(topic);
//                }
//
//                topicAdapter.list = topicList; // Update the adapter's data
//                topicAdapter.getFilter().filter(""); // Reset the filter
//                topicAdapter.notifyDataSetChanged(); // Notify the adapter
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle this error condition
//            }
//        });
    }
}

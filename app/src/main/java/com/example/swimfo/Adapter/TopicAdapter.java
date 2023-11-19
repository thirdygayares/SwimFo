package com.example.swimfo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swimfo.Teacher.topic.AddTopic;
import com.example.swimfo.Teacher.topic.LessonsActivity;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.topic.TopicModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> implements Filterable {
    Context context;
//    ArrayList<TopicModel> list;
    String userRole;
    public List<TopicModel> list;
    private List<TopicModel> filteredDataList;

//    public TopicAdapter(Context context, ArrayList<TopicModel> list, String userRole) {
//        this.context = context;
//        this.list = list;
//        this.userRole = userRole;
//    }
        public TopicAdapter(Context context, ArrayList<TopicModel> list, String userRole) {
            this.context = context;
            this.list = list;
            this.userRole = userRole;
            this.filteredDataList = new ArrayList<>(list); // Initialize the filtered list
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topic_item,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopicModel topicModel = list.get(position);
        boolean isAdmin = false;

        if (userRole.equalsIgnoreCase("teacher")){
            holder.btnWrap.setVisibility(View.VISIBLE);
            isAdmin = true;
        }

        if (topicModel.getImageUrl() != null){
            Glide.with(context).load(topicModel.getImageUrl()).into(holder.banner);
        }

        if (topicModel.getTopicHidden() == true){
            holder.hide.setText("UnHide");
        }else{
            holder.hide.setText("Hide");
        }

        holder.tt.setText(topicModel.getTopicTitle());
        boolean finalIsAdmin = isAdmin;
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LessonsActivity.class);
                intent.putExtra("topicId", topicModel.getTopicId());
                intent.putExtra("imgUrl", topicModel.getImageUrl());
                intent.putExtra("title", topicModel.getTopicTitle());
                intent.putExtra("desc", topicModel.getTopicDesc());
                intent.putExtra("isAdmin", finalIsAdmin);
                context.startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddTopic.class);
                intent.putExtra("topicId", topicModel.getTopicId());
                intent.putExtra("isUpdate", true);
                intent.putExtra("imgUrl", topicModel.getImageUrl());
                intent.putExtra("title", topicModel.getTopicTitle());
                intent.putExtra("desc", topicModel.getTopicDesc());
                context.startActivity(intent);
            }
        });
        holder.rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title
                builder.setTitle("Removing");
                //set message
                builder.setMessage("Are you sure you want to remove this topic?\nLessons under this topic will also remove!");
                //positive yes
                builder.setPositiveButton("YES", (dialog, which) -> {
                    //remove section subject
                    String topicid = list.get(position).getTopicId();
                    DatabaseReference sectionRef = FirebaseDatabase.getInstance().getReference("topics").child(topicid);
                    sectionRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Topic removed successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure( Exception e) {
                                    Toast.makeText(context, "Failed to remove Topic", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //dismiss dialog
                    dialog.dismiss();
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.create();
                builder.show();

            }
        });
        holder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = (String) holder.hide.getText();
                Boolean hideIt = false;

                if (txt.equalsIgnoreCase("hide")){
                    hideIt = true;
                }
                //alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set title
                builder.setTitle("Topic's Visibility");
                //set message
                builder.setMessage("Are you sure you want to "+txt+" this topic?");
                //positive yes
                Boolean finalHideIt = hideIt;
                builder.setPositiveButton("YES", (dialog, which) -> {
                    //remove section subject
                    String topicid = list.get(position).getTopicId();
                    DatabaseReference sectionRef = FirebaseDatabase.getInstance().getReference("topics").child(topicid);
                    sectionRef.child("topicHidden").setValue(finalHideIt).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Successfully "+txt, Toast.LENGTH_SHORT).show();
//                            holder.hide.setText("UnHide");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //dismiss dialog
                    dialog.dismiss();
                });
                //negative no
                builder.setNegativeButton("NO", (dialog, which) -> {
                    //dismiss dialog
                    dialog.dismiss();
                });
                builder.create();
                builder.show();

            }
        });
        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LessonsActivity.class);
                intent.putExtra("topicId", topicModel.getTopicId());
                intent.putExtra("isUpdate", true);
                intent.putExtra("imgUrl", topicModel.getImageUrl());
                intent.putExtra("title", topicModel.getTopicTitle());
                intent.putExtra("desc", topicModel.getTopicDesc());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase();

                List<TopicModel> filteredList = new ArrayList<>();
                for (TopicModel item : list) {
                    if (item.getTopicTitle().toLowerCase().contains(query)) {
                        filteredList.add(item);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList.clear();
                filteredDataList.addAll((List<TopicModel>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tt,hide;
        CardView card;
        LinearLayout btnWrap;
        ImageView edit,rem,go,banner;
        public MyViewHolder(View itemView) {
            super(itemView);

            tt = itemView.findViewById(R.id.titleTopic);
            hide = itemView.findViewById(R.id.hide);
            banner = itemView.findViewById(R.id.topicBanner);
            edit = itemView.findViewById(R.id.edit);
            rem = itemView.findViewById(R.id.remove);
            go = itemView.findViewById(R.id.go);
            card = itemView.findViewById(R.id.t1);
            btnWrap = itemView.findViewById(R.id.btnWrapper);
        }
    }
}

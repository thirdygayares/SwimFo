package com.example.swimfo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Model.SectionModel;
import com.example.swimfo.Teacher.section.StudentList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AvailableClassAdapter extends RecyclerView.Adapter<AvailableClassAdapter.MyViewHolder> {

    Context context;
    ArrayList<SectionModel> list;


    FirebaseDatabase rootNode;
    DatabaseReference studentRef,adminRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public AvailableClassAdapter(Context context, ArrayList<SectionModel>list) {
        this.context = context;
        this.list = list;
    }

    
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_code,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        SectionModel sectionModel = list.get(position);
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();


        rootNode = FirebaseDatabase.getInstance();


        holder.mySectionTv.setText(sectionModel.getSectionName());
        holder.go.setOnClickListener(v->{
            Intent intent = new Intent(context, StudentList.class);
            intent.putExtra("section", list.get(position).getSectionName());
            intent.putExtra("id", list.get(position).getId());
            context.startActivity(intent);
        });
        holder.cv.setOnClickListener(v->{
            Intent intent = new Intent(context, StudentList.class);
            intent.putExtra("section", list.get(position).getSectionName());
            intent.putExtra("id", list.get(position).getId());
            context.startActivity(intent);
        });
        holder.remove.setOnClickListener(v -> {
            //alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //set title
            builder.setTitle("Removing");
            //set message
            builder.setMessage("Are you sure you want to remove this section?");
            //positive yes
            builder.setPositiveButton("YES", (dialog, which) -> {
                //remove section subject
                String sectionId = list.get(position).getId();
                DatabaseReference sectionRef = FirebaseDatabase.getInstance().getReference("users/sections/"+uid).child(sectionId);
                sectionRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Section removed successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure( Exception e) {
                                Toast.makeText(context, "Failed to remove section", Toast.LENGTH_SHORT).show();
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

        });

        holder.edit.setOnClickListener(v -> {
            // Alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Inflate the custom layout
            View dialogView = LayoutInflater.from(context).inflate(R.layout.update_section, null);

            EditText inputEditText = dialogView.findViewById(R.id.dialogEditText);
            Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
            Button saveButton = dialogView.findViewById(R.id.dialogSaveButton);

            // Set the current section name as the initial text in the EditText
            String currentSectionName = list.get(position).getSectionName();
            inputEditText.setText(currentSectionName);

            // Set the custom view for the AlertDialog
            builder.setView(dialogView);

            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            // Handle cancel button click
            cancelButton.setOnClickListener(cancelView -> {
                dialog.dismiss();
            });

            // Handle save button click
            saveButton.setOnClickListener(saveView -> {
                String updatedSectionName = inputEditText.getText().toString().trim();

                // Update the section name in Firebase Realtime Database
                String sectionId = list.get(position).getId();
                DatabaseReference sectionRef = FirebaseDatabase.getInstance().getReference("users/sections/" + uid).child(sectionId);
                sectionRef.child("sectionName").setValue(updatedSectionName)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Section name updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to update section name", Toast.LENGTH_SHORT).show();
                        });

                dialog.dismiss();
            });

            // Show the AlertDialog
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView edit,remove,go;
        CardView cv;
        TextView mySectionTv;
     //   View parentLayout;
        public MyViewHolder( View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            remove = itemView.findViewById(R.id.remove);
            go = itemView.findViewById(R.id.go);
            cv = itemView.findViewById(R.id.section);
            mySectionTv = itemView.findViewById(R.id.section_name);
           // parentLayout = itemView.findViewById(android.R.id.content);
        }
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}

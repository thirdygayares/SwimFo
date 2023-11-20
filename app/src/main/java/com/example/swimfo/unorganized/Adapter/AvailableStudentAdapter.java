package com.example.swimfo.unorganized.Adapter;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.unorganized.Model.StudentModel;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.EditStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableStudentAdapter extends RecyclerView.Adapter<AvailableStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentModel> list;


    FirebaseDatabase rootNode;
    DatabaseReference studentRef,adminRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public AvailableStudentAdapter(Context context, ArrayList<StudentModel>list) {
        this.context = context;
        this.list = list;
    }

   
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        StudentModel studentModel = list.get(position);
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();


        holder.tempPassTextView.setText(studentModel.getTempPassword());

        holder.copyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(studentModel.getTempPassword());
            }
        });


        rootNode = FirebaseDatabase.getInstance();

        holder.myStudentTv.setText(studentModel.getName());
      
        holder.remove.setOnClickListener(v -> {
            //alert dialog

            //alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //set title
            builder.setTitle("Removing");
            //set message
            builder.setMessage("Are you sure you want to remove this student?");
            //positive yes
            builder.setPositiveButton("YES", (dialog, which) -> {
                //remove section subject
                String idNumberToRemove = list.get(position).getUid(); // The ID number you want to match

                DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("studentlist");

// Query for the matching records based on the ID number
                Query query = studentsRef.orderByChild("uid").equalTo(idNumberToRemove);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Iterate through the matching records and remove them
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                dataSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            // Record removed successfully
                                            Toast.makeText(context.getApplicationContext(), "Record removed successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Failed to remove the record
                                            Toast.makeText(context.getApplicationContext(), "Failed to remove the record", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // No matching records found
                            Toast.makeText(context.getApplicationContext(), "No matching records found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError error) {
                        // Handle any database error
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
            Intent intent = new Intent(context, EditStudent.class);
            intent.putExtra("section", list.get(position).getSection());
            intent.putExtra("id", list.get(position).getSectionId());
            intent.putExtra("name", list.get(position).getName());
            intent.putExtra("uid", list.get(position).getUid());
            intent.putExtra("img", list.get(position).getImgUrl());
            intent.putExtra("email", list.get(position).getEmailId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setText(text);
            Toast.makeText(context, "text has been copied", Toast.LENGTH_SHORT).show();
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView edit,remove;
        CardView cv;
        TextView myStudentTv;
        TextView tempPassTextView, copyTextView;
     //   View parentLayout;
        public MyViewHolder( View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            remove = itemView.findViewById(R.id.remove);
          
            cv = itemView.findViewById(R.id.student);
            myStudentTv = itemView.findViewById(R.id.student_name);
           // parentLayout = itemView.findViewById(android.R.id.content);
            tempPassTextView = itemView.findViewById(R.id.tempPass);
            copyTextView = itemView.findViewById(R.id.copy);
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

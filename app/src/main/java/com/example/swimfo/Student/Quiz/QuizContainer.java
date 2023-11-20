package com.example.swimfo.Student.Quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.Finish;
import com.example.swimfo.unorganized.Model.LeaderBoardModel;
import com.example.swimfo.Modelclass;
import com.example.swimfo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class QuizContainer extends AppCompatActivity {
    ArrayList<Modelclass> list;


    FirebaseAuth fAuth;
    int count = 1;

    FirebaseDatabase db;
    DatabaseReference databaseReference;

    List<Modelclass> allQuestionsList;
    Modelclass modelClass;
    int index=0;
    CardView cardOA,cardOB,cardOC, cardOD;
    TextView card_question,optiona,optionb,optionc,optiond;
    int correctCount= 0;
    int wrongCount= 0;
    LinearLayout nxtBtn;

    ProgressBar loading;

    String path = "", name = "";
    TextView nTv;
    CircularTimerView progressBar;
    TextView gscore;


    TextView nxt;
    int missed = 0;
    int wrongy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_container);

        //getintent
//        Intent route = getIntent();
//        path = route.getStringExtra("quiznum");

        list=new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("NewQuiz/1");

        card_question=findViewById(R.id.card_question);
        optiona=findViewById(R.id.card_optiona);
        optionb=findViewById(R.id.card_optionb);
        optionc=findViewById(R.id.card_optionc);
        optiond=findViewById(R.id.card_optiond);
        nTv = findViewById(R.id.username);
        gscore = findViewById(R.id.score);

        nTv.setText(name);

        cardOA=findViewById(R.id.cardOA);
        cardOB=findViewById(R.id.cardOB);
        cardOC=findViewById(R.id.cardOC);
        cardOD=findViewById(R.id.cardOD);

        cardOA.setEnabled(false);
        cardOB.setEnabled(false);
        cardOC.setEnabled(false);
        cardOD.setEnabled(false);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);


        nxt = findViewById(R.id.next);
        nxtBtn=findViewById(R.id.nxtBtn);
       progressBar = findViewById(R.id.progress_circular);
       gscore.setText(String.valueOf(count));
//
//
//
// To Initialize Timer
     progressBar.setCircularTimerListener(new CircularTimerListener() {
        @Override
        public String updateDataOnTick(long remainingTimeInMs) {
            return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
        }

        @Override
        public void onTimerFinished() {
//            Toast.makeText(QuizContainer.this, "FINISHED", Toast.LENGTH_SHORT).show();
            progressBar.setPrefix("");
            progressBar.setSuffix("");
            progressBar.setText("FINISHED THANKS!");
            missed++;

            index++;
            gscore.setText(String.valueOf(count));

            if(index != list.size()){
                modelClass=list.get(index);
                resetColor();
                setAllData();
                enableButton();
            }else {
                GameWon();
            }
        }
    }, 30, TimeFormatEnum.SECONDS, 10);


//
//// To start timer
//





        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Modelclass> dataList = new ArrayList<>();
            @Override
            public void onDataChange( @NotNull DataSnapshot snapshot) {
                list.clear();
                if (!snapshot.exists()){
                    Toast.makeText(QuizContainer.this, "No quiz for the meantime.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    dataList.add(dataSnapshot.getValue(Modelclass.class));
                }

                //  Toast.makeText(QuizContainer.this, String.valueOf(dataList.size()), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);

                cardOA.setEnabled(true);
                cardOB.setEnabled(true);
                cardOC.setEnabled(true);
                cardOD.setEnabled(true);


                sendlist(dataList);

                cardOA.setBackgroundColor(getResources().getColor(R.color.blue_light));
                cardOB.setBackgroundColor(getResources().getColor(R.color.blue_light));
                cardOC.setBackgroundColor(getResources().getColor(R.color.blue_light));
                cardOD.setBackgroundColor(getResources().getColor(R.color.blue_light));

                nxtBtn.setClickable(false);
                nxtBtn.setEnabled(false);


            }

            @Override
            public void onCancelled( @NotNull DatabaseError databaseError) {

            }
        });

    }
    private void sendlist(ArrayList<Modelclass> dataLists) {
        allQuestionsList = dataLists;
        Collections.shuffle(allQuestionsList);
        modelClass = dataLists.get(index);
        for(int i = 0; i < dataLists.size(); i++){
            list.add(dataLists.get(i));
        }
        setAllData();
    }
    private void setAllData() {
        nxtBtn.setEnabled(false);
        nxtBtn.setClickable(false);
        card_question.setText(modelClass.getQuestion());
        optiona.setText(modelClass.getC1());
        optionb.setText(modelClass.getC2());
        optionc.setText(modelClass.getC3());
        optiond.setText(modelClass.getC4());
        progressBar.startTimer();

    }
    public void Correct(CardView cardView){

        cardView.setBackgroundColor(getResources().getColor(R.color.green));
        Toast.makeText(this, "Correct!!", Toast.LENGTH_SHORT).show();

        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctCount++;
                index++;
                count++;
                gscore.setText(String.valueOf(count));

                if(index != list.size()){
                    modelClass=list.get(index);
                    resetColor();
                    setAllData();
                    enableButton();
                }else {
                    GameWon();
                }
            }
        });
    }
    private void GameWon() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String name = user.getDisplayName();


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = db.getReference("studentlist");

        ref1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   String secId = snapshot.child("sectionId").getValue(String.class);
                   saveToLeaderBoard(uid,name,secId);
                } else {
                    // Handle the case where the user data doesn't exist
                    Toast.makeText(QuizContainer.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any database errors here
            }
        });




    }

    private void saveToLeaderBoard(String uid, String name, String secId) {
        Toast.makeText(this, secId, Toast.LENGTH_SHORT).show();

        // Get the current date and time
        Date currentDate = new Date();

        // Define a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Format the current date as a string
        String formattedDate = dateFormat.format(currentDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("LeaderBoard");

        LeaderBoardModel leaderBoardModel = new LeaderBoardModel(name, secId, String.valueOf(correctCount), formattedDate);

        ref.child("1").child(uid).setValue(leaderBoardModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(QuizContainer.this, Finish.class);
                intent.putExtra("SCORE",String.valueOf(correctCount));
                intent.putExtra("WSCORE",String.valueOf(wrongy));
                intent.putExtra("MSCORE",String.valueOf(missed));
                startActivity(intent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(QuizContainer.this, "Some Error Occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void enableButton(){
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    public void disableButton(){
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }

    public void resetColor(){
        cardOA.setBackgroundColor(getResources().getColor(R.color.blue_light));
        cardOB.setBackgroundColor(getResources().getColor(R.color.blue_light));
        cardOC.setBackgroundColor(getResources().getColor(R.color.blue_light));
        cardOD.setBackgroundColor(getResources().getColor(R.color.blue_light));
    }

    public void OptionAClick(View view){
        disableButton();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getC1().equals(modelClass.getAns())){
            cardOA.setCardBackgroundColor(getResources().getColor(R.color.green));

            if(index < list.size()){
                Correct(cardOA);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOA);
        }
    }

    public void OptionBClick(View view){
        disableButton();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getC2().equals(modelClass.getAns())){
            cardOB.setCardBackgroundColor(getResources().getColor(R.color.green));
            if(index < list.size()){
                Correct(cardOB);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOB);
        }
    }


    public void OptionCClick(View view){
        disableButton();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getC3().equals(modelClass.getAns())){
            cardOC.setCardBackgroundColor(getResources().getColor(R.color.green));
            if(index < list.size()){
                Correct(cardOC);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOC);
        }
    }
    public void OptionDClick(View view){
        disableButton();
        nxtBtn.setClickable(true);
        nxtBtn.setEnabled(true);
        if(modelClass.getC4().equals(modelClass.getAns())){
            cardOD.setCardBackgroundColor(getResources().getColor(R.color.green));


            if(index < list.size()){
                Correct(cardOD);
            }else {
                GameWon();
            }
        }else {
            Wrong(cardOD);
        }
    }
    public void Wrong(CardView cardOA){
        wrongy++;

        cardOA.setBackgroundColor(getResources().getColor(R.color.red));
        Toast.makeText(this, "Wrong!!", Toast.LENGTH_SHORT).show();

        nxtBtn.setOnClickListener(v -> {
            wrongCount++;
            index++;

            if(index != list.size()){
                modelClass=list.get(index);
                resetColor();
                setAllData();
                enableButton();
            }else {
                GameWon();
            }


        });


    }
}
package com.example.swimfo.Student.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Student.model.QuizStudentModel;
import com.example.swimfo.Teacher.model.QuestionQuizModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuestionQuizListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    Context context;
    List<QuestionQuizModel> quizStudentModel;
    private int selectedPosition = -1; // -1 indicates nothing is selected
    public QuestionQuizListAdapter(Context context, List<QuestionQuizModel> quizStudentModel) {
        this.context = context;
        this.quizStudentModel = quizStudentModel;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.item_student_quiz_multiple_choice:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_quiz_multiple_choice, parent, false);
                return new MultipleChoiceViewHolder(view);
            case R.layout.item_student_quiz_identification:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_quiz_identification, parent, false);
                return new IdentificationViewHolder(view);
            default:
                // Handle default case
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestionQuizModel question = quizStudentModel.get(position);

        switch (holder.getItemViewType()) {
            case R.layout.item_student_quiz_multiple_choice:
                MultipleChoiceViewHolder mcHolder = (MultipleChoiceViewHolder) holder;
                mcHolder.txtQuestion.setText(question.getQuestionText());
                mcHolder.txtQuestionNumber.setText("Question " + (position + 1));

                mcHolder.btnAnswer1.setText(question.getOptions().get(0).toString());
                mcHolder.btnAnswer2.setText(question.getOptions().get(1).toString());
                mcHolder.btnAnswer3.setText(question.getOptions().get(2).toString());
                mcHolder.btnAnswer4.setText(question.getOptions().get(3).toString());

                // Set the click listener for each button
                mcHolder.btnAnswer1.setOnClickListener(v -> {
                    resetButtonColors(mcHolder);
                    mcHolder.btnAnswer1.setBackgroundColor(Color.BLUE); // Change color to blue to indicate selection
                    // Do something with the answer\
                    quizStudentModel.get(position).setAnswer(mcHolder.btnAnswer1.getText().toString());
                });

                mcHolder.btnAnswer2.setOnClickListener(v -> {
                    resetButtonColors(mcHolder);
                    mcHolder.btnAnswer2.setBackgroundColor(Color.BLUE);
                    // Do something with the answer
                    quizStudentModel.get(position).setAnswer(mcHolder.btnAnswer2.getText().toString());

                });

                mcHolder.btnAnswer3.setOnClickListener(v -> {
                    resetButtonColors(mcHolder);
                    mcHolder.btnAnswer3.setBackgroundColor(Color.BLUE);
                    // Do something with the answer
                    quizStudentModel.get(position).setAnswer(mcHolder.btnAnswer3.getText().toString());

                });

                mcHolder.btnAnswer4.setOnClickListener(v -> {
                    resetButtonColors(mcHolder);
                    mcHolder.btnAnswer4.setBackgroundColor(Color.BLUE);
                    // Do something with the answer
                    quizStudentModel.get(position).setAnswer(mcHolder.btnAnswer4.getText().toString());

                });

                //mcHolder.btnCorrectAnswer.setText(question.getAnswer());

                break;

            case R.layout.item_student_quiz_identification:
                IdentificationViewHolder idHolder = (IdentificationViewHolder) holder;
                idHolder.edtQuestion.setText(question.getQuestionText());
                //set question number
                question.setQuestionNumber(position + 1);
                idHolder.txtQuestionNumber.setText("Question " + (position + 1));
                idHolder.edtAnswer.setAllCaps(true);

                idHolder.edtAnswer.setText(question.getAnswer());
                //UpdateAnswer(idHolder.edtAnswer, position);

                idHolder.btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (idHolder.edtAnswer.getText().toString().isEmpty()){
                            Toast.makeText(context, "Please enter your answer", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String answer = idHolder.edtAnswer.getText().toString().toUpperCase();
                        idHolder.edtAnswer.setText(answer);
                        idHolder.edtAnswer.setEnabled(false);

                        int currentPosition = holder.getAdapterPosition();
                        quizStudentModel.get(currentPosition).setAnswer(idHolder.edtAnswer.getText().toString());
                    }
                });

                break;
        }
    }


    // First, reset all button colors to white
    // First, ensure that resetButtonColors accepts the correct ViewHolder type
    private void resetButtonColors(MultipleChoiceViewHolder mcHolder) {
        mcHolder.btnAnswer1.setBackgroundColor(Color.WHITE);
        mcHolder.btnAnswer2.setBackgroundColor(Color.WHITE);
        mcHolder.btnAnswer3.setBackgroundColor(Color.WHITE);
        mcHolder.btnAnswer4.setBackgroundColor(Color.WHITE);
    }

    public void UpdateAnswer(EditText editText, int position){
        // Remove existing TextWatcher if any
        editText.removeTextChangedListener((TextWatcher) editText.getTag());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Optional
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Optional
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("RESULT", s.toString() + " " + position);
                Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
                quizStudentModel.get(position).setAnswer(s.toString());
                // Optionally, notify the adapter if necessary
            }
        };

        // Store the TextWatcher in the EditText's tag so it can be retrieved later
        editText.setTag(textWatcher);

        // Add the TextWatcher to EditText
        editText.addTextChangedListener(textWatcher);
    }




    @Override
    public int getItemViewType(int position) {
        QuestionQuizModel question = quizStudentModel.get(position);
        switch (question.getType()) {
            case MULTIPLE_CHOICE:
                return R.layout.item_student_quiz_multiple_choice;
            case IDENTIFICATION:
                return R.layout.item_student_quiz_identification;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return quizStudentModel.size(); // Return the total number of items in the list
    }


    class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestionNumber, txtQuestion;

        MaterialButton btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;

        public MultipleChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestionNumber = itemView.findViewById(R.id.txtQuestionNumber);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            btnAnswer1 = itemView.findViewById(R.id.btnAnswer1);
            btnAnswer2 = itemView.findViewById(R.id.btnAnswer2);
            btnAnswer3 = itemView.findViewById(R.id.btnAnswer3);
            btnAnswer4 = itemView.findViewById(R.id.btnAnswer4);






        }
        // UI elements for multiple choice
        // ...
    }

    class IdentificationViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestionNumber;
        EditText edtQuestion, edtAnswer;
        Button btnSubmit;

        public IdentificationViewHolder(@NonNull View itemView) {
            super(itemView);

        txtQuestionNumber = itemView.findViewById(R.id.txtQuestionNumber);
        edtQuestion = itemView.findViewById(R.id.edtQuestion);
        edtAnswer = itemView.findViewById(R.id.edtAnswer);
        btnSubmit = itemView.findViewById(R.id.btnSubmit);

        }
    }





}

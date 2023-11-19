package com.example.swimfo.Teacher.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.model.QuestionQuizModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class QuestionQuizListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    Context context;
    List<QuestionQuizModel> questionQuizModelList;

    public QuestionQuizListAdapter(Context context,List<QuestionQuizModel> questionQuizModelList) {
        this.context = context;
        this.questionQuizModelList = questionQuizModelList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.item_quiz_multiple_choice:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_multiple_choice, parent, false);
                return new MultipleChoiceViewHolder(view);
            case R.layout.item_quiz_identification:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_identification, parent, false);
                return new IdentificationViewHolder(view);
            default:
                // Handle default case
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestionQuizModel question = questionQuizModelList.get(position);
        switch (holder.getItemViewType()) {
            case R.layout.item_quiz_multiple_choice:
                MultipleChoiceViewHolder mcHolder = (MultipleChoiceViewHolder) holder;
                mcHolder.edtQuestion.setText(question.getQuestionText());
                mcHolder.txtQuestionNumber.setText("Question " + (position + 1));

                //mcHolder.btnCorrectAnswer.setText(question.getAnswer());

                question.setQuestionNumber(position + 1);
                UpdateQuestion(mcHolder.edtQuestion, position);
                UpdateOption(mcHolder.edtOption1, position, 0);
                UpdateOption(mcHolder.edtOption2, position, 1);
                UpdateOption(mcHolder.edtOption3, position, 2);
                UpdateOption(mcHolder.edtOption4, position, 3);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.options_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mcHolder.spinner.setAdapter(adapter);

                mcHolder.btnCorrectAnswer.setOnClickListener(view ->{

                        if (mcHolder.edtOption1.getText().toString().isEmpty() || mcHolder.edtOption2.getText().toString().isEmpty() || mcHolder.edtOption3.getText().toString().isEmpty() || mcHolder.edtOption4.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Please fill all the options", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        view.clearFocus(); // Clear focus from the button
                        mcHolder.spinner.performClick();
                        mcHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int xx, long id) {

                                // Handle selection
                                 if (xx == 0) {

                                }
                                else if (xx == 1) {
                                    mcHolder.btnCorrectAnswer.setText(questionQuizModelList.get(holder.getAdapterPosition()).getOptions().get(0));
                                    question.setAnswer(mcHolder.edtOption1.getText().toString());
                                } else if (xx == 2) {
                                    mcHolder.btnCorrectAnswer.setText(questionQuizModelList.get(holder.getAdapterPosition()).getOptions().get(1));
                                    question.setAnswer(mcHolder.edtOption2.getText().toString());
                                } else if (xx == 3) {
                                    mcHolder.btnCorrectAnswer.setText(questionQuizModelList.get(holder.getAdapterPosition()).getOptions().get(2));
                                    question.setAnswer(mcHolder.edtOption3.getText().toString());
                                } else if (xx == 4) {
                                    mcHolder.btnCorrectAnswer.setText(questionQuizModelList.get(holder.getAdapterPosition()).getOptions().get(3));
                                    question.setAnswer(mcHolder.edtOption4.getText().toString());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Handle no selection
                            }
                        });




                });



                break;
            case R.layout.item_quiz_identification:
                IdentificationViewHolder idHolder = (IdentificationViewHolder) holder;
                idHolder.edtQuestion.setText(question.getQuestionText());
                idHolder.edtAnswer.setText(question.getAnswer());
                //set question number
                question.setQuestionNumber(position + 1);
                idHolder.txtQuestionNumber.setText("Question " + (position + 1));

                UpdateQuestion(idHolder.edtQuestion, position);
                UpdateAnswer(idHolder.edtAnswer, position);
                break;
        }
    }


    public void UpdateQuestion(EditText editText, int position){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is optional for this scenario
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is optional for this scenario
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("RESULT", s.toString() + " " + position);
                questionQuizModelList.get(position).setQuestionText(s.toString());
            }
        });
    }

    public void UpdateAnswer(EditText editText, int position){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is optional for this scenario
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is optional for this scenario
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("RESULT", s.toString() + " " + position);
                questionQuizModelList.get(position).setAnswer(s.toString());
            }
        });
    }

    public void UpdateOption(EditText editText, int position, int arrayPosition){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is optional for this scenario
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is optional for this scenario
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("RESULT", s.toString() + " POSTION" + position + arrayPosition);
                questionQuizModelList.get(position).getOptions().set(arrayPosition, s.toString());

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        QuestionQuizModel question = questionQuizModelList.get(position);
        switch (question.getType()) {
            case MULTIPLE_CHOICE:
                return R.layout.item_quiz_multiple_choice;
            case IDENTIFICATION:
                return R.layout.item_quiz_identification;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return questionQuizModelList.size(); // Return the total number of items in the list
    }

    private void setupTextWatcher(EditText editText, RecyclerView.ViewHolder holder, int optionIndex) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Implementation not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    QuestionQuizModel question = questionQuizModelList.get(position);
                    if (optionIndex == -1) { // -1 for question text
                        question.setQuestionText(s.toString());
                    } else { // For option text
                        List<String> options = question.getOptions();
                        if (options.size() > optionIndex) {
                            options.set(optionIndex, s.toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Implementation not needed for this case
            }
        });
    }


    class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestionNumber;
        EditText edtQuestion, edtOption1, edtOption2, edtOption3, edtOption4;
        MaterialButton btnCorrectAnswer;
        Spinner spinner;
        public MultipleChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            edtQuestion = itemView.findViewById(R.id.edtQuestion);
            edtOption1 = itemView.findViewById(R.id.edtOption1);
            edtOption2 = itemView.findViewById(R.id.edtOption2);
            edtOption3 = itemView.findViewById(R.id.edtOption3);
            edtOption4 = itemView.findViewById(R.id.edtOption4);
            txtQuestionNumber = itemView.findViewById(R.id.txtQuestionNumber);
            btnCorrectAnswer = itemView.findViewById(R.id.btnCorrectAnswer);
            spinner = itemView.findViewById(R.id.spinner);




        }
        // UI elements for multiple choice
        // ...
    }

    class IdentificationViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestionNumber;
        EditText edtQuestion, edtAnswer;

        public IdentificationViewHolder(@NonNull View itemView) {
            super(itemView);

        txtQuestionNumber = itemView.findViewById(R.id.txtQuestionNumber);
        edtQuestion = itemView.findViewById(R.id.edtQuestion);
        edtAnswer = itemView.findViewById(R.id.edtAnswer);


        }
    }





}

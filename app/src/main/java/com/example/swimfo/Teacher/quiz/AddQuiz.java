package com.example.swimfo.Teacher.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swimfo.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddQuiz extends Fragment {
    private View view;
    private String date;
    private EditText etQuizTitle;
    private MaterialButton btnNext;

    CalendarView calendarView;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.teacher_quiz_activity_add_quiz, container, false);

        etQuizTitle = view.findViewById(R.id.etQuizTitle);
        calendarView = view.findViewById(R.id.calendarView);
        btnNext = view.findViewById(R.id.btnNext);




        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
        date = dateFormat.format(currentDate.getTime());


        //calendar
        calendar();

        btnNext.setOnClickListener(v -> {
            if (etQuizTitle.getText().toString().isEmpty()) {
                etQuizTitle.setError("Please enter quiz title");
                etQuizTitle.requestFocus();
                return;
            }
           next();
        });

        return view;
    }

    private void next() {
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putString("title", etQuizTitle.getText().toString());
        Fragment fragment = new AddQuestionQuiz();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
    }

    private void calendar() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.getDefault());
                date = dateFormat.format(selectedDate.getTime());
            }
        });

        // Get the instance of Calendar for the current date and time
        Calendar calendar = Calendar.getInstance();
        // Add one day to the current date to get tomorrow's date
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Convert the date to milliseconds
        long tomorrowInMillis = calendar.getTimeInMillis();

        // Set the minimum date of the CalendarView to tomorrow
        calendarView.setMinDate(tomorrowInMillis);
    }


}
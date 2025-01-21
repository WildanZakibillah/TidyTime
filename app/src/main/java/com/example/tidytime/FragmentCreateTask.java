package com.example.tidytime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.DatePicker;
import java.util.Calendar;

public class FragmentCreateTask extends Fragment {
    private EditText addTaskTitle, addTaskDescription, taskDate, taskTime, addAnEvent;
    private Button addTaskButton;
    private DatabaseTask databaseTask;
    private int taskId = -1;

    public FragmentCreateTask() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        addTaskTitle = view.findViewById(R.id.addTaskTitle);
        addTaskDescription = view.findViewById(R.id.addTaskDescription);
        taskDate = view.findViewById(R.id.taskDate);
        taskTime = view.findViewById(R.id.taskTime);
        addAnEvent = view.findViewById(R.id.taskEvent);
        addTaskButton = view.findViewById(R.id.addTask);
        databaseTask = new DatabaseTask(getContext());

        // Jika sedang mengedit tugas, isi field dengan data yang ada
        if (getArguments() != null) {
            taskId = getArguments().getInt("task_id", -1);
            addTaskTitle.setText(getArguments().getString("task_name", ""));
            addTaskDescription.setText(getArguments().getString("task_description", ""));
            taskDate.setText(getArguments().getString("task_date", ""));
            taskTime.setText(getArguments().getString("task_time", ""));
            addAnEvent.setText(getArguments().getString("add_an_event", ""));
            addTaskButton.setText("Update Task");
        }

        // Menampilkan TimePicker
        taskTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    (TimePicker view1, int selectedHour, int selectedMinute) ->
                            taskTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute)),
                    hour, minute, true);
            timePickerDialog.show();
        });

        // Menampilkan DatePicker
        taskDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (DatePicker view1, int selectedYear, int selectedMonth, int selectedDayOfMonth) ->
                            taskDate.setText(String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)),
                    year, month, dayOfMonth);
            datePickerDialog.show();
        });

        addTaskButton.setOnClickListener(v -> {
            String title = addTaskTitle.getText().toString().trim();
            String description = addTaskDescription.getText().toString().trim();
            String date = taskDate.getText().toString().trim();
            String time = taskTime.getText().toString().trim();
            String event = addAnEvent.getText().toString().trim();

            if (!title.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                if (taskId == -1) {
                    databaseTask.addTask(title, description, date, time, event);
                } else {
                    databaseTask.updateTask(taskId, title, description, date, time, event);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}

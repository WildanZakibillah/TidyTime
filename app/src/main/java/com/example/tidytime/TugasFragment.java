package com.example.tidytime;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class TugasFragment extends Fragment {
    private RecyclerView taskRecycler;
    private TextView addTask;
    private DatabaseTask databaseTask;
    private List<Task> taskList;
    private TaskAdapter adapter;

    public TugasFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tugas, container, false);

        taskRecycler = view.findViewById(R.id.taskRecycler);
        addTask = view.findViewById(R.id.addTask);
        databaseTask = new DatabaseTask(getContext());

        taskRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTasks();

        addTask.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentCreateTask())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }


    private void loadTasks() {
        taskList = new ArrayList<>();
        Cursor cursor = databaseTask.getAllTasks();
        while (cursor.moveToNext()) {
            taskList.add(new Task(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5)
            ));
        }
        cursor.close();
        adapter = new TaskAdapter(getContext(), taskList, new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onEdit(Task task) {
                FragmentCreateTask fragment = new FragmentCreateTask();
                Bundle args = new Bundle();
                args.putInt("task_id", task.getId());
                args.putString("task_name", task.getTaskName());
                args.putString("task_date", task.getTaskDate());
                args.putString("task_description", task.getTaskDescription());
                args.putString("task_time", task.getTaskTime());
                args.putString("task_event", task.getAdditionalEvent());
                fragment.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDelete(int id) {
                databaseTask.deleteTask(id);
                loadTasks();
            }
        });
        taskRecycler.setAdapter(adapter);
    }
}

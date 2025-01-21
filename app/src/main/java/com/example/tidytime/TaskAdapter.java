package com.example.tidytime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private OnTaskActionListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "TASK_REMINDER_CHANNEL";
    private boolean isRingtonePlaying = false;

    public interface OnTaskActionListener {
        void onEdit(Task task);
        void onDelete(int id);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskActionListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
        checkTaskTimes();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        String taskDate = (task.getTaskDate() != null) ? task.getTaskDate() : "No Date";
        String taskTitle = (task.getTaskName() != null) ? task.getTaskName() : "No Title";
        String taskDescription = (task.getTaskDescription() != null) ? task.getTaskDescription() : "No Description";
        String taskTime = (task.getTaskTime() != null) ? task.getTaskTime() : "No Time";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());

        try {
            Date date = inputFormat.parse(taskDate);
            holder.dayTextView.setText(dayFormat.format(date));
            holder.dateTextView.setText(dateFormat.format(date));
            holder.monthTextView.setText(monthFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.titleTextView.setText(taskTitle);
        holder.descriptionTextView.setText(taskDescription);
        holder.timeTextView.setText(taskTime);

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.task_menu);
            popupMenu.setOnMenuItemClickListener(item -> handleMenuClick(item, task));
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void checkTaskTimes() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Task task : taskList) {
                    checkIfTaskTimeReached(task);
                }
                handler.postDelayed(this, 60000); // repeat every 1 minute
            }
        }, 60000);
    }

    private void checkIfTaskTimeReached(Task task) {
        try {
            String taskDateTime = task.getTaskDate() + " " + task.getTaskTime();
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date taskDate = dateTimeFormat.parse(taskDateTime);
            Date currentDate = new Date();

            if (taskDate != null && taskDate.before(currentDate) && !isRingtonePlaying) {
                playRingtone(task);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void playRingtone(Task task) {
        if (!isRingtonePlaying) {
            isRingtonePlaying = true;
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean("isPlaying", true)
                    .apply();

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI);
                mediaPlayer.setLooping(true);
            }

            mediaPlayer.start();
            showNotification(task);
        }
    }

    public static void stopRingtone() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showNotification(Task task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Task Reminder Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Intent stopIntent = new Intent(context, StopRingtoneReceiver.class);
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(
                context,
                0,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Task Time Reached!")
                .setContentText("Task: " + task.getTaskName() + " is now due.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .addAction(android.R.drawable.ic_media_pause, "Stop Sound", pendingStopIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(), notification);
    }

    private boolean handleMenuClick(MenuItem item, Task task) {
        int itemId = item.getItemId();
        if (itemId == R.id.edit_task) {
            listener.onEdit(task); // Memanggil listener untuk aksi edit
            return true;
        } else if (itemId == R.id.delete_task) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete", (dialog, which) -> listener.onDelete(task.getId()))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }
        return false;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, dateTextView, monthTextView, titleTextView, descriptionTextView, timeTextView;
        View options;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day);
            dateTextView = itemView.findViewById(R.id.date);
            monthTextView = itemView.findViewById(R.id.month);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);
            timeTextView = itemView.findViewById(R.id.time);
            options = itemView.findViewById(R.id.options);
        }
    }
}

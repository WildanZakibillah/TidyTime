package com.example.tidytime;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Initialize the Switch and NotificationManager
        switchNotifications = findViewById(R.id.switch_notifications);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if notifications are enabled and set the Switch accordingly
        boolean areNotificationsEnabled = areNotificationsEnabled();
        switchNotifications.setChecked(areNotificationsEnabled);

        // Set listener for Switch state change
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableNotifications();
                } else {
                    disableNotifications();
                }
            }
        });
    }

    // Check if notifications are enabled (for Android 8.0 and above)
    private boolean areNotificationsEnabled() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            return notificationManager.areNotificationsEnabled();
        }
        return true; // For older Android versions, assume notifications are enabled
    }

    // Enable notifications
    private void enableNotifications() {
        // You can implement the logic to enable notifications here
        Toast.makeText(this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
    }

    // Disable notifications
    private void disableNotifications() {
        // You can implement the logic to disable notifications here
        Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
    }
}

package com.example.tidytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        // Logout button logic with confirmation
        Button logoutButton = rootView.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Hapus status login dari SharedPreferences
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userPrefs", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("isLoggedIn");
                        editor.remove("email");
                        editor.apply();

                        // Tampilkan pesan logout berhasil
                        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                        // Arahkan ke LoginActivity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish(); // Tutup SettingFragment dan kembali ke LoginActivity
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Exit button logic with confirmation
        Button exitButton = rootView.findViewById(R.id.btn_exit);
        exitButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit the app?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Keluar aplikasi
                        getActivity().finishAffinity();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Navigasi ke ChangePasswordActivity
        TextView btnChangePassword = rootView.findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Navigasi ke EditProfileActivity
        TextView btnEditProfile = rootView.findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Navigasi ke NotificationsActivity
        TextView btnNotifications = rootView.findViewById(R.id.btn_notifications);
        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationsActivity.class);
            startActivity(intent);
        });

        // Navigasi ke PrivacyActivity
        TextView btnPrivacy = rootView.findViewById(R.id.btn_privacy);
        btnPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        });

        // Navigasi ke InfoActivity
        TextView btnInfo = rootView.findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            startActivity(intent);
        });

        // Navigasi ke HelpActivity
        TextView btnHelp = rootView.findViewById(R.id.btn_help);
        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
        });

        return rootView;
    }
}

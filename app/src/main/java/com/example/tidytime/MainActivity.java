package com.example.tidytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tidytime.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Memeriksa status login
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Mengecek status login

        // Jika belum login, arahkan ke halaman login
        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Tutup MainActivity setelah mengarahkan ke LoginActivity
            return;
        }

        // Jika sudah login, lanjutkan dengan aktivitas normal
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Replace fragment default ketika activity pertama kali dijalankan
        replaceFragment(new TugasFragment());

        // Set listener untuk item BottomNavigationView yang dipilih
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Ganti fragment berdasarkan item yang dipilih
            if (itemId == R.id.tugas) {
                replaceFragment(new TugasFragment());
            } else if (itemId == R.id.kalender) {
                replaceFragment(new KalenderFragment());
            } else if (itemId == R.id.setting) {
                replaceFragment(new SettingFragment());
            }

            return true;
        });
    }

    // Helper method untuk mengganti fragment
    private void replaceFragment(Fragment fragment) {
        // Ganti fragment dalam FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Opsional: Tambahkan fragment ke back stack
        transaction.commit();
    }
}

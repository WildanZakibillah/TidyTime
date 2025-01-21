package com.example.tidytime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // request code for picking an image

    private ImageView profilePicture;
    private EditText etUsername, etEmail;
    private Button btnSaveChanges;
    private Uri imageUri; // Uri to store the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        profilePicture = findViewById(R.id.profile_picture);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        // Set OnClickListener for the "Save Changes" button
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user inputs from the EditTexts
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                // Validate inputs
                if (username.isEmpty() || email.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Here you can save changes to the database or shared preferences (simulating with Toast for now)
                // If you have a DatabaseHelper, you can call a method to save the data

                // For example, if you have a method saveProfileData(username, email, imageUri):
                // boolean isSaved = saveProfileData(username, email, imageUri);

                // Simulating successful save
                boolean isSaved = true;

                if (isSaved) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // You can add a logic to navigate to another activity, like HomeActivity
                    Intent intent = new Intent(EditProfileActivity.this, SettingFragment.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set OnClickListener for the profile picture ImageView
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    // Open the gallery to pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the gallery image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Get the image URI

            try {
                // Set the selected image to the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // You can define methods to save profile data, for example:
    /*
    private boolean saveProfileData(String username, String email, Uri imageUri) {
        // Logic to save the profile data to a database or SharedPreferences
        // For example, saving data in SharedPreferences or a database
        return true;
    }
    */
}

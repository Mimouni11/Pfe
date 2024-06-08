package com.example.pfemini.UI.Mecano;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfemini.Network.Apiservices;
import com.example.pfemini.R;
import com.example.pfemini.Network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rapport_activity extends AppCompatActivity {
    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private LinearLayout notesContainer;
    private List<Note> noteList;

    private EditText vehicleIdEditText;
    private EditText issueDescriptionEditText;
    private EditText workDescriptionEditText;
    private EditText signatureEditText;
    private ImageView selectedImageView;
    private Button uploadButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        notesContainer = findViewById(R.id.notesContainer);
        saveButton = findViewById(R.id.saveButton);
        uploadButton = findViewById(R.id.uploadButton);

        vehicleIdEditText = findViewById(R.id.vehicleIdEditText);
        issueDescriptionEditText = findViewById(R.id.issueDescriptionEditText);
        workDescriptionEditText = findViewById(R.id.workDescriptionEditText);
        signatureEditText = findViewById(R.id.signatureEditText);
        noteList = new ArrayList<>();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Rapport_activity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Rapport_activity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    openImageChooser();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all required fields are not empty
                String vehicleId = vehicleIdEditText.getText().toString().trim();
                String issueDescription = issueDescriptionEditText.getText().toString().trim();
                String workDescription = workDescriptionEditText.getText().toString().trim();
                String signature = signatureEditText.getText().toString().trim();

                if (vehicleId.isEmpty() || issueDescription.isEmpty() || workDescription.isEmpty() || signature.isEmpty()) {
                    Toast.makeText(Rapport_activity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If all fields are filled, save the report
                saveReport(vehicleId, issueDescription, workDescription, signature);
                saveNote();
                sendNotification(vehicleId, issueDescription);
            }
        });

        loadNotesFromPreferences();
        displayNotes();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
        }
    }

    private void saveReport(String vehicleId, String issueDescription, String workDescription, String signature) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Create ApiService instance
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiService.saveReport(vehicleId, issueDescription, workDescription, signature, username);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Rapport_activity.this, "Report saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(Rapport_activity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Rapport_activity.this, "Failed to save report: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayNotes() {
        for (Note note : noteList) {
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i = 0; i < noteCount; i++) {
            String title = sharedPreferences.getString("note_title_" + i, "");
            String content = sharedPreferences.getString("note_content_" + i, "");
            String workDescription = sharedPreferences.getString("note_work_description_" + i, "");
            String signature = sharedPreferences.getString("note_signature_" + i, "");

            Note note = new Note();
            note.setMatricule(title);
            note.setContent(content);
            note.setWorkDescription(workDescription);
            note.setSignature(signature);

            noteList.add(note);
        }
    }


    private void sendNotification(String vehicleId, String issueDescription) {
        String chefUsername = "kaka";
        String title = "New report for " + vehicleId;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String mecano = sharedPreferences.getString("username", "");

        // Call the endpoint to send notification
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiService.sendNotification(chefUsername, title, issueDescription, mecano);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Rapport_activity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Rapport_activity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Rapport_activity.this, "Failed to send notification: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNote() {
        String title = vehicleIdEditText.getText().toString().trim();
        String content = issueDescriptionEditText.getText().toString().trim();
        String workDescription = workDescriptionEditText.getText().toString().trim();
        String signature = signatureEditText.getText().toString().trim();

        if (!title.isEmpty() && !content.isEmpty() && !workDescription.isEmpty() && !signature.isEmpty()) {
            Note note = new Note();
            note.setMatricule(title);
            note.setContent(content);
            note.setWorkDescription(workDescription);
            note.setSignature(signature);

            noteList.add(note);
            saveNotesToPreferences();

            createNoteView(note);
            clearInputFields();
        }
    }

    private void clearInputFields() {
        vehicleIdEditText.getText().clear();
        issueDescriptionEditText.getText().clear();
        workDescriptionEditText.getText().clear();
        signatureEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);
        TextView workDescriptionTextView = noteView.findViewById(R.id.workDescriptionTextView);
        TextView signatureTextView = noteView.findViewById(R.id.signatureTextView);

        titleTextView.setText(note.getMatricule());
        contentTextView.setText(note.getContent());
        workDescriptionTextView.setText(note.getWorkDescription());
        signatureTextView.setText(note.getSignature());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }
    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this note.");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteAndRefresh(note);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note) {
        noteList.remove(note);
        saveNotesToPreferences();
        refreshNoteViews();
    }

    private void refreshNoteViews() {
        notesContainer.removeAllViews();
        displayNotes();
    }

    private void saveNotesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, noteList.size());
        for (int i = 0; i < noteList.size(); i++) {
            Note note = noteList.get(i);
            editor.putString("note_title_" + i, note.getMatricule());
            editor.putString("note_content_" + i, note.getContent());
            editor.putString("note_work_description_" + i, note.getWorkDescription());
            editor.putString("note_signature_" + i, note.getSignature());
        }
        editor.apply();
    }
}

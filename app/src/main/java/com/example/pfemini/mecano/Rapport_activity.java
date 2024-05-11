package com.example.pfemini.mecano;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfemini.Apiservices;
import com.example.pfemini.R;
import com.example.pfemini.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Rapport_activity extends AppCompatActivity {
    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private LinearLayout notesContainer;
    private List<com.example.test2.Note> noteList;

    private EditText titleEditText;
    private EditText contentEditText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        noteList = new ArrayList<>();




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if title and content are not empty
                String title = titleEditText.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(Rapport_activity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If title and content are not empty, then save note and report
                saveReport();
                saveNote();
                sendNotification(content,title);
            }
        });


        loadNotesFromPreferences();
        displayNotes();
    }



    private void saveReport() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Check if title and content are not empty
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(Rapport_activity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Retrofit instance


        // Create ApiService instance
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiService.saveReport(title, content, username);
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
        for (com.example.test2.Note note : noteList) {
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i = 0; i < noteCount; i++) {
            String title = sharedPreferences.getString("note_title_" + i, "");
            String content = sharedPreferences.getString("note_content_" + i, "");

            com.example.test2.Note note = new com.example.test2.Note();
            note.setMatricule(title);
            note.setContent(content);

            noteList.add(note);
        }
    }


    private void sendNotification(String message, String t) {
        // Hardcode the chef's username for now
        String chefUsername = "kaka";
        String title ="New report for "+" "+t;
        Log.d("title",title);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String mecano = sharedPreferences.getString("username", "");
        Log.d("mecano",mecano);
        // Call the endpoint to send notification
        Apiservices apiService = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiService.sendNotification(chefUsername,title,message,mecano);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Notification sent successfully
                    Toast.makeText(Rapport_activity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed to send notification
                    Toast.makeText(Rapport_activity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Toast.makeText(Rapport_activity.this, "Failed to send notification: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (!title.isEmpty() && !content.isEmpty()) {
            com.example.test2.Note note = new com.example.test2.Note();
            note.setMatricule(title);
            note.setContent(content);

            noteList.add(note);
            saveNotesToPreferences();

            createNoteView(note);
            clearInputFields();
        }
    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final com.example.test2.Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item,null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);

        titleTextView.setText(note.getMatricule());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final com.example.test2.Note note) {

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

    private void deleteNoteAndRefresh(com.example.test2.Note note) {
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
        for (int i = 0; i < noteList.size(); i ++) {
            com.example.test2.Note note = noteList.get(i);
            editor.putString("note_title_" + i, note.getMatricule());
            editor.putString("note_content_" + i, note.getContent());
        }
        editor.apply();
    }
}
package com.example.pfemini.mecano;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pfemini.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class Rapport_activity extends AppCompatActivity {

    private static final String TAG = "RapportActivity";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    EditText reportEditText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapport);

        reportEditText = findViewById(R.id.reportEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    saveReportAsPDF();
                } else {
                    Log.d(TAG, "Storage permission not granted. Requesting permission...");
                    requestStoragePermission();
                }
            }
        });
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Storage permission granted. Saving report as PDF...");
                saveReportAsPDF();
            } else {
                Log.d(TAG, "Storage permission denied by the user.");
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveReportAsPDF() {
        String reportContent = reportEditText.getText().toString().trim();

        if (!reportContent.isEmpty()) {
            try {
                // Create document
                Document document = new Document();
                String filePath = new File(getExternalFilesDir(null), "report.pdf").getAbsolutePath();
                Log.d(TAG, "PDF will be saved to: " + filePath);

                // Create instance of PDFWriter class
                PdfWriter.getInstance(document, new FileOutputStream(filePath));

                // Open the document
                document.open();

                // Add content to the document
                document.add(new Paragraph(reportContent));

                // Close document
                document.close();

                // Show success message
                Toast.makeText(this, "Report saved as PDF", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Failed to save report as PDF", e);
                Toast.makeText(this, "Failed to save report as PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please write your report before saving", Toast.LENGTH_SHORT).show();
        }
    }
}

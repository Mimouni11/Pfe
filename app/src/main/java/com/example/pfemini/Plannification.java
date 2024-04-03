package com.example.pfemini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Plannification extends AppCompatActivity {

    private LinearLayout editTextContainer;
    private Button addButton;
    private Button doneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannification);

        editTextContainer = findViewById(R.id.editTextContainer);
        addButton = findViewById(R.id.addButton);
        doneButton = findViewById(R.id.doneButton); // Initialize doneButton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditText();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToNextActivity();
            }
        });



    }

    private void sendDataToNextActivity() {
        Intent intent = new Intent(Plannification.this, stops.class);

        int childCount = editTextContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = editTextContainer.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String editTextValue = editText.getText().toString();
                intent.putExtra("editTextValue" + i, editTextValue);
            }
        }

        intent.putExtra("childCount", childCount); // Pass the total number of EditText fields
        startActivity(intent);
    }



    private void addEditText() {
        EditText newEditText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 0); // Set margins to create space between EditText fields
        newEditText.setLayoutParams(params);
        newEditText.setHint("Stop added");
        editTextContainer.addView(newEditText);
    }
}
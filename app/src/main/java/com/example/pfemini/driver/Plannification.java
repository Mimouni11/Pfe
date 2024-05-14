package com.example.pfemini.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.pfemini.Apiservices;
import com.example.pfemini.R;
import com.example.pfemini.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Plannification extends AppCompatActivity {

    private LinearLayout editTextContainer;
    private Button addButton;
    private Button doneButton;
    private LocationListener locationListener;
    private LocationManager locationManager; // Declare locationManager here
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

        // Retrieve the value from the default EditText (first EditText)
        EditText defaultEditText = findViewById(R.id.defaultEditText);
        String defaultEditTextValue = defaultEditText.getText().toString();

        List<String> addresses = new ArrayList<>();
        StringBuilder addressesString = new StringBuilder(); // StringBuilder to construct the string

        // Append the value from the default EditText to addressesString if it's not empty
        if (!defaultEditTextValue.isEmpty()) {
            addressesString.append(defaultEditTextValue).append(",");
            addresses.add(defaultEditTextValue);
        }

        // Process dynamically added EditText views
        int childCount = editTextContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = editTextContainer.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String editTextValue = editText.getText().toString();
                addresses.add(editTextValue);
                // Append each address to addressesString with a comma
                addressesString.append(editTextValue).append(",");
            }
        }

        // Remove the trailing comma if the StringBuilder is not empty
        if (addressesString.length() > 0) {
            addressesString.deleteCharAt(addressesString.length() - 1);
        }

        // Pass the constructed string to the next activity
        intent.putExtra("addresses", addressesString.toString());
        startActivity(intent);

        // Now, you can proceed with making the API call using addressesString
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Apiservices apiServices = RetrofitClient.getClient().create(Apiservices.class);
        Call<Void> call = apiServices.saveRehla(username, addressesString.toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // API call was successful
                    // Handle success response if needed
                } else {
                    // API call failed
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // API call failed due to network error or other reasons
                // Handle failure
            }
        });
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
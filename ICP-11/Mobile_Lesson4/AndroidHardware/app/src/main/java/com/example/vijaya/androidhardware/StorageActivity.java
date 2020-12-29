package com.example.vijaya.androidhardware;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StorageActivity extends AppCompatActivity {
    EditText txt_content;
    EditText contentToDisplay;
    String FILENAME = "MyAppStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        txt_content = (EditText) findViewById(R.id.id_txt_mycontent);
        contentToDisplay = (EditText) findViewById(R.id.id_txt_display);
    }

    public void saveTofile(View v) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_APPEND);
        fileOutputStream.write(txt_content.getText().toString().getBytes());
        fileOutputStream.close();
        Toast toast = Toast.makeText(getApplicationContext(), "File Saved!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void retrieveFromFile(View v) throws IOException {
        FileInputStream fileInputStream = openFileInput(FILENAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String bufferedReturn;
        StringBuilder builder = new StringBuilder();

        while((bufferedReturn = bufferedReader.readLine()) != null){
            builder.append(bufferedReturn);
        }
        fileInputStream.close();
        inputStreamReader.close();
        bufferedReader.close();

        contentToDisplay.setText(builder.toString());
        contentToDisplay.setVisibility(View.VISIBLE);
        // ICP Task4: Write the code to display the above saved text

    }
}

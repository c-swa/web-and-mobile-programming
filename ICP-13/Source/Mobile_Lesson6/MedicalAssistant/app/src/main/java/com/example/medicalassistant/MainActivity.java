package com.example.medicalassistant;

// Speech-to-Text Components
import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

// Text-to-Speech Components
import android.speech.tts.TextToSpeech;
import android.net.Uri;




public class MainActivity extends AppCompatActivity {

    // Speech-to-Text
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;

    // -----------------------------------------------------------------
    // Text-to-Speech
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1500;

    private Speaker speaker;

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    // -----------------------------------------------------------------
    // Lesson Plan Code Snippet
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /* -- Lesson Plan code commented out temporarily
     preferences = getSharedPreferences(PREFS, 0);
     editor = preferences.edit();
     editor.putString(NAME, <extracted name>).apply(); // private static final String name = "name";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        // Text-to-Speech
        checkTTS();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e){
            System.out.println("Error! : msg :" + e);
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intentData) {
        super.onActivityResult(request, result, intentData);

        switch (request) {
            case REQ_CODE_SPEECH_INPUT: {
                if (result == RESULT_OK && intentData != null){
                    ArrayList<String> res = intentData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(res.get(0));
                }
                break;
            }
        }
    }

    // Below is the Text-to-Speech Component

    @Override
    protected void onDestroy(){
        super.onDestroy();
        speaker.destroy();
    }
}
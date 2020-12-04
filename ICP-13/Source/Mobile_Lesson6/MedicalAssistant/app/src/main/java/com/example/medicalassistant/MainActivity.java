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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    TextToSpeech textToSpeech;

    // Answers
    private final String I_CAN_UNDERSTAND = "I can understand, Please tell us your symptoms in short.";
    private final String THANK_YOU = "Thank you too ";
    private final String TAKE_CARE = " take care.";
    private final String CURRENT_TIME = "The current time is ";
    private final String MEDICINE = "I think you have a fever, please take this medicine.";

    private Speaker speaker;


    // -----------------------------------------------------------------
    // Lesson Plan Code Snippet
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


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

        preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        editor = preferences.edit();

        // Text-to-Speech
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status < 0){
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.speak("Welcome",TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What can I do for you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e){
            System.out.println("Error! : msg :" + e);
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int request, int result, Intent intentData) {
        super.onActivityResult(request, result, intentData);

        switch (request) {
            case REQ_CODE_SPEECH_INPUT: {
                if (result == RESULT_OK && intentData != null) {
                    // pulls text from speech input and renders text on-screen.
                    ArrayList<String> res = intentData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (res.size() > 0) {
                        // Asks user's name after preliminary "hello"
                        if (res.get(0).equalsIgnoreCase("HELLO")) {
                            textToSpeech.speak("What is your name?", TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\nWhat is your name?");
                        }
                        // Retrieves user's name.
                        else if (res.get(0).contains("name")) {
                            editor.putString("NAME", res.get(0).substring(res.get(0).lastIndexOf(" ") + 1)).apply();
                            textToSpeech.speak(("Nice to meet you " + preferences.getString("NAME", "")), TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + "Nice to meet you " + preferences.getString("NAME", ""));
                        }
                        // Returns "I can understand. Please tell me your symptoms in short" if user says "I'm not feeling well"
                        else if (res.get(0).contains("not feeling well")) {
                            textToSpeech.speak(I_CAN_UNDERSTAND, TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + I_CAN_UNDERSTAND);
                        }
                        // Says "thank you" if user also says "thank you"
                        else if (res.get(0).contains("thank")) {
                            textToSpeech.speak(THANK_YOU + preferences.getString("NAME", "") + TAKE_CARE, TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + THANK_YOU + preferences.getString("NAME", "") + TAKE_CARE);
                        }
                        // Returns the time
                        else if (res.get(0).contains("time")) {
                            textToSpeech.speak("The time is " + getCurrentTime(), TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + "The time is " + getCurrentTime());
                        }
                        // Suggests medicine
                        else if (res.get(0).contains("medicines")) {
                            textToSpeech.speak(MEDICINE, TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + MEDICINE);
                        }
                        // If any issues or spoken words did not make sense to speech interpreter
                        else {
                            textToSpeech.speak("I didn't understand you, can you say that again?", TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append("\n" + "Please say that again, I didn't understand you.");
                        }
                    }
                    break;
                }
            }
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        String[] strDate = sdf.format(now).split(":");
        if (strDate[1].contains("00")){
            strDate[1] = "o'clock";
        }
        return sdf.format(now);
    }

    // Below is the Text-to-Speech Component

    @Override
    protected void onDestroy(){
        super.onDestroy();
        speaker.destroy();
    }
}
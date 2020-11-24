package com.csee5590.helloworldapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText usernameCtrl = (EditText) findViewById(R.id.usernameEdit);
        final EditText passwordCtrl = (EditText) findViewById(R.id.passwordEdit);

        final Button buttonSubmit = (Button) findViewById(R.id.submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = usernameCtrl.getText().toString();
                String passwordText = passwordCtrl.getText().toString();

                if (Submit(usernameText, passwordText) == 0){
                    try {
                        Intent redirect = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(redirect);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        });
    }

    public int Submit(String usernameText, String passwordText){
        if (onSubmit(usernameText, passwordText)){
            return 0;
        } else {
            return 100;
        }
    }

    private boolean onSubmit(String usernameText, String passwordText){
        if (!usernameText.isEmpty() && !passwordText.isEmpty()){
            if (usernameText.equals("Admin") && passwordText.equals("Admin")){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

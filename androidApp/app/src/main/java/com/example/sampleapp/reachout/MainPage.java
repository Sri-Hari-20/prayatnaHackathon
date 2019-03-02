package com.example.sampleapp.reachout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {

    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button signout = findViewById(R.id.button4);
        Button analyze = findViewById(R.id.button7);
        Button gethelp = findViewById(R.id.button8);

        title = findViewById(R.id.textView7);
        title.setText("Welcome " + SaveSharedPreference.getUserName(getApplicationContext()) + " !");
        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setCreds(getApplication(), "","");
                Intent signout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(signout);
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent analyze = new Intent(getApplicationContext(), Analyze.class);
                startActivity(analyze);
            }
        });
    }
}

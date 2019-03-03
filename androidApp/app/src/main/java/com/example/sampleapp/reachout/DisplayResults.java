package com.example.sampleapp.reachout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayResults extends AppCompatActivity {

    TextView finalAudio, finalPercent, symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        String results = getIntent().getStringExtra("json");
        try {
            JSONObject resultArray = new JSONObject(results);
            String audio = resultArray.get("audio").toString();
            Log.e("Audio: ", audio);
            String percent = resultArray.get("class").toString();
            finalAudio = findViewById(R.id.textView26);
            finalPercent = findViewById(R.id.textView24);
            symbol = findViewById(R.id.textView25);
            if(!audio.equals("none")){
                finalAudio.setText(audio);
            } else{
                finalAudio.setText("No Audio Was Uploaded");
            }

            if(!percent.equals("none")){
                finalPercent.setText(percent);
                float pa = Float.parseFloat(percent);
                if(pa > 50){
                    finalPercent.setTextColor(Color.parseColor("#ff2b2b"));
                    symbol.setTextColor(Color.parseColor("#ff2b2b"));
                } else{
                    finalPercent.setTextColor(Color.parseColor("#42f442"));
                    symbol.setTextColor(Color.parseColor("#42f442"));
                }
            }else{
                finalPercent.setText("NA");
            }
        } catch (Exception e) {
            Log.e("Error: ", e.toString());
        }
    }
}

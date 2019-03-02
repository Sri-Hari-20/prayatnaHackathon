package com.example.sampleapp.reachout;


import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Analyze extends AppCompatActivity {

    EditText link, messages;
    TextView pathView;
    Uri audiouri;
    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        Button signout = findViewById(R.id.button6);
        Button analyze = findViewById(R.id.button5);
        Button fileChoose = findViewById(R.id.button9);
        pathView = findViewById(R.id.textView12);

        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setCreds(getApplication(), "","");
                Intent signout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(signout);
            }
        });

        fileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a Wav File to Upload"),
                            SELECT_AUDIO);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getApplicationContext(), "Please install a File Manager!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        analyze.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                link = findViewById(R.id.editText7);
                messages = findViewById(R.id.editText8);
                JSONObject creds = new JSONObject();
                try {
                    creds.put("link", link.getText().toString().trim());
                    creds.put("email", SaveSharedPreference.getEmail(getApplicationContext()));
                    creds.put("messages", messages.getText().toString());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream in =
                            getContentResolver().openInputStream(audiouri);
                    int read;
                    byte[] buff = new byte[1024];
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                    out.flush();
                    byte[] audioBytes = out.toByteArray();
                    String audioEncoded = Base64.encodeToString(audioBytes, Base64.DEFAULT);
                    creds.put("audio", audioEncoded);

                    new analyzeSend().execute(getString(R.string.server_url)+"analyze", creds.toString());
                } catch (Exception e) {
                    Log.e("Error ", e.toString());
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == SELECT_AUDIO)
            {
                audiouri = data.getData();
                pathView.setText(audiouri.getPath());
            }
        }
    }


    private class analyzeSend extends AsyncTask<String, Void, String>{
        String data = "";
        @Override
        protected String doInBackground(String... strings) {
            try{
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                Log.e("Data: ", strings[1]);
                wr.writeBytes(strings[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(data.equals("Done")){
                Toast.makeText(getApplicationContext(), "Audio sent successfully", Toast.LENGTH_LONG).show();
            }
            else if(data.equals("NF")){
                Toast.makeText(getApplicationContext(), "Audio failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}

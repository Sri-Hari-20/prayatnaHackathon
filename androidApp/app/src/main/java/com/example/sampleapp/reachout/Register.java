package com.example.sampleapp.reachout;

import android.os.AsyncTask;
import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText email, name, password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button regAC = findViewById(R.id.button3);

        regAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.editText3);
                name = findViewById(R.id.editText4);
                password = findViewById(R.id.editText5);
                confirmPassword = findViewById(R.id.editText6);

                String em, na, pwd, cpwd;
                em = email.getText().toString().trim();
                na = name.getText().toString().trim();
                pwd = password.getText().toString().trim();
                cpwd = confirmPassword.getText().toString().trim();

                if(em.length() == 0 | !Patterns.EMAIL_ADDRESS.matcher(em).matches()){
                    Toast.makeText(getApplicationContext(), "Invalid / empty email",Toast.LENGTH_LONG).show();
                    return ;
                }

                if(na.length() == 0){
                    Toast.makeText(getApplicationContext(), "Name field is empty",Toast.LENGTH_LONG).show();
                    return ;
                }

                if(pwd.length() == 0){
                    Toast.makeText(getApplicationContext(), "Password field is empty",Toast.LENGTH_LONG).show();
                    return ;
                }

                if(cpwd.length() == 0){
                    Toast.makeText(getApplicationContext(), "Confirm Password field is empty",Toast.LENGTH_LONG).show();
                    return ;
                }

                if(!pwd.equals(cpwd)){
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password don't match"
                            ,Toast.LENGTH_LONG).show();
                    return ;
                }

                JSONObject creds = new JSONObject();
                try {
                    creds.put("email", em);
                    creds.put("name", na);
                    creds.put("password", pwd);
                    new registerActivity().execute(getString(R.string.server_url)+"register", creds.toString());
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });
    }

    private class registerActivity extends AsyncTask<String, Void, String>{
        String data = "";
        protected String doInBackground(String... strings){
            try {
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

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result.equals("Done")){
                Toast.makeText(getApplicationContext(), "Account has been created successfully!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Error in creating account, contact admin!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.sampleapp.reachout;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.net.URL;

public class LoginPage extends AppCompatActivity {

    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button register = findViewById(R.id.button2);
        Button login = findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), Register.class);
                startActivity(reg);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.editText);
                password = findViewById(R.id.editText2);

                String em, pwd;

                em = email.getText().toString().trim();
                pwd = password.getText().toString().trim();

                if(em.length() == 0){
                    Toast.makeText(getApplicationContext(), "Empty Email Field", Toast.LENGTH_LONG).show();
                    return ;
                }

                if(pwd.length() == 0){
                    Toast.makeText(getApplicationContext(), "Empty Password Field", Toast.LENGTH_LONG).show();
                    return ;
                }

                JSONObject creds = new JSONObject();

                try {
                    creds.put("email", em);
                    creds.put("password", pwd);
                    new loginActivity().execute(getString(R.string.server_url)+ "login", creds.toString());
                } catch (Exception e) {
                    Log.e("Error: ", e.toString());
                }
            }
        });
    }

    private class loginActivity extends AsyncTask<String, Void, String>{
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
            if(result.equals("NF")){
                Toast.makeText(getApplicationContext(), "Invalid Email / Password!", Toast.LENGTH_LONG).show();
            }
            else{
                Intent main = new Intent(getApplicationContext(), MainPage.class);
                Log.e("Name: ", result);
                SaveSharedPreference.setCreds(getApplicationContext(), result, email.getText().toString().trim());
                Log.e("Email: ", email.getText().toString().trim());
                startActivity(main);
                finish();
            }
        }
    }
}

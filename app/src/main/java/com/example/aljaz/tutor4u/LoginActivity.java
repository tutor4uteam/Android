package com.example.aljaz.tutor4u;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);

        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    private void login() {
        Log.d(TAG, "Login");
        // Preveri pravilnost formatov emaila in gesla
        if (!validData()){
            // format ni pravi
            loginButton.setEnabled(true);
        }
        else {
            // format je pravi
            loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating ...");
            progressDialog.show();


            // 3 sekundi screen za avtorizacijo
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // preveri pravilnost podatkov preko API-ja
                    validateTutor(new VolleyCallback() {
                        @Override
                        public void onSuccess(boolean result) {
                            // preveri vrnjeni boolean
                            if (!result) {
                                // če ni tutor preveri ce je student
                                validateStudent(new VolleyCallback() {
                                    @Override
                                    public void onSuccess(boolean result) {
                                        if (!result){
                                            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(getBaseContext(), "Logged in as STUDENT", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                loginButton.setEnabled(true);
                                progressDialog.dismiss();
                            } else {
                                System.out.println("IsValid succ after: " + result);
                                Toast.makeText(getBaseContext(), "Logged in as TUTOR", Toast.LENGTH_LONG).show();
                                loginButton.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        }
                    });

                }
            }, 3000);
        }

    }
    // metoda za preverjanje formatov
    private boolean validData() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_LONG).show();
            return false;
        }else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4){
            Toast.makeText(this, "Password must be longer than 4 chars", Toast.LENGTH_LONG).show();
            return false;
        }else {
            passwordText.setError(null);
        }
        return valid;
    }

    // metoda za preverjanje preko API-a
    private void validateTutor(final VolleyCallback callback) {
        String getTutor = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/LoginTutor/%s/%s", emailText.getText(), passwordText.getText());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getTutor, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String result = jsonObject.getString("result");
                        callback.onSuccess(result.equals("1") ? true : false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);

    }

    private void validateStudent(final VolleyCallback callback) {
        String getStudent = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/LoginStudent/%s/%s", emailText.getText(), passwordText.getText());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getStudent, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String result = jsonObject.getString("result");
                        callback.onSuccess(result.equals("1") ? true : false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);

    }

    public interface VolleyCallback{
        void onSuccess(boolean result);
    }
}

package com.example.aljaz.tutor4u;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Fragment {
    private RequestQueue requestQueue;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    UserInfo userInfo;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_log, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");

        emailText = view.findViewById(R.id.input_email);
        passwordText = view.findViewById(R.id.input_password);

        SharedPreferences sp1 = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String unm = sp1.getString("Username", null);
        String pass = sp1.getString("Password", null);

        emailText.setText(unm);
        passwordText.setText(pass);
        loginButton = view.findViewById(R.id.btn_login);
        signupLink = view.findViewById(R.id.link_signup);
        if (emailText.getText().length() > 0 && passwordText.getText().length() > 0){
            login();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new SignupActivity(), "SignUp")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
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

            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
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
                                            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor Ed = sp.edit();
                                            Ed.putString("Username", emailText.getText().toString());
                                            Ed.putString("Password", passwordText.getText().toString());
                                            Ed.commit();
                                            Toast.makeText(getContext(), "Logged in as STUDENT", Toast.LENGTH_LONG).show();
                                            loginSuccessful();
                                        }
                                    }
                                });
                                loginButton.setEnabled(true);
                                progressDialog.dismiss();
                            } else {
                                System.out.println("IsValid succ after: " + result);
                                SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor Ed = sp.edit();
                                Ed.putString("Username", emailText.getText().toString());
                                Ed.putString("Password", passwordText.getText().toString());
                                Ed.commit();
                                Toast.makeText(getContext(), "Logged in as TUTOR", Toast.LENGTH_LONG).show();
                                loginButton.setEnabled(true);
                                progressDialog.dismiss();
                                loginSuccessful();
                            }
                        }
                    });

                }
            }, 500);
        }

    }


    private void loginSuccessful() {
        Intent intent = new Intent(getContext(), TutorMainActivity.class);
        SharedPreferences  mPrefs = getContext().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        prefsEditor.putString("Profile_info", json);
        prefsEditor.commit();
        //intent.putExtra("UserInfo", userInfo);
        startActivity(intent);
        //finish();
    }


    // metoda za preverjanje formatov
    private boolean validData() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getContext(), "Enter a valid email address", Toast.LENGTH_LONG).show();
            return false;
        }else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4){
            Toast.makeText(getContext(), "Password must be longer than 4 chars", Toast.LENGTH_LONG).show();
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
                        if (result.equals("1")){
                            userInfo = new UserInfo(jsonObject.getString("id"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("lastname"),
                                                    jsonObject.getString("street"),
                                                    jsonObject.getString("houseNumber"),
                                                    jsonObject.getString("postNumber"),
                                                    jsonObject.getString("postName"),
                                                    jsonObject.getString("mail"),
                                                    jsonObject.getString("phone"),
                                                    "tutor");
                        }
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
                        if (result.equals("1")){
                            userInfo = new UserInfo(jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("lastname"),
                                    jsonObject.getString("street"),
                                    jsonObject.getString("houseNumber"),
                                    jsonObject.getString("postNumber"),
                                    jsonObject.getString("postName"),
                                    jsonObject.getString("mail"),
                                    jsonObject.getString("phone"), "student");
                        }
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

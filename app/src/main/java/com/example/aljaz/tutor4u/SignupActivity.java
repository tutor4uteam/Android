package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

import java.util.ArrayList;

public class SignupActivity extends Fragment {
    private RequestQueue requestQueue;

    EditText firstName;
    EditText surname;
    EditText address;
    EditText streetNum;
    EditText postCode;
    EditText postName;
    EditText email;
    EditText mobileNumber;
    EditText password;
    EditText rePassword;
    TextView rePasswordError;
    Switch aSwitch;
    Button createAccount;
    TextView login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Sign up");

        firstName = view.findViewById(R.id.input_first_name);
        surname = view.findViewById(R.id.input_surname);
        address = view.findViewById(R.id.input_address);
        streetNum = view.findViewById(R.id.input_addressNum);
        postCode = view.findViewById(R.id.input_postcode);
        postName = view.findViewById(R.id.input_post);
        email = view.findViewById(R.id.input_email);
        mobileNumber = view.findViewById(R.id.input_mobile);
        password = view.findViewById(R.id.input_password);
        rePassword = view.findViewById(R.id.input_reEnterPassword);
        rePasswordError = view.findViewById(R.id.rePasswordError);
        aSwitch = view.findViewById(R.id.switch1);
        createAccount = view.findViewById(R.id.btn_signup);
        login = view.findViewById(R.id.link_login);

        //mobileNumber.setEnabled(false);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    aSwitch.setHint("Student");
                    //mobileNumber.setEnabled(false);
                }else {
                    aSwitch.setHint("Tutor");
                    //mobileNumber.setEnabled(true);
                    //mobileNumber.requestFocus();
                }
            }
        });

        postCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(s + " " + count);
                if (s.length() == 4){
                    getPost(s);
                }else {
                    postName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
        });

        rePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }
            // pregleduj drugega vpisa gesla
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    rePasswordError.setText("");
                }
                else if (!s.toString().equals(password.getText().toString())){
                    rePasswordError.setText("Passwords do not match");
                    createAccount.setEnabled(false);
                }
                else{
                    rePasswordError.setText("");
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                return;
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new LoginActivity(), "Login")
                        .addToBackStack(null)
                        .commit();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });



        return view;
    }

    private void getPost(CharSequence s) {
        String getPostName = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Town/" + s);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getPostName,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        postName.setText(name);
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

    private void signup() {


        if (!validate()) {
            onSignupFailed();
            return;
        }

        createAccount.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        registerTutor(new VolleyCallback(){
                            @Override
                            public void onSuccess(boolean result) {
                                if (result){
                                    onSignupSuccess();
                                }
                                else {
                                    onSignupFailed();
                                }
                            }
                        });

                        progressDialog.dismiss();
                    }
                }, 2000);

    }

    public void onSignupSuccess() {
        Toast.makeText(getContext(), "Account successfully created", Toast.LENGTH_LONG).show();
        createAccount.setEnabled(true);
        SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("Username", email.getText().toString());
        Ed.putString("Password", password.getText().toString());
        Ed.commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.flcontent, new LoginActivity(), "Login")
                .commit();
    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Can't create account", Toast.LENGTH_LONG).show();
        createAccount.setEnabled(true);
    }


    // Preveri pravilnost vpisanih podatkov
    public boolean validate() {
        boolean valid = true;

        String fName = firstName.getText().toString();
        String sName = surname.getText().toString();
        String sAddress = address.getText().toString();
        String sStreetNum = streetNum.getText().toString();
        String sEmail = email.getText().toString();
        String sMobile = mobileNumber.getText().toString();
        String sPassword = password.getText().toString();
        Boolean isStudent = aSwitch.isChecked();

        if (fName.isEmpty() || fName.length() < 3) {
            firstName.setError("at least 3 characters");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (sName.isEmpty() || sName.length() < 3) {
            surname.setError("at least 3 characters");
            valid = false;
        } else {
            surname.setError(null);
        }

        if (sAddress.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }

        if (sStreetNum.isEmpty()) {
            streetNum.setError("Enter Valid Street Num");
            valid = false;
        } else {
            streetNum.setError(null);
        }


        if (sEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (!isStudent && (sMobile.isEmpty() || sMobile.length()!=9)) {
            mobileNumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileNumber.setError(null);
        }

        if (sPassword.isEmpty() || sPassword.length() < 4) {
            password.setError("Password must be longer than 4");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void registerTutor(final VolleyCallback callback) {
        // TODO Popravi telefonsko v add student
        String fName = firstName.getText().toString().replaceAll(" ", "%20");
        String sName = surname.getText().toString().replaceAll(" ", "%20");
        String sAddress = address.getText().toString().replaceAll(" ", "%20");
        String sStreetNum = streetNum.getText().toString().replaceAll(" ", "%20");
        String sPostCode = postCode.getText().toString().replaceAll(" ", "%20");
        String sEmail = email.getText().toString().replaceAll(" ", "%20");
        String sMobile = mobileNumber.getText().toString().replaceAll(" ", "%20");
        String sPassword = password.getText().toString().replaceAll(" ", "%20");


//        String registerTutor = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addTutor/"+fName+"/" +
//                "" + sName+ "/"+sEmail+"/" + sMobile + "/"+sPassword+"/"+sPostCode+"/"+sAddress+"/"+sStreetNum);
//        String registerUser = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addUser/"+fName+"/" +
//                "" + sName+ "/"+sEmail + "/"+sPassword+"/"+sPostCode+"/"+sAddress+"/"+sStreetNum);

        //http://apitutor.azurewebsites.net/RestServiceImpl.svc/addStudent/
        // {NAME}/{SURNAME}/{MAIL}/{PASSWORD}/{POSTNUMBER}/{STREET}/{HOUSENO}
        String registerTutor = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addTutor/%s/%s/%s/%s/%s/%s/%s/%s",
                fName, sName, sEmail, sMobile, sPassword, sPostCode, sAddress, sStreetNum, sMobile);
        String registerUser = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addStudent/%s/%s/%s/%s/%s/%s/%s/%s",
                fName, sName, sEmail, sPassword, sPostCode, sAddress, sStreetNum, sMobile);
        String usedURL = aSwitch.isChecked() ? registerTutor : registerUser;
        Log.d("Signed up as :" ,usedURL);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, usedURL, null, new Response.Listener<JSONArray>() {
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
                Toast.makeText(getContext(),"There was a problem, please try later.", Toast.LENGTH_LONG).show();
                createAccount.setEnabled(true);
                System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);

    }

    public interface VolleyCallback{
        void onSuccess(boolean result);
    }
}

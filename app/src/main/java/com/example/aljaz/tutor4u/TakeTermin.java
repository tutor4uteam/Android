package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.listViewAllTermins.ModelAllTermins;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class TakeTermin extends Fragment {
    TextView username, userAddress, userMail, userPhone, terminDate, terminPrice;
    Button takeTerm;
    private ProgressBar spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_termin, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tutor information");

        spinner = view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        username = view.findViewById(R.id.UserName);
        userAddress = view.findViewById(R.id.userAddress);
        userMail = view.findViewById(R.id.userMail);
        userPhone = view.findViewById(R.id.userPhone);
        terminDate = view.findViewById(R.id.terminDate);
        terminPrice = view.findViewById(R.id.terminPrice);

        takeTerm = view.findViewById(R.id.btn_take_term);


        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        Bundle bundle = getArguments();
        final ModelAllTermins tutorInformation = (ModelAllTermins) bundle.getSerializable("termin");

        getTutorInfo(tutorInformation);

        takeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTermin(userInfo.getUser_id(),  tutorInformation.getIdTermin());
            }
        });

        return view;
    }

    private void takeTermin(String user_id, String idTermin) {
        String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/TakeTermin/%s/%s", idTermin, user_id);
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Term cant be taken", Toast.LENGTH_LONG).show();
                Log.i("Debug", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(myResponse);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String id = jsonObject.getString("result");

                            if (id.equals("1")) {
                                Toast.makeText(getContext(), "Term successfuly taken", Toast.LENGTH_LONG).show();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.flcontent, new Dashboard())
                                        .commit();
                            } else {
                                Toast.makeText(getContext(), "Term cant be taken", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }


    private void getTutorInfo(final ModelAllTermins tutorInformation) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor/" + tutorInformation.getIdTutor());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Sorry there was a problem getting data. Please try later", Toast.LENGTH_LONG).show();
                Log.i("Debug", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                ArrayList<ModelAllTermins> modelAllTermins = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String address = jsonObject.getString("address");
                                    String grade = jsonObject.getString("grade");
                                    String mail = jsonObject.getString("mail");
                                    String phone = jsonObject.getString("phone");

                                    username.setText(tutorInformation.getTutorName());
                                    userAddress.setText(address);
                                    userMail.setText(mail);
                                    userPhone.setText(phone);
                                    SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    String finalDate = dt1.format(tutorInformation.getDate());
                                    terminDate.setText(finalDate);
                                    terminPrice.setText(tutorInformation.getPrice());

                                    spinner.setVisibility(View.GONE);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}

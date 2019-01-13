package com.example.aljaz.tutor4u.listViewStudentTerms;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aljaz.tutor4u.Dashboard;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.Interface.DialogCallback;
import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.utils.GlobalUtils;
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


public class MyTermsStudent extends Fragment {
    ListView listView;
    ListViewStudentTermsAdapter adapter;
    ArrayList<ModelStudentTerm> arrayList;
    private ProgressBar spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_terms_student, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Terms");

        spinner = view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();
        listView = view.findViewById(R.id.listViewStudentTerm);
        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        getAllTerms(userInfo.getUser_id());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDialog(view, arrayList.get(position));
            }
        });

        return view;
    }

    public void showDialog(View view, final ModelStudentTerm modelStudentTerm){
        GlobalUtils.showDialog(getContext(), new DialogCallback() {
            @Override
            public void callback(String ratings) {
                giveGrade(modelStudentTerm.getTerminId(), ratings);
            }
        });
    }

    private void giveGrade(String terminId, String ratings) {
        String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/GiveGrade/%s/%s", terminId, ratings);
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
                                Toast.makeText(getContext(), "Thank you for your vote", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), "Ups, there was a problem :(", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void getAllTerms(String idStudent) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/MyTerminStudent/" + idStudent);
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
                                ArrayList<ModelStudentTerm> modelStudentTerms = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String tutorId = jsonObject.getString("tutorId");
                                    String terminId = jsonObject.getString("idTermin");
                                    String tutorName = jsonObject.getString("tutorName") + " " + jsonObject.getString("tutorLastname");
                                    String tutorAddress = jsonObject.getString("address");
                                    String subject = jsonObject.getString("subject");

                                    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH-mm");
                                    Date date = dt.parse(jsonObject.getString("date"));
                                    SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    String finalDate = dt1.format(date);
                                    Date dateOfTerm = dt1.parse(finalDate);

                                    ModelStudentTerm newModelAllSubjects = new ModelStudentTerm(tutorId, terminId, tutorName, tutorAddress, subject, dateOfTerm);
                                    if (!new Date().after(dateOfTerm)) {
                                        modelStudentTerms.add(newModelAllSubjects);
                                    }
                                }

                                arrayList.addAll(modelStudentTerms);
                                arrayList.sort(new Comparator<ModelStudentTerm>() {
                                    @Override
                                    public int compare(ModelStudentTerm o1, ModelStudentTerm o2) {
                                        return o1.date.compareTo(o2.date);
                                    }
                                });
                                Collections.reverse(arrayList);
                                try {
                                    adapter = new ListViewStudentTermsAdapter(getContext(), arrayList);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                listView.setAdapter(adapter);
                                spinner.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
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

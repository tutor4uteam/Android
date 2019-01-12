package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.Subject;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.subjectSpinner.ModelSubjectSpinner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class AddTermin extends Fragment implements AddSubjectDialog.AddSubjectDialogListener {

    ArrayList<ModelSubjectSpinner> subjectsArray;
    ArrayAdapter<ModelSubjectSpinner> adapter;
    private RequestQueue requestQueue;
    Button btn_create_term;
    ImageButton addSubject;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner subjects;
    String idTutor = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        subjectsArray = new ArrayList<>();
        subjectsArray.add(new ModelSubjectSpinner("0", "Choose from your subjects", ""));
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_termin, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add term");


        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        subjects = view.findViewById(R.id.spin_subjects);
        subjects.setEnabled(false);
        datePicker = view.findViewById(R.id.datePicked);
        timePicker = view.findViewById(R.id.timePicked);
        timePicker.setIs24HourView(true);
        btn_create_term = view.findViewById(R.id.btn_create_term);
        addSubject = view.findViewById(R.id.addNewSubjectITeach);

        idTutor = userInfo.getUser_id();

        getAllSubjects(idTutor);

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        btn_create_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelSubjectSpinner modelSubjectSpinner = (ModelSubjectSpinner) subjects.getSelectedItem();
                String subject_id = modelSubjectSpinner.getId();
                int day = datePicker.getDayOfMonth();
                String sDay = String.valueOf(day).length() == 1 ? "0" + String.valueOf(day) : String.valueOf(day);
                int month = datePicker.getMonth();
                String sMonth = String.valueOf(month + 1).length() == 1 ? "0" + String.valueOf(month + 1) : String.valueOf(month + 1);
                int year = datePicker.getYear();
                int hour = timePicker.getHour();
                String sHour = String.valueOf(hour).length() == 1 ? "0" + String.valueOf(hour) : String.valueOf(hour);
                int minute = timePicker.getMinute();
                String sMinute = String.valueOf(minute).length() == 1 ? "0" + String.valueOf(minute) : String.valueOf(minute);
                String dateAndTime = String.format("%s-%s-%s %s-%s", sDay, sMonth, year, sHour, sMinute);
                Log.i("Date", dateAndTime + " " + subject_id);

                Calendar my = Calendar.getInstance();
                my.set(year, month, day, hour, minute);

                if (my.compareTo(Calendar.getInstance()) == 1)
                    createTerm(idTutor, subject_id, dateAndTime);
                else
                    Toast.makeText(getContext(), "Please choose a valid date", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void getAllSubjects(String idTutor) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/subjectITeach/" + idTutor);
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
                                ArrayList<ModelSubjectSpinner> modelSubjectSpinners = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String price = jsonObject.getString("price");
                                    ModelSubjectSpinner newModelAllSubjects = new ModelSubjectSpinner(id, name, price);
                                    modelSubjectSpinners.add(newModelAllSubjects);
                                }

                                subjectsArray.addAll(modelSubjectSpinners);
                                try {
                                    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectsArray);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subjects.setAdapter(adapter);
                                subjects.setEnabled(true);

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



    private void createTerm(final String idTutor, String subject_id, String dateAndTime) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addTermin/%s/%s/%s", idTutor, subject_id, dateAndTime);

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Subject cant be created", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getContext(), "Term successfuly created", Toast.LENGTH_LONG).show();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.flcontent, new Dashboard())
                                        .commit();
                            } else {
                                Toast.makeText(getContext(), "Subject cant be created", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    public void openDialog() {
        AddSubjectDialog addSubjectDialog = new AddSubjectDialog();
        addSubjectDialog.setTargetFragment(AddTermin.this, 1);
        addSubjectDialog.show(getFragmentManager().beginTransaction(), "subject dialog");
    }

    @Override
    public void applyChange(Subject subject, String price) {
//        createSubject(idTutor, subject.getId_subject(), price);
        addSubjectTutor(idTutor, subject.getId_subject(), price);
        ModelSubjectSpinner modelSubjectSpinner = new ModelSubjectSpinner(subject.getId_subject(), subject.getSubject(), price);
        boolean subjectsArrayContainsNewSubject = false;
        for (ModelSubjectSpinner model:subjectsArray) {
            if (model.getId().equals(modelSubjectSpinner.getId())){
                subjectsArrayContainsNewSubject = true;
            }
        }

        if (!subjectsArrayContainsNewSubject) {
            subjectsArray.add(modelSubjectSpinner);
            adapter.notifyDataSetChanged();
        }
    }


    private void addSubjectTutor(String idTutor, String idSubject, String price) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addSubjectPrice/%s/%s/%s", idTutor, idSubject, price);
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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

                            if (id.equals("1"))
                                Toast.makeText(getContext(), "Subject successfuly created", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getContext(), "Subject cant be created", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

}


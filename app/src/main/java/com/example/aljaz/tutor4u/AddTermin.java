package com.example.aljaz.tutor4u;

import android.os.Handler;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.Subject;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.subjectSpinner.ModelSubjectSpinner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AddTermin extends Fragment  implements AddSubjectDialog.AddSubjectDialogListener {

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubjects(new VolleyCallback() {

                    @Override
                    public void onSuccess(ArrayList result) {
                        subjectsArray.addAll(result);
                        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectsArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjects.setAdapter(adapter);
                        subjects.setEnabled(true);
                    }
                }, idTutor);
            }
        }, 0);

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

                if(my.compareTo(Calendar.getInstance()) == 1) createTerm(idTutor, subject_id, dateAndTime);
                else Toast.makeText(getContext(), "Please choose a valid date", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void openDialog(){
        AddSubjectDialog addSubjectDialog = new AddSubjectDialog();
        addSubjectDialog.setTargetFragment(AddTermin.this, 1);
        addSubjectDialog.show(getFragmentManager().beginTransaction(), "subject dialog");
    }

    private void createTerm(final String idTutor, String subject_id, String dateAndTime) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addTermin/%s/%s/%s", idTutor, subject_id, dateAndTime);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pushTerm(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        if (result.get(0).equals("1")) {
                            Toast.makeText(getContext(), "Term successfuly created", Toast.LENGTH_LONG).show();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.flcontent, new Dashboard())
                                    .commit();
                        }
                        else {
                            Toast.makeText(getContext(), "There was a problem, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }, url);
            }
        }, 500);
    }

    private void pushTerm(final VolleyCallback callback, final String url) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<String> result = new ArrayList<>();

                    JSONObject jsonObject = response.getJSONObject(0);
                    String id = jsonObject.getString("result");
                    result.add(id);

                    callback.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void getSubjects(final VolleyCallback callback, String userId) {
        final String getSubject = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/subjectITeach/" + userId);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getSubject, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<ModelSubjectSpinner> modelSubjectSpinners = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        ModelSubjectSpinner newModelAllSubjects = new ModelSubjectSpinner(id, name, price);
                        modelSubjectSpinners.add(newModelAllSubjects);
                    }

                    callback.onSuccess(modelSubjectSpinners);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sorry there was a problem. Please try later", Toast.LENGTH_LONG);
                //System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);
    }

    private void createSubject(final String idTutor, final String subject_id, String price) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/addSubjectPrice/%s/%s/%s", idTutor, subject_id, price);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pushTerm(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        //querySubjects();
                        if (result.get(0).equals("1")) Toast.makeText(getContext(), "Subject successfuly created", Toast.LENGTH_LONG).show();
                        else Toast.makeText(getContext(), "Subject cant be created", Toast.LENGTH_LONG).show();
                    }
                }, url);
            }
        }, 500);
    }



    @Override
    public void applyChange(Subject subject, String price) {
        createSubject(idTutor, subject.getId_subject(), price);
        subjectsArray.add(new ModelSubjectSpinner(subject.getId_subject(), subject.getSubject(), price));
        adapter.notifyDataSetChanged();
    }


    public interface VolleyCallback {
        void onSuccess(ArrayList result);
    }
}


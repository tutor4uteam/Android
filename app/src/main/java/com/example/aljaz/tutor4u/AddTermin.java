package com.example.aljaz.tutor4u;

import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.Subject;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;


public class AddTermin extends Fragment{

    ArrayList<Subject> subjectsArray = new ArrayList<>();
    Button btn_date_pick, btn_time_pick, btn_create_term;
    Spinner subjects;
    private RequestQueue requestQueue;
    int day, month, year, hour, min;
    int dayFinal, monthFinal, yearFinal, hourFinal, minFinal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_termin, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add term");
        subjects = view.findViewById(R.id.spin_subjects);
        btn_date_pick = view.findViewById(R.id.btn_date_pick);
        btn_time_pick = view.findViewById(R.id.btn_time_pick);
        btn_create_term = view.findViewById(R.id.btn_create_term);

        getSubjects();

        btn_date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
                DialogFragment datePicker = new SelectDateFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });



        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_termin, container, false);
    }

    private void getSubjects() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubjectsToArray(new VolleyCallback(){
                    @Override
                    public void onSuccess(ArrayList result) {

                    }
                });
            }
        }, 500);
    }

    private void getSubjectsToArray(VolleyCallback volleyCallback) {

    }

    public interface VolleyCallback{
        void onSuccess(ArrayList result);
    }
}


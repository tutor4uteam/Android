package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.listViewAllSubjects.AllSubjects;
import com.example.aljaz.tutor4u.listViewAllTutors.AllTutors;
import com.example.aljaz.tutor4u.listViewStudentTerms.MyTermsStudent;
import com.example.aljaz.tutor4u.listViewTutorTerms.ListViewTutorTerms;
import com.example.aljaz.tutor4u.listViewTutorTerms.freeTerms.AllFreeTermsTutor;
import com.example.aljaz.tutor4u.map.MapOfTutorsActivity;
import com.google.gson.Gson;


public class Dashboard extends Fragment {


    private CardView allSubjectsCard, filterCard, allTutorsCard, mapCard, addTerm, myTerms, seeMyTermsStudent;
    private LinearLayout addTermLayout, studentViewLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");
        allSubjectsCard = view.findViewById(R.id.allSubjectsCardId);
        filterCard = view.findViewById(R.id.filterCardId);
        allTutorsCard = view.findViewById(R.id.allTutorsCardId);
        mapCard = view.findViewById(R.id.mapCardId);
        addTerm = view.findViewById(R.id.addTerm);
        addTermLayout = view.findViewById(R.id.addTermLayout);
        myTerms = view.findViewById(R.id.seeMyTerms);
        studentViewLayout = view.findViewById(R.id.studentView);
        seeMyTermsStudent = view.findViewById(R.id.seeMyTermsStudent);

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        if (userInfo.getRole().equals("tutor")) addTermLayout.setVisibility(View.VISIBLE);
        else studentViewLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
            getFragmentManager().popBackStack();
        }

        allSubjectsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new AllSubjects())
                        .addToBackStack(null)
                        .commit();
            }
        });

        allTutorsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new AllTutors())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstpage= new Intent(getActivity(),MapOfTutorsActivity.class);
                getActivity().startActivity(firstpage);

            }
        });

        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new AddTermin())
                        .addToBackStack(null)
                        .commit();
            }
        });

        filterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "To be implemented",
                        Toast.LENGTH_LONG).show();
            }
        });

        myTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new AllFreeTermsTutor())
                        .addToBackStack(null)
                        .commit();
            }
        });

        seeMyTermsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new MyTermsStudent())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }


}

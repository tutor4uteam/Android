package com.example.aljaz.tutor4u;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public class Dashboard extends Fragment {


    private CardView allSubjectsCard, filterCard, allTutorsCard, mapCard;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        allSubjectsCard = view.findViewById(R.id.allSubjectsCardId);
        filterCard = view.findViewById(R.id.filterCardId);
        allTutorsCard = view.findViewById(R.id.allTutorsCardId);
        mapCard = view.findViewById(R.id.mapCardId);

        allSubjectsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, new AllSubjects())
                        .commit();
            }
        });
        return view;
    }


}

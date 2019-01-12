package com.example.aljaz.tutor4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aljaz.tutor4u.Helpers.GetData;


public class Settings extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetData getData = new GetData();
        //getData.doInBackground("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor");
        getData.execute("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


}

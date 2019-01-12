package com.example.aljaz.tutor4u;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aljaz.tutor4u.listViewAllTutors.ModelAllTutors;


public class TutorProfileInfo extends Fragment {
    TextView name, grade, email, phone, address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_profile_info, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tutor information");

        Bundle bundle = getArguments();
        final ModelAllTutors tutorInformation = (ModelAllTutors) bundle.getSerializable("id_tutor");

        name = view.findViewById(R.id.username);
        grade = view.findViewById(R.id.grade);
        email = view.findViewById(R.id.userMail);
        phone = view.findViewById(R.id.userPhone);
        address = view.findViewById(R.id.userAddress);

        name.setText(tutorInformation.getProfile_name() + " " + tutorInformation.getProfile_surname());
        grade.setText(tutorInformation.getProfile_grade());
        address.setText(tutorInformation.getProfile_address());
        email.setText(tutorInformation.getProfile_mail());
        phone.setText(tutorInformation.getProfile_phone());

        getTutorTermins(tutorInformation.getTutorId());

        return view;
    }

    private void getTutorTermins(String tutorId) {

    }
}

package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.google.gson.Gson;


public class UserInfoStudent extends Fragment {

    TextView name, grade, email, phone, address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_student, container, false);
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.userMail);
        phone = view.findViewById(R.id.userPhone);
        address = view.findViewById(R.id.userAddress);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("User info");

        /*Bundle bundle = this.getArguments();
        UserInfo userInfo = null;
        if (bundle != null) {
            userInfo = (UserInfo) bundle.getSerializable("UserInfo");
        }*/

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        name.setText(userInfo.getFirsname() + " " + userInfo.getLastname());
        address.setText(userInfo.getStreet()+ " " +userInfo.getHouseNum() + " " + userInfo.getPostNum() + " " + userInfo.getPostName() );
        email.setText(userInfo.getMail());
        phone.setText(userInfo.getPhoneNum());

        return view;
    }
}

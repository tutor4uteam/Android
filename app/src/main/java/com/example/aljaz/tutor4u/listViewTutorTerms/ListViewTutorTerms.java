package com.example.aljaz.tutor4u.listViewTutorTerms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.listViewTutorTerms.freeTerms.AllFreeTermsTutor;
import com.example.aljaz.tutor4u.listViewTutorTerms.reservedTerms.AllReservedTermsTutor;

public class ListViewTutorTerms extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view_tutor_terms, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Terms");

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new AllFreeTermsTutor(), "Free Terms");
        //adapter.addFragment(new AllReservedTermsTutor(), "Reserved Terms");
        viewPager.setAdapter(adapter);
    }
}

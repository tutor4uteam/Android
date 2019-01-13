package com.example.aljaz.tutor4u;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import android.view.ViewGroup.LayoutParams;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Settings extends Fragment {
    TextView textView;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getData.doInBackground("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        //textView = view.findViewById(R.id.text_settings);
        linearLayout = view.findViewById(R.id.customRow);
        for (int i = 0; i < 10; i++) {
            TextView valueTV = new TextView(getContext());
            valueTV.setText("hallo hallo");
            valueTV.setId(i);
            valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            valueTV.setPadding(0,10,0,0);
            linearLayout.addView(valueTV);
        }

        String url = "http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Debug", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        afterRun(myResponse);
                    }
                });
            }
        });



        return view;
    }

    private void afterRun(String myResponse) {

    }


}

package com.example.aljaz.tutor4u;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aljaz.tutor4u.listViewAllTermins.ListViewTerminsAdapter;
import com.example.aljaz.tutor4u.listViewAllTermins.ModelAllTermins;
import com.example.aljaz.tutor4u.listViewAllTutors.ModelAllTutors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;


public class TutorProfileInfo extends Fragment {
    TextView name, grade, email, phone, address;
    ListView listView;
    ListViewTerminsAdapter adapter;
    ArrayList<ModelAllTermins> arrayList = new ArrayList<>();

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

        listView = view.findViewById(R.id.tutorTermins);

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
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/MyTerminTutor/" + tutorId);
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
                                ArrayList<ModelAllTermins> modelAllTermins = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH-mm");
                                    Date date = dt.parse(jsonObject.getString("date"));
                                    SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    String finalDate = dt1.format(date);
                                    Date dateOfTerm = dt1.parse(finalDate);

                                    String id_termin = jsonObject.getString("idTermin");
                                    String price = jsonObject.getString("price");
                                    String subjectName = jsonObject.getString("subject");
                                    ModelAllTermins newModelAllTermins = new ModelAllTermins(subjectName, dateOfTerm, price + " €", id_termin);
                                    if (!new Date().after(dateOfTerm)) {
                                        modelAllTermins.add(newModelAllTermins);
                                    }
                                }

                                arrayList.addAll(modelAllTermins);
                                arrayList.sort(new Comparator<ModelAllTermins>() {
                                    @Override
                                    public int compare(ModelAllTermins o1, ModelAllTermins o2) {
                                        return o1.getDate().compareTo(o2.getDate());
                                    }
                                });
                                Collections.reverse(arrayList);

                                try {
                                    adapter = new ListViewTerminsAdapter(getContext(), arrayList);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                listView.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
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
}

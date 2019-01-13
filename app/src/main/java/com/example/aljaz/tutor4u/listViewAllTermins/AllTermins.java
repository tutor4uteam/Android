package com.example.aljaz.tutor4u.listViewAllTermins;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.TakeTermin;
import com.example.aljaz.tutor4u.listViewAllSubjects.AllSubjects;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class AllTermins extends Fragment {
    private RequestQueue requestQueue;
    ListView listView;
    ListViewTerminsAdapter adapter;
    ArrayList<ModelAllTermins> arrayList;
    private ProgressBar spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_termins, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All termins");
        spinner = view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        Bundle bundle = getArguments();
        final String idSubject = bundle.getString("id_subject");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getTermins(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        if (result.size() == 0)
                            Toast.makeText(getContext(), "Error, please try again later", Toast.LENGTH_LONG);
                        arrayList.addAll(result);
                        arrayList.sort(new Comparator<ModelAllTermins>() {
                            @Override
                            public int compare(ModelAllTermins o1, ModelAllTermins o2) {
                                return o1.date.compareTo(o2.date);
                            }
                        });
                        Collections.reverse(arrayList);
                        try {
                            adapter = new ListViewTerminsAdapter(getContext(), arrayList);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listView.setAdapter(adapter);
                    }
                }, idSubject);
                spinner.setVisibility(View.GONE);
            }
        }, 1000);

        listView = view.findViewById(R.id.listViewTermins);
        if (userInfo.getRole().equals("student")) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TakeTermin takeTermin = new TakeTermin();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("termin", arrayList.get(position));

                    takeTermin.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.flcontent, takeTermin)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }


        return view;
    }

    private void getTermins(final VolleyCallback callback, String idSubject) {
        String url = "http://apitutor.azurewebsites.net/RestServiceImpl.svc/TerminInfoBySubjectId/" + idSubject;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<ModelAllTermins> modelAllTermins = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH-mm");
                        Date date = dt.parse(jsonObject.getString("date"));
                        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        String finalDate = dt1.format(date);
                        Date dateOfTerm = dt1.parse(finalDate);

                        String grade = jsonObject.getString("grade");
                        String id_termin = jsonObject.getString("idTermin");
                        String id_tutor = jsonObject.getString("idTutor");
                        String price = jsonObject.getString("price");
                        String tutorName = jsonObject.getString("tutorName") + " " + jsonObject.getString("tutorLastname");
                        ModelAllTermins newModelAllTermins = new ModelAllTermins(id_tutor, tutorName, dateOfTerm, price + " €", id_termin);
                        if (!new Date().after(dateOfTerm)) {
                            modelAllTermins.add(newModelAllTermins);
                        }

                            if (modelAllTermins.isEmpty()) {
                                Toast.makeText(getContext(), "There are currently no termins for this subject", Toast.LENGTH_LONG).show();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.flcontent, new AllSubjects())
                                        .commit();
                            }
                    }
                    callback.onSuccess(modelAllTermins);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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


    public interface VolleyCallback {
        void onSuccess(ArrayList result);
    }
}

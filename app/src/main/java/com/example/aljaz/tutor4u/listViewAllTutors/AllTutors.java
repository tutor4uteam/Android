package com.example.aljaz.tutor4u.listViewAllTutors;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.TutorProfileInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AllTutors extends Fragment {
    private RequestQueue requestQueue;
    ListView listView;
    ListViewTutorsAdapter adapter;
    ArrayList<ModelAllTutors> arrayList = new ArrayList<>();
    private ProgressBar spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tutors, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All tutors");
        spinner = view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getTutors(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        arrayList.addAll(result);
                        Collections.sort(arrayList, new Comparator<ModelAllTutors>() {
                            @Override
                            public int compare(ModelAllTutors o1, ModelAllTutors o2) {
                                return o1.getProfile_grade().compareTo(o2.getProfile_grade());
                            }
                        });
                        Collections.reverse(arrayList);
                        System.out.println("Array size from onCreate: " + arrayList.size());
                        try {
                            adapter = new ListViewTutorsAdapter(getContext(), arrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listView.setAdapter(adapter);
                    }
                });
                spinner.setVisibility(View.GONE);
            }
        }, 1000);

        listView = view.findViewById(R.id.listViewTutors);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TutorProfileInfo tutorProfileInfo = new TutorProfileInfo();

                Bundle bundle = new Bundle();
                bundle.putSerializable("id_tutor", arrayList.get(position));

                tutorProfileInfo.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, tutorProfileInfo)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });

    }

    private void getTutors(final VolleyCallback callback) {
        String getTutor = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getTutor, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<ModelAllTutors> modelAllTutors = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("idTutor");
                        String surname = jsonObject.getString("surname");
                        String address = jsonObject.getString("address");
                        String mail = jsonObject.getString("mail");
                        String phone = jsonObject.getString("phone");
                        String grade = jsonObject.getString("grade");
                        ModelAllTutors newModelAllTutors = new ModelAllTutors(id, "", name, surname, address, mail, phone, grade);
                        modelAllTutors.add(newModelAllTutors);
                    }

                    callback.onSuccess(modelAllTutors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList result);
    }


}

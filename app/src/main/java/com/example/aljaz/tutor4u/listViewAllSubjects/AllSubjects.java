package com.example.aljaz.tutor4u.listViewAllSubjects;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.Subject;
import com.example.aljaz.tutor4u.Helpers.Termin;
import com.example.aljaz.tutor4u.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllSubjects extends Fragment {
    private RequestQueue requestQueue;
    ListView listView;
    ListViewSubjectsAdapter adapter;
    ArrayList<ModelAllSubjects> arrayList = new ArrayList<>();
    ArrayList<Subject> subjectsArray = new ArrayList<>();
    ArrayList<Termin> termsArray = new ArrayList<>();
    private ProgressBar spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getContext());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_subjects, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All subjects");
        spinner = view.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubjects(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        arrayList.addAll(result);
                        System.out.println("Array size from onCreate: " + arrayList.size());
                        adapter = new ListViewSubjectsAdapter(getContext(), arrayList);
                        listView.setAdapter(adapter);
                    }
                });
                spinner.setVisibility(View.GONE);
            }
        }, 500);

        listView = view.findViewById(R.id.listViewSubjects);
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
                if (TextUtils.isEmpty(newText)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }else {
                    adapter.filter(newText);
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    private void getSubjects(final VolleyCallback callback){
        String getSubject = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Subject");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getSubject,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<ModelAllSubjects> modelAllSubjects = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        //subjectName[i] = name;
                        Subject subject = new Subject(id, name);
                        ModelAllSubjects newModelAllSubjects = new ModelAllSubjects(name, "10");
                        modelAllSubjects.add(newModelAllSubjects);
                    }

                    callback.onSuccess(modelAllSubjects);
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

    public interface VolleyCallback{
        void onSuccess(ArrayList result);
    }
}

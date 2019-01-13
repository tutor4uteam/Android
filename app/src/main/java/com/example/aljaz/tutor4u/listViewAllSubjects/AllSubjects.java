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
import com.example.aljaz.tutor4u.AddSubjectDialog;
import com.example.aljaz.tutor4u.listViewAllTermins.AllTermins;
import com.example.aljaz.tutor4u.Helpers.Subject;
import com.example.aljaz.tutor4u.Helpers.Termin;
import com.example.aljaz.tutor4u.R;
import com.google.android.gms.tasks.TaskCompletionSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllSubjects extends Fragment {
    private RequestQueue requestQueue;
    ListView listView;
    ListViewSubjectsAdapter adapter;
    ArrayList<ModelAllSubjects> arrayList;
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
        arrayList = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubjects(new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList result) {
                        arrayList.addAll(result);
                        System.out.println("Array size from onCreate: " + arrayList.size());
                        try {
                            adapter = new ListViewSubjectsAdapter(getContext(), arrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        listView.setAdapter(adapter);
                    }

                });
                spinner.setVisibility(View.GONE);
            }
        }, 500);

        listView = view.findViewById(R.id.listViewSubjects);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getContext(), "This is my Toast message! "+arrayList.get(position).subjectName,
                        Toast.LENGTH_LONG).show();*/
                AllTermins allTermins = new AllTermins();
                Bundle bundle = new Bundle();
                bundle.putString("id_subject", arrayList.get(position).id_subject);

                allTermins.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, allTermins)
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
        final String getSubject = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Subject");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getSubject,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<ModelAllSubjects> modelAllSubjects = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String freeTerms = jsonObject.getString("freeTermin").equals("1") ? jsonObject.getString("freeTermin") + " free term" : jsonObject.getString("freeTermin") + " free terms";
                        ModelAllSubjects newModelAllSubjects = new ModelAllSubjects(id, name, freeTerms);
                        modelAllSubjects.add(newModelAllSubjects);
                    }
                    if (modelAllSubjects.isEmpty()) modelAllSubjects.add(new ModelAllSubjects("", "There are no terms for this subject", ""));

                    callback.onSuccess(modelAllSubjects);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Sorry there was a problem. Please try later", Toast.LENGTH_LONG);
                //System.out.println("Error: " + error.toString());
            }
        });

        requestQueue.add(request);
    }



    public interface VolleyCallback{
        void onSuccess(ArrayList result);
    }
}

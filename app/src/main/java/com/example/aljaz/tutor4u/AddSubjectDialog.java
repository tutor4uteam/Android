package com.example.aljaz.tutor4u;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aljaz.tutor4u.Helpers.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddSubjectDialog extends AppCompatDialogFragment {
    ArrayList<Subject> subjectsArray;
    ArrayAdapter<Subject> adapter;
    private RequestQueue requestQueue;

    private Spinner subjects;
    private EditText price;
    private AddSubjectDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getContext());

        subjectsArray = new ArrayList<>();
        subjectsArray.add(new Subject("0", "Choose from all subjects"));


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_subject, null);

        builder.setView(view)
                .setTitle("Add Subject")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Subject subject = (Subject) subjects.getSelectedItem();
                String subject_id = subject.getId_subject();
                if (subject_id.equals("0") && price.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        listener.applyChange(subject, price.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        querySubjects();

        subjects = view.findViewById(R.id.spin_subjects_dialog);
        price = view.findViewById(R.id.price_subject_dialog);

        return builder.create();
    }

    private void querySubjects() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSubjects(new VolleyCallback() {

                    @Override
                    public void onSuccess(ArrayList result) {
                        subjectsArray.addAll(result);
                        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectsArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjects.setAdapter(adapter);
                        subjects.setEnabled(true);
                    }
                });
            }
        }, 0);
    }

    private void getSubjects(final VolleyCallback callback) {
        final String getSubject = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/SubjectList");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getSubject, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<Subject> modelSubjectSpinners = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        Subject newSubject = new Subject(id, name);
                        modelSubjectSpinners.add(newSubject);
                    }

                    callback.onSuccess(modelSubjectSpinners);
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


    public interface VolleyCallback {
        void onSuccess(ArrayList result);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddSubjectDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface AddSubjectDialogListener {
        void applyChange(Subject subject, String price);
    }

}

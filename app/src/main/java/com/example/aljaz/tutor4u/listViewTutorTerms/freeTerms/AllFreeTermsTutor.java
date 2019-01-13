package com.example.aljaz.tutor4u.listViewTutorTerms.freeTerms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.listViewStudentTerms.ListViewStudentTermsAdapter;
import com.example.aljaz.tutor4u.listViewStudentTerms.ModelStudentTerm;
import com.google.gson.Gson;

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

public class AllFreeTermsTutor extends Fragment {

    SwipeMenuListView listView;
    ListViewFreeTermsAdapter adapter;
    ArrayList<ModelFreeTerms> arrayList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_free_terms_tutor, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Free terms");

        SharedPreferences mPrefs = getActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo userInfo = gson.fromJson(json, UserInfo.class);

        arrayList = new ArrayList<>();
        listView = view.findViewById(R.id.swipeFreeTerms);

        getFreeTermins(userInfo.getUser_id());

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new AlertDialog.Builder(getContext())
                                .setTitle("Deleting termin...")
                                .setMessage("Do you really want to delete this termin?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        deleteTermin(arrayList.get(position));
                                        arrayList.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                        // open
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        // Left
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        return view;
    }

    private void deleteTermin(ModelFreeTerms modelFreeTerms) {
        String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/DeleteTermin/%s", modelFreeTerms.getIdTermin());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(), "Term cant be taken", Toast.LENGTH_LONG).show();
                Log.i("Debug", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(myResponse);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String id = jsonObject.getString("result");

                            if (id.equals("1")) {
                                Toast.makeText(getContext(), "Termin deleted", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), "Can't delete termin", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void getFreeTermins(String user_id) {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/MyTerminTutor/" + user_id);
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

                final SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getContext());
                        // set item background
                        deleteItem.setBackground(R.color.cardview_dark_background);
                        // set item width
                        deleteItem.setWidth(170);
                        // set a icon
                        deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                    }
                };

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                ArrayList<ModelFreeTerms> modelFreeTermins = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String terminId = jsonObject.getString("idTermin");
                                    String price = jsonObject.getString("price");
                                    String subject = jsonObject.getString("subject");

                                    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH-mm");
                                    Date date = dt.parse(jsonObject.getString("date"));
                                    SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    String finalDate = dt1.format(date);
                                    Date dateOfTerm = dt1.parse(finalDate);

                                    ModelFreeTerms newModelAllSubjects = new ModelFreeTerms(dateOfTerm, terminId, price,subject);
                                    if (!new Date().after(dateOfTerm)) {
                                        modelFreeTermins.add(newModelAllSubjects);
                                    }
                                }

                                arrayList.addAll(modelFreeTermins);
                                arrayList.sort(new Comparator<ModelFreeTerms>() {
                                    @Override
                                    public int compare(ModelFreeTerms o1, ModelFreeTerms o2) {
                                        return o1.getDate().compareTo(o2.getDate());
                                    }
                                });
                                Collections.reverse(arrayList);
                                try {
                                    adapter = new ListViewFreeTermsAdapter(getContext(), arrayList);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                listView.setAdapter(adapter);
                                listView.setMenuCreator(creator);



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

package com.example.aljaz.tutor4u.map;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.TutorProfileInfo;
import com.example.aljaz.tutor4u.listViewAllTutors.ModelAllTutors;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class MapOfTutorsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_of_tutors);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override

    public void onMapReady(GoogleMap googleMap) {
        getAllTutors();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng ljubljana = new LatLng(46, 15);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ljubljana, (float) 7.5));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TutorProfileInfo tutorProfileInfo = new TutorProfileInfo();
                TutorOnMap tutorOnMap = (TutorOnMap) marker.getTag();

                ModelAllTutors m = new ModelAllTutors(tutorOnMap.getId(), null, tutorOnMap.getName(), tutorOnMap.getSurname(), tutorOnMap.getAddress(), tutorOnMap.getMail(), tutorOnMap.getPhone(), tutorOnMap.getGrade());

                Bundle bundle = new Bundle();
                bundle.putSerializable("id_tutor", m);

                tutorProfileInfo.setArguments(bundle);

                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flcontent, tutorProfileInfo)
                        .addToBackStack(null)
                        .commit();*/
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.hide(getSupportFragmentManager().findFragmentById(R.id.flcontent));
                return false;
            }
        });
    }

    private void getAllTutors() {
        final String url = String.format("http://apitutor.azurewebsites.net/RestServiceImpl.svc/Tutor");
        final ArrayList<TutorOnMap> tutors = new ArrayList<>();
        final Geocoder geocoder = new Geocoder(getBaseContext());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getBaseContext(), "Sorry there was a problem getting data. Please try later", Toast.LENGTH_LONG).show();
                Log.i("Debug", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();

                try {
                    MapOfTutorsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String surname = jsonObject.getString("surname");
                                    String address = jsonObject.getString("address");
                                    String mail = jsonObject.getString("mail");
                                    String phone = jsonObject.getString("phone");
                                    String grade = jsonObject.getString("grade");
                                    String idTutor = jsonObject.getString("idTutor");
                                    TutorOnMap tutorOnMap = new TutorOnMap(idTutor, name, surname, address, mail, phone, grade);
                                    tutors.add(tutorOnMap);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                for (TutorOnMap tutor : tutors) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(geocoder.getFromLocationName(tutor.getAddress(), 1).get(0).getLatitude(),
                                            geocoder.getFromLocationName(tutor.getAddress(), 1).get(0).getLongitude())).title(tutor.getName() + " " + tutor.getSurname())).setTag(tutor);
                                }

                            } catch (IOException e) {
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
package com.example.aljaz.tutor4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aljaz.tutor4u.Helpers.Tutor;
import com.example.aljaz.tutor4u.Helpers.UserInfo;
import com.google.gson.Gson;

public class TutorMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main);
        toolbar = findViewById(R.id.tutorToolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_view);

        //final UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        // Retrive user info
        SharedPreferences mPrefs = getSharedPreferences("User_info", MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        if (!(this.getSharedPreferences("Login", MODE_PRIVATE).contains("Username"))){
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.flcontent, LoginActivity.class.newInstance(), "Login")
                        .commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else {


            Gson gson = new Gson();
            final UserInfo user = gson.fromJson(json, UserInfo.class);

            View headerView = navigationView.getHeaderView(0);
            TextView userName = headerView.findViewById(R.id.first_last_name);
            TextView email = headerView.findViewById(R.id.mail);
            userName.setText(user.getFirsname() + " " + user.getLastname() + " (" + user.getRole() + ")");
            email.setText(user.getMail());


            Fragment fragment = null;
            String tag = null;

            try {
                fragment = Dashboard.class.newInstance();
                tag = "Dashboard";
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment, tag).commit();

            drawerLayout = findViewById(R.id.drawer_tutor);
            toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setupDrawerContent(navigationView);


            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment userInfoFragment = null;
                    try {
                        if(user.getRole().equals("tutor"))userInfoFragment = UserInfoScreen.class.newInstance();
                        else userInfoFragment = UserInfoStudent.class.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UserInfo", user);
                    userInfoFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.flcontent, userInfoFragment, "User info")
                            .addToBackStack(null)
                            .commit();
                    drawerLayout.closeDrawers();
                }
            });
        }
    }



    public void selectIterDrawer(MenuItem menuItem){
        Intent intent = null;
        Fragment fragment = null;
        Class fragmentClass= null;
        String tag = null;
        switch (menuItem.getItemId()){
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("Login",this.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                SharedPreferences pre = getSharedPreferences("User_info",this.MODE_PRIVATE);
                SharedPreferences.Editor pref = pre.edit();
                pref.clear();
                pref.commit();




                intent = new Intent(getApplicationContext(), TutorMainActivity.class);
                //fragmentClass = Dashboard.class;
                tag = "Login";
                break;
            case R.id.db:
                fragmentClass = Dashboard.class;
                tag = "Dashboard";
                break;
            case R.id.settings:
                fragmentClass = Settings.class;
                tag = "Settings";
                break;
        }
        if (intent != null){
            startActivity(intent);
        }
        else {
            try{
                fragment = (Fragment) fragmentClass.newInstance();
            }catch (Exception e){
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment, tag).addToBackStack(null).commit();
            drawerLayout.closeDrawers();
        }
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectIterDrawer(item);
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getClass().equals("Dashboard")) finish();

        super.onBackPressed();
    }
}


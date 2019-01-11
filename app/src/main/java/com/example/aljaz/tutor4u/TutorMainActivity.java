package com.example.aljaz.tutor4u;

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

    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main);
        toolbar = findViewById(R.id.tutorToolbar);
        setSupportActionBar(toolbar);
        final NavigationView navigationView = findViewById(R.id.navigation_view);

        //final UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        // Retrive user info
        SharedPreferences mPrefs = getSharedPreferences("User_info", MODE_PRIVATE);
        String json = mPrefs.getString("Profile_info", null);
        Gson gson = new Gson();
        final UserInfo tutor = gson.fromJson(json, UserInfo.class);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.first_last_name);
        TextView email = headerView.findViewById(R.id.mail);
        userName.setText(tutor.getFirsname()+" "+tutor.getLastname());
        email.setText(tutor.getMail());


        Fragment fragment = null;
        try {
            fragment = Dashboard.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();

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
                     userInfoFragment = UserInfoScreen.class.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("UserInfo", tutor);
                userInfoFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.flcontent, userInfoFragment, "Dashboard")
                        .addToBackStack(null)
                        .commit();
                drawerLayout.closeDrawers();
            }
        });
    }



    public void selectIterDrawer(MenuItem menuItem){
        Intent intent = null;
        Fragment fragment = null;
        Class fragmentClass= null;
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

                intent = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            case R.id.db:
                fragmentClass = Dashboard.class;
                break;
            case R.id.settings:
                fragmentClass = Settings.class;
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
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).addToBackStack(null).commit();
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


}


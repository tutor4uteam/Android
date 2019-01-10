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

import com.example.aljaz.tutor4u.Helpers.Student;
import com.example.aljaz.tutor4u.Helpers.UserInfo;

public class StudentMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_view);
        toolbar = findViewById(R.id.studentToolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("UserInfo");

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

        drawerLayout = findViewById(R.id.drawer_student);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(navigationView);
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
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
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


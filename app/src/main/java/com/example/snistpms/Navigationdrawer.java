package com.example.snistpms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

public class Navigationdrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationdrawer);
        Toolbar toolbar1=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        drawerLayout=findViewById(R.id.navigationdrawer);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar1,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Materialactivity()).commit();
            navigationView.setCheckedItem(R.id.nav_materials);
        }

    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_materials:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Materialactivity()).addToBackStack(null).commit();
                break;
            case  R.id.nav_totalsubs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new showandaddsubjects()).addToBackStack(null).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

package com.example.updatedputatoedelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.updatedputatoedelivery.Fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    //when user application, this activity shows for the first time


    //reference variable of the bottom navigation view
    private BottomNavigationView bottomNavigationView;

    //reference variable of the fragment
    private Fragment selectedFragment;


    //reference variable of the toolbar
    private Toolbar toolbar;


    //navigation item selected listener
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

           switch (item.getItemId()) {


                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;

                    case R.id.navigation_activitye:
                        break;

                    case R.id.navigation_summary:

                        break;

                    case R.id.navigation_calculator:
                        break;

                    case R.id.navigation_trackOrders:

                        break;

                }

            if(selectedFragment != null)
            {
                if(selectedFragment instanceof  HomeFragment ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment,"home")
                            .commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer , selectedFragment)
                            .commit();
                }
            }




            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //init the toolbar
        toolbar = findViewById(R.id.toolbar);
         // set toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set title of action bar
        getSupportActionBar().setTitle("Putatoe Delivery");



        //init the bottom navigation views
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);

        //set navigationItem selected listener to the bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer , new HomeFragment() , "Home")
                .commit();





    }
}
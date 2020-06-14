package com.example.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialise();
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main_id, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_item_home_id:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_item_bmi_id:
                            selectedFragment = new BmiFragment();
                            break;

                        case R.id.nav_item_profile_id:
                            selectedFragment = new ProfileFragment();
                            break;
                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main_id, selectedFragment).commit();

                    return true;
                }
            };

    public void Initialise()
    {
        bottomNav = findViewById(R.id.bottom_nav_main_id);
    }
}

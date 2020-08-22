package com.example.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    public String perdayCal;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDataReference, perdayCalRef, todayCalorieReference, activityLevelReference;
    private static String TAG = "Main Activity ";
    private BmiFragment bmiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialise();
        Intent intent = getIntent();
        String calSum = intent.getStringExtra("sum");
        if (calSum!=null)
        {
            bmiFragment.getCalSum(calSum);
        }
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
                            selectedFragment = bmiFragment;
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
        firebaseAuth = FirebaseAuth.getInstance();
        bmiFragment = new BmiFragment();
        perdayCalRef = FirebaseDatabase.getInstance().getReference("perday_calorie");
        perdayCalRef.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    perdayCal = dataSnapshot.child("calorie").getValue().toString().trim();
                    Log.d(TAG, "In perday calorie intake : " + perdayCal);
                } else {
                    Log.d(TAG, "Perday calorie intake not present");
                    perdayCal="0";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this
                        , databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

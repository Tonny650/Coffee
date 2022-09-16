package com.thisname.coffee.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thisname.coffee.R;
import com.thisname.coffee.activitys.fragments.ChatFragment;
import com.thisname.coffee.activitys.fragments.FilterFragment;
import com.thisname.coffee.activitys.fragments.HomeFragment;
import com.thisname.coffee.activitys.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_home:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.item_filter:
                    openFragment(new FilterFragment());
                    return true;
                case R.id.item_chat:
                    openFragment(new ChatFragment());
                    return true;
                case R.id.item_profile:
                    openFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };
}
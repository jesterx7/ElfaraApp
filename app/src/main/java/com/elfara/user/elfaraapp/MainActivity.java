package com.elfara.user.elfaraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.ui.AccessSettingsFragment;
import com.elfara.user.elfaraapp.ui.DateReadData;
import com.elfara.user.elfaraapp.ui.FormFragment;
import com.elfara.user.elfaraapp.ui.MainFragment;
import com.elfara.user.elfaraapp.ui.ReadDataFragment;
import com.elfara.user.elfaraapp.ui.SettingsFragment;
import com.elfara.user.elfaraapp.ui.SummarySelling;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(new MainFragment());
                    return true;
                case R.id.navigation_settings:
                    changeFragment(new SettingsFragment());
                    return true;
                case R.id.navigation_logout:
                    Session session = new Session(getApplicationContext());
                    session.clearSession();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new MainFragment());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackCount == 1) {
            this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    public void goToFormForm(View view) {
        changeFragment(new FormFragment());
    }

    public void goToSummarySelling(View view) {
        changeFragment(new SummarySelling());
    }

    public void goToAccessSettings(View view) {
        changeFragment(new AccessSettingsFragment());
    }

    public void goToReadData(View view) {
        changeFragment(new DateReadData());
    }
}

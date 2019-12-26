package com.elfara.user.elfaraapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.ui.AccessSettingsFragment;
import com.elfara.user.elfaraapp.ui.AddUserFragment;
import com.elfara.user.elfaraapp.ui.DateReadData;
import com.elfara.user.elfaraapp.ui.FormFragment;
import com.elfara.user.elfaraapp.ui.MainFragment;
import com.elfara.user.elfaraapp.ui.SettingsFragment;
import com.elfara.user.elfaraapp.ui.SummarySelling;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(new MainFragment());

        session = new Session(this);
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

    public void goToAccessSettings(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Password");
        final EditText edtInputPassword = new EditText(this);
        edtInputPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(edtInputPassword);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edtInputPassword.getText().toString().equals(session.getSession("password"))) {
                    changeFragment(new AccessSettingsFragment());
                } else {
                    edtInputPassword.setText("");
                    Toast.makeText(view.getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    public void goToReadData(View view) {
        changeFragment(new DateReadData());
    }

    public void goToAddUser(View view) {
        changeFragment(new AddUserFragment());
    }
}

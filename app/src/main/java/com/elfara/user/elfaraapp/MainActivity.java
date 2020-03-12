package com.elfara.user.elfaraapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.User;
import com.elfara.user.elfaraapp.ui.AccessSettingsFragment;
import com.elfara.user.elfaraapp.ui.AddUserFragment;
import com.elfara.user.elfaraapp.ui.DateReadData;
import com.elfara.user.elfaraapp.ui.EventTitleFragment;
import com.elfara.user.elfaraapp.ui.FormFragment;
import com.elfara.user.elfaraapp.ui.MainFragment;
import com.elfara.user.elfaraapp.ui.SettingsFragment;
import com.elfara.user.elfaraapp.ui.SummarySelling;
import com.elfara.user.elfaraapp.ui.UploadPhotoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    FunctionEventLog functionEventLog = new FunctionEventLog(getApplicationContext());
                    functionEventLog.writeEventLog("Logout");
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
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> userCall = apiInterface.authLogin(
                        session.getSession("email"),
                        edtInputPassword.getText().toString()
                );
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body().getSuccess()) {
                            changeFragment(new AccessSettingsFragment());
                        } else {
                            edtInputPassword.setText("");
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error, check your connection!", Toast.LENGTH_SHORT).show();
                    }
                });
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

    public void goToEventTitle(View view) {
        changeFragment(new EventTitleFragment());
    }

    public void goToUploadPhoto(View view) {
        changeFragment(new UploadPhotoFragment());
    }
}

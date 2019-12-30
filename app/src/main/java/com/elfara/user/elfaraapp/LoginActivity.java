package com.elfara.user.elfaraapp;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private Session session;
    private Boolean pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);
        if (!session.getSession("name").isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBarLogin);

        pass = true;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = checkField();
                if (pass) {
                    progressBar.setVisibility(View.VISIBLE);
                    User userLog = new User();
                    userLog.setEmail(edtEmail.getText().toString());
                    userLog.setPassword(edtPassword.getText().toString().trim());
                    auth(userLog);
                } else {
                    Toast.makeText(getApplicationContext(), "Field cant be empty !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean checkField() {
        if (edtEmail.getText().toString().isEmpty()) return false;
        else if (edtPassword.getText().toString().isEmpty()) return false;
        else return true;
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }

    public void auth(final User user) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> userCall = apiInterface.authLogin(
           user.getEmail(), user.getPassword()
        );

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    session.setSession("iduser", String.valueOf(response.body().getId()));
                    session.setSession("name", response.body().getName());
                    session.setSession("password", response.body().getPassword());
                    session.setSession("email", response.body().getEmail());
                    session.setSession("level", String.valueOf(response.body().getLevel()));
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed, " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Login Failed!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}

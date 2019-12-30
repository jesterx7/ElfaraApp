package com.elfara.user.elfaraapp.Function;

import android.content.Context;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FunctionEventLog {
    private Context context;
    private Session session;

    public FunctionEventLog(Context context) {
        this.context = context;
        session = new Session(this.context);
    }

    public void writeEventLog(String detail) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<com.elfara.user.elfaraapp.Model.EventLog> eventLogCall = apiInterface.writeEventLog(
                Integer.parseInt(session.getSession("iduser")), detail
        );

        eventLogCall.enqueue(new Callback<com.elfara.user.elfaraapp.Model.EventLog>() {
            @Override
            public void onResponse(Call<com.elfara.user.elfaraapp.Model.EventLog> call, Response<com.elfara.user.elfaraapp.Model.EventLog> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {

                } else {
                    Toast.makeText(context, "Failed to Write", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.elfara.user.elfaraapp.Model.EventLog> call, Throwable t) {
                Toast.makeText(context, "Error to Write", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

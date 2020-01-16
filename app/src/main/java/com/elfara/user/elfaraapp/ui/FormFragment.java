package com.elfara.user.elfaraapp.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.EventLog;
import com.elfara.user.elfaraapp.Model.InputData;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {
    private View view;
    private LinearLayout linearLayout;
    private EditText edtNama, edtTanggal, edtAlamat, edtUmur, edtTelp, edtSelling, edtSampling, edtMedsos;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private Boolean pass;
    private FunctionEventLog functionEventLog;

    private final Calendar calendar = Calendar.getInstance();
    private final String DATEFORMATINPUT = "YYYY-MM-dd";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATEFORMATINPUT, Locale.US);
    private DatePickerDialog.OnDateSetListener dateListenerTanggal = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            edtTanggal.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };

    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_form, container, false);

        linearLayout = view.findViewById(R.id.llForm);
        edtNama = view.findViewById(R.id.edtNamaForm);
        edtTanggal = view.findViewById(R.id.edtTanggalForm);
        edtAlamat = view.findViewById(R.id.edtAlamatForm);
        edtMedsos = view.findViewById(R.id.edtInstagramForm);
        edtSampling = view.findViewById(R.id.edtJumlahSamplingForm);
        edtSelling = view.findViewById(R.id.edtJumlahSellingForm);
        edtTelp = view.findViewById(R.id.edtTelpForm);
        edtUmur = view.findViewById(R.id.edtUmurForm);
        btnSubmit = view.findViewById(R.id.btnSubmitForm);
        progressBar = view.findViewById(R.id.progressBarForm);

        pass = true;
        functionEventLog = new FunctionEventLog(view.getContext());

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateListenerTanggal, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = checkField();
                if (pass) {
                    progressBar.setVisibility(View.VISIBLE);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date c = Calendar.getInstance().getTime();

                    InputData inputData = new InputData();
                    inputData.setNamapelanggan(edtNama.getText().toString());
                    inputData.setTanggal(edtTanggal.getText().toString());
                    inputData.setAlamat(edtAlamat.getText().toString());
                    inputData.setMediasosial(edtMedsos.getText().toString());
                    inputData.setNomortelepon(edtTelp.getText().toString());
                    inputData.setSelling(Integer.valueOf(edtSelling.getText().toString()));
                    inputData.setSampling(Integer.valueOf(edtSampling.getText().toString()));
                    if (!edtUmur.getText().toString().isEmpty())
                        inputData.setUmur(Integer.parseInt(edtUmur.getText().toString()));
                    else
                        inputData.setUmur(0);
                    inputData.setIdsales(1);
                    saveForm(inputData);
                } else {
                    Toast.makeText(view.getContext(), "Nama, Tanggal, Selling, & Sampling is Mandatory !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Input Data");
    }

    private Boolean checkField() {
        if (edtNama.getText().toString().isEmpty()) return false;
        else if (edtTanggal.getText().toString().isEmpty()) return false;
        else if (edtSelling.getText().toString().isEmpty()) return false;
        else if (edtSampling.getText().toString().isEmpty()) return false;
        else return true;
    }

    public void saveForm(InputData inputData) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<InputData> sellingCall = apiInterface.saveNote(
                inputData.getNamapelanggan(), inputData.getTanggal(), inputData.getUmur(),
                inputData.getAlamat(), inputData.getNomortelepon(), inputData.getMediasosial(),
                inputData.getSelling(), inputData.getSampling(), inputData.getIdsales());

        sellingCall.enqueue(new Callback<InputData>() {
            @Override
            public void onResponse(Call<InputData> call, Response<InputData> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    clearField();
                    functionEventLog.writeEventLog("Insert Transaction");
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Response", Toast.LENGTH_SHORT).show();
                    clearField();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<InputData> call, Throwable t) {
                Toast.makeText(view.getContext(), "Insert Failed!!", Toast.LENGTH_SHORT).show();
                clearField();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void clearField() {
        edtNama.setText("");
        edtTanggal.setText("");
        edtUmur.setText("");
        edtAlamat.setText("");
        edtTelp.setText("");
        edtMedsos.setText("");
        edtSelling.setText("");
        edtSampling.setText("");
    }

}

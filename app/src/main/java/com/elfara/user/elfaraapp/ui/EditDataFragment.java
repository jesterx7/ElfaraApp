package com.elfara.user.elfaraapp.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDataFragment extends Fragment {
    private View view;
    private EditText edtNama, edtTanggal, edtAlamat, edtUmur, edtTelp, edtSelling, edtSampling, edtMedsos;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private String idtransaksi;
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

    public EditDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = inflater.inflate(R.layout.fragment_edit_data, container, false);

        edtNama = view.findViewById(R.id.edtNamaEditData);
        edtTanggal = view.findViewById(R.id.edtTanggalEditData);
        edtAlamat = view.findViewById(R.id.edtAlamatEditData);
        edtUmur = view.findViewById(R.id.edtUmurEditData);
        edtTelp = view.findViewById(R.id.edtTelpEditData);
        edtSelling = view.findViewById(R.id.edtJumlahSellingEditData);
        edtSampling = view.findViewById(R.id.edtJumlahSamplingEditData);
        edtMedsos = view.findViewById(R.id.edtInstagramEditData);
        btnSubmit = view.findViewById(R.id.btnSubmitEditData);
        progressBar = view.findViewById(R.id.progressBarEditData);

        functionEventLog = new FunctionEventLog(view.getContext());
        idtransaksi = getArguments().getString("idtransaksi");
        pass = true;

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateListenerTanggal, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setData(idtransaksi);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = checkField();
                if (pass) {
                    ReadData read = new ReadData();
                    read.setIdtransaksi(Integer.valueOf(idtransaksi));
                    read.setNamapelanggan(edtNama.getText().toString());
                    if (!edtUmur.getText().toString().isEmpty())
                        read.setUmur(Integer.parseInt(edtUmur.getText().toString()));
                    else
                        read.setUmur(0);
                    read.setAlamat(edtAlamat.getText().toString());
                    read.setNomortelepon(edtTelp.getText().toString());
                    read.setMediasosial(edtMedsos.getText().toString());
                    read.setSelling(Integer.valueOf(edtSelling.getText().toString()));
                    read.setSampling(Integer.valueOf(edtSampling.getText().toString()));
                    read.setTanggal(edtTanggal.getText().toString());

                    updateData(read);
                } else {
                    Toast.makeText(view.getContext(), "Nama, Tanggal, Selling, & Sampling must be filled !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Edit Data");
    }

    private void setData(String idtransaksi) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReadData> dataCall = apiInterface.getDataTransaksi(
                Integer.valueOf(idtransaksi)
        );

        dataCall.enqueue(new Callback<ReadData>() {
            @Override
            public void onResponse(Call<ReadData> call, Response<ReadData> response) {
                if (response.isSuccessful()) {
                    edtNama.setText(response.body().getNamapelanggan());
                    edtAlamat.setText(response.body().getAlamat());
                    edtUmur.setText(String.valueOf(response.body().getUmur()));
                    edtTelp.setText(response.body().getNomortelepon());
                    edtMedsos.setText(response.body().getMediasosial());
                    edtSelling.setText(String.valueOf(response.body().getSelling()));
                    edtSampling.setText(String.valueOf(response.body().getSampling()));
                    edtTanggal.setText(response.body().getTanggal());
                } else {
                    Toast.makeText(view.getContext(), "Failed get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReadData> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData(final ReadData readData) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReadData> dataCall = apiInterface.updateDataTransaksi(
                readData.getIdtransaksi(), readData.getNamapelanggan(), readData.getUmur(),
                readData.getAlamat(), readData.getNomortelepon(), readData.getMediasosial(),
                readData.getSelling(), readData.getSampling(), readData.getTanggal()
        );

        dataCall.enqueue(new Callback<ReadData>() {
            @Override
            public void onResponse(Call<ReadData> call, Response<ReadData> response) {
                if (response.isSuccessful()) {
                    functionEventLog.writeEventLog("Edited Transaction Data with ID " + String.valueOf(readData.getIdtransaksi()));
                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(view.getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReadData> call, Throwable t) {
                Toast.makeText(view.getContext(), "Update Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean checkField() {
        if (edtNama.getText().toString().isEmpty()) return false;
        else if (edtTanggal.getText().toString().isEmpty()) return false;
        else if (edtSelling.getText().toString().isEmpty()) return false;
        else if (edtSampling.getText().toString().isEmpty()) return false;
        else return true;
    }

}

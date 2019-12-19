package com.elfara.user.elfaraapp.Core;

import com.elfara.user.elfaraapp.Model.InputData;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.Model.SummarySample;
import com.elfara.user.elfaraapp.Model.SummarySell;
import com.elfara.user.elfaraapp.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("save.php")
    Call<InputData> saveNote(
            @Field("namapelanggan") String nama,
            @Field("tanggal") String tanggal,
            @Field("tanggallahir") String tanggallahir,
            @Field("alamat") String alamat,
            @Field("nomortelepon") String telp,
            @Field("mediasosial") String instagram,
            @Field("selling") int selling,
            @Field("sampling") int sampling,
            @Field("idsales") int idsales
    );

    @GET("get_data.php")
    Call<List<InputData>> getNote();

    @FormUrlEncoded
    @POST("selling_summary.php")
    Call<List<SummarySell>> getSummarySelling(
            @Field("tanggaldari") String tanggaldari,
            @Field("tanggalsampai") String tanggalsampai
    );

    @FormUrlEncoded
    @POST("sampling_summary.php")
    Call<List<SummarySample>> getSummarySampling(
            @Field("tanggaldari") String tanggaldari,
            @Field("tanggalsampai") String tanggalsampai
    );

    @FormUrlEncoded
    @POST("auth_login.php")
    Call<User> authLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("change_password.php")
    Call<User> changePassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("read_data.php")
    Call<ArrayList<ReadData>> getReadData(
            @Field("tanggaldari") String tanggaldari,
            @Field("tanggalsampai") String tanggalsampai
    );

    @FormUrlEncoded
    @POST("get_data_transaksi.php")
    Call<ReadData> getDataTransaksi(
            @Field("idtransaksi") int idtransaksi
    );

    @FormUrlEncoded
    @POST("update_data_transaksi.php")
    Call<ReadData> updateDataTransaksi(
            @Field("idtransaksi") int idtransaksi,
            @Field("namapelanggan") String namapelanggan,
            @Field("tanggallahir") String tanggallahir,
            @Field("alamat") String alamat,
            @Field("nomortelepon") String nomortelepon,
            @Field("mediasosial") String mediasosial,
            @Field("selling") int selling,
            @Field("sampling") int sampling,
            @Field("tanggal") String tanggal
    );

    @FormUrlEncoded
    @POST("delete_data_transaksi.php")
    Call<ReadData> deleteDataTransaksi(
            @Field("idtransaksi") int idtransaksi
    );
}

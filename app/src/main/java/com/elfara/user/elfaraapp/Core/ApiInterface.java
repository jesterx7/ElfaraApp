package com.elfara.user.elfaraapp.Core;

import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.Model.EventLog;
import com.elfara.user.elfaraapp.Model.ImageAssets;
import com.elfara.user.elfaraapp.Model.InputData;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.Model.SummarySample;
import com.elfara.user.elfaraapp.Model.SummarySell;
import com.elfara.user.elfaraapp.Model.UploadResponse;
import com.elfara.user.elfaraapp.Model.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("save.php")
    Call<InputData> saveNote(
            @Field("namapelanggan") String nama,
            @Field("tanggal") String tanggal,
            @Field("umur") int umur,
            @Field("alamat") String alamat,
            @Field("nomortelepon") String telp,
            @Field("mediasosial") String instagram,
            @Field("selling") int selling,
            @Field("sampling") int sampling,
            @Field("idsales") int idsales
    );

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
            @Field("oldpassword") String oldpassword,
            @Field("confirmpassword") String confirmpassword,
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
            @Field("umur") int umur,
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

    @FormUrlEncoded
    @POST("create_user.php")
    Call<User> addUser(
            @Field("namauser") String namauser,
            @Field("email") String email,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("telp") String telp,
            @Field("status") String status,
            @Field("level") int level
    );

    @FormUrlEncoded
    @POST("get_user.php")
    Call<User> getUser(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("update_user_access.php")
    Call<User> updateUserAccess(
            @Field("email") String email,
            @Field("name") String name,
            @Field("alamat") String alamat,
            @Field("handphone") String handphone,
            @Field("status") String status,
            @Field("level") int level
    );

    @GET("get_event_name.php")
    Call<Event> getEventName();

    @FormUrlEncoded
    @POST("data_table.php")
    Call<ResponseBody> exportToExcel(
            @Field("tanggaldari") String tanggaldari,
            @Field("tanggalsampai") String tanggalsampai,
            @Field("filename") String filename
    );

    @FormUrlEncoded
    @POST("change_event_title.php")
    Call<Event> changeEventTitle(
            @Field("nama") String nama
    );

    @FormUrlEncoded
    @POST("write_event_log.php")
    Call<EventLog> writeEventLog(
            @Field("iduser") int iduser,
            @Field("detail") String detail
    );

    @Multipart
    @POST("upload_images.php")
    Call<UploadResponse> uploadImage(
            @Part("iduser") int iduser,
            @Part MultipartBody.Part file
    );
}

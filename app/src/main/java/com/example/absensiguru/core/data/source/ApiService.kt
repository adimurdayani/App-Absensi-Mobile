package com.example.absensiguru.core.data.source

import com.example.absensiguru.core.data.model.ResponseModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("guru")
    fun inputGuru(
        @Field("id") id: Int,
        @Field("nip") nip: String,
        @Field("kelamin") kelamin: String,
        @Field("t_lahir") t_lahir: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("agama") agama: String,
        @Field("alamat") alamat: String,
        @Field("pendidikan") pendidikan: String,
        @Field("jabatan") jabatan: String,
        @Field("status_kepegawaian") status_kepegawaian: String,
        @Field("mapel") mapel: String,
        @Field("sertifikasi") sertifikasi: String
    ): Call<ResponseModel>

    @GET("absen")
    fun dataGuru(): Call<ResponseModel>

    @GET("absen")
    fun dataIdAbsen(
        @Query("id_guru") id_guru: Int,
    ): Call<ResponseModel>

    @GET("jadwal")
    fun jadwal(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("absen/absenmasuk")
    fun absenMasuk(
        @Field("id_guru") id_guru: Int,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("absen/absenpulang")
    fun absenPulang(
        @Field("id") id: Int,
        @Field("id_guru") id_guru: Int,
    ): Call<ResponseModel>
//
//    @FormUrlEncoded
//    @POST("user/ubahprofile/{id}")
//    fun ubahprofile(
//        @Path("id") id: Int,
//        @Field("nama") nama: String,
//        @Field("email") email: String,
//        @Field("phone") phone: String
//    ): Call<ResponsModel>
//
//    @FormUrlEncoded
//    @POST("user/ubahpassword/{id}")
//    fun ubahpassword(
//        @Path("id") id: Int,
//        @Field("password") password: String
//    ): Call<ResponsModel>
//
//    @POST("checkout")
//    fun checkout(
//        @Body data: Checkout
//    ): Call<ResponsModel>
//
//    //
//    @GET("produk/{kategori}")
//    fun produk(
//        @Path("kategori") kategori: String
//    ): Call<ResponsModel>
//
//    @GET("produk/kategori_terbaru/{kategori}")
//    fun produkTerbaru(
//        @Path("kategori") kategori: String
//    ): Call<ResponsModel>
//
//    @GET("produk/kategori_terlaris/{kategori}")
//    fun produkTerlaris(
//        @Path("kategori") kategori: String
//    ): Call<ResponsModel>
//
//    @GET("province")
//    fun getProvinsi(
//        @Header("key") key: String
//    ): Call<ResponsModel>
//
//    @GET("city")
//    fun getKota(
//        @Header("key") key: String,
//        @Query("province") id: String
//    ): Call<ResponsModel>
//
//    @FormUrlEncoded
//    @POST("cost")
//    fun ongkir(
//        @Header("key") key: String,
//        @Field("origin") origin: String,
//        @Field("destination") destination: String,
//        @Field("weight") weight: Int,
//        @Field("courier") courier: String
//    ): Call<ResponsOngkir>
//
//    //
//    @GET("checkout/user/{id}")
//    fun getRiwayat(
//        @Path("id") id: Int
//    ): Call<ResponsModel>
//
//    //
//    @GET("user/{id}")
//    fun getIdUser(
//        @Path("id") id: Int
//    ): Call<ResponsModel>
//
//    //
//    @POST("checkout/batal/{id}")
//    fun batalcheckout(
//        @Path("id") id: Int
//    ): Call<ResponsModel>
//
//    //
//    @Multipart
//    @POST("checkout/upload/{id}")
//    fun uploadbukti(
//        @Path("id") id: Int,
//        @Part image: MultipartBody.Part
//    ): Call<ResponsModel>
}
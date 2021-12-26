package com.example.absensiguru.ui.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.Absensi
import com.example.absensiguru.core.data.model.ResponseModel
import com.example.absensiguru.core.data.source.ApiConfig
import com.example.absensiguru.util.SharedPref
import com.example.absensiguru.util.Util
import com.google.gson.Gson
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailAbsen : AppCompatActivity() {
    lateinit var btn_absen_pulang: LinearLayout
    lateinit var img_user: ImageView
    lateinit var btn_kembali: ImageView
    lateinit var s: SharedPref
    lateinit var jam: TextClock
    lateinit var div_jam_masuk: LinearLayout
    lateinit var jm_masuk: TextView
    lateinit var div_jam_pulang: LinearLayout
    lateinit var jm_pulang: TextView
    lateinit var absensi: Absensi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_absen)
        s = SharedPref(this)
        val data = intent.getStringExtra("extra")
        absensi = Gson().fromJson<Absensi>(data, Absensi::class.java)
        setinit()
        setButton()
        setDisplay()
    }

    private fun setButton() {
        btn_absen_pulang.setOnClickListener {
            setAbsenPulang()
        }
        btn_kembali.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setAbsenPulang() {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Harap tunggu data sedang dikirim!")
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
        ApiConfig.instanceRetrofit.absenPulang(absensi.id, s.getUser()!!.id)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    val respon = response.body()!!
                    alertDialog.dismiss()
                    if (respon.status == 1) {
                        sukses("Absen pulang sukses!")
                    } else {
                        alertDialog.dismiss()
                        setError(respon.message)
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    alertDialog.dismiss()
                    Log.d("Respon", "Pesan: " + t.message)
                    setError(t.message.toString())
                }
            })
    }

    private fun sukses(pesan: String) {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(this, DialogTypes.TYPE_SUCCESS)
                .setTitle("Sukses")
                .setDescription(pesan)
                .setPositiveText("Ok")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        onBackPressed()
                        dialog.dismiss()
                    }
                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setDisplay() {
        val user = s.getUser()!!
        Log.d("Response", "Image: " + user.image)
        Picasso.get()
            .load(Util.produkUrl + user.image)
            .into(img_user)

        jam.timeZone = "Asia/Makassar"
    }

    private fun setJadwal() {
        ApiConfig.instanceRetrofit.jadwal().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>,
            ) {
                if (response.body() == null) {
                    div_jam_masuk.visibility = View.GONE
                    div_jam_pulang.visibility = View.GONE
                    jm_masuk.text = "0"
                    jm_pulang.text = "0"
                } else {
                    div_jam_masuk.visibility = View.VISIBLE
                    div_jam_pulang.visibility = View.VISIBLE
                    val res = response.body()!!
                    if (res.status == 1) {
                        val getjadwal = res.jadwal
                        jm_masuk.text = getjadwal.jam_masuk
                        jm_pulang.text = getjadwal.jam_pulang
                    } else {
                        Log.d("Response", "Error: " + res.message)
                        setError(res.message)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                setError("Terjadi kesalahan koneksi!")
                Log.d("Response", "Error: " + t.message)
            }
        })
    }

    private fun setError(pesan: String) {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(this, DialogTypes.TYPE_ERROR)
                .setTitle("Something error")
                .setDescription(pesan)
                .setPositiveText("Oke")
                .setPositiveTextColor(Color.WHITE)
                .setPositiveButtonColor(Color.RED)
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                    }

                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setinit() {
        btn_absen_pulang = findViewById(R.id.btn_absen_pulang)
        img_user = findViewById(R.id.img_user)
        jam = findViewById(R.id.jam)
        div_jam_masuk = findViewById(R.id.div_jam_masuk)
        jm_masuk = findViewById(R.id.jm_masuk)
        div_jam_pulang = findViewById(R.id.div_jam_pulang)
        jm_pulang = findViewById(R.id.jm_pulang)
        btn_kembali = findViewById(R.id.btn_kembali)
    }

    override fun onResume() {
        setJadwal()
        super.onResume()
    }
}
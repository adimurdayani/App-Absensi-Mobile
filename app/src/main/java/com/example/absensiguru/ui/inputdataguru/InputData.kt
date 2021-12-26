package com.example.absensiguru.ui.inputdataguru

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.absensiguru.HomeActivity
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.ResponseModel
import com.example.absensiguru.core.data.source.ApiConfig
import com.example.absensiguru.util.SharedPref
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class InputData : AppCompatActivity() {
    lateinit var btn_kembali: ImageView
    lateinit var nip: EditText
    lateinit var nama: EditText
    lateinit var email: EditText
    lateinit var kelamin: Spinner
    lateinit var t_lahir: EditText
    lateinit var tgl_lahir: EditText
    lateinit var agama: Spinner
    lateinit var pendidikan: Spinner
    lateinit var jabatan: EditText
    lateinit var alamat: EditText
    lateinit var status_kepegawaian: EditText
    lateinit var mapel: EditText
    lateinit var sertifikasi: Spinner
    lateinit var btn_simpan: LinearLayout
    lateinit var progress: ProgressBar
    lateinit var text_simpan: TextView
    private lateinit var s: SharedPref
    lateinit var getkelamin: String
    lateinit var getAgama: String
    lateinit var getPendidikan: String
    lateinit var getSertifikasi: String

    var datePickerDialog: DatePickerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)
        s = SharedPref(this)
        setinit()
        setDisplay()
        setButton()
        setSpinner()
        setDate()
    }

    @SuppressLint("SetTextI18n")
    private fun setDate() {
        tgl_lahir.setOnClickListener(View.OnClickListener { v: View? ->
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR] // current year
            val mMonth = c[Calendar.MONTH] // current month
            val mDay = c[Calendar.DAY_OF_MONTH] // current day
            datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth -> tgl_lahir.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year) },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog!!.show()
        })
    }

    private fun setSpinner() {
        kelamin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getkelamin = kelamin.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        agama.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getAgama = agama.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        pendidikan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getPendidikan = pendidikan.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        sertifikasi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getSertifikasi = sertifikasi.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setButton() {
        btn_kembali.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        btn_simpan.setOnClickListener {
            if (validasi()) {
                simpan()
            }
        }
    }

    private fun simpan() {
        val id = s.getUser()!!.id
        val s_nip = nip.text.toString()
        val s_tlahir = t_lahir.text.toString()
        val s_tgllahir = tgl_lahir.text.toString()
        val s_alamat = alamat.text.toString()
        val s_jabatan = jabatan.text.toString()
        val s_status_kepegawian = status_kepegawaian.text.toString()
        val s_mapel = mapel.text.toString()

        progress.visibility = View.VISIBLE
        text_simpan.visibility = View.GONE
        ApiConfig.instanceRetrofit.inputGuru(
            id, s_nip, getkelamin, s_tlahir, s_tgllahir,
            getAgama,
            s_alamat, getPendidikan,
            s_jabatan,
            s_status_kepegawian,
            s_mapel,
            getSertifikasi
        )
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    progress.visibility = View.GONE
                    text_simpan.visibility = View.VISIBLE
                    val respon = response.body()!!
                    if (respon.status == 1) {
                        s.setInputGuru(true)
                        sukses("Data telah berhasil disimpan!")
                    } else {
                        progress.visibility = View.GONE
                        text_simpan.visibility = View.VISIBLE
                        Log.d("Response", "Error: " + respon.message)
                        setError(respon.message)
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    progress.visibility = View.GONE
                    text_simpan.visibility = View.VISIBLE
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
                        startActivity(Intent(this@InputData, HomeActivity::class.java))
                        finish()
                        dialog.dismiss()
                    }
                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
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

    private fun validasi(): Boolean {
        if (nip.text.toString().isEmpty()) {
            nip.error = "Kolom NIP tidak boleh kosong!"
            nip.requestFocus()
            return false
        }
        if (t_lahir.text.toString().isEmpty()) {
            t_lahir.error = "Kolom tempat lahir tidak boleh kosong!"
            t_lahir.requestFocus()
            return false
        }
        if (tgl_lahir.text.toString().isEmpty()) {
            tgl_lahir.error = "Kolom tanggal lahir tidak boleh kosong!"
            tgl_lahir.requestFocus()
            return false
        }
        if (jabatan.text.toString().isEmpty()) {
            jabatan.error = "Kolom jabatan tidak boleh kosong!"
            jabatan.requestFocus()
            return false
        }
        if (status_kepegawaian.text.toString().isEmpty()) {
            status_kepegawaian.error = "Kolom status kepegawaian tidak boleh kosong!"
            status_kepegawaian.requestFocus()
            return false
        }
        if (mapel.text.toString().isEmpty()) {
            mapel.error = "Kolom mapel tidak boleh kosong!"
            mapel.requestFocus()
            return false
        }
        if (alamat.text.toString().isEmpty()) {
            alamat.error = "Kolom alamat tidak boleh kosong!"
            alamat.requestFocus()
            return false
        }
        return true
    }

    private fun setDisplay() {
        val user = s.getUser()!!
        nama.setText(user.nama)
        email.setText(user.email)
    }

    private fun setinit() {
        btn_kembali = findViewById(R.id.btn_kembali)
        nip = findViewById(R.id.nip)
        nama = findViewById(R.id.nama)
        email = findViewById(R.id.email)
        kelamin = findViewById(R.id.kelamin)
        t_lahir = findViewById(R.id.t_lahir)
        tgl_lahir = findViewById(R.id.tgl_lahir)
        agama = findViewById(R.id.agama)
        pendidikan = findViewById(R.id.pendidikan)
        jabatan = findViewById(R.id.jabatan)
        status_kepegawaian = findViewById(R.id.status_kepegawaian)
        mapel = findViewById(R.id.mapel)
        sertifikasi = findViewById(R.id.sertifikasi)
        btn_simpan = findViewById(R.id.btn_simpan)
        progress = findViewById(R.id.progress)
        text_simpan = findViewById(R.id.text_simpan)
        alamat = findViewById(R.id.alamat)
    }
}
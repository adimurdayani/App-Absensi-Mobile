package com.example.absensiguru.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.ResponseModel
import com.example.absensiguru.core.data.source.ApiConfig
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragmet : Fragment() {
    lateinit var btn_kembali: ImageView
    lateinit var nama: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var konfirmasi_password: EditText
    lateinit var btn_register: LinearLayout
    lateinit var progress: ProgressBar
    lateinit var text_register: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)
        setinit(view)
        setButton()
        return view
    }

    private fun setButton() {
        btn_kembali.setOnClickListener {
            val fragment = LoginFragment()
            movetoLogin(fragment)
        }
        btn_register.setOnClickListener {
            if (validasi()) {
                register()
            }
        }
    }

    private fun register() {
        val enama = nama.text.toString()
        val eemail = email.text.toString()
        val epassword = password.text.toString()

        progress.visibility = View.VISIBLE
        text_register.visibility = View.GONE
        ApiConfig.instanceRetrofit.register(enama, eemail, epassword)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    progress.visibility = View.GONE
                    text_register.visibility = View.VISIBLE
                    val respon = response.body()!!
                    if (respon.status == 1) {
                        sukses("Anda telah berhasil registrasi, klik ok untuk login!")
                    } else {
                        progress.visibility = View.GONE
                        text_register.visibility = View.VISIBLE
                        setError(respon.message)
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    progress.visibility = View.GONE
                    text_register.visibility = View.VISIBLE
                    Log.d("Respon", "Pesan: " + t.message)
                    setError(t.message.toString())
                }
            })
    }

    private fun sukses(pesan: String) {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_SUCCESS)
                .setTitle("Sukses")
                .setDescription(pesan)
                .setPositiveText("Ok")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        val fragment = LoginFragment()
                        movetoLogin(fragment)
                        dialog.dismiss()
                    }
                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setError(pesan: String) {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_ERROR)
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
        if (nama.text.toString().isEmpty()) {
            nama.error = "Kolom nama tidak boleh kosong!"
            nama.requestFocus()
            return false
        }
        if (email.text.toString().isEmpty()) {
            email.error = "Kolom email tidak boleh kosong!"
            email.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Format email salah!. Contoh: gunakan @example.com"
            email.requestFocus()
            return false
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Kolom password tidak boleh kosong!"
            password.requestFocus()
            return false
        } else if (password.text.toString().length < 6) {
            password.error = "Password tidak boleh kurang dari 6 karakter!"
            password.requestFocus()
            return false
        }
        if (konfirmasi_password.text.toString().isEmpty()) {
            konfirmasi_password.error = "Kolom password tidak boleh kosong!"
            konfirmasi_password.requestFocus()
            return false
        } else if (!konfirmasi_password.text.toString()
                .matches(password.text.toString().toRegex())
        ) {
            konfirmasi_password.error = "Konfirmasi password tidak sama dengan password!"
            konfirmasi_password.requestFocus()
            return false
        } else if (konfirmasi_password.text.toString().length < 6) {
            konfirmasi_password.error = "Password tidak boleh kurang dari 6 karakter!"
            konfirmasi_password.requestFocus()
            return false
        }
        return true
    }

    private fun movetoLogin(fragment: LoginFragment) {
        val frm = fragmentManager?.beginTransaction()
        frm!!.replace(R.id.frm_login, fragment)
        frm.commit()
    }

    private fun setinit(view: View) {
        btn_kembali = view.findViewById(R.id.btn_kembali)
        nama = view.findViewById(R.id.nama)
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        konfirmasi_password = view.findViewById(R.id.konfirmasi_password)
        btn_register = view.findViewById(R.id.btn_register)
        progress = view.findViewById(R.id.progress)
        text_register = view.findViewById(R.id.text_register)
    }
}
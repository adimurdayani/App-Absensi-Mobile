package com.example.absensiguru.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
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


class LoginFragment : Fragment() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var btn_login: LinearLayout
    lateinit var progress: ProgressBar
    lateinit var register: TextView
    lateinit var text_login: TextView
    lateinit var s: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        s = SharedPref(requireActivity())
        setinit(view)
        setButton()
        return view
    }

    private fun setButton() {
        btn_login.setOnClickListener {
            if (validasi()) {
                login()
            }
        }
        register.setOnClickListener {
            val fragment = RegisterFragmet()
            movetoFragment(fragment)
        }
    }

    private fun login() {
        val e_email = email.text.toString()
        val e_password = password.text.toString()

        progress.visibility = View.VISIBLE
        text_login.visibility = View.GONE
        ApiConfig.instanceRetrofit.login(e_email, e_password)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    progress.visibility = View.GONE
                    text_login.visibility = View.VISIBLE
                    if (response.body() != null) {
                        val respon = response.body()!!
                        if (respon.status == 1) {
                            s.setStatusLogin(true)
                            s.setUser(respon.data)
                            val intent = Intent(requireActivity(), HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            progress.visibility = View.GONE
                            text_login.visibility = View.VISIBLE
                            Log.d("Response", "Error: " + respon.message)
                            setError(respon.message)
                        }
                    } else {
                        setError("Email atau password salah!")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    progress.visibility = View.GONE
                    text_login.visibility = View.VISIBLE
                    setError(t.message.toString())
                }
            })
    }

    private fun validasi(): Boolean {
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
        return true
    }

    private fun movetoFragment(fragment: RegisterFragmet) {
        val frm = fragmentManager?.beginTransaction()
        frm!!.replace(R.id.frm_login, fragment)
        frm.commit()
    }

    fun setError(pesan: String) {
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

    private fun setinit(view: View) {
        email = view.findViewById(R.id.email)
        password = view.findViewById(R.id.password)
        btn_login = view.findViewById(R.id.btn_login)
        progress = view.findViewById(R.id.progress)
        register = view.findViewById(R.id.register)
        text_login = view.findViewById(R.id.text_login)
    }
}
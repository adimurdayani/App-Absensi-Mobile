package com.example.absensiguru.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.absensiguru.LoginActivity
import com.example.absensiguru.R
import com.example.absensiguru.ui.auth.LoginFragment
import com.example.absensiguru.util.SharedPref
import com.example.absensiguru.util.Util
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    lateinit var img_user: ImageView
    lateinit var nama: TextView
    lateinit var email: TextView
    lateinit var btn_logout: ImageView
    lateinit var img_default: ImageView
    lateinit var s: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        s = SharedPref(requireActivity())
        setinit(view)
        setDisplay()
        setButton()
        return view
    }

    private fun setButton() {
        btn_logout.setOnClickListener {
            var alertDialog: LottieAlertDialog =
                LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_WARNING)
                    .setTitle("Apakah anda yakin ingin keluar?")
                    .setPositiveText("Iya")
                    .setPositiveTextColor(Color.WHITE)
                    .setNegativeTextColor(Color.WHITE)
                    .setPositiveListener(object : ClickListener {
                        override fun onClick(dialog: LottieAlertDialog) {
                            s.setStatusLogin(false)
                            val intent = Intent(requireActivity(), LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                            dialog.dismiss()
                        }
                    })
                    .setNegativeText("Tidak")
                    .setNegativeListener(object : ClickListener {
                        override fun onClick(dialog: LottieAlertDialog) {

                            dialog.dismiss()
                        }
                    })
                    .build()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun setDisplay() {
        val user = s.getUser()!!
        nama.text = user.nama
        email.text = user.email
        Picasso.get()
            .load(Util.produkUrl + user.image)
            .into(img_user)
    }

    private fun setinit(view: View) {
        img_user = view.findViewById(R.id.img_user)
        nama = view.findViewById(R.id.nama)
        email = view.findViewById(R.id.email)
        btn_logout = view.findViewById(R.id.btn_logout)
        img_default = view.findViewById(R.id.img_default)
    }
}
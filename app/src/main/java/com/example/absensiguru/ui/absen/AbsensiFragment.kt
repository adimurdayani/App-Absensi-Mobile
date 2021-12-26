package com.example.absensiguru.ui.absen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.Absensi
import com.example.absensiguru.core.data.model.ResponseModel
import com.example.absensiguru.core.data.source.ApiConfig
import com.example.absensiguru.ui.auth.LoginFragment
import com.example.absensiguru.ui.detail.DetailAbsen
import com.example.absensiguru.ui.home.adapter.AdapterGuru
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
import java.text.SimpleDateFormat
import java.util.*

class AbsensiFragment : Fragment() {
    lateinit var btn_absen_masuk: LinearLayout
    lateinit var img_user: ImageView
    lateinit var s: SharedPref
    lateinit var jam: TextClock
    lateinit var search: SearchView
    lateinit var animationView4: LottieAnimationView
    lateinit var sw_data: SwipeRefreshLayout
    lateinit var rc_data: RecyclerView
    lateinit var div_jam_masuk: LinearLayout
    lateinit var jm_masuk: TextView
    lateinit var div_jam_pulang: LinearLayout
    lateinit var jm_pulang: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_absen, container, false)
        s = SharedPref(requireActivity())
        setinit(view)
        setDisplay()
        setButton()
        return view
    }

    private fun setJadwal() {
        ApiConfig.instanceRetrofit.jadwal().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>,
            ) {
                sw_data.isRefreshing = false
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
                sw_data.isRefreshing = false
                setError("Terjadi kesalahan koneksi!")
                Log.d("Response", "Error: " + t.message)
            }
        })
    }

    private var listAbsen: ArrayList<Absensi> = ArrayList()
    private fun setDisplay() {
        val user = s.getUser()!!
        Log.d("Response", "Image: " + user.image)
        Picasso.get()
            .load(Util.produkUrl + user.image)
            .into(img_user)
        jam.timeZone = "Asia/Makassar"

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rc_data.adapter = AdapterGuru(listAbsen, object : AdapterGuru.Listeners {
            override fun onClicked(data: Absensi) {
                val intent = Intent(requireActivity(), DetailAbsen::class.java)
                val str = Gson().toJson(data, Absensi::class.java)
                intent.putExtra("extra", str)
                requireActivity().startActivity(intent)
            }

        })
        rc_data.layoutManager = layoutManager
        sw_data.setOnRefreshListener { setData() }

        val adapter = AdapterGuru(listAbsen, object : AdapterGuru.Listeners {
            override fun onClicked(data: Absensi) {

            }

        })

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.getSearchData().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setData()
                return false
            }
        })
    }

    private fun setData() {
        sw_data.isRefreshing = true
        ApiConfig.instanceRetrofit.dataIdAbsen(s.getUser()!!.id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>,
            ) {
                sw_data.isRefreshing = false
                if (response.body() == null) {
                    animationView4.visibility = View.VISIBLE
                } else {
                    animationView4.visibility = View.GONE
                    val res = response.body()!!
                    if (res.status == 1) {
                        listAbsen = res.absen
                        setDisplay()
                        val calendar = Calendar.getInstance()
                        val formatTanggal =
                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val tanggal = formatTanggal.format(calendar.time)
                        if (res.absen[0].tanggal == tanggal) {
                            btn_absen_masuk.visibility = View.GONE
                        }
                    } else {
                        Log.d("Response", "Error: " + res.message)
                        setError(res.message)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                sw_data.isRefreshing = false
                setError("Terjadi kesalahan koneksi!")
                Log.d("Response", "Error: " + t.message)
            }
        })
    }

    private fun setButton() {
        btn_absen_masuk.setOnClickListener { setAbsenMasuk() }
    }

    private fun setAbsenMasuk() {
        var alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Harap tunggu data sedang dikirim!")
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
        ApiConfig.instanceRetrofit.absenMasuk(s.getUser()!!.id)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    val respon = response.body()!!
                    alertDialog.dismiss()
                    if (respon.status == 1) {
                        sukses("Absen masuk sukses!")
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
        val alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_SUCCESS)
                .setTitle("Sukses")
                .setDescription(pesan)
                .setPositiveText("Ok")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                    }
                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setError(pesan: String) {
        val alertDialog: LottieAlertDialog =
            LottieAlertDialog.Builder(requireActivity(), DialogTypes.TYPE_ERROR)
                .setTitle("Something error")
                .setDescription(pesan)
                .setPositiveText("Oke")
                .setPositiveTextColor(Color.WHITE)
                .setPositiveButtonColor(Color.RED)
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        val fragmentAbsen = AbsensiFragment()
                        val  fragment = fragmentManager!!.beginTransaction()
                        fragment.replace(R.id.frm_home, fragmentAbsen)
                        fragment.commit()
                        dialog.dismiss()
                    }

                })
                .build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setinit(view: View) {
        btn_absen_masuk = view.findViewById(R.id.btn_absen_masuk)
        img_user = view.findViewById(R.id.img_user)
        jam = view.findViewById(R.id.jam)
        search = view.findViewById(R.id.search)
        animationView4 = view.findViewById(R.id.animationView4)
        sw_data = view.findViewById(R.id.sw_data)
        rc_data = view.findViewById(R.id.rc_data)
        div_jam_masuk = view.findViewById(R.id.div_jam_masuk)
        jm_masuk = view.findViewById(R.id.jm_masuk)
        div_jam_pulang = view.findViewById(R.id.div_jam_pulang)
        jm_pulang = view.findViewById(R.id.jm_pulang)
    }

    override fun onResume() {
        setData()
        setJadwal()
        super.onResume()
    }
}
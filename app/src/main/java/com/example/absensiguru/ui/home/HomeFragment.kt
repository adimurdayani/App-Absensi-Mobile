package com.example.absensiguru.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextClock
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.Absensi
import com.example.absensiguru.core.data.model.ResponseModel
import com.example.absensiguru.core.data.source.ApiConfig
import com.example.absensiguru.ui.home.adapter.AdapterGuru
import com.example.absensiguru.util.SharedPref
import com.example.absensiguru.util.Util
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var jam: TextClock
    lateinit var nama: TextView
    lateinit var tgl: TextView
    lateinit var search: SearchView
    lateinit var animationView4: LottieAnimationView
    lateinit var sw_data: SwipeRefreshLayout
    lateinit var rc_data: RecyclerView
    lateinit var img_user: ImageView
    lateinit var s: SharedPref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        s = SharedPref(requireActivity())
        setinit(view)
        setDisplay()
        return view
    }

    private var listAbsen: ArrayList<Absensi> = ArrayList()

    @SuppressLint("SimpleDateFormat")
    private fun setDisplay() {
        val calendar = Calendar.getInstance()
        val formatTanggal = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val tanggal = formatTanggal.format(calendar.time)
        tgl.text = tanggal
        jam.timeZone = "Asia/Makassar"

        val user = s.getUser()!!
        nama.text = user.nama
        Log.d("Response", "Image: " + user.image)
        Picasso.get()
            .load(Util.produkUrl + user.image)
            .into(img_user)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rc_data.adapter = AdapterGuru(listAbsen, object : AdapterGuru.Listeners {
            override fun onClicked(data: Absensi) {

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
        ApiConfig.instanceRetrofit.dataGuru().enqueue(object :
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

    private fun setinit(view: View) {
        jam = view.findViewById(R.id.jam)
        search = view.findViewById(R.id.search)
        animationView4 = view.findViewById(R.id.animationView4)
        sw_data = view.findViewById(R.id.sw_data)
        rc_data = view.findViewById(R.id.rc_data)
        img_user = view.findViewById(R.id.img_user)
        nama = view.findViewById(R.id.nama)
        tgl = view.findViewById(R.id.tgl)
    }

    override fun onResume() {
        setData()
        super.onResume()
    }
}
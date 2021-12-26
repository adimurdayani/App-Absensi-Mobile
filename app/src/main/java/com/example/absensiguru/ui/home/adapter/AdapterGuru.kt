package com.example.absensiguru.ui.home.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.absensiguru.R
import com.example.absensiguru.core.data.model.Absensi
import com.example.absensiguru.ui.detail.DetailAbsen
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class AdapterGuru( var data: ArrayList<Absensi>, var listener: Listeners) :
    RecyclerView.Adapter<AdapterGuru.HolderData>() {
    class HolderData(view: View) : RecyclerView.ViewHolder(view) {
        val tgl = view.findViewById<TextView>(R.id.tgl)
        val div_jam_masuk = view.findViewById<LinearLayout>(R.id.div_jam_masuk)
        val jam_masuk = view.findViewById<TextView>(R.id.jam_masuk)
        val div_jam_pulang = view.findViewById<LinearLayout>(R.id.div_jam_pulang)
        val div_layout = view.findViewById<LinearLayout>(R.id.layout)
        val jam_pulang = view.findViewById<TextView>(R.id.jam_pulang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderData {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_guru, parent, false)
        return HolderData(view)
    }

    override fun onBindViewHolder(holder: HolderData, position: Int) {
        holder.tgl.text = data[position].tanggal
        holder.jam_masuk.text = data[position].absen_masuk
        holder.jam_pulang.text = data[position].absen_pulang

        val getabsen = data[position]
        if (getabsen.absen_masuk == null) {
            holder.div_jam_masuk.visibility = View.GONE
        } else if (getabsen.absen_pulang == null) {
            holder.div_jam_pulang.visibility = View.GONE
        } else {
            holder.div_jam_masuk.visibility = View.VISIBLE
            holder.div_jam_pulang.visibility = View.VISIBLE
        }

        holder.div_layout.setOnClickListener {
            listener.onClicked(getabsen)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private var searchData: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val searchList: java.util.ArrayList<Absensi> = java.util.ArrayList<Absensi>()
            if (constraint.toString().isEmpty()) {
                searchList.addAll(data)
            } else {
                for (getRekamMedik in data) {
                    if (getRekamMedik.tanggal.toLowerCase(Locale.getDefault())
                            .contains(constraint.toString().toLowerCase(Locale.getDefault()))
                    ) {
                        searchList.add(getRekamMedik)
                    }
                }
            }
            val results = FilterResults()
            results.values = searchList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            data.clear()
            data.addAll(results.values as Collection<Absensi>)
            notifyDataSetChanged()
        }
    }

    fun getSearchData(): Filter {
        return searchData
    }

    interface Listeners {
        fun onClicked(data: Absensi)
    }
}
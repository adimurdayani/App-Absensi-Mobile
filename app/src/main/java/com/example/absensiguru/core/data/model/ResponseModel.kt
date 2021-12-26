package com.example.absensiguru.core.data.model

class ResponseModel {
    var status = 0
    lateinit var message: String
    var data = Guru()
    var absen: ArrayList<Absensi> = ArrayList()
    var jadwal = Jadwal()
}

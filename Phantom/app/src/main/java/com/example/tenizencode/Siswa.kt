package com.example.tenizencode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Siswa(
    val nis: String,
    val namasiswa: String,
    val gender: String,
    val alamat: String,
    val tanggallahir: String,
    val foto: String?
) : Parcelable

package com.example.tenizencode.api

object Konfigurasi {
    // Ganti sesuai IP kamu
    private const val BASE_URL = "http://10.0.2.2:8080/TenizenCode/"

    // Product
    const val URL_GET_PRODUK = BASE_URL + "produk/tampilproduk.php"
    const val URL_ADD_PRODUK = BASE_URL + "produk/tambahproduk.php"
    const val URL_DELETE_PRODUK = BASE_URL + "produk/hapusproduk.php"

    // User
    const val URL_LOGIN = BASE_URL + "login.php"
    const val URL_REGISTER = BASE_URL + "register.php"

    // Siswa
    const val URL_GET_SISWA = BASE_URL + "siswa/tampilsiswanis.php"
    const val URL_GET_ALL_SISWA = BASE_URL + "siswa/tampilsiswa.php"
    const val URL_ADD_SISWA = BASE_URL + "siswa/tambahsiswa.php"
    const val URL_DELETE_SISWA = BASE_URL + "siswa/hapusiswa.php"
    const val URL_UPDATE_SISWA = BASE_URL + "siswa/updatesiswa.php"
}
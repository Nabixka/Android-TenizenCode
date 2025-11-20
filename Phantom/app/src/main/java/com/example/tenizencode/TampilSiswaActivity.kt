package com.example.tenizencode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenizencode.api.Konfigurasi
import com.example.tenizencode.api.RequestHandler
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONObject

class TampilSiswaActivity : AppCompatActivity(), SiswaAdapter.OnAdapterListener {

    private lateinit var rvSiswa: RecyclerView
    private lateinit var siswaAdapter: SiswaAdapter
    private var siswaList = mutableListOf<Siswa>()
    private lateinit var searchView: SearchView

    private val editSiswaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val isUpdated = result.data?.getBooleanExtra("IS_DATA_UPDATED", false) ?: false
            if (isUpdated) {
                fetchAllSiswa()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampil_siswa)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        rvSiswa = findViewById(R.id.rvTampilSiswa)
        searchView = findViewById(R.id.searchView)

        setupRecyclerView()
        setupSearchView()
        fetchAllSiswa()
    }

    private fun setupRecyclerView() {
        siswaAdapter = SiswaAdapter(siswaList, this)
        rvSiswa.layoutManager = LinearLayoutManager(this)
        rvSiswa.adapter = siswaAdapter
    }

    override fun onEditClick(siswa: Siswa) {
        val intent = Intent(this, EditSiswaActivity::class.java).apply {
            putExtra("EXTRA_SISWA", siswa)
        }
        editSiswaLauncher.launch(intent)
    }

    override fun onDeleteClick(siswa: Siswa, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus siswa bernama ${siswa.namasiswa}?")
            .setPositiveButton("Ya") { _, _ -> deleteSiswa(siswa.nis, position) }
            .setNegativeButton("Tidak", null)
            .show()
    }


    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Dipanggil saat user menekan tombol search di keyboard
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchSiswaByNis(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    fetchAllSiswa()
                }
                return true
            }
        })
    }

    private fun fetchAllSiswa() {
        RequestHandler.get(Konfigurasi.URL_GET_ALL_SISWA) { response, error ->
            handleSiswaResponse(response, error)
        }
    }

    private fun searchSiswaByNis(nis: String) {
        val url = "${Konfigurasi.URL_GET_SISWA}?nis=$nis"
        RequestHandler.get(url) { response, error ->
            handleSiswaResponse(response, error)
        }
    }

    private fun handleSiswaResponse(response: String?, error: String?) {
        runOnUiThread {
            if (error != null) {
                Toast.makeText(this, "Gagal mengambil data: $error", Toast.LENGTH_LONG).show()
                return@runOnUiThread
            }
            if (response != null) {
                try {
                    val jsonResponse = JSONObject(response)
                    siswaList.clear() // Kosongkan list sebelum diisi data baru
                    if (jsonResponse.getString("status") == "success" && jsonResponse.has("data")) {
                        val dataArray = jsonResponse.getJSONArray("data")
                        for (i in 0 until dataArray.length()) {
                            val siswaObject = dataArray.getJSONObject(i)
                            val siswa = Siswa(
                                nis = siswaObject.getString("nis"),
                                namasiswa = siswaObject.getString("namasiswa"),
                                gender = siswaObject.getString("gender"),
                                alamat = siswaObject.getString("alamat"),
                                tanggallahir = siswaObject.getString("tanggallahir"),
                                foto = siswaObject.optString("foto", null)
                            )
                            siswaList.add(siswa)
                        }
                    } else {
                        val message = jsonResponse.optString("message", "Data tidak ditemukan")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                    siswaAdapter.notifyDataSetChanged() // Perbarui tampilan RecyclerView
                } catch (e: Exception) {
                    Toast.makeText(this, "Gagal parsing data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteSiswa(nis: String, position: Int) {
        val params = mapOf("nis" to nis)
        RequestHandler.post(Konfigurasi.URL_DELETE_SISWA, params) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Gagal menghapus: $error", Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")
                    val message = jsonResponse.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    if (status == "success") {
                        siswaAdapter.removeItem(position)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Respons tidak valid: $response", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

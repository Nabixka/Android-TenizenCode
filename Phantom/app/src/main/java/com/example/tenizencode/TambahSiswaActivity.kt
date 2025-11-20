package com.example.tenizencode

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tenizencode.api.Konfigurasi
import com.example.tenizencode.api.RequestHandler
import java.text.SimpleDateFormat
import java.util.*

class TambahSiswaActivity : AppCompatActivity() {

    private lateinit var etNis: EditText
    private lateinit var etNama: EditText
    private lateinit var spJK: Spinner
    private lateinit var etAlamat: EditText
    private lateinit var etTgLahir: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnTampil: Button

    private var jenisKelamin: String = ""
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_siswa)

        setupView()

        setupSpinner()

        setupListeners()
    }

    private fun setupView() {
        etNis = findViewById(R.id.etNis)
        etNama = findViewById(R.id.etNama)
        spJK = findViewById(R.id.spJK)
        etAlamat = findViewById(R.id.etAlamat)
        etTgLahir = findViewById(R.id.etTgLahir)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnTampil = findViewById(R.id.btnTampil)
    }

    private fun setupSpinner() {
        val jkAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.jenis_kelamin_array,
            R.layout.spinner_item_bold
        )

        jkAdapter.setDropDownViewResource(R.layout.spinner_item_bold)
        spJK.adapter = jkAdapter

        spJK.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                jenisKelamin = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupListeners() {
        etTgLahir.setOnClickListener {
            showDatePickerDialog()
        }

        btnSimpan.setOnClickListener {
            simpanDataSiswa()
        }

        btnTampil.setOnClickListener {
            val intent = Intent(this, TampilSiswaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        DatePickerDialog(
            this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etTgLahir.setText(sdf.format(calendar.time))
    }


    private fun simpanDataSiswa() {
        val nis = etNis.text.toString().trim()
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val tglLahir = etTgLahir.text.toString().trim()

        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty() || tglLahir.isEmpty() || jenisKelamin.isEmpty()) {
            Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_LONG).show()
            return
        }

        val params = mapOf(
            "nis" to nis,
            "namasiswa" to nama,
            "gender" to jenisKelamin,
            "alamat" to alamat,
            "tanggallahir" to tglLahir
        )

        RequestHandler.post(Konfigurasi.URL_ADD_SISWA, params) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Gagal mengirim data: $error", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Data siswa berhasil disimpan!", Toast.LENGTH_LONG).show()
                    etNis.text.clear()
                    etNama.text.clear()
                    etAlamat.text.clear()
                    etTgLahir.text.clear()
                }
            }
        }
    }
}

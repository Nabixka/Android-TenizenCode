package com.example.tenizencode

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenizencode.api.Konfigurasi
import com.example.tenizencode.api.RequestHandler
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONObject

class EditSiswaActivity : AppCompatActivity() {

    private lateinit var edtNis: EditText
    private lateinit var edtNama: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var edtAlamat: EditText
    private lateinit var edtTglLahir: EditText
    private var siswa: Siswa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_siswa)

        edtNis = findViewById(R.id.edtNis)
        edtNama = findViewById(R.id.edtNamaSiswa)
        rgGender = findViewById(R.id.rgGender)
        edtAlamat = findViewById(R.id.edtAlamat)
        edtTglLahir = findViewById(R.id.edtTanggalLahir)
        val btnSimpan: Button = findViewById(R.id.btnSimpanPerubahan)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbarEdit)

        toolbar.setNavigationOnClickListener { finish() }

        siswa = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("EXTRA_SISWA", Siswa::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_SISWA")
        }

        populateForm()
        btnSimpan.setOnClickListener { updateDataSiswa() }
    }

    private fun populateForm() {
        siswa?.let {
            edtNis.setText(it.nis)
            edtNama.setText(it.namasiswa)
            edtAlamat.setText(it.alamat)
            edtTglLahir.setText(it.tanggallahir)
            if (it.gender.equals("Laki-laki", ignoreCase = true)) {
                rgGender.check(R.id.rbLaki)
            } else {
                rgGender.check(R.id.rbPerempuan)
            }
        }
    }

    private fun updateDataSiswa() {
        val nama = edtNama.text.toString().trim()
        val alamat = edtAlamat.text.toString().trim()
        val tglLahir = edtTglLahir.text.toString().trim()
        val selectedGenderId = rgGender.checkedRadioButtonId

        if (selectedGenderId == -1 || nama.isEmpty() || alamat.isEmpty() || tglLahir.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val gender = findViewById<RadioButton>(selectedGenderId).text.toString()
        val params = mapOf(
            "nis" to edtNis.text.toString(),
            "namasiswa" to nama,
            "gender" to gender,
            "alamat" to alamat,
            "tanggallahir" to tglLahir
        )

        RequestHandler.post(Konfigurasi.URL_UPDATE_SISWA, params) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Gagal update: $error", Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                try {
                    val json = JSONObject(response)
                    Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()
                    if (json.getString("status") == "success") {
                        val resultIntent = Intent()
                        resultIntent.putExtra("IS_DATA_UPDATED", true)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Respons tidak valid: $response", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

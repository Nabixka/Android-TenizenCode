package com.example.tenizencode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenizencode.api.Konfigurasi
import com.example.tenizencode.api.RequestHandler
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.editName)
        editPassword = findViewById(R.id.editPassword)
        btnLogin = findViewById(R.id.button)

        btnLogin.setOnClickListener {
            loginUser()
        }

        val tvGoToRegister: TextView = findViewById(R.id.tvGoToRegister)

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (email.isEmpty()) {
            editEmail.error = "Email tidak boleh kosong!"
            editEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editPassword.error = "Password tidak boleh kosong!"
            editPassword.requestFocus()
            return
        }

        val params = mapOf(
            "email" to email,
            "password" to password
        )
        RequestHandler.post(Konfigurasi.URL_LOGIN, params) { response, error ->
            runOnUiThread {
                if (error != null) {
                    Toast.makeText(this, "Gagal terhubung ke server: $error", Toast.LENGTH_LONG).show()
                } else if (response != null) {
                    try {
                        val jsonResponse = JSONObject(response)
                        val status = jsonResponse.getString("status")
                        val message = jsonResponse.getString("message")

                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        if (status == "success") {
                            val intent = Intent(this, TambahSiswaActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this, "Format respons tidak valid: $response", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

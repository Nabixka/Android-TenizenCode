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

class RegisterActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editUsername = findViewById(R.id.editUsername)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            registerUser()
        }

        val tvGoToLogin: TextView = findViewById(R.id.tvGoToLogin)

        tvGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val username = editUsername.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val password = editPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val params = mapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )

        RequestHandler.post(Konfigurasi.URL_REGISTER, params) { response, error ->
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
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Format respons tidak valid: $response", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

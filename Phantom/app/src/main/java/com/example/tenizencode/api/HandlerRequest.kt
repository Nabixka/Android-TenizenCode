package com.example.tenizencode.api

import okhttp3.*
import java.io.IOException


object RequestHandler {

    private val client = OkHttpClient()

    fun post(url: String, params: Map<String, String>, callback: (String?, String?) -> Unit) {
        val formBodyBuilder = FormBody.Builder()
        for ((key, value) in params) {
            formBodyBuilder.add(key, value)
        }
        val requestBody = formBodyBuilder.build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(null, "Server error: ${response.code}")
                    } else {
                        callback(response.body?.string(), null)
                    }
                }
            }
        })
    }


    fun get(url: String, callback: (String?, String?) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback(null, "Server error: ${response.code}")
                    } else {
                        callback(response.body?.string(), null)
                    }
                }
            }
        })
    }
}

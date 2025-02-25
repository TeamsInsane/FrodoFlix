package com.frodo.frodoflix.api

import android.util.Log
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class TMDB {
    companion object {
        suspend fun getDataFromTMDB(url: String, result: String): Any? {
            return withContext(Dispatchers.IO) {
                val client = OkHttpClient()

                val dotenv = dotenv {
                    directory = "/assets"
                    filename = "env"
                }

                val API_KEY = dotenv["MOVIES_ACCESS_API_KEY"]

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer $API_KEY"
                    )
                    .build()

                val response = client.newCall(request).execute()
                var jsonObject: JSONObject? = null

                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    if (jsonData != null) {
                        jsonObject = JSONObject(jsonData)
                    } else {
                        Log.e("ResponseError", "Response body is null")
                    }
                } else {
                    Log.e("ResponseError", "Request failed with code: ${response.code}")
                }

                if (result != "") {
                    val arrayOfData = jsonObject?.getJSONArray(result)

                    return@withContext arrayOfData
                }

                jsonObject
            }
        }
    }
}
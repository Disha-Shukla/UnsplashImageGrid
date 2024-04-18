package com.das.unsplashimagegrid

import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.MutableList

class APICalls {

    fun fetchImagesFromUnsplash(accessKey: String, page: Int, callback: (List<String>?, String?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.unsplash.com/photos/random?client_id=$accessKey&count=10&page=$page") // Fetch 10 random images
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                response.body()?.close()

                // Parse JSON response and extract image URLs
                val imageUrls = parseJsonResponse(responseData)

                // Pass imageUrls back to the callback
                callback(imageUrls, null)
            }
        })
    }

    /*fun fetchImagesFromUnsplash(accessKey: String, page: Int, callback: (List<String>?, String?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.unsplash.com/photos/random?client_id=$accessKey&count=10&page=$page")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                response.body()?.close()

                // Parse JSON response and extract image URLs
                val imageUrls = parseJsonResponse(responseData)

                // Pass imageUrls back to the callback
                callback(imageUrls, null)
            }
        })
    }*/

    private fun parseJsonResponse(responseData: String?): List<String> {
        val imageUrls = ArrayList<String>() // Use ArrayList instead of mutableListOf
        try {
            val jsonArray = JSONArray(responseData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val imageUrl = jsonObject.getJSONObject("urls").getString("regular")
                imageUrls.add(imageUrl)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return imageUrls
    }

}
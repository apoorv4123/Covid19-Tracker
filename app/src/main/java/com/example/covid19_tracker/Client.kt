package com.example.covid19_tracker

import okhttp3.OkHttpClient
import okhttp3.Request

object Client {

    private val okhttpClient = OkHttpClient()// client is vo cheez isse hum networking kar payenge

    private val request = Request.Builder()// this part tells us, which information we want to fetch
        .url("https://api.covid19india.org/data.json")
        .build()

    val api = okhttpClient.newCall(request)// This is what we'll be using in code

}
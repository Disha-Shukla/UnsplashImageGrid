package com.das.unsplashimagegrid

import com.das.unsplashimagegrid.model.ImageModel
import retrofit2.http.GET
import retrofit2.Call

interface  RetrofitAPI {

    @GET("8RFY") fun getImage(): Call<ImageModel?>?
}
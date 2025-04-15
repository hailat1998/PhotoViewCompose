package com.hd.photoview.data.remote.dto

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Username (
    @Json(name = "username") val username: String
)
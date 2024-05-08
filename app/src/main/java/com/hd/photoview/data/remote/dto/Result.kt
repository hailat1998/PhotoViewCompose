package com.hd.photoview.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(@Json(name="results") val photoItems : List<PhotoItem>)
package com.hd1998.photofetch.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(@Json(name="results") val photoItems : List<PhotoItem>)
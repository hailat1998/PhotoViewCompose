package com.hd1998.photofetch.api
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoItem(@Json(name = "urls") val urls : Urls , val description : String? , val id : String  )

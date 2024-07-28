package com.hd.photoview.data.remote.dto
import Urls
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoItem(val urls: Urls,
                     @Json(name = "alt_description") val description : String?, val id : String )

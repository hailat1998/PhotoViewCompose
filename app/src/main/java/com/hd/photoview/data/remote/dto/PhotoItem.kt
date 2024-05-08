package com.hd.photoview.data.remote.dto
import com.hd.photoview.data.remote.dto.Src
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoItem(@Json(name = "urls") val urls : Src, val description : String?, val id : String  )

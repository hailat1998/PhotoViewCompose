package com.hd.photoview.data.remote.dto
import Urls
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoItem( val urls: Urls,
                     val description : String?, val id : String )

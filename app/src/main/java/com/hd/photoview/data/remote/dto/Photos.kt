package com.hd.photoview.data.remote.dto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Photos(val photoItems: List<PhotoItem>)

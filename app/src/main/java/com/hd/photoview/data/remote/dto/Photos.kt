package com.hd1998.photofetch.api
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Photos(val photoItems: List<PhotoItem>)

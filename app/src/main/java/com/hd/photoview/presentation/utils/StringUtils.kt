package com.hd.photoview.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.hd.photoview.domain.model.Photo
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Photo.toDecoded(): Photo {
    return Photo(id = this.id.encodeString(), description = this.description.encodeString()
        ,full = this.full.encodeString(), small = this.small.encodeString(), regular = this.regular.encodeString())
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun String.encodeString():String{
    return URLEncoder.encode(this, UTF_8)
}
package com.hd.photoview.data.remote.dto.dto.mapper

import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.domain.model.PhotoHD
import com.hd.photoview.domain.model.PhotoLow
import com.hd.photoview.domain.model.PhotoSD

fun PhotoItem.toPhotoHD(): PhotoHD{
    return PhotoHD(urls.full, description, id)
}

fun PhotoItem.toPhotoSD(): PhotoSD {
    return PhotoSD(urls.regular, description, id)
}

fun PhotoItem.toPhotoLow(): PhotoLow {
    return PhotoLow(urls.small, description, id)
}
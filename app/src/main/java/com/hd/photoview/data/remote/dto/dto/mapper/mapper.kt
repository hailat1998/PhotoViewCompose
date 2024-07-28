package com.hd.photoview.data.remote.dto.dto.mapper

import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.domain.model.Photo

fun PhotoItem.toPhoto(): Photo {
    return Photo(id = this.id,
           description = this.description ?: "unsplash photo",
        full= this.urls.full,
        regular = this.urls.regular,
        small = this.urls.small,
        )
}
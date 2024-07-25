package com.hd.photoview.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoSD(val src : String ,
                   val description : String? ,
                   val id : String)

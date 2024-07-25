package com.hd.photoview.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoLow(val src : String ,
                    val description : String? ,
                    val id : String)
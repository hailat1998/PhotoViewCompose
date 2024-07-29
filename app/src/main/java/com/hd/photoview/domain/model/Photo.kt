package com.hd.photoview.domain.model



import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Immutable
@Serializable
@Parcelize
data class Photo(val id: String, val small: String,
                 val full:String, val regular: String, val description: String): Parcelable

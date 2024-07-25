import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "full") val full: String,
    @Json(name = "regular") val regular: String,
    @Json(name = "small") val small: String
)
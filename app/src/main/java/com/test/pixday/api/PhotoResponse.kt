package com.test.pixday.api

import com.google.gson.annotations.SerializedName
import com.test.pixday.model.Photo

/**
 * A ShutterStock [PhotoResponse] Object
 * The returned JSON data is serialized into [PhotoResponse] class
 */
class PhotoResponse(
    @SerializedName("data") val data: List<Photo> = emptyList(),
)
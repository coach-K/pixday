package com.test.pixday.model

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("id") val id: String,
    @SerializedName("aspect") val aspect: Double,
    @SerializedName("description") val description: String,
    @SerializedName("assets") val assets: Assets,
)
package com.test.pixday.model

import com.google.gson.annotations.SerializedName

class Assets (
    @SerializedName("preview") val preview : Media,
)

class Media (
    @SerializedName("height") val height : Int,
    @SerializedName("url") val url : String,
    @SerializedName("width") val width : Int
)
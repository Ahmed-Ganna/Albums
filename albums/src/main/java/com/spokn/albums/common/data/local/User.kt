package com.spokn.albums.common.data.local


import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") val id: Int,
                @SerializedName("name") val userName: String,
                @SerializedName("username") val userIdentity: String,
                @SerializedName("email") val userEmail: String,
                @SerializedName("address") val address: Address )
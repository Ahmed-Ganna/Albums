package com.spokn.albums.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Photo(

	@field:SerializedName("albumId")
	val albumId: Int,

	@field:SerializedName("id")  @PrimaryKey
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("thumbnailUrl")
	val thumbnailUrl: String
)

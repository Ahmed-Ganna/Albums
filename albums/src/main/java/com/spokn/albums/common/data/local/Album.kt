package com.spokn.albums.common.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Album(

	@SerializedName("id") @PrimaryKey
	val id: Int,

	@SerializedName("title")
	val title: String,

	@SerializedName("userId")
	val userId: Int
):Parcelable

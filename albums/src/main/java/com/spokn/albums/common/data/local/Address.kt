package com.spokn.albums.common.data.local

import com.google.gson.annotations.SerializedName

data class Address(

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("suite")
	val suite: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null
)


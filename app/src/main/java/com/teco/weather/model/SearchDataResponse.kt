package com.teco.weather.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchDataResponse(

	@SerializedName("generationtime_ms")
	val generationtimeMs: Double? = null,

	@SerializedName("results")
	val results: List<ResultsItem>? = null
)

@Parcelize
data class ResultsItem(

	@SerializedName("elevation")
	val elevation: String? = null,

	@SerializedName("country")
	val country: String? = null,

	@SerializedName("admin1_id")
	val admin1Id: Int? = null,

	@SerializedName("timezone")
	val timezone: String? = null,

	@SerializedName("latitude")
	val latitude: Double? = null,

	@SerializedName("admin2_id")
	val admin2Id: Int? = null,

	@SerializedName("country_code")
	val countryCode: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("admin1")
	val admin1: String? = null,

	@SerializedName("admin2")
	val admin2: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("feature_code")
	val featureCode: String? = null,

	@SerializedName("country_id")
	val countryId: Int? = null,

	@SerializedName("longitude")
	val longitude: Double? = null,

	@SerializedName("admin3_id")
	val admin3Id: Int? = null,

	@SerializedName("admin4_id")
	val admin4Id: Int? = null,

	@SerializedName("admin3")
	val admin3: String? = null,

	@SerializedName("admin4")
	val admin4: String? = null,

	@SerializedName("population")
	val population: Int? = null,

	@SerializedName("postcodes")
	val postcodes: List<String>? = null
) : Parcelable

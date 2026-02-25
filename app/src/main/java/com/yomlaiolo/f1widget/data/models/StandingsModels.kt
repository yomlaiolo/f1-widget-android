package com.yomlaiolo.f1widget.data.models

import com.google.gson.annotations.SerializedName

data class StandingsResponse(
    @SerializedName("MRData") val mrData: StandingsMRData
)

data class StandingsMRData(
    @SerializedName("StandingsTable") val standingsTable: StandingsTable
)

data class StandingsTable(
    @SerializedName("StandingsLists") val standingsLists: List<StandingsList>
)

data class StandingsList(
    @SerializedName("season") val season: String,
    @SerializedName("round") val round: String,
    @SerializedName("DriverStandings") val driverStandings: List<DriverStanding>? = null,
    @SerializedName("ConstructorStandings") val constructorStandings: List<ConstructorStanding>? = null
)

data class DriverStanding(
    @SerializedName("position") val position: String,
    @SerializedName("positionText") val positionText: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Driver") val driver: Driver,
    @SerializedName("Constructors") val constructors: List<Constructor>
)

data class Driver(
    @SerializedName("driverId") val driverId: String,
    @SerializedName("permanentNumber") val permanentNumber: String? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("givenName") val givenName: String,
    @SerializedName("familyName") val familyName: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("nationality") val nationality: String
)

data class Constructor(
    @SerializedName("constructorId") val constructorId: String,
    @SerializedName("name") val name: String,
    @SerializedName("nationality") val nationality: String
)

data class ConstructorStanding(
    @SerializedName("position") val position: String,
    @SerializedName("positionText") val positionText: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Constructor") val constructor: Constructor
)
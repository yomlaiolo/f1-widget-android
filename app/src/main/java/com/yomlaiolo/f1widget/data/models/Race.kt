package com.yomlaiolo.f1widget.data.models

import com.google.gson.annotations.SerializedName

data class RaceResponse(
    @SerializedName("MRData")
    val mrData: MRData
)

data class MRData(
    @SerializedName("RaceTable")
    val raceTable: RaceTable
)

data class RaceTable(
    @SerializedName("Races")
    val races: List<Race>
)

data class Race(
    @SerializedName("season")
    val season: String,
    
    @SerializedName("round")
    val round: String,
    
    @SerializedName("raceName")
    val raceName: String,
    
    @SerializedName("Circuit")
    val circuit: Circuit,
    
    @SerializedName("date")
    val date: String,
    
    @SerializedName("time")
    val time: String?,
    
    // Sessions
    @SerializedName("FirstPractice")
    val firstPractice: Session?,
    
    @SerializedName("SecondPractice")
    val secondPractice: Session?,
    
    @SerializedName("ThirdPractice")
    val thirdPractice: Session?,
    
    @SerializedName("Qualifying")
    val qualifying: Session?,
    
    @SerializedName("Sprint")
    val sprint: Session?
)

data class Circuit(
    @SerializedName("circuitId")
    val circuitId: String,
    
    @SerializedName("circuitName")
    val circuitName: String,
    
    @SerializedName("Location")
    val location: Location
)

data class Location(
    @SerializedName("lat")
    val lat: String,
    
    @SerializedName("long")
    val long: String,
    
    @SerializedName("locality")
    val locality: String,
    
    @SerializedName("country")
    val country: String
)

data class Session(
    @SerializedName("date")
    val date: String,
    
    @SerializedName("time")
    val time: String? = null
)
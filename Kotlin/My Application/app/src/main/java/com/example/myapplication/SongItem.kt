package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongItem(var songName:String, var artistName:String, var duration:Int , var pathResource: Long):Parcelable
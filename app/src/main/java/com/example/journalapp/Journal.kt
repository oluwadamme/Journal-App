package com.example.journalapp

import com.google.firebase.Timestamp


data class Journal(
    val title:String,
    val desc:String,
    val image:String,
    val timeAdded: Timestamp,
    val userId:String,
    val username:String
)

package com.example.journalapp

import java.sql.Timestamp

data class Journal(
    val title:String,
    val desc:String,
    val image:Int,
    val timeAdded:Timestamp,
    val userId:String,
    val username:String
)

package com.Table.Server

data class SearchPreferences(val min_age: Int,val max_age: Int, val city:String, val card_games:Boolean=false,
                             val board_games:Boolean=false, val ttrpg:Boolean=false, val wargames:Boolean=false, val username: String)
package com.Table.Server

data class SearchPreferences(val age:Int?=null, val city:String?=null, val card_games:Boolean?=false ,
                             val board_games:Boolean?=false, val ttrpg:Boolean?=false, val wargames:Boolean?=false)
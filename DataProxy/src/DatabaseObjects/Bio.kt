package com.Table.Server.DatabaseObjects

import com.Table.Server.DatabaseObjects.Users.autoIncrement
import com.Table.Server.DatabaseObjects.Users.nullable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class Bio(val id: Int?=null, val user_name:String?=null, val age:Int?=null, val city:String?=null, val card_games:Boolean?=false , val board_games:Boolean?=false, val ttrpg:Boolean?=false, val wargames:Boolean?=false)

object Bios: Table() {
    val bio_id: Column<Int> = Bios.integer("bio_id").autoIncrement()
    val user_name: Column<String?> = Bios.varchar("user_name",255).nullable()
    val age: Column<Int?> = Bios.integer("age").nullable()
    val city: Column<String?> = Bios.varchar("city", 255).nullable()
    val card_games: Column<Boolean?> = Bios.bool("card_games").nullable()
    val board_games: Column<Boolean?> = Bios.bool("board_games").nullable()
    val ttrpg: Column<Boolean?> = Bios.bool("ttrpg").nullable()
    val wargames: Column<Boolean?> = Bios.bool("wargames").nullable()
    override val primaryKey = PrimaryKey(bio_id, name = "Pref_ID")

    fun toBio(row: ResultRow): Bio = Bio(
        id = row[Bios.bio_id],
        user_name = row[Bios.user_name],
        age = row[Bios.age],
        city = row[Bios.city],
        card_games = row[Bios.card_games],
        board_games = row[Bios.board_games],
        ttrpg = row[Bios.ttrpg],
        wargames = row[Bios.wargames],

        )

}